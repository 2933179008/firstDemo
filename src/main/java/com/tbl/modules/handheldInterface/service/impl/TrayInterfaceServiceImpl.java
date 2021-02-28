package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.DepotPositionInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.TrayInterfaceDAO;
import com.tbl.modules.handheldInterface.service.*;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 托盘管理接口Service实现
 */
@Service(value = "trayInterfaceService")
public class TrayInterfaceServiceImpl extends ServiceImpl<TrayInterfaceDAO, Tray> implements TrayInterfaceService {

    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    @Autowired
    private StockInterfaceService stockInterfaceService;

    @Autowired
    private TrayInterfaceService trayInterfaceService;

    @Autowired
    private TrayDetailInterfaceService trayDetailInterfaceService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;

    @Autowired
    private MaterielBindRfidDetailInterfaceService materielBindRfidDetailInterfaceService;

    @Autowired
    private OutStorageService outStorageService;

    /**
     * 自动生成拆分/合并编码
     *
     * @return
     */
    @Override
    public String getMergeOrSplitCode(String type) {
        //合并/拆分编号
        String mergeOrSplitCode = null;

        //合并编号开头字符
        if (type.equals(DyylConstant.TYPE_MG)) {
            mergeOrSplitCode = DyylConstant.MEGRGE_CODE;
        } else if (type.equals(DyylConstant.TYPE_SL)) {
            //拆分编号开头字符
            mergeOrSplitCode = DyylConstant.SPLIT_CODE;
        }

        List<Tray> trayList = new ArrayList<>();
        //若编号不为空则根据编号开头字符获取集合
        if (StringUtils.isNotBlank(mergeOrSplitCode)) {
            //获取合并单数据集合
            trayList = this.selectList(
                    new EntityWrapper<Tray>()
                            .like("type", type)
                            .orderBy(true, "id")
            );
        }

        //如果集合为长度为0则为第一条添加的数据
        if (trayList.size() == 0) {
            mergeOrSplitCode = mergeOrSplitCode + 1;
        } else {
            //获取集合中最后一条数据
            Tray tray = trayList.get(trayList.size() - 1);
            //获取最后一条数据中拆分/合并编码并在数字基础上加1
            Integer number = Integer.parseInt(tray.getMergeOrSplitCode().substring(2)) + 1;
            //拼接字符串
            mergeOrSplitCode = mergeOrSplitCode + number.toString();
        }
        return mergeOrSplitCode;
    }

