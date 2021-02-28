package com.tbl.modules.slab.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import com.tbl.modules.visualization.entity.StockCar;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InstorageSlabDAO {
    /**
     * @Description: 根据用户id和ip查询平板绑定关系表中是否存在数据
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    Integer selectCountByUserIDAndIp(@Param("userIP") String userIP);

    /**
     * @Description: 插入平板绑定关系表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    void insertSlabBillBunding(Map<String, Object> paramMap);

    /**
     * @Description: 更新平板绑定关系表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/28
     */
    void updateSlabBillBunding(Map<String, Object> paramMap);

    /**
     * @Description: 获取上架单列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/1
     */
    List<PutBill> getPagePutBillList(Page<PutBill> pagePutBill, Map<String, Object> params);

    /**
     * @Description: 更新上架单表状态为“待上架”
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/1
     */
    void updatePutBillState(@Param("putBillId") Long putBillId, @Param("userId") Long userId, @Param("userName") String userName);

    /**
     * @Description: 获取叉车绑定表信息
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    Map<String, Object> getSlabBillBunding(@Param("userIP") String userIP);

    /**
     * @Description: 根据rfid查询绑定的物料
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    List<Map<String, Object>> getMaterialBundingDetail(@Param("rfid") String rfid);

    /**
     * @Description: 根据上架单id和物料编号获取上架单对应的入库详情单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    Map<String, Object> getPutBillDetailMap(@Param("putBillId") String putBillId, @Param("materialCode") String materialCode);

    /**
     * @Description: 更新平板参数绑定表的rfid和验证字段
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    void updateSlabBillBundingRfid(Map<String, Object> paramMap);

    /**
     * @Description: 获取库位下拉框列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/4
     */
    List<Map<String, Object>> getSelectPositionList(@Param("queryString") String queryString,
                                                    @Param("areaId") String areaId, @Param("xSize") Double xSize,
                                                    @Param("ySize") Double ySize, @Param("x_Size") Double x_Size,
                                                    @Param("y_Size") Double y_Size);

    /**
     * @Description: 获取库位下拉框列表总条数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/4
     */
    Integer getSelectPositionTotal(@Param("queryString") String queryString, @Param("areaId") String areaId,
                                   @Param("xSize") Double xSize, @Param("ySize") Double ySize,
                                   @Param("x_Size") Double x_Size, @Param("y_Size") Double y_Size);

    /**
     * @Description: 更新平板参数绑定表的库位编号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/4
     */
    void updateSlabBillBundingPositionCode(@Param("userIP") String userIP, @Param("positionCode") String positionCode);

    /**
     * @Description: 根据ip查询叉车正在操作单据的类型表中是否存在数据
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    Integer selectOperateTypeCount(@Param("userIP") String userIP);

    /**
     * @Description: 插入叉车正在操作单据的类型表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    void insertSlabOperateType(Map<String, Object> paramMap1);

    /**
     * @Description: 更新叉车正在操作单据的类型表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    void updateSlabOperateType(Map<String, Object> paramMap1);

    /**
     * @Description: 根据库位编号查询该库位上已存放的物料的托盘库存数量总数和库存重量总数
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    Map<String, Object> getStockBypositionCode(@Param("positionCode") String positionCode);

    /**
     * @Description: 查询推荐的新的库位
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    Map<String, Object> selectRecommendPosition(Map<String, Object> paramMap);

    /**
     * @Description: 查询推荐的新的库位2
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/13
     */
    Map<String, Object> selectRecommendPosition2(Map<String, Object> paramMap);

    /**
     * @Description: 根据入库单id和物料编号获取入库单详情中的批次号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/15
     */
    String getInstorageBillBatchNo(@Param("instorageBillId") Long instorageBillId, @Param("materialCode") String materialCode);

    /**
     * @Description: 更新平板入库操作参数绑定关系表的字段
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/28
     */
    void updateAlert1(@Param("userIP") String userIP);

    void updateAlert2(@Param("userIP") String userIP);

    Map<String, Object> getExecuteRfid(@Param("userIP") String userIP);

    /**
     * 更新slab_bill_bunding表的alert_key字段为3（已确定库位）
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/29
     */
    void updateAlertToUp(@Param("userIP") String userIP);

    /**
     * @Description: 获取小车位置
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/4/16
     */
    List<StockCar> getCarPosition(@Param("areaCode") String areaCode, @Param("forkliftTag") String forkliftTag, @Param("formDate") String formDate, @Param("nowDate") String nowDate);

    /**
     * @Description: 获取关于userip的tag
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/4/18
     */
    String getForkliftSelected(@Param("userIp") String userIp);

    /**
     * @Description: 查询上架单对应的入库单的入库流程（0：一般流程；1：白糖流程）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/4/19
     */
    String getInstorageProcess(@Param("putBillId") String putBillId);

    /**
     * @Description: 查询rfid是否已存在
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/5/6
     */
    Long getExistRfidCount(@Param("rfid") String rfid);

    /**
     * 获取UWB定位数据
     *
     * @param userIp
     * @return
     * @Author: yuany
     * @Date: 2019-06-11
     */
    UwbMoveRecord getUwbMoveRecord(@Param("userIp") String userIp);

}
