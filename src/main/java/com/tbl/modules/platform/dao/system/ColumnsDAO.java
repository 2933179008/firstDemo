package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.platform.entity.system.Columns;

import java.util.Map;

/**
 * @author anss
 * @date 2018-09-17
 */
public interface ColumnsDAO extends BaseMapper<Columns> {


    boolean insertCoumns(Map<String, Object> map);

}
