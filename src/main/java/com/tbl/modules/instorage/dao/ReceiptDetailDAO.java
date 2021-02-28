package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @Description:  收货计划详情DAO
* @Param:
* @return:
* @Author: zj
* @Date: 2019/1/7
*/
public interface ReceiptDetailDAO extends BaseMapper<ReceiptDetail> {

    List<Map<String, Object>> getSelectMaterialList(Page page, @Param("queryString") String queryString);

    Integer getSelectMaterialTotal(@Param("queryString") String queryString);

    Integer getMaterialCount(Map<String, Object> map);

    Integer updatePlanReceiptAmount(@Param("receiptDetailId") Long receiptDetailId, @Param("planReceiptAmount") String planReceiptAmount);

    Integer updateBatchNo(@Param("receiptDetailId") Long receiptDetailId, @Param("batchNo") String batchNo);

    Integer updatePlanReceiptWeight(@Param("receiptDetailId") Long receiptDetailId, @Param("planReceiptWeight") String planReceiptWeight);

    Integer updateSeparableAmountAndWeight(@Param("detail") Map<String, Object> detail);

    Integer insertInstorageDetail(@Param("detail") Map<String, Object> detail);
    /**
    * @Description:  根据当前日期获取最大编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/16
     *
     * update by anss 2019-04-22
     * 根据批次规则获取流水号
     *
    */
    String getMaxBatchNoCode(@Param("bathNoRule") String bathNoRule);

    Integer updatePlanReceiptAmountAndWeight(@Param("receiptDetailId") Long receiptDetailId, @Param("planReceiptAmount") String planReceiptAmount);

}
