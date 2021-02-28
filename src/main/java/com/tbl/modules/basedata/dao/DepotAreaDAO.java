package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.DepotArea;

import java.util.List;

/**
 * 库区管理Dao
 *
 * @author yuany
 * @date 2019-01-04
 */
public interface DepotAreaDAO extends BaseMapper<DepotArea> {

    /**
     * 获取导出列
     *
     * @return List<DepotArea>
     * @author yuany
     * @date 2019-01-04
     */
    List<DepotArea> getAllLists(List<Long> ids);
}
