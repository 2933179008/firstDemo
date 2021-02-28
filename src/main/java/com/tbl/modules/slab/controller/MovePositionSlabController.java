package com.tbl.modules.slab.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.IPUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.service.InstorageSlabService;
import com.tbl.modules.slab.service.MovePositionSlabService;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.MovePosition;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MovePositionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 平板的移位控制类
 * @author: zj
 * @create: 2019-03-06 14:26
 **/
@Controller
@RequestMapping(value = "/movePositionSlab")
public class MovePositionSlabController extends AbstractController {
    @Autowired
    private MovePositionSlabService movePositionSlabService;
    @Autowired
    private MovePositionService movePositionService;
    @Autowired
    private InstorageSlabService instorageSlabService;
    @Autowired
    private StockChangeDAO stockChangeDAO;
    @Autowired
    private DepotPositionService depotPositionService;
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;


    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/movePositionSlab/movePositionSlab_list");
        return mv;
    }

    /**
     * @Description: 跳转到库位选择页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    @RequestMapping(value = "/toSlabPosition")
    public ModelAndView toSlabPosition() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/movePositionSlab/movePositionSlab_position");
        return mv;
    }

    /**
     * @Description: 获取移位列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        Page<MovePosition> movePositionPageList = movePositionSlabService.queryPage(map);
        page.setTotalRows(movePositionPageList.getTotal() == 0 ? 1 : movePositionPageList.getTotal());
        map.put("rows", movePositionPageList.getRecords());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 开始移位操作
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    @RequestMapping(value = "/startMoveplace")
    @ResponseBody
    public Map<String, Object> startMoveplace() {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        try {
            /**1.插入/更新平板移库操作参数绑定关系表**/
            movePositionSlabService.insertOrUpdateSlabMovePositionBunding(request);
            result = true;
            msg = "请开始叉车操作";
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
            msg = "开始移库单失败";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 获取叉车执行状态，返回到前台做报警提示
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    @RequestMapping(value = "/getSlabExecuteState")
    @ResponseBody
    public Map<String, Object> getSlabExecuteState() {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String code = "";
        String msg = "";
        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //根据当前ip获取该叉车的验证信息
            Map<String, Object> map1 = movePositionSlabService.getSlabMovePositionBunding(userIP);
            if (map1 != null) {
                code = map1.get("alert_key") == null ? "" : map1.get("alert_key").toString();
                msg = map1.get("alert_value") == null ? "" : map1.get("alert_value").toString();
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            code = "-1";
            msg = "系统错误";
        }
        map.put("result", result);
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 获取库位下拉框列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/4
     */
    @RequestMapping(value = "/getSelectPosition")
    @ResponseBody
    public Map<String, Object> getSelectPosition(String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> positionList = movePositionSlabService.getSelectPositionList(queryString, pageSize, pageNo);
        map.put("result", positionList);
        map.put("total", movePositionSlabService.getSelectPositionTotal(queryString));
        return map;
    }

    /**
     * @Description: 库位确认
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    @RequestMapping(value = "/confirmPosition")
    @ResponseBody
    public Map<String, Object> confirmPosition(Long positionId) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        try {
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            //当前登陆人id
            Long userId = user.getUserId();
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //移入库位编号
            String positionCode = depotPositionService.selectById(positionId).getPositionCode();
            /**1.判断库位是否可用，即验证所选的库位是否可以放该托盘rfid**/
            Map<String, Object> map1 = movePositionSlabService.isAvailablePosition(userIP, positionCode);
            Boolean result1 = map1.get("result") == null ? false : Boolean.parseBoolean(map1.get("result").toString());
            if (result1) {//如果验证通过
                /**2.创建移库单**/
                Long movePositionId = movePositionSlabService.insertMovePosition(userId, userIP, positionId);
                /**3.更新平板移库操作参数绑定关系表的移入库位编号**/
                movePositionSlabService.updateSlabMovePositionBundingPositionCode(movePositionId, userIP, positionCode);
                result = true;
                msg = "库位确认成功";
            } else {
                result = result1;
                msg = map1.get("msg") == null ? "" : map1.get("msg").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            msg = "系统错误";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 跳转到托盘确认页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/29
     */
    @RequestMapping(value = "/toSureRfid")
    @ResponseBody
    public ModelAndView toSureRfid() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/movePositionSlab/movePositionSlab_sureRfid");
        return mv;
    }

    /**
     * @Description: 获取验证通过的正在操作的rfid和对应的库位
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/4/2
     */
    @RequestMapping(value = "/getExecuteRfid")
    @ResponseBody
    public Map<String, Object> getExecuteRfid() {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        String rfid = "";
        //移出库位编码
        String outPositionCode = "";
        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //根据当前登陆人id和ip获取该叉车的验证信息
            Map<String, Object> map1 = movePositionSlabService.getExecuteRfid(userIP);
            if (map1 != null) {
                rfid = map1.get("rfid") == null ? "" : map1.get("rfid").toString();
            }
            if (StringUtils.isNotBlank(rfid)) {
                //根据rfid查询库位编号
                outPositionCode = movePositionSlabService.selectPositionCodeByRfid(rfid);
                //获取绑定详情
                MaterielBindRfid materielBindRfid = materielBindRfidDAO.materielBindRfid(rfid);
                List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                                .eq("status", DyylConstant.STATE_UNTREATED)
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                );
                map.put("materielBindRfidDetailList",materielBindRfidDetailList);

            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            msg = "系统错误";
        }
        map.put("result", result);
        map.put("msg", msg);
        map.put("rfid", rfid);
        map.put("outPositionCode", outPositionCode);
        return map;
    }


    /**
     * @Description: 更新平板移库操作参数绑定关系表的字段
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/28
     */
    @RequestMapping(value = "/updateAlert")
    @ResponseBody
    public Map<String, Object> updateAlert(String flag) {
        Map<String, Object> map = new HashMap<>();
        try {
            movePositionSlabService.updateAlert(request, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
    