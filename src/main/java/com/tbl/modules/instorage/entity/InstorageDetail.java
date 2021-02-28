package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 入库单详情实体类
 * @author: zj
 * @create: 2019-01-21 15:03
 **/
@Getter
@Setter
@TableName("instorage_bill_detail")
public class InstorageDetail implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //入库单id
    private Long instorageBillId;
    //物料编码
    private String materialCode;
    //物料名称
    private String materialName;
    //批次号
    private String batchNo;
    //包装单位
    private String unit;
    //入库数量
    private String instorageAmount;
    //可拆分数量
    private String separableAmount;
    //入库重量
    private String instorageWeight;
    //可拆分重量
    private String separableWeight;
    //生产日期
    private String productDate;

}
    