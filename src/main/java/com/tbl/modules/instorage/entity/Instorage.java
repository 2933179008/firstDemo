package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 入库单实体类
 * @author: zj
 * @create: 2019-01-15 14:26
 **/
@Getter
@Setter
@TableName("instorage_bill")
public class Instorage implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //入库单编号
    private String instorageBillCode;
    //入库类型（0：采购入库；1：委托加工入库；2：生产退货入库；3：其他入库；）
    private String instorageType;
    //入库流程（0：一般流程；1：白糖流程）
    private String instorageProcess;
    //是否越库（0：否；1：是）
    private String crossDocking;
    //收货计划id
    private Long receiptPlanId;
    //出库单id（“生产退货入库”类型的入库单关联出库单）
    private Long outstorageBillId;
    //出库单编号（“生产退货入库”类型的入库单关联出库单）
    private String outstorageBillCode;
    //生产任务单号
    private String productNo;
    //客户code（货主code）
    private String customerCode;
    //客户名称（货主名称（冗余））
    private String customerName;
    //供应商编号
    private String supplierCode;
    //供应商名称（冗余）
    private String supplierName;
    //备注
    private String remark;
    //状态（0：待提交；1：已提交）
    private String state;
    //提交时间
    private String submitTime;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private Long createBy;

    //当前登陆人（页面交互缓存字段）
    @TableField(exist = false)
    private String userName;
    //收货计划编号（页面交互缓存字段）
    @TableField(exist = false)
    private String receiptCode;

    //入库单类型转译
    @TableField(exist = false)
    private String instorageTypeContent;
    //状态转译
    @TableField(exist = false)
    private String stateContent;

}
    