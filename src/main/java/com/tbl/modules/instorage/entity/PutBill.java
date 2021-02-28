package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 上架单实体类
 * @author: zj
 * @create: 2019-01-22 17:38
 **/
@Getter
@Setter
@TableName("put_bill")
public class PutBill implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //上架单编号
    private String putBillCode;
    //入库单主键id
    private Long instorageBillId;
    //入库单编号
    private String instorageBillCode;
    //库区编码（多个之间用逗号“,”隔开）
    private String areaCode;
    //库区名称（冗余，多个之间用逗号“,”隔开）
    private String areaName;
    //上架单操作人（对应sys_user表的主键id）
    private Long operator;
    //上架单操作人名称（冗余）
    private String operatorName;
    //状态（0：未提交；1：待上架；2：上架中；3：上架完成）
    private String state;
    //提交时间
    private String submitTime;
    //备注
    private String remark;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private Long createBy;
    //入库类型
    @TableField(exist = false)
    private String instorageType;
    //入库流程
    @TableField(exist = false)
    private String instorageProcess;
    //状态转译
    @TableField(exist = false)
    private String stateContent;

}
    