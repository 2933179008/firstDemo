package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PutBillDAO extends BaseMapper<PutBill> {

    /**
    * @Description:  获取入库单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    List<PutBill> getPagePutBillList(Page<PutBill> pagePutBill, Map<String, Object> params);
    /**
    * @Description:  获取最大上架单编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    String getMaxPutBillCode();
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
    * @Description:  获取操作人下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    List<Map<String, Object>> getSelectOperatorList(Page page,@Param("queryString") String queryString);
    /**
    * @Description:  获取操作人下拉框列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    Integer getSelectOperatorTotal(@Param("queryString") String queryString);
    /**
    * @Description:  更新上架单的提交状态
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    Integer updateState(Long putBillId);
    /**
    * @Description:  查询物料绑定rfid信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    Map<String, Object> selectRfidBindDetail(@Param("rfid") String rfid,@Param("materialCode") String materialCode);
    /**
    * @Description:  根据上架单id获取上架详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/15
    */
    List<PutBillDetail> getPutBillDetail(@Param("putBillId") Long putBillId);
    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     * */
    List<PutBill> getAllLists(List<Long> ids);
}
