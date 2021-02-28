package com.tbl.modules.platform.controller.system;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageData;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.constant.LogActionConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.Menu;
import com.tbl.modules.platform.entity.system.Role;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.entity.system.UserModel;
import com.tbl.modules.platform.service.system.OperationLogService;
import com.tbl.modules.platform.service.system.MenuService;
import com.tbl.modules.platform.service.system.RoleService;
import com.tbl.modules.platform.service.system.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户controller
 *
 * @author anss
 * @date 2018-09-10
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractController {

    //用户service
    @Autowired
    private UserService userService;
    //日志service
    @Autowired
    private OperationLogService operationLogService;
    //角色service
    @Autowired
    private RoleService roleService;
    //菜单service
    @Autowired
    private MenuService menuService;

    //存放在map中的两个值的key：结果，提示信息
    private String mapResultkey = "result";
    private String mapDatakey = "data";


    /*
     * 跳转到用户列表页面
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/toList.do")
    @ResponseBody
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/platform/system/user/user_list");
        return mv;
    }

    /**
     * 获取用户列表数据
     *
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Object listUsers(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "user_id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("suserid", getSessionUser().getUserId());
        PageUtils PageUser = userService.queryPage(map);
        page.setTotalRows(PageUser.getTotalCount() == 0 ? 1 : PageUser.getTotalCount());
        map.put("rows", PageUser.getList());
        executePageMap(map, page);
        return map;
    }

    @Override
    public User getSessionUser() {
        return super.getSessionUser();
    }

    /*
     * 判断用户类型
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/getUserMenu")
    @ResponseBody
    public List<Map<String, Object>> getUserMenu() {
        Long id = this.getSessionUser().getUserId();
        String userName = this.getSessionUser().getUsername();
        //判断用户是供应闪还是系统用户
        Integer count = userService.getByUserName(userName);
        List<Map<String, Object>> list = null;
        if (count > 0) {
            list = userService.getUserMenu(id, "0");
        } else {
            list = userService.getUserMenu(id, "1");
        }
        return list;
    }

    /*
     * 判断用户类型
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/getUserMenu11")
    @ResponseBody
    public List<Map<String, Object>> getUserMenu11() {
        Long id = this.getSessionUser().getUserId();
        String userName = this.getSessionUser().getUsername();
        //判断用户是供应闪还是系统用户
        Integer count = userService.getByUserName(userName);
        List<Map<String, Object>> list = null;
        if (count > 0) {
            list = userService.getUserMenu11(id, "0");
        } else {
            list = userService.getUserMenu11(id, "1");
        }
        return list;
    }

    /**
     * 保存自定义导航菜单
     *
     * @param menus
     * @return
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/setUserMenu")
    @ResponseBody
    public boolean setUserMenu(String menus) {
        return userService.setUserMenu(menus, getSessionUser().getUserId());
    }

    /**
     * 设置快捷菜单
     *
     * @return
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/setUserQuickMenu")
    @ResponseBody
    public boolean setUserQuickMenu(String menus) {
        String name = this.getSessionUser().getUsername();
        Long id = this.getSessionUser().getUserId();
        //判断用户是供应商还是系统用户
        Integer count = userService.getByUserName(name);
        if (count > 0) {
            return userService.setUserQuickMenu(menus, id);
        } else {
            return userService.setUserQuickMenus(menus, id);
        }
    }

    /**
     * 返回到用户添加页面
     *
     * @return
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/user_add")
    public String userAdd() {
        return "techbloom/platform/system/user/user_add";
    }

    /**
     * 保存/修改用户
     *
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Map<String, Object> saveU(User user) {
        Map<String, Object> map = Maps.newHashMap();
        boolean result = false;
        if (user != null) {
            if (user.getUserId() == null) {
                //新增：密码加密
                user.setPassword(new SimpleHash("SHA-1", user.getPassword(), user.getUsername()).toString());
            }
        }

        result = userService.saveU(user);
        if (result) {

            if (user.getRoleId() != null) {

                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("role_id", user.getRoleId());
                List<Menu> lstAllmenu = menuService.listAllMenuByRoleId(menuMap);
                String menuIds = "";
                for (Menu menu : lstAllmenu) {
                    menuIds += menu.getMenuId() + ",";
                }
                if (StringUtils.isNotBlank(menuIds)) {
                    menuIds = menuIds.substring(0, menuIds.length() - 1);
                }
                userService.setUserMenu(menuIds, user.getUserId());
            }

            if (user.getUserId() != null) {
                //修改
                operationLogService.logInsert("修改用户[" + user.getUsername() + "]成功！", LogActionConstant.USER_EDIT, this.getRequest());
            } else {
                //添加
                operationLogService.logInsert("添加用户[" + user.getUsername() + "]成功！", LogActionConstant.USER_ADD, this.getRequest());
            }
        }

        map.put("result", result);
        return map;
    }

    /**
     * 用户设置：修改用户信息
     */
    @RequestMapping(value = "/updateUserInfo")
    @ResponseBody
    public boolean updateUser(User user) {
        boolean isSuccess = false;
        Long userID = this.getSessionUser().getUserId();
        if (user != null) {
            user.setUserId(userID);
            isSuccess = userService.saveU(user);
            if (isSuccess) {
                operationLogService.logInsert("修改个人资料成功！", LogActionConstant.USER_EDIT, this.getRequest());
            }
        }
        return isSuccess;
    }

    /**
     * 获取用户的角色和部门信息
     *
     * @param userId
     * @return
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/getSomeInfo")
    @ResponseBody
    public Map<String, Object> getUserRelship(Long userId) {
        Map<String, Object> map = Maps.newHashMap();
        if (userId != null) {
            User user = getUserById(userId);
            if (user != null) {
                Role role = roleService.selectById(user.getRoleId());
                map.put("role", role);
            }
        }
        return map;
    }

    /**
     * 返回到用户修改页面
     *
     * @return
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/user_edit")
    public String userEdit() {
        return "techbloom/platform/system/user/user_edit";
    }

    /**
     * 根据用户主键获取用户信息
     *
     * @param userId
     * @return User
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/getUser")
    @ResponseBody
    public User getUserById(long userId) {
        User user = null;
        user = userService.findByUserId(userId + "");
        return user;
    }

    /**
     * 判断用户名是否存在
     *
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/hasU.do")
    @ResponseBody
    public boolean hasU() {
        boolean errInfo = false;
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            if (userService.findByUId(pd) == null) {
                errInfo = true;
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return errInfo;
    }

    /**
     * 初始化用户密码，根据用户主键，初始化用户密码，即将用户密码修改为123456
     *
     * @param userID
     * @return boolean
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/initUserPass")
    @ResponseBody
    public boolean initUserPass(Long userID) {
        User user = userService.findByUserId(String.valueOf(userID));
        String initPass = new SimpleHash("SHA-1", "123456", user.getUsername()).toString();
        boolean isSuccess = userService.updateUserColumn(userID, "PASSWORD", initPass);
        if (isSuccess) {
            operationLogService.logInsert("初始化用户[" + user.getUsername() + "]的密码成功！", LogActionConstant.USER_EDIT, this.getRequest());
        }
        return isSuccess;
    }

    /**
     * 用户删除
     *
     * @param ids
     * @return Boolean
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/delUById")
    @ResponseBody
    public Boolean delUById(String[] ids) {
        boolean result = false;
        if (!StringUtils.isEmptyString(ids.toString())) {
            String delNames = userService.getUserNameByIds(StringUtils.join(ids, ",", true));
            String strIds = StringUtils.join(ids, ",", true);
            List<Long> lstId = Arrays.asList(strIds.replaceAll("'", "").split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            result = userService.deleteBatchIds(lstId);
            if (result) {
                operationLogService.logInsert("删除用户[" + delNames + "]成功！", LogActionConstant.USER_DELETE, request);
            }
        }
        return result;
    }

    /**
     * 停用、启用用户
     *
     * @return Object
     * @author anss
     * @date 2018-09-13
     */
    @RequestMapping(value = "/changeStat")
    @ResponseBody
    public Object changeStat() {
        PageData pd = this.getPageData();
        // 要修改后的状态
        String state = pd.getString("state");
        // 要修改状态的userid
        String ids = pd.getString("ids");

        return userService.changeState(state, ids);
    }

    /**
     * 导出Excel
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void artBomExcel() {
        String ids = request.getParameter("ids");//得到要导出的ids
        List<UserModel> list = userService.getAllLists(ids);
        userService.toExcel(response, "", list);
    }

    /**
     * 获取列表信息：下拉框使用
     *
     * @return
     * @author anss
     * @date 2018-09-17
     */
    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public Map<String, Object> getRoleList(String queryString, int pageSize, int pageNo) {
        Map<String, Object> map = new HashMap<>();

        if (!StringUtils.isEmpty(queryString)) {
            map = JSON.parseObject(queryString);
        }
        map.put("page", pageNo);
        map.put("limit", pageSize);

        PageUtils pageUser = userService.getUserPageList(map);

        map.put("result", userService.getLstMapUser(map));
        map.put("total", pageUser.getTotalCount());
        return map;
    }

    /**
     * 获取可以添加的用户(下拉框使用，模糊查询结果)
     *
     * @param queryString
     * @param pageNo
     * @param pageSize
     * @return Map
     */
    @RequestMapping(value = "/getUserSelect")
    @ResponseBody
    public Map<String, Object> getUserSelect(String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        int listSize = 0;
        listSize = userService.selectCount(new EntityWrapper<User>().like("username", queryString));
        PageTbl page = this.getPage();
        page.setPageno(pageNo);
        page.setPagesize(pageSize);
        Page<Map<String, Object>> pageMap = userService.getUserSelect(page, queryString);
        List<Map<String, Object>> ulist = pageMap.getRecords();
        map.put("result", ulist == null ? "" : ulist);
        map.put("total", listSize);
        return map;
    }

    /**
     * 用户设置：修改用户密码
     */
    @RequestMapping(value = "/updateUserPass")
    @ResponseBody
    public Map<String, Object> updateUserPass(String oldPass, String newPass) {
        String username = this.getSessionUser().getUsername();
        Map<String, Object> map = Maps.newHashMap();
        Integer count = userService.getByUserName(username);
        if (count > 0) {
            //先判断旧密码是否正确
            User user = userService.findByUsername(username);
            map = this.updateUserPass(this.getSessionUser().getUserId(), oldPass, newPass, user.getUsername());
        }
        return map;
    }

    /**
     * 修改用户密码
     *
     * @param userId  用户主键
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @param salt    加密使用的salt值（加密时要使用相同的salt，才能确保旧密码加密之后，与之前加密的密码相同）
     * @return Map
     */
    @RequestMapping(value = "/updatePassWordById")
    @ResponseBody
    public Map<String, Object> updateUserPass(Long userId, String oldPass, String newPass, String salt) {
        Map<String, Object> map = Maps.newHashMap();    //存放操作结果，和提示信息
        boolean isSuccess = false;
        if (userId != null) {
            System.out.println("-------------用户密码修改-------------");
            //如果要修改密码
            //先判断旧密码是否正确
            User oldUser = userService.findByUserId(String.valueOf(userId));
            //使用用户输入的旧密码加密得到加密后的密码
            //String simpleHash =new SimpleHash("SHA-1", str, salt).toString();
            if (oldUser != null) {
                String oldHashPass = new SimpleHash("SHA-1", oldPass, oldUser.getUsername()).toString();
                //判断加密的密码和之前的加密的密码是否相等：相等则表示密码正确，可以修改密码
                if (oldUser.getPassword().equalsIgnoreCase(oldHashPass)) {
                    //用户输入的旧密码正确，可以修改密码,使用用户输入的新密码
                    if (!StringUtils.isEmptyString(newPass)) {
                        String newHashPass = new SimpleHash("SHA-1", newPass, salt).toString();
                        if (newHashPass.equalsIgnoreCase(oldHashPass)) {
                            //与老密码相同，给出提示，不能与老密码相同
                            map.put(this.mapDatakey, "不能与旧密码相同！");
                        } else {
                            //可以修改密码
                            isSuccess = userService.updateUserColumn(userId, "PASSWORD", newHashPass);
                            if (isSuccess) {
                                map.put(this.mapDatakey, "密码修改成功！");
                            } else {
                                map.put(this.mapDatakey, "密码修改失败！");
                            }
                        }
                    }
                } else {
                    //密码不正确，不可以修改密码
                    map.put(this.mapDatakey, "密码错误！");
                }
            }

        }
        if (isSuccess) {
            operationLogService.logInsert("修改用户[" + salt + "]的密码成功！", LogActionConstant.USER_EDIT, this.getRequest());
        }
        map.put(this.mapResultkey, isSuccess);
        return map;
    }

}
