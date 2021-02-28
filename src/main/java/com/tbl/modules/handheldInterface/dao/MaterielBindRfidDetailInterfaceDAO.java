package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料绑定RFID详情接口DAO
 */
public interface MaterielBindRfidDetailInterfaceDAO extends BaseMapper<MaterielBindRfidDetail> {

    /**
     * 获取物料绑定详情集合
     */
    List<MaterielBindRfidDetail> queryMaterielBindRfidDetail(@Param("rfid") String rfid, @Param("barcode") String barcode, @Param("positionBy") String positionBy, @Param("batchNo") String batchNo);

    /**
     * 获取白糖绑定rfid未绑定数量重量的详情
     */
    List<MaterielBindRfidDetail> getMaterielBindRfidDetail(@Param("rfid") String rfid);
}
