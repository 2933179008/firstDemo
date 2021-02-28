package com.tbl.modules.visualization.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@TableName("uwb_move_record")
public class StockCar implements Serializable {
    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 标签号
     */
    private String tag;

    /**
     * x坐标
     */
    private String xSize;

    /**
     * y坐标
     */
    private String ySize;

    /**
     * 坐标生成时间
     */
    private String createTime;

    /**
     * 移动：1:运动；0：静止；-1：默认值
     */
    private String isMove;

    /**
     * 库区号
     */
    private String sceneCode;

    /**
     * 叉车名称
     */
    @TableField(exist = false)
    private String forkliftName;

    /**
     * 用户IP
     */
    @TableField(exist = false)
    private String userIp;

}
