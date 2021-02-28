package com.tbl.modules.platform.controller.system;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口日志controller
 *
 * @author anss
 * @date 2018-09-16
 */
@Controller
@RequestMapping(value = "/interfaceLog")
public class InterfaceLogController extends AbstractController {

    //接口日志service
    @Autowired
    private InterfaceLogService interfaceLogService;



    /**
     * 跳转到日志列表界面
     * @author anss
     * @date 2018-09-16
     * @return ModelAndView
     * */
    @RequestMapping(value="/toView.do")
    public ModelAndView toView(){
        ModelAndView mv=this.getModelAndView();
        mv.setViewName("techbloom/platform/system/interfaceLog/interface_log_list");
        return mv;
    }

    /**
     * 获取日志列表数据
     * @author anss
     * @date 2018-09-16
     * @param queryJsonString
     * @return Map
     * */
    @RequestMapping(value="/list")
    @ResponseBody
    public Map<String,Object> listLog(String queryJsonString){

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
        PageUtils utils = interfaceLogService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

}
