package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 上架单详情实体类
 * @author: zj
 * @create: 2019-01-22 17:38
 **/
@Getter
@Setter
@TableName("put_bill_detail")
public class PutBillDetail implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //上架单id
    private Long putBillId;
    //物料编码
    private String materialCode;
    //物料名称
    private String materialName;
    //批次号
    private String batchNo;
    //生产日期
    private String productDate;
    //包装单位
    private String unit;
    //RFID
    private String rfid;
    //上架数量
    private String putAmount;
    //上架重量
    private String putWeight;
    //上架库位编号
    private String positionCode;
    //详情状态（0：未上架；1：已上架）
    private String state;
}
    