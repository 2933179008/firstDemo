package com.tbl.modules.external.service;

/**
 * erp 库存接口
 */
public interface ErpStockService {

    /**
     * 货权转移(散货)
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void transferView();

    /**
     * 库存更新接口(调拨单 散货)
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setDiaoBo();

    /**
     * 库存更新接口(调拨单 整货)
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setRfidDiaoBo();

    /**
     * 库存更新接口（其他入库）盘盈
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void SetOtherInstockIT();

    /**
     * 库存更新接口（其他出库）盘亏
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setOtherOutstockIT();

}
