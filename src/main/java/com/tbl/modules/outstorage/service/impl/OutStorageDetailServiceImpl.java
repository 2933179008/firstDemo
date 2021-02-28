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
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.service.InstorageDetailService;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.outstorage.dao.LowerShelfDAO;
import com.tbl.modules.outstorage.dao.LowerShelfDetailDAO;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.dao.OutStorageDetailDAO;
import com.tbl.modules.outstorage.entity.*;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lcg
 * data 2019/1/17
 */
@Service("outStorageDetailService")
public class OutStorageDetailServiceImpl extends ServiceImpl<OutStorageDetailDAO, OutStorageDetailManagerVO> implements OutStorageDetailService {

    @Autowired
    private OutStorageDetailDAO outStorageDetailDAO;

    @Autowired
    private MaterielDAO materielDao;

    @Autowired
    private LowerShelfDAO lowerShelfDAO;

    @Autowired
    private LowerShelfDetailDAO lowerShelfDetailDAO;

    @Autowired
    private OutStorageDAO outStorageDAO;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private StockService stockService;

    @Autowired
    private SpareBillService spareBillService;

    @Autowired
    private SpareBillDetailService spareBillDetailService;

    @Autowired
    private InstorageService instorageService;

    @Autowired
    private InstorageDetailService instorageDetailService;

    @Autowired
    private DepotPositionService depotPositionService;

    @Autowired
    private MaterielService materielService;

    /**
     * 出库详情列表展示
     *
     * @param map
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> map) {
        String outstorageId = map.get("outStorageId").toString();
        String[] id = outstorageId.split(",");
        List<String> idList = new ArrayList<>(Arrays.asList(id));
        outstorageId = idList.get(idList.size() - 1);
        Page<OutStorageDetailManagerVO> outStorageDetailManagerVOPage = new Page<>();
        if (!Strings.isNullOrEmpty(outstorageId)) {
            outStorageDetailManagerVOPage = this.selectPage(
                    new Query<OutStorageDetailManagerVO>(map).getPage(),
                    new EntityWrapper<OutStorageDetailManagerVO>().eq("outstorage_bill_id", outstorageId)
            );
        }

        return new PageUtils(outStorageDetailManagerVOPage);
    }

    /**
     * 出库详情中的物料下拉框
     *
     * @param queryString
     * @param pageSize
     * @param pageNo
     * @return
     */
    @Override
    public List<Map<String, Object>> getSelectMaterialList(String positionCode, String customerCode, List<String> materialCodes, String billType, String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        List<Map<String, Object>> list = outStorageDetailDAO.getSelectMaterialList(positionCode, customerCode, materialCodes, billType, queryString, page);
        return list;
    }

    @Override
    public Integer getSelectMaterialTotal(String queryString) {
        return outStorageDetailDAO.getSelectMaterialTotal(queryString);
    }

    /**
     * 出库单详情物料添加
     *
     * @param outStorageId
     * @param materialContent
     * @return
     */
    @Override
    public Map<String, Object> addOutStorageDetail(String outStorageId, String materialContent) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "";
        String[] materialList = materialContent.split(",");

