package com.tbl.modules.external.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ErpInterfaceServiceDAO {
    /**
     * @Description:  查询满足调用采购入库单接口条件的入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/20
     */
    List<Map<String,Object>> selectConfessInstorageBill();
    /**
     * @Description:  查询满足调用采购入库单接口条件的入库单(越库入库)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/20
     */
    List<Map<String,Object>> selectCrossInstorageBill();
    /**
     * @Description:  根据入库单id查询上架单详情
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/20
     */
    List<Map<String, Object>> selectPutBillDetail(@Param("instorageBillId") String instorageBillId);

    /**
     * @Description:  更新入库表erp_flag字段
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/25
     */
    void updateInstorageBillErpFlag(List<Map<String, Object>> list);
    /**
     * @Description:  查询满足调用委托加工入库单接口条件的入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/25
     */
    List<Map<String, Object>> selectEntrustInstorageBill();
    /**
     * @Description:  查询满足调用委托加工入库单接口条件的入库单(越库入库)
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/3/25
     */
    List<Map<String, Object>> selectCrossEntrustInstorageBill();
    /**
     * @Description:  查询满足调用生产退货入库单(自采料)接口条件的入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/26
     */
    List<Map<String, Object>> selectConfessReturnInstorageBill(@Param("materialType") String materialType);

}
