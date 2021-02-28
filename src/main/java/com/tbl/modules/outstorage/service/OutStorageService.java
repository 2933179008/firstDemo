package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;

import java.util.List;
import java.util.Map;


public interface OutStorageService extends IService<OutStorageManagerVO> {

    /**
     * 获取分列页
     * @param params
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取当前库中最大的出库单编号
     * @return
     */
    String getMaxBillCode();

    /**
     * 获取货主的下拉列表
     * @return
     */
    List<Map<String,Object>> getCustomerList();

    /**
     * 获取库位的下拉列表
     * @return
     */
    List<Map<String,Object>> getAreaList();

    /**
     * 获取备料单的下拉信息
     * @return
     */
    List<Map<String,Object>> getSpareList();

    /**
     * 获取收货单的下拉单信息
     * @return
     */
    List<Map<String,Object>> getReceipList();

    /**
     * 获取入库单的下拉单信息
     * @return
     */
    List<Map<String,Object>> getInstorageList();

    /**
     * 出库单的添加或者编辑
     * @param outStorageManagerVO
     * @return
     */
    Map<String,Object> saveOutstorage(OutStorageManagerVO outStorageManagerVO);

    /**
     * 出库单删除
     * @return
     */
    Map<String,Object> delOutStorage(String ids);

    /**
     * 审核
     * @param id
     * @return
     */
    Map<String,Object> auditing(String id,String state);

    /**
     * 获取产线的IP
     * @param deviceIp
     * @return
     */
    Object getLineIp(String deviceIp);

    /**
     * 通过rfid获取对应的产线
     * @param rfid
     * @return
     */
    Map<String,Object> getRfidIp(String rfid);


}
