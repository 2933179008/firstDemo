package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.*;
import com.tbl.modules.handheldInterface.service.*;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.service.PutBillDetailService;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.stock.service.StockChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上架接口Service实现
 *
 * @author yuany
 * @date 2019-02-15
 */
@Service("putBillInterfaceService")
public class PutBillInterfaceServiceImpl extends ServiceImpl<PutBillInterfaceDAO, PutBill> implements PutBillInterfaceService {

    //日志接口
    @Autowired
    private InterfaceLogService interfaceLogService;

    //上架接口DAO
    @Autowired
    private PutBillInterfaceDAO putBillInterfaceDAO;

    //上架详情DAO
    @Autowired
    private PutBillDetailInterfaceDAO putBillDetailInterfaceDAO;

    //入库详情DAO
    @Autowired
    private InstorageDetailInterfaceDAO instorageDetailInterfaceDAO;

    //入库DAO
    @Autowired
    private InstorageInterfaceDAO instorageInterfaceDAO;

    //库位DAO
    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    //物料绑定接口DAO
    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    //库位DAO
    @Autowired
    private DepotPositionService depotPositionService;

    //物料绑定详情接口DAO
    @Autowired
    private MaterielBindRfidDetailInterfaceDAO materielBindRfidDetailInterfaceDAO;

    //物料绑定Service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    //入库详情Service
    @Autowired
    private InstorageDetailInterfaceService instorageDetailInterfaceService;

    @Autowired
    private DepotPositionInterfaceService depotPositionInterfaceService;

    @Autowired
    private PutBillService putBillService;

    @Autowired
    private PutBillDetailService putBillDetailService;

    @Autowired
    private StockChangeService stockChangeService;

    @Autowired
    private MovePositionInterfaceService movePositionInterfaceService;

    @Autowired
    private StockInterfaceService stockInterfaceService;


    /**
     * 条件查询上架列表
     *
     * @param userId
     * @return
     * @author yuany
     * @date 2019-02-15
     */
    @Override
    public List<PutBill> getPutBillList(Long userId) {
        return putBillInterfaceDAO.getPutBillList(userId);
    }

    @Override
    public boolean updatePutBillState(Long putBillId) {
        boolean result = false;
        Integer updateResult = putBillInterfaceDAO.updateState(putBillId);
        if (updateResult > 0) {
            result = true;
        }
        return result;
    }

