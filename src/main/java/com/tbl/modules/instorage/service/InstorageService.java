package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import javax.servlet.http.HttpServletResponse;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;

import java.util.List;
import java.util.Map;

public interface InstorageService extends IService<Instorage> {

    /**
    * @Description:  生成入库单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/15
    */
    String generateInstorageCode();
    /**
    * @Description:  获取入库单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Page<Instorage> queryPage(Map<String, Object> map);
    /**
    * @Description:  保存入库单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Map<String, Object> saveInstorage(Instorage instorage);
    /**
    * @Description:  入库单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    void updateInstorageState(Long instorageId,String instorageType,String crossDocking,Long outstorageBillId,List<InstorageDetail> lstInstorageDetail);
    /**
    * @Description:  入库单删除
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    void deleteInstorage(String instorageType, Long id);
    /**
    * @Description:  生成上架单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    void generatePutBill(Long instorageId);
    /**
    * @Description:  获取出库单下拉列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    List<Map<String, Object>> getSelectOutstorageBillList(String queryString, int pageSize, int pageNo);

    /**
     * @Description:  获取出库单下拉列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/23
     */
    List<Map<String, Object>> getSelectReceiptPlanList(String queryString, int pageSize, int pageNo);

    /**
    * @Description:  获取出库单下拉列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    Integer getSelectOutstorageBillTotal(String queryString);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     */
    List<Instorage> getAllList(String ids);

    /**
     * 导出excel
     * @author pz
     * @date 2019-01-08
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<Instorage> list);
}
