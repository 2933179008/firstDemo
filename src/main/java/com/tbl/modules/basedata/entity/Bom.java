package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: bom实体
 * @author: zj
 * @create: 2019-01-04 10:05
 **/
@Getter
@Setter
@TableName("base_bom")
public class Bom implements Serializable {
    /**
     * bom主键
     */
    @TableId(value="id")
    private Long id;

    //bom编号
    @TableField(value="bom_code")
    private String bomCode;
    //bom名称
    private String bomName;
    //产品主键id
    private Long productId;
    //产品编号
    private String productCode;
    //产品名称
    private String productName;
    //状态（0未提交，1提交）
    private String state;
    //备注
    private String remark;
    //bom版本号
    private String bomVersion;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //删除标识（0删除，1存在）
    private String deletedFlag;
    //删除人id
    private Long deletedBy;

}
    