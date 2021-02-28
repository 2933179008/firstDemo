package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.MaterielInterfaceDAO;
import com.tbl.modules.handheldInterface.service.MaterielInterfaceService;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料接口Service实现
 */
@Service(value = "materielInterfaceService")
public class MaterielInterfaceServiceImpl extends ServiceImpl<MaterielInterfaceDAO, Materiel> implements MaterielInterfaceService {

    //日志接口Service
    @Autowired
    private InterfaceLogService interfaceLogService;

    //物料接口DAO
    @Autowired
    private MaterielInterfaceDAO materielInterfaceDAO;

    /**
     * 根据物料条码获取物料信息
     *
     * @param barcode
     * @return
     */
    @Override
    public Map<String, Object> getMateriel(String barcode) {

        boolean result = true;
        String msg = "获取成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        Materiel materiel = null;
        List<Materiel> materielList = null;
        if (!Strings.isNullOrEmpty(barcode)) {
            materielList = materielInterfaceDAO.selectList(
                    new EntityWrapper<Materiel>()
                            .eq("barcode", barcode)
            );
            if (materielList.isEmpty()) {
                result = false;
                msg = "未找到相关物料";
            } else if (materielList.size() > 1) {
                result = false;
                msg = "失败原因：此条码存在多个物料信息";
                materielList = null;
                errorinfo = DateUtils.getTime();
            } else {
                for (Materiel mtl : materielList) {
                    materiel = mtl;
                }
            }

        } else {
            result = false;
            msg = "失败原因：物料条码为空";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用获取物料信息接口";
        String parameter = "Barcode:" + barcode;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", materiel);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 模糊查询物料信息
     *
     * @param materielCodeOrName
     * @return
     */
    @Override
    public Map<String, Object> getMaterielByMaterielCodeOrName(String materielCodeOrName) {

        boolean result = true;
        String msg = "查询成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<Materiel> materielList = null;
        if (!Strings.isNullOrEmpty(materielCodeOrName)) {
            materielList = materielInterfaceDAO.selectList(
                    new EntityWrapper<Materiel>()
                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                            .like("materiel_code", materielCodeOrName)
            );
            if (materielList.isEmpty()) {
                materielList = materielInterfaceDAO.selectList(
                        new EntityWrapper<Materiel>()
                                .eq("deleted_flag", DyylConstant.NOTDELETED)
                                .like("materiel_name", materielCodeOrName)
                );
                if (materielList.isEmpty()) {
                    result = false;
                    msg = "失败原因：未找到相符的物料信息";
                    errorinfo = DateUtils.getTime();
                }
            }
        } else {
            result = false;
            msg = "失败原因：无参数";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "调用模糊查询物料信息接口";
        String parameter = "MaterielCodeOrName:" + materielCodeOrName;

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", materielList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }


    /**
     * 根据物料条码模糊查询物料信息接口
     * @author anss
     * @date 2019-04-28
     * @param barcode
     * @return
     */
    @Override
    public Map<String, Object> getMaterielByBarcode(String barcode) {
        Map<String, Object> map = new HashMap<>();

        List<Materiel> materielList = materielInterfaceDAO.selectList(
                new EntityWrapper<Materiel>()
                        .like(StringUtils.isNotBlank(barcode),"barcode", barcode)
        );

        interfaceLogService.interfaceLogInsert("根据物料条码模糊查询物料信息接口", "Barcode:" + barcode, "获取成功", null);

        map.put("data", materielList);
        map.put("result", true);
        map.put("msg", "获取成功");

        return map;
    }
}
