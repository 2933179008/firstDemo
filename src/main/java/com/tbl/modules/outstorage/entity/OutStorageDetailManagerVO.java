package com.tbl.modules.outstorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lcg
 * data 2019/1/17
 */
@Getter
@Setter
@TableName("outstorage_bill_detail")
public class OutStorageDetailManagerVO implements Serializable{
    @TableField(value="id")
    private Long id;
    //物料编码
    private String materialCode;
    //物料名称
    private String materialName;
    //物料单位
    private String unit;
    //物料批次号
    private String batchNo;
    //数量
    private String amount;
    //出库单Id
    private String outstorageBillId;
    //初始化数量
    private String separableAmount;
    //初始化重量
    private String separableWeight;
    //重量
    private String weight;

    //生产日期
    private String productData;

    //库位编号
    private String positionCode;
}
