package com.tbl.modules.noah.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * UWB移动记录实体
 *
 * @author yuany
 * @date 2019-03-04
 */
@Setter
@Getter
@TableName("uwb_move_record")
public class UwbMoveRecord implements Serializable {

    //主键ID
    @TableField(value = "id")
    private Long id;

    //标签号
    private String tag;

    //X起始坐标
    private String xsize;

    //Y起始坐标
    private String ysize;

    //坐标生成时间
    private String createTime;

    //移动：1:运动；0：静止；-1：默认值
    private String isMove;

    //库区编码
    private String sceneCode;

}
