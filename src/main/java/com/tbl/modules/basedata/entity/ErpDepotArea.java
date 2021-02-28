package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * erp库区管理实体类
 *
 * @author pz
 * @date 2019-04-29
 */
@Getter
@Setter
@TableName("erp_base_depot_area")
public class ErpDepotArea implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 库区编号
     */
    private String areaCode;

    /**
     * 库区名称
     */
    private String areaName;

    /**
     * 区分实仓虚仓（值为503为客供，其它为自采）
     */
    private String ftypeId;

    /**
     * 依据fTypeID赋值
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 库位数
     */
    private  Integer positionAmount;

    /**
     * 库区类别（0收货区，1发货区，2良品存储区，3虚拟存储区，4退货存储区，5不良品存储区）
     */
    private String areaType;

    /**
     * 库区类别名称
     */
    @TableField(exist = false)
    private String areaTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * X轴起始坐标
     */
    private String xsizeStart;

    /**
     * Y起始坐标
     */
    private String ysizeStart;

    /**
     * X轴终点坐标
     */
    private String xsizeEnd;

    /**
     * Y轴终点坐标
     */
    private String ysizeEnd;
}