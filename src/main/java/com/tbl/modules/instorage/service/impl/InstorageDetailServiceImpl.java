package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.instorage.dao.InstorageDAO;
import com.tbl.modules.instorage.dao.InstorageDetailDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.service.InstorageDetailService;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 入库单详情service实现类
 * @author: zj
 * @create: 2019-01-21 15:15
 **/
@Service("instorageDetailService")
public class InstorageDetailServiceImpl extends ServiceImpl<InstorageDetailDAO, InstorageDetail> implements InstorageDetailService {
    @Autowired
    private InstorageDAO instorageDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    @Autowired
    private MaterielDAO materielDao;
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //入库单主键id
        String instorageId = (String) params.get("instorageId");
        Page<InstorageDetail> page = new Page<InstorageDetail>();
        if(StringUtils.isNotEmpty(instorageId)){
            page = this.selectPage(
                    new Query<InstorageDetail>(params).getPage(),
                    new EntityWrapper<InstorageDetail>()
                            .eq("instorage_bill_id", instorageId)
            );
        }
        return new PageUtils(page);
    }

    @Override
    public List<Map<String, Object>> getSelectMaterialList(Long outstorageBillId,String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return instorageDetailDAO.getSelectMaterialList(page,outstorageBillId,queryString);
    }

    @Override
    public Integer getSelectMaterialTotal(Long outstorageBillId,String queryString) {
        return instorageDetailDAO.getSelectMaterialTotal(outstorageBillId,queryString);
    }

    @Override
    public boolean hasMaterial(Long instorageId, String materialCodes) {
        boolean ret=false;
        if(StringUtils.isNotEmpty(materialCodes)){
            List<String> lstMaterialCodes = new ArrayList<>();
            Instorage instorage = instorageDAO.selectById(instorageId);
            //入库类型
            String instorageType = instorage.getInstorageType();
            if("2".equals(instorageType)){
                String[] materialCodeArr = materialCodes.split(",");
                for (int i=0;i<materialCodeArr.length;i++){
                    String materialCode = materialCodeArr[i];
                    materialCode = materialCode.substring(0,materialCode.indexOf("|"));
                    lstMaterialCodes.add(materialCode);
                }
                // 通过去重之后的HashSet长度来判断原list是否包含重复元素
                boolean isRepeat = lstMaterialCodes.size() != new HashSet<String>(lstMaterialCodes).size();
                if(isRepeat){
                    ret=true;
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("instorageId", instorageId);
                    map.put("materialCodes", lstMaterialCodes);
                    Integer count = instorageDetailDAO.getMaterialCount(map);
                    if(count > 0){
                        ret=true;
                    }
                }
            }else{
                lstMaterialCodes = Arrays.asList(materialCodes.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                Map<String, Object> map = new HashMap<>();
                map.put("instorageId", instorageId);
                map.put("materialCodes", lstMaterialCodes);
                Integer count = instorageDetailDAO.getMaterialCount(map);
                if(count > 0){
                    ret=true;
                }
            }
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInstorageDetail(Long instorageId, String materialCodes) {
        if(StringUtils.isNotEmpty(materialCodes)){
            //入库类型
            String instorageType = "";
            //出库单id
            Long outstorageBillId = 0l;
            if(instorageId != null){
                Instorage instorage = instorageDAO.selectById(instorageId);
                instorageType = instorage.getInstorageType();
                outstorageBillId = instorage.getOutstorageBillId();

            }
            String[] materialCodeArr = materialCodes.split(",");
            InstorageDetail instorageDetail = null;
            for(int i=0;i<materialCodeArr.length;i++){
                instorageDetail = new InstorageDetail();
                //插入入库单主键id
                instorageDetail.setInstorageBillId(instorageId);

                if("2".equals(instorageType)){//如果是“生产退货入库”类型的入库单
                    //物料编号
                    String materialCode = materialCodeArr[i].substring(0,materialCodeArr[i].indexOf("|"));
                    instorageDetail.setMaterialCode(materialCode);
                    //批次号
                    String batchNo = materialCodeArr[i].substring(materialCodeArr[i].indexOf("|")+1);
                    instorageDetail.setBatchNo(batchNo);
                    //获取出库单详情
                    Map<String,Object> outstorageBillDetailMap = instorageDetailDAO.getOutstorageBillDetail(outstorageBillId,materialCode,batchNo);
                    if(outstorageBillDetailMap != null){
                        instorageDetail.setMaterialName(outstorageBillDetailMap.get("material_name")==null?"":outstorageBillDetailMap.get("material_name").toString());
                        instorageDetail.setUnit(outstorageBillDetailMap.get("unit")==null?"":outstorageBillDetailMap.get("unit").toString());
                        instorageDetail.setInstorageAmount(outstorageBillDetailMap.get("separable_amount")==null?"":outstorageBillDetailMap.get("separable_amount").toString());
                        instorageDetail.setInstorageWeight(outstorageBillDetailMap.get("separable_weight")==null?"":outstorageBillDetailMap.get("separable_weight").toString());
                    }

                }else{
                    //插入物料编码
                    instorageDetail.setMaterialCode(materialCodeArr[i]);

                    //根据物料编号查询物料相关信息
                    EntityWrapper<Materiel> entity = new EntityWrapper<>();
                    entity.eq("materiel_code",materialCodeArr[i]);
                    List<Materiel> lstMaterial = materielDao.selectList(entity.eq("materiel_code",materialCodeArr[i]));
                    if(lstMaterial != null && lstMaterial.size() > 0){
                        //根据物料编号查询的物料只存在一条，所以就取第一条数据
                        Materiel material = lstMaterial.get(0);
                        //插入物料名称
                        instorageDetail.setMaterialName(material.getMaterielName());
                        //插入物料包装单位
                        instorageDetail.setUnit(material.getUnit());
                    }
                }
                instorageDetailDAO.insert(instorageDetail);
            }
        }
        return true;
    }

    @Override
    public boolean deleteInstorageDetail(String ids) {
        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        Integer delCount = instorageDetailDAO.deleteBatchIds(lstMaterialIds);
        if (delCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getInstorageStateByDetailId(Long instorageDetailId) {
        //入库单主键id
        Long instorageId = instorageDetailDAO.selectById(instorageDetailId).getInstorageBillId();
        //入库单状态
        String state = instorageDAO.selectById(instorageId).getState();
        return state;
    }

    /**
     * @Author update by anss 2019-04-22
     * @param instorageDetailId
     * @param batchNo
     * @return
     */
    @Override
    public boolean updateBatchNo(Long instorageDetailId, String batchNo) {
        //根据主键获取入库单详情实体
        InstorageDetail instorageDetail = instorageDetailDAO.selectById(instorageDetailId);
        if (instorageDetail == null) {
            return false;
        }
        //根据主键获取入库单实体
        Instorage instorage = instorageDAO.selectById(instorageDetail.getInstorageBillId());
        if (instorage == null) {
            return false;
        }

        //批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
        String batchNoCode = receiptDetailService.generateBatchNoCode(instorage.getSupplierCode(), instorage.getCustomerCode(), batchNo);
        Integer updateResult = instorageDetailDAO.updateBatchNo(instorageDetailId, batchNoCode);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateProductDate(Long instorageDetailId, String productDate) {
        Integer updateResult = instorageDetailDAO.updateProductDate(instorageDetailId,productDate);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateInstorageAmount(Long instorageDetailId, String instorageAmount) {
        Integer updateResult = instorageDetailDAO.updateInstorageAmount(instorageDetailId,instorageAmount);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateInstorageWeight(Long instorageDetailId, String instorageWeight) {
        Integer updateResult = instorageDetailDAO.updateInstorageWeight(instorageDetailId,instorageWeight);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean selectMaterialType(Long instorageId) {
        boolean result = false;
        List<Map<String,Object>> list = instorageDetailDAO.selectMaterialType(instorageId);
        //如果返回结果集大于1条数据，则物料种类不只一种
        if(list!=null && list.size()>1){
            result = false;
        }else{
            result = true;
        }
        return result;
    }
}
    