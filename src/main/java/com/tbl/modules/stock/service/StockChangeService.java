package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.StockChange;

import java.util.Map;


/**
 * 库存变动Service
 *
 * @author pz
 * @date 2019-01-08
 */
public interface StockChangeService extends IService<StockChange> {

    /**
     * 获取库存变动分页列表
     *
     * @param parms
     * @return
     * @author pz
     * @date 2019-01-08
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * @Description:  插入库存变动记录
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    boolean saveStockChange(String changeCode, String materialCode, String materialName, String batchNo, String changType, String outAmount, String inAmount, String balanceAmount, Long positionBy, Long userId);

}
