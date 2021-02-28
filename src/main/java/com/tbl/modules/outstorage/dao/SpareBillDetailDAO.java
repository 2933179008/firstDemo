package com.tbl.modules.outstorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpareBillDetailDAO extends BaseMapper<SpareBillDetailManagerVO> {

    /**
     * 获取对应的备料单的详情
     * @param id
     * @return
     */
    List<SpareBillDetailManagerVO> spareBillDetailList(String id);


    List<SpareBillDetailManagerVO> spareDetailList(@Param("id") String id);

    /**
     * 修改备料单中的实际的发货数量
     * @param spareBillId
     * @param weight
     * @param amount
     * @return
     */
    Object updateDetail(@Param("spareBillId") String spareBillId, @Param("weight") String weight,@Param("amount") String amount,@Param("materialCode") String materialCode);
}
