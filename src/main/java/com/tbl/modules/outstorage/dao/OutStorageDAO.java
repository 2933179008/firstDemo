package com.tbl.modules.outstorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OutStorageDAO extends BaseMapper<OutStorageManagerVO>{

    /**
     * 列表分页展示
     * @param page
     * @param map
     * @return
     */
    List<OutStorageManagerVO> selectOutStorageList(Pagination page, Map<String,Object> map);

    /**
     * 获取当前系统最大的单据的
     * @return
     */
    String getMaxOutstorageCode();


    /**
     * 获取对应的货主的下拉列表
     * @return
     */
    List<Map<String,Object>> getCustomerList();


    /**
     * 获取对应的库位的下拉框
     * @return
     */
    List<Map<String,Object>> getAreaList();

    /**
     * 获取对应的备料单的下拉框
     * @return
     */
    List<Map<String,Object>> getSpareList();

    /**
     * 收货单下拉框展示
     * @return
     */
    List<Map<String,Object>> getReceipList();

    /**
     * 入库单单下拉框展示
     * @return
     */
    List<Map<String,Object>> getInstorageList();

    /**
     * 判断出库单是否已经生成了下架单
     * @param ids
     * @return
     */
    Integer isExist(String ids);

    Integer outStorageStatusCount(String spareBillCode);

    /**
     * 审核
     * @param id
     * @return
     */
    Boolean auditing(@Param("id")String id,@Param("state")String state);


    /**
     * 通过设备IP查询对应的产线的IP
     * @param deviceIp
     * @return
     */
    Object getLineIP(String deviceIp);

    /**
     * 通过rfid获取对应的出库单的ID
     * @param rfid
     * @return
     */
    Object outStorageId(String rfid);

    /**
     * 通过rfid获取当前系统中的下架单的Id
     * @param rfid
     * @return
     */
    Object getLowerId(String rfid);

    /**
     * 通过出库单的ID获取对应的备料单中的产线的信息
     * @param outStorageId
     * @return
     */
    Map<String,Object> getRfidIp(Object outStorageId);


    /**
     * 删除出库单的详情
     * @param map
     */
    Object deleteMap(Map<String,Object> map);


}
