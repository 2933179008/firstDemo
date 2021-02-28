package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 列实体
 *
 * @author pz
 * @date 2019-01-02
 */
@Getter
@Setter
@TableName("base_customer")
public class Customer implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户类别
     */
    private String customerType;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 逻辑删除字段（0删除，1存在）
     */
    private String deletedFlag;

    /**
     * 删除人
     */
    private Long deletedBy;

}
