package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.DepotPositionInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MovePositionInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.StockChangeInterfaceDAO;
import com.tbl.modules.handheldInterface.service.DepotPositionInterfaceService;
import com.tbl.modules.handheldInterface.service.MaterielBindRfidDetailInterfaceService;
import com.tbl.modules.handheldInterface.service.MaterielBindRfidInterfaceService;
import com.tbl.modules.handheldInterface.service.MovePositionInterfaceService;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MovePosition;
import com.tbl.modules.stock.entity.StockChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 移库接口Service实现
 */
@Service(value = "movePositionInterfaceService")
public class MovePositionInterfaceServiceImpl extends ServiceImpl<MovePositionInterfaceDAO, MovePosition> implements MovePositionInterfaceService {

    @Autowired
    private DepotPositionInterfaceDAO depotPositionInterfaceDAO;

    @Autowired
    private MovePositionInterfaceDAO movePositionInterfaceDAO;

    @Autowired
    private MaterielDAO materielDAO;

    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDAO;

    @Autowired
    private StockChangeInterfaceDAO stockChangeInterfaceDAO;

    @Autowired
    private DepotPositionInterfaceService depotPositionInterfaceService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private PutBillService putBillService;

    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;

    @Autowired
    private MaterielBindRfidDetailInterfaceService materielBindRfidDetailInterfaceService;

    @Override
    public boolean isBundingSugar(String rfid) {
        boolean result = false;
        //（1）如果是白糖类型的rfid，判断是否做过绑定，如果没做过绑定则不给移库
        //判断没做过绑定的白糖类型的rfid：该rfid只绑定了一种物料，并且如果该绑定的rfid是“已入库”状态，并且数量和重量都为0或空
        List<Map<String, Object>> materialList = movePositionInterfaceDAO.getMaterialBundingDetail(rfid);
        if (materialList != null && materialList.size() == 1) {
            Map<String, Object> materialMap = materialList.get(0);
            //绑定的数量
            Double amount = materialMap.get("amount") == null || "".equals(materialMap.get("amount").toString().trim()) ? 0d : Double.parseDouble(materialMap.get("amount").toString());
            //绑定的重量
            Double weight = materialMap.get("weight") == null || "".equals(materialMap.get("weight").toString().trim()) ? 0d : Double.parseDouble(materialMap.get("weight").toString());
            if (amount == 0 && weight == 0) {
                result = true;
            } else {
                result = false;
            }

        } else {
            result = false;
        }

        return result;
    }

    /**
     * 库位判断是否符合条件
     * @param rfid
     * @param positionCode
     * @return
     */
    @Override
    public Map<String, Object> isAvailablePosition(String rfid, String positionCode) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean result = false;
        String msg = "";

        //2.判断选中的库位上是否有物料，如果有物料，判断是否可以混放，再判断这个库位的容量
        //（1）获取库位的属性信息
        //混放类型（ 0表示能混放 1表示不能混放）
        String blendType = "";
        //ABC分类
        String classify = "";
        //托盘容量
        Integer capacityRfidAmount = 0;
        //重量容量
        Double capacityWeight = 0d;
        //库位冻结（0:未冻结；1：冻结)
        String positionFrozen = "";

        EntityWrapper<DepotPosition> depotPositionEntity = new EntityWrapper<DepotPosition>();
        depotPositionEntity.eq("position_code", positionCode);
        List<DepotPosition> depotPositionList = depotPositionInterfaceService.selectList(depotPositionEntity);
        if (depotPositionList != null && depotPositionList.size() > 0) {
            //库位编号是唯一的，所以取第一条数据
            DepotPosition depotPosition = depotPositionList.get(0);
            blendType = depotPosition.getBlendType();
            classify = depotPosition.getClassify();
            capacityRfidAmount = depotPosition.getCapacityRfidAmount() == null ? 0 : Integer.parseInt(depotPosition.getCapacityRfidAmount());
            capacityWeight = depotPosition.getCapacityWeight() == null ? 0d : Double.parseDouble(depotPosition.getCapacityWeight());
            positionFrozen = depotPosition.getPositionFrozen();
        }
        if ("1".equals(positionFrozen)) {
            result = false;
            msg = "该库位已冻结，不能移位，请确认";
            resultMap.put("result", result);
            resultMap.put("msg", msg);
            return resultMap;
        }

