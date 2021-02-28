package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.instorage.service.*;
import com.tbl.modules.platform.entity.system.InterfaceLog;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.StockChangeService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料绑定RFID详情管理service实现
 *
 * @author yuany
 * @date 2019-01-11
 */
@Service("materielBindRfidDetailService")
public class MaterielBindRfidDetailServiceImpl extends ServiceImpl<MaterielBindRfidDetailDAO, MaterielBindRfidDetail> implements MaterielBindRfidDetailService {

    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDao;

    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @Autowired
    private MaterielBindRfidDAO materielBindRfidDao;


    @Autowired
    private MaterielDAO materielDao;

    @Autowired
    private PutBillDetailService putBillDetailService;

    @Autowired
    private PutBillService putBillService;

    @Autowired
    private InstorageDetailService instorageDetailService;

    @Autowired
    private QualityBillService qualityBillService;

    @Autowired
    private InstorageService instorageService;

    @Autowired
    private StockService stockService;

    @Autowired
    private DepotPositionService depotPositionService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private StockChangeService stockChangeService;
    /**
     * 分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-11
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        //物料绑定RFID 主键id
        String materielBindRfidBy = (String) parms.get("materielBindRfidBy");

        Page<MaterielBindRfidDetail> page = this.selectPage(
                new Query<MaterielBindRfidDetail>(parms).getPage(),
                new EntityWrapper<>()
        );

        if (StringUtils.isEmpty(materielBindRfidBy)) {
            return null;
        }

        MaterielBindRfid materielBindRfid = materielBindRfidDao.selectById(materielBindRfidBy);
        if (materielBindRfid == null && StringUtils.isEmpty(materielBindRfid.getStatus())) {
            return null;
        }
        //如果materielBindRfidBy不为空分页查询获取的数据
        List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = null;
        //当status的值为0时，则表明物料绑定RFID的库位为空。物料绑定RFID查询与库位并无关联
        if (materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED) || materielBindRfid.getPositionBy().equals(0L)) {
            lstMaterielBindRfidDetail = materielBindRfidDetailDao.selectByListN(page, parms);
        } else if (materielBindRfid.getStatus().equals(DyylConstant.STATE_UNTREATED)) {  //当status的值为1时，则表明物料绑定RFID的库位有值。物料绑定RFID查询与库位有关联
            lstMaterielBindRfidDetail = materielBindRfidDetailDao.selectByList(page, parms);
        }

        return new PageUtils(page.setRecords(lstMaterielBindRfidDetail));
    }

    /**
     * 更改物料详情物料数量
     *
     * @param amount
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    @Override
    public Integer updateAmount(String amount, Long materielBindRfidDetailBy) {

        int count = materielBindRfidDetailDao.updateAmount(amount, materielBindRfidDetailBy);

        return count;
    }

    /**
     * 更改物料详情物料重量
     *
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    @Override
    public Integer updateWeight(String weight, Long materielBindRfidDetailBy) {

        int count = materielBindRfidDetailDao.updateWeight(weight, materielBindRfidDetailBy);

        return count;
    }

    /**
     * 更改物料详情物料数量/重量
     *
     * @param amount
     * @param weight
     * @param materielBindRfidDetailBy
     * @return
     * @author yuany
     * @date 2019-01-13
     */
    @Override
    public Integer updateSurplus(String amount, String weight, Long materielBindRfidDetailBy) {

        int count = materielBindRfidDetailDao.updateSurplus(amount, weight, materielBindRfidDetailBy);

        return count;
    }

