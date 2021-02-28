package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.platform.entity.system.User;

/**
 * 用户Service
 */
public interface UserInterfaceService extends IService<User> {

    /**
     * 登录判断
     * @author anss
     * @date 2018-09-11
     * @param username
     * @param pwd
     * @return User
     */
    User getUserByNameAndPwd(String username,String pwd);
}
