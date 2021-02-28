package com.tbl.modules.slab.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.IPUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.dao.MovePositionSlabDAO;
import com.tbl.modules.slab.service.InstorageSlabService;
import com.tbl.modules.slab.service.MovePositionSlabService;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MovePositionDAO;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MovePosition;
import com.tbl.modules.stock.entity.StockChange;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 平板移位service实现类
 * @author: zj
 * @create: 2019-03-06 15:13
 **/
@Service("movePositionSlabService")
public class MovePositionSlabServiceImpl implements MovePositionSlabService {

    @Autowired
    private MovePositionSlabDAO movePositionSlabDAO;
    @Autowired
    private StockDAO stockDAO;
    @Autowired
    private DepotPositionDAO depotPositionDAO;
    @Autowired
    private MaterielDAO materielDAO;
    @Autowired
    private StockChangeDAO stockChangeDAO;
    @Autowired
    private InstorageSlabService instorageSlabService;
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;
    @Autowired
    private MovePositionDAO movePositionDAO;

    @Override
    public Page<MovePosition> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<MovePosition> pageMovePosition = new Page<MovePosition>(pg,rows,sortname,order);

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        params.put("userId",userId);
        //获取上架单列表
        List<MovePosition> list = movePositionSlabDAO.getPageMovePositionList(pageMovePosition, params);

