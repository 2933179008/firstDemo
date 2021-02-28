package com.tbl.modules.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.external.dao.ErpOutstorageServiceDAO;
import com.tbl.modules.external.service.ErpOutstorageService;
import com.tbl.modules.external.service.ErpService;
import com.tbl.modules.instorage.dao.ReceiptDetailDAO;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.ReceiptService;
import com.tbl.modules.outstorage.dao.SpareBillDAO;
import com.tbl.modules.outstorage.dao.SpareBillDetailDAO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcg
 * data 2019/3/27
 */

/**
 * ERP 出库接口
 */
@Service("erpOutstorageService")
public class ErpOutstorageServiceImpl implements ErpOutstorageService {

    @Autowired
    private ErpOutstorageServiceDAO erpOutstorageServiceDAO;

    @Autowired
    private DepotPositionDAO depotPositionDAO;

    @Autowired
    private MaterielDAO materielDAO;

    @Autowired
    private ErpService erpService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptDetailDAO receiptDetailDAO;

    @Autowired
    private SpareBillDAO spareBillDAO;

    @Autowired
    private SpareBillDetailDAO spareBillDetailDAO;

    @Autowired
    private OutStorageService outStorageService;

    @Autowired
    private StockService stockService;

    //销毁出库
    @Value("${erp.OtherOutStockURL}")
    private String OtherOutStockURL;

    //自采退货
    @Value("${erp.POInstockRedUrl}")
    private String POInstockRedUrl;

    //公司领料
    @Value("${erp.SCOutstockUrl}")
    private String SCOutstockUrl;

    //客料领料
    @Value("${erp.STOutstockUrl}")
    private String STOutstockUrl;

