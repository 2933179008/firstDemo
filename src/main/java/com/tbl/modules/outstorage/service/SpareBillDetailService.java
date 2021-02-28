package com.tbl.modules.outstorage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;

import java.util.List;
import java.util.Map;

public interface SpareBillDetailService extends IService<SpareBillDetailManagerVO> {

    /**
     * @Description:  获取备料单详情列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019/4/8
     */
    PageUtils queryPage(Map<String, Object> parms);


    List<SpareBillDetailManagerVO> spareBillDetailList(String id);

    /**
     * 通过备料单的id获取对应的备料单的详情
     * @param id
     * @param i
     * @return
     */
    List<SpareBillDetailManagerVO> getSpareBillList(String id);


    /**
     * 更新备料单中的仓库的发送数量
     * @param spareBillId
     * @param map
     * @return
     */
    Object updateDetail(String spareBillId,Map<String,Object> map);



}
