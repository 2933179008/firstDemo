package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.InventoryTask;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @Description: 盘点任务Service
 * @Param:
 * @return:
 * @author pz
 * @date 2019-01-14
 */
public interface InventoryTaskService extends IService<InventoryTask> {

    /**
     * 获取盘点任务分页列表
     * @param parms
     * @return
     * @author pz
     * @date 2019-01-14
     */
    PageUtils queryPageI(Map<String, Object> parms);

    /**
     * @Description:  生成盘点任务编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    String generatInventoryTaskCode();

    /**
     * 审核
     * @Param:
     * @return Map
     * @author pz
     * @date 2019-01-22
     */
    Map<String, Object> auditTaskCheck(String stockIdStr,String qualityDateStr,String qualityPeriodStr,Long userId);

    /**
     * @Description:  新增/修改
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    Map<String,Object> saveInventoryTask(InventoryTask inventoryTask, Long userId);

    /**
     * @Description:  提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    Map<String, Object> submitInventoryTask(Long id);

    /**
     * @Description: 复盘
     * @Param:
     * @return Map
     * @author pz
     * @date 2019-01-22
     */
    Map<String, Object> replayInventory(Long id);

    /**
     * @Description: 导出excel
     * @param list
     * @return Map
     * @author pz
     * @date 2019-01-14
     */
    void toExcel(HttpServletResponse response, String path, List<InventoryTask> list);

    /**
     * @Description: 获取导出列
     * @param
     * @return List<InventoryTask>
     * @author pz
     * @date 2019-01-14
     */
    List<InventoryTask> getAllListI(String ids);

    /**
     * 根据主键查询盘点任务
     *
     * @param id 任务主键
     * @return StockCheck
     * @author pz
     * @date 2019-01-14
     */
    InventoryTask getInventoryTaskById(Long id);

    /**
     * @Description:  获取库位下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo,String positionCode);

    /**
     * 根据主键查询盘点任务
     *
     * @param id 任务主键
     * @return StockCheck
     * @author pz
     * @date 2018-10-08
     */
    InventoryTask getInventoryTaskVoById(Long id);

}
