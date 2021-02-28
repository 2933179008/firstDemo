package com.tbl.modules.external.service;


/**
 * erp 入库提供接口
 */
public interface ErpInterfaceService {

    /**
     * 采购入库单
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setPOInstock();

    /**
     * 受托加工入库单接口
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setSTJGInStock();

    /**
     * 生产退库单及生产用料分摊接口(公司料)
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void setSCOutstockRed();

    /**
     * 生产退库单及生产用料分摊接口(客料)
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    void SetSTOutstockRed();

}
