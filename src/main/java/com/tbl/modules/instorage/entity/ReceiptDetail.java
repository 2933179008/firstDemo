package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 收货单详情实体类
 * @author: zj
 * @create: 2019-01-07 13:19
 **/
@Getter
@Setter
@TableName("receipt_plan_detail")
public class ReceiptDetail implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //收货计划id
    private Long receiptPlanId;
    //物料编码
    private String materialCode;
    //物料名称
    private String materialName;
    //包装单位（一、二级单位）
    private String unit;
    //计划收货数量
    private String planReceiptAmount;
    //可拆分数量
    private String separableAmount;
    //实际收货数量
    private String actualReceiptAmount;
    //计划收货重量
    private String planReceiptWeight;
    //可拆分重量
    private String separableWeight;
    //实际收货重量
    private String actualReceiptWeight;
    //行号
    private Long line;

    @TableField(exist = false)
    private String batchNo;

    @TableField(exist = false)
    private String productDate;

    @TableField(exist = false)
    private String qualityPeriod;
}
    