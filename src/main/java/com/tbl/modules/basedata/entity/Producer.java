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
 * @date 2019-01-11
 */
@Getter
@Setter
@TableName("base_producer")
public class Producer implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 生产产商编号
     */
    private String producerCode;

    /**
     * 生产产商名称
     */
    private String  producerName;

    /**
     * 生产产商类别
     */
    private String producerType;

    /**
     * 联系人
     */
    private String  linkman;

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
