package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.platform.entity.system.Menu;

import java.util.List;
import java.util.Map;

/**
 * 菜单接口
 *
 * @author anss
 * @date 2018-09-11
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-11
     * @return
     */
    List<Menu> listAllMenuByRoleId(Map<String, Object> menuMap);

    /**
     * 获取角色所有快捷菜单
     * @author anss
     * @date 2018-09-11
     * @param map
     * @return
     */
    List<Menu> listquickMenu(Map<String, Object> map);

    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-11
     * @return List<Menu>
     */
    List<Menu> listAllMenu();

    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-15
     * @return List<Menu>
     */
    List<Menu> listAllMenu(Long parentId);

    /**
     * 获取所有菜单分页列表
     * @author anss
     * @date 2018-09-15
     * @return
     */
    PageUtils getMenuPageList(Map<String, Object> params);

}
