package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.InstorageDetailDAO;
import com.tbl.modules.instorage.dao.PutBillDAO;
import com.tbl.modules.instorage.dao.PutBillDetailDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.util.DeriveExcel;
import com.tbl.modules.slab.dao.MovePositionSlabDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 上架单service实现类
 * @author: zj
 * @create: 2019-01-22 17:40
 **/
@Service("putBillService")
public class PutBillServiceImpl extends ServiceImpl<PutBillDAO, PutBill> implements PutBillService {
    @Autowired
    private PutBillDAO putBillDAO;
    @Autowired
    private PutBillDetailDAO putBillDetailDAO;
    @Autowired
    private DepotPositionDAO depotPositionDAO;
    @Autowired
    private MaterielDAO materielDAO;
    @Autowired
    private StockDAO stockDAO;
    @Autowired
    private MovePositionSlabDAO movePositionSlabDAO;
    @Autowired
    private MaterielBindRfidService materielBindRfidService;
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDAO;
    @Autowired
    private InstorageDetailDAO instorageDetailDAO;


    @Override
    public Page<PutBill> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<PutBill> pagePutBill = new Page<PutBill>(pg,rows,sortname,order);
        //获取入库单列表
        List<PutBill> list = putBillDAO.getPagePutBillList(pagePutBill, params);

