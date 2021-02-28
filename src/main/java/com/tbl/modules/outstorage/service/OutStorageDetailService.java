package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;

import java.util.List;
import java.util.Map;

public interface OutStorageDetailService extends IService<OutStorageDetailManagerVO> {

    /**
     * 出库详情列表展示
     * @param map
     * @return
     */
    PageUtils queryPage(Map<String,Object> map);


    /**
     * 物料的下拉列表
     * @param queryString
     * @param pageSize
     * @param pageNo
     * @return
     */
    List<Map<String,Object>> getSelectMaterialList(String positionCode, String customerCode,List<String> materialCodes,String billType,String queryString, int pageSize, int pageNo);

    /**
     * 下拉列表的总数
     * @param queryString
     * @return
     */
    Integer getSelectMaterialTotal(String queryString);

    /**
     * 出库详情的物料的添加
     * @param outStorageId
     * @param materialContent
     * @return
     */
    Map<String,Object> addOutStorageDetail(String outStorageId,String materialContent);

    /**
     * 单据提交
     * @return
     */
    List<OutStorageDetailManagerVO> getDetailList(String id);

    /**
     * 提交更新状态
     * @param id
     * @return
     */
    Object updateState(String id);

    /**
     * 更新数量
     * @param id
     * @param amoung
     */
    Map<String,Object> updateDetailAmount(String id,String amoung,String weight);

    /**
     * 生成下架单
     */
    Map<String,Object> addshelve(Long userId,OutStorageManagerVO outStorageManagerVO, String id);

    /**
     * 通过物料编号以及批次号进行库存物料的查询
     * @param materialNo
     * @param batchNo
     * @return
     */
    Object getstockAmount(String materialNo,String batchNo,String materialType);

    /**
     * 根据物料编号以及批次号进行库存物料的锁定
     * @return
     */
    Map<String,Object> getMaterialList(String materialNo,String batchNo,String amount,String weight,String id,String rfidList,Long outStorageDetailId);

    /**
     * 物料绑定拆分
     * @param id
     * @param occupyAmount  占用数量
     * @param materialList  拆分后的绑定详情
     * @return
     */
    Map<String,Object> stockBindSpilt(Long userId,String materialNo,String batchNo,String id,String occupyAmount,String occupyWight,List<Map<String,Object>> materialList);


    /**
     * 查询库存中的可用的RFID编号
     * @param materialNo
     * @param batchNo
     * @return
     */
    String getRfidList(String batchNo,String materialNo);

    /**
     * 根据出货单id删除出货详情id
     * @param outStorageId
     */
    void deleteByOutStorageId(Long outStorageId);
}
