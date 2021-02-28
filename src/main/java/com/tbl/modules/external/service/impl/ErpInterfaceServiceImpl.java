package com.tbl.modules.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.external.dao.ErpInterfaceServiceDAO;
import com.tbl.modules.external.service.ErpInterfaceService;
import com.tbl.modules.external.service.ErpService;
import com.tbl.modules.instorage.dao.InstorageDetailDAO;
import com.tbl.modules.instorage.dao.ReceiptDAO;
import com.tbl.modules.instorage.dao.ReceiptDetailDAO;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.outstorage.dao.SpareBillDAO;
import com.tbl.modules.outstorage.dao.SpareBillDetailDAO;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ERP 入库接口
 *
 * @author pengzhuang
 * @date 2019-2-25
 */
@Service("erpInterfaceService")
public class ErpInterfaceServiceImpl implements ErpInterfaceService {

    //erp通用方法Service
    @Autowired
    private ErpService erpService;
    //erp入库接口DAO
    @Autowired
    private ErpInterfaceServiceDAO erpInterfaceServiceDAO;
    //收获计划DAO
    @Autowired
    private ReceiptDAO receiptDAO;
    //收获计划详情DAO
    @Autowired
    private ReceiptDetailDAO receiptDetailDAO;
    //入库详情DAO
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    //物料DAO
    @Autowired
    private MaterielDAO materielDAO;
    //库位DAO
    @Autowired
    private DepotPositionDAO depotPositionDAO;
    //备料单DAO
    @Autowired
    private SpareBillDAO spareBillDAO;
    //备料单详情DAO
    @Autowired
    private SpareBillDetailDAO spareBillDetailDAO;

    //采购入库单接口调用url
    @Value("${erp.POInstockURL}")
    private String POInstockURL;
    //受托加工入库单接口
    @Value("${erp.STJGInStockURL}")
    private String STJGInStockURL;
    //生产退库单及生产用料分摊接口(自采料)
    @Value("${erp.SCOutstockRedURL}")
    private String SCOutstockRedURL;
    //生产退库单及生产用料分摊接口(客料)
    @Value("${erp.STOutstockRed}")
    private String STOutstockRed;

    /**
     * 采购入库单接口调用
     *
     * @return
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void setPOInstock() {

        /**1.判断入库单是否满足调用接口条件 **/
        //如果不是越库类型的入库单，则在该入库单对应的上架单都上架完成并且该入库单是质检通过的，调用接口
        //如果是越库类型的入库单，则该入库单是提交状态

        //（1）查询满足调用采购入库单接口条件的入库单
        List<Map<String, Object>> list = erpInterfaceServiceDAO.selectConfessInstorageBill();
        JSONArray jsonArrayResult = new JSONArray();

        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                //入库单id
                String instorageBillId = map1.get("id") == null ? "" : map1.get("id").toString();
                //收货单id
                String receiptPlanId = map1.get("receipt_plan_id") == null ? "" : map1.get("receipt_plan_id").toString();
                //供应商编号
                String supplierCode = map1.get("supplier_code") == null ? "" : map1.get("supplier_code").toString();

                Receipt receipt = receiptDAO.selectById(receiptPlanId);
                //wms系统收货计划单号（wms采购订单编号）
                String wmsReceiptCode = receipt.getReceiptCode();
                //ERP系统采购订单编号
                String erpReceiptCode = receipt.getErpReceiptCode();

