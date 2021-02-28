package com.tbl.modules.alarm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.common.utils.mail.SendMail;
import com.tbl.common.utils.message.SendNoticeCode;
import com.tbl.modules.alarm.dao.AlarmDAO;
import com.tbl.modules.alarm.dao.AlarmDeployDAO;
import com.tbl.modules.alarm.entity.Alarm;
import com.tbl.modules.alarm.entity.AlarmDeploy;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预警Service实现
 *
 * @author yuany
 * @date 2019-01-31
 */
@Service("alarmService")
public class AlarmServiceImpl extends ServiceImpl<AlarmDAO, Alarm> implements AlarmService {

    /**
     * 接口日志service
     */
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 库区DAO
     */
    @Autowired
    private DepotAreaDAO depotAreaDao;

    /**
     * 预警设置DAO
     */
    @Autowired
    private AlarmDeployDAO alarmDeployDAO;

    @Autowired
    private AlarmDAO alarmDAO;

    /**
     * 用户DAO
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * 获取预警分页查询列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-31
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        //获取类型
        String type = (String) parms.get("type");
        //获取库区ID
        String areaBy = (String) parms.get("areaBy");

        Page<Alarm> alarmPage = this.selectPage(
                new Query<Alarm>(parms).getPage(),
                new EntityWrapper<Alarm>()
                        .eq(StringUtils.isNotBlank(type), "type", type)
                        .eq(StringUtils.isNotBlank(areaBy), "area_by", areaBy)
        );
        for (Alarm alarm : alarmPage.getRecords()) {
            if (alarm.getAreaBy() != null) {
                DepotArea depotArea = depotAreaDao.selectById(alarm.getAreaBy());
                if (depotArea != null) {
                    alarm.setAreaName(depotArea.getAreaName());
                }
            }
            if (!Strings.isNullOrEmpty(alarm.getAddressesBys())) {
                List<Long> lstUserIds = Arrays.stream(alarm.getAddressesBys().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<User> userList = userDAO.selectBatchIds(lstUserIds);
                String userName = null;
                for (User user : userList) {
                    if (Strings.isNullOrEmpty(userName)) {
                        userName = user.getName();
                    } else {
                        if (!Strings.isNullOrEmpty(user.getName())) {
                            userName = userName + "," + user.getName();
                        }
                    }
                }
                alarm.setAddressesNames(userName);
            }
        }

        return new PageUtils(alarmPage);
    }

    /**
     * 根据ID 更改状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean dispose(String id) {

        //获取当前时间并格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);

        //根据id获取预警，添加预警状态
        Alarm alarm = this.selectById(id);
        alarm.setState(DyylConstant.STATE_UNTREATED);
        alarm.setEndTime(time);

        //获取预警开始时间
        Date startTime = null;
        try {
            startTime = sdf.parse(alarm.getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取当前时间预警开始时间的时间差
        Long between = (date.getTime() - startTime.getTime()) / 1000;
        Long min = between / 60;
        alarm.setDuration(min.toString());

        return this.updateById(alarm);
    }


    /**
     * 添加预警
     *
     * @author yuany
     * @date 2019-02-01
     */
    @Override
    public boolean addAlarm(Long areaBy, String type, String bindid) {

        boolean result = true;
        String msg = "发送成功";
        //预警对象
        Alarm alarm = new Alarm();
        alarm.setStartTime(DateUtils.getTime());
        alarm.setState(DyylConstant.STATE_PROCESSED);
        alarm.setAreaBy(areaBy);
        alarm.setType(type);
        if (StringUtils.isNotBlank(bindid)) {
            alarm.setBindCode(bindid);
        }
        //发送邮件/短信
        List<AlarmDeploy> alarmDeployList = alarmDeployDAO.selectList(
                new EntityWrapper<AlarmDeploy>()
                        .eq("apply", DyylConstant.ELABLE)
                        .eq("alarm_type", type)
        );
        //获取库区名称
        String areaName = null;
         if (areaBy!=null){
             DepotArea area = depotAreaDao.selectById(areaBy);
             if (area!=null){
                 areaName = area.getAreaName();
             }
         }
        String errorinfo = null;
        if (!alarmDeployList.isEmpty()) {
            for (AlarmDeploy alarmDeploy : alarmDeployList) {
                alarm.setAlarmCode(this.getAlarmCode());
                alarm.setAddressesBys(alarmDeploy.getAddressesBys());
                alarm.setSendType(alarmDeploy.getSendType());
                alarm.setSendContent(alarmDeploy.getSendContent());
                if (this.insert(alarm)) {
                    List<Long> lstUserIds = Arrays.stream(alarmDeploy.getAddressesBys().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    List<User> lstUser = userDAO.selectBatchIds(lstUserIds);
                    String subject = null;

                    //获取预警类型，添加预警主题
                    switch (alarmDeploy.getAlarmType()) {
                        case DyylConstant.ALARM_BOM:
                            subject = "BOM差异预警";
                            break;
                        case DyylConstant.ALARM_TIME:
                            subject = "质检超时预警";
                            break;
                        case DyylConstant.ALARM_RFID:
                            subject = "RFID未绑定预警";
                            break;
                        case DyylConstant.ALARM_GOODS:
                            subject = "叉车取货预警";
                            break;
                        case DyylConstant.ALARM_OUT_STOCK:
                            subject = "扫描门出库预警";
                            break;
                        case DyylConstant.ALARM_LIBRARY_AGE:
                            subject = "库存到期预警";
                            break;
                        case DyylConstant.ALARM_INVENTORY_VARIANCE:
                            subject = "库存差异预警";
                            break;
                        default:
                            break;
                    }
                    //拼接所有需要发送邮件的用户邮箱
                    for (User user : lstUser) {
                        //发送短信
                        if (alarmDeploy.getSendType().equals(DyylConstant.SENDTYPECODE)) {
                            try {
                                //手机号
                                JSONArray mobileJson = new JSONArray();
                                mobileJson.add(user.getPhone());
                                //添加参数
                                JSONArray parameterJson = new JSONArray();
                                parameterJson.add(user.getPhone());
                                parameterJson.add(bindid);
                                parameterJson.add(subject);
                                parameterJson.add(areaName);
                                SendNoticeCode.sendNoticeCode(JSON.toJSONString((mobileJson)), JSON.toJSONString((parameterJson)));
                            } catch (Exception e) {
                                msg = "发送短信失败";
                                errorinfo = DateUtils.getTime();
                            } finally {
                                String parameter = user.getPhone();
                                String interfacename = "调用发送短信接口";
                                interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
                            }
                        } else if (alarmDeploy.getSendType().equals(DyylConstant.SENDTYPEEMAIL)) {

                            //发邮件
                            try {
                                SendMail.send(user.getEmail(), subject, alarmDeploy.getSendContent());
                            } catch (Exception e) {
                                msg = "发送邮件失败";
                                errorinfo = DateUtils.getTime();
                            } finally {
                                String parameter = user.getEmail() + "/" + subject + "/" + alarmDeploy.getSendContent();
                                String interfacename = "调用发送邮件接口";
                                interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
                            }
                        } else if (alarmDeploy.getSendType().equals(DyylConstant.SENDTYPECODEANDEmail)) {

                            //发送短信和邮件
                            try {
                                //手机号
                                JSONArray mobileJson = new JSONArray();
                                mobileJson.add(user.getPhone());
                                //添加参数
                                JSONArray parameterJson = new JSONArray();
                                parameterJson.add(user.getPhone());
                                parameterJson.add(bindid);
                                parameterJson.add(subject);
                                parameterJson.add(areaName);
                                SendNoticeCode.sendNoticeCode(JSON.toJSONString((mobileJson)), JSON.toJSONString((parameterJson)));
                            } catch (Exception e) {
                                msg = "发送短信失败";
                                errorinfo = DateUtils.getTime();
                            } finally {
                                String parameter = user.getPhone();
                                String interfacename = "调用发送短信接口";
                                interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
                            }

                            try {
                                SendMail.send(user.getEmail(), subject, alarmDeploy.getSendContent());
                            } catch (Exception e) {
                                msg = "发送邮件失败";
                                errorinfo = DateUtils.getTime();
                            } finally {
                                String parameter = user.getEmail() + "/" + subject + "/" + alarmDeploy.getSendContent();
                                String interfacename = "调用发送邮件接口";
                                interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
                            }
                        }
                    }
                }
            }
        } else {
            result = false;
            msg = "失败原因：未找对应的预警设置";
            errorinfo = DateUtils.getTime();
        }
        String parameter = areaBy + "," + type;
        String interfacename = "调用预警记录接口";
        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
        return result;
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
            Double number = Double.valueOf(alarm.getAlarmCode().substring(2)) + 1;
            //拼接字符串
            alarmCode = "AL000000" + number.toString();
        }

        return alarmCode;
    }

    /**
     * 根据ID获取预警信息
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-31
     */
    @Override
    public PageUtils getAlarmById(Map<String, Object> parms) {

        //获取id
        String id = (String) parms.get("id");

        Page<Alarm> alarmPage = new Page<>();
        //若id不为空则根据id查询
        if (StringUtils.isNotBlank(id)) {
            alarmPage = this.selectPage(
                    new Query<Alarm>(parms).getPage(),
                    new EntityWrapper<Alarm>()
                            .eq("id", id)
            );
        }
        for (Alarm alarm : alarmPage.getRecords()) {
            if (alarm.getAreaBy() != null) {
                alarm.setAreaName(depotAreaDao.selectById(alarm.getAreaBy()).getAreaName());
            }
        }

        return new PageUtils(alarmPage);
    }

}
