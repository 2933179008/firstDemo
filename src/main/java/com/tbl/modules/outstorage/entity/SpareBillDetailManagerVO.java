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
@TableName("spare_bill_detail")
public class SpareBillDetailManagerVO implements Serializable {

    @TableField(value="id")
    private Long id;

    //行号
    private Long line;

    //备料单ID
    private Long spareBillId;

    //物料编码
    private String materialCode;

    //物料名称
    private String  materialName;

    //供应商编号
    private String supplierCode;

    //供应商名称
    private String supplierName;

    //库位编码
    private String positionCode;

    //库位名称
    private String positionName;

    //使用数量
    private String usedAmount;

    //仓库发货数量
    private String sendAmount;

    //准备数量
    private String quantityReady;

    //生产剩余数量
    private String surplusAmount;

    //计量日期
    private String measurementTime;

    //使用量
    private String userAmount;

    //理论剩余量
    private String theoryAmount;

    //实际剩余量
    private String realAmount;

    //差异
    private String difference;

    //月份
    private String month;

    //日期
    private String day;

    //顺序
    private String orderBy;

    //确认重量
    private String queryWeight;

    //确认箱数
    private String queryBox;

    //确认添加数量
    private String queryAdd;

    //使用的箱数
    private String usedBox;

    //使用的每箱重量
    private String usedWeight;

    //使用的添加数量
    private String addAmount;

    //准备重量
    private String quantityWeight;

}
