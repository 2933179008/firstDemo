package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.service.OutStorageInterfaceService;
import com.tbl.modules.outstorage.dao.LowerShelfDAO;
import com.tbl.modules.outstorage.dao.LowerShelfDetailDAO;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.entity.LowerShelfBillDetailVO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.stock.service.StockChangeService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lcg
 * data 2019/2/22
 */
@Service("outStorageInterfaceService")
public class OutStorageInterfaceServiceImpl extends ServiceImpl<LowerShelfDAO, LowerShelfBillVO> implements OutStorageInterfaceService {

    @Autowired
    private LowerShelfDAO lowerShelfDAO;

    @Autowired
    private LowerShelfDetailDAO lowerShelfDetailDAO;

    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OutStorageDAO outStorageDAO;

    @Autowired
    private StockChangeService stockChangeService;

    @Autowired
    private DepotPositionService depotPositionService;

    /**
     * 通过userId获取对应的下架任务，按照时间的顺序
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> lowerInterfaceList(String userId) {
        List<Map<String, Object>> mapList = lowerShelfDAO.lowerInterfaceList(userId);
        return mapList;
    }

    /**
     * 通过下架单的ID获取对应的下架单的详情
     *
     * @param detailId
     * @return
     */
    @Override
    public List<Map<String, Object>> lowerInterfaceDetailList(String detailId) {
        List<Map<String, Object>> mapList = lowerShelfDetailDAO.lowerInterfaceDetailList(detailId);
        return mapList;
    }

    /**
     * 通过rfid获取对应的下架的人员
     *
     * @param rfid
     * @return
     */
    @Override
    public String getLowerUserId(String rfid) {
        Object userId = lowerShelfDetailDAO.getLowerUserId(rfid);
        return userId.toString();
    }


