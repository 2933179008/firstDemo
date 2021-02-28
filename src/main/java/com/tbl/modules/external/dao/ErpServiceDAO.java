package com.tbl.modules.external.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * erp接口
 */
public interface ErpServiceDAO {

    /**
     * 获取散货库区
     * @return
     */
    String getAreaCode(@Param("materialCode")String materialCode, @Param("positionCode")String positionCode);

    /**
     * 获取Erp所有客供库区
     * @return
     */
    List<String> getErpAreaCode();

    /**
     * 根据库位编号获取库区编号
     * @return
     */
    String selectDepotAreaCode(@Param("positionCode") String positionCode);

    /**
     * 依据库区编码与库位名称查找库位编码
     */
    String selectPositionCode(@Param("positionName")String positionName);

    /**
     * 根据库位编号与客供字段获取库区编码
     */
    String selectByErpAreaCode(@Param("positionName")String positionName,@Param("ftypeId")String ftypeId);

    /**
     * 依据部门名称获取部门编号
     */
    String getDepartmentCode(@Param("departmentName")String departmentName);
}
