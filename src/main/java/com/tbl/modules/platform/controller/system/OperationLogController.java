package com.tbl.modules.platform.controller.system;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.platform.service.system.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志Controller
 *
 * @author yuany
 * @date 2019-1-2
 */
@Controller
@RequestMapping(value = "/operationLog")
public class OperationLogController extends AbstractController {

    //操作日志service
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 跳转到日志列表界面
     * @author yuany
     * @date 2019-01-01
     * @return ModelAndView
     * */
    @RequestMapping(value="/toView.do")
    public ModelAndView toView(){
        ModelAndView mv=this.getModelAndView();
        mv.setViewName("techbloom/platform/system/operationLog/operation_log_list");
        return mv;
    }

    /**
     * 获取日志列表数据
     * @author yuany
     * @date 2019-01-01
     * @param queryJsonString
     * @return Map
     * */
    @RequestMapping(value="/list.do")
    @ResponseBody
    public Map<String,Object> listLog(String queryJsonString){

        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = operationLogService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

}
