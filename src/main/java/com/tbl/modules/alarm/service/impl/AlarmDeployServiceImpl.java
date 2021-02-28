package com.tbl.modules.alarm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.alarm.dao.AlarmDeployDAO;
import com.tbl.modules.alarm.entity.Alarm;
import com.tbl.modules.alarm.entity.AlarmDeploy;
import com.tbl.modules.alarm.service.AlarmDeployService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预警部署Service实现
 */

@Service("alarmDeployService")
public class AlarmDeployServiceImpl extends ServiceImpl<AlarmDeployDAO, AlarmDeploy> implements AlarmDeployService {

    //用户User
    @Autowired
    private UserService userService;

    //预警设置DAO
    @Autowired
    private AlarmDeployDAO alarmDeployDAO;

    /**
     * 预警部署分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-03-11
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        Page<AlarmDeploy> alarmDeployPage = this.selectPage(
                new Query<AlarmDeploy>(parms).getPage(),
                new EntityWrapper<>()
        );
        for (AlarmDeploy alarmDeploy : alarmDeployPage.getRecords()) {
            if (!Strings.isNullOrEmpty(alarmDeploy.getAddressesBys())) {
                List<Long> userIdList = Arrays.asList(alarmDeploy.getAddressesBys().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                if (!userIdList.isEmpty()) {
                    List<User> userList = userService.selectBatchIds(userIdList);
                    String name = null;
                    for (User user : userList) {
                        if (name == null) {
                            name = user.getName();
                        } else {
                            name = name + "," + user.getName();
                        }
                    }
                    alarmDeploy.setAddressesNames(name);
                }
            }
        }
        return new PageUtils(alarmDeployPage);
    }

    /**
     * 删除预警设置
     *
     * @param id:要删除的id
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @Override
    public boolean delAlarmDeploy(Long id) {
        boolean result = false;
        AlarmDeploy alarmDeploy = this.selectById(id);
        if (alarmDeploy != null && alarmDeploy.getApply().equals(DyylConstant.PROHIBIT)) {
            result = this.deleteById(id);
        }

        return result;
    }

    /**
     * 禁用预警设置
     *
     * @param ids:要删除的ids
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @Override
    public boolean prohibitting(String ids) {
        List<Long> lstAlarmDeployIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<AlarmDeploy> alarmDeployList = this.selectBatchIds(lstAlarmDeployIds);
        for (AlarmDeploy alarmDeploy : alarmDeployList) {
            alarmDeploy.setApply(DyylConstant.PROHIBIT);
        }

        return updateBatchById(alarmDeployList);
    }

    /**
     * 启用预警设置
     *
     * @param ids:要删除的ids
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @Override
    public boolean enableding(String ids) {

        List<Long> lstAlarmDeployIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<AlarmDeploy> alarmDeployList = this.selectBatchIds(lstAlarmDeployIds);
        for (AlarmDeploy alarmDeploy : alarmDeployList) {
            alarmDeploy.setApply(DyylConstant.ELABLE);
        }

        return updateBatchById(alarmDeployList);
    }

    /**
     * @Description: 获取用户下拉列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-03-12
     */
    @Override
    public List<Map<String, Object>> getSelectUserList(String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return alarmDeployDAO.getSelectUserList(page, queryString);
    }

    /**
     * @Description: 获取用户数量
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-03-12
     */
    @Override
    public Integer getSelectUserTotal(String queryString) {
        return alarmDeployDAO.getSelectUserTotal(queryString);
    }
}
