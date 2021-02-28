package com.tbl.modules.outstorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OutStorageDetailDAO extends BaseMapper<OutStorageDetailManagerVO> {

    /**
     * 详情添加下拉框
     *
     * @param queryString
     * @param page
     * @return
     */
    List<Map<String, Object>> getSelectMaterialList(@Param("positionCode") String positionCode, @Param("customerCode") String customerCode, @Param("materialCodes") List materialCodes, @Param("billType") String billType, @Param("queryString") String queryString, Page page);

    /**
     * 详情添加下拉框
     *
     * @param queryString
     * @return
     */
    Integer getSelectMaterialTotal(@Param("queryString") String queryString);

    /**
     * 判断物料是否存在
     *
     * @param outStorageId
     * @param materialCode
     * @return
     */
    Integer getMaterialCount(@Param("outStorageId") String outStorageId, @Param("materialCode") String materialCode, @Param("batchNo") String batchNo, @Param("positionCode") String positionCode);

    /**
     * 出库详情中的物料的添加
     *
     * @param outStorageId
     * @param materialCode
     * @param materialName
     * @return
     */
    Boolean addOutStorageDetail(@Param("outStorageId") String outStorageId, @Param("materialCode") String materialCode, @Param("materialName") String materialName);

    /**
     * 根据出库单的Id查询是否存在详情
     *
     * @param outStorageId
     * @return
     */
    List<OutStorageDetailManagerVO> getDetailList(@Param("outStorageId") String outStorageId);

    /**
     * 更新状态
     *
     * @param outStorageId
     * @return
     */
    Object updateState(@Param("outStorageId") String outStorageId);

    /**
     * 更新数量
     *
     * @param id
     * @param amount
     * @return
     */
    Integer updateDetailAmount(@Param("amount") String amount, @Param("id") String id);

    /**
     * 更新批次号
     *
     * @param id
     * @param batchNo
     * @return
     */
    Integer updateDetailBatchNo(@Param("batchNo") String batchNo, @Param("id") String id);

    /**
     * 获取最大编号
     *
     * @return
     */
    String getMaxXJstorageCode();


    /**
     * 获取占用库存
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> getoccupystock(@Param("outStorageBillCode") String outStorageBillCode);


    /**
     * 通过物料编号以及批次号进行对应的库存的查询
     *
     * @param materialNo
     * @param batchNo
     * @return
     */
    Object getstockAmount(@Param("materialNo") String materialNo, @Param("batchNo") String batchNo, @Param("materialType") String materialType);

    /**
     * 通过物料编号以及批次号进行对应的所有的库存信息查询
     *
     * @param materialNo
     * @param batchNo
     * @return
     */
    List<Map<String, Object>> getMaterialList(@Param("materialNo") String materialNo, @Param("batchNo") String batchNo, @Param("rfids") List rfids);

    /**
     * 根据物料编号以及批次号和可用的rfid 查询是否存在足够的数量
     *
     * @param materialNo
     * @param batchNo
     * @param rfids
     * @return
     */
    Integer getAmount(@Param("materialNo") String materialNo, @Param("batchNo") String batchNo, @Param("rfids") List rfids);

    /**
     * 根据物料编号以及批次号和可用的rfid 查询是否存在足够的重量
     *
     * @param materialNo
     * @param batchNo
     * @param rfids
     * @return
     */
    Double getWeight(@Param("materialNo") String materialNo, @Param("batchNo") String batchNo, @Param("rfids") List rfids);

    /**
     * 通过物料编号以及批次号获取对应的货权转移的来源
     *
     * @param materialCode
     * @param batchNo
     * @return
     */
    Map<String, Object> getDataSource(@Param("materialCode") String materialCode, @Param("batchNo") String batchNo, @Param("materialType") String materialType);

    /**
     * 通过绑定表中的ID进行数据的占用
     *
     * @param mfId
     * @param weight
     */
    Integer occupiedInventory(@Param("mfId") String mfId, @Param("amount") String amount, @Param("weight") String weight, @Param("type") String type, @Param("outStorageBillCode") String outStorageBillCode);

    /**
     * 通过Rfid锁定库存中的数量
     *
     * @param amount
     * @param outStorageBillCode
     * @return
     */
    Integer occupiedStock(@Param("amount") String amount, @Param("weight") String weight, @Param("rfid") String rfid, @Param("outStorageBillCode") String outStorageBillCode, @Param("batchNo") String batchNo,@Param("materialCode")String materialCode);

    /**
     * 查询可用的RFID编号
     *
     * @param materialNo
     * @param batchNo
     * @return
     */
    List<String> getRfidList(@Param("materialNo") String materialNo, @Param("batchNo") String batchNo);


    Boolean updateDetail(@Param("amount") String amount, @Param("weight") String weight, Long outDetailId);

    /**
     * 根据物料编号以及批次号获取对应的总的出库的数量
     *
     * @param materialCode
     * @param batchNo
     * @param lowerId
     * @return
     */
    Object getmaterialAmount(String lowerId, String materialCode, String batchNo);

    /**
     * 根据物料编号以及批次号获取对应的总的出库的数量
     *
     * @param materialCode
     * @param batchNo
     * @param lowerId
     * @return
     */
    Object getmaterialWeight(String lowerId, String materialCode, String batchNo);

    /**
     * 通过物料编号查询对应的物料的单位
     *
     * @param materialName
     * @return
     */
    Object getUnit(String materialCode);

    /**
     * 根据出库单Id删除出库单详情
     *
     * @param outStorageId
     */
    void deleteByOutStorageId(Long outStorageId);
}
