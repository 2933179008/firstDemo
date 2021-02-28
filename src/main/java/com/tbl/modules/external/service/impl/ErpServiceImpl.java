package com.tbl.modules.external.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.EncodeUtil;
import com.tbl.common.utils.HttpUtil;
import com.tbl.modules.external.dao.ErpServiceDAO;
import com.tbl.modules.external.service.ErpService;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.PutBillDetailService;
import com.tbl.modules.instorage.service.PutBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公共方法调用接口类
 *
 * @author pz
 * @date 2019-01-02
 */
@Service("erpService")
public class ErpServiceImpl implements ErpService {

    //入库单service
    @Autowired
    private InstorageService instorageService;

    //上架单service
    @Autowired
    private PutBillService putBillService;

    //上架单详情service
    @Autowired
    private PutBillDetailService putBillDetailService;

    //erp通用方法service
    @Autowired
    private ErpServiceDAO erpServiceDAO;

    /**
     * 依据生产日期与保质期计算有效期至
     *
     * @param qualityPeriod productDate
     * @return
     * @author pz
     * @date 20190525
     */
    @Override
    public String setQualityDate(Integer qualityPeriod, String productDate) {

        Date aD = DateUtils.stringToDate(productDate, "yyyyMMdd");

        String productDateS = DateUtils.format(aD, "yyyy-MM-dd");

        Date productDate_ = DateUtils.stringToDate(productDateS, "yyyy-MM-dd");

        //有效期至
        String deadline = DateUtils.format(DateUtils.addDateDays(productDate_, qualityPeriod), "yyyyMMdd");

        return deadline;
    }

    /**
     * 获取有效天数
     */
    @Override
    public Long getQualityPeriod(String qualityDate, String productDate) {
        Date quality = DateUtils.stringToDate(qualityDate, "yyyyMMdd");

        Date product = DateUtils.stringToDate(productDate, "yyyyMMdd");

        Long qualityPeriod = ((quality.getTime() - product.getTime()) / (24 * 60 * 60 * 1000));

        return qualityPeriod;
    }

    /**
     * 调接口传数据给erp
     */
    @Override
    public boolean getDate(JSONArray jsonArrayResult, String url) {
        boolean result = false;
        try {
            if (!jsonArrayResult.isEmpty() && jsonArrayResult.size() > 0) {
                Map<String, Object> responseMap = HttpUtil.postJSONWithResponseHeaders(url + EncodeUtil.urlEncode(JSON.toJSONString(jsonArrayResult)), null, null);
                if (responseMap != null) {
                    String responseString = (String) (responseMap.get("data"));
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("sucess") == null ? false : Boolean.parseBoolean(jsonObject.getString("sucess"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 东洋库区转成erp库区
     */
    @Override
    public String changDepotPosition(String areaCode) {
        String erpAreaCode = "";
        if (areaCode.equals("KQ0000001")) {
            erpAreaCode = "A.01";
        } else if (areaCode.equals("KQ0000002")) {
            erpAreaCode = "A.02";
        } else if (areaCode.equals("KQ0000003")) {
            erpAreaCode = "A.04";
        } else if (areaCode.equals("KQ0000004") || areaCode.equals("KQ0000005") || areaCode.equals("KQ0000006")) {
            erpAreaCode = "A.03";
        } else {
            erpAreaCode = areaCode;
        }
        return erpAreaCode;
    }

    /**
     * 整货类型(自采或客供)
     */
    @Override
    public String getInstorageType(String materialCode, String batchNo, String positionCode) {

        EntityWrapper<PutBillDetail> wrapperPutBillDetail = new EntityWrapper<>();
        wrapperPutBillDetail.eq("material_code", materialCode);
        wrapperPutBillDetail.eq("batch_no", batchNo);
        wrapperPutBillDetail.eq("position_code", positionCode);
        PutBillDetail putBillDetail = putBillDetailService.selectOne(wrapperPutBillDetail);
        //获取上架单id
        Long putBillId = putBillDetail.getPutBillId();
        //依据上架单id，获取上架单
        PutBill putBill = putBillService.selectById(putBillId);
        //获取入库单id
        Long instorageBillId = putBill.getInstorageBillId();
        //获取入库单
        Instorage instorage = instorageService.selectById(instorageBillId);
        String instorageType = instorage.getInstorageType();

        return instorageType;
    }

    /**
     * 散货类型(自采或客供)
     */
    @Override
    public String getMaterielType(String materialCode, String positionCode) {
        String instorageType = null;
        //获取库区编号
        String areaCode = erpServiceDAO.getAreaCode(materialCode, positionCode);
        //获取所有Erp客供库区
        List<String> lstAreaCode = erpServiceDAO.getErpAreaCode();
        if (lstAreaCode.contains(areaCode)) {
            instorageType = "1";
        } else {
            instorageType = "0";
        }
        return instorageType;
    }

    /**
     * 根据库位编号获取库区编号
     */
    @Override
    public String selectDepotAreaCode(String positionCode) {
        return erpServiceDAO.selectDepotAreaCode(positionCode);
    }

    /**
     * 依据库区编码与库位名称查找库位编码
     */
    @Override
    public String selectPositionCode(String positionName) {
        return erpServiceDAO.selectPositionCode(positionName);
    }

    /**
     * 根据库位编号与客供字段获取库区编码
     */
    @Override
    public String selectByErpAreaCode(String positionName, String ftypeId) {
        return erpServiceDAO.selectByErpAreaCode(positionName, ftypeId);
    }

    /**
     * 依据部门名称获取部门编号
     */
    @Override
    public String getDepartmentCode(String departmentName){
        return erpServiceDAO.getDepartmentCode(departmentName);
    }

    /**
     * 获取散货库区
     * @return
     */
    @Override
    public String getAreaCode(String materialCode, String positionCode){
        return erpServiceDAO.getAreaCode(materialCode,positionCode);
    }
}




