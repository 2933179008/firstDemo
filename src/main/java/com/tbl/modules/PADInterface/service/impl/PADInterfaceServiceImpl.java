package com.tbl.modules.PADInterface.service.impl;

import com.tbl.modules.PADInterface.dao.PADInterfaceDAO;
import com.tbl.modules.PADInterface.service.PADInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: dyyl
 * @description: 平板接口service实现类
 * @author: zj
 * @create: 2019-03-12 11:30
 **/
@Service("PADInterfaceService")
public class PADInterfaceServiceImpl implements PADInterfaceService {
    @Autowired
    private PADInterfaceDAO padInterfaceDAO;

    @Override
    public String getOperateType(String ip) {
        return padInterfaceDAO.getOperateType(ip);
    }
}
    