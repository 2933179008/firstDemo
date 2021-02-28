package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBillDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PutBillDetailDAO extends BaseMapper<PutBillDetail> {
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
    * @Description:  根据上架单主键和物料编码获取物料数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    Integer getMaterialCount(Map<String, Object> map);
    /**
    * @Description:  更行上架单上架数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    Integer updatePutAmount(@Param("putBillDetailId") Long putBillDetailId,@Param("putAmount") String putAmount);
    /**
    * @Description:  更新上架单的上架重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    Integer updatePutWeight(@Param("putBillDetailId") Long putBillDetailId,@Param("putWeight") String putWeight);
    /**
    * @Description:  更新上架单的RFID
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    Integer updateRfid(@Param("putBillDetailId") Long putBillDetailId,@Param("rfid") String rfid);
    /**
    * @Description:  查询已放有其他物料的可混放类型库位信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/30
    */
    List<Map<String, Object>> selectBlendPosition(Map<String, Object> paramMap);

    /**
     * @Description:  查询没放过物料的全新库位
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/30
     */
    List<Map<String, Object>> selectNewPosition(List<String> lstPositionCode);

    /**
    * @Description:  获取上架单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    List<PutBillDetail> getPagePutBillDetailList(Page<PutBillDetail> pagePutBillDetail, Map<String, Object> params);
    /**
    * @Description:  更新上架单的库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    Integer updatePositionCode(@Param("putBillDetailId") Long putBillDetailId,@Param("positionCode") String positionCode);
    /**
    * @Description:  根据入库单id、物料编号和批次号更新入库单的可拆分数量和可拆分重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updateSeparableAmountAndWeight(Map<String, Object> paramsMap);
    /**
    * @Description:  根据上架单详情id更新上架单详情状态为‘已上架’
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updatePutBillDetailState(@Param("putBillDetailId") String putBillDetailId);
    /**
    * @Description:  更新上架单状态为‘上架中’
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updateStateToPuting(@Param("putBillId") Long putBillId);
    /**
    * @Description:  更新上架单状态为‘上架完成’
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updateStateToComplete(@Param("putBillId") Long putBillId);

    /**
    * @Description:  根据入库单id更新上架单状态为‘上架完成’
    * @Param:
    * @return:
    * @Author: anss
    * @Date: 2019/04/29
    */
    Integer updateStateSToComplete(@Param("instorageBillId") Long instorageBillId);

    /**
    * @Description:  更新入库单状态为‘入库完成’
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updateInstorageStateToBeing(@Param("instorageBillId") Long instorageBillId);
    /**
    * @Description:  更新入库单状态为‘入库完成’
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/1
    */
    Integer updateInstorageStateToComplete(@Param("instorageBillId") Long instorageBillId);
    /**
    * @Description:  更新库存表的可用库存数量、可用库存重量、可用托盘库存数量、可用的RFID（多个rfid用逗号隔开）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/16
    */
    void updateStockAvailableField(Map<String, Object> paramsMap);
    /**
    * @Description:  根据物料编码和rfid更新物料绑定详情表的批次号和库位id
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/20
    */
    void updateBindRfidDetail(Map<String, Object> paramsMap1);
    /**
    * @Description:  校验一个rfid不能放在多个库位上
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    Map<String, Object> selectRfidInPosition(Map<String, Object> paramMap);
    /**
    * @Description:  更新物料绑定表状态为入库状态
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    void updateBindRfid(@Param("rfid") String rfid,@Param("positionId") Long positionId);
    /**
    * @Description:  根据上架单id查询上架详情中物料的种类
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/27
    */
    List<String> getInstorageMaterialCode(@Param("instorageBillId") Long instorageBillId);
    /**
    * @Description:  根据rfid查询rfid绑定的物料种类
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/27
    */
    List<String> getRfidMaterialCode(@Param("rfid") String rfid);
}
