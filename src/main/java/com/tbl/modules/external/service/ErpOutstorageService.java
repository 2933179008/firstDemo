package com.tbl.modules.external.service;

/**
 * erp 出库接口
 */
public interface ErpOutstorageService {

    /**
     * 销毁出库
     */
    void destoryOutstorage();

    /**
     * 越库出库接口(自采)
     */
    void cusCrossOutstorage();

    /**
     * 越库出库接口(客供)
     */
    void crossOutstorage();

    /**
     * 自采退货
     */
    void SelfCollectionAndReturn();

    /**
     * 公司领料
     */
    void materialcompanyRequisition();

    /**
     * 客供领料
     */
    void materialCustomerRequisition();

}
