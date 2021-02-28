package com.tbl.modules.erpInterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.erpInterface.service.ReceiptErpInterfaceService;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.instorage.service.ReceiptService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @program: dyyl
 * @description: 生成收获信息
 * @author: pz
 * @create: 2019-02-15
 **/
@Service("receiptErpInterfaceService")
public class ReceiptErpInterfaceServiceImpl implements ReceiptErpInterfaceService {

    @Value("${dyyl.ERPId}")
    private Long ERPId;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptDetailService receiptDetailService;



    /**
     * 生成收货计划信息
     *
     * @param paramInfo
     * @return
     * @author pz
     * @date 2019-02-15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String receiptInfo(String paramInfo) {

        String nowDate = DateUtils.getTime();

        JSONObject resultObj = new JSONObject();
        resultObj.put("msg", "收货单添加成功！");
        resultObj.put("success", true);

        JSONObject receiptInfoObj = JSON.parseObject(paramInfo);

        //erp采购订单编码
        String erpReceiptCode = receiptInfoObj.getString("erpReceiptCode");

        //收货计划详情
        String receiptDetailInfo = receiptInfoObj.getString("receiptDetail");

        JSONArray receiptDetailArr = JSON.parseArray(receiptDetailInfo);

        // 验证采购订单编码是否为空
        if (StringUtils.isEmpty(erpReceiptCode)) {
            resultObj.put("msg", "失败原因：收货编号为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        // 验证收货计划详情是否为空
        if (StringUtils.isEmpty(receiptDetailInfo)) {
            resultObj.put("msg", "失败原因：收货计划详情为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        //客户编号
        String customerCode = receiptInfoObj.getString("customerCode");
        //客户名称
        String customerName = receiptInfoObj.getString("customerName");
        //供应商编号
        String supplierCode = receiptInfoObj.getString("supplierCode");
        //供应商名称
        String supplierName = receiptInfoObj.getString("supplierName");
        //预计收货时间
        String estimatedDeliveryTime = receiptDetailArr.getJSONObject(0).getString("estimatedDeliveryTime");
        //备注
        String remark = receiptInfoObj.getString("remark");

        if (StringUtils.isEmpty(supplierCode)) {
            resultObj.put("msg", "供应商编码不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }
        if (StringUtils.isEmpty(supplierName)) {
            resultObj.put("msg", "供应商名称不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        String receiptCode = receiptService.generateReceiptCode();
        boolean receiptResult = isReceiptResult(receiptCode, erpReceiptCode, customerCode, customerName,
                supplierCode, supplierName, estimatedDeliveryTime, remark);

        EntityWrapper<Receipt> rece = new EntityWrapper<>();
        rece.eq("receipt_code", receiptCode);
        Receipt rp = receiptService.selectList(rece).get(0);


        //将JsonArray转换成list
        List<ReceiptDetail> lstReceiptDet = JSONObject.parseArray(receiptDetailArr.toJSONString(), ReceiptDetail.class);

        //取出物料编码重复的收获计划详情   先去重然后取出去重留下的list
        //对收获计划详情 去重
        List<ReceiptDetail> uniqueReceiptDetail = lstReceiptDet.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(ReceiptDetail::getMaterialCode))), ArrayList::new)
        );

        // 3、保存收货计划详细list实体
        List<ReceiptDetail> lstReceiptDetail = new ArrayList<>();
        for (int i = 0; i < uniqueReceiptDetail.size(); i++) {
            ReceiptDetail rd = uniqueReceiptDetail.get(i);


            if (StringUtils.isEmpty(rd.getMaterialCode())) {
                resultObj.put("msg", "物料编码不能为空！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }
            rd.setSeparableAmount(rd.getPlanReceiptAmount());
            rd.setReceiptPlanId(rp.getId());

            lstReceiptDetail.add(rd);
        }
        boolean receiptDetailResult = receiptDetailService.insertBatch(lstReceiptDetail);

        //创建去重去掉的数据
        List<ReceiptDetail> repeatReceiptDetail = new ArrayList<>();
        for (int i = 0; i < lstReceiptDet.size(); i++) {
            if (lstReceiptDet.get(i).getId() == null) {
                repeatReceiptDetail.add(lstReceiptDet.get(i));
            }
        }

        //如果去重去掉的数据为空，则表明list中无重复对象,反之则需要再次创建收获单
        if (repeatReceiptDetail.size() == 0) {
            // 判断收货单是否添加成功
            if (receiptResult && receiptDetailResult) {
                resultObj.put("msg", "收货单添加成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("收货单调用接口", receiptCode, resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            } else {
                resultObj.put("msg", "失败原因：“收货单添加失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("收货单调用接口", receiptCode, resultObj.get("msg").toString(), nowDate);
            }
        } else {
            //将list转换成JSONArray
            JSONArray repeatArray = JSONArray.parseArray(JSON.toJSONString(repeatReceiptDetail));

            this.receiptInfo(ReceiptObject(erpReceiptCode, customerCode, customerName,supplierCode,
                    supplierName, estimatedDeliveryTime, remark, repeatArray));
        }

        return JSON.toJSONString(resultObj);
    }

    /**
     *
     * @return
     */
    private boolean isReceiptResult(String receiptCode, String erpReceiptCode, String customerCode, String customerName,
                                    String supplierCode, String supplierName, String estimatedDeliveryTime, String remark) {

        Receipt receipt = new Receipt();

        receipt.setReceiptCode(receiptCode);
        receipt.setErpReceiptCode(erpReceiptCode);
        receipt.setCustomerCode(customerCode);
        receipt.setCustomerName(customerName);
        receipt.setSupplierCode(supplierCode);
        receipt.setSupplierName(supplierName);
        receipt.setEstimatedDeliveryTime(estimatedDeliveryTime);
        receipt.setDocumentType("0");
        receipt.setRemark(remark);
        receipt.setCreateTime(DateUtils.getTime());
        receipt.setCreateBy(ERPId);

        return receiptService.insert(receipt);
    }


    private String ReceiptObject(String erpReceiptCode, String customerCode, String customerName,String supplierCode,
                             String supplierName, String estimatedDeliveryTime, String remark, JSONArray repeatArray) {
        JSONObject receiptObj = new JSONObject();

        receiptObj.put("erpReceiptCode",  erpReceiptCode);
        receiptObj.put("customerCode", customerCode);
        receiptObj.put("customerName", customerName);
        receiptObj.put("supplierCode", supplierCode);
        receiptObj.put("supplierName", supplierName);
        receiptObj.put("estimatedDeliveryTime", estimatedDeliveryTime);
        receiptObj.put("remark", remark);

        receiptObj.put("receiptDetail", repeatArray);

        return JSON.toJSONString(receiptObj);
    }



}
    