package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.PutBillDetail;

import java.util.Map;

/**
 * 上架详情接口Service
 */
public interface PutBillDetailInterfaceService extends IService<PutBillDetail> {

    /**
     * @Description: 获取推荐库位
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/29
     */
    String getRecommendPosition(String putBilDetailId);

    /**
     * 获取上架单详情 并添加操作人，更新上架单状态
     *
     * @param userId
     * @param putBillId
     * @return
     */
    Map<String, Object> getPutBillDetailList(Long userId, Long putBillId);

    /**
     * 白糖上架
     *
     * @param putBillId
     * @param rfid
     * @param positionId
     * @param userId
     * @return
     */
    Map<String, Object> sugarStorage(Long putBillId, String rfid, Long positionId, Long userId);

    /**
     * 白糖绑定更新重量数量接口
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param weight
     * @return
     */
    Map<String, Object> sugarBind(String rfid, String materialCode, String amount, String weight,Long userId);

    /**
     * 上架详情物料更新参数
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> getPutBillDetail(Map<String, Object> paramMap);
}