    /**
     * 根据登陆ID获取上架单
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getPutBill(Long userId) {

        boolean result = true;
        String msg = "获取上架单成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<PutBill> putBillList = null;
        if (userId != null) {
            putBillList = putBillInterfaceDAO.getPutBillList(userId);
            if (putBillList.isEmpty()) {
                result = false;
                msg = "当前无上架任务";
            }
        } else {
            result = false;
            msg = "失败原因：获取当前用户ID失败";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "上架单调用接口";
        String parameter = "UserId:" + userId.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", putBillList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 上架RFID验证接口
     *
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> getProvingRfid(String instorageProcess, String rfid) {
        boolean result = true;
        String msg = "验证成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<MaterielBindRfidDetail> materielBindRfidDetailList = null;
        if (!Strings.isNullOrEmpty(rfid) && !Strings.isNullOrEmpty(instorageProcess)) {
            //白糖
            if (instorageProcess.equals("1")) {
                MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.materielBindRfid(rfid);
                if (materielBindRfid != null) {
                    result = false;
                    msg = "失败原因：此RFID存在绑定关系";
                    errorinfo = DateUtils.getTime();
                }
            } else {
                materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("status", DyylConstant.STATE_PROCESSED)
                                .eq("rfid", rfid)
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                );
                if (materielBindRfidDetailList.isEmpty()) {
                    result = false;
                    msg = "失败原因：RFID不存在物料详情绑定关系";
                    errorinfo = DateUtils.getTime();
                } else {
                    map.put("materielBindRfidDetailList", materielBindRfidDetailList);
                }
            }


        } else {
            result = false;
            msg = "失败原因：未获取RFID";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用上架RFID验证接口";
        String parameter = "InstorageProcess" + instorageProcess + "RFID:" + rfid;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

//    /**
//     * 上架确认
//     *
//     * @param putBillId
//     * @return
//     */
//    @Override
//    public Map<String, Object> completePutBill(Long userId, Long putBillId) {
//
//        boolean result = true;
//        String msg = "上架成功";
//        Map<String, Object> map = new HashMap<>();
//
//        //获取上架表
//        PutBill putBill = putBillInterfaceDAO.selectById(putBillId);
//        //获取上架详情集合
//        List<PutBillDetail> putBillDetailList = putBillDetailInterfaceDAO.selectList(
//                new EntityWrapper<PutBillDetail>()
//                        .eq("put_bill_id", putBillId)
//        );
//        String errorinfo = null;
//        //判断是否符合上架确认条件
//        for (PutBillDetail putBillDetail : putBillDetailList) {
//            if (Strings.isNullOrEmpty(putBillDetail.getRfid()) || Strings.isNullOrEmpty(putBillDetail.getPutAmount())
//                    || Strings.isNullOrEmpty(putBillDetail.getPutWeight()) || putBillDetail.getState().equals(DyylConstant.STATE_ON)) {
//                result = false;
//                msg = "失败原因：详情中数据不符合上架条件";
//                errorinfo = DateUtils.getTime();
//                break;
//            }
//        }
//
//        int count = 0;
//        //若为true 则符合上架条件
//        if (result) {
//            //判断上架详情中是否存在重复的RFID
//            OUT:
//            for (PutBillDetail putBillDetail : putBillDetailList) {
//                for (PutBillDetail pbd : putBillDetailList) {
//                    if (putBillDetail.getRfid().equals(pbd.getRfid())) {
//                        result = false;
//                        msg = "失败原因：此上架单中存在重复的RFID";
//                        errorinfo = DateUtils.getTime();
//                        break OUT;
//                    }
//                }
//            }
//            //若为True,则不存在重复的RFID
//            if (result) {
//                //遍历更改上架单详情的状态为已上架
//                for (PutBillDetail putBillDetail : putBillDetailList) {
//                    putBillDetail.setState(DyylConstant.STATE_ON);
//                    count = putBillDetailInterfaceDAO.updateById(putBillDetail);
//                    //更改成功则对库存/入库单/物料绑定RFID等数据更新
//                    if (count > 0) {
//                        instorageDetailInterfaceService.completePutBill(userId, putBillDetail.getId().toString());
//                    } else {
//                        result = false;
//                        msg = "失败原因：上架详情状态更新异常";
//                        errorinfo = DateUtils.getTime();
//                    }
//                }
//                //若为True,说明上架详情中所有单据状态已更新为已上架，则对上架单更新为上架完成状态
//                if (result) {
//                    putBill.setState(DyylConstant.STATE_COMPLETED);
//                    count = putBillInterfaceDAO.updateById(putBill);
//                    if (count < 0) {
//                        result = false;
//                        msg = "失败原因：上架状态更新异常";
//                        errorinfo = DateUtils.getTime();
//                    }
//                }
//            }
//        }
//        String interfacename = "调用上架确认接口";
//        String parameter = "userId:" + userId + "/putBillId:" + putBillId;
//
//        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
//
//        map.put("result", result);
//        map.put("msg", msg);
//        return map;
//    }

