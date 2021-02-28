package com.tbl.modules.outstorage.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lcg
 * data 2019/2/14
 */
@Getter
@Setter
@TableName("lower_shelf_bill_detail")
public class LowerShelfBillDetailVO {

    @TableField(value="id")
    private Long id;

    //下架单ID
    private String lowerShelfBillId;

    //物料编号
    private String materialCode;

    //物料名称
    private String materialName;

    //批次号
    private String batchNo;

    //数量
    private String amount;

    //数量
    private String state;

    //库存位置
    private String positionCode;

    //rfid编号
    private String rfid;

    //重量
    private String weight;

    //是否经过货权转移
    private String transform;

    //生产采购日期
    private String productData;

}
