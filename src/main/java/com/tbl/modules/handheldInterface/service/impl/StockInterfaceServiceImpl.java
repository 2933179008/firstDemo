package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.*;
import com.tbl.modules.handheldInterface.service.DepotPositionInterfaceService;
import com.tbl.modules.handheldInterface.service.MaterielInterfaceService;
import com.tbl.modules.handheldInterface.service.StockInterfaceService;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.*;
import com.tbl.modules.stock.service.MaterielPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存接口Service实现
 */
@Service(value = "stockInterfaceService")
public class StockInterfaceServiceImpl extends ServiceImpl<StockInterfaceDAO, Stock> implements StockInterfaceService {

    //日志接口Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //库位接口DAO
    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    //库存接口DAO
    @Autowired
    private StockInterfaceDAO stockInterfaceDAO;

    //库存变动接口DAO
    @Autowired
    private StockChangeInterfaceDAO stockChangeInterfaceDAO;

    //物料接口DAO
    @Autowired
    private MaterielInterfaceDAO materielInterfaceDAO;

    //物料绑定接口DAO
    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    //物料绑定详情接口DAO
    @Autowired
    private MaterielBindRfidDetailInterfaceDAO materielBindRfidDetailInterfaceDAO;

    @Autowired
    private DepotPositionInterfaceService depotPositionService;

    @Autowired
    private StockInterfaceService stockService;

    @Autowired
    private MaterielInterfaceService materielInterfaceService;

    //入库详情Service
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private MaterielPowerService materielPowerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean changeStrok(Stock stock, String rfids) {

        List<String> rfidList = Arrays.asList(stock.getRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
        if (!Strings.isNullOrEmpty(stock.getAvailableRfid())) {
            List<String> availableRfidList = Arrays.asList(stock.getAvailableRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
            if (!rfidList.isEmpty() && !availableRfidList.isEmpty()) {
                for (int i = 0; i < rfidList.size(); i++) {
                    if (rfidList.get(i).equals(rfids)) {
                        Double stockRfidAmount = Double.valueOf(stock.getStockRfidAmount()) - 1;
                        stock.setStockRfidAmount(stockRfidAmount.toString());
                        rfidList.remove(i);
                    }
                }

                for (int i = 0; i < availableRfidList.size(); i++) {
                    if (availableRfidList.get(i).equals(rfids)) {
                        Double availableStockRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) - 1;
                        stock.setAvailableStockRfidAmount(availableStockRfidAmount.toString());
                        availableRfidList.remove(i);
                    }
                }

                //去除已经删除的物料绑定表中RFID对应的库存rfid
                String stockRfid = "";
                String availableStockRfid = "";
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
            } else {
                return false;
            }

        } else {
            return false;
        }

        return this.updateById(stock);
    }

    /**
     * 库存查询
     *
     * @param rfid
     * @param materielCode
     * @param positionCode
     * @param batchRule
     * @param barcode
     * @return
     */
    @Override
    public Map<String, Object> queryStock(String rfid, String materielCode, String positionCode, String batchRule, String barcode) {

        boolean result = true;
        String msg = "查询成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "RFID:" + rfid + "/" + "MaterielCode:" + materielCode + "/" + "PositionCode:" + positionCode + "/" + "BatchRule:" + batchRule;
        if (rfid.equals("null") && materielCode.equals("null") && positionCode.equals("null") && batchRule.equals("null") && barcode.equals("null")) {
            interfaceLogService.interfaceLogInsert("调用库存查询接口", parameter, "无查询条件", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "无查询条件");
            return map;
        }

        //若库位编码不为空则获取其对应的库位ID
        Long positionId = null;
        if (!positionCode.equals("null")) {
            DepotPosition depotPosition = depotPositionService.selectOne(
                    new EntityWrapper<DepotPosition>()
                            .eq("position_code", positionCode)
            );
            if (depotPosition == null) {
                interfaceLogService.interfaceLogInsert("调用库存查询接口", parameter, "库位编码未找到对应的唯一库位信息", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "库位编码未找到对应的库位信息");
                return map;
            }
            positionId = depotPosition.getId();
        }

        //若物料条码不为空且物料编码为空，则获取对应的物料编码/若物料编码不为空，则直接进行下一步查询
        if (!barcode.equals("null") && materielCode.equals("null")) {
            Materiel materiel = materielInterfaceService.selectOne(
                    new EntityWrapper<Materiel>().eq("barcode", barcode));
            if (materiel == null) {
                interfaceLogService.interfaceLogInsert("调用库存查询接口", parameter, "物料条码未找到对应的唯一物料", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "物料条码未找到对应的唯一物料");
                return map;
            }
            materielCode = materiel.getMaterielCode();
        }

        //条件查询绑定详情
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
                        .eq(positionId != null, "position_id", positionId)
                        .like(!rfid.equals("null"), "rfid", rfid)
                        .like(!batchRule.equals("null"), "batch_rule", batchRule)
                        .like(!materielCode.equals("null"), "materiel_code", materielCode)
        );

        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList){
            if (materielBindRfidDetail.getPositionId() != null){
                DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                if (depotPosition!=null){
                    materielBindRfidDetail.setPositionCode(depotPosition.getPositionCode());
                    materielBindRfidDetail.setPositionName(depotPosition.getPositionName());
                    materielBindRfidDetail.setPositionforzen(depotPosition.getPositionFrozen());
                }
            }
        }

