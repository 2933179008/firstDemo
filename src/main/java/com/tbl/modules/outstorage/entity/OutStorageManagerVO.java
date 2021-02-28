package com.tbl.modules.outstorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lcg
 * data 2019/1/14
 */
@Getter
@Setter
@TableName("outstorage_bill")
public class OutStorageManagerVO implements Serializable {

    @TableField(value="id")
    private Long id;
    //出库单编号
    private String outstorageBillCode;
    //备料单编号
    private String spareBillCode;
    //库位编码
    private String positionCode;
    //客户code（货主code）
    private String customerCode;
    //出库计划时间
    private String outstoragePlanTime;
    //备注
    private String remark;
    //状态（0：未提交；1：待确认；2：已确认；3：已下架；）
    private String state;
    //创建时间
    private String createTime;
    //创建人
    private Long createBy;
    //修改时间
    private String updateTime;
    //出库类型
    private Long outstorageBillType;

    //物料类型 0表示自采 1表示客供
    private String materialType;

    //单据类型（0：无RFID单据；1：有RFID单据）
    private String billType;

    //入库单编码
    private String instorageCode;

    //收货单编码
    private String receiptCode;

}
