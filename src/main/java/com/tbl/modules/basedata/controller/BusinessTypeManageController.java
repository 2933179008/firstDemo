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
 * 出入库业务类型管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/businessTypeManage")
public class BusinessTypeManageController extends AbstractController {

    /**
     * 跳转到出入库业务类型管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/businessTypeManage/businessTypeManage_list");
        return mv;
    }

    /**
     * 获取出入库业务类型列表数据
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Object getBusinessList() {
        Map<String, Object> map = new HashMap<>();
        JSONArray array = new JSONArray();
        JSONObject object = null;
        for (int i = 0; i < 5; i++) {
            object = new JSONObject();
            object.put("id", i);

            if (i == 0) {
                object.put("code", "OUT21524"+ i);
                object.put("businessType", i);
                object.put("outOfInType", "1");
                object.put("date", "2018-09-16 12:23:56");
                object.put("remarks", "备注");
            } else if (i == 1) {
                object.put("code", "IN21535"+ i);
                object.put("businessType", i);
                object.put("outOfInType", "2");
                object.put("date", "2018-09-30 12:23:56");
                object.put("remarks", "");
            } else if (i == 2) {
                object.put("code", "OUT21524"+ i);
                object.put("businessType", i);
                object.put("outOfInType", "1");
                object.put("date", "2018-11-16 12:23:56");
                object.put("remarks", "线上需要");
            } else if (i == 3) {
                object.put("code", "OUT21524"+ i);
                object.put("businessType", i);
                object.put("outOfInType", "1");
                object.put("date", "2018-12-19 12:23:56");
                object.put("remarks", "");
            } else {
                object.put("code", "IN21535"+ i);
                object.put("businessType", i);
                object.put("outOfInType", "2");
                object.put("date", "2018-10-16 06:23:56");
                object.put("remarks", "详情");
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
        mv.setViewName("techbloom/basedata/businessTypeManage/businessTypeManage_edit");
        return mv;
    }
}
