package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.dao.TrayDetailDAO;
import com.tbl.modules.stock.entity.*;
import com.tbl.modules.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 托盘管理详情Service实现
 *
 * @author yuany
 * @date 2019-01-18
 */
@Service("trayDetailService")
public class TrayDetailServiceImpl extends ServiceImpl<TrayDetailDAO, TrayDetail> implements TrayDetailService {

    /**
     * 物料绑定详情Dao
     */
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDao;

    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    @Autowired
    private TrayService trayService;

    @Autowired
    private DepotPositionService depotPositionService;

    @Autowired
    private StockService stockService;

    @Autowired
    private TrayDetailService trayDetailService;

    @Autowired
    private OutStorageService outStorageService;

    /**
     * 获取托盘管理详情分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-18
     */
    @Override
    public PageUtils queryTrayDetailPage(Map<String, Object> parms) {

        //托盘管理 主键id
        String trayBy = (String) parms.get("trayBy");

        Page<TrayDetail> trayDetailPage = new Page<TrayDetail>();

        //如果materielBindRfidBy不为空分页查询获取的数据
        if (StringUtils.isNotBlank(trayBy)) {
            trayDetailPage = this.selectPage(
                    new Query<TrayDetail>(parms).getPage(),
                    new EntityWrapper<TrayDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("tray_by", trayBy)

            );
        }

        for (TrayDetail trayDetail : trayDetailPage.getRecords()) {
            if (trayDetail.getMbrDetailBy() != null) {
                MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailDao.selectById(trayDetail.getMbrDetailBy());
                if (null != materielBindRfidDetail) {
                    trayDetail.setMaterielCode(materielBindRfidDetail.getMaterielCode());
                    trayDetail.setMaterielName(materielBindRfidDetail.getMaterielName());
                    trayDetail.setUnit(materielBindRfidDetail.getUnit());
                }
            }
        }
        return new PageUtils(trayDetailPage);
    }

