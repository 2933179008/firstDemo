package com.tbl.modules.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.job.dao.ScheduleJobLogDao;
import com.tbl.modules.job.entity.ScheduleJobLogEntity;
import com.tbl.modules.job.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String jobId = (String)params.get("jobId");

		Page<ScheduleJobLogEntity> page = this.selectPage(
				new Query<ScheduleJobLogEntity>(params).getPage(),
				new EntityWrapper<ScheduleJobLogEntity>().like(StringUtils.isNotBlank(jobId),"job_id", jobId)
		);

		return new PageUtils(page);
	}

}
