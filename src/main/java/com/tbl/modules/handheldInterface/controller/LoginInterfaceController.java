package com.tbl.modules.handheldInterface.controller;

import com.tbl.common.utils.DateUtils;
import com.tbl.modules.handheldInterface.service.UserInterfaceService;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuany
 * @description 登陆调用接口
 * @date 2019-02-13
 */
@Controller
@RequestMapping(value = "/loginInterface")
public class LoginInterfaceController {

    /**
     * 用户service
     */
    @Autowired
    private UserInterfaceService userInterfaceService;

    /**
     * 接口日志service
     */
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 验证登陆
     *
     * @param username
     * @param password
     * @return
     * @author yuany
     * @date 2019-02-14
     */
    @RequestMapping(value = "/login/{username}/{password}")
    @ResponseBody
    public Map<String, Object> beatOutLight(@PathVariable String username, @PathVariable String password) {

        boolean result = true;
        Map<String, Object> map = new HashMap<>();
        String msg = "登陆成功！";
        String passwd = new SimpleHash("SHA-1", password, username).toString(); // 密码加密
        User user = userInterfaceService.getUserByNameAndPwd(username, passwd);

        String errorinfo = null;
        //判断user实体是否为空
        if (user == null) {
            result = false;
            msg = "失败原因：用户名或密码错误";
            errorinfo = DateUtils.getTime();
        } else {
            map.put("data", user.getUserId());
        }

        String interfacename = "登陆调用接口";
        String parameter = "UserName:"+username+"/"+"PassWord:"+passwd;

        interfaceLogService.interfaceLogInsert(interfacename,parameter,msg,errorinfo);

        map.put("msg", msg);
        map.put("result", result);

        return map;
    }
}
