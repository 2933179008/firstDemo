package com.tbl.modules.platform.service.impl.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.dao.system.RoleDAO;
import com.tbl.modules.platform.dao.system.RoleMenuDAO;
import com.tbl.modules.platform.entity.system.Role;
import com.tbl.modules.platform.service.system.RoleMenuService;
import com.tbl.modules.platform.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色接口实现
 *
 * @author anss
 * @date 2018-09-11
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDAO, Role> implements RoleService {

    //角色DAO
    @Autowired
    private RoleDAO roleDAO;
    //角色菜单DAO
    @Autowired
    private RoleMenuDAO roleMenuDAO;
    //角色菜单servie
    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 下拉列表获取所有角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @Override
    public List<Map<String, Object>> getRoleList(Map<String, Object> params) {

        return roleDAO.getRoleList(params);
    }

    /**
     * 获取所有角色分页列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @Override
    public PageUtils getRolePageListS(Map<String, Object> params) {
        Page<Role> page = this.selectPage(
                new Query<Role>(params).getPage(),
                new EntityWrapper<>()
        );
        return new PageUtils(page.setRecords(roleDAO.selectRoleListS(page, params)));
    }

    /**
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @Override
    public List<Map<String, Object>> getRoles() {
        return roleDAO.getRoles();
    }

    /**
     * 根据角色主键获取角色菜单
     * @author anss
     * @date 2018-09-13
     * @param rid
     * @return List<Map>
     */
    @Override
    public List<Map<String, Object>> getMenuListByRid(Long rid) {
        List<Map<String, Object>> list = null;
        String msql = "";
        if(rid!=null){
            list = roleDAO.getMenuListByRidOne(rid);
            Map<String, ? extends Object> rootm = ImmutableMap.of("id", 0, "pId", -1,
                    "name", "所有菜单", "checked", false);
            List<Map<String, Object>> alllist = Lists.newArrayList();
            alllist.add((Map<String, Object>) rootm);
            alllist.addAll(list);
            return alllist;
        }else{
            list = roleDAO.getMenuListByRidTwo();
        }
        return list;
    }

    /**
     * 保存信息，并返回主键
     * @author anss
     * @date 2018-09-14
     * @param role 角色对象
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveForGernatedKey(Role role){
        baseMapper.insert(role);
        return role.getRoleId();
    }

    /**
     * 角色保存/修改
     * @author anss
     * @date 2018-09-14
     * @param role
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRole(Role role) {
        boolean ret = false;
        Long roleId = role.getRoleId();

        // 更新
        if( roleId != null ){
            ret = this.updateById(role);
        // 新增
        }else{
            this.insert(role);
        }
        return ret;
    }

    /**
     * 批量保存角色对应菜单信息
     * @author anss
     * @date 2018-09-14
     * @param roleId 角色ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delRoleMenuByRoleId(long roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", roleId);
        return roleMenuService.deleteByMap(map);
    }

    /**
     * 批量保存角色对应菜单信息
     * @author anss
     * @date 2018-09-14
     * @param roleId 角色ID
     * @param menuIds 菜单ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleMenu(long roleId, String[] menuIds) {
        boolean flag = false;
        Map<String, Object> map = null;
        String sql[] = new String[menuIds.length];
        for( int i = 0; i < menuIds.length; i++ ){
            map = new HashMap<>();
            map.put("roleId", roleId);
            map.put("menuId", Long.valueOf(menuIds[i]));
            flag = roleMenuDAO.saveRoleMenu(map);
        }
        return flag;
    }

    /**
     * 根据角色主键获取角色名称
     * @author anss
     * @date 2018-09-14
     * @param ids
     * @return String
     */
    @Override
    public String getRoleNameByRoleids(String ids) {
        String rolename = "";
        if(StringUtils.isNotEmpty(ids)){
            List<Long> lstId = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<Role> lstRole = baseMapper.selectBatchIds(lstId);
            for (Role role: lstRole) {
                rolename += role.getRoleName() + ",";
            }
            if (!StringUtils.isEmpty(rolename)) {
                rolename = rolename.substring(0, rolename.length() -1);
            }
        }
        return rolename;
    }

    /**
     * 查询存在用户的角色列表
     * @author anss
     * @date 2018-09-14
     * @param ids 角色id
     * @return
     */
    @Override
    public List<Role> isHasRoleUser(String ids) {
        List<Role> roleList = new ArrayList<Role>();
        if(!StringUtils.isEmptyString(ids)){
            List<Long> lstId = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            roleList = roleDAO.isHasRoleUser(lstId);
        }
        return roleList;
    }

    /**
     * 删除角色信息
     * @author anss
     * @date 2018-09-14
     * @param ids
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delRole(String ids) {
        List<Long> lstId = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return this.deleteBatchIds(lstId);
    }

    /**
     * 查询是否有相同名称的角色
     * @author anss
     * @date 2018-09-14
     * @param name 角色名称
     * @param rid
     * @return boolean
     */
    @Override
    public boolean isHasRoleByName( String name,String rid ) {
        boolean ret = false;
        if( !StringUtils.isEmptyString(name) ){
            Map<String, Object> map = new HashMap<>();
            map.put("roleName", name);
            map.put("roleId", rid);

            Integer _total = roleDAO.isHasRoleByName(map);
            int count = (_total == null ? 0 : _total.intValue());
            if( count > 0 ){
                ret = true;
            }
        }
        return ret;
    }

    /**
     * 更新角色权限
     * @param roleId 角色id
     * @param qxName 权限名称
     * @param qxValue 权限值
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleQX(String roleId,String qxName,String qxValue){

        return roleDAO.updateRoleQX(roleId, qxName, qxValue);
    }

}
