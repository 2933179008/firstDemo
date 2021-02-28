package com.tbl.modules.alarm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.alarm.entity.Alarm;

import java.util.List;
import java.util.Map;

/**
 * 预警Dao
 *
 * @author yuany
 * @date 2019-01-31
 */
public interface AlarmDAO extends BaseMapper<Alarm> {

    Alarm getOneTimeout(String bindcode);

    Alarm getOneQualityTimeout(String bindcode);
}
