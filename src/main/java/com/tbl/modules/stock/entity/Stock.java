package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库存实体类
 *
 * @author pz
 * @date 2019-01-08
 */
@Getter
@Setter
@TableName("stock")
public class Stock implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 物料编码
     */
    private String  materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 货物类型(0：没有RFID;1:有RFID)
     */
    private String materialType;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 库位编码
     */
    private String positionCode;

    /**
     * 库位名称
     */
    private String positionName;

    /**
     * 库存数量
     */
    private String stockAmount;

    /**
     * 可用库存数量
     */
    private String availableStockAmount;

    /**
     * 库存重量
     */
    private String stockWeight;

    /**
     * 可用库存重量
     */
    private String availableStockWeight;

    /**
     * 托盘库存数量
     */
    private String stockRfidAmount;

    /**
     * 可用托盘库存数量
     */
    private String availableStockRfidAmount;

    /**
     * RFID
     */
    private String rfid;

    /**
     * 可用的RFID（多个rfid用逗号隔开）
     */
    private String availableRfid;

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

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 占用库存数量
     */
    private String occupyStockAmount;

    /**
     * 占用库存重量
     */
    private String occupyStockWeight;

    /**
     * 占用库存的出库单据
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String outstorageBillCode;

    /**
     * 是否货权转移过（0:未货权转移1：货权转移）
     */
    private String materielPower;

    /**
     * 货权转移的数据源（由那个物料转移过来的，传ID）
     */
    private Long materialSource;

    /**
     * 生产采购日期
     */
    private String productDate;

    /**
     * 保质期至
     */
    private String qualityDate;

    /**
     * 库位类型(0：地堆;1：货架;2：不良品;3：暂存;)
     */
    @TableField(exist = false)
    private String positionType;

    /**
     * 货主编码
     */
    private  String customerCode;
    @TableField(exist = false)
    private  String documentType;

    /**
     * 货主名称
     */
    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private String text;

    @TableField(exist = false)
    private String supplierCode;

    @TableField(exist = false)
    private String materielCode;

    @TableField(exist = false)
    private String materielName;

    @TableField(exist = false)
    private String batchRule;

    @TableField(exist = false)
    private String unit;

    @TableField(exist = false)
    private String amount;

    @TableField(exist = false)
    private String weight;

}
