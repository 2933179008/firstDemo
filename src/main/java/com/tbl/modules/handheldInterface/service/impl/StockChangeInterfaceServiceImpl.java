package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.handheldInterface.dao.StockChangeInterfaceDAO;
import com.tbl.modules.handheldInterface.service.StockChangeInterfaceService;
import com.tbl.modules.stock.entity.StockChange;
import org.springframework.stereotype.Service;

/**
 * 库存变动接口Service实现
 */
@Service(value = "stockChangeInterfaceService")
public class StockChangeInterfaceServiceImpl extends ServiceImpl<StockChangeInterfaceDAO, StockChange> implements StockChangeInterfaceService {
}
