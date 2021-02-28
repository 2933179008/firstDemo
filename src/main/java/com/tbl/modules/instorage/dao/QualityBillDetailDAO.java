package com.tbl.modules.instorage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.QualityBillDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QualityBillDetailDAO extends BaseMapper<QualityBillDetail> {

    /**
     * @Description:  获取质检单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/31
     */
    List<QualityBillDetail> getPageQualityBillDetailList(Page<QualityBillDetail> pageQualityBillDetail, Map<String, Object> params);

    /**
     * @Description:  根据质检单主键和物料编码获取物料数量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/21
     */
    Integer getMaterialCount(Map<String, Object> map);
    /**
    * @Description:  更新质检单的样本重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/13
    */
    Integer updateSampleWeight(@Param("qualityBillDetailId") Long qualityBillDetailId,@Param("sampleWeight") String sampleWeight);
    /**
    * @Description:  更新质检单的合格重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/13
    */
    Integer updateQualifiedWeight(@Param("qualityBillDetailId") Long qualityBillDetailId,@Param("qualifiedWeight") String qualifiedWeight);
}
