package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.instorage.entity.PutBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上架接口
 *
 * @author yuany
 * @date 2019-02-15
 */
public interface PutBillInterfaceDAO extends BaseMapper<PutBill> {

    /**
     * 根据条件获取商家单
     *
     * @author yuany
     * @date 2019-02-15
     * @param userId
     * @return
     */
    List<PutBill> getPutBillList(@Param("userId") Long userId);

    /**
     * @Description:  更新上架单的提交状态
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/31
     */
    Integer updateState(Long putBillId);
}
