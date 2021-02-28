package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.Inventory;

/**
 * 盘点单DAO
 */
public interface InventoryInterfaceDAO extends BaseMapper<Inventory> {

    /**
     * 获取最大盘点编号
     * @author pz
     * @date 2019-01-18
     * */
    String getMaxInventoryCode();
}
