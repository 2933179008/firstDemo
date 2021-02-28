package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.ErpDepotArea;

import java.util.Map;


/**
 * erp库区管理Service
 *
 * @author pz
 * @date 2019-04-29
 */
public interface ErpDepotAreaService extends IService<ErpDepotArea> {

    /**
     * 获取库区管理分页列表
     *
     * @param parms
     * @return
     * @author pz
     * @date 2019-04-29
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 生成库区编码
     *
     * @author pz
     * @date 2019-04-29
     */
    String getAreaCode();
}
