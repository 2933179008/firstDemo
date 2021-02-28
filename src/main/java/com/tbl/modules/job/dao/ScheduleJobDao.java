package com.tbl.modules.job.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.job.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * 定时任务
 *
 * @author anss
 * @date 2018-11-28
 */
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
