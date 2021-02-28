package com.tbl.modules.platform.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 列实体
 *
 * @author anss
 * @date 2018-09-17
 */
@Getter
@Setter
@TableName("sys_columns")
public class Columns implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    private String uuid;

    private String content;

}
