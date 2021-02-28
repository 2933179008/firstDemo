package com.tbl.modules.slab.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;

import java.util.Map;

public interface OutStorageSlabService {

    /**
     * 获取出库的列表
     * @param map
     * @return
     */
    Page<LowerShelfBillVO> queryPage(Map<String,Object> map);

    /**
     * 更新当前操作
     * @param userIp
     */
    void updateType(String userIp);

    /**
     * 更新绑定的信息
     * @param lowerId
     */
    void insertOrUpdateSlabOutBillBunding(String lowerId,String userIp);

    /**
     * 更新下架单的状态
     * @param lowerId
     */
    void updateLowerBillState(String lowerId);


    /**
     * 叉车查到rfid 时调用的方法
     * @param userIp
     * @param rfid
     * @return
     */
    Map<String,Object> slabDownRfid(String userIp,String rfid);

    /**
     *  叉车放下料箱时调用的方法
     * @param userIp
     * @return
     */
    Object slabDrop(String userIp);

    /**
     * 通过用户(叉车IP)获取对应的当前存放的rfid 的信息
     * @param userIp
     * @return
     */
    Map<String,Object> alivableRfid(String userIp);

    /**
     * 清除绑定关系中的rfid
     * @param ip
     */
    void updateRfid(String ip);

    /**
     * 获取下架单的ID
     * @param map
     * @return
     */
    Object getLowerId(Map<String,Object> map);

    /**
     * 获取对应的task 的任务的集合
     * @param userIp
     * @return
     */
    Map<String,Object> getTaskMap(String userIp);

    /**
     * 通过下架单ID以及rfid获取对应的物料的基础信息
     * @param rfid
     * @param lowerId
     * @return
     */
    Map<String,Object> getMaterialMap(String rfid,String lowerId);

    /**
     * 通过下架单以及rfid将当前系统的可用的rfid进行更新
     * @param lowerId
     * @param rfid
     * @return
     */
    Map<String,Object> confirmState(String lowerId,String rfid,String rfid1);

    /**
     * 通过id获取对应的可用的rfid的值
     * @param id
     * @return
     */
    Object getArfid(String id);
}