                JSONObject jsonObject = new JSONObject();
                //采购方式  可传空值
                jsonObject.put("FPOStyle", "");
                //供应商编号
                jsonObject.put("FSupplyID", supplierCode);
                //保管人
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询上架单详情
                List<Map<String, Object>> lstPutBillDetail = erpInterfaceServiceDAO.selectPutBillDetail(instorageBillId);
                if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
                    for (int i = 0; i < lstPutBillDetail.size(); i++) {
                        Map<String, Object> putBillDetailMap = lstPutBillDetail.get(i);
                        //物料编码
                        String materialCode = putBillDetailMap.get("material_code") == null ? "" : putBillDetailMap.get("material_code").toString();
                        //物料名称
                        String materialName = putBillDetailMap.get("material_name") == null ? "" : putBillDetailMap.get("material_name").toString();
                        //库位编号
                        String positionCode = putBillDetailMap.get("position_code") == null ? "" : putBillDetailMap.get("position_code").toString();
                        //上架数量
                        Double putAmount = putBillDetailMap.get("put_amount") == null ? 0d : Double.parseDouble(putBillDetailMap.get("put_amount").toString());
                        //批次号
                        String batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();
                        //根据库位编号获取库区编号
                        String areaCode = erpService.selectDepotAreaCode(positionCode);
                        //东洋库区编号转成erp库区编号
                        String erpAreaCode = erpService.changDepotPosition(areaCode);

                        //根据收货单id和物料编号查询收货计划单详情
                        ReceiptDetail receiptDetail = new ReceiptDetail();
                        receiptDetail.setReceiptPlanId(Long.parseLong(receiptPlanId));
                        receiptDetail.setMaterialCode(materialCode);
                        receiptDetail = receiptDetailDAO.selectOne(receiptDetail);
                        //erp传过来的行号
                        Long line = receiptDetail.getLine();

                        //根据入库单id和物料编号查询入库单详情
                        InstorageDetail instorageDetail = new InstorageDetail();
                        instorageDetail.setInstorageBillId(Long.parseLong(instorageBillId));
                        instorageDetail.setMaterialCode(materialCode);
                        instorageDetail = instorageDetailDAO.selectOne(instorageDetail);
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //入库数量
                        jsonObjectDetail.put("FQty", putAmount);
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", erpAreaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", positionCode);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", erpReceiptCode);
                        //源单分录(源单对应行号)
                        jsonObjectDetail.put("FSourceEntryID", line);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "采购订单");
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", wmsReceiptCode);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", line);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", "30");
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", "20190530");
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);

                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, POInstockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpInterfaceServiceDAO.updateInstorageBillErpFlag(list);
            }
        }
        /**2.判断入库单是否满足调用接口条件  越库类型**/
        List<Map<String, Object>> crossList = erpInterfaceServiceDAO.selectCrossInstorageBill();
        JSONArray JsonArrayResult = new JSONArray();

        if (crossList != null && crossList.size() > 0) {

            for (Map<String, Object> map1 : crossList) {
                //入库单id
                String instorageBillId = map1.get("id") == null ? "" : map1.get("id").toString();
                //收货单id
                String receiptPlanId = map1.get("receipt_plan_id") == null ? "" : map1.get("receipt_plan_id").toString();
                //供应商编号
                String supplierCode = map1.get("supplier_code") == null ? "" : map1.get("supplier_code").toString();

                Receipt receipt = receiptDAO.selectById(receiptPlanId);
                //wms系统收货计划单号（wms采购订单编号）
                String wmsReceiptCode = receipt.getReceiptCode();
                //ERP系统采购订单编号
                String erpReceiptCode = receipt.getErpReceiptCode();

                JSONObject jsonObject = new JSONObject();
                //采购方式  可传空值
                jsonObject.put("FPOStyle", "");
                //供应商编号
                jsonObject.put("FSupplyID", supplierCode);
                //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询入库单详情
                EntityWrapper<InstorageDetail> instorageDetailEntity = new EntityWrapper<>();
                instorageDetailEntity.eq("instorage_bill_id", instorageBillId);
                List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(instorageDetailEntity);
                if (lstInstorageDetail != null && lstInstorageDetail.size() > 0) {
                    for (int i = 0; i < lstInstorageDetail.size(); i++) {
                        InstorageDetail instorageDetail = lstInstorageDetail.get(i);
                        //物料编号
                        String materialCode = instorageDetail.getMaterialCode();
                        //物料名称
                        String materialName = instorageDetail.getMaterialName();
                        //入库数量
                        Double instorageAmount = StringUtils.isBlank(instorageDetail.getInstorageAmount()) ? 0d : Double.parseDouble(instorageDetail.getInstorageAmount());
                        //批次号
                        String batchNo = instorageDetail.getBatchNo();
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据收货单id和物料编号查询收货计划单详情
                        ReceiptDetail receiptDetail = new ReceiptDetail();
                        receiptDetail.setReceiptPlanId(Long.parseLong(receiptPlanId));
                        receiptDetail.setMaterialCode(materialCode);
                        receiptDetail = receiptDetailDAO.selectOne(receiptDetail);
                        //erp传过来的行号
                        Long line = receiptDetail.getLine();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //入库数量
                        jsonObjectDetail.put("FQty", instorageAmount);
                        //仓库编号(待定)
                        jsonObjectDetail.put("FDCStockID", "A.01");
                        //仓位编号（待定）
                        jsonObjectDetail.put("FDCSPID", "A01");
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", erpReceiptCode);
                        //源单分录(源单对应行号)
                        jsonObjectDetail.put("FSourceEntryID", line);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "采购订单");
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", wmsReceiptCode);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);

                        jsonArray.add(jsonObjectDetail);

                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                JsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(JsonArrayResult, POInstockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpInterfaceServiceDAO.updateInstorageBillErpFlag(crossList);
            }
        }
    }

    /**
     * 受托加工入库单接口
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    @Override
    public void setSTJGInStock() {
        /**1.判断入库单是否满足调用接口条件 非越库入库类型**/
        //（1）查询满足调用委托加工入库单接口条件的入库单
        List<Map<String, Object>> list = erpInterfaceServiceDAO.selectEntrustInstorageBill();
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                //入库单id
                String instorageBillId = map.get("id") == null ? "" : map.get("id").toString();
                //收货单id
                String receiptPlanId = map.get("receipt_plan_id") == null ? "" : map.get("receipt_plan_id").toString();
                //客户编号
                String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();

                Receipt receipt = receiptDAO.selectById(receiptPlanId);
                //wms系统收货计划单号
                String wmsReceiptCode = receipt.getReceiptCode();

                JSONObject jsonObject = new JSONObject();
                //购货单位（客户或者叫货主）
                jsonObject.put("FCustID", customerCode);
                //保管人
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询上架单详情
                List<Map<String, Object>> lstPutBillDetail = erpInterfaceServiceDAO.selectPutBillDetail(instorageBillId);
                if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
                    for (int i = 0; i < lstPutBillDetail.size(); i++) {
                        Map<String, Object> putBillDetailMap = lstPutBillDetail.get(i);
                        //物料编码
                        String materialCode = putBillDetailMap.get("material_code") == null ? "" : putBillDetailMap.get("material_code").toString();
                        //物料名称
                        String materialName = putBillDetailMap.get("material_name") == null ? "" : putBillDetailMap.get("material_name").toString();
                        //库位编号
                        String positionCode = putBillDetailMap.get("position_code") == null ? "" : putBillDetailMap.get("position_code").toString();
                        //上架数量
                        Double putAmount = putBillDetailMap.get("put_amount") == null ? 0d : Double.parseDouble(putBillDetailMap.get("put_amount").toString());
                        //批次号
                        String batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();
                        //依据库位编号获取库位名称
                        String positionName = depotPositionDAO.selectPositionName(positionCode);
                        //依据库区编码与库位名称查找库位编码
                        String erpPositionCode = erpService.selectPositionCode(positionName);
                        //根据库位编号与客供字段获取库区编码
                        String erpAreaCode = erpService.selectByErpAreaCode(erpPositionCode, "503");
                        //根据入库单id和物料编号查询入库单详情
                        InstorageDetail instorageDetail = new InstorageDetail();
                        instorageDetail.setInstorageBillId(Long.parseLong(instorageBillId));
                        instorageDetail.setMaterialCode(materialCode);
                        instorageDetail = instorageDetailDAO.selectOne(instorageDetail);
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());

                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //入库数量
                        jsonObjectDetail.put("FQty", putAmount);
                        //仓库编码
                        jsonObjectDetail.put("FDCStockID", erpAreaCode);
                        //仓位编码
                        jsonObjectDetail.put("FDCSPID", erpPositionCode);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", wmsReceiptCode);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);

                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, STJGInStockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpInterfaceServiceDAO.updateInstorageBillErpFlag(list);
            }
        }

        /**2.判断入库单是否满足调用接口条件  越库类型**/
        List<Map<String, Object>> crossList = erpInterfaceServiceDAO.selectCrossEntrustInstorageBill();
        JSONArray JsonArrayResult = new JSONArray();
        if (crossList != null && crossList.size() > 0) {
            for (Map<String, Object> map : crossList) {
                //入库单id
                String instorageBillId = map.get("id") == null ? "" : map.get("id").toString();
                //收货单id
                String receiptPlanId = map.get("receipt_plan_id") == null ? "" : map.get("receipt_plan_id").toString();
                //客户编号
                String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();
                //获取收货单
                Receipt receipt = receiptDAO.selectById(receiptPlanId);
                //wms系统收货计划单号
                String wmsReceiptCode = receipt.getReceiptCode();

                JSONObject jsonObject = new JSONObject();
                //购货单位（客户或者叫货主）
                jsonObject.put("FCustID", customerCode);
                //保管人
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询入库单详情
                EntityWrapper<InstorageDetail> instorageDetailEntity = new EntityWrapper<>();
                instorageDetailEntity.eq("instorage_bill_id", instorageBillId);
                List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(instorageDetailEntity);
                if (lstInstorageDetail != null && lstInstorageDetail.size() > 0) {
                    for (int i = 0; i < lstInstorageDetail.size(); i++) {
                        InstorageDetail instorageDetail = lstInstorageDetail.get(i);
                        //物料编号
                        String materialCode = instorageDetail.getMaterialCode();
                        //物料名称
                        String materialName = instorageDetail.getMaterialName();
                        //入库数量
                        Double instorageAmount = StringUtils.isBlank(instorageDetail.getInstorageAmount()) ? 0d : Double.parseDouble(instorageDetail.getInstorageAmount());
                        //批次号
                        String batchNo = instorageDetail.getBatchNo();
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //入库数量
                        jsonObjectDetail.put("FQty", instorageAmount);
                        //仓库编码
                        jsonObjectDetail.put("FDCStockID", "SUN.01");
                        //仓位编码
                        jsonObjectDetail.put("FDCSPID", "A01");
                        //批号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", wmsReceiptCode);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                JsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(JsonArrayResult, STJGInStockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpInterfaceServiceDAO.updateInstorageBillErpFlag(crossList);
            }
        }
    }

    /**
     * 生产退库单及生产用料分摊接口(自采料)
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    @Override
    public void setSCOutstockRed() {

        //（1）查询满足调用生产退货入库单(自采料)接口条件的入库单
        List<Map<String, Object>> list = erpInterfaceServiceDAO.selectConfessReturnInstorageBill(DyylConstant.SELFMINING);
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                //入库单id
                String instorageBillId = map.get("id") == null ? "" : map.get("id").toString();
                //备料单编号
                String spareBillCode = map.get("spare_bill_code") == null ? "" : map.get("spare_bill_code").toString();
                SpareBillManagerVO spareBill = new SpareBillManagerVO();
                spareBill.setSpareBillCode(spareBillCode);
                spareBill = spareBillDAO.selectOne(spareBill);
                //备料单id
                Long spareBillId = spareBill.getId();
                //调配室使用线（值为：1或2或3或4），对应领料部门编号
                String mixUseLine = spareBill.getMixUseLine();
                //获取部门编号
                String departmentCode = erpService.getDepartmentCode(mixUseLine);
                //ERP备料单号
                String erpSpareBillCode = spareBill.getErpSpareBillCode();
                //生产任务编号
                String productNo = spareBill.getProductNo();

                JSONObject jsonObject = new JSONObject();
                //领料部门编号
                jsonObject.put("FDeptID", departmentCode);
                //红蓝字
                jsonObject.put("Frob", "-1");
                //保管人
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询上架单详情
                List<Map<String, Object>> lstPutBillDetail = erpInterfaceServiceDAO.selectPutBillDetail(instorageBillId);
                if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
                    for (int i = 0; i < lstPutBillDetail.size(); i++) {
                        Map<String, Object> putBillDetailMap = lstPutBillDetail.get(i);
                        //物料编码
                        String materialCode = putBillDetailMap.get("material_code") == null ? "" : putBillDetailMap.get("material_code").toString();
                        //物料名称
                        String materialName = putBillDetailMap.get("material_name") == null ? "" : putBillDetailMap.get("material_name").toString();
                        //库位编号
                        String positionCode = putBillDetailMap.get("position_code") == null ? "" : putBillDetailMap.get("position_code").toString();
                        //上架数量
                        Double putAmount = putBillDetailMap.get("put_amount") == null ? 0d : Double.parseDouble(putBillDetailMap.get("put_amount").toString());
                        //批次号
                        String batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();

                        //根据备料单id和物料编号获取行号
                        SpareBillDetailManagerVO spareBillDetail = new SpareBillDetailManagerVO();
                        spareBillDetail.setSpareBillId(spareBillId);
                        spareBillDetail.setMaterialCode(materialCode);
                        spareBillDetail = spareBillDetailDAO.selectOne(spareBillDetail);
                        //行号
                        Long line = spareBillDetail.getLine();

                        //根据库位编号获取库区编号
                        String areaCode = erpService.selectDepotAreaCode(positionCode);
                        //东洋库区编号转成erp库区编号
                        String erpAreaCode = erpService.changDepotPosition(areaCode);

                        //根据入库单id和物料编号查询入库单详情
                        InstorageDetail instorageDetail = new InstorageDetail();
                        instorageDetail.setInstorageBillId(Long.parseLong(instorageBillId));
                        instorageDetail.setMaterialCode(materialCode);
                        instorageDetail = instorageDetailDAO.selectOne(instorageDetail);
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());

                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //入库数量
                        jsonObjectDetail.put("FQty", putAmount);
                        //仓库编码
                        jsonObjectDetail.put("FSCStockID", erpAreaCode);
                        //仓位编码
                        jsonObjectDetail.put("FDCSPID", positionCode);
                        //批号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", erpSpareBillCode);
                        //源单分录
                        jsonObjectDetail.put("FSourceEntryID", line);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "生产领料单");
                        //生产任务单
                        jsonObjectDetail.put("FICMOBillNo", productNo);
                        //WMS单号
                        jsonObjectDetail.put("FWMSSourceBillNo", spareBillCode);
                        //WMS行号
                        jsonObjectDetail.put("FWMSSourceEntryID", line);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);

                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }

        }
        //调用接口路径传输数据给erp
        boolean result = erpService.getDate(jsonArrayResult, SCOutstockRedURL);
        //如果调用接口成功，更新入库表erp_flag字段
        if (result) {
            erpInterfaceServiceDAO.updateInstorageBillErpFlag(list);
        }

    }

    /**
     * 生产退库单及生产用料分摊接口(客料)
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    @Override
    public void SetSTOutstockRed() {
        //（1）查询满足调用生产退货入库单(客料)接口条件的入库单
        List<Map<String, Object>> list = erpInterfaceServiceDAO.selectConfessReturnInstorageBill(DyylConstant.CUSTOMERSUPPLY);
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                //入库单id
                String instorageBillId = map.get("id") == null ? "" : map.get("id").toString();
                //备料单编号
                String spareBillCode = map.get("spare_bill_code") == null ? "" : map.get("spare_bill_code").toString();
                //备料单编号
                String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();
                SpareBillManagerVO spareBill = new SpareBillManagerVO();
                spareBill.setSpareBillCode(spareBillCode);
                spareBill = spareBillDAO.selectOne(spareBill);
                //备料单id
                Long spareBillId = spareBill.getId();
                //调配室使用线（值为：1或2或3或4），对应领料部门编号
                String mixUseLine = spareBill.getMixUseLine();
                //获取部门编号
                String departmentCode = erpService.getDepartmentCode(mixUseLine);
                //ERP备料单号
                String erpSpareBillCode = spareBill.getErpSpareBillCode();
                //生产任务编号
                String productNo = spareBill.getProductNo();

                JSONObject jsonObject = new JSONObject();
                //领料部门编号
                jsonObject.put("FDeptID", departmentCode);
                //红蓝字
                jsonObject.put("Frob", "-1");
                //保管人
                jsonObject.put("FSManagerID", "002");
                //验收人
                jsonObject.put("FFManagerID", "002");
                //制单人
                jsonObject.put("FBillerID", "Administrator");
                //审核人
                jsonObject.put("FCheckerID", "Administrator");
                //客户
                jsonObject.put("FCustID", customerCode);

                JSONArray jsonArray = new JSONArray();

                //根据入库单id查询上架单详情
                List<Map<String, Object>> lstPutBillDetail = erpInterfaceServiceDAO.selectPutBillDetail(instorageBillId);
                if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
                    for (int i = 0; i < lstPutBillDetail.size(); i++) {
                        Map<String, Object> putBillDetailMap = lstPutBillDetail.get(i);
                        //物料编码
                        String materialCode = putBillDetailMap.get("material_code") == null ? "" : putBillDetailMap.get("material_code").toString();
                        //物料名称
                        String materialName = putBillDetailMap.get("material_name") == null ? "" : putBillDetailMap.get("material_name").toString();
                        //库位编号
                        String positionCode = putBillDetailMap.get("position_code") == null ? "" : putBillDetailMap.get("position_code").toString();
                        //上架数量
                        Double putAmount = putBillDetailMap.get("put_amount") == null ? 0d : Double.parseDouble(putBillDetailMap.get("put_amount").toString());
                        //批次号
                        String batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();

                        //根据备料单id和物料编号获取行号
                        SpareBillDetailManagerVO spareBillDetail = new SpareBillDetailManagerVO();
                        spareBillDetail.setSpareBillId(spareBillId);
                        spareBillDetail.setMaterialCode(materialCode);
                        spareBillDetail = spareBillDetailDAO.selectOne(spareBillDetail);
                        //行号
                        Long line = spareBillDetail.getLine();

                        //依据库位编号获取库位名称
                        String positionName = depotPositionDAO.selectPositionName(positionCode);
                        //依据库区编码与库位名称查找库位编码
                        String erpPositionCode = erpService.selectPositionCode(positionName);
                        //根据库位编号与客供字段获取库区编码
                        String erpAreaCode = erpService.selectByErpAreaCode(erpPositionCode, "503");

                        //根据入库单id和物料编号查询入库单详情
                        InstorageDetail instorageDetail = new InstorageDetail();
                        instorageDetail.setInstorageBillId(Long.parseLong(instorageBillId));
                        instorageDetail.setMaterialCode(materialCode);
                        instorageDetail = instorageDetailDAO.selectOne(instorageDetail);
                        //生产日期
                        String productDate = instorageDetail.getProductDate();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());

                        //有效期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productDate);

                        JSONObject jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //入库数量
                        jsonObjectDetail.put("FQty", putAmount);
                        //仓库编码
                        jsonObjectDetail.put("FSCStockID", erpAreaCode);
                        //仓位编码
                        jsonObjectDetail.put("FDCSPID", erpPositionCode);
                        //批号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", erpSpareBillCode);
                        //源单分录
                        jsonObjectDetail.put("FSourceEntryID", line);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "生产领料单");
                        //生产任务单
                        jsonObjectDetail.put("FICMOBillNo", productNo);
                        //WMS单号
                        jsonObjectDetail.put("FWMSSourceBillNo", spareBillCode);
                        //WMS行号
                        jsonObjectDetail.put("FWMSSourceEntryID", line);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);

                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
        }
        //调用接口路径传输数据给erp
        boolean result = erpService.getDate(jsonArrayResult, STOutstockRed);
        //如果调用接口成功，更新入库表erp_flag字段
        if (result) {
            erpInterfaceServiceDAO.updateInstorageBillErpFlag(list);
        }
    }

}