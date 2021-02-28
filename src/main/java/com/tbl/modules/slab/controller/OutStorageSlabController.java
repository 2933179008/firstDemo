package com.tbl.modules.slab.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.IPUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.service.OutStorageSlabService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lcg
 * data 2019/3/11
 */
@Controller
@RequestMapping(value = "/outstorageSlab")
public class OutStorageSlabController extends AbstractController {

    @Autowired
    private OutStorageSlabService outStorageSlabService;

    @Autowired
    private DepotPositionService depotPositionService;


    /**
     * 跳转到页面
     *
     * @return
     */
    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/outstorageSlab/outstorageSlab_list");
        return mv;
    }


    /**
     * 获取列表
     *
     * @param queryJsonString
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = Maps.newHashMap();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        map.put("userId", userId);

        Page<LowerShelfBillVO> instoragePageList = outStorageSlabService.queryPage(map);
        page.setTotalRows(instoragePageList.getTotal() == 0 ? 1 : instoragePageList.getTotal());

        map.put("rows", instoragePageList.getRecords());
        executePageMap(map, page);
        return map;
    }

    /**
     * 下架点击操作
     *
     * @param lowerId
     * @return
     */
    @RequestMapping(value = "/slabOutBill")
    @ResponseBody
    public Map<String, Object> slabOutBill(String lowerId) {
        Map<String, Object> map = Maps.newHashMap();
        boolean result = false;
        String msg = "";
        try {
            //获取当前登录的IP
            String userIP = IPUtils.getIpAddr(request);
            //更新当前的操作状态
            outStorageSlabService.updateType(userIP);
            //插入当前的绑定关系
            outStorageSlabService.insertOrUpdateSlabOutBillBunding(lowerId, userIP);
            //更新当前任务的状态
            outStorageSlabService.updateLowerBillState(lowerId);
            result = true;
            msg = "开始下架单成功";
        } catch (Exception e) {
            result = false;
            msg = "开始下架单失败";
            e.printStackTrace();
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 获取叉车的状态,返回到前台页面
     *
     * @return
     */
    @RequestMapping(value = "/getSlabExecuteState")
    @ResponseBody
    public Map<String, Object> getSlabExecuteState() {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String code = "";
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
            //根据当前登陆人id和ip获取该叉车的验证信息
            Map<String, Object> taskMap = outStorageSlabService.getTaskMap(userIP);
            code = taskMap.get("alert_key") == null ? "" : taskMap.get("alert_key").toString();
            msg = taskMap.get("alert_value") == null ? "" : taskMap.get("alert_value").toString();
            map.put("taskMap", taskMap);
            result = true;
        } catch (Exception e) {
            result = false;
            code = "-1";
            msg = "系统错误";
            e.printStackTrace();
        }
        map.put("result", result);
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }


    /**
     * 跳转到详情界面
     *
     * @return
     */
    @RequestMapping(value = "/toDetailView")
    public ModelAndView toDetailView(String id) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("lowerId", id);
        mv.setViewName("techbloom/slab/outstorageSlab/outstorageSlab_detail");
        return mv;
    }

    /**
     * 判断对应的rfid 是不是所需要的物料
     *
     * @return
     */
    @RequestMapping(value = "/toSlabPosition")
    public ModelAndView toSlabPosition() {
        ModelAndView mv = new ModelAndView();
        //查询物料的基础信息,重量,库位,物料编号
        mv.setViewName("techbloom/slab/outstorageSlab/outstorageSlab_position");
        return mv;
    }

    /**
     * 通过下架单,rfid,id进行数据的查询
     *
     * @param lowerId
     * @param rfid
     * @param id      单据的I的
     * @return
     */
    @RequestMapping(value = "/getAllValue")
    @ResponseBody
    public Map<String, Object> getAllValue(String lowerId, String rfid, String id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("lowerId", lowerId);
        map.put("rfid", rfid);
        map.put("id", id);
        //通过lowerId以及rfid 获取当前系统中的可用的rfid
        Object aRfid = outStorageSlabService.getArfid(id);
        if (aRfid != null) {
            String rfids = aRfid.toString();
            String[] rfidList = rfids.split(",");
            if (rfidList.length == 1) {
                if (rfidList[0].equals(rfid)) {
                    Map<String, Object> newMap = outStorageSlabService.getMaterialMap(rfidList[0], lowerId);
                    if (newMap == null) {
                        map.put("result", false);
                        map.put("msg", "物料绑定信息中未找到对应的绑定物料");
                        return map;
                    }
                    map.put("materialCode0", newMap.get("materiel_code") == null ? "" : newMap.get("materiel_code").toString());
                    map.put("materialName0", newMap.get("materiel_name") == null ? "" : newMap.get("materiel_name").toString());
                    map.put("amount0", newMap.get("amount") == null ? "" : newMap.get("amount").toString());
                    map.put("weight0", newMap.get("weight") == null ? "" : newMap.get("weight").toString());
                    map.put("occupyStockAmount0", newMap.get("occupy_stock_amount") == null ? "" : newMap.get("occupy_stock_amount").toString());
                    map.put("occupyStockWeight0", newMap.get("occupy_stock_weight") == null ? "" : newMap.get("occupy_stock_weight").toString());
                    String positionId = newMap.get("position_id") == null ? "" : newMap.get("position_id").toString();
                    if (StringUtils.isNotEmpty(positionId)) {
                        DepotPosition depotPosition = depotPositionService.selectById(positionId);
                        if (depotPosition != null) {
                            map.put("positionId0", depotPosition.getPositionCode());
                        }
                    }
                    map.put("rfid0", rfidList[0]);
                    map.put("materialCode1", "");
                    map.put("materialName1", "");
                    map.put("amount1", "");
                    map.put("weight1", "");
                    map.put("occupyStockAmount1", "");
                    map.put("occupyStockWeight1", "");
                    map.put("positionId1", "");
                    map.put("rfid1", "");
                } else {
                    Map<String, Object> newMap = outStorageSlabService.getMaterialMap(rfidList[0], lowerId);
                    map.put("materialCode0", newMap.get("materiel_code") == null ? "" : newMap.get("materiel_code").toString());
                    map.put("materialName0", newMap.get("materiel_name") == null ? "" : newMap.get("materiel_name").toString());
                    map.put("amount0", newMap.get("amount") == null ? "" : newMap.get("amount").toString());
                    map.put("weight0", newMap.get("weight") == null ? "" : newMap.get("weight").toString());
                    map.put("occupyStockAmount0", newMap.get("occupy_stock_amount") == null ? "" : newMap.get("occupy_stock_amount").toString());
                    map.put("occupyStockWeight0", newMap.get("occupy_stock_weight") == null ? "" : newMap.get("occupy_stock_weight").toString());
                    String positionId = newMap.get("position_id") == null ? "" : newMap.get("position_id").toString();
                    if (StringUtils.isNotEmpty(positionId)) {
                        DepotPosition depotPosition = depotPositionService.selectById(positionId);
                        if (depotPosition != null) {
                            map.put("positionId0", depotPosition.getPositionCode());
                        }
                    }
                    map.put("rfid0", rfidList[0]);
                    Map<String, Object> oldMap = outStorageSlabService.getMaterialMap(rfid, lowerId);
                    map.put("materialCode1", oldMap.get("materiel_code") == null ? "" : oldMap.get("materiel_code").toString());
                    map.put("materialName1", oldMap.get("materiel_name") == null ? "" : oldMap.get("materiel_name").toString());
                    map.put("amount1", oldMap.get("amount") == null ? "" : oldMap.get("amount").toString());
                    map.put("weight1", oldMap.get("weight") == null ? "" : oldMap.get("weight").toString());
                    map.put("occupyStockAmount1", oldMap.get("occupy_stock_amount") == null ? "" : oldMap.get("occupy_stock_amount").toString());
                    map.put("occupyStockWeight1", oldMap.get("occupy_stock_weight") == null ? "" : oldMap.get("occupy_stock_weight").toString());
                    positionId = oldMap.get("position_id") == null ? "" : oldMap.get("position_id").toString();
                    if (StringUtils.isNotEmpty(positionId)) {
                        DepotPosition depotPosition = depotPositionService.selectById(positionId);
                        if (depotPosition != null) {
                            map.put("positionId1", depotPosition.getPositionCode());
                        }
                    }
                    map.put("rfid1", rfid);
                }
            }
            if (rfidList.length == 2) {
                for (int i = 0; i < rfidList.length; i++) {
                    Map<String, Object> newMap = outStorageSlabService.getMaterialMap(rfidList[i], lowerId);
                    String ss = String.valueOf(i);
                    map.put("materialCode" + ss, newMap.get("materiel_code") == null ? "" : newMap.get("materiel_code").toString());
                    map.put("materialName" + ss, newMap.get("materiel_name") == null ? "" : newMap.get("materiel_name").toString());
                    map.put("amount" + ss, newMap.get("amount") == null ? "" : newMap.get("amount").toString());
                    map.put("weight" + ss, newMap.get("weight") == null ? "" : newMap.get("weight").toString());
                    map.put("occupyStockAmount" + ss, newMap.get("occupy_stock_amount") == null ? "" : newMap.get("occupy_stock_amount").toString());
                    map.put("occupyStockWeight" + ss, newMap.get("occupy_stock_weight") == null ? "" : newMap.get("occupy_stock_weight").toString());
                    String positionId = newMap.get("position_id") == null ? "" : newMap.get("position_id").toString();
                    String positonCode = "";
                    if (StringUtils.isNotEmpty(positionId)) {
                        DepotPosition depotPosition = depotPositionService.selectById(positionId);
                        if (depotPosition != null) {
                            map.put("positionId" + ss, depotPosition.getPositionCode());
                        }
                    }
                    map.put("rfid" + ss, rfidList[i]);
                }
            }
        }
        return map;
    }

    @RequestMapping(value = "/confirmState")
    @ResponseBody
    public Map<String, Object> confirmState(String lowerId, String rfid, String rfid1) {
        Map<String, Object> map = Maps.newHashMap();
        map = outStorageSlabService.confirmState(lowerId, rfid, rfid1);
        return map;
    }
}
