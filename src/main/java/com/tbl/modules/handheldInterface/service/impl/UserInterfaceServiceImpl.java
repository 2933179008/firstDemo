package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.handheldInterface.dao.UserInterfaceDAO;
import com.tbl.modules.handheldInterface.service.UserInterfaceService;
import com.tbl.modules.platform.entity.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Service实现
 */
@Service("userInterfaceService")
public class UserInterfaceServiceImpl extends ServiceImpl<UserInterfaceDAO, User> implements UserInterfaceService {

    /**
     * 用户接口DAO
     */
    @Autowired
    private UserInterfaceDAO userInterfaceDAO;

    /**
     * 登录判断
     * @author anss
     * @date 2018-09-11
     * @param username
     * @param pwd
     * @return User
     */
    @Override
    public User getUserByNameAndPwd(String username,String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("pwd", pwd);

        return userInterfaceDAO.getUserByNameAndPwd(map);
    }
}
