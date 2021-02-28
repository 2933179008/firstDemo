package com.tbl.modules.platform.service.impl.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.platform.dao.system.MenuDAO;
import com.tbl.modules.platform.entity.system.Menu;
import com.tbl.modules.platform.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单接口实现
 *
 * @author anss
 * @date 2018-09-11
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDAO, Menu> implements MenuService {

    //菜单DAO
    @Autowired
    private MenuDAO menuDAO;


    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-11
     * @return
     */
    @Autowired
    public List<Menu> listAllMenuByRoleId(Map<String, Object> menuMap){
        Map<String, Object> map = null;
        List<Menu> rl = menuDAO.listAllParentMenu(menuMap);
        for(Menu menu : rl){
            map = menuMap;
            map.put("parentId", menu.getMenuId());
            List<Menu> subList = menuDAO.listSubMenuByParentId(map);
            if( subList != null && subList.size() > 0){
                menu.setSubMenu(subList);
                menu.setHasMenu(true);
            }
        }
        return rl;
    }

    /**
     * 获取角色所有快捷菜单
     * @author anss
     * @date 2018-09-11
     * @param map
     * @return
     */
    @Autowired
    public List<Menu> listquickMenu(Map<String, Object> map) {
        return menuDAO.listquickMenu(map);
    }


    /**
     * 获取所有菜单分页列表
     * @author anss
     * @date 2018-09-15
     * @return
     */
    @Override
    public PageUtils getMenuPageList(Map<String, Object> params) {
        Page<Menu> page = this.selectPage(
                new Query<Menu>(params).getPage(),
                new EntityWrapper<>()
        );
        return new PageUtils(page.setRecords(menuDAO.selectMenuList(page, params)));
    }

    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-15
     * @return List<Menu>
     */
    @Override
    public List<Menu> listAllMenu() {

        return baseMapper.selectByMap(new HashMap<>());
    }

    /**
     * 获取所有菜单列表
     * @author anss
     * @date 2018-09-15
     * @return List<Menu>
     */
    @Override
    public List<Menu> listAllMenu(Long parentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("parent_id", parentId);
        return baseMapper.selectByMap(map);
    }

}
