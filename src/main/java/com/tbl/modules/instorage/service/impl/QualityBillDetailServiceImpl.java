package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.instorage.dao.InstorageDetailDAO;
import com.tbl.modules.instorage.dao.QualityBillDAO;
import com.tbl.modules.instorage.dao.QualityBillDetailDAO;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.entity.QualityBillDetail;
import com.tbl.modules.instorage.service.QualityBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 质检单详情service实现类
 * @author: zj
 * @create: 2019-02-11 16:12
 **/
@Service("qualityBillDetailService")
public class QualityBillDetailServiceImpl extends ServiceImpl<QualityBillDetailDAO, QualityBillDetail> implements QualityBillDetailService {
    @Autowired
    private QualityBillDAO qualityBillDAO;
    @Autowired
    private QualityBillDetailDAO qualityBillDetailDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    @Autowired
    private MaterielDAO materielDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveQualityBillDetail(Long qualityId, String materialCodes) {
        //入库单id
        Long instorageId = 0l;
        if(qualityId != null){
            //根据质检单id获取入库单id
            instorageId = qualityBillDAO.selectById(qualityId).getInstorageBillId();
        }

        if(StringUtils.isNotEmpty(materialCodes)){
            String[] materialCodeArr = materialCodes.split(",");
            QualityBillDetail qualityBillDetail = null;
            for(int i=0;i<materialCodeArr.length;i++){
                qualityBillDetail = new QualityBillDetail();
                //插入质检单主键id
                qualityBillDetail.setQualityId(qualityId);
                //插入物料编码
                qualityBillDetail.setMaterialCode(materialCodeArr[i]);

                //根据入库单id和物料编码获取物料批次号
                EntityWrapper<InstorageDetail> entityInstorageDetail = new EntityWrapper<InstorageDetail>();
                entityInstorageDetail.eq("instorage_bill_id",instorageId);
                entityInstorageDetail.eq("material_code",materialCodeArr[i]);
                //批次号
                String batchNo = "";
                List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(entityInstorageDetail);
                if(lstInstorageDetail != null && lstInstorageDetail.size() > 0){
                    //根据入库单id和物料编码获取物料批次号只存在一条，所以就取第一条数据
                    InstorageDetail instorageDetail = lstInstorageDetail.get(0);
                    batchNo = instorageDetail.getBatchNo();
                }
                //插入批次号
                qualityBillDetail.setBatchNo(batchNo);

                //根据物料编号查询物料相关信息
                EntityWrapper<Materiel> entity = new EntityWrapper<>();
                entity.eq("materiel_code",materialCodeArr[i]);
                List<Materiel> lstMaterial = materielDAO.selectList(entity.eq("materiel_code",materialCodeArr[i]));
                if(lstMaterial != null && lstMaterial.size() > 0){
                    //根据物料编号查询的物料只存在一条，所以就取第一条数据
                    Materiel material = lstMaterial.get(0);
                    //插入物料名称
                    qualityBillDetail.setMaterialName(material.getMaterielName());
                }
                qualityBillDetailDAO.insert(qualityBillDetail);
            }
        }
    }

    @Override
    public Page<QualityBillDetail> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<QualityBillDetail> pageQualityBillDetail = new Page<QualityBillDetail>(pg,rows,sortname,order);
        //获取质检单详情列表
        List<QualityBillDetail> list = qualityBillDetailDAO.getPageQualityBillDetailList(pageQualityBillDetail, params);

        return pageQualityBillDetail.setRecords(list);

    }

    @Override
    public boolean deleteQualityBillDetail(String ids) {
        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        Integer delCount = qualityBillDetailDAO.deleteBatchIds(lstMaterialIds);
        if (delCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasMaterial(Long qualityId, String materialCodes) {
        boolean ret=false;
        List<String> lstMaterialCodes = Arrays.asList(materialCodes.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("qualityId", qualityId);
        map.put("materialCodes", lstMaterialCodes);
        Integer count = qualityBillDetailDAO.getMaterialCount(map);
        if(count > 0){
            ret=true;
        }
        return ret;
    }

    @Override
    public void updateSampleWeight(Long qualityBillDetailId, String sampleWeight) {
        qualityBillDetailDAO.updateSampleWeight(qualityBillDetailId,sampleWeight);
    }

    @Override
    public void updateQualifiedWeight(Long qualityBillDetailId, String qualifiedWeight) {
        qualityBillDetailDAO.updateQualifiedWeight(qualityBillDetailId,qualifiedWeight);
    }

    @Override
    public boolean updateDetailState(String id, String state, Double rate) {
        DecimalFormat df = new DecimalFormat("#.00");
        boolean result = false;
        try{
            QualityBillDetail qualityBillDetail = qualityBillDetailDAO.selectById(Long.valueOf(id));
            qualityBillDetail.setState(state);
            qualityBillDetail.setPassRate(df.format(rate)+"%");
            qualityBillDetailDAO.updateById(qualityBillDetail);
            result = true;
        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
    