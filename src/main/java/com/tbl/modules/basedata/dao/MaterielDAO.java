package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.Materiel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料管理Dao
 *
 * @author yuany
 * @date 2019-01-02
 */
public interface MaterielDAO extends BaseMapper<Materiel> {
    /**
     * 获取导出列
     *
     * @return List<Materiel>
     * @author yuany
     * @date 2019-01-03
     */
    List<Materiel> getAllLists(List<Long> ids);

    /**
     * 根据id更新rfid
     *
     * @param rfid
     * @param id
     * @return
     * @author yuany
     * @date 2019-01-09
     */
    int updateMaterielRfid(@Param("rfid") String rfid, @Param("id") Long id);

    /**
     * 保存物料
     * @param
     * @return
     * @author pz
     * @date 2019-02-19
     */
    Boolean savaMateriel(@Param("id") Long id, @Param("materielCode") String materielCode, @Param("materielName") String materielName, @Param("barcode") String barcode,
                         @Param("customerBy") Long customerBy, @Param("qualityPeriod") String qualityPeriod, @Param("length") String length, @Param("wide") String wide,
                         @Param("height") String height, @Param("volume") String volume, @Param("unit") String unit, @Param("spec") String spec, @Param("upshelfClassify") String upshelfClassify,
                         @Param("pickClassify") String pickClassify, @Param("batchRule") String batchRule, @Param("supplierBy") Long supplierBy, @Param("producerBy") Long producerBy, @Param("createTime") String createTime);


}
