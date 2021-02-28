package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.Receipt;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ReceiptService extends IService<Receipt> {
    /**
    * @Description:  获取收货单列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/7
    */
    PageUtils queryPage(Map<String, Object> map);

    /**
    * @Description:  生成收货单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/7
    */
    String generateReceiptCode();
    
    /** 
    * @Description:  获取供应商下拉框列表
    * @Param:  
    * @return:  
    * @Author: zj 
    * @Date: 2019/1/8 
    */ 
    List<Map<String, Object>> getSelectSupplierList(String queryString, int pageSize, int pageNo);

    /**
    * @Description:  获取供应商下拉框列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/8
    */
    Integer getSelectSupplierTotal(String queryString);

    /**
    * @Description:  获取货主（客户）下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/8
    */
    List<Map<String, Object>> getSelectCustomerList(String queryString, int pageSize, int pageNo);

    /**
    * @Description:  获取货主（客户）下拉框列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/8
    */
    Integer getSelectCustomerTotal(String queryString);

    /**
    * @Description:  新增/修改 保存收货单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/8
    */
    Map<String,Object> saveReceipt(Receipt receipt);

    /**
    * @Description:  收货单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/11
    */
    boolean updateReceiptState(Long receiptId);
    /**
    * @Description:  异常收货
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/4
    */
    void updateReceiptAbnormalState(Long receiptId);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     */
    List<Receipt> getAllList(String ids);

    /**
     * 导出excel
     * @author pz
     * @date 2019-01-08
     * @param response
     * @param path
     * @param list
     */
    void toExcel(HttpServletResponse response, String path, List<Receipt> list);
}
