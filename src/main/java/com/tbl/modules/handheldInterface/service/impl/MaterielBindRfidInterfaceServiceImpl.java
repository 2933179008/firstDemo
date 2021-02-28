package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.DepotPositionInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielInterfaceDAO;
import com.tbl.modules.handheldInterface.service.MaterielBindRfidInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料绑定RFID接口Service实现
 */
@Service(value = "materielBindRfidInterfaceService")
public class MaterielBindRfidInterfaceServiceImpl extends ServiceImpl<MaterielBindRfidInterfaceDAO, MaterielBindRfid> implements MaterielBindRfidInterfaceService {

    //日志接口DAO
    @Autowired
    private InterfaceLogService interfaceLogService;

    //物料绑定RFID管理Dao
    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    //物料绑定RFIDService
    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;

    //物料绑定RFID详情DAO
    @Autowired
    private MaterielBindRfidDetailInterfaceDAO materielBindRfidDetailInterfaceDAO;

    //物料接口DAO
    @Autowired
    private MaterielInterfaceDAO materielInterfaceDAO;

    //库位接口Dao
    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    /**
     * 根据rfid获取实体
     *
     * @param rfid
     * @return
     */
    @Override
    public MaterielBindRfid materielBindRfid(String rfid) {
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.materielBindRfid(rfid);
        return materielBindRfid;
    }

