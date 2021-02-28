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
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.outstorage.dao.LowerShelfDAO;
import com.tbl.modules.outstorage.dao.LowerShelfDetailDAO;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.dao.OutStorageDetailDAO;
import com.tbl.modules.outstorage.entity.*;
import com.tbl.modules.outstorage.service.LowerShelfDetailService;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

/**
 * @author lcg
 * data 2019/2/17
 */
@Service("LowerShelfDetailService")
public class LowerShelfDetailServiceImpl extends ServiceImpl<LowerShelfDetailDAO, LowerShelfBillDetailVO> implements LowerShelfDetailService {

    @Autowired
    private LowerShelfDetailDAO lowerShelfDetailDAO;

    @Autowired
    private LowerShelfDAO lowerShelfDAO;

    @Autowired
    private OutStorageDAO outStorageDAO;

    @Autowired
    private OutStorageDetailDAO outStorageDetailDAO;

    @Autowired
    private DepotPositionService depotPositionService;

    @Autowired
    private StockChangeDAO stockChangeDAO;

    @Autowired
    private SpareBillDetailService spareBillDetailService;

    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @Autowired
    private OutStorageDetailService outStorageDetailService;

    @Autowired
    private SpareBillService spareBillService;

    /**
     * 下架单详情列表分页
     *
     * @param map
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> map) {
        String lowerShelfBillId = map.get("lowerShelfBillId").toString();
        Page<LowerShelfBillDetailVO> page = new Page<>();
        if (!Strings.isNullOrEmpty(lowerShelfBillId)) {
            page = this.selectPage(
                    new Query<LowerShelfBillDetailVO>(map).getPage(),
                    new EntityWrapper<LowerShelfBillDetailVO>().eq("lower_shelf_bill_id", lowerShelfBillId)
            );
        }

        return new PageUtils(page);
    }

    /**
     * 通过下架单详情Id获取对应的出库单ID
     *
     * @param lowerDetailId
     * @return
     */
    @Override
    public Object getOutStorageId(String lowerDetailId) {
        return lowerShelfDetailDAO.getOutStorageId(lowerDetailId);
    }

    /**
     * 判断对应的下架单是否全部完成
     *
     * @param lowerDetailId
     * @return
     */
    @Override
    public Integer lowerCount(String lowerDetailId) {
        return lowerShelfDetailDAO.lowerCount(lowerDetailId);
    }

    @Override
    public Integer outStorageCount(String outstorageId) {
        return lowerShelfDAO.outStorageCount(outstorageId);
    }

    @Override
    public List<OutStorageDetailManagerVO> getDetailList(String outStorageId) {
        return outStorageDetailDAO.getDetailList(outStorageId);
    }

    @Autowired
    private MaterielService materielService;

    /**
     * 下架确认
     *
     * @param lowerShelfBillDetailVO
     * @param lowerDetailId
     * @param type                   状态0表示扫描门的时候调用的方法,1表示自动页面点击的方法和手持机调用的方法,2表示叉车调用的方法,叉车调用过程中如果是领料出库的话,则不进行状态的更新
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmLowerShelf(LowerShelfBillDetailVO lowerShelfBillDetailVO, String lowerDetailId, Integer type) {

        Map<String, Object> map = Maps.newHashMap();
        boolean result = false;
        String msg = "";

        if (lowerShelfBillDetailVO == null || Strings.isNullOrEmpty(lowerDetailId) || type == null) {
            map.put("msg", "下架确认参数未获取");
            map.put("result", false);
            return map;
        }
        Object outStorageId = this.getOutStorageId(lowerDetailId);
        //根据出库单获取出库单的类型
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId.toString());
        //获取出库单详情的数据
        List<OutStorageDetailManagerVO> outStorageDetailList = this.getDetailList(outStorageId.toString());
        String isrfid = outStorageManagerVO.getBillType();
        //获取出库类型
        Long outtype = outStorageManagerVO.getOutstorageBillType();
        String outcode = outStorageManagerVO.getOutstorageBillCode();
        String amount = lowerShelfBillDetailVO.getAmount();
        String weight = lowerShelfBillDetailVO.getWeight();
        String rfid = lowerShelfBillDetailVO.getRfid();
        String lowerId = lowerShelfBillDetailVO.getLowerShelfBillId();
        String batcNo = lowerShelfBillDetailVO.getBatchNo();
        String materialCode = lowerShelfBillDetailVO.getMaterialCode();
        LowerShelfBillVO lowerShelfBillVO = lowerShelfDAO.selectById(lowerId);
        lowerShelfBillDetailVO.setState("2");
        /**
         * 判断该rfid对应的数据是否存在货权转移的情况
         */
        //判断该rfid 在库存中的情况
        Map<String, Object> stockMap = lowerShelfDetailDAO.getStockMap(rfid, batcNo, materialCode, isrfid);
        if (stockMap == null) {
            map.put("msg", "该无关于此RFID的库存");
            map.put("result", false);
            return map;
        }
        //释放库存表中的锁定
        //判断库存表中是否对对应了多个出库单
        String outcodes = (String) stockMap.get("outstorage_bill_code");

