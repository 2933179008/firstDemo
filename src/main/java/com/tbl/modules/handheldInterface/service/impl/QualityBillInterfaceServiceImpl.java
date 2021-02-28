package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.handheldInterface.dao.QualityBillInterfaceDAO;
import com.tbl.modules.handheldInterface.service.QualityBillInterfaceService;
import com.tbl.modules.instorage.entity.QualityBill;
import org.springframework.stereotype.Service;

@Service(value = "qualityBillInterfaceService")
public class QualityBillInterfaceServiceImpl extends ServiceImpl<QualityBillInterfaceDAO, QualityBill> implements QualityBillInterfaceService {
}
