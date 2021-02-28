package com.tbl.modules.stock.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 物料绑定RFID详情管理Dao
 *
 * @author yuany
 * @date 2019-01-11
 */
public interface MaterielBindRfidDetailDAO extends BaseMapper<MaterielBindRfidDetail> {

    /**
     * 更改物料详情物料数量
     *
     * @param amount
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateAmount(@Param("amount") String amount, @Param("materielBindRfidDetailBy") Long materielBindRfidDetailBy);

    /**
     * 更改物料详情物料重量
     *
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateWeight(@Param("weight") String weight, @Param("materielBindRfidDetailBy") Long materielBindRfidDetailBy);

    /**
     * 更改拆分后物料详情物料重量/数量
     *
     * @param amount
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    Integer updateSurplus(@Param("amount")String amount,@Param("weight") String weight, @Param("materielBindRfidDetailBy") Long materielBindRfidDetailBy);

    /**
     * 根据ID清空数据中number的值
     *
     * @param id
     * @return
     */
    Integer updateNumWeiById(@Param("id") Long id);

    /**
     * 根据ID逻辑删除物料绑定详情
     *
     * @param deleteBy
     * @param id
     * @return
     */
    Integer updateDeleteFlag(@Param("deleteBy") Long deleteBy, @Param("id") Long id);

    /**
     * @Description:  根据rfid查询绑定的物料
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/2
     */
    List<Map<String, Object>> getMaterialBundingDetail(@Param("rfid") String rfid);

    /**
     * 获取物料详情绑定列表数据(未入库)
     * @author pz
     * @date 2019-04-11
     * @return
     */
    List<MaterielBindRfidDetail> selectByListN(Pagination page, Map<String, Object> parms);

    /**
     * 获取物料详情绑定列表数据(已入库)
     * @author pz
     * @date 2019-04-11
     * @return
     */
    List<MaterielBindRfidDetail> selectByList(Pagination page, Map<String, Object> parms);

    /**
    * @Description:  库位编、物料编码、RFID可确定唯一库存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/5/5
    */
    Stock getStockInfo(@Param("materialType") String materialType,@Param("positionCode") String positionCode,@Param("materialCode") String materialCode,@Param("rfid") String rfid);

    /**
     * 通过库位获取物料详情并关联排序
     */
    List<MaterielBindRfidDetail> selectListBySort(@Param("positionId") String positionId);

}
