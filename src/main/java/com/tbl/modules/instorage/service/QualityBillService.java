package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.QualityBill;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface QualityBillService extends IService<QualityBill> {

    /**
    * @Description:  获取质检单列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    Page<QualityBill> queryPage(Map<String, Object> map);
    /**
    * @Description:  生成质检单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    String generateQualityCode();
    /**
    * @Description:  质检单的新增/修改 保存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/12
    */
    Map<String, Object> saveQualityBill(QualityBill qualityBill);

    /**
     * @Description:  获取入库单下拉框列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/23
     */
    List<Map<String, Object>> getSelectInstorageList(String queryString, int pageSize, int pageNo);
    /**
     * @Description:  获取入库单下拉框列表数据总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/23
     */
    Integer getSelectInstorageTotal(String queryString);

    /**
     * @Description:  获取物料下拉列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectMaterialList(Long instorageBillId, String queryString, int pageSize, int pageNo);
    /**
     * @Description:  获取物料下拉列表总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    Integer getSelectMaterialTotal(Long instorageBillId, String queryString);
    /**
    * @Description:  质检单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/14
    */
    void submitQualityBill(String qualityId);
    /**
    * @Description:  根据质检单id查询对应的入库单的入库流程
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/22
    */
    Map<String,Object> getInstorageProcess(String qualityId);
    /**
    * @Description:  根据质检单id查询质检单对应上架单详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/22
    */
    List<Map<String, Object>> getPutBillDetail(String qualityId);
    /**
    * @Description:  删除质检单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/18
    */
    void deleteQualityBill(Long id);

    List<Map<String, Object>> getTimeOutList();

    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     */
    List<QualityBill> getAllList(String ids);

    /**
     * 导出excel
     * @author pz
     * @date 2019-01-08
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<QualityBill> list);
}
