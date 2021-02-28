package com.tbl.modules.external.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ErpStockInterfaceServiceDAO {

    /**
     * @Description:  查询满足调用接口条件的盘点任务
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String,Object>> selectInventoryTask();

    /**
     * @Description:  根据盘点任务单id查询盘点任务单详情(盘盈)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/20
     */
    List<Map<String, Object>> selecInventoryTaskDetail(@Param("inventoryTaskId") String inventoryTaskId);

    /**
     * @Description:  根据盘点任务单id查询盘点任务单详情(盘亏)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/20
     */
    List<Map<String, Object>> selecInventoryTaskDet(@Param("inventoryTaskId") String inventoryTaskId);

    /**
     * @Description:  查询满足调用接口条件的库存任务
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String,Object>> selectStock();

    /**
     * @Description:  查询满足调用接口条件的库存任务(货权转移后)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String,Object>> selectGenerateStock();

    /**
     * @Description:  更新盘点任务表erp_flag字段
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    void updateInventoryTaskErpFlag(List<Map<String, Object>> list);

    /**
     * @Description:  查询满足调用接口条件的移位任务(散货)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String,Object>> selectMovePosition();

    /**
     * @Description:  查询满足调用接口条件的移位任务(整货)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String,Object>> selectRfidMovePosition();


    /**
     * @Description:  更新移位任务表erp_flag字段
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    void updateMovePositionErpFlag(List<Map<String, Object>> list);


    /**
     * @Description:  更新库存表materielPower字段
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    void updateMaterielErpFlag(List<Map<String, Object>> list);

    /**
     * @Description:  更新库存表erp_flag字段
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    void updateStockErpFlag(List<Map<String, Object>> list);

}
