package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点任务详情接口DAO
 */
public interface InventoryTaskDetailInterfaceDAO extends BaseMapper<InventoryTaskDetail> {

    /**
     * 通过盘点任务id获取盘点库位
     */
    List<InventoryTaskDetail> getInventoryTaskPosition(@Param("taskId")Long taskId);

    /**
     * 通过盘点任务ID获取盘点单id获取盘点库位
     */
    List<InventoryDetail> getInventoryPosition(@Param("taskId")Long taskId);

    //获取盘点任务详情
    InventoryTaskDetail getInventoryTaskDetail(@Param("inventoryTaskId")Long inventoryTaskId,
                                               @Param("materielCode")String materielCode,
                                               @Param("batchNo")String batchNo,@Param("positionCode")String positionCode);
}