    /**
     * @Description: 保存物料详情
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-13
     */
    @Override
    public boolean saveReceiptDetail(Long materielBindRfidBy, String materialCodes) {
        if (StringUtils.isNotEmpty(materialCodes)) {
            String[] materialCodeArr = materialCodes.split(",");
            MaterielBindRfidDetail materielBindRfidDetail = null;
            for (int i = 0; i < materialCodeArr.length; i++) {
                materielBindRfidDetail = new MaterielBindRfidDetail();

                //插入物料绑定主键id
                materielBindRfidDetail.setMaterielBindRfidBy(materielBindRfidBy);
                //插入物料编码
                materielBindRfidDetail.setMaterielCode(materialCodeArr[i]);
                //插入物料未删除条件
                materielBindRfidDetail.setDeleteFlag(DyylConstant.NOTDELETED);
                //获取物料绑定实体
                MaterielBindRfid materielBindRfid = materielBindRfidDao.selectById(materielBindRfidBy);
                //根据materielBindRfidBy获取RFID并插入
                materielBindRfidDetail.setRfid(materielBindRfid.getRfid());
                //插入物料绑定详情库位id
//                materielBindRfidDetail.setPositionId();

                //根据物料编号查询物料相关信息
                EntityWrapper<Materiel> entity = new EntityWrapper<>();
                entity.eq("materiel_code", materialCodeArr[i]);
                List<Materiel> lstMaterial = materielDao.selectList(entity.eq("materiel_code", materialCodeArr[i]));
                if (lstMaterial != null && lstMaterial.size() > 0) {
                    //根据物料编号查询的物料只存在一条，所以就取第一条数据
                    Materiel material = lstMaterial.get(0);
                    //插入物料名称
                    materielBindRfidDetail.setMaterielName(material.getMaterielName());
                    //插入物料包装单位
                    materielBindRfidDetail.setUnit(material.getUnit());
                }

                materielBindRfidDetailDao.insert(materielBindRfidDetail);
            }
        }
        return true;
    }

    /**
     * 删除物料绑定RFID详情
     *
     * @param ids
     * @return
     * @author yuany
     * @date 2019-01-14
     */
    @Override
    public boolean deleteMaterielBindRfidDetail(String ids) {
        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        for (Long id : lstMaterialIds){
            MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(id);
            if (materielBindRfidDetail!=null){
                materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
                if (!materielBindRfidDetailService.updateById(materielBindRfidDetail)){
                    return  false;
                }
            }
        }
        return true;
    }

