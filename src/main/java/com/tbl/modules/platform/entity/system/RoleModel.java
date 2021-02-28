package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色模块实体
 *
 * @author anss
 * @date 2018-09-15
 */
@Getter
@Setter
@TableName("sys_rolemodel")
public class RoleModel implements Serializable {

    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long modelId;
}
