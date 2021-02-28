package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;

import java.util.List;
import java.util.Map;

public interface LowerShelfService extends IService<LowerShelfBillVO> {

    /**
     * 通过出库单ID获取对应的下架单的数量
     * @param id
     * @return
     */
    Integer getDtailCount(String id);

    /**
     * 获取列表页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过下架单的ID获取对应的下架单的数值
     * @param id
     * @return
     */
    LowerShelfBillVO getLowerShelfVO(String id);

    /**
     * 查询系统中的操作人
     * @return
     */
    List<Map<String,Object>> getUserList();

    /**
     * 更新操作人
     * @param lowerId
     * @param userId
     * @return
     */
    Map<String,Object> updateOperation(String lowerId,String userId);

    /**
     *  修改下架单的状态
     * @param id
     * @param state
     * @return
     */
    Boolean updateLoserState(String id,String state);

    /**
     * 通过下架单的ID删除对应的下架单的信息
     * @param lowerId
     * @return
     */
    Map<String,Object> delLower(String lowerId);

}
