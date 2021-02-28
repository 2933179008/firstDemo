package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.Stock;

import java.util.Map;


/**
 * 库存Service
 * @author pz
 * @date 2019-01-08
 */
public interface StockService extends IService<Stock> {

    /**
     * 获取库存分页列表
     *
     * @param parms
     * @return
     * @author pz
     * @date 2019-01-08
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 获取库存详细分页列表
     * @param params
     * @return
     * @author pz
     * @date 2019-01-10
     */
    PageUtils queryPageS(Map<String, Object> params,Long stockId);

    /**
     * select查询盘点类型数据字典
     */
    Page<Stock> getSelectPositionList(Map<String, Object> map);

}
