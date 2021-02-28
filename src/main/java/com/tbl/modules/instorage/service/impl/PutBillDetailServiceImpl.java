package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.*;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.instorage.service.PutBillDetailService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 上架单详情service实现类
 * @author: zj
 * @create: 2019-01-22 17:41
 **/
@Service("putBillDetail")
public class PutBillDetailServiceImpl extends ServiceImpl<PutBillDetailDAO, PutBillDetail> implements PutBillDetailService {
    @Autowired
    private PutBillDAO putBillDAO;
    @Autowired
    private PutBillDetailDAO putBillDetailDAO;
    @Autowired
    private MaterielDAO materielDao;
    @Autowired
    private DepotPositionDAO depotPositionDao;
    @Autowired
    private InstorageDAO instorageDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;
    @Autowired
    private StockDAO stockDao;
    @Autowired
    private QualityBillDAO qualityBillDAO;
    @Autowired
    private StockChangeDAO stockChangeDAO;
    @Autowired
    private MaterielBindRfidService materielBindRfidService;


    @Override
    public Page<PutBillDetail> queryPage(Map<String, Object> params) {
        //上架单主键id
//        String putBillId = (String) params.get("putBillId");
//        Page<PutBillDetail> page = new Page<PutBillDetail>();
//        if(StringUtils.isNotEmpty(putBIllId)){
//            page = this.selectPage(
//                    new Query<PutBillDetail>(params).getPage(),
//                    new EntityWrapper<PutBillDetail>()
//                            .eq("put_bill_id", putBIllId)
//            );
//        }
//        return new PageUtils(page);


        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if ("asc".equals(String.valueOf(params.get("order")))) {
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<PutBillDetail> pagePutBillDetail = new Page<PutBillDetail>(pg, rows, sortname, order);
        //获取上架单详情列表
        List<PutBillDetail> list = putBillDetailDAO.getPagePutBillDetailList(pagePutBillDetail, params);

        return pagePutBillDetail.setRecords(list);

    }

    @Override
    public List<Map<String, Object>> getSelectMaterialList(Long instorageBillId, String queryString, int pageSize, int pageNo) {
        Page page = new Page(pageNo, pageSize);
        return putBillDetailDAO.getSelectMaterialList(page, instorageBillId, queryString);
    }

    @Override
    public Integer getSelectMaterialTotal(Long instorageBillId, String queryString) {
        return putBillDetailDAO.getSelectMaterialTotal(instorageBillId, queryString);
    }

    @Override
    public boolean hasMaterial(Long putBillId, String materialCodes) {
        boolean ret = false;
        List<String> lstMaterialCodes = Arrays.asList(materialCodes.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("putBillId", putBillId);
        map.put("materialCodes", lstMaterialCodes);
        Integer count = putBillDetailDAO.getMaterialCount(map);
        if (count > 0) {
            ret = true;
        }
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean savePutBillDetail(Long putBillId, String materialCodes) {
        //入库单id
        Long instorageId = 0l;
        if (putBillId != null) {
            //根据上架单id获取入库单id
            instorageId = putBillDAO.selectById(putBillId).getInstorageBillId();
        }

        if (StringUtils.isNotEmpty(materialCodes)) {
            String[] materialCodeArr = materialCodes.split(",");
            PutBillDetail putBillDetail = null;
            for (int i = 0; i < materialCodeArr.length; i++) {
                putBillDetail = new PutBillDetail();
                //插入上架单主键id
                putBillDetail.setPutBillId(putBillId);
                //插入物料编码
                putBillDetail.setMaterialCode(materialCodeArr[i]);

                //根据入库单id和物料编码获取物料批次号
                EntityWrapper<InstorageDetail> entityInstorageDetail = new EntityWrapper<InstorageDetail>();
                entityInstorageDetail.eq("instorage_bill_id", instorageId);
                entityInstorageDetail.eq("material_code", materialCodeArr[i]);
                //批次号
                String batchNo = "";
                //生产日期
                String productDate = "";
                List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(entityInstorageDetail);
                if (lstInstorageDetail != null && lstInstorageDetail.size() > 0) {
                    //根据入库单id和物料编码获取物料批次号只存在一条，所以就取第一条数据
                    InstorageDetail instorageDetail = lstInstorageDetail.get(0);
                    batchNo = instorageDetail.getBatchNo();
                    productDate = instorageDetail.getProductDate();
                }
                //插入批次号
                putBillDetail.setBatchNo(batchNo);
                //插入生产日期
                putBillDetail.setProductDate(productDate);

                //根据物料编号查询物料相关信息
                EntityWrapper<Materiel> entity = new EntityWrapper<>();
                entity.eq("materiel_code", materialCodeArr[i]);
                List<Materiel> lstMaterial = materielDao.selectList(entity.eq("materiel_code", materialCodeArr[i]));
                if (lstMaterial != null && lstMaterial.size() > 0) {
                    //根据物料编号查询的物料只存在一条，所以就取第一条数据
                    Materiel material = lstMaterial.get(0);
                    //插入物料名称
                    putBillDetail.setMaterialName(material.getMaterielName());
                    //插入物料包装单位
                    putBillDetail.setUnit(material.getUnit());
                }
                putBillDetailDAO.insert(putBillDetail);
            }
        }
        return true;
    }

    @Override
    public boolean deletePutBillDetail(String ids) {
        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        Integer delCount = putBillDetailDAO.deleteBatchIds(lstMaterialIds);
        if (delCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getPosition() {
        EntityWrapper<DepotPosition> entity = new EntityWrapper<DepotPosition>();
        List<Map<String, Object>> list = depotPositionDao.selectMaps(entity);
        return list;
    }

    @Override
    public String getPutBillDetailByDetailId(Long putBillDetailId) {
        //上架单主键id
        Long putBillId = putBillDetailDAO.selectById(putBillDetailId).getPutBillId();
        //上架单状态
        String state = putBillDAO.selectById(putBillId).getState();
        return state;
    }

    @Override
    public boolean updatePutAmount(Long putBillDetailId, String putAmount) {
        Integer updateResult = putBillDetailDAO.updatePutAmount(putBillDetailId, putAmount);
        if (updateResult > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updatePutWeight(Long putBillDetailId, String putWeight) {
        Integer updateResult = putBillDetailDAO.updatePutWeight(putBillDetailId, putWeight);
        if (updateResult > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateRfid(Long putBillDetailId, String rfid) {
        Integer updateResult = putBillDetailDAO.updateRfid(putBillDetailId, rfid);
        if (updateResult > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updatePositionCode(Long putBillDetailId, String positionCode) {
        Integer updateResult = putBillDetailDAO.updatePositionCode(putBillDetailId, positionCode);
        if (updateResult > 0) {
            return true;
        } else {
            return false;
        }
    }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completePutBill(String putBillDetailId) {
        /**1.往库存表插入/更新数据**/
        //查询上架单详情信息
        PutBillDetail putBillDetail = putBillDetailDAO.selectById(putBillDetailId);
        //上架单id
        Long putBillId = putBillDetail.getPutBillId();
        //物料编号
        String materialCode = putBillDetail.getMaterialCode();
        //物料名称
        String materialName = putBillDetail.getMaterialName();
        //批次号
        String batchNo = putBillDetail.getBatchNo();
        //生产日期
        String productDate = putBillDetail.getProductDate();
        //库位编号
        String positionCode = putBillDetail.getPositionCode();
        //库位名称
        String positionName = "";
        //库位主键id
        Long positionId = 0l;
        //根据库位编号查询库位名称
        EntityWrapper<DepotPosition> positionEntity = new EntityWrapper<DepotPosition>();
        positionEntity.eq("position_code", positionCode);
        List<DepotPosition> lstDepotPosition = depotPositionDao.selectList(positionEntity);
        if (lstDepotPosition != null && lstDepotPosition.size() > 0) {
            //库位编号是唯一的，所以取第一条数据
            DepotPosition depotPosition = lstDepotPosition.get(0);
            positionName = depotPosition.getPositionName() == null ? "" : depotPosition.getPositionName();
            positionId = depotPosition.getId() == null ? 0l : depotPosition.getId();
        }

        PutBill putBill = putBillDAO.selectById(putBillId);
        //入库单id
        Long instorageBillId = putBill.getInstorageBillId();
        Instorage instorage = instorageDAO.selectById(instorageBillId);
        //货主编号
        String customerCode = instorage.getCustomerCode();

        //上架数量
        String putAmount = putBillDetail.getPutAmount();
        //上架重量
        String putWeight = putBillDetail.getPutWeight();
        //托盘库存数量（一次上架一个托盘，所有数量为1）
        String stockRfidAmount = "1";
        //RFID
        String rfid = putBillDetail.getRfid();
        //当前时间
        String nowDate = DateUtils.getTime();
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人
        Long userId = user.getUserId();

        //根据物料编号、批次号、库位编号查询库存表
        EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
        stockEntity.eq("material_code", materialCode);
        stockEntity.eq("batch_no", batchNo);
        stockEntity.eq("position_code", positionCode);
        stockEntity.eq("material_type", "1");
        List<Stock> lstStock = stockDao.selectList(stockEntity);

        if (lstStock != null && lstStock.size() > 0) {//如果存在则更新
            //获取数据是唯一的，所以取第一条数据
            Stock stock = lstStock.get(0);
            Long id = stock.getId();
            //更新库存表相关信息
            stockDao.updateStockById(id, putAmount, putWeight, rfid);

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
            stockDao.insert(stock);
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

//        /**2.更新入库单的可拆分数量和可拆分重量**/
//        Map<String,Object> paramsMap = new HashMap();
//        paramsMap.put("instorageBillId",instorageBillId);
//        paramsMap.put("materialCode",materialCode);
//        paramsMap.put("batchNo",batchNo);
//        paramsMap.put("putAmount",putAmount);
//        paramsMap.put("putWeight",putWeight);
//        //根据入库单id、物料编号和批次号更新入库单的可拆分数量和可拆分重量
//        putBillDetailDAO.updateSeparableAmountAndWeight(paramsMap);


        /**3.更新上架单详情状态和上架单状态**/
        //（1）根据上架单详情id更新上架单详情状态为‘已上架’
        putBillDetailDAO.updatePutBillDetailState(putBillDetailId);

        //（2）查询该上架单对应的上架单详情的状态是否都已上架，
        // 如果都已上架则更新上架单状态为‘上架完成’，否则更新状态为‘上架中’
        EntityWrapper<PutBillDetail> putBillDetailEntity = new EntityWrapper<PutBillDetail>();
        putBillDetailEntity.eq("put_bill_id", putBillId);
        putBillDetailEntity.eq("state", "0");
        Integer count = putBillDetailDAO.selectCount(putBillDetailEntity);
        if (count > 0) {//如果count大于0，则更新上架单状态为‘上架中’
            putBillDetailDAO.updateStateToPuting(putBillId);
        } else {//否则更新上架单状态为‘上架完成’
            putBillDetailDAO.updateStateToComplete(putBillId);
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

        /**5.更新物料绑定详情表的批次号和库位id**/
        Map<String, Object> paramsMap1 = new HashMap();
        paramsMap1.put("materialCode", materialCode);
        paramsMap1.put("rfid", rfid);
        paramsMap1.put("batchNo", batchNo);
        paramsMap1.put("positionId", positionId);
        paramsMap1.put("productDate", productDate);
        paramsMap1.put("status", DyylConstant.STATE_UNTREATED);
        putBillDetailDAO.updateBindRfidDetail(paramsMap1);
        //更新物料绑定表状态为入库状态
        putBillDetailDAO.updateBindRfid(rfid, positionId);

        /**6.更新物料绑定表的序号**/
        EntityWrapper<MaterielBindRfid> wraMaterielBindRfid = new EntityWrapper<>();
        wraMaterielBindRfid.eq("position_by", positionId);
        List<MaterielBindRfid> lstMaterielBindRfid = materielBindRfidService.selectList(wraMaterielBindRfid);
        wraMaterielBindRfid.eq("rfid", rfid);
        MaterielBindRfid materielBindRfid = materielBindRfidService.selectOne(wraMaterielBindRfid);
        materielBindRfid.setSort(lstMaterielBindRfid.size());
        materielBindRfidService.updateById(materielBindRfid);

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
        stockChange.setInAmount(putAmount);
        stockChange.setInWeight(putWeight);
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

    @Override
    public Map<String, Object> selectRfidInPosition(Map<String, Object> paramMap) {
        return putBillDetailDAO.selectRfidInPosition(paramMap);
    }

}
    