    /**
     * 拆分或合并
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> doSplitOrMerge(Map<String, Object> paramMap) {

        boolean result = true;
        String msg = "提交成功";
        Map<String, Object> map = new HashMap<>();

        String strUserId = (String) paramMap.get("userId");
        String oldRfid = (String) paramMap.get("oldRfid");
        String newRfid = (String) paramMap.get("newRfid");

        String parameter = "UserId:" + strUserId + "/OldRfid:" + oldRfid + "/NewRfid:" + newRfid + "/" + "/List + materielBindRfidDetailList";
        if (Strings.isNullOrEmpty(strUserId) || Strings.isNullOrEmpty(oldRfid) || Strings.isNullOrEmpty(newRfid)) {

            msg = "失败原因：strUserId或oldRfid或newRfid至少有一个为空！";
            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }


        Long userId = Long.valueOf(strUserId);

        Gson gson = new Gson();
        List<MaterielBindRfidDetail> materielBindRfidDetailList = gson.fromJson(paramMap.get("data").toString(),
                new TypeToken<List<MaterielBindRfidDetail>>() {
                }.getType());

        if (materielBindRfidDetailList.isEmpty()) {
            msg = "失败原因：未获取物料详情数据";
            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }
        //获取拆分数据
        MaterielBindRfidDetail mbrdData = materielBindRfidDetailList.get(materielBindRfidDetailList.size() - 1);

        //通过被拆分/合并的RFID获取物料绑定信息
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceService.materielBindRfid(oldRfid);
        if (materielBindRfid == null || materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED)) {
            msg = "失败原因：根据被拆分/合并的RFID未找到绑定信息或RFID绑定未入库";
            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }

        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailInterfaceService.selectOne(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                        .eq("materiel_code", mbrdData.getMaterielCode())
                        .eq("batch_rule", mbrdData.getBatchRule())
        );

        //获取库位信息
        DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfid.getPositionBy());
        if (depotPosition == null) {
            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, "失败原因：库位为空或库位类型为可混放或库位已冻结 ", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：库位为空或库位类型为可混放或库位已冻结 ");
            return map;
        }

        //通过拆分/合并后的RFID获取绑定信息
        MaterielBindRfid mbr = materielBindRfidInterfaceService.materielBindRfid(newRfid);

        //若newRfid获取信息为NUll，则为拆分
        if (mbr == null) {
            //判断库位托盘容量是否满足条件
            List<Stock> stockList = stockInterfaceService.selectList(new EntityWrapper<Stock>().eq("position_code", depotPosition.getPositionCode()));
            Double stockRfidAmount = 0d;
            for (Stock stock : stockList) {
                if (stockRfidAmount == 0) {
                    stockRfidAmount = Double.parseDouble(stock.getStockRfidAmount());
                } else {
                    stockRfidAmount = stockRfidAmount + Double.parseDouble(stock.getStockRfidAmount());
                }
            }
            if (Double.parseDouble(depotPosition.getCapacityRfidAmount()) - stockRfidAmount < 1) {
                interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, "失败原因：库位可用托盘容量不足，不可拆分 ", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：库位可用托盘容量不足，不可拆分 ");
                return map;
            }
        }

        //合并   ——  单物料限制
//        else {
//            //若为合并 则物料绑定详情不可为多个
//            List<MaterielBindRfidDetail> materielBindRfidDetailList1 = materielBindRfidDetailInterfaceService.selectList(
//                    new EntityWrapper<MaterielBindRfidDetail>()
//                            .eq("delete_flag", DyylConstant.NOTDELETED)
//                            .eq("materiel_bind_rfid_by", materielBindRfid.getId())
//            );
//            if (materielBindRfidDetailList1.size() != 1) {
//                msg = "失败原因：被合并物料绑定详情不唯一或为空 ";
//                interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());
//
//                map.put("result", false);
//                map.put("msg", msg);
//                return map;
//            }
//        }

        //获取三个条件可确定的唯一库存
        Stock stock = stockInterfaceService.selectOne(
                new EntityWrapper<Stock>()
                        .eq("position_code", depotPosition.getPositionCode())
                        .eq("material_code", materielBindRfidDetail.getMaterielCode())
                        .eq("batch_no", materielBindRfidDetail.getBatchRule())
                        .eq("material_type", DyylConstant.MATERIAL_RFID)
        );

        if (stock == null) {
            msg = "失败原因：库存查询为空 ";
            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }

        //判断可拆分/合并的范围
//        if (Double.valueOf(mbrdData.getAmount()) > Double.valueOf(materielBindRfidDetail.getAmount())
//                || Double.valueOf(mbrdData.getWeight()) > Double.valueOf(materielBindRfidDetail.getWeight())) {
//            msg = "失败原因：数量或重量超出可拆分/合并范围 ";
//            interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());
//
//            map.put("result", false);
//            map.put("msg", msg);
//            return map;
//        }

        //判断是否存在出库占用，存在则判断已占用数量(重量)+拆分数量（重量）是否在物料数量（重量）范围内，若不存在，则判断拆分数量（重量）是否在物料数量（重量）范围内
        if(Strings.isNullOrEmpty(materielBindRfidDetail.getOutstorageBillCode())){
            //若拆分出的数量大于原本物料的数量，或拆分的重量大于原本物料重量，则返回失败原因
            if (Double.valueOf(mbrdData.getAmount()) > Double.valueOf(materielBindRfidDetail.getAmount())
                    || Double.valueOf(mbrdData.getWeight()) > Double.valueOf(materielBindRfidDetail.getWeight())) {
                msg = "失败原因：数量或重量超出可拆分/合并范围 ";
                interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

                map.put("result", false);
                map.put("msg", msg);
                return map;
            }
        }else {
            //存在出库占用，先判断是否为备料单生成的出库单。
            //获取出库单
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectOne(new EntityWrapper<OutStorageManagerVO>().eq("outstorage_bill_code",materielBindRfidDetail.getOutstorageBillCode()));
            //若出库单与备料单不存在绑定关系，则判断拆分数量是否满足条件
            if (Strings.isNullOrEmpty(outStorageManagerVO.getSpareBillCode())){
                Double trayAmount = Double.parseDouble(materielBindRfidDetail.getOccupyStockAmount()) + Double.parseDouble(mbrdData.getAmount());
                Double trayWeight = Double.parseDouble(materielBindRfidDetail.getOccupyStockWeight()) + Double.parseDouble(mbrdData.getWeight());
                if (Double.parseDouble(materielBindRfidDetail.getAmount()) < trayAmount || Double.parseDouble(materielBindRfidDetail.getWeight()) < trayWeight){
                    Double num = Double.parseDouble(materielBindRfidDetail.getWeight()) - Double.parseDouble(materielBindRfidDetail.getOccupyStockWeight());
                    map.put("msg", "失败原因：此物料存在出库单占用，输入重量/数量超出可拆分范围！最多可拆出【"+num+"】，请重新输入");
                    map.put("result", false);
                    return map;
                }
            }else {
                map.put("msg", "此物料已被出库单全部占用，不可进行拆分/合并操作");
                map.put("result", false);
                return map;
            }

        }

        //计算拆分后，被拆分托盘剩余物料的重量和数量
        Double amount = Double.valueOf(materielBindRfidDetail.getAmount()) - Double.valueOf(mbrdData.getAmount());
        Double weight = Double.valueOf(materielBindRfidDetail.getWeight()) - Double.valueOf(mbrdData.getWeight());

        //若为0，则删除被拆分的物料绑定及详情和库存信息，否则更新
        if (amount == 0 && weight == 0) {
            materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
            materielBindRfidDetail.setDeleteBy(userId);
            materielBindRfidDetailInterfaceService.updateById(materielBindRfidDetail);

            //更新库存中关于RFID的数据
            result = stockInterfaceService.changeStrok(stock, oldRfid);
            //若库存更新失败则返回失败信息
            if (!result) {
                msg = "失败原因：库存更新失败 ";
                interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

                map.put("result", false);
                map.put("msg", msg);
                return map;
            }

            List<MaterielBindRfidDetail> materielBindRfidDetailList1 = materielBindRfidDetailInterfaceService.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("materiel_bind_rfid_by", materielBindRfid.getId())
            );
            if (materielBindRfidDetailList1.size()==0){
                materielBindRfid.setDeletedFlag(DyylConstant.DELETED);
                materielBindRfid.setDeletedBy(userId);
                materielBindRfidInterfaceService.updateById(materielBindRfid);
            }
        } else {
            materielBindRfidDetail.setWeight(weight.toString());
            materielBindRfidDetail.setAmount(amount.toString());
            materielBindRfidDetailInterfaceService.updateById(materielBindRfidDetail);
        }

        //创建托盘记录
        Tray tray = new Tray();
        //获取合并newRfid物料详情
        MaterielBindRfidDetail mbrd = null;
        //若拆分/合并后RFID为空 则新建 否则更新
        if (mbr != null) {
            tray.setType(DyylConstant.TYPE_MG);
            tray.setMergeOrSplitCode(trayInterfaceService.getMergeOrSplitCode(DyylConstant.TYPE_MG));
            //Long类型比较value值是否想等 除用.longValue()获取进行比较外 还可用 →方法 0 != Long.compare(Long x,Long y)
            if (mbr.getStatus().equals(DyylConstant.STATE_PROCESSED) || mbr.getPositionBy().longValue() != depotPosition.getId().longValue()) {
                msg = "失败原因：未入库或不为同库位 ";
                interfaceLogService.interfaceLogInsert("调用拆分或合接口", parameter, msg, DateUtils.getTime());

                map.put("result", false);
                map.put("msg", msg);
                return map;
            }
            mbrd = materielBindRfidDetailInterfaceService.selectOne(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("materiel_bind_rfid_by", mbr.getId())
                            .eq("materiel_code", mbrdData.getMaterielCode())
                            .eq("batch_rule", mbrdData.getBatchRule())
            );
            //若详情为空则新建 否则更新
            if (mbrd == null) {
                mbrd = new MaterielBindRfidDetail();
                mbrd.setMaterielBindRfidBy(mbr.getId());
                mbrd.setMaterielCode(materielBindRfidDetail.getMaterielCode());
                mbrd.setMaterielName(materielBindRfidDetail.getMaterielName());
                mbrd.setBatchRule(materielBindRfidDetail.getBatchRule());
                mbrd.setUnit(materielBindRfidDetail.getUnit());
                mbrd.setWeight(mbrdData.getWeight());
                mbrd.setAmount(mbrdData.getAmount());
                mbrd.setDeleteFlag(DyylConstant.NOTDELETED);
                mbrd.setRfid(newRfid);
                mbrd.setPositionId(depotPosition.getId());
                mbrd.setStatus(DyylConstant.STATE_UNTREATED);
                materielBindRfidDetailInterfaceService.insert(mbrd);
                //更新库存
                String rfid = null;
                String avilRfid = null;
                if (Strings.isNullOrEmpty(stock.getRfid())) {
                    rfid = newRfid;
                } else {
                    rfid = stock.getRfid() + "," + newRfid;
                }
                if (Strings.isNullOrEmpty(stock.getAvailableRfid())) {
                    avilRfid = newRfid;
                } else {
                    avilRfid = stock.getAvailableRfid() + "," + newRfid;
                }
                Double rfidAmount = Double.valueOf(stock.getStockRfidAmount()) + 1;
                Double avilRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) + 1;
                stock.setRfid(rfid);
                stock.setAvailableRfid(avilRfid);
                stock.setStockRfidAmount(rfidAmount.toString());
                stock.setAvailableStockRfidAmount(avilRfidAmount.toString());
                result = stockInterfaceService.updateById(stock);

            } else {
                Double mbrdWight = Double.valueOf(mbrd.getWeight()) + Double.valueOf(mbrdData.getWeight());
                Double mbrdAmount = Double.valueOf(mbrd.getAmount()) + Double.valueOf(mbrdData.getAmount());
                mbrd.setWeight(mbrdWight.toString());
                mbrd.setAmount(mbrdAmount.toString());
                materielBindRfidDetailInterfaceService.updateById(mbrd);
            }
        } else {
            tray.setType(DyylConstant.TYPE_SL);
            tray.setMergeOrSplitCode(trayInterfaceService.getMergeOrSplitCode(DyylConstant.TYPE_SL));
            mbr = new MaterielBindRfid();
            mbr.setBindCode(materielBindRfidInterfaceService.getBindCode());
            mbr.setRfid(newRfid);
            mbr.setCreateTime(DateUtils.getTime());
            mbr.setCreateBy(userId);
            mbr.setDeletedFlag(DyylConstant.NOTDELETED);
            mbr.setPositionBy(materielBindRfid.getPositionBy());
            mbr.setStatus(materielBindRfid.getStatus());
            mbr.setRemarks("手持机拆分/合并产生数据");
            mbr.setInstorageProcess(materielBindRfid.getInstorageProcess());
            materielBindRfidInterfaceService.insert(mbr);
            mbrd = new MaterielBindRfidDetail();
            mbrd.setMaterielBindRfidBy(mbr.getId());
            mbrd.setMaterielCode(materielBindRfidDetail.getMaterielCode());
            mbrd.setMaterielName(materielBindRfidDetail.getMaterielName());
            mbrd.setBatchRule(materielBindRfidDetail.getBatchRule());
            mbrd.setUnit(materielBindRfidDetail.getUnit());
            mbrd.setWeight(mbrdData.getWeight());
            mbrd.setAmount(mbrdData.getAmount());
            mbrd.setDeleteFlag(DyylConstant.NOTDELETED);
            mbrd.setRfid(newRfid);
            mbrd.setPositionId(depotPosition.getId());
            mbrd.setStatus(DyylConstant.STATE_UNTREATED);
            materielBindRfidDetailInterfaceService.insert(mbrd);
            //更新库存
            String rfid = null;
            String avilRfid = null;
            if (Strings.isNullOrEmpty(stock.getRfid())) {
                rfid = newRfid;
            } else {
                rfid = stock.getRfid() + "," + newRfid;
            }
            if (Strings.isNullOrEmpty(stock.getAvailableRfid())) {
                avilRfid = newRfid;
            } else {
                avilRfid = stock.getAvailableRfid() + "," + newRfid;
            }
            Double rfidAmount = Double.valueOf(stock.getStockRfidAmount()) + 1;
            Double avilRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) + 1;
            stock.setRfid(rfid);
            stock.setAvailableRfid(avilRfid);
            stock.setStockRfidAmount(rfidAmount.toString());
            stock.setAvailableStockRfidAmount(avilRfidAmount.toString());
            result = stockInterfaceService.updateById(stock);

        }
        tray.setRfid(newRfid);
        tray.setPositionBy(depotPosition.getId());
        tray.setCreateTime(DateUtils.getTime());
        tray.setCreateBy(userId);
        tray.setDeleteFlag(DyylConstant.NOTDELETED);
        tray.setStatus(DyylConstant.STATE_UNTREATED);
        tray.setRemarks("手持机产生数据");
        trayInterfaceService.insert(tray);
        TrayDetail trayDetail = new TrayDetail();
        trayDetail.setTrayBy(tray.getId());
        trayDetail.setMbrDetailBy(mbrd.getId());
        trayDetail.setAmount(mbrdData.getAmount());
        trayDetail.setWeight(mbrdData.getWeight());
        trayDetail.setDeleteFlag(DyylConstant.NOTDELETED);
        trayDetail.setMaterielCode(mbrd.getMaterielCode());
        trayDetail.setBatchRule(mbrd.getBatchRule());
        trayDetailInterfaceService.insert(trayDetail);

        if (!result) {
            msg = "失败原因：库存数据更新失败";
        }

        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

}
