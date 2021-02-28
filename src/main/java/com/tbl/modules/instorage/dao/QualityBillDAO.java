package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.QualityBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QualityBillDAO extends BaseMapper<QualityBill> {

    /**
    * @Description:  获取质检单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    List<QualityBill> getPageQualityBillList(Page<QualityBill> pageQualityBill, Map<String, Object> params);
    /**
    * @Description:  获取最大质检单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    String getMaxQualityCode();

    /**
     * @Description:  获取入库单下拉框列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/23
     */
    List<Map<String, Object>> getSelectInstorageList(Page page, @Param("queryString") String queryString);
    /**
     * @Description:  获取入库单下拉框列表数据总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/23
     */
    Integer getSelectInstorageTotal(@Param("queryString") String queryString);

    /**
     * @Description:  获取物料下拉列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    List<Map<String, Object>> getSelectMaterialList(Page page, @Param("instorageBillId") Long instorageBillId,@Param("queryString") String queryString);

    /**
     * @Description:  获取物料下拉列表总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    Integer getSelectMaterialTotal(@Param("instorageBillId") Long instorageBillId,@Param("queryString") String queryString);
    /**
    * @Description:  根据入库单查询入库单中已上架的物料的信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/15
    */
    List<Map<String, Object>> selectPutDetailByInstorageBillId(Long instorageBillId);
    /**
    * @Description:  更新库存表中的可用库存数量、可用库存重量、可用托盘库存数量、可用rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/15
    */
    void updateStock(@Param("lstPutBillDetail") List<Map<String, Object>> lstPutBillDetail);
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

    //获取质检超时集合

    List<Map<String, Object>> getTimeOutList();
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     * */
    List<QualityBill> getAllLists(List<Long> ids);
}
