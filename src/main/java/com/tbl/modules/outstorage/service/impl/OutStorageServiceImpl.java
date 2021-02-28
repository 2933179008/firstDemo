package com.tbl.modules.outstorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.instorage.service.*;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author lcg
 * data 2019/1/14
 */
@Service("outstorageService")
public class OutStorageServiceImpl extends ServiceImpl<OutStorageDAO, OutStorageManagerVO> implements OutStorageService {

    @Autowired
    private OutStorageDAO outStorageDAO;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptDetailService receiptDetailService;

    @Autowired
    private InstorageService instorageService;

    @Autowired
    private InstorageDetailService instorageDetailService;

    @Autowired
    private OutStorageDetailService outStorageDetailService;

    @Autowired
    private PutBillService putBillService;


    /**
     * 列表展示
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        //收货单号
        String outstorage_bill_code = (String) params.get("outstorage_bill_code");
        //状态
        String outstorage_bill_type = (String) params.get("outstorage_bill_type");

        Page<OutStorageManagerVO> page = this.selectPage(
                new Query<OutStorageManagerVO>(params).getPage(),
                new EntityWrapper<OutStorageManagerVO>()
        );

        for (OutStorageManagerVO outStorageManagerVO : page.getRecords()) {
            if (StringUtils.isNotEmpty(outStorageManagerVO.getReceiptCode())) {
                Receipt receipt = receiptService.selectOne(new EntityWrapper<Receipt>().eq("receipt_code", outStorageManagerVO.getReceiptCode()));
                if (receipt != null) {
                    outStorageManagerVO.setReceiptCode(receipt.getReceiptCode());
                }
            }

            if (StringUtils.isNotEmpty(outStorageManagerVO.getInstorageCode())) {
                Instorage instorage = instorageService.selectOne(new EntityWrapper<Instorage>().eq("Instorage_bill_code", outStorageManagerVO.getInstorageCode()));
                if (instorage != null) {
                    outStorageManagerVO.setInstorageCode(instorage.getInstorageBillCode());
                }
            }
        }
        return new PageUtils(page);
    }

    /**
     * 获取系统中最大的出库单号
     *
     * @return
     */
    @Override
    public String getMaxBillCode() {
        //收货单编号
        String outstorageCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.OUTORAGE_CODE_FORMAT);
        //获取最大收货单编号
        String maxoutstorageCode = outStorageDAO.getMaxOutstorageCode();
        if (Strings.isNullOrEmpty(maxoutstorageCode)) {
            outstorageCode = "OU00000001";
        } else {
            Integer outstorageCode_count = Integer.parseInt(maxoutstorageCode.replace("OU", ""));
            outstorageCode = df.format(outstorageCode_count + 1);
        }
        return outstorageCode;
    }

    /**
     * 货主下拉框展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getCustomerList() {
        List<Map<String, Object>> list = outStorageDAO.getCustomerList();
        return list;
    }

    /**
     * 库位下拉框展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getAreaList() {
        List<Map<String, Object>> list = outStorageDAO.getAreaList();
        return list;
    }

    /**
     * 备料单下拉框展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getSpareList() {
        List<Map<String, Object>> list = outStorageDAO.getSpareList();
        return list;
    }

    /**
     * 收货单下拉框展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getReceipList() {
        List<Map<String, Object>> list = outStorageDAO.getReceipList();
        return list;
    }

    /**
     * 入库单下拉框展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getInstorageList() {
        List<Map<String, Object>> list = outStorageDAO.getInstorageList();
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> saveOutstorage(OutStorageManagerVO outStorageManagerVO) {
        Map<String, Object> map = Maps.newHashMap();
        boolean result = false;
        OutStorageManagerVO oldOutStorage = outStorageManagerVO.getId() == null ? null : outStorageDAO.selectById(outStorageManagerVO.getId());
        //当前时间
        String nowTime = DateUtils.getTime();
        if (outStorageManagerVO.getId() == null) {//新增保存
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            outStorageManagerVO.setCreateBy(user.getUserId());
            outStorageManagerVO.setCreateTime(nowTime);
            outStorageManagerVO.setOutstorageBillCode(this.getMaxBillCode());
            if (outStorageDAO.insert(outStorageManagerVO) > 0) {
                result = true;
            }
        } else {//编辑保存
            outStorageManagerVO.setUpdateTime(nowTime);
            if (outStorageDAO.updateById(outStorageManagerVO) > 0) {
                result = true;
            }
        }
        //跃库出库
        if (outStorageManagerVO.getOutstorageBillType().toString().equals(DyylConstant.OUTSTORAGE_TYPE2.toString())) {
            //添加为有RFID单据
            outStorageManagerVO.setBillType(DyylConstant.MATERIAL_RFID);
            outStorageDAO.updateById(outStorageManagerVO);

            if (StringUtils.isNotEmpty(outStorageManagerVO.getInstorageCode())) {
                Instorage instorage = instorageService.selectOne(new EntityWrapper<Instorage>().eq("instorage_bill_code", outStorageManagerVO.getInstorageCode()));
                if (instorage != null) {
                    //新增或者入库单编号修改情况下需要更新出库单详情
                    if (oldOutStorage == null || (oldOutStorage != null && !oldOutStorage.getInstorageCode().equals(outStorageManagerVO.getInstorageCode()))) {
                        //入库单编号修改情况下需要删掉以前关联的数据
                        if (oldOutStorage != null) {
                            outStorageDetailService.deleteByOutStorageId(outStorageManagerVO.getId());
                        }
                        List<InstorageDetail> instorageDetailList = instorageDetailService.selectList(new EntityWrapper<InstorageDetail>().eq("instorage_bill_id", instorage.getId()));
                        for (InstorageDetail instorageDetail : instorageDetailList) {
                            OutStorageDetailManagerVO outStorageDetailManagerVO = new OutStorageDetailManagerVO();
                            outStorageDetailManagerVO.setPositionCode(outStorageManagerVO.getPositionCode());
                            outStorageDetailManagerVO.setMaterialCode(instorageDetail.getMaterialCode());
                            outStorageDetailManagerVO.setMaterialName(instorageDetail.getMaterialName());
                            outStorageDetailManagerVO.setBatchNo(instorageDetail.getBatchNo());
                            outStorageDetailManagerVO.setUnit(instorageDetail.getUnit());
                            outStorageDetailManagerVO.setAmount(instorageDetail.getInstorageAmount());
                            outStorageDetailManagerVO.setSeparableAmount(instorageDetail.getInstorageAmount());
                            outStorageDetailManagerVO.setSeparableWeight(instorageDetail.getInstorageWeight());
                            outStorageDetailManagerVO.setWeight(instorageDetail.getInstorageWeight());
                            outStorageDetailManagerVO.setProductData(instorageDetail.getProductDate());
                            outStorageDetailManagerVO.setOutstorageBillId(outStorageManagerVO.getId().toString());
                            outStorageDetailService.insert(outStorageDetailManagerVO);
                        }
                    }
                } else {
                    result = false;
                    map.put("msg", "未找到对应的入库单，请确认后重新编辑选择");
                }
            } else {
                result = false;
                map.put("msg", "未获取入库单，请编辑重新选择");

            }
        } else if (outStorageManagerVO.getOutstorageBillType().toString().equals(DyylConstant.OUTSTORAGE_TYPE1.toString())) {
            //退货出库
            //添加为有RFID单据
            outStorageManagerVO.setBillType(DyylConstant.MATERIAL_RFID);
            outStorageDAO.updateById(outStorageManagerVO);

            //下拉框获取入库单中物料对应库存
//            if (StringUtils.isNotEmpty(outStorageManagerVO.getReceiptCode())) {
//                Receipt receipt = receiptService.selectOne(new EntityWrapper<Receipt>().eq("receipt_code", outStorageManagerVO.getReceiptCode()));
//                if (receipt != null) {
//                    //新增或者出库单编号修改情况下需要更新出库单详情
//                    if (oldOutStorage==null||(oldOutStorage!=null&&!oldOutStorage.getReceiptCode().equals(outStorageManagerVO.getReceiptCode()))) {
//                        //出库单编号修改情况下需要删掉以前关联的数据
//                        if(oldOutStorage!=null) {
//                            outStorageDetailService.deleteByOutStorageId(outStorageManagerVO.getId());
//                        }
//                        List<ReceiptDetail> receiptDetailList = receiptDetailService.selectList(new EntityWrapper<ReceiptDetail>().eq("receipt_plan_id", receipt.getId()));
//                        for (ReceiptDetail receiptDetail : receiptDetailList) {
//                            OutStorageDetailManagerVO outStorageDetailManagerVO = new OutStorageDetailManagerVO();
//                            outStorageDetailManagerVO.setMaterialCode(receiptDetail.getMaterialCode());
//                            outStorageDetailManagerVO.setMaterialName(receiptDetail.getMaterialName());
//                            outStorageDetailManagerVO.setBatchNo(receiptDetail.getBatchNo());
//                            outStorageDetailManagerVO.setUnit(receiptDetail.getUnit());
//                            outStorageDetailManagerVO.setAmount(receiptDetail.getActualReceiptAmount());
//                            outStorageDetailManagerVO.setWeight(receiptDetail.getActualReceiptWeight());
//                            outStorageDetailManagerVO.setSeparableWeight(receiptDetail.getActualReceiptWeight());
//                            outStorageDetailManagerVO.setSeparableAmount(receiptDetail.getActualReceiptAmount());
//                            outStorageDetailManagerVO.setProductData(receiptDetail.getProductDate());
//                            outStorageDetailManagerVO.setPositionCode(outStorageManagerVO.getPositionCode());
//                            outStorageDetailManagerVO.setOutstorageBillId(outStorageManagerVO.getId().toString());
//                            outStorageDetailService.insert(outStorageDetailManagerVO);
//                        }
//                    }
//
//                } else {
//                    result = false;
//                    map.put("msg", "未找到对应的入库单，请确认后重新编辑选择");
//                }
//            } else {
//                result = false;
//                map.put("msg", "未获取收货单，请编辑重新选择");
//            }
        }
        //返回ID
        map.put("outstorageId", outStorageManagerVO.getId());
        map.put("outstorageBillCode", outStorageManagerVO.getOutstorageBillCode());
        map.put("spareBillCode", outStorageManagerVO.getSpareBillCode());
        map.put("instorageCode", outStorageManagerVO.getInstorageCode());
        map.put("receiptCode", outStorageManagerVO.getReceiptCode());
        map.put("result", result);
        return map;
    }

    /**
     * 出库单删除
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> delOutStorage(String ids) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "";
        String[] idList = ids.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(idList));
        boolean result = false;
        if (!list.isEmpty() || list.size() > 0) {
            for (String id : list) {
                OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(id);
                //判断删除中是否包含出未提交之外的状态
                if (!outStorageManagerVO.getState().equals("0")) {
                    map.put("msg", "只可删除未提交的出库单");
                    map.put("result", false);
                    return map;
                }
            }
        }
        //判断要删除的出库单中是否有已经生成下架单的，如果存在，则不进行删除
        Integer count = outStorageDAO.isExist(ids);
        if (count > 0) {
            msg = "其中有出库单已经生成下架单,不能进行删除";
        } else {
            //TODO 讨论要不要判断备料单出库的时候的删除

            Integer resultCount = outStorageDAO.deleteBatchIds(list);
            if (resultCount > 0) {
                for (String id : list) {
                    List<OutStorageDetailManagerVO> outStorageDetailManagerVOList = outStorageDetailService.selectList(
                            new EntityWrapper<OutStorageDetailManagerVO>()
                                    .eq("outstorage_bill_id", Long.parseLong(id))
                    );
                    if (!outStorageDetailManagerVOList.isEmpty() || outStorageDetailManagerVOList.size() > 0) {
                        for (OutStorageDetailManagerVO outStorageDetailManagerVO : outStorageDetailManagerVOList) {
                            outStorageDetailService.deleteById(outStorageDetailManagerVO);
                        }
                    }
                }
                result = true;
                msg = "删除成功";
            } else {
                result = false;
                msg = "删除失败";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 根据ID进行审核
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> auditing(String id, String state) {
        Map<String, Object> map = Maps.newHashMap();
        OutStorageManagerVO outStorageManagerVO = this.selectById(id);
        outStorageManagerVO.setState(state);
        boolean result = this.updateById(outStorageManagerVO);
        map.put("result", result);
        return map;
    }

    /**
     * 获取设备的IP
     *
     * @param deviceIp
     * @return
     */
    @Override
    public Object getLineIp(String deviceIp) {
        Object lineIP = outStorageDAO.getLineIP(deviceIp);
        return lineIP;
    }

    /**
     * 通过rfid获取对应的产线的值
     *
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> getRfidIp(String rfid) {
        //通过rfid获取对应的出库单的ID
        Object outStorageId = outStorageDAO.outStorageId(rfid);
        Object lowerId = outStorageDAO.getLowerId(rfid);
        //通过出库单的ID获取对应的备料单的产线
        Map<String, Object> map = outStorageDAO.getRfidIp(outStorageId);
        map.put("lowerId", lowerId == null ? "" : lowerId.toString());
        return map;
    }
}


