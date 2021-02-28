package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.util.DeriveExcel;
import com.tbl.modules.stock.dao.InventoryDAO;
import com.tbl.modules.stock.dao.InventoryTaskDAO;
import com.tbl.modules.stock.entity.*;
import com.tbl.modules.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pz
 * @Description: 盘点service实现
 * @Param:
 * @return:
 * @date 2019-01-08
 */
@Service("inventoryService")
public class InventoryServiceImpl extends ServiceImpl<InventoryDAO, Inventory> implements InventoryService {

    // 盘点DAO
    @Autowired
    private InventoryDAO inventoryDAO;

    // 盘点Service
    @Autowired
    private InventoryService inventoryService;

    // 盘点详情Service
    @Autowired
    private InventoryDetailService inventoryDetailService;

    // 盘点任务DAO
    @Autowired
    private InventoryTaskDAO inventoryTaskDao;

    // 盘点任务详情DAO
    @Autowired
    private InventoryTaskDetailService inventoryTaskDetailService;

    // 物料绑定rfid Service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    // 库存Service
    @Autowired
    private StockService stockService;

    //用户DAO
    @Autowired
    private UserDAO userDAO;

    /**
     * @Description: 盘点分页查询
     * @Param:
     * @return:
     * @author pz
     * @Date: 2018/12/26
     */
    public PageUtils queryPageI(Map<String, Object> params) {

        //盘点编号
        String inventoryCode = (String) params.get("inventoryCode");
        //状态
        String state = (String) params.get("state");

        Page<Inventory> page = this.selectPage(
                new Query<Inventory>(params).getPage(),
                new EntityWrapper<Inventory>()
                        .like(StringUtils.isNotEmpty(inventoryCode), "inventory_code", inventoryCode)
                        .like(StringUtils.isNotEmpty(state), "state", state)
        );

        for (Inventory inventory : page.getRecords()) {
            inventory.setCreateName(userDAO.selectById(inventory.getCreateBy()).getUsername());
            inventory.setInventoryName(userDAO.selectById(inventory.getInventoryUserId()).getUsername());
            if (inventoryTaskDao.selectById(inventory.getInventoryTaskId()) != null) {
                inventory.setInventoryTaskCode(inventoryTaskDao.selectById(inventory.getInventoryTaskId()).getInventoryTaskCode());
            }
        }
        return new PageUtils(page);
    }

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
        String maxInventoryCode = inventoryDAO.getMaxInventoryCode();
        if (StringUtils.isEmptyString(maxInventoryCode)) {
            inventoryCode = "PD00000001";
        } else {
            Integer maxInventoryCode_count = Integer.parseInt(maxInventoryCode.replace("PD", ""));
            inventoryCode = df.format(maxInventoryCode_count + 1);
        }
        return inventoryCode;
    }

    /**
     * @Description: 新增/修改
     * @Param:
     * @return:
     * @author pz
     * @Date: 2019/1/15
     */
    @Override
    public Map<String, Object> saveInventory(Inventory inventory, Long userId) {
        Map<String, Object> map = new HashMap<>();

        boolean result = false;
        String msg = "";
        InventoryTask inventoryTask = inventoryTaskDao.selectById(inventory.getInventoryTaskId());
        String taskState = inventoryTask.getState();
        if (taskState.equals(DyylConstant.INVENTORY_TASK0)) {
            msg = "盘点任务尚未提交。保存失败！";
        } else if (taskState.equals(DyylConstant.INVENTORY_TASK4)) {
            msg = "盘点任务已审核。保存失败！";
        } else {
            if (inventory.getId() == null) {   //新增保存
                //当前时间
                inventory.setCreateTime(DateUtils.getTime());
                inventory.setCreateBy(userId);
                inventory.setState(DyylConstant.INVENTORY_0);
                result = inventoryService.insert(inventory);
            } else { //修改保存
                //获取修改前的盘点记录
                Inventory preInventory = inventoryDAO.selectById(inventory.getId());
                if (inventoryService.updateById(inventory)) { //更新成功
                    result = true;
                    //如果盘点详情中的盘点任务编号改变，则需要删除修改前的盘点详情
                    if (preInventory.getInventoryTaskId() != inventory.getInventoryTaskId()) {
                        EntityWrapper<InventoryDetail> entity = new EntityWrapper<>();
                        entity.eq("inventory_id", inventory.getId());
                        inventoryDetailService.delete(entity);
                    }
                }
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        //返回插入后的收货单的id
        map.put("id", inventory.getId());
        map.put("inventoryCode", inventory.getInventoryCode());
        return map;
    }

    /**
     * @Description: 修改库位编号
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updatePositionCode(Long id, String positionCode) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setPositionCode(positionCode);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改rfid
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateRfid(Long id, String rfid) {
        Map<String, Object> map = Maps.newHashMap();
        boolean b = false;
        if (StringUtils.isNotEmpty(rfid)) {
            //通过id获取盘点详情
            InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
            //获取库位
            String positionCode = inventoryDetail.getPositionCode();
            //通过库位编号获取库位id
            Long positionBy = inventoryTaskDao.selectBYCode(positionCode);
            //依据RFID查找物料绑定详情
            EntityWrapper<MaterielBindRfid> wraMaterielBindRfid = new EntityWrapper<>();
            wraMaterielBindRfid.eq("rfid", rfid);
            wraMaterielBindRfid.eq("deleted_flag", DyylConstant.NOTDELETED);
            wraMaterielBindRfid.eq("status", DyylConstant.STATE_UNTREATED);
            int cou = materielBindRfidService.selectCount(wraMaterielBindRfid);
            //判断该RFID是否所在该库位
            wraMaterielBindRfid.eq("position_by", positionBy);
            int positionCou = materielBindRfidService.selectCount(wraMaterielBindRfid);
            if (cou == 0) {
                map.put("message", "请输入存在且已入库的rfid!");
                map.put("result", b);
                return map;
            } else if (positionCou == 0) {
                map.put("message", "rfid不存在于该库位，请重新选择!");
                map.put("result", b);
                return map;
            } else {
                InventoryDetail detail = inventoryDetailService.selectById(id);
                detail.setRfid(rfid);
                b = inventoryDetailService.updateById(detail);
            }
        } else {
            InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
            inventoryDetail.setRfid(rfid);
            b = inventoryDetailService.updateAllColumnById(inventoryDetail);
        }

        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改物料编号
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateMaterialCode(Long id, String materialCode) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setMaterialCode(materialCode);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改物料名称
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateMaterialName(Long id, String materialName) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setMaterialName(materialName);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改批次号
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateBatchNo(Long id, String batchNo) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setBatchNo(batchNo);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改盘点结果数量
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateInventoryAmount(Long id, String inventoryAmount) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setInventoryAmount(inventoryAmount);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 修改盘点结果重量
     * @Param:
     * @return:
     * @Author: pz
     * @date 2018-01-21
     */
    public Map<String, Object> updateInventoryWeight(Long id, String inventoryWeight) {
        Map<String, Object> map = Maps.newHashMap();
        InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
        inventoryDetail.setInventoryWeight(inventoryWeight);
        boolean b = inventoryDetailService.updateById(inventoryDetail);
        map.put("message", b ? "修改成功!" : "修改失败!");
        map.put("result", b);
        return map;
    }

    /**
     * @Description: 获取导出列
     * @Param:
     * @return:List<Inventory>
     * @author pz
     * @date 2019-01-10
     */
    @Override
    public List<Inventory> getAllListI(String ids) {
        List<Inventory> list = inventoryDAO.getAllListI(StringUtils.stringToList(ids));
        list.forEach(a -> {

        });

        return list;
    }

    /**
     * @Description: 导出excel
     * @Param:
     * @return:list
     * @author pz
     * @date 2019-01-10
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Inventory> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "盘点管理表" + "(" + date + ")";
            if (path != null && !"".equals(path)) {
                sheetName = sheetName + ".xls";
            } else {
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-disposition", "attachment;filename="
                        + new String(sheetName.getBytes("gbk"), "ISO8859-1") + ".xls");
            }

            for (Inventory in : list) {
                if (in.getState().equals(DyylConstant.INVENTORY_0)) {
                    in.setState("未提交");
                } else {
                    in.setState("已提交");
                }

                in.setUserName(userDAO.selectById(in.getInventoryUserId()).getUsername());
                in.setCreateName(userDAO.selectById(in.getCreateBy()).getUsername());
                in.setInventoryTaskCode(inventoryTaskDao.selectById(in.getInventoryTaskId()).getInventoryTaskCode());
            }

            Map<String, String> mapFields = new LinkedHashMap<String, String>();
            mapFields.put("inventoryCode", "盘点编码");
            mapFields.put("inventoryTaskCode", "盘点任务");
            mapFields.put("userName", "盘点人");
            mapFields.put("inventoryTime", "盘点时间");
            mapFields.put("State", "状态");
            mapFields.put("createName", "创建人");
            mapFields.put("remark", "备注");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 获取库存下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @Override
    public List<Map<String, Object>> getSelectPositionList(Long inventoryTaskId, String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return inventoryDAO.getSelectPositionList(page, inventoryTaskId, queryString);
    }

    /**
     * @Description: 获取库存下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @Override
    public Integer getSelectPositionTotal(Long inventoryTaskId, String queryString) {
        return inventoryDAO.getSelectPositionTotal(inventoryTaskId, queryString);
    }

    /**
     * @Description: 获取物料下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @Override
    public List<Map<String, Object>> getSelectMaterielList(Long inventoryTaskId, String positionCode, String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return inventoryDAO.getSelectMaterielList(page, inventoryTaskId, positionCode, queryString);
    }

    /**
     * @Description: 获取物料下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @Override
    public Integer getSelectMaterielTotal(Long inventoryTaskId, String positionCode, String queryString) {
        return inventoryDAO.getSelectMaterielTotal(inventoryTaskId, positionCode, queryString);
    }

    /**
     * @Description: 提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    public Map<String, Object> submitInventory(Long id) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";

        //获取盘点记录实体
        Inventory inventory = inventoryDAO.selectById(id);
        //获取盘点任务id
        Long inventoryTaskId = inventory.getInventoryTaskId();
        //获取库位编码
        String positionCode = inventory.getPositionCode();
        //获取全部盘点信息，判断是否有过同盘点任务同库位的已提交。如果存在，盘点记录提交，则需覆盖盘点任务库位存在的信息。
        EntityWrapper<Inventory> wraInventory = new EntityWrapper<>();
        wraInventory.eq("inventory_task_id", inventoryTaskId);
        wraInventory.eq("position_code", positionCode);
        wraInventory.eq("state", DyylConstant.INVENTORY_1);
        Inventory inventoryEntity = inventoryService.selectOne(wraInventory);

        //实体为空，则直接将盘点详情记录存在盘点任务详情中。
        if (inventoryEntity == null) {
            result = this.saveTaskDetail(id);
        } else {     //实体不为空，则需要对上次盘点数据进行覆盖
            //获取盘点记录库位对应的盘点任务详情
            EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
            wraInventoryTaskDetail.eq("position_code", positionCode);
            wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
            List<InventoryTaskDetail> lstInventoryTaskDetail = inventoryTaskDetailService.selectList(wraInventoryTaskDetail);
            for (InventoryTaskDetail inventoryTaskDetail : lstInventoryTaskDetail) {
                //获取库存数量
                String stockAmount = inventoryTaskDetail.getStockAmount();
                //库存数量为空，则表明原盘点任务详情中并没有此物料。应当在盘点任务详情中删除该物料
                if (StringUtils.isEmpty(stockAmount)) {
                    inventoryTaskDetailService.deleteById(inventoryTaskDetail);
                } else {      //库存数量不为空，则表明物料在盘点任务详情中。只需将盘点数量置空即可
                    inventoryTaskDetail.setInventoryAmount("");
                    inventoryTaskDetailService.updateAllColumnById(inventoryTaskDetail);
                }
            }
            //此时，将盘点记录存入到盘点任务详情中即可
            result = this.saveTaskDetail(id);
        }
        if (result) {
            //修改盘点记录状态，变为已提交
            inventory.setState(DyylConstant.INVENTORY_1);
            inventory.setInventoryTime(DateUtils.getTime());
            inventoryDAO.updateById(inventory);
            //将盘点任务详情中在盘点记录详情中未盘到或者新增的物料状态
            EntityWrapper<InventoryTaskDetail> wrapperInventoryTaskDetail = new EntityWrapper<>();
            wrapperInventoryTaskDetail.eq("position_code", positionCode);
            wrapperInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
            wrapperInventoryTaskDetail.eq("state", DyylConstant.INVENTORY_TASKDETAIL0);
            List<InventoryTaskDetail> lstInventoryTaskDetail = inventoryTaskDetailService.selectList(wrapperInventoryTaskDetail);
            for (InventoryTaskDetail inventoryTaskDetail : lstInventoryTaskDetail) {
                inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL1);
                inventoryTaskDetailService.updateById(inventoryTaskDetail);
            }
            //修改盘点任务信息状态，判断盘点任务详情中是否全部已盘点。如果全部已盘点，则显示待审核，反之则为盘点中。
            EntityWrapper<InventoryTaskDetail> wrapperInventoryTaskDet = new EntityWrapper<>();
            wrapperInventoryTaskDet.eq("inventory_task_id", inventoryTaskId);
            wrapperInventoryTaskDet.eq("state", DyylConstant.INVENTORY_TASKDETAIL0);
            int count = inventoryTaskDetailService.selectCount(wrapperInventoryTaskDet);
            if (count == 0) {
                InventoryTask inventoryTask = inventoryTaskDao.selectById(inventoryTaskId);
                inventoryTask.setState(DyylConstant.INVENTORY_TASK3);
                inventoryTaskDao.updateById(inventoryTask);
            } else {
                InventoryTask inventoryTask = inventoryTaskDao.selectById(inventoryTaskId);
                inventoryTask.setState(DyylConstant.INVENTORY_TASK2);
                inventoryTaskDao.updateById(inventoryTask);
            }
            msg = "提交成功";
        } else {
            msg = "提交失败:库存中不存在提交的物料";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 将盘点记录存放到盘点任务中
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    public boolean saveTaskDetail(Long id) {
        boolean result = false;
        //获取盘点记录详情list
        EntityWrapper<InventoryDetail> wraDetail = new EntityWrapper<>();
        wraDetail.eq("inventory_id", id);
        List<InventoryDetail> detList = inventoryDetailService.selectList(wraDetail);
        //判断盘点记录是否存在于库存中
        for (InventoryDetail inventoryDetail : detList) {
            //判断盘点记录详情是否存在于库存
            EntityWrapper<Stock> wraStock = new EntityWrapper<>();
            wraStock.eq("material_code", inventoryDetail.getMaterialCode());
            wraStock.eq("position_code", inventoryDetail.getPositionCode());
            wraStock.eq("batch_no", inventoryDetail.getBatchNo());
            if (StringUtils.isEmpty(inventoryDetail.getRfid())) {
                wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_NOCARGO);
            } else {
                wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_CARGO);
            }
            Stock stock = stockService.selectOne(wraStock);
            if (stock == null) {
                return false;
            }
        }
        //依据盘点记录填充进盘点任务详情
        for (InventoryDetail inventoryDetail : detList) {
            //获取盘点记录库位对应的盘点任务详情
            EntityWrapper<InventoryTaskDetail> wraTaskDetail = new EntityWrapper<>();
            wraTaskDetail.eq("id", inventoryDetail.getInventoryTaskDetailId());
            InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailService.selectOne(wraTaskDetail);
            if (inventoryTaskDetail != null) {
                //判断有无RFID类型
                boolean rfidFlag = (StringUtils.isEmpty(inventoryTaskDetail.getRfid()) && StringUtils.isEmpty(inventoryDetail.getRfid()))
                        || (StringUtils.isNotEmpty(inventoryTaskDetail.getRfid()) && StringUtils.isNotEmpty(inventoryDetail.getRfid()));
                //通过盘点详情找到对应一条盘点任务详情，如果物料编码、库位编码、批次号都相同。则只修改盘点任务中的盘点数量
                if (inventoryTaskDetail.getMaterialCode().equals(inventoryDetail.getMaterialCode()) && inventoryTaskDetail.getPositionCode().equals(inventoryDetail.getPositionCode())
                        && inventoryTaskDetail.getBatchNo().equals(inventoryDetail.getBatchNo()) && rfidFlag) {
                    //获取盘点数量
                    String inventoryAmount = inventoryDetail.getInventoryAmount();
                    //获取盘点重量
                    String inventoryWeight = inventoryDetail.getInventoryWeight();
                    //获取RFID
                    String rfid = inventoryDetail.getRfid();
                    //判断盘点数量是否为空 不为空则累加
                    if (StringUtils.isEmpty(inventoryTaskDetail.getInventoryAmount())) {
                        inventoryTaskDetail.setInventoryAmount(inventoryAmount);
                        inventoryTaskDetail.setInventoryWeight(inventoryWeight);
                        inventoryTaskDetail.setRfid(rfid);
                    } else {
                        Double inventoryAmountDouble = Double.parseDouble(inventoryAmount) + Double.parseDouble(inventoryTaskDetail.getInventoryAmount());
                        inventoryTaskDetail.setInventoryAmount(String.valueOf(inventoryAmountDouble));
                        Double inventoryWeightDouble = Double.parseDouble(inventoryWeight) + Double.parseDouble(inventoryTaskDetail.getInventoryWeight());
                        inventoryTaskDetail.setInventoryWeight(String.valueOf(inventoryWeightDouble));
                        inventoryTaskDetail.setRfid(rfid + "," + inventoryTaskDetail.getRfid());
                    }
                    inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL1);
                    result = inventoryTaskDetailService.updateById(inventoryTaskDetail);
                } else {   //不一致则需要在新建盘点任务详情记录
                    result = this.saveInventoryTaskDetail(inventoryDetail);
                }
            } else {
                result = this.saveInventoryTaskDetail(inventoryDetail);
            }
            if (result) {
                //修改盘点详情状态
                inventoryDetail.setState(DyylConstant.STATUS_OVER);
                inventoryDetail.setCompleteTime(DateUtils.getTime());
                inventoryDetailService.updateById(inventoryDetail);
            }
        }
        return result;
    }

    /**
     * @Description: 保存盘点任务详情
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    public boolean saveInventoryTaskDetail(InventoryDetail inventoryDetail) {
        boolean flag = false;
        //库位编码
        String positionCode = inventoryDetail.getPositionCode();
        //物料编码
        String materialCode = inventoryDetail.getMaterialCode();
        //批次号
        String batchNo = inventoryDetail.getBatchNo();
        //rfid
        String rfid = inventoryDetail.getRfid();
        //获取盘点任务详情
        EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
        wraInventoryTaskDetail.eq("position_code", positionCode);
        wraInventoryTaskDetail.eq("material_code", materialCode);
        wraInventoryTaskDetail.eq("batch_no", batchNo);
        if (StringUtils.isEmpty(rfid)) {
            wraInventoryTaskDetail.eq("rfid", "");
        } else {
            wraInventoryTaskDetail.ne("rfid", "");
        }
        InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailService.selectOne(wraInventoryTaskDetail);

        if (inventoryTaskDetail != null) {
            //判断盘点数量是否为空
            if (StringUtils.isEmpty(inventoryTaskDetail.getInventoryAmount())) {
                inventoryTaskDetail.setRfid(inventoryDetail.getRfid());
                inventoryTaskDetail.setInventoryAmount(inventoryDetail.getInventoryAmount());
                inventoryTaskDetail.setInventoryWeight(inventoryDetail.getInventoryWeight());
                flag = inventoryTaskDetailService.updateById(inventoryTaskDetail);
            } else {
                if (StringUtils.isEmpty(rfid)) {
                    inventoryTaskDetail.setRfid("");
                } else {
                    inventoryTaskDetail.setRfid(inventoryDetail.getRfid() + "," + inventoryTaskDetail.getRfid());
                }
                //盘点数量累加
                Double inventoryAmountDouble = Double.parseDouble(inventoryDetail.getInventoryAmount()) + Double.parseDouble(inventoryTaskDetail.getInventoryAmount());
                inventoryTaskDetail.setInventoryAmount(String.valueOf(inventoryAmountDouble));
                //盘点重量累加
                Double inventoryWeightDouble = 0.0;
                if (StringUtils.isEmpty(inventoryTaskDetail.getInventoryWeight())) {
                    inventoryWeightDouble = Double.parseDouble(inventoryDetail.getInventoryWeight());
                } else {
                    inventoryWeightDouble = Double.parseDouble(inventoryDetail.getInventoryWeight()) + Double.parseDouble(inventoryTaskDetail.getInventoryWeight());
                }
                inventoryTaskDetail.setInventoryWeight(String.valueOf(inventoryWeightDouble));
                //修改
                flag = inventoryTaskDetailService.updateAllColumnById(inventoryTaskDetail);
            }
        } else {
            InventoryTaskDetail taskDet = new InventoryTaskDetail();
            taskDet.setState(DyylConstant.INVENTORY_TASKDETAIL1);
            taskDet.setMaterialCode(materialCode);
            taskDet.setMaterialName(inventoryDetail.getMaterialName());
            taskDet.setBatchNo(inventoryDetail.getBatchNo());
            taskDet.setPositionCode(inventoryDetail.getPositionCode());
            taskDet.setInventoryTaskId(inventoryDAO.selectById(inventoryDetail.getInventoryId()).getInventoryTaskId());
            taskDet.setInventoryAmount(inventoryDetail.getInventoryAmount());
            taskDet.setInventoryWeight(inventoryDetail.getInventoryWeight());
            taskDet.setRfid(inventoryDetail.getRfid());
            flag = inventoryTaskDetailService.insert(taskDet);
        }
        return flag;
    }

}
