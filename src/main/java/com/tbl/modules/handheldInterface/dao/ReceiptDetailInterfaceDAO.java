package com.tbl.modules.handheldInterface.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收货接口DAO
 * @author yuany
 * @date 2019-02-18
 */
public interface ReceiptDetailInterfaceDAO extends BaseMapper<ReceiptDetail> {

    Integer insertInstorageDetail(@Param("detailList") List<Map<String, Object>> detailList);

    Integer updateSeparableAmountAndWeight(@Param("detailList") List<Map<String, Object>> detailList);

}
