package com.tbl.modules.alarm.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预警提醒controller
 *
 * @author yuany
 * @date 2019-01-31
 */
@Controller
@RequestMapping(value = "/alarm")
public class AlarmController extends AbstractController {


    /**
     * 预警Service
     */
    @Autowired
    private AlarmService alarmService;

    /**
     * 库区Service
     */
    @Autowired
    private DepotAreaService depotAreaService;

    /**
     * 跳转到预警列表
     *
     * @return
     * @author yuany
     * @date 2019-01-31
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/alarm/alarm_list");

        List<DepotArea> areaList = depotAreaService.selectList(
                new EntityWrapper<>()
        );
        mv.addObject("areaList", areaList);

        return mv;
    }

    /**
     * @Description: 获取预警管理列表
     * @return:
     * @Author: yuany
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!com.tbl.common.utils.StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = alarmService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 未处理状态更改
     *
     * @param sceneId
     * @return
     * @author yuany
     * @date 2019-02-01
     */
    @RequestMapping(value = "stateDispose")
    @ResponseBody
    public boolean stateDispose(String sceneId) {
        //如果sceneId为空，则返回false
        if (StringUtils.isEmpty(sceneId)) {
            return false;
        }

        return alarmService.dispose(sceneId);
    }

    /**
     * 跳转到弹出的查看页面
     *
     * @return
     * @author yuany
     * @date 2019-02-01
     */
    @RequestMapping(value = "/lookAlarm")
    @ResponseBody
    public ModelAndView lookAlarm(Long sceneId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("sceneId", sceneId);
        mv.setViewName("techbloom/alarm/alarm_edit");

        return mv;
    }

    /**
     * 根据ID获取查看预警信息
     *
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-02-01
     */
    @RequestMapping(value = "/getAlarmById")
    @ResponseBody
    public Map<String, Object> getAlarmById(String id) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("id", id);
        PageUtils PagePlatform = alarmService.getAlarmById(map);
        page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
        map.put("rows", PagePlatform.getList());
        executePageMap(map, page);
        return map;
    }

//    @RequestMapping(value = "/test")
//    @ResponseBody
//    public boolean test() {
//        Long areaBy=Long.parseLong("2");
//        return alarmService.addAlarm(areaBy,"0");
//    }
}
