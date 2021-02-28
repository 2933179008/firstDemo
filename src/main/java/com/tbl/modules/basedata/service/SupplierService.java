package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.Supplier;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface SupplierService extends IService<Supplier> {

    /**
     * 获取分页列表
     * @author pz
     * @date 2018-01-02
     * @return
     */
    PageUtils queryPageS(Map<String, Object> params);

    /**
     * 删除供应商实体（逻辑删除）
     * @author pz
     * @date 2018-01-02
     * @param ids:要删除的id集合
     * @param userId：当前登陆人Id
     * @return
     */
    boolean delLstSupplier(String ids, Long userId);

    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Supplier>
     */
    List<Supplier> getAllListS(String ids);


    /**
     * 导出excel
     * @author pz
     * @date 2019-01-08
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<Supplier> list);


}