package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 质检单详情实体类
 * @author: zj
 * @create: 2019-02-11 15:59
 **/
@Getter
@Setter
@TableName("quality_bill_detail")
public class QualityBillDetail implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //质检单id主键
    private Long qualityId;
    //物料编号
    private String materialCode;
    //物料名称
    private String materialName;
    //批次号
    private String batchNo;
    //样本重量
    private String sampleWeight;
    //合格重量
    private String qualifiedWeight;
    //合格率
    private String passRate;
    //物料质检h合格状态：0：合格，1：不合格
    private String state;
}
    