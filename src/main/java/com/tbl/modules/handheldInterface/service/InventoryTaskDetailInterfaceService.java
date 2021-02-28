package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTaskDetail;

import java.util.List;
import java.util.Map;

/**
 * 盘点任务详情接口Service
 */
public interface InventoryTaskDetailInterfaceService extends IService<InventoryTaskDetail> {

    /**
     * 通过盘点任务id获取盘点库位
     */
    List<InventoryTaskDetail> getTaskPosition(Long taskId);

    /**
     * 通过盘点任务ID获取盘点单id获取盘点库位
     */
    List<InventoryDetail> getInventoryPosition(Long taskId);

    /**
     * 盘点任务详情获取
     *
     * @param inventoryTaskId
     * @return
     */
    Map<String, Object> getInventoryTaskDetail(Long inventoryTaskId);

    /**
     * 通过盘点任务ID获取盘点任务详情库位及盘点记录详情库位
     *
     * @param taskId
     * @return
     */
    Map<String, Object> getPosition(Long taskId);

}
