package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.handheldInterface.dao.InventoryTaskDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.service.InventoryTaskDetailInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点任务详情接口Service实现
 */
@Service(value = "inventoryTaskDetailInterfaceService")
public class InventoryTaskDetailInterfaceServiceImpl extends ServiceImpl<InventoryTaskDetailInterfaceDAO, InventoryTaskDetail> implements InventoryTaskDetailInterfaceService {

    //日志接口Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //盘点任务详情接口DAO
    @Autowired
    private InventoryTaskDetailInterfaceDAO inventoryTaskDetailInterfaceDAO;


    /**
     * 通过盘点任务id获取盘点库位
     */
    @Override
    public List<InventoryTaskDetail> getTaskPosition(Long taskId) {

        return inventoryTaskDetailInterfaceDAO.getInventoryTaskPosition(taskId);
    }

    /**
     * 通过盘点任务ID获取盘点单id获取盘点库位
     */
    @Override
    public List<InventoryDetail> getInventoryPosition(Long taskId) {

        return inventoryTaskDetailInterfaceDAO.getInventoryPosition(taskId);
    }

    /**
     * 盘点任务详情获取
     *
     * @param inventoryTaskId
     * @return
     */
    @Override
    public Map<String, Object> getInventoryTaskDetail(Long inventoryTaskId) {

        boolean result = true;
        String msg = "获取数据成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<InventoryTaskDetail> inventoryTaskDetailList = null;
        if (inventoryTaskId != null) {
            inventoryTaskDetailList = inventoryTaskDetailInterfaceDAO.selectList(
                    new EntityWrapper<InventoryTaskDetail>()
                            .eq("inventory_Task_id", inventoryTaskId)
            );
            if (inventoryTaskDetailList.isEmpty()) {
                result = false;
                msg = "无数据";
            }
        } else {
            result = false;
            msg = "失败原因：获取当前任务ID失败";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用盘点任务详情获取接口";
        String parameter = "InventoryTaskId:" + inventoryTaskId.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", inventoryTaskDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 通过盘点任务ID获取盘点任务详情库位及盘点记录详情库位
     *
     * @param taskId
     * @return
     */
    @Override
    public Map<String, Object> getPosition(Long taskId) {

        boolean result = true;
        String msg = "查询成功";
        Map<String, Object> map = new HashMap<>();
        String parameter = "TaskId:" + taskId;
        if (taskId == null) {
            map.put("result", false);
            map.put("msg", "失败原因：参数未获取");
            interfaceLogService.interfaceLogInsert("调用通过盘点任务ID获取盘点任务详情库位及盘点记录详情库位接口",
                    parameter, "失败原因：参数未获取", DateUtils.getTime());

            return map;
        }
        //盘点任务库位集合
        List<InventoryTaskDetail> inventoryTaskDetailList = getTaskPosition(taskId);
        //盘点已完成库位集合
        List<InventoryDetail> inventoryDetailList = inventoryTaskDetailInterfaceDAO.getInventoryPosition(taskId);

        map.put("inventoryTaskDetailList", inventoryTaskDetailList);
        map.put("inventoryDetailList", inventoryDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

}
