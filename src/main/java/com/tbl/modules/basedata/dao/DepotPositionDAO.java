package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.DepotPosition;

import java.util.List;

/**
 * 库位管理Dao
 *
 * @author yuany
 * @date 2019-01-04
 */
public interface DepotPositionDAO extends BaseMapper<DepotPosition> {

    /**
     * 获取导出列
     *
     * @return List<DepotPosition>
     * @author yuany
     * @date 2019-01-04
     */
    List<DepotPosition> getAllLists(List<Long> ids);

    /**
     * 获取库位名称
     *
     * @return List<DepotPosition>
     * @author yuany
     * @date 2019-01-04
     */
    String selectPositionName(String positionCode);
}
