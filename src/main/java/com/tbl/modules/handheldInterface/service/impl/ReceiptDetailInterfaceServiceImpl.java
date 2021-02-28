package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.handheldInterface.dao.ReceiptDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.service.ReceiptDetailInterfaceService;
import com.tbl.modules.instorage.dao.InstorageDAO;
import com.tbl.modules.instorage.dao.ReceiptDAO;
import com.tbl.modules.instorage.dao.ReceiptDetailDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货接口Service
 *
 * @author yuany
 * @date 2018-02-19
 */
@Service(value = "receiptDteailInterfaceService")
public class ReceiptDetailInterfaceServiceImpl extends ServiceImpl<ReceiptDetailInterfaceDAO, ReceiptDetail> implements ReceiptDetailInterfaceService {

    //日志接口
    @Autowired
    private InterfaceLogService interfaceLogService;

    //收货单详情DAO
    @Autowired
    private ReceiptDetailInterfaceDAO receiptDetailInterfaceDAO;

    //收获单详情Service
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private MaterielService materielService;

    @Autowired
    private ReceiptDAO receiptDAO;
    @Autowired
    private ReceiptDetailDAO receiptDetailDAO;
    @Autowired
    private InstorageService instorageService;
    @Autowired
    private InstorageDAO instorageDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateInstorage(Long userId, String receiptPlanId, String receiptPlanDetailIdStr,
                                  String inStorageAmountStr, String inStorageWeightStr, String batchNoStr, String productDateStr,String qualityPeriodStr) {
        if (StringUtils.isNotBlank(receiptPlanId) && StringUtils.isNotBlank(receiptPlanDetailIdStr) && StringUtils.isNotBlank(inStorageAmountStr)
                && StringUtils.isNotBlank(inStorageWeightStr) && StringUtils.isNotBlank(batchNoStr) && StringUtils.isNotBlank(productDateStr)) {
            /**1.入库单插入数据**/
            //当前时间
            String nowTime = DateUtils.getTime();

            //根据收货单id获取收货单信息
            Receipt receipt = receiptDAO.selectById(receiptPlanId);
            //自动生成入库单编号
            String instorageCode = instorageService.generateInstorageCode();

            Instorage instorage = new Instorage();
            instorage.setInstorageBillCode(instorageCode);
            instorage.setInstorageType(receipt.getDocumentType());
            instorage.setReceiptPlanId(Long.parseLong(receiptPlanId));
            instorage.setCustomerCode(receipt.getCustomerCode());
            instorage.setCustomerName(receipt.getCustomerName());
            instorage.setSupplierCode(receipt.getSupplierCode());
            instorage.setSupplierName(receipt.getSupplierName());
            instorage.setRemark("收货单生成入库单");
            instorage.setState("0");
            instorage.setCreateTime(nowTime);
            instorage.setUpdateTime(nowTime);
            instorage.setCreateBy(userId);
            //入库单插入数据
            instorageDAO.insert(instorage);

            /**2.入库单详情插入数据**/
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
            String[] receiptPlanDetailIdArr = receiptPlanDetailIdStr.split(",");
            String[] inStorageAmountArr = inStorageAmountStr.split(",");
            String[] inStorageWeightArr = inStorageWeightStr.split(",");
            String[] batchNoArr = batchNoStr.split(",");
            String[] productDateArr = productDateStr.split(",");
            String[] qualityPeriodArr = qualityPeriodStr.split(",");

            for (int i = 0; i < receiptPlanDetailIdArr.length; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                //入库单id
                map.put("instorageBillId", instorage.getId());
                map.put("receiptPlanDetailId", receiptPlanDetailIdArr[i]);
                map.put("inStorageAmount", inStorageAmountArr[i]);
                map.put("inStorageWeight", inStorageWeightArr[i]);

                //获取收货单详情
                ReceiptDetail receiptDetail = receiptDetailDAO.selectById(receiptPlanDetailIdArr[i]);
                //获取物料基础信息
                Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",receiptDetail.getMaterialCode()));
                materiel.setQualityPeriod(qualityPeriodArr[i]);
                //更新保质期
                materielService.updateById(materiel);

                //update by anss 2019-04-22 start
                //批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
                String batchNoCode = generateBatchNoCode(instorage.getSupplierCode(), instorage.getCustomerCode(), batchNoArr[i]);
                map.put("batchNo", batchNoCode);
                //update by anss 2019-04-22 end

                map.put("productDate", productDateArr[i]);
                //入库单详情插入数据
                receiptDetailDAO.insertInstorageDetail(map);

                /**3.根据收货单详情id更新收货计划详情的可拆分数量和可拆分重量**/
                receiptDetailDAO.updateSeparableAmountAndWeight(map);
            }

            /**4.根据收货单id更新收货计划单的状态为收货中**/
            receiptDAO.updateStateToReceipt(receiptPlanId);

            //查询收货详情单更新后的可拆分数量和可拆分重量
            EntityWrapper<ReceiptDetail> entity = new EntityWrapper<ReceiptDetail>();
            entity.eq("receipt_plan_id", receiptPlanId);
            List<ReceiptDetail> lstReceiptDetail = receiptDetailDAO.selectList(entity);
            if (lstReceiptDetail != null && lstReceiptDetail.size() > 0) {
                //可拆分数量总和
                Double separableAmountTotal = 0d;
                //可拆分重量总和
                Double separableWeightTotal = 0d;
                for (ReceiptDetail receiptDetail : lstReceiptDetail) {
                    separableAmountTotal += receiptDetail.getSeparableAmount() == null ? 0d : Double.parseDouble(receiptDetail.getSeparableAmount());
                    separableWeightTotal += receiptDetail.getSeparableWeight() == null ? 0d : Double.parseDouble(receiptDetail.getSeparableWeight());
                }
                //如果可拆分数量总和小于等于0并且可拆分重量总和小于等于0，则将收货单状态更新为收货完成
                if (separableAmountTotal <= 0 && separableWeightTotal <= 0) {
                    /**5.根据收货单id更新收货计划单的状态为已完成**/
                    receiptDAO.updateStateToComplete(receiptPlanId);
                }
            }

        }

    }