    /**
     * 判断下架RFID中物料是否符合下架详情中物料
     *
     * @author yuany
     * @date 2019-06-06
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> determineRfid(String rfid, String lowerId) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String alertValue = "";

        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(lowerId)) {
            map.put("alertKey", "0");
            map.put("alertValue", "失败原因：未获取RFID或下架单ID");
            map.put("result", result);
            return map;
        }

        //1.获取此RFID的物料绑定详情
        MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
                        .eq("rfid", rfid)
        );
        if (materielBindRfid == null) {
            map.put("alertKey", "0");
            map.put("alertValue", "失败原因：此RFID无已入库绑定信息");
            map.put("result", result);
            return map;
        }
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
        );
        if (materielBindRfidDetailList.isEmpty()) {
            map.put("alertKey", "0");
            map.put("alertValue", "失败原因：此RFID无已入库绑定详情");
            map.put("result", result);
            return map;
        }

        //2.获取下架但详情
        List<LowerShelfBillDetailVO> lowerShelfBillDetailVOList = lowerShelfDetailDAO.selectList(
                new EntityWrapper<LowerShelfBillDetailVO>()
                        .eq("lower_shelf_bill_id", lowerId)
        );
        if (lowerShelfBillDetailVOList.isEmpty()) {
            map.put("alertKey", "0");
            map.put("alertValue", "失败原因：未获取关于此下架单的下架险情");
            map.put("result", result);
            return map;
        }

        //3.对比 判断是否有符合下架条件的物料（同物料/同批次/数量或重量大于等于下架详情中的重量或数量,且RFID 不同，） 进行替换
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            for (LowerShelfBillDetailVO lowerShelfBillDetailVO : lowerShelfBillDetailVOList) {
                //物料 / 批次号相同
                if (materielBindRfidDetail.getMaterielCode().equals(lowerShelfBillDetailVO.getMaterialCode())
                        && materielBindRfidDetail.getBatchRule().equals(lowerShelfBillDetailVO.getBatchNo())
                        && !materielBindRfidDetail.getRfid().equals(lowerShelfBillDetailVO.getRfid())) {
                    //判断下架单中物料是否已进行过替换操作
                    if (!"P-01".equals(lowerShelfBillDetailVO.getPositionCode())) {
                        //判断重量/数量是否符合条件
                        if (Double.parseDouble(materielBindRfidDetail.getAmount()) >= Double.parseDouble(lowerShelfBillDetailVO.getAmount())
                                && Double.parseDouble(materielBindRfidDetail.getWeight()) >= Double.parseDouble(lowerShelfBillDetailVO.getWeight())) {

                            //4.获取下架单中RFID对应的物料
                            MaterielBindRfid lowerRfid = materielBindRfidService.selectOne(
                                    new EntityWrapper<MaterielBindRfid>()
                                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                                            .eq("status", DyylConstant.STATE_UNTREATED)
                                            .eq("rfid", lowerShelfBillDetailVO.getRfid())
                            );
                            MaterielBindRfidDetail lowerRfidDetail = materielBindRfidDetailService.selectOne(
                                    new EntityWrapper<MaterielBindRfidDetail>()
                                            .eq("materiel_code", lowerShelfBillDetailVO.getMaterialCode())
                                            .eq("batch_rule", lowerShelfBillDetailVO.getBatchNo())
                                            .eq("materiel_bind_rfid_by", lowerRfid.getId())
                                            .eq("delete_flag", DyylConstant.NOTDELETED)
                                            .eq("status", DyylConstant.STATE_UNTREATED)
                            );

                            //5.将库存占用替换到新的RFID并清除原有RFID中库存占用
                            //获取原物料的库存信息
                            Stock oldStock = stockService.selectOne(
                                    new EntityWrapper<Stock>()
                                            .eq("material_code", lowerShelfBillDetailVO.getMaterialCode())
                                            .eq("batch_no", lowerShelfBillDetailVO.getBatchNo())
                                            .eq("rfid", lowerRfid.getRfid())
                                            .eq("position_code", lowerShelfBillDetailVO.getPositionCode())
                            );
                            //获取替换的物料的库位信息
                            Long newPositionId = materielBindRfid.getPositionBy();
                            DepotPosition depotPosition = depotPositionService.selectById(newPositionId);
                            //获取替换的无聊库存信息
                            Stock newStock = stockService.selectOne(
                                    new EntityWrapper<Stock>()
                                            .eq("material_code", lowerShelfBillDetailVO.getMaterialCode())
                                            .eq("batch_no", lowerShelfBillDetailVO.getBatchNo())
                                            .eq("rfid", rfid)
                                            .eq("position_code", depotPosition.getPositionCode())
                            );
                            newStock.setOutstorageBillCode(oldStock.getOutstorageBillCode());
                            newStock.setOccupyStockAmount(oldStock.getOccupyStockAmount());
                            newStock.setOccupyStockWeight(oldStock.getOccupyStockWeight());
                            oldStock.setOutstorageBillCode("");
                            oldStock.setOccupyStockAmount("0");
                            oldStock.setOccupyStockWeight("0");
                            stockService.updateById(newStock);
                            stockService.updateById(oldStock);


                            //6.将出库单占用替换到新的RFID并清除原有RFID中出库占用
                            materielBindRfidDetail.setOutstorageBillCode(lowerRfidDetail.getOutstorageBillCode());
                            materielBindRfidDetail.setOccupyStockAmount(lowerRfidDetail.getOccupyStockAmount());
                            materielBindRfidDetail.setOccupyStockWeight(lowerRfidDetail.getOccupyStockWeight());
                            lowerRfidDetail.setOutstorageBillCode("");
                            lowerRfidDetail.setOccupyStockWeight("0.0");
                            lowerRfidDetail.setOccupyStockAmount("0.0");
                            materielBindRfidDetailService.updateById(materielBindRfidDetail);
                            materielBindRfidDetailService.updateById(lowerRfidDetail);
                            //7.将下架单中对应的详情RFID替换
                            lowerShelfBillDetailVO.setRfid(rfid);
                            lowerShelfDetailDAO.updateById(lowerShelfBillDetailVO);
                            map.put("alertKey", "1");
                            map.put("alertValue", "替换成功，可出库！");
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!result) {
            map.put("alertKey", "0");
            map.put("alertValue", "失败原因：此RFID无下架任务或已对下架单中物料进行过替换操作，不可再次替换");
            map.put("result", result);
            return map;
        }

        map.put("result", result);
        return map;
    }

    /**
     * 合并出库
     * 隐含替换
     *
     * @author yuany
     * @date 2019-06-09
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> mergeOutStorage(Map<String, Object> paramMap) {

        Map<String, Object> map = new HashMap<>();

        //获取参数
        String newRfid = (String) paramMap.get("newRfid");
        String oldRfid = (String) paramMap.get("oldRfid");
        String userId = (String) paramMap.get("userId");

        Gson gson = new Gson();
        List<LowerShelfBillDetailVO> lowerShelfBillDetailVOList = gson.fromJson(paramMap.get("data").toString(),
                new TypeToken<List<LowerShelfBillDetailVO>>() {
                }.getType());

        String parameter = "newRfid:" + newRfid + "/lowerShelfBillDetailVOList:lowerShelfBillDetailVOList" + "/oldRfid:" + oldRfid + "/userId:" + userId;
        //1.判断获取的参数是否为空
        if (Strings.isNullOrEmpty(newRfid) || lowerShelfBillDetailVOList.isEmpty() || Strings.isNullOrEmpty(oldRfid) || Strings.isNullOrEmpty(userId)) {
            interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：参数未获取");

            return map;
        }

        //2.判断新托盘是否存在绑定关系，若存在返回提示错误信息，若不存在，新建关于此RFID的绑定关系（已经入库，一般入库类型，库位P01(ID = 0)）
        MaterielBindRfid materielBindRfid = materielBindRfidService.materielBindRfid(newRfid);
        if (materielBindRfid != null && !materielBindRfid.getPositionBy().equals(0L)) {
            interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：新托盘存在非合并出库的RFID绑定关系", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：新托盘存在非合并出库的RFID绑定关系");

            return map;
        }
        if (materielBindRfid == null) {
            materielBindRfid = new MaterielBindRfid();
            materielBindRfid.setBindCode(materielBindRfidService.getBindCode());
            materielBindRfid.setRfid(newRfid);
            materielBindRfid.setDeletedFlag(DyylConstant.NOTDELETED);
            materielBindRfid.setStatus(DyylConstant.STATE_UNTREATED);
            materielBindRfid.setPositionBy(0L);//虚拟库位P-01
            materielBindRfid.setCreateTime(DateUtils.getTime());
            materielBindRfid.setCreateBy(Long.parseLong(userId));
            materielBindRfidService.insert(materielBindRfid);
        }

        MaterielBindRfid mbr = materielBindRfidService.materielBindRfid(oldRfid);
        //3.遍历拆分数据
        for (LowerShelfBillDetailVO lowerShelfBillDetailVO : lowerShelfBillDetailVOList) {

            //获取实际下架单详情数据
            LowerShelfBillDetailVO lsbDetail = lowerShelfDetailDAO.selectById(lowerShelfBillDetailVO.getId());
            //获取需要从下架单详情中拆分的数量、重量
            Double amount = Double.parseDouble(lowerShelfBillDetailVO.getAmount());
            Double weight = Double.parseDouble(lowerShelfBillDetailVO.getWeight());

            if (amount > Double.parseDouble(lsbDetail.getAmount()) || weight > Double.parseDouble(lsbDetail.getWeight())) {
                interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：拆出数量或重量超出下架详情中物料所需数量或重量", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：拆出数量或重量超出下架详情中物料所需数量或重量");

                return map;
            }

            //获取所需拆分的下架单详情对应的oldRfid绑定的物料
            MaterielBindRfidDetail mbrd = materielBindRfidDetailService.selectOne(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("materiel_code", lowerShelfBillDetailVO.getMaterialCode())
                            .eq("batch_rule", lowerShelfBillDetailVO.getBatchNo())
                            .eq("materiel_bind_rfid_by", mbr.getId())
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("status", DyylConstant.STATE_UNTREATED)
            );
            if (mbrd == null) {
                interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：下架单详情中未找到与" + oldRfid + "对应的绑定详情", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：下架单详情中未找到与" + oldRfid + "对应的绑定详情");

                return map;
            }

            if (amount > Double.parseDouble(mbrd.getAmount()) || weight > Double.parseDouble(mbrd.getWeight())) {
                interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：拆出数量或重量超出" + oldRfid + "可拆分范围", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：拆出数量或重量超出" + oldRfid + "可拆分范围");

                return map;
            }

            //RFID不相等表示 替换
            if (!mbrd.getRfid().equals(lsbDetail.getRfid()) && mbrd.getOutstorageBillCode() != null) {
                interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：" + oldRfid + "已被其它出库单据占用", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：" + oldRfid + "已被其它出库单据占用");

                return map;
            }

            //获取库位编码
            DepotPosition depotPosition = depotPositionService.selectById(mbr.getPositionBy());
            //获取对应库存,且oldRfid 在此库存可拆分RFID内
            Stock stock = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("material_code", mbrd.getMaterielCode())
                            .eq("batch_no", mbrd.getBatchRule())
                            .eq("position_code", depotPosition.getPositionCode())
                            .eq("material_type", DyylConstant.MATERIAL_RFID)
                            .like("available_rfid", oldRfid)
            );
            if (stock == null) {
                interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：未获取对应库存数据", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：未获取对应库存数据");

                return map;
            }
            //表示替换的RFID 拆分，需要判断库存的可用数量重量-出库占用数量重量 大于拆分数量重量
            if (mbrd.getOutstorageBillCode() == null) {
                Double avAmount = Double.parseDouble(stock.getAvailableStockAmount()) - Double.parseDouble(stock.getOccupyStockAmount());
                Double avWeight = Double.parseDouble(stock.getAvailableStockWeight()) - Double.parseDouble(stock.getOccupyStockWeight());
                if (amount > avAmount || weight > avWeight) {
                    interfaceLogService.interfaceLogInsert("调用合并出库接口", parameter, "失败原因：未获取对应库存数据", DateUtils.getTime());
                    map.put("result", false);
                    map.put("msg", "失败原因：拆分数量或重量超出" + oldRfid + "库存可拆分范围");

                    return map;
                }
            }

            //新增关于newRfid的绑定关系，若存在则数量重量相加
            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("materiel_code", lowerShelfBillDetailVO.getMaterialCode())
                            .eq("batch_rule", lowerShelfBillDetailVO.getBatchNo())
                            .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("status", DyylConstant.STATE_UNTREATED)
            );
            if (materielBindRfidDetail == null) {
                materielBindRfidDetail = mbrd;
                materielBindRfidDetail.setId(null);
                materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
                materielBindRfidDetail.setAmount(amount.toString());
                materielBindRfidDetail.setWeight(weight.toString());
                materielBindRfidDetail.setOccupyStockAmount(amount.toString());
                materielBindRfidDetail.setOccupyStockWeight(weight.toString());
                materielBindRfidDetail.setPositionId(0L);
                materielBindRfidDetailService.insert(materielBindRfidDetail);
            } else {
                Double a = Double.parseDouble(materielBindRfidDetail.getAmount()) + amount;
                Double w = Double.parseDouble(materielBindRfidDetail.getWeight()) + weight;
                Double oa = Double.parseDouble(materielBindRfidDetail.getOccupyStockAmount()) + amount;
                Double ow = Double.parseDouble(materielBindRfidDetail.getOccupyStockWeight()) + weight;
                materielBindRfidDetail.setAmount(a.toString());
                materielBindRfidDetail.setWeight(w.toString());
                materielBindRfidDetail.setOccupyStockAmount(oa.toString());
                materielBindRfidDetail.setOccupyStockWeight(ow.toString());
                materielBindRfidDetailService.updateById(materielBindRfidDetail);
            }

            //获取关于P01的库存，若为空则新增
            Stock stock1 = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("material_code", mbrd.getMaterielCode())
                            .eq("batch_no", mbrd.getBatchRule())
                            .eq("position_code", "P01")
                            .eq("rfid", newRfid)
                            .eq("material_type", DyylConstant.MATERIAL_RFID)
            );
            if (stock1 == null) {
                stock1 = stock;
                stock1.setId(null);
                stock1.setStockAmount(amount.toString());
                stock1.setStockWeight(weight.toString());
                stock1.setAvailableStockAmount(amount.toString());
                stock1.setAvailableStockWeight(weight.toString());
                stock1.setRfid(newRfid);
                stock1.setAvailableRfid(newRfid);
                stock1.setStockRfidAmount("1");
                stock1.setAvailableStockRfidAmount("1");
                stock1.setPositionCode("P01");
                stock1.setPositionName("P01");
                stock1.setOutstorageBillCode(mbrd.getOutstorageBillCode());
                stock1.setOccupyStockAmount(amount.toString());
                stock1.setOccupyStockWeight(weight.toString());
                stockService.insert(stock1);
            } else {
                Double a = Double.parseDouble(stock1.getStockAmount()) + amount;
                Double w = Double.parseDouble(stock1.getStockWeight()) + weight;
                Double aa = Double.parseDouble(stock1.getAvailableStockAmount()) + amount;
                Double ww = Double.parseDouble(stock1.getAvailableStockWeight()) + weight;
                Double oa = Double.parseDouble(stock1.getOccupyStockAmount()) + amount;
                Double ow = Double.parseDouble(stock1.getOccupyStockWeight()) + weight;
                stock1.setStockAmount(a.toString());
                stock1.setStockWeight(w.toString());
                stock1.setAvailableStockAmount(aa.toString());
                stock1.setAvailableStockWeight(ww.toString());
                stock1.setOccupyStockAmount(oa.toString());
                stock1.setOccupyStockWeight(ow.toString());
                stockService.updateById(stock1);
            }


            //去除oldRfid绑定详情相应的重量 数量 占用数量 重量
            Double amt = Double.parseDouble(mbrd.getAmount()) - amount;
            Double wgt = Double.parseDouble(mbrd.getWeight()) - weight;
            if (amt <= 0 && wgt <= 0) {
                mbrd.setDeleteFlag(DyylConstant.DELETED);
            } else {
                mbrd.setAmount(amt.toString());
                mbrd.setWeight(wgt.toString());
                //出库单占用不为空，表示不是替换
                if (mbrd.getOutstorageBillCode() != null) {
                    if (amount == Double.parseDouble(lsbDetail.getAmount()) && weight == Double.parseDouble(lsbDetail.getWeight())) {
                        mbrd.setOccupyStockWeight("0");
                        mbrd.setOccupyStockAmount("0");
                        mbrd.setOutstorageBillCode("");
                    } else {
                        Double osa = Double.parseDouble(mbrd.getOccupyStockAmount()) - amount;
                        Double osw = Double.parseDouble(mbrd.getOccupyStockWeight()) - weight;
                        mbrd.setOccupyStockWeight(osa.toString());
                        mbrd.setOccupyStockAmount(osw.toString());
                    }
                }
            }
            materielBindRfidDetailService.updateById(mbrd);

            //若无绑定详情 则删除oldRfid 的绑定关系
            List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("materiel_bind_rfid_by", mbr.getId())
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("status", DyylConstant.STATE_UNTREATED)
            );
            if (materielBindRfidDetailList.isEmpty()) {
                mbr.setDeletedFlag(DyylConstant.NOTDELETED);
                materielBindRfidService.updateById(mbr);
            }

            //计算剩余库存数量&重量
            Double stockAmount = Double.parseDouble(stock.getStockAmount()) - amount;
            Double stockAviAmount = Double.parseDouble(stock.getAvailableStockAmount()) - amount;
            Double stockWeight = Double.parseDouble(stock.getStockWeight()) - weight;
            Double stockAviWeight = Double.parseDouble(stock.getAvailableStockWeight()) - amount;
            //若库存数量重量小于等于0，删除
            if (stockAmount <= 0 && stockWeight <= 0) {
                stockService.deleteById(stock);
            } else {
                mbrd = materielBindRfidDetailService.selectOne(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("materiel_code", lowerShelfBillDetailVO.getMaterialCode())
                                .eq("batch_rule", lowerShelfBillDetailVO.getBatchNo())
                                .eq("materiel_bind_rfid_by", mbr.getId())
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                                .eq("status", DyylConstant.STATE_UNTREATED)
                );

                List<String> stockRfid = Arrays.asList(stock.getRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                List<String> aviRfid = Arrays.asList(stock.getAvailableRfid().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());

                //去除库存占用的RFID
                if (mbrd == null) {
                    //去除库存RFID占用(增强for不可用remove方法)
                    for (int i = 0; i < stockRfid.size(); i++) {
                        if (stockRfid.get(i).equals(oldRfid)) {
                            stockRfid.remove(i);
                            break;
                        }
                    }
                    for (int i = 0; i < aviRfid.size(); i++) {
                        if (aviRfid.get(i).equals(oldRfid)) {
                            aviRfid.remove(i);
                            break;
                        }
                    }


                    String rfids = null;
                    String avirfids = null;
                    String outStorageCode = null;
                    for (String rfid : stockRfid) {
                        if (Strings.isNullOrEmpty(rfids)) {
                            rfids = rfid;
                        } else {
                            rfids = rfids + "," + rfid;
                        }
                    }
                    for (String rfid : aviRfid) {
                        if (Strings.isNullOrEmpty(avirfids)) {
                            rfids = rfid;
                        } else {
                            rfids = rfids + "," + rfid;
                        }
                    }


                    Double rfidAmount = Double.parseDouble(stock.getStockRfidAmount()) - 1;
                    Double aviRfidAmount = Double.parseDouble(stock.getAvailableStockRfidAmount()) - 1;
                    stock.setRfid(rfids);
                    stock.setAvailableRfid(avirfids);
                    stock.setStockRfidAmount(rfidAmount.toString());
                    stock.setAvailableStockRfidAmount(aviRfidAmount.toString());

                    if (mbrd.getOutstorageBillCode() != null) {
                        List<String> outstorageBillCodelst = Arrays.asList(stock.getOutstorageBillCode().split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                        //去除库存出库单占用(增强for不可用remove方法)
                        for (int i = 0; i < outstorageBillCodelst.size(); i++) {
                            if (outstorageBillCodelst.get(i).equals(mbrd.getOutstorageBillCode())) {
                                outstorageBillCodelst.remove(i);
                                break;
                            }
                        }
                        for (String osc : outstorageBillCodelst) {
                            if (Strings.isNullOrEmpty(outStorageCode)) {
                                outStorageCode = osc;
                            } else {
                                outStorageCode = outStorageCode + "," + osc;
                            }
                        }
                        Double num = Double.parseDouble(stock.getOccupyStockAmount()) - Double.parseDouble(mbrd.getOccupyStockAmount());
                        Double wet = Double.parseDouble(stock.getOccupyStockWeight()) - Double.parseDouble(mbrd.getOccupyStockWeight());
                        stock.setOccupyStockWeight(num.toString());
                        stock.setOccupyStockAmount(wet.toString());
                        stock.setOutstorageBillCode(outStorageCode);
                    }
                }

                stock.setStockAmount(stockAmount.toString());
                stock.setStockWeight(stockWeight.toString());
                stock.setAvailableStockAmount(stockAviAmount.toString());
                stock.setAvailableStockWeight(stockAviWeight.toString());

                stockService.updateById(stock);
            }
            //新增合并出库库存变动(出)
            StockChange stockChange = new StockChange();
            stockChange.setChangeCode(mbr.getBindCode());
            stockChange.setMaterialCode(mbrd.getMaterielCode());
            stockChange.setMaterialName(mbrd.getMaterielName());
            stockChange.setBatchNo(mbrd.getBatchRule());
            stockChange.setBusinessType(DyylConstant.MG_OUT);
            stockChange.setOutWeight(amount.toString());
            stockChange.setOutAmount(amount.toString());
            stockChange.setCreateBy(Long.parseLong(userId));
            stockChange.setCreateTime(DateUtils.getTime());
            stockChange.setPositionBy(mbr.getPositionBy());
            stockChangeService.insert(stockChange);
            //新增合并出库库存变动(入)
            StockChange stockChange1 = new StockChange();
            stockChange1.setChangeCode(materielBindRfid.getBindCode());
            stockChange1.setMaterialCode(materielBindRfidDetail.getMaterielCode());
            stockChange1.setMaterialName(materielBindRfidDetail.getMaterielName());
            stockChange1.setBatchNo(materielBindRfidDetail.getBatchRule());
            stockChange1.setBusinessType(DyylConstant.MG_IN);
            stockChange1.setOutWeight(amount.toString());
            stockChange1.setOutAmount(weight.toString());
            stockChange1.setCreateBy(Long.parseLong(userId));
            stockChange1.setCreateTime(DateUtils.getTime());
            stockChange1.setPositionBy(0L);
            stockChangeService.insert(stockChange1);

            //若相等则替换下架详情 否则去除后新增
            if (amount == Double.parseDouble(lsbDetail.getAmount()) && weight == Double.parseDouble(lsbDetail.getWeight())) {
                lowerShelfBillDetailVO.setRfid(newRfid);
                lowerShelfBillDetailVO.setPositionCode("P01");
                lowerShelfDetailDAO.updateById(lowerShelfBillDetailVO);
            } else {
                Double lsbAmount = Double.parseDouble(lsbDetail.getAmount()) - amount;
                Double lsbWeight = Double.parseDouble(lsbDetail.getWeight()) - weight;
                lsbDetail.setAmount(lsbAmount.toString());
                lsbDetail.setWeight(lsbWeight.toString());
                lowerShelfDetailDAO.updateById(lsbDetail);

                LowerShelfBillDetailVO detailVO = lsbDetail;
                detailVO.setId(null);
                detailVO.setRfid(newRfid);
                detailVO.setPositionCode("P01");
                detailVO.setAmount(amount.toString());
                detailVO.setWeight(weight.toString());
                lowerShelfDetailDAO.insert(detailVO);
            }
        }

        map.put("result", true);
        map.put("msg", "合并出库成功！");
        return map;

    }

    /**
     * 获取关于RFID的下架单详情
     * 隐含替换
     * @param oldRfid
     * @param lowerId
     * @return
     * @author yuany
     * @date 2019-07-10
     */
    @Override
    public Map<String, Object> getLowerDetail(String oldRfid, String lowerId) {

        Map<String, Object> map = new HashMap<>();

        String parameter = "oldRfid:" + oldRfid + "/lowerId:" + lowerId;
        //1.判断获取的参数是否为空
        if (Strings.isNullOrEmpty(oldRfid) || Strings.isNullOrEmpty(lowerId)) {
            interfaceLogService.interfaceLogInsert("调用获取关于RFID的下架单详情接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：参数未获取");

            return map;
        }


        //获取关于oldRfid的下架单详情，若为空则获取oldRfid的绑定详情和关于lowerId下架单详情，并进行对比筛选出此下架强请中包含绑定详情物料的数据
        List<LowerShelfBillDetailVO> lowerShelfBillDetailVOList = lowerShelfDetailDAO.selectList(
                new EntityWrapper<LowerShelfBillDetailVO>()
                        .eq("lower_shelf_bill_id", lowerId)
                        .eq("rfid", oldRfid)
        );

        if (lowerShelfBillDetailVOList.isEmpty()) {
            MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(
                    new EntityWrapper<MaterielBindRfid>()
                            .eq("rfid", oldRfid)
                            .eq("deleted_flag", DyylConstant.NOTDELETED)
                            .eq("status", DyylConstant.STATE_UNTREATED)
            );
            List<MaterielBindRfidDetail> materielBindRfidDetailList = new ArrayList<>();
            if (materielBindRfid != null) {
                materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                                .eq("status", DyylConstant.STATE_UNTREATED)
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                );
            }

            if (materielBindRfidDetailList.isEmpty()) {
                interfaceLogService.interfaceLogInsert("调用获取关于RFID的下架单详情接口", parameter, "失败原因：未获取关于此RFID的下架详情", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：" + oldRfid + "无绑定信息");

                return map;
            }

            lowerShelfBillDetailVOList = lowerShelfDetailDAO.selectList(
                    new EntityWrapper<LowerShelfBillDetailVO>()
                            .eq("lower_shelf_bill_id", lowerId)
            );
            if (lowerShelfBillDetailVOList.isEmpty()) {
                interfaceLogService.interfaceLogInsert("调用获取关于RFID的下架单详情接口", parameter, "失败原因：未获取关于此RFID的下架详情", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：未获取下架详情");

                return map;
            }

            //至此，已获取关于oldRfid的绑定详情、关于lowerId的下架详情，遍历保留相同物料
            for (int i = 0; i < lowerShelfBillDetailVOList.size(); i++) {
                boolean rsg = true;
                for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                    if (lowerShelfBillDetailVOList.get(i).getMaterialCode().equals(materielBindRfidDetail.getMaterielCode())
                            && lowerShelfBillDetailVOList.get(i).getBatchNo().equals(materielBindRfidDetail.getBatchRule())) {
                        rsg = false;
                    }
                }
                if (rsg) {
                    lowerShelfBillDetailVOList.remove(i);
                }
            }
        }

        if (lowerShelfBillDetailVOList.isEmpty()) {
            interfaceLogService.interfaceLogInsert("调用获取关于RFID的下架单详情接口", parameter, "失败原因：未获取关于此RFID中符合条件的的下架详情", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未获取关于此RFID中符合条件的的下架详情");

            return map;
        }
        map.put("lowerShelfBillDetailVOList", lowerShelfBillDetailVOList);
        map.put("result", true);
        map.put("msg", "获取列表成功！");

        return map;
    }
}
