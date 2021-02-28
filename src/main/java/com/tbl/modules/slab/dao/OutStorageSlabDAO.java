package com.tbl.modules.slab.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OutStorageSlabDAO {

    /**
     * 获取对应的下架的人
     * @return
     */
    List<LowerShelfBillVO> getPagePutBillList(Page<LowerShelfBillVO> pagePutBill, Map<String, Object> params);

    /**
     * 通过userIP获取绑定表中是否已经存在
     * @param ip
     * @return
     */
    Integer getUserIpCount(String ip);

    /**
     * 插入操作类型
     * @param map
     * @return
     */
    Void insertMap(Map<String,Object> map);

    /**
     * 更新操作类型
     * @param map
     * @return
     */
    Void updateMap(Map<String,Object> map);

    /**
     * 查看当前的设备IP是否已经绑定的任务
     * @param userIp
     * @return
     */
    Integer getIpCount(String userIp);

    /**
     *  更新绑定关系
     * @param map
     */
    Void updateSlabOutBillBunding(Map<String,Object> map);

    /**
     * 插入绑定关系
     * @param map
     * @return
     */
    Void insertSlabOutBillBunding(Map<String,Object> map);

    /**
     * 更新下架单状态为进行中
     * @param lowerId
     * @return
     */
    Void updateLowerState(String lowerId);

    /**
     * 更新下架单详情状态为进行中
     * @param lowerId
     * @return
     */
    Void updateLowerDetailState(String lowerId);

    /**
     * 通过userIP获取当前的正在执行的任务
     * @param userIp
     * @return
     */
    Map<String,Object> getTaskMap(String userIp);

    /**
     * 通过下架单的ID获取到对应的出库单的ID
     * @param lowerId
     * @return
     */
    Object getOutStorageId(String lowerId);

    /**
     * 通过下架单Id以及rfid 获取当前的下架的任务
     * @param lowerId
     * @param rfid
     * @return
     */
    Integer getLowerTask(@Param("lowerId") String lowerId, @Param("rfid") String rfid);


    /**
     * 更新绑定关系中的数值
     * @param map
     * @return
     */
    Void updateSlabOutBillBundingByKey(Map<String,Object> map);


    /**
     *  获取下架单的详情
     * @param map
     * @return
     */
    Object getLowerDetailId(Map<String,Object> map);

    /**
     * 通过userIP获取可用的rfid
     * @param userIp
     * @return
     */
    Map<String,Object> avliableRfid(String userIp);

    /**
     * 清除表中的ip的rfid
     * @param ip
     */
    Object removeRfid(String ip);

    /**
     * 通过rfid 获取绑定表中的数值
     * @param rfid
     * @return
     */
    Map<String,Object> rfidBind(@Param("rfid") String rfid,@Param("outStorageId") String outStorageId);

    /**
     * 通过批次号已经重量判断在下架单详情中是否存在相同的数值
     * @param batch_no
     * @param weight
     * @return
     */
    Integer isExist(@Param("lowerId") String lowerId,@Param("batch_no") String batch_no,@Param("weight") String weight);

    /**
     * 更新下架单中的数值
     * @param lowerId
     * @param batch_no
     * @param weight
     * @return
     */
    Void updateRfid(@Param("rfid") String rfid,@Param("positionId") String positionId,@Param("lowerId") String lowerId,@Param("batch_no") String batch_no,@Param("weight") String weight,@Param("oldRfid") String oldRfid);

    /**
     * 移出物料绑定关系中的绑定关系
     * @param rfid
     * @param outStorageId
     * @return
     */
    Void removeMaterial(@Param("rfid") String rfid,@Param("outStorageId") String outStorageId);

    /**
     * 移出库存中的绑定关系
     * @param rfid
     * @param outStorageId
     * @return
     */
    Void removeStock(@Param("rfid") String rfid,@Param("outStorageId") String outStorageId);

    /**
     * 通过单据的id获取对应的可用的rfid的信息
     * @param id
     * @return
     */
    Object getRfid(String id);

    /**
     * 更新系统中的可用的rfid
     * @param id
     * @param avilabledRfid
     * @return
     */
    Integer updateAvilabledRfid(@Param("avilabledRfid") String avilabledRfid,@Param("id") String id);

    /**
     * 通过下架单ID以及rfid获取对应的物料的基础信息
     * @param rfid
     * @param lowerId
     * @return
     */
    Map<String,Object> getMaterialMap(@Param("rfid") String rfid,@Param("lowerId") String lowerId);

    /**
     * 通过id获取对应的可用的rfid 的值
     * @param id
     * @return
     */
    Object getArfid(String id);
}
