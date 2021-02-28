package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户 model类
 *
 * @author jzy
 * @date
 */
@Getter
@Setter
public class UserModel implements Serializable {


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 姓名拼音
     */
    private String pinyin;

    /**
     * 性别
     */
    private Long gender;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色对象
     */
    private Role role;

    /**
     * 公司主键
     */
    private Long enterpriseID;

    /**
     * 公司名称
     */
    private String enterpriseName;

    /**
     * 最后登录时间
     */
    private String lastLogin;

    /**
     * 用户IP
     */
    private String ip;

    /**
     * 用户状态1：启用，2：禁用
     */
    private Long status = 1L;

    /**
     * 邮箱
     */
    private String email;

    /**
     *
     */
    private String number;

    /**
     * 移动电话
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 每页显示条数
     */
    private Long pagesize;

    /**
     *
     */
    private Long isalert;

    /**
     * 完成时间
     */
    private Long finishtime;

    //添加权限
    private Long addQx;
    //编辑权限
    private Long editQx;
    //删除权限
    private Long deleteQx;
    //查询权限
    private Long queryQx;
    private String rolename;

    private String deptname;

    private String StateName;

    private String text;




}