        return pagePutBill.setRecords(list);
    }

    @Override
    public String generatePutBillCode() {
        //上架单编号
        String putBillCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.PUTBILL_CODE_FORMAT);
        //获取最大上架单编号
        String maxPutBillCode = putBillDAO.getMaxPutBillCode();
        if(StringUtils.isEmptyString(maxPutBillCode)){
            putBillCode = "PU00000001";
        }else{
            Integer maxPutBillCode_count = Integer.parseInt(maxPutBillCode.replace("PU",""));
            putBillCode = df.format(maxPutBillCode_count+1);
        }
        return putBillCode;
    }

    @Override
    public List<Map<String, Object>> getSelectInstorageList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return putBillDAO.getSelectInstorageList(page,queryString);
    }

    @Override
    public Integer getSelectInstorageTotal(String queryString) {
        return putBillDAO.getSelectInstorageTotal(queryString);
    }

    @Override
    public List<Map<String, Object>> getSelectOperatorList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return putBillDAO.getSelectOperatorList(page,queryString);
    }

    @Override
    public Integer getSelectOperatorTotal(String queryString) {
        return putBillDAO.getSelectOperatorTotal(queryString);
    }

    @Override
    public Map<String, Object> savePutBill(PutBill putBill) {
        Map<String,Object> map = new HashMap<String,Object>();
        boolean code = false;
        //当前时间
        String nowTime = DateUtils.getTime();
        if(putBill.getId() == null){//新增保存
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            putBill.setCreateBy(user.getUserId());
            putBill.setCreateTime(nowTime);
            putBill.setUpdateTime(nowTime);
            putBill.setState("0");
            //在并发的情况下，防止插入相同的上架单编号，同时插入相同的上架单编号，后插入的上架单编号会比页面上显示的大1
            putBill.setPutBillCode(this.generatePutBillCode());
            if(putBillDAO.insert(putBill) > 0){//插入成功
                code = true;
            }

        }else{//修改保存
            if(putBillDAO.updateById(putBill) > 0){//更新成功
                code = true;
            }

        }
        map.put("code",code);
        //返回插入后的上架单的id
        map.put("putBillId",putBill.getId());
        map.put("putBillCode",putBill.getPutBillCode());
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitPutBill(Long instorageBillId,Long putBillId,String instorageProcess) {
        /**1.更新上架单的状态**/
        putBillDAO.updateState(putBillId);
        /**2.如果是白糖类型，则新建一个白糖绑定单**/
        if("1".equals(instorageProcess)){
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            Long userId = user.getUserId();
            //根据上架单id获取上架详情
            List<PutBillDetail> lstPutBillDetail = putBillDAO.getPutBillDetail(putBillId);
            if(lstPutBillDetail!=null && lstPutBillDetail.size()>0){
                for(PutBillDetail putBillDetail : lstPutBillDetail){
                    String materialCode = putBillDetail.getMaterialCode();
                    String materialName = putBillDetail.getMaterialName();
                    String rfid= putBillDetail.getRfid();
                    String unit = putBillDetail.getUnit();
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
                    materielBindRfid.setStatus("0");
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
                    materielBindRfidDetail.setStatus("0");
                    materielBindRfidDetailDAO.insert(materielBindRfidDetail);
                }
            }

        }

        /**3.更新入库单的可拆分数量和可拆分重量**/
        EntityWrapper<PutBillDetail> putBillDetailEntity = new EntityWrapper<>();
        putBillDetailEntity.eq("put_bill_id",putBillId);
        //获取上架单详情
        List<PutBillDetail> lstPutBillDetail = putBillDetailDAO.selectList(putBillDetailEntity);
        for(PutBillDetail putBillDetail : lstPutBillDetail){
            String materialCode = putBillDetail.getMaterialCode();
            String batchNo = putBillDetail.getBatchNo();
            String putAmount = StringUtils.isBlank(putBillDetail.getPutAmount())?"0":putBillDetail.getPutAmount();
            String putWeight = StringUtils.isBlank(putBillDetail.getPutWeight())?"0":putBillDetail.getPutWeight();
            Map<String,Object> paramsMap = new HashMap();
            paramsMap.put("instorageBillId",instorageBillId);
            paramsMap.put("materialCode",materialCode);
            paramsMap.put("batchNo",batchNo);
            paramsMap.put("putAmount",putAmount);
            paramsMap.put("putWeight",putWeight);
            //根据入库单id、物料编号和批次号更新入库单的可拆分数量和可拆分重量
            putBillDetailDAO.updateSeparableAmountAndWeight(paramsMap);
        }
    }

    @Override
    public Map<String, Object> checkRfidAmountAndWeight(Long instorageBillId,String rfid,String materialCode, Double putAmount, Double putWeight) {
        Map<String,Object> resultMap = new HashMap<>();
        boolean result = false;
        String msg = "";

        //根据上架单id查询上架详情中物料的种类
        List<String> lstInstorageMaterialCode = putBillDetailDAO.getInstorageMaterialCode(instorageBillId);
        //根据rfid查询rfid绑定的物料种类
        List<String> lstRfidMaterialCode = putBillDetailDAO.getRfidMaterialCode(rfid);
        if(lstRfidMaterialCode == null || lstRfidMaterialCode.size() == 0){
            result = false;
            msg = "提交失败！请确认托盘【"+rfid+"】是否绑定了物料";
        }else{
            //判断rfid绑定的物料是否在上架单中都存在
            if(lstInstorageMaterialCode.containsAll(lstRfidMaterialCode)){
                //rfid绑定的物料数量
                Double amount = 0d;
                //rfid绑定的物料重量
                Double weight = 0d;
                //查询物料绑定rfid信息
                Map<String,Object> map = putBillDAO.selectRfidBindDetail(rfid,materialCode);
                if(map != null){
                    amount = map.get("amount")==null?0d:Double.parseDouble(map.get("amount").toString());
                    weight = map.get("weight")==null?0d:Double.parseDouble(map.get("weight").toString());
                }
                if((putAmount-amount)==0 && (putWeight-weight)==0){

                    /**校验上架数量**/
                    InstorageDetail instorageDetail = new InstorageDetail();
                    instorageDetail.setInstorageBillId(instorageBillId);
                    instorageDetail.setMaterialCode(materialCode);
                    instorageDetail = instorageDetailDAO.selectOne(instorageDetail);
                    //可拆分数量
                    Double separableAmount = StringUtils.isBlank(instorageDetail.getSeparableAmount())?0d:Double.parseDouble(instorageDetail.getSeparableAmount());
                    //可拆分重量
                    Double separableWeight = StringUtils.isBlank(instorageDetail.getSeparableWeight())?0d:Double.parseDouble(instorageDetail.getSeparableWeight());
                    //如果上架的数量或重量大于入库详情中的可拆分数量或重量，则不给提交
                    if(putAmount > separableAmount || putWeight > separableWeight){
                        result = false;
                        msg = "提交失败！请确认托盘【"+rfid+"】上物料【"+materialCode+"】的数量或重量是否超过了入库详情单的可拆分数量或重量";
                    }else{
                        result = true;
                    }

                }else{
                    result = false;
                    msg = "提交失败！请确认托盘【"+rfid+"】上物料【"+materialCode+"】的数量或重量是否填写正确";

                }
            }else{
                result = false;
                msg = "提交失败！请确认托盘【"+rfid+"】绑定的物料在上架单中都存在！";
            }
        }
        resultMap.put("result",result);
        resultMap.put("msg",msg);
        return resultMap;
    }

    @Override
    public Map<String, Object> isAvailablePosition(String rfid, String positionCode) {
        Map<String,Object> resultMap = new HashMap<>();
        boolean result = false;
        String msg = "";

        //判断选中的库位上是否有物料，如果有物料，判断是否可以混放，再判断这个库位的容量
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
        depotPositionEntity.eq("position_code",positionCode);
        List<DepotPosition> depotPositionList = depotPositionDAO.selectList(depotPositionEntity);
        if(depotPositionList!=null && depotPositionList.size()>0){
            //库位编号是唯一的，所以取第一条数据
            DepotPosition depotPosition = depotPositionList.get(0);
            blendType = depotPosition.getBlendType();
            classify = depotPosition.getClassify();
            capacityRfidAmount = depotPosition.getCapacityRfidAmount()==null?0:Integer.parseInt(depotPosition.getCapacityRfidAmount());
            capacityWeight = depotPosition.getCapacityWeight()==null?0d:Double.parseDouble(depotPosition.getCapacityWeight());
            positionFrozen = depotPosition.getPositionFrozen();
        }

        if("1".equals(positionFrozen)){
            result = false;
            msg = "该库位已冻结，不能上架，请确认";
            resultMap.put("result",result);
            resultMap.put("msg",msg);
            return resultMap;
        }

        //获取rfid绑定的物料信息
        List<Map<String, Object>> materialBundingList = movePositionSlabDAO.getMaterialBundingDetail(rfid);
        if("1".equals(blendType)){//如果是不混放类型的库位
            if(materialBundingList!=null &&  materialBundingList.size()>0){
                if(materialBundingList.size()==1){//这边表示：如果rfid只绑定了一种物料
                    Map<String, Object> materialBundingMap = materialBundingList.get(0);
                    //托盘绑定的物料编号
                    String materialCode = materialBundingMap.get("materiel_code")==null?"":materialBundingMap.get("materiel_code").toString();
                    //托盘绑定的物料重量
                    Double rfidWeight = materialBundingMap.get("weight")==null?0d:Double.parseDouble(materialBundingMap.get("weight").toString());

                    //根据物料编号获取物料的“ABC”分类
                    String materialClassify = "";
                    EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
                    materialEntity.eq("materiel_code",materialCode);
                    materialEntity.eq("deleted_flag","1");
                    List<Materiel> materialList = materielDAO.selectList(materialEntity);
                    if(materialList!=null && materialList.size()>0){
                        //物料编号是唯一的，所以取第一条
                        Materiel material = materialList.get(0);
                        materialClassify = material.getUpshelfClassify()==null?"":material.getUpshelfClassify();
                    }

                    if(StringUtils.isBlank(materialClassify) || classify.equals(materialClassify)){//判断物料的“ABC分类”是否和库位的分类一致
                        //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
                        Map<String,Object> stockMap = movePositionSlabDAO.getStockInfo(positionCode);
                        if(stockMap != null){//如果有物料
                            //库存中的物料编号
                            String stockMaterialCode = stockMap.get("material_code").toString();
                            if(stockMaterialCode.equals(materialCode)){
                                //库存中的物料重量
                                Double stockWeight = stockMap.get("stock_weight_sum")==null?0d:Double.parseDouble(stockMap.get("stock_weight_sum").toString());
                                //库存中的托盘库存数量
                                Double stockRfidAmount = stockMap.get("stock_rfid_amount_sum")==null?0d:Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());
                                //库位的剩余重量容量
                                Double surplusCapacityWeight = capacityWeight-stockWeight;
                                //库位的剩余托盘库存数量容量
                                Double surplusCapacityStockRfidAmount = capacityRfidAmount-stockRfidAmount;

                                //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                                if(surplusCapacityWeight >= rfidWeight && surplusCapacityStockRfidAmount >= 1){
                                    result = true;
                                    msg = "该库位可以上架";
                                }else{
                                    result = false;
                                    msg = "该库位容量不够，请确认";
                                }
                            }else{
                                result = false;
                                msg = "该库位不可混放，请确认上架的物料与库位上的物料是否一致";
                            }
                        }else{//如果没有物料，表示空库位

                            //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                            if(capacityWeight >= rfidWeight && capacityRfidAmount >= 1){
                                result = true;
                                msg = "该库位可以上架";
                            }else{
                                result = false;
                                msg = "该库位容量不够，请确认";
                            }
                        }
                    }else{
                        result = false;
                        msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
                    }
                }else{//如果rfid绑定了多种物料，则不能入“不混放”类型的库位
                    result = false;
                    msg = "该库位不可混放，请确认托盘中是否有多种物料";
                }

            }else{//如果rfid绑定的物料没有，表示是白糖类型的，不做校验
                result = true;
                msg = "该库位可以上架";
            }
        }else{//如果是混放类型的库位，
            if(materialBundingList!=null && materialBundingList.size()>0){
                //根据库位编号分组，获取库存表中该库位上的所有物料种类的物料重量和托盘库存数量
                Map<String,Object> stockMap = movePositionSlabDAO.getStockInfo(positionCode);

                //库位的剩余重量容量
                Double surplusCapacityWeight = 0d;
                //库位的剩余托盘库存数量容量
                Double surplusCapacityStockRfidAmount = 0d;

                if(stockMap!=null){//如果有物料

                    //库存中该库位总的物料数量
                    Double stockWeightTotal = stockMap.get("stock_weight_sum")==null?0d:Double.parseDouble(stockMap.get("stock_weight_sum").toString());
                    //库存中该库位总的托盘库存数量
                    Double stockRfidAmountTotal = stockMap.get("stock_rfid_amount_sum")==null?0d:Double.parseDouble(stockMap.get("stock_rfid_amount_sum").toString());

                    surplusCapacityWeight = capacityWeight-stockWeightTotal;
                    surplusCapacityStockRfidAmount = capacityRfidAmount-stockRfidAmountTotal;

                }else{//如果没有物料，表示空库位
                    surplusCapacityWeight = capacityWeight;
                    surplusCapacityStockRfidAmount = Double.parseDouble(capacityRfidAmount.toString());
                }

                //该托盘上物料的“ABC”分类集合
                List<String> materialClassifyList = new ArrayList<>();
                //托盘中总的物料重量
                Double rfidWeightTotal = 0d;
                for(Map<String, Object> materialBundingMap : materialBundingList){
                    //托盘绑定的物料编号
                    String materialCode = materialBundingMap.get("materiel_code")==null?"":materialBundingMap.get("materiel_code").toString();
                    //托盘绑定的物料重量
                    Double rfidWeight = materialBundingMap.get("weight")==null?0d:Double.parseDouble(materialBundingMap.get("weight").toString());

                    rfidWeightTotal += rfidWeight;

                    //根据物料编号获取物料的“ABC”分类
                    String materialClassify = "";
                    EntityWrapper<Materiel> materialEntity = new EntityWrapper<Materiel>();
                    materialEntity.eq("materiel_code",materialCode);
                    materialEntity.eq("deleted_flag","1");
                    List<Materiel> materialList = materielDAO.selectList(materialEntity);
                    if(materialList!=null && materialList.size()>0){
                        //物料编号是唯一的，所以取第一条
                        Materiel material = materialList.get(0);
                        materialClassify = material.getUpshelfClassify()==null?"":material.getUpshelfClassify();
                    }
                    materialClassifyList.add(materialClassify);
                }


                /**判断“ABC分类”是否符合=====start====**/
                //定义该托盘物料“ABC分类”属性是否与库位“ABC分类”属性符合的字段
                boolean isAccord = false;

                //分类规则：托盘中有物料AB类则放A货架，AC放A货架，BC放B货架，A和不填放A货架，B和不填放B货架，C和不填放C货架
                //去除集合中的空字符串
                List<String> materialClassifyList1=materialClassifyList.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
                if(materialClassifyList1!=null && materialClassifyList1.size()>0){
                    if(materialClassifyList1.size()>1){
                        //再去除集合中的重复元素
                        List<String> materialClassifyList2 = new ArrayList<String>(new HashSet<String>(materialClassifyList1));
                        if(materialClassifyList2.size()>1){//如果再去除重复元素后托盘中的所有物料有多种“ABC分类”的属性
                            //这边只有三种情况：AB，AC，BC,所以可放的库位类型不是A就是B
                            if(materialClassifyList2.contains("A")){
                                if("A".equals(classify)){
                                    isAccord = true;
                                }else{
                                    isAccord = false;
                                }
                            }else{
                                if("B".equals(classify)){
                                    isAccord = true;
                                }else{
                                    isAccord = false;
                                }
                            }
                        }else{//如果再去除重复元素后托盘中的所有物料只有一种“ABC分类”的属性,则放该种分类的库位
                            if(materialClassifyList2.contains(classify)){
                                isAccord = true;
                            }else{
                                isAccord = false;
                            }
                        }

                    }else{//如果去除空字符串后托盘中的所有物料只有一种“ABC分类”的属性，则可以放任意类型库位
                        isAccord = true;
                    }

                }else{//该种情况表示托盘中的所有物料都没有填写“ABC分类”属性，则可以放任意类型库位
                    isAccord = true;
                }

                /**判断“ABC分类”是否符合=====end====**/

                if(isAccord){//如果库位类型符合
                    //如果库位的剩余重量容量大于等于托盘绑定的物料重量，并且库位的剩余托盘库存数量容量大于等于1
                    if(surplusCapacityWeight >= rfidWeightTotal && surplusCapacityStockRfidAmount >= 1){
                        result = true;
                        msg = "该库位可以上架";
                    }else{
                        result = false;
                        msg = "该库位容量不够，请确认";
                    }
                }else{
                    result = false;
                    msg = "该库位的‘ABC’分类与物料的分类不一致，请确认";
                }

            }else{//如果rfid绑定的物料没有，表示是白糖类型的，不做校验
                result = true;
                msg = "该库位可以上架";
            }
        }
        resultMap.put("result",result);
        resultMap.put("msg",msg);
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePutBill(Long id) {
        /**1.删除入库单**/
        putBillDAO.deleteById(id);
        /**2.删除入库单详情**/
        EntityWrapper<PutBillDetail> putBillDetailEntity = new EntityWrapper<>();
        putBillDetailEntity.eq("put_bill_id",id);
        putBillDetailDAO.delete(putBillDetailEntity);
    }

    /**
     * 获取导出列
     *
     * @return List<Customer>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<PutBill> getAllList(String ids) {
        System.out.println(ids);
        List<PutBill> list = null;
        if(ids.equals("")||ids==null) {
            EntityWrapper<PutBill> wraPutBill = new EntityWrapper<>();
            list = this.selectList(wraPutBill);
        }else{
            list = putBillDAO.getAllLists(StringUtils.stringToList(ids));
            list.forEach(a->{

            });
        }

        return list;
    }

    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<PutBill> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "上架单管理表" + "(" + date + ")";
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
            mapFields.put("putBillCode", "上架单编号");
            mapFields.put("instorageBillCode", "入库单编号");
            mapFields.put("operatorName", "操作人");
            mapFields.put("createTime", "创建时间");
            mapFields.put("stateContent", "状态");
            //对上架单字段进行转译
            List putBillList = new ArrayList();
            for(PutBill is:list){
                String state = is.getState();
                //状态（0：未提交；1：待上架；2：上架中；3：上架完成）
                switch (state){
                    case "0":is.setStateContent("未提交");break;
                    case "1":is.setStateContent("待上架");break;
                    case "2":is.setStateContent("上架中");break;
                    case "3":is.setStateContent("上架完成");break;
                    default:is.setStateContent("");
                }
                putBillList.add(is);
            }
            DeriveExcel.exportExcel(sheetName, putBillList, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    