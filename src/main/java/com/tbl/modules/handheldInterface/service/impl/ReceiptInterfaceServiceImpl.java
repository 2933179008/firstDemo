package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.ReceiptInterfaceDAO;
import com.tbl.modules.handheldInterface.service.ReceiptInterfaceService;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.platform.entity.system.InterfaceLog;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货人接口Service实现
 */
@Service(value = "receiptInterfaceService")
public class ReceiptInterfaceServiceImpl extends ServiceImpl<ReceiptInterfaceDAO, Receipt> implements ReceiptInterfaceService {

    //日志接口
    @Autowired
    private InterfaceLogService interfaceLogService;

    //收货DAO
    @Autowired
    private ReceiptInterfaceDAO receiptInterfaceDAO;

    /**
     * 获取收货单
     *
     * @return
     */
    @Override
    public Map<String, Object> getReceiptList() {

        boolean result = true;
        String msg = "获取收货单成功！";
        Map<String, Object> map = new HashMap<>();

        //获取收货单
        List<Receipt> receiptList = receiptInterfaceDAO.selectList(
                new EntityWrapper<Receipt>()
                        .eq("state", DyylConstant.STATE_WAIT)
                        .or()
                        .eq("state", DyylConstant.STATE_HARVEST)
        );

        String errorinfo = null;
        //判断收货单集合是否有值
        if (receiptList.isEmpty()) {
            result = false;
            msg = "无收货单！";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "收货单调用接口";
        interfaceLogService.interfaceLogInsert(interfacename, null, msg, errorinfo);

        map.put("data", receiptList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }
}
