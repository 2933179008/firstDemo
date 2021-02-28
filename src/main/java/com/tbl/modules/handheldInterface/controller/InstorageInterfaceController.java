package com.tbl.modules.handheldInterface.controller;

import com.tbl.modules.handheldInterface.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 入库接口
 *
 * @author yuany
 * @date 2019-02-14
 */
@Controller
@RequestMapping(value = "/InstorageInterface")
public class
InstorageInterfaceController {

    /**
     * 收货Service
     */
    @Autowired
    private ReceiptInterfaceService receiptInterfaceService;

    /**
     * 收货详情接口Service
     */
    @Autowired
    private ReceiptDetailInterfaceService receiptDetailInterfaceService;


    /**
     * 上架接口Service
     */
    @Autowired
    private PutBillInterfaceService putBillInterfaceService;

    /**
     * 上架单详情Service
     */
    @Autowired
    private PutBillDetailInterfaceService putBillDetailInterfaceService;

    /**
     * 库区接口Service
     */
    @Autowired
    private DepotPositionInterfaceService depotPositionInterfaceService;


    /**
     * 获取收货单
     *
     * @return
     * @author yuany
     * @date 2019-02-14
     */
    @RequestMapping(value = "/getReceipt")
    @ResponseBody
    public Map<String, Object> getReceiptList() {

        return receiptInterfaceService.getReceiptList();
    }


    /**
     * 根据收货单ID获取收货单详情
     *
     * @param receiptPlanId
     * @return
     * @author yuany
     * @date 2019-02-14
     */
    @RequestMapping(value = "/getReceiptDetailList/{receiptPlanId}")
    @ResponseBody
    public Map<String, Object> getReceiptDetailList(@PathVariable String receiptPlanId) {

        return receiptDetailInterfaceService.getReceiptDetailList(receiptPlanId);
    }


    /**
     * 生成入库单
     *
     * @param paramMap
     * @return
     * @author yuany
     * @date 2019-02-18
     */
    @RequestMapping(value = "/getReceiptDetail")
    @ResponseBody
    public Map<String, Object> getReceiptDetail(@RequestBody Map<String, Object> paramMap) {

        return receiptDetailInterfaceService.getReceiptDetail(paramMap);
    }


    /**
     * 获取上架单
     *
     * @param userId
     * @return
     * @author yuany
     * @date 2019-02-14
     */
    @RequestMapping(value = "/getPutBill/{userId}")
    @ResponseBody
    public Map<String, Object> getPutBill(@PathVariable Long userId) {
        return putBillInterfaceService.getPutBill(userId);
    }


    /**
     * 获取上架单详情 并添加操作人，更新上架单状态
     *
     * @param userId
     * @param putBillId
     * @return
     * @author yuany
     * @date 2019-02-15
     */
    @RequestMapping(value = "/getPutBillDetailList/{userId}/{putBillId}")
    @ResponseBody
    public Map<String, Object> getPutBillDetailList(@PathVariable Long userId, @PathVariable Long putBillId) {

        return putBillDetailInterfaceService.getPutBillDetailList(userId, putBillId);
    }


    /**
     * 上架RFID验证接口
     *
     * @param rfid
     * @return
     */
    @RequestMapping(value = "/getProvingRfid/{instorageProcess}/{rfid}")
    @ResponseBody
    public Map<String, Object> getProvingRfid(@PathVariable String instorageProcess, @PathVariable String rfid) {

        return putBillInterfaceService.getProvingRfid(instorageProcess, rfid);
    }

    /**
     * 根据库位编码获取库位接口
     *
     * @return
     * @author yuany
     * @date 2019-02-20
     */
    @RequestMapping(value = "/getDepotPosition/{positionCode}")
    @ResponseBody
    public Map<String, Object> getDepotPosition(@PathVariable String positionCode) {

        return depotPositionInterfaceService.getDepotPosition(positionCode);
    }

//    /**
//     * 上架详情物料更新参数接口
//     *
//     * @return
//     */
//    @RequestMapping(value = "/getPutBillDetail")
//    @ResponseBody
//    public Map<String, Object> getPutBillDetail(@RequestBody Map<String, Object> paramMap) {
//
//        return putBillDetailInterfaceService.getPutBillDetail(paramMap);
//    }

    /**
     * 上架详情参数更新接口
     *
     * @return
     */
    @RequestMapping(value = "/completePutBill")
    @ResponseBody
    public Map<String, Object> completePutBill(@RequestBody Map<String, Object> paramMap) {

        return putBillInterfaceService.completePutBill(paramMap);
    }

    /**
     * 上架完成接口
     */
    @RequestMapping(value = "/changePutBillState/{putBillId}/{userId}")
    @ResponseBody
    public Map<String, Object> changePutBillState(@PathVariable Long putBillId,@PathVariable Long userId) {

        return putBillInterfaceService.changePutBillState(putBillId,userId);
    }


    /**
     * 上架单提交
     *
     * @param putBillId
     * @return
     */
    @RequestMapping(value = "/submitPutBill/{putBillId}")
    @ResponseBody
    public Map<String, Object> submitPutBill(@PathVariable Long putBillId) {

        return putBillInterfaceService.submitPutBill(putBillId);
    }

    /**
     * 白糖上架接口
     *
     * @param putBillId
     * @param rfid
     * @param positionId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sugarStorage/{putBillId}/{rfid}/{positionId}/{userId}")
    @ResponseBody
    public Map<String, Object> sugarStorage(@PathVariable Long putBillId, @PathVariable String rfid, @PathVariable Long positionId, @PathVariable Long userId) {

        return putBillDetailInterfaceService.sugarStorage(putBillId, rfid, positionId, userId);
    }

    /**
     * 白糖绑定更新重量数量接口
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param weight
     * @return
     */
    @RequestMapping(value = "/sugarBind/{rfid}/{materialCode}/{amount}/{weight}/{userId}")
    @ResponseBody
    public Map<String, Object> sugarBind(@PathVariable String rfid, @PathVariable String materialCode, @PathVariable String amount, @PathVariable String weight,@PathVariable Long userId) {

        return putBillDetailInterfaceService.sugarBind(rfid, materialCode, amount, weight,userId);
    }
}