    /**
     * 根据收货单ID获取收货单详情
     *
     * @param receiptPlanId
     * @return
     */
    @Override
    public Map<String, Object> getReceiptDetailList(String receiptPlanId) {

        boolean result = true;
        String msg = "获取详情成功";
        Map<String, Object> map = new HashMap<>();

        List<ReceiptDetail> receiptDetailList = null;

        String errorinfo = null;
        //若收货单ID不为空则获取收货单详情
        if (StringUtils.isNotBlank(receiptPlanId)) {
            receiptDetailList = receiptDetailInterfaceDAO.selectList(
                    new EntityWrapper<ReceiptDetail>()
                            .eq("receipt_plan_id", receiptPlanId)
            );
            //若集合为空则返回提示信息
            if (receiptDetailList.isEmpty()) {
                msg = "未找到关于此收货单的详细";
                result = false;
            } else {
                for (int i = 0; i < receiptDetailList.size(); i++) {
                    ReceiptDetail receiptDetail = receiptDetailList.get(i);
                    if (receiptDetail.getSeparableAmount().equals("0") && receiptDetail.getSeparableWeight().equals("0")) {
                        receiptDetailList.remove(i);
                    }else {
                        //获取基础物料信息
                        Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",receiptDetail.getMaterialCode()));
                        receiptDetail.setQualityPeriod(materiel.getQualityPeriod());
                    }
                }
            }
        } else {
            msg = "失败原因：收货单ID为空！";
            result = false;
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "收货详情调用接口";
        String parameter = "ReceiptPlanId:" + receiptPlanId;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", receiptDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 生成入库单
     *
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> getReceiptDetail(Map<String, Object> paramMap) {

        boolean result = true;
        String msg = "已生成入库单";
        Map<String, Object> map = new HashMap<>();

        String receiptPlanId = paramMap.get("receiptPlanId").toString();
        Long userId = Long.parseLong(paramMap.get("userId").toString());

        String receiptPlanDetailIdStr = null;
        String inStorageAmountStr = null;
        String inStorageWeightStr = null;
        String productDateStr = null;
        String batchNoStr = null;
        String qualityPeriodStr = null;
        String errorinfo = null;
        if (!Strings.isNullOrEmpty(receiptPlanId)) {
            Gson gson = new Gson();
            List<ReceiptDetail> receiptDetailList = gson.fromJson(paramMap.get("data").toString(),
                    new TypeToken<List<ReceiptDetail>>() {
                    }.getType());

            for (ReceiptDetail receiptDetail : receiptDetailList) {
                if (Strings.isNullOrEmpty(receiptPlanDetailIdStr)) {
                    receiptPlanDetailIdStr = receiptDetail.getId().toString();
                } else {
                    receiptPlanDetailIdStr = receiptPlanDetailIdStr + "," + receiptDetail.getId();
                }

                if (Strings.isNullOrEmpty(inStorageAmountStr)) {
                    inStorageAmountStr = receiptDetail.getActualReceiptAmount();
                } else {
                    inStorageAmountStr = inStorageAmountStr + "," + receiptDetail.getActualReceiptAmount();
                }

                if (Strings.isNullOrEmpty(inStorageWeightStr)) {
                    inStorageWeightStr = receiptDetail.getActualReceiptWeight();
                } else {
                    inStorageWeightStr = inStorageWeightStr + "," + receiptDetail.getActualReceiptWeight();
                }

                if (Strings.isNullOrEmpty(batchNoStr)) {

                    batchNoStr = receiptDetail.getBatchNo();
                } else {
                    batchNoStr = batchNoStr + "," + receiptDetail.getBatchNo();
                }
                if (Strings.isNullOrEmpty(productDateStr)) {
                    productDateStr = receiptDetail.getProductDate();
                } else {
                    productDateStr = productDateStr + "," + receiptDetail.getProductDate();
                }
                if (Strings.isNullOrEmpty(qualityPeriodStr)) {
                    qualityPeriodStr = receiptDetail.getQualityPeriod();
                } else {
                    qualityPeriodStr = qualityPeriodStr + "," + receiptDetail.getQualityPeriod();
                }


            }

            try {
                generateInstorage(userId, receiptPlanId, receiptPlanDetailIdStr, inStorageAmountStr, inStorageWeightStr, batchNoStr, productDateStr,qualityPeriodStr);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
                msg = "系统错误！";
                errorinfo = DateUtils.getTime();
            }

        } else {
            result = false;
            msg = "失败原因：参数未获取";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "调用收货生成入库单接口";
        String parameter = "ReceiptPlanId:" + receiptPlanId + "/UserId" + userId;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);


        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
     *
     * @param supplierCode 供应商代码
     * @param customerCode 客户代码
     * @param date         日期（yymmdd,6位）
     *                     SerialNumber 流水号（1，最长3位）
     * @return
     * @Author update by anss
     * @Date 2019-04-22
     */
    @Override
    public String generateBatchNoCode(String supplierCode, String customerCode, String date) {
        //供应商代码补位符7位*
        supplierCode = String.format("%-7s", supplierCode).replace(' ', '*');
        //客户代码补位符9位*
        customerCode = String.format("%-9s", customerCode).replace(' ', '*');
        //批次规则
        String bathNoRule = supplierCode + customerCode + "-" + date + "-";
        //流水号
        String seriaNumber = receiptDetailDAO.getMaxBatchNoCode(bathNoRule);

        return bathNoRule + seriaNumber;
    }
}

