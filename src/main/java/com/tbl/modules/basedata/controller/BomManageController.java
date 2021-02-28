package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Bom;
import com.tbl.modules.basedata.entity.BomDetail;
import com.tbl.modules.basedata.service.BomDetailService;
import com.tbl.modules.basedata.service.BomService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.stock.entity.InventoryTask;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * BOM管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/bomManage")
public class BomManageController extends AbstractController {

    @Autowired
    private BomService bomService;
    @Autowired
    private BomDetailService bomDetailService;

    /**
     * 跳转到BOM管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/bomManage/bomManage_list");
        return mv;
    }

    /**
     * 获取BOM列表数据
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getBomList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils PagePlatform = bomService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount()==0?1:PagePlatform.getTotalCount());
        map.put("rows",PagePlatform.getList());
        executePageMap(map,page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @author yuany
     * @date 2018-12-28
     * @return
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(String id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/bomManage/bomManage_edit");
//        if(!StringUtils.isEmptyString(id)){
//            Bom bom = bomService.selectById(id);
//            mv.addObject("bom",bom);
//        }

        if (!StringUtils.isEmptyString(id)) {
            Bom bom = bomService.selectById(id);
            mv.addObject("bomCode", bom.getBomCode());
            mv.addObject("bom",bom);
        } else {
            //获取bom编号
            String bomCode = bomService.generatBomCode();
            mv.addObject("bomCode", bomCode);
        }

        return mv;
    }

    /**
     * 判断客户编码是否存在
     *  @author pz
     *  @date 2019-01-08
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String bomCode,Long id) {
        boolean errInfo = true;
        // 根据bomCode查询实体list
        EntityWrapper<Bom> wraBom = new EntityWrapper<>();
        wraBom.eq("bom_code",bomCode);
        try {
            if(id != null)
            {
                int count = bomService.selectCount(wraBom);
                if(count>1){
                    errInfo = false;
                }
            }
            else{
                int count = bomService.selectCount(wraBom);
                if(count>0){
                    errInfo = false;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return errInfo;
    }

    /**
    * @Description:  保存/修改bom
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/saveBom")
    @ResponseBody
    public boolean saveBom(Bom bom){
        boolean result = false;
        if(bom != null){
            //当前时间
            String nowTime = DateUtils.getTime();
            if(bom.getId() != null){//修改
                bom.setCreateTime(nowTime);
                result = bomService.updateById(bom);
            }else{//新增
                bom.setCreateTime(nowTime);
                bom.setUpdateTime(nowTime);
                result = bomService.insert(bom);
            }
        }
        return result;
    }

    /**
    * @Description:  删除bom（逻辑删除）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/deleteBom")
    @ResponseBody
    public boolean deleteBom(String ids) {
        boolean result = false;
        Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);

        List<Long> lstIds = Arrays.stream(ids.split(",")).map(a -> Long.parseLong(a)).collect(Collectors.toList());
        List<Bom> bomList = bomService.selectBatchIds(lstIds);
        for(Bom bom : bomList){
            bom.setDeletedFlag(DyylConstant.DELETED);
            bom.setDeletedBy(user.getUserId());
        }
        //逻辑删除bom主表数据
        boolean bomResult = bomService.updateBatchById(bomList);

        //逻辑删除bom详情表数据
        EntityWrapper<BomDetail> wrapper = new EntityWrapper<>();
        wrapper.in("bom_id",lstIds);
        BomDetail bomDetail = new BomDetail();
        bomDetail.setDeletedFlag(DyylConstant.DELETED);
        bomDetail.setDeletedBy(user.getUserId());
        boolean bomDetailResult = bomDetailService.update(bomDetail, wrapper);

        if(bomResult){
            result = true;
        }
        return result;
    }

    /**
    * @Description:  跳转到BOM详情页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/toBomDetail")
    @ResponseBody
    public ModelAndView toBomDetail(Long id){
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/bomManage/bom_detail_list");
        mv.addObject("bomId", id);
        return mv;
    }

    /**
    * @Description:  获取BOM详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String bomId,String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("bomId", bomId);
        PageUtils PagePlatform = bomDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount()==0?1:PagePlatform.getTotalCount());
        map.put("rows",PagePlatform.getList());
        executePageMap(map,page);
        return map;
    }

    /**
    * @Description:  跳转到bom详情的新增/修改页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/toBomDetailEdit")
    @ResponseBody
    public ModelAndView toBomDetailEdit(String id,String bomId) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/bomManage/bom_detail_edit");
        mv.addObject("bomId",bomId);

        if(!StringUtils.isEmptyString(id)){//跳转修改页面
            BomDetail bomDetail = bomDetailService.selectById(id);
            mv.addObject("bomDetail",bomDetail);
        }
        return mv;
    }

    /**
    * @Description:  保存/修改bom详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/5
    */
    @RequestMapping(value = "/saveDetail")
    @ResponseBody
    public boolean saveDetail(BomDetail bomDetail){
        boolean result = false;
        if(bomDetail != null){
            //当前时间
            String nowTime = DateUtils.getTime();
            if(bomDetail.getId() != null){//修改
                bomDetail.setCreateTime(nowTime);
                result = bomDetailService.updateById(bomDetail);
            }else{//新增
                bomDetail.setCreateTime(nowTime);
                bomDetail.setUpdateTime(nowTime);
                result = bomDetailService.insert(bomDetail);
            }
        }
        return result;
    }


    /**
     * @Description:  删除bom详情（逻辑删除）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/5
     */
    @RequestMapping(value = "/delBomDetail")
    @ResponseBody
    public boolean delBomDetail(String ids) {
        boolean result = false;
        Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);

        List<Long> lstIds = Arrays.stream(ids.split(",")).map(a -> Long.parseLong(a)).collect(Collectors.toList());
        List<BomDetail> bomDetailList = bomDetailService.selectBatchIds(lstIds);
        for(BomDetail bomDetail : bomDetailList){
            bomDetail.setDeletedFlag(DyylConstant.DELETED);
            bomDetail.setDeletedBy(user.getUserId());
        }
        //逻辑删除bom详情主表数据
        result = bomDetailService.updateBatchById(bomDetailList);

        return result;
    }
}
