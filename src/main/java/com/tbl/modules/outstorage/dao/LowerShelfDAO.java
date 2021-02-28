package com.tbl.modules.outstorage.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LowerShelfDAO extends BaseMapper<LowerShelfBillVO>{

    /**
     * 根据出库单的ID获取下架单的数量
     * @param id
     * @return
     */
    Integer getDtailCount(@Param("id")String id);

    /**
     * 分页列表
     * @param page
     * @param map
     * @return
     */
    List<LowerShelfBillVO> selectLowerShelfList(Pagination page, Map<String,Object> map);


    /**
     * 通过下架单的ID获取对应的下架单的数值
     * @param id
     * @return
     */
    LowerShelfBillVO getLowerShelfVO(@Param("id")String id);

    /**
     * 通过下架单的Id将对应的slab表中的数据进行更新
     * @param lowerId
     * @return
     */
    Object updateOutStorageSlab(@Param("rfid") String rfid,@Param("lowerId") String lowerId);

    Integer outStorageCount(@Param("outStorageId")String outStorageId);

    /**
     * 查询系统中的操作员
     * @return
     */
    List<Map<String,Object>> getUserList();

    /**
     * 更新操作人
     * @param lowerId
     * @param userId
     * @return
     */
    Boolean updateOperation(String lowerId,String userId);


    /**
     * 通过userId获取对应的
     * @param userId
     * @return
     */
    List<Map<String,Object>> lowerInterfaceList(String userId);


    /**
     * 更新出库单的状态
     * @param outStorageId
     * @return
     */
    Object updateOutStorageState(String outStorageId);
}
