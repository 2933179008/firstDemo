package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.Tray;

import java.util.Map;

/**
 * 托盘管理Service
 *
 * @author yuany
 * @date 2019-01-15
 */
public interface TrayService extends IService<Tray> {

    /**
     * 获取托盘管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-15
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 生成拆分/合并单号
     *
     * @author yuany
     * @date 2019-01-16
     */
    String getMergeOrSplitCode(String type);

    /**
     * 删除托盘管理选中数据（逻辑删除）
     *
     * @param ids:要删除的ids
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2019-01-18
     */
    boolean delTray(String ids, Long userId);

}
