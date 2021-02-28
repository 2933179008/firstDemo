package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.Inventory;

import java.util.Map;

/**
 * 盘点单Service
 */
public interface InventoryInterfaceService extends IService<Inventory> {

    /**
     * @Description: 生成盘点编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    String generateInventoryCode();

    /**
     * 盘点记录提交
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> doInventory(Map<String, Object> paramMap);

    Map<String,Object> doInventoryTaskStatus(Long taskId);

}
