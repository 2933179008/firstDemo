package com.tbl.modules.platform.controller.system;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.PageData;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.constant.LogActionConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.Role;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.OperationLogService;
import com.tbl.modules.platform.service.system.RoleService;
import com.tbl.modules.platform.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色controller
 *
 * @author anss
 * @date 2018-09-13
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends AbstractController {

    //角色service
    @Autowired
    private RoleService roleService;
    //日志service
    @Autowired
    private OperationLogService operationLogService;
    //用户service
    @Autowired
    private UserService userService;


    /**
     * 跳转到角色列表页面
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @RequestMapping(value="/toList.do")
    @ResponseBody
    public ModelAndView systemLog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/platform/system/role/role_list");
        return mv;
    }

    /**
     * 获取角色列表
     * @param response
     * @param queryJsonString
     * @return
     */
    @RequestMapping(value = "/roleList.do")
    @ResponseBody
    public Object roleList(HttpServletResponse response, String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        String sortName = page.getSortname();
        if( StringUtils.isEmptyString(sortName) )
        {
            sortName = "role_id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if(StringUtils.isEmptyString(sortOrder))
        {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils pageRole = roleService.getRolePageListS(map);
        page.setTotalRows(pageRole.getTotalCount()==0?1:pageRole.getTotalCount());
        map.put("rows",pageRole.getList());
        executePageMap(map,page);

        return map;
    }

    /**
     * 获取列表信息：下拉框使用
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @RequestMapping(value="/getRoleList")
    @ResponseBody
    public Map<String, Object> getRoleList(String queryString, int pageSize, int pageNo){
        Map<String,Object> map = new HashMap<>();

        PageTbl page = this.getPage();
        map.put("page",pageNo);
        map.put("limit",pageSize);
        String sortOrder = page.getSortorder();
        if(StringUtils.isEmptyString(sortOrder))
        {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("dname", queryString);
        PageUtils pageRole = roleService.getRolePageListS(map);

        map.put("result", roleService.getRoleList(map));
        map.put("total", pageRole.getTotalCount());
        return map;
    }

    /**
     * 获取角色列表
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @RequestMapping(value ="/getRoles")
    @ResponseBody
    public List<Map<String, Object>>getRoles(){
        return roleService.getRoles();
    }

    /**
     * 根据角色主键获取角色信息
     * @author anss
     * @date 2018-09-13
     * @param rid
     * @return
     */
    @RequestMapping(value="/getRoleById")
    @ResponseBody
    public Role getRoleById(Long rid){
        Role role = null;
        if(rid!=null){
            role = roleService.selectById(rid);
        }
        return role;
    }

    /**
     * 根据角色主键获取角色菜单
     * @author anss
     * @date 2018-09-13
     * @param rid
     * @return
     */
    @RequestMapping(value = "/getMenulistByRoleId")
    @ResponseBody
    public List<Map<String, Object>> getMenulistByRoleId(Long rid){
        List<Map<String, Object>> list = null;
        list = roleService.getMenuListByRid(rid);
        return list;
    }

    /**
     * 返回到角色添加页面
     * @author anss
     * @date 2018-09-13
     * @return
     */
    @RequestMapping(value="/role_edit")
    public String roledit() {
        return "techbloom/platform/system/role/role_edit";
    }

    /**
     * 保存/修改角色信息
     * @author anss
     * @date 2018-09-14
     */
    @RequestMapping(value = "/addRole")
    @ResponseBody
    public boolean addRole(Role role) {
        PageData pd = this.getPageData();
        boolean result = false;
        Long roleId = role.getRoleId();
        if (roleId == null) {
            //新增(保存角色信息)
            roleId = roleService.saveForGernatedKey(role);
            if(roleId>0){
                operationLogService.logInsert("新增角色["+role.getRoleName()+"]成功！", LogActionConstant.USER_ADD, this.getRequest());
            }
        } else {
            //更新(该方法新增/修改都可以)
            boolean res = roleService.saveRole(role);
            if(res){
                operationLogService.logInsert("修改角色["+role.getRoleName()+"]成功！", LogActionConstant.USER_EDIT, this.getRequest());
            }
        }

        if (roleId != null) {
            //删除角色之前的权限，重新插入选中的角色权限
            roleService.delRoleMenuByRoleId(roleId);
            String menuIds = pd.getString("menuId");

            Map<String, Object> map = null;
            if (!StringUtils.isEmptyString(menuIds)) {
                String[] menuArray = menuIds.split(",");
                //保存角色菜单信息
                result = roleService.saveRoleMenu(roleId, menuArray);

                //获取该角色下的人员
                map = new HashMap<>();
                map.put("role_id", roleId);
                List<User> lstUser = userService.getLstUserByCondition(map);
                for (User user : lstUser) {
                    userService.setUserMenu(menuIds, user.getUserId());
                }

            } else {
                //不选择菜单权限的时候
                result = true;
            }
        }

        return result;
    }


    /**
     * 删除
     * @author anss
     * @date 2018-09-14
     */
    @RequestMapping(value = "/delRole")
    @ResponseBody
    public Object deleteRole() {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        String ids = pd.getString("ids");
        String rolenames = roleService.getRoleNameByRoleids(ids);
        boolean result = false;
        if (!StringUtils.isEmptyString(ids)) {
            List<Role> roleList = roleService.isHasRoleUser(ids);
            if (roleList != null && roleList.size() > 0) {
                String roleNames = "";
                for (Role role : roleList) {
                    roleNames += (StringUtils.isEmptyString(roleNames) ? "" : "，") + role.getRoleName();
                }
                map.put("roleInfo", roleNames);
            } else {
                result = roleService.delRole(ids);
                if(result){
                    operationLogService.logInsert("删除了["+rolenames+"]角色！", LogActionConstant.USER_DELETE, this.getRequest());
                }
            }
        }
        map.put("_result", result);
        return map;
    }

    /**
     * 判断是否有相同名称的角色
     * @author anss
     * @date 2018-09-14
     * @return
     */
    @RequestMapping(value = "/hasR.do")
    @ResponseBody
    public Object hasRole() {
        //true通过（不存在重复），false不通过（存在重复）
        boolean ret = true;
        HttpServletRequest request = this.getRequest();
        String _id = request.getParameter("_id");
        String roleName = request.getParameter("roleName");
        //true存在重复，false不存在重复
        boolean isHas = roleService.isHasRoleByName(roleName,_id);
        ret = !isHas;
        return ret;
    }

    /**
     * 更新角色权限
     * @author anss
     * @date 2018-09-14
     * @return
     */
    @RequestMapping(value = "/changeQX.do")
    @ResponseBody
    public Object changeQX() {
        boolean ret = false;
        String roleId = request.getParameter("roleId");
        String qxName = request.getParameter("qxName");
        String qxValue = request.getParameter("qxValue");

        if (!StringUtils.isEmptyString(roleId) && !StringUtils.isEmptyString(qxName)
                && !StringUtils.isEmptyString(qxValue)) {
            ret = roleService.updateRoleQX(roleId, qxName, qxValue);
            if (ret) {
                Role role = roleService.selectById(Long.parseLong(roleId));
                operationLogService.logInsert("修改角色[" + role.getRoleName() + "]的权限成功！", LogActionConstant.USER_EDIT, this.getRequest());
            }
        }
        return ret;
    }

    /**
     * 权限
     * @author anss
     * @date 2018-09-14
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject();  //shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
    }

}
