package com.tbl.modules.job.controller;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.job.service.ScheduleJobLogService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务日志
 *
 * @author anss
 * @date 2018-11-28
 */
@RestController
@RequestMapping(value = "/scheduleLog")
public class ScheduleJobLogController extends AbstractController {

	@Autowired
	private ScheduleJobLogService scheduleJobLogService;


	@RequestMapping(value = "/toList.do")
	@ResponseBody
	public ModelAndView toList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("techbloom/job/scheduleLog_list");
		return mv;
	}

	/**
	 * 定时任务列表
	 * @author anss
	 * @date 2018-11-18
	 * @param queryJsonString
	 * @return Map
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	public Map<String, Object> getList(String queryJsonString) {
		Map<String,Object> map = new HashMap<>();
		if (!StringUtils.isEmpty(queryJsonString)) {
			map = JSON.parseObject(queryJsonString);
		}

		PageTbl page = this.getPage();
		map.put("page",page.getPageno());
		map.put("limit",page.getPagesize());
		map.put("sidx", page.getSortname());
		map.put("order", page.getSortorder());
		PageUtils PageScheduleLog = scheduleJobLogService.queryPage(map);
		page.setTotalRows(PageScheduleLog.getTotalCount()==0?1:PageScheduleLog.getTotalCount());
		map.put("rows",PageScheduleLog.getList());
		executePageMap(map,page);
		return map;
	}
}