    /**
     * 销毁出库
     */
    @Override
    public void destoryOutstorage() {
        /**销毁出库**/
        JSONArray jsonArrayResult = new JSONArray();
        List<Map<String, Object>> list = erpOutstorageServiceDAO.getDestoryOutstorage();
        if (list != null && list.size() > 0) {
            for (Map map : list) {
                JSONObject jsonObject = new JSONObject();
                //出库单ID
                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                //获取有无RFID
                String billType = map.get("bill_type") == null ? "" : map.get("bill_type").toString();
                //领料部门编号（先开始是默认值，后期修改）
                jsonObject.put("FDeptID", "02.09");
                //保管人（可以是默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（可以是默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（可以是默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(可以是默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();
                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {
                        JSONObject jsonObjectDetail = new JSONObject();
                        //物料编码
                        String materialCode = detailList.get(i).get("material_code") == null ? "" : detailList.get(i).get("material_code").toString();
                        String materialName = detailList.get(i).get("material_name") == null ? "" : detailList.get(i).get("material_name").toString();
                        String batchNo = detailList.get(i).get("batch_no") == null ? "" : detailList.get(i).get("batch_no").toString();
                        String amount = detailList.get(i).get("weight") == null ? "" : detailList.get(i).get("weight").toString();
                        String postionCode = detailList.get(i).get("position_code") == null ? "" : detailList.get(i).get("position_code").toString();
                        //库区编码
                        String areaCode = erpService.selectDepotAreaCode(postionCode);

                        Double damount = Double.parseDouble(amount);
                        //获取生产时间
                        String productData = detailList.get(i).get("product_data") == null ? "" : detailList.get(i).get("product_data").toString();

                        //获取对应的库存信息
                        EntityWrapper<Stock> wrapperStock = new EntityWrapper<>();
                        wrapperStock.eq("material_code", materialCode);
                        wrapperStock.eq("material_type", billType);
                        wrapperStock.eq("position_code", postionCode);
                        wrapperStock.eq("batch_no", batchNo);
                        Stock stock = stockService.selectOne(wrapperStock);
                        String qualityDate = stock.getQualityDate();
                        Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productData);

                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", areaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", postionCode);
                        //数量
                        jsonObjectDetail.put("FQty", damount);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", 1);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", 1);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产、采购日期
                        jsonObjectDetail.put("FKFDate", productData);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
        }
        //调用接口路径传输数据给erp
        boolean result = erpService.getDate(jsonArrayResult, OtherOutStockURL);
        //如果调用接口成功，更新入库表erp_flag字段
        if (result) {
            erpOutstorageServiceDAO.updateOutstorageBillErpFlag(list);
        }
    }

    /**
     * 越库出库接口(自采)
     */
    @Override
    public void cusCrossOutstorage() {
        JSONArray JsonArrayResult = new JSONArray();
        List<Map<String, Object>> crossList = erpOutstorageServiceDAO.getCusCrossOutstorage();
        if (crossList != null && crossList.size() > 0) {
            for (Map map : crossList) {
                JSONObject jsonObject = new JSONObject();
                //出库单ID
                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                //领料部门编号（默认值）
                jsonObject.put("FDeptID", "02.09");
                //保管人（默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();
                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {
                        JSONObject jsonObjectDetail = new JSONObject();
                        //物料编码
                        String materialCode = detailList.get(i).get("material_code") == null ? "" : detailList.get(i).get("material_code").toString();
                        String materialName = detailList.get(i).get("material_name") == null ? "" : detailList.get(i).get("material_name").toString();
                        String batchNo = detailList.get(i).get("batch_no") == null ? "" : detailList.get(i).get("batch_no").toString();
                        String amount = detailList.get(i).get("weight") == null ? "" : detailList.get(i).get("weight").toString();

                        Double damount = Double.parseDouble(amount);
                        //获取生产时间
                        String productData = detailList.get(i).get("product_data") == null ? "" : detailList.get(i).get("product_data").toString();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                        //获取保质期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productData);

                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", "A.01");
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", "A01");
                        //数量
                        jsonObjectDetail.put("FQty", damount);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", i + 1);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", i + 1);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产、采购日期
                        jsonObjectDetail.put("FKFDate", productData);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                JsonArrayResult.add(jsonObject);
            }
        }

        //调用接口路径传输数据给erp
        boolean crossResult = erpService.getDate(JsonArrayResult, OtherOutStockURL);
        //如果调用接口成功，更新入库表erp_flag字段
        if (crossResult) {
            erpOutstorageServiceDAO.updateOutstorageBillErpFlag(crossList);
        }
    }


    /**
     * 越库出库接口(客供)
     */
    @Override
    public void crossOutstorage() {
        JSONArray JsonArrayResult = new JSONArray();
        List<Map<String, Object>> crossList = erpOutstorageServiceDAO.getCrossOutstorage();
        if (crossList != null && crossList.size() > 0) {
            for (Map map : crossList) {
                JSONObject jsonObject = new JSONObject();
                //出库单ID
                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                //出库单ID
                String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();
                //物料领用部门
                jsonObject.put("FDeptID", "02.09");
                //客户编号
                jsonObject.put("FCustID", customerCode);
                //保管人（可以是默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（可以是默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（可以是默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(可以是默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();
                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {
                        JSONObject jsonObjectDetail = new JSONObject();
                        //物料编码
                        String materialCode = detailList.get(i).get("material_code") == null ? "" : detailList.get(i).get("material_code").toString();
                        String materialName = detailList.get(i).get("material_name") == null ? "" : detailList.get(i).get("material_name").toString();
                        String batchNo = detailList.get(i).get("batch_no") == null ? "" : detailList.get(i).get("batch_no").toString();
                        String amount = detailList.get(i).get("weight") == null ? "" : detailList.get(i).get("weight").toString();

                        Double damount = Double.parseDouble(amount);
                        //获取生产时间
                        String productData = detailList.get(i).get("product_data") == null ? "" : detailList.get(i).get("product_data").toString();

                        //根据物料编号获取保质期
                        Materiel materiel = new Materiel();
                        materiel.setMaterielCode(materialCode);
                        materiel = materielDAO.selectOne(materiel);
                        //保质期(天)
                        Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                        //获取保质期至
                        String deadline = erpService.setQualityDate(qualityPeriod, productData);

                        //行号
                        jsonObjectDetail.put("FEntryID", 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //出库数量
                        jsonObjectDetail.put("FQty", damount);
                        //仓库编号
                        jsonObjectDetail.put("FSCStockID", "SUN.01");
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", "A01");
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", "");
                        //源单分录
                        jsonObjectDetail.put("FSourceEntryID", 1);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "");
                        //采购订单
                        jsonObjectDetail.put("FICMOBillNo", "");
                        //wms源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", "");
                        //wms源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", 1);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产、采购日期
                        jsonObjectDetail.put("FKFDate", productData);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", deadline);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                JsonArrayResult.add(jsonObject);
            }
        }

        //调用接口路径传输数据给erp
        boolean crossResult = erpService.getDate(JsonArrayResult, STOutstockUrl);
        //如果调用接口成功，更新入库表erp_flag字段
        if (crossResult) {
            erpOutstorageServiceDAO.updateOutstorageBillErpFlag(crossList);
        }
    }

    /**
     * 自采退货
     */
    @Override
    public void SelfCollectionAndReturn() {
        JSONArray jsonArrayResult = new JSONArray();
        List<Map<String, Object>> list = erpOutstorageServiceDAO.getReturnList();
        if (list != null && list.size() > 0) {
            for (Map map : list) {
                JSONObject jsonObject = new JSONObject();
                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(Long.valueOf(outStorageId));
                EntityWrapper<Receipt> wrapperReceipt = new EntityWrapper<>();
                wrapperReceipt.eq("receipt_code", outStorageManagerVO.getReceiptCode());
                Receipt receipt = receiptService.selectOne(wrapperReceipt);
                //供应商编码
                String customerCode = receipt.getCustomerCode();
                //采购方式
                String type = receipt.getDocumentType();

                //wms系统收货计划单号（wms采购订单编号）
                String wmsReceiptCode = receipt.getReceiptCode();
                //ERP系统采购订单编号
                String erpReceiptCode = receipt.getErpReceiptCode();

                //采购方式
                jsonObject.put("FPOStyle", type);
                //供应商编码
                jsonObject.put("FSupplyID", customerCode);
                //红蓝字（固定-1）
                jsonObject.put("Frob", -1);
                //保管人（可以是默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（可以是默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（可以是默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(可以是默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();

                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {

                        JSONObject jsonObjectDetail = new JSONObject();
                        //物料编码
                        String materialCode = detailList.get(i).get("material_code") == null ? "" : detailList.get(i).get("material_code").toString();
                        String materialName = detailList.get(i).get("material_name") == null ? "" : detailList.get(i).get("material_name").toString();
                        String batchNo = detailList.get(i).get("batch_no") == null ? "" : detailList.get(i).get("batch_no").toString();
                        String amount = detailList.get(i).get("weight") == null ? "" : detailList.get(i).get("weight").toString();
                        Double damount = Double.parseDouble(amount);
                        String postionCode = detailList.get(i).get("position_code") == null ? "" : detailList.get(i).get("position_code").toString();
                        //通过库位编号获取仓位编号
                        String areaCode = erpService.selectDepotAreaCode(postionCode);
                        //东洋库区编号转成erp库区编号
                        String erpAreaCode = erpService.changDepotPosition(areaCode);
                        //获取生产时间
                        String productData = detailList.get(i).get("product_data") == null ? "" : detailList.get(i).get("product_data").toString();

                        //获取对应的库存信息
                        EntityWrapper<Stock> wrapperStock = new EntityWrapper<>();
                        wrapperStock.eq("material_code", materialCode);
                        wrapperStock.eq("material_type", DyylConstant.MATERIAL_RFID);
                        wrapperStock.eq("position_code", postionCode);
                        wrapperStock.eq("batch_no", batchNo);
                        Stock stock = stockService.selectOne(wrapperStock);
                        String qualityDate = stock.getQualityDate();
                        Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productData);

                        //根据收货单id和物料编号查询收货计划单详情
                        ReceiptDetail receiptDetail = new ReceiptDetail();
                        receiptDetail.setReceiptPlanId(receipt.getId());
                        receiptDetail.setMaterialCode(materialCode);
                        receiptDetail = receiptDetailDAO.selectOne(receiptDetail);
                        //erp传过来的行号
                        Long line = receiptDetail.getLine();

                        //行号
                        jsonObjectDetail.put("FEntryID", i + 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //出库数量
                        jsonObjectDetail.put("FQty", damount);
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", erpAreaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", postionCode);
                        //源单单号
                        jsonObjectDetail.put("FSourceBillNo", erpReceiptCode);
                        //源单分录
                        jsonObjectDetail.put("FSourceEntryID", line);
                        //源单类型
                        jsonObjectDetail.put("FSourceTranType", "采购入库单");
                        //采购订单
                        jsonObjectDetail.put("FOrderBillNo", wmsReceiptCode);
                        //wms源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", "");
                        //wms源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产、采购日期
                        jsonObjectDetail.put("FKFDate", productData);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
        }
        //调用接口路径传输数据给erp
        boolean result = erpService.getDate(jsonArrayResult, POInstockRedUrl);
        //如果调用接口成功，更新入库表erp_flag字段
        if (result) {
            erpOutstorageServiceDAO.updateOutstorageBillErpFlag(list);
        }
    }

    /**
     * 公司领料
     */
    @Override
    public void materialcompanyRequisition() {
        JSONArray jsonArrayResult = new JSONArray();
        List<Map<String, Object>> list = erpOutstorageServiceDAO.getMaterialRequisition(DyylConstant.SELFMINING);
        if (list != null && list.size() > 0) {
            for (Map map : list) {

                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                String materialType = map.get("material_type") == null ? "" : map.get("material_type").toString();
                String spareBillCode = map.get("spare_bill_code") == null ? "" : map.get("spare_bill_code").toString();
                //有无RFID
                String billType = map.get("bill_type") == null ? "" : map.get("bill_type").toString();
                SpareBillManagerVO spareBillManagerVO = new SpareBillManagerVO();
                spareBillManagerVO.setSpareBillCode(spareBillCode);
                spareBillManagerVO = spareBillDAO.selectOne(spareBillManagerVO);
                //自产领料部门
                String mixUseLine = spareBillManagerVO.getMixUseLine();
                //获取部门编号
                String departmentCode = erpService.getDepartmentCode(mixUseLine);
                //源单单号
                String erpSpareBillCode = spareBillManagerVO.getErpSpareBillCode();

                //源单单号spareBillManagerVO
                String wmsSpareBillCode = spareBillManagerVO.getSpareBillCode();

                //生产任务单号
                String productNo = spareBillManagerVO.getProductNo();

                JSONObject jsonObject = new JSONObject();
                //物料领用部门
                jsonObject.put("FDeptID", departmentCode);
                //保管人（可以是默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（可以是默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（可以是默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(可以是默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();
                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {
                        JSONObject jsonObjectDetail = this.commonMethod(detailList, materialType, spareBillManagerVO, erpSpareBillCode, i, productNo, wmsSpareBillCode, billType);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, SCOutstockUrl);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpOutstorageServiceDAO.updateOutstorageBillErpFlag(list);
            }
        }
    }

    /**
     * 客供领料
     */
    @Override
    public void materialCustomerRequisition() {
        JSONArray jsonArrayResult = new JSONArray();
        List<Map<String, Object>> list = erpOutstorageServiceDAO.getMaterialRequisition(DyylConstant.CUSTOMERSUPPLY);
        if (list != null && list.size() > 0) {
            for (Map map : list) {

                String outStorageId = map.get("id") == null ? "" : map.get("id").toString();
                String materialType = map.get("material_type") == null ? "" : map.get("material_type").toString();
                String spareBillCode = map.get("spare_bill_code") == null ? "" : map.get("spare_bill_code").toString();
                String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();
                //有无RFID
                String billType = map.get("bill_type") == null ? "" : map.get("bill_type").toString();
                SpareBillManagerVO spareBillManagerVO = new SpareBillManagerVO();
                spareBillManagerVO.setSpareBillCode(spareBillCode);
                spareBillManagerVO = spareBillDAO.selectOne(spareBillManagerVO);
                //自产领料部门
                String mixUseLine = spareBillManagerVO.getMixUseLine();
                //获取部门编号
                String departmentCode = erpService.getDepartmentCode(mixUseLine);
                //源单单号
                String erpSpareBillCode = spareBillManagerVO.getErpSpareBillCode();
                //源单单号spareBillManagerVO
                String wmsSpareBillCode = spareBillManagerVO.getSpareBillCode();

                //生产任务单号
                String productNo = spareBillManagerVO.getProductNo();

                JSONObject jsonObject = new JSONObject();
                //物料领用部门
                jsonObject.put("FDeptID", departmentCode);
                //客户编号
                jsonObject.put("FCustID", customerCode);
                //保管人（可以是默认值）
                jsonObject.put("FSManagerID", "002");
                //验收人（可以是默认值）
                jsonObject.put("FFManagerID", "002");
                //制单人（可以是默认值）
                jsonObject.put("FBillerID", "Administrator");
                //审核人(可以是默认值)
                jsonObject.put("FCheckerID", "Administrator");

                JSONArray jsonArray = new JSONArray();
                List<Map<String, Object>> detailList = erpOutstorageServiceDAO.getDetailList(outStorageId);
                if (detailList != null && detailList.size() > 0) {
                    for (int i = 0; i < detailList.size(); i++) {
                        JSONObject jsonObjectDetail = this.commonMethod(detailList, materialType, spareBillManagerVO, erpSpareBillCode, i, productNo, wmsSpareBillCode, billType);
                        jsonArray.add(jsonObjectDetail);
                    }
                }
                jsonObject.put("detailinfo", jsonArray);
                jsonArrayResult.add(jsonObject);
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, STOutstockUrl);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpOutstorageServiceDAO.updateOutstorageBillErpFlag(list);
            }
        }
    }

    /**
     * 客料以及自供的共用方法
     *
     * @param detailList
     * @param spareBillManagerVO
     * @param erpSpareBillCode
     * @param i
     * @param productNo
     * @param wmsSpareBillCode
     * @return
     */
    public JSONObject commonMethod(List<Map<String, Object>> detailList, String materialType, SpareBillManagerVO spareBillManagerVO, String erpSpareBillCode, int i, String productNo, String wmsSpareBillCode, String billType) {
        JSONObject jsonObjectDetail = new JSONObject();
        //物料编码
        String materialCode = detailList.get(i).get("material_code") == null ? "" : detailList.get(i).get("material_code").toString();
        String materialName = detailList.get(i).get("material_name") == null ? "" : detailList.get(i).get("material_name").toString();
        String batchNo = detailList.get(i).get("batch_no") == null ? "" : detailList.get(i).get("batch_no").toString();
        String amount = detailList.get(i).get("weight") == null ? "" : detailList.get(i).get("weight").toString();
        Double damount = Double.parseDouble(amount);

        String positionCode = detailList.get(i).get("position_code") == null ? "" : detailList.get(i).get("position_code").toString();
        //库区编码
        String areaCode = null;
        //如果为自采物料
        if (materialType.equals(DyylConstant.SELFMINING)) {
            //通过库位编号获取仓位编号
            areaCode = erpService.selectDepotAreaCode(positionCode);
        } else {   //客供物料
            //依据库位编号获取库位名称
            String positionName = depotPositionDAO.selectPositionName(positionCode);
            //依据库区编码与库位名称查找库位编码
            positionCode = erpService.selectPositionCode(positionName);
            if (billType.equals(DyylConstant.MATERIAL_NORFID)) {  //物料为散货时
                areaCode = erpService.getAreaCode(materialCode,positionCode);
            } else {   //物料为整货时
                //根据库位编号与客供字段获取库区编码
                areaCode = erpService.selectByErpAreaCode(positionCode, "503");
            }
        }

        //源单分录
        SpareBillDetailManagerVO spareBillDetailManagerVO = new SpareBillDetailManagerVO();
        spareBillDetailManagerVO.setSpareBillId(spareBillManagerVO.getId());
        spareBillDetailManagerVO.setMaterialCode(materialCode);
        spareBillDetailManagerVO = spareBillDetailDAO.selectOne(spareBillDetailManagerVO);

        Long line = spareBillDetailManagerVO.getLine();

        //获取生产时间
        String productData = detailList.get(i).get("product_data") == null ? "" : detailList.get(i).get("product_data").toString();
        //获取对应的库存信息
        EntityWrapper<Stock> wrapperStock = new EntityWrapper<>();
        wrapperStock.eq("material_code", materialCode);
        wrapperStock.eq("material_type", billType);
        wrapperStock.eq("position_code", positionCode);
        wrapperStock.eq("batch_no", batchNo);
        Stock stock = stockService.selectOne(wrapperStock);
        //获取有效期至
        String qualityDate = stock.getQualityDate();
        //获取保质期
        Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productData);

        //行号
        jsonObjectDetail.put("FEntryID", i++);
        //物料编码
        jsonObjectDetail.put("FItemID", materialCode);
        //物料名称
        jsonObjectDetail.put("FItemName", materialName);
        //批次号
        jsonObjectDetail.put("FBatchNo", batchNo);
        //出库数量
        jsonObjectDetail.put("FQty", damount);
        //仓库编号
        jsonObjectDetail.put("FSCStockID", areaCode);
        //仓位编号
        jsonObjectDetail.put("FDCSPID", positionCode);
        //源单单号
        jsonObjectDetail.put("FSourceBillNo", erpSpareBillCode);
        //源单分录
        jsonObjectDetail.put("FSourceEntryID", line);
        //源单类型
        jsonObjectDetail.put("FSourceTranType", "投料单");
        //采购订单
        jsonObjectDetail.put("FICMOBillNo", productNo);
        //wms源单单号
        jsonObjectDetail.put("FWMSSourceBillNo", wmsSpareBillCode);
        //wms源单分录
        jsonObjectDetail.put("FWMSSourceEntryID", line);
        //有效天数
        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
        //生产、采购日期
        jsonObjectDetail.put("FKFDate", productData);
        //有效期至
        jsonObjectDetail.put("FPeriodDate", qualityDate);
        return jsonObjectDetail;
    }

}
