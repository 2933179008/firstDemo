package com.tbl.modules.visualization.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.visualization.dao.StockCarDAO;
import com.tbl.modules.visualization.entity.StockCar;
import com.tbl.modules.visualization.service.StockCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stockCarService")
public class StockCarServiceImpl extends ServiceImpl<StockCarDAO, StockCar> implements StockCarService {

    @Value("${stockCar.Time}")
    private int CarTime;

    @Autowired
    StockCarDAO stockCarDAO;
    @Override
    public List<StockCar> getCarPosition(String areaCode) {
        //当前时间
        String nowDate = DateUtils.getTime();
        //15秒前的时间
        String formDate = DateUtils.getBeforeSecondTime(CarTime);
        return stockCarDAO.getStockCarPosition(areaCode,formDate,nowDate);
    }

}
