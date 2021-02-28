package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.Materiel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 物料管理Service
 *
 * @author yuany
 * @date 2019-01-02
 */
public interface MaterielService extends IService<Materiel> {

    /**
     * 获取物料管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-02
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 删除物料实体（逻辑删除）
     *
     * @param ids:要删除的id集合
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2018-01-03
     */
    boolean delLstMateriel(String ids, Long userId);

    /**
     * 获取导出列
     *
     * @return List<Materiel>
     * @author yuany
     * @date 2019-01-03
     */
    List<Materiel> getAllLists(String ids);


    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author yuany
     * @date 2019-01-03
     */
    void toExcel(HttpServletResponse response, String path, List<Materiel> list);

    /**
     * 根据id更新rfid
     *
     * @param rfid
     * @param id
     * @return
     * @author yuany
     * @date 2019-01-09
     */
    int updateMaterielRfid(String rfid, Long id);

    /**
     * 生成物料单号
     *
     * @author yuany
     * @date 2019-02-01
     */
    String getMaterielCode();
}
