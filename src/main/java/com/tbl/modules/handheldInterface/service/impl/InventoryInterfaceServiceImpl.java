package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.InventoryDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.InventoryInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.InventoryTaskDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.InventoryTaskInterfaceDAO;
import com.tbl.modules.handheldInterface.service.InventoryInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点单Service实现
 */
@Service("inventoryInterfaceService")
public class InventoryInterfaceServiceImpl extends ServiceImpl<InventoryInterfaceDAO, Inventory> implements InventoryInterfaceService {

    //接口日志Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //盘点接口DAO
    @Autowired
    private InventoryInterfaceDAO inventoryInterfaceDAO;

    //盘点任务详情接口DAO
    @Autowired
    private InventoryTaskDetailInterfaceDAO inventoryTaskDetailInterfaceDAO;

    //盘点任务接口DAO
    @Autowired
    private InventoryDetailInterfaceDAO inventoryDetailInterfaceDAO;

    @Autowired
    private InventoryTaskInterfaceDAO inventoryTaskInterfaceDAO;


    /**
     * @Description: 生成盘点编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    @Override
    public String generateInventoryCode() {
        //盘点编号
        String inventoryCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.INVENTORY_CODE_FORMAT);
        //获取最大收货单编号
        String maxInventoryCode = inventoryInterfaceDAO.getMaxInventoryCode();
        if (StringUtils.isEmptyString(maxInventoryCode)) {
            inventoryCode = "PD00000001";
        } else {
            Integer maxInventoryCode_count = Integer.parseInt(maxInventoryCode.replace("PD", ""));
            inventoryCode = df.format(maxInventoryCode_count + 1);
        }
        return inventoryCode;
    }

    /**
     * 盘点记录提交
     *
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> doInventory(Map<String, Object> paramMap) {

        boolean result = true;
        String msg = "提交成功";
        Map<String, Object> map = new HashMap<>();

        //获取参数
        Long userId = Long.parseLong((String) paramMap.get("userId"));
        Long taskId = Long.parseLong((String) paramMap.get("taskId"));
        String positionCode = (String) paramMap.get("positionCode");

        Gson gson = new Gson();
        List<MaterielBindRfidDetail> materielBindRfidDetailList = gson.fromJson(paramMap.get("data").toString(),
                new TypeToken<List<MaterielBindRfidDetail>>() {
                }.getType());


        String parameter = "UserId:" + userId + "/TaskId" + taskId;
        if (userId == null || taskId == null || materielBindRfidDetailList.isEmpty() || Strings.isNullOrEmpty(positionCode)) {
            interfaceLogService.interfaceLogInsert("调用盘点记录提交接口", parameter, "失败原因：参数未获取", DateUtils.getTime());

            map.put("result", false);
            map.put("msg", "失败原因：参数未获取");

            return map;
        }
        InventoryTask inventoryTask = inventoryTaskInterfaceDAO.selectById(taskId);
        if (inventoryTask == null){
            interfaceLogService.interfaceLogInsert("调用盘点记录提交接口", parameter, "失败原因：盘点任务ID未找到对应的任务单", DateUtils.getTime());

            map.put("result", false);
            map.put("msg", "失败原因：盘点任务ID未找到对应的任务单");

            return map;
        }
        //更改盘点任务状态
        inventoryTask.setState(DyylConstant.INVENTORY_TASK2);
        inventoryTaskInterfaceDAO.updateById(inventoryTask);
        List<InventoryTaskDetail> inventoryTaskDetailList = inventoryTaskDetailInterfaceDAO.selectList(
                new EntityWrapper<InventoryTaskDetail>()
                .eq("inventory_task_id",taskId)
        );

        //清空盘点任务详情中盘点数量和重量，删除盘点库存数量为零的任务
        for (InventoryTaskDetail inventoryTaskDetail : inventoryTaskDetailList){
            //若盘点库存数量为0或为空，则删除此条盘点任务详情
            if (Strings.isNullOrEmpty(inventoryTaskDetail.getStockAmount()) || inventoryTaskDetail.getStockAmount().equals("0")){
                inventoryTaskDetailInterfaceDAO.deleteById(inventoryTaskDetail.getId());
                continue;
            }
            inventoryTaskDetail.setInventoryAmount("0");
            inventoryTaskDetail.setInventoryWeight("0");
            inventoryTaskDetailInterfaceDAO.updateById(inventoryTaskDetail);
        }

        //创建新的盘点记录数据
        Inventory inventory = new Inventory();
        inventory.setPositionCode(positionCode);
        inventory.setInventoryTaskId(taskId);
        inventory.setInventoryCode(generateInventoryCode());
        inventory.setInventoryUserId(userId);
        inventory.setInventoryTime(DateUtils.getTime());
        inventory.setCreateBy(userId);
        inventory.setCreateTime(DateUtils.getTime());
        inventory.setState(DyylConstant.INVENTORY_1);
        inventory.setRemark("手持机生成数据");
        inventoryInterfaceDAO.insert(inventory);

        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            //判断条件有无对应的盘点任务详情
            InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailInterfaceDAO.getInventoryTaskDetail(taskId, materielBindRfidDetail.getMaterielCode(), materielBindRfidDetail.getBatchRule(), materielBindRfidDetail.getPositionCode());

            InventoryDetail inventoryDetail = new InventoryDetail();
            InventoryTaskDetail itd = new InventoryTaskDetail();
            //若无对应的盘点任务详情，则新建一条；否则盘点重量、盘点数量叠加
            if (inventoryTaskDetail == null) {
                itd.setInventoryTaskId(taskId);
                itd.setPositionCode(positionCode);
                itd.setMaterialCode(materielBindRfidDetail.getMaterielCode());
                itd.setMaterialName(materielBindRfidDetail.getMaterielName());
                itd.setBatchNo(materielBindRfidDetail.getBatchRule());
                itd.setInventoryAmount(materielBindRfidDetail.getAmount());
                itd.setInventoryWeight(materielBindRfidDetail.getWeight());
                itd.setCompleteTime(DateUtils.getTime());
                itd.setCreateBy(userId);
                itd.setRfid(materielBindRfidDetail.getRfid());
                inventoryTaskDetailInterfaceDAO.insert(itd);
                inventoryDetail.setInventoryTaskDetailId(itd.getId());
            } else {
                if (Strings.isNullOrEmpty(inventoryTaskDetail.getInventoryAmount()) || inventoryTaskDetail.getInventoryAmount().equals("0")) {
                    inventoryTaskDetail.setInventoryAmount(materielBindRfidDetail.getAmount());
                } else {
                    Double inventoryAmount = Double.valueOf(inventoryTaskDetail.getInventoryAmount()) + Double.valueOf(materielBindRfidDetail.getAmount());
                    inventoryTaskDetail.setInventoryAmount(inventoryAmount.toString());
                }
                if (Strings.isNullOrEmpty(inventoryTaskDetail.getInventoryWeight())
                        || inventoryTaskDetail.getInventoryAmount().equals("0") || inventoryTaskDetail.getInventoryWeight().equals("0.0")) {
                    inventoryTaskDetail.setInventoryWeight(materielBindRfidDetail.getWeight());
                } else {
                    Double inventoryWeight = Double.valueOf(inventoryTaskDetail.getInventoryWeight()) + Double.valueOf(materielBindRfidDetail.getWeight());
                    inventoryTaskDetail.setInventoryWeight(inventoryWeight.toString());
                }
                inventoryTaskDetailInterfaceDAO.updateById(inventoryTaskDetail);
                inventoryDetail.setInventoryTaskDetailId(inventoryTaskDetail.getId());
            }
            inventoryDetail.setInventoryId(inventory.getId());
            inventoryDetail.setPositionCode(positionCode);
            inventoryDetail.setMaterialCode(materielBindRfidDetail.getMaterielCode());
            inventoryDetail.setMaterialName(materielBindRfidDetail.getMaterielName());
            inventoryDetail.setBatchNo(materielBindRfidDetail.getBatchRule());
            inventoryDetail.setInventoryAmount(materielBindRfidDetail.getAmount());
            inventoryDetail.setInventoryWeight(materielBindRfidDetail.getWeight());
            inventoryDetail.setRfid(materielBindRfidDetail.getRfid());
            inventoryDetail.setState(DyylConstant.STATUS_OVER);
            inventoryDetail.setCompleteTime(DateUtils.getTime());
            inventoryDetail.setCreateBy(userId);
            inventoryDetail.setCreateTime(DateUtils.getTime());

            inventoryDetailInterfaceDAO.insert(inventoryDetail);
        }
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 更改盘点任务状态为待审核
     * @param taskId
     * @return
     */
    @Override
    public Map<String, Object> doInventoryTaskStatus(Long taskId) {
        Map<String,Object> map = new HashMap<>();

        String parameter = "/TaskId" + taskId;
        if (taskId == null){
            interfaceLogService.interfaceLogInsert("调用更改盘点任务状态为待审核接口", parameter, "失败原因：未获取参数", DateUtils.getTime());

            map.put("result", false);
            map.put("msg", "失败原因：为获取参数");

            return map;
        }

        InventoryTask inventoryTask = inventoryTaskInterfaceDAO.selectById(taskId);

        if (inventoryTask == null){
            interfaceLogService.interfaceLogInsert("调用更改盘点任务状态为待审核接口", parameter, "失败原因：任务ID未获取对应的盘点任务单据", DateUtils.getTime());

            map.put("result", false);
            map.put("msg", "失败原因：任务ID未获取对应的盘点任务单据");

            return map;
        }

        inventoryTask.setState(DyylConstant.INVENTORY_TASK3);
        inventoryTaskInterfaceDAO.updateById(inventoryTask);

        map.put("result",true);
        map.put("msg","更改成功");

        return map;
    }

}
