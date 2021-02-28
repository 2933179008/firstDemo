package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.basedata.entity.DepotPosition;

import java.util.Map;

/**
 * 库位接口Service
 */
public interface DepotPositionInterfaceService extends IService<DepotPosition> {

    /**
     * 根据库位编码获取库位接口
     *
     * @param positionCode
     * @return
     */
    Map<String, Object> getDepotPosition(String positionCode);

    /**
     * 获取所有库位
     *
     * @return
     */
    Map<String, Object> getDepotPositionList();

    /**
     * 库位冻结/解冻
     *
     * @param positionCode
     * @param frozen
     * @return
     */
    Map<String, Object> frozenStatus(String positionCode, String frozen);

    /**
     * 判断库位是否冻结
     * @param positionCode
     * @return
     */
    Map<String,Object> positionFrozen(String positionCode);

    /**
     * 根据库位编码获取库位接口（根据编码模糊查询）
     * @return
     * @author anss
     * @date 2019-04-30
     * @param positionCode
     * @return
     */
    Map<String, Object> getDepotPositionByPositionCode(String positionCode);

}
