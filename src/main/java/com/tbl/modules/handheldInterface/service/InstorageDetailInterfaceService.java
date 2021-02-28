package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.InstorageDetail;

/**
 * 入库详情接口Service
 */
public interface InstorageDetailInterfaceService extends IService<InstorageDetail> {

    /**
     * @Description:  确认完成上架
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/31
     */
    void completePutBill(Long userId,String putBillDetailId);

}
