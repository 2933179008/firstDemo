package com.tbl.modules.outstorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;

import java.util.List;
import java.util.Map;

public interface SpareBillDAO extends BaseMapper<SpareBillManagerVO> {

    List<SpareBillManagerVO> selectSpareBillList(Pagination pagination, Map<String, Object> map);

    Integer isExist(String ids);

    String getMaxOutstorageCode();

    List<SpareBillManagerVO> getAllList(List<Long> ids);
}
