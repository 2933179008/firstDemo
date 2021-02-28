package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.User;
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
import java.util.stream.Collectors;

/**
 * @author pz
 * @Description: 盘点任务service实现
 * @Param:
 * @return:
 * @date 2019-01-14
 */
@Service("inventoryTaskService")
public class InventoryTaskServiceImpl extends ServiceImpl<InventoryTaskDAO, InventoryTask> implements InventoryTaskService {

    // 盘点任务DAO
    @Autowired
    private InventoryTaskDAO inventoryTaskDao;

    // 盘点DAO
    @Autowired
    private InventoryDAO inventoryDao;

    // 盘点任务详情service
    @Autowired
    private InventoryTaskDetailService inventoryTaskDetailService;

    // 盘点详情Service
    @Autowired
    private InventoryDetailService inventoryDetailService;

    // 库存service
    @Autowired
    private StockService stockService;

    // 预警service
    @Autowired
    private AlarmService alarmService;

    // 物料绑定service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    // 物料绑定详情service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    //用户DAO
    @Autowired
    private UserDAO userDAO;

    //库存变动Service
    @Autowired
    private StockChangeService stockChangeService;

    //库位Service
    @Autowired
    private DepotPositionService depotPositionService;

    //库位DAO
    @Autowired
    private DepotPositionDAO depotPositionDAO;

    @Autowired
    private MaterielService materielService;

    /**
     * @Description: 盘点分页查询
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-08
     */
    public PageUtils queryPageI(Map<String, Object> params) {

        //盘点任务编号
        String inventoryTaskCode = (String) params.get("inventoryTaskCode");
        //盘点类型
        String inventoryType = (String) params.get("inventoryType");

        Page<InventoryTask> page = this.selectPage(
                new Query<InventoryTask>(params).getPage(),
                new EntityWrapper<InventoryTask>()
                        .like(StringUtils.isNotEmpty(inventoryTaskCode), "inventory_task_code", inventoryTaskCode)
                        .like(StringUtils.isNotEmpty(inventoryType), "inventory_type", inventoryType)
        );

        for (InventoryTask inventoryTask : page.getRecords()) {
            User user = userDAO.selectById(inventoryTask.getInventoryUserId());
            if (user != null) {
                inventoryTask.setInventoryTaskName(user.getUsername());
            }
        }
        return new PageUtils(page);
    }