    /**
     * 上架详情参数更新
     *
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> completePutBill(Map<String, Object> paramMap) {

        boolean result = true;
        String msg = "上架确认成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;

        Long userId = Long.parseLong((String) paramMap.get("userId"));

        //获取上架单ID
        String putBillId = (String) paramMap.get("putBillId");

        //获取入库单ID
        String instorageBillId = (String) paramMap.get("instorageBillId");

        //获取库位编码判断库位是否冻结
        String positionCode = (String) paramMap.get("positionCode");


        //add by anss 2019-04-29 start
        //rfid
        String rfid = (String) paramMap.get("rfid");

        //校验库位是否可以上架(注：白糖类型的在判断库位时无法检验)
        Map<String,Object> map2 = putBillService.isAvailablePosition(rfid,positionCode);
        result = map2.get("result")==null?false:Boolean.parseBoolean(map2.get("result").toString());
        msg = map2.get("msg")==null?"":map2.get("msg").toString();
        if(!result){
            map.put("result", false);
            map.put("msg", msg);
            return map;
        }
        //add by anss 2019-04-29 end

        //获取物料绑定详情
        Gson gson = new Gson();
        List<MaterielBindRfidDetail> materielBindRfidDetailList = gson.fromJson(paramMap.get("data").toString(),
                new TypeToken<List<MaterielBindRfidDetail>>() {
                }.getType());

        String parameter = "PutBillDetailId:" + putBillId + "/instorageBillId:" + instorageBillId + "/userId:" + userId;
        if (materielBindRfidDetailList.isEmpty() && Strings.isNullOrEmpty(instorageBillId) && Strings.isNullOrEmpty(putBillId) && Strings.isNullOrEmpty(positionCode)) {
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：未获取参数", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未获取参数");
            return map;
        }

        DepotPosition dp = depotPositionInterfaceService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code", positionCode));

        if (dp == null) {
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：未获取库位信息", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未获取库位信息");
            return map;
        }

        if (dp.getPositionFrozen().equals(DyylConstant.FROZEN_1)) {
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：此库位已经冻结", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：此库位已经冻结");
            return map;
        }

        //验证库位重量容量和 库位rifd容量 是否超出
        Double materielWeight = 0.0;
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList){
            materielWeight = materielWeight + Double.parseDouble(materielBindRfidDetail.getWeight());
        }
        List<Stock> stockList = stockInterfaceService.selectList(new EntityWrapper<Stock>().eq("position_code",dp.getPositionCode()));
        Double stockWeight = 0.0;
        Double stcokRfidAmount = 1d;
        for (Stock stock : stockList){
            stockWeight = stockWeight + Double.parseDouble(stock.getStockWeight());
            stcokRfidAmount = stcokRfidAmount + Double.parseDouble(stock.getStockRfidAmount());
        }
        Double weight = materielWeight + stockWeight;
        if (Double.parseDouble(dp.getCapacityWeight())<weight || Double.parseDouble(dp.getCapacityRfidAmount())<stcokRfidAmount){
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：库位重量容量或库位托盘容量超出", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：库位重量容量或库位托盘容量超出");
            return map;
        }

        //获取上架单详情
        List<PutBillDetail> putBillDetailList = putBillDetailInterfaceDAO.selectList(
                new EntityWrapper<PutBillDetail>()
                        .eq("put_bill_id", putBillId)
        );
        //获取入库单详情
        List<InstorageDetail> instorageDetailList = instorageDetailInterfaceDAO.selectList(
                new EntityWrapper<InstorageDetail>()
                        .eq("instorage_bill_id", instorageBillId)
        );
        //跳出标记
        labe:
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            for (InstorageDetail instorageDetail : instorageDetailList) {
                //判断物料编码是否相同
                if (materielBindRfidDetail.getMaterielCode().equals(instorageDetail.getMaterialCode())) {
                    map = movePositionInterfaceService.isAvailablePosition(materielBindRfidDetail.getRfid(),dp.getPositionCode());

                    // update by anss 2019-04-29 start
                    if (!(boolean)map.get("result")){
                        msg = "库位不否符合条件";
                        result = false;
                        break;
                    }
                    // update by anss 2019-04-29 end

                    //判断相同物料，数量重量是否再可拆分范围
                    if (Double.valueOf(materielBindRfidDetail.getAmount()) > Double.valueOf(instorageDetail.getSeparableAmount()) ||
                            Double.valueOf(materielBindRfidDetail.getWeight()) > Double.valueOf(instorageDetail.getSeparableWeight())) {
                        interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, msg, DateUtils.getTime());
                        map.put("result", false);
                        map.put("msg", "失败原因：物料数量或重量超出可拆分范围");
                        return map;
                    }
                }
            }
        }

        //若result为真，说明没有触发跳出标记
        if (result) {
            for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                for (PutBillDetail putBillDetail : putBillDetailList) {
                    //相同物料插入上架详情重量和数量、库位，并添加物料绑定批次号,更新入库单可拆分物料数量重量
                    if (materielBindRfidDetail.getMaterielCode().equals(putBillDetail.getMaterialCode())) {
                        int count = 0;
                        boolean a = false;
                        PutBillDetail pd = new PutBillDetail();
                        if (Strings.isNullOrEmpty(putBillDetail.getRfid())) {
                            a = true;
                            putBillDetail.setPutAmount(materielBindRfidDetail.getAmount());
                            putBillDetail.setPutWeight(materielBindRfidDetail.getWeight());
                            if (materielBindRfidDetail.getMaterielBindRfidBy() != null) {
                                MaterielBindRfid mbr = materielBindRfidInterfaceDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
                                if (mbr != null) {
                                    putBillDetail.setRfid(mbr.getRfid());
                                }
                            }

                            if (materielBindRfidDetail.getPositionId() != null) {
                                DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                                if (depotPosition != null) {
                                    putBillDetail.setPositionCode(depotPosition.getPositionCode());
                                }
                            }

                            putBillDetail.setState(DyylConstant.STATE_UNTREATED);
                            count = putBillDetailInterfaceDAO.updateById(putBillDetail);
                        } else {
                            pd.setPutBillId(Long.parseLong(putBillId));
                            pd.setMaterialCode(materielBindRfidDetail.getMaterielCode());
                            pd.setMaterialName(materielBindRfidDetail.getMaterielName());
                            pd.setBatchNo(putBillDetail.getBatchNo());
                            pd.setUnit(materielBindRfidDetail.getUnit());
                            pd.setRfid(materielBindRfidDetail.getRfid());
                            pd.setPutAmount(materielBindRfidDetail.getAmount());
                            pd.setPutWeight(materielBindRfidDetail.getWeight());

                            if (materielBindRfidDetail.getPositionId() != null) {
                                DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                                if (depotPosition != null) {
                                    pd.setPositionCode(depotPosition.getPositionCode());
                                }
                            }
                            pd.setState(DyylConstant.STATE_UNTREATED);
                            count = putBillDetailInterfaceDAO.insert(pd);
                        }

                        if (count > 0) {
                            instorageDetailInterfaceService.completePutBill(userId, a == true ? putBillDetail.getId().toString() : pd.getId().toString());
                        } else {
                            result = false;
                            msg = "失败原因：上架单详情更新失败";
                            errorinfo = DateUtils.getTime();
                            String interfacename = "调用上架确认接口";
                            interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
                            map.put("result", result);
                            map.put("msg", msg);

                            return map;
                        }
                    }
                }
            }
            putBillDetailList = putBillDetailInterfaceDAO.selectList(
                    new EntityWrapper<PutBillDetail>()
                            .eq("put_bill_id", putBillId)
            );
            for (PutBillDetail putBillDetail : putBillDetailList) {
                if (putBillDetail.getState().equals(DyylConstant.STATE_PROCESSED)) {
                    //存在未入库  做标记为“0”
                    map.put("data", DyylConstant.STATE_PROCESSED);
                    break;
                } else {
                    //存在已入库  做标记为“1”
                    map.put("data", DyylConstant.STATE_UNTREATED);
                }
            }
        }

        /**更新物料绑定表的序号**/
        //依据库位编码查找库位id
        EntityWrapper<DepotPosition> wraDepotPosition = new EntityWrapper<>();
        wraDepotPosition.eq("position_code",positionCode);
        DepotPosition position = depotPositionService.selectOne(wraDepotPosition);
        EntityWrapper<MaterielBindRfid> wraMaterielBindRfid = new EntityWrapper<>();
        wraMaterielBindRfid.eq("position_by", position.getId());
        List<MaterielBindRfid> lstMaterielBindRfid = materielBindRfidService.selectList(wraMaterielBindRfid);
        wraMaterielBindRfid.eq("rfid", rfid);
        MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(wraMaterielBindRfid);
        materielBindRfid.setSort(lstMaterielBindRfid.size());
        materielBindRfidService.updateById(materielBindRfid);

        interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, msg, DateUtils.getTime());

        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 上架完成
     *
     * @param putBillId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> changePutBillState(Long putBillId,Long userId) {

        boolean result = true;
        String msg = "上架完成";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        if (putBillId != null) {
            PutBill putBill = putBillInterfaceDAO.selectById(putBillId);
            if (putBill != null) {
                putBill.setState(DyylConstant.STATE_COMPLETED);
                if (putBillInterfaceDAO.updateById(putBill) <= 0) {
                    result = false;
                    msg = "失败原因：上架单状态更新失败";
                    errorinfo = DateUtils.getTime();
                } else {
                    //获取已完成上架的上架单详情
                    List<PutBillDetail> putBillDetailList = putBillDetailService.selectList(
                            new EntityWrapper<PutBillDetail>()
                                    .eq("put_bill_id", putBillId)
                    );
                    for (PutBillDetail putBillDetail : putBillDetailList) {
                        StockChange stockChange = new StockChange();
                        stockChange.setMaterialCode(putBillDetail.getMaterialCode());
                        stockChange.setMaterialName(putBillDetail.getMaterialName());
                        stockChange.setChangeCode(putBill.getPutBillCode());
                        stockChange.setBatchNo(putBillDetail.getBatchNo());
                        stockChange.setBusinessType(DyylConstant.DEPOT_IN);
                        stockChange.setInAmount(putBillDetail.getPutAmount());
                        stockChange.setInWeight(putBillDetail.getPutWeight());
                        if (StringUtils.isNotEmpty(putBillDetail.getPositionCode())) {
                            DepotPosition depotPosition = depotPositionInterfaceService.selectOne(new EntityWrapper<DepotPosition>()
                                    .eq("position_code", putBillDetail.getPositionCode()));
                            if (depotPosition != null) {
                                stockChange.setPositionBy(depotPosition.getId());
                            }
                        }
                        stockChange.setCreateTime(DateUtils.getTime());
                        stockChange.setCreateBy(userId);
                        stockChangeService.insert(stockChange);
                    }
                }
            } else {
                result = false;
                msg = "失败原因：上架单未获取";
                errorinfo = DateUtils.getTime();
            }
        } else {
            result = false;
            msg = "失败原因：上架单ID为空";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用上架完成接口";
        String parameter = "putBillId:" + putBillId;
        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);


        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 上架单提交
     *
     * @param putBillId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> submitPutBill(Long putBillId) {

        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        String msg = "";
        PutBill putBill = putBillService.selectById(putBillId);

        //入库单id
        Long instorageBillId = putBill.getInstorageBillId();
        Instorage instorage = instorageInterfaceDAO.selectById(instorageBillId);
        //入库流程（0：一般流程；1：白糖流程）
        String instorageProcess = instorage.getInstorageProcess();


        //根据上架单id查询上架单详情
        EntityWrapper<PutBillDetail> entity = new EntityWrapper<PutBillDetail>();
        entity.eq("put_bill_id", putBillId);
        List<PutBillDetail> lstPutBillDetail = putBillDetailInterfaceDAO.selectList(entity);
        if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
            for (PutBillDetail putBillDetail : lstPutBillDetail) {
                //物料编号
                String materialCode = putBillDetail.getMaterialCode();
                //上架数量
                Double putAmount = putBillDetail.getPutAmount() == null || StringUtils.isBlank(putBillDetail.getPutAmount()) ? 0d : Double.parseDouble(putBillDetail.getPutAmount());
                //上架重量
                Double putWeight = putBillDetail.getPutWeight() == null || StringUtils.isBlank(putBillDetail.getPutWeight()) ? 0d : Double.parseDouble(putBillDetail.getPutWeight());
                //RFID
                String rfid = putBillDetail.getRfid();
                //库位编码
                String positionCode = putBillDetail.getPositionCode();
                if (StringUtils.isEmptyString(rfid)) {
                    result = false;
                    msg = "提交失败！请确认物料【" + materialCode + "】的RFID是否填写";
                    break;
                }
                if (StringUtils.isEmptyString(positionCode)) {
                    result = false;
                    msg = "提交失败！请确认物料【" + materialCode + "】的库位是否填写";
                    break;
                }

                //校验一个rfid不能放在多个库位上
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("putBillId", putBillId);
                paramMap.put("rfid", rfid);
                Map<String, Object> map3 = putBillDetailService.selectRfidInPosition(paramMap);
                if (map3 == null) {
                    result = false;
                    msg = "提交失败！请确认托盘【" + rfid + "】是否放在了多个的库位上";
                    break;
                }

                //一般流程的入库流程的单据需要验证数量和重量,都不可以为0
                if ("0".equals(instorageProcess)) {
                    //校验：1.rfid绑定的物料数量和重量是否与上架数量和重量一致；2.上架数量和重量是否小于等于入库单的可拆分数量和重量
                    Map<String, Object> map1 = putBillService.checkRfidAmountAndWeight(instorageBillId, rfid, materialCode, putAmount, putWeight);
                    result = map1.get("result") == null ? false : Boolean.parseBoolean(map1.get("result").toString());
                    msg = map1.get("msg") == null ? "" : map1.get("msg").toString();
                    if (!result) {
                        break;
                    }

                }
                //白糖流程的入库流程的单据需要验证数量和重量,必须为0
                if ("1".equals(instorageProcess)) {
                    if (putAmount != 0) {
                        result = false;
                        msg = "提交失败！该单据是白糖流程类型，确保物料数量都为0";
                        break;
                    }
                    if (putWeight != 0) {
                        result = false;
                        msg = "提交失败！该单据是白糖流程类型，确保物料重量都为0";
                        break;
                    }
                }

                //校验库位是否可以上架(注：白糖类型的在判断库位时无法检验)
                Map<String, Object> map2 = putBillService.isAvailablePosition(rfid, positionCode);
                result = map2.get("result") == null ? false : Boolean.parseBoolean(map2.get("result").toString());
                msg = map2.get("msg") == null ? "" : map2.get("msg").toString();
                if (!result) {
                    break;
                }
            }
        } else {
            result = false;
            msg = "提交失败！请填写物料信息";
        }

        if (result) {
            //获取上架单的状态
            String state = putBill.getState();
            if ("0".equals(state)) {
                try {
                    putBillService.submitPutBill(instorageBillId, putBillId, instorageProcess);
                    result = true;
                    msg = "提交成功！";
                } catch (Exception e) {
                    e.printStackTrace();
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


}
