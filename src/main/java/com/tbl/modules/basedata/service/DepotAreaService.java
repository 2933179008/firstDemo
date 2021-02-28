package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.DepotArea;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 库区管理Service
 *
 * @author yuany
 * @date 2019-01-04
 */
public interface DepotAreaService extends IService<DepotArea> {

    /**
     * 获取库区管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-04
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 获取导出列
     *
     * @return List<DepotArea>
     * @author yuany
     * @date 2019-01-04
     */
    List<DepotArea> getAllLists(String ids);


    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author yuany
     * @date 2019-01-04
     */
    void toExcel(HttpServletResponse response, String path, List<DepotArea> list);
}
