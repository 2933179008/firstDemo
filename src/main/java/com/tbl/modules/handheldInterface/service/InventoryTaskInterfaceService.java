package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.InventoryTask;

import java.util.Map;

/**
 * 盘点任务接口Service
 */
public interface InventoryTaskInterfaceService extends IService<InventoryTask> {

    /**
     * 盘点任务获取
     *
     * @param inventoryUserId
     * @return
     */
    Map<String, Object> getInventoryTask(Long inventoryUserId);

    /**
     * 盘点任务提交
     *
     * @param id
     * @return
     */
    Map<String, Object> doInventoryTask(Long id);

}
