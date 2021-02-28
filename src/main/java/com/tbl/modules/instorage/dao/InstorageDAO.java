package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InstorageDAO extends BaseMapper<Instorage> {

    String getMaxInstorageCode();
    /**
    * @Description:  获取入库单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    List<Instorage> getPageInstorageList(Page<Instorage> pageInstorage, Map<String, Object> params);
    /**
    * @Description:  根据入库单id更新状态
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    Integer updateState(@Param("instorageId") Long instorageId,@Param("state") String state);
    /**
    * @Description:  更新收货单详情的可拆分数量和可拆分重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    void updateReceiptSeparableAmountAndWeight(@Param("lstInstorageDetail") List<InstorageDetail> lstInstorageDetail,@Param("receiptPlanId") Long receiptPlanId);
    /**
    * @Description:  获取出库单下拉列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    List<Map<String, Object>> getSelectOutstorageBillList(Page page,@Param("queryString") String queryString);

    /**
     * @Description:  获取收货单下拉列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/23
     */
    List<Map<String, Object>> getSelectReceiptPlanList(Page page,@Param("queryString") String queryString);
    /**
    * @Description:  获取出库单下拉列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    Integer getSelectOutstorageBillTotal(@Param("queryString") String queryString);
    /**
    * @Description:  根据出库单id查询生产任务单号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    String getProductNo(Long outstorageBillId);
    /**
    * @Description:  更新出库单详情中物料的可拆分数量和重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/26
    */
    void updateOutstorageBillDetail(@Param("outstorageBillId") Long outstorageBillId,@Param("lstInstorageDetail") List<InstorageDetail> lstInstorageDetail);
    /**
    * @Description:  更新入库单详情的可拆分数量和重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/26
    */
    void updateDetailSeparableAmountAndWeight(@Param("instorageId") Long instorageId);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     * */
    List<Instorage> getAllLists(List<Long> ids);
}
