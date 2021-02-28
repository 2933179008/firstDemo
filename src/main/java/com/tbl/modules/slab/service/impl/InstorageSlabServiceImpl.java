package com.tbl.modules.slab.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.IPUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.common.utils.Tools;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.*;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.instorage.service.PutBillDetailService;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.dao.InstorageSlabDAO;
import com.tbl.modules.slab.dao.MovePositionSlabDAO;
import com.tbl.modules.slab.service.InstorageSlabService;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.visualization.entity.StockCar;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 平板入库service实现类
 * @author: zj
 * @create: 2019-02-28 14:41
 **/
@Service("instorageSlabService")
public class InstorageSlabServiceImpl implements InstorageSlabService {
    @Autowired
    private InstorageSlabDAO instorageSlabDAO;
    @Autowired
    private PutBillDAO putBillDAO;
    @Autowired
    private PutBillDetailDAO putBillDetailDAO;
    @Autowired
    private PutBillDetailService putBillDetailService;
    @Autowired
    private StockDAO stockDAO;
    @Autowired
    private QualityBillDAO qualityBillDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    @Autowired
    private MaterielDAO materialDAO;
    @Autowired
    private DepotPositionDAO depotPositionDAO;
    @Autowired
    private DepotPositionService depotPositionService;
    @Autowired
    private DepotAreaDAO depotAreaDAO;
    @Autowired
    private MovePositionSlabDAO movePositionSlabDAO;
    @Autowired
    private MaterielDAO materielDAO;
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;
    @Autowired
    private InstorageDAO instorageDAO;
    @Autowired
    private MaterielBindRfidService materielBindRfidService;
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDAO;
    @Autowired
    private StockChangeDAO stockChangeDAO;


    @Value("${stockCar.Time}")
    private int CarTime;

    @Override
    public void insertOrUpdateSlabBillBunding(HttpServletRequest request, Long putBillId) {
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        //1.根据ip查询叉车正在操作单据的类型表中是否存在数据
        Integer count1 = instorageSlabDAO.selectOperateTypeCount(userIP);
        Map<String, Object> paramMap1 = new HashMap<String, Object>();
        paramMap1.put("userIP", userIP);
        if (count1 == 0) {//插入
            instorageSlabDAO.insertSlabOperateType(paramMap1);
        } else {//更新
            instorageSlabDAO.updateSlabOperateType(paramMap1);
        }

        //2.根据ip查询平板绑定关系表中是否存在数据
        Integer count2 = instorageSlabDAO.selectCountByUserIDAndIp(userIP);
        Map<String, Object> paramMap2 = new HashMap<String, Object>();
        paramMap2.put("userId", userId);
        paramMap2.put("userIP", userIP);
        paramMap2.put("putBillId", putBillId);
        if (count2 == 0) {//插入
            instorageSlabDAO.insertSlabBillBunding(paramMap2);
        } else {//更新
            instorageSlabDAO.updateSlabBillBunding(paramMap2);
        }
    }

    @Override
    public Page<PutBill> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if ("asc".equals(String.valueOf(params.get("order")))) {
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<PutBill> pagePutBill = new Page<PutBill>(pg, rows, sortname, order);

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        params.put("userId", userId);
        //获取上架单列表
        List<PutBill> list = instorageSlabDAO.getPagePutBillList(pagePutBill, params);

        return pagePutBill.setRecords(list);
    }

    @Override
    public void updatePutBillState(Long putBillId, Long userId, String userName) {
        instorageSlabDAO.updatePutBillState(putBillId, userId, userName);
    }

    @Override
    public Map<String, Object> getSlabBillBunding(String userIP) {
        return instorageSlabDAO.getSlabBillBunding(userIP);
    }

    @Override
    public List<Map<String, Object>> getMaterialBundingDetail(String rfid) {
        return instorageSlabDAO.getMaterialBundingDetail(rfid);
    }

    @Override
    public Map<String, Object> getPutBillDetailMap(String putBillId, String materialCode) {
        return instorageSlabDAO.getPutBillDetailMap(putBillId, materialCode);
    }

    @Override
    public void updateSlabBillBundingRfid(Map<String, Object> paramMap) {
        instorageSlabDAO.updateSlabBillBundingRfid(paramMap);
    }

    @Override
    public List<Map<String, Object>> getSelectPositionList(String areaId, Double xSize, Double x_Size, Double ySize, Double y_Size, String queryString) {
//        Page page = new Page(pageNo, pageSize);
        return instorageSlabDAO.getSelectPositionList(queryString, areaId, xSize, ySize, x_Size, y_Size);
    }

    @Override
    public Integer getSelectPositionTotal(String areaId, Double xSize, Double x_Size, Double ySize, Double y_Size, String queryString) {
        return instorageSlabDAO.getSelectPositionTotal(queryString, areaId, xSize, ySize, x_Size, y_Size);
    }