        if (outcodes.split(",").length > 1) {
            if (outcodes.endsWith(outcode)) {
                if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
                    lowerShelfDetailDAO.updateStockAmountOutStorageBill(batcNo, materialCode, amount, rfid, isrfid, weight, "," + outcode);
                } else {
                    lowerShelfDetailDAO.updateStockAmount(batcNo, materialCode, amount, rfid, isrfid, weight, "," + outcode);
                }
            } else {
                if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
                    lowerShelfDetailDAO.updateStockAmountOutStorageBill(batcNo, materialCode, amount, rfid, isrfid, weight, outcode + ",");
                } else {
                    lowerShelfDetailDAO.updateStockAmount(batcNo, materialCode, amount, rfid, isrfid, weight, outcode + ",");
                }

            }
        } else {
            if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
                lowerShelfDetailDAO.updateStockAmountOutStorageBill(batcNo, materialCode, amount, rfid, isrfid, weight, outcode);
            } else {
                lowerShelfDetailDAO.updateStockAmount(batcNo, materialCode, amount, rfid, isrfid, weight, outcode);
            }

        }

        //有rfid 更新绑定表
        if ("1".equals(isrfid)) {
            //释放锁定表中的锁定(根据下架单找到对应的出库单,通过出库单ID以及rfid对物料绑定关系中的锁定进行释放)
            lowerShelfDetailDAO.updateMaterialBind(batcNo, materialCode, amount, rfid, weight);
            //获取释放锁定的绑定详情
            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("batch_rule", batcNo)
                            .eq("materiel_code", materialCode)
                            .eq("rfid", rfid)
                            .eq("delete_flag", DyylConstant.NOTDELETED)
            );
            //若 数量/重量为null 或  重量 /数量 小于等于0，则删除此条绑定详情
            if ((Strings.isNullOrEmpty(materielBindRfidDetail.getAmount()) || Double.parseDouble(materielBindRfidDetail.getAmount()) <= 0)
                    && (Strings.isNullOrEmpty(materielBindRfidDetail.getWeight()) || Double.parseDouble(materielBindRfidDetail.getWeight()) <= 0)) {
                materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
                materielBindRfidDetailService.updateById(materielBindRfidDetail);
            }
            //获取关于此RFID的主表信息,并通过主表ID获取其从表所有数据
            MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(new EntityWrapper<MaterielBindRfid>().eq("rfid", rfid));
            List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                            .eq("delete_flag", DyylConstant.NOTDELETED)
            );
            //解绑RFID   若此RFID的物料绑定从表为空 或 size等于0，则删除此RFID的绑定关系
            if (materielBindRfidDetailList.isEmpty() || materielBindRfidDetailList.size() == 0) {
                materielBindRfid.setDeletedFlag(DyylConstant.DELETED);
                materielBindRfidService.updateById(materielBindRfid);

                //删除库存中对应的RFID
                String aliRfid = stockMap.get("available_rfid").toString();             //可用rfid
                String[] alivRfid = aliRfid.split(",");
                String trueRfid = "";                                                   //变更后的可用RFID
                String stockRfid = stockMap.get("rfid").toString();                     //库存中rfid
                String[] stocksRfid = stockRfid.split(",");
                String stockdRfid = "";                                                //变更后的rfid
                for (String realrfid : alivRfid) {
                    if (!realrfid.equals(rfid)) {
                        if (trueRfid == null || trueRfid.equals("")) {
                            trueRfid = realrfid;
                        } else {
                            trueRfid = trueRfid + "," + realrfid;
                        }
                    }
                }
                for (String realsrfid : stocksRfid) {
                    if (!realsrfid.equals(rfid)) {
                        if (stockdRfid == null || stockdRfid.equals("")) {
                            stockdRfid = realsrfid;
                        } else {
                            stockdRfid = stockdRfid + "," + realsrfid;
                        }
                    }
                }
                //则将库存中的rfid以及数量进行变更
                lowerShelfDetailDAO.updateStockRfid(trueRfid, stockdRfid, rfid, batcNo, materialCode, isrfid);
            }

        }

        if ("1".equals(lowerShelfBillDetailVO.getTransform()) && isrfid.equals("1")) {
            //表示的是经过货权转移
            //更新库存表中的原有的数据
            //暂不确定
            lowerShelfDetailDAO.updateTransformStock(amount, rfid, batcNo, outStorageId.toString(), isrfid, weight);
            //更新货权转移之前的数据
            lowerShelfDetailDAO.updateTransformAmount(amount, rfid, batcNo, outStorageId.toString(), isrfid, weight);

        }
        if (!"0".equals(outtype.toString())) {
            //有rfid的情况
            //更新下架单详情任务状态
            lowerShelfDetailDAO.updateById(lowerShelfBillDetailVO);
        } else {
            if (type != 2) {
                //通过下架单获取对应的备料单的数据
                Object spareBillId = lowerShelfDetailDAO.getSpareBillId(outStorageId.toString());
                Map<String, Object> lowerDetail = this.lowerDetail(lowerDetailId);
                //更新备料单中的值
                spareBillDetailService.updateDetail(spareBillId.toString(), lowerDetail);
                //更新下架单详情任务状态
                lowerShelfDetailDAO.updateById(lowerShelfBillDetailVO);

                //更新出库单已下架数量
                OutStorageDetailManagerVO outStorageDetailManagerVO = outStorageDetailService.selectOne(
                        new EntityWrapper<OutStorageDetailManagerVO>()
                                .eq("outstorage_bill_id", outStorageId)
                                .eq("material_code", materialCode)
                                .eq("batch_no", lowerShelfBillDetailVO.getBatchNo())
                                .eq("position_code", lowerShelfBillDetailVO.getPositionCode())
                );
                Double separableAmount = Double.parseDouble(outStorageDetailManagerVO.getSeparableAmount()) + Double.parseDouble(lowerShelfBillDetailVO.getAmount());
                Double separableWeight = Double.parseDouble(outStorageDetailManagerVO.getSeparableWeight()) + Double.parseDouble(lowerShelfBillDetailVO.getWeight());
                outStorageDetailManagerVO.setSeparableAmount(separableAmount.toString());
                outStorageDetailManagerVO.setSeparableWeight(separableWeight.toString());
                outStorageDetailService.updateById(outStorageDetailManagerVO);
            }
        }

        //库存为0 删除这条库存记录
        String stockAmount = stockMap.get("stock_amount").toString();
        if (Double.parseDouble(stockAmount) - Double.parseDouble(amount) <= 0) {
            //则删除该条数据
            lowerShelfDetailDAO.deleteStockById(stockMap.get("id").toString());
        }

        //如果是其他出库的话,则将库存中的rfid 和 物料绑定表中的rfid 数据删除
        //删除绑定表中rfid 数据
        //lowerShelfDetailDAO.deleteMaterialBind(rfid,batcNo,materialCode);
        //删除库存中的rfid 数据
