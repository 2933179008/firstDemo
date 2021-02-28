package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.*;
import com.tbl.modules.handheldInterface.service.*;
import com.tbl.modules.instorage.dao.PutBillDetailDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上架详情接口Service实现
 */
@Service(value = "putBillDetailInterfaceService")
public class PutBillDetailInterfaceServiceImpl extends ServiceImpl<PutBillDetailInterfaceDAO, PutBillDetail> implements PutBillDetailInterfaceService {

    //日志接口
    @Autowired
    private InterfaceLogService interfaceLogService;

    //上架单接口DAO
    @Autowired
    private PutBillInterfaceDAO putBillInterfaceDAO;

    @Autowired
    private PutBillDetailInterfaceService putBillDetailInterfaceService;

    //上架单详情接口DAO
    @Autowired
    private PutBillDetailInterfaceDAO putBillDetailInterfaceDAO;

    //入库单详情接口DAO
    @Autowired
    private InstorageDetailInterfaceDAO instorageDetailInterfaceDAO;

    @Autowired
    private InstorageDetailInterfaceService instorageDetailInterfaceService;

    //库位接口DAO
    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    //物料绑定接口DAO
    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    //物料绑定详情接口DAO
    @Autowired
    private MaterielBindRfidDetailInterfaceDAO materielBindRfidDetailInterfaceDAO;

    @Autowired
    private MaterielBindRfidDetailInterfaceService materielBindRfidDetailInterfaceService;

    //物料绑定详情接口Service
    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;

    @Autowired
    private StockChangeInterfaceService stockChangeInterfaceService;

    //库存接口DAO
    @Autowired
    private StockInterfaceDAO stockInterfaceDAO;

    @Autowired
    private StockInterfaceService stockInterfaceService;

    @Autowired
    private UserInterfaceDAO userInterfaceDAO;

    @Autowired
    private PutBillDetailDAO putBillDetailDAO;
    @Autowired
    private MaterielDAO materielDao;
    @Autowired
    private DepotPositionDAO depotPositionDao;
    @Autowired
    private InstorageService instorageService;
    @Autowired
    private StockDAO stockDao;

    @Autowired
    private MaterielInterfaceService materielInterfaceService;

    @Autowired
    private CustomerService customerService;

