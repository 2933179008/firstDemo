package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import org.apache.ibatis.annotations.Param;

/**
 * 物料绑定RFID接口
 */
public interface MaterielBindRfidInterfaceDAO extends BaseMapper<MaterielBindRfid> {

    /**
     * 根据rfid获取实体
     * @param rfid
     */
    MaterielBindRfid materielBindRfid(@Param("rfid") String rfid);
}
