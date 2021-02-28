package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.ErpDepotPosition;

import java.util.Map;


/**
 * erp库位管理Service
 *
 * @author pz
 * @date 2019-04-29
 */
public interface ErpDepotPositionService extends IService<ErpDepotPosition> {

    /**
     * 获取库位管理分页列表
     *
     * @param parms
     * @return
     * @author pz
     * @date 2019-04-29
     */
    PageUtils queryPage(Map<String, Object> parms);


    /**
     * 生成库位编码
     *
     * @author pz
     * @date 2019-04-29
     */
    String getPositionCode();
}
