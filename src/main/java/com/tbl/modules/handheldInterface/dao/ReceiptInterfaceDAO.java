package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.instorage.entity.Receipt;

/**
 * 收货DAO
 *
 * @author yuany
 * @date 2019-02-18
 */
public interface ReceiptInterfaceDAO extends BaseMapper<Receipt> {

    /**
     * @Description:  根据收货单id更新收货计划单的状态为收货中
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/22
     */
    void updateStateToReceipt(String receiptPlanId);

    /**
     * @Description:  根据收货单id更新收货计划单的状态为已完成
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/22
     */
    void updateStateToComplete(String receiptPlanId);
}
