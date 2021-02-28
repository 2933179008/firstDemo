package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.InventoryTaskDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.InventoryTaskInterfaceDAO;
import com.tbl.modules.handheldInterface.service.InventoryTaskInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.InventoryTask;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点任务接口Service实现
 */
@Service(value = "inventoryTaskInterfaceService")
public class InventoryTaskInterfaceServiceImpl extends ServiceImpl<InventoryTaskInterfaceDAO, InventoryTask> implements InventoryTaskInterfaceService {

    //接口日志Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //盘点任务接口DAO
    @Autowired
    private InventoryTaskInterfaceDAO inventoryTaskInterfaceDAO;

    @Autowired
    private InventoryTaskDetailInterfaceDAO inventoryTaskDetailInterfaceDAO;

    /**
     * 盘点任务获取
     *
     * @param inventoryUserId
     * @return
     */
    @Override
    public Map<String, Object> getInventoryTask(Long inventoryUserId) {

        boolean result = true;
        String msg = "获取数据成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<InventoryTask> inventoryList = null;
        if (inventoryUserId != null) {
            inventoryList = inventoryTaskInterfaceDAO.selectList(
                    new EntityWrapper<InventoryTask>()
                            .eq("inventory_user_id", inventoryUserId)
                            .eq("state", DyylConstant.INVENTORY_TASK1)
                            .or()
                            .eq("state", DyylConstant.INVENTORY_TASK2)
                            .or()
                            .eq("state", DyylConstant.INVENTORY_TASK5)
            );
            if (inventoryList.isEmpty()) {
                result = false;
                msg = "无任务";
            }
        } else {
            result = false;
            msg = "获取当前用户ID失败";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用盘点任务获取接口";
        String parameter = "InventoryUserId:" + inventoryUserId.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);


        map.put("data", inventoryList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 盘点任务提交
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> doInventoryTask(Long id) {

        boolean result = true;
        String msg = "提交成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "Id:" + id;
        if (id == null){
            interfaceLogService.interfaceLogInsert("调用盘点单提交接口", parameter, "失败原因：盘点状态为盘点中或复盘中才可提交", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：盘点状态为盘点中或复盘中才可提交");

            return map;
        }

        InventoryTask inventoryTask = inventoryTaskInterfaceDAO.selectById(id);
        if (inventoryTask == null){
            interfaceLogService.interfaceLogInsert("调用盘点单提交接口", parameter, "失败原因：id未获取对应盘点任务单", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：id未获取对应盘点任务单");

            return map;
        }

        if (inventoryTask.getState().equals(DyylConstant.INVENTORY_TASK2) || inventoryTask.getState().equals(DyylConstant.INVENTORY_TASK5)) {
            inventoryTask.setState(DyylConstant.INVENTORY_TASK3);
            inventoryTaskInterfaceDAO.updateById(inventoryTask);
            List<InventoryTaskDetail> inventoryTaskDetailList = inventoryTaskDetailInterfaceDAO.selectList(
                    new EntityWrapper<InventoryTaskDetail>().eq("inventory_task_id",inventoryTask.getId())
            );
            for (InventoryTaskDetail inventoryTaskDetail : inventoryTaskDetailList){
                inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL1);
                inventoryTaskDetailInterfaceDAO.updateById(inventoryTaskDetail);
            }
        }

        map.put("result", result);
        map.put("msg", msg);

        return map;
    }
}
