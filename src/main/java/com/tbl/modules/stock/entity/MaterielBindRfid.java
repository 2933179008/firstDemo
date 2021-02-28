package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 物料绑定RFID管理实体类
 *
 * @author yuany
 * @date 2019-01-07
 */
@Getter
@Setter
@TableName("materiel_bind_rfid")
public class MaterielBindRfid implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 绑定编码
     */
    private String bindCode;

    /**
     * RFID
     */
    private String rfid;

    /**
     * 操作时间
     */
    private String createTime;

    /**
     * 操作人ID
     */
    private Long createBy;

    /**
     * 操作人
     */
    @TableField(exist = false)
    private String createName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 修改人
     */
    @TableField(exist = false)
    private String updateName;

    /**
     * 逻辑删除标记字段（0表示删除，1表示存在）
     */
    private String deletedFlag;

    /**
     * 删除人id
     */
    private Long deletedBy;


    /**
     * 库位id
     */
    private Long positionBy;

    /**
     * 库位名称
     */
    @TableField(exist = false)
    private String positionName;

    /**
     * 库区名称
     */
    @TableField(exist = false)
    private String areaName;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 入库流程（0：一般流程；1：白糖流程）
     */
    private String instorageProcess;

    /**
     * x轴坐标
     */
    private String coordinateX;

    /**
     * y轴坐标
     */
    private String coordinateY;

    /**
     * 排序
     */
    private Integer sort;

}