    @Override
    public void updateSlabBillBundingPositionCode(String userIP, String positionCode) {
        instorageSlabDAO.updateSlabBillBundingPositionCode(userIP, positionCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void slabDownRfid(String userIP) {
        Map<String, Object> map = instorageSlabDAO.getSlabBillBunding(userIP);
        //当前登陆人id
        Long userId = 0l;
        //当前登陆人正在执行的上架单id
        String putBillId = "0";
        //当前登陆人正在上架的托盘rfid
        String rfidStr = "";
        //当前登陆人正在上架的库位编码
        String positionCode = "";
        //平板页面交互用的参数字段key值
        String alertKey = "";

        if (map != null) {
            userId = map.get("user_id") == null ? 0l : Long.parseLong(map.get("user_id").toString());
            putBillId = map.get("put_bill_id") == null ? "0" : map.get("put_bill_id").toString();
            rfidStr = map.get("rfid") == null ? "" : map.get("rfid").toString();
            positionCode = map.get("position_code") == null ? "" : map.get("position_code").toString();
            alertKey = map.get("alert_key") == null ? "" : map.get("alert_key").toString();
        }
        //如果是没有确定库位的rfid，则不走逻辑
        if (!"3".equals(alertKey)) {
            return;
        }


        if (StringUtils.isNotBlank(rfidStr)) {
            String[] rfidArr = rfidStr.split(",");
            for (int i = 0; i < rfidArr.length; i++) {
                String rfid = rfidArr[i];

                //查询上架单对应的入库单的入库流程（0：一般流程；1：白糖流程）
                String instorageProcess = instorageSlabDAO.getInstorageProcess(putBillId);
                if ("1".equals(instorageProcess)) {
                    //根据上架单id获取上架详情
                    List<PutBillDetail> lstPutBillDetail = putBillDAO.getPutBillDetail(Long.parseLong(putBillId));
                    if (lstPutBillDetail != null && lstPutBillDetail.size() > 0) {
                        for (PutBillDetail putBillDetail : lstPutBillDetail) {
                            String materialCode = putBillDetail.getMaterialCode();
                            String materialName = putBillDetail.getMaterialName();
                            String unit = putBillDetail.getUnit();

                            //查询rfid是否已存在，如果存在则更新，否则插入
                            Long materielBindRfidId = instorageSlabDAO.getExistRfidCount(rfid);
                            if (materielBindRfidId != null) {//更新
                                //更新绑定主表
                                MaterielBindRfid materielBindRfid = new MaterielBindRfid();
                                materielBindRfid.setId(materielBindRfidId);
                                materielBindRfid.setUpdateBy(userId);
                                materielBindRfid.setDeletedFlag("1");
                                materielBindRfid.setStatus("1");
                                materielBindRfid.setInstorageProcess("1");
                                materielBindRfidDAO.updateById(materielBindRfid);
                                //先删除绑定从表，再插入
                                EntityWrapper<MaterielBindRfidDetail> entity = new EntityWrapper<>();
                                entity.eq("materiel_bind_rfid_by", materielBindRfidId);
                                materielBindRfidDetailDAO.delete(entity);
                                MaterielBindRfidDetail materielBindRfidDetail = new MaterielBindRfidDetail();
                                materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
                                materielBindRfidDetail.setMaterielCode(materialCode);
                                materielBindRfidDetail.setMaterielName(materialName);
                                materielBindRfidDetail.setAmount("0");
                                materielBindRfidDetail.setWeight("0");
                                materielBindRfidDetail.setUnit(unit);
                                materielBindRfidDetail.setRfid(rfid);
                                materielBindRfidDetail.setStatus("1");
                                materielBindRfidDetailDAO.insert(materielBindRfidDetail);

                            } else {//插入
                                //生成绑定编号
                                String bindCode = materielBindRfidService.getBindCode();
                                //插入绑定主表
                                MaterielBindRfid materielBindRfid = new MaterielBindRfid();
                                materielBindRfid.setBindCode(bindCode);
                                materielBindRfid.setRfid(rfid);
                                materielBindRfid.setCreateTime(DateUtils.getTime());
                                materielBindRfid.setCreateBy(userId);
                                materielBindRfid.setUpdateTime(DateUtils.getTime());
                                materielBindRfid.setUpdateBy(userId);
                                materielBindRfid.setDeletedFlag("1");
                                materielBindRfid.setStatus("1");
                                materielBindRfid.setInstorageProcess("1");
                                materielBindRfidDAO.insert(materielBindRfid);
                                //插入绑定从表
                                MaterielBindRfidDetail materielBindRfidDetail = new MaterielBindRfidDetail();
                                materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfid.getId());
                                materielBindRfidDetail.setMaterielCode(materialCode);
                                materielBindRfidDetail.setMaterielName(materialName);
                                materielBindRfidDetail.setAmount("0");
                                materielBindRfidDetail.setWeight("0");
                                materielBindRfidDetail.setUnit(unit);
                                materielBindRfidDetail.setRfid(rfid);
                                materielBindRfidDetail.setStatus("1");
                                materielBindRfidDetailDAO.insert(materielBindRfidDetail);
                            }

                        }
                    }
                }

                /**1.插入或更新上架单详情**/
                //根据rfid查询绑定的物料（如果一个rfid绑定了多种物料，不需要再验证物料是否与单据匹配）
                List<Map<String, Object>> materialList = instorageSlabDAO.getMaterialBundingDetail(rfid);
                if (materialList != null && materialList.size() > 0) {
                    for (Map<String, Object> materialMap : materialList) {
                        //物料编码
                        String materialCode = materialMap.get("materiel_code") == null ? "" : materialMap.get("materiel_code").toString();
                        //物料名称
                        String materialName = materialMap.get("materiel_name") == null ? "" : materialMap.get("materiel_name").toString();
                        //数量
                        String amount = materialMap.get("amount") == null ? "" : materialMap.get("amount").toString();
                        //重量
                        String weight = materialMap.get("weight") == null ? "" : materialMap.get("weight").toString();
                        EntityWrapper<PutBillDetail> putBillDetailEntity = new EntityWrapper<PutBillDetail>();
                        putBillDetailEntity.eq("put_bill_id", putBillId);
                        putBillDetailEntity.eq("material_code", materialCode);
                        putBillDetailEntity.eq("state", "0");

                        //根据上架单id和物料编号获取上架单对应的入库详情单
                        Map<String, Object> instorageBillDetailMap = instorageSlabDAO.getPutBillDetailMap(putBillId, materialCode);
                        //入库单id
                        Long instorageBillId = 0l;
                        //批次号
                        String batchNo = "";
                        //生产日期
                        String productDate = "";
                        //包装单位
                        String unit = "";
                        if (instorageBillDetailMap != null) {
                            batchNo = instorageBillDetailMap.get("batch_no") == null ? "" : instorageBillDetailMap.get("batch_no").toString();
                            unit = instorageBillDetailMap.get("unit") == null ? "" : instorageBillDetailMap.get("unit").toString();
                            instorageBillId = instorageBillDetailMap.get("instorage_bill_id") == null ? 0l : Long.parseLong(instorageBillDetailMap.get("instorage_bill_id").toString());
                            productDate = instorageBillDetailMap.get("product_date") == null ? "" : instorageBillDetailMap.get("product_date").toString();
                        }
                        Integer count = putBillDetailDAO.selectCount(putBillDetailEntity);
                        PutBillDetail putBillDetail = new PutBillDetail();
                        if (count > 0) {//如果上架单详情中有该种物料，则更新
                            putBillDetail.setPutAmount(amount);
                            putBillDetail.setPutWeight(weight);
                            putBillDetail.setPositionCode(positionCode);
                            putBillDetail.setRfid(rfid);
                            putBillDetail.setState("1");
                            putBillDetailDAO.update(putBillDetail, putBillDetailEntity);
                        } else {//否则插入
                            putBillDetail.setPutBillId(Long.parseLong(putBillId));
                            putBillDetail.setMaterialCode(materialCode);
                            putBillDetail.setMaterialName(materialName);
                            putBillDetail.setBatchNo(batchNo);
                            putBillDetail.setProductDate(productDate);
                            putBillDetail.setUnit(unit);
                            putBillDetail.setRfid(rfid);
                            putBillDetail.setPutAmount(amount);
                            putBillDetail.setPutWeight(weight);
                            putBillDetail.setPositionCode(positionCode);
                            putBillDetail.setState("1");
                            putBillDetailDAO.insert(putBillDetail);
                        }

                        PutBillDetail putBillDetail2 = new PutBillDetail();
                        putBillDetail2.setPutBillId(Long.parseLong(putBillId));
                        putBillDetail2.setMaterialCode(materialCode);
                        putBillDetail2.setRfid(rfid);
                        putBillDetail2.setState("1");
                        putBillDetail2 = putBillDetailDAO.selectOne(putBillDetail2);
                        //上架单详情id
                        String putBillDetailId = putBillDetail2.getId().toString();


                        Instorage instorage = instorageDAO.selectById(instorageBillId);
                        //货主编号
                        String customerCode = instorage.getCustomerCode();

                        /**2.往库存表插入/更新数据**/
                        //库位名称
                        String positionName = "";
                        //库位主键id
                        Long positionId = 0l;
                        //根据库位编号查询库位名称
                        EntityWrapper<DepotPosition> positionEntity = new EntityWrapper<DepotPosition>();
                        positionEntity.eq("position_code", positionCode);
                        List<DepotPosition> lstDepotPosition = depotPositionDAO.selectList(positionEntity);
                        if (lstDepotPosition != null && lstDepotPosition.size() > 0) {
                            //库位编号是唯一的，所以取第一条数据
                            DepotPosition depotPosition = lstDepotPosition.get(0);
                            positionName = depotPosition.getPositionName() == null ? "" : depotPosition.getPositionName();
                            positionId = depotPosition.getId() == null ? 0l : depotPosition.getId();
                        }
                        //上架数量
                        String putAmount = putBillDetail.getPutAmount();
                        //上架重量
                        String putWeight = putBillDetail.getPutWeight();
                        //托盘库存数量（一次上架一个托盘，所以数量为1）
                        String stockRfidAmount = "1";
                        //当前时间
                        String nowDate = DateUtils.getTime();

                        //根据物料编号、批次号、库位编号查询库存表
                        EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
                        stockEntity.eq("material_code", materialCode);
                        stockEntity.eq("batch_no", batchNo);
                        stockEntity.eq("position_code", positionCode);
                        stockEntity.eq("material_type", "1");
                        List<Stock> lstStock = stockDAO.selectList(stockEntity);

                        if (lstStock != null && lstStock.size() > 0) {//如果存在则更新
                            //获取数据是唯一的，所以取第一条数据
                            Stock stock = lstStock.get(0);
                            Long id = stock.getId();
                            //更新库存表相关信息
                            stockDAO.updateStockById(id, putAmount, putWeight, rfid);

                        } else {//如果不存在则插入
                            Stock stock = new Stock();
                            stock.setMaterialCode(materialCode);
                            stock.setMaterialName(materialName);
                            stock.setMaterialType("1");
                            stock.setBatchNo(batchNo);
                            stock.setProductDate(productDate);
                            stock.setPositionCode(positionCode);
                            stock.setPositionName(positionName);
                            stock.setStockAmount(putAmount);
                            stock.setStockWeight(putWeight);
                            stock.setStockRfidAmount(stockRfidAmount);
                            stock.setRfid(rfid);
                            stock.setCreateTime(nowDate);
                            stock.setCreateBy(userId);
                            stock.setCustomerCode(customerCode);
                            stockDAO.insert(stock);
                        }

                        /**如果该上架单对应的入库单，入库单对应的质检单是“质检通过”的单据，则更新库存表的可用库存数量、可用库存重量和可用托盘库存数量**/
                        //定义质检单状态（0：未提交；1：质检通过；2：质检退回；）
                        EntityWrapper<QualityBill> qualityBillEntity = new EntityWrapper<QualityBill>();
                        qualityBillEntity.eq("instorage_bill_id", instorageBillId);
                        qualityBillEntity.eq("state", "1");
                        List<QualityBill> lstQualityBill = qualityBillDAO.selectList(qualityBillEntity);
                        if (lstQualityBill != null && lstQualityBill.size() > 0) {
                            //一条入库单只会对应一条“质检通过”的质检单（可以对应多条“质检退回”的质检单）
                            Map<String, Object> paramsMap = new HashMap();
                            paramsMap.put("materialCode", materialCode);
                            paramsMap.put("batchNo", batchNo);
                            paramsMap.put("positionCode", positionCode);
                            paramsMap.put("putAmount", putAmount);
                            paramsMap.put("putWeight", putWeight);
                            paramsMap.put("rfid", rfid);
                            //更新库存表的可用库存数量、可用库存重量、可用托盘库存数量、可用的RFID（多个rfid用逗号隔开）
                            putBillDetailDAO.updateStockAvailableField(paramsMap);
                        }

                        /**3.更新入库单的可拆分数量和可拆分重量**/
                        Map<String, Object> paramsMap = new HashMap();
                        paramsMap.put("instorageBillId", instorageBillId);
                        paramsMap.put("materialCode", materialCode);
                        paramsMap.put("batchNo", batchNo);
                        paramsMap.put("putAmount", putAmount);
                        paramsMap.put("putWeight", putWeight);
                        //根据入库单id、物料编号和批次号更新入库单的可拆分数量和可拆分重量
                        putBillDetailDAO.updateSeparableAmountAndWeight(paramsMap);

                        /**4.更新上架单详情状态和上架单状态**/
                        //（1）根据上架单详情id更新上架单详情状态为‘已上架’
                        putBillDetailDAO.updatePutBillDetailState(putBillDetailId);
                        //（2）查询该上架单对应的上架单详情的状态是否都已上架，
                        // 如果都已上架则更新上架单状态为‘上架完成’，否则更新状态为‘上架中’
                        EntityWrapper<PutBillDetail> putBillDetailEntity2 = new EntityWrapper<PutBillDetail>();
                        putBillDetailEntity2.eq("put_bill_id", putBillId);
                        putBillDetailEntity2.eq("state", "0");
                        Integer putBillDetailCount = putBillDetailDAO.selectCount(putBillDetailEntity2);
                        if (putBillDetailCount > 0) {//如果count大于0，则更新上架单状态为‘上架中’
                            putBillDetailDAO.updateStateToPuting(Long.parseLong(putBillId));
                        } else {//否则更新上架单状态为‘上架完成’
                            putBillDetailDAO.updateStateToComplete(Long.parseLong(putBillId));
                        }

                        /**4.更新入库单的入库状态**/
                        //查询该上架单对应的入库单，再查询该入库单对应的所有入库单详情的物料的可拆分数量和可拆分重量是否都为0，
                        //如果都为0，则更新入库单状态为‘入库完成’，否则更新入库单状态为‘入库中’
                        EntityWrapper<InstorageDetail> instorageDetailEntity = new EntityWrapper<InstorageDetail>();
                        instorageDetailEntity.eq("instorage_bill_id", instorageBillId);
                        List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(instorageDetailEntity);
                        //定义入库单详情的可拆分数量总和
                        Double separableAmountTotal = 0d;
                        //定义入库单详情的可拆分重量总和
                        Double separableWeightTotal = 0d;
                        if (lstInstorageDetail != null && lstInstorageDetail.size() > 0) {
                            for (InstorageDetail instorageDetail : lstInstorageDetail) {
                                separableAmountTotal += instorageDetail.getSeparableAmount() == null ? 0d : Double.parseDouble(instorageDetail.getSeparableAmount());
                                separableWeightTotal += instorageDetail.getSeparableWeight() == null ? 0d : Double.parseDouble(instorageDetail.getSeparableWeight());
                            }
                        }
                        //获取关于此入库单的所有上架单
                        List<PutBill> putBillList = putBillDAO.selectList(new EntityWrapper<PutBill>().eq("instorage_bill_id", instorageBillId));
                        //判断商家但中是否存在待上架
                        Boolean result = true;
                        for (PutBill putBill1 : putBillList) {
                            if (putBill1.getState().equals("1") || putBill1.getState().equals("2")) {
                                result = false;
                                break;
                            }
                        }
                        //可拆分重量和数量为零，且所有的上架中不存在待上架或上架中状态的上架单（只可为未提交，或已完成状态）
                        if (separableAmountTotal == 0 && separableWeightTotal == 0 && result) {
                            //更新入库单状态为‘入库完成’
                            putBillDetailDAO.updateInstorageStateToComplete(instorageBillId);
                        } else {
                            //更新入库单状态为‘入库中’
                            putBillDetailDAO.updateInstorageStateToBeing(instorageBillId);
                        }

                        /**6.更新物料绑定详情表的批次号和库位id**/
                        Map<String, Object> paramsMap1 = new HashMap();
                        paramsMap1.put("materialCode", materialCode);
                        paramsMap1.put("rfid", rfid);
                        paramsMap1.put("batchNo", batchNo);
                        paramsMap1.put("positionId", positionId);
                        paramsMap1.put("status", DyylConstant.STATE_UNTREATED);
                        putBillDetailDAO.updateBindRfidDetail(paramsMap1);
                        //更新物料绑定表状态为入库状态
                        putBillDetailDAO.updateBindRfid(rfid, positionId);

                        /**7.库存变动表插入数据**/
                        //根据上架单id获取上架单编号
                        PutBill putBill1 = putBillDAO.selectById(putBillId);
                        String putBillCode = putBill1 == null ? "" : putBill1.getPutBillCode();
                        StockChange stockChange = new StockChange();
                        stockChange.setChangeCode(putBillCode);
                        stockChange.setMaterialCode(materialCode);
                        stockChange.setMaterialName(materialName);
                        stockChange.setBatchNo(batchNo);
                        //设置“入库”类型
                        stockChange.setBusinessType("0");
                        stockChange.setInAmount(amount);
                        stockChange.setInWeight(weight);
                        stockChange.setPositionBy(positionId);
                        stockChange.setCreateTime(nowDate);
                        stockChange.setCreateBy(userId);
                        stockChangeDAO.insert(stockChange);

                        /**8.若此入库单为已完成状态 则清空关于此入库单，且状态为未提交的上架单**/
                        if (result) {
                            for (PutBill pb : putBillList) {
                                //若上架单状态为未提交，则删除此上架单和上架详情
                                if (pb.getState().equals(DyylConstant.STATE_UNCOMMIT)) {
                                    putBillDetailDAO.delete(new EntityWrapper<PutBillDetail>().eq("put_bill_id", pb.getId()));
                                    putBillDAO.deleteById(pb);
                                }
                            }
                        }
                    }
                }

            }

            //更新物料绑定坐标和更新排序
            updateMaterielBindRfidShort(userIP, positionCode, rfidArr);

            //清除绑定关系表的相关字段
            instorageSlabDAO.updateAlert1(userIP);

        } else {
            return;
        }
    }

    @Override
    public Map<String, Object> slabUpRfid(String IP, String rfid) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        String alertKey = "";
        String alertValue = "";
        String slabRfid = "";
        try {
            /**1.验证叉到的rfid绑定的物料与正在执行的上架单中的物料详情是否匹配（包括数量和重量验证）**/
            /**2.验证物料的数量和重量是否超过了入库单的可拆分数量和重量**/
            /**3.验证通过的话，更新平板参数绑定表的rfid**/
            /**4.页面弹出一个推荐库位**/
            if (StringUtils.isBlank(IP) || StringUtils.isBlank(rfid)) {
                map.put("result", false);
                map.put("msg", "调用接口失败，请确认参数是否有效");
                return map;
            }
            //根据ip获取该叉车绑定信息，IP是平板服务端传过来的平板ip地址，在该表中是唯一的
            Map<String, Object> map1 = getSlabBillBunding(IP);
            //上架单主键id
            String putBillId = "";
            if (map1 != null) {
                putBillId = map1.get("put_bill_id") == null ? "" : map1.get("put_bill_id").toString();
            }

            List<Map<String, Object>> rfidDetailList = new ArrayList<>();
            //托盘rfid在库状态（0未入库，1已入库）
            String status = "0";
            MaterielBindRfid materielBindRfid = new MaterielBindRfid();
            materielBindRfid.setRfid(rfid);
            materielBindRfid.setDeletedFlag("1");
            materielBindRfid = materielBindRfidDAO.selectOne(materielBindRfid);
            if (materielBindRfid != null) {
                status = materielBindRfid.getStatus() == null ? "0" : materielBindRfid.getStatus();
            }
            if ("1".equals(status)) {
                alertKey = "0";
                alertValue = "该托盘已入库，请确认！";
            } else {
                //根据rfid查询绑定的物料
                rfidDetailList = getMaterialBundingDetail(rfid);
                if (rfidDetailList != null && rfidDetailList.size() > 0) {
                    for (Map<String, Object> rfidMap : rfidDetailList) {
                        //物料编号
                        String materialCode = rfidMap.get("materiel_code").toString();
                        //绑定的数量
                        Double amount = rfidMap.get("amount") == null ? 0d : Double.parseDouble(rfidMap.get("amount").toString());
                        //绑定的重量
                        Double weight = rfidMap.get("weight") == null ? 0d : Double.parseDouble(rfidMap.get("weight").toString());

                        //根据上架单id和物料编号获取上架单对应的入库详情单
                        Map<String, Object> instorageDetailMap = getPutBillDetailMap(putBillId, materialCode);
                        if (instorageDetailMap != null) {
                            //可拆分数量
                            Double separableAmount = instorageDetailMap.get("separable_amount") == null ? 0d : Double.parseDouble(instorageDetailMap.get("separable_amount").toString());
                            //可拆分重量
                            Double separableWeight = instorageDetailMap.get("separable_weight") == null ? 0d : Double.parseDouble(instorageDetailMap.get("separable_weight").toString());
                            if (amount > separableAmount || weight > separableWeight) {
                                alertKey = "0";
                                alertValue = "该托盘绑定的物料的数量或重量过大，请拆分托盘！";
                                break;
                            } else {
                                slabRfid = rfid;
                                alertKey = "1";
                                alertValue = "验证通过";
                            }
                        } else {
                            alertKey = "0";
                            alertValue = "该托盘绑定的物料与要上架的物料不符，请确认！";
                            break;
                        }
                    }

                } else {
                    //查询上架单对应的入库单的入库流程（0：一般流程；1：白糖流程）
                    String instorageProcess = instorageSlabDAO.getInstorageProcess(putBillId);
                    if ("0".equals(instorageProcess)) {
                        alertKey = "0";
                        alertValue = "该托盘未绑定物料，请确认！";
                    } else {//如果该rfid没有绑定物料，默认是白糖类型的单据。注：就算叉错了托盘系统也无法分辨
                        slabRfid = rfid;
                        alertKey = "1";
                        alertValue = "验证通过";
                    }
                }
            }

            //将验证通过的rfid以逗号分隔，更新到表中。
            if ("1".equals(alertKey)) {
                Map<String, Object> map2 = getSlabBillBunding(IP);
                if (map2 != null) {
                    //如果是叉了两个rfid，则在传第二个rfid时表里有第一个rfid存在了
                    String tempRfid = map2.get("rfid") == null ? "" : map2.get("rfid").toString();
                    if (StringUtils.isNotBlank(tempRfid) && tempRfid.indexOf(slabRfid) == -1) {//如果不包含

                        /**如果叉了两个托盘，验证这两个托盘中的物料和批次号是否一致**/
                        //第一个托盘rfid
                        List<Map<String, Object>> tempRfidDetailList = getMaterialBundingDetail(tempRfid);
                        if (rfidDetailList != null && rfidDetailList.size() > 0 && tempRfidDetailList != null && tempRfidDetailList.size() > 0) {
                            List<Map<String, Object>> firstRfidList = new ArrayList<>();
                            List<Map<String, Object>> firstRfidList2 = new ArrayList<>();
                            for (Map<String, Object> firstRfid : tempRfidDetailList) {
                                String materialCode = firstRfid.get("materiel_code").toString();
                                String batchNo = "";
                                Double rfidAmount = firstRfid.get("amount") == null ? 0d : Double.parseDouble(firstRfid.get("amount").toString());
                                Double rfidWeight = firstRfid.get("weight") == null ? 0d : Double.parseDouble(firstRfid.get("weight").toString());
                                //根据上架单id和物料编号获取上架单对应的入库详情单
                                Map<String, Object> putBillDetailMap = getPutBillDetailMap(putBillId, materialCode);
                                if (putBillDetailMap != null) {
                                    batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();
                                }
                                Map<String, Object> firstMap = new HashMap<>();
                                firstMap.put("materialCode", materialCode);
                                firstMap.put("batchNo", batchNo);
                                firstRfidList.add(firstMap);

                                Map<String, Object> firstMap2 = new HashMap<>();
                                firstMap2.put("materialCode", materialCode);
                                firstMap2.put("rfidAmount", rfidAmount);
                                firstMap2.put("rfidWeight", rfidWeight);
                                firstRfidList2.add(firstMap2);

                            }

                            List<Map<String, Object>> secondRfidList = new ArrayList<>();
                            List<Map<String, Object>> secondRfidList2 = new ArrayList<>();
                            for (Map<String, Object> secondRfid : rfidDetailList) {
                                String materialCode = secondRfid.get("materiel_code").toString();
                                String batchNo = "";
                                Double rfidAmount = secondRfid.get("amount") == null ? 0d : Double.parseDouble(secondRfid.get("amount").toString());
                                Double rfidWeight = secondRfid.get("weight") == null ? 0d : Double.parseDouble(secondRfid.get("weight").toString());
                                //根据上架单id和物料编号获取上架单对应的入库详情单
                                Map<String, Object> putBillDetailMap = getPutBillDetailMap(putBillId, materialCode);
                                if (putBillDetailMap != null) {
                                    batchNo = putBillDetailMap.get("batch_no") == null ? "" : putBillDetailMap.get("batch_no").toString();
                                }
                                Map<String, Object> secondMap = new HashMap<>();
                                secondMap.put("materialCode", materialCode);
                                secondMap.put("batchNo", batchNo);
                                secondRfidList.add(secondMap);

                                Map<String, Object> secondMap2 = new HashMap<>();
                                secondMap2.put("materialCode", materialCode);
                                secondMap2.put("rfidAmount", rfidAmount);
                                secondMap2.put("rfidWeight", rfidWeight);
                                secondRfidList2.add(secondMap2);
                            }
                            //判断两个list的内容是否一样
                            boolean b = Tools.isListEqual(firstRfidList, secondRfidList);
                            if (!b) {//如果物料编号和批次号不一样
                                alertKey = "0";
                                alertValue = "请确定两个rifd绑定的物料编码和批次号是否一样";
                            } else {
                                //如果两个rfid的物料编号和批次号都一样，则判断两个rfid中对应的各物料的数量和重量总和是否超过入库单的可拆分数量和重量
                                OUT:
                                for (int i = 0; i < firstRfidList2.size(); i++) {
                                    for (int j = 0; j < secondRfidList2.size(); j++) {
                                        //第一个rfid
                                        Map<String, Object> firstRfidMap2 = firstRfidList2.get(i);
                                        //第二个rfid
                                        Map<String, Object> secondRfidMap2 = secondRfidList2.get(j);
                                        String firstRfidMaterialCode = firstRfidMap2.get("materialCode").toString();
                                        String secondRfidMaterialCode = secondRfidMap2.get("materialCode").toString();
                                        //如果两个rfid中的物料编号相等
                                        if (firstRfidMaterialCode.equals(secondRfidMaterialCode)) {
                                            Double firstRfidAmount = Double.parseDouble(firstRfidMap2.get("rfidAmount").toString());
                                            Double firstRfidWeight = Double.parseDouble(firstRfidMap2.get("rfidWeight").toString());
                                            Double secondRfidAmount = Double.parseDouble(secondRfidMap2.get("rfidAmount").toString());
                                            Double secondRfidWeight = Double.parseDouble(secondRfidMap2.get("rfidWeight").toString());
                                            //两个托盘数量总和
                                            Double totalRfidAmount = firstRfidAmount + secondRfidAmount;
                                            //两个托盘重量总和
                                            Double totalRfidWeight = firstRfidWeight + secondRfidWeight;
                                            //根据上架单id和物料编号获取上架单对应的入库详情单
                                            Map<String, Object> instorageDetailMap = instorageSlabDAO.getPutBillDetailMap(putBillId, firstRfidMaterialCode);
                                            //入库单可拆分数量
                                            Double separableAmount = instorageDetailMap.get("separable_amount") == null ? 0d : Double.parseDouble(instorageDetailMap.get("separable_amount").toString());
                                            //入库单可拆分重量
                                            Double separableWeight = instorageDetailMap.get("separable_weight") == null ? 0d : Double.parseDouble(instorageDetailMap.get("separable_weight").toString());

                                            if (totalRfidAmount > separableAmount) {
                                                alertKey = "0";
                                                alertValue = "请确定两个rifd绑定的【" + firstRfidMaterialCode + "】物料的数量总和是否超过入库单的可拆分数量";
                                                break OUT;
                                            }

                                            if (totalRfidWeight > separableWeight) {
                                                alertKey = "0";
                                                alertValue = "请确定两个rifd绑定的【" + firstRfidMaterialCode + "】物料的重量总和是否超过入库单的可拆分重量";
                                                break OUT;
                                            }
                                        }

                                    }
                                }

                            }

                            slabRfid = tempRfid + "," + slabRfid;
                        } else {//默认是白糖类型的单据。
                            slabRfid = tempRfid + "," + slabRfid;
                        }

                    }
                }
            }

            //更新平板参数绑定表的rfid和验证字段
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userIP", IP);
            paramMap.put("slabRfid", slabRfid);
            paramMap.put("alertKey", alertKey);
            paramMap.put("alertValue", alertValue);
            updateSlabBillBundingRfid(paramMap);
            result = true;
            msg = "接口执行成功";
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            msg = "接口执行失败";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 推荐库位
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getRecommendPosition(String area, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        //推荐库位编号
        String recommendPositionCode = "";
        //推荐库位名称
        String recommendPositionName = "";
        //库区id
//        Long depotareaId = 0l;

        try {
            //RFID（以逗号分隔）
            String rfidStr = "";
            //当前登陆的ip
            String userIP = IPUtils.getIpAddr(request);
            //获取叉车所在

            //获取关于此IP的RFID
            Map<String, Object> instorageSlabMap = instorageSlabDAO.getSlabBillBunding(userIP);
            if (instorageSlabMap != null) {
                rfidStr = instorageSlabMap.get("rfid") == null ? "" : instorageSlabMap.get("rfid").toString();
            }
            //如果有两个托盘，这边定义两个rfid
            String rfid1 = "";
            String rfid2 = "";
            if (StringUtils.isNotBlank(rfidStr)) {
                String[] rfidArr = rfidStr.split(",");
                if (rfidArr.length == 1) {
                    rfid1 = rfidArr[0];
                }
                if (rfidArr.length == 2) {
                    rfid1 = rfidArr[0];
                    rfid2 = rfidArr[1];
                }
            }

            //根据rfid查询绑定的物料
            List<Map<String, Object>> listMaterialDetail = instorageSlabDAO.getMaterialBundingDetail(rfid1);
            if (listMaterialDetail == null || listMaterialDetail.size() == 0) {//rfid绑定的物料为空，默认是白糖类型的，不推荐库位
                recommendPositionCode = "";
                recommendPositionName = "";
            }


//            if (listMaterialDetail.size() == 1) {//如果rfid绑定的物料种类只有一种，表示该托盘既可以放在混放类型的库位，也可以放在不混放类型的库位上
//                Map<String, Object> materialDetailMap = listMaterialDetail.get(0);
//                //物料编号
//                String materialCode = materialDetailMap.get("materiel_code") == null ? "" : materialDetailMap.get("materiel_code").toString();
//                //托盘绑定重量
//                Double weight = materialDetailMap.get("weight") == null ? 0d : Double.parseDouble(materialDetailMap.get("weight").toString());
//
//                //是否需要推荐新的库位
//                boolean isNewPosition = false;
//                /**1.验证已存放该种物料的库位是否可以推荐，如果可以存放则推荐该库位，否则推荐新的库位**/
//                EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
//                stockEntity.eq("material_code", materialCode);
//                //根据库位编码升序排序
//                stockEntity.orderBy("position_code", true);
//                //根据物料编号去库存表中查询对应的库位（有可能放在了多个库位上）
//                List<Stock> lstStock = stockDAO.selectList(stockEntity);
//                if (lstStock != null && lstStock.size() > 0) {//如果查出的库位存在，则表示该物料已经有对应的库位
//                    /**循环校验对应的库位是否都能用，如果不满足存放条件则推荐新的库位**/
//                    for (Stock stock : lstStock) {
//                        //获取库位编码
//                        String positionCode = stock.getPositionCode();
//                        /**（1）.根据库位编号查询该库位上已存放的物料的托盘库存数量总数和库存重量总数**/
//                        //库位上总的库存重量
//                        Double totalStockWeight = 0d;
//                        //库位上总的托盘库存数量
//                        Double totalStockRfidAmount = 0d;
//                        Map<String, Object> stockMap = instorageSlabDAO.getStockBypositionCode(positionCode);
//                        if (stockMap != null) {
//                            totalStockWeight = stockMap.get("totalStockWeight") == null ? 0d : Double.parseDouble(stockMap.get("totalStockWeight").toString());
//                            totalStockRfidAmount = stockMap.get("totalStockRfidAmount") == null ? 0d : Double.parseDouble(stockMap.get("totalStockRfidAmount").toString());
//                        }
//                        /**（2）.根据库位编号查询库位信息**/
//                        DepotPosition depotPosition = new DepotPosition();
//                        depotPosition.setPositionCode(positionCode);
//                        depotPosition = depotPositionDAO.selectOne(depotPosition);
//                        //库位托盘数量容量
//                        Double capacityRfidAmount = 0d;
//                        //库位重量容量
//                        Double capacityWeight = 0d;
//                        //库位冻结（0:未冻结；1：冻结)
//                        String positionFrozen = "";
//                        if (depotPosition != null) {
//                            capacityRfidAmount = depotPosition.getCapacityRfidAmount() == null ? 0d : Double.parseDouble(depotPosition.getCapacityRfidAmount());
//                            capacityWeight = depotPosition.getCapacityWeight() == null ? 0d : Double.parseDouble(depotPosition.getCapacityWeight());
//                            positionFrozen = depotPosition.getPositionFrozen();
//                        }
//
//                        //如果是未冻结的库位
//                        if ("0".equals(positionFrozen)) {
//                            //剩余库位托盘数量容量
//                            Double surplusCapacityRfidAmount = capacityRfidAmount - totalStockRfidAmount;
//                            //剩余库位重量容量
//                            Double surplusCapacityWeight = capacityWeight - totalStockWeight;
//
//                            //如果剩余库位托盘数量容量大于等于1(每次上架只上一个托盘)并且上架重量小于等于剩余库位重量容量，则将该库位作为推荐库位
//                            //注：如果有多个可以推荐的库位，则取第一个可用库位作为推荐库位
//                            if (surplusCapacityRfidAmount >= 1 && surplusCapacityWeight >= weight) {
//                                recommendPositionCode = positionCode;
//                                break;
//                            } else {
//                                isNewPosition = true;
//                            }
//                        }
//                    }
//                } else {
//                    isNewPosition = true;
//                }
//
//                /**2.推荐新的库位，表示库存中对应的库位都不符合推荐条件,则重新推荐新的库位**/
//                if (isNewPosition) {
//                    //根据物料编号查询物料的“ABC分类”类型
//                    Materiel material = new Materiel();
//                    material.setMaterielCode(materialCode);
//                    String materialClassify = materialDAO.selectOne(material).getUpshelfClassify();
//
//                    //定义该物料可以存放的“ABC分类”
//                    List<String> lstAvailableClassify = new ArrayList<>();
//                    if (StringUtils.isBlank(materialClassify)) {
//                        lstAvailableClassify.add("A");
//                        lstAvailableClassify.add("B");
//                        lstAvailableClassify.add("C");
//                    } else if ("A".equals(materialClassify)) {
//                        lstAvailableClassify.add("A");
//                        lstAvailableClassify.add("B");
//                        lstAvailableClassify.add("C");
//                    } else if ("B".equals(materialClassify)) {
//                        lstAvailableClassify.add("B");
//                        lstAvailableClassify.add("C");
//                    } else if ("C".equals(materialClassify)) {
//                        lstAvailableClassify.add("C");
//                    }
//
//                    EntityWrapper<Stock> stockEntity2 = new EntityWrapper<Stock>();
//                    stockEntity2.groupBy("position_code");
//                    List<Stock> lstStock2 = stockDAO.selectList(stockEntity2);
//                    //库存中的库位
//                    List<String> lstpositionCode = new ArrayList<>();
//                    if (lstStock2 != null && lstStock2.size() > 0) {
//                        for (Stock stock : lstStock2) {
//                            lstpositionCode.add(stock.getPositionCode());
//                        }
//                    }
//                    Map<String, Object> paramMap = new HashMap<>();
//                    paramMap.put("weight", weight);
//                    paramMap.put("lstAvailableClassify", lstAvailableClassify);
//                    paramMap.put("lstpositionCode", lstpositionCode);
//                    //查询推荐的新的库位
//                    Map<String, Object> recommendMap = instorageSlabDAO.selectRecommendPosition(paramMap);
//                    if (recommendMap != null) {
//                        recommendPositionCode = recommendMap.get("position_code") == null ? "" : recommendMap.get("position_code").toString();
//                    }
//                }
//
//            } else if (listMaterialDetail.size() > 1) {//如果rfid绑定的物料种类大于一种，表示该托盘只能放在混放类型的库位上
            /**1.验证已存放该种物料的库位是否可以推荐，如果可以存放则推荐该库位，否则推荐新的库位**/

            //是否需要推荐新的库位
            boolean isNewPosition = false;

            //该rfid托盘上物料的总的物料重量
//            Double totalMaterialWeight = 0d;
            //该rfid托盘上物料的“ABC分类”集合
//            List<String> lstMaterialClassify = new ArrayList<>();
            //库存中存放了该rfid上的物料的库位信息集合
            List<Map<String, Object>> lstPosition = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> materialDetailMap : listMaterialDetail) {
                //物料编号
                String materialCode = materialDetailMap.get("materiel_code") == null ? "" : materialDetailMap.get("materiel_code").toString();
                //托盘绑定重量
//                Double weight = materialDetailMap.get("weight") == null ? 0d : Double.parseDouble(materialDetailMap.get("weight").toString());
//                totalMaterialWeight += weight;

                EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
                stockEntity.eq("material_code", materialCode);
                //根据库位编码升序排序
                stockEntity.orderBy("position_code", true);
                //根据物料编号去库存表中查询对应的库位（有可能放在了多个库位上）
                List<Stock> lstStock = stockDAO.selectList(stockEntity);
                for (Stock stock : lstStock) {
                    //获取库位编码
                    String positionCode = stock.getPositionCode();
                    /**（1）.根据库位编号查询该库位上已存放的物料的托盘库存数量总数和库存重量总数**/
                    //库位上总的库存重量
//                    Double totalStockWeight = 0d;
                    //库位上总的托盘库存数量
                    Double totalStockRfidAmount = 0d;
                    Map<String, Object> stockMap = instorageSlabDAO.getStockBypositionCode(positionCode);
                    if (stockMap != null) {
//                        totalStockWeight = stockMap.get("totalStockWeight") == null ? 0d : Double.parseDouble(stockMap.get("totalStockWeight").toString());
                        totalStockRfidAmount = stockMap.get("totalStockRfidAmount") == null ? 0d : Double.parseDouble(stockMap.get("totalStockRfidAmount").toString());
                    }
                    /**（2）.根据库位编号查询库位信息**/
                    DepotPosition depotPosition = new DepotPosition();
                    depotPosition.setPositionCode(positionCode);
                    depotPosition = depotPositionDAO.selectOne(depotPosition);
                    //库位托盘数量容量
                    Double capacityRfidAmount = 0d;
                    //库位重量容量
//                    Double capacityWeight = 0d;
                    //库位的“ABC分类”类型
//                    String positionClassify = "";
                    //库位冻结（0:未冻结；1：冻结)
                    String positionFrozen = "";

                    if (depotPosition != null) {
                        capacityRfidAmount = depotPosition.getCapacityRfidAmount() == null ? 0d : Double.parseDouble(depotPosition.getCapacityRfidAmount());
//                        capacityWeight = depotPosition.getCapacityWeight() == null ? 0d : Double.parseDouble(depotPosition.getCapacityWeight());
//                        positionClassify = depotPosition.getClassify() == null ? "" : depotPosition.getClassify();
                        positionFrozen = depotPosition.getPositionFrozen();
                    }
                    if ("1".equals(positionFrozen)) {
                        break;
                    }
                    //剩余库位托盘数量容量
                    Double surplusCapacityRfidAmount = capacityRfidAmount - totalStockRfidAmount;
                    //剩余库位重量容量
//                    Double surplusCapacityWeight = capacityWeight - totalStockWeight;

                    //将库位的信息放入集合中
                    Map<String, Object> positionMap = new HashMap<>();
                    positionMap.put("positionCode", positionCode);
//                    positionMap.put("positionClassify", positionClassify);
                    positionMap.put("surplusCapacityRfidAmount", surplusCapacityRfidAmount);
//                    positionMap.put("surplusCapacityWeight", surplusCapacityWeight);
                    lstPosition.add(positionMap);

                    //根据物料编号查询物料的“ABC分类”类型
//                    Materiel material = new Materiel();
//                    material.setMaterielCode(materialCode);
//                    String materialClassify = materialDAO.selectOne(material) == null ? "" : materialDAO.selectOne(material).getUpshelfClassify();
//                    lstMaterialClassify.add(materialClassify);

                }
            }

            if (lstPosition != null && lstPosition.size() > 0) {
                for (Map<String, Object> map : lstPosition) {
                    String positionCode = map.get("positionCode") == null ? "" : map.get("positionCode").toString();
                    Double surplusCapacityRfidAmount = map.get("surplusCapacityRfidAmount") == null ? 0d : Double.parseDouble(map.get("surplusCapacityRfidAmount").toString());
//                    Double surplusCapacityWeight = map.get("surplusCapacityWeight") == null ? 0d : Double.parseDouble(map.get("surplusCapacityWeight").toString());
                    if (surplusCapacityRfidAmount >= 1
//                            && surplusCapacityWeight >= totalMaterialWeight
                    ) {
                        recommendPositionCode = positionCode;
                        break;
                    } else {
                        isNewPosition = true;
                    }
                }
            } else {
                isNewPosition = true;
            }

            /**2.推荐新的库位，表示库存中对应的库位都不符合推荐条件,则重新推荐新的库位**/
            if (isNewPosition) {
                //定义该物料可以存放的“ABC分类”
//                List<String> lstAvailableClassify = new ArrayList<>();
//
//                //去除集合中的空字符串
//                List<String> lstMaterialClassify1 = lstMaterialClassify.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
//                if (lstMaterialClassify1 != null && lstMaterialClassify1.size() > 0) {
//                    if (lstMaterialClassify1.size() > 1) {
//                        //再去除集合中的重复元素
//                        List<String> lstMaterialClassify2 = new ArrayList<String>(new HashSet<String>(lstMaterialClassify1));
//                        if (lstMaterialClassify2.size() > 1) {//如果再去除重复元素后托盘中的所有物料有多种“ABC分类”的属性
//                            //这边只有四种情况：AB，AC，BC，ABC，所以可放的库位类型不是A就是B
//                            if (lstMaterialClassify2.contains("A")) {
//                                lstAvailableClassify.add("A");
//                                lstAvailableClassify.add("B");
//                                lstAvailableClassify.add("C");
//                            } else {
//                                if (lstMaterialClassify2.contains("B")) {
//                                    lstAvailableClassify.add("B");
//                                    lstAvailableClassify.add("C");
//                                } else {
//                                    lstAvailableClassify.add("C");
//                                }
//                            }
//                        } else if (lstMaterialClassify2.size() == 1) {//如果再去除重复元素后托盘中的所有物料只有一种“ABC分类”的属性
//                            if (lstMaterialClassify2.contains("A")) {
//                                lstAvailableClassify.add("A");
//                            } else if (lstMaterialClassify2.contains("B")) {
//                                lstAvailableClassify.add("B");
//                            } else if (lstMaterialClassify2.contains("C")) {
//                                lstAvailableClassify.add("C");
//                            }
//                        }
//
//                    } else {//如果去除空字符串后托盘中的所有物料只有一种“ABC分类”的属性，则可以放任意类型库位
//                        lstAvailableClassify.add("A");
//                        lstAvailableClassify.add("B");
//                        lstAvailableClassify.add("C");
//                    }
//
//                } else {//该种情况表示托盘中的所有物料都没有填写“ABC分类”属性，则可以放任意类型库位
//                    lstAvailableClassify.add("A");
//                    lstAvailableClassify.add("B");
//                    lstAvailableClassify.add("C");
//                }


                EntityWrapper<Stock> stockEntity2 = new EntityWrapper<Stock>();
                stockEntity2.groupBy("position_code");
                List<Stock> lstStock2 = stockDAO.selectList(stockEntity2);
                //库存中的库位
                List<String> lstpositionCode = new ArrayList<>();
                if (lstStock2 != null && lstStock2.size() > 0) {
                    for (Stock stock : lstStock2) {
                        lstpositionCode.add(stock.getPositionCode());
                    }
                }
                Map<String, Object> paramMap = new HashMap<>();
//                paramMap.put("totalMaterialWeight", totalMaterialWeight);
//                paramMap.put("lstAvailableClassify", lstAvailableClassify);
                paramMap.put("lstpositionCode", lstpositionCode);
                paramMap.put("areaId", area);
                //查询推荐的新的库位
                Map<String, Object> recommendMap = instorageSlabDAO.selectRecommendPosition2(paramMap);
                if (recommendMap != null) {
                    recommendPositionCode = recommendMap.get("position_code") == null ? "" : recommendMap.get("position_code").toString();
                }
            }
//            }
            if (StringUtils.isNotBlank(recommendPositionCode)) {
                //根据推荐库位编码获取库位id
                DepotPosition depotPosition = new DepotPosition();
                depotPosition.setPositionCode(recommendPositionCode);
                depotPosition = depotPositionDAO.selectOne(depotPosition);
                if (depotPosition != null) {
                    recommendPositionName = depotPosition.getPositionName();
//                    depotareaId = depotPosition.getDepotareaId();
                }
                 recommendPositionName = depotPositionDAO.selectOne(depotPosition).getPositionName();
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            recommendPositionCode = "";
            recommendPositionName = "";
            result = false;
        }
        resultMap.put("recommendPositionCode", recommendPositionCode);
        resultMap.put("recommendPositionName", recommendPositionName);
        resultMap.put("depotareaId", area);
        resultMap.put("result", result);
        return resultMap;
    }

    /**
     * 验证库位是否可用
     *
     * @param userIP
     * @param positionCode
     * @return
     */
    @Override
    public Map<String, Object> isAvailablePosition(String userIP, String positionCode) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean result = false;
        String msg = "";

        //1.获取rfid（以逗号隔开）
        String rfidStr = "";
        //上架单id
        Long putBillId = 0l;
        Map<String, Object> map = instorageSlabDAO.getSlabBillBunding(userIP);
        if (map != null) {
            rfidStr = map.get("rfid") == null ? "" : map.get("rfid").toString();
            putBillId = map.get("put_bill_id") == null ? 0l : Long.parseLong(map.get("put_bill_id").toString());
        }

        //如果有两个托盘，定义两个rfid
        String rfid1 = "";
        String rfid2 = "";
        if (StringUtils.isNotBlank(rfid1)) {
            String[] rfidArr = rfidStr.split(",");
            if (rfidArr.length == 1) {
                rfid1 = rfidArr[0];
            }
            if (rfidArr.length == 2) {
                rfid1 = rfidArr[0];
                rfid2 = rfidArr[1];
            }
        }

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
            msg = "该库位已冻结，不能上架，请确认";
            resultMap.put("result", result);
            resultMap.put("msg", msg);
            return resultMap;
        }

        //获取rfid绑定的物料信息
        List<Map<String, Object>> materialBundingList = movePositionSlabDAO.getMaterialBundingDetail(rfid1);
//        if ("1".equals(blendType)) {//如果是不混放类型的库位
//            if (materialBundingList != null && materialBundingList.size() > 0) {
//                if (materialBundingList.size() == 1) {//这边表示：如果rfid只绑定了一种物料
//                    Map<String, Object> materialBundingMap = materialBundingList.get(0);
//                    //托盘绑定的物料编号
//                    String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
//                    //托盘绑定的物料重量
//                    Double rfidWeight = materialBundingMap.get("weight") == null ? 0d : Double.parseDouble(materialBundingMap.get("weight").toString());
//
//                    //根据物料编号获取物料的“ABC”分类
//                    String materialClassify = "";
//                    EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
//                    materialEntity.eq("materiel_code", materialCode);
//                    materialEntity.eq("deleted_flag", "1");
//                    List<Materiel> materialList = materielDAO.selectList(materialEntity);
//                    if (materialList != null && materialList.size() > 0) {
//                        //物料编号是唯一的，所以取第一条
//                        Materiel material = materialList.get(0);
//                        materialClassify = material.getUpshelfClassify() == null ? "" : material.getUpshelfClassify();
//                    }
//
//                    if (StringUtils.isBlank(materialClassify) || classify.equals(materialClassify)) {//判断物料的“ABC分类”是否和库位的分类一致
//                        //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
//                        Map<String, Object> stockMap = movePositionSlabDAO.getStockInfo(positionCode);
//                        if (stockMap != null) {//如果有物料
//                            //库存中的物料编号
//                            String stockMaterialCode = stockMap.get("material_code").toString();
//                            if (stockMaterialCode.equals(materialCode)) {
//                                //库存中的物料重量
//                                Double stockWeight = stockMap.get("stock_weight_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_weight_sum").toString());
//                                //库存中的托盘库存数量
//                                Double stockRfidAmount = stockMap.get("stock_rfid_amount_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());
//                                //库位的剩余重量容量
//                                Double surplusCapacityWeight = capacityWeight - stockWeight;
//                                //库位的剩余托盘库存数量容量
//                                Double surplusCapacityStockRfidAmount = capacityRfidAmount - stockRfidAmount;
//
//                                //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
//                                if (surplusCapacityWeight >= rfidWeight && surplusCapacityStockRfidAmount >= 1) {
//                                    result = true;
//                                    msg = "该库位可以上架";
//                                } else {
//                                    result = false;
//                                    msg = "该库位容量不够，请确认";
//                                }
//                            } else {
//                                result = false;
//                                msg = "该库位不可混放，请确认上架的物料与库位上的物料是否一致";
//                            }
//                        } else {//如果没有物料，表示空库位
//                            //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
//                            if (capacityWeight >= rfidWeight && capacityRfidAmount >= 1) {
//                                result = true;
//                                msg = "该库位可以上架";
//                            } else {
//                                result = false;
//                                msg = "该库位容量不够，请确认";
//                            }
//                        }
//                    } else {
//                        result = false;
//                        msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
//                    }
//                } else {//如果rfid绑定了多种物料，则不能入“不混放”类型的库位
//                    result = false;
//                    msg = "该库位不可混放，请确认托盘中是否有多种物料";
//                }
//
//            } else {//如果rfid绑定的物料没有，表示是白糖类型的，不做校验
//                result = true;
//                msg = "该库位可以上架";
//            }

//        } else {//如果是混放类型的库位，
        if (materialBundingList != null && materialBundingList.size() > 0) {
            //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
            Map<String, Object> stockMap = movePositionSlabDAO.getStockInfo(positionCode);

            //库位的剩余重量容量
//            Double surplusCapacityWeight = 0d;
            //库位的剩余托盘库存数量容量
            Double surplusCapacityStockRfidAmount = 0d;

            if (stockMap != null) {//如果有物料

                //库存中该库位总的物料数量
//                Double stockWeightTotal = stockMap.get("stock_weight_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_weight_sum").toString());
                //库存中该库位总的托盘库存数量
                Double stockRfidAmountTotal = stockMap.get("stock_rfid_amount_sum") == null ? 0d : Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());

//                surplusCapacityWeight = capacityWeight - stockWeightTotal;
                surplusCapacityStockRfidAmount = capacityRfidAmount - stockRfidAmountTotal;

            } else {//如果没有物料，表示空库位
//                surplusCapacityWeight = capacityWeight;
                surplusCapacityStockRfidAmount = Double.parseDouble(capacityRfidAmount.toString());
            }

//            //该托盘上物料的“ABC”分类集合
//            List<String> materialClassifyList = new ArrayList<>();
//            //托盘中总的物料重量
//            Double rfidWeightTotal = 0d;
//            for (Map<String, Object> materialBundingMap : materialBundingList) {
//                //托盘绑定的物料编号
//                String materialCode = materialBundingMap.get("materiel_code") == null ? "" : materialBundingMap.get("materiel_code").toString();
//                //托盘绑定的物料重量
//                Double rfidWeight = materialBundingMap.get("weight") == null ? 0d : Double.parseDouble(materialBundingMap.get("weight").toString());
//
//                rfidWeightTotal += rfidWeight;
//
//                //根据物料编号获取物料的“ABC”分类
//                String materialClassify = "";
//                EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
//                materialEntity.eq("materiel_code", materialCode);
//                materialEntity.eq("deleted_flag", "1");
//                List<Materiel> materialList = materielDAO.selectList(materialEntity);
//                if (materialList != null && materialList.size() > 0) {
//                    //物料编号是唯一的，所以取第一条
//                    Materiel material = materialList.get(0);
//                    materialClassify = material.getUpshelfClassify() == null ? "" : material.getUpshelfClassify();
//                }
//                materialClassifyList.add(materialClassify);
//            }


            /**判断“ABC分类”是否符合=====start====**/
            //定义该托盘物料“ABC分类”属性是否与库位“ABC分类”属性符合的字段
//            boolean isAccord = false;
//
//            //分类规则：托盘中有物料AB类则放A货架，AC放A货架，BC放B货架，A和不填放A货架，B和不填放B货架，C和不填放C货架
//            //去除集合中的空字符串
//            List<String> materialClassifyList1 = materialClassifyList.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
//            if (materialClassifyList1 != null && materialClassifyList1.size() > 0) {
//                if (materialClassifyList1.size() > 1) {
//                    //再去除集合中的重复元素
//                    List<String> materialClassifyList2 = new ArrayList<String>(new HashSet<String>(materialClassifyList1));
//                    if (materialClassifyList2.size() > 1) {//如果再去除重复元素后托盘中的所有物料有多种“ABC分类”的属性
//                        //这边只有三种情况：AB，AC，BC,所以可放的库位类型不是A就是B
//                        if (materialClassifyList2.contains("A")) {
//                            if ("A".equals(classify)) {
//                                isAccord = true;
//                            } else {
//                                isAccord = false;
//                            }
//                        } else {
//                            if ("B".equals(classify)) {
//                                isAccord = true;
//                            } else {
//                                isAccord = false;
//                            }
//                        }
//                    } else {//如果再去除重复元素后托盘中的所有物料只有一种“ABC分类”的属性,则放该种分类的库位
//                        if (materialClassifyList2.contains(classify)) {
//                            isAccord = true;
//                        } else {
//                            isAccord = false;
//                        }
//                    }
//
//                } else {//如果去除空字符串后托盘中的所有物料只有一种“ABC分类”的属性，则可以放任意类型库位
//                    isAccord = true;
//                }
//
//            } else {//该种情况表示托盘中的所有物料都没有填写“ABC分类”属性，则可以放任意类型库位
//                isAccord = true;
//            }

            /**判断“ABC分类”是否符合=====end====**/

//            if (isAccord) {//如果库位类型符合
//                //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
            if (
//                        surplusCapacityWeight >= rfidWeightTotal &&
                    surplusCapacityStockRfidAmount >= 1) {
                result = true;
                msg = "该库位可以上架";
            } else {
                result = false;
                msg = "该库位托盘容量不够，请确认";
            }
//            } else {
//                result = false;
//                msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
//            }

        } else {//如果rfid绑定的物料没有，表示是白糖类型的，不做校验
            result = true;
            msg = "该库位可以上架";
        }
//        }
        resultMap.put("result", result);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    public void updateAlert(HttpServletRequest request, String flag) {
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        if ("Y".equals(flag)) {
            instorageSlabDAO.updateAlert2(userIP);
        } else if ("N".equals(flag)) {
            instorageSlabDAO.updateAlert1(userIP);
        }

    }

