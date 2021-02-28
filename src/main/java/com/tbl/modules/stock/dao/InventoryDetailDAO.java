package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import com.tbl.modules.stock.entity.Stock;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 盘点详细Dao
 *
 * @author pz
 * @date 2019-01-09
 */
public interface InventoryDetailDAO extends BaseMapper<InventoryDetail> {

    Integer getMaterielCount(Map<String, Object> map);

    /**
     * @Description:  查询盘点任务详情(无rfid)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/5/5
     */
    InventoryTaskDetail getInventoryTaskDetail(@Param("inventoryTaskId") Long inventoryTaskId,@Param("positionCode") String positionCode, @Param("materialCode") String materialCode);

}
