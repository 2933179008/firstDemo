package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.platform.entity.system.UserMenu;

import java.util.List;
import java.util.Map;

/**
 * 用户菜单DAO
 *
 * @author anss
 * @date 2018-09-12
 */
public interface UserMenuDAO extends BaseMapper<UserMenu> {


    /**
     * 获取用户菜单
     * @author anss
     * @date 2018-09-11
     * @param map
     * @return
     */
    List<Map<String, Object>> getUserMenu(Map<String, Object> map);

    /**
     * 获取用户菜单11
     * @author anss
     * @date 2018-09-11
     * @param map
     * @return
     */
    List<Map<String, Object>> getUserMenu11(Map<String, Object> map);

    /**
     * 根据用户id删除用户菜单信息
     * @param map
     * @return
     */
    boolean deleteUserMenu(Map<String, Object> map);

    /**
     * 保存用户菜单信息
     * @param map
     * @return
     */
    boolean insertUserMenus(Map<String, Object> map);

    /**
     * 供应商管理    插入
     * @param userMenu
     * @return
     */
    int insertMenu(UserMenu userMenu);


}
