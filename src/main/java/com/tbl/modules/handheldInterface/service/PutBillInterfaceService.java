package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.PutBill;

import java.util.List;
import java.util.Map;

/**
 * 上架接口Service
 *
 * @author yuany
 * @date 2019-02-15
 */
public interface PutBillInterfaceService extends IService<PutBill> {

    /**
     * 根据条件获取商家单
     *
     * @param userId
     * @return
     * @author yuany
     * @date 2019-02-15
     */
    List<PutBill> getPutBillList(Long userId);

    /**
     * @Description: 更新上架单的提交状态
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/31
     */
    boolean updatePutBillState(Long putBillId);


    /**
     * 根据登陆ID获取上架单
     *
     * @param userId
     * @return
     */
    Map<String, Object> getPutBill(Long userId);

    /**
     * 上架RFID验证接口
     *
     * @param rfid
     * @return
     */
    Map<String, Object> getProvingRfid(String instorageProcess, String rfid);


    /**
     * 上架详情参数更新
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> completePutBill(Map<String, Object> paramMap);

    /**
     * 上架完成
     * @param putBillId
     * @return
     */
    Map<String, Object> changePutBillState(Long putBillId,Long userId);

    /**
     * 上架单提交
     *
     * @param putBillId
     * @return
     */
    Map<String, Object> submitPutBill(Long putBillId);
}
