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
 * 库存接口
 *
 * @author yuany
 * @date 2019-02-15
 */
@Controller
@RequestMapping(value = "/stockInterface")
public class StockInterfaceController {

    /**
     * 库存管理接口Service
     */
    @Autowired
    private StockInterfaceService stockInterfaceService;

    /**
     * 移库管理接口Service
     */
    @Autowired
    private MovePositionInterfaceService movePositionInterfaceService;

    /**
     * 托盘管理详情接口Service
     */
    @Autowired
    private TrayDetailInterfaceService trayDetailInterfaceService;

    /**
     * 盘点任务接口Service
     */
    @Autowired
    private InventoryTaskInterfaceService inventoryTaskInterfaceService;

    /**
     * 盘点任务详情接口Service
     */
    @Autowired
    private InventoryTaskDetailInterfaceService inventoryTaskDetailInterfaceService;

    /**
     * 盘点单接口Serivce
     */
    @Autowired
    private InventoryInterfaceService inventoryInterfaceService;


    /**
     * 物料接口Service
     */
    @Autowired
    private MaterielInterfaceService materielInterfaceService;

    /**
     * 物料绑定RFID接口Service
     */
    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;

    /**
     * 物料绑定RFID详情接口Service
     */
    @Autowired
    private MaterielBindRfidDetailInterfaceService materielBindRfidDetailInterfaceService;

    /**
     * 库位接口Service
     */
    @Autowired
    private DepotPositionInterfaceService depotPositionInterfaceService;

    /**
     * 托盘管理接口Service
     */
    @Autowired
    private TrayInterfaceService trayInterfaceService;

    /**
     * 库存查询接口
     *
     * @param rfid
     * @param materielCode
     * @param positionCode
     * @param batchRule
     * @return
     * @author yuany
     * @date 2019-02-15
     */
    @RequestMapping(value = "/queryStock/{rfid}/{materielCode}/{positionCode}/{batchRule}/{barcode}")
    @ResponseBody
    public Map<String, Object> queryStock(@PathVariable String rfid, @PathVariable String materielCode, @PathVariable String positionCode,
                                          @PathVariable String batchRule, @PathVariable String barcode) {

        return stockInterfaceService.queryStock(rfid, materielCode, positionCode, batchRule, barcode);
    }

    /**
     * 未完成的移库单
     *
     * @return
     * @author yuany
     * @date 2019-02-16
     */
    @RequestMapping("/getUnfinishedMovePosition/{moveUserId}")
    @ResponseBody
    public Map<String, Object> getUnfinishedMovePosition(@PathVariable Long moveUserId) {

        return movePositionInterfaceService.getUnfinishedMovePosition(moveUserId);
    }


    /**
     * 托盘详情查询接口
     *
     * @param trayBy
     * @return
     * @author yuany
     * @date 2019-02-16
     */
    @RequestMapping(value = "/getTrayDetail/{trayBy}")
    @ResponseBody
    public Map<String, Object> getTrayDetail(@PathVariable Long trayBy) {

        return trayDetailInterfaceService.getTrayDetail(trayBy);
    }

    /**
     * 盘点任务获取接口
     *
     * @param inventoryUserId
     * @return
     * @author yuany
     * @date 2019-02-16
     */
    @RequestMapping(value = "/getInventory/{inventoryUserId}")
    @ResponseBody
    public Map<String, Object> getInventory(@PathVariable Long inventoryUserId) {

        return inventoryTaskInterfaceService.getInventoryTask(inventoryUserId);
    }

    /**
     * 盘点任务详情获取接口
     *
     * @param inventoryTaskId
     * @return
     * @author yuany
     * @date 2019-02-16
     */
    @RequestMapping(value = "/getInventoryTaskDetail/{inventoryTaskId}")
    @ResponseBody
    public Map<String, Object> getInventoryTaskDetail(@PathVariable Long inventoryTaskId) {

        return inventoryTaskDetailInterfaceService.getInventoryTaskDetail(inventoryTaskId);
    }

    /**
     * 根据物料条码获取物料信息接口
     *
     * @param barcode
     * @return
     * @author yuany
     * @date 2019-02-19
     */
    @RequestMapping(value = "/getMateriel/{barcode}")
    @ResponseBody
    public Map<String, Object> getMateriel(@PathVariable String barcode) {

        return materielInterfaceService.getMateriel(barcode);
    }