        List<OutStorageDetailManagerVO> list = new ArrayList<>();
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId);
        if (outStorageManagerVO == null) {
            map.put("result", false);
            return map;
        }

        for (String materialCode : materialList) {
            String[] materialCodeList = materialCode.split("_");

            //若备料单编号不为空，则添加物料必须在备料详情范围内
            if (StringUtils.isNotEmpty(outStorageManagerVO.getSpareBillCode())) {
                SpareBillManagerVO spareBillManagerVO = spareBillService.selectOne(
                        new EntityWrapper<SpareBillManagerVO>()
                                .eq("spare_bill_code", outStorageManagerVO.getSpareBillCode())
                );
                if (spareBillManagerVO == null) {
                    map.put("msg", "未找到关于此出库单的备料单");
                    map.put("result", false);
                    return map;
                }
                List<SpareBillDetailManagerVO> spareBillDetailManagerVOList = spareBillDetailService.selectList(
                        new EntityWrapper<SpareBillDetailManagerVO>()
                                .eq("spare_bill_id", spareBillManagerVO.getId())
                );

                if (spareBillDetailManagerVOList.isEmpty()) {
                    map.put("msg", "未找到关于此出库单的备料单详情");
                    map.put("result", false);
                    return map;
                }
                //判断备料单详情中是否存在所添加的物料
                boolean result = false;
                for (SpareBillDetailManagerVO spareBillDetailManagerVO : spareBillDetailManagerVOList) {
                    if (spareBillDetailManagerVO.getMaterialCode().trim().equals(materialCodeList[0])) {
                        result = true;
                        break;
                    }
                }
                if (!result) {
                    map.put("msg", "关于此出库单的备料单详情中不包含所添加的物料");
                    map.put("result", false);
                    return map;
                }
            }


            //判断对应的物料是否存在
            Integer count = outStorageDetailDAO.getMaterialCount(outStorageId, materialCodeList[0], materialCodeList[1], materialCodeList[3]);
            OutStorageDetailManagerVO outStorageDetailManagerVO = new OutStorageDetailManagerVO();
            if (count == 0) {
                //通过物料编码获取物料的名称
                //根据物料编号查询物料相关信息
                EntityWrapper<Materiel> entity = new EntityWrapper<>();
                entity.eq("materiel_code", materialCodeList[0]);
                List<Materiel> lstMaterial = materielDao.selectList(entity.eq("materiel_code", materialCodeList[0]));
                //将数据放入到List中
                outStorageDetailManagerVO.setMaterialCode(materialCodeList[0]);
                outStorageDetailManagerVO.setMaterialName(lstMaterial.get(0).getMaterielName());
                outStorageDetailManagerVO.setOutstorageBillId(outStorageId);
                outStorageDetailManagerVO.setBatchNo(materialCodeList[1]);
                outStorageDetailManagerVO.setProductData(materialCodeList[2]);
                outStorageDetailManagerVO.setUnit(lstMaterial.get(0).getUnit());
                outStorageDetailManagerVO.setPositionCode(materialCodeList[3]);
                list.add(outStorageDetailManagerVO);
            } else {
                map.put("msg", "已存在相同物料");
                map.put("result", false);
                return map;
            }
        }
        boolean result = false;
        //将数据插入到对应的详情表中去
        for (OutStorageDetailManagerVO outStorageDetailManagerVO : list) {
            Integer count = outStorageDetailDAO.insert(outStorageDetailManagerVO);
            if (count > 0) {
                result = true;
            }
        }
        map.put("result", result);
        return map;
    }

    /**
     * 单据提交判断是不是有详情
     *
     * @param id
     * @return
     */
    @Override
    public List<OutStorageDetailManagerVO> getDetailList(String id) {
        List<OutStorageDetailManagerVO> list = outStorageDetailDAO.getDetailList(id);
        return list;
    }

    /**
     * 更新状态
     *
     * @param id
     * @return
     */
    @Override
    public Object updateState(String id) {
        Object result = outStorageDetailDAO.updateState(id);
        return result;
    }

    @Override
    public Map<String, Object> updateDetailAmount(String id, String amount, String weight) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("msg", "未获取所需更改详情的信息");
            map.put("result", false);
            return map;
        }

        //获取出库单详情
        OutStorageDetailManagerVO outStorageDetailManagerVO = outStorageDetailDAO.selectById(id);
        if (outStorageDetailManagerVO == null) {
            map.put("msg", "未获取所需更改详情的信息");
            map.put("result", false);
            return map;
        }

        //获取出库单
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageDetailManagerVO.getOutstorageBillId());
        if (outStorageManagerVO == null) {
            map.put("msg", "未获取所需更改详情主表的信息");
            map.put("result", false);
            return map;
        }

        //获取库存
        Stock stock = stockService.selectOne(
                new EntityWrapper<Stock>()
                        .eq("material_type", outStorageManagerVO.getBillType())
                        .eq("material_code", outStorageDetailManagerVO.getMaterialCode())
                        .eq("batch_no", outStorageDetailManagerVO.getBatchNo())
                        .eq("position_code", outStorageDetailManagerVO.getPositionCode())
        );
        if (stock == null) {
            map.put("msg", "无对应库存");
            map.put("result", false);
            return map;
        }

        //退货出库
        if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {

            if (StringUtils.isNotEmpty(amount)) {
                //若库存数量小于出库单数量，则返回错误信息
                if (Double.valueOf(stock.getStockAmount()) < Double.valueOf(amount)) {
                    map.put("msg", "该物料库存数量不足");
                    map.put("result", false);
                    return map;
                }
                outStorageDetailManagerVO.setAmount(amount);
                outStorageDetailManagerVO.setSeparableAmount(amount);
                Integer count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                if (count > 0) {
                    map.put("msg", "更新数量成功");
                    map.put("result", true);
                }
            }
            if (StringUtils.isNotEmpty(weight)) {
                //若库存数量小于出库单数量，则返回错误信息
                if (Double.parseDouble(stock.getStockWeight()) < Double.parseDouble(weight)) {
                    map.put("msg", "该物料库存重量不足");
                    map.put("result", false);
                    return map;
                }
                outStorageDetailManagerVO.setWeight(weight);
                outStorageDetailManagerVO.setSeparableWeight(weight);
                Integer count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                if (count > 0) {
                    map.put("msg", "更新重量成功");
                    map.put("result", true);
                }
            }

        } else if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE2)) {
            Instorage instorage = instorageService.selectOne(new EntityWrapper<Instorage>().eq("instorage_code", outStorageManagerVO.getInstorageCode()));
            List<InstorageDetail> instorageDetailList = instorageDetailService.selectList(
                    new EntityWrapper<InstorageDetail>()
                            .eq("instorage_bill_id", instorage.getId())
            );

            for (InstorageDetail instorageDetail : instorageDetailList) {

                Integer count = 0;
                if (instorageDetail.getMaterialCode().equals(outStorageDetailManagerVO.getMaterialCode())) {
                    //重量
                    if (StringUtils.isNotEmpty(weight)) {
                        if (Double.parseDouble(instorageDetail.getInstorageWeight()) < Double.parseDouble(weight)) {
                            map.put("msg", "跃库出库重量超出对应的跃库入库单重量");
                            map.put("result", false);
                            return map;
                        } else {
                            outStorageDetailManagerVO.setWeight(weight);
                            outStorageDetailManagerVO.setSeparableWeight(weight);
                            count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                            if (count > 0) {
                                map.put("msg", "更新重量成功");
                                map.put("result", true);
                            }
                        }
                    }
                    //数量
                    if (StringUtils.isNotEmpty(amount)) {
                        if (Double.parseDouble(instorageDetail.getInstorageAmount()) < Double.parseDouble(amount)) {
                            map.put("msg", "跃库出库数量超出对应的跃库入库单数量");
                            map.put("result", false);
                            return map;
                        } else {
                            outStorageDetailManagerVO.setAmount(amount);
                            outStorageDetailManagerVO.setSeparableAmount(amount);
                            count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                            if (count > 0) {
                                map.put("msg", "更新数量成功");
                                map.put("result", true);
                            }
                        }
                    }
                }
                if (count == 0) {
                    map.put("msg", "入库单中未找到对应物料");
                    map.put("result", false);
                }
            }
        } else {
            if (StringUtils.isNotEmpty(amount)) {
                //库存剩余可用库存数量
                Double aviAmount = Double.valueOf(stock.getAvailableStockAmount()) - Double.valueOf(stock.getOccupyStockAmount());

                //若可用库存数量小于出库单数量，则返回错误信息
                if (aviAmount < Double.valueOf(amount)) {
                    map.put("msg", "此物料库存可用数量不足");
                    map.put("result", false);
                    return map;
                }
                outStorageDetailManagerVO.setAmount(amount);
                outStorageDetailManagerVO.setSeparableAmount(amount);
                Integer count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                if (count > 0) {
                    map.put("msg", "更新数量成功");
                    map.put("result", true);
                }
            }

            if (StringUtils.isNotEmpty(weight)) {
                //库存剩余可用库存重量
                Double aviWeight = Double.parseDouble(stock.getAvailableStockWeight()) - Double.parseDouble(stock.getOccupyStockWeight());

                //若可用库存重量小于出库单重量，则返回错误信息
                if (aviWeight < Double.parseDouble(weight)) {
                    map.put("msg", "此物料可用库存重量不足");
                    map.put("result", false);
                    return map;
                }
                outStorageDetailManagerVO.setWeight(weight);
                outStorageDetailManagerVO.setSeparableWeight(weight);
                Integer count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
                if (count > 0) {
                    map.put("msg", "更新重量成功");
                    map.put("result", true);
                }
            }

        }
