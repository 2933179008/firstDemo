package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.instorage.entity.Instorage;

/**
 * 入库接口DAO
 * @author yuany
 * @date 2019-02-18
 */
public interface InstorageInterfaceDAO extends BaseMapper<Instorage> {

    String getMaxInstorageCode();
}
