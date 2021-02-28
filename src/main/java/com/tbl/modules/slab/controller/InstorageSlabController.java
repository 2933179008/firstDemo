package com.tbl.modules.slab.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Strings;
import com.tbl.common.utils.IPUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import com.tbl.modules.noah.service.UwbMoveRecordService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.service.InstorageSlabService;
import com.tbl.modules.slab.service.MovePositionSlabService;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.visualization.entity.StockCar;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 平板入库控制类
 * @author: zj
 * @create: 2019-02-27 18:59
 **/
@Controller
@RequestMapping(value = "/instorageSlab")
public class InstorageSlabController extends AbstractController {
    @Autowired
    private PutBillService putBillService;
    @Autowired
    private InstorageSlabService instorageSlabService;
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;
    @Autowired
    private UwbMoveRecordService uwbMoveRecordService;
    @Autowired
    private DepotAreaService depotAreaService;

    //UWB定位库位范围
    @Value("${postion.RecommendedLocationX}")
    private Double RecommendedLocationX;

    @Value("${postion.RecommendedLocationY}")
    private Double RecommendedLocationY;

    /**
     * @Description: 跳转到平板入库页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/27
     */
    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/slab/instorageSlab/instorageSlab_list");
        return mv;
    }

    /**
     * @Description: 跳转到库位选择页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    @RequestMapping(value = "/toSlabPosition")
    @ResponseBody
    public ModelAndView toSlabPosition(String area) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("area",area);
        mv.setViewName("techbloom/slab/instorageSlab/instorageSlab_position");
        return mv;
    }

    /**
     * @Description: 获取入库列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
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
        Page<PutBill> instoragePageList = instorageSlabService.queryPage(map);
        page.setTotalRows(instoragePageList.getTotal() == 0 ? 1 : instoragePageList.getTotal());
        map.put("rows", instoragePageList.getRecords());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 确认上架单的操作人
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    @RequestMapping(value = "/sureSlabPutBillOperate")
    @ResponseBody
    public Map<String, Object> sureSlabPutBillOperate(Long putBillId, HttpServletRequest request) {
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
            String userName = user.getUsername();

            PutBill putBill = putBillService.selectById(putBillId);
            if (putBill != null) {
                //上架单操作人
                Long operator = putBill.getOperator();
                //如果上架操作人不为空并且上架操作人不是当前登陆人，则不能上架（多人同时上架该单据的时候会发生这种情况）
                if (operator != null && operator != userId) {
                    result = false;
                    msg = "开始上架单失败！该单据已被占用执行";
                } else {
                    /**1.插入/更新平板参数绑定表**/
                    instorageSlabService.insertOrUpdateSlabBillBunding(request, putBillId);
                    /**2.更新上架单表状态为“待上架”和上架单操作人**/
                    instorageSlabService.updatePutBillState(putBillId, userId, userName);
                    result = true;
                    msg = "开始上架单成功，请开始叉车操作";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
            msg = "系统错误";
        }
        map.put("result", result);
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
    public Map<String, Object> getSelectPosition(String queryString, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();

        List<Map<String, Object>> positionList = new ArrayList<>();
        Integer count = 0;
        UwbMoveRecord uwbMoveRecord = new UwbMoveRecord();
        DepotArea depotArea = new DepotArea();
        Double xSize = 0d;
        Double ySize = 0d;
        Double x_Size = 0d;
        Double y_Size = 0d;

        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //通过ip地址获取UWB的定位信息
            uwbMoveRecord = uwbMoveRecordService.getUwbByUserIp(userIP);
            //获取挂于UWB位置的库位范围
            xSize = Double.parseDouble(uwbMoveRecord.getXsize()) + RecommendedLocationX;
            x_Size = Double.parseDouble(uwbMoveRecord.getXsize()) - RecommendedLocationX;
            ySize = Double.parseDouble(uwbMoveRecord.getYsize()) + RecommendedLocationY;
            y_Size = Double.parseDouble(uwbMoveRecord.getYsize()) - RecommendedLocationY;

            if (StringUtils.isNotEmpty(uwbMoveRecord.getSceneCode())){
                if (uwbMoveRecord.getSceneCode().length()<13){
                    depotArea = depotAreaService.selectOne(new EntityWrapper<DepotArea>().eq("area_code",uwbMoveRecord.getSceneCode()));
                    positionList = instorageSlabService.getSelectPositionList(depotArea.getId().toString(),xSize,x_Size,ySize,y_Size,queryString);
                    count = instorageSlabService.getSelectPositionTotal(depotArea.getId().toString(),xSize,x_Size,ySize,y_Size,queryString);
                }else {
                    positionList = instorageSlabService.getSelectPositionList("12,13,14",xSize,x_Size,ySize,y_Size,queryString);
                    count = instorageSlabService.getSelectPositionTotal("12,13,14",xSize,x_Size,ySize,y_Size,queryString);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("result", positionList);
        map.put("total", count);
        return map;
    }

    /**
     * @Description: 库位的确认
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    @RequestMapping(value = "/confirmPosition")
    @ResponseBody
    public Map<String, Object> confirmPosition(String positionCode, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String code = "0";
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
            /**1.判断库位是否可用，即验证所选的库位是否可以放该托盘rfid**/
            Map<String, Object> map1 = instorageSlabService.isAvailablePosition(userIP, positionCode);
            Boolean result1 = map1.get("result") == null ? false : Boolean.parseBoolean(map1.get("result").toString());
            msg = map1.get("msg") == null ? "" : map1.get("msg").toString();
//            if (result1) {
//                /**2.根据当前时间判断uwb定位的小车位置是否与选中的库位位置一致**/
//
//
//                code = "1";
//                result = result1;
//            }

            /**3.如果一致则更新相关参数，否则给页面提示**/

            result = result1;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        map.put("result", result);
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 对话框确认
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/4
     */
    @RequestMapping(value = "/submitPosition")
    @ResponseBody
    public Map<String, Object> submitPosition(String positionCode, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            /**更新平板参数绑定表的库位编号**/
            instorageSlabService.updateSlabBillBundingPositionCode(userIP, positionCode);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        map.put("result", result);
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
    public Map<String, Object> getSlabExecuteState(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String code = "";
        String msg = "";
        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //根据当前登陆人id和ip获取该叉车的验证信息
            Map<String, Object> map1 = instorageSlabService.getSlabBillBunding(userIP);
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
     * @Description: 获取推荐货位
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/11
     */
    @RequestMapping(value = "/getRecommendPosition")
    @ResponseBody
    public Map<String, Object> getRecommendPosition(String area,HttpServletRequest request) {

        return instorageSlabService.getRecommendPosition(area,request);
    }

    /**
     * @Description: 跳转到可视化页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/27
     */
    @RequestMapping(value = "/toVisualization")
    @ResponseBody
    public ModelAndView toVisualization(Long putBillId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        String forkliftTag = instorageSlabService.getForkliftSelected(userIP);
        //上架单号
        String putBillCode = "";
        //上架单操作人名称
        String operatorName = "";
        PutBill putBill = putBillService.selectById(putBillId);
        if (putBill != null) {
            putBillCode = putBill.getPutBillCode();
            operatorName = putBill.getOperatorName();
        }

        mv.addObject("putBillCode", putBillCode);
        mv.addObject("operatorName", operatorName);
        mv.addObject("forkliftTag", forkliftTag);
        mv.setViewName("techbloom/slab/instorageSlab/instorageSlab_visualization");
        return mv;
    }

    /**
     * @Description: 更新平板入库操作参数绑定关系表的字段
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/28
     */
    @RequestMapping(value = "/updateAlert")
    @ResponseBody
    public Map<String, Object> updateAlert(String flag, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            instorageSlabService.updateAlert(request, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        mv.setViewName("techbloom/slab/instorageSlab/instorageSlab_sureRfid");
        return mv;
    }


    @RequestMapping(value = "/getExecuteRfid")
    @ResponseBody
    public Map<String, Object> getExecuteRfid(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        Map<String, Object> mapList = new HashMap<>();
        try {
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //根据当前登陆人id和ip获取该叉车的验证信息
            Map<String, Object> map1 = instorageSlabService.getExecuteRfid(userIP);
            if (map1 == null) {
                result = false;
                msg = "当前用户IP未获取数据";
                return map;
            }
            String rfids = map1.get("rfid") == null ? "" : map1.get("rfid").toString();
            if (Strings.isNullOrEmpty(rfids)) {
                result = false;
                msg = "未获取当前所操作的托盘RFID";
                return map;
            }
            String[] rfidlts = rfids.split(",");
            if (rfidlts == null) {
                result = false;
                msg = "未获取当前所操作的托盘RFID";
                return map;
            }
            for (String rfid : rfidlts) {
                List<Map<String, Object>> maps = materielBindRfidDetailService.getMaterialBundingDetail(rfid);
                if (!maps.isEmpty()) {
                    mapList.put(rfid, maps);
                }
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            msg = "系统错误";
        }
        map.put("result", result);
        map.put("msg", msg);
        map.put("mapList", mapList);
        return map;
    }

    /**
     * @Description: 更新slab_bill_bunding表的alert_key字段为3（已确定库位）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/29
     */
    @RequestMapping(value = "/updateAlertToUp")
    @ResponseBody
    public Map<String, Object> updateAlertToUp(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            instorageSlabService.updateAlertToUp(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 获取小车位置
     */
    @RequestMapping(value = "/getCarPosition")
    @ResponseBody
    public List<StockCar> getCarPosition(String areaId, String forkliftTag) {
        return instorageSlabService.getCarPosition(areaId, forkliftTag);
    }

    /**
     * @Description: 获取叉车下拉框列表数据
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/4/18
     */
//    @RequestMapping(value = "/getForkliftSelected")
//    @ResponseBody
//    public Map<String, Object> getForkliftSelected() {
//        Map<String, Object> map = new HashMap<>();
//        boolean result = false;
//        List<Map<String, Object>> list = new ArrayList<>();
//        try {
//            list = instorageSlabService.getForkliftSelected();
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result = false;
//        }
//        map.put("result", result);
//        map.put("list", list);
//        return map;
//    }

}
