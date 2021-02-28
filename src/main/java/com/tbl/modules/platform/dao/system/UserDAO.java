package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.common.utils.PageData;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.entity.system.UserModel;

import java.util.List;
import java.util.Map;

/**
 * 用户DAO
 *
 * @author anss
 * @date 2018-09-10
 */
public interface UserDAO extends BaseMapper<User> {

    /**
     * 获取用户列表数据
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<User> selectUserList(Pagination page, Map<String, Object> params);

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
     * @param map
     * @return User
     */
    User getUserByNameAndPwd(Map<String, Object> map);

    /**
     * @author anss
     * @date 2018-09-11
     * @param username
     * @return
     */
    User getUserAndRoleByIds(String username);

    /**
     * 根据名称获取用户
     * @param username
     * @return
     */
    int getByName(String username);

    /**
     * 根据主键查询用户
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @return User
     * */
    User findByUserId(Long userId);

    /**
     * 根据用户主键修改用户密码
     * @author anss
     * @date 2018-09-13
     * @param map
     * @return boolean
     */
    boolean updateUserColumn(Map<String, Object> map);

    /**
     * 通过loginname获取数据
     *  @author anss
     *  @date 2018-09-13
     * @param pd
     * @return User
     * */
    User findByUId(PageData pd);

    /**
     * 获取导出列
     * @author anss
     * @date 2018-09-13
     * @param ids
     * @return List<User>
     * */
    List<UserModel> getAllLists(List<Long> ids);

    /**
     * 根据用户信息获取用户能访问的菜单权限：这里指定一个设备检测的菜单
     * @author anss
     * @date 2018-09-13
     * @param userId
     * @return Integer
     */
    int getJcMenuByUserId(Long userId);

    /**
     * 获取所有用户列表:供下拉框使用
     * @author anss
     * @date 2018-09-17
     * @param map
     * @return
     */
    List<User> getUserPageList(Pagination page, Map<String, Object> map);
    
    /**
     * 获取用户下拉列表（台套化配置页面使用）
     * @author cxf
     * @date 2018-09-30
     * @param page
     * @param queryString
     * @return page
     */
    List<Map<String,Object>> getUserQuerySelect(Page<Map<String,Object>> page,Map<String, Object> map);

    /**
     * 获取未完成的入库单的数量
     * @return Integer
     * */
    Integer getInAmount();

    /**
     * 获取未完成的出库单的数量
     * @return Integer
     * */
    Integer getOutAmount();

    /**
     * 获取异常的数量
     * @return Integer
     * */
    Integer getAbnormalAmount();

}
