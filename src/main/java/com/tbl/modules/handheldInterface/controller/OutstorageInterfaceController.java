package com.tbl.modules.handheldInterface.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.service.OutStorageInterfaceService;
import com.tbl.modules.noah.service.ScanGateService;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.LowerShelfDetailService;
import com.tbl.modules.outstorage.service.LowerShelfService;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库接口
 *
 * @author yuany
 * @date 2019-02-16
 */
@Controller
@RequestMapping(value = "/OutstorageInterface")
public class OutstorageInterfaceController {

    @Autowired
    private OutStorageInterfaceService outStorageInterfaceService;

    @Autowired
    private OutStorageService outStorageService;

    @Autowired
    private LowerShelfService lowerShelfService;

    @Autowired
    private LowerShelfDetailService lowerShelfDetailService;

    @Autowired
    private ScanGateService scanGateService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 根据下架的人获取对应的下架任务
     *
     * @param userId 下架人
     * @return
     */
    @RequestMapping(value = "/lowerList/{userId}")
    @ResponseBody
    public Object lowerList(@PathVariable String userId) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<Map<String, Object>> lowerList = outStorageInterfaceService.lowerInterfaceList(userId);
        if (lowerList != null && lowerList.size() > 0) {
            for (Map mapList : lowerList) {
                JSONObject jsonstr = new JSONObject();
                jsonstr.put("id", mapList.get("id").toString());
                jsonstr.put("lowerShelfBillCode", mapList.get("lowerShelfBillCode").toString());
                jsonstr.put("outstorageBillCode", mapList.get("outstorageBillCode").toString());
                jsonstr.put("state", mapList.get("state").toString());
                jsonstr.put("username", mapList.get("userId") == null ? "" : mapList.get("userId").toString());
                jsonArray.add(jsonstr);
//                if("0".equals(mapList.get("state").toString())){
//                    //将下架单的状态修改成一分配
//                    lowerShelfService.updateLoserState(mapList.get("id").toString(),"3");
//                }
            }
            json.put("data", jsonArray);
            json.put("result", true);
        } else {
            json.put("result", false);
            json.put("msg", "当前操作人员没有分配的任务");
        }
        return json.toJSONString();
    }

    /**
     * 根据下架单ID获取对应的下架单的详情
     *
     * @param lowerId
     * @return
     */
    @RequestMapping(value = "/lowerDetailList/{lowerId}/{userId}")
    @ResponseBody
    public Object lowerDetailList(@PathVariable String lowerId, @PathVariable String userId) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        LowerShelfBillVO lowerShelfBillVO = new LowerShelfBillVO();
        lowerShelfBillVO = lowerShelfService.selectById(lowerId);
        String realUserId = lowerShelfBillVO.getUserId();
        String user_id = "";
        if (Strings.isNullOrEmpty(realUserId)) {
            user_id = userId;
        } else {
            if (!realUserId.equals(userId)) {
                //如果不相同的话,则提示当前的任务已经绑定
                json.put("result", false);
                json.put("msg", "当前的任务已经被人锁定");
                return json.toJSONString();
            } else {
                user_id = realUserId;
            }
        }
        lowerShelfBillVO.setUserId(user_id);
        String state = lowerShelfBillVO.getState();
        //只有在状态为1的时候才将数据进行更新为已已分配
        if ("0".equals(state)) {
            //修改状态为已分配
            lowerShelfBillVO.setState("3");
        }
        lowerShelfService.updateById(lowerShelfBillVO);
        List<Map<String, Object>> lowerList = outStorageInterfaceService.lowerInterfaceDetailList(lowerId);
        if (lowerList != null && lowerList.size() > 0) {
            for (Map mapList : lowerList) {
                JSONObject jsonstr = new JSONObject();
                jsonstr.put("id", mapList.get("id").toString());
                jsonstr.put("material_code", mapList.get("material_code").toString());
                jsonstr.put("material_name", mapList.get("material_name").toString());
                jsonstr.put("batch_no", mapList.get("batch_no").toString());
                jsonstr.put("amount", mapList.get("amount").toString());
                jsonstr.put("state", mapList.get("state").toString());
                jsonstr.put("position_code", mapList.get("position_code"));
                jsonstr.put("rfid", (String) mapList.get("rfid"));
                jsonstr.put("weight", (String) mapList.get("weight"));
                jsonArray.add(jsonstr);
            }
            json.put("data", jsonArray);
            json.put("result", true);
        } else {
            json.put("result", false);
            json.put("msg", "当前操作人员没有分配的任务");
        }
        return json.toJSONString();
    }

    /**
     * 手持机确认出库完成（只需传过来rfid编号就行了）
     *
     * @param rfid
     * @return
     */
    @RequestMapping(value = "/rfidConfirm/{rfid}/{lowerId}")
    @ResponseBody
    public Object rfidConfirm(@PathVariable String rfid, @PathVariable String lowerId) {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        String parameter = "rfid:" + rfid;
        if (Strings.isNullOrEmpty(rfid)) {
            interfaceLogService.interfaceLogInsert("调用手持机确认出库完成接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
            json.put("result", false);
            json.put("msg", "失败原因：参数未获取");

            return json.toJSONString();
        }

        //通过rfid 获取对应的任务详情
        List<LowerShelfBillDetailVO> lowerShelfBillDetailVOList = lowerShelfDetailService.selectList(
                new EntityWrapper<LowerShelfBillDetailVO>()
                        .eq("rfid", rfid)
                        .eq("lower_shelf_bill_id", lowerId)
        );

        if (lowerShelfBillDetailVOList.isEmpty()) {
            interfaceLogService.interfaceLogInsert("调用手持机确认出库完成接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
            json.put("result", false);
            json.put("msg", "失败原因：此RFID无出库任务");

            return json.toJSONString();
        }
        for (LowerShelfBillDetailVO lowerShelfBillDetailVO : lowerShelfBillDetailVOList) {
            map = lowerShelfDetailService.confirmLowerShelf(lowerShelfBillDetailVO, lowerShelfBillDetailVO.getId().toString(), 1);
            if (!(Boolean) map.get("result")) {
                interfaceLogService.interfaceLogInsert("调用手持机确认出库完成接口", parameter, "失败原因：部分物料下架失败", DateUtils.getTime());
                json.put("result", false);
                json.put("msg", map.get("msg").toString());

                return json.toJSONString();
            }
        }

        json.put("msg", map.get("msg").toString());
        json.put("result", map.get("result").toString());
        return json.toJSONString();
    }

    /**
     * 扫描门扫描标签
     *
     * @param deviceIp
     * @param data
     * @return
     */
    @RequestMapping(value = "/scanDoor/{deviceIp}/{data}")
    @ResponseBody
    public Object scanDoor(@PathVariable String deviceIp, @PathVariable String data) {
        //通过扫描门的IP查找对应的产线
        Object linieIp = outStorageService.getLineIp(deviceIp);

        //判断当前的rfid是否是该产线的数据
        String[] rfidList = data.split(",");
        for (String rfid : rfidList) {
            //判断rfid 对应的当前的lineIp
            Map<String, Object> lineIp = outStorageService.getRfidIp(rfid);
            if (lineIp != null && !lineIp.isEmpty()) {
                //获取备料单产线信息
                String[] mixUseLines = lineIp.get("mix_use_line").toString().split(",");
                //当前所在产线
                String[] scanDoorLines = linieIp.toString().split(",");
                //判断产线集合中是否包含所在产线
                boolean result = false;
                for (String mul : mixUseLines) {
                    for (String sdl : scanDoorLines) {
                        if (mul.substring(0, 2).equals(sdl)) {
                            result = true;
                            break;
                        }
                    }
                }
                if (!result) {
                    //TODO 如果不是当前产线里面的,则报警
                    //点亮塔灯进行报警
                    scanGateService.scanGate(deviceIp);
                    //调用预警添加
                    alarmService.addAlarm(null, DyylConstant.ALARM_OUT_STOCK, null);
                } else {
                    //通过标签号获取当前的数据的信息
                    Map<String, Object> lowerDetail = lowerShelfDetailService.lowerDetail(rfid);
                    if (lowerDetail != null && !lowerDetail.isEmpty()) {

                        //通过id获取下架单详情的数据
                        LowerShelfBillDetailVO lowerShelfBillDetailVO = lowerShelfDetailService.selectById(lowerDetail.get("id").toString());

                        lowerShelfBillDetailVO.setState("2");

                        lowerShelfDetailService.updateById(lowerShelfBillDetailVO);
                        //获取下架单ID
                        String lowerId = lowerShelfBillDetailVO.getLowerShelfBillId();

                        LowerShelfBillVO lowerShelfBillVO = lowerShelfService.selectById(lowerId);

                        //获取下架单的数据
                        Object outstorageId = lowerShelfDetailService.getOutStorageId(lowerDetail.get("id").toString());

                        OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(outstorageId.toString());

                        List<OutStorageDetailManagerVO> outStorageDetailList = lowerShelfDetailService.getDetailList(outstorageId.toString());

                        Integer count = lowerShelfDetailService.lowerCount(lowerDetail.get("id").toString());

                        String state = "1";
                        if (count > 0) {
                            //表示当前还有未下架的任务
                            lowerShelfBillVO.setState(state);
                            //更新下架单状态
                            lowerShelfService.updateById(lowerShelfBillVO);
                        } else {
                            //下架任务已经完成
                            lowerShelfBillVO.setState("2");
                            lowerShelfService.updateById(lowerShelfBillVO);
                            Integer outStorageCount = lowerShelfDetailService.outStorageCount(outstorageId.toString());
                            if (outStorageCount > 0) {
                                outStorageManagerVO.setState("4");
                                outStorageService.updateById(outStorageManagerVO);
                            } else {
                                outStorageManagerVO.setState("5");
                                outStorageService.updateById(outStorageManagerVO);
                                lowerShelfDetailService.updateOutStorageDetail(outstorageId);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 合并出库
     *
     * @return
     */
    @RequestMapping(value = "/mergeOutStorage")
    @ResponseBody
    public Map<String, Object> mergeOutStorage(@RequestBody Map<String, Object> paramMap) {

        return outStorageInterfaceService.mergeOutStorage(paramMap);
    }

    /**
     * 获取RFID中对应的下架但列表
     *
     * @param oldRfid
     * @param lowerId
     * @return
     */
    @RequestMapping(value = "/getLowerDetail/{oldRfid}/{lowerId}")
    @ResponseBody
    public Map<String, Object> getLowerDetail(@PathVariable String oldRfid, @PathVariable String lowerId) {

        return outStorageInterfaceService.getLowerDetail(oldRfid, lowerId);
    }

}
