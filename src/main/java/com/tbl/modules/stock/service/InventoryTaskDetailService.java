package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.InventoryTaskDetail;

import java.util.List;
import java.util.Map;


/**
 * 盘点任务详情Service
 *
 * @author pz
 * @date 2019-01-14
 */
public interface InventoryTaskDetailService extends IService<InventoryTaskDetail> {


    /**
     * 获取盘点任务详情分页列表
     *
     * @param parms
     * @return
     * @author pz
     * @date 2019-01-14
     */
    PageUtils queryPageI(Map<String, Object> parms, Long id);

    /**
     * @Description:  获取库位下拉列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo);

    /**
     * @Description:  获取库位下拉列表数据总条数
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    Integer getSelectPositionTotal(String queryString);

    /**
     * 根据ID获取进行中盘点任务单详细
     * @author pz
     * @date 2019-02-11
     * @param id
     * @return
     */
    List<InventoryTaskDetail> taskDeatilList(Long id);

}
