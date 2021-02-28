package com.tbl.modules.stock.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 货权单据实体类
 *
 * @author yuany
 * @date 2019-05-24
 */
@Getter
@Setter
@TableName("materiel_power")
public class MaterielPower implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 库存id
     */
    private Long stockId;

    /**
     *单据类型（0：自采；1：客供）
     */
    private String documentType;

    /**
     *货权转移之前的物料
     */
    private String materielCode;
}
