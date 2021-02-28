package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.handheldInterface.dao.AlarmInterfaceDAO;
import com.tbl.modules.handheldInterface.service.AlarmInterfaceService;
import com.tbl.modules.alarm.entity.Alarm;
import com.tbl.modules.constant.DyylConstant;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警接口Service实现
 */
@Service(value = "alarmInterfaceService")
public class AlarmInterfaceServiceImpl extends ServiceImpl<AlarmInterfaceDAO, Alarm> implements AlarmInterfaceService {

    /**
     * 添加预警
     *
     * @author yuany
     * @date 2019-02-01
     */
    @Override
    public boolean addAlarm(Long areaBy, String type) {
        //预警对象
        Alarm alarm = new Alarm();
        alarm.setAlarmCode(this.getAlarmCode());
        alarm.setStartTime(DateUtils.getTime());
        alarm.setState(DyylConstant.STATE_PROCESSED);

        alarm.setAreaBy(areaBy);
        alarm.setType(type);

        return this.insert(alarm);
    }

    /**
     * 自动生成预警编码
     *
     * @author
     * @date 2019-02-01
     */
    @Override
    public String getAlarmCode() {

        //预警编号
        String alarmCode = null;
        //预警集合
        List<Alarm> alarmList = this.selectList(
                new EntityWrapper<>()
        );
        //如果集合为长度为0则为第一条添加的数据
        if (alarmList.size() == 0) {
            alarmCode = "AL0000001";
        } else {
            //获取集合中最后一条数据
            Alarm alarm = alarmList.get(alarmList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Long number = Long.parseLong(alarm.getAlarmCode().substring(2)) + 1;
            //拼接字符串
            alarmCode = "AL000000" + number.toString();
        }

        return alarmCode;
    }
}
