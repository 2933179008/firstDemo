package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 收货详情接口Service
 *
 * @author yuany
 * @date 2019-02-18
 */
public interface ReceiptDetailInterfaceService extends IService<ReceiptDetail> {

    /**
     * @Description:  生成入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/14
     */
    void generateInstorage(Long userId,String receiptPlanId,String receiptPlanDetailIdStr,
                           String inStorageAmountStr, String inStorageWeightStr,String batchNoStr,String productDateStr,String qualityPeriodStr);

    /**
     * 根据收货单ID获取收货单详情
     *
     * @param receiptPlanId
     * @return
     */
    Map<String, Object> getReceiptDetailList(String receiptPlanId);

    /**
     * 生成入库单
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> getReceiptDetail(Map<String, Object> paramMap);

    /**
     * @Description:  生成批次号序列号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/16
     *
     * 批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
     * @Author update by anss
     * @Date 2019-04-22
     */
    String generateBatchNoCode(String supplierCode, String customerCode, String date);
}
