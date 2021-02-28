package com.tbl.modules.outstorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lcg
 * data 2019/1/15
 */
@Getter
@Setter
@TableName("spare_bill")
public class SpareBillManagerVO implements Serializable {

    @TableField(value="id")
    private Long id;
    //备料单编号
    private String spareBillCode;
    //使用开始预定时间
    private String userStartTime;
    //产品编号
    private String productCode;
    //产品名称
    private String productName;
    //总生产数量（单位：箱）
    private String totalProductAmount;
    //单缸投料数量（单位：箱）
    private String simplexFeedAmount;
    //缸投料数量（单位：缸）
    private String cylinderFeedAmount;
    //调配室使用线（值为：1或2或3或4）
    private String mixUseLine;
    //特殊事项栏
    private String specialMatter;
    //状态（0：未生成出库单；1：生成部分出库单；2：已生成出库单；3：出库中；4：已完成）
    private String state;
    //生成出库单时间
    private String receiptStartTime;
    //创建时间
    private String createTime;
    //创建人
    private Long createBy;
    //修改时间
    private String updateTime;
    //生产任务编号
    private String productNo;
    //备注
    private String remark;

    //erp备料单编号
    private String erpSpareBillCode;

    private String productUser;

    private String productTime;

    private String productState;

    private String materialsUser;

    private String materialsTime;

    private String materialsState;

    private String qualityUser;

    private String qualityTime;

    private String qualityState;
}
