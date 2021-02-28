package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.platform.entity.system.Menu;

import java.util.List;
import java.util.Map;

/**
 * 菜单DAO
 *
 * @author anss
 * @date 2018-09-11
 */
public interface MenuDAO extends BaseMapper<Menu> {

    /**
     * 获取角色所有顶层菜单
     * @author anss
     * @date 2018-09-11
     * @param menuMap
     * @return
     */
    List<Menu> listAllParentMenu(Map<String, Object> menuMap);

    /**
     * 根据父菜单ID获取所有子菜单
     * @author anss
     * @date 2018-09-11
     * @return List<Menu>
     */
    List<Menu> listSubMenuByParentId(Map<String, Object> menuMap);

    /**
     * 获取角色所有快捷菜单
     * @author anss
     * @date 2018-09-11
     * @param map
     * @return
     */
    List<Menu> listquickMenu(Map<String, Object> map);

    /**
     * 获取到模块id
     * @return
     */
    List<Map<String,Object>> selectMenuid();

    /**
     * 获取菜单列表数据
     * @author anss
     * @date 2018-09-15
     * @return
     */
    List<Menu> selectMenuList(Pagination page, Map<String, Object> params);

}
