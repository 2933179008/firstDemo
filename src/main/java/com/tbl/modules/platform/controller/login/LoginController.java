package com.tbl.modules.platform.controller.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录（总入口）
 *
 * @author anss
 * @date 2018-09-10
 */
@RestController
public class LoginController {

    /**
     * 访问登录页
     * @author anss
     * @date 2018-09-11
     * @return
     */
    @RequestMapping(value="/")
    public String login(){
        return "你好";
    }

}
