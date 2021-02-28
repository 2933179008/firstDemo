package com.tbl.modules.alarm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.alarm.entity.AlarmDeploy;
import com.tbl.modules.alarm.service.AlarmDeployService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.Role;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.RoleService;
import com.tbl.modules.platform.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预警部署Controller
 *
 * @author yuany
 * @date 2019-03-11
 */
@Controller
@RequestMapping(value = "/alarmDeploy")
public class AlarmDeployController extends AbstractController {

    //预警部署Service
    @Autowired
    private AlarmDeployService alarmDeployService;

    //用户Service
    @Autowired
    private UserService userService;

    //角色Service
    @Autowired
    private RoleService roleService;

    /**
     * 跳转到预警部署列表
     *
     * @return
     * @author yuany
     * @date 2019-03-11
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("techbloom/alarm/alarm_deploy_list");

        return mv;
    }

    /**
     * 获取预警部署列表
     *
     * @param queryJsonString
     * @return
     * @author yuany
     * @date 2019-03-11
     */
    @RequestMapping(value = "/getList.do")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!com.tbl.common.utils.StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = alarmDeployService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/alarm/alarm_deploy_edit");
        AlarmDeploy alarmDeploy = null;

        if (id != -1) {
            alarmDeploy = alarmDeployService.selectById(id);
        }

        //获取用户集合
        List<User> userList = userService.selectList(
                new EntityWrapper<>()
        );

        List<AlarmDeploy> alarmDeployList = alarmDeployService.selectList(
                new EntityWrapper<>()
        );
        Map<String, Object> map = DyylConstant.AlARM_TYPE();
        Map<String, Object> typeMap = new HashMap<>();
        if (!alarmDeployList.isEmpty()) {
            for (String alarmType : map.keySet()) {
                int i = 0;
                for (AlarmDeploy alarmDeploy1 : alarmDeployList) {
                    if (!alarmDeploy1.getAlarmType().equals(alarmType)) {
                        i++;
                    }
                    if (i == alarmDeployList.size()) {
                        typeMap.put(alarmType, map.get(alarmType));
                    }
                }
            }
        }else {
            for (String alarmType : map.keySet()){
                typeMap.put(alarmType,map.get(alarmType));
            }
        }
        //创建角色对象
        Role role = null;
        //遍历添加用户角色
        for (User user : userList) {
            if (user.getRoleId() != null) {
                role = roleService.selectById(user.getRoleId());
                if (role != null) {
                    user.setRole(role);
                } else {
                    role.setName("");
                    user.setRole(role);
                }
            } else {
                role.setName("");
                user.setRole(role);
            }
        }
        mv.addObject("alarmDeploy", alarmDeploy);
        mv.addObject("userList", userList);
        mv.addObject("typeMap", typeMap);
        mv.addObject("map", map);
        return mv;
    }

    /**
     * 保存预警设置
     *
     * @param alarmDeploy
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @RequestMapping(value = "/addAlarmDeploy")
    @ResponseBody
    public Map<String, Object> addAlarmDeploy(AlarmDeploy alarmDeploy) {

        Map<String, Object> map = new HashMap<>();
        boolean result = true;

        //判断修改/新增
        if (alarmDeploy.getId() != null) {
            result = alarmDeployService.updateById(alarmDeploy);
        } else {
            alarmDeploy.setApply(DyylConstant.ELABLE);
            result = alarmDeployService.insert(alarmDeploy);
        }

        map.put("result", result);

        return map;
    }

    /**
     * 预警设置删除
     *
     * @param id
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @RequestMapping(value = "/delAlarmDeploy")
    @ResponseBody
    public boolean delAlarmDeploy(Long id) {
        return alarmDeployService.delAlarmDeploy(id);
    }

    /**
     * 预警设置禁用
     *
     * @param ids
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @RequestMapping(value = "/prohibitting")
    @ResponseBody
    public boolean prohibitting(String ids) {
        return alarmDeployService.prohibitting(ids);
    }

    /**
     * 预警设置启用
     *
     * @param ids
     * @return
     * @author yuany
     * @date 2019-03-12
     */
    @RequestMapping(value = "/enableding")
    @ResponseBody
    public boolean enableding(String ids) {
        return alarmDeployService.enableding(ids);
    }

    /**
     * @Description: 获取用户下拉列表数据源
     * @return:
     * @Author: yuany
     * @Date: 2019-03-12
     */
//    @RequestMapping(value = "/getSelectUser")
//    @ResponseBody
//    public Map<String, Object> getSelectUser(String queryString, int pageNo, int pageSize) {
//        Map<String,Object> map=new HashMap<String,Object>();
//        List<Map<String, Object>> userlList =alarmDeployService.getSelectUserList(queryString, pageSize, pageNo);
//        map.put("result", userlList);
//        map.put("total", alarmDeployService.getSelectUserTotal(queryString));
//        return map;
//
//    }

    /**
     * 获取用户下拉数据
     *
     * @param username
     * @param addressesBys
     * @return Map
     * @author yuany
     * @date 2018-11-21
     */
    @RequestMapping(value = "/getSelectUser")
    @ResponseBody
    public Map<String, Object> getSelectUser(String username, String addressesBys) {
        Map<String, Object> map = new HashMap<>();

        // 根据条件获取未添加用户list实体
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.like(StringUtils.isNotEmpty(username), "username", username);
        List<User> lstUser = userService.selectList(userEntityWrapper);

        JSONArray arr = new JSONArray();
        JSONObject obj = null;
        for (User user : lstUser) {
            obj = new JSONObject();
            // 用户ID
            obj.put("id", user.getUserId());
            // 基站名称
            obj.put("text", user.getName());
            arr.add(obj);
        }

        // 根据条件获取该场景已添加的基站信息
        if (StringUtils.isNotBlank(addressesBys)) {
            Map<String, Object> sceneMap = new HashMap<>();
            sceneMap.put("user_id", addressesBys);
            List<User> userList = userService.selectByMap(sceneMap);
            if (userList != null && userList.size() > 0) {
                for (User user : userList) {
                    obj = new JSONObject();
                    obj.put("id", user.getUserId());
                    obj.put("text", user.getName());
                    arr.add(obj);
                }
            }
        }
        map.put("result", arr);
        return map;
    }

    /**
     * 获取用户信息（修改时，用户下拉框赋值使用）
     *
     * @return
     * @author yuany
     * @date 2019-01-213
     */
    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public JSONArray getPlatformInfo(String addressesBys) {
        JSONArray arr = new JSONArray();
        JSONObject obj = null;
        Map<String, Object> map = new HashMap<>();
        List<Long> lstUserIds = Arrays.stream(addressesBys.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<User> lstUser = userService.selectBatchIds(lstUserIds);
        if (lstUser != null && lstUser.size() > 0) {
            for (User user : lstUser) {
                obj = new JSONObject();
                obj.put("userId", user.getUserId());
                obj.put("name", user.getName());
                arr.add(obj);
            }
        }
        return arr;
    }
}
