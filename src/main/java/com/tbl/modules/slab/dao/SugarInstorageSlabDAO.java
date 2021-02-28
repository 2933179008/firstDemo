package com.tbl.modules.slab.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;

import java.util.List;
import java.util.Map;

public interface SugarInstorageSlabDAO {
    /**
    * @Description:  获取白糖类型上架单列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/5
    */
    List<PutBill> getPagePutBillList(Page<PutBill> pagePutBill, Map<String, Object> params);
}
