package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 盘点任务实体类
 *
 * @author pz
 * @date 2019-01-14
 */
@Getter
@Setter
@TableName("inventory_task")
public class InventoryTask implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 盘点任务编码
     */
    private String inventoryTaskCode;

    /**
     * 盘点类型（0：库位盘点；1：动碰盘点）
     */
    private String inventoryType;

    /**
     * 盘点库位编码
     */
    private String positionCode;

    /**
     * 盘点人id
     */
    private int inventoryUserId;

    /**
     * 盘点人名称
     */
    @TableField(exist = false)
    private String inventoryTaskName;

    /**
     * 盘点时间
     */
    private String inventoryTime;

    /**
     * 完成时间
     */
    private String completeTime;

    /**
     * 状态（0：未提交；1：待盘点；2：盘点中；3：待审核；4：已审核；5：复盘中）
     */
    private String state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private Long createBy;

    @TableField(exist = false)
    private String userName;

}
