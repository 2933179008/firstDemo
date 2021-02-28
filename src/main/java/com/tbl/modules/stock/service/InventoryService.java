package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.Inventory;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @Description: 盘点Service
 * @Param:
 * @return:
 * @author pz
 * @date 2019-01-08
 */
public interface InventoryService extends IService<Inventory> {

    /**
     * @Description: 获取盘点分页列表
     * @param parms
     * @return
     * @author pz
     * @date 2019-01-08
     */
    PageUtils queryPageI(Map<String, Object> parms);

    /**
     * @Description:  生成盘点编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    String generateInventoryCode();

    /**
     * @Description:  新增/修改
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    Map<String,Object> saveInventory(Inventory inventory,Long userId);

    /**
     * @Description:  修改库位编码
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updatePositionCode(Long id, String positionCode);

    /**
     * @Description:  修改rfid
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateRfid(Long id, String rfid);

    /**
     * @Description:  修改物料编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateMaterialCode(Long id, String materialCode);

    /**
     * @Description:  修改物料名称
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateMaterialName(Long id, String materialName);

    /**
     * @Description:  修改批次号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateBatchNo(Long id, String batchNo);

    /**
     * @Description:  修改盘点结果数量
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateInventoryAmount(Long id, String inventoryAmount);

    /**
     * @Description:  修改盘点结果重量
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    Map<String, Object> updateInventoryWeight(Long id, String inventoryWeight);

    /**
     * @Description:  提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    Map<String, Object> submitInventory(Long id);

    /**
     * @Description: 获取导出列
     * @author pz
     * @date 2019-01-10
     * @return List<Inventory>
     */
    List<Inventory> getAllListI(String ids);

    /**
     * @Description: 导出excel
     * @author pz
     * @date 2019-01-10
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<Inventory> list);

    /**
     * @Description:  获取库存下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectPositionList(Long inventoryTaskId, String queryString, int pageSize, int pageNo);
    /**
     * @Description:  获取库存下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    Integer getSelectPositionTotal(Long inventoryTaskId, String queryString);

    /**
     * @Description:  获取物料下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectMaterielList(Long inventoryTaskId,String positionCode, String queryString, int pageSize, int pageNo);
    /**
     * @Description:  获取物料下拉列表总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    Integer getSelectMaterielTotal(Long inventoryTaskId,String positionCode,String queryString);

}
