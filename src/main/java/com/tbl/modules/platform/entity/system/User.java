package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户实体
 *
 * @author anss
 * @date 2018-09-10
 */
@Getter
@Setter
@TableName("sys_user")
public class User implements Serializable {


    /**
     * 用户ID
     */
    @Getter
    @Setter
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Getter
    @Setter
    private String username;

    /**
     * 密码
     */
    @Getter
    @Setter
    private String password;

    /**
     * 姓名
     */
    @Getter
    @Setter
    private String name;

    /**
     * 姓名拼音
     */
    @Getter
    @Setter
    private String pinyin;

    /**
     * 性别
     */
    @Getter
    @Setter
    private Long gender;

    /**
     * 角色ID
     */
    @Getter
    @Setter
    private Long roleId;

    /**
     * 角色对象
     */
    @TableField(exist = false)
    private Role role;

    /**
     * 公司主键
     */
    @TableField(exist = false)
    private Long enterpriseID;

    /**
     * 公司名称
     */
    @TableField(exist = false)
    private String enterpriseName;

    /**
     * 最后登录时间
     */
    private Long lastLogin;

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
    @TableField(exist=false)
    private Long addQx;
    //编辑权限
    @TableField(exist=false)
    private Long editQx;
    //删除权限
    @TableField(exist=false)
    private Long deleteQx;
    //查询权限
    @TableField(exist=false)
    private Long queryQx;

    @TableField(exist=false)
    private Long productQX;

    @TableField(exist=false)
    private Long materialQX;

    @TableField(exist=false)
    private Long qualityQX;


    @TableField(exist = false)
    private String rolename;

    @TableField(exist = false)
    private String deptname;

    @TableField(exist = false)
    private String StateName;

    @TableField(exist = false)
    private String text;




}
