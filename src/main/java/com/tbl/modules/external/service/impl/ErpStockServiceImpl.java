package com.tbl.modules.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.external.dao.ErpServiceDAO;
import com.tbl.modules.external.dao.ErpStockInterfaceServiceDAO;
import com.tbl.modules.external.service.ErpService;
import com.tbl.modules.external.service.ErpStockService;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.MaterielPower;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielPowerService;
import com.tbl.modules.stock.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author pz
 * data 2019/3/27
 */

/**
 * ERP 库存接口
 */
@Service("erpStockService")
public class ErpStockServiceImpl implements ErpStockService {
    //erp通用方法Service
    @Autowired
    private ErpService erpService;
    //erp通用方法DAo
    @Autowired
    private ErpServiceDAO erpServiceDAO;
    //erp库存DAO
    @Autowired
    private ErpStockInterfaceServiceDAO erpStockInterfaceServiceDAO;
    //库存service
    @Autowired
    private StockService stockService;
    //库位Service
    @Autowired
    private DepotPositionDAO depotPositionDAO;
    //物料Service
    @Autowired
    private MaterielService materielService;
    //货权转移Service
    @Autowired
    private MaterielPowerService materielPowerService;
    //物料绑定详情Service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    //调拨方法路径
    @Value("${erp.DiaoBoURL}")
    private String DiaoBoURL;
    //其它入库方法路径
    @Value("${erp.OtherInstockURL}")
    private String OtherInstockURL;
    //受托加工入库单接口
    @Value("${erp.STJGInStockURL}")
    private String STJGInStockURL;
    //其它出库方法路径
    @Value("${erp.OtherOutStockURL}")
    private String OtherOutStockURL;
    //客料领料
    @Value("${erp.STOutstockUrl}")
    private String STOutstockUrl;