    @Override
    public Map<String, Object> updateAmountAndWeight(String mlWeight, Long id, Long trayBy, Long userId, String rfidSelect) {

        Map<String, Object> map = new HashMap<>();
        String msg = "操作成功";

        //获取托盘信息
        Tray tray = trayService.selectById(trayBy);
        if (tray == null) {
            map.put("msg", "失败原因：未获取托盘信息");
            map.put("result", false);
            return map;
        }

        //判断合并时，两个RFID是否为同一个，如果相同。则提示同一RFID不能做合并
        if(tray.getType().equals(DyylConstant.MERGE)&&tray.getRfid().equals(rfidSelect)) {
            map.put("msg", "失败原因：不能对同一RFID进行合并操作");
            map.put("result", false);
            return map;
        }

        //ID获取绑定详情
        MaterielBindRfidDetail mbrd = materielBindRfidDetailService.selectById(id);
        if (mbrd == null) {
            map.put("msg", "失败原因：未获取相关物料绑定详情");
            map.put("result", false);
            return map;
        }
//        List<MaterielBindRfidDetail> mbrd2 = materielBindRfidDetailService.selectList(
//                new EntityWrapper<MaterielBindRfidDetail>()
//                        .eq("delete_flag", DyylConstant.NOTDELETED)
//                        .eq("materiel_bind_rfid_by", mbrd.getMaterielBindRfidBy())
//        );
//        if (mbrd2.size() != 1) {
//            map.put("msg", "失败原因 物料绑定详情不唯一");
//            map.put("result", false);
//            return map;
//        }

        //获取绑定详情中物料数量
        String number = mbrd.getNumber();
        if (Strings.isNullOrEmpty(number)) {
            map.put("msg", "失败原因：请先输入拆分数量");
            map.put("result", false);
            return map;
        }

        //获取关于此id的数量、重量信息
        String amount = mbrd.getAmount();
        String weight = mbrd.getWeight();

        //判断是否存在出库占用，存在则判断已占用数量(重量)+拆分数量（重量）是否在物料数量（重量）范围内，若不存在，则判断拆分数量（重量）是否在物料数量（重量）范围内
        if(Strings.isNullOrEmpty(mbrd.getOutstorageBillCode())){
            //若拆分出的数量大于原本物料的数量，或拆分的重量大于原本物料重量，则返回失败原因
            if (Double.parseDouble(number) > Double.parseDouble(amount) || Double.parseDouble(mlWeight) > Double.parseDouble(weight)) {
                map.put("msg", "失败原因：拆分数量或重量超出可拆分范围");
                map.put("result", false);
                return map;
            }
        }else {
            //存在出库占用，先判断是否为备料单生成的出库单。
            //获取出库单
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectOne(new EntityWrapper<OutStorageManagerVO>().eq("outstorage_bill_code",mbrd.getOutstorageBillCode()));
            //若出库单与备料单不存在绑定关系，则判断拆分数量是否满足条件
            if (Strings.isNullOrEmpty(outStorageManagerVO.getSpareBillCode())){
                Double trayAmount = Double.parseDouble(mbrd.getOccupyStockAmount()) + Double.parseDouble(number);
                Double trayWeight = Double.parseDouble(mbrd.getOccupyStockWeight()) + Double.parseDouble(mlWeight);
                if (Double.parseDouble(amount) < trayAmount || Double.parseDouble(weight) < trayWeight){
                    Double num = Double.parseDouble(weight) - Double.parseDouble(mbrd.getOccupyStockWeight());
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

        //若拆分出的数量大于原本物料的数量，或拆分的重量大于原本物料重量，则返回失败原因
//        if (Double.parseDouble(number) > Double.parseDouble(amount) || Double.parseDouble(mlWeight) > Double.parseDouble(weight)) {
//            map.put("msg", "失败原因：拆分数量或重量超出可拆分范围");
//            map.put("result", false);
//            return map;
//        }

        //通过物料绑定详情中物料绑定ID获取绑定信息
        MaterielBindRfid mbr = materielBindRfidService.selectById(mbrd.getMaterielBindRfidBy());
        if (mbr == null) {
            map.put("msg", "失败原因：未获取绑定详情对应的物料绑定信息");
            map.put("result", false);
            return map;
        }

        //获取库位信息
        DepotPosition depotPosition = depotPositionService.selectById(mbr.getPositionBy());
        if (depotPosition == null) {
            map.put("msg", "失败原因：未获取库位信息");
            map.put("result", false);
            return map;
        }

        //根据托盘管理所需操作的RFID获取唯一物料绑定数据
        MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("rfid", tray.getRfid())
        );

        //依据库位编码，物料编码，批次号，物料类型确定一条库存记录
        Stock stock = stockService.selectOne(
                new EntityWrapper<Stock>()
                        .eq("position_code", depotPosition.getPositionCode())
                        .eq("material_code", mbrd.getMaterielCode())
                        .eq("batch_no", mbrd.getBatchRule())
                        .eq("material_type", DyylConstant.MATERIAL_RFID)
        );
        if (stock == null) {
            map.put("msg", "失败原因：未获取库存数据");
            map.put("result", false);
            return map;
        }

        //获取关于需要操作的物料绑定RFID的详情
        List<MaterielBindRfidDetail> lstmaterielBindRfidDetail = materielBindRfidDetailService.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("materiel_bind_rfid_by", mbrd.getMaterielBindRfidBy())
        );

        if (lstmaterielBindRfidDetail.size() > 0) {
            for (MaterielBindRfidDetail materielBindRfidDetail : lstmaterielBindRfidDetail) {
                //同物料不同批次的物料不能合并
                if (materielBindRfidDetail.getMaterielCode().equals(mbrd.getMaterielCode()) && !materielBindRfidDetail.getBatchRule().equals(mbrd.getBatchRule())) {
                    map.put("msg", "失败原因：同物料不同批次的物料不能合并");
                    map.put("result", false);
                    return map;
                }
            }
        }

        //添加拆分重量
        mbrd.setMlWeight(mlWeight);
        if (!materielBindRfidDetailService.updateById(mbrd)) {
            map.put("msg", "失败原因：添加拆分重量失败");
            map.put("result", false);
            return map;
        }

//        tray.setStatus(mbr.getStatus());
        tray.setPositionBy(mbr.getPositionBy());
        if (!trayService.updateById(tray)) {
            map.put("msg", "失败原因：更新托盘库位失败");
            map.put("result", false);
            return map;
        }

        //插入托盘管理详细的数据
        TrayDetail trayDetail = new TrayDetail();
        trayDetail.setTrayBy(trayBy);
        trayDetail.setBatchRule(stock.getBatchNo());
        trayDetail.setMaterielCode(stock.getMaterialCode());
        trayDetail.setMbrDetailBy(id);
        trayDetail.setDeleteFlag(DyylConstant.NOTDELETED);
        trayDetail.setAmount(number);
        trayDetail.setWeight(mlWeight);
        if (!trayDetailService.insert(trayDetail)) {
            map.put("msg", "失败原因：托盘详情数据操作失败");
            map.put("result", false);
            return map;
        }

        map.put("result", true);
        map.put("msg", msg);

        return map;
    }

    /**
     * 单据提交
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> submitTray(String id,Long userId) {
        Map<String,Object> map = new HashMap<>();

        if (Strings.isNullOrEmpty(id)){
            map.put("msg","未获取保存的拆分单据ID");
            map.put("result",false);
            return map;
        }

        //获取关于此id的拆分详情
        List<TrayDetail> trayDetailList = trayDetailService.selectList(
                new EntityWrapper<TrayDetail>()
                        .eq("tray_by",id)
                        .eq("delete_flag",DyylConstant.NOTDELETED)
        );
        if (trayDetailList.size()<=0){
            map.put("msg","请填写拆分物料的数量和重量后再提交");
            map.put("result",false);
            return map;
        }

        //判断是否可提交
        for (TrayDetail trayDetail : trayDetailList){
            //获取所要拆分的绑定详情
            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(trayDetail.getMbrDetailBy());
            if (materielBindRfidDetail == null){
                map.put("msg","获取【"+trayDetail.getMaterielCode()+":"+trayDetail.getMaterielName()+"】物料的绑定关系失败");
                map.put("result",false);
                return map;
            }
            MaterielBindRfid materielBindRfid = materielBindRfidService.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
            if (materielBindRfid == null){
                map.put("msg","获取物料的绑定关系失败");
                map.put("result",false);
                return map;
            }
        }

        Tray tray = trayService.selectById(id);
        MaterielBindRfid newMaterielBindRfid = materielBindRfidService.materielBindRfid(tray.getRfid());

        //更新数据
        for (TrayDetail trayDetail : trayDetailList){
            //获取所要拆分的绑定详情
            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(trayDetail.getMbrDetailBy());
            MaterielBindRfid materielBindRfid = materielBindRfidService.selectById(materielBindRfidDetail.getMaterielBindRfidBy());

            DepotPosition depotPosition = depotPositionService.selectById(materielBindRfid.getPositionBy());

            //依据库位编码，物料编码，批次号，物料类型确定一条库存记录
            Stock stock = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("position_code", depotPosition.getPositionCode())
                            .eq("material_code", materielBindRfidDetail.getMaterielCode())
                            .eq("batch_no", materielBindRfidDetail.getBatchRule())
                            .eq("material_type", DyylConstant.MATERIAL_RFID)
            );

            if (Double.parseDouble(trayDetail.getAmount()) < Double.parseDouble(materielBindRfidDetail.getAmount())
                    && Double.parseDouble(trayDetail.getWeight()) < Double.parseDouble(materielBindRfidDetail.getWeight())) {
                Double a = Double.parseDouble(materielBindRfidDetail.getAmount()) - Double.parseDouble(trayDetail.getAmount());
                Double b = Double.parseDouble(materielBindRfidDetail.getWeight()) - Double.parseDouble(trayDetail.getWeight());
                String surplusNumber = a.toString();
                String surplusWeight = b.toString();
                if (materielBindRfidDetailService.updateSurplus(surplusNumber, surplusWeight, trayDetail.getMbrDetailBy()) < 0) {
                    map.put("msg", "失败原因：更新详情数量重量失败");
                    map.put("result", false);
                    return map;
                }else {
                    trayDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                    trayDetailService.updateById(trayDetail);
                }
            } else if (Double.parseDouble(trayDetail.getAmount()) == Double.parseDouble(materielBindRfidDetail.getAmount())
                    && Double.parseDouble(trayDetail.getWeight()) == Double.parseDouble(materielBindRfidDetail.getWeight())) {
                if (materielBindRfidDetailService.updateSurplus(null, null, trayDetail.getMbrDetailBy()) < 0) {
                    map.put("msg", "失败原因：更新详情数量重量失败");
                    map.put("result", false);
                    return map;
                }
                materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
                materielBindRfidDetail.setDeleteBy(userId);
                if (!materielBindRfidDetailService.updateById(materielBindRfidDetail)) {
                    map.put("msg", "失败原因：物料绑定RFID详情数据操作异常1");
                    map.put("result", false);
                    return map;
                }else {
                    trayDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                    trayDetailService.updateById(trayDetail);
                }

                List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("delete_flag",DyylConstant.NOTDELETED)
                                .eq("materiel_bind_rfid_by",materielBindRfidDetail.getMaterielBindRfidBy())
                                .eq("status",DyylConstant.STATE_UNTREATED)
                );
                if (materielBindRfidDetailList.size()<=0){
                    materielBindRfid.setDeletedFlag(DyylConstant.DELETED);
                    materielBindRfidService.updateById(materielBindRfid);

                    //获取可用RFID 库存RFID集合
                    List<String> rfidList = Arrays.asList(stock.getRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                    List<String> availableRfidList = Arrays.asList(stock.getAvailableRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                    //若不为空则去除对应RFID
                    if (!rfidList.isEmpty() && !availableRfidList.isEmpty()) {
                        for (int i = 0; i < rfidList.size(); i++) {
                            if (rfidList.get(i).equals(materielBindRfid.getRfid())) {
                                Double stockRfidAmount = Double.valueOf(stock.getStockRfidAmount()) - 1;
                                stock.setStockRfidAmount(stockRfidAmount.toString());
                                rfidList.remove(i);
                            }
                        }

                        for (int i = 0; i < availableRfidList.size(); i++) {
                            if (availableRfidList.get(i).equals(materielBindRfid.getRfid())) {
                                Double availableStockRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) - 1;
                                stock.setAvailableStockRfidAmount(availableStockRfidAmount.toString());
                                availableRfidList.remove(i);
                            }
                        }

                        //去除已经删除的物料绑定表中RFID对应的库存rfid
                        String stockRfid = null;
                        String availableStockRfid = null;
                        for (String rfid : rfidList) {
                            if (Strings.isNullOrEmpty(stockRfid)) {
                                stockRfid = rfid;
                            } else {
                                stockRfid = stockRfid + "," + rfid;
                            }
                        }
                        for (String availableRfid : availableRfidList) {
                            if (Strings.isNullOrEmpty(availableStockRfid)) {
                                availableStockRfid = availableRfid;
                            } else {
                                availableStockRfid = availableStockRfid + "," + availableRfid;
                            }
                        }
                        stock.setAvailableRfid(availableStockRfid);
                        stock.setRfid(stockRfid);
                    }
                    if (!stockService.updateById(stock)) {
                        map.put("msg", "失败原因：库存数据操作异常1");
                        map.put("result", false);
                        return map;
                    }
                }
            }

            //若为空则插入新的绑定关系和绑定详细，否则在原有绑定关系中添加或者更改绑定详细
            if (newMaterielBindRfid == null) {
                newMaterielBindRfid = new MaterielBindRfid();
                newMaterielBindRfid.setBindCode(materielBindRfidService.getBindCode());
                newMaterielBindRfid.setRfid(tray.getRfid());
                newMaterielBindRfid.setCreateBy(userId);
                newMaterielBindRfid.setCreateTime(DateUtils.getTime());
                newMaterielBindRfid.setDeletedFlag(DyylConstant.NOTDELETED);
                newMaterielBindRfid.setPositionBy(tray.getPositionBy());
                newMaterielBindRfid.setStatus(materielBindRfid.getStatus());
                newMaterielBindRfid.setRemarks("合并/拆分产生数据");
                newMaterielBindRfid.setInstorageProcess(materielBindRfid.getInstorageProcess());
                if (!materielBindRfidService.insert(newMaterielBindRfid)) {
                    map.put("msg", "失败原因：物料绑定RFID数据操作异常2");
                    map.put("result", false);
                    return map;
                }
                MaterielBindRfidDetail newMaterielBindRfidDetail = new MaterielBindRfidDetail();
                newMaterielBindRfidDetail.setMaterielBindRfidBy(newMaterielBindRfid.getId());
                newMaterielBindRfidDetail.setMaterielCode(materielBindRfidDetail.getMaterielCode());
                newMaterielBindRfidDetail.setMaterielName(materielBindRfidDetail.getMaterielName());
                newMaterielBindRfidDetail.setUnit(materielBindRfidDetail.getUnit());
                newMaterielBindRfidDetail.setBatchRule(materielBindRfidDetail.getBatchRule());
                newMaterielBindRfidDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                newMaterielBindRfidDetail.setRfid(newMaterielBindRfid.getRfid());
                newMaterielBindRfidDetail.setPositionId(newMaterielBindRfid.getPositionBy());
                newMaterielBindRfidDetail.setAmount(trayDetail.getAmount());
                newMaterielBindRfidDetail.setStatus(materielBindRfidDetail.getStatus());
                newMaterielBindRfidDetail.setProductData(materielBindRfidDetail.getProductData());
                newMaterielBindRfidDetail.setWeight(trayDetail.getWeight());
                if (!materielBindRfidDetailService.insert(newMaterielBindRfidDetail)) {
                    map.put("msg", "失败原因：物料绑定RFID详情数据操作异常2");
                    map.put("result", false);
                    return map;
                }

                //若数据添加成功 则库存增加RFID
                Double stockRfidAmount = Double.valueOf(stock.getStockRfidAmount()) + 1;
                Double availableStockRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) + 1;
                stock.setStockRfidAmount(stockRfidAmount.toString());
                stock.setAvailableStockRfidAmount(availableStockRfidAmount.toString());
                String stockRfid = null;
                String availableStockRfid = null;
                if (!Strings.isNullOrEmpty(stock.getRfid()) && !Strings.isNullOrEmpty(stock.getAvailableRfid())) {
                    stockRfid = stock.getRfid() + "," + tray.getRfid();
                    availableStockRfid = stock.getAvailableRfid() + "," + tray.getRfid();
                } else {
                    stockRfid = tray.getRfid();
                    availableStockRfid = tray.getRfid();
                }
                stock.setAvailableRfid(availableStockRfid);
                stock.setRfid(stockRfid);

                if (!stockService.updateById(stock)) {
                    map.put("msg", "失败原因：库存数据操作异常2");
                    map.put("result", false);
                    return map;
                }

            } else {
                //获取关于需要操作的物料绑定RFID的详情
                List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                                .eq("materiel_bind_rfid_by", newMaterielBindRfid.getId())
                );

                int count = 0;
                for (MaterielBindRfidDetail newMaterielBindRfidDetail : materielBindRfidDetailList){
                    if (newMaterielBindRfidDetail.getMaterielCode().equals(materielBindRfidDetail.getMaterielCode())
                            && newMaterielBindRfidDetail.getBatchRule().equals(materielBindRfidDetail.getBatchRule())) {
                        Double am = Double.parseDouble(newMaterielBindRfidDetail.getAmount()) + Double.parseDouble(trayDetail.getAmount());
                        newMaterielBindRfidDetail.setAmount(am.toString());
                        Double we = Double.parseDouble(newMaterielBindRfidDetail.getWeight()) + Double.parseDouble(trayDetail.getWeight());
                        newMaterielBindRfidDetail.setWeight(we.toString());
                        if (!materielBindRfidDetailService.updateById(newMaterielBindRfidDetail)) {
                            map.put("msg", "失败原因：物料绑定RFID详情数据操作异常3");
                            map.put("result", false);
                            return map;
                        }
                        break;
                    } else {
                        count = count+1;
                        if (count == materielBindRfidDetailList.size()){
                            MaterielBindRfidDetail mbrDetail = new MaterielBindRfidDetail();
                            mbrDetail.setMaterielBindRfidBy(newMaterielBindRfid.getId());
                            mbrDetail.setMaterielCode(materielBindRfidDetail.getMaterielCode());
                            mbrDetail.setMaterielName(materielBindRfidDetail.getMaterielName());
                            mbrDetail.setUnit(materielBindRfidDetail.getUnit());
                            mbrDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                            mbrDetail.setRfid(tray.getRfid());
                            mbrDetail.setAmount(trayDetail.getAmount());
                            mbrDetail.setWeight(trayDetail.getWeight());
                            mbrDetail.setStatus(materielBindRfidDetail.getStatus());
                            mbrDetail.setProductData(materielBindRfidDetail.getProductData());
                            if (!materielBindRfidDetailService.insert(mbrDetail)) {
                                map.put("msg", "失败原因：物料绑定RFID详情数据操作异常4");
                                map.put("result", false);
                                return map;
                            }
                        }
                    }
                }
            }
        }

        //更新托盘状态
        tray.setDeleteFlag(DyylConstant.NOTDELETED);
        tray.setStatus(DyylConstant.STATE_UNTREATED);
        trayService.updateById(tray);

        map.put("msg","提交成功");
        map.put("result",tray);
        return map;
    }


}
