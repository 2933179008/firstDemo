package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.InventoryDetail;

import java.util.Map;


/**
 * 盘点详细Service
 *
 * @author pz
 * @date 2019-01-09
 */
public interface InventoryDetailService extends IService<InventoryDetail> {

        /**
     * @Description: 获取详细分页列表
     * @param params
     * @return
     * @author pz
     * @date 2019-01-15
     */
    PageUtils queryPageID(Map<String, Object> params, Long id);

    /**
     * @Description:  保存物料详情
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    Map<String, Object> saveInventoryDetail(Long id, String positionCode,String rfid,String materielCode,String materielType,Long userId);

}
