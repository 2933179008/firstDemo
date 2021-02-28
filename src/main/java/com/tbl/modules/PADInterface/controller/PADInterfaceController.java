package com.tbl.modules.PADInterface.controller;

import com.tbl.modules.PADInterface.service.PADInterfaceService;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.service.LowerShelfDetailService;
import com.tbl.modules.outstorage.service.LowerShelfService;
import com.tbl.modules.slab.service.InstorageSlabService;
import com.tbl.modules.slab.service.MovePositionSlabService;
import com.tbl.modules.slab.service.OutStorageSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 平板调用接口
 * @author: zj
 * @create: 2019-03-12 11:21
 **/
@Controller
@RequestMapping(value = "/PADInterfaceController")
public class PADInterfaceController {
    @Autowired
    private InstorageSlabService instorageSlabService;
    @Autowired
    private MovePositionSlabService movePositionSlabService;
    @Autowired
    private PADInterfaceService padInterfaceService;

    @Autowired
    private OutStorageSlabService outStorageSlabService;

    @Autowired
    private LowerShelfDetailService lowerShelfDetailService;


    /**
    * @Description:  叉车叉起托盘触发光电调用接口
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    @RequestMapping(value = "/slabUpRfid/{IP}/{rfid}")
    @ResponseBody
    public Map<String, Object> slabUpRfid(@PathVariable String IP, @PathVariable String rfid) {
        Map<String,Object> map = new HashMap<>();
        //获取叉车操作类型
        String operateType = padInterfaceService.getOperateType(IP);
        if("1".equals(operateType)){//入库操作
            map = instorageSlabService.slabUpRfid(IP,rfid);
        }else if("2".equals(operateType)){//出库操作
            map = outStorageSlabService.slabDownRfid(IP,rfid);
        }else if("3".equals(operateType)){//移库操作
            map = movePositionSlabService.slabUpRfid(IP,rfid);
        }
        return map;
    }

    /**
    * @Description:  叉车放下托盘触发光电调用接口
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    @RequestMapping(value = "/slabDownRfid/{IP}")
    @ResponseBody
    public Map<String, Object> slabDownRfid(@PathVariable String IP) {
        Map<String,Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        try{
            //获取叉车操作类型
            String operateType = padInterfaceService.getOperateType(IP);
            if("1".equals(operateType)){//入库操作
                instorageSlabService.slabDownRfid(IP);
            }else if("2".equals(operateType)){//出库操作
                //通过Ip 获取对应的可用的rfid编号
                Map<String,Object> detailMap = outStorageSlabService.alivableRfid(IP);
                if (detailMap != null && !detailMap.isEmpty()) {
                    //判断当前是否确认已经下架
                    if("2".equals(detailMap.get("alert_key").toString())) {
                        String rfid = detailMap.get("avilable_rfid").toString();
                        String[] rfidList = rfid.split(",");
                        for (String rfids : rfidList) {
                            Map<String, Object> newMap = new HashMap<>();
                            newMap.put("rfid", rfids);
                            newMap.put("lower_id", detailMap.get("lower_id").toString());
                            Object lowerId = outStorageSlabService.getLowerId(newMap);
                            LowerShelfBillDetailVO lowerShelfBillDetailVO = lowerShelfDetailService.selectById(lowerId.toString());
                            lowerShelfDetailService.confirmLowerShelf(lowerShelfBillDetailVO, lowerId.toString(), 1);
                        }
                        outStorageSlabService.updateRfid(IP);
                    }else{
                        //如果是其他操作的话,将表中的rfid 信息清除
                        outStorageSlabService.updateRfid(IP);
                    }
                }
            }else if("3".equals(operateType)){//移库操作
                movePositionSlabService.slabDownRfid(IP);
            }
            result = true;
            msg = "接口调用成功";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "接口调用失败";
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
    }
}
    