        map.put("data",materielBindRfidDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 货权转移
     *
     * @param id
     * @param materielId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> getMaterielDroitShift(Long id, Long materielId, String batchNo, Long userId,String documentType) {

        boolean result = true;
        String msg = "转移成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "Id:" + id + "/MaterielId" + materielId + "/UserId" + userId;
        //判断是否获取参数
        if (id == null && materielId == null && userId == null && Strings.isNullOrEmpty(batchNo)) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：无参数", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：无参数");
            return map;
        }
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailInterfaceDAO.selectById(id);
        if (materielBindRfidDetail == null) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：物料绑定详情为空", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：无参数");
            return map;
        }
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
        Materiel materiel = materielInterfaceDAO.selectById(materielId);

        //判断参数是否获取到可用数据
        if (materielBindRfid == null || materiel == null) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：参数获取部分信息为空", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：参数获取部分信息为空");
            return map;
        }
        DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfid.getPositionBy());
        //已入库&&未被占用&&不可混放库位
        if (!materielBindRfid.getStatus().equals(DyylConstant.STATE_UNTREATED)
                || !Strings.isNullOrEmpty(materielBindRfidDetail.getOutstorageBillCode())
                || depotPosition == null || depotPosition.getBlendType().equals(DyylConstant.BLEND_TYPE0)) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：不符合货权转移条件", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：不符合货权转移条件（货权转移条件：已入库&&未被占用&&不可混放库位）");
            return map;
        }
        Stock stock = stockService.selectOne(
                new EntityWrapper<Stock>()
                        .eq("batch_no", materielBindRfidDetail.getBatchRule())
                        .eq("material_code", materielBindRfidDetail.getMaterielCode())
                        .eq("position_code", depotPosition.getPositionCode())
                        .eq("material_type", DyylConstant.MATERIAL_RFID)
        );

        //判断是否获取相应的库存数据,若未获取唯一的一条库存数据，则表明不存在此库存或有重复的错误库存
        if (stock == null) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：未找到对应库存", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未找到对应库存");
            return map;
        }

        if (Strings.isNullOrEmpty(stock.getAvailableStockAmount()) || Strings.isNullOrEmpty(stock.getAvailableStockWeight())) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：可用库存数量/重量为空", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：可用库存数量/重量为空");
            return map;
        }
        //判断数据条件是否符合，若符合，对库存进行数据更改操作
        if (Double.valueOf(stock.getAvailableStockAmount()) < Double.valueOf(materielBindRfidDetail.getAmount())
                || Double.valueOf(stock.getAvailableStockWeight()) < Double.valueOf(materielBindRfidDetail.getWeight())) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：转移物料库存不足", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：转移物料库存不足");
            return map;
        }
        Double amount = Double.valueOf(stock.getAvailableStockAmount()) - Double.valueOf(materielBindRfidDetail.getAmount());
        Double weight = Double.valueOf(stock.getAvailableStockWeight()) - Double.valueOf(materielBindRfidDetail.getWeight());
        Double stockAmount = Double.valueOf(stock.getStockAmount()) - Double.valueOf(materielBindRfidDetail.getAmount());
        Double stockWeight = Double.valueOf(stock.getStockWeight()) - Double.valueOf(materielBindRfidDetail.getWeight());
        stock.setStockAmount(stockAmount.toString());
        stock.setStockWeight(stockWeight.toString());
        stock.setAvailableStockAmount(amount.toString());
        stock.setAvailableStockWeight(weight.toString());
        stock.setMaterielPower("1");//已做货权转移标记
        String rfid = stock.getRfid();
        String aviRfid = stock.getAvailableRfid();
        List<String> lstRfids = Arrays.stream(rfid.split(",")).map(s -> s.trim()).collect(Collectors.toList());
        List<String> lstAviRfids = Arrays.stream(aviRfid.split(",")).map(s -> s.trim()).collect(Collectors.toList());
        for (int i = 0; i < lstRfids.size(); i++) {
            if (lstRfids.get(i).equals(materielBindRfid.getRfid())) {
                lstRfids.remove(i);
                Double RfidAmount = Double.valueOf(stock.getStockRfidAmount()) - 1;
                stock.setStockRfidAmount(RfidAmount.toString());
                rfid = null;
            }
        }
        for (int i = 0; i < lstAviRfids.size(); i++) {
            if (lstAviRfids.get(i).equals(materielBindRfid.getRfid())) {
                lstAviRfids.remove(i);
                Double aviRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) - 1;
                stock.setAvailableStockRfidAmount(aviRfidAmount.toString());
                aviRfid = null;
            }
        }
        if (Strings.isNullOrEmpty(rfid) && Strings.isNullOrEmpty(aviRfid)) {
            for (String rfids : lstRfids) {
                if (Strings.isNullOrEmpty(rfid)) {
                    rfid = rfids;
                } else {
                    rfid = rfid + "," + rfids;
                }
            }
            for (String aviRfids : lstAviRfids) {
                if (Strings.isNullOrEmpty(aviRfid)) {
                    aviRfid = aviRfids;
                } else {
                    aviRfid = aviRfid + "," + aviRfids;
                }
            }
            stock.setAvailableRfid(aviRfid);
            stock.setRfid(rfid);
        }
        int count = 0;
        //若库存操作成功，则对库存进行变更记录
        if (stockInterfaceDAO.updateById(stock) > 0) {
            if (stock.getStockAmount().equals("0.0") && stock.getStockRfidAmount().equals("0.0")) {
                stockInterfaceDAO.deleteById(stock);
            }
            StockChange stockChange = new StockChange();
            stockChange.setChangeCode(materielBindRfid.getBindCode());
            stockChange.setMaterialName(materielBindRfidDetail.getMaterielName());
            stockChange.setMaterialCode(materielBindRfidDetail.getMaterielCode());
            stockChange.setBatchNo(materielBindRfidDetail.getBatchRule());
            stockChange.setBusinessType(DyylConstant.DROIT_SHIFT_OUT);
            stockChange.setOutAmount(materielBindRfidDetail.getAmount());
            stockChange.setPositionBy(materielBindRfid.getPositionBy());
            stockChange.setCreateTime(DateUtils.getTime());
            stockChange.setCreateBy(userId);
            stockChangeInterfaceDAO.insert(stockChange);
            //并对物料详情数据进行更改
            materielBindRfidDetail.setMaterielCode(materiel.getMaterielCode());
            materielBindRfidDetail.setMaterielName(materiel.getMaterielName());
            materielBindRfidDetail.setBatchRule(batchNo);
            materielBindRfidDetail.setUnit(materiel.getUnit());
            count = materielBindRfidDetailInterfaceDAO.updateById(materielBindRfidDetail);
        }

        //若物料绑定详情数据更改成功，则以现在的物料详情对库存进行查询操作
        if (count < 0) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：物料绑定详情数据更新异常", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：物料绑定详情数据更新异常");
            return map;
        }
        //新增经过货权转移的库存数据
        Stock stock1 = new Stock();
        stock1.setMaterialType(DyylConstant.MATERIAL_RFID);
        stock1.setMaterialCode(materielBindRfidDetail.getMaterielCode());
        stock1.setMaterialName(materielBindRfidDetail.getMaterielName());
        stock1.setBatchNo(materielBindRfidDetail.getBatchRule());
        stock1.setPositionCode(depotPosition.getPositionCode());
        stock1.setPositionName(depotPosition.getPositionName());
        stock1.setStockAmount(materielBindRfidDetail.getAmount());
        stock1.setStockWeight(materielBindRfidDetail.getWeight());
        stock1.setAvailableStockWeight(materielBindRfidDetail.getWeight());
        stock1.setAvailableStockAmount(materielBindRfidDetail.getAmount());
        stock1.setStockRfidAmount("1");
        stock1.setAvailableStockRfidAmount("1");
        stock1.setAvailableRfid(materielBindRfid.getRfid());
        stock1.setRfid(materielBindRfid.getRfid());
        stock1.setAvailableRfid(materielBindRfid.getRfid());
        stock1.setCreateTime(DateUtils.getTime());
        stock1.setCreateBy(userId);

        stock1.setMaterialSource(stock.getId());//货权转移物料ID

        // add by anss 2019-04-30 start
        stock1.setCustomerCode(stock.getCustomerCode());
        //生产采购日期
        stock1.setProductDate(stock.getProductDate());
        // add by anss 2019-04-30 end

        count = stockInterfaceDAO.insert(stock1);

        if (count < 0) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：库存数据更新异常", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：库存数据更新异常");
            return map;
        }else {
            MaterielPower materielPower = new MaterielPower();
            materielPower.setStockId(stock1.getId());
            materielPower.setDocumentType(documentType);
            materielPowerService.insert(materielPower);
        }

        //插入库存变动记录
        StockChange stockChange = new StockChange();
        stockChange.setChangeCode(materielBindRfid.getBindCode());
        stockChange.setMaterialName(materielBindRfidDetail.getMaterielName());
        stockChange.setMaterialCode(materielBindRfidDetail.getMaterielCode());
        stockChange.setBatchNo(materielBindRfidDetail.getBatchRule());
        stockChange.setBusinessType(DyylConstant.DROIT_SHIFT_IN);
        stockChange.setInAmount(materielBindRfidDetail.getAmount());
        stockChange.setPositionBy(materielBindRfid.getPositionBy());
        stockChange.setCreateTime(DateUtils.getTime());
        stockChange.setCreateBy(userId);
        if (stockChangeInterfaceDAO.insert(stockChange) < 0) {
            interfaceLogService.interfaceLogInsert("调用货权转移接口", parameter, "失败原因：库存变动数据更新异常", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：库存变动数据更新异常");
            return map;
        }


        map.put("result", result);
        map.put("msg", msg);

        return map;
    }


}
