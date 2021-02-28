package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.util.DeriveExcel;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.dao.MovePositionDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.MovePosition;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MovePositionService;
import com.tbl.modules.stock.service.StockChangeService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 移位管理Service实现
 *
 * @author yuany
 * @date 2019-01-21
 */
@Service("movePositionService")
public class MovePositionServiceImpl extends ServiceImpl<MovePositionDAO, MovePosition> implements MovePositionService {

    /**
     * 用户Dao
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * 库存Dao
     */
    @Autowired
    private StockService stockService;

    /**
     * 库位Dao
     */
    @Autowired
    private DepotPositionDAO depotPositionDAO;

    /**
     * 物料绑定Dao
     */
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;

    /**
     * 物料绑定Dao
     */
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    /**
     * 物料绑定详情Dao
     */
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDAO;

    /**
     * 库存变动Dao
     */
    @Autowired
    private StockChangeService stockChangeService;

    @Autowired
    private MovePositionService movePositionService;

    /**
     * 库存DAO
     */
    @Autowired
    private StockDAO stockDAO;

    /**
     * 移位管理Dao
     */
    @Autowired
    private MovePositionDAO movePositionDAO;

    /**
     * 物料DAO
     */
    @Autowired
    private MaterielDAO materielDAO;

    /**
     * 移位管理分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-21
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        //获取RFID
        String rfid = (String) parms.get("rfid");
        if (StringUtils.isNotBlank(rfid)) {
            rfid = rfid.trim();
        }

        //获取移库单号
        String movePositionCode = (String) parms.get("movePositionCode");
        if (StringUtils.isNotBlank(movePositionCode)) {
            movePositionCode = movePositionCode.trim();
        }

        //获取状态
        String status = (String) parms.get("status");
        //获取开始时间
        String movePositionTime = (String) parms.get("movePositionTime");

        String minMovePositionTime = null;
        String maxMovePositionTime = null;
        if (StringUtils.isNotBlank(movePositionTime)) {
            minMovePositionTime = movePositionTime + " 00:00:00";
            maxMovePositionTime = movePositionTime + " 23:59:59";
        }
        Page<MovePosition> movePositionPage = this.selectPage(
                new Query<MovePosition>(parms).getPage(),
                new EntityWrapper<MovePosition>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .ge(minMovePositionTime != null, "move_position_time", minMovePositionTime)
                        .le(maxMovePositionTime != null, "move_position_time", maxMovePositionTime)
                        .like(StringUtils.isNotBlank(rfid), "rfid", rfid)
                        .like(StringUtils.isNotBlank(movePositionCode), "move_position_code", movePositionCode)
                        .eq(StringUtils.isNotBlank(status), "status", status)
        );

        for (MovePosition movePosition : movePositionPage.getRecords()) {
            if (movePosition.getMoveUserId() != null & userDAO.selectById(movePosition.getMoveUserId()) != null) {
                movePosition.setMoveUserName(userDAO.selectById(movePosition.getMoveUserId()).getUsername());
            }

            if (movePosition.getPositionBy() != null && depotPositionDAO.selectById(movePosition.getPositionBy()) != null) {
                movePosition.setPositionName(depotPositionDAO.selectById(movePosition.getPositionBy()).getPositionName());
            }
            if (movePosition.getFormerPosition() != null && depotPositionDAO.selectById(movePosition.getFormerPosition()) != null) {
                movePosition.setFormerPositionName(depotPositionDAO.selectById(movePosition.getFormerPosition()).getPositionName());
            }
        }

        return new PageUtils(movePositionPage);
    }

    /**
     * 删除移位（逻辑删除）
     *
     * @param ids:要删除的id
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2018-01-22
     */
    @Override
    public boolean delMovePosition(String ids, Long userId) {

        boolean result = true;

        List<Long> idsList = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<MovePosition> movePositionList = this.selectBatchIds(idsList);

        //遍历物料绑定list
        for (MovePosition movePosition : movePositionList) {
            if (!movePosition.getStatus().equals(DyylConstant.STATUS_NO)) {
                result = false;
                break;
            }
        }
        if (result) {
            for (MovePosition movePosition : movePositionList) {
                movePosition.setDeleteBy(userId);
                movePosition.setDeleteFlag(DyylConstant.DELETED);
            }
            result = updateBatchById(movePositionList);
        }

        return result;
    }

    /**
     * 移库添加/修改
     *
     * @param movePosition
     * @return
     */
    @Override
    public Map<String, Object> addMovePosition(MovePosition movePosition, Long userId) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "保存成功";

