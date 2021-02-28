package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.InstorageDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.InstorageInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidInterfaceDAO;
import com.tbl.modules.handheldInterface.service.InstorageDetailInterfaceService;
import com.tbl.modules.instorage.dao.InstorageDetailDAO;
import com.tbl.modules.instorage.dao.PutBillDAO;
import com.tbl.modules.instorage.dao.PutBillDetailDAO;
import com.tbl.modules.instorage.dao.QualityBillDAO;
import com.tbl.modules.instorage.entity.*;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库详情接口Service实现
 */
@Service(value = "instorageDetailInterfaceService")
public class InstorageDetailInterfaceServiceImpl extends ServiceImpl<InstorageDetailInterfaceDAO, InstorageDetail> implements InstorageDetailInterfaceService {

    @Autowired
    private PutBillDetailDAO putBillDetailDAO;

    @Autowired
    private DepotPositionDAO depotPositionDao;

    @Autowired
    private PutBillDAO putBillDAO;

    @Autowired
    private InstorageDetailDAO instorageDetailDAO;

    @Autowired
    private InstorageInterfaceDAO instorageInterfaceDAO;

    @Autowired
    private StockDAO stockDao;

    @Autowired
    private QualityBillDAO qualityBillDAO;

    @Autowired
    private StockChangeDAO stockChangeDAO;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completePutBill(Long userId, String putBillDetailId) {
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
        positionEntity.eq("position_code",positionCode);
        List<DepotPosition> lstDepotPosition = depotPositionDao.selectList(positionEntity);
        if(lstDepotPosition!=null && lstDepotPosition.size() > 0){
            //库位编号是唯一的，所以取第一条数据
            DepotPosition depotPosition = lstDepotPosition.get(0);
            positionName = depotPosition.getPositionName()==null?"":depotPosition.getPositionName();
            positionId = depotPosition.getId()==null?0l:depotPosition.getId();
        }

        PutBill putBill = putBillDAO.selectById(putBillId);
        //入库单id
        Long instorageBillId = putBill.getInstorageBillId();
        Instorage instorage = instorageInterfaceDAO.selectById(instorageBillId);
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

        //根据物料编号、批次号、库位编号查询库存表
        EntityWrapper<Stock> stockEntity = new EntityWrapper<Stock>();
        stockEntity.eq("material_code",materialCode);
        stockEntity.eq("batch_no",batchNo);
        stockEntity.eq("position_code",positionCode);
        stockEntity.eq("material_type","1");
        List<Stock> lstStock = stockDao.selectList(stockEntity);

        if(lstStock!=null && lstStock.size()>0){//如果存在则更新
            //获取数据是唯一的，所以取第一条数据
            Stock stock = lstStock.get(0);
            Long id = stock.getId();
            //更新库存表相关信息
            stockDao.updateStockById(id,putAmount,putWeight,rfid);

        }else{//如果不存在则插入
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
        qualityBillEntity.eq("instorage_bill_id",instorageBillId);
        qualityBillEntity.eq("state","1");
        List<QualityBill> lstQualityBill = qualityBillDAO.selectList(qualityBillEntity);
        if(lstQualityBill!=null && lstQualityBill.size()>0){
            //一条入库单只会对应一条“质检通过”的质检单（可以对应多条“质检退回”的质检单）
            Map<String,Object> paramsMap = new HashMap();
            paramsMap.put("materialCode",materialCode);
            paramsMap.put("batchNo",batchNo);
            paramsMap.put("positionCode",positionCode);
            paramsMap.put("putAmount",putAmount);
            paramsMap.put("putWeight",putWeight);
            paramsMap.put("rfid",rfid);
            //更新库存表的可用库存数量、可用库存重量、可用托盘库存数量、可用的RFID（多个rfid用逗号隔开）
            putBillDetailDAO.updateStockAvailableField(paramsMap);
        }

        /**2.更新入库单的可拆分数量和可拆分重量**/
        Map<String,Object> paramsMap = new HashMap();
        paramsMap.put("instorageBillId",instorageBillId);
        paramsMap.put("materialCode",materialCode);
        paramsMap.put("batchNo",batchNo);
        paramsMap.put("putAmount",putAmount);
        paramsMap.put("putWeight",putWeight);
        //根据入库单id、物料编号和批次号更新入库单的可拆分数量和可拆分重量
        putBillDetailDAO.updateSeparableAmountAndWeight(paramsMap);

        /**3.更新上架单详情状态和上架单状态**/
        //（1）根据上架单详情id更新上架单详情状态为‘已上架’
        putBillDetailDAO.updatePutBillDetailState(putBillDetailId);

        //（2）查询该上架单对应的上架单详情的状态是否都已上架，
        // 如果都已上架则更新上架单状态为‘上架完成’，否则更新状态为‘上架中’
        EntityWrapper<PutBillDetail> putBillDetailEntity = new EntityWrapper<PutBillDetail>();
        putBillDetailEntity.eq("put_bill_id",putBillId);
        putBillDetailEntity.eq("state","0");
        Integer count = putBillDetailDAO.selectCount(putBillDetailEntity);
        if(count > 0){//如果count大于0，则更新上架单状态为‘上架中’
            putBillDetailDAO.updateStateToPuting(putBillId);
        }else{//否则更新上架单状态为‘上架完成’
            putBillDetailDAO.updateStateToComplete(putBillId);
        }

        /**4.更新入库单的入库状态**/
        //查询该上架单对应的入库单，再查询该入库单对应的所有入库单详情的物料的可拆分数量和可拆分重量是否都为0，
        //如果都为0，则更新入库单状态为‘入库完成’，否则更新入库单状态为‘入库中’
        EntityWrapper<InstorageDetail> instorageDetailEntity = new EntityWrapper<InstorageDetail>();
        instorageDetailEntity.eq("instorage_bill_id",instorageBillId);
        List<InstorageDetail> lstInstorageDetail = instorageDetailDAO.selectList(instorageDetailEntity);
        //定义入库单详情的可拆分数量总和
        Double separableAmountTotal = 0d;
        //定义入库单详情的可拆分重量总和
        Double separableWeightTotal = 0d;
        if(lstInstorageDetail!=null && lstInstorageDetail.size()>0){
            for(InstorageDetail instorageDetail : lstInstorageDetail){
                separableAmountTotal += instorageDetail.getSeparableAmount()==null?0d:Double.parseDouble(instorageDetail.getSeparableAmount());
                separableWeightTotal += instorageDetail.getSeparableWeight()==null?0d:Double.parseDouble(instorageDetail.getSeparableWeight());
            }
        }
        //获取关于此入库单的所有上架单
        List<PutBill> putBillList = putBillDAO.selectList(new EntityWrapper<PutBill>().eq("instorage_bill_id",instorageBillId));
        //判断商家但中是否存在待上架
        Boolean result = true ;
        for (PutBill putBill1 : putBillList){
            if (putBill1.getState().equals("1") || putBill1.getState().equals("2")){
                result = false;
                break;
            }
        }
        //可拆分重量和数量为零，且所有的上架中不存在待上架或上架中状态的上架单（只可为未提交，或已完成状态）
        if(separableAmountTotal==0 && separableWeightTotal==0 && result){
            //更新入库单状态为‘入库完成’
            putBillDetailDAO.updateInstorageStateToComplete(instorageBillId);
        }else{
            //更新入库单状态为‘入库中’
            putBillDetailDAO.updateInstorageStateToBeing(instorageBillId);
        }

        /**5.更新物料绑定详情表的批次号和库位id**/
        Map<String,Object> paramsMap1 = new HashMap();
        paramsMap1.put("materialCode",materialCode);
        paramsMap1.put("rfid",rfid);
        paramsMap1.put("batchNo",batchNo);
        paramsMap1.put("positionId",positionId);
        paramsMap1.put("productDate",productDate);
        paramsMap1.put("status", DyylConstant.STATE_UNTREATED);
        putBillDetailDAO.updateBindRfidDetail(paramsMap1);
        //更新物料绑定表状态为入库状态
        putBillDetailDAO.updateBindRfid(rfid,positionId);

        /**6.库存变动表插入数据**/
        //根据上架单id获取上架单编号
        PutBill putBill1 = putBillDAO.selectById(putBillId);
        String putBillCode = putBill1==null?"":putBill1.getPutBillCode();
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

        /**7.若此入库单为已完成状态 则清空关于此入库单，且状态为未提交的上架单**/
        if (result){
            for (PutBill pb : putBillList){
                //若上架单状态为未提交，则删除此上架单和上架详情
                if (pb.getState().equals(DyylConstant.STATE_UNCOMMIT)){
                    putBillDetailDAO.delete(new EntityWrapper<PutBillDetail>().eq("put_bill_id",pb.getId()));
                    putBillDAO.deleteById(pb);
                }
            }
        }
    }
}
