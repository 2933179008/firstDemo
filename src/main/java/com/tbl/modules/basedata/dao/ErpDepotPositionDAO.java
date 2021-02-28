package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.ErpDepotPosition;

/**
 * erp库位管理Dao
 *
 * @author pz
 * @date 2019-04-29
 */
public interface ErpDepotPositionDAO extends BaseMapper<ErpDepotPosition> {

    /**
     * 获取库位名称
     *
     * @return List<DepotPosition>
     * @author yuany
     * @date 2019-01-04
     */
    String selectPositionName(String positionCode);
}
