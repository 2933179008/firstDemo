package com.tbl.modules.platform.service.impl.system;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.platform.dao.system.RoleMenuDAO;
import com.tbl.modules.platform.entity.system.RoleMenu;
import com.tbl.modules.platform.service.system.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单接口实现
 *
 * @author anss
 * @date 2018-09-11
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDAO, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuDAO roleMenuDAO;


    /**
     * 验证当前用户是否具有RFID数据权限
     * @param id
     * @return
     */
    @Override
    public boolean getDataManage(Long id) {
        List<RoleMenu> lstRoleMenu = roleMenuDAO.getDataManage(id);
        return lstRoleMenu.size() == 0 ? false : true;
    }

}
