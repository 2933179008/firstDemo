package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.Receipt;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 收货计划DAO
 * @author: zj
 * @create: 2019-01-07 13:34
 **/
public interface ReceiptDAO extends BaseMapper<Receipt> {

    String getMaxReceiptCode();

    List<Map<String, Object>> getSelectSupplierList(Page page, @Param("queryString") String queryString);

    Integer getSelectSupplierTotal(@Param("queryString") String queryString);

    List<Map<String, Object>> getSelectCustomerList(Page page, @Param("queryString") String queryString);

    Integer getSelectCustomerTotal(@Param("queryString") String queryString);

    Integer updateState(Long receiptId);

    /**
     * @Description: 根据收货单id更新收货计划单的状态为收货中
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/22
     */
    void updateStateToReceipt(String receiptPlanId);

    /**
     * @Description: 根据收货单id更新收货计划单的状态为已完成
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/22
     */
    void updateStateToComplete(String receiptPlanId);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     * */
    List<Receipt> getAllLists(List<Long> ids);
}
    