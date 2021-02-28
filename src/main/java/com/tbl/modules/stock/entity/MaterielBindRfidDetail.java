package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 物料绑定RFID详情管理实体类
 *
 * @author yuany
 * @date 2019-01-11
 */
@Getter
@Setter
@TableName("materiel_bind_rfid_detail")
public class MaterielBindRfidDetail implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 物料绑定RFID ID
     */
    private Long materielBindRfidBy;

    /**
     * 物料编码
     */
    private String materielCode;

    /**
     * 物料名称
     */
    private String materielName;

    /**
     * 物料条码
     */
    @TableField(exist = false)
    private String barcode;

    /**
     * 批次规则
     */
    private String batchRule;

    /**
     * 包装单位
     */
    private String unit;

    /**
     * 数量
     */
    private String amount;

    /**
     * 重量
     */
    private String weight;

    /**
     * 托盘管理ID
     */
    private Long trayBy;

    /**
     * 合并或拆分的数量
     */
    private String number;

    /**
     * 合并或拆分重量
     */
    private String mlWeight;

    /**
     * 逻辑删除（0已删除，1为删除）
     */
    private String deleteFlag;

    /**
     * 删除人ID
     */
    private Long deleteBy;

    /**
     * RFID
     */
    private String rfid;

    /**
     * 库位ID
     */
    private Long positionId;

    /**
     * 库位编号
     */
    @TableField(exist = false)
    private String positionCode;

    /**
     * 库位名称
     */
    @TableField(exist = false)
    private String positionName;

    /**
     * 状态 （0.未入库  1.已入库）
     */
    private String status;

    /**
     * 冻结状态（0.冻结 1.解冻）
     */
    private String frozen;

    /**
     * 冻结操作人
     */
    private Long frozenUserBy;

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

    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private String supplierName;

    //库位冻结状态
    @TableField(exist = false)
    private String positionforzen;

    /**
     * 生产日期
     */
    private String productData;

    /**
     * 排序
     */
    @TableField(exist = false)
    private Integer sort;

    @TableField(exist = false)
    private  String documentType;

}
