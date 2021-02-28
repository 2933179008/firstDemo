package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface SpareBillService extends IService<SpareBillManagerVO> {

    /**
     * 获取列表分列页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String,Object> params);

    /**
     * 通过Id删除对应的备料单
     * @param ids
     * @return
     */
    Map<String,Object> deleteSpareList(String ids,String spareBillCode);

    /**
     * 生成出库单
     * @param spareBillManagerVO
     * @param list
     * @return
     */
    Map<String,Object> createOutStorage(SpareBillManagerVO spareBillManagerVO, List<SpareBillDetailManagerVO> list,String outStorageCode);

    /**
     * 备料单主表的保存
     * @param spareBillJsonString
     * @return
     */
    Map<String,Object> saveSpareBill(String spareBillJsonString,String json);


    /**
     * 通过ID获取对应的实体的主表的信息
     * @param id
     * @return
     */
    SpareBillManagerVO getSpareBillById(String id);


    /**
     * 备料单审核
     * @param ids
     * @param type
     * @return
     */
    Map<String,Object> check(String ids,String type);

    /**
     * 生成出库单详情
     *
     * @param outStorageId
     * @return
     */
    Map<String,Object> toOutStorageDetail(Long outStorageId);

    /**
     * 导出
     */
    void toExcel(HttpServletResponse response, String path, List<SpareBillManagerVO> list);

    List<SpareBillManagerVO> getAllList(String ids);
}
