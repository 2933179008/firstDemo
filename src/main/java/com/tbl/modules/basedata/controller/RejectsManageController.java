package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 不良品管理Controller
 *
 * @author yuany
 * @date 2018-12-28
 */
@Controller
@RequestMapping(value = "/rejectsManage")
public class RejectsManageController extends AbstractController {

    /**
     * 不良品管理
     *
     * @return
     * @author yuany
     * @date 2018-12-28
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/rejectsManage/rejectsManage_list");
        return mv;
    }

    /**
     * 不良品列表数据
     *
     * @return
     * @author yuany
     * @date 2018-12-28
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Object getRejectsList() {
        Map<String, Object> map = new HashMap<>();
        JSONArray array = new JSONArray();
        JSONObject object = null;
        for (int i = 0; i < 5; i++) {
            object = new JSONObject();
            object.put("id", i);
            object.put("materielCode", "PT2153468"+i);
            if (i == 0) {
                object.put("code", "OUT-0101010" + i);
                object.put("libraryArea", "G-11"+i);
                object.put("rejectsType", i);
                object.put("date", "2018-11-19 03:53:24");
                object.put("remarks", "");
                object.put("states", "待处理");
                object.put("dutyPerson","赵斌");
            } else if (i == 1) {
                object.put("code", "IN-0101010" + i);
                object.put("libraryArea", "F-01"+i);
                object.put("rejectsType", i);
                object.put("date", "2018-11-16 02:53:24");
                object.put("remarks", "");
                object.put("states", "已处理");
                object.put("dutyPerson","李虎");
            } else if (i == 2) {
                object.put("code", "IN-0101010" + i);
                object.put("libraryArea", "E-01"+i);
                object.put("rejectsType", i);
                object.put("date", "2018-12-16 18:03:24");
                object.put("remarks", "详情描述");
                object.put("states", "待处理");
                object.put("dutyPerson","周涛");
            } else if (i == 3) {
                object.put("code", "OUT-0101010" + i);
                object.put("libraryArea", "D-01"+i);
                object.put("rejectsType",i);
                object.put("date", "2018-12-16 06:53:24");
                object.put("remarks", "");
                object.put("states", "已处理");
                object.put("dutyPerson","王景略");
            } else {
                object.put("code", "IN-0101010" + i);
                object.put("libraryArea", "B-01"+i);
                object.put("rejectsType", i);
                object.put("date", "2018-12-16 12:53:24");
                object.put("remarks", "略");
                object.put("states", "已处理");
                object.put("dutyPerson","贾源");
            }
            array.add(object);
        }
        map.put("rows", array);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @author yuany
     * @date 2018-12-28
     * @return
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/rejectsManage/rejectsManage_edit");
        return mv;
    }
}
