package com.tbl.modules.alarm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.alarm.entity.AlarmDeploy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预警部署DAO
 */
public interface AlarmDeployDAO extends BaseMapper<AlarmDeploy> {

    List<Map<String, Object>> getSelectUserList(Page page, @Param("queryString") String queryString);

    Integer getSelectUserTotal(@Param("queryString") String queryString);

    AlarmDeploy getOneBytype(String type);
}
