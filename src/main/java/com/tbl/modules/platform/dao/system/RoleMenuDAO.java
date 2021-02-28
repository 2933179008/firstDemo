package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.platform.entity.system.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * 角色菜单DAO
 *
 * @author anss
 * @date 2018-09-11
 */
public interface RoleMenuDAO extends BaseMapper<RoleMenu> {

    /**
     * 验证当前用户是否具有RFID数据权限
     * @param id
     * @return
     */
    List<RoleMenu> getDataManage(Long id);

    /**
     * 检查当前用户是否有设备管理权限
     * @param id
     * @return
     */
    List<RoleMenu> getDeviceT(Long id);

    /**
     * 保存角色对应菜单信息
     * @author anss
     * @date 2018-09-14
     * @param map
     * @return boolean
     */
    boolean saveRoleMenu(Map<String, Object> map);

}
