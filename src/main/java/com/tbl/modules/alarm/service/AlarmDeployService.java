package com.tbl.modules.alarm.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.alarm.entity.AlarmDeploy;

import java.util.List;
import java.util.Map;

/**
 * 预警部署Service
 */
public interface AlarmDeployService extends IService<AlarmDeploy> {

    /**
     * 获取预警部署分页查询列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-03-11
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 删除预警设置
     *
     * @param id:要删除的id
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    boolean delAlarmDeploy(Long id);

    /**
     * 禁用预警设置
     *
     * @param ids:要删除的ids
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    boolean prohibitting(String ids);

    /**
     * 启用预警设置
     *
     * @param
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    boolean enableding(String ids);

    /**
     * @Description:  获取用户下拉列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-03-12
     */
    List<Map<String, Object>> getSelectUserList(String queryString, int pageSize, int pageNo);

    /**
     * @Description:  获取用户下拉列表数据总条数
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-03-12
     */
    Integer getSelectUserTotal(String queryString);

}
