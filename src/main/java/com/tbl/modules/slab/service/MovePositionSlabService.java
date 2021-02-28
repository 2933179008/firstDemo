package com.tbl.modules.slab.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.stock.entity.MovePosition;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MovePositionSlabService {
    /**
    * @Description:  获取平板入库列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Page<MovePosition> queryPage(Map<String, Object> map);
    /**
    * @Description:  插入/更新平板移库操作参数绑定关系表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void insertOrUpdateSlabMovePositionBunding(HttpServletRequest request);
    /**
    * @Description:  更新移库单表状态为“移位中”
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateMovePositionState(Long movePositionId);
    /**
    * @Description:  根据当前ip获取该叉车的验证信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Map<String, Object> getSlabMovePositionBunding(String userIP);
    /**
    * @Description:  获取库位下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo);
    /**
    * @Description:  获取库位下拉框列表总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    Integer getSelectPositionTotal(String queryString);
    /**
    * @Description:  更新平板移库操作参数绑定关系表的库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateSlabMovePositionBundingPositionCode(Long movePositionId ,String userIP, String positionCode);
    /**
    * @Description:  更新平板参数绑定表的rfid和验证字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/6
    */
    void updateSlabMovePositionBundingRfid(Map<String, Object> paramMap);
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
    Map<String,Object> isAvailablePosition(String userIP, String positionCode);
    /**
    * @Description:  根据移库表的id查询原库位主键id、库位编号和需要移库的rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/9
    */
    Map<String, Object> selectFormerPosition(String movePositionId);
    /**
    * @Description:  查询该rfid是否可用
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/11
    */
    Integer getSelectAvailableRfid(String materialCode1, String batchNo1, String formerpositionCode,String rfid);

    /**
    * @Description:  叉车叉起托盘触发光电调用接口的移位验证逻辑
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    Map<String,Object> slabUpRfid(String IP,String rfid);
    /**
     * @Description:  叉车放下托盘触发光电调用移库逻辑
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/6
     */
    void slabDownRfid(String ip);
    /**
    * @Description:  获取验证通过的正在操作的rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    Map<String, Object> getExecuteRfid(String userIP);
    /**
    * @Description:  根据rfid查询库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    String selectPositionCodeByRfid(String rfid);
    /**
    * @Description:  更新平板移库操作参数绑定关系表的字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    void updateAlert(HttpServletRequest request, String flag);
    /**
    * @Description:  创建移库单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/2
    */
    Long insertMovePosition(Long userId,String userIP, Long positionId);
}
