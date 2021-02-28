package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.stock.entity.Inventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 盘点Dao
 *
 * @author pz
 * @date 2019-01-08
 */
public interface InventoryDAO extends BaseMapper<Inventory> {

    /**
     * 获取最大盘点编号
     *
     * @author pz
     * @date 2019-01-18
     */
    String getMaxInventoryCode();

    /**
     * 获取导出列
     *
     * @return List<Inventory>
     * @author pz
     * @date 2019-01-10
     */
    List<Inventory> getAllListI(List<Long> ids);

    Integer updateState(Long id);

    /**
     * 获取用户列表数据
     *
     * @return
     * @author pz
     * @date 2019-01-15
     */
    List<Inventory> selectList(Pagination page, Map<String, Object> params, Long id);

    /**
     * 获取详细列表数据
     *
     * @return
     * @author pz
     * @date 2019-01-10
     */
    List<Inventory> selectListS(Pagination page, Map<String, Object> params, @Param("id") Long id);

    /**
     * @Description: 获取库存下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectPositionList(Page page, @Param("inventoryTaskId") Long inventoryTaskId, @Param("queryString") String queryString);

    /**
     * @Description: 获取库存下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    Integer getSelectPositionTotal(@Param("inventoryTaskId") Long inventoryTaskId, @Param("queryString") String queryString);

    /**
     * @Description: 获取物料下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectMaterielList(Page page, @Param("inventoryTaskId") Long inventoryTaskId, @Param("positionCode") String positionCode, @Param("queryString") String queryString);

    /**
     * @Description: 获取物料下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    Integer getSelectMaterielTotal(@Param("inventoryTaskId") Long inventoryTaskId, @Param("positionCode") String positionCode, @Param("queryString") String queryString);

}
