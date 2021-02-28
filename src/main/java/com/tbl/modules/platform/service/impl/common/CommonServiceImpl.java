package com.tbl.modules.platform.service.impl.common;

import com.google.common.collect.Maps;
import com.tbl.modules.platform.dao.system.ColumnsDAO;
import com.tbl.modules.platform.entity.system.Columns;
import com.tbl.modules.platform.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tbl.modules.platform.util.EhcacheUtils.columnCache;

/**
 * 公告接口实现
 *
 * @author anss
 * @date 2018-09-17
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private ColumnsDAO columnsDAO;


    /**
     * 保存用户自定义列
     * @author anss
     * @date 2018-09-17
     * @param columns 列
     * @param uuid 唯一标志
     * @return
     */
    @Override
    public Map<String,Object> dynamicColumns(String columns,String uuid) {
        Map<String,Object> map= Maps.newHashMap();
        map.put("uuid", uuid);
        map.put("columns", columns);
        boolean result = columnsDAO.insertCoumns(map);
        columnCache.put(uuid,columns);
        map.put("result",result);
        map.put("msg","操作成功!");
        return map;
    }

    /**
     * @author anss
     * @date 2018-09-17
     * @param uuid 用户自定义列唯一标志
     * @return
     */
    @Override
    public Map<String,Object> dynamicGetColumns(String uuid) {
        Map<String,Object> map=Maps.newHashMap();
        String content = "";
        Object data=columnCache.get(uuid);
        if(data==null){
            map.put("uuid", uuid);
            List<Columns> lstColumn = columnsDAO.selectByMap(map);

            if(lstColumn != null && lstColumn.size() > 0){
                columnCache.put(uuid, lstColumn.get(0).getContent());
                content = lstColumn.get(0).getContent();
            }
        }
        map.put("data", content);
        return map;
    }

}
