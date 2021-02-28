package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.Bom;

public interface BomDAO extends BaseMapper<Bom> {

    /**
     * @Description: 获取最大bom编号
     * @Param:
     * @return
     * @author pz
     * @date 2019-02-25
     * */
    String getMaxBomCode();
}
