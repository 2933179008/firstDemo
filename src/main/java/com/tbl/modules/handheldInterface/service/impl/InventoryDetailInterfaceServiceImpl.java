package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.handheldInterface.service.InventoryDetailInterfaceService;
import com.tbl.modules.stock.dao.InventoryDetailDAO;
import com.tbl.modules.stock.entity.InventoryDetail;
import org.springframework.stereotype.Service;

@Service("inventoryDetailInterfaceService")
public class InventoryDetailInterfaceServiceImpl extends ServiceImpl<InventoryDetailDAO, InventoryDetail> implements InventoryDetailInterfaceService {
}
