package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 盘点任务详情Dao
 *
 * @author pz
 * @date 2019-01-14
 */
public interface InventoryTaskDetailDAO extends BaseMapper<InventoryTaskDetail> {

    List<Map<String, Object>> getSelectPositionList(Page page, @Param("queryString") String queryString);

    Integer getSelectPositionTotal(@Param("queryString") String queryString);

}
