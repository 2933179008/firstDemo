package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageData;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.entity.system.UserModel;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户service
 *
 * @author anss
 * @date 2018-09-10
 */
public interface UserService extends IService<User> {


    /**
     * 获取分页列表
     * @author anss
     * @date 2018-09-13
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据登录名获取实体
     * @author anss
     * @date 2018-09-10
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 判断登录用户属于哪个用户
     * @author anss
     * @date 2018-09-11
     * @param username
     * @return Integer
     */
    Integer getByUserName(String username);

    /**
     * 登录判断
     * @author anss
     * @date 2018-09-11
     * @param username
     * @param pwd
     * @return User
     */
    User getUserByNameAndPwd(String username,String pwd);

    /**
     * 更新登录时间
     * @author anss
     * @date 2018-09-11
     * @param pd
     */
    void updateLastLogin(PageData pd);

    /**
     * 通过id获取数据
     * @author anss
     * @date 2018-09-11
     * @param USER_ID
     * @return User
     */
    User getUserAndRoleById(long USER_ID);

    /**
     * 验证当前用户是否具有RFID数据权限
     * @author anss
     * @date 2018-09-11
     * @param id
     * @return boolean
     */
    boolean getDataManage(Long id);

    /**
     * 检查当前用户是否有设备管理权限
     * @author anss
     * @date 2018-09-11
     * @param id
     * @return
     */
    boolean getDeviceT(Long id);

    /**
     * 获取用户菜单
     * @author anss
     * @date 2018-09-11
     * @param id
     * @param type
     * @return
     */
    List<Map<String, Object>> getUserMenu(Long id, String type);

    /**
     * 获取用户菜单
     * @author anss
     * @date 2018-09-11
     * @param id
     * @param type
     * @return
     */
    List<Map<String, Object>> getUserMenu11(Long id, String type);

    /**
     *  @author anss
     *  @date 2018-09-13
     * @param menus
     * @param userid
     * @return
     */
    boolean setUserMenu(String menus, Long userid);

    /**
     * @author anss
     * @date 2018-09-13
     * @param menus
     * @param userid
     * @return
     */
    boolean setUserQuickMenu(String menus, Long userid);

    /**
     * 保存/更新用户
     * @author anss
     * @date 2018-09-13
     * @param user
     * @return boolean
     */
    boolean saveU(User user);

    /**
     * 设置快捷菜单s
     * @author anss
     * @date 2018-09-13
     * @return
     */
    boolean setUserQuickMenus(String menus, Long userid);

    /**
     * 根据主键查询用户
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @return User
     */
    User findByUserId(String userId);

    /**
     * 根据用户主键修改用户密码
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @param columnName
     * @param columnValue
     * @return boolean
     */
    boolean updateUserColumn(long userId ,String columnName,String columnValue);

    /**
     * 通过loginname获取数据
     *  @author anss
     *  @date 2018-09-13
     * @param pd
     * @return User
     * */
    User findByUId(PageData pd);

    /**
     * 根据用户主键获取用户名称
     * @author anss
     * @date 2018-09-13
     * @param ids    多个用户主键
     * @return String
     */
    String getUserNameByIds(String ids);

    /**
     * 更改制定用户状态
     * @author anss
     * @date 2018-09-13
     * @param state 用户状态1：启用，2：停用
     * @param ids 用户ID，多个以逗号隔开
     * @return boolean
     */
    boolean  changeState(String state,String ids);

    /**
     * 获取导出列
     * @author anss
     * @date 2018-09-13
     * @param ids
     * @return List<User>
     */
    List<UserModel> getAllLists(String ids);

    /**
     * 导出excel
     * @author anss
     * @date 2018-09-13
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<UserModel> list);

    /**
     * 获取所有用户列表:供下拉框使用
     * @author anss
     * @date 2018-09-17
     * @param map
     * @return
     */
    PageUtils getUserPageList(Map<String, Object> map);

    /**
     * 获取用户列表信息（下拉框使用）
     * @param map
     * @return
     */
    List<Map<String, Object>> getLstMapUser(Map<String, Object> map);

    /**
     * 根据条件获取用户list
     * @author anss
     * @date 2018-09-19
     * @param map
     */
    List<User> getLstUserByCondition(Map<String, Object> map);
    
    /**
     * 获取用户下拉列表（台套化配置页面使用）
     * @author cxf
     * @date 2018-09-30
     * @param page
     * @param queryString
     * @return page
     */
    Page<Map<String,Object>> getUserSelect(PageTbl page,String queryString);

    /**
     * @author anss
     * @date 2018-10-07
     * @param role_id
     * @return
     */
    Map<String,Integer> getDeviceOrDataAutor(Long role_id);

    /**
     * 获取未完成的入库单的数量
     * @return Integer
     */
    Integer getInAmount();

    /**
     * 获取未完成的出库单的数量
     * @return Integer
     */
    Integer getOutAmount();

    /**
     * 获取异常的数量
     * @return Integer
     */
    Integer getAbnormalAmount();

}
