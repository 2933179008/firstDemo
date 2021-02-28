package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.BomDetail;

import java.util.Map;

public interface BomDetailService extends IService<BomDetail> {

    PageUtils queryPage(Map<String, Object> map);
}