    @Override
    public Map<String, Object> getExecuteRfid(String userIP) {
        return instorageSlabDAO.getExecuteRfid(userIP);
    }

    @Override
    public void updateAlertToUp(HttpServletRequest request) {
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        instorageSlabDAO.updateAlertToUp(userIP);
    }

    @Override
    public List<StockCar> getCarPosition(String areaId, String forkliftTag) {
        String areaCode = "";
        DepotArea depotArea = depotAreaDAO.selectById(areaId);
        if (depotArea != null) {
            areaCode = depotArea.getAreaCode();
        }
        //当前时间
        String nowDate = DateUtils.getTime();
        //5秒前的时间
        String formDate = DateUtils.getBeforeSecondTime(CarTime);
        return instorageSlabDAO.getCarPosition(areaCode, forkliftTag, formDate, nowDate);
    }

    @Override
    public String getForkliftSelected(String userIp) {
        return instorageSlabDAO.getForkliftSelected(userIp);
    }

    /**
     * 更新RFID坐标数据，并作库位位置排序
     *
     * @param userIP
     * @param positionCode
     * @param rfidArr
     * @author yuany
     * @date 2019-06-11
     */
    public void updateMaterielBindRfidShort(String userIP, String positionCode, String[] rfidArr) {

        //获取当前叉车所在坐标位置
        UwbMoveRecord uwbMoveRecord = instorageSlabDAO.getUwbMoveRecord(userIP);
        //获取库位信息
        DepotPosition depotPosition = depotPositionService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code", positionCode));

        //根据RFID，插入坐标数据
        for (int i = 0; i < rfidArr.length; i++) {
            MaterielBindRfid materielBindRfid = materielBindRfidDAO.materielBindRfid(rfidArr[i]);
            materielBindRfid.setCoordinateX(uwbMoveRecord.getXsize());
            materielBindRfid.setCoordinateY(uwbMoveRecord.getYsize());
            materielBindRfidDAO.updateById(materielBindRfid);
        }

        //获取同一库位的RFID绑定关系集合
        List<MaterielBindRfid> materielBindRfidList = materielBindRfidService.selectList(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("position_by", depotPosition.getId())


        );

        //将绑定集合数据添加到库位排序集合中
        List<Double> list = new ArrayList<>();
        for (MaterielBindRfid materielBindRfid : materielBindRfidList) {
            //判断坐标XY值是否为空，不为空则按X/Y方向排序，若为空则不排序，库位方向为空也不排序
            if (StringUtils.isNotEmpty(materielBindRfid.getCoordinateX()) && StringUtils.isNotEmpty(materielBindRfid.getCoordinateY())) {
                if ("X".equals(depotPosition.getPositionDirection())) {
                    list.add(Double.parseDouble(materielBindRfid.getCoordinateX().trim()));
                } else if ("Y".equals(depotPosition.getPositionDirection())) {
                    list.add(Double.parseDouble(materielBindRfid.getCoordinateY().trim()));
                } else {
                    continue;
                }
            }
        }

        //List集合 去重
        List<Double> positionDirection = list.stream().distinct().collect(Collectors.toList());
        //List集合 自动排序
        Collections.sort(positionDirection);
        //遍历排序集合，给绑定集合排序赋值
        for (int i = 1; i <= positionDirection.size(); i++) {
            for (MaterielBindRfid materielBindRfid : materielBindRfidList) {
                //区分X坐标 Y坐标排序
                if ("X".equals(depotPosition.getPositionDirection())) {
                    if (positionDirection.get(i - 1).toString().equals(materielBindRfid.getCoordinateX())) {
                        materielBindRfid.setSort(i);
                        materielBindRfidService.updateById(materielBindRfid);
                    }
                } else if ("Y".equals(depotPosition.getPositionDirection())) {
                    if (positionDirection.get(i - 1).toString().equals(materielBindRfid.getCoordinateY())) {
                        materielBindRfid.setSort(i);
                        materielBindRfidService.updateById(materielBindRfid);
                    }
                } else {
                    continue;
                }
            }
        }

    }
}
    