package com.tbl.modules.outstorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lcg
 * data 2019/2/14
 */
@Getter
@Setter
@TableName("lower_shelf_bill")
public class LowerShelfBillVO {

    @TableField(value="id")
    private Long id;

    //下架单编号
    private String lowerShelfBillCode;

    //出库单ID
    private String outstorageBillId;

    //出库单编号
    private String outstorageBillCode;

    //库位编号
    private String positionCode;

    //客户编号
    private String customerCode;

    //下架时间
    private String lowerShelfTime;

    //状态（0：未下架；1：下架中；2：已下架；）
    private String state;

    //下架单创建时间
    private String createTime;

    //创建人
    private Long createBy;

    //修改时间
    private String updateTime;

    //操作人
    private String userId;

    //物料类型 0表示自产 1表示客供
    private String materialType;

    //单据类型（0：无RFID单据；1：有RFID单据）
    private String billType;

}
