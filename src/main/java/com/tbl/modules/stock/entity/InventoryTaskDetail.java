package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 盘点任务详情实体类
 *
 * @author pz
 * @date 2019-01-14
 */
@Getter
@Setter
@TableName("inventory_task_detail")
public class InventoryTaskDetail implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 盘点任务id
     */
    private  Long inventoryTaskId;

    /**
     * 库位编码
     */
    private String positionCode;

    /**
     * 物料编码
     */
    private String  materialCode;

    /**
     * 物料名称
     */
    private String   materialName;

    /**
     * 批次号
     */
    private String  batchNo;

    /**
     * 库存数量
     */
    private String  stockAmount;

    /**
     * 盘点数量
     */
    private String  inventoryAmount;

    /**
     * 盘点重量
     */
    private String  inventoryWeight;


    /**
     * 状态（0：未盘点；1：已盘点；2:已审核；3：复盘中）
     */
    private String state;

    /**
     * 盘盈盘亏（空：未盘；0：盘亏；1：盘盈；2：盘平）
     */
    @TableField(exist = false)
    private String inventoryState;

    /**
     * 完成时间
     */
    private String  completeTime;

    /**
     * 创建时间
     */
    private String  createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * rfid
     */
    private String rfid;

    /**
     * 保质期至
     */
    @TableField(exist = false)
    private String qualityDate;

    /**
     * 保质期（天）
     */
    @TableField(exist = false)
    private String qualityPeriod;

}
