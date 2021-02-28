package com.tbl.modules.slab.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.visualization.entity.StockCar;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface InstorageSlabService {
    /**
    * @Description:  插入/更新平板参数绑定表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/28
    */
    void insertOrUpdateSlabBillBunding(HttpServletRequest request, Long putBillId);

    Page<PutBill> queryPage(Map<String, Object> map);
    /**
    * @Description:  更新上架单表状态为“待上架”
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/1
    */
    void updatePutBillState(Long putBillId,Long userId,String userName);
    /**
    * @Description:  根据ip获取该叉车绑定表信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/2
    */
    Map<String, Object> getSlabBillBunding( String userIP);
    /**
    * @Description:  根据rfid查询绑定的物料
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/2
    */
    List<Map<String,Object>> getMaterialBundingDetail(String rfid);
    /**
    * @Description:  根据上架单id和物料编号获取上架单对应的入库详情单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/2
    */
    Map<String, Object> getPutBillDetailMap(String putBillId, String materialCode);
    /**
    * @Description:  更新平板参数绑定表的rfid和验证字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/2
    */
    void updateSlabBillBundingRfid(Map<String, Object> paramMap);

    /**
    * @Description:  获取库位下拉框列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/4
    */
    List<Map<String, Object>> getSelectPositionList(String areaId,Double xSize,Double x_Size,Double ySize,Double y_Size,String queryString);
    /**
    * @Description:  获取库位下拉框列表总条数
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/4
    */
    Integer getSelectPositionTotal(String areaId,Double xSize,Double x_Size,Double ySize,Double y_Size,String queryString);
    /**
    * @Description:  更新平板参数绑定表的库位编号
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/4
    */
    void updateSlabBillBundingPositionCode(String userIP,String positionCode);

    /**
    * @Description:  叉车叉起托盘触发光电调用接口的上架操作验证信息
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    Map<String,Object> slabUpRfid(String IP,String rfid);

    /**
     * @Description:  叉车叉放下托盘触发光电调用接口的上架操作
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/5
     */
    void slabDownRfid(String userIP);
    /**
    * @Description:
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    Map<String, Object> getRecommendPosition(String area,HttpServletRequest request);
    /**
    * @Description:  判断库位是否可用，即验证所选的库位是否可以放该托盘rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/15
    */
    Map<String, Object> isAvailablePosition(String userIP, String positionCode);
    /**
    * @Description:  更新平板入库操作参数绑定关系表的字段
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/28
    */
    void updateAlert(HttpServletRequest request,String flag);

    Map<String, Object> getExecuteRfid(String userIP);
    /**
    * @Description:  更新slab_bill_bunding表的alert_key字段为3（已确定库位）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/29
    */
    void updateAlertToUp(HttpServletRequest request);
    /**
    * @Description:  获取小车位置
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/16
    */
    List<StockCar> getCarPosition(String areaId, String forkliftTag);
    /**
    * @Description:  userip获取小车的tag
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/18
    */
    String getForkliftSelected(String userIp);

    /**
     * 更新RFID坐标数据，并作库位位置排序
     *
     * @param userIP
     * @param positionCode
     * @param rfidArr
     * @author yuany
     * @date 2019-06-11
     */
    void updateMaterielBindRfidShort(String userIP, String positionCode, String[] rfidArr);
}
