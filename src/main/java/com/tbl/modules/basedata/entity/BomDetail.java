package com.tbl.modules.basedata.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: dyyl
 * @description: bom详情表实体
 * @author: zj
 * @create: 2019-01-04 10:13
 **/
@Getter
@Setter
@TableName("base_bom_detail")
public class BomDetail implements Serializable {
    /**
     * bom主键
     */
    @TableId(value="id")
    private Long id;
    //bom主键id
    private Long bomId;
    //物料编号
    private String materialCode;
    //物料名称
    private String materialName;
    //物料类别
    private String materialType;
    //数量
    private String amount;
    //重量(kg)
    private String weight;
    //单价
    private String unitPrice;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //删除标识（0删除，1存在）
    private String deletedFlag;
    //删除人id
    private Long deletedBy;


}
    