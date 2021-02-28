package com.tbl.modules.erpInterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.Bom;
import com.tbl.modules.basedata.entity.BomDetail;
import com.tbl.modules.basedata.service.BomDetailService;
import com.tbl.modules.basedata.service.BomService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.erpInterface.service.SpareBillErpInterfaceService;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.outstorage.service.impl.SpareBillServiceImpl;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: dyyl
 * @description: 生成备料单信息
 * @author: pz
 * @create: 2019-02-15
 **/
@Service("spareBillErpInterfaceService")
public class SpareBillErpInterfaceServiceImpl implements SpareBillErpInterfaceService {

    @Value("${dyyl.ERPId}")
    private Long ERPId;

    //备料单Service
    @Autowired
    private SpareBillService spareBillService;

    //备料单详情Service
    @Autowired
    private SpareBillDetailService spareBillDetailService;

    @Autowired
    private SpareBillServiceImpl spareBillServiceImpl;

    @Autowired
    private DepotPositionDAO depotPositionDAO;

    //bomService
    @Autowired
    private BomService bomService;

    //bomDetailService
    @Autowired
    private BomDetailService bomDetailService;

    //预警Service
    @Autowired
    private AlarmService alarmService;

    //接口日志service
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 根据Key去重
     *
     * @param arrayInfo
     */
    public String uniqueArray(String arrayInfo) {
        String nowDate = DateUtils.getTime();
        JSONArray array = JSON.parseArray(arrayInfo);

        JSONArray arrayTemp = new JSONArray();
        int num = 0;
        for (int i = 0; i < array.size(); i++) {
            if (num == 0) {
                arrayTemp.add(array.get(i));
            } else {

                int numJ = 0;
                for (int j = 0; j < arrayTemp.size(); j++) {
                    JSONObject newJsonObjectI = (JSONObject) array.get(i);
                    JSONObject newJsonObjectJ = (JSONObject) arrayTemp.get(j);
                    Long bomId = newJsonObjectI.getLong("bomId");
                    String materialCode = newJsonObjectI.getString("materialCode");
                    String materialName = newJsonObjectI.getString("materialName");
                    String materialType = newJsonObjectI.getString("materialType");
                    String amount = newJsonObjectI.getString("amount");
                    String weight = newJsonObjectI.getString("weight");
                    String unitPrice = newJsonObjectI.getString("unitPrice");
                    String updateTime = newJsonObjectI.getString("updateTime");
                    String deletedFlag = newJsonObjectI.getString("deletedFlag");
                    Long deletedBy = newJsonObjectI.getLong("deletedBy");

                    String materielCode = newJsonObjectJ.getString("materialCode");
                    if (materialCode.equals(materielCode)) {
                        arrayTemp.remove(j);
                        JSONObject newObject = new JSONObject();
                        newObject.put("bomId", bomId);
                        newObject.put("materialCode", materialCode);
                        newObject.put("materialName", materialName);
                        newObject.put("materialType", materialType);
                        newObject.put("amount", amount);
                        newObject.put("weight", weight);
                        newObject.put("unitPrice", unitPrice);
                        newObject.put("creatTime", nowDate);
                        newObject.put("updateTime", updateTime);
                        newObject.put("deletedFlag", deletedFlag);
                        newObject.put("deletedBy", deletedBy);
                        arrayTemp.add(newObject);
                        break;
                    }
                    numJ++;
                }
                if (numJ - 1 == arrayTemp.size() - 1) {
                    arrayTemp.add(array.get(i));
                }
            }
            num++;
        }

        return JSON.toJSONString(arrayTemp);
    }

