package com.tbl.modules.alarm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 预警部署
 *
 * @yuany
 * @date 2019-03-11
 */

@Getter
@Setter
@TableName("alarm_deploy")
public class AlarmDeploy implements Serializable {

    @TableId("id")
    private Long id;

    /**
     * 预警类型（0：BOM差异预警；1：质检超时预警；2：RFID未绑定预警；3：叉车取货预警；4：扫描门出库预警；5：库存库龄预警；6：库存差异预警；）
     * 待议
     */
    private String alarmType;

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
     * 应用
     */
    private String apply;

}
