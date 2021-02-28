package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import org.apache.ibatis.annotations.Param;

/**
 * 物料绑定RFID管理Dao
 *
 * @author yuany
 * @date 2019-01-07
 */
public interface MaterielBindRfidDAO extends BaseMapper<MaterielBindRfid> {

    MaterielBindRfid materielBindRfid(@Param("rfid") String rfid);

}
