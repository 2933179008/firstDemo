package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;

import java.util.List;
import java.util.Map;

public interface LowerShelfDetailService extends IService<LowerShelfBillDetailVO> {

    /**
     * 下架单详情
     * @param map
     * @return
     */
    PageUtils queryPage(Map<String,Object> map);

    /**
     * 通过下架单详情ID获取对应的出库单ID
     * @param lowerDetailId
     * @return
     */
    Object getOutStorageId(String lowerDetailId);

    /**
     * 判断对应的任务是否全部完成
     * @param lowerDetailId
     * @return
     */
    Integer lowerCount(String lowerDetailId);

    /**
     * 判断对应的出库单是否全部完成
     * @param outStorageId
     * @return
     */
    Integer outStorageCount(String outStorageId);

    /**
     * 通过出库单ID获取出库详情
     * @param outStorageId
     * @return
     */
    List<OutStorageDetailManagerVO> getDetailList(String outStorageId);

    /**
     * 下架确认
     * @param lowerShelfBillDetailVO
     * @param lowerDetailId
     * @return
     */
    Map<String,Object> confirmLowerShelf(LowerShelfBillDetailVO lowerShelfBillDetailVO,String lowerDetailId,Integer type);

    /**
     * 通过rfid获取对应的任务的信息
     * @param rfid
     * @return
     */
    Map<String,Object> lowerDetail(String rfid);

    void updateOutStorageDetail(Object outStorageId);

    Object getmaterialAmount(String outStorageDetailId,String materialCode,String batchNo);

    Object getmaterialWeight(String outStorageDetailId,String materialCode,String batchNo);

    void updateDetail(Object materialAmount,Object materialWeight,Long outStorageDetailId);
}
