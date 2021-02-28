package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.TrayDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.service.TrayDetailInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.TrayDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 托盘管理详情接口Service实现
 */
@Service(value = "trayDetailInterfaceService")
public class TrayDetailInterfaceServiceImpl extends ServiceImpl<TrayDetailInterfaceDAO, TrayDetail> implements TrayDetailInterfaceService {

    //日志接口Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //托盘详情接口DAO
    @Autowired
    private TrayDetailInterfaceDAO trayDetailInterfaceDAO;

    /**
     * 托盘详情查询接口
     *
     * @param trayBy
     * @return
     */
    @Override
    public Map<String, Object> getTrayDetail(Long trayBy) {

        boolean result = true;
        String msg = "查询成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<TrayDetail> trayDetailList = null;
        if (trayBy != null) {
            trayDetailList = trayDetailInterfaceDAO.selectList(
                    new EntityWrapper<TrayDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("tray_by", trayBy)
            );
            if (trayDetailList.isEmpty()) {
                result = false;
                msg = "无数据";
            }
        } else {
            result = false;
            msg = "失败原因：获取此托盘ID为空";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用托盘详情查询接口";
        String parameter = "TrayBy:" + trayBy.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", trayDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }
}
