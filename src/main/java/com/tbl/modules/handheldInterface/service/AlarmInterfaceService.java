package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.alarm.entity.Alarm;

/**
 * 预警接口Service
 */
public interface AlarmInterfaceService extends IService<Alarm> {

    /**
     * 添加预警
     *
     * @author yuany
     * @date 2019-02-01
     */
    boolean addAlarm(Long areaBy, String type);

    /**
     * 自动生成预警编码
     *
     * @author
     * @date 2019-02-01
     */
    String getAlarmCode();
}
