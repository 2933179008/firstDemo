package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 盘点详细实体类
 *
 * @author pz
 * @date 2019-01-09
 */
@Getter
@Setter
@TableName("inventory_detail")
public class InventoryDetail implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 盘点id
     */
    private Long inventoryId;

    /**
     * 盘点详情id
     */
    private Long  inventoryTaskDetailId;

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
    private String  materialName;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 盘点数量
     */
    private String inventoryAmount;

    /**
     * 盘点重量
     */
    private String  inventoryWeight;

    /**
     * 状态（0：未处理；1：处理中；2：已完成）
     */
    private String state;

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

    @TableField(exist = false)
    private String barcode;

}