    /**
     * 根据materielCode获取
     *
     * @param materielCode
     * @return
     */
    @Override
    public List<Long> getMaterielBindRfidByList(String materielCode, String materielName) {

        List<Long> materielBindRfidBys = new ArrayList<>();

        List<MaterielBindRfidDetail> materielBindRfidDetailList = null;

        //防止materielCode或materielName为空
        if (StringUtils.isNotBlank(materielCode) || StringUtils.isNotBlank(materielName)) {
            //获取物料绑定RFID详情集合
            materielBindRfidDetailList = materielBindRfidDetailDao.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq(StringUtils.isNotBlank(materielCode), "materiel_code", materielCode)
                            .eq(StringUtils.isNotBlank(materielName), "materiel_name", materielName)
            );

            for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                materielBindRfidBys.add(materielBindRfidDetail.getMaterielBindRfidBy());
            }
        }

        return materielBindRfidBys;
    }

    /**
     * 合并/拆分分页条件查询
     *
     * @param parms
     * @return
     */
    @Override
    public PageUtils queryMaterielBindRfidDetailPage(Map<String, Object> parms) {
        String rfid = (String) parms.get("rfid");
        //不为空则去掉前后空格
        if (StringUtils.isNotBlank(rfid)) {
            rfid = rfid.trim();
        }

        Page<MaterielBindRfidDetail> mbrdPage = new Page<>();

        MaterielBindRfid materielBindRfid = materielBindRfidDao.materielBindRfid(rfid);
        //根据RFID判断物料绑定 是否已入库
        if (materielBindRfid != null && materielBindRfid.getStatus().equals(DyylConstant.STATE_UNTREATED) && materielBindRfid.getPositionBy() != null) {
            DepotPosition depotPosition = depotPositionService.selectById(materielBindRfid.getPositionBy());
            //判断库位是否为空，是否为不可混放的条件
            if (depotPosition != null) {
                mbrdPage = this.selectPage(
                        new Query<MaterielBindRfidDetail>(parms).getPage(),
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("delete_flag", DyylConstant.NOTDELETED)
                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                );
//                List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailDao.selectList(
//                        new EntityWrapper<MaterielBindRfidDetail>()
//                                .eq("delete_flag", DyylConstant.NOTDELETED)
//                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
//                );
//                boolean result = false;
//                for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
//                    if (Strings.isNullOrEmpty(materielBindRfidDetail.getOutstorageBillCode())) {
//                        result = true;
//                    }
//                }
                //判断是否有出库单占用
//                if (result) {
//                    mbrdPage = this.selectPage(
//                            new Query<MaterielBindRfidDetail>(parms).getPage(),
//                            new EntityWrapper<MaterielBindRfidDetail>()
//                                    .eq("delete_flag", DyylConstant.NOTDELETED)
//                                    .eq("rfid", rfid)
//                    );
//                }
            }
        }

        return new PageUtils(mbrdPage);
    }

    /**
     * 根据ID 清空数据中number的值
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateNumWeiById(Long id) {
        int count = materielBindRfidDetailDao.updateNumWeiById(id);
        return count;
    }

    /**
     * 逻辑删除物料详情
     *
     * @param deleteBy
     * @param id
     * @return
     */
    @Override
    public Integer updateDeleteFlag(Long deleteBy, Long id) {
        int count = materielBindRfidDetailDao.updateDeleteFlag(deleteBy, id);
        return count;
    }

    /**
     * 白糖绑定更新数量
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @return
     */
    public Map<String, Object> sugarBindAmount(String rfid, String materialCode, String amount) {

        Map<String, Object> map = new HashMap<>();

        //判断参数是否为空
        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(materialCode) || Strings.isNullOrEmpty(amount)) {
            map.put("msg", "失败原因：获取参数存在空值");
            map.put("result", false);
            return map;
        }

        //RFID/物料编码可获取关于此条件唯一的上架单详情
        PutBillDetail putBillDetail = putBillDetailService.selectOne(
                new EntityWrapper<PutBillDetail>()
                        .eq("rfid", rfid)
                        .eq("material_code", materialCode)
        );
        if (putBillDetail == null) {
            map.put("msg", "失败原因：为获取唯一上架单详情");
            map.put("result", false);
            return map;
        }

        //获取上架单
        PutBill putBill = putBillService.selectById(putBillDetail.getPutBillId());
        //获取入库单详情
        if (putBill == null) {
            map.put("msg", "失败原因：未获取上架单");
            map.put("result", false);
            return map;
        }

        //条件确定唯一入库详情
        InstorageDetail instorageDetail = instorageDetailService.selectOne(
                new EntityWrapper<InstorageDetail>()
                        .eq("instorage_bill_id", putBill.getInstorageBillId())
                        .eq("material_code", materialCode)
        );
        if (instorageDetail == null) {
            map.put("msg", "失败原因：未获取唯一的入库单详情");
            map.put("result", false);
            return map;
        }

        //若可拆分数量小于上架单数量，则返回错误信息
        if (Double.valueOf(amount) > Double.valueOf(instorageDetail.getSeparableAmount())) {
            map.put("msg", "失败原因：上架数量超出入库可拆分的值");
            map.put("result", false);
            return map;
        }

        //RFID 物料编码可获取唯一物料绑定详情
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("rfid", rfid)
                        .eq("materiel_code", materialCode)
        );
        if (materielBindRfidDetail == null) {
            map.put("msg", "失败原因：为获取唯一物料绑定详情");
            map.put("result", false);
            return map;
        }

        //更新绑定详情数量
        materielBindRfidDetail.setAmount(amount);
        if (!materielBindRfidDetailService.updateById(materielBindRfidDetail)) {
            map.put("msg", "物料绑定详情更新失败");
            map.put("result", false);
            return map;
        }

        map.put("msg", "更新成功");
        map.put("result", true);
        return map;
    }

    /**
     * 白糖绑定更新重量数量
     *
     * @param rfid
     * @param materialCode
     * @param amount
     * @param weight
     * @return
     */
    public Map<String, Object> sugarBind(String rfid, String materialCode, String amount, String weight, Long userId) {

        Map<String, Object> map = new HashMap<>();

        String parameter = "Rfid:" + rfid + "/MaterialCode:" + materialCode + "/Amount:" + amount + "/Weight:" + weight;
        //判断参数是否为空
        if (Strings.isNullOrEmpty(rfid) || Strings.isNullOrEmpty(materialCode) || Strings.isNullOrEmpty(amount) || Strings.isNullOrEmpty(weight)) {
            map.put("msg", "失败原因：获取参数存在空值");
            map.put("result", false);
            return map;
        }

        //RFID/物料编码可获取关于此条件唯一的上架单详情
        PutBillDetail putBillDetail = putBillDetailService.selectOne(
                new EntityWrapper<PutBillDetail>()
                        .eq("rfid", rfid)
                        .eq("material_code", materialCode)
        );
        if (putBillDetail == null) {
            map.put("msg", "失败原因：为获取唯一上架单详情");
            map.put("result", false);
            return map;
        }

        //获取上架单
        PutBill putBill = putBillService.selectById(putBillDetail.getPutBillId());
        //获取入库单详情
        if (putBill == null) {
            map.put("msg", "失败原因：未获取上架单");
            map.put("result", false);
            return map;
        }

        //条件确定唯一入库详情
        InstorageDetail instorageDetail = instorageDetailService.selectOne(
                new EntityWrapper<InstorageDetail>()
                        .eq("instorage_bill_id", putBill.getInstorageBillId())
                        .eq("material_code", materialCode)
        );
        if (instorageDetail == null) {
            map.put("msg", "失败原因：未获取唯一的入库单详情");
            map.put("result", false);
            return map;
        }

        //若可拆分重量小于上架单重量，则返回错误信息
        if (Double.parseDouble(weight) > Double.parseDouble(instorageDetail.getSeparableWeight())) {
            map.put("msg", "失败原因：上架重量超出入库可拆分的值");
            map.put("result", false);
            return map;
        }

        //RFID 物料编码可获取唯一物料绑定详情
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("rfid", rfid)
                        .eq("materiel_code", materialCode)
        );
        if (materielBindRfidDetail == null) {
            map.put("msg", "失败原因：未获取唯一物料绑定详情");
            map.put("result", false);
            return map;
        }

        //物料绑定ID获取绑定信息
        MaterielBindRfid materielBindRfid = materielBindRfidDao.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
        if (materielBindRfid == null || materielBindRfid.getPositionBy() == null || materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED)) {
            map.put("msg", "失败原因：ID未获取物料绑定信息或绑定信息不符合白糖绑定条件");
            map.put("result", false);
            return map;
        }

        //ID获取库位信息
        DepotPosition depotPosition = depotPositionService.selectById(materielBindRfid.getPositionBy());
        if (depotPosition == null) {
            map.put("msg", "失败原因：ID未获取库位信息");
            map.put("result", false);
            return map;
        }

        //验证库位重量容量、托盘容量是否满足
        //获取关于此库位编码的库存
        List<Stock> stockList = stockService.selectList(new EntityWrapper<Stock>().eq("position_code",depotPosition.getPositionCode()));
        Double weightStock = 0d;
        Integer rfidStockAmout = 0;
        for (Stock stock : stockList){
            weightStock += stock.getStockWeight()==null?0d:Double.parseDouble(stock.getStockWeight());
            rfidStockAmout += stock.getStockRfidAmount()==null?0:Integer.parseInt(stock.getStockRfidAmount());
        }
        Double aviWeight = Double.parseDouble(depotPosition.getCapacityWeight()) - weightStock;
        Integer aviRfidStockAmount = Integer.parseInt(depotPosition.getCapacityRfidAmount()) - rfidStockAmout;
        if (aviWeight < Double.parseDouble(weight) || aviRfidStockAmount < 1){
            map.put("msg", "失败原因：所在库位重量容量或RFID数量容量不足");
            map.put("result", false);
            return map;
        }


