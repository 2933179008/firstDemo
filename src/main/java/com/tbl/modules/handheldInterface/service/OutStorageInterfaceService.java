package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;

import java.util.List;
import java.util.Map;

public interface OutStorageInterfaceService extends IService<LowerShelfBillVO> {

    /**
     * 通过ID获取分配的下架任务
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> lowerInterfaceList(String userId);

    /**
     * 通过下架单的ID获取对应的下架单的详情
     *
     * @param detailId
     * @return
     */
    List<Map<String, Object>> lowerInterfaceDetailList(String detailId);

    /**
     * 通过下架单的ID获取正在执行的任务的操作人
     *
     * @param rfid
     * @return
     */
    String getLowerUserId(String rfid);

    /**
     * 判断下架RFID中物料是否符合下架详情中物料
     */
    Map<String, Object> determineRfid(String Rfid, String lowerId);

    /**
     * 合并出库
     *
     * @author yuany
     * @date 2019-06-09
     */
    Map<String, Object> mergeOutStorage(Map<String, Object> paramMap);

    /**
     * 获取关于RFID的下架单详情
     *
     * @param oldRfid
     * @param lowerId
     * @return
     * @author yuany
     * @date 2019-07-10
     */
    Map<String, Object> getLowerDetail(String oldRfid, String lowerId);
}