//        if (StringUtils.isNotBlank(batchNo)) {
//            //获取库存
//            Stock stock = stockService.selectOne(
//                    new EntityWrapper<Stock>()
//                            .eq("material_type", outStorageManagerVO.getBillType())
//                            .eq("material_code", outStorageDetailManagerVO.getMaterialCode())
//                            .eq("batch_no", batchNo)
//                            .eq("position_code", outStorageDetailManagerVO.getPositionCode())
//            );
//            if (stock == null) {
//                map.put("msg", "无对应库存");
//                map.put("result", false);
//                return map;
//            }
//
//            //退货出库
//            if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
//                //库存剩余不可用库存
//                Integer noAViAmount = Integer.parseInt(stock.getStockAmount()) - Integer.parseInt(stock.getAvailableStockAmount());
//                //若不可用库存数量小于出库单数量，则返回错误信息
//                if (noAViAmount < Integer.parseInt(outStorageDetailManagerVO.getAmount())) {
//                    map.put("msg", "该物料可用库存数量不足");
//                    map.put("result", false);
//                    return map;
//                }
//            } else {
//                //库存剩余可用库存
//                Integer aviAmount = Integer.parseInt(stock.getAvailableStockAmount()) - Integer.parseInt(stock.getOccupyStockAmount());
//                //判断库存详情中数量，若不为空或“0”，则与库存中可用数量对比
//                if (StringUtils.isNotBlank(outStorageDetailManagerVO.getAmount()) && !outStorageDetailManagerVO.getAmount().equals("0")) {
//                    //若可用库存数量小于出库单数量，则返回错误信息
//                    if (aviAmount < Integer.parseInt(outStorageDetailManagerVO.getAmount())) {
//                        map.put("msg", "此物料库存可用数量不足");
//                        map.put("result", false);
//                        return map;
//                    }
//                }
//            }
//
//            outStorageDetailManagerVO.setBatchNo(batchNo);
//            Integer count = outStorageDetailDAO.updateById(outStorageDetailManagerVO);
//            if (count > 0) {
//                map.put("msg", "更新批次号成功");
//                map.put("result", true);
//            }
//        }
        return map;
    }

    /**
     * 生成下架单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addshelve(Long userId, OutStorageManagerVO outStorageManagerVO, String id) {
        String msg = "";
        boolean result = false;
        //更新出库单状态为已经生成下架单
        outStorageManagerVO.setState("2");
        Map<String, Object> map = Maps.newHashMap();
        //记录生成时间
        String nowTime = DateUtils.getTime();
        //下架单的编号的处理
        //下架单编号
        String XjCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.XJ_CODE_FORMAT);
        //获取最大下架单编号
        String maxXjCode = outStorageDetailDAO.getMaxXJstorageCode();
        if (Strings.isNullOrEmpty(maxXjCode)) {
            XjCode = "XJ00000001";
        } else {
            Integer outstorageCode_count = Integer.parseInt(maxXjCode.replace("XJ", ""));
            XjCode = df.format(outstorageCode_count + 1);
        }
        //根据出库单ID获取出库详情
        List<OutStorageDetailManagerVO> outStorageDetailManagerVOList = outStorageDetailDAO.selectList(
                new EntityWrapper<OutStorageDetailManagerVO>()
                        .eq("outstorage_bill_id", outStorageManagerVO.getId())
        );
        if (outStorageDetailManagerVOList.isEmpty() || outStorageDetailManagerVOList.size() == 0) {
            msg = "此出库单无出库详情";
            return map;
        }
        LowerShelfBillVO lowerShelfBillVO = new LowerShelfBillVO();
        lowerShelfBillVO.setCreateBy(userId);
        lowerShelfBillVO.setCreateTime(nowTime);
        lowerShelfBillVO.setCustomerCode(outStorageManagerVO.getCustomerCode());
        lowerShelfBillVO.setLowerShelfBillCode(XjCode);
        lowerShelfBillVO.setOutstorageBillCode(outStorageManagerVO.getOutstorageBillCode());
        lowerShelfBillVO.setOutstorageBillId(id);
        lowerShelfBillVO.setMaterialType(outStorageManagerVO.getMaterialType());
        lowerShelfBillVO.setBillType(outStorageManagerVO.getBillType());
        lowerShelfBillVO.setState("0");
        //生成下架单主表
        Integer lowerId = lowerShelfDAO.insert(lowerShelfBillVO);

        if (lowerId > 0) {
            //有RFID生成下架但
            if (outStorageManagerVO.getBillType().equals(DyylConstant.MATERIAL_RFID)) {
                //将库存中的占用库存直接当做下架单的详情插入
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList = outStorageDetailDAO.getoccupystock(outStorageManagerVO.getOutstorageBillCode());
                for (Map maps : mapList) {
                    LowerShelfBillDetailVO lowerShelfBillDetailVO = new LowerShelfBillDetailVO();
                    lowerShelfBillDetailVO.setLowerShelfBillId(lowerShelfBillVO.getId().toString());
                    lowerShelfBillDetailVO.setMaterialCode(maps.get("material_code").toString());
                    lowerShelfBillDetailVO.setMaterialName(maps.get("material_name").toString());
                    lowerShelfBillDetailVO.setBatchNo(maps.get("batch_no").toString());
                    lowerShelfBillDetailVO.setPositionCode(maps.get("position_code").toString());
                    lowerShelfBillDetailVO.setAmount(maps.get("occupy_stock_amount").toString());
                    lowerShelfBillDetailVO.setWeight(maps.get("occupy_stock_weight").toString());
                    lowerShelfBillDetailVO.setState("0");
                    lowerShelfBillDetailVO.setPositionCode(maps.get("position_code").toString());
                    lowerShelfBillDetailVO.setRfid(maps.get("rfid").toString());
                    lowerShelfBillDetailVO.setTransform(maps.get("transform").toString());
                    lowerShelfBillDetailVO.setProductData((String) maps.get("product_data"));
                    Integer lowerDetailID = lowerShelfDetailDAO.insert(lowerShelfBillDetailVO);
                    if (lowerDetailID > 0) {
                        msg = "生成下架单成功";
                        result = true;
//                        //更新出库单中可拆分数量/重量数据
//                        for (OutStorageDetailManagerVO outStorageDetailManagerVO : outStorageDetailManagerVOList) {
//                            if (outStorageDetailManagerVO.getMaterialCode().equals(lowerShelfBillDetailVO.getMaterialCode())
//                                    && outStorageDetailManagerVO.getBatchNo().equals(lowerShelfBillDetailVO.getBatchNo())
//                                    && outStorageDetailManagerVO.getPositionCode().equals(lowerShelfBillDetailVO.getPositionCode())) {
//                                if (Double.valueOf(outStorageDetailManagerVO.getAmount()) <= Double.valueOf(lowerShelfBillDetailVO.getAmount())) {
//                                    outStorageDetailManagerVO.setSeparableAmount("0");
//                                } else {
//                                    Double amount = Double.valueOf(outStorageDetailManagerVO.getAmount()) - Double.valueOf(lowerShelfBillDetailVO.getAmount());
//                                    outStorageDetailManagerVO.setSeparableAmount(amount.toString());
//                                }
//
//                                if (Double.parseDouble(outStorageDetailManagerVO.getWeight()) <= Double.parseDouble(lowerShelfBillDetailVO.getAmount())) {
//                                    outStorageDetailManagerVO.setSeparableWeight("0.0");
//                                } else {
//                                    Double weight = Double.parseDouble(outStorageDetailManagerVO.getWeight()) - Double.parseDouble(lowerShelfBillDetailVO.getWeight());
//                                    outStorageDetailManagerVO.setSeparableWeight(weight.toString());
//                                }
//                                outStorageDetailDAO.updateById(outStorageDetailManagerVO);
//                            }
//                        }
                    } else {
                        msg = "生成下架单失败";
                    }
                }
            } else {
                for (OutStorageDetailManagerVO osd : outStorageDetailManagerVOList) {
                    Stock stock = stockService.selectOne(
                            new EntityWrapper<Stock>()
                                    .eq("material_type", outStorageManagerVO.getBillType())
                                    .eq("material_code", osd.getMaterialCode())
                                    .eq("batch_no", osd.getBatchNo())
                                    .eq("position_code", osd.getPositionCode())
                    );
                    LowerShelfBillDetailVO lowerShelfBillDetailVO = new LowerShelfBillDetailVO();
                    lowerShelfBillDetailVO.setLowerShelfBillId(lowerShelfBillVO.getId().toString());
                    lowerShelfBillDetailVO.setMaterialCode(stock.getMaterialCode());
                    lowerShelfBillDetailVO.setMaterialName(stock.getMaterialName());
                    lowerShelfBillDetailVO.setBatchNo(stock.getBatchNo());
                    lowerShelfBillDetailVO.setPositionCode(stock.getPositionCode());
                    lowerShelfBillDetailVO.setAmount(osd.getAmount());
                    lowerShelfBillDetailVO.setWeight(osd.getWeight());
                    lowerShelfBillDetailVO.setState("0");
                    lowerShelfBillDetailVO.setTransform(stock.getMaterielPower());
                    lowerShelfBillDetailVO.setProductData(stock.getProductDate());
                    Integer lowerDetailID = lowerShelfDetailDAO.insert(lowerShelfBillDetailVO);
                    if (lowerDetailID > 0) {
                        msg = "生成下架单成功";
                        result = true;
                        osd.setWeight("0.0");
                        osd.setAmount("0");
                        outStorageDetailDAO.updateById(osd);
                    } else {
                        msg = "生成下架单失败";
                    }
                }
            }
        } else {
            msg = "生成下架单失败";
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        outStorageManagerVO.setState("3");
        //更新下架单的状态
        outStorageDAO.updateById(outStorageManagerVO);

        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 通过料编号以及批次号查询库存中剩余的数量是否能够进行出库
     *
     * @param materialNo
     * @param batchNo
     * @return
     */
    @Override
    public Object getstockAmount(String materialNo, String batchNo, String materialType) {
        Object amount = outStorageDetailDAO.getstockAmount(materialNo, batchNo, materialType);
        return amount;
    }

    /**
     * 根据物料编号以及批次号进行库存物料的锁定
     *
     * @param materialNo
     * @param batchNo
     * @param amount
     * @param outStorageId 出库单ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> getMaterialList(String materialNo, String batchNo, String amount, String weight, String outStorageId, String rfidList, Long outStorageDetailId) {
        Map<String, Object> map = Maps.newHashMap();
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId);
        List<String> rfids = new ArrayList<>();
        if (StringUtils.isNotBlank(rfidList)) {
            rfids = Arrays.asList(rfidList.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
        }

        List<Map<String, Object>> stockList = new ArrayList<>();
        //有RFID出库
        if (outStorageManagerVO.getBillType().equals(DyylConstant.MATERIAL_RFID)) {
            //获取符合条件的物料绑定详情
            List<Map<String, Object>> materialList = outStorageDetailDAO.getMaterialList(materialNo, batchNo, rfids);
            //判断对应的可用的rfid 是否数量/重量足够，如果数量/重量足够的话，则没有货权转移，如果数量不够的话，查看当前是否经过了货权转移
            Integer number = outStorageDetailDAO.getAmount(materialNo, batchNo, rfids);
            Double bindWeight = outStorageDetailDAO.getWeight(materialNo, batchNo, rfids);

            if (number == null || bindWeight == null) {
                map.put("msg", "已有出库单占用此物料（或绑定数量/重量为0 或所选物料不存在绑定关系），无法提交");
                map.put("result", false);
                return map;
            }
            if (number < Double.valueOf(amount) || bindWeight < Double.valueOf(weight)) {
                //查询该物料的货权转移的数据源
                Map<String, Object> dataSource = outStorageDetailDAO.getDataSource(materialNo, batchNo, outStorageManagerVO.getMaterialType());
                if (dataSource != null && !dataSource.isEmpty()) {
                    String availableRfid = (String) dataSource.get("available_rfid");
                    List<String> availableRfids = Arrays.asList(availableRfid.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                    //该物料存在货权转移的情况
                    stockList = outStorageDetailDAO.getMaterialList((String) dataSource.get("material_code"), (String) dataSource.get("batch_no"), availableRfids);
                    map = CommonGenerationMethod(outStorageId, amount, weight, materialList, "1", number, bindWeight, stockList);
                } else {
                    map.put("msg", "物料数量不足");
                    map.put("result", false);
                }
            } else {
                //物料不存在货权转移的情况
                map = CommonGenerationMethod(outStorageId, amount, weight, materialList, "0", number, bindWeight, stockList);
            }
        } else {
            OutStorageDetailManagerVO outStorageDetailManagerVO = outStorageDetailDAO.selectById(outStorageDetailId);
            if (outStorageDetailManagerVO == null) {
                map.put("msg", "出库详情ID未获取数据");
                map.put("result", false);
                return map;
            }
            Stock stock = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("position_code", outStorageDetailManagerVO.getPositionCode())
                            .eq("material_code", materialNo)
                            .eq("batch_no", batchNo)
                            .eq("material_type", outStorageManagerVO.getBillType())
            );
            if (stock == null) {
                map.put("msg", "未找到符合条件的库存信息");
                map.put("result", false);
                return map;
            }
            //判断库存是否足够占用
            if (Double.valueOf(stock.getAvailableStockAmount()) >= Double.valueOf(amount) || Double.parseDouble(stock.getAvailableStockWeight()) >= Double.parseDouble(weight)) {
                if (!Strings.isNullOrEmpty(stock.getOutstorageBillCode())) {
                    String outstorageBillCode = stock.getOutstorageBillCode() + "," + outStorageManagerVO.getOutstorageBillCode();
                    stock.setOutstorageBillCode(outstorageBillCode);
                    Double occupyStockAmount = Double.valueOf(stock.getOccupyStockAmount()) + Double.valueOf(amount);
                    Double occupyStockWeight = Double.parseDouble(stock.getOccupyStockWeight()) + Double.parseDouble(weight);
                    stock.setOccupyStockAmount(occupyStockAmount.toString());
                    stock.setOccupyStockWeight(occupyStockWeight.toString());
                } else {
                    stock.setOutstorageBillCode(outStorageManagerVO.getOutstorageBillCode());
                    stock.setOccupyStockAmount(amount);
                    stock.setOccupyStockWeight(weight);
                }
                stockService.updateById(stock);
                map.put("mag", "占用成功");
                map.put("result", true);
            } else {
                //若库存不足，占用全部库存，并在出库单中减去已占用部分
//                stock.setOccupyStockAmount(stock.getAvailableStockAmount());
//                stock.setOutstorageBillCode(outStorageManagerVO.getOutstorageBillCode());
//                Integer number = Integer.parseInt(amount) - Integer.parseInt(stock.getAvailableStockAmount());
//                outStorageDetailManagerVO.setAmount(number.toString());
//                stockService.updateById(stock);
//                outStorageDetailDAO.updateById(outStorageDetailManagerVO);
//                map.put("mag","库存不足，已占用全部库存，剩余["+number+outStorageDetailManagerVO.getUnit()+"]需要占用");
//                map.put("result",true);
                //返回占用失败原因
                map.put("mag", "库存不足，提交失败");
                map.put("result", false);
                return map;
            }
        }

        return map;
    }


    /**
     * 公共生成方法
     *
     * @param outStorageId 出库单ID
     * @param occupyAmount 出库数量
     * @param materialList 没有货权转移的出库单的详情
     * @param type         类型 1表示货权转移 0表示没有货权转移
     * @param stockAmount  没有货权转移是的可用的rfid 的总体数量
     * @param stockList    货权转移时对应的出库的详情
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> CommonGenerationMethod(String outStorageId, String occupyAmount, String occupyweight, List<Map<String, Object>> materialList, String type, Integer stockAmount, Double stockWeight, List<Map<String, Object>> stockList) {
        Map<String, Object> map = Maps.newHashMap();
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId);
        String msg = "提交成功";
        boolean result = true;
        //先将可以出库的物料进行占用
        for (Map mapList : materialList) {
            String mfId = mapList.get("id").toString();
            Double amount = Double.valueOf(mapList.get("amount").toString());
            Double weight = Double.parseDouble(mapList.get("weight").toString());
            String batchNo = mapList.get("batch_rule").toString();
            Double occupiedmaterialAmount = amount - Double.valueOf(occupyAmount);
            Double occupiedmaterialWeight = weight - Double.parseDouble(occupyweight);
            String rfid = (String) mapList.get("rfid");
            String materialCode = mapList.get("materiel_code").toString();
            if (Strings.isNullOrEmpty(occupyAmount) || Strings.isNullOrEmpty(occupyweight) || Strings.isNullOrEmpty(materialCode) || Strings.isNullOrEmpty(batchNo) || Strings.isNullOrEmpty(mfId) || amount == null || weight == null) {
                map.put("msg", "系统错误：部分参数未获取,请重试");
                map.put("result", false);
                return map;
            }
            if (occupiedmaterialAmount >= 0 && occupiedmaterialWeight >= 0) {
                if (!outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE0)) {
                    //表示的不是领料出库,则可以进行不是全部的占用,只占用部数据,如果是领料出库,则占用全部库存
                    //占用绑定详情库存
                    outStorageDetailDAO.occupiedInventory(mfId, occupyAmount, occupyweight, "0", outStorageManagerVO.getOutstorageBillCode());
                    //开始占用库存表中的库存
                    outStorageDetailDAO.occupiedStock(occupyAmount, occupyweight, rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                    //将已经锁定的物料减除,用剩余的物料去锁定剩余的物料
                    occupyAmount = occupiedmaterialAmount.toString();
                    occupyweight = occupiedmaterialWeight.toString();
                } else {
                    //占用绑定详情库存
                    outStorageDetailDAO.occupiedInventory(mfId, amount.toString(), weight.toString(), "0", outStorageManagerVO.getOutstorageBillCode());
                    //开始占用库存表中的库存
                    outStorageDetailDAO.occupiedStock(amount.toString(), weight.toString(), rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                    occupyAmount = "0";
                    occupyweight = "0.0";
                }

                msg = "提交成功";
                result = true;
                //表示当前的数据已经满足
                break;
            } else {
                //开始占用物料绑定表中的库存
                outStorageDetailDAO.occupiedInventory(mfId, amount.toString(), weight.toString(), "0", outStorageManagerVO.getOutstorageBillCode());
                //开始占用库存中表中的库存
                outStorageDetailDAO.occupiedStock(amount.toString(), weight.toString(), rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                Double oa = 0 - occupiedmaterialAmount;
                Double ow = 0 - occupiedmaterialWeight;
                occupyAmount = oa.toString();
                occupyweight = ow.toString();

            }
        }
        //如果是存在货权转移的物料,则进行货权转移的处理
        if ("1".equals(type)) {
            Double remainderAmount = Double.valueOf(occupyAmount) - stockAmount;
            Double remainderWeight = Double.parseDouble(occupyweight) - stockWeight;
            for (Map mapList : stockList) {
                String mfId = mapList.get("id").toString();
                Double amount = Double.valueOf(mapList.get("amount").toString());
                Double weight = Double.parseDouble(mapList.get("weight").toString());
                String batchNo = mapList.get("batch_rule").toString();
                Double occupiedAmount = remainderAmount - amount;
                Double occupiedWeight = remainderWeight - weight;
                String rfid = mapList.get("rfid").toString();
                String materialCode = mapList.get("materiel_code").toString();
                if (occupiedAmount < 0 && occupiedWeight < 0) {
                    if (!outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE0)) {
                        //表示的不是领料出库,则可以进行不是全部的占用,只占用部数据,如果是领料出库,则占用全部库存
                        //占用绑定详情库存
                        outStorageDetailDAO.occupiedInventory(mfId, occupyAmount, occupyweight, "0", outStorageManagerVO.getOutstorageBillCode());
                        //开始占用库存表中的库存
                        outStorageDetailDAO.occupiedStock(occupyAmount, occupyweight, rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                    } else {
                        //占用绑定详情库存
                        outStorageDetailDAO.occupiedInventory(mfId, amount.toString(), weight.toString(), "0", outStorageManagerVO.getOutstorageBillCode());
                        //开始占用库存表中的库存
                        outStorageDetailDAO.occupiedStock(amount.toString(), weight.toString(), rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                    }
                    msg = "提交成功";
                    result = true;
                    //表示当前的数据已经满足
                    break;
                } else {
                    //开始占用物料绑定表中的库存
                    outStorageDetailDAO.occupiedInventory(mfId, amount.toString(), weight.toString(), "1", outStorageManagerVO.getOutstorageBillCode());
                    //开始占用库存中表中的库存
                    outStorageDetailDAO.occupiedStock(amount.toString(), weight.toString(), rfid, outStorageManagerVO.getOutstorageBillCode(), batchNo, materialCode);
                }
                //将已经锁定的物料减除,用剩余的物料去锁定剩余的物料
                remainderAmount = occupiedAmount;
                remainderWeight = occupiedWeight;
            }
        }
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 物料绑定拆分
     *
     * @param outStorageId 出库单ID
     * @param occupyAmount 锁定数量
     * @param materialList 拆分后的详情数量(拆分详情ID,需要物料编号materialCode,物料名称materialName,RFID编号rfid,实际数量weight) 查询的时候按照weight的顺序查询从小到大
     * @return
     */
    @Override
    public Map<String, Object> stockBindSpilt(Long userId, String materialNo, String batchNo, String outStorageId, String occupyAmount, String occupyWight, List<Map<String, Object>> materialList) {
        Map<String, Object> map = Maps.newHashMap();
        List<Map<String, Object>> stockList = new ArrayList<>();
        //拆分的时候肯定不存在货权转移的情况
        map = CommonGenerationMethod(outStorageId, occupyAmount, occupyWight, materialList, "0", null, null, stockList);
        //然后判断对应的出库单是否已经生成下架单,如果已经生成下架单,则更新下架单详情,如果没有生成对应的下架单,则不进行处理
        Integer count = lowerShelfDAO.getDtailCount(outStorageId);
        if (count > 0) {
            //先删除下架单详情数据
            lowerShelfDetailDAO.deleteLowerShelDetail(outStorageId);
            OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId);
            //重新生成下架单
            this.addshelve(userId, outStorageManagerVO, outStorageId);
        }
        return map;
    }

    /**
     * 查询库存中的可用的RFID
     *
     * @param materialNo
     * @param batchNo
     * @return
     */
    @Override
    public String getRfidList(String materialNo, String batchNo) {
        String rfid = "";
        List<String> rfidList = outStorageDetailDAO.getRfidList(batchNo, materialNo);
        if (rfidList != null && rfidList.size() > 0) {
            for (String ss : rfidList) {
                rfid += ss + ",";
            }
            return rfid.substring(0, rfid.length() - 1);
        } else {
            return rfid;
        }
    }

    /**
     * 根据出库单号删除出库单详情
     *
     * @param outStorageId
     */
    @Override
    public void deleteByOutStorageId(Long outStorageId) {
        outStorageDetailDAO.deleteByOutStorageId(outStorageId);
    }
}
