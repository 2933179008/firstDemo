package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 移位管理实体类
 *
 * @author yuany
 * @date 2019-01-21
 */
@Getter
@Setter
@TableName("move_position")
public class MovePosition implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 移位编码
     */
    private String movePositionCode;

    /**
     * 移位类型(0:散货移位;1:整货移位)
     */
    private String movePositionType;

    /**
     * 物料编码
     */
    private String materielCode;

    /**
     * 物料名称
     */
    private String materielName;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * RFID
     */
    private String rfid;

    /**
     * 移出库位
     */
    private Long formerPosition;

    /**
     * 原库位名称
     */
    @TableField(exist = false)
    private String formerPositionName;

    /**
     * 原库位编码
     */
    @TableField(exist = false)
    private String formerPositionCode;

    /**
     * 移入库位id
     */
    private Long positionBy;

    /**
     * 移入库位名称
     */
    @TableField(exist = false)
    private String positionName;

    /**
     * 移入库位编码
     */
    @TableField(exist = false)
    private String positionCode;

    /**
     * 移位数量
     */
    private String movePositionAmount;

    /**
     * 移位数量
     */
    private String movePositionWeight;

    /**
     * 移位人ID
     */
    private Long moveUserId;

    /**
     * 移位人名称
     */
    @TableField(exist = false)
    private String moveUserName;

    /**
     * 移位创建时间
     */
    private String moveFoundTime;

    /**
     * 开始移位时间
     */
    private String movePositionTime;

    /**
     * 移位完成时间
     */
    private String completeTime;

    /**
     * 状态(0:未移位，1：移位中，2：已完成)
     */
    private String status;

    /**
     * 状态名称
     */
    @TableField(exist = false)
    private String statusName;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 删除（0已删除，1未删除）
     */
    private String deleteFlag;

    /**
     * 删除人
     */
    private Long deleteBy;

}
