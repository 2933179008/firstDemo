package com.tbl.modules.slab.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.stock.entity.MovePosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MovePositionSlabDAO {
    /**
    * @Description:  获取平板移位列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    List<MovePosition> getPageMovePositionList(Page<MovePosition> pageMovePosition, Map<String, Object> params);
    /**
    * @Description:  根据ip查询平板移库操作参数绑定关系表中是否存在数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Integer selectCountByUserIDAndIp(@Param("userIP") String userIP);
    /**
    * @Description:  平板移库操作参数绑定关系表插入数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void insertSlabMovePositionBunding(Map<String, Object> paramMap);
    /**
    * @Description:  平板移库操作参数绑定关系表更新数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateSlabMovePositionBunding(Map<String, Object> paramMap);
    /**
    * @Description:  更新移库单表状态为“移位中”
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateMovePositionState(@Param("movePositionId") Long movePositionId);
    /**
    * @Description:  根据当前ip获取该叉车的验证信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Map<String, Object> getSlabMovePositionBunding(@Param("userIP") String userIP);
    /**
    * @Description:  获取库位下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    List<Map<String, Object>> getSelectPositionList(Page page,@Param("queryString") String queryString);
    /**
    * @Description:  获取库位下拉框列表总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Integer getSelectPositionTotal(@Param("queryString") String queryString);
    /**
    * @Description:  更新平板移库操作参数绑定关系表的库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateSlabMovePositionBundingPositionCode(@Param("movePositionId") Long movePositionId, @Param("userIP") String userIP,@Param("positionCode") String positionCode);
    /**
    * @Description:  更新平板参数绑定表的rfid和验证字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateSlabMovePositionBundingRfid(Map<String, Object> paramMap);
    /**
    * @Description:  根据rfid查询绑定的物料
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/7
    */
    List<Map<String, Object>> getMaterialBundingDetail(@Param("rfid") String rfid);
    /**
    * @Description:  根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/7
    */
    Map<String, Object> getStockInfo(@Param("positionCode") String positionCode);
    /**
    * @Description:  更新移库表的库位编码和其他字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/8
    */
    void updateMovePositionById(@Param("movePositionId") String movePositionId);
    /**
    * @Description:  根据rfid获取绑定表id和绑定详情表id
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/8
    */
    List<Map<String, Object>> selectMaterialBundingByRfid(@Param("rfid") String rfid);
    /**
    * @Description:  更新绑定表库位id
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/8
    */
    void updateMaterialBundingPosition(@Param("materielBindId") String materielBindId,@Param("positionId") Long positionId);
    /**
    * @Description:  更新绑定详情表库位id
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/8
    */
    void updateMaterialBundingDetailPosition(@Param("lstMaterielBindDetailId") List<String> lstMaterielBindDetailId,@Param("positionId") Long positionId);
    /**
    * @Description:  根据移库表的id查询原库位主键id和库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/9
    */
    Map<String, Object> selectFormerPosition(@Param("movePositionId") String movePositionId);
    /**
    * @Description:  减去原库位的库存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/9
    */
    void updateStockFormerPosition(Map<String,Object> paramMap);
    /**
    * @Description:  根据物料编号、批次号、库位编号查询库存中是否存在该条数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/9
    */
    Integer selectStockPositionCount(@Param("materialCode") String materialCode,@Param("batchNo") String batchNo,@Param("positionCode") String positionCode);
    /**
    * @Description:  更新现库位的库存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/9
    */
    void updateStockCurrentPosition(Map<String, Object> paramMap1);
    /**
    * @Description:  插入现库位的库存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/11
    */
    void insertStockCurrentPosition(Map<String, Object> paramMap1);
    /**
    * @Description:  查询该rfid是否可用
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/11
    */
    Integer getSelectAvailableRfid(@Param("materialCode") String materialCode1,@Param("batchNo") String batchNo1,@Param("formerpositionCode") String formerpositionCode,@Param("rfid") String rfid);
    /**
    * @Description:  根据ip查询叉车正在操作单据的类型表中是否存在数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    Integer selectOperateTypeCount(@Param("userIP") String userIP);
    /**
    * @Description:  插入叉车正在操作单据的类型表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    void insertSlabOperateType(Map<String, Object> paramMap1);
    /**
    * @Description:  更新叉车正在操作单据的类型表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    void updateSlabOperateType(Map<String, Object> paramMap1);
    /**
    * @Description:  根据物料编号和库位编号查询该库位中该物料的批次号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    String getStockBatchNo(@Param("materialCode") String materialCode,@Param("positionCode") String positionCode);
    /**
    * @Description:  根据库位编号获取库存中库位的物料编号和批次号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    List<Map<String, Object>> getStockMaterialCodeAndBatchNo(@Param("positionCode") String positionCode);
    /**
    * @Description:  获取验证通过的正在操作的rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    Map<String, Object> getExecuteRfid(@Param("userIP") String userIP);
    /**
    * @Description:  根据rfid查询移出库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    String selectPositionCodeByRfid(@Param("rfid") String rfid);

    /**
    * @Description:  更新平板移库操作参数绑定关系表的字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    void updateAlert1(@Param("userIP") String userIP);

    void updateAlert2(@Param("userIP") String userIP);
    /**
    * @Description:  查询库存数量和重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    Map<String, Object> selectStockAmountAndWeight(@Param("materialCode") String materialCode,@Param("batchNo") String batchNo,@Param("formerPositionCode") String formerPositionCode);
    /**
    * @Description:  根据物料编号，批次号，库位编号删除该条数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    void deleteStock(@Param("materialCode") String materialCode,@Param("batchNo") String batchNo,@Param("formerPositionCode") String formerPositionCode);
}
