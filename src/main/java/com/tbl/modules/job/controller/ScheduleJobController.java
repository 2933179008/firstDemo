package com.tbl.modules.job.controller;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.common.validator.ValidatorUtils;
import com.tbl.modules.job.entity.ScheduleJobEntity;
import com.tbl.modules.job.service.ScheduleJobService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务
 *
 * @author anss
 * @date 2018-11-28
 */
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleJobController extends AbstractController {
    @Autowired
    private ScheduleJobService scheduleJobService;


    /**
     * 跳转到月台列表界面
     * @author anss
     * @date 2018-11-13
     * @return ModelAndView
     */
    @RequestMapping(value = "/toList.do")
    @ResponseBody
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/job/schedule_list");
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
        PageUtils PageSchedule = scheduleJobService.queryPage(map);
        page.setTotalRows(PageSchedule.getTotalCount()==0?1:PageSchedule.getTotalCount());
        map.put("rows",PageSchedule.getList());
        executePageMap(map,page);
        return map;
    }

    /**
     * 跳转到添加/编辑页面
     * @author anss
     * @date 2018-11-28
     * @param jobId
     * @return ModelAndView
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long jobId) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/job/schedule_edit");
        ScheduleJobEntity schedule = null;

        //修改
        if (jobId != -1) {
            schedule = scheduleJobService.selectById(jobId);
        } else {
            schedule = new ScheduleJobEntity();
            schedule.setCreateTime(new Date());
        }

        mv.addObject("schedule", schedule);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 保存定时任务
     */
    @RequestMapping("/save")
    @ResponseBody
    public boolean save(ScheduleJobEntity scheduleJob){
        ValidatorUtils.validateEntity(scheduleJob);
        if (scheduleJob.getJobId() == null) {
            scheduleJobService.save(scheduleJob);
        } else {
            scheduleJobService.update(scheduleJob);
        }
        return true;
    }

    /**
     * 删除定时任务
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Map<String, Object> delete(Long[] jobIds) {
        Map<String, Object> map = new HashMap<>();
        try {
            scheduleJobService.deleteBatch(jobIds);
            map.put("msg", "删除成功！");
        } catch (Exception e) {
            map.put("msg", "删除失败，请稍后重试。");
        }

        return map;
    }

    /**
     * 立即执行任务
     */
    @RequestMapping("/run")
    @ResponseBody
    public Map<String, Object> run(Long[] jobIds){
        Map<String, Object> map = new HashMap<>();
        try {
            scheduleJobService.run(jobIds);
            map.put("msg", "操作成功！");
        } catch (Exception e) {
            map.put("msg", "操作失败，请稍后重试。");
        }

        return map;
    }

    /**
     * 暂停定时任务
     */
    @RequestMapping("/pause")
    @ResponseBody
    public Map<String, Object> pause(Long[] jobIds){
        Map<String, Object> map = new HashMap<>();
        try {
            scheduleJobService.pause(jobIds);
            map.put("msg", "操作成功！");
        } catch (Exception e) {
            map.put("msg", "操作失败，请稍后重试。");
        }

        return map;
    }

    /**
     * 恢复定时任务
     */
    @RequestMapping("/resume")
    @ResponseBody
    public Map<String, Object> resume(Long[] jobIds){
        Map<String, Object> map = new HashMap<>();
        try {
            scheduleJobService.resume(jobIds);
            map.put("msg", "操作成功！");
        } catch (Exception e) {
            map.put("msg", "操作失败，请稍后重试。");
        }

        return map;
    }

}
