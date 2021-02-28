package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库存变动实体类
 *
 * @author pz
 * @date 2019-01-08
 */
@Getter
@Setter
@TableName("stock_change")
public class StockChange implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 变动单据
     */
    private String changeCode;

    /**
     * 物料编码
     */
    private String  materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 业务类型（0：入库；1：出库；2：移位；3：报溢入库；4：报损出库）
     */
    private String  businessType;

    /**
     * 收入数量
     */
    private String inAmount;

    /**
     * 库位
     */
    private Long positionBy;

    /**
     * 库位名称
     */
    @TableField(exist = false)
    private String positionName;

    /**
     * 发出数量
     */
    private String outAmount;
    /**
     * 收入重量
     */
    private String inWeight;
    /**
     * 发出重量
     */
    private String outWeight;
    /**
     * 结余重量
     */
    private String balanceWeight;

    /**
     * 结余数量
     */
    private String balanceAmount;


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

}