//        List<String> lstRfids = Arrays.stream(rfid.split(",")).map(s -> s.trim()).collect(Collectors.toList());

        //库位编、物料编码、RFID可确定唯一库存
//        Stock stock = stockService.selectOne(
//                new EntityWrapper<Stock>()
//                        .eq("material_type", DyylConstant.MATERIAL_RFID)
//                        .eq("position_code", depotPosition.getPositionCode())
//                        .eq("material_code", materialCode)
//                        .in("rfid", lstRfids)
//        );
        Stock stock = materielBindRfidDetailDao.getStockInfo(DyylConstant.MATERIAL_RFID,depotPosition.getPositionCode(),materialCode,rfid);


        if (stock == null) {
            map.put("msg", "失败原因：未获取库存信息");
            map.put("result", false);
            return map;
        }

        //1.更新上架单详情重量数量
        putBillDetail.setPutWeight(weight);
        putBillDetail.setPutAmount(amount);
        if (!putBillDetailService.updateById(putBillDetail)) {
            map.put("msg", "失败原因：上架单数量重量更新失败");
            map.put("result", false);
            return map;
        }

        //2.更新对应的入库单详情中可拆分数量和重量
        Double instorageAmount = Double.parseDouble(instorageDetail.getSeparableAmount()) - Double.parseDouble(amount);
        Double instorageWeight = Double.parseDouble(instorageDetail.getSeparableWeight()) - Double.parseDouble(weight);
        instorageDetail.setSeparableWeight(instorageWeight.toString());
        instorageDetail.setSeparableAmount(instorageAmount.toString());
        if (!instorageDetailService.updateById(instorageDetail)) {
            map.put("msg", "失败原因：入库单详情数量重量更新失败");
            map.put("result", false);
            return map;
        }

        //3.更新绑定详情重量数量
        materielBindRfidDetail.setAmount(amount);
        materielBindRfidDetail.setWeight(weight);
        if (!materielBindRfidDetailService.updateById(materielBindRfidDetail)) {
            map.put("msg", "物料绑定详情更新失败");
            map.put("result", false);
            return map;
        }

        //4.更新库存数据
        Double stockAmount = Double.valueOf(stock.getStockAmount()) + Double.valueOf(amount);
        Double stockWeight = Double.valueOf(stock.getStockWeight()) + Double.valueOf(weight);

        stock.setStockAmount(stockAmount.toString());
        stock.setStockWeight(stockWeight.toString());
        if (!stockService.updateById(stock)) {
            map.put("msg", "失败原因：库存更新失败");
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
        if (!stockChangeService.insert(stockChange)){
            map.put("msg", "失败原因：库存变更记录更新失败");
            map.put("result", false);
            return map;
        }
        map.put("msg", "更新成功");
        map.put("result", true);
        return map;
    }

    @Override
    public List<Map<String, Object>> getMaterialBundingDetail(String rfid) {
        return materielBindRfidDetailDao.getMaterialBundingDetail(rfid);
    }

    /**
     * 排序分页列表
     *
     * @param parms
     * @return
     */
    @Override
    public PageUtils getMaterielBySort(Map<String, Object> parms) {

        //物料绑定RFID 主键id
        String positionId = (String) parms.get("positionId");

        Page<MaterielBindRfidDetail> page = this.selectPage(
                new Query<MaterielBindRfidDetail>(parms).getPage(),
                new EntityWrapper<>()
        );

        if (StringUtils.isEmpty(positionId)) {
            return null;
        }

        //如果positionId不为空分页查询获取的数据
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailDao.selectListBySort(positionId);

        return new PageUtils(page.setRecords(materielBindRfidDetailList));
    }
}
