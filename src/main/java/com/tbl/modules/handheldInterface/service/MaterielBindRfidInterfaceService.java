package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.MaterielBindRfid;

import java.util.Map;

/**
 * 物料绑定RFID接口Service
 */
public interface MaterielBindRfidInterfaceService extends IService<MaterielBindRfid> {

    /**
     * 根据rfid获取实体
     * @param rfid
     * @return
     */
    MaterielBindRfid materielBindRfid(String rfid);

    /**
     * 生成绑定单号
     *
     * @author yuany
     * @date 2019-01-11
     */
    String getBindCode();

    /**
     * 根据RFID获取物料绑定中库位信息
     *
     * @param rfid
     * @return
     */
    Map<String, Object> getDepotPositionByRfid(String rfid);

    /**
     * 物料绑定RFID
     *
     * @param rfid
     * @param barcode
     * @param amount
     * @param weight
     * @param userId
     * @return
     */
    Map<String, Object> doMaterielBindRfid(String rfid, String barcode, String amount, String weight, Long userId);

    /**
     * 根据RFID获取物料绑定详情
     *
     * @param rfid
     * @return
     */
    Map<String, Object> getMaterielBindRfidDetailByRfid(String rfid);

    /**
     * 判断是否是白糖绑定
     *
     * @param rfid
     * @return
     */
    Map<String, Object> judgeSugar(String rfid);

}
