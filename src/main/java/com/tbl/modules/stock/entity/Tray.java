package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 托盘管理实体类
 *
 * @author yuany
 * @date 2019-01-15
 */

@Getter
@Setter
@TableName("tray")
public class Tray implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 合并/拆分编码
     */
    private String mergeOrSplitCode;

    /**
     * RFID
     */
    private String rfid;

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
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String createName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 状态（0已入库/1未入库）
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 还原人（删除人）
     */
    private Long deleteBy;

    /**
     * 还原状态（删除状态）0已经还原（删除），1未还原（删除）
     */
    private String deleteFlag;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 类型
     */
    private String type;

}
