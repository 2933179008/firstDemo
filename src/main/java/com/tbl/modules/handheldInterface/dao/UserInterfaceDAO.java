package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.platform.entity.system.User;

import java.util.Map;

/**
 * 用户接口DAO
 */
public interface UserInterfaceDAO extends BaseMapper<User> {

    /**
     * 登录判断
     * @param map
     * @return User
     */
    User getUserByNameAndPwd(Map<String, Object> map);
}
