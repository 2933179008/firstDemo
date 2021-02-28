package com.tbl.modules.outstorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.outstorage.service.LowerShelfDetailService;
import com.tbl.modules.outstorage.service.LowerShelfService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 下架单管理控制类
 * @author: zj
 * @create: 2018-12-29 10:38
 **/
@Controller
@RequestMapping(value = "/lowerShelfBillManager")
public class LowerShelfBillManagerController extends AbstractController {

    @Autowired
    private LowerShelfService lowerShelfService;

    @Autowired
    private LowerShelfDetailService lowerShelfDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    /**
    * @Description:  跳转到下架单管理页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2018/12/29
    */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/outstorage/lowerShelfBill_list");
        return mv;
    }

    /**
    * @Description:  获取下架单管理列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2018/12/29
    */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        PageTbl pageTbl = this.getPage();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        map.put("page", pageTbl.getPageno());
        map.put("limit", pageTbl.getPagesize());
        String sortName = pageTbl.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "lowerShelfBillCode";
            pageTbl.setSortname(sortName);
        }
        String sortOrder = pageTbl.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            pageTbl.setSortorder(sortOrder);
        }
        map.put("sidx", pageTbl.getSortname());
        map.put("order", pageTbl.getSortorder());
        PageUtils pageUtils = lowerShelfService.queryPage(map);
        pageTbl.setTotalRows(pageUtils.getTotalCount() == 0 ? 1 : pageUtils.getTotalCount());
        map.put("rows", pageUtils.getList());
        executePageMap(map, pageTbl);
        return map;
    }

    /**
    * @Description:  跳转到下架详情页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2018/12/29
    */
    @RequestMapping(value = "/toDetailView")
    @ResponseBody
    public ModelAndView toDetailView(String id) {
        ModelAndView mv = new ModelAndView();
        //通过ID获取对应的下架单的数据
        LowerShelfBillVO lowerShelfBillVO = lowerShelfService.getLowerShelfVO(id);
        mv.setViewName("techbloom/outstorage/lowerShelfBill_edit");
        mv.addObject("lowerShelfBillVO",lowerShelfBillVO);
        if(StringUtils.isNotBlank(lowerShelfBillVO.getUserId())){
            User u = userService.selectById(lowerShelfBillVO.getUserId());
            mv.addObject("username",u.getUsername());
        }
        return mv;
    }

    /**
    * @Description:  获取下架单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2018/12/29
    */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String id) {
        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();

        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("lowerShelfBillId", id);
        PageUtils PagePlatform = lowerShelfDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount()==0?1:PagePlatform.getTotalCount());
        map.put("rows",PagePlatform.getList());
        executePageMap(map,page);
        map.put("rows",PagePlatform.getList());

        return map;
    }


    /**
     * 通过详情ID获取对应的详情的信息
     * @param lowerDetailId        下架单详情ID
     * @return
     */
    @RequestMapping(value="/confirmLowerShelf")
    @ResponseBody
    public Map<String,Object> confirmLowerShelf(String lowerDetailId){
        Map<String,Object> map = Maps.newHashMap();
        LowerShelfBillDetailVO lowerShelfBillDetailVO = lowerShelfDetailService.selectById(lowerDetailId);

        //若无操作人，则获取当前登陆人更新未操作人
        if (lowerShelfBillDetailVO!=null && StringUtils.isNotEmpty(lowerShelfBillDetailVO.getLowerShelfBillId())){
            LowerShelfBillVO lowerShelfBillVO = lowerShelfService.selectById(lowerShelfBillDetailVO.getLowerShelfBillId());
            if (lowerShelfBillVO!=null && Strings.isNullOrEmpty(lowerShelfBillVO.getUserId())){
                lowerShelfBillVO.setUserId(getUserId().toString());
                lowerShelfService.updateById(lowerShelfBillVO);
            }
        }

        map = lowerShelfDetailService.confirmLowerShelf(lowerShelfBillDetailVO,lowerDetailId,1);
        return map;
    }

    /**
     * 跳转到对应的任务分配的界面
     * @param lowerId
     * @return
     */
    @RequestMapping(value="/lowerAllocation")
    public ModelAndView lowerAllocation(String lowerId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("lowerId",lowerId);
        //获取对应的操作人
        List<Map<String,Object>> userList = lowerShelfService.getUserList();
        mv.addObject("userList",userList);
        mv.setViewName("techbloom/outstorage/lowerShelf_operation");
        return mv;
    }

    /**
     * 更新操作人
     * @param lowerId
     * @param userId
     * @return
     */
    @RequestMapping(value="/updateOperation")
    @ResponseBody
    public Map<String,Object> updateOperation(String lowerId,String userId){
        Map<String,Object> map = lowerShelfService.updateOperation(lowerId,userId);
        return map;
    }

    /**
     * 通过下架单的ID进行删除操作
     * @param ids
     * @return
     */
    @RequestMapping(value="/delLower")
    @ResponseBody
    public Map<String,Object> delLower(String ids){
        Map<String,Object> map = Maps.newHashMap();
        map = lowerShelfService.delLower(ids);
        return map;
    }

}
    