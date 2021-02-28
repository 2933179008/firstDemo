package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;

import java.util.List;
import java.util.Map;

/**
 * 物料绑定RFID详情管理Service
 *
 * @author yuany
 * @date 2019-01-11
 */
public interface MaterielBindRfidDetailService extends IService<MaterielBindRfidDetail> {
    /**
     * 获取物料绑定RFID详情管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-11
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 更改物料详情物料数量
     *
     * @param amount
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateAmount(String amount, Long materielBindRfidDetailBy);

    /**
     * 更改物料详情物料重量
     *
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateWeight(String weight, Long materielBindRfidDetailBy);

    /**
     * 更改物料详情物料数量/重量
     *
     * @param amount
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateSurplus(String amount,String weight, Long materielBindRfidDetailBy);

    /**
     * @Description: 保存物料详情
     * @return:
     * @Author: yuany
     * @Date: 2019-01-13
     */
    boolean saveReceiptDetail(Long materielBindRfidBy, String materielCodes);

    /**
     * @Description: 删除收货单详情（物料详情）
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-14
     */
    boolean deleteMaterielBindRfidDetail(String ids);

    /**
     * 根据materielCode查询materielBindRfidBy
     */
    List<Long> getMaterielBindRfidByList(String materielCode, String materielName);

    /**
     * 获取合并/拆分条件分页查询
     */
    PageUtils queryMaterielBindRfidDetailPage(Map<String, Object> parms);

    /**
     * 根据ID清空数据中number mlweight的值
     *
     * @param id
     * @return
     */
    Integer updateNumWeiById(Long id);

    /**
     * 根据ID逻辑删除物料绑定详情
     *
     * @param deleteBy
     * @param id
     * @return
     */
    Integer updateDeleteFlag(Long deleteBy, Long id);

    /**
     * 白糖绑定更新数量
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param amount
     * @return
     */
    Map<String,Object> sugarBindAmount(String rfid, String materialCode,String amount);

    /**
     * 白糖绑定更新重量数量
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param weight
     * @return
     */
    Map<String,Object> sugarBind(String rfid, String materialCode,String amount,String weight,Long userId);

    List<Map<String, Object>> getMaterialBundingDetail(String rfid);

    /**
     * 获取物料绑定RFID详情管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-11
     */
    PageUtils getMaterielBySort(Map<String, Object> parms);

}
