package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.platform.entity.system.RoleMenu;

/**
 * 角色菜单接口
 *
 * @author anss
 * @date 2018-09-11
 */
public interface RoleMenuService extends IService<RoleMenu> {


    /**
     * 验证当前用户是否具有RFID数据权限
     * @author anss
     * @date 2018-09-11
     * @param id
     * @return
     */
    boolean getDataManage(Long id);
}
