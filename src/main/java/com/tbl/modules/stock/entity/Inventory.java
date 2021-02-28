package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 盘点实体类
 *
 * @author pz
 * @date 2019-01-08
 */
@Getter
@Setter
@TableName("inventory")
public class Inventory implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 盘点任务id
     */
    private Long  inventoryTaskId;

    /**
     * 盘点任务编号
     */
    @TableField(exist = false)
    private String inventoryTaskCode;

    /**
     * 库位编号
     */
    private String positionCode;

    /**
     * 盘点编码
     */
    private String inventoryCode;

    /**
     * 盘点人id
     */
    private Long  inventoryUserId;

    /**
     * 盘点人名称
     */
    @TableField(exist = false)
    private String inventoryName;

    /**
     * 盘点时间
     */
    private String inventoryTime;

    /**
     * 状态（0：未提交；1：已提交）
     */
    private String state;

    /**
     * 备注
     */
    private String  remark;

    /**
     * 创建时间
     */
    private String  createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createName;

    @TableField(exist = false)
    private String userName;

}
