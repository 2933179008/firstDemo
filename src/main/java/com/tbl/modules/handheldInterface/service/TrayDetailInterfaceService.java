package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.TrayDetail;

import java.util.Map;

/**
 * 托盘管理详情接口Service
 */
public interface TrayDetailInterfaceService extends IService<TrayDetail> {

    /**
     * 托盘详情查询接口
     *
     * @param trayBy
     * @return
     */
    Map<String, Object> getTrayDetail(Long trayBy);
}
