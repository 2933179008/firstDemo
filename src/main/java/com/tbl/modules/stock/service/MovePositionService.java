package com.tbl.modules.stock.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.stock.entity.MovePosition;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 移位管理Service
 *
 * @author yuany
 * @date 2019-01-21
 */
public interface MovePositionService extends IService<MovePosition> {

    /**
     * 获取移位管理分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-21
     */
    PageUtils queryPage(Map<String, Object> parms);


    /**
     * 删除移位（逻辑删除）
     *
     * @param ids:要删除的ids
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2019-01-22
     */
    boolean delMovePosition(String ids, Long userId);

    /**
     * 开始移位/移位完成
     *
     * @author yuany
     * @date 2019-01-22
     */
    boolean statusMovePosition(String ids, String time, Integer sign, Long userId);


    /**
     * 获取导出列
     *
     * @return List<Materiel>
     * @author yuany
     * @date 2019-01-23
     */
    List<MovePosition> getAllLists(String ids);


    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author yuany
     * @date 2019-01-23
     */
    void toExcel(HttpServletResponse response, String path, List<MovePosition> list);

    /**
     * @Description:  判断是否是做过绑定的白糖类型的rfid
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/7
     */
    boolean isBundingSugar(String rfid);

    /**
     * @Description:  判断库位是否可用，即验证所选的库位是否可以放该托盘rfid
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/7
     */
    Map<String,Object> isAvailablePosition(String rfid, String positionCode);

    /**
     * @Description:  rfid移位验证逻辑
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/12
     */
    Map<String,Object> upRfid(String rfid);

    /**
     * @Description:  查询该rfid是否可用
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/11
     */
    Integer getSelectAvailableRfid(String materialCode1, String batchNo1, String formerpositionCode,String rfid);

    /**
     * @Description:  移库完成
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    void moveOver(String movePositionId,String rfid,String positionCode,Long userId);

    /**
     * 移库添加/修改
     *
     * @param movePosition
     * @return
     */
    Map<String,Object> addMovePosition(MovePosition movePosition, Long userId);

    /**
     * 获取移位详细分页列表
     * @param params
     * @return
     * @author pz
     * @date 2019-01-10
     */
    PageUtils queryPageS(Map<String, Object> params,Long movePositionId);

    /**
     * 自动生成移位编码(针对无RFID的移位)
     *
     * @author
     * @date 2019-02-01
     */
    String getMovePositionCode();
}
