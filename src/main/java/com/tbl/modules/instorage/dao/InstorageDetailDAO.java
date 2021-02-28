package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.InstorageDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InstorageDetailDAO extends BaseMapper<InstorageDetail> {

    /**
    * @Description:  获取物料下拉列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    List<Map<String, Object>> getSelectMaterialList(Page page,@Param("outstorageBillId") Long outstorageBillId,@Param("queryString") String queryString);

    /**
    * @Description:  获取物料下拉列表数据总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer getSelectMaterialTotal(@Param("outstorageBillId") Long outstorageBillId, @Param("queryString") String queryString);

    /**
    * @Description:  根据入库单主键和物料编码获取物料数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer getMaterialCount(Map<String, Object> map);
    /**
    * @Description:  更新入库单详情的批次号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer updateBatchNo(@Param("instorageDetailId") Long instorageDetailId,@Param("batchNo") String batchNo);
    /**
    * @Description:  更新入库单详情的生产日期
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    Integer updateProductDate(@Param("instorageDetailId") Long instorageDetailId,@Param("productDate") String productDate);
    /**
    * @Description:  更新入库单详情的入库数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer updateInstorageAmount(@Param("instorageDetailId") Long instorageDetailId,@Param("instorageAmount") String instorageAmount);
    /**
    * @Description:  更新入库单详情的入库重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    Integer updateInstorageWeight(@Param("instorageDetailId") Long instorageDetailId,@Param("instorageWeight") String instorageWeight);
    /**
    * @Description:  插入上架单详情数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    void insertPutBillDetail(@Param("lstInstorageDetail") List<InstorageDetail> lstInstorageDetail,@Param("putBillId") Long putBillId);
    /**
    * @Description:  查询入库单详情物料种类
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/22
    */
    List<Map<String,Object>> selectMaterialType(@Param("instorageId") Long instorageId);
    /**
    * @Description:  获取出库单详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/25
    */
    Map<String, Object> getOutstorageBillDetail(@Param("outstorageBillId") Long outstorageBillId,@Param("materialCode") String materialCode,@Param("batchNo") String batchNo);
}
