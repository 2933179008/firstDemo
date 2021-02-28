package com.tbl.modules.slab.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.slab.service.SugarInstorageSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 平板白糖入库控制类
 * @author: zj
 * @create: 2019-03-05 11:25
 **/
@Controller
@RequestMapping(value = "/sugarInstorageSlab")
public class SugarInstorageSlabController extends AbstractController {
    @Autowired
    private SugarInstorageSlabService sugarInstorageSlabService;

    /**
     * @Description:  跳转到平板白糖入库页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/27
     */
    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/sugarInstorageSlab/sugarInstorageSlab_list");
        return mv;
    }

    /**
    * @Description:  获取平板白糖入库列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/5
    */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String,Object> getList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        Page<PutBill> sugarInstoragePageList =  sugarInstorageSlabService.queryPage(map);
        page.setTotalRows(sugarInstoragePageList.getTotal()==0?1:sugarInstoragePageList.getTotal());
        map.put("rows",sugarInstoragePageList.getRecords());
        executePageMap(map,page);
        return map;
    }

}
    