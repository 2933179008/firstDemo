package com.tbl.modules.instorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.PutBillDetail;

import java.util.List;
import java.util.Map;

public interface PutBillDetailService extends IService<PutBillDetail> {
    /**
    * @Description:  获取上架单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    Page<PutBillDetail> queryPage(Map<String, Object> map);
    /**
    * @Description:  获取物料下拉列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    List<Map<String, Object>> getSelectMaterialList(Long instorageBillId, String queryString, int pageSize, int pageNo);
    /**
    * @Description:  获取物料下拉列表总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    Integer getSelectMaterialTotal(Long instorageBillId, String queryString);
    /**
    * @Description:  判断物料是否已添加
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    boolean hasMaterial(Long putBillId, String materialCodes);
    /**
    * @Description:  保存物料详情
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    boolean savePutBillDetail(Long putBillId, String materialCodes);

    /**
    * @Description:  删除上架单详情（物料详情）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    boolean deletePutBillDetail(String ids);

    /**
    * @Description:  获取库位下拉框数据源
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    List<Map<String, Object>> getPosition();
    /**
    * @Description:  根据上架单详情id获取收货单状态
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    String getPutBillDetailByDetailId(Long putBillDetailId);
    /**
    * @Description:  更新上架单上架数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    boolean updatePutAmount(Long putBillDetailId, String putAmount);
    /**
    * @Description:  更新上架单的上架重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    boolean updatePutWeight(Long putBillDetailId, String putWeight);
    /**
    * @Description:  更新上架单的rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    boolean updateRfid(Long putBillDetailId, String rfid);
    /**
    * @Description:  获取推荐库位
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/29
    */
    String getRecommendPosition(String putBilDetailId);
    /**
    * @Description:  更新上架单的库位
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    boolean updatePositionCode(Long putBillDetailId, String positionCode);
    /**
    * @Description:  确认完成上架
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    void completePutBill(String putBillDetailId);
    /**
    * @Description:  校验一个rfid不能放在多个库位上
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/14
    */
    Map<String, Object> selectRfidInPosition(Map<String, Object> paramMap);
}
