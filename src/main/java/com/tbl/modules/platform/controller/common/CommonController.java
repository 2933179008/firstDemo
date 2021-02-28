package com.tbl.modules.platform.controller.common;

import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.service.common.CommonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 公共控制器
 * @author anss
 * @create
 */
@Controller
public class CommonController extends AbstractController {

    @Resource(name="commonService")
    private CommonService commonService;

    /**
     * 保存用户自定义列
     * @param columns 列
     * @param uuid 唯一标志
     * @return Map
     */
    @RequestMapping(value = "/dynamicColumns/saveColumn")
    @ResponseBody
    public Map<String,Object> dynamicColumns(String columns,String uuid){
        return commonService.dynamicColumns(columns,uuid+getSessionUser().getUsername());
    }
    /**
     * 获取用户自定义列
     * @param uuid 唯一标志
     * @return Map
     */
    @RequestMapping(value = "/dynamicColumns/getColumns")
    @ResponseBody
    public Map<String,Object> dynamicGetColumns(String uuid){
        return commonService.dynamicGetColumns(uuid+getSessionUser().getUsername());
    }
}