//        Map<String,Object> stockMap = lowerShelfDetailDAO.getStockMap(rfid,batcNo,materialCode);
//        if (stockMap != null && !stockMap.isEmpty()) {
//
////                lowerShelfDetailDAO.deleteStockRfid(rfid, batcNo, materialCode);
//            }

        //更新下架单以及出库单的状态
        //先判断对应的下架单是否全部完成
        Integer lowerCount = this.lowerCount(lowerDetailId);
        String state = "1";
        Integer id = null;
        //状态已经是出库中的表示已经不需要将库存变动插入到库存变动表中
        if ("2".equals(lowerShelfBillDetailVO.getState())) {
            //将变动插入到库存变动表中
            StockChange stockChange = new StockChange();
            stockChange.setChangeCode(lowerShelfBillVO.getLowerShelfBillCode());
            stockChange.setMaterialName(lowerShelfBillDetailVO.getMaterialName());
            stockChange.setMaterialCode(lowerShelfBillDetailVO.getMaterialCode());
            stockChange.setBatchNo(lowerShelfBillDetailVO.getBatchNo());
            stockChange.setBusinessType("1");
            stockChange.setOutAmount(lowerShelfBillDetailVO.getAmount());
            stockChange.setOutWeight(lowerShelfBillDetailVO.getWeight());
            if (StringUtils.isNotEmpty(lowerShelfBillDetailVO.getPositionCode())) {
                DepotPosition depotPosition = depotPositionService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code", lowerShelfBillDetailVO.getPositionCode()));
                if (depotPosition != null) {
                    stockChange.setPositionBy(depotPosition.getId());
                }
            }
            String nowTime = DateUtils.getTime();
            stockChange.setCreateTime(nowTime);
            if (lowerShelfBillVO.getUserId() != null) {
                stockChange.setCreateBy(Long.parseLong(lowerShelfBillVO.getUserId()));
            }
            id = stockChangeDAO.insert(stockChange);
            if (id <= 0) {
                //事务回滚
                msg = "出库失败";
                result = false;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        //将对应的可用的单据中的rfid进行更新
        lowerShelfDAO.updateOutStorageSlab(rfid, lowerId);
        if (lowerCount > 0) {
            lowerShelfBillVO.setState(state);
            //表示下架单并没有全部完成
            //将下架单状态改为正在执行中
            id = lowerShelfDAO.updateById(lowerShelfBillVO);
            if (id <= 0) {
                //事务回滚
                msg = "出库失败";
                result = false;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {
                state = "4";
                outStorageManagerVO.setState(state);
                //表示出库没有全部完成
                id = outStorageDAO.updateById(outStorageManagerVO);
                if (id <= 0) {
                    //事务回滚
                    result = false;
                    msg = "出库失败";
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                } else {
                    if ("0".equals(outtype.toString())) {
                        Object spareBillId = lowerShelfDetailDAO.getSpareBillId(outStorageId.toString());
                        SpareBillManagerVO spareBillManagerVO = spareBillService.selectOne(new EntityWrapper<SpareBillManagerVO>().eq("spare_bill_code", spareBillId));
                        //备料单 出库中
                        spareBillManagerVO.setState("3");
                        spareBillService.updateById(spareBillManagerVO);
                    }
                }
            }
        } else {
            String time = DateUtils.getTime();
            state = "2";
            lowerShelfBillVO.setState(state);
            lowerShelfBillVO.setLowerShelfTime(time);
            //更新下架状态为已完成
            id = lowerShelfDAO.updateById(lowerShelfBillVO);
            if (id <= 0) {
                //事务回滚
                msg = "出库失败";
                result = false;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            //查询出库单对应的下架单主表是否全部完成
            Integer outStorageCount = this.outStorageCount(outStorageId.toString());
            if (outStorageCount <= 0) {
                state = "5";
                outStorageManagerVO.setState(state);
                outStorageManagerVO.setOutstoragePlanTime(time);
                id = outStorageDAO.updateById(outStorageManagerVO);
                if (id <= 0) {
                    //事务回滚
                    result = false;
                    msg = "出库失败";
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
//                this.updateOutStorageDetail(outStorageId);
                //更新对应备料单的状态为完成
                if ("0".equals(outtype.toString())) {
                    Object spareBillId = lowerShelfDetailDAO.getSpareBillId(outStorageId.toString());
                    SpareBillManagerVO spareBillManagerVO = spareBillService.selectOne(new EntityWrapper<SpareBillManagerVO>().eq("spare_bill_code", spareBillId));
                    //查询关于此备料编码的出库单
                    Integer count = outStorageDAO.outStorageStatusCount(spareBillId.toString());
                    //无 关于此备料单且状态非已完成的出库单
                    if (count <= 0) {
                        if (spareBillManagerVO != null) {
                            //获取关于此备料单的详情单
                            List<SpareBillDetailManagerVO> spareBillDetailManagerVOList = spareBillDetailService.selectList(
                                    new EntityWrapper<SpareBillDetailManagerVO>()
                                            .eq("spare_bill_id", spareBillManagerVO.getId())
                            );
                            //遍历判断详情中数量与仓库发货数量
                            if (!spareBillDetailManagerVOList.isEmpty() && spareBillDetailManagerVOList.size() > 0) {
                                Integer num = 0;
                                for (SpareBillDetailManagerVO spareBillDetailManagerVO : spareBillDetailManagerVOList) {
                                    if (Double.parseDouble(spareBillDetailManagerVO.getUsedBox()) > Double.parseDouble(spareBillDetailManagerVO.getSendAmount())) {
                                        num = 1;
                                        break;
                                    }
                                }
                                if (num == 0) {
                                    //已完成
                                    lowerShelfDetailDAO.updateState(spareBillId.toString());
                                }else {
                                    //备料单 出库中
                                    spareBillManagerVO.setState("3");
                                    spareBillService.updateById(spareBillManagerVO);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (id > 0) {
            msg = "下架成功";
            result = true;
        } else {
            msg = "下架失败";
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 根据下架单详情更新出库单的数据
     *
     * @param outStorageId
     */
    @Override
    public void updateOutStorageDetail(Object outStorageId) {
        //根据下架单详情更新对应该的出库单的详情
        Map<String, Object> detailMap = Maps.newHashMap();
        detailMap.put("outstorageId", outStorageId.toString());
//        outStorageDAO.deleteMap(detailMap);
        //获取对应的下架单详情的list
        List<Map<String, Object>> lowerList = lowerShelfDetailDAO.getDetailList(outStorageId.toString());
        for (Map detailMaps : lowerList) {
            Materiel materiel = null;
            if (StringUtils.isNotEmpty(detailMaps.get("material_code").toString())) {
                materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code", detailMaps.get("material_code").toString()));
            }
            String batchNo = detailMaps.get("batch_no").toString();
            String materialCode = detailMaps.get("material_code").toString();
            String positionCode = detailMaps.get("position_code").toString();
            String amount = detailMaps.get("amount").toString();
            String weight = detailMaps.get("weight").toString();
            OutStorageDetailManagerVO outStorageDetailManagerVO = outStorageDetailService.selectOne(
                    new EntityWrapper<OutStorageDetailManagerVO>()
                            .eq("outstorage_bill_id", outStorageId)
                            .eq("material_code", materialCode)
                            .eq("batch_no", batchNo)
                            .eq("position_code", positionCode)
            );
            //插入到出库单详情中
            outStorageDetailDAO.insert(outStorageDetailManagerVO);
        }
    }

    @Override
    public Object getmaterialAmount(String outStorageDetailId, String materialCode, String batcNo) {
        return outStorageDetailDAO.getmaterialAmount(outStorageDetailId, materialCode, batcNo);
    }

    @Override
    public Object getmaterialWeight(String outStorageDetailId, String materialCode, String batcNo) {
        return outStorageDetailDAO.getmaterialWeight(outStorageDetailId, materialCode, batcNo);
    }

    @Override
    public void updateDetail(Object materialAmount, Object materialWeight, Long outStorageDetailId) {
        outStorageDetailDAO.updateDetail(materialAmount == null ? "0" : materialAmount.toString(), materialWeight == null ? "0" : materialWeight.toString(), outStorageDetailId);
    }


    /**
     * 通过rfid 获取对应的任务的信息
     *
     * @param lowerDetailId
     * @return
     */
    @Override
    public Map<String, Object> lowerDetail(String lowerDetailId) {
        Map<String, Object> map = Maps.newHashMap();
        map = lowerShelfDetailDAO.lowerDetail(lowerDetailId);
        return map;
    }
}
