package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.Instorage;

/**
 * 入库接口Service
 *
 * @author yuany
 * @date 2019-02-18
 */
public interface InstorageInterfaceService extends IService<Instorage> {

    /**
     * @Description:  生成入库单编号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/15
     */
    String generateInstorageCode();
}
