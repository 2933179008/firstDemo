package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.platform.entity.system.Role;

import java.util.List;
import java.util.Map;

/**
 * 角色接口
 *
 * @author anss
 * @date 2018-09-11
 */
public interface RoleService extends IService<Role> {

    /**
     * 下拉列表获取所有角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<Map<String, Object>> getRoleList(Map<String, Object> params);

    /**
     * 下拉列表获取所有角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    PageUtils getRolePageListS(Map<String, Object> params);

    /**
     * 获取角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<Map<String, Object>> getRoles();

    /**
     * 根据角色主键获取角色菜单
     * @author anss
     * @date 2018-09-13
     * @param rid
     * @return List<Map>
     */
    List<Map<String, Object>> getMenuListByRid(Long rid);

    /**
     * 保存信息，并返回主键
     * @author anss
     * @date 2018-09-14
     * @param role 角色对象
     * @return Long
     */
    Long saveForGernatedKey(Role role);

    /**
     * 角色保存/修改
     * @author anss
     * @date 2018-09-14
     * @param role
     * @return boolean
     */
    boolean saveRole(Role role);

    /**
     * 批量保存角色对应菜单信息
     * @author anss
     * @date 2018-09-14
     * @param roleId 角色ID
     * @return boolean
     */
    boolean delRoleMenuByRoleId(long roleId);

    /**
     * 批量保存角色对应菜单信息
     * @author anss
     * @date 2018-09-14
     * @param roleId 角色ID
     * @param menuIds 菜单ID
     * @return boolean
     */
    boolean saveRoleMenu(long roleId, String[] menuIds);

    /**
     * 根据角色主键获取角色名称
     * @author anss
     * @date 2018-09-14
     * @param ids
     * @return String
     */
    String getRoleNameByRoleids(String ids);

    /**
     * 查询存在用户的角色列表
     * @author anss
     * @date 2018-09-14
     * @param ids 角色id
     * @return
     */
    List<Role> isHasRoleUser(String ids);

    /**
     * 删除角色信息
     * @author anss
     * @date 2018-09-14
     * @param ids
     * @return boolean
     */
    boolean delRole(String ids);

    /**
     * 查询是否有相同名称的角色
     * @author anss
     * @date 2018-09-14
     * @param name 角色名称
     * @param rid
     * @return boolean
     */
    boolean isHasRoleByName( String name, String rid);

    /**
     * 更新角色权限
     * @param roleId 角色id
     * @param qxName 权限名称
     * @param qxValue 权限值
     * @return
     */
    boolean updateRoleQX(String roleId,String qxName,String qxValue);

}
