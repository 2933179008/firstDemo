package com.tbl.modules.outstorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LowerShelfDetailDAO extends BaseMapper<LowerShelfBillDetailVO> {

    /**
     * 通过出库单ID删除下架单的详情
     * @param outStorageId
     */
    Object deleteLowerShelDetail(@Param("outStorageId")String outStorageId);

    /**
     * 更新下架单详情为出库完成
     * @param lowerDetailId
     * @return
     */
    Integer updateLowerDetail(@Param("lowerDetailId")String lowerDetailId);

    /**
     * 通过下架单详情的ID获取对应的出库单ID
     * @param lowerDetailId
     * @return
     */
    Object getOutStorageId(@Param("lowerDetailId")String lowerDetailId);

    /**
     * 通过出库单ID,出库数量以及出库的Rfid进行物料绑定表中的变动
     * @param outStorageId
     * @param amount
     * @param rfid
     * @return
     */
    Integer updateMaterialBind(@Param("batchNo")String batchNo,@Param("materialCode")String materialCode,@Param("amount")String amount,@Param("rfid")String rfid,@Param("weight") String weight);

    /**
     * 通过出库单ID,出库数量以及出库的Rfid进行库存的变动
     * @param outcode
     * @param amount
     * @param rfid
     * @return
     */
    Integer updateStockAmount(@Param("batchNo")String batchNo,@Param("materialCode")String materialCode,@Param("amount")String amount,@Param("rfid")String rfid,@Param("type")String type,@Param("weight") String weight,@Param("outcode") String outcode);

    /**
     * 退货出库的库存变动
     * @param batchNo
     * @param materialCode
     * @param amount
     * @param rfid
     * @param type
     * @param weight
     * @param outcode
     * @return
     */
    Integer updateStockAmountOutStorageBill(@Param("batchNo")String batchNo,@Param("materialCode")String materialCode,@Param("amount")String amount,@Param("rfid")String rfid,@Param("type")String type,@Param("weight") String weight,@Param("outcode") String outcode);

    /**
     * 通过下架单详情Id
     * @param id
     * @return
     */
    Integer lowerCount(@Param("lowerDetailId")String lowerDetailId);

    /**
     * 更新下架状态为正在执行中
     * @param lowerDetailId
     * @return
     */
    Integer updateLowerShelfState(@Param("lowerDetailId")String lowerDetailId,@Param("state")String state);

    /**
     * 通过下架单ID获取对应的下架单的详情
     * @param lowerId                       下架单的Id
     * @return
     */
    List<Map<String,Object>> lowerInterfaceDetailList(String lowerId);

    /**
     * 根据出库单ID获取对应的出库单的类型
     * @param outStorageId
     * @return
     */
    Object getOutStorageType(String outStorageId);

    /**
     * 刪除绑定表中的库存
     * @return
     */
    Boolean deleteMaterialBind(@Param("rfid") String rfid,@Param("batchNo") String batchNo,@Param("materialCode") String materialCode);

    /**
     * 通过rfid查库存中的rfid 的数量以及数值,判断是否要删除
     * @param rfid
     * @return
     */
    Map<String,Object> getStockMap(@Param("rfid") String rfid,@Param("batchNo") String batchNo,@Param("materialCode") String materialCode, @Param("type")String type);

    /**
     * 判断是否经过货权转移
     * @param rfid
     * @param lowerDetailId
     * @return
     */
    Object getTransform(String rfid,String lowerDetailId);


    /**
     * 将货权转移的对应的数量加回去
     * @param weight
     * @param rfid
     * @param batcNo
     * @param outStorageId
     * @return
     */
    Void updateTransformStock(String amount,String rfid,String batcNo,String outStorageId,String type,String weight);

    /**
     * 更新货权转转移的数量
     * @param weight
     * @param rfid
     * @param batcNo
     * @param outStorageId
     * @return
     */
    Void updateTransformAmount(String amount,String rfid,String batcNo,String outStorageId,String type,String weight);

    /**
     * 删除库存中的rfid 绑定的数据
     * @param rfid
     * @return
     */
    Boolean deleteStockRfid(@Param("rfid") String rfid,@Param("batchNo") String batchNo,@Param("materialCode") String materialCode);

    /**
     * ID删除库存数据
     * @param id
     * @return
     */
    Boolean deleteStockById(@Param("id") String id);

    /**
     * 将库存中的rfid进行删除
     * @param trueRfid
     * @param stockdRfid
     * @param rfid
     * @return
     */
    Boolean updateStockRfid(@Param("trueRfid") String trueRfid,@Param("stockdRfid") String stockdRfid,@Param("rfid") String rfid,@Param("batchNo") String batchNo,@Param("materialCode") String materialCode,@Param("type") String type);

    /**
     * 通过rfid获取对应的出库单的操作人
     * @param rfid
     * @return
     */
    Object getLowerUserId(String rfid);

    /**
     * 通过rfid获取对应的任务的信息
     * @param rfid
     * @return
     */
    Map<String,Object> lowerDetail(String lowerDetailId);


    Object getSpareBillId(String outStorageId);

    /**
     * 通过下架单的ID删除对应的下架的信息
     * @param lowerId
     * @return
     */
    Object deleteLowerDetail(String lowerId);

    /**
     *  通过出库单的Id获取对应的下架单的详情的数据
     * @return
     */
    List<Map<String,Object>> getDetailList(String outstorageId);

    /**
     * 更新对应备料单的状态为完成
     */
    Object updateState(@Param("spareBillId") String spareBillId);
}