    /**
     * 生成备料单信息
     *
     * @param spareInfo bomInfo 参数集合
     * @return
     * @author pz
     * @date 2019-02-15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String spareBillInfo(String spareInfo) {

        String nowDate = DateUtils.getTime();

        JSONObject resultObj = new JSONObject();
        resultObj.put("msg", "未进行操作！");
        resultObj.put("success", true);

        JSONObject spareBillInfoObj = JSON.parseObject(spareInfo);

        //备料单详情
        String spareBillDetailInfo = spareBillInfoObj.getString("spareBillDetail");
        //产品编号
        String productCode = spareBillInfoObj.getString("productCode");
        //产品名称
        String productName = spareBillInfoObj.getString("productName");
        //备料单编号
        String erpSpareBillCode = spareBillInfoObj.getString("spareBillCode");

        JSONArray spareBillDetailArr = JSON.parseArray(spareBillDetailInfo);

        // 验证备料单编号是否为空
        if (StringUtils.isEmpty(erpSpareBillCode)) {
            resultObj.put("msg", "失败原因：备料单编号为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        // 验证备料单编号是否唯一
        EntityWrapper<SpareBillManagerVO> pareEntity = new EntityWrapper<>();
        pareEntity.eq("erp_spare_bill_code", erpSpareBillCode);
        int count = spareBillService.selectCount(pareEntity);

        // 验证收货生产任务详情是否为空
        if (StringUtils.isEmpty(spareBillDetailInfo)) {
            resultObj.put("msg", "失败原因：收货计划详情为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        List<SpareBillManagerVO> lstSpareBill = spareBillService.selectList(pareEntity);
        if (lstSpareBill.size() == 0) {
            //使用开始预定时间
            String userStartTime = spareBillInfoObj.getString("userStartTime");
            //总生产数量
            String totalProductAmount = spareBillInfoObj.getString("totalProductAmount");
            //调配室使用线
            String mixUseLine = spareBillInfoObj.getString("mixUseLine");
            //特殊事项
            String specialMatter = spareBillInfoObj.getString("specialMatter");
            //生成出库单时间
            String receiptStartTime = spareBillInfoObj.getString("receiptStartTime");
            //生产任务编号
            String productNo = spareBillInfoObj.getString("productNo");
            //备注
            String remark = spareBillInfoObj.getString("remark");

            if (count > 0) {
                resultObj.put("msg", "失败原因：erp备料单编号重复！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }

            SpareBillManagerVO spareBill = new SpareBillManagerVO();
            spareBill.setSpareBillCode(spareBillServiceImpl.getMaxBillCode());
            spareBill.setErpSpareBillCode(erpSpareBillCode);
            spareBill.setUserStartTime(userStartTime);
            spareBill.setProductCode(productCode);
            spareBill.setProductName(productName);
            spareBill.setTotalProductAmount(totalProductAmount);
            spareBill.setMixUseLine(mixUseLine);
            spareBill.setSpecialMatter(specialMatter);
            spareBill.setReceiptStartTime(receiptStartTime);
            spareBill.setCreateBy(ERPId);
            spareBill.setCreateTime(nowDate);
            spareBill.setProductNo(productNo);
            spareBill.setRemark(remark);
            spareBill.setState("0");

            boolean spareResult = spareBillService.insert(spareBill);

            EntityWrapper<SpareBillManagerVO> sem = new EntityWrapper<>();
            sem.eq("erp_spare_bill_code", erpSpareBillCode);
            SpareBillManagerVO sm = spareBillService.selectList(sem).get(0);

            List<SpareBillDetailManagerVO> lstSpareBillDetail = new ArrayList<>();
            SpareBillDetailManagerVO sbdm = null;
            for (int i = 0; i < spareBillDetailArr.size(); i++) {
                JSONObject spareBillDetailObj = spareBillDetailArr.getJSONObject(i);

                //行号
                Long line = spareBillDetailObj.getLong("line");
                //物料编码
                String materialCode = spareBillDetailObj.getString("materialCode");
                //物料名称
                String materialName = spareBillDetailObj.getString("materialName");
                //库位编码
                String positionCode = spareBillDetailObj.getString("positionCode");
                String positionName = null;
                //依据库位编码获取库位名称
                if(StringUtils.isNotEmpty(positionCode)){
                    positionName = depotPositionDAO.selectPositionName(positionCode);
                }
                //使用数量
                String usedAmount = spareBillDetailObj.getString("usedAmount");
                //仓库发货数量
                String sendAmount = spareBillDetailObj.getString("sendAmount");
                //准备数量
                String quantityReady = spareBillDetailObj.getString("quantityReady");
                //生产剩余数量
                String surplusAmount = spareBillDetailObj.getString("surplusAmount");
                //计量日期
                String measurementTime = spareBillDetailObj.getString("measurementTime");
                //备料单显示顺序
                String orderBy = String.valueOf(i+1);
                //使用箱数
                String usedBox = spareBillDetailObj.getString("usedBox");
                //单箱重量
                String usedWeight = spareBillDetailObj.getString("usedWeight");

                sbdm = new SpareBillDetailManagerVO();
                sbdm.setSpareBillId(sm.getId());
                sbdm.setLine(line);
                sbdm.setMaterialCode(materialCode);
                sbdm.setMaterialName(materialName);
                sbdm.setPositionCode(positionCode);
                sbdm.setPositionName(positionName);
                sbdm.setUserAmount(usedAmount);
                sbdm.setSendAmount(sendAmount);
                sbdm.setQuantityReady(quantityReady);
                sbdm.setSurplusAmount(surplusAmount);
                sbdm.setMeasurementTime(measurementTime);
                sbdm.setUsedBox(usedBox);
                sbdm.setUsedWeight(usedWeight);
                sbdm.setOrderBy(orderBy);

                lstSpareBillDetail.add(sbdm);
            }
            boolean spareBillDetailResult = spareBillDetailService.insertBatch(lstSpareBillDetail);

            // 判断备料单是否添加成功
            if (spareResult && spareBillDetailResult) {
                resultObj.put("msg", "备料单添加成功！");
                resultObj.put("success", true);
            } else {
                resultObj.put("msg", "失败原因：“备料单添加失败”！");
                resultObj.put("success", false);
            }
            interfaceLogService.interfaceLogInsert("备料单调用接口", erpSpareBillCode, resultObj.get("msg").toString(), nowDate);
        }
        EntityWrapper<Bom> wraBom = new EntityWrapper<>();
        wraBom.eq("product_code", productCode);
        List<Bom> lstBom = bomService.selectList(wraBom);

        //判断bom中是否存在产品编码的信息
        Bom b = null;
        String bomDetailInfo = this.uniqueArray(spareBillDetailInfo);
        JSONArray bomDetailArr = JSON.parseArray(bomDetailInfo);
        if (lstBom.size() == 0) {
            b = new Bom();

            b.setBomCode(bomService.generatBomCode());
            b.setBomName("Bom" + productName);
            b.setProductCode(productCode);
            b.setProductId(null);
            b.setProductName(productName);
            b.setState("0");
            b.setCreateTime(nowDate);
            boolean bomResult = bomService.insert(b);

            EntityWrapper<Bom> wraBom1 = new EntityWrapper<>();
            Bom bom = bomService.selectList(wraBom1.eq("product_code", productCode)).get(0);

            List<BomDetail> lstBomDetail = new ArrayList<>();
            BomDetail bomDetail = null;
            for (int i = 0; i < bomDetailArr.size(); i++) {
                JSONObject bomDetailObj = bomDetailArr.getJSONObject(i);
                bomDetail = new BomDetail();
                bomDetail.setBomId(bom.getId());
                bomDetail.setMaterialCode(bomDetailObj.getString("materialCode"));
                bomDetail.setMaterialName(bomDetailObj.getString("materialName"));
                bomDetail.setCreateTime(nowDate);
                lstBomDetail.add(bomDetail);
            }
            boolean bomDetailResult = bomDetailService.insertBatch(lstBomDetail);
            // 判断bom和bom详细是否添加成功
            if (bomResult && bomDetailResult) {
                resultObj.put("msg", "bom添加成功！");
                resultObj.put("success", true);
            } else {
                resultObj.put("msg", "失败原因：“bom添加失败”！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }
        } else {
            EntityWrapper<Bom> wraB = new EntityWrapper<>();
            Bom Boml = bomService.selectList(wraB.eq("product_code", productCode)).get(0);

            EntityWrapper<BomDetail> wraBomDetail = new EntityWrapper<>();
            List<BomDetail> listBomDetail = bomDetailService.selectList(wraBomDetail.eq("bom_id", Boml.getId()));

            JSONArray arrayList = JSONArray.parseArray(JSON.toJSONString(listBomDetail));
            JSONArray arrayList1 = JSONArray.parseArray(JSON.toJSONString(listBomDetail));

            JSONArray bomDetailArr1 = JSON.parseArray(bomDetailInfo);

            int sizeArr = bomDetailArr.size();
            int sizeList = arrayList1.size();
            for (int a = 0; a < sizeArr; a++) {
                JSONObject bomDetailObj = bomDetailArr1.getJSONObject(a);
                for (int c = 0; c < sizeList; c++) {
                    JSONObject bomDetailObj1 = arrayList1.getJSONObject(c);
                    if (bomDetailObj.getString("materialCode").equals(bomDetailObj1.getString("materialCode"))) {
                        arrayList.remove(bomDetailObj1);
                        bomDetailArr.remove(bomDetailObj);
                        break;
                    }
                }
            }

            listBomDetail.clear();
            listBomDetail = JSONObject.parseArray(arrayList.toJSONString(), BomDetail.class);

            if (bomDetailArr.size() != 0 || listBomDetail.size() != 0) {
                boolean addBomFlag = false;
                boolean delBomFlag = false;
                for (int k = 0; k < listBomDetail.size(); k++) {
                    addBomFlag = bomDetailService.deleteById(listBomDetail.get(k));
                }

                List<BomDetail> lstBomDetail = new ArrayList<>();
                BomDetail bomDet = null;
                for (int i = 0; i < bomDetailArr.size(); i++) {
                    JSONObject bomDetailObj = bomDetailArr.getJSONObject(i);
                    bomDet = new BomDetail();
                    bomDet.setMaterialCode(bomDetailObj.getString("materialCode"));
                    bomDet.setBomId(Boml.getId());
                    bomDet.setMaterialName(bomDetailObj.getString("materialName"));
                    lstBomDetail.add(bomDet);
                }

                if(lstBomDetail.size()>0){
                    delBomFlag = bomDetailService.insertBatch(lstBomDetail);
                }

                if (addBomFlag&&delBomFlag) {
                    alarmService.addAlarm(null,DyylConstant.ALARM_BOM,alarmService.getAlarmCode());
                    resultObj.put("msg", "bom修改成功！");
                    resultObj.put("success", true);
                    return JSON.toJSONString(resultObj);
                }
            }
        }
        interfaceLogService.interfaceLogInsert("bom调用接口", productCode, resultObj.get("msg").toString(), nowDate);

        return JSON.toJSONString(resultObj);
    }


}
    