package com.tbl.modules.noah.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.noah.entity.UwbMoveRecord;

import java.util.Map;

/**
 * UWB移动记录Service
 *
 * @author yuany
 * @date 2019-03-04
 */
public interface UwbMoveRecordService extends IService<UwbMoveRecord> {

    /**
     * 添加UWB移动记录
     *
     * @return
     */
    String addUwbMoveRecord(String uwbMoveRecordInfo);

    /**
     * 根据UserIp获取定位数据
     */
    UwbMoveRecord getUwbByUserIp(String userIp);

}
