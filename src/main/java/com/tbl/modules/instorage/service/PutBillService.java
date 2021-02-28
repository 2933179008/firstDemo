package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.PutBill;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface PutBillService extends IService<PutBill> {
    /**
    * @Description: 获取上架单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    Page<PutBill> queryPage(Map<String, Object> map);

    /**
    * @Description:  生成上架单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    String generatePutBillCode();
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
    * @Description:  获取操作人下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    List<Map<String, Object>> getSelectOperatorList(String queryString, int pageSize, int pageNo);
    /**
    * @Description:  获取操作人下拉框列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    Integer getSelectOperatorTotal(String queryString);
    /**
    * @Description:  上架单的新增/修改 保存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    Map<String, Object> savePutBill(PutBill putBill);
    /**
    * @Description:  上架单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    void submitPutBill(Long instorageBillId,Long putBillId,String instorageProcess);
    /**
    * @Description:  校验绑定rfid绑定的物料数量和重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    Map<String, Object> checkRfidAmountAndWeight(Long instorageBillId,String rfid,String materialCode, Double putAmount, Double putWeight);
    /**
    * @Description:  校验库位是否可以上架(
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    Map<String, Object> isAvailablePosition(String rfid, String positionCode);
    /**
    * @Description:  删除上架单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/18
    */
    void deletePutBill(Long id);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     */
    List<PutBill> getAllList(String ids);

    /**
     * 导出excel
     * @author pz
     * @date 2019-01-08
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<PutBill> list);
}
