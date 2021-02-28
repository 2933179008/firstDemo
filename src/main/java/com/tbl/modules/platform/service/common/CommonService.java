package com.tbl.modules.platform.service.common;

import java.util.List;
import java.util.Map;

/**
 * 公共接口
 *
 * @author anss
 * @date 2018-09-17
 */
public interface CommonService {

    /**
     * 保存用户自定义列
     * @author anss
     * @date 2018-09-17
     * @param columns 列
     * @param uuid 唯一标志
     * @return
     */
    Map<String,Object> dynamicColumns(String columns,String uuid);

    /**
     * @author anss
     * @date 2018-09-17
     * @param uuid 用户自定义列唯一标志
     * @return
     */
    Map<String,Object> dynamicGetColumns(String uuid);
}
