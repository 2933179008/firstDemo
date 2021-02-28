package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.DepotPositionInterfaceDAO;
import com.tbl.modules.handheldInterface.service.DepotPositionInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库位接口Service实现
 */
@Service(value = "depotPositionInterfaceService")
public class DepotPositionInterfaceServiceImpl extends ServiceImpl<DepotPositionInterfaceDAO, DepotPosition> implements DepotPositionInterfaceService {

    //日志接口
    @Autowired
    private InterfaceLogService interfaceLogService;

    //库位接口DAO
    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;


    @Autowired
    private DepotPositionService depotPositionService;

    /**
     * 根据库位编码获取库位接口
     *
     * @param positionCode
     * @return
     */
    @Override
    public Map<String, Object> getDepotPosition(String positionCode) {
        boolean result = true;
        String msg = "获取数据成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<DepotPosition> depotPositionList = null;
        if (!Strings.isNullOrEmpty(positionCode)) {

            depotPositionList = depotPositionInterfaceDAO.selectList(
                    new EntityWrapper<DepotPosition>()
                            .eq("position_code", positionCode)
            );
            if (depotPositionList.isEmpty()) {
                result = false;
                msg = "无此库位";
            }

        } else {
            result = false;
            msg = "失败原因：库位编码为空";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用库位接口";
        String parameter = "PositionCode:" + positionCode;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", depotPositionList);
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 获取所有库位接口
     *
     * @return
     */
    @Override
    public Map<String, Object> getDepotPositionList() {

        boolean result = true;
        String msg = "获取库位信息成功";
        Map<String, Object> map = new HashMap<>();

        List<DepotPosition> depotPositionList = depotPositionInterfaceDAO.selectList(
                new EntityWrapper<DepotPosition>()
                        .eq("position_frozen", DyylConstant.FROZEN_0)
        );

        String errorinfo = null;
        if (depotPositionList.isEmpty()) {
            result = false;
            msg = "失败原因：无库位信息";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "调用获取所有库位接口";
        interfaceLogService.interfaceLogInsert(interfacename, null, msg, errorinfo);

        map.put("data", depotPositionList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 根据库位编码获取库位接口（根据编码模糊查询）
     * @return
     * @author anss
     * @date 2019-04-30
     * @param positionCode
     * @return
     */
    @Override
    public Map<String, Object> getDepotPositionByPositionCode(String positionCode) {

        boolean result = true;
        String msg = "获取库位信息成功";
        Map<String, Object> map = new HashMap<>();

        List<DepotPosition> depotPositionList = depotPositionInterfaceDAO.selectList(
                new EntityWrapper<DepotPosition>()
                        .eq("position_frozen", DyylConstant.FROZEN_0)
                .like(StringUtils.isNotBlank(positionCode), "position_code", positionCode)
        );

        String errorinfo = null;
        if (depotPositionList.isEmpty()) {
            result = false;
            msg = "失败原因：无库位信息";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "根据库位编码获取库位接口（根据编码模糊查询）";
        interfaceLogService.interfaceLogInsert(interfacename, null, msg, errorinfo);

        map.put("data", depotPositionList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 库位冻结/解冻
     *
     * @param positionCode
     * @param frozen
     * @return
     */
    @Override
    public Map<String, Object> frozenStatus(String positionCode, String frozen) {

        boolean result = true;
        String msg = "操作成功";
        Map<String, Object> map = new HashMap<>();
        String parameter = "PositionCode:" + positionCode + "/Frozen:" + frozen;
        if (Strings.isNullOrEmpty(positionCode) && Strings.isNullOrEmpty(frozen)) {
            interfaceLogService.interfaceLogInsert("调用库位冻结/解冻接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：参数未获取");

            return map;
        }

        DepotPosition depotPosition = depotPositionService.selectOne(
                new EntityWrapper<DepotPosition>()
                        .eq("position_code", positionCode)
        );
        if (depotPosition == null) {
            interfaceLogService.interfaceLogInsert("调用库位冻结/解冻接口", parameter, "失败原因：未找到对应的库位信息", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未找到对应的库位信息");
        }
        depotPosition.setPositionFrozen(frozen);
        result = depotPositionService.updateById(depotPosition);
        if (!result) {
            msg = "失败原因：操作失败";
        }
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 判断库位是否冻结
     *
     * @param positionCode
     * @return
     */
    @Override
    public Map<String, Object> positionFrozen(String positionCode) {
        boolean result = true;
        String msg = "提交成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "positionCode:" + positionCode;
        if (Strings.isNullOrEmpty(positionCode)) {
            interfaceLogService.interfaceLogInsert("调用判断库位是否冻结接口", parameter, "失败原因：库位编码为空", DateUtils.getTime());

            map.put("result", false);
            map.put("msg", "失败原因：库位编码为空");
            return map;
        }
        DepotPosition depotPosition = depotPositionService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code", positionCode));

        if (depotPosition == null) {
            msg = "失败原因：未找到此库位信息";
            interfaceLogService.interfaceLogInsert("调用判断库位是否冻结接口", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }

        map.put("data", depotPosition.getPositionFrozen());
        map.put("result", false);
        map.put("msg", msg);
        return map;
    }
}
