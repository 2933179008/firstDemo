package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.InstorageDetail;

import java.util.List;
import java.util.Map;

public interface InstorageDetailService extends IService<InstorageDetail> {
    /**
    * @Description:  获取入库单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    PageUtils queryPage(Map<String, Object> map);
    /**
    * @Description:  获取物料下拉列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    List<Map<String, Object>> getSelectMaterialList(Long outstorageBillId,String queryString, int pageSize, int pageNo);
    /**
    * @Description:  获取物料下拉列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer getSelectMaterialTotal(Long outstorageBillId,String queryString);
    /**
    * @Description:  判断物料是否已添加
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean hasMaterial(Long instorageId, String materialCodes);
    /**
    * @Description:  保存物料详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean saveInstorageDetail(Long instorageId, String materialCodes);
    /**
    * @Description:  删除入库单详情（物料详情）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean deleteInstorageDetail(String ids);
    /**
    * @Description:  根据入库单详情id获取入库单状态
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    String getInstorageStateByDetailId(Long instorageDetailId);
    /**
    * @Description:  更新入库单详情的批次号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean updateBatchNo(Long instorageDetailId, String batchNo);
    /**
    * @Description:  更新入库单详情的生产日期
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    boolean updateProductDate(Long instorageDetailId, String productDate);
    /**
    * @Description:  更新入库单详情的入库数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean updateInstorageAmount(Long instorageDetailId, String instorageAmount);
    /**
    * @Description:  更新入库单详情的入库重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    boolean updateInstorageWeight(Long instorageDetailId, String instorageWeight);
    /**
    * @Description:  查询入库单详情物料种类是否只有一种
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/22
    */
    boolean selectMaterialType(Long instorageId);
}
