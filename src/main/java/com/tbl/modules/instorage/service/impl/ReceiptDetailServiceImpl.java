package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.instorage.dao.InstorageDAO;
import com.tbl.modules.instorage.dao.ReceiptDAO;
import com.tbl.modules.instorage.dao.ReceiptDetailDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.stock.dao.StockDAO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 收货计划详情service实现类
 * @author: zj
 * @create: 2019-01-07 13:40
 **/
@Service("receiptDetailService")
public class ReceiptDetailServiceImpl extends ServiceImpl<ReceiptDetailDAO, ReceiptDetail> implements ReceiptDetailService {
    @Autowired
    private ReceiptDAO receiptDAO;
    @Autowired
    private ReceiptDetailDAO receiptDetailDAO;
    @Autowired
    private MaterielService materielService;

    @Autowired
    private InstorageService instorageService;
    @Autowired
    private InstorageDAO instorageDAO;
    @Autowired
    private StockDAO stockDAO;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //收货单主键id
        String receiptId = (String) params.get("receiptId");
        Page<ReceiptDetail> page = new Page<ReceiptDetail>();
        if(StringUtils.isNotEmpty(receiptId)){
            page = this.selectPage(
                    new Query<ReceiptDetail>(params).getPage(),
                    new EntityWrapper<ReceiptDetail>()
                            .eq("receipt_plan_id", receiptId)
            );
        }
        for (ReceiptDetail receiptDetail : page.getRecords()){
            Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",receiptDetail.getMaterialCode()));
            receiptDetail.setQualityPeriod(materiel.getQualityPeriod());
        }
        return new PageUtils(page);
    }

    @Override
    public List<Map<String, Object>> getSelectMaterialList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return receiptDetailDAO.getSelectMaterialList(page,queryString);
    }

    @Override
    public Integer getSelectMaterialTotal(String queryString) {
        return receiptDetailDAO.getSelectMaterialTotal(queryString);
    }

    @Override
    public boolean hasMaterial(Long receiptId, String materialCodes) {
        boolean ret=false;
        List<String> lstMaterialCodes = Arrays.asList(materialCodes.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("receiptId", receiptId);
        map.put("materialCodes", lstMaterialCodes);
        Integer count = receiptDetailDAO.getMaterialCount(map);
        if(count > 0){
            ret=true;
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveReceiptDetail(Long receiptId, String materialCodes) {
        if(StringUtils.isNotEmpty(materialCodes)){
            String[] materialCodeArr = materialCodes.split(",");
            ReceiptDetail reDetail = null;
            for(int i=0;i<materialCodeArr.length;i++){
                reDetail = new ReceiptDetail();
                //插入收货单主键id
                reDetail.setReceiptPlanId(receiptId);
                //插入物料编码
                reDetail.setMaterialCode(materialCodeArr[i]);

                //根据物料编号查询物料相关信息
                EntityWrapper<Materiel> entity = new EntityWrapper<>();
                entity.eq("materiel_code",materialCodeArr[i]);
                List<Materiel> lstMaterial = materielService.selectList(entity.eq("materiel_code",materialCodeArr[i]));
                if(lstMaterial != null && lstMaterial.size() > 0){
                    //根据物料编号查询的物料只存在一条，所以就取第一条数据
                    Materiel material = lstMaterial.get(0);
                    //插入物料名称
                    reDetail.setMaterialName(material.getMaterielName());
                    //插入物料包装单位
                    reDetail.setUnit(material.getUnit());
                }

                receiptDetailDAO.insert(reDetail);
            }
        }
        return true;
    }

    @Override
    public String getReceiptStateByDetailId(Long receiptDetailId) {
        //收货单主键id
        Long receiptPlanId = receiptDetailDAO.selectById(receiptDetailId).getReceiptPlanId();
        //收货单状态
        String state = receiptDAO.selectById(receiptPlanId).getState();
        return state;
    }

    @Override
    public boolean updatePlanReceiptAmount(Long receiptDetailId, String planReceiptAmount) {
        //获取单位重量
        String materialcode =  receiptDetailDAO.selectById(receiptDetailId).getMaterialCode();
        Materiel m = new Materiel();
        m.setMaterielCode(materialcode);
        Integer updateResult = receiptDetailDAO.updatePlanReceiptAmountAndWeight(receiptDetailId,planReceiptAmount);


        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteReceiptDetail(String ids) {
        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        Integer delCount = receiptDetailDAO.deleteBatchIds(lstMaterialIds);
        if (delCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateBatchNo(Long receiptDetailId, String batchNo) {
        Integer updateResult = receiptDetailDAO.updateBatchNo(receiptDetailId,batchNo);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updatePlanReceiptWeight(Long receiptDetailId, String planReceiptWeight) {
        Integer updateResult = receiptDetailDAO.updatePlanReceiptWeight(receiptDetailId,planReceiptWeight);
        if(updateResult > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateInstorage(String receiptPlanId,String receiptPlanDetailIdStr, String inStorageAmountStr,
                                  String inStorageWeightStr,String batchNoStr,String productDateStr,String qualityPeriodStr) {
        if(StringUtils.isNotBlank(receiptPlanId) && StringUtils.isNotBlank(receiptPlanDetailIdStr)
                && StringUtils.isNotBlank(inStorageAmountStr) && StringUtils.isNotBlank(inStorageWeightStr)
                && StringUtils.isNotBlank(batchNoStr) && StringUtils.isNotBlank(productDateStr) && StringUtils.isNotEmpty(qualityPeriodStr)){
            /**1.入库单插入数据**/
            //当前时间
            String nowTime = DateUtils.getTime();
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            //当前登陆人id
            Long userId = user.getUserId();

            //根据收货单id获取收货单信息
            Receipt receipt = receiptDAO.selectById(receiptPlanId);
            //自动生成入库单编号
            String instorageCode = instorageService.generateInstorageCode();

            Instorage instorage = new Instorage();
            instorage.setInstorageBillCode(instorageCode);
            instorage.setInstorageType(receipt.getDocumentType());
            instorage.setReceiptPlanId(Long.parseLong(receiptPlanId));
            instorage.setCustomerCode(receipt.getCustomerCode());
            instorage.setCustomerName(receipt.getCustomerName());
            instorage.setSupplierCode(receipt.getSupplierCode());
            instorage.setSupplierName(receipt.getSupplierName());
            instorage.setRemark("收货单生成入库单");
            instorage.setState("0");
            instorage.setCreateTime(nowTime);
            instorage.setUpdateTime(nowTime);
            instorage.setCreateBy(userId);
            //入库单插入数据
            instorageDAO.insert(instorage);

            /**2.入库单详情插入数据**/
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            String[] receiptPlanDetailIdArr = receiptPlanDetailIdStr.split(",");
            String[] inStorageAmountArr = inStorageAmountStr.split(",");
            String[] inStorageWeightArr = inStorageWeightStr.split(",");
            String[] batchNoArr = batchNoStr.split(",");
            String[] productDateArr = productDateStr.split(",");
            String[] qualityPeriodArr = qualityPeriodStr.split(",");

            for(int i=0;i<receiptPlanDetailIdArr.length;i++){
                Map<String,Object> map = new HashMap<String,Object>();
                //入库单id
                map.put("instorageBillId",instorage.getId());
                map.put("receiptPlanDetailId",receiptPlanDetailIdArr[i]);
                map.put("inStorageAmount",inStorageAmountArr[i]);
                map.put("inStorageWeight",inStorageWeightArr[i]);

                //获取收货单详情
                ReceiptDetail receiptDetail = receiptDetailDAO.selectById(receiptPlanDetailIdArr[i]);
                //获取物料基础信息
                Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",receiptDetail.getMaterialCode()));
                materiel.setQualityPeriod(qualityPeriodArr[i]);
                //更新保质期
                materielService.updateById(materiel);

                //update by anss 2019-04-22 start
                //批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
                String batchNoCode = generateBatchNoCode(instorage.getSupplierCode(), instorage.getCustomerCode(), batchNoArr[i]);
                map.put("batchNo",batchNoCode);
                //update by anss 2019-04-22 end

                map.put("productDate",productDateArr[i]);
                detailList.add(map);

                //入库单详情插入数据
                receiptDetailDAO.insertInstorageDetail(map);

                /**3.根据收货单详情id更新收货计划详情的可拆分数量和可拆分重量**/
                receiptDetailDAO.updateSeparableAmountAndWeight(map);
            }

            /**4.根据收货单id更新收货计划单的状态为收货中**/
            receiptDAO.updateStateToReceipt(receiptPlanId);

            //查询收货详情单更新后的可拆分数量和可拆分重量
            EntityWrapper<ReceiptDetail> entity = new EntityWrapper<ReceiptDetail>();
            entity.eq("receipt_plan_id",receiptPlanId);
            List<ReceiptDetail> lstReceiptDetail = receiptDetailDAO.selectList(entity);
            if(lstReceiptDetail != null && lstReceiptDetail.size() > 0){
                //可拆分数量总和
                Double separableAmountTotal = 0d;
                //可拆分重量总和
                Double separableWeightTotal = 0d;
                for(ReceiptDetail receiptDetail : lstReceiptDetail){
                    separableAmountTotal += receiptDetail.getSeparableAmount()==null?0d:Double.parseDouble(receiptDetail.getSeparableAmount());
                    separableWeightTotal += receiptDetail.getSeparableWeight()==null?0d:Double.parseDouble(receiptDetail.getSeparableWeight());
                }
                //如果可拆分数量总和小于等于0并且可拆分重量总和小于等于0，则将收货单状态更新为收货完成
                if(separableAmountTotal <=0 && separableWeightTotal <=0){
                    /**5.根据收货单id更新收货计划单的状态为已完成**/
                    receiptDAO.updateStateToComplete(receiptPlanId);
                }
            }

        }
    }

    /**
     * 批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
     * @Author update by anss
     * @Date 2019-04-22
     * @param supplierCode 供应商代码
     * @param customerCode 客户代码
     * @param date 日期（yymmdd,6位）
     * SerialNumber 流水号（1，最长3位）
     * @return
     */
    @Override
    public String generateBatchNoCode(String supplierCode, String customerCode, String date){
        //供应商代码补位符7位*
        supplierCode = String.format("%-7s", supplierCode).replace(' ', '*');
        //客户代码补位符9位*
        customerCode = String.format("%-9s", customerCode).replace(' ', '*');
        //批次规则
        String bathNoRule = supplierCode + customerCode + "-" + date + "-";
        //流水号
        String seriaNumber = receiptDetailDAO.getMaxBatchNoCode(bathNoRule);

        return bathNoRule + seriaNumber;
    }
}
    