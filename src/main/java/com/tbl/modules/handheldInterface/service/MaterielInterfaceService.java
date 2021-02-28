package com.tbl.modules.handheldInterface.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.modules.basedata.entity.Materiel;

import java.util.Map;

public interface MaterielInterfaceService extends IService<Materiel> {

    /**
     * 根据物料条码获取物料信息
     *
     * @param barcode
     * @return
     */
    Map<String, Object> getMateriel(String barcode);

    /**
     * 模糊查询物料信息
     *
     * @param materielCodeOrName
     * @return
     */
    Map<String, Object> getMaterielByMaterielCodeOrName(String materielCodeOrName);

    /**
     * 根据物料条码模糊查询物料信息接口
     * @author anss
     * @date 2019-04-28
     * @param barcode
     * @return
     */
    Map<String, Object> getMaterielByBarcode(String barcode);
}