        //当移位类型为整货移位时，则需判断RFID
        if (movePosition.getMovePositionType().equals(DyylConstant.MOVEPOSITIONTYPE_CARGO)) {
            //获取移库的物料绑定RFID信息
            MaterielBindRfid materielBindRfid = materielBindRfidDAO.materielBindRfid(movePosition.getRfid());
            //RFID是否存在绑定关系
            if (materielBindRfid != null) {
                //验证RFID验证
                map = upRfid(materielBindRfid.getRfid());
                result = (boolean) map.get("result");
                if (result) {
                    //获取此物料绑定关系中的库位信息
                    DepotPosition rfidPosition = depotPositionDAO.selectById(materielBindRfid.getPositionBy());
                    //获取移库单中移库目标库位的信息
                    DepotPosition depotPosition = depotPositionDAO.selectById(movePosition.getPositionBy());
                    if (rfidPosition.getPositionCode().equals(depotPosition.getPositionCode())) {
                        map.put("msg", "失败原因：移出库位与移入库位不能在同一库位");
                        map.put("result", false);
                        return map;
                    }
                    //获取此RFID 绑定的物料总重量
                    Double rfidWeight = null;
                    boolean count = true;
                    List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailDAO.selectList(
                            new EntityWrapper<MaterielBindRfidDetail>()
                                    .eq("delete_flag", DyylConstant.NOTDELETED)
                                    .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                    );
                    boolean quality = true;
                    if (!materielBindRfidDetailList.isEmpty()) {
                        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                            //判断物料是否通过质检
                            Integer num = getSelectAvailableRfid(materielBindRfidDetail.getMaterielCode(), materielBindRfidDetail.getBatchRule(), rfidPosition.getPositionCode(), movePosition.getRfid());
                            if (num < 0) {
                                quality = false;
                                break;
                            } else {
                                if (rfidWeight == null) {
                                    rfidWeight = Double.parseDouble(materielBindRfidDetail.getWeight());
                                } else {
                                    rfidWeight = rfidWeight + Double.valueOf(materielBindRfidDetail.getWeight());
                                }
                                if (!Strings.isNullOrEmpty(materielBindRfidDetail.getOutstorageBillCode())) {
                                    count = false;
                                }
                            }
                        }
                    } else {
                        rfidWeight = new Double(0);
                    }

                    //获取要移动库位的已用库存容量和托盘容量
                    List<Stock> stockList = stockDAO.selectList(
                            new EntityWrapper<Stock>()
                                    .eq("position_code", depotPosition.getPositionCode())
                    );
                    Double capacityWeight = null;
                    Double capacityRfidAmount = null;
                    if (!stockList.isEmpty()) {
                        for (Stock stock : stockList) {
                            if (capacityWeight == null) {
                                capacityWeight = Double.parseDouble(stock.getStockWeight());
                            } else {
                                capacityWeight = capacityWeight + Double.parseDouble(stock.getStockWeight());
                            }

                            if (capacityRfidAmount == null) {
                                capacityRfidAmount = Double.parseDouble(stock.getStockRfidAmount());
                            } else {
                                capacityRfidAmount = capacityRfidAmount + Double.valueOf(stock.getStockRfidAmount());
                            }
                        }
                    } else {
                        capacityWeight = new Double(0);
                        capacityRfidAmount = new Double(1);
                    }

                    //RFID是否通过质检
                    if (quality) {
                        movePositionService.isAvailablePosition(movePosition.getRfid(), depotPosition.getPositionCode());
                        //库位类型判断
                        if (rfidPosition.getPositionType().equals(depotPosition.getPositionType())) {
                            //判断物料详情是否存在占用用库存的出库单数据
                            if (count) {
                                //判断是否做过白糖绑定
                                if (isBundingSugar(movePosition.getRfid())) {
                                    map.put("msg", "失败原因：白糖类型物料，未做数量重量绑定");
                                    map.put("result", false);
                                    return map;
                                } else {
                                    movePosition.setMovePositionCode(materielBindRfid.getBindCode());
                                    movePosition.setFormerPosition(rfidPosition.getId());
                                    if (movePosition.getId() == null) {
                                        movePosition.setMoveUserId(userId);
                                        movePosition.setMoveFoundTime(DateUtils.getTime());
                                        movePosition.setStatus(DyylConstant.STATUS_NO);
                                        movePosition.setDeleteFlag(DyylConstant.NOTDELETED);
                                        Integer tally = movePositionDAO.insert(movePosition);
                                        if (tally > 0) {
                                            result = true;
                                        }
                                    } else {
                                        movePosition.setMoveFoundTime(DateUtils.getTime());
                                        Integer tally = movePositionDAO.updateById(movePosition);
                                        if (tally > 0) {
                                            result = true;
                                        }
                                    }
                                }
                            } else {
                                map.put("msg", "失败原因：此RFID绑定的物料中存在出库单占用");
                                map.put("result", false);
                                return map;
                            }
                        } else {
                            msg = "失败原因：库位类型不匹配";
                        }
                    } else {
                        msg = "失败原因：移位RFID未通过质检";
                    }
                } else {
                    return map;
                }
            } else {
                msg = "失败原因：移位RFID不存在绑定关系";
            }
        } else { //散货移位
            //获取移出库位id
            Long formerPosition = movePosition.getFormerPosition();
            //获取移出库位信息
            DepotPosition forPosition = depotPositionDAO.selectById(formerPosition);
            //获取移出库位相应的库存信息 依据库位编码，物料名称，批次号，物料类型确定一条库存信息
            EntityWrapper<Stock> wraStock = new EntityWrapper<>();
            wraStock.eq("position_code", forPosition.getPositionCode());
            wraStock.eq("material_code", movePosition.getMaterielCode());
            wraStock.eq("batch_no", movePosition.getBatchNo());
            wraStock.eq("material_type", DyylConstant.MOVEPOSITIONTYPE_NOCARGO);
            Stock stockEntity = stockService.selectOne(wraStock);
            if (stockEntity == null) {
                msg = "失败原因：库存中无此物料库存信息，请选择重新选择移出物料";
                map.put("msg", msg);
                map.put("result", result);
                return map;
            }
            //获取移入库位id
            Long positionBy = movePosition.getPositionBy();
            //获取散货移位数量
            String movePositionAmount = movePosition.getMovePositionAmount();
            //获取散货移位重量
            String movePositionWeight = movePosition.getMovePositionWeight();
            //获取移入库位信息
            DepotPosition depotPos = depotPositionDAO.selectById(positionBy);
            if (forPosition.getPositionFrozen().equals(DyylConstant.POSITION_FROZEN1)) { //判断移出库位是否被冻结
                msg = "失败原因：移出库位已冻结，不能进行移位";
            } else if (depotPos.getPositionFrozen().equals(DyylConstant.POSITION_FROZEN1)) { //判断移入库位是否被冻结
                msg = "失败原因：移入库位已冻结，不能进行移位";
            } else if (!depotPos.getPositionType().equals(forPosition.getPositionType())) { //判断移出库位与移入库位类型是否一致
                msg = "失败原因：移出库位类型与移入库位不一致";
            } else if (movePosition.getFormerPosition().equals(movePosition.getPositionBy())) { //判断移出库位与移入库位是否一致
                msg = "失败原因：移出库位与移入库位一致,请重新选择";
            } else {
                //库存可用数量
                Long availableStockAmount = Long.valueOf(stockEntity.getAvailableStockAmount());
                //移位数量
                Long moveAmount = Long.valueOf(movePositionAmount);
                //库存可用重量
                Long availableStockWeight = Long.valueOf(stockEntity.getAvailableStockWeight());
                //移位重量
                Long moveWeight = Long.valueOf(movePositionWeight);

                //判断移出库位可用数量是否大于移位数量
                if (moveAmount > availableStockAmount) {
                    msg = "失败原因：移位数量大于库存可用数量，无法进行移位";
                } else if (moveWeight > availableStockWeight) {
                    msg = "失败原因：移位重量大于库存可用重量，无法进行移位";
                } else {
                    movePosition.setMovePositionCode(this.getMovePositionCode());
                    movePosition.setFormerPosition(formerPosition);
                    movePosition.setMoveUserId(userId);
                    movePosition.setMoveFoundTime(DateUtils.getTime());
                    movePosition.setStatus(DyylConstant.STATUS_NO);
                    movePosition.setDeleteFlag(DyylConstant.NOTDELETED);
                    if (movePosition.getId() == null) {
                        result = movePositionDAO.insert(movePosition) > 0;
                    } else {
                        result = movePositionDAO.updateById(movePosition) > 0;
                    }
                }
            }
        }
        map.put("msg", msg);
        map.put("result", result);

        return map;
    }

    /**
     * 开始移位/移位完成
     *
     * @author yuany
     * @date 2019-01-22
     */
    @Override
    public boolean statusMovePosition(String ids, String time, Integer sign, Long userId) {
        //移位逻辑
        //开始移位：记录库存变动，并对相应库存更新，最后更改移位单状态
        //移位完成：记录库存变动，并对相应库存更新，若无对应的库存则新增，最后更新移位单状态
        boolean result = false;
        List<Long> idsList = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<MovePosition> movePositionList = this.selectBatchIds(idsList);
        for (MovePosition movePosition : movePositionList) {
            if (movePosition.getMovePositionType().equals(DyylConstant.MOVEPOSITIONTYPE_CARGO)) {
                MaterielBindRfid materielBindRfid = materielBindRfidDAO.materielBindRfid(movePosition.getRfid());
                if (materielBindRfid != null) {
                    //原库位信息
                    DepotPosition rfidPosition = depotPositionDAO.selectById(movePosition.getFormerPosition());
                    //移动目标库位信息
                    DepotPosition depotPosition = depotPositionDAO.selectById(movePosition.getPositionBy());
                    Stock stock = null;
                    //获取符合条件的物料绑定
                    List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailDAO.selectList(
                            new EntityWrapper<MaterielBindRfidDetail>()
                                    .eq("delete_flag", DyylConstant.NOTDELETED)
                                    .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                    );
                    if (sign == 0) {
                        if (movePosition.getStatus().equals(DyylConstant.STATUS_IN) || movePosition.getStatus().equals(DyylConstant.STATUS_OVER)) {
                            return false;
                        } else {
                            movePosition.setStatus(DyylConstant.STATUS_IN);
                            movePosition.setMovePositionTime(time);
                            movePosition.setMoveUserId(userId);
                            result = movePositionDAO.updateById(movePosition) > 0;
                        }
                    } else {
                        if (movePosition.getStatus().equals(DyylConstant.STATUS_OVER) || movePosition.getStatus().equals(DyylConstant.STATUS_NO)) {
                            return false;
                        } else {
                            //若托盘RFID不为空则记录库存变动，若为空则不记录，只更改移位状态
                            if (!materielBindRfidDetailList.isEmpty()) {
                                for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                                    materielBindRfidDetail.setPositionId(movePosition.getPositionBy());
                                    if (materielBindRfidDetailDAO.updateById(materielBindRfidDetail) > 0) {
                                        if (depotPosition != null) {
                                            //获取移位前的库存信息
                                            Stock preStock = stockService.selectOne(
                                                    new EntityWrapper<Stock>()
                                                            .eq("position_code", rfidPosition.getPositionCode())
                                                            .eq("material_code", materielBindRfidDetail.getMaterielCode())
                                                            .eq("batch_no", materielBindRfidDetail.getBatchRule())
                                                            .eq("material_type", DyylConstant.MOVEPOSITIONTYPE_CARGO)
                                            );
                                            if (preStock == null) {
                                                return false;
                                            }
                                            //获取移位后的库存信息
                                            Stock stk = stockService.selectOne(
                                                    new EntityWrapper<Stock>()
                                                            .eq("position_code", depotPosition.getPositionCode())
                                                            .eq("material_code", materielBindRfidDetail.getMaterielCode())
                                                            .eq("batch_no", materielBindRfidDetail.getBatchRule())
                                                            .eq("material_type", DyylConstant.MOVEPOSITIONTYPE_CARGO)
                                            );
                                            //三个条件可却定唯一一条库存信息
                                            if (stk != null) {
                                                stock = stk;
                                                Double stockRfidAmount = Double.valueOf(stock.getStockRfidAmount()) + 1;
                                                Double availableStockRfidAmount = Double.valueOf(stock.getAvailableStockRfidAmount()) + 1;
                                                Double stockAmount = Double.valueOf(stock.getStockAmount()) + Double.valueOf(materielBindRfidDetail.getAmount());
                                                Double aviAmount = Double.valueOf(stock.getAvailableStockAmount()) + Double.valueOf(materielBindRfidDetail.getAmount());
                                                Double stockWeight = Double.parseDouble(stock.getStockWeight()) + Double.parseDouble(materielBindRfidDetail.getWeight());
                                                Double aviWeight = Double.parseDouble(stock.getAvailableStockWeight()) + Double.parseDouble(materielBindRfidDetail.getWeight());
                                                String rfid = stock.getRfid() + "," + movePosition.getRfid();
                                                String aviRfid = stock.getAvailableRfid() + "," + movePosition.getRfid();
                                                stock.setStockAmount(stockAmount.toString());
                                                stock.setStockWeight(stockWeight.toString());
                                                stock.setAvailableStockWeight(aviWeight.toString());
                                                stock.setAvailableStockAmount(aviAmount.toString());
                                                stock.setRfid(rfid);
                                                stock.setAvailableRfid(aviRfid);
                                                stock.setStockRfidAmount(stockRfidAmount.toString());
                                                stock.setAvailableStockRfidAmount(availableStockRfidAmount.toString());
                                                if (stockDAO.updateById(stock) <= 0) {
                                                    break;
                                                } else {
                                                    result = true;
                                                }
                                            } else {
                                                stock = new Stock();
                                                stock.setMaterialCode(materielBindRfidDetail.getMaterielCode());
                                                stock.setMaterialName(materielBindRfidDetail.getMaterielName());
                                                stock.setBatchNo(materielBindRfidDetail.getBatchRule());
                                                stock.setPositionCode(depotPosition.getPositionCode());
                                                //依据库位编码查询库位名称
                                                String positionName = depotPositionDAO.selectPositionName(depotPosition.getPositionCode());
                                                stock.setPositionName(positionName);
                                                stock.setStockAmount(materielBindRfidDetail.getAmount());
                                                stock.setStockWeight(materielBindRfidDetail.getWeight());
                                                stock.setAvailableStockAmount(materielBindRfidDetail.getAmount());
                                                stock.setAvailableStockWeight(materielBindRfidDetail.getWeight());
                                                stock.setStockRfidAmount("1");
                                                stock.setAvailableStockRfidAmount("1");
                                                stock.setRfid(movePosition.getRfid());
                                                stock.setAvailableRfid(movePosition.getRfid());
                                                stock.setCreateTime(time);
                                                stock.setCreateBy(userId);
                                                stock.setProductDate(preStock.getProductDate());
                                                stock.setQualityDate(preStock.getQualityDate());
                                                stock.setCustomerCode(preStock.getCustomerCode());
                                                if (stockDAO.insert(stock) <= 0) {
                                                    break;
                                                } else {
                                                    result = true;

                                                }
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                    if (result) {
                                        stockChangeService.saveStockChange(materielBindRfid.getBindCode(), materielBindRfidDetail.getMaterielCode(), materielBindRfidDetail.
                                                getMaterielName(), materielBindRfidDetail.getBatchRule(), DyylConstant.MOVE_OUT, materielBindRfidDetail.getAmount(), "", "", materielBindRfid.getPositionBy(), userId);
                                        stockChangeService.saveStockChange(materielBindRfid.getBindCode(), materielBindRfidDetail.getMaterielCode(), materielBindRfidDetail.
                                                getMaterielName(), materielBindRfidDetail.getBatchRule(), DyylConstant.MOVE_IN, "", "", materielBindRfidDetail.getAmount(), materielBindRfid.getPositionBy(), userId);
                                    }
                                }
                            }

                            this.moveOver(movePosition.getId().toString(), movePosition.getRfid(), depotPosition.getPositionCode(), userId);
                            movePosition.setStatus(DyylConstant.STATUS_OVER);
                            movePosition.setCompleteTime(time);
                            movePosition.setMoveUserId(userId);
                            movePositionDAO.updateById(movePosition);
                        }
                    }
                } else {
                    return false;
                }
            } else {        //移位类型为散货移位时
                //sign为0时，则表示开始移位
                if (sign == 0) {
                    //开始移位  改变状态
                    movePosition.setStatus(DyylConstant.STATUS_IN);
                    movePosition.setMovePositionTime(DateUtils.getTime());
                    result = movePositionDAO.updateById(movePosition) > 0;
                } else {
                    //获取移位数量
                    Double movePositionAmount = Double.valueOf(movePosition.getMovePositionAmount());
                    //获取移位重量
                    Double movePositionWeight = Double.valueOf(movePosition.getMovePositionWeight());
                    //获取物料编码
                    String materielCode = movePosition.getMaterielCode();
                    //获取批次号
                    String batchNo = movePosition.getBatchNo();
                    //获取库位编码
                    Long positionId = movePosition.getFormerPosition();
                    String positionCode = depotPositionDAO.selectById(positionId).getPositionCode();
                    //依据物料编码 库位编码 批次号 物料类型确定一条库存信息
                    Stock stock = stockService.selectOne(
                            new EntityWrapper<Stock>()
                                    .eq("position_code", positionCode)
                                    .eq("material_code", materielCode)
                                    .eq("batch_no", batchNo)
                                    .eq("material_type", DyylConstant.MATERIAL_NORFID)
                    );
                    Stock s = stockDAO.selectById(stock.getId());
                    //获取库存可用数量
                    Double availableStockAmount = Double.valueOf(stock.getAvailableStockAmount());
                    //获取库存可用重量
                    Double availableStockWeight = Double.valueOf(stock.getAvailableStockWeight());
                    DepotPosition dp = depotPositionDAO.selectById(movePosition.getPositionBy());

                    //如果移位数量与库存数量完全相同,则直接修改库存中的库位
                    if (movePosition.getMovePositionAmount().equals(stock.getAvailableStockAmount()) && movePosition.getMovePositionWeight().equals(stock.getAvailableStockWeight())) {
                        stock.setPositionCode(dp.getPositionCode());
                        stock.setPositionName(dp.getPositionName());
                        stockDAO.updateById(stock);
                        stockChangeService.saveStockChange(movePosition.getMovePositionCode(), movePosition.getMaterielCode(), movePosition.getMaterielName(), movePosition.getBatchNo(), DyylConstant.MOVE_OUT, stock.getAvailableStockAmount(), "", "0", movePosition.getFormerPosition(), userId);
                        stockChangeService.saveStockChange(movePosition.getMovePositionCode(), movePosition.getMaterielCode(), movePosition.getMaterielName(), movePosition.getBatchNo(), DyylConstant.MOVE_IN, "", stock.getAvailableStockAmount(), stock.getAvailableStockAmount(), movePosition.getFormerPosition(), userId);
                        //移位完成
                        movePosition.setStatus(DyylConstant.STATUS_OVER);
                        movePosition.setCompleteTime(DateUtils.getTime());
                        result = movePositionDAO.updateById(movePosition) > 0;
                    } else if (availableStockAmount > movePositionAmount) {  //如果移位数量小于库存数量,则在库存中新建移位后的库存信息，并在原来库存中减少数量
                        stock.setPositionCode(dp.getPositionCode());
                        stock.setPositionName(dp.getPositionName());
                        stock.setStockAmount(movePosition.getMovePositionAmount());
                        stock.setAvailableStockAmount(movePosition.getMovePositionAmount());
                        stock.setStockWeight(movePosition.getMovePositionWeight());
                        stock.setAvailableStockWeight(movePosition.getMovePositionWeight());
                        stockDAO.insert(stock);
                        //库存剩余数量
                        Double overAmount = Double.valueOf(s.getStockAmount()) - movePositionAmount;
                        //库存剩余可用数量
                        Double overAvailableAmount = availableStockAmount - movePositionAmount;
                        //库存剩余重量
                        Double overWeight = Double.valueOf(s.getStockAmount()) - movePositionAmount;
                        //库存剩余可用重量
                        Double overAvailableWeight = availableStockWeight - movePositionWeight;
                        s.setAvailableStockAmount(String.valueOf(overAvailableAmount));
                        s.setStockAmount(String.valueOf(overAmount));
                        s.setAvailableStockWeight(String.valueOf(overAvailableWeight));
                        s.setStockWeight(String.valueOf(overWeight));
                        stockDAO.updateById(s);
                        stockChangeService.saveStockChange(movePosition.getMovePositionCode(), movePosition.getMaterielCode(), movePosition.getMaterielName(), movePosition.getBatchNo(), DyylConstant.MOVE_OUT, movePosition.getMovePositionAmount(), "", String.valueOf(overAmount), movePosition.getFormerPosition(), userId);
                        stockChangeService.saveStockChange(movePosition.getMovePositionCode(), movePosition.getMaterielCode(), movePosition.getMaterielName(), movePosition.getBatchNo(), DyylConstant.MOVE_IN, "", movePosition.getMovePositionAmount(), String.valueOf(overAmount), movePosition.getFormerPosition(), userId);
                        //移位完成
                        movePosition.setStatus(DyylConstant.STATUS_OVER);
                        movePosition.setCompleteTime(DateUtils.getTime());
                        result = movePositionDAO.updateById(movePosition) > 0;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取导出列
     *
     * @return List<MovePosition>
     * @author yuany
     * @date 2019-01-23
     */
    @Override
    public List<MovePosition> getAllLists(String ids) {
        List<MovePosition> list = movePositionDAO.getAllLists(StringUtils.stringToList(ids));
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
     * @author yuany
     * @date 2018-09-23
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<MovePosition> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "移位管理表" + "(" + date + ")";
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
            mapFields.put("movePositionCode", "移位单号");
            mapFields.put("rfid", "RFID");
            mapFields.put("formerPositionName", "原库位");
            mapFields.put("positionName", "移动库位");
            mapFields.put("moveFoundTime", "创建时间");
            mapFields.put("movePositionTime", "开始时间");
            mapFields.put("completeTime", "完成时间");
            mapFields.put("moveUserName", "移位人员");
            mapFields.put("statusName", "状态");
            mapFields.put("remarks", "备注");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBundingSugar(String rfid) {
        boolean result = false;
        //（1）如果是白糖类型的rfid，判断是否做过绑定，如果没做过绑定则不给移库
        //判断没做过绑定的白糖类型的rfid：该rfid只绑定了一种物料，并且如果该绑定的rfid是“已入库”状态，并且数量和重量都为0或空
        List<Map<String, Object>> materialList = movePositionDAO.getMaterialBundingDetail(rfid);
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
     * @Description: 判断库位是否可用，即验证所选的库位是否可以放该托盘rfid
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/3/7
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
        List<DepotPosition> depotPositionList = depotPositionDAO.selectList(depotPositionEntity);
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
        List<Map<String, Object>> materialBundingList = movePositionDAO.getMaterialBundingDetail(rfid);
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
                        Map<String, Object> stockMap = movePositionDAO.getStockInfo(positionCode);
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
                            } else {
                                result = false;
                                msg = "该库位不可混放，请确认上架的物料与库位上的物料是否一致";
                            }
                        } else { //如果没有物料，表示空库位
                            //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                            if (capacityWeight >= rfidWeight && capacityRfidAmount >= 1) {
                                result = true;
                                msg = "该库位可以上架";
                            } else {
                                result = false;
                                msg = "该库位容量不够，请确认";
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
        } else { //如果是混放类型的库位，
            if (materialBundingList != null && materialBundingList.size() > 0) {
                //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
                Map<String, Object> stockMap = movePositionDAO.getStockInfo(positionCode);
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
        return movePositionDAO.getSelectAvailableRfid(materialCode1, batchNo1, formerpositionCode, rfid);
    }

    @Override
    public Map<String, Object> upRfid(String rfid) {

        Map<String, Object> map = new HashMap<>();
        boolean result = true;
        String msg = "";

        //原库位编号
        String formerpositionCode = "";
        List<MaterielBindRfid> materielBindRfidList = materielBindRfidDAO.selectList(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
                        .eq("rfid", rfid)
        );
        MaterielBindRfid mbr = null;
        if (materielBindRfidList.size() == 1) {
            for (MaterielBindRfid materielBindRfid : materielBindRfidList) {
                mbr = materielBindRfid;
            }
            DepotPosition depotPosition = depotPositionDAO.selectById(mbr.getPositionBy());
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
                        msg = "该托盘绑定的物料是未做过绑定的白糖类型的物料，不能移库，请确认";
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
                            msg = "该托盘中有物料已被占用，不能移位，请确认";
                        } else {
                            /**验证该rfid托盘是否可用**/
                            //查询该rfid是否可用
                            Integer count = getSelectAvailableRfid(materialCode1, batchNo1, formerpositionCode, rfid);
                            if (count < 0) {
                                result = false;
                                msg = "该托盘未通过质检，不可用，请确认";
                            }
                        }
                    }
                } else {
                    result = false;
                    msg = "该托盘没绑定物料，请确认！";
                }
            } else {
                result = false;
                msg = "该托盘无绑定库位信息，请确认！";
            }
        } else {
            result = false;
            msg = "该托盘无绑定关系，请确认！";
        }

        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    @Override
    public void moveOver(String movePositionId, String rfid, String positionCode, Long userId) {

        /**更新移库表的库位编码和其他字段**/
        //库位id
        Long positionId = 0l;

        EntityWrapper<DepotPosition> depotPositionEntity = new EntityWrapper<>();
        depotPositionEntity.eq("position_code", positionCode);
        List<DepotPosition> lstDepotPosition = depotPositionDAO.selectList(depotPositionEntity);
        if (lstDepotPosition != null && lstDepotPosition.size() > 0) {
            //数据是唯一的，取第一条
            DepotPosition depotPosition = lstDepotPosition.get(0);
            positionId = depotPosition.getId();
        }

        /**3.更新物料绑定rfid表库位id和详情表库位id**/
        //根据移库表的id查询原库位主键id和库位编号
        Map<String, Object> formerPositionIdMap = movePositionDAO.selectFormerPosition(movePositionId);
        //原库位编码
        String formerPositionCode = "";
        if (formerPositionIdMap != null) {
            formerPositionCode = formerPositionIdMap.get("position_code") == null ? "" : formerPositionIdMap.get("position_code").toString();
        }

        //绑定表id
        String materielBindId = "";
        //绑定详情表id集合
        List<String> lstMaterielBindDetailId = new ArrayList<>();
        List<Map<String, Object>> lstMaterialBunding = movePositionDAO.selectMaterialBundingByRfid(rfid);
        if (lstMaterialBunding != null && lstMaterialBunding.size() > 0) {
            for (Map<String, Object> materialBundingMap : lstMaterialBunding) {
                materielBindId = materialBundingMap.get("materielBindId") == null ? "" : materialBundingMap.get("materielBindId").toString();
                String materielBindDetailId = materialBundingMap.get("materielBindDetailId") == null ? "" : materialBundingMap.get("materielBindDetailId").toString();
                lstMaterielBindDetailId.add(materielBindDetailId);

                /**4.插入库存变动表(“移位-入库”类型的)**/
                //物料编号
                String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
                //批次号
                String batchNo = materialBundingMap.get("batch_rule") == null ? "" : materialBundingMap.get("batch_rule").toString();
                //数量
                String amount = materialBundingMap.get("amount") == null ? "" : materialBundingMap.get("amount").toString();
                //重量
                String weight = materialBundingMap.get("weight") == null ? "" : materialBundingMap.get("weight").toString();

                /**5.更新库存表**/
                /**（1）减去原库位的库存**/
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("materialCode", materialCode);
                paramMap.put("batchNo", batchNo);
                paramMap.put("formerPositionCode", formerPositionCode);
                paramMap.put("amount", amount);
                paramMap.put("weight", weight);
                paramMap.put("rfid", rfid);
                movePositionDAO.updateStockFormerPosition(paramMap);
                /**判断库存数量是否为空   为空则删除此条库存记录**/
                EntityWrapper<Stock> stockEntity = new EntityWrapper<>();
                stockEntity.eq("material_code", materialCode);
                stockEntity.eq("position_code", formerPositionCode);
                stockEntity.eq("batch_no", batchNo);
                stockEntity.eq("rfid", rfid);
                Stock stock = stockService.selectOne(stockEntity);
                if (stock != null && stock.getStockAmount().equals("0")) {
                    stockService.deleteById(stock);
                }
            }
        }
        //更新绑定表库位id
        movePositionDAO.updateMaterialBundingPosition(materielBindId, positionId);
        //更新绑定详情表库位id
        movePositionDAO.updateMaterialBundingDetailPosition(lstMaterielBindDetailId, positionId);
    }


    /**
     * 库存详细分页查询
     *
     * @param params
     * @return
     * @author pz
     * @date 2018-01-10
     */
    public PageUtils queryPageS(Map<String, Object> params, Long movePositionId) {
        if (movePositionId != null) {
            MovePosition movePosition = movePositionDAO.selectById(movePositionId);
            if (movePosition == null) {
                return new PageUtils(null);
            }

            //整货详情
            if (movePosition.getMovePositionType().equals(DyylConstant.MOVEPOSITIONTYPE_CARGO)) {
                //库存物料RFID
                String rfid = movePosition.getRfid();
                //库位
                Long positionBy = movePosition.getPositionBy();
                Page<MaterielBindRfidDetail> page = null;
                if (!Strings.isNullOrEmpty(rfid)) {
                    page = materielBindRfidDetailService.selectPage(
                            new Query<MaterielBindRfidDetail>(params).getPage(),
                            new EntityWrapper<MaterielBindRfidDetail>()
                                    .eq("delete_flag", DyylConstant.NOTDELETED)
                                    .eq("position_id", positionBy)
                                    .in("rfid", rfid)
                    );
                }
                return new PageUtils(page);
            } else { //散货详情
                //获取库位编码
                Long positionId = movePosition.getPositionBy();
                String positionCode = depotPositionDAO.selectById(positionId).getPositionCode();
                Page<Stock> stockPage = stockService.selectPage(
                        new Query<Stock>(params).getPage(),
                        new EntityWrapper<>()
                );
                return new PageUtils(stockPage.setRecords(movePositionDAO.selectByListS(stockPage, movePosition.getMaterielCode(), positionCode, movePosition.getBatchNo())));
            }
        }

        return new PageUtils(null);

    }

    /**
     * 自动生成移位编码(针对无RFID的移位)
     *
     * @author
     * @date 2019-02-01
     */
    @Override
    public String getMovePositionCode() {
        //预警编号
        String movePositionCode = null;
        //预警集合
        List<MovePosition> movePositionList = this.selectList(
                new EntityWrapper<MovePosition>().eq("move_position_type", DyylConstant.MOVEPOSITIONTYPE_NOCARGO)
        );
        //如果集合为长度为0则为第一条添加的数据
        if (movePositionList.size() == 0) {
            movePositionCode = "MP0000001";
        } else {
            //获取集合中最后一条数据
            MovePosition movePosition = movePositionList.get(movePositionList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Integer number = Integer.valueOf(movePosition.getMovePositionCode().substring(2)) + 1;
            //拼接字符串
            movePositionCode = "MP000000" + number.toString();
        }
        return movePositionCode;
    }
}
