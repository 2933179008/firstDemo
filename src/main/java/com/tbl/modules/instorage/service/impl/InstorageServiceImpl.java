package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.*;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.instorage.service.InstorageDetailService;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.instorage.service.QualityBillService;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.util.DeriveExcel;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.entity.StockChange;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: dyyl
 * @description: 入库单管理service实现
 * @author: zj
 * @create: 2019-01-15 14:25
 **/
@Service("instorageService")
public class InstorageServiceImpl extends ServiceImpl<InstorageDAO, Instorage> implements InstorageService {

    @Autowired
    private InstorageDAO instorageDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    @Autowired
    private InstorageDetailService instorageDetailService;
    @Autowired
    private ReceiptDAO receiptDAO;
    @Autowired
    private ReceiptDetailDAO receiptDetailDAO;
    @Autowired
    private PutBillService putBillService;
    @Autowired
    private PutBillDAO putBillDAO;
    @Autowired
    private PutBillDetailDAO putBillDetailDAO;
    @Autowired
    private QualityBillDAO qualityBillDAO;
    @Autowired
    private QualityBillService qualityBillService;
    @Autowired
    private StockChangeDAO stockChangeDAO;
    @Autowired
    private OutStorageDetailService outStorageDetailService;

    @Override
    public String generateInstorageCode() {
        //入库单编号
        String instorageCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.INSTORAGE_CODE_FORMAT);
        //获取最大入库单编号
        String maxInstorageCode = instorageDAO.getMaxInstorageCode();
        if (StringUtils.isEmptyString(maxInstorageCode)) {
            instorageCode = "IN00000001";
        } else {
            Integer maxInstorageCode_count = Integer.parseInt(maxInstorageCode.replace("IN", ""));
            instorageCode = df.format(maxInstorageCode_count + 1);
        }
        return instorageCode;
    }

    @Override
    public Page<Instorage> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if ("asc".equals(String.valueOf(params.get("order")))) {
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<Instorage> pageInstorage = new Page<Instorage>(pg, rows, sortname, order);
        //获取入库单列表
        List<Instorage> list = instorageDAO.getPageInstorageList(pageInstorage, params);

        return pageInstorage.setRecords(list);

    }

    @Override
    public Map<String, Object> saveInstorage(Instorage instorage) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean code = false;
        //当前时间
        String nowTime = DateUtils.getTime();
        //生产任务单号
        String productNo = "";
        if (instorage.getOutstorageBillId() != null) {
            //根据出库单id查询生产任务单号
            productNo = instorageDAO.getProductNo(instorage.getOutstorageBillId());
        }
        if (instorage.getId() == null) {//新增保存
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            instorage.setCreateBy(user.getUserId());
            instorage.setCreateTime(nowTime);
            instorage.setUpdateTime(nowTime);
            instorage.setState("0");
            instorage.setProductNo(productNo);
            //在并发的情况下，防止插入相同的收货单编号，同时插入相同的入库单编号，后插入的入库单编号会比页面上显示的大1
            instorage.setInstorageBillCode(this.generateInstorageCode());
            if (instorageDAO.insert(instorage) > 0) {//插入成功
                code = true;
            }
        } else {//修改保存
            instorage.setProductNo(productNo);
            if (instorageDAO.updateById(instorage) > 0) {//更新成功
                code = true;
            }
        }

        //若添加或修改成功 且入库单类型为退货入库，则添加退货入库详情
        if (code && instorage.getInstorageType().equals("2")) {
            //无论修改还是新增，先清关于此入库单的详情
            code = instorageDetailService.delete(new EntityWrapper<InstorageDetail>().eq("instorage_bill_id", instorage.getId()));
            if (code) {
                //获取出库单详情
                List<OutStorageDetailManagerVO> outStorageDetailManagerVOList = outStorageDetailService.selectList(
                        new EntityWrapper<OutStorageDetailManagerVO>()
                                .eq("outstorage_bill_id", instorage.getOutstorageBillId())
                );
                //遍历出库详情 插入入库详情
                for (OutStorageDetailManagerVO outStorageDetailManagerVO : outStorageDetailManagerVOList) {
                    InstorageDetail instorageDetail = new InstorageDetail();
                    instorageDetail.setInstorageBillId(instorage.getId());
                    instorageDetail.setMaterialName(outStorageDetailManagerVO.getMaterialName());
                    instorageDetail.setMaterialCode(outStorageDetailManagerVO.getMaterialCode());
                    instorageDetail.setBatchNo(outStorageDetailManagerVO.getBatchNo());
                    instorageDetail.setUnit(outStorageDetailManagerVO.getUnit());
                    instorageDetail.setProductDate(outStorageDetailManagerVO.getProductData());
                    instorageDetail.setInstorageAmount(outStorageDetailManagerVO.getSeparableAmount());
                    instorageDetail.setSeparableAmount(outStorageDetailManagerVO.getSeparableAmount());
                    instorageDetail.setInstorageWeight(outStorageDetailManagerVO.getSeparableWeight());
                    instorageDetail.setSeparableWeight(outStorageDetailManagerVO.getSeparableWeight());
                    instorageDetailDAO.insert(instorageDetail);
                }
            }
        }

        map.put("code", code);
        //返回插入后的入库单的id
        map.put("instorageId", instorage.getId());
        map.put("instorageBillCode", instorage.getInstorageBillCode());
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInstorageState(Long instorageId, String instorageType, String crossDocking, Long outstorageBillId, List<InstorageDetail> lstInstorageDetail) {

        /**1.更新入库单详情的可拆分数量和重量**/
        instorageDAO.updateDetailSeparableAmountAndWeight(instorageId);
        /**2.生成对应的质检单**/
        //对于越库的“采购入库”或“委托加工入库”类型的入库单不生成质检单
        if (!("1".equals(crossDocking) && ("0".equals(instorageType) || "1".equals(instorageType)))) {
            EntityWrapper<QualityBill> qualityBillEntity = new EntityWrapper<QualityBill>();
            qualityBillEntity.eq("instorage_bill_id", instorageId);
            Integer qualityBillCount = qualityBillDAO.selectCount(qualityBillEntity);
            if (qualityBillCount == 0) {//如果该入库单没有对应的质检单，则生成相应的质检单
                QualityBill qualityBill = new QualityBill();
                //生成质检单编号
                String qualityCode = qualityBillService.generateQualityCode();
                qualityBill.setQualityCode(qualityCode);
                qualityBill.setInstorageBillId(instorageId);
                Instorage instorage = instorageDAO.selectById(instorageId);
                //入库单编号
                String instorageBillCode = instorage.getInstorageBillCode();
                qualityBill.setInstorageBillCode(instorageBillCode);
                qualityBill.setState("0");
                qualityBill.setCreateTime(DateUtils.getTime());
                // shiro管理的session
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession();
                User user = (User) session.getAttribute(Const.SESSION_USER);
                qualityBill.setCreateBy(user.getUserId());
                qualityBillDAO.insert(qualityBill);
            }
            /**更新入库单状态为待入库**/
            instorageDAO.updateState(instorageId, "1");
        } else {//如果是越库类型的单据
            /**更新入库单状态为入库完成**/
            instorageDAO.updateState(instorageId, "3");
            /**库存变动表插入数据**/
            Instorage instorage = instorageDAO.selectById(instorageId);
            //入库单编号
            String instorageBillCode = instorage.getInstorageBillCode();
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            Long userId = user.getUserId();
            for (InstorageDetail instorageDetail : lstInstorageDetail) {
                StockChange stockChange = new StockChange();
                stockChange.setChangeCode(instorageBillCode);
                stockChange.setMaterialCode(instorageDetail.getMaterialCode());
                stockChange.setMaterialName(instorageDetail.getMaterialName());
                stockChange.setBatchNo(instorageDetail.getBatchNo());
                //设置“越库入库”类型
                stockChange.setBusinessType("12");
                stockChange.setInAmount(instorageDetail.getInstorageAmount());
                stockChange.setInWeight(instorageDetail.getInstorageAmount());
                stockChange.setCreateTime(DateUtils.getTime());
                stockChange.setCreateBy(userId);
                stockChangeDAO.insert(stockChange);
            }
        }
        //如果是“生产退货入库”类型的入库单
        if ("2".equals(instorageType)) {
            //更新出库单详情中物料的可拆分数量和重量
            instorageDAO.updateOutstorageBillDetail(outstorageBillId, lstInstorageDetail);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteInstorage(String instorageType, Long id) {
        EntityWrapper<InstorageDetail> entity = new EntityWrapper<InstorageDetail>();
        entity.eq("instorage_bill_id", id);
        //采购入库和委托加工入库类型的入库单的处理
        if ("0".equals(instorageType) || "1".equals(instorageType)) {
            //获取收货单主键id
            Long receiptPlanId = instorageDAO.selectById(id).getReceiptPlanId();
            /**1.将收货详情单的可拆分数量和可拆分重量回退**/
            List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(entity);
            if (lstInstorageDetail != null && lstInstorageDetail.size() > 0) {
                instorageDAO.updateReceiptSeparableAmountAndWeight(lstInstorageDetail, receiptPlanId);
            }
            /**2.将收货单的状态回退**/
            //业务场景（1）：当收货单为收货中状态并且对应的收货单详情的各物料的可拆分数量和可拆分重量与计划收货数量和计划收货重量都相等时，将收货单状态退回为待收货
            //业务场景（2）：当收货单为已完成状态并且对应的收货单详情的各物料的可拆分数量和可拆分重量有一个不为0时，将收货单状态退回为收货中

            //获取收货单状态
            String receiptState = receiptDAO.selectById(receiptPlanId).getState();
            EntityWrapper<ReceiptDetail> receiptDetailEntity = new EntityWrapper<ReceiptDetail>();
            receiptDetailEntity.eq("receipt_plan_id", receiptPlanId);
            //获取收货单详情list
            List<ReceiptDetail> lstReceiptDetail = receiptDetailDAO.selectList(receiptDetailEntity);
            //数量差值总和
            Double differAmountTotal = 0d;
            //重量差值总和
            Double differWeightTotal = 0d;

            if (lstReceiptDetail != null && lstReceiptDetail.size() > 0) {
                for (ReceiptDetail receiptDetail : lstReceiptDetail) {
                    //计划收货数量
                    Double planReceiptAmount = receiptDetail.getPlanReceiptAmount() == null ? 0d : Double.parseDouble(receiptDetail.getPlanReceiptAmount());
                    //计划收货重量
                    Double planReceiptWeight = receiptDetail.getPlanReceiptWeight() == null ? 0d : Double.parseDouble(receiptDetail.getPlanReceiptWeight());
                    //收货单详情可拆分数量
                    Double separableAmount = receiptDetail.getSeparableAmount() == null ? 0d : Double.parseDouble(receiptDetail.getSeparableAmount());
                    //收货单详情可拆分重量
                    Double separableWeight = receiptDetail.getSeparableWeight() == null ? 0d : Double.parseDouble(receiptDetail.getSeparableWeight());
                    //计划收货数量减去收货单详情可拆分数量的差值
                    Double differAmount = planReceiptAmount - separableAmount;
                    //计划收货重量减去收货单详情可拆分重量的差值
                    Double differWeight = planReceiptWeight - separableWeight;

                    differAmountTotal += differAmount;
                    differWeightTotal += differWeight;
                }
            }
            //业务场景（1）
            if ("2".equals(receiptState) && differAmountTotal <= 0 && differWeightTotal <= 0) {
                //更新收货单状态为待收货
                receiptDAO.updateState(receiptPlanId);
            }
            //业务场景（2）
            if ("3".equals(receiptState) && (differAmountTotal > 0 || differWeightTotal > 0)) {
                //更新收货单状态为收货中
                receiptDAO.updateStateToReceipt(String.valueOf(receiptPlanId));
            }
            //场景（3）
            if ("3".equals(receiptState) && differAmountTotal <= 0 && differWeightTotal <= 0) {
                //更新收货单状态为待收货
                receiptDAO.updateState(receiptPlanId);
            }
        }

        //删除入库单
        instorageDAO.deleteById(id);
        //删除入库单详情
        instorageDetailDAO.delete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generatePutBill(Long instorageId) {
        //当前时间
        String nowTime = DateUtils.getTime();
        //自动生成上架单编号
        String putBillCode = putBillService.generatePutBillCode();
        //获取入库单编号
        String instorageBillCode = instorageDAO.selectById(instorageId).getInstorageBillCode();

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        PutBill putBill = new PutBill();
        putBill.setCreateBy(user.getUserId());
        putBill.setCreateTime(nowTime);
        putBill.setUpdateTime(nowTime);
        putBill.setState("0");
        putBill.setPutBillCode(putBillCode);
        putBill.setInstorageBillId(instorageId);
        putBill.setInstorageBillCode(instorageBillCode);
        //插入上架单数据
        putBillDAO.insert(putBill);

        /**插入上架单详情数据**/
        //上架单主键id
        Long putBillId = putBill.getId();
        EntityWrapper<InstorageDetail> entity = new EntityWrapper<InstorageDetail>();
        entity.eq("instorage_bill_id", instorageId);
        //入库单详情list
        List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(entity);

        //插入上架单详情数据
        instorageDetailDAO.insertPutBillDetail(lstInstorageDetail, putBillId);


    }

    @Override
    public List<Map<String, Object>> getSelectOutstorageBillList(String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return instorageDAO.getSelectOutstorageBillList(page, queryString);
    }

    @Override
    public List<Map<String, Object>> getSelectReceiptPlanList(String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return instorageDAO.getSelectReceiptPlanList(page, queryString);
    }

    @Override
    public Integer getSelectOutstorageBillTotal(String queryString) {
        return instorageDAO.getSelectOutstorageBillTotal(queryString);
    }

    /**
     * 获取导出列
     *
     * @return List<Customer>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<Instorage> getAllList(String ids) {
        System.out.println(ids);
        List<Instorage> list = null;
        list = instorageDAO.getAllLists(StringUtils.stringToList(ids));
        list.forEach(a -> {

        });
        return list;
    }

    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Instorage> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "入库单管理表" + "(" + date + ")";
            if (path != null && !"".equals(path)) {
                sheetName = sheetName + ".xls";
            } else {
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-disposition", "attachment;filename="
                        + new String(sheetName.getBytes("gbk"), "ISO8859-1") + ".xls");
            }

            Map<String, String> mapFields = new LinkedHashMap<String, String>();
            mapFields.put("instorageBillCode", "入库单编号");
            mapFields.put("receiptCode", "收获计划单编号");
            mapFields.put("instorageTypeContent", "入库类型");
            mapFields.put("createTime", "入库时间");
            mapFields.put("userName", "入库人");
            mapFields.put("remark", "备注");
            mapFields.put("stateContent", "状态");

            //对入库单字段进行转译
            List instorageList = new ArrayList();
            for (Instorage is : list) {
                String instorageType = is.getInstorageType();
                String state = is.getState();
                //入库类型（0：采购入库；1：委托加工入库；2：生产退货入库；3：其他入库；）
                switch (instorageType) {
                    case "0":
                        is.setInstorageTypeContent("采购入库");
                        break;
                    case "1":
                        is.setInstorageTypeContent("委托加工入库");
                        break;
                    case "2":
                        is.setInstorageTypeContent("生产退货入库");
                        break;
                    case "3":
                        is.setInstorageTypeContent("其他入库");
                        break;
                    default:
                        is.setInstorageTypeContent("");
                }
                //状态（0：未提交；1：待入库；2：入库中；3：入库完成；）
                switch (state) {
                    case "0":
                        is.setStateContent("未提交");
                        break;
                    case "1":
                        is.setStateContent("待入库");
                        break;
                    case "2":
                        is.setStateContent("入库中");
                        break;
                    case "3":
                        is.setStateContent("入库完成");
                        break;
                    default:
                        is.setStateContent("");
                }
                instorageList.add(is);
            }

            DeriveExcel.exportExcel(sheetName, instorageList, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    