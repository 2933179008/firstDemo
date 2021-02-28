package com.tbl.modules.platform.dao.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.platform.entity.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色DAO
 *
 * @author anss
 * @date 2018-09-11
 */
public interface RoleDAO extends BaseMapper<Role> {


    /**
     * 获取橘色列表数据
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<Map<String, Object>> getRoleList(Map<String, Object> params);

    /**
     * 获取角色列表数据
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<Role> selectRoleListS(Pagination page, Map<String, Object> params);

    /**
     * 获取角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    List<Map<String, Object>> getRoles();

    /**
     * 根据角色主键获取角色菜单1
     * @author anss
     * @date 2018-09-13
     * @param rid
     * @return List<Map>
     */
    List<Map<String, Object>> getMenuListByRidOne(Long rid);

    /**
     * 根据角色主键获取角色菜单2
     * @author anss
     * @date 2018-09-13
     * @return List<Map>
     */
    List<Map<String, Object>> getMenuListByRidTwo();

    /**
     * 查询存在用户的角色列表
     * @author anss
     * @date 2018-09-14
     * @param ids 角色id
     * @return
     */
    List<Role> isHasRoleUser(List<Long> ids);

    /**
     * 查询是否有相同名称的角色
     * @author anss
     * @date 2018-09-14
     * @param map
     * @return Integer
     */
    Integer isHasRoleByName(Map<String, Object> map);

    /**
     * 更新角色权限
     * @param roleId
     * @param qxName
     * @param qxValue
     * @return
     */
    boolean updateRoleQX(@Param("roleId") String roleId, @Param("qxName") String qxName, @Param("qxValue") String qxValue);

}
