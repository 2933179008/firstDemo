package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 物料管理实体类
 *
 * @author yuany
 * @date 2019-01-02
 */
@Getter
@Setter
@TableName("base_materiel")
public class Materiel implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

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
    private String barcode;

    /**
     * 客户id
     */
    private Long customerBy;

    /**
     * 客户名称
     */
    @TableField(exist = false)
    private String customerName;

    /**
     * 供应商id
     */
    private Long supplierBy;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 生产厂家id
     */
    private Long producerBy;

    /**
     * 生产厂家名称
     */
    @TableField(exist = false)
    private String producerName;

    /**
     * 保质期(天)
     */
    private String qualityPeriod;

    /**
     * 长
     */
    private String length;

    /**
     * 宽
     */
    private String wide;

    /**
     * 高
     */
    private String height;

    /**
     * 体积
     */
    private String volume;

    /**
     * 包装单位
     */
    private String unit;

    /**
     * 物料规格（单位关联重量）
     */
    private String spec;

    /**
     * 上架分类
     */
    private String upshelfClassify;

    /**
     * 拣货分类
     */
    private String pickClassify;

    /**
     * 批次规则
     */
    private String batchRule;


    /**
     * 数据创建时间
     */
    private String createTime;

    /**
     * 数据更新时间
     */
    private String updateTime;

    /**
     * 逻辑删除标记字段（0表示删除，1表示存在）
     */
    private String deletedFlag;

    /**
     * 删除人ID
     */
    private Long deletedBy;

    /**
     * 托盘容量
     */
    private String trayWeight;

    /**
     * 助记码
     */
    private String mnemonicCode;


}
