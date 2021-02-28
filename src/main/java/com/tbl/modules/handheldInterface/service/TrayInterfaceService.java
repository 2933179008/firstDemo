package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.Tray;

import java.util.Map;

/**
 * 托盘管理Service
 *
 * @author yuany
 * @date 2019-02-23
 */
public interface TrayInterfaceService extends IService<Tray> {

    /**
     * 生成拆分/合并单号
     *
     * @author yuany
     * @date 2019-01-16
     */
    String getMergeOrSplitCode(String type);

    /**
     * 拆分或合并
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> doSplitOrMerge(Map<String, Object> paramMap);
}
