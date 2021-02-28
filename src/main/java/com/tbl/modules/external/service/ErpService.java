package com.tbl.modules.external.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;

/**
 * 客户对外接口interface
 *
 * @author anss
 * @date 2019-01-02
 */
public interface ErpService {
    /**
     * 依据生产日期与保质期计算有效期至
     *
     * @return
     * @author pz
     * @date 2019-02-28
     */
    String setQualityDate(Integer qualityPeriod, String productDate);

    /**
     * 获取有效天数
     */
    Long getQualityPeriod(String qualityDate, String productDate);

    /**
     * 调接口传数据给erp
     */
    boolean getDate(JSONArray jsonArrayResult, String url);

    /**
     * 东洋库区转成erp库区
     */
    String changDepotPosition(String areaCode);

    /**
     * 整货类型(自采或客供)
     */
    String getInstorageType(String materialCode, String batchNo, String positionCode);

    /**
     * 散货类型(自采或客供)
     */
    String getMaterielType(String materialCode, String positionCode);

    /**
     * 根据库位编号获取库区编号
     */
    String selectDepotAreaCode(String positionCode);

    /**
     * 依据库区编码与库位名称查找库位编码
     */
    String selectPositionCode(String positionName);

    /**
     * 根据库位编号与客供字段获取库区编码
     */
    String selectByErpAreaCode(String positionName, String ftypeId);

    /**
     * 依据部门名称获取部门编号
     */
    String getDepartmentCode(String departmentName);

    /**
     * 获取散货库区
     * @return
     */
    String getAreaCode(String materialCode, String positionCode);
}
