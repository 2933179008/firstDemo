package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.Producer;

import java.util.List;

/**
 * @author pz
 * @date 2019-01-02
 */
public interface ProducerDAO extends BaseMapper<Producer> {

    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Supplier>
     * */
    List<Producer> getAllListP(List<Long> ids);



}
