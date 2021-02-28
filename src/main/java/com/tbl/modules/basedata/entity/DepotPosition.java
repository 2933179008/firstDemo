package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库位管理实体类
 *
 * @author yuany
 * @date 2018-01-04
 */
@Getter
@Setter
@TableName("base_depot_position")
public class DepotPosition implements Serializable {

    /**
     * 库位主键ID
     */
    private Long id;

    /**
     * 库区外键ID
     */
    private Long depotareaId;

    /**
     * 库区名称
     */
    @TableField(exist = false)
    private String areaName;

    /**
     * 库位编码
     */
    private String positionCode;

    /**
     * 库位名称
     */
    private String positionName;

    /**
     * 排
     */
    private String row;

    /**
     * 列
     */
    private String column;

    /**
     * 层
     */
    private String layer;

    /**
     * 库位类型
     */
    private String positionType;

    /**
     * 库位类型名称
     */
    @TableField(exist = false)
    private String positionTypeName;

    /**
     * 混放类型（0表示能混放，1表示不能混放）
     */
    private String blendType;

    /**
     * 混放类型名称
     */
    @TableField(exist = false)
    private String blendTypeName;

    /**
     * ABC分类
     */
    private String classify;

    /**
     * 托盘容量
     */
    private String capacityRfidAmount;

    /**
     * 容量
     */
    private String capacityWeight;

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
     * 长（m）
     */
    private String length;

    /**
     * 宽（m）
     */
    private String width;

    /**
     * 起始x轴（左下）
     */
    private String startX;

    /**
     * 起始y轴（左下）
     */
    private String startY;

    /**
     * 终点x轴（右上）
     */
    private String endX;

    /**
     * 终点y轴（右上）
     */
    private String endY;

    /**
     * 库位方向
     */
    private String positionDirection;

    /**
     * 库位冻结（0:未冻结;1:已冻结）
     */
    private String positionFrozen;

}
