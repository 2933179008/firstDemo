package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.instorage.entity.QualityBillDetail;

import java.util.Map;

public interface QualityBillDetailService extends IService<QualityBillDetail> {
    /**
     * @Description:  保存物料详情
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    void saveQualityBillDetail(Long qualityId, String materialCodes);

    /**
     * @Description:  获取质检单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    Page<QualityBillDetail> queryPage(Map<String, Object> map);

    /**
     * @Description:  删除质检单详情（物料详情）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    boolean deleteQualityBillDetail(String ids);
    /**
     * @Description:  判断物料是否已添加
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/21
     */
    boolean hasMaterial(Long qualityId, String materialCodes);
    /**
    * @Description:  更新质检单的样本重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/13
    */
    void updateSampleWeight(Long qualityBillDetailId, String sampleWeight);
    /**
    * @Description:  更新质检单的合格重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/13
    */
    void updateQualifiedWeight(Long qualityBillDetailId, String qualifiedWeight);
    /**
    * @Description:  更新质检单详情的合格状态和合格百分比
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/14
    */
    boolean updateDetailState(String id, String state, Double rate);
}
