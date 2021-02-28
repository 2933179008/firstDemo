package com.tbl.modules.external.dao;

import java.util.List;
import java.util.Map;

/**
 * erp出库接口
 */
public interface ErpOutstorageServiceDAO {

    /**
     * 获取对应的销毁出库的为未上传的订单的信息
     * @return
     */
    List<Map<String,Object>> getDestoryOutstorage();

    /**
     * 获取对应自采的越库出库的为未上传的订单的信息
     * @return
     */
    List<Map<String,Object>> getCusCrossOutstorage();

    /**
     * 获取对应客供的越库出库的为未上传的订单的信息
     * @return
     */
    List<Map<String,Object>> getCrossOutstorage();

    /**
     * 通过出库单的ID查询对应的详情数据
     * @param outStorageId
     * @return
     */
    List<Map<String,Object>> getDetailList(String outStorageId);

    /**
     * 批量更新数据
     * @param list
     * @return
     */
    Integer updateOutstorageBillErpFlag(List<Map<String,Object>> list);

    /**
     * 获取出库类型为自采退货出库的单据
     * @return
     */
    List<Map<String,Object>> getReturnList();


    /**
     * 获取生产领料
     * type 0表示自产 1表示客供
     * @return
     */
    List<Map<String,Object>> getMaterialRequisition(String type);

}
