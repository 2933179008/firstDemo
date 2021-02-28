package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;

import java.util.List;
import java.util.Map;

/**
 * 物料绑定RFID详情接口Service
 */
public interface MaterielBindRfidDetailInterfaceService extends IService<MaterielBindRfidDetail> {

    /**
     * 获取白糖绑定rfid未绑定数量重量的详情
     */
    List<MaterielBindRfidDetail> getMaterielBindRfidDetail(String rfid);


    /**
     * RFID 查询未入库的详情
     */
    Map<String,Object> selectRfidDetail(String rfid);

    /**
     * RFID 查询未入库的详情
     */
    Map<String,Object> deleteRfidDetail(Long id);
}
