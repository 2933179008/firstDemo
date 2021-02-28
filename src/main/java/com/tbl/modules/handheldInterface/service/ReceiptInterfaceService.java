package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.Receipt;

import java.util.Map;

/**
 * 收货接口Service
 *
 * @author yuany
 * @date 2019-02-18
 */
public interface ReceiptInterfaceService extends IService<Receipt> {

    /**
     * 获取收货单
     * @return
     */
    Map<String, Object> getReceiptList();
}