    /**
     * 移库接口
     *
     * @param rfid
     * @param positionCode
     * @param userId
     * @return
     * @author yuany
     * @date 2019-02-19
     */
    @RequestMapping(value = "/doMovePosition/{rfid}/{positionCode}/{userId}")
    @ResponseBody
    public Map<String, Object> doMovePosition(@PathVariable String rfid, @PathVariable String positionCode, @PathVariable Long userId) {

        return movePositionInterfaceService.doMovePosition(rfid, positionCode, userId);
    }

//    /**
//     * 移库单提交接口
//     *
//     * @param movePositionId
//     * @param userId
//     * @return
//     * @author yuany
//     * @date 2019-02-20
//     */
//    @RequestMapping(value = "/completeMovePosition/{movePositionId}/{userId}")
//    @ResponseBody
//    public Map<String, Object> completeMovePosition(@PathVariable Long movePositionId, @PathVariable Long userId) {
//
//        return movePositionInterfaceService.completeMovePosition(movePositionId, userId);
//    }

    /**
     * 根据RFID获取物料绑定中库位信息接口
     *
     * @param rfid
     * @return
     * @author yuany
     * @date 2019-02-21
     */
    @RequestMapping(value = "/getDepotPositionByRfid/{rfid}")
    @ResponseBody
    public Map<String, Object> getDepotPositionByRfid(@PathVariable String rfid) {

        return materielBindRfidInterfaceService.getDepotPositionByRfid(rfid);
    }

    /**
     * 物料绑定RFID接口
     *
     * @param rfid
     * @param barcode
     * @param amount
     * @param weight
     * @param userId
     * @return
     * @author yuany
     * @date 2019-02-21
     */
    @RequestMapping(value = "/doMaterielBindRfid/{rfid}/{barcode}/{amount}/{weight}/{userId}")
    @ResponseBody
    public Map<String, Object> doMaterielBindRfid(@PathVariable String rfid, @PathVariable String barcode, @PathVariable String amount, @PathVariable String weight, @PathVariable Long userId) {

        return materielBindRfidInterfaceService.doMaterielBindRfid(rfid, barcode, amount, weight, userId);
    }

    /**
     * 库位冻结/解冻接口
     *
     * @param rfid
     * @param frozen
     * @return
     */
    @RequestMapping(value = "/frozenStatus/{rfid}/{frozen}")
    @ResponseBody
    public Map<String, Object> frozenStatus(@PathVariable String rfid, @PathVariable String frozen) {

        return depotPositionInterfaceService.frozenStatus(rfid, frozen);
    }

    /**
     * 判断库位是否冻结
     *
     * @param positionCode
     * @return
     */
    @RequestMapping(value = "/positionFrozen/{positionCode}")
    @ResponseBody
    public Map<String, Object> positionFrozen(@PathVariable String positionCode) {

        return depotPositionInterfaceService.positionFrozen(positionCode);
    }

    /**
     * 获取所有库位接口
     *
     * @return
     * @author yuany
     * @date 2019-02-22
     */
    @RequestMapping(value = "/getDepotPositionList")
    @ResponseBody
    public Map<String, Object> getDepotPositionList() {

        return depotPositionInterfaceService.getDepotPositionList();
    }

    /**
     * 根据库位编码获取库位接口（根据编码模糊查询）
     *
     * @return
     * @author anss
     * @date 2019-04-30
     */
    @RequestMapping(value = "/getDepotPositionByPositionCode/{positionCode}")
    @ResponseBody
    public Map<String, Object> getDepotPositionByPositionCode(@PathVariable String positionCode) {

        return depotPositionInterfaceService.getDepotPositionByPositionCode(positionCode);
    }

    /**
     * 根据RFID获取物料绑定详情
     *
     * @param rfid
     * @return
     * @author yuany
     * @date 2019-02-22
     */
    @RequestMapping(value = "/getMaterielBindRfidDetailByRfid/{rfid}")
    @ResponseBody
    public Map<String, Object> getMaterielBindRfidDetailByRfid(@PathVariable String rfid) {

        return materielBindRfidInterfaceService.getMaterielBindRfidDetailByRfid(rfid);
    }

