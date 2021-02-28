package com.tbl.modules.platform.entity.system;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@TableName("sys_operation_log")
@Getter
@Setter
public class OperationLog implements Serializable {

    @TableId("id")
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 操作时间
     */
    private Long operationTime;

    /**
     * 操作时间
     * 用于Long转String类型存储
     */
    @TableField(exist = false)
    private String operationT;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 用户动作
     */
    private Long useraction;

    /**
     * 操作用户IP
     */
    private String userip;

    /**
     * 操作用户MAC
     */
    private String usermac;

    /**
     * 操作用户名
     */
    @TableField(exist = false)
    private String username;

    private String jsonstr;

}
