package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.TrayDetail;

import java.util.Map;


/**
 * 托盘管理详情Service
 *
 * @author yuany
 * @date 2019-01-18
 */
public interface TrayDetailService extends IService<TrayDetail> {

    /**
     * 获取托盘管理详情分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-18
     */
    PageUtils queryTrayDetailPage(Map<String, Object> parms);

    /**
     * 拆分/合并逻辑
     *
     * @param mlWeight
     * @param id
     * @param trayBy
     * @return
     */
    Map<String,Object> updateAmountAndWeight(String mlWeight, Long id, Long trayBy,Long userId,String rfidSelect);

    /**
     * 单据提交
     */
    Map<String, Object> submitTray(String id,Long userId);

}
