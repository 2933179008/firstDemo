package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户菜单实体
 *
 * @author anss
 * @date 2018-09-11
 */
@Getter
@Setter
@TableName("sys_usermenu")
public class UserMenu implements Serializable {


    /**
     * 角色ID
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 菜单ID
     */
    private Long menuId;
    private Long order;
    private Long type;
    private Long isquick;


}
