package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.stock.entity.Inventory;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 盘点任务Dao
 * @Param:
 * @return:
 * @author pz
 * @date 2019-01-14
 */
public interface InventoryTaskDAO extends BaseMapper<InventoryTask> {

    /**
     * @Description: 获取导出列
     * @Param:
     * @return List<InventoryTask>
     * @author pz
     * @date 2019-01-14
     * */
    List<InventoryTask> getAllListT(List<Long> ids);

    /**
     * @Description: 修改状态
     * @Param:
     * @return List<InventoryTask>
     * @author pz
     * @date 2019-01-14
     * */
    Integer updateState(Long id);

    /**
     * @Description: 查询库存ID
     * @Param:
     * @return List<InventoryTask>
     * @author pz
     * @date 2019-01-14
     * */
    Long selectBYCode(String PositionCode);

    /**
     * @Description: 修改状态
     * @Param:
     * @return
     * @author pz
     * @date 2019-01-14
     * */
    Integer updateInventoryTask(Long id);

    /**
     * @Description: 获取最大盘点任务编号
     * @Param:
     * @return
     * @author pz
     * @date 2019-01-18
     * */
    String getMaxInventoryTaskCode();

    /**
     * 根据主键查询盘点任务
     *
     * @param id 任务主键
     * @return InventoryTask
     * @author pz
     * @date 2019-01-25
     */
    InventoryTask queryForInventoryTask(@Param("id") Long id);


    List<Map<String, Object>> getSelectPositionList(Page page, @Param("queryString") String queryString, @Param("positionCode") String positionCode);

    List<Long> selectByPosition(@Param("creatTime") String creatTime, @Param("endTime") String endTime);

    /**
     * 依据盘点任务编码与库位编码查询盘点记录
     *
     * @param inventoryTaskId 盘点任务编码 positionCode 库位编码
     * @return Inventory
     * @author pz
     * @date 2019-04-25
     */
    Inventory selectByInventory(@Param("inventoryTaskId")Long inventoryTaskId,@Param("positionCode")String positionCode);

    /**
     * 查询盘点记录详情(有rfid)
     *
     * @param
     * @return Inventory
     * @author pz
     * @date 2019-04-25
     */
    InventoryDetail selectByRfidInventoryDetail(@Param("positionCode")String positionCode, @Param("materialCode")String materialCode,@Param("batchNo")String batchNo, @Param("rfid")String rfid);

    /**
     * 查询盘点记录详情(无rfid)
     *
     * @param
     * @return Inventory
     * @author pz
     * @date 2019-04-25
     */
    InventoryDetail selectByInventoryDetail(@Param("positionCode")String positionCode, @Param("materialCode")String materialCode,@Param("batchNo")String batchNo);
}
