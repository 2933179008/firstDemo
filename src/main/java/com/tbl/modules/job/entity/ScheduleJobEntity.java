package com.tbl.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务
 *
 * @author anss
 * @date 2018-11-28
 */
@Getter
@Setter
@TableName("schedule_job")
public class ScheduleJobEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务调度参数key
	 */
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	/**
	 * 任务id
	 */
	@TableId
	private Long jobId;

	/**
	 * spring bean名称
	 */
	@NotBlank(message="bean名称不能为空")
	private String beanName;

	/**
	 * 方法名
	 */
	@NotBlank(message="方法名称不能为空")
	private String methodName;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * cron表达式
	 */
	@NotBlank(message="cron表达式不能为空")
	private String cronExpression;

	/**
	 * 任务状态
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;


}
