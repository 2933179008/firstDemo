package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色实体
 *
 * @author anss
 * @date 2018-09-11
 */
@Getter
@Setter
@TableName("sys_role")
public class Role implements Serializable {

    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 增加权限1：开，0：关闭
     */
    private Long addQx=0L;

    /**
     * 修改权限1：开，0：关闭
     */
    private Long delQx=0L;

    /**
     * 删除权限1：开，0：关闭
     */
    private Long editQx=0L;

    /**
     * 查询权限1：开，0：关闭
     */
    private Long chaQx=1L;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String text;

    @TableField(exist = false)
    private String name;
}
