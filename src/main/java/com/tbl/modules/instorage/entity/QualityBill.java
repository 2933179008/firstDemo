package com.tbl.modules.instorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: 质检单实体类
 * @author: zj
 * @create: 2019-02-11 15:54
 **/
@Getter
@Setter
@TableName("quality_bill")
public class QualityBill implements Serializable {
    //主键
    @TableField(value = "id")
    private Long id;
    //质检单编号
    private String qualityCode;
    //入库单id主键
    private Long instorageBillId;
    //入库单编号
    private String instorageBillCode;
    //质检时间
    private String qualityTime;
    //状态（0：未提交；1：质检通过；2：质检退回；）
    private String state;
    //备注
    private String remark;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private Long createBy;
    //状态转译
    @TableField(exist = false)
    private String stateContent;
}
    