package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ReceiptDetailService extends IService<ReceiptDetail> {
    /**
     * @Description:  收货单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    PageUtils queryPage(Map<String, Object> map);

    /**
     * @Description:  获取物料下拉列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    List<Map<String, Object>> getSelectMaterialList(String queryString, int pageSize, int pageNo);

    /**
     * @Description:  获取物料下拉列表数据总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    Integer getSelectMaterialTotal(String queryString);

    /**
     * @Description:  判断物料是否已添加
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    boolean hasMaterial(Long receiptId, String materialCodes);

    /**
     * @Description:  保存物料详情
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    boolean saveReceiptDetail(Long receiptId, String materialCodes);

    /**
     * @Description:  根据收货单详情id获取收货单状态
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    String getReceiptStateByDetailId(Long receiptDetailId);

    /**
     * @Description:  更新收货单详情的计划收货数量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    boolean updatePlanReceiptAmount(Long receiptDetailId, String planReceiptAmount);

    /**
     * @Description:  删除收货单详情（物料详情）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    boolean deleteReceiptDetail(String ids);

    /**
     * @Description:  更新收货单详情的批次号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    boolean updateBatchNo(Long receiptDetailId, String batchNo);

    /**
     * @Description:  更新收货单详情的计划收货重量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    boolean updatePlanReceiptWeight(Long receiptDetailId, String planReceiptWeight);

    /**
     * @Description:  生成入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/14
     */
    void generateInstorage(String receiptPlanId,String receiptPlanDetailIdStr, String inStorageAmountStr,
                           String inStorageWeightStr,String batchNoStr,String productDateStr,String qualityPeriodStr);

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
