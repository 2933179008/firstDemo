package com.tbl.modules.slab.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;

import java.util.Map;

public interface SugarInstorageSlabService {

    Page<PutBill> queryPage(Map<String, Object> map);
}
