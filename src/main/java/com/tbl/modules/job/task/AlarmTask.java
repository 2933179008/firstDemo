package com.tbl.modules.job.task;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.alarm.dao.AlarmDAO;
import com.tbl.modules.alarm.entity.Alarm;
import com.tbl.modules.alarm.entity.AlarmDeploy;
import com.tbl.modules.alarm.service.AlarmDeployService;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.instorage.service.QualityBillService;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("AlarmTask")
public class AlarmTask {

    @Autowired
    private QualityBillService qualityBillService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private AlarmDeployService alarmDeployService;
    @Autowired
    private AlarmDAO alarmDAO;
    @Autowired
    private StockService stockService;


    @Value("${message.DURATIONTIME}")
    private String durationtime;

    @Value("${message.QUALITYTIME}")
    private String qualitytime;


    public void qualityCheckTimeout(){
        List<Map<String, Object>> qualityBills = qualityBillService.getTimeOutList();
        AlarmDeploy alarmDeploy = alarmDeployService.selectOne(new EntityWrapper<AlarmDeploy>().eq("alarm_Type","1"));
        for (Map<String, Object> q:qualityBills) {
            if(q.get("quality_time")!=null){
                String zjdate = q.get("quality_time").toString();
                //质检时间
                Long zjtime =  DateUtils.stringToLong(zjdate,"yyyy-MM-dd HH:mm:ss");
                Long now = DateUtils.dateToLong(new Date());
                //小于多少时间
                Long duration = now-zjtime;
                if(zjtime<(now+1000*60*60*24*Long.parseLong(durationtime))){
                    Alarm alarm =new Alarm();
                    alarm.setAlarmCode(alarmService.getAlarmCode());
                    alarm.setStartTime(DateUtils.format(new Date()));
                    alarm.setType("1");
                    alarm.setState("0");
                    alarm.setBindCode(q.get("id").toString());
                    if (null!=alarmDeploy){
                        alarm.setAddressesBys(alarmDeploy.getAddressesBys());
                        alarm.setAddressesNames(alarmDeploy.getAddressesNames());
                        alarm.setSendContent(alarmDeploy.getSendContent());
                        //预警时长待定
                        alarm.setDuration(String.valueOf(duration/1000/60/60/24));
                        alarm.setSendType(alarmDeploy.getSendType());
                    }
                    //判断是否已经在预警表中
                    //存在就更新
                    Alarm alarm1 = alarmDAO.getOneTimeout(q.get("id").toString());
                    if (alarm1!=null){
                        if (alarm1.getState().equals("0")){
                            alarm1.setAddressesBys(alarmDeploy.getAddressesBys());
                            alarm1.setAddressesNames(alarmDeploy.getAddressesNames());
                            alarm1.setSendContent(alarmDeploy.getSendContent());
                            //预警时长待定
                            alarm1.setDuration(String.valueOf(duration/1000/60/60/24));
                            alarm1.setSendType(alarmDeploy.getSendType());
                            alarmService.updateById(alarm1);
                        }
                    }else {
                        //不存在新增保存
                        //alarmService.insert(alarm);
                        alarmService.addAlarm(null,"1",q.get("id").toString());
                    }

                }
            }
        }
        try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }


    public void stockMaturityWarning(){
        List<Stock> stockList = stockService.selectList(new EntityWrapper<>());
        AlarmDeploy alarmDeploy = alarmDeployService.selectOne(new EntityWrapper<AlarmDeploy>().eq("alarm_Type","5"));
        for (Stock s: stockList) {
            if(StringUtils.isNotBlank(s.getQualityDate())){
                //当前时间
                Long now = DateUtils.dateToLong(new Date());
                //保质期
                Long dqtime =  DateUtils.stringToLong(s.getQualityDate(),"yyyyMMdd");
                //即将过期 预警
                if((dqtime-now)<1000*60*60*24*Long.parseLong(qualitytime)){
                    Alarm alarm =new Alarm();
                    alarm.setAlarmCode(alarmService.getAlarmCode());
                    alarm.setStartTime(DateUtils.format(new Date()));
                    alarm.setType("5");
                    alarm.setState("0");
                    alarm.setBindCode(s.getId().toString());
                    alarm.setAreaBy(s.getId());
                    if (null!=alarmDeploy){
                        alarm.setAddressesBys(alarmDeploy.getAddressesBys());
                        alarm.setAddressesNames(alarmDeploy.getAddressesNames());
                        alarm.setSendContent(alarmDeploy.getSendContent());
                        //预警时长待定
                        alarm.setDuration("10");
                        alarm.setSendType(alarmDeploy.getSendType());
                    }
                    //判断是否已经在预警表中
                    //存在就更新
                    Alarm alarm1 = alarmDAO.getOneQualityTimeout(s.getId().toString());
                    if (alarm1!=null){
                        if (alarm1.getState().equals("0")){
                            alarm1.setAddressesBys(alarmDeploy.getAddressesBys());
                            alarm1.setAddressesNames(alarmDeploy.getAddressesNames());
                            alarm1.setSendContent(alarmDeploy.getSendContent());
                            //预警时长待定
                            alarm1.setDuration(String.valueOf("10"));
                            alarm1.setSendType(alarmDeploy.getSendType());
                            alarmService.updateById(alarm1);
                        }
                    }else {
                        //不存在新增保存
                        //alarmService.insert(alarm);
                        alarmService.addAlarm(s.getId(),"5",s.getId().toString());
                    }
                }
            }
        }


        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