        //获取rfid绑定的物料信息
        List<Map<String, Object>> materialBundingList = movePositionInterfaceDAO.getMaterialBundingDetail(rfid);
        if ("1".equals(blendType)) {//如果是不混放类型的库位
            if (materialBundingList != null && materialBundingList.size() > 0) {
                if (materialBundingList.size() == 1) { //这边表示：如果rfid只绑定了一种物料
                    Map<String, Object> materialBundingMap = materialBundingList.get(0);
                    //托盘绑定的物料编号
                    String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
                    //托盘绑定的物料重量
                    Double rfidWeight = materialBundingMap.get("weight") == null ? 0d : Double.parseDouble(materialBundingMap.get("weight").toString());

                    //根据物料编号获取物料的“ABC”分类
                    String materialClassify = "";
                    EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
                    materialEntity.eq("materiel_code", materialCode);
                    materialEntity.eq("deleted_flag", "1");
                    List<Materiel> materialList = materielDAO.selectList(materialEntity);
                    if (materialList != null && materialList.size() > 0) {
                        //物料编号是唯一的，所以取第一条
                        Materiel material = materialList.get(0);
                        materialClassify = material.getUpshelfClassify() == null ? "" : material.getUpshelfClassify();
                    }

                    if (StringUtils.isBlank(materialClassify) || classify.equals(materialClassify)) {//判断物料的“ABC分类”是否和库位的分类一致
                        //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
                        Map<String, Object> stockMap = movePositionInterfaceDAO.getStockInfo(positionCode);
                        if (stockMap != null) { //如果有物料
                            //库存中的物料编号
                            String stockMaterialCode = stockMap.get("material_code").toString();
                            if (stockMaterialCode.equals(materialCode)) {
                                //库存中的物料重量
                                Double stockWeight = stockMap.get("stock_weight_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_weight_sum").toString());
                                //库存中的托盘库存数量
                                Double stockRfidAmount = stockMap.get("stock_rfid_amount_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());
                                //库位的剩余重量容量
                                Double surplusCapacityWeight = capacityWeight - stockWeight;
                                //库位的剩余托盘库存数量容量
                                Double surplusCapacityStockRfidAmount = capacityRfidAmount - stockRfidAmount;

                                //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                                if (surplusCapacityWeight >= rfidWeight && surplusCapacityStockRfidAmount >= 1) {
                                    result = true;
                                    msg = "该库位可以上架";
                                } else {
                                    result = false;
                                    msg = "该库位容量不够，请确认";
                                }

                            } else {//如果没有物料，表示空库位
                                //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                                if (capacityWeight >= rfidWeight && capacityRfidAmount >= 1) {
                                    result = true;
                                    msg = "该库位可以上架";
                                } else {
                                    result = false;
                                    msg = "该库位容量不够，请确认";
                                }
                            }
                        }
                    } else {
                        result = false;
                        msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
                    }
                } else {//如果rfid绑定了多种物料，则不能入“不混放”类型的库位
                    result = false;
                    msg = "该库位不可混放，请确认托盘中是否有多种物料";
                }
            } else { //如果rfid绑定的物料没有，表示是白糖类型的，不做校验
                result = true;
                msg = "该库位可以上架";
            }
        } else {//如果是混放类型的库位，
            if (materialBundingList != null && materialBundingList.size() > 0) {
                //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
                Map<String, Object> stockMap = movePositionInterfaceDAO.getStockInfo(positionCode);
                //库位的剩余重量容量
                Double surplusCapacityWeight = 0d;
                //库位的剩余托盘库存数量容量
                Double surplusCapacityStockRfidAmount = 0d;
                if (stockMap != null) {//如果有物料
                    //库存中该库位总的物料数量
                    Double stockWeightTotal = stockMap.get("stock_weight_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_weight_sum").toString());
                    //库存中该库位总的托盘库存数量
                    Double stockRfidAmountTotal = stockMap.get("stock_rfid_amount_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());

                    surplusCapacityWeight = capacityWeight - stockWeightTotal;
                    surplusCapacityStockRfidAmount = capacityRfidAmount - stockRfidAmountTotal;

                } else {//如果没有物料，表示空库位
                    surplusCapacityWeight = capacityWeight;
                    surplusCapacityStockRfidAmount = Double.parseDouble(capacityRfidAmount.toString());
                }
                //该托盘上物料的“ABC”分类集合
                List<String> materialClassifyList = new ArrayList<>();
                //托盘中总的物料重量
                Double rfidWeightTotal = 0d;
                for (Map<String, Object> materialBundingMap : materialBundingList) {
                    //托盘绑定的物料编号
                    String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
                    //托盘绑定的物料重量
                    Double rfidWeight = materialBundingMap.get("weight") == null ? 0d : Double.parseDouble(materialBundingMap.get("weight").toString());

                    rfidWeightTotal += rfidWeight;

                    //根据物料编号获取物料的“ABC”分类
                    String materialClassify = "";
                    EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
                    materialEntity.eq("materiel_code", materialCode);
                    materialEntity.eq("deleted_flag", "1");
                    List<Materiel> materialList = materielDAO.selectList(materialEntity);
                    if (materialList != null && materialList.size() > 0) {
                        //物料编号是唯一的，所以取第一条
                        Materiel material = materialList.get(0);
                        materialClassify = material.getUpshelfClassify() == null ? "" : material.getUpshelfClassify();
                    }
                    materialClassifyList.add(materialClassify);

                }


                /**判断“ABC分类”是否符合=====start====**/
                //定义该托盘物料“ABC分类”属性是否与库位“ABC分类”属性符合的字段
                boolean isAccord = false;

                //分类规则：托盘中有物料AB类则放A货架，AC放A货架，BC放B货架，A和不填放A货架，B和不填放B货架，C和不填放C货架
                //去除集合中的空字符串
                List<String> materialClassifyList1 = materialClassifyList.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
                if (materialClassifyList1 != null && materialClassifyList1.size() > 0) {
                    if (materialClassifyList1.size() > 1) {
                        //再去除集合中的重复元素
                        List<String> materialClassifyList2 = new ArrayList<String>(new HashSet<String>(materialClassifyList1));
                        if (materialClassifyList2.size() > 1) { //如果再去除重复元素后托盘中的所有物料有多种“ABC分类”的属性
                            //这边只有三种情况：AB，AC，BC,所以可放的库位类型不是A就是B
                            if (materialClassifyList2.contains("A")) {
                                if ("A".equals(classify)) {
                                    isAccord = true;
                                } else {
                                    isAccord = false;
                                }
                            } else {
                                if ("B".equals(classify)) {
                                    isAccord = true;
                                } else {
                                    isAccord = false;
                                }
                            }
                        } else {//如果再去除重复元素后托盘中的所有物料只有一种“ABC分类”的属性,则放该种分类的库位
                            if (materialClassifyList2.contains(classify)) {
                                isAccord = true;
                            } else {
                                isAccord = false;
                            }
                        }

                    } else {//如果去除空字符串后托盘中的所有物料只有一种“ABC分类”的属性，则可以放任意类型库位
                        isAccord = true;
                    }

                } else {//该种情况表示托盘中的所有物料都没有填写“ABC分类”属性，则可以放任意类型库位
                    isAccord = true;
                }

                /**判断“ABC分类”是否符合=====end====**/

                if (isAccord) {//如果库位类型符合
                    //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                    if (surplusCapacityWeight >= rfidWeightTotal && surplusCapacityStockRfidAmount >= 1) {
                        result = true;
                        msg = "该库位可以移库";
                    } else {
                        result = false;
                        msg = "该库位容量不够，请确认";
                    }
                } else {
                    result = false;
                    msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
                }
            } else {//如果没有物料，表示空库位
                result = true;
                msg = "该库位可以上架";
            }
        }
        resultMap.put("result", result);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    public Integer getSelectAvailableRfid(String materialCode1, String batchNo1, String formerpositionCode, String rfid) {
        return movePositionInterfaceDAO.getSelectAvailableRfid(materialCode1, batchNo1, formerpositionCode, rfid);
    }

    @Override
    public Map<String, Object> upRfid(String rfid) {

        Map<String, Object> map = new HashMap<>();
        boolean result = true;
        String msg = "";

        //原库位编号
        String formerpositionCode = "";
        List<MaterielBindRfid> materielBindRfidList = materielBindRfidInterfaceDAO.selectList(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
                        .eq("rfid", rfid)
        );
        MaterielBindRfid mbr = null;
        String errorinfo = null;
        if (materielBindRfidList.size() == 1) {
            for (MaterielBindRfid materielBindRfid : materielBindRfidList) {
                mbr = materielBindRfid;
            }
            DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(mbr.getPositionBy());
            if (depotPosition != null) {
                formerpositionCode = depotPosition.getPositionCode();
                //根据rfid查询绑定的物料
                List<Map<String, Object>> rfidDetailList = materielBindRfidDetailDAO.getMaterialBundingDetail(rfid);
                if (rfidDetailList != null && rfidDetailList.size() > 0) {
                    //如果是白糖类型的rfid，判断是否做过绑定，如果没做过绑定则不给移库
                    //判断没做过绑定的白糖类型的rfid：该rfid只绑定了一种物料，并且如果该绑定的rfid是“已入库”状态，并且数量和重量都为0或空
                    boolean b = isBundingSugar(rfid);
                    if (b) {
                        result = false;
                        msg = "失败原因：该托盘绑定的物料是未做过绑定的白糖类型的物料，不能移库，请确认";
                        errorinfo = DateUtils.getTime();
                    } else {
                        //该rfid上所有物料占用的库存数量总数
                        Double occupyStockAmountTotal = 0d;
                        //该rfid上所有物料占用的库存重量总数
                        Double occupyStockWeightTotal = 0d;

                        //物料编号(如果该托盘绑定多钟物料，只要取其中一种物料)
                        String materialCode1 = "";
                        //批次号(如果该托盘绑定多钟物料，只要取其中一种物料的批次号)
                        String batchNo1 = "";
                        for (Map<String, Object> map2 : rfidDetailList) {
                            occupyStockAmountTotal += map2.get("occupy_stock_amount") == null || "".equals(map2.get("occupy_stock_amount").toString()) ? 0d : Double.parseDouble(map2.get("occupy_stock_amount").toString());
                            occupyStockWeightTotal += map2.get("occupy_stock_weight") == null || "".equals(map2.get("occupy_stock_weight").toString()) ? 0d : Double.parseDouble(map2.get("occupy_stock_weight").toString());

                            materialCode1 = map2.get("materiel_code") == null ? "" : map2.get("materiel_code").toString();
                            batchNo1 = map2.get("batch_no") == null ? "" : map2.get("batch_no").toString();
                        }
                        if (occupyStockAmountTotal > 0 || occupyStockWeightTotal > 0) {
                            result = false;
                            msg = "失败原因：该托盘中有物料已被占用，不能移位，请确认";
                            errorinfo = DateUtils.getTime();
                        } else {
                            /**验证该rfid托盘是否可用**/
                            //查询该rfid是否可用
                            Integer count = getSelectAvailableRfid(materialCode1, batchNo1, formerpositionCode, rfid);
                            if (count < 0) {
                                result = false;
                                msg = "失败原因：该托盘未通过质检，不可用，请确认";
                                errorinfo = DateUtils.getTime();
                            }
                        }
                    }
                } else {
                    result = false;
                    msg = "失败原因：该托盘没绑定物料，请确认！";
                    errorinfo = DateUtils.getTime();
                }
            } else {
                result = false;
                msg = "失败原因：该托盘无绑定库位信息，请确认！";
                errorinfo = DateUtils.getTime();
            }
        } else {
            result = false;
            msg = "失败原因：该托盘无绑定关系或未入库，请确认！";
            errorinfo = DateUtils.getTime();
        }
        String interfacename = "调用移库单提交接口:Rfid验证逻辑失败";
        String parameter = "rfid:" + rfid;
        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveOver(String movePositionId, String rfid, String positionCode, Long userId) {

        /**2.更新移库表的库位编码和其他字段**/
        //库位id
        Long positionId = 0l;
        //库位名称
        String positionName = "";

        EntityWrapper<DepotPosition> depotPositionEntity = new EntityWrapper<DepotPosition>();
        depotPositionEntity.eq("position_code", positionCode);
        List<DepotPosition> lstDepotPosition = depotPositionInterfaceDAO.selectList(depotPositionEntity);
        if (lstDepotPosition != null && lstDepotPosition.size() > 0) {
            //数据是唯一的，取第一条
            DepotPosition depotPosition = lstDepotPosition.get(0);
            positionId = depotPosition.getId();
            positionName = depotPosition.getPositionName();
        }
        //更新移库表
        movePositionInterfaceDAO.updateMovePositionById(movePositionId);

        /**3.更新物料绑定rfid表库位id和详情表库位id**/
        //根据移库表的id查询原库位主键id和库位编号
        Map<String, Object> formerPositionIdMap = movePositionInterfaceDAO.selectFormerPosition(movePositionId);
        //原库位编码
        String formerPositionCode = "";
        if (formerPositionIdMap != null) {
            formerPositionCode = formerPositionIdMap.get("position_code") == null ? "" : formerPositionIdMap.get("position_code").toString();
        }

        //绑定表id
        String materielBindId = "";
        //绑定详情表id集合
        List<String> lstMaterielBindDetailId = new ArrayList<>();
        List<Map<String, Object>> lstMaterialBunding = movePositionInterfaceDAO.selectMaterialBundingByRfid(rfid);
        if (lstMaterialBunding != null && lstMaterialBunding.size() > 0) {
            for (Map<String, Object> materialBundingMap : lstMaterialBunding) {
                materielBindId = materialBundingMap.get("materielBindId") == null ? "" : materialBundingMap.get("materielBindId").toString();
                String materielBindDetailId = materialBundingMap.get("materielBindDetailId") == null ? "" : materialBundingMap.get("materielBindDetailId").toString();
                lstMaterielBindDetailId.add(materielBindDetailId);

                /**4.插入库存变动表(“移位-入库”类型的)**/
                //绑定单号
                String bindCode = materialBundingMap.get("bind_code") == null ? "" : materialBundingMap.get("bind_code").toString();
                //物料编号
                String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
                //物料名称
                String materialName = materialBundingMap.get("materiel_name") == null ? "" : materialBundingMap.get("materiel_name").toString();
                //批次号
                String batchNo = materialBundingMap.get("batch_rule") == null ? "" : materialBundingMap.get("batch_rule").toString();
                //数量
                String amount = materialBundingMap.get("amount") == null ? "" : materialBundingMap.get("amount").toString();
                //重量
                String weight = materialBundingMap.get("weight") == null ? "" : materialBundingMap.get("weight").toString();
                StockChange stockChange = new StockChange();
                stockChange.setChangeCode(bindCode);
                stockChange.setMaterialCode(materialCode);
                stockChange.setMaterialName(materialName);
                stockChange.setBatchNo(batchNo);
                //设置“移位-入库”类型
                stockChange.setBusinessType("3");
                stockChange.setInAmount(amount);
                stockChange.setOutAmount(amount);
                stockChange.setInWeight(weight);
                stockChange.setOutWeight(weight);
                stockChange.setPositionBy(positionId);
                stockChange.setCreateTime(DateUtils.getTime());
                stockChange.setCreateBy(userId);
                stockChangeInterfaceDAO.insert(stockChange);

                /**5.更新库存表**/
                /**（1）减去原库位的库存**/
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("materialCode", materialCode);
                paramMap.put("batchNo", batchNo);
                paramMap.put("formerPositionCode", formerPositionCode);
                paramMap.put("amount", amount);
                paramMap.put("weight", weight);
                paramMap.put("rfid", rfid);
                movePositionInterfaceDAO.updateStockFormerPosition(paramMap);
                /**如果原库位的库存量为0，则将库存表中的数据删除**/
                //查询库存数量和重量
                Map<String, Object> stockMap = movePositionInterfaceDAO.selectStockAmountAndWeight(materialCode, batchNo, formerPositionCode);
                //生产日期
                String productDate = "";
                //货位编号
                String customerCode = "";
                if (stockMap != null) {
                    Double stockAmount = stockMap.get("stock_amount") == null ? 0d : Double.parseDouble(stockMap.get("stock_amount").toString());
                    Double stockWeight = stockMap.get("stock_weight") == null ? 0d : Double.parseDouble(stockMap.get("stock_weight").toString());
                    if (stockAmount == 0 && stockWeight == 0) {
                        //根据物料编号，批次号，库位编号删除该条数据
                        movePositionInterfaceDAO.deleteStock(materialCode, batchNo, formerPositionCode);
                    }
                    productDate = stockMap.get("product_date").toString();
                    customerCode = stockMap.get("customer_code").toString();
                }

                /**（2）加上现库位的库存**/
                //先根据物料编号、批次号、库位编号查询库存中是否存在该条数据
                Integer count2 = movePositionInterfaceDAO.selectStockPositionCount(materialCode, batchNo, positionCode);
                if (count2 > 0) {//更新现库位的库存
                    Map<String, Object> paramMap1 = new HashMap<>();
                    paramMap1.put("materialCode", materialCode);
                    paramMap1.put("batchNo", batchNo);
                    paramMap1.put("positionCode", positionCode);
                    paramMap1.put("amount", amount);
                    paramMap1.put("weight", weight);
                    paramMap1.put("rfid", rfid);
                    movePositionInterfaceDAO.updateStockCurrentPosition(paramMap1);
                } else {//插入现库位的库存
                    Map<String, Object> paramMap1 = new HashMap<>();
                    paramMap1.put("materialCode", materialCode);
                    paramMap1.put("materialName", materialName);
                    paramMap1.put("batchNo", batchNo);
                    paramMap1.put("positionCode", positionCode);
                    paramMap1.put("positionName", positionName);
                    paramMap1.put("amount", amount);
                    paramMap1.put("weight", weight);
                    paramMap1.put("rfid", rfid);
                    paramMap1.put("userId", userId);
                    paramMap1.put("productDate",productDate);
                    paramMap1.put("customerCode",customerCode);
                    movePositionInterfaceDAO.insertStockCurrentPosition(paramMap1);
                }
            }
        }
        //更新绑定表库位id
        movePositionInterfaceDAO.updateMaterialBundingPosition(materielBindId, positionId);
        //更新绑定详情表库位id
        movePositionInterfaceDAO.updateMaterialBundingDetailPosition(lstMaterielBindDetailId, positionId);

    }

    /**
     * 未完成的移库单
     *
     * @param moveUserId
     * @return
     */
    @Override
    public Map<String, Object> getUnfinishedMovePosition(Long moveUserId) {


        boolean result = true;
        String msg = "数据获取成功";
        Map<String, Object> map = new HashMap<>();

        String errorinfo = null;
        List<MovePosition> movePositionList = null;
        if (moveUserId != null) {
            movePositionList = movePositionInterfaceDAO.selectList(
                    new EntityWrapper<MovePosition>()
                            .eq("move_user_id", moveUserId)
                            .eq("status", DyylConstant.STATUS_IN)
                            .or()
                            .eq("status", DyylConstant.STATUS_NO)
                            .eq("move_user_id", moveUserId)
                            .orderBy(true, "status")
            );
            if (movePositionList.isEmpty()) {
                result = false;
                msg = "未获取到相关数据";
            } else {
                for (MovePosition movePosition : movePositionList) {
                    if (movePosition.getFormerPosition() != null) {
                        DepotPosition formerPosition = depotPositionInterfaceDAO.selectById(movePosition.getFormerPosition());
                        if (formerPosition != null) {
                            movePosition.setFormerPositionName(formerPosition.getPositionName());
                            movePosition.setFormerPositionCode(formerPosition.getPositionCode());
                        }
                    }

                    if (movePosition.getPositionBy() != null) {
                        DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(movePosition.getPositionBy());
                        if (depotPosition != null) {
                            movePosition.setPositionName(depotPosition.getPositionName());
                            movePosition.setPositionCode(depotPosition.getPositionCode());
                        }
                    }
                }
            }
        } else {
            result = false;
            msg = "失败原因：获取当前用户ID失败";
            errorinfo = DateUtils.getTime();
        }

        String interfacename = "调用未完成移库调用接口";
        String parameter = "MoveUserId:" + moveUserId.toString();

        interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);

        map.put("data", movePositionList);
        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * 移库
     *
     * @param rfid
     * @param positionCode
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> doMovePosition(String rfid, String positionCode, Long userId) {

        boolean result = false;
        String msg = "移库失败";
        Map<String, Object> map = new HashMap<>();

        String parameter = "rfid:" + rfid + "/positionCode:" + positionCode;
        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(positionCode) || Strings.isNullOrEmpty(userId.toString())) {
            map.put("result", false);
            map.put("msg", "失败原因：未获取参数");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:未获取参数", parameter, msg, DateUtils.getTime());
            return map;
        }
        //rfid移位验证逻辑
        map = upRfid(rfid);
        if (!(boolean) map.get("result")) {
            map.put("result", false);
            map.put("msg", "失败原因：rfid未通过验证");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:rfid未通过验证", parameter, msg, DateUtils.getTime());
            return map;
        }
        DepotPosition depotPosition = depotPositionInterfaceService.selectOne(
                new EntityWrapper<DepotPosition>()
                        .eq("position_code", positionCode)
        );

        if (depotPosition == null || depotPosition.getPositionFrozen().equals(DyylConstant.FROZEN_1)) {
            map.put("result", false);
            map.put("msg", "失败原因：未找到对应库位或库位已冻结");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:未找到对应库位", parameter, "失败原因：未找到对应库位或库位已冻结", DateUtils.getTime());
            return map;
        }

        //判断库位是否可用，即验证所选的库位是否可以放该托盘rfid
        map = putBillService.isAvailablePosition(rfid, positionCode);
        if (!(boolean) map.get("result")) {
            map.put("result", false);
            map.put("msg", "失败原因：库位验证未通过");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:库位验证未通过", parameter, msg, DateUtils.getTime());
            return map;
        }
        //获取符合条件的物料绑定
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.materielBindRfid(rfid);
        if (materielBindRfid == null) {
            map.put("result", false);
            map.put("msg", "失败原因：未获取RFID对应绑定信息");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:未获取RFID对应绑定信息", parameter, msg, DateUtils.getTime());
            return map;
        }
        MovePosition movePosition = new MovePosition();
        movePosition.setMovePositionCode(materielBindRfid.getBindCode());
        movePosition.setMovePositionCode(materielBindRfid.getBindCode());
        movePosition.setRfid(rfid);
        movePosition.setMoveUserId(userId);
        movePosition.setFormerPosition(materielBindRfid.getPositionBy());
        movePosition.setPositionBy(depotPosition.getId());
        movePosition.setMoveFoundTime(DateUtils.getTime());
        movePosition.setMovePositionTime(DateUtils.getTime());
        movePosition.setCompleteTime(DateUtils.getTime());
        movePosition.setStatus(DyylConstant.STATUS_IN);
        movePosition.setDeleteFlag(DyylConstant.NOTDELETED);
        movePosition.setRemarks("手持机创建移位单");
        if (movePositionInterfaceDAO.insert(movePosition) < 0) {
            map.put("result", false);
            map.put("msg", "失败原因：新增移库单失败");
            interfaceLogService.interfaceLogInsert("调用移库单提交接口:新增移库单失败", parameter, msg, DateUtils.getTime());
            return map;
        }

        result = statusMovePosition(movePosition.getId(), userId);
        if (result) {
            msg = "移库成功";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

//    /**
//     * 移库单提交
//     *
//     * @param movePositionId
//     * @param userId
//     * @return
//     */
//    @Override
//    public Map<String, Object> completeMovePosition(Long movePositionId, Long userId) {
//
//        boolean result = true;
//        String msg = "移位成功";
//        Map<String, Object> map = new HashMap<>();
//
//        result = statusMovePosition(movePosition.getMoveUserId(), userId);
//        String errorinfo = null;
//        if (!result) {
//            msg = "失败原因：移位数据或库存数据更改失败";
//            errorinfo = DateUtils.getTime();
//            String interfacename = "调用移库单提交接口";
//            String parameter = "movePositionId:" + movePositionId + "/userId:" + userId;
//
//            interfaceLogService.interfaceLogInsert(interfacename, parameter, msg, errorinfo);
//        }
//
//        map.put("result", result);
//        map.put("msg", msg);
//
//        return map;
//    }

    /**
     * 开始移位/移位完成
     *
     * @author yuany
     * @date 2019-01-22
     */
    @Override
    public boolean statusMovePosition(Long id, Long userId) {
        MovePosition movePosition = movePositionInterfaceDAO.selectById(id);
        if (movePosition != null) {
            if (movePosition.getStatus().equals(DyylConstant.STATUS_OVER) || movePosition.getStatus().equals(DyylConstant.STATUS_NO)) {
                return false;
            } else {
                DepotPosition depotPosition = depotPositionInterfaceDAO.selectById(movePosition.getPositionBy());
                this.moveOver(movePosition.getId().toString(), movePosition.getRfid(), depotPosition.getPositionCode(), userId);
                movePosition.setStatus(DyylConstant.STATUS_OVER);
                movePosition.setCompleteTime(DateUtils.getTime());
                movePosition.setMoveUserId(userId);
            }
        } else {
            return false;
        }

        return updateById(movePosition);
    }

}