    @Override
    public String getRecommendPosition(String putBilDetailId) {
        //定义推荐的库位编号
        String recommendPositionCode = "";

        //根据上架单详情id获取物料信息
        PutBillDetail putBillDetail = putBillDetailDAO.selectById(putBilDetailId);
        //物料编号
        String materialCode = putBillDetail.getMaterialCode();
        //批次号
        String batchNo = putBillDetail.getBatchNo();
        //RFID
        String rfid = putBillDetail.getRfid();
        //上架数量
        Double putAmount = putBillDetail.getPutAmount() == null ? 0d : Double.parseDouble(putBillDetail.getPutAmount());
        //上架重量
        Double putWeight = putBillDetail.getPutWeight() == null ? 0d : Double.parseDouble(putBillDetail.getPutWeight());
        //物料的上架分类
        String upshelfClassify = "";
        //根据物料编号查询物料信息
        EntityWrapper<Materiel> materielEntity = new EntityWrapper<Materiel>();
        materielEntity.eq("materiel_code", materialCode);
        List<Materiel> lstMateriel = materielDao.selectList(materielEntity);
        if (lstMateriel != null && lstMateriel.size() > 0) {
            Materiel materiel = lstMateriel.get(0);
            upshelfClassify = materiel.getUpshelfClassify();
        }

        //根据物料编号和批次号去库存表中查询对应的库位（同一库位不同批次号的同一种物料不能混放）
        //注：库存表的数据结构：同物料编号的同批次并且同库位，作为一条数据
        EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
        stockEntity.eq("material_code", materialCode);
        stockEntity.eq("batch_no", batchNo);
        //根据库位编码升序排序
        stockEntity.orderBy("position_code", true);
        List<Stock> lstStock = stockDao.selectList(stockEntity);
        if (lstStock != null && lstStock.size() > 0) {//如果查出的库位存在，则表示该物料已经有对应的库位
            for (Stock stock : lstStock) {
                //获取库位编码
                String positionCode = stock.getPositionCode();
                //获取库位上的托盘库存数量
                Double stockRfidAmount = stock.getStockRfidAmount() == null ? 0d : Double.parseDouble(stock.getStockRfidAmount());
                //获取库位上的库存重量
                Double stockWeight = stock.getStockWeight() == null ? 0d : Double.parseDouble(stock.getStockWeight());

                DepotPosition depotPosition = new DepotPosition();
                depotPosition.setPositionCode(positionCode);
                depotPosition = depotPositionDao.selectOne(depotPosition);
                //库位托盘数量容量
                Double capacityRfidAmount = 0d;
                //库位重量容量
                Double capacityWeight = 0d;
                //获取库位混放类型
                String blendType = "";
                if (depotPosition != null) {
                    capacityRfidAmount = depotPosition.getCapacityRfidAmount() == null ? 0d : Double.parseDouble(depotPosition.getCapacityRfidAmount());
                    capacityWeight = depotPosition.getCapacityWeight() == null ? 0d : Double.parseDouble(depotPosition.getCapacityWeight());
                    blendType = depotPosition.getBlendType();
                }
                //定义其他物料的总的托盘库存数量
                Double otherStockRfidAmountTotal = 0d;
                //定义其他物料的总的库存重量
                Double otherStockWeightTotal = 0d;

                if ("0".equals(blendType)) {//如果库位是可以混放的类型（0 可混放，1 不可混放）
                    /**查询该库位上的其他物料的总的库存托盘数量和库存重量**/
                    EntityWrapper<Stock> stockEntity2 = new EntityWrapper<Stock>();
                    stockEntity2.eq("position_code", positionCode);
                    List<Stock> lstStock2 = stockDao.selectList(stockEntity2);
                    if (lstStock2 != null && lstStock2.size() > 0) {
                        for (Stock Stock : lstStock2) {
                            //排除当前的物料
                            if (materialCode.equals(stock.getMaterialCode())) {
                                continue;
                            }
                            otherStockRfidAmountTotal += stock.getStockRfidAmount() == null ? 0d : Double.parseDouble(stock.getStockRfidAmount());
                            otherStockWeightTotal += stock.getStockWeight() == null ? 0d : Double.parseDouble(stock.getStockWeight());
                        }
                    }

                }

                //剩余库位托盘数量容量
                Double surplusCapacityRfidAmount = capacityRfidAmount - stockRfidAmount - otherStockRfidAmountTotal;
                //剩余库位重量容量
                Double surplusCapacityWeight = capacityWeight - stockWeight - otherStockWeightTotal;
                //如果剩余库位托盘数量容量大于等于1(每次上架只上一个托盘)并且上架重量小于等于剩余库位重量容量，则将该库位作为推荐库位
                //注：如果有多个可以推荐的库位，则取第一个可用库位作为推荐库位
                if (surplusCapacityRfidAmount >= 1 && putWeight <= surplusCapacityWeight) {
                    recommendPositionCode = positionCode;
                    break;
                }

            }
//            //如果推荐库位为空，表示库存中对应的库位都不符合推荐条件,则重新推荐新的库位
//            if("".equals(recommendPositionCode)){
//
//            }

        } else {//如果根据物料编号和批次号查出的库位不存在，则推荐已放有其他物料的可混放类型库位
            //1.推荐已放有其他物料的可混放类型库位，如果可混放类型库位已存放了不同批次号的该物料则不推荐
            //查询已放有其他物料的可混放类型库位信息
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("materialCode", materialCode);
            paramMap.put("upshelfClassify", upshelfClassify);
            List<Map<String, Object>> list = putBillDetailDAO.selectBlendPosition(paramMap);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    //当前物料编号
                    String currentMaterialCode = map.get("material_code") == null ? "" : map.get("material_code").toString();
                    //库位编号
                    String positionCode = map.get("position_code") == null ? "" : map.get("position_code").toString();
                    //库位托盘数量容量
                    Double capacityRfidAmount = map.get("capacity_rfid_amount") == null ? 0d : Double.parseDouble(map.get("capacity_rfid_amount").toString());
                    //库位重量容量
                    Double capacityWeight = map.get("capacity_weight") == null ? 0d : Double.parseDouble(map.get("capacity_weight").toString());
                    //库存托盘数量
                    Double stockRfidAmount = map.get("stock_rfid_amount") == null ? 0d : Double.parseDouble(map.get("stock_rfid_amount").toString());
                    //库存重量
                    Double stockWeight = map.get("stock_weight") == null ? 0d : Double.parseDouble(map.get("stock_weight").toString());

                    //定义其他物料的总的托盘库存数量
                    Double otherStockRfidAmountTotal = 0d;
                    //定义其他物料的总的库存重量
                    Double otherStockWeightTotal = 0d;

                    /**查询该库位上的其他物料的总的库存托盘数量和库存重量**/
                    EntityWrapper<Stock> stockEntity2 = new EntityWrapper<Stock>();
                    stockEntity2.eq("position_code", positionCode);
                    List<Stock> lstStock2 = stockDao.selectList(stockEntity2);
                    if (lstStock2 != null && lstStock2.size() > 0) {
                        for (Stock stock : lstStock2) {
                            //排除当前的物料
                            if (currentMaterialCode.equals(stock.getMaterialCode())) {
                                continue;
                            }
                            otherStockRfidAmountTotal += stock.getStockRfidAmount() == null ? 0d : Double.parseDouble(stock.getStockRfidAmount());
                            otherStockWeightTotal += stock.getStockWeight() == null ? 0d : Double.parseDouble(stock.getStockWeight());
                        }
                    }

                    //剩余库位托盘数量容量
                    Double surplusCapacityRfidAmount = capacityRfidAmount - stockRfidAmount - otherStockRfidAmountTotal;
                    //剩余库位重量容量
                    Double surplusCapacityWeight = capacityWeight - stockWeight - otherStockWeightTotal;
                    //如果剩余库位托盘数量容量大于等于1(每次上架只上一个托盘)并且上架重量小于等于剩余库位重量容量，则将该库位作为推荐库位
                    //注：如果有多个可以推荐的库位，则取第一个可用库位作为推荐库位
                    if (surplusCapacityRfidAmount >= 1 && putWeight <= surplusCapacityWeight) {
                        recommendPositionCode = positionCode;
                        break;
                    }
                }

            }

        }

        //如果推荐库位为空，表示库存中对应的库位都不符合推荐条件,则重新推荐新的库位
        if ("".equals(recommendPositionCode)) {
            //2.推荐没放过物料的全新库位
            //获取库存表中的库位，即都放了物料的库位
            EntityWrapper<Stock> stockEntity3 = new EntityWrapper<Stock>();
            stockEntity3.groupBy("position_code");
            List<Stock> lstStock3 = stockDao.selectList(stockEntity3);
            String positionCodeStr = "";
            if (lstStock3 != null && lstStock3.size() > 0) {
                for (Stock stock : lstStock3) {
                    positionCodeStr += stock.getPositionCode() + ",";
                }
            }
            if (!"".equals(positionCodeStr)) {
                positionCodeStr = positionCodeStr.substring(0, positionCodeStr.length() - 1);
            }
            List<String> lstPositionCode = Arrays.asList(positionCodeStr.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
            //查询没放过物料的全新库位
            List<Map<String, Object>> lstPosition = putBillDetailDAO.selectNewPosition(lstPositionCode);
            if (lstPosition != null && lstPosition.size() > 0) {
                //取第一个库位作为推荐库位
                Map<String, Object> map = lstPosition.get(0);
                recommendPositionCode = map.get("position_code") == null ? "" : map.get("position_code").toString();
            }
        }


        return recommendPositionCode;
    }

    /**
     * 获取上架单详情 并添加操作人，更新上架单状态
     *
     * @param userId
     * @param putBillId
     * @return
     */
    @Override
    public Map<String, Object> getPutBillDetailList(Long userId, Long putBillId) {
        boolean result = true;
        String msg = "获取上架单详情成功";
        Map<String, Object> map = new HashMap<>();

        List<PutBillDetail> putBillDetailList = null;

        String errorinfo = null;
        //若用户id 和 上架单id 不为空则进行更改操作
        if (userId != null && putBillId != null) {
            //根据上架单id 获取详细
            putBillDetailList = putBillDetailInterfaceDAO.selectList(
                    new EntityWrapper<PutBillDetail>()
                            .eq("put_bill_id", putBillId)
            );

            User user = null;
            //获取操作人的名称
            if (userId != null) {
                user = userInterfaceDAO.selectById(userId);
            }
            //根据上架单id 获取上架单并添加操作人并更新上架单当前状态为进行中
            PutBill putBill = putBillInterfaceDAO.selectById(putBillId);
            if (putBill != null) {
                putBill.setOperator(userId);
                if (user != null) {
                    putBill.setOperatorName(user.getUsername());
                }
                putBill.setState(DyylConstant.STATE_WAIT);
                //若更新失败则返回失败信息
                if (putBillInterfaceDAO.updateById(putBill) < 0) {
                    result = false;
                    msg = "失败原因：上架单状态或操作人信息添加失败";
                    errorinfo = DateUtils.getTime();
                } else {
                    //若根据上架单ID未找到相关详情，则导入入库单
                    if (putBillDetailList.isEmpty()) {
                        //获取入库单详情
                        if (putBill.getInstorageBillId() != null) {
                            List<InstorageDetail> instorageDetailList = instorageDetailInterfaceDAO.selectList(
                                    new EntityWrapper<InstorageDetail>()
                                            .eq("instorage_bill_id", putBill.getInstorageBillId())
                            );
                            //遍历添加到上架单详情
                            for (InstorageDetail instorageDetail : instorageDetailList) {
                                PutBillDetail putBillDetail = new PutBillDetail();
                                putBillDetail.setPutBillId(putBillId);
                                putBillDetail.setMaterialCode(instorageDetail.getMaterialCode());
                                putBillDetail.setMaterialName(instorageDetail.getMaterialName());
                                putBillDetail.setBatchNo(instorageDetail.getBatchNo());
                                putBillDetail.setUnit(instorageDetail.getUnit());
                                putBillDetail.setState(DyylConstant.STATE_ON);
                                putBillDetailInterfaceDAO.insert(putBillDetail);
                            }
                            putBillDetailList = putBillDetailInterfaceDAO.selectList(
                                    new EntityWrapper<PutBillDetail>()
                                            .eq("put_bill_id", putBillId)
                            );
                        }

                    }
                }
            } else {
                result = false;
                msg = "失败原因：未查到此上架单信息";
                errorinfo = DateUtils.getTime();
            }

        } else {
            result = false;
            msg = "失败原因：当前用户或上架单ID为空";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用获取上架单详情更新上架状态接口";
        String parameter = "UserId:" + userId.toString() + "/" + "PutBillId:" + putBillId.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", putBillDetailList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 白糖上架
     *
     * @param putBillId
     * @param rfid
     * @param positionId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> sugarStorage(Long putBillId, String rfid, Long positionId, Long userId) {

        boolean result = true;
        String msg = "白糖类型操作成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "PutBillId:" + putBillId + "/Rfid:" + rfid + "/PositionId:" + positionId + "/UserId:" + userId;
        if (positionId == null || Strings.isNullOrEmpty(rfid) || putBillId == null || userId == null) {
            interfaceLogService.interfaceLogInsert("上架白糖入库接口", parameter, "失败原因：未获取参数", DateUtils.getTime());

            map.put("msg", false);
            map.put("result", "失败原因：未获取参数");

            return map;
        }

        //获取上架单详情
        List<PutBillDetail> putBillDetailList = putBillDetailInterfaceDAO.selectList(
                new EntityWrapper<PutBillDetail>()
                        .eq("put_bill_id", putBillId)
        );

        if (putBillDetailList.isEmpty()) {
            interfaceLogService.interfaceLogInsert("上架白糖入库接口", parameter, "失败原因：未获取上架单详情集合", DateUtils.getTime());

            map.put("msg", false);
            map.put("result", "失败原因：未获取上架单详情集合");

            return map;
        }
        //获取库位信息
        DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(positionId);
        if (depotPosition == null) {
            interfaceLogService.interfaceLogInsert("上架白糖入库接口", parameter, "失败原因：ID未获取库位信息", DateUtils.getTime());
            map.put("msg", false);
            map.put("result", "失败原因：未获取库位信息");
            return map;
        }
        if (depotPosition.getPositionFrozen().equals(DyylConstant.FROZEN_1)) {
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：此库位已冻结", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：此库位已冻结");
            return map;
        }

        //新增一条物料绑定RFID信息
        MaterielBindRfid materielBindRfid = new MaterielBindRfid();
        materielBindRfid.setBindCode(materielBindRfidInterfaceService.getBindCode());
        materielBindRfid.setRfid(rfid);
        materielBindRfid.setCreateTime(DateUtils.getTime());
        materielBindRfid.setCreateBy(userId);
        materielBindRfid.setDeletedFlag(DyylConstant.NOTDELETED);
        materielBindRfid.setPositionBy(positionId);
        materielBindRfid.setStatus(DyylConstant.STATE_UNTREATED);
        materielBindRfid.setRemarks("手持机白糖类型上架产生数据");
        materielBindRfid.setInstorageProcess(DyylConstant.INSTORAGEPROCESS1);
        //若新增物料绑定RFID信息成功，则新增对应的物料绑定RFID详情
        if (materielBindRfidInterfaceDAO.insert(materielBindRfid) < 0) {
            interfaceLogService.interfaceLogInsert("调用上架详情参数更新接口", parameter, "失败原因：物料绑定RFID新增信息失败", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：物料绑定RFID新增信息失败");
            return map;
        }

        //遍历 若详情中RFID为空 则给此条详情添加RFID库位更改入库状态，并获取此条详情的id，若详情中RFID不为空则获取详情所有信息
        for (PutBillDetail putBillDetail : putBillDetailList) {
            PutBill putBill = putBillInterfaceDAO.selectById(putBillDetail.getPutBillId());
            //根据库位、物料编码、批次号获取唯一的库存信息
            Stock stock = stockInterfaceService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("position_code", depotPosition.getPositionCode())
                            .eq("material_code", putBillDetail.getMaterialCode())
                            .eq("batch_no", putBillDetail.getBatchNo())
                            .eq("material_type", DyylConstant.MATERIAL_RFID)
            );
            if (stock == null) {
                stock = new Stock();
                stock.setMaterialType(DyylConstant.MATERIAL_RFID);
                stock.setMaterialCode(putBillDetail.getMaterialCode());
                stock.setMaterialName(putBillDetail.getMaterialName());
                stock.setBatchNo(putBillDetail.getBatchNo());
                stock.setPositionCode(depotPosition.getPositionCode());
                stock.setPositionName(depotPosition.getPositionName());
                stock.setRfid(rfid);
                stock.setStockRfidAmount("1");
                stock.setCreateBy(userId);
                stock.setCreateTime(DateUtils.getTime());
                stock.setProductDate(putBillDetail.getProductDate());
                Materiel materiel = materielInterfaceService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code", putBillDetail.getMaterialCode()));
                if (materiel != null) {
                    // 保质期至    时间处理
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String time = DateUtils.dateStringToString(putBillDetail.getProductDate());
                    try {
                        Date date = format.parse(time);
                        int days = Integer.parseInt(materiel.getQualityPeriod());
                        Date qualityDate = DateUtils.addDateDays(date, days);
                        format = new SimpleDateFormat("yyyyMMdd");
                        String qualityDates = format.format(qualityDate);
                        stock.setQualityDate(qualityDates);
                    } catch (Exception e) {
                    }
                    Customer customer = customerService.selectById(materiel.getCustomerBy());
                    if (customer != null) {
                        stock.setCustomerCode(customer.getCustomerCode());
                    }
                }
                stockInterfaceDAO.insert(stock);
            } else {
                stock.setRfid(stock.getRfid() + "," + rfid);
                if (stock.getStockRfidAmount().equals("0") || Strings.isNullOrEmpty(stock.getStockRfidAmount())) {
                    stock.setStockRfidAmount("1");
                } else {
                    Double num = Double.parseDouble(stock.getStockRfidAmount()) + 1;
                    stock.setStockRfidAmount(num.toString());
                }
                stockInterfaceDAO.updateById(stock);
            }

            //若为true，证明上架详情中不存在空RFID的信息，且pd不为null，则添加一条信息详情，并获取新添加的上架详情id
            if (Strings.isNullOrEmpty(putBillDetail.getRfid())) {
                putBillDetail.setRfid(rfid);
                putBillDetail.setPositionCode(depotPosition.getPositionCode());
                putBillDetail.setState(DyylConstant.STATE_UNTREATED);
                putBillDetailInterfaceDAO.updateById(putBillDetail);
            } else {
                putBillDetail.setRfid(rfid);
                putBillDetail.setPositionCode(depotPosition.getPositionCode());
                putBillDetail.setState(DyylConstant.STATE_UNTREATED);
                putBillDetailInterfaceDAO.insert(putBillDetail);
            }
            MaterielBindRfidDetail materielBindRfidDetail = new MaterielBindRfidDetail();
            materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
            materielBindRfidDetail.setMaterielCode(putBillDetail.getMaterialCode());
            materielBindRfidDetail.setMaterielName(putBillDetail.getMaterialName());
            materielBindRfidDetail.setBatchRule(putBillDetail.getBatchNo());
            materielBindRfidDetail.setUnit(putBillDetail.getUnit());
            materielBindRfidDetail.setDeleteFlag(DyylConstant.NOTDELETED);
            materielBindRfidDetail.setRfid(materielBindRfid.getRfid());
            materielBindRfidDetail.setStatus(DyylConstant.STATE_UNTREATED);
            materielBindRfidDetail.setPositionId(materielBindRfid.getPositionBy());
            materielBindRfidDetailInterfaceDAO.insert(materielBindRfidDetail);
            //添加库存变动记录
            StockChange stockChange = new StockChange();
            stockChange.setCreateBy(userId);
            stockChange.setCreateTime(DateUtils.getTime());
            stockChange.setPositionBy(depotPosition.getId());
            stockChange.setMaterialName(putBillDetail.getMaterialName());
            stockChange.setMaterialCode(putBillDetail.getMaterialCode());
            stockChange.setBatchNo(putBillDetail.getBatchNo());
            stockChange.setBusinessType(DyylConstant.DEPOT_IN);
            stockChange.setInWeight(putBillDetail.getPutWeight());
            stockChange.setInAmount(putBillDetail.getPutAmount());
            stockChange.setChangeCode(putBill.getPutBillCode());
            stockChangeInterfaceService.insert(stockChange);
        }

        interfaceLogService.interfaceLogInsert("上架白糖入库接口", parameter, msg, DateUtils.getTime());

        map.put("msg", msg);
        map.put("result", result);

        return map;
    }


    /**
     * 白糖绑定更新重量数量接口
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param weight
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> sugarBind(String rfid, String materialCode, String amount, String weight, Long userId) {

        String msg = "更新成功";
        Map<String, Object> map = new HashMap<>();

        String parameter = "Rfid:" + rfid + "/MaterialCode:" + materialCode + "/Amount:" + amount + "/Weight:" + weight;
        //判断参数是否为空
        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(materialCode) || Strings.isNullOrEmpty(amount) || Strings.isNullOrEmpty(weight)) {
            msg = "失败原因：获取参数存在空值";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //RFID/物料编码可获取关于此条件唯一的上架单详情
        PutBillDetail putBillDetail = putBillDetailInterfaceService.selectOne(
                new EntityWrapper<PutBillDetail>()
                        .eq("rfid", rfid)
                        .eq("material_code", materialCode)
        );
        if (putBillDetail == null) {
            msg = "失败原因：为获取唯一上架单详情";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //获取上架单
        PutBill putBill = putBillInterfaceDAO.selectById(putBillDetail.getPutBillId());
        //获取入库单详情
        if (putBill == null) {
            msg = "失败原因：未获取上架单";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //条件确定唯一入库详情
        InstorageDetail instorageDetail = instorageDetailInterfaceService.selectOne(
                new EntityWrapper<InstorageDetail>()
                        .eq("instorage_bill_id", putBill.getInstorageBillId())
                        .eq("material_code", materialCode)
        );
        if (instorageDetail == null) {
            msg = "失败原因：未获取唯一的入库单详情";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //若可拆分数据量和重量小于上架单数量和重量，则返回错误信息
        if (Double.parseDouble(amount) > Double.parseDouble(instorageDetail.getSeparableAmount()) || Double.parseDouble(weight) > Double.parseDouble(instorageDetail.getSeparableWeight())) {
            msg = "失败原因：上架重量或数量超出入库可拆分的值";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //RFID 物料编码可获取唯一物料绑定详情
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailInterfaceService.selectOne(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("rfid", rfid)
                        .eq("materiel_code", materialCode)
                        .eq("delete_flag", DyylConstant.NOTDELETED)
        );
        if (materielBindRfidDetail == null) {
            msg = "失败原因：为获取唯一物料绑定详情";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //物料绑定ID获取绑定信息
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
        if (materielBindRfid == null || materielBindRfid.getPositionBy() == null || materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED)) {
            msg = "失败原因：ID未获取物料绑定信息或绑定信息不符合白糖绑定条件";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //ID获取库位信息
        DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfid.getPositionBy());
        if (depotPosition == null) {
            msg = "失败原因：ID未获取库位信息";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //验证库位重量容量、托盘容量是否满足
        //获取关于此库位编码的库存
        List<Stock> stockList = stockInterfaceService.selectList(new EntityWrapper<Stock>().eq("position_code",depotPosition.getPositionCode()));
        Double weightStock = 0d;
        Double rfidStockAmout = 0d;
        for (Stock stock : stockList){
            if (weightStock ==0d){
                weightStock = Double.parseDouble(stock.getStockWeight());
            }else {
                weightStock = weightStock + Double.parseDouble(stock.getStockWeight());
            }
            if (rfidStockAmout == 0d){
                rfidStockAmout = Double.parseDouble(stock.getStockRfidAmount());
            }else {
                rfidStockAmout = rfidStockAmout + Double.parseDouble(stock.getStockRfidAmount());
            }
        }
        Double aviWeight = Double.parseDouble(depotPosition.getCapacityWeight()) - weightStock;
        Double aviRfidStockAmount = Double.parseDouble(depotPosition.getCapacityRfidAmount()) - rfidStockAmout;
        if (aviWeight < Double.parseDouble(weight) || aviRfidStockAmount < 1){
            map.put("msg", "失败原因：所在库位重量容量或RFID数量容量不足");
            map.put("result", false);
            return map;
        }

        //List<String> lstRfids = Arrays.stream(rfid.split(",")).map(s -> s.trim()).collect(Collectors.toList());

        //库位编、物料编码、RFID可确定唯一库存
        Stock stock = stockInterfaceService.selectOne(
                new EntityWrapper<Stock>()
                        .eq("material_type", DyylConstant.MATERIAL_RFID)
                        .eq("position_code", depotPosition.getPositionCode())
                        .eq("material_code", materialCode)
                        .like("rfid", rfid)
        );
        if (stock == null) {
            msg = "失败原因：未获取库存信息";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //1.更新上架单详情重量数量
        putBillDetail.setPutWeight(weight);
        putBillDetail.setPutAmount(amount);
        if (putBillDetailInterfaceDAO.updateById(putBillDetail) < 0) {
            msg = "失败原因：上架单数量重量更新失败";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //2.更新对应的入库单详情中可拆分数量和重量
        Double instorageAmount = Double.parseDouble(instorageDetail.getSeparableAmount()) - Double.parseDouble(amount);
        Double instorageWeight = Double.parseDouble(instorageDetail.getSeparableWeight()) - Double.parseDouble(weight);
        instorageDetail.setSeparableWeight(instorageWeight.toString());
        instorageDetail.setSeparableAmount(instorageAmount.toString());
        if (instorageDetailInterfaceDAO.updateById(instorageDetail) < 0) {
            msg = "失败原因：入库单详情数量重量更新失败";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //3.更新绑定详情重量数量
        materielBindRfidDetail.setAmount(amount);
        materielBindRfidDetail.setWeight(weight);
        materielBindRfidDetail.setProductData(putBillDetail.getProductDate());
        if (materielBindRfidDetailInterfaceDAO.updateById(materielBindRfidDetail) < 0) {

            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //4.更新库存数据
        Double stockAmount = Double.valueOf(stock.getStockAmount()) + Double.valueOf(amount);
        Double stockWeight = Double.valueOf(stock.getStockWeight()) + Double.valueOf(weight);

        stock.setStockAmount(stockAmount.toString());
        stock.setStockWeight(stockWeight.toString());
        if (stockInterfaceDAO.updateById(stock) < 0) {
            msg = "失败原因：库存更新失败";
            interfaceLogService.interfaceLogInsert("调用白糖绑定更新重量数量接口", parameter, msg, DateUtils.getTime());

            map.put("msg", msg);
            map.put("result", false);
            return map;
        }

        //5.更新入库状态
        Instorage instorage = instorageService.selectById(instorageDetail.getInstorageBillId());
        //白糖类型的入库单对应的物料只会有一种，所以只需要判断一种物料的可拆分数量和重量
        if(instorageAmount == 0d && instorageWeight == 0d){
            instorage.setState(DyylConstant.STATE_COMPLETED);
            instorageService.updateById(instorage);

        }

        //6.添加库存变动记录
        StockChange stockChange = new StockChange();
        stockChange.setChangeCode(instorage.getInstorageBillCode());
        stockChange.setMaterialName(instorageDetail.getMaterialName());
        stockChange.setMaterialCode(instorageDetail.getMaterialCode());
        stockChange.setBatchNo(instorageDetail.getBatchNo());
        stockChange.setBusinessType(DyylConstant.DEPOT_IN);
        stockChange.setInAmount(amount);
        stockChange.setInWeight(weight);
        stockChange.setCreateTime(DateUtils.getTime());
        stockChange.setCreateBy(userId);
        if (!stockChangeInterfaceService.insert(stockChange)) {
            map.put("msg", "失败原因：库存变更记录更新失败");
            map.put("result", false);
            return map;
        }

        map.put("msg", msg);
        map.put("result", true);
        return map;
    }

    /**
     * 上架详情物料更新参数
     *
     * @param paramMap
     * @return
     */
    @Override
    public Map<String, Object> getPutBillDetail(Map<String, Object> paramMap) {

        String msg = "上架详情物料参数更新成功";
        Map<String, Object> map = new HashMap<>();

        //获取上架单ID
        String putBillId = paramMap.get("putBillId").toString();

        //获取入库单ID
        String instorageBillId = paramMap.get("instorageBillId").toString();
        //获取物料绑定详情
        Gson gson = new Gson();
        List<MaterielBindRfidDetail> materielBindRfidDetailList = gson.fromJson(paramMap.get("data").toString(),
                new TypeToken<List<MaterielBindRfidDetail>>() {
                }.getType());
        String parameter = "putBillId:" + putBillId + "/instorageBillId" + instorageBillId + "/materielBindRfidDetailList" + materielBindRfidDetailList;
        //判断获取参数是否为空
        if (materielBindRfidDetailList.isEmpty() || Strings.isNullOrEmpty(instorageBillId) || Strings.isNullOrEmpty(putBillId)) {
            msg = "失败原因：获取参数存在空值";
            interfaceLogService.interfaceLogInsert("调用上架详情物料添加参数", parameter, msg, DateUtils.getTime());

            map.put("result", false);
            map.put("msg", msg);
            return map;
        }
        //获取上架单详情
        List<PutBillDetail> putBillDetailList = putBillDetailInterfaceDAO.selectList(
                new EntityWrapper<PutBillDetail>()
                        .eq("put_bill_id", putBillId)
        );
        //获取入库单详情
        List<InstorageDetail> instorageDetailList = instorageDetailInterfaceDAO.selectList(
                new EntityWrapper<InstorageDetail>()
                        .eq("instorage_bill_id", instorageBillId)
        );

        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            for (InstorageDetail instorageDetail : instorageDetailList) {
                //判断物料编码是否相同
                if (materielBindRfidDetail.getMaterielCode().equals(instorageDetail.getMaterialCode())) {
                    //判断相同物料，数量重量是否在可拆分范围
                    if (Double.parseDouble(materielBindRfidDetail.getAmount()) > Double.parseDouble(instorageDetail.getSeparableAmount())
                            && Double.parseDouble(materielBindRfidDetail.getWeight()) > Double.parseDouble(instorageDetail.getSeparableWeight())) {
                        msg = "失败原因：物料数量或重量超出可拆分范围";
                        interfaceLogService.interfaceLogInsert("调用上架详情物料添加参数", parameter, msg, DateUtils.getTime());

                        map.put("result", false);
                        map.put("msg", msg);
                        return map;
                    }
                }
            }
        }

        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            for (PutBillDetail putBillDetail : putBillDetailList) {
                //相同物料插入上架详情重量和数量、库位，并添加物料绑定批次号
                if (materielBindRfidDetail.getMaterielCode().equals(putBillDetail.getMaterialCode())) {
                    int count = 0;
                    if (Strings.isNullOrEmpty(putBillDetail.getRfid())) {
                        putBillDetail.setPutAmount(materielBindRfidDetail.getAmount());
                        putBillDetail.setPutWeight(materielBindRfidDetail.getWeight());
                        if (materielBindRfidDetail.getMaterielBindRfidBy() != null) {
                            MaterielBindRfid mbr = materielBindRfidInterfaceDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
                            if (mbr != null) {
                                putBillDetail.setRfid(mbr.getRfid());
                            }
                        }

                        if (materielBindRfidDetail.getPositionId() != null) {
                            DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                            if (depotPosition != null) {
                                putBillDetail.setPositionCode(depotPosition.getPositionCode());
                            }
                        }

                        count = putBillDetailInterfaceDAO.updateById(putBillDetail);
                    } else {
                        PutBillDetail pd = new PutBillDetail();
                        pd.setPutBillId(Long.parseLong(putBillId));
                        pd.setMaterialCode(materielBindRfidDetail.getMaterielCode());
                        pd.setMaterialName(materielBindRfidDetail.getMaterielName());
                        pd.setBatchNo(putBillDetail.getBatchNo());
                        pd.setUnit(materielBindRfidDetail.getUnit());
                        pd.setRfid(materielBindRfidDetail.getRfid());
                        pd.setPutAmount(materielBindRfidDetail.getAmount());
                        pd.setPutWeight(materielBindRfidDetail.getWeight());

                        if (materielBindRfidDetail.getPositionId() != null) {
                            DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(materielBindRfidDetail.getPositionId());
                            if (depotPosition != null) {
                                pd.setPositionCode(depotPosition.getPositionCode());
                            }
                        }

                        count = putBillDetailInterfaceDAO.insert(pd);
                    }

                    if (count < 0) {
                        msg = "失败原因：上架单详情物料参数更新失败";
                        interfaceLogService.interfaceLogInsert("调用上架详情物料添加参数", parameter, msg, DateUtils.getTime());

                        map.put("result", false);
                        map.put("msg", msg);
                        return map;
                    }
                    break;
                }
            }
        }

        map.put("result", true);
        map.put("msg", msg);
        return map;
    }

}
