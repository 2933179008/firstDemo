package com.tbl.modules.visualization.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.visualization.entity.StockCar;

import java.util.List;


public interface StockCarService extends IService<StockCar> {

    /**
     * 根据库区获取小车坐标
     */
    List<StockCar> getCarPosition(String areaCode);
}
