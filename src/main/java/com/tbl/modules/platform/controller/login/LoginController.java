package com.tbl.modules.platform.controller.login;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.tbl.common.utils.*;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.constant.LogActionConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.Menu;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.OperationLogService;
import com.tbl.modules.platform.service.system.MenuService;
import com.tbl.modules.platform.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录（总入口）
 *
 * @author anss
 * @date 2018-09-10
 */
@Controller
@RequestMapping("/login")
public class LoginController extends AbstractController {

    //菜单service
    @Autowired
    private MenuService menuService;
    //用户service
    @Autowired
    private UserService userService;
    //日志service
    @Autowired
    private OperationLogService operationLogService;

    // 记录错误代码
    protected String errcode = "undefined";

    @Value("${read.adapteRfid}")
    private String adapteRfid;



    /**
     * 访问登录页
     * @author anss
     * @date 2018-09-11
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/login_toLogin")
    public ModelAndView toLogin(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/platform/login/to_login");
        return mv;
    }

    /**
     * 跳转到登录页面
     * @author anss
     * @date 2018-09-11
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public ModelAndView _login() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/platform/login/logins");
        mv.addObject("err", errcode);
        return mv;
    }

    /**
     * @author anss
     * @date 2018-09-11
     * @param filePath
     * @return
     */
    public List<String> readTxtFile(String filePath) {
        List<String> lineTxt = Lists.newArrayList();
        try {
            filePath = filePath.replace("file:", "");
            File file = new File(filePath);
            if (file.isFile()) { // 判断文件是否存在
                lineTxt = Files.readLines(file, Charsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineTxt;
    }
    /**
     * 跳转设备Licence设置界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/devicelicence")
    public ModelAndView todevicelicence() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/devicelicence");
        return mv;
    }
    /**
     * 跳转apiLicence设置界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/apilicence")
    public ModelAndView toapilicence() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/apilicence");
        return mv;
    }

    /**
     * 请求登录，验证用户
     * @param request
     * @return Map
     */
    @RequestMapping(value = "/aysncLogCheck")
    @ResponseBody
    public Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> m = Maps.newHashMap();
        PageData pd = this.getPageData();
        String errInfo = "";

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        String USERNAME = pd.getString("loginname");
        String PASSWORD = pd.getString("password");
        String passwd = new SimpleHash("SHA-1", PASSWORD, USERNAME).toString(); // 密码加密
        pd.put("USERNAME", USERNAME);
        pd.put("PASSWORD", passwd);

        User user = null;
        //根据不同的用户名查找用户属于系统用户还是供应商用户
        Integer count = userService.getByUserName(USERNAME);
        if(count > 0){
            try {
                user = userService.getUserByNameAndPwd(USERNAME, passwd);
                if (user != null) {
                    if (user.getStatus() == 2) {
                        errInfo = "102"; // 账号禁用
                    } else {
                        pd.put("USER_ID", user.getUserId());
                        pd.put("LAST_LOGIN", DateUtils.getTime().toString());
                        userService.updateLastLogin(pd);

                        session.setAttribute(Const.SESSION_USER, user);
                        // shiro加入身份验证
                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD);
                        try {
                            subject.login(token);
                            operationLogService.logInsert("用户登录成功！", LogActionConstant.LOG_IN, request);
                        } catch (AuthenticationException e) {
                            errInfo = "104";
                        }
                    }
                }else{
                    errInfo = "101";
                    operationLogService.logInsert("用户登录失败！", LogActionConstant.LOG_IN, request);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if(user == null){
            errInfo = "101";
            operationLogService.logInsert("用户登录失败！", LogActionConstant.LOG_IN, request);
        }
        if(StringUtils.isEmptyString(adapteRfid)){
            errInfo = "107";
            operationLogService.logInsert("用户登录失败！没有配置adapteRfid参数", LogActionConstant.LOG_IN, request);
        }
        String _url = "";
        if (StringUtils.isEmptyString(errInfo)) {
            _url = "/login/main/index.do";
            errcode = "undefined";
        } else {
            _url = "/login/login.do";
            errcode = errInfo;
        }
        m.put("errcode", errInfo);
        m.put("url", _url);
        return m;
    }

    /**
     * 访问系统首页
     * @return ModelAndView
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/main/index.do")
    public ModelAndView login_index() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        if (user != null) {
            User userr = (User) session.getAttribute(Const.SESSION_USERROL);
            if (null == userr) {
                String username = user.getUsername();
                Integer count = userService.getByUserName(username);
                if(count >0) {
                    user = userService.getUserAndRoleById(user.getUserId());
                }
                session.setAttribute(Const.SESSION_USERROL, user);
            } else {
                user = userr;
            }
            // 放入用户名
            session.setAttribute(Const.SESSION_USERNAME, user.getUsername());
            List<Menu> allmenuList = new ArrayList<Menu>();

            if (null == session.getAttribute(Const.SESSION_allmenuList)) {
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("role_id", user.getRoleId());
                allmenuList = menuService.listAllMenuByRoleId(menuMap);
                // 菜单权限放入session中
                session.setAttribute(Const.SESSION_allmenuList, allmenuList);
            } else {
                allmenuList = (List<Menu>) session.getAttribute(Const.SESSION_allmenuList);
            }

            if (null == session.getAttribute(Const.SESSION_QX)) {
                // 按钮权限放到session中
                session.setAttribute(Const.SESSION_QX, this.getUQX(session, user));
            }
            // 首页面上“更多”按钮的权限
            boolean AT = userService.getDataManage(user.getUserId());
            // 提示框上“更多”按钮的权限
            boolean at = userService.getDeviceT(user.getUserId());
            mv.setViewName("/techbloom/platform/main/index");
            session.setAttribute("AT", AT);
            session.setAttribute("at", at);
            mv.addObject("user", user);
            mv.addObject("menuList", allmenuList);
        } else {
            // session失效后跳转登录页面
            mv.setViewName("/techbloom/platform/login/logins");
        }

        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        String sortName = page.getSortname();
        if( StringUtils.isEmptyString(sortName) )
        {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if(StringUtils.isEmptyString(sortOrder))
        {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", "id");
        executePageMap(map,page);

        return mv;
    }

    /**
     * 获取用户权限
     * @param session
     * @param user
     * @return Map
     */
    public Map<String, Long> getUQX(Session session, User user) {
        PageData pd = new PageData();
        Map<String, Long> map = new HashMap<String, Long>();
        try {
            String USERNAME = session.getAttribute(Const.SESSION_USERNAME).toString();
            pd.put(Const.SESSION_USERNAME, USERNAME);
            Integer count = userService.getByUserName(USERNAME);
            long ROLE_ID = 0;
            if(count >0) {
                ROLE_ID = userService.selectById(user.getUserId()).getRoleId();
            }
            pd.put("ROLE_ID", ROLE_ID);

            PageData pd2 = new PageData();
            pd2.put(Const.SESSION_USERNAME, USERNAME);
            pd2.put("ROLE_ID", ROLE_ID);

            this.getRemortIP(USERNAME);
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return map;
    }

    /**
     * 获取登录用户的IP
     * @author anss
     * @date 2018-09-11
     * @param
     */
    public void getRemortIP(String USERNAME) {
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", USERNAME);
        if (userService.selectByMap(map) != null) {
            User user = userService.selectByMap(map).get(0);
            user.setIp(ip);
            userService.updateById(user);
        }
    }

    @RequestMapping(value = "/getMenu")
    @ResponseBody
    public List<Menu> getMenu() {
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        User userr = (User) session.getAttribute(Const.SESSION_USERROL);
        if (null == userr) {
            user = userService.getUserAndRoleById(user.getUserId());
        } else {
            user = userr;
        }
        List<Menu> allmenuList = new ArrayList<Menu>();
        if (null == session.getAttribute(Const.SESSION_allmenuList)) {
            Map<String, Object> menuMap = new HashMap<>();
            menuMap.put("role_id", user.getRoleId());
            allmenuList = menuService.listAllMenuByRoleId(menuMap);
        } else {
            allmenuList = (List<Menu>) session.getAttribute(Const.SESSION_allmenuList);
        }
        //更新用户快捷菜单或者 在保存快捷菜单时就更新下session-quickmenuList
        List<Menu> quickmenuList = new ArrayList<Menu>();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        quickmenuList = menuService.listquickMenu(map);

        session.setAttribute("quickmenuList", quickmenuList);
        //更新用户快捷菜单 - end
        return allmenuList;
    }

    /**
     * 用户注销
     * @return ModelAndView
     */
    @RequestMapping(value = "/logout.do")
    @ResponseBody
    public ModelAndView logout() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        operationLogService.logInsert("用户退出！", LogActionConstant.LOG_OUT, request);
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.removeAttribute(Const.SESSION_USER);
        session.removeAttribute(Const.SESSION_ROLE_RIGHTS);
        session.removeAttribute(Const.SESSION_allmenuList);
        session.removeAttribute(Const.SESSION_menuList);
        session.removeAttribute(Const.SESSION_QX);
        session.removeAttribute(Const.SESSION_USERNAME);
        session.removeAttribute(Const.SESSION_USERROL);
        session.removeAttribute("changeMenu");
        // shiro销毁登录
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        pd = this.getPageData();
        String msg = pd.getString("msg");
        pd.put("msg", msg);
//        pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); // 读取系统名称
        mv.addObject("notes",readTxtFile(Thread.currentThread().getContextClassLoader().getResource("") + "releasenote.txt"));
        mv.setViewName("/techbloom/platform/login/logins");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 首页上获取对应的入库单，出库单，异常单据的数据
     * @return Map
     */
    @RequestMapping(value="/getAllDate")
    @ResponseBody
    public Map<String,Object> getAllDate(){
        Map<String,Object> map = Maps.newHashMap();
        //获取对应的入库单的数量
        Integer inAmount = userService.getInAmount();
        map.put("inAmount", inAmount);
        //获取对应的下架单的数量
        Integer outAmount = userService.getOutAmount();
        map.put("outAmount", outAmount);
        //获取异常的数量
        Integer abnormalAmount = userService.getAbnormalAmount();
        map.put("abnormalAmount", abnormalAmount);
        return map;
    }

    /**
     * 首页上获取快捷菜单
     * @return Map
     */
    @RequestMapping(value = "/getQuickMenu")
    @ResponseBody
    public Map<String,Object> getQuickMenu() {
        Map<String,Object> map = Maps.newHashMap();
        Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        User userr = (User) session.getAttribute(Const.SESSION_USERROL);
        if (null == userr) {
            user = userService.getUserAndRoleById(user.getUserId());
        } else {
            user = userr;
        }
        List<Menu> quickmenuList = new ArrayList<Menu>();
        map.put("userId",user.getUserId());
        quickmenuList = menuService.listquickMenu(map);
        session.setAttribute("quickmenuList", quickmenuList);
        map.put("quickmenuList", quickmenuList);
        map.put("url", "/techbloom/platform/main/boxs.jsp");
        return map;
    }

}