    /**
     * 获取绑定编号
     *
     * @return
     * @author yuany
     * @date 2019-01-11
     */
    @Override
    public String getBindCode() {

        //绑定编号
        String bindCode = null;

        //获取绑定表数据集合
        List<MaterielBindRfid> materielBindRfidList = this.selectList(
                new EntityWrapper<>()
        );

        //如果集合为长度为0则为第一条添加的数据
        if (materielBindRfidList.size() == 0) {
            bindCode = "BD0000001";
        } else {
            //获取集合中最后一条数据
            MaterielBindRfid materielBindRfid = materielBindRfidList.get(materielBindRfidList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Integer number = Integer.valueOf(materielBindRfid.getBindCode().substring(2)) + 1;
            //拼接字符串
            bindCode = "BD000000" + number.toString();
        }
        return bindCode;
    }

    /**
     * 根据RFID获取物料绑定中库位信息
     *
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> getDepotPositionByRfid(String rfid) {

        boolean result = true;
        String msg = "获取成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        DepotPosition depotPosition = null;
        if (!Strings.isNullOrEmpty(rfid)) {
            List<MaterielBindRfid> materielBindRfidList = materielBindRfidInterfaceDAO.selectList(
                    new EntityWrapper<MaterielBindRfid>()
                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                            .eq("status", DyylConstant.STATE_UNTREATED)
                            .eq("rfid", rfid)
            );

            if (!materielBindRfidList.isEmpty()) {
                if (materielBindRfidList.size() == 1) {
                    for (MaterielBindRfid materielBindRfid : materielBindRfidList) {
                        depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfid.getPositionBy());
                    }
                } else {
                    result = false;
                    msg = "失败原因：此RFID对应的物料绑定不唯一，请修改该绑定信息";
                    errorinfo = DateUtils.getTime();
                }
            } else {
                result = false;
                msg = "失败原因：未找到此RFID对应的物料绑定，无法获取库位信息";
                errorinfo = DateUtils.getTime();
            }
        } else {
            result = false;
            msg = "失败原因：未获取参数";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "调用根据RFID获取库位信息接口";
        String parameter = "Rfid:" + rfid;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", depotPosition);
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 物料绑定RFID
     *
     * @param rfid
     * @param barcode
     * @param amount
     * @param weight
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> doMaterielBindRfid(String rfid, String barcode, String amount, String weight, Long userId) {

        boolean result = true;
        String msg = "提交成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "Rfid:" + rfid + "/Barcode:" + barcode + "/Amount" + amount + "/Weight:" + weight + "/UserId" + userId;

        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(barcode)
                || Strings.isNullOrEmpty(amount) || Strings.isNullOrEmpty(weight) || Strings.isNullOrEmpty(userId.toString())) {
            map.put("result", false);
            map.put("msg", "失败原因：未获取参数");
            interfaceLogService.interfaceLogInsert("调用提交物料绑定RFID接口", parameter, "失败原因：未获取参数", DateUtils.getTime());
            return map;
        }
        //根据RFID获取物料绑定信息
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.materielBindRfid(rfid);
        //若为null则添加一条新的物料绑定RFID并添加此物料绑定的物料绑定详情，若不为NUll 则根据获取的物料绑定RFID信息添加物料绑定详情
        if (materielBindRfid == null) {
            materielBindRfid = new MaterielBindRfid();
            materielBindRfid.setBindCode(materielBindRfidInterfaceService.getBindCode());
            materielBindRfid.setRfid(rfid);
            materielBindRfid.setCreateTime(DateUtils.getTime());
            materielBindRfid.setCreateBy(userId);
            materielBindRfid.setDeletedFlag(DyylConstant.NOTDELETED);
            materielBindRfid.setStatus(DyylConstant.STATE_PROCESSED);
            materielBindRfid.setRemarks("手持机提交数据");
            materielBindRfid.setInstorageProcess(DyylConstant.INSTORAGEPROCESS0);
            //若添加成功 则添加此物料绑定的详情
            if (!materielBindRfidInterfaceService.insert(materielBindRfid)) {
                map.put("result", false);
                map.put("msg", "失败原因：物料绑定RFID信息添加失败");
                interfaceLogService.interfaceLogInsert("调用提交物料绑定RFID接口", parameter, "失败原因：物料绑定RFID信息添加失败", DateUtils.getTime());
                return map;
            }
            MaterielBindRfidDetail materielBindRfidDetail = new MaterielBindRfidDetail();
            List<Materiel> materielList = materielInterfaceDAO.selectList(
                    new EntityWrapper<Materiel>()
                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                            .eq("barcode", barcode)
            );
            if (materielList.size() != 1 || materielList.isEmpty()) {
                map.put("result", false);
                map.put("msg", "失败原因：物料条码不唯一");
                interfaceLogService.interfaceLogInsert("调用提交物料绑定RFID接口", parameter, "失败原因：物料条码不唯一", DateUtils.getTime());
                return map;
            }
            for (Materiel materiel : materielList) {
                materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
                materielBindRfidDetail.setMaterielCode(materiel.getMaterielCode());
                materielBindRfidDetail.setAmount(amount);
                materielBindRfidDetail.setWeight(weight);
                materielBindRfidDetail.setMaterielName(materiel.getMaterielName());
                materielBindRfidDetail.setUnit(materiel.getUnit());
                materielBindRfidDetail.setRfid(materielBindRfid.getRfid());
                materielBindRfidDetail.setStatus(materielBindRfid.getStatus());
                materielBindRfidDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                materielBindRfidDetailInterfaceDAO.insert(materielBindRfidDetail);
            }

        } else {
            //根据绑定物料ID获取对应的物料绑定详情集合
            List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("materiel_bind_rfid_by", materielBindRfid.getId())
            );
            //根据物料物料条码获取物料信息
            List<Materiel> materielList = materielInterfaceDAO.selectList(
                    new EntityWrapper<Materiel>()
                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                            .eq("barcode", barcode)
            );
            if (materielList.size() != 1 || materielList.isEmpty()) {
                map.put("result", false);
                map.put("msg", "失败原因：物料绑定RFID信息添加失败");
                interfaceLogService.interfaceLogInsert("调用提交物料绑定RFID接口", parameter, "失败原因：物料绑定RFID信息添加失败", DateUtils.getTime());
                return map;
            }
            for (Materiel materiel : materielList) {
                if (!materielBindRfidDetailList.isEmpty()) {
                    int i = 0;
                    for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                        if (materiel.getMaterielCode().equals(materielBindRfidDetail.getMaterielCode())) {
                            Double amt = Double.valueOf(materielBindRfidDetail.getAmount()) + Double.valueOf(amount);
                            Double wgt = Double.valueOf(materielBindRfidDetail.getWeight()) + Double.valueOf(weight);
                            materielBindRfidDetail.setAmount(amt.toString());
                            materielBindRfidDetail.setWeight(wgt.toString());
                            materielBindRfidDetailInterfaceDAO.updateById(materielBindRfidDetail);
                        } else {
                            i++;
                        }
                    }
                    if (i == materielBindRfidDetailList.size()) {
                        MaterielBindRfidDetail mbrd = new MaterielBindRfidDetail();
                        mbrd.setMaterielBindRfidBy(materielBindRfid.getId());
                        mbrd.setMaterielCode(materiel.getMaterielCode());
                        mbrd.setAmount(amount);
                        mbrd.setWeight(weight);
                        mbrd.setMaterielName(materiel.getMaterielName());
                        mbrd.setBatchRule(materiel.getBatchRule());
                        mbrd.setUnit(materiel.getUnit());
                        mbrd.setRfid(materielBindRfid.getRfid());
                        mbrd.setStatus(materielBindRfid.getStatus());
                        mbrd.setDeleteFlag(DyylConstant.NOTDELETED);
                        materielBindRfidDetailInterfaceDAO.insert(mbrd);
                    }
                } else {
                    MaterielBindRfidDetail materielBindRfidDetail = new MaterielBindRfidDetail();
                    materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
                    materielBindRfidDetail.setMaterielCode(materiel.getMaterielCode());
                    materielBindRfidDetail.setMaterielName(materiel.getMaterielName());
                    materielBindRfidDetail.setAmount(amount);
                    materielBindRfidDetail.setWeight(weight);
                    materielBindRfidDetail.setBatchRule(materiel.getBatchRule());
                    materielBindRfidDetail.setUnit(materiel.getUnit());
                    materielBindRfidDetail.setRfid(materielBindRfid.getRfid());
                    materielBindRfidDetail.setStatus(materielBindRfid.getStatus());
                    materielBindRfidDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                    materielBindRfidDetailInterfaceDAO.insert(materielBindRfidDetail);
                }
            }

        }

        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 根据RFID获取物料绑定详情
     *
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> getMaterielBindRfidDetailByRfid(String rfid) {

        boolean result = true;
        String msg = "获取库位信息成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<MaterielBindRfidDetail> materielBindRfidDetailList = null;
        if (!Strings.isNullOrEmpty(rfid)) {
            MaterielBindRfid materielBindRfid = materielBindRfidInterfaceService.materielBindRfid(rfid);
            if (materielBindRfid != null) {
                materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                );
                if (materielBindRfidDetailList.isEmpty()) {
                    result = false;
                    msg = "失败原因：此RFID绑定物料信息中无物料绑定详情";
                    errorinfo = DateUtils.getTime();
                } else {
                    for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                        if (materielBindRfidDetail.getPositionId() != null) {
                            DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                            if (depotPosition != null) {
                                materielBindRfidDetail.setPositionName(depotPosition.getPositionName());
                                materielBindRfidDetail.setPositionCode(depotPosition.getPositionCode());
                            }
                        }
                    }
                }
            } else {
                result = false;
                msg = "失败原因：RFID无对应的物料绑定信息";
                errorinfo = DateUtils.getTime();
            }
        } else {
            result = false;
            msg = "失败原因：参数获取失败";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用根据RFID获取物料绑定详情接口";
        String parameter = "RFID:" + rfid;
        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", materielBindRfidDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 判断是否是白糖绑定
     *
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> judgeSugar(String rfid) {

        boolean result = true;
        String msg = "查询成功";
        Map<String, Object> map = new HashMap<>();

        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.getMaterielBindRfidDetail(rfid);

        String errorinfo = null;
//        MaterielBindRfidDetail materielBindRfidDetail = null;
//        if (materielBindRfidDetailList.isEmpty()) {
//            result = false;
//            msg = "失败原因：未找到对应的物料绑定详情";
//            errorinfo = DateUtils.getTime();
//        } else if (materielBindRfidDetailList.size() > 1) {
//            result = false;
//            msg = "失败原因：条件查询物料绑定详情不唯一";
//            errorinfo = DateUtils.getTime();
//        } else {
//            for (MaterielBindRfidDetail mbrd : materielBindRfidDetailList) {
//                materielBindRfidDetail = mbrd;
//            }
//        }

        String interfacename = "调用判断是否是白糖绑定接口";
        String parameter = "Rfid:" + rfid;
        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", materielBindRfidDetailList);
        map.put("result", true);
        map.put("msg", "查询成功");

        return map;
    }

}
