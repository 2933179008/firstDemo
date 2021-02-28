package com.tbl.modules.alarm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.tbl.modules.constant.DyylConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 预警实体类
 *
 * @author yuany
 * @date 2019-01-31
 */
@Getter
@Setter
@TableName("alarm")
public class Alarm implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 预警编号
     */
    private String alarmCode;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 处理时间
     */
    private String endTime;

    /**
     * 预警时长
     */
    private String duration;

    /**
     * 库区
     */
    private Long areaBy;

    /**
     * 库区名称
     */
    @TableField(exist = false)
    private String areaName;

    /**
     * 类型（0：BOM差异预警；1：质检超时预警；2：RFID未绑定预警；3：叉车取货预警；4：扫描门出库预警；5：库存到期预警；6：库存差异预警；）
     */
    private String type;

    /**
     * 状态（0：未处理；1：已处理）
     */
    private String state;

    /**
     * 收件人ID
     */
    private String addressesBys;

    /**
     * 收件人姓名
     */
    @TableField(exist = false)
    private String addressesNames;

    /**
     * 发送方式（0：短信；1：邮件；）
     */
    private String sendType;

    /**
     * 发送内容
     */
    private String sendContent;

    /**
     * 绑定对应单据code
     */
    private String bindCode;
}