    /**
     * 货权转移
     *
     * @return
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void transferView() {

        /**判断库存表中是否满足调用接口条件 出库**/
        List<Map<String, Object>> list = erpStockInterfaceServiceDAO.selectStock();
        JSONArray jsonArrayOutResult = new JSONArray();
        if (list != null && list.size() > 0) {
            String url = "";
            for (Map<String, Object> map1 : list) {
                if (map1 != null) {
                    //物料类型
                    String materialType = map1.get("material_type") == null ? "" : map1.get("material_type").toString();
                    //物料编码
                    String materialCode = map1.get("material_code") == null ? "" : map1.get("material_code").toString();
                    //物料名称
                    String materialName = map1.get("material_name") == null ? "" : map1.get("material_name").toString();
                    //批次号
                    String batchNo = map1.get("batch_no") == null ? "" : map1.get("batch_no").toString();
                    //生产日期
                    String productDate = map1.get("product_date") == null ? "" : map1.get("product_date").toString();
                    //有效期至
                    String qualityDate = map1.get("quality_date") == null ? "" : map1.get("quality_date").toString();
                    //库位编码
                    String positionCode = map1.get("position_code") == null ? "" : map1.get("position_code").toString();
                    //获取保质期(天数)
                    Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productDate);
                    //获取出库数量
                    EntityWrapper<Stock> wraStock = new EntityWrapper<>();
                    //库存id
                    String stockId = map1.get("id") == null ? "" : map1.get("id").toString();
                    List<Stock> lstList = stockService.selectList(wraStock.eq("material_source", stockId));
                    Double outAmount = 0.0;
                    for (Stock s : lstList) {
                        outAmount = Double.parseDouble(s.getStockAmount()) + outAmount;
                    }
                    //货权转移源id
                    String materialSource = map1.get("material_source") == null ? "" : map1.get("material_source").toString();
                    //未进行货权转移之前的物料
                    String oldMaterialCode = "";
                    if (stockId.equals(materialSource)) {
                        EntityWrapper<MaterielPower> wrapperMaterielPower = new EntityWrapper<>();
                        wrapperMaterielPower.eq("stock_id", stockId);
                        List<MaterielPower> lstMaterielPower = materielPowerService.selectList(wrapperMaterielPower);
                        MaterielPower materielPower = null;
                        if (lstMaterielPower.size() == 1) {
                            materielPower = lstMaterielPower.get(0);
                            oldMaterialCode = materielPower.getMaterielCode();
                        } else {
                            materielPower = lstMaterielPower.get(lstMaterielPower.size() - 1);
                            oldMaterialCode = lstMaterielPower.get(0).getMaterielCode();
                        }
                        materialCode = materielPower.getMaterielCode();
                        //获取物料
                        EntityWrapper<Materiel> wrapperMateriel = new EntityWrapper<>();
                        wrapperMateriel.eq("materiel_code", materialCode);
                        Materiel materiel = materielService.selectOne(wrapperMateriel);
                        materialName = materiel.getMaterielName();
                    }
                    //获取物料类型
                    String instorageType = null;
                    if (materialType.equals(DyylConstant.MATERIAL_NORFID)) {  //散货
                        instorageType = erpService.getMaterielType(oldMaterialCode, positionCode);
                    } else {
                        instorageType = erpService.getInstorageType(oldMaterialCode, batchNo, positionCode);
                    }

                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    JSONObject jsonObjectDetail = null;
                    if (instorageType.equals(DyylConstant.SELFMINING)) {  //自采  其它出库
                        url = OtherOutStockURL;

                        jsonObject = new JSONObject();
                        //领料部门编号(必填)
                        jsonObject.put("FDeptID", "02.09");
                        //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                        jsonObject.put("FSManagerID", "002");
                        //验收人
                        jsonObject.put("FFManagerID", "002");
                        //制单人
                        jsonObject.put("FBillerID", "Administrator");
                        //审核人
                        jsonObject.put("FCheckerID", "Administrator");

                        jsonArray = new JSONArray();

                        //根据库位编号获取库区编号
                        String areaCode = erpService.selectDepotAreaCode(positionCode);
                        //东洋库区编号转成erp库区编号
                        String erpAreaCode = erpService.changDepotPosition(areaCode);

                        jsonObjectDetail = new JSONObject();
                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //入库数量
                        jsonObjectDetail.put("FQty", String.valueOf(outAmount));
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", erpAreaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", positionCode);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", "1");
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "1");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);
                    } else if (instorageType.equals("1")) { //客供 领料出库
                        url = STOutstockUrl;
                        //获取顾客编码
                        String customerCode = map1.get("customer_code") == null ? "" : map1.get("customer_code").toString();
                        jsonObject = new JSONObject();
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

                        jsonArray = new JSONArray();

                        //依据库位编号获取库位名称
                        String positionName = depotPositionDAO.selectPositionName(positionCode);
                        //依据库区编码与库位名称查找库位编码
                        positionCode = erpService.selectPositionCode(positionName);
                        //库区编码
                        String areaCode = null;
                        if (materialType.equals("0")) {  //物料为散货时
                            areaCode = erpService.getAreaCode(materialCode,positionCode);
                        } else {   //物料为整货时
                            //根据库位编号与客供字段获取库区编码
                            areaCode = erpService.selectByErpAreaCode(positionCode, "503");
                        }

                        jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //出库数量
                        jsonObjectDetail.put("FQty", outAmount);
                        //仓库编号
                        jsonObjectDetail.put("FSCStockID", areaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", positionCode);
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
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);
                    }
                    jsonArray.add(jsonObjectDetail);
                    jsonObject.put("detailinfo", jsonArray);
                    jsonArrayOutResult.add(jsonObject);
                }
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayOutResult, url);

            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpStockInterfaceServiceDAO.updateMaterielErpFlag(list);
            }
        }


        /**判断库存表中是否满足调用接口条件 入库**/
        List<Map<String, Object>> stockList = erpStockInterfaceServiceDAO.selectGenerateStock();
        JSONArray jsonArrayInResult = new JSONArray();
        if (stockList != null && stockList.size() > 0) {
            String url = "";
            for (Map<String, Object> map : stockList) {
                if (map != null) {
                    //库存id
                    String stockId = map.get("id") == null ? "" : map.get("id").toString();
                    //获取货物类型（整货或散货）
                    String materialType = map.get("material_type") == null ? "" : map.get("material_type").toString();
                    //物料编码
                    String materialCode = map.get("material_code") == null ? "" : map.get("material_code").toString();
                    //物料名称
                    String materialName = map.get("material_name") == null ? "" : map.get("material_name").toString();
                    //批次号
                    String batchNo = map.get("batch_no") == null ? "" : map.get("batch_no").toString();
                    //库位编码
                    String positionCode = map.get("position_code") == null ? "" : map.get("position_code").toString();
                    //库存数量
                    String stockAmount = map.get("stock_amount") == null ? "" : map.get("stock_amount").toString();
                    //生产日期
                    String productDate = map.get("product_date") == null ? "" : map.get("product_date").toString();
                    //有效期至
                    String qualityDate = map.get("quality_date") == null ? "" : map.get("quality_date").toString();
                    //获取保质期
                    Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productDate);
                    //判断转换后的物料为自采还是客供
                    EntityWrapper<MaterielPower> wrapperMaterielPower = new EntityWrapper<>();
                    wrapperMaterielPower.eq("stock_id", stockId);
                    MaterielPower materielPower = materielPowerService.selectOne(wrapperMaterielPower);
                    String instorageType = materielPower.getDocumentType();

                    JSONObject jsonObject = new JSONObject();
                    //保管人
                    jsonObject.put("FSManagerID", "002");
                    //验收人
                    jsonObject.put("FFManagerID", "002");
                    //制单人
                    jsonObject.put("FBillerID", "Administrator");
                    //审核人
                    jsonObject.put("FCheckerID", "Administrator");

                    JSONArray jsonArray = null;
                    JSONObject jsonObjectDetail = null;
                    if (instorageType.equals(DyylConstant.SELFMINING)) {  //自采  其它入库
                        url = OtherInstockURL;
                        //领料部门编号(必填)
                        jsonObject.put("FDeptID", "02.09");

                        jsonArray = new JSONArray();

                        //根据库位编号获取库区编号
                        String areaCode = erpService.selectDepotAreaCode(positionCode);
                        //东洋库区编号转成erp库区编号
                        String erpAreaCode = erpService.changDepotPosition(areaCode);

                        jsonObjectDetail = new JSONObject();
                        //行号  单据信息笔数，从1开始
                        jsonObjectDetail.put("FEntryID", 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批次号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //入库数量
                        jsonObjectDetail.put("FQty", stockAmount);
                        //仓库编号
                        jsonObjectDetail.put("FDCStockID", erpAreaCode);
                        //仓位编号
                        jsonObjectDetail.put("FDCSPID", positionCode);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", 1);
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", 1);
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);
                    } else if (instorageType.equals(DyylConstant.CUSTOMERSUPPLY)) {  //客供   委托加工入库
                        url = STJGInStockURL;
                        String customerCode = map.get("customer_code") == null ? "" : map.get("customer_code").toString();
                        //购货单位（客户或者叫货主）
                        jsonObject.put("FCustID", customerCode);

                        jsonArray = new JSONArray();

                        //依据库位编号获取库位名称
                        String positionName = depotPositionDAO.selectPositionName(positionCode);
                        //依据库区编码与库位名称查找库位编码
                        positionCode = erpService.selectPositionCode(positionName);
                        //库区编码
                        String areaCode = null;
                        if (materialType.equals("0")) {  //物料为散货时
                            areaCode = erpService.getAreaCode(materialCode,positionCode);
                        } else {   //物料为整货时
                            //根据库位编号与客供字段获取库区编码
                            areaCode = erpService.selectByErpAreaCode(positionCode, "503");
                        }

                        jsonObjectDetail = new JSONObject();
                        //行号
                        jsonObjectDetail.put("FEntryID", 1);
                        //物料编码
                        jsonObjectDetail.put("FItemID", materialCode);
                        //物料名称
                        jsonObjectDetail.put("FItemName", materialName);
                        //批号
                        jsonObjectDetail.put("FBatchNo", batchNo);
                        //入库数量
                        jsonObjectDetail.put("FQty", stockAmount);
                        //仓库编码
                        jsonObjectDetail.put("FDCStockID", areaCode);
                        //仓位编码
                        jsonObjectDetail.put("FDCSPID", positionCode);
                        //源单单号
                        jsonObjectDetail.put("FWMSSourceBillNo", "");
                        //源单分录
                        jsonObjectDetail.put("FWMSSourceEntryID", "");
                        //有效天数
                        jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                        //生产/采购日期
                        jsonObjectDetail.put("FKFDate", productDate);
                        //有效期至
                        jsonObjectDetail.put("FPeriodDate", qualityDate);

                    }
                    jsonArray.add(jsonObjectDetail);
                    jsonObject.put("detailinfo", jsonArray);
                    jsonArrayInResult.add(jsonObject);
                }
            }
            //调用接口路径传输数据给erp
            boolean Result = erpService.getDate(jsonArrayInResult, url);

            //如果调用接口成功，更新入库表erp_flag字段
            if (Result) {
                erpStockInterfaceServiceDAO.updateStockErpFlag(stockList);
            }
        }
    }

    /**
     * 库存更新接口（调拨单  散货）
     *
     * @return
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void setDiaoBo() {

        /**1.判断盘点任务单是否满足调用接口条件**/
        List<Map<String, Object>> list = erpStockInterfaceServiceDAO.selectMovePosition();
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                if (map1 != null) {
                    //移位单编号
                    String movePositionCode = map1.get("move_position_code") == null ? "" : map1.get("move_position_code").toString();
                    //原库位
                    Long formerPosition = map1.get("former_position") == null ? 0 : Long.parseLong(map1.get("former_position").toString());
                    //移动库位
                    Long positionBy = map1.get("position_by") == null ? 0 : Long.parseLong(map1.get("position_by").toString());
                    //物料编码
                    String materielCode = map1.get("materiel_code") == null ? "" : map1.get("materiel_code").toString();
                    //批次号
                    String batchNo = map1.get("batch_no") == null ? "" : map1.get("batch_no").toString();
                    //移出数量
                    String movePositionAmount = map1.get("move_position_amount") == null ? "" : map1.get("move_position_amount").toString();

                    JSONObject jsonObject = new JSONObject();
                    //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                    jsonObject.put("FSManagerID", "002");
                    //验收人
                    jsonObject.put("FFManagerID", "002");
                    //制单人
                    jsonObject.put("FBillerID", "Administrator");
                    //审核人
                    jsonObject.put("FCheckerID", "Administrator");

                    JSONArray jsonArray = new JSONArray();

                    //获取库位编码
                    String formerPositionCode = depotPositionDAO.selectById(formerPosition).getPositionCode();
                    String positionCode = depotPositionDAO.selectById(positionBy).getPositionCode();
                    //根据库位编号获取库区编号
                    String formerAreaCode = erpService.selectDepotAreaCode(formerPositionCode);
                    //东洋库区编号转成erp库区编号
                    String erpFormerAreaCode = erpService.changDepotPosition(formerAreaCode);
                    String areaCode = erpService.selectDepotAreaCode(positionCode);
                    //东洋库区编号转成erp库区编号
                    String erpAreaCode = erpService.changDepotPosition(areaCode);

                    //获取库位的库存信息
                    EntityWrapper<Stock> wraStock = new EntityWrapper<>();
                    wraStock.eq("position_code", positionCode);
                    wraStock.eq("material_code", materielCode);
                    wraStock.eq("batch_no", batchNo);
                    wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_NOCARGO);
                    Stock stock = stockService.selectOne(wraStock);
                    //物料名称
                    String materialName = stock.getMaterialName();
                    //生产日期
                    String productDate = stock.getProductDate();
                    //有效期至
                    String qualityDate = stock.getQualityDate();
                    //根据物料编号获取保质期
                    EntityWrapper<Materiel> wrapperMateriel = new EntityWrapper<Materiel>();
                    wrapperMateriel.eq("materiel_code",materielCode);
                    Materiel materiel = materielService.selectOne(wrapperMateriel);
                    //保质期(天)
                    Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());

                    //依据库区名称与客供查找库区编码
                    areaCode = erpService.getAreaCode(materielCode,positionCode);
                    //获取所有Erp客供库区
                    List<String> lstAreaCode = erpServiceDAO.getErpAreaCode();
                    if (lstAreaCode.contains(areaCode)) {
                        continue;
                    }

                    JSONObject jsonObjectDetail = new JSONObject();
                    //行号  单据信息笔数，从1开始
                    jsonObjectDetail.put("FEntryID", 1);
                    //物料编码
                    jsonObjectDetail.put("FItemID", materielCode);
                    //物料名称
                    jsonObjectDetail.put("FItemName", materialName);
                    //批次号
                    jsonObjectDetail.put("FBatchNo", batchNo);
                    //库存数量
                    jsonObjectDetail.put("FQty", movePositionAmount);
                    //调出仓库编号
                    jsonObjectDetail.put("FSCStockID", erpFormerAreaCode);
                    //调出仓位编号
                    jsonObjectDetail.put("FSCSPID", formerPositionCode);
                    //调入仓库编号
                    jsonObjectDetail.put("FDCStockID", erpAreaCode);
                    //调入仓位编号
                    jsonObjectDetail.put("FDCSPID", positionCode);
                    //源单单号
                    jsonObjectDetail.put("FWMSSourceBillNo", movePositionCode);
                    //源单分录
                    jsonObjectDetail.put("FWMSSourceEntryID", 1);
                    //有效天数
                    jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                    //生产/采购日期
                    jsonObjectDetail.put("FKFDate", productDate);
                    //有效期至
                    jsonObjectDetail.put("FPeriodDate", qualityDate);

                    jsonArray.add(jsonObjectDetail);

                    if (!jsonArray.isEmpty() && jsonArray.size() > 0) {
                        jsonObject.put("detailinfo", jsonArray);
                        jsonArrayResult.add(jsonObject);
                    }
                }
            }

            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, DiaoBoURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpStockInterfaceServiceDAO.updateMovePositionErpFlag(list);
            }
        }
    }

    /**
     * 库存更新接口（调拨单  整货）
     *
     * @return
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void setRfidDiaoBo() {

        /**1.判断盘点任务单是否满足调用接口条件**/
        List<Map<String, Object>> list = erpStockInterfaceServiceDAO.selectRfidMovePosition();
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                if (map1 != null) {
                    //移位单编号
                    String movePositionCode = map1.get("move_position_code") == null ? "" : map1.get("move_position_code").toString();

                    //获取rfid
                    String rfid = map1.get("rfid") == null ? "" : map1.get("rfid").toString();

                    EntityWrapper<MaterielBindRfidDetail> wrapperMaterielBindRfidDetail = new EntityWrapper<>();
                    wrapperMaterielBindRfidDetail.eq("rfid", rfid);
                    wrapperMaterielBindRfidDetail.eq("delete_flag", DyylConstant.NOTDELETED);
                    List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = materielBindRfidDetailService.selectList(wrapperMaterielBindRfidDetail);

                    JSONObject jsonObject = new JSONObject();
                    //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                    jsonObject.put("FSManagerID", "002");
                    //验收人
                    jsonObject.put("FFManagerID", "002");
                    //制单人
                    jsonObject.put("FBillerID", "Administrator");
                    //审核人
                    jsonObject.put("FCheckerID", "Administrator");

                    JSONArray jsonArray = new JSONArray();

                    if (lstMaterielBindRfidDetail != null && lstMaterielBindRfidDetail.size() > 0) {
                        for (int i = 0; i < lstMaterielBindRfidDetail.size(); i++) {
                            MaterielBindRfidDetail materielBindRfidDetail = lstMaterielBindRfidDetail.get(i);
                            //物料编码
                            String materialCode = materielBindRfidDetail.getMaterielCode();
                            //物料名称
                            String materialName = materielBindRfidDetail.getMaterielName();
                            //批次号
                            String batchNo = materielBindRfidDetail.getBatchRule();
                            //库存数量
                            String stockAmount = materielBindRfidDetail.getAmount();
                            //生产日期
                            String productDate = materielBindRfidDetail.getProductData();
                            //根据物料编号获取保质期
                            EntityWrapper<Materiel> wrapperMateriel = new EntityWrapper<Materiel>();
                            wrapperMateriel.eq("materiel_code",materialCode);
                            Materiel materiel = materielService.selectOne(wrapperMateriel);
                            //保质期(天)
                            Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());
                            //有效期至
                            String qualityDate = erpService.setQualityDate(qualityPeriod, productDate);

                            //原库位
                            Long formerPosition = map1.get("former_position") == null ? 0 : Long.parseLong(map1.get("former_position").toString());
                            //移动库位
                            Long positionBy = map1.get("position_by") == null ? 0 : Long.parseLong(map1.get("position_by").toString());
                            //获取库位编码
                            String formerPositionCode = depotPositionDAO.selectById(formerPosition).getPositionCode();
                            String positionCode = depotPositionDAO.selectById(positionBy).getPositionCode();
                            //根据库位编号获取库区编号
                            String formerAreaCode = erpService.selectDepotAreaCode(formerPositionCode);
                            //东洋库区编号转成erp库区编号
                            String erpFormerAreaCode = erpService.changDepotPosition(formerAreaCode);
                            String areaCode = erpService.selectDepotAreaCode(positionCode);
                            //东洋库区编号转成erp库区编号
                            String erpAreaCode = erpService.changDepotPosition(areaCode);

                            //货权移库物料的入库类型(自采或客供)
                            String instorageType = erpService.getInstorageType(materialCode, batchNo, formerPositionCode);
                            if (instorageType.equals(DyylConstant.CUSTOMERSUPPLY)) {
                                continue;
                            }

                            JSONObject jsonObjectDetail = new JSONObject();
                            //行号  单据信息笔数，从1开始
                            jsonObjectDetail.put("FEntryID", i + 1);
                            //物料编码
                            jsonObjectDetail.put("FItemID", materialCode);
                            //物料名称
                            jsonObjectDetail.put("FItemName", materialName);
                            //批次号
                            jsonObjectDetail.put("FBatchNo", batchNo);
                            //库存数量
                            jsonObjectDetail.put("FQty", stockAmount);
                            //调出仓库编号
                            jsonObjectDetail.put("FSCStockID", erpFormerAreaCode);
                            //调出仓位编号
                            jsonObjectDetail.put("FSCSPID", formerPositionCode);
                            //调入仓库编号
                            jsonObjectDetail.put("FDCStockID", erpAreaCode);
                            //调入仓位编号
                            jsonObjectDetail.put("FDCSPID", positionCode);
                            //源单单号
                            jsonObjectDetail.put("FWMSSourceBillNo", movePositionCode);
                            //源单分录
                            jsonObjectDetail.put("FWMSSourceEntryID", i + 1);
                            //有效天数
                            jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                            //生产/采购日期
                            jsonObjectDetail.put("FKFDate", productDate);
                            //有效期至
                            jsonObjectDetail.put("FPeriodDate", qualityDate);

                            jsonArray.add(jsonObjectDetail);
                        }
                    }
                    if (!jsonArray.isEmpty() && jsonArray.size() > 0) {
                        jsonObject.put("detailinfo", jsonArray);
                        jsonArrayResult.add(jsonObject);
                    }
                }
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, DiaoBoURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpStockInterfaceServiceDAO.updateMovePositionErpFlag(list);
            }
        }
    }

    /**
     * 库存更新接口（其它入库） 盘盈
     *
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void SetOtherInstockIT() {

        /**1.判断盘点任务单是否满足调用接口条件**/

        List<Map<String, Object>> list = erpStockInterfaceServiceDAO.selectInventoryTask();

        //盘点任务详情
        List<Map<String, Object>> lstInventoryTaskDetail = null;
        JSONArray jsonArrayResult = new JSONArray();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                if (map1 != null) {
                    //盘点任务单id
                    String inventoryTaskId = map1.get("id") == null ? "" : map1.get("id").toString();
                    //盘点任务单编号
                    String inventoryTaskCode = map1.get("inventory_task_code") == null ? "" : map1.get("inventory_task_code").toString();

                    JSONObject jsonObject = new JSONObject();
                    //领料部门编号(必填)
                    jsonObject.put("FDeptID", "02.09");
                    //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                    jsonObject.put("FSManagerID", "002");
                    //验收人
                    jsonObject.put("FFManagerID", "002");
                    //制单人
                    jsonObject.put("FBillerID", "Administrator");
                    //审核人
                    jsonObject.put("FCheckerID", "Administrator");

                    JSONArray jsonArray = new JSONArray();

                    //根据盘点任务单id查询盘点任务单详情(符合盘盈)
                    lstInventoryTaskDetail = erpStockInterfaceServiceDAO.selecInventoryTaskDetail(inventoryTaskId);
                    if (lstInventoryTaskDetail != null && lstInventoryTaskDetail.size() > 0) {
                        for (int i = 0; i < lstInventoryTaskDetail.size(); i++) {
                            Map<String, Object> inventortTaskDetailMap = lstInventoryTaskDetail.get(i);
                            //物料编码
                            String materialCode = inventortTaskDetailMap.get("material_code") == null ? "" : inventortTaskDetailMap.get("material_code").toString();
                            //物料名称
                            String materialName = inventortTaskDetailMap.get("material_name") == null ? "" : inventortTaskDetailMap.get("material_name").toString();
                            //批次号
                            String batchNo = inventortTaskDetailMap.get("batch_no") == null ? "" : inventortTaskDetailMap.get("batch_no").toString();
                            //盘点数量
                            Double inventoryAmount = inventortTaskDetailMap.get("inventory_amount") == null ? 0.0 : Double.parseDouble(inventortTaskDetailMap.get("inventory_amount").toString());
                            //库存数量
                            Double stockAmount = inventortTaskDetailMap.get("stock_amount") == null ? 0.0 : Double.parseDouble(inventortTaskDetailMap.get("stock_amount").toString());
                            //盘盈数量
                            String inWinAmont = String.valueOf(inventoryAmount - stockAmount);
                            //库位编码
                            String positionCode = inventortTaskDetailMap.get("position_code") == null ? "" : inventortTaskDetailMap.get("position_code").toString();
                            //rfid
                            String rfid = inventortTaskDetailMap.get("rfid") == null ? "" : inventortTaskDetailMap.get("rfid").toString();
                            //依据库位编号获取库区编号
                            String areaCode = erpService.selectDepotAreaCode(positionCode);
                            //东洋库区编号转成erp库区编号
                            String erpAreaCode = erpService.changDepotPosition(areaCode);
                            //判断物料是否为客料，如果为客料则不传给erp
                            String instorageType = null;
                            if (StringUtils.isNotEmpty(rfid)) {
                                instorageType = erpService.getInstorageType(materialCode, batchNo, positionCode);
                            } else {
                                instorageType = erpService.getMaterielType(materialCode, positionCode);
                            }
                            if (instorageType.equals(DyylConstant.CUSTOMERSUPPLY)) {
                                continue;
                            }

                            //获取库存
                            EntityWrapper<Stock> wraStock = new EntityWrapper<>();
                            wraStock.eq("material_code", materialCode);
                            wraStock.eq("position_code", positionCode);
                            wraStock.eq("batch_no", batchNo);
                            if (StringUtils.isEmpty(rfid)) {
                                wraStock.eq("material_type", DyylConstant.MATERIAL_NORFID);
                            } else {
                                wraStock.eq("material_type", DyylConstant.MATERIAL_RFID);
                            }
                            Stock s = stockService.selectOne(wraStock);

                            //生产日期
                            String productDate = s.getProductDate();
                            //保质期至
                            String qualityDate = s.getQualityDate();
                            //保质期(天)
                            Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productDate);

                            JSONObject jsonObjectDetail = new JSONObject();
                            //行号  单据信息笔数，从1开始
                            jsonObjectDetail.put("FEntryID", i + 1);
                            //物料编码
                            jsonObjectDetail.put("FItemID", materialCode);
                            //物料名称
                            jsonObjectDetail.put("FItemName", materialName);
                            //批次号
                            jsonObjectDetail.put("FBatchNo", batchNo);
                            //入库数量
                            jsonObjectDetail.put("FQty", inWinAmont);
                            //仓库编号
                            jsonObjectDetail.put("FDCStockID", erpAreaCode);
                            //仓位编号
                            jsonObjectDetail.put("FDCSPID", positionCode);
                            //源单单号
                            jsonObjectDetail.put("FWMSSourceBillNo", inventoryTaskCode);
                            //源单分录
                            jsonObjectDetail.put("FWMSSourceEntryID", "");
                            //有效天数
                            jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                            //生产/采购日期
                            jsonObjectDetail.put("FKFDate", productDate);
                            //有效期至
                            jsonObjectDetail.put("FPeriodDate", qualityDate);

                            jsonArray.add(jsonObjectDetail);
                        }
                    }
                    if (!jsonArray.isEmpty() && jsonArray.size() > 0) {
                        jsonObject.put("detailinfo", jsonArray);
                        jsonArrayResult.add(jsonObject);
                    }
                }
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, OtherInstockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpStockInterfaceServiceDAO.updateInventoryTaskErpFlag(lstInventoryTaskDetail);
            }
        }
    }

    /**
     * 库存更新接口（其它出库） 盘亏
     *
     * @author pz
     * @date 2018-11-26
     */
    @Override
    public void setOtherOutstockIT() {

        /**1.判断盘点任务单是否满足调用接口条件**/

        List<Map<String, Object>> list = erpStockInterfaceServiceDAO.selectInventoryTask();
        JSONArray jsonArrayResult = new JSONArray();

        //盘点任务详情
        List<Map<String, Object>> lstInventoryTaskDetail = null;

        if (list != null && list.size() > 0) {
            for (Map<String, Object> map1 : list) {
                if (map1 != null) {
                    //盘点任务单id
                    String inventoryTaskId = map1.get("id") == null ? "" : map1.get("id").toString();
                    //盘点任务单编号
                    String inventoryTaskCode = map1.get("inventory_task_code") == null ? "" : map1.get("inventory_task_code").toString();

                    JSONObject jsonObject = new JSONObject();
                    //领料部门编号(必填)
                    jsonObject.put("FDeptID", "02.09");
                    //保管人    保管人、验收人、制单人、审核人可放相同值(待定)
                    jsonObject.put("FSManagerID", "002");
                    //验收人
                    jsonObject.put("FFManagerID", "002");
                    //制单人
                    jsonObject.put("FBillerID", "Administrator");
                    //审核人
                    jsonObject.put("FCheckerID", "Administrator");

                    JSONArray jsonArray = new JSONArray();

                    //根据盘点任务单id查询盘点任务单详情(符合盘盈)
                    lstInventoryTaskDetail = erpStockInterfaceServiceDAO.selecInventoryTaskDet(inventoryTaskId);
                    if (lstInventoryTaskDetail != null && lstInventoryTaskDetail.size() > 0) {
                        for (int i = 0; i < lstInventoryTaskDetail.size(); i++) {
                            Map<String, Object> inventortTaskDetailMap = lstInventoryTaskDetail.get(i);
                            //物料编码
                            String materialCode = inventortTaskDetailMap.get("material_code") == null ? "" : inventortTaskDetailMap.get("material_code").toString();
                            //物料名称
                            String materialName = inventortTaskDetailMap.get("material_name") == null ? "" : inventortTaskDetailMap.get("material_name").toString();
                            //批次号
                            String batchNo = inventortTaskDetailMap.get("batch_no") == null ? "" : inventortTaskDetailMap.get("batch_no").toString();
                            //盘点数量
                            Double invAmount = inventortTaskDetailMap.get("inventory_amount") == null ? 0.0 : Double.parseDouble(inventortTaskDetailMap.get("inventory_amount").toString());
                            //库存数量
                            Double stoAmount = inventortTaskDetailMap.get("stock_amount") == null ? 0.0 : Double.parseDouble(inventortTaskDetailMap.get("stock_amount").toString());
                            //库位编码
                            String positionCode = inventortTaskDetailMap.get("position_code") == null ? "" : inventortTaskDetailMap.get("position_code").toString();
                            //rfid
                            String rfid = inventortTaskDetailMap.get("rfid") == null ? "" : inventortTaskDetailMap.get("rfid").toString();
                            //根据库位编号获取库区编号
                            String areaCode = erpService.selectDepotAreaCode(positionCode);
                            //东洋库区编号转成erp库区编号
                            String erpAreaCode = erpService.changDepotPosition(areaCode);

                            //判断物料是否为客料，如果为客料则不传给erp
                            String instorageType = null;
                            if (StringUtils.isNotEmpty(rfid)) {
                                instorageType = erpService.getInstorageType(materialCode, batchNo, positionCode);
                            } else {
                                instorageType = erpService.getMaterielType(materialCode, positionCode);
                            }
                            if (instorageType.equals(DyylConstant.CUSTOMERSUPPLY)) {
                                continue;
                            }

                            //获取库存信息
                            EntityWrapper<Stock> wraStock = new EntityWrapper<>();
                            wraStock.eq("material_code", materialCode);
                            wraStock.eq("position_code", positionCode);
                            wraStock.eq("batch_no", batchNo);
                            if (StringUtils.isEmpty(rfid)) {
                                wraStock.eq("material_type", DyylConstant.MATERIAL_NORFID);
                            } else {
                                wraStock.eq("material_type", DyylConstant.MATERIAL_RFID);
                            }
                            Stock s = stockService.selectOne(wraStock);

                            //生产日期
                            String productDate = s.getProductDate();
                            //有效期至
                            String qualityDate = s.getQualityDate();
                            //保质期(天)
                            Long qualityPeriod = erpService.getQualityPeriod(qualityDate, productDate);
                            JSONObject jsonObjectDetail = new JSONObject();
                            //行号  单据信息笔数，从1开始
                            jsonObjectDetail.put("FEntryID", i + 1);
                            //物料编码
                            jsonObjectDetail.put("FItemID", materialCode);
                            //物料名称
                            jsonObjectDetail.put("FItemName", materialName);
                            //批次号
                            jsonObjectDetail.put("FBatchNo", batchNo);
                            //入库数量
                            jsonObjectDetail.put("FQty", String.valueOf(invAmount - stoAmount));
                            //仓库编号
                            jsonObjectDetail.put("FDCStockID", erpAreaCode);
                            //仓位编号
                            jsonObjectDetail.put("FDCSPID", positionCode);
                            //源单单号
                            jsonObjectDetail.put("FWMSSourceBillNo", inventoryTaskCode);
                            //源单分录
                            jsonObjectDetail.put("FWMSSourceEntryID", "");
                            //有效天数
                            jsonObjectDetail.put("FKFPeriod", qualityPeriod);
                            //生产/采购日期
                            jsonObjectDetail.put("FKFDate", productDate);
                            //有效期至
                            jsonObjectDetail.put("FPeriodDate", qualityDate);

                            jsonArray.add(jsonObjectDetail);
                        }
                    }
                    if (!jsonArray.isEmpty() && jsonArray.size() > 0) {
                        jsonObject.put("detailinfo", jsonArray);
                        jsonArrayResult.add(jsonObject);
                    }
                }
            }
            //调用接口路径传输数据给erp
            boolean result = erpService.getDate(jsonArrayResult, OtherOutStockURL);
            //如果调用接口成功，更新入库表erp_flag字段
            if (result) {
                erpStockInterfaceServiceDAO.updateInventoryTaskErpFlag(lstInventoryTaskDetail);
            }
        }
    }

}
