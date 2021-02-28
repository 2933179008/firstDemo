package com.tbl.modules.alarm.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.alarm.entity.Alarm;

import java.util.Map;

/**
 * 预警service
 *
 * @author yuany
 * @date 2019-01-31
 */
public interface AlarmService extends IService<Alarm> {

    /**
     * 获取预警分页查询列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-31
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 根据ID 更改状态
     *
     * @param id
     * @return
     */
    boolean dispose(String id);

    /**
     * 添加预警
     *
     * @author yuany
     * @date 2019-02-01
     */
    boolean addAlarm(Long areaBy, String type,String bindid);

    /**
     * 自动生成预警编码
     *
     * @author
     * @date 2019-02-01
     */
    String getAlarmCode();

    /**
     * 根据ID获取预警信息
     *
     * @param parms
     * @author yuany
     * @date 2019-01-31
     */
    PageUtils getAlarmById(Map<String, Object> parms);

}
