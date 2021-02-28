package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单实体
 *
 * @author anss
 * @date 2018-09-11
 */
@Getter
@Setter
@TableName("sys_menu")
public class Menu implements Serializable {

    /**
     * 菜单ID
     */
    @TableId(value = "menu_id")
    private Long menuId;

    private String menuName;

    private String menuUrl;

    private Long parentId;


    @TableField(exist=false)
    private String pId;

    private Long menuOrder;


    private String menuIcon;

    /**
     * 菜单类型
     */
    private String menuType;

    private String remark;

    @TableField(exist=false)
    private Menu parentMenu;

    @TableField(exist=false)
    private String applicationId;

    @TableField(exist=false)
    private String applicationName;

    @TableField(exist=false)
    private String permissionId;

    @TableField(exist=false)
    private String disable;

    @TableField(exist=false)
    private List<Menu> subMenu;

    @TableField(exist=false)
    private List<Menu> funMenu;

    @TableField(exist=false)
    private String funName;

    private String vip;

    @TableField(exist=false)
    private boolean hasMenu= false ;

    @TableField(exist=false)
    private boolean hasChecked= false;
}
