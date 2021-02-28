package com.tbl.modules.visualization.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.visualization.entity.StockCar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockCarDAO extends BaseMapper<StockCar> {
    /**
     * 获取所有小车信息
     * @param areaCode
     * @return
     */
    List<StockCar> getStockCarPosition(@Param("sceneCode") String areaCode,@Param("formDate") String formDate,@Param("nowDate") String nowDate);

}