    /**
     * 拆分或合并接口
     *
     * @return
     * @author yuany
     * @date 2019-02-22
     */
    @RequestMapping("/doSplitOrMerge")
    @ResponseBody
    public Map<String, Object> doSplitOrMerge(@RequestBody Map<String, Object> paramMap) {

        return trayInterfaceService.doSplitOrMerge(paramMap);
    }

    /**
     * 模糊查询物料信息
     *
     * @param materielCodeOrName 物料编码或名称
     * @return
     * @author yuany
     * @date 2019-02-25
     */
    @RequestMapping(value = "/getMaterielByMaterielCodeOrName/{materielCodeOrName}")
    @ResponseBody
    public Map<String, Object> getMaterielByMaterielCodeOrName(@PathVariable String materielCodeOrName) {

        return materielInterfaceService.getMaterielByMaterielCodeOrName(materielCodeOrName);
    }

    /**
     * 货权转移
     *
     * @return
     * @author yuany
     * @date 2019-02-25
     */
    @RequestMapping(value = "/getMaterielDroitShift/{id}/{materielId}/{batchNo}/{userId}/{documentType}")
    @ResponseBody
    public Map<String, Object> getMaterielDroitShift(@PathVariable Long id, @PathVariable Long
            materielId, @PathVariable String batchNo, @PathVariable Long userId, @PathVariable String documentType) {

        return stockInterfaceService.getMaterielDroitShift(id, materielId, batchNo, userId,documentType);
    }

    /**
     * 盘点任务提交接口
     *
     * @param id
     * @return
     * @author yuany
     * @date 2019-02-26
     */
    @RequestMapping(value = "/doInventoryTask/{id}")
    @ResponseBody
    public Map<String, Object> doInventoryTask(@PathVariable Long id) {

        return inventoryTaskInterfaceService.doInventoryTask(id);
    }

    /**
     * 通过盘点任务ID获取盘点任务详情库位及盘点记录详情库位
     *
     * @return
     */
    @RequestMapping(value = "/getPosition/{taskId}")
    @ResponseBody
    public Map<String, Object> getPosition(@PathVariable Long taskId) {

        return inventoryTaskDetailInterfaceService.getPosition(taskId);
    }

    /**
     * 盘点记录提交接口
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/doInventory")
    @ResponseBody
    public Map<String, Object> doInventory(@RequestBody Map<String, Object> paramMap) {

        return inventoryInterfaceService.doInventory(paramMap);
    }

    /**
     * 更改盘点任务状态为待审核
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/doInventoryTaskStatus")
    @ResponseBody
    public Map<String, Object> doInventoryTaskStatus(@PathVariable Long taskId) {

        return inventoryInterfaceService.doInventoryTaskStatus(taskId);
    }

    /**
     * 判断是否是白糖绑定
     *
     * @param rfid
     * @return
     */
    @RequestMapping(value = "/judgeSugar/{rfid}")
    @ResponseBody
    public Map<String, Object> judgeSugar(@PathVariable String rfid) {

        return materielBindRfidInterfaceService.judgeSugar(rfid);
    }

    /**
     * RFID 查询未入库的详情
     * @param rfid
     * @return
     */
    @RequestMapping(value = "/selectRfidDetail/{rfid}")
    @ResponseBody
    public Map<String,Object> selectRfidDetail(@PathVariable String rfid){
        return materielBindRfidDetailInterfaceService.selectRfidDetail(rfid);
    }

    /**
     * RFID 查询未入库的详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteRfidDetail/{id}")
    @ResponseBody
    public Map<String,Object> deleteRfidDetail(@PathVariable Long id){
        return materielBindRfidDetailInterfaceService.deleteRfidDetail(id);
    }

    /**
     * 根据物料条码模糊查询物料信息接口
     * @author anss
     * @date 2019-04-28
     * @param barcode
     * @return
     */
    @RequestMapping(value = "/getMaterielByBarcode/{barcode}")
    @ResponseBody
    public Map<String, Object> getMaterielByBarcode(@PathVariable String barcode) {

        return materielInterfaceService.getMaterielByBarcode(barcode);
    }

}
