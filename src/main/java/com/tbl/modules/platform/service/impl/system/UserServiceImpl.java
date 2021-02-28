package com.tbl.modules.platform.service.impl.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.*;
import com.tbl.modules.platform.dao.system.RoleDAO;
import com.tbl.modules.platform.dao.system.RoleMenuDAO;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.dao.system.UserMenuDAO;
import com.tbl.modules.platform.entity.system.RoleMenu;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.entity.system.UserModel;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.platform.util.DeriveExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户接口实现
 *
 * @author anss
 * @date 2018-09-10
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDAO, User> implements UserService {

    //用户DAO
    @Autowired
    private UserDAO userDAO;
    //角色service
    @Autowired
    private RoleDAO roleDAO;
    //角色菜单DAO
    @Autowired
    private RoleMenuDAO roleMenuDAO;
    //用户菜单DAO
    @Autowired
    private UserMenuDAO userMenuDAO;


    /**
     * 获取分页列表
     * @author anss
     * @date 2018-09-13
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<User> page = this.selectPage(
                new Query<User>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page.setRecords(userDAO.selectUserList(page, params)));
    }

    /**
     * 根据登录名获取实体
     * @author anss
     * @date 2018-09-10
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = null;
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<User> lstUser = baseMapper.selectByMap(map);
        if (lstUser != null && lstUser.size() > 0) {
            user = lstUser.get(0);
        }
        return user;
    }

    /**
     * 判断登录用户属于哪个用户
     * @author anss
     * @date 2018-09-11
     * @param username
     * @return Integer
     */
    @Override
    public Integer getByUserName(String username){
        return userDAO.getByUserName(username);
    }

    /**
     * 登录判断
     * @author anss
     * @date 2018-09-11
     * @param username
     * @param pwd
     * @return User
     */
    @Override
    public User getUserByNameAndPwd(String username,String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("pwd", pwd);

        return userDAO.getUserByNameAndPwd(map);
    }

    /**
     * 更新登录时间
     * @author anss
     * @date 2018-09-11
     * @param pd
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLastLogin(PageData pd) {
        User user = this.selectById(pd.getLong("USER_ID"));
        if (user != null && !StringUtils.isEmpty(pd.getString("LAST_LOGIN"))) {
            user.setLastLogin(DateUtils.stringToDate(pd.getString("LAST_LOGIN"), "yyyy-MM-dd HH:mm:ss").getTime());
        }
        this.updateById(user);
    }

    /**
     * 通过id获取数据
     * @author anss
     * @date 2018-09-11
     * @param USER_ID
     * @return User
     */
    @Override
    public User getUserAndRoleById(long USER_ID) {
        User user = baseMapper.selectById(USER_ID);
        if( user != null){
            user.setRole(roleDAO.selectById(user.getRoleId()));
        }
        return user;
    }

    /**
     * 验证当前用户是否具有RFID数据权限
     * @author anss
     * @date 2018-09-11
     * @param id
     * @return boolean
     */
    @Override
    public boolean getDataManage(Long id) {
        List<RoleMenu> lstRoleMenu = roleMenuDAO.getDataManage(id);
        return lstRoleMenu.size() == 0 ? false : true;
    }

    /**
     * 检查当前用户是否有设备管理权限
     * @param id
     * @return
     */
    @Override
    public boolean getDeviceT(Long id) {
        List<RoleMenu> lstRoleMenu = roleMenuDAO.getDeviceT(id);
        return lstRoleMenu.size() == 0 ? false : true;
    }

    /**
     * 获取用户菜单
     * @author anss
     * @date 2018-09-11
     * @param id
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> getUserMenu(Long id, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("type", Long.valueOf(type));
        return userMenuDAO.getUserMenu(map);
    }

    /**
     * 获取用户菜单
     * @author anss
     * @date 2018-09-11
     * @param id
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> getUserMenu11(Long id, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("type", Long.valueOf(type));
        return userMenuDAO.getUserMenu11(map);
    }

    /**
     * @author anss
     * @date 2018-09-12
     * @param menus
     * @param userid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setUserMenu(String menus, Long userid) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userid);
        map.put("type", 0l);
        userMenuDAO.deleteUserMenu(map);

        if (StringUtils.isNotBlank(menus)) {
            String[] list = menus.split(",");

            Map<String, Object> userMenuMap = null;
            for(int i = 0;i < list.length; i++){
                userMenuMap = new HashMap<>();
                userMenuMap.put("userId", userid);
                userMenuMap.put("menuId", Long.valueOf(list[i]));
                userMenuMap.put("order", Long.valueOf(i+1));
                userMenuMap.put("type", 0l);
                userMenuMap.put("isQuick", null);

                userMenuDAO.insertUserMenus(userMenuMap);
            }
        }
        return true;
    }

    /**
     * @author anss
     * @date 2018-09-13
     * @param menus
     * @param userid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setUserQuickMenu(String menus, Long userid) {
        Map<String, Object> umMap = new HashMap<>();
        umMap.put("user_id", userid);
        umMap.put("type", 0l);
        umMap.put("isQuick", 1l);

        userMenuDAO.deleteUserMenu(umMap);

        String[] list = menus.split(",");
        Map<String, Object> userMenuMap = null;
        for(int i = 0;i < list.length; i++){
            userMenuMap = new HashMap<>();
            userMenuMap.put("userId", userid);
            userMenuMap.put("menuId", Long.valueOf(list[i]));
            userMenuMap.put("order", Long.valueOf(i+1));
            userMenuMap.put("type", 0l);
            userMenuMap.put("isQuick", 1l);

            userMenuDAO.insertUserMenus(userMenuMap);
        }

        return true;
    }

    /**
     * 设置快捷菜单s
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean setUserQuickMenus(String menus, Long userid) {
        Map<String, Object> umMap = new HashMap<>();
        umMap.put("user_id", userid);
        umMap.put("type", 1l);
        umMap.put("isQuick", 1l);

        boolean b  = userMenuDAO.deleteUserMenu(umMap);
        if(b){
            String[] list = menus.split(",");
            Map<String, Object> userMenuMap = null;
            for(int i = 0;i < list.length; i++){
                userMenuMap = new HashMap<>();
                userMenuMap.put("userId", userid);
                userMenuMap.put("menuId", Long.valueOf(list[i]));
                userMenuMap.put("order", Long.valueOf(i+1));
                userMenuMap.put("type", 1l);
                userMenuMap.put("isQuick", 1l);

                userMenuDAO.insertUserMenus(userMenuMap);
            }
        }
        return b;
    }


    /**
     * 保存/更新用户
     * @author anss
     * @date 2018-09-13
     * @param user
     * @return boolean
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveU(User user){
        boolean ret = false;
        //添加
        if (user.getUserId() == null) {
            ret = this.insert(user);
        //修改
        } else {
            ret = this.updateById(user);

            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user.getUserId());
            userMenuDAO.deleteUserMenu(map);
        }
        return ret;
    }

    /**
     * 根据主键查询用户
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @return User
     * */
    @Override
    public User findByUserId(String userId){
        return userDAO.findByUserId(Long.valueOf(userId));
    }

    /**
     * 根据用户主键修改用户密码
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @param columnName
     * @param columnValue
     * @return boolean
     */
    @Override
    public boolean updateUserColumn(long userId ,String columnName,String columnValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("columnValue", columnValue);
        return userDAO.updateUserColumn(map);
    }

    /**
     * 通过loginname获取数据
     *  @author anss
     *  @date 2018-09-13
     * @param pd
     * @return User
     * */
    @Override
    public User findByUId(PageData pd) {
        return userDAO.findByUId(pd);
    }

    /**
     * 根据用户主键获取用户名称
     * @author anss
     * @date 2018-09-13
     * @param ids    多个用户主键
     * @return String
     */
    @Override
    public String getUserNameByIds(String ids){
        String username = "";
        List<Long> lstId = Arrays.asList(ids.replaceAll("'", "").split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<User> lstUser = this.selectBatchIds(lstId);
        for (User user: lstUser) {
            username += user.getUsername() + ", ";
        }
        if (!StringUtils.isEmpty(username)) {
            username = username.substring(0, username.length() -1);
        }
        return username;
    }

    /**
     * 更改制定用户状态
     * @author anss
     * @date 2018-09-13
     * @param state 用户状态1：启用，2：停用
     * @param ids 用户ID，多个以逗号隔开
     * @return boolean
     */
    @Override
    public boolean  changeState(String state,String ids) {
        List<Long> lstId = Arrays.asList(ids.replaceAll("'", "").split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<User> lstUser = this.selectBatchIds(lstId);
        for (User user: lstUser) {
            user.setStatus(Long.valueOf(state));
        }
        return this.updateBatchById(lstUser);
    }

    /**
     * 获取导出列
     * @author anss
     * @date 2018-09-13
     * @param ids
     * @return List<User>
     * */
    @Override
    public List<UserModel> getAllLists(String ids) {
        List<UserModel> list = userDAO.getAllLists(StringUtils.stringToList(ids));
        list.forEach(a->{
            if(!StringUtils.isEmptyString(a.getLastLogin())){
                a.setLastLogin(DateUtils.longToStringParams(Long.parseLong(a.getLastLogin()),"yyyy-MM-dd HH:mm"));
            }
        });
        return list;
    }

    /**
     * 导出excel
     * @author anss
     * @date 2018-09-13
     * @param response
     * @param path
     * @param list
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<UserModel> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "用户管理表" + "(" + date + ")";
            if (path != null && !"".equals(path)) {
                sheetName = sheetName + ".xls";
            } else {
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-disposition", "attachment;filename="
                        + new String(sheetName.getBytes("gbk"), "ISO8859-1") + ".xls");
            }
            for (UserModel in : list) {
                if (in.getStatus().equals(1L)) {
                    in.setStateName("启用");
                } else if (in.getStatus().equals(2L)) {
                    in.setStateName("禁用");
                } else {
                    in.setStateName("未知");
                }
            }
            Map<String, String> mapFields = new LinkedHashMap<String, String>();
            mapFields.put("username", "用户名");
            mapFields.put("name", "姓名");
            mapFields.put("roleName", "所属角色");
//            mapFields.put("deptName", "所属部门");
            mapFields.put("StateName", "状态");
            mapFields.put("email", "邮箱");
            mapFields.put("phone", "手机号码");
            mapFields.put("lastLogin", "最近登录时间");
//            mapFields.put("ip", "上次登录IP");
            mapFields.put("remark", "备注");
            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户列表:供下拉框使用
     * @author anss
     * @date 2018-09-17
     * @param map
     * @return
     */
    @Override
    public PageUtils getUserPageList(Map<String, Object> map) {
        Page<User> page = this.selectPage(
                new Query<User>(map).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page.setRecords(userDAO.getUserPageList(page, map)));
    }
    
    /**
     * 获取用户下拉列表（台套化配置页面使用）
     * @author cxf
     * @date 2018-09-30
     * @param page
     * @param queryString
     * @return page
     */
    @Override
    public Page<Map<String,Object>> getUserSelect(PageTbl page,String queryString){
    	int current = page.getPageno();
        int limit = page.getPagesize();
        Page<Map<String,Object>> pages = new Page<>(current, limit);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("queryString", queryString);
        return pages.setRecords(userDAO.getUserQuerySelect(pages, map));
    }

    @Override
    public List<Map<String, Object>> getLstMapUser(Map<String, Object> map) {

        Page<User> page = this.selectPage(
                new Query<User>(map).getPage(),
                new EntityWrapper<>()
        );

        List<Map<String, Object>> lstMap = new ArrayList<>();
        Map<String, Object> userMap = null;
        List<User> lstUser = userDAO.getUserPageList(page, map);
        for (User user:lstUser) {
            userMap = new HashMap<>();
            userMap.put("id", user.getUserId());
            userMap.put("text", user.getText());
            lstMap.add(userMap);
        }

        return lstMap;
    }

    /**
     * 根据条件获取用户list
     * @author anss
     * @date 2018-09-19
     * @param map
     */
    @Override
    public List<User> getLstUserByCondition(Map<String, Object> map) {
        return userDAO.selectByMap(map);
    }

    /**
     * @author anss
     * @date 2018-10-07
     * @param role_id
     * @return
     */
    @Override
    public Map<String, Integer> getDeviceOrDataAutor(Long role_id) {
        Map<String,Integer> list = new HashMap<>();
        return list;
    }

    /**
     * 获取未完成的入库单的数量
     * @return Integer
     */
    @Override
    public Integer getInAmount() {
        return userDAO.getInAmount();
    }

    /**
     * 获取未完成的出库单的数量
     * @return Integer
     */
    @Override
    public Integer getOutAmount() {
        return userDAO.getOutAmount();
    }

    /**
     * 获取异常的数量
     * @return Integer
     */
    @Override
    public Integer getAbnormalAmount() {
        return userDAO.getAbnormalAmount();
    }
}
