package com.tbl.modules.stock.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tbl.modules.stock.entity.Stock;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库存Dao
 *
 * @author pz
 * @date 2019-01-08
 */
public interface StockDAO extends BaseMapper<Stock> {

    /**
     * 获取库存列表数据
     *
     * @return
     * @author pz
     * @date 2019-04-09
     */
    List<Stock> selectByList(Pagination page, Map<String, Object> parms);

    /**
     * 获取保质期为空的数据
     *
     * @return
     * @author pz
     * @date 2019-04-09
     */
    List<Stock> selectByListQuality();


    /**
     * 获取库存详细列表数据
     *
     * @return
     * @author pz
     * @date 2019-01-10
     */
    List<Stock> selectListS(Pagination page, Map<String, Object> params, @Param("stockId") Long stockId, @Param("materialCode") String materialCode, @Param("materialName") String materialName);

    /**
     * select查询盘点类型数据字典
     *
     * @author pz
     */
    List<Stock> getSelectPositionList(Pagination page, Map<String, Object> map);

    /**
     * @Description: 更新库存表相关信息
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/1
     */
    Integer updateStockById(@Param("id") Long id, @Param("putAmount") String putAmount, @Param("putWeight") String putWeight, @Param("rfid") String rfid);

    List<Stock> uniqeList(List<String> lstPositionCodes);
}
