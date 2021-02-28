package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;

import java.util.List;
import java.util.Map;


/**
 * 物料绑定RFID管理Service
 *
 * @author yuany
 * @date 2019-01-07
 */
public interface MaterielBindRfidService extends IService<MaterielBindRfid> {

    /**
     * 获取物料绑定RFID管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-07
     */
    PageUtils queryPage(Map<String, Object> parms);

    /**
     * 删除物料实体（逻辑删除）
     *
     * @param ids:要删除的ids
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2019-01-10
     */
    boolean delMaterielBindRfid(String ids, Long userId);

    /**
     * 生成绑定单号
     *
     * @author yuany
     * @date 2019-01-11
     */
    String getBindCode();

    /**
     * 根据rfid获取实体
     * @param rfid
     * @return
     */
    MaterielBindRfid materielBindRfid(String rfid);

}
