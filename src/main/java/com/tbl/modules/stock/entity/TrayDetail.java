package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 托盘管理详情实现类
 *
 * @author yuany
 * @date 2019-01-18
 */
@Getter
@Setter
@TableName("tray_detail")
public class TrayDetail implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 托盘管理ID
     */
    private Long trayBy;

    /**
     * 物料绑定详情ID
     */
    private Long mbrDetailBy;

    /**
     * 数量
     */
    private String amount;

    /**
     * 重量
     */
    private String weight;

    /**
     * 显示状态（0已删除，1未删除）
     */
    private String deleteFlag;

    /**
     * 物料编码
     */
    private String materielCode;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materielName;

    /**
     * 批次规则
     */
    private String batchRule;

    /**
     * 包装单位
     */
    @TableField(exist = false)
    private String unit;

}