    /**
     * @Description: 生成盘点任务编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    @Override
    public String generatInventoryTaskCode() {
        //盘点任务编号
        String inventoryTaskCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.INVENTASK_CODE_FORMAT);
        //获取最大盘点任务编号
        String maxInventoryCode = inventoryTaskDao.getMaxInventoryTaskCode();
        if (StringUtils.isEmptyString(maxInventoryCode)) {
            inventoryTaskCode = "PT00000001";
        } else {
            Integer maxInventoryTaskCode_count = Integer.parseInt(maxInventoryCode.replace("PT", ""));
            inventoryTaskCode = df.format(maxInventoryTaskCode_count + 1);
        }
        return inventoryTaskCode;
    }

    /**
     * @Description: 新增/修改
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    @Override
    public Map<String, Object> saveInventoryTask(InventoryTask inventoryTask, Long userId) {
        Map<String, Object> map = new HashMap<>();
        boolean code = false;
        String msg = "";
        //获取盘点任务id
        Long inventoryTaskId = inventoryTask.getId();
        //获取盘点开始时间
        String inventoryTime = inventoryTask.getInventoryTime();
        //获取盘点结束时间
        String completeTime = inventoryTask.getCompleteTime();

        //新增保存
        if (inventoryTaskId == null) {
            inventoryTask.setCreateBy(userId);
            inventoryTask.setCreateTime(DateUtils.getTime());
            inventoryTask.setState(DyylConstant.INVENTORY_TASK0);

            //盘点任务为库位盘点时
            if (inventoryTask.getInventoryType().equals(DyylConstant.INVENTORYTYPE_0)) {
                //保存inventoryTask对象
                code = this.insert(inventoryTask);
                //调用添加盘点任务详情（库位类型）
                this.stockDetail(inventoryTask, userId);
            } else {  //盘点任务为动碰盘点时，依据起始时间取库位
                List<Long> positionBy = inventoryTaskDao.selectByPosition(inventoryTime, completeTime);
                if (positionBy.size() == 0) {
                    msg = "该时间内,盘点库位为空";
                    map.put("code", false);
                    map.put("msg", msg);
                    return map;
                } else {
                    //保存盘点任务
                    code = this.insert(inventoryTask);
                    //调用添加盘点任务详情（动碰类型）
                    this.dynamicDetail(inventoryTask, positionBy, userId);
                }
            }
        } else { //修改
            InventoryTask invTask = inventoryTaskDao.selectById(inventoryTaskId);
            //如果盘点类型没有被修改
            if (inventoryTask.getInventoryType().equals(invTask.getInventoryType())) {
                //如果盘点类型为库位盘点
                if (inventoryTask.getInventoryType().equals(DyylConstant.INVENTORYTYPE_0)) {
                    //如果库位编码没有被修改
                    if (invTask.getPositionCode().equals(inventoryTask.getPositionCode())) {
                        code = this.updateById(inventoryTask);
                    } else {  //如果库位被修改
                        //1.清除原库位盘点任务详情
                        EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
                        wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
                        inventoryTaskDetailService.delete(wraInventoryTaskDetail);

                        //2.插入现库位盘点任务详情
                        int count = inventoryTaskDao.selectCount(new EntityWrapper<InventoryTask>().eq("id", inventoryTaskId).eq("state", 4));
                        if (count == 0) {
                            this.stockDetail(inventoryTask, userId);
                        }
                        code = this.updateById(inventoryTask);
                    }
                } else {  //如果库位类型为动碰盘点
                    //如果动碰盘点中开始时间与结束时间没有改变
                    if (inventoryTask.getInventoryTime().equals(invTask.getInventoryTime()) && inventoryTask.getCompleteTime().equals(invTask.getCompleteTime())) {
                        code = this.updateById(inventoryTask);
                    } else {  //如果动碰盘点中开始时间与结束时间被改变
                        //1.清除原库位盘点任务详情
                        EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
                        wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
                        inventoryTaskDetailService.delete(wraInventoryTaskDetail);

                        //2.插入现库位盘点任务详情
                        List<Long> positionBy = inventoryTaskDao.selectByPosition(inventoryTime, completeTime);
                        if (positionBy == null || positionBy.size() == 0) {
                            msg = "该时间内,盘点库位为空";
                            map.put("code", false);
                            map.put("msg", msg);
                            return map;
                        } else {
                            this.dynamicDetail(inventoryTask, positionBy, userId);
                            code = this.updateById(inventoryTask);
                        }
                    }
                }
            } else {  //如果盘点类型没有被修改
                //清除对应的盘点任务详情
                EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
                wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
                inventoryTaskDetailService.delete(wraInventoryTaskDetail);

                //由动碰盘点改为库存盘点
                if (inventoryTask.getInventoryType().equals(DyylConstant.INVENTORYTYPE_0)) {
                    //添加盘点任务详情
                    this.stockDetail(inventoryTask, userId);
                } else {  //由库存盘点改为动碰盘点，添加盘点任务详情
                    List<Long> positionBy = inventoryTaskDao.selectByPosition(inventoryTime, completeTime);
                    if (positionBy == null || positionBy.size() == 0) {
                        msg = "该时间内,盘点库位为空";
                        map.put("code", false);
                        map.put("msg", msg);
                        return map;
                    } else {
                        this.dynamicDetail(inventoryTask, positionBy, userId);
                    }
                }
                code = this.updateById(inventoryTask);
            }
        }

        map.put("code", code);
        //返回插入后的收货单的id
        map.put("id", inventoryTask.getId());
        map.put("inventoryTaskCode", inventoryTask.getInventoryTaskCode());
        return map;
    }

    /**
     * @Description: 添加盘点任务详情（库位方法）
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    private void stockDetail(InventoryTask inventoryTask, Long userId) {
        String[] positionCodeArr = inventoryTask.getPositionCode().split(",");

        for (int i = 0; i < positionCodeArr.length; i++) {
            this.saveInventoryTaskDetail(positionCodeArr[i], inventoryTask.getId(), userId);
        }
    }

    /**
     * @Description: 添加盘点任务详情（动碰方法）
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    private void dynamicDetail(InventoryTask inventoryTask, List<Long> positionBy, Long userId) {
        String pcode = "";
        for (Long position : positionBy) {
            if (position != null) {
                String positionCode = depotPositionService.selectById(position).getPositionCode();
                Long inventoryTaskId = inventoryTask.getId();
                this.saveInventoryTaskDetail(positionCode, inventoryTaskId, userId);
                pcode = pcode + positionCode + ",";
            }
        }
        if (pcode.length() > 0) {
            pcode = pcode.substring(0, pcode.length() - 1);
        }
        inventoryTask.setPositionCode(pcode);
        inventoryTaskDao.updateById(inventoryTask);
    }

    /**
     * 根据库位编码、盘点任务id和当前登陆人保存盘点任务详细
     *
     * @param positionCode
     * @param inventoryTaskId
     * @param userId
     */
    private void saveInventoryTaskDetail(String positionCode, Long inventoryTaskId, Long userId) {
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        wraStock.eq("position_code", positionCode);
        List<Stock> lstStock = stockService.selectList(wraStock);
        if (lstStock != null && lstStock.size() != 0) {
            List<InventoryTaskDetail> lstInventoryTaskDetail = new ArrayList<>();
            InventoryTaskDetail inventoryTaskDetail = null;
            for (Stock stock : lstStock) {
                inventoryTaskDetail = new InventoryTaskDetail();
                inventoryTaskDetail.setInventoryTaskId(inventoryTaskId);
                inventoryTaskDetail.setPositionCode(positionCode);
                inventoryTaskDetail.setMaterialCode(stock.getMaterialCode());
                inventoryTaskDetail.setRfid(stock.getRfid());
                inventoryTaskDetail.setCreateTime(DateUtils.getTime());
                inventoryTaskDetail.setCreateBy(userId);
                inventoryTaskDetail.setMaterialName(stock.getMaterialName());
                inventoryTaskDetail.setBatchNo(stock.getBatchNo());
                inventoryTaskDetail.setStockAmount(stock.getStockAmount());
                inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL0);
                lstInventoryTaskDetail.add(inventoryTaskDetail);
            }
            if (lstInventoryTaskDetail != null || lstInventoryTaskDetail.size() != 0) {
                inventoryTaskDetailService.insertBatch(lstInventoryTaskDetail);
            }
        }
    }

    /**
     * @Description: 提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    public Map<String, Object> submitInventoryTask(Long id) {
        Map<String, Object> map = new HashMap<>();
        boolean result = true;
        String msg = "";
        //根据id查询详情
        EntityWrapper<InventoryTaskDetail> entity = new EntityWrapper<>();
        entity.eq("inventory_task_id", id);
        List<InventoryTaskDetail> lstInventoryTaskDetail = inventoryTaskDetailService.selectList(entity);
        if (lstInventoryTaskDetail != null && lstInventoryTaskDetail.size() > 0) {
            for (InventoryTaskDetail inventoryTaskDetail : lstInventoryTaskDetail) {
                //物料名称
                String materialName = inventoryTaskDetail.getMaterialName();
                //批次号
                String batchNo = inventoryTaskDetail.getBatchNo();
                //库存数量
                String stockAmount = inventoryTaskDetail.getStockAmount();

                if (StringUtils.isEmptyString(materialName)) {
                    result = false;
                    msg = "提交失败！，请确认盘点任务【" + inventoryTaskDetail.getMaterialName() + "】的物料名称是否填写";
                }
                if (StringUtils.isEmptyString(batchNo)) {
                    result = false;
                    msg = "提交失败！，请确认盘点任务【" + inventoryTaskDetail.getBatchNo() + "】的批次号是否填写";
                }
                if (StringUtils.isEmptyString(stockAmount) || Double.parseDouble(stockAmount) <= 0) {
                    result = false;
                    msg = "提交失败！，请确认盘点任务【" + inventoryTaskDetail.getStockAmount() + "】的库存数量是否填写正确";
                }
            }
        } else {
            result = false;
            msg = "提交失败！请填写物料信息";
        }

        if (result) {
            //获取收货计划单的状态
            String state = inventoryTaskDao.selectById(id).getState();
            if (DyylConstant.INVENTORYTYPE_0.equals(state)) {
                result = inventoryTaskDao.updateState(id) > 0;
                if (result) {
                    msg = "提交成功！";
                } else {
                    result = false;
                    msg = "提交失败！";
                }
            } else {
                result = false;
                msg = "提交失败！该单据不可提交";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 审核
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-22
     */
    public Map<String, Object> auditTaskCheck(String stockIdStr, String qualityDateStr, String qualityPeriodStr, Long userId) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String message = "审核成功";

        if (StringUtils.isEmpty(stockIdStr)) {
            map.put("result", result);
            map.put("message", "请选择一个要审核的盘点任务！");
            return map;
        }

        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(stockIdStr.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //获取盘点任务详情记录
        EntityWrapper<InventoryTaskDetail> wrapperInventoryDetail = new EntityWrapper<>();
        wrapperInventoryDetail.in("id", lstIds);
        List<InventoryTaskDetail> lstInventoryTaskDetail = inventoryTaskDetailService.selectList(wrapperInventoryDetail);
        wrapperInventoryDetail.eq("state", DyylConstant.INVENTORY_TASKDETAIL2);
        int count = inventoryTaskDetailService.selectCount(wrapperInventoryDetail);
        if (count > 0) {
            map.put("result", result);
            map.put("message", "审核失败:存在记录已审核！");
            return map;
        }

        //获取盘点任务id
        Long inventoryTaskId = lstInventoryTaskDetail.get(0).getInventoryTaskId();
        //获取盘点任务实体
        InventoryTask inventoryTask = inventoryTaskDao.selectById(inventoryTaskId);

        for (InventoryTaskDetail inventoryTaskDetail : lstInventoryTaskDetail) {
            //获取盘点任务编号
            String inventoryTaskCode = inventoryTask.getInventoryTaskCode();
            //获取盘点数量
            String inventoryAmount = inventoryTaskDetail.getInventoryAmount();
            //获取盘点重量
            String inventoryWeight = inventoryTaskDetail.getInventoryWeight();
            //获取库存数量
            String stockAmount = inventoryTaskDetail.getStockAmount();
            //获取库位编码
            String positionCode = inventoryTaskDetail.getPositionCode();
            //获取物料编号
            String materialCode = inventoryTaskDetail.getMaterialCode();
            //获取物料名称
            String materialName = inventoryTaskDetail.getMaterialName();
            //获取批次号
            String batchNo = inventoryTaskDetail.getBatchNo();
            //获取rfid
            String rfid = inventoryTaskDetail.getRfid();

            List<String> rfidList = null;
            //将rfid转换成list
            if (StringUtils.isNotEmpty(rfid)) {
                rfidList = Arrays.asList(rfid.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
            }

            //依据库位编码，物料编码，批次号，物料类型确定一条库存信息
            EntityWrapper<Stock> wraStock = new EntityWrapper<>();
            wraStock.eq("position_code", positionCode);
            wraStock.eq("material_code", materialCode);
            wraStock.eq("batch_no", batchNo);
            if (StringUtils.isNotEmpty(rfid)) {
                wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_CARGO);
            } else {
                wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_NOCARGO);
            }
            Stock stock = stockService.selectOne(wraStock);

            //如果盘点数量为空或为0，则表示着未盘点物料，在库存中删除物料
            if (StringUtils.isEmpty(inventoryAmount) || inventoryAmount.equals("0")) {
                if (StringUtils.isNotEmpty(rfid)) {
                    for (String Rfid : rfidList) {
                        //获取对应的物料绑定rfid详情
                        EntityWrapper<MaterielBindRfidDetail> wrapperMaterielBindRfidDetail = new EntityWrapper<>();
                        wrapperMaterielBindRfidDetail.eq("materiel_code", materialCode);
                        wrapperMaterielBindRfidDetail.eq("batch_rule", batchNo);
                        wrapperMaterielBindRfidDetail.eq("rfid", Rfid);
                        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(wrapperMaterielBindRfidDetail);
                        Long mid = materielBindRfidDetail.getMaterielBindRfidBy();
                        EntityWrapper<MaterielBindRfidDetail> wrapperMaterielBindRfidDet = new EntityWrapper<>();
                        wrapperMaterielBindRfidDet.eq("materiel_bind_rfid_by", mid);
                        wrapperMaterielBindRfidDet.eq("delete_flag", DyylConstant.NOTDELETED);
                        List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = materielBindRfidDetailService.selectList(wrapperMaterielBindRfidDet);
                        //如果绑定详情只有一条，那么在删除物料绑定详情的同时还需要删除物料绑定RFID信息
                        if(lstMaterielBindRfidDetail.size()==1){
                            materielBindRfidDetailService.delete(wrapperMaterielBindRfidDetail);
                            materielBindRfidService.deleteById(mid);
                        }else{
                            materielBindRfidDetailService.deleteById(materielBindRfidDetail);
                        }
                    }
                }
                //删除库存
                result = stockService.deleteById(stock);
                if (result) {
                    //修改盘点任务详情
                    inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL2);
                    inventoryTaskDetail.setCompleteTime(DateUtils.getTime());
                    inventoryTaskDetailService.updateById(inventoryTaskDetail);
                    //获取库位id
                    Long PositionBy = inventoryTaskDao.selectBYCode(positionCode);
                    //插入库存变动详情(盘亏出库)
                    stockChangeService.saveStockChange(inventoryTaskCode, materialCode, materialName, batchNo, DyylConstant.LESS_DEPOT_OUT, inventoryAmount, "", inventoryAmount, PositionBy, userId);
                } else {
                    map.put("message", "审核失败!");
                    map.put("result", false);
                    return map;
                }
            } else if (StringUtils.isEmpty(stockAmount) || stockAmount.equals("0")) {  //如果库存数量为空或为0，则表示着未需要在库存中新建记录
                //盘点库位id
                Long positionBy = null;
                //盘点详情的库位id
                Long detPositionBy = inventoryTaskDao.selectBYCode(positionCode);
                if (StringUtils.isNotEmpty(rfid)) {
                    //获取盘点记录详情(有rfid)
                    InventoryDetail inventoryDet = inventoryTaskDao.selectByRfidInventoryDetail(positionCode, materialCode, batchNo, rfidList.get(0));
                    //获取盘点id
                    Long inventoryId = inventoryDet.getInventoryId();
                    //获取盘点库位编码
                    String posCode = inventoryDao.selectById(inventoryId).getPositionCode();
                    //查询盘点
                    Inventory inventory = inventoryTaskDao.selectByInventory(inventoryTaskId, posCode);
                    List<String> rfidList2 = null;
                    List<String> listRfid = null;
                    if (StringUtils.isNotEmpty(stock.getRfid())) {
                        rfidList2 = Arrays.asList(stock.getRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                        listRfid = Arrays.asList(stock.getRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                    }
                    if (inventory != null) {
                        //获取盘点库位编码
                        String inPostionCode = inventory.getPositionCode();
                        //获取盘点库位名称
                        String inPositionName = depotPositionDAO.selectPositionName(inPostionCode);
                        listRfid.removeAll(rfidList);
                        if (listRfid.size() == 0) {  //直接修改库位
                            //修改库位编码
                            stock.setPositionCode(inPostionCode);
                            //修改库位名称
                            stock.setPositionName(inPositionName);
                            stock.setStockAmount(inventoryAmount);
                            stock.setAvailableStockAmount(inventoryAmount);
                            stock.setStockWeight(inventoryWeight);
                            stock.setAvailableStockWeight(inventoryWeight);
                            //获取rfid数量
                            String rfidAmount = String.valueOf(rfidList.size());
                            stock.setStockRfidAmount(rfidAmount);
                            stock.setAvailableStockRfidAmount(rfidAmount);
                            stockService.updateById(stock);
                        } else { //不相等的话需要对rfid进行增删
                            //对库存进行删减RFID
                            rfidList2.removeAll(rfidList);
                            String stockdRfid = "";
                            for (String realsrfid : rfidList2) {
                                stockdRfid = stockdRfid + realsrfid + ",";
                            }
                            stockdRfid = stockdRfid.substring(0, stockdRfid.length() - 1);
                            //对rfid对应的物料数量进行求和
                            EntityWrapper<MaterielBindRfidDetail> wraMaterielBindRfidDetail = new EntityWrapper<>();
                            wraMaterielBindRfidDetail.eq("delete_flag", DyylConstant.NOTDELETED);
                            wraMaterielBindRfidDetail.eq("materiel_code", materialCode);
                            wraMaterielBindRfidDetail.eq("batch_rule", batchNo);
                            wraMaterielBindRfidDetail.in("rfid", rfid);
                            List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = materielBindRfidDetailService.selectList(wraMaterielBindRfidDetail);
                            //对重量，数量求和
                            Double mbrdAmount = 0.0;
                            Double mbrdWeight = 0.0;
                            if (lstMaterielBindRfidDetail.size() == 1) {
                                mbrdAmount = Double.valueOf(lstMaterielBindRfidDetail.get(0).getAmount());
                                mbrdWeight = Double.valueOf(lstMaterielBindRfidDetail.get(0).getWeight());
                            } else if (lstMaterielBindRfidDetail.size() > 1) {
                                for (MaterielBindRfidDetail materielBindRfidDetail : lstMaterielBindRfidDetail) {
                                    mbrdAmount = mbrdAmount + Double.valueOf(materielBindRfidDetail.getAmount());
                                    mbrdWeight = mbrdWeight + Double.valueOf(materielBindRfidDetail.getWeight());
                                }
                            }
                            String number = String.valueOf(Double.valueOf(stock.getStockAmount()) - mbrdAmount);
                            String weight = String.valueOf(Double.valueOf(stock.getStockWeight()) - mbrdWeight);
                            stock.setStockAmount(number);
                            stock.setAvailableStockAmount(number);
                            stock.setStockWeight(weight);
                            stock.setAvailableStockWeight(weight);
                            stock.setRfid(stockdRfid);
                            stock.setAvailableRfid(stockdRfid);
                            stock.setStockRfidAmount(String.valueOf(rfidList2.size()));
                            stock.setAvailableStockRfidAmount(String.valueOf(rfidList2.size()));
                            stockService.updateById(stock);
                            //修改库位编码
                            stock.setPositionCode(inPostionCode);
                            //修改库位名称
                            stock.setPositionName(inPositionName);
                            stock.setRfid(rfid);
                            stock.setAvailableRfid(rfid);
                            stock.setStockAmount(inventoryAmount);
                            stock.setAvailableStockAmount(inventoryAmount);
                            stock.setStockWeight(inventoryWeight);
                            stock.setAvailableStockWeight(inventoryWeight);
                            stock.setStockRfidAmount(String.valueOf(rfidList.size()));
                            stock.setAvailableStockRfidAmount(String.valueOf(rfidList.size()));
                            stockService.insert(stock);
                        }
                        for (String rfid1 : rfidList) {
                            //修改物料绑定rfid
                            EntityWrapper<MaterielBindRfid> wraMaterielBindRfid = new EntityWrapper<>();
                            wraMaterielBindRfid.eq("rfid", rfid1);
                            wraMaterielBindRfid.eq("deleted_flag", DyylConstant.NOTDELETED);
                            wraMaterielBindRfid.eq("status", DyylConstant.STATE_UNTREATED);
                            MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(wraMaterielBindRfid);
                            //获取库位id
                            positionBy = inventoryTaskDao.selectBYCode(inPostionCode);
                            materielBindRfid.setPositionBy(positionBy);
                            result = materielBindRfidService.updateById(materielBindRfid);
                            //获取盘点记录详情
                            EntityWrapper<InventoryDetail> wraInventoryDet = new EntityWrapper<>();
                            wraInventoryDet.in("rfid", rfid1);
                            wraInventoryDet.eq("inventory_id", inventory.getId());
                            wraInventoryDet.eq("material_code", materialCode);
                            wraInventoryDet.eq("position_Code", positionCode);
                            wraInventoryDet.eq("batch_no", batchNo);
                            List<InventoryDetail> lstInventoryDetail = inventoryDetailService.selectList(wraInventoryDet);
                            for (InventoryDetail inventoryDetail : lstInventoryDetail) {
                                EntityWrapper<MaterielBindRfidDetail> wraMaterielBindRfidDetail = new EntityWrapper<>();
                                wraMaterielBindRfidDetail.eq("materiel_bind_rfid_by", materielBindRfid.getId());
                                wraMaterielBindRfidDetail.eq("materiel_code", materialCode);
                                wraMaterielBindRfidDetail.eq("position_id", detPositionBy);
                                wraMaterielBindRfidDetail.eq("batch_rule", batchNo);
                                wraMaterielBindRfidDetail.eq("delete_flag", DyylConstant.NOTDELETED);
                                wraMaterielBindRfidDetail.eq("rfid", rfid1);
                                MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(wraMaterielBindRfidDetail);
                                materielBindRfidDetail.setAmount(inventoryDetail.getInventoryAmount());
                                materielBindRfidDetail.setWeight(inventoryDetail.getInventoryWeight());
                                materielBindRfidDetail.setPositionId(positionBy);
                                materielBindRfidDetailService.updateById(materielBindRfidDetail);
                            }
                        }
                    }
                } else {
                    //获取盘点记录详情(无rfid)
                    InventoryDetail inventoryDet = inventoryTaskDao.selectByInventoryDetail(positionCode, materialCode, batchNo);
                    //获取盘点
                    Long inventoryId = inventoryDet.getInventoryId();
                    //获取盘点库位编码
                    String posCode = inventoryDao.selectById(inventoryId).getPositionCode();
                    //查询盘点
                    Inventory inventory = inventoryTaskDao.selectByInventory(inventoryTaskId, posCode);
                    if (inventory == null) {
                        map.put("message", "盘点记录提交不全!");
                        map.put("result", false);
                        return map;
                    }
                    //获取盘点库位编码
                    String inPostionCode = inventory.getPositionCode();
                    //获取库位名称
                    String inPositionName = depotPositionDAO.selectPositionName(inPostionCode);
                    stock.setPositionCode(inPostionCode);
                    stock.setPositionName(inPositionName);
                    stock.setStockAmount(inventoryAmount);
                    stock.setAvailableStockAmount(inventoryAmount);
                    stock.setStockWeight(inventoryWeight);
                    stock.setAvailableStockWeight(inventoryWeight);
                    result = stockService.updateById(stock);
                }
                if (result) {
                    //修改盘点任务详情
                    inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL2);
                    inventoryTaskDetail.setCompleteTime(DateUtils.getTime());
                    inventoryTaskDetailService.updateById(inventoryTaskDetail);
                    //插入库存变动详情(盘盈入库)
                    stockChangeService.saveStockChange(inventoryTaskCode, materialCode, materialName, batchNo, DyylConstant.MANG_DEPOT_IN, "", inventoryAmount, inventoryAmount, positionBy, userId);
                } else {
                    map.put("message", "审核失败!");
                    map.put("result", false);
                    return map;
                }
            } else {
                //判断盘平标志 false 不盘平 true盘平 盘平不引发库存变动与预警
                boolean inventoryFlag = false;
                if (stock != null) {
                    //判断是有rfid物料还是无rfid物料  有rfid则需要修改库存与物料绑定信息
                    if (StringUtils.isNotEmpty(rfid)) {
                        //查询盘点记录（依据最新时间与已提交状态）
                        Inventory inventory = inventoryTaskDao.selectByInventory(inventoryTaskId, positionCode);
                        if (inventory == null) {
                            map.put("message", "盘点记录提交不全!");
                            map.put("result", false);
                            return map;
                        }

                        EntityWrapper<InventoryDetail> wraInventoryDetail = new EntityWrapper<>();
                        wraInventoryDetail.eq("inventory_id", inventory.getId());
                        wraInventoryDetail.eq("position_code", positionCode);
                        wraInventoryDetail.eq("material_code", materialCode);
                        wraInventoryDetail.eq("batch_no", batchNo);
                        wraInventoryDetail.in("rfid", rfid);
                        List<InventoryDetail> lstInventoryDetail = inventoryDetailService.selectList(wraInventoryDetail);
                        //循环修改物料绑定rfid详情
                        for (InventoryDetail inventoryDetail : lstInventoryDetail) {
                            //获取库位id
                            Long positionBy = inventoryTaskDao.selectBYCode(positionCode);
                            //获取物料绑定rfid详情
                            EntityWrapper<MaterielBindRfidDetail> wraMaterielBindRfidDetail = new EntityWrapper<>();
                            wraMaterielBindRfidDetail.eq("materiel_code", inventoryDetail.getMaterialCode());
                            wraMaterielBindRfidDetail.eq("position_id", positionBy);
                            wraMaterielBindRfidDetail.eq("batch_rule", inventoryDetail.getBatchNo());
                            wraMaterielBindRfidDetail.eq("rfid", inventoryDetail.getRfid());
                            wraMaterielBindRfidDetail.eq("delete_flag", DyylConstant.NOTDELETED);
                            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(wraMaterielBindRfidDetail);
                            materielBindRfidDetail.setAmount(inventoryDetail.getInventoryAmount());
                            materielBindRfidDetail.setWeight(inventoryDetail.getInventoryWeight());
                            materielBindRfidDetailService.updateById(materielBindRfidDetail);
                        }
                        if (stock.getStockAmount().equals(inventoryAmount)) {
                            inventoryFlag = true;
                        }
                        //修改库存信息
                        stock.setStockAmount(inventoryAmount);
                        stock.setAvailableStockAmount(inventoryAmount);
                        stock.setStockWeight(inventoryWeight);
                        stock.setAvailableStockWeight(inventoryWeight);
                        stock.setRfid(rfid);
                        stock.setAvailableRfid(rfid);
                        stock.setStockRfidAmount(String.valueOf(rfidList.size()));
                        stock.setAvailableStockRfidAmount(String.valueOf(rfidList.size()));
                        result = stockService.updateById(stock);
                    } else {
                        if (stock.getStockAmount().equals(inventoryAmount)) {
                            inventoryFlag = true;
                        }
                        //修改库存信息
                        stock.setStockAmount(inventoryAmount);
                        stock.setAvailableStockAmount(inventoryAmount);
                        stock.setStockWeight(inventoryWeight);
                        stock.setAvailableStockWeight(inventoryWeight);
                        result = stockService.updateById(stock);
                    }
                    if (result) {
                        //修改盘点任务详情状态
                        inventoryTaskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL2);
                        inventoryTaskDetail.setCompleteTime(DateUtils.getTime());
                        result = inventoryTaskDetailService.updateById(inventoryTaskDetail);
                        //获取库位id
                        Long positionBy = inventoryTaskDao.selectBYCode(positionCode);
                        //判断是否盘平  不盘平则引发库存变动与预警
                        if (!inventoryFlag) {
                            //盘点数量减去库存数量
                            Double amount = Double.parseDouble(inventoryAmount) - Double.parseDouble(stockAmount);

                            //获取库区
                            DepotPosition depotPosition = depotPositionService.selectById(positionBy);
                            //插入库存变动详情  先判断盘盈盘亏 盘点数量大于库存数量则表明盘盈，否则则是盘亏
                            if (amount > 0) {
                                stockChangeService.saveStockChange(inventoryTaskCode, materialCode, materialName, batchNo, DyylConstant.MANG_DEPOT_IN, "", String.valueOf(amount), inventoryAmount, positionBy, userId);
                                //插入预警
                                alarmService.addAlarm(depotPosition.getDepotareaId(), DyylConstant.ALARM_INVENTORY_VARIANCE, String.valueOf(inventoryTaskDetail.getInventoryTaskId()));
                            } else if (amount < 0) {
                                stockChangeService.saveStockChange(inventoryTaskCode, materialCode, materialName, batchNo, DyylConstant.LESS_DEPOT_OUT, String.valueOf(Math.abs(amount)), "", inventoryAmount, positionBy, userId);
                                //插入预警
                                alarmService.addAlarm(depotPosition.getDepotareaId(), DyylConstant.ALARM_INVENTORY_VARIANCE, String.valueOf(inventoryTaskDetail.getInventoryTaskId()));
                            }
                        }
                    } else {
                        map.put("message", "审核失败!");
                        map.put("result", false);
                        return map;
                    }
                } else {
                    map.put("message", "审核失败:库存中不存在此物料");
                    map.put("result", false);
                    return map;
                }
            }
        }
        if (result) {
            //修改盘点任务状态，如果盘点任务对应的详情全部为已审核，盘点状态改为已审核。如果对应的详情存在未审核，则不改变状态仍然为待审核。
            EntityWrapper<InventoryTaskDetail> wrapperInventoryTaskDetail = new EntityWrapper<>();
            wrapperInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
            wrapperInventoryTaskDetail.eq("state", DyylConstant.INVENTORY_TASKDETAIL1);
            int detailCount = inventoryTaskDetailService.selectCount(wrapperInventoryTaskDetail);
            if (detailCount == 0) {
                inventoryTask.setState(DyylConstant.INVENTORY_TASK4);
                inventoryTaskDao.updateById(inventoryTask);
            }
        }

        /******************** 对相应的库存物料的保质期到期日期和基础物料的保质期进行修改  @author yuany @date 20190605 ***********************/
        String[] id = stockIdStr.split(",");
        String[] qualityDate = qualityDateStr.split(",");
        String[] qualityPeriod = qualityPeriodStr.split(",");
        for (int i = 0; i < id.length; i++) {
            //获取盘点任务详情
            InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailService.selectById(id[i]);
            //判断有无RFID
            String billType = null;
            if (Strings.isNullOrEmpty(inventoryTaskDetail.getRfid())) {
                billType = DyylConstant.MATERIAL_NORFID;
            } else {
                billType = DyylConstant.MATERIAL_RFID;
            }
            //获取对应的库存
            Stock stock = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("material_type", billType)
                            .eq("material_code", inventoryTaskDetail.getMaterialCode())
                            .eq("batch_no", inventoryTaskDetail.getBatchNo())
                            .eq("position_code", inventoryTaskDetail.getPositionCode())

            );
            //获取物料基础信息
            Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code", inventoryTaskDetail.getMaterialCode()));
            //若库存保质期到期日期和盘点任务详情对应的物料不同，则更新
            if (!stock.getQualityDate().equals(qualityDate[i])) {
                stock.setQualityDate(qualityDate[i]);
                stockService.updateById(stock);
            }
            if (!materiel.getQualityPeriod().equals(qualityPeriod[i])) {
                materiel.setQualityPeriod(qualityPeriod[i]);
                materielService.updateById(materiel);
            }
        }


        map.put("message", message);
        map.put("result", result);
        return map;
    }

    /**
     * @Description: 复盘
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-22
     */
    public Map<String, Object> replayInventory(Long id) {
        Map<String, Object> map = Maps.newHashMap();
        //获取盘点任务详情实体
        InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailService.selectById(id);
        //获取库位
        String positionCode = inventoryTaskDetail.getPositionCode();
        //获取同库位盘点任务详情
        EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
        wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskDetail.getInventoryTaskId());
        wraInventoryTaskDetail.eq("position_code", positionCode);
        List<InventoryTaskDetail> lstInventoryTaskDetail = inventoryTaskDetailService.selectList(wraInventoryTaskDetail);
        for (InventoryTaskDetail taskDetail : lstInventoryTaskDetail) {
            taskDetail.setState(DyylConstant.INVENTORY_TASKDETAIL3);
            inventoryTaskDetailService.updateById(taskDetail);
        }
        //获取盘点任务实体
        InventoryTask inventoryTask = inventoryTaskDao.selectById(inventoryTaskDetail.getInventoryTaskId());
        inventoryTask.setState(DyylConstant.INVENTORY_TASK1);
        boolean flag = inventoryTaskDao.updateById(inventoryTask) > 0;
        if (flag) {
            map.put("result", true);
            map.put("message", "复盘成功!");
        } else {
            map.put("result", false);
            map.put("message", "复盘失败!");
        }
        EntityWrapper<InventoryTask> wraInventoryTask = new EntityWrapper<>();
        wraInventoryTask.ne("state", DyylConstant.INVENTORY_TASK5).eq("id", inventoryTaskDetailService.selectById(id).getInventoryTaskId());
        List<InventoryTask> lstInventoryTask = inventoryTaskDao.selectList(wraInventoryTask);
        if (lstInventoryTask.size() == 0) {
            inventoryTaskDao.updateInventoryTask(id);
        }
        return map;
    }

    /**
     * @Description: 获取导出列
     * @Param:
     * @return:List<InventoryTask>
     * @author pz
     * @date 2019-01-14
     */
    @Override
    public List<InventoryTask> getAllListI(String ids) {
        List<InventoryTask> list = inventoryTaskDao.getAllListT(StringUtils.stringToList(ids));
        list.forEach(a -> {

        });

        return list;
    }

    /**
     * @Description: 导出excel
     * @Param:list
     * @return:
     * @author pz
     * @date 2019-01-14
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<InventoryTask> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "盘点任务管理表" + "(" + date + ")";
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

            //0：未提交；1：待盘点；2：盘点中；3： 待审核；4：已审核；5：复盘中
            for (InventoryTask in : list) {
                if (in.getState().equals(DyylConstant.INVENTORY_TASK0)) {
                    in.setState("未提交");
                } else if (in.getState().equals(DyylConstant.INVENTORY_TASK1)) {
                    in.setState("待盘点");
                } else if (in.getState().equals(DyylConstant.INVENTORY_TASK2)) {
                    in.setState("盘点中");
                } else if (in.getState().equals(DyylConstant.INVENTORY_TASK3)) {
                    in.setState("待审核");
                } else if (in.getState().equals(DyylConstant.INVENTORY_TASK4)) {
                    in.setState("已审核");
                } else {
                    in.setState("复盘中");
                }
                in.setUserName(userDAO.selectById(in.getInventoryUserId()).getUsername());
                if (in.getInventoryType().equals(DyylConstant.INVENTORYTYPE_0)) {
                    in.setInventoryType("库位盘点");
                } else {
                    in.setInventoryType("动碰盘点");
                }
            }

            Map<String, String> mapFields = new LinkedHashMap<>();

            mapFields.put("inventoryTaskCode", "盘点任务编号");
            mapFields.put("inventoryType", "盘点类型");
            mapFields.put("createTime", "盘点时间");
            mapFields.put("userName", "盘点人");
            mapFields.put("state", "状态");
            mapFields.put("remark", "备注");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据主键查询盘点任务
     *
     * @param id 任务主键
     * @return StockCheck
     * @author pz
     * @date 2018-10-08
     */
    public InventoryTask getInventoryTaskById(Long id) {
        InventoryTask task = inventoryTaskDao.queryForInventoryTask(id);
        if (null == task) {
            return new InventoryTask();
        }
        if (task.getInventoryTime() != null) {
            String nowTime = DateUtils.getTime();
            task.setInventoryTime(nowTime);
        }
        return task;
    }

    @Override
    public List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo, String positionCode) {
        Page page = new Page(pageNo, pageSize);
        return inventoryTaskDao.getSelectPositionList(page, queryString, positionCode);
    }

    /**
     * 根据主键查询盘点任务
     *
     * @param id 任务主键
     * @return StockCheck
     * @author pz
     * @date 2018-10-08
     */
    public InventoryTask getInventoryTaskVoById(Long id) {
        InventoryTask task = inventoryTaskDao.selectById(id);
        if (null == task) {
            return new InventoryTask();
        }
        if (task.getInventoryTime() != null) {
            //当前时间
            String nowTime = DateUtils.getTime();
            task.setInventoryTime(nowTime);
        }
        return task;
    }


}