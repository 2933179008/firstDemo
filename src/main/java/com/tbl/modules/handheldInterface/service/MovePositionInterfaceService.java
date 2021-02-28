package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.MovePosition;

import java.util.Map;

/**
 * 移库接口Service
 */
public interface MovePositionInterfaceService extends IService<MovePosition> {

    /**
     * 开始移位/移位完成
     *
     * @author yuany
     * @date 2019-01-22
     */
    boolean statusMovePosition(Long id,Long userId);

    /**
     * @Description:  判断是否是做过绑定的白糖类型的rfid
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/7
     */
    boolean isBundingSugar(String rfid);

    /**
     * @Description:  判断库位是否可用，即验证所选的库位是否可以放该托盘rfid
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/7
     */
    Map<String,Object> isAvailablePosition(String rfid, String positionCode);

    /**
     * @Description:  rfid移位验证逻辑
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    Map<String,Object> upRfid(String rfid);

    /**
     * @Description:  查询该rfid是否可用
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/11
     */
    Integer getSelectAvailableRfid(String materialCode1, String batchNo1, String formerpositionCode,String rfid);

    /**
     * @Description:  移库完成
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    void moveOver(String movePositionId,String rfid,String positionCode,Long userId);

    /**
     * 未完成的移库单
     *
     * @param moveUserId
     * @return
     */
    Map<String, Object> getUnfinishedMovePosition(Long moveUserId);

    /**
     * 移库
     *
     * @param rfid
     * @param positionCode
     * @param userId
     * @return
     */
    Map<String, Object> doMovePosition(String rfid, String positionCode, Long userId);

//    /**
//     * 移库单提交
//     *
//     * @param movePositionId
//     * @param userId
//     * @return
//     */
//    Map<String, Object> completeMovePosition(Long movePositionId, Long userId);

}
