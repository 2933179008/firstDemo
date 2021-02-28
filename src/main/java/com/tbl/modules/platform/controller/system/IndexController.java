package com.tbl.modules.platform.controller.system;

import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页处理controller
 *
 * @author anss
 * @date 2018-10-07
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController extends AbstractController {

    //用户接口
    @Autowired
    private UserService userService;
    @Value("${read.adapteRfid}")
    private String StrAdapteRfid;


    /**
     * 主页面跳转Controller
     * @author anss
     * @date 2018-10-07
     * @return
     */
    @RequestMapping(value = "/main")
    public ModelAndView main() {
        ModelAndView mv = this.getModelAndView();
        mv.addObject("adapteRfid", StrAdapteRfid);
        mv.setViewName("/main/home");
        mv.addObject("authentic", userService.getDeviceOrDataAutor(getSessionUser().getRoleId()));
        return mv;
    }

    /**
     * 主页跳转到 pwd.jsp
     * @return
     */
    @RequestMapping(value = "/pwd")
    public ModelAndView pwd() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/techbloom/platform/main/pwd");
        return mv;
    }

    /**
     * 主页跳转到 userInfo.jsp
     * @return
     */
    @RequestMapping(value = "/userInfo")
    public ModelAndView userInfo() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/techbloom/platform/main/userInfo");
        return mv;
    }

    /**
     * 主页跳转到 productCode.jsp
     * @return
     */
    @RequestMapping(value = "/productCode")
    public ModelAndView productCode() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("/techbloom/platform/main/productCode");
        return mv;
    }

}