        return pageMovePosition.setRecords(list);
    }

    @Override
    public void insertOrUpdateSlabMovePositionBunding(HttpServletRequest request) {
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        //1.根据ip查询叉车正在操作单据的类型表中是否存在数据
        Integer count1 = movePositionSlabDAO.selectOperateTypeCount(userIP);
        Map<String,Object> paramMap1 = new HashMap<String,Object>();
        paramMap1.put("userIP",userIP);
        if(count1 == 0){//插入
            movePositionSlabDAO.insertSlabOperateType(paramMap1);
        }else{//更新
            movePositionSlabDAO.updateSlabOperateType(paramMap1);
        }
        //2.根据ip查询平板移库操作参数绑定关系表中是否存在数据
        Integer count2 = movePositionSlabDAO.selectCountByUserIDAndIp(userIP);
        Map<String,Object> paramMap2 = new HashMap<String,Object>();
        paramMap2.put("userId",userId);
        paramMap2.put("userIP",userIP);
        if(count2 == 0){//插入
            movePositionSlabDAO.insertSlabMovePositionBunding(paramMap2);
        }else{//更新
            movePositionSlabDAO.updateSlabMovePositionBunding(paramMap2);
        }
    }

    @Override
    public void updateMovePositionState(Long movePositionId) {
        movePositionSlabDAO.updateMovePositionState(movePositionId);
    }

    @Override
    public Map<String, Object> getSlabMovePositionBunding(String userIP) {
        return movePositionSlabDAO.getSlabMovePositionBunding(userIP);
    }

    @Override
    public List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return movePositionSlabDAO.getSelectPositionList(page,queryString);
    }

    @Override
    public Integer getSelectPositionTotal(String queryString) {
        return movePositionSlabDAO.getSelectPositionTotal(queryString);
    }

    @Override
    public void updateSlabMovePositionBundingPositionCode(Long movePositionId,String userIP, String positionCode) {
        movePositionSlabDAO.updateSlabMovePositionBundingPositionCode(movePositionId,userIP,positionCode);
    }

    @Override
    public void updateSlabMovePositionBundingRfid(Map<String, Object> paramMap) {
        movePositionSlabDAO.updateSlabMovePositionBundingRfid(paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void slabDownRfid(String ip) {
        Map<String, Object> map = movePositionSlabDAO.getSlabMovePositionBunding(ip);
        //当前登陆人id
        Long userId = 0l;
        //当前登陆人正在执行的移库单id
        String movePositionId = "0";
        //当前登陆人正在移库的托盘rfid
        String rfid = "";
        //当前登陆人需要移库的库位编码
        String positionCode = "";
        //平板页面交互用的参数字段key值
        String alertKey = "";

        if(map != null){
            userId = map.get("user_id")==null?0l:Long.parseLong(map.get("user_id").toString());
            movePositionId = map.get("move_position_id")==null?"0":map.get("move_position_id").toString();
            rfid = map.get("rfid")==null?"":map.get("rfid").toString();
            positionCode = map.get("position_code")==null?"":map.get("position_code").toString();
            alertKey = map.get("alert_key")==null?"":map.get("alert_key").toString();
        }
        //如果是没有确定库位的rfid，则不走逻辑
        if(!"3".equals(alertKey)){
            return;
        }

        /**2.更新移库表的库位编码和其他字段**/
        //库位id
        Long positionId = 0l;
        //库位名称
        String positionName="";

        EntityWrapper<DepotPosition> depotPositionEntity = new EntityWrapper<DepotPosition>();
        depotPositionEntity.eq("position_code",positionCode);
        List<DepotPosition> lstDepotPosition = depotPositionDAO.selectList(depotPositionEntity);
        if(lstDepotPosition!=null && lstDepotPosition.size()>0){
            //数据是唯一的，取第一条
            DepotPosition depotPosition = lstDepotPosition.get(0);
            positionId = depotPosition.getId();
            positionName = depotPosition.getPositionName();
        }
        //更新移库表
        movePositionSlabDAO.updateMovePositionById(movePositionId);

        /**3.更新物料绑定rfid表库位id和详情表库位id**/
        //根据移库表的id查询原库位主键id和库位编号
        Map<String,Object> formerPositionIdMap = movePositionSlabDAO.selectFormerPosition(movePositionId);
        //原库位编码
        String formerPositionCode = "";
        if(formerPositionIdMap!=null){
            formerPositionCode = formerPositionIdMap.get("position_code")==null?"":formerPositionIdMap.get("position_code").toString();
        }

        //绑定表id
        String materielBindId = "";
        //绑定详情表id集合
        List<String> lstMaterielBindDetailId = new ArrayList<>();
        List<Map<String,Object>> lstMaterialBunding = movePositionSlabDAO.selectMaterialBundingByRfid(rfid);
        if(lstMaterialBunding!=null && lstMaterialBunding.size()>0){
            for(Map<String,Object> materialBundingMap :lstMaterialBunding){
                materielBindId = materialBundingMap.get("materielBindId")==null?"":materialBundingMap.get("materielBindId").toString();
                String materielBindDetailId = materialBundingMap.get("materielBindDetailId")==null?"":materialBundingMap.get("materielBindDetailId").toString();
                lstMaterielBindDetailId.add(materielBindDetailId);

                /**4.插入库存变动表(“移位-入库”类型的)**/
                //绑定单号
                String bindCode = materialBundingMap.get("bind_code")==null?"":materialBundingMap.get("bind_code").toString();
                //物料编号
                String materialCode = materialBundingMap.get("materiel_code")==null?"":materialBundingMap.get("materiel_code").toString();
                //物料名称
                String materialName = materialBundingMap.get("materiel_name")==null?"":materialBundingMap.get("materiel_name").toString();
                //批次号
                String batchNo = materialBundingMap.get("batch_rule")==null?"":materialBundingMap.get("batch_rule").toString();
                //数量
                String amount = materialBundingMap.get("amount")==null?"":materialBundingMap.get("amount").toString();
                //重量
                String weight = materialBundingMap.get("weight")==null?"":materialBundingMap.get("weight").toString();
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
                stockChangeDAO.insert(stockChange);

                /**5.更新库存表**/
                /**（1）减去原库位的库存**/
                Map<String,Object> paramMap= new HashMap<>();
                paramMap.put("materialCode",materialCode);
                paramMap.put("batchNo",batchNo);
                paramMap.put("formerPositionCode",formerPositionCode);
                paramMap.put("amount",amount);
                paramMap.put("weight",weight);
                paramMap.put("rfid",rfid);
                movePositionSlabDAO.updateStockFormerPosition(paramMap);
                /**如果原库位的库存量为0，则将库存表中的数据删除**/
                //查询库存数量和重量
                Map<String,Object> stockMap = movePositionSlabDAO.selectStockAmountAndWeight(materialCode,batchNo,formerPositionCode);
                //生产日期
                String productDate = "";
                //货主编号
                String customerCode = "";
                if(stockMap != null){
                    Double stockAmount = stockMap.get("stock_amount")==null?0d:Double.parseDouble(stockMap.get("stock_amount").toString());
                    Double stockWeight = stockMap.get("stock_weight")==null?0d:Double.parseDouble(stockMap.get("stock_weight").toString());
                    if(stockAmount == 0 && stockWeight == 0){
                        //根据物料编号，批次号，库位编号删除该条数据
                        movePositionSlabDAO.deleteStock(materialCode,batchNo,formerPositionCode);
                    }
                    productDate = stockMap.get("product_date").toString();
                    customerCode = stockMap.get("customer_code").toString();
                }

                /**（2）加上现库位的库存**/
                //先根据物料编号、批次号、库位编号查询库存中是否存在该条数据
                Integer count2 = movePositionSlabDAO.selectStockPositionCount(materialCode,batchNo,positionCode);
                if(count2>0){//更新现库位的库存
                    Map<String,Object> paramMap1= new HashMap<>();
                    paramMap1.put("materialCode",materialCode);
                    paramMap1.put("batchNo",batchNo);
                    paramMap1.put("positionCode",positionCode);
                    paramMap1.put("amount",amount);
                    paramMap1.put("weight",weight);
                    paramMap1.put("rfid",rfid);
                    movePositionSlabDAO.updateStockCurrentPosition(paramMap1);
                }else{//插入现库位的库存
                    Map<String,Object> paramMap1 = new HashMap<>();
                    paramMap1.put("materialCode",materialCode);
                    paramMap1.put("materialName",materialName);
                    paramMap1.put("batchNo",batchNo);
                    paramMap1.put("positionCode",positionCode);
                    paramMap1.put("positionName",positionName);
                    paramMap1.put("amount",amount);
                    paramMap1.put("weight",weight);
                    paramMap1.put("rfid",rfid);
                    paramMap1.put("userId",userId);
                    paramMap1.put("productDate",productDate);
                    paramMap1.put("customerCode",customerCode);
                    movePositionSlabDAO.insertStockCurrentPosition(paramMap1);
                }
            }
        }
        //更新绑定表库位id
        movePositionSlabDAO.updateMaterialBundingPosition(materielBindId,positionId);
        //更新绑定详情表库位id
        movePositionSlabDAO.updateMaterialBundingDetailPosition(lstMaterielBindDetailId,positionId);

        //清除绑定关系表的相关字段
        movePositionSlabDAO.updateAlert1(ip);
    }

    @Override
    public boolean isBundingSugar(String rfid) {
        boolean result = false;
        //（1）如果是白糖类型的rfid，判断是否做过绑定，如果没做过绑定则不给移库
        //判断没做过绑定的白糖类型的rfid：该rfid只绑定了一种物料，并且如果该绑定的rfid是“已入库”状态，并且数量和重量都为0或空
        List<Map<String, Object>> materialList = movePositionSlabDAO.getMaterialBundingDetail(rfid);
        if(materialList!=null && materialList.size()==1){
            Map<String, Object> materialMap = materialList.get(0);
            //绑定的数量
            Double amount = materialMap.get("amount")==null||"".equals(materialMap.get("amount").toString().trim())?0d:Double.parseDouble(materialMap.get("amount").toString());
            //绑定的重量
            Double weight = materialMap.get("weight")==null||"".equals(materialMap.get("weight").toString().trim())?0d:Double.parseDouble(materialMap.get("weight").toString());
            if(amount==0 && weight==0){
                result = true;
            }else{
                result = false;
            }

        }else{
            result = false;
        }

        return result;
    }

    @Override
    public Map<String,Object> isAvailablePosition(String userIP, String positionCode) {
        Map<String,Object> resultMap = new HashMap<>();
        boolean result = false;
        String msg = "";

        //1.获取rfid
        String rfid = "";
        Map<String, Object> map = movePositionSlabDAO.getSlabMovePositionBunding(userIP);
        if(map != null){
            rfid = map.get("rfid")==null?"":map.get("rfid").toString();
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
            msg = "该库位已冻结，不能移位，请确认";
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

    @Override
    public Map<String, Object> selectFormerPosition(String movePositionId) {
        return movePositionSlabDAO.selectFormerPosition(movePositionId);
    }

    @Override
    public Integer getSelectAvailableRfid(String materialCode1, String batchNo1, String formerpositionCode,String rfid) {
        return movePositionSlabDAO.getSelectAvailableRfid(materialCode1,batchNo1,formerpositionCode,rfid);
    }

    @Override
    public Map<String, Object> slabUpRfid(String IP, String rfid) {
        Map<String,Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        String alertKey = "";
        String alertValue = "";
        //需要移库的rfid
        String movePositionRfid = "";
        try{
            if(StringUtils.isBlank(IP) || StringUtils.isBlank(rfid)){
                map.put("result",false);
                map.put("msg","调用接口失败，请确认参数是否有效");
                return map;
            }
            //根据ip获取该叉车绑定信息，IP是平板服务端传过来的平板ip地址，在该表中是唯一的
            Map<String,Object> map1 = movePositionSlabDAO.getSlabMovePositionBunding(IP);
            Long userId = 0l;
            String rfidBind = "";
            if(map1!=null){
                userId = map.get("user_id")==null?0l:Long.parseLong(map.get("user_id").toString());
                rfidBind = map.get("rfid")==null?"":map.get("rfid").toString();
            }
            if(StringUtils.isNotBlank(rfidBind)){//rfidBind，则表示叉车叉取了两个托盘rfid
                alertKey = "0";
                alertValue = "移库不能叉取两个托盘，请确认";
            }else{
                //判断rfid是否在库状态
                MaterielBindRfid materielBindRfid = new MaterielBindRfid();
                materielBindRfid.setRfid(rfid);
                materielBindRfid.setDeletedFlag("1");
                materielBindRfid = materielBindRfidDAO.selectOne(materielBindRfid);
                //状态（0未入库，1已入库）
                String rfidStatus = materielBindRfid.getStatus();
                if(!"1".equals(rfidStatus)){
                    alertKey = "0";
                    alertValue = "该托盘不是在库状态，请确认";
                }else{
                    //根据rfid查询绑定的物料
                    List<Map<String,Object>> rfidDetailList = instorageSlabService.getMaterialBundingDetail(rfid);
                    if(rfidDetailList!=null && rfidDetailList.size()>0){
                        //如果是白糖类型的rfid，判断是否做过绑定，如果没做过绑定则不给移库
                        //判断没做过绑定的白糖类型的rfid：该rfid只绑定了一种物料，并且如果该绑定的rfid是“已入库”状态，并且数量和重量都为0或空
                        boolean b = isBundingSugar(rfid);
                        if(b){
                            alertKey = "0";
                            alertValue = "该托盘绑定的物料是未做过绑定的白糖类型的物料，不能移库，请确认";
                        }else{
                            //该rfid上所有物料占用的库存数量总数
                            Double occupyStockAmountTotal = 0d;
                            //该rfid上所有物料占用的库存重量总数
                            Double occupyStockWeightTotal = 0d;

                            //物料编号(如果该托盘绑定多钟物料，只要取其中一种物料)
                            String materialCode1 = "";
                            //批次号(如果该托盘绑定多钟物料，只要取其中一种物料的批次号)
                            String batchNo1 = "";
                            for(Map<String,Object> map2 : rfidDetailList){
                                occupyStockAmountTotal += map2.get("occupy_stock_amount")==null||"".equals(map2.get("occupy_stock_amount").toString())?0d:Double.parseDouble(map2.get("occupy_stock_amount").toString());
                                occupyStockWeightTotal += map2.get("occupy_stock_weight")==null||"".equals(map2.get("occupy_stock_weight").toString())?0d:Double.parseDouble(map2.get("occupy_stock_weight").toString());

                                materialCode1 = map2.get("materiel_code")==null?"":map2.get("materiel_code").toString();
                                batchNo1 = map2.get("batch_rule")==null?"":map2.get("batch_rule").toString();
                            }
                            if(occupyStockAmountTotal>0 || occupyStockWeightTotal>0){
                                alertKey = "0";
                                alertValue = "该托盘中有物料已被占用，不能移位，请确认";
                            }else{
                                /**验证该rfid托盘是否可用**/
                                //获取rfid对应的库位
                                DepotPosition depotPosition = depotPositionDAO.selectById(materielBindRfid.getPositionBy());
                                String formerpositionCode = depotPosition==null?"":depotPosition.getPositionCode();
                                Integer count = getSelectAvailableRfid(materialCode1,batchNo1,formerpositionCode,rfid);
                                if(count > 0){
                                    alertKey = "1";
                                    alertValue = "验证通过";
                                    /**插入库存变动表(“移位-出库”类型的)**/
                                    for(Map<String,Object> map2 : rfidDetailList){
                                        //库位id主键
                                        Long positionId = map2.get("position_id")==null?0l:Long.parseLong(map2.get("position_id").toString());
                                        //绑定单号
                                        String bindCode = map2.get("bind_code")==null?"":map2.get("bind_code").toString();
                                        //物料编号
                                        String materialCode = map2.get("materiel_code")==null?"":map2.get("materiel_code").toString();
                                        //物料名称
                                        String materialName = map2.get("materiel_name")==null?"":map2.get("materiel_name").toString();
                                        //批次号
                                        String batchNo = map2.get("batch_rule")==null?"":map2.get("batch_rule").toString();
                                        //数量
                                        String amount = map2.get("amount")==null?"":map2.get("amount").toString();
                                        //重量
                                        String weight = map2.get("weight")==null?"":map2.get("weight").toString();
                                        StockChange stockChange = new StockChange();
                                        stockChange.setChangeCode(bindCode);
                                        stockChange.setMaterialCode(materialCode);
                                        stockChange.setMaterialName(materialName);
                                        stockChange.setBatchNo(batchNo);
                                        //设置“移位-出库”类型
                                        stockChange.setBusinessType("2");
                                        stockChange.setInAmount(amount);
                                        stockChange.setOutAmount(amount);
                                        stockChange.setInWeight(weight);
                                        stockChange.setOutWeight(weight);
                                        stockChange.setPositionBy(positionId);
                                        stockChange.setCreateTime(DateUtils.getTime());
                                        stockChange.setCreateBy(userId);
                                        stockChangeDAO.insert(stockChange);
                                    }

                                }else{
                                    alertKey = "0";
                                    alertValue = "该托盘未通过质检，不可用，请确认";
                                }
                            }
                        }
                    }else{
                        alertKey = "0";
                        alertValue = "该托盘没绑定物料，请确认！";
                    }
                }
            }

            //更新平板参数绑定表的rfid和验证字段
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("userIP",IP);
            paramMap.put("rfid",rfid);
            paramMap.put("alertKey",alertKey);
            paramMap.put("alertValue",alertValue);
            updateSlabMovePositionBundingRfid(paramMap);
            result = true;
            msg = "接口执行成功";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "接口执行失败";
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
    }

    @Override
    public Map<String, Object> getExecuteRfid(String userIP) {
        return movePositionSlabDAO.getExecuteRfid(userIP);
    }

    @Override
    public String selectPositionCodeByRfid(String rfid) {
        return movePositionSlabDAO.selectPositionCodeByRfid(rfid);
    }

    @Override
    public void updateAlert(HttpServletRequest request, String flag) {
        //当前登陆的ip
        String userIP = IPUtils.getIpAddr(request);
        if("Y".equals(flag)){
            movePositionSlabDAO.updateAlert2(userIP);
        }else if("N".equals(flag)){
            movePositionSlabDAO.updateAlert1(userIP);
        }
    }

    @Override
    public Long insertMovePosition(Long userId, String userIP, Long positionId) {
        Long movePositionId = 0l;
        //根据userIP获取rfid
        Map<String, Object> map1 = movePositionSlabDAO.getSlabMovePositionBunding(userIP);
        if(map1 != null){
            String rfid = map1.get("rfid")==null?"":map1.get("rfid").toString();
            //根据rfid查询绑定编号和移出库位id
            MaterielBindRfid materielBindRfid = new MaterielBindRfid();
            materielBindRfid.setRfid(rfid);
            materielBindRfid.setDeletedFlag("1");
            materielBindRfid.setStatus("1");
            materielBindRfid = materielBindRfidDAO.selectOne(materielBindRfid);
            //绑定编号
            String bindCode = materielBindRfid.getBindCode();
            //移出库位id
            Long outPositionId= materielBindRfid.getPositionBy();

            //插入移库单
            MovePosition movePosition = new MovePosition();
            movePosition.setMovePositionCode(bindCode);
            movePosition.setRfid(rfid);
            movePosition.setMoveUserId(userId);
            movePosition.setFormerPosition(outPositionId);
            movePosition.setPositionBy(positionId);
            movePosition.setMoveFoundTime(DateUtils.getTime());
            movePosition.setMovePositionTime(DateUtils.getTime());
            movePosition.setStatus("1");
            movePosition.setRemarks("叉车移位");
            movePosition.setDeleteFlag("1");
            movePositionDAO.insert(movePosition);
            movePositionId = movePosition.getId();
        }

        return movePositionId;
    }
}
    