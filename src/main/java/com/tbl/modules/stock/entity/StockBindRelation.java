package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库存物料绑定rfid关系实体类
 *
 * @author pz
 * @date 2019-01-10
 */
@Getter
@Setter
@TableName("stock_bind_relation")
public class StockBindRelation implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;


    /**
     * 库存ID
     */
    private Long stockId;


    /**
     * 物料绑定rfid主键ID
     */
    private Long materialBindRfidId;


}
