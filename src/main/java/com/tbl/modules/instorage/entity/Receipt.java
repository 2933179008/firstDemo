package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 收货单实体类
 * @author: zj
 * @create: 2019-01-07 13:19
 **/
@Getter
@Setter
@TableName("receipt_plan")
public class Receipt implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //收货单编号
    private String receiptCode;
    //客户code（货主code）
    private String customerCode;
    //客户名称（货主名称（冗余））
    private String customerName;
    //供应商编号
    private String supplierCode;
    //供应商名称（冗余）
    private String supplierName;
    //库区编码（多个之间用逗号“,”隔开）
    private String areaCode;
    //库区名称（冗余，多个之间用逗号“,”隔开）
    private String areaName;
    //预计收货时间
    private String estimatedDeliveryTime;
    //单据类型（0：自采；1：客供）
    private String documentType;
    //备注
    private String remark;
    //状态（0：未提交；1：待收货；2：收获中；3：已完成；4：强制完成；5：超收）
    private String state;
    //收货开始时间
    private String receiptStartTime;
    //收货完成时间
    private String receiptEndTime;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private Long createBy;
    //ERP采购订单编号
    private String erpReceiptCode;

    //创建人姓名
    @TableField(exist = false)
    private String createName;
    //状态转译
    @TableField(exist = false)
    private String stateContent;
    //单据类型转译
    @TableField(exist = false)
    private String documentTypeContent;
}
    