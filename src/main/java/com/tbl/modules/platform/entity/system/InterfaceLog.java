package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口日志实体
 *
 * @author anss
 * @date 2018-09-16
 */
@Getter
@Setter
@TableName("interface_log")
public class InterfaceLog implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;
    /**
     * 接口名称
     * */
    private String interfacename;
    /**
     * 请求时间
     * */
    private String requesttime;
    /**
     * 请求参数
     * */
    private String parameter;
    /**
     * 请求结果
     * */
    private String result;
    /**
     * 报错内容
     * */
    private String errorinfo;
}
