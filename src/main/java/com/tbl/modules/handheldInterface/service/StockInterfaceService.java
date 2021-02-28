package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.Stock;

import java.util.Map;

/**
 * 库存接口Service
 */
public interface StockInterfaceService extends IService<Stock> {


    /**
     * 更改库存RIFD
     *
     * @param stock
     * @return
     */
    boolean changeStrok(Stock stock,String rfids);

    /**
     * 库存查询
     *
     * @param rfid
     * @param materielCode
     * @param positionCode
     * @param batchRule
     * @param barcode
     * @return
     */
    Map<String, Object> queryStock(String rfid,String materielCode, String positionCode, String batchRule, String barcode);

    /**
     * 货权转移
     *
     * @param id
     * @param materielId
     * @param userId
     * @return
     */
    Map<String, Object> getMaterielDroitShift(Long id, Long materielId,String batchNo, Long userId,String documentType);
}
