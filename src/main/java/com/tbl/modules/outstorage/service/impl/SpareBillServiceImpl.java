package com.tbl.modules.outstorage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.dao.OutStorageDetailDAO;
import com.tbl.modules.outstorage.dao.SpareBillDAO;
import com.tbl.modules.outstorage.dao.SpareBillDetailDAO;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.util.DeriveExcel;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.Stock;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lcg
 * data 2019/1/15
 */
@Service("sparebillService")
public class SpareBillServiceImpl extends ServiceImpl<SpareBillDAO, SpareBillManagerVO> implements SpareBillService {

    @Autowired
    private SpareBillDAO spareBillDAO;

    @Autowired
    private OutStorageDAO outStorageDAO;

    @Autowired
    private OutStorageDetailDAO outStorageDetailDAO;

    @Autowired
    private SpareBillDetailDAO spareBillDetailDAO;

    @Autowired
    private MaterielDAO materielDAO;

    @Autowired
    private MaterielService materielService;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private CustomerService customerService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SpareBillManagerVO> page = this.selectPage(
                new Query<SpareBillManagerVO>(params).getPage(),
                new EntityWrapper<SpareBillManagerVO>()
        );
        return new PageUtils(page.setRecords(spareBillDAO.selectSpareBillList(page, params)));
    }

    /**
     * 通过id将对应的备料单删除
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> deleteSpareList(String ids, String spareBillCode) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "";
        String[] idList = ids.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(idList));
        boolean result = false;
        //判断对应的备料单是否已经生成了出库单,如果已经生成了出库单,则不进行删除
        Integer count = spareBillDAO.isExist(spareBillCode);
        if (count > 0) {
            msg = "其中有备料单已经生成出库单,不能进行删除";
        } else {
            Integer resuCount = spareBillDAO.deleteBatchIds(list);
            if (resuCount > 0) {
                result = true;
                msg = "删除成功";
            } else {
                result = false;
                msg = "删除失败";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 生成出库单
     *
     * @param spareBillManagerVO
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createOutStorage(SpareBillManagerVO spareBillManagerVO, List<SpareBillDetailManagerVO> list, String outStorageCode) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "";
        boolean result = false;
        OutStorageManagerVO outStorageManagerVO = new OutStorageManagerVO();
        //插入到出库单中
        outStorageManagerVO.setOutstorageBillCode(outStorageCode);
        outStorageManagerVO.setSpareBillCode(spareBillManagerVO.getSpareBillCode());
        outStorageManagerVO.setOutstorageBillType(0L);
        outStorageManagerVO.setState("0");
        String nowTime = DateUtils.getTime();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        outStorageManagerVO.setCreateBy(user.getUserId());
        outStorageManagerVO.setCreateTime(nowTime);
        Integer id = null;
        id = outStorageDAO.insert(outStorageManagerVO);
        if (id > 0) {
            Long outStorageId = outStorageManagerVO.getId();
            //对备料单的详情进行处理
            for (SpareBillDetailManagerVO spareBillDetailManagerVO : list) {
                OutStorageDetailManagerVO outStorageDetailManagerVO = new OutStorageDetailManagerVO();
                Materiel materiel = new Materiel();
                materiel.setMaterielCode(spareBillDetailManagerVO.getMaterialCode().trim());
                materiel = materielDAO.selectOne(materiel);
                //通过物料编号查询对应的物料的单位
                Object unit = outStorageDetailDAO.getUnit(spareBillDetailManagerVO.getMaterialCode().trim());
                if (unit != null) {
                    outStorageDetailManagerVO.setUnit(unit.toString());
                } else {
                    outStorageDetailManagerVO.setUnit("");
                }
                //查询库存中的数量
                outStorageDetailManagerVO.setMaterialName(materiel.getMaterielName());
                outStorageDetailManagerVO.setMaterialCode(spareBillDetailManagerVO.getMaterialCode().trim());
                outStorageDetailManagerVO.setWeight(spareBillDetailManagerVO.getSendAmount());
                outStorageDetailManagerVO.setOutstorageBillId(outStorageId.toString());
                id = outStorageDetailDAO.insert(outStorageDetailManagerVO);
                if (id > 0) {
                    result = true;
                    msg = "生成成功";
                } else {
                    msg = "生成失败";
                    //事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            //更新备料单的状态为已经生成出库单
            spareBillManagerVO.setState("1");
            spareBillDAO.updateById(spareBillManagerVO);
        } else {
            msg = "生成失败";
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 备料单主表的保存
     *
     * @param spareBillJsonString
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveSpareBill(String spareBillJsonString, String json) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "请添加备料详情";
        boolean result = false;
        Integer id = null;
        JSONArray jsonArray = JSONArray.parseArray(spareBillJsonString);
        JSONArray jsonArray1 = JSONArray.parseArray(json);
        for (Object spareBill : jsonArray) {
            JSONObject jsonObject = JSONObject.parseObject(spareBill.toString());
            SpareBillManagerVO spareBillManagerVO = new SpareBillManagerVO();
            spareBillManagerVO.setUserStartTime(jsonObject.get("user_start_time") == null ? "" : jsonObject.get("user_start_time").toString());
            spareBillManagerVO.setProductName(jsonObject.get("product_name") == null ? "" : jsonObject.get("product_name").toString());
            spareBillManagerVO.setTotalProductAmount(jsonObject.get("total_product_amount") == null ? "" : jsonObject.get("total_product_amount").toString());
            spareBillManagerVO.setSimplexFeedAmount(jsonObject.get("simplex_feed_amount") == null ? "" : jsonObject.get("simplex_feed_amount").toString());
            spareBillManagerVO.setCylinderFeedAmount(jsonObject.get("cylinder_feed_amount") == null ? "" : jsonObject.get("cylinder_feed_amount").toString());
            spareBillManagerVO.setMixUseLine(jsonObject.get("mix_use_line") == null ? "" : jsonObject.get("mix_use_line").toString());
            spareBillManagerVO.setSpecialMatter(jsonObject.get("special_matter") == null ? "" : jsonObject.get("special_matter").toString());
            String nowTime = DateUtils.getTime();
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            spareBillManagerVO.setCreateBy(user.getUserId());
            spareBillManagerVO.setCreateTime(nowTime);
            if (!Strings.isNullOrEmpty(jsonObject.get("id").toString())) {
                //表示的是编辑
                spareBillManagerVO.setId(Long.parseLong(jsonObject.get("id").toString()));
                id = spareBillDAO.updateById(spareBillManagerVO);
            } else {
                spareBillManagerVO.setSpareBillCode(this.getMaxBillCode());
                //表示的添加
                id = spareBillDAO.insert(spareBillManagerVO);
            }
            if (id > 0) {
                Long spareBillId = spareBillManagerVO.getId();
                SpareBillDetailManagerVO spareBillDetailManagerVO = new SpareBillDetailManagerVO();
                spareBillDetailManagerVO.setSpareBillId(spareBillId);
                for (Object jsond : jsonArray1) {
                    JSONObject jsonObjectDetail = JSONObject.parseObject(jsond.toString());
                    if (jsonObjectDetail.get("material_code") == null || jsonObjectDetail.get("material_code").equals("")) {
                        continue;
                    }
                    spareBillDetailManagerVO.setMaterialCode(jsonObjectDetail.get("material_code") == null ? "" : jsonObjectDetail.get("material_code").toString());
                    spareBillDetailManagerVO.setPositionCode(jsonObjectDetail.get("position_code") == null ? "" : jsonObjectDetail.get("position_code").toString());
                    spareBillDetailManagerVO.setSupplierCode(jsonObjectDetail.get("supplier_code") == null ? "" : jsonObjectDetail.get("supplier_code").toString());
                    spareBillDetailManagerVO.setUserAmount(jsonObjectDetail.get("user_amount") == null ? "" : jsonObjectDetail.get("user_amount").toString());
                    spareBillDetailManagerVO.setUsedAmount(jsonObjectDetail.get("used_amount") == null ? "" : jsonObjectDetail.get("used_amount").toString());
                    spareBillDetailManagerVO.setQueryWeight(jsonObjectDetail.get("query_weight") == null ? "" : jsonObjectDetail.get("query_weight").toString());
                    spareBillDetailManagerVO.setQueryBox(jsonObjectDetail.get("query_box") == null ? "" : jsonObjectDetail.get("query_box").toString());
                    spareBillDetailManagerVO.setQueryAdd(jsonObjectDetail.get("query_add") == null ? "" : jsonObjectDetail.get("query_add").toString());
                    spareBillDetailManagerVO.setUsedBox(jsonObjectDetail.get("used_box") == null ? "" : jsonObjectDetail.get("used_box").toString());
                    spareBillDetailManagerVO.setUsedWeight(jsonObjectDetail.get("used_weight") == null ? "" : jsonObjectDetail.get("used_weight").toString());
                    spareBillDetailManagerVO.setAddAmount(jsonObjectDetail.get("add_amount") == null ? "" : jsonObjectDetail.get("add_amount").toString());
                    spareBillDetailManagerVO.setSendAmount(jsonObjectDetail.get("send_amount") == null ? "" : jsonObjectDetail.get("send_amount").toString());
                    spareBillDetailManagerVO.setTheoryAmount(jsonObjectDetail.get("theory_amount") == null ? "" : jsonObjectDetail.get("theory_amount").toString());
                    spareBillDetailManagerVO.setRealAmount(jsonObjectDetail.get("real_amount") == null ? "" : jsonObjectDetail.get("real_amount").toString());
                    spareBillDetailManagerVO.setDifference(jsonObjectDetail.get("difference") == null ? "" : jsonObjectDetail.get("difference").toString());
                    spareBillDetailManagerVO.setMonth(jsonObjectDetail.get("month") == null ? "" : jsonObjectDetail.get("month").toString());
                    spareBillDetailManagerVO.setDay(jsonObjectDetail.get("day") == null ? "" : jsonObjectDetail.get("day").toString());
                    spareBillDetailManagerVO.setSurplusAmount(jsonObjectDetail.get("surplus_amount") == null ? "" : jsonObjectDetail.get("surplus_amount").toString());
                    spareBillDetailManagerVO.setOrderBy(jsonObjectDetail.get("order") == null ? "" : jsonObjectDetail.get("order").toString());

                    if (!Strings.isNullOrEmpty(jsonObjectDetail.get("id").toString())) {
                        //表示的是编辑
                        spareBillDetailManagerVO.setId(Long.parseLong(jsonObjectDetail.get("id").toString()));
                        id = spareBillDetailDAO.updateById(spareBillDetailManagerVO);
                    } else {
                        //表示的添加
                        id = spareBillDetailDAO.insert(spareBillDetailManagerVO);
                    }

                    if (id > 0) {
                        msg = "保存成功";
                        result = true;
                    } else {
                        msg = "保存失败";
                        result = false;
                        //将事务进行回滚
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        break;
                    }
                }
            } else {
                msg = "保存出错";
            }
        }
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 通过ID获取对应的主表的实体
     *
     * @param id
     * @return
     */
    @Override
    public SpareBillManagerVO getSpareBillById(String id) {
        SpareBillManagerVO spareBillManagerVO = spareBillDAO.selectById(id);
        return spareBillManagerVO;
    }

    /**
     * 备料单审核
     *
     * @param ids
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> check(String ids, String type) {
        Map<String, Object> map = Maps.newHashMap();
        boolean result = false;
        String msg = "";
        Integer spareId = null;
        String time = DateUtils.getTime();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);

        SpareBillManagerVO spareBillManagerVO = spareBillDAO.selectById(ids);
        if ("1".equals(type)) {
            spareBillManagerVO.setProductTime(time);
            spareBillManagerVO.setProductState("1");
            spareBillManagerVO.setProductUser(user.getUserId().toString());
            spareId = spareBillDAO.updateById(spareBillManagerVO);
        }
        if ("2".equals(type)) {
            spareBillManagerVO.setMaterialsTime(time);
            spareBillManagerVO.setMaterialsUser(user.getUserId().toString());
            spareBillManagerVO.setMaterialsState("1");
            spareId = spareBillDAO.updateById(spareBillManagerVO);
        }
        if ("3".equals(type)) {
            spareBillManagerVO.setQualityTime(time);
            spareBillManagerVO.setQualityUser(user.getUserId().toString());
            spareBillManagerVO.setQualityState("1");
            spareId = spareBillDAO.updateById(spareBillManagerVO);
        }
        if (spareId > 0) {
            result = true;
            msg = "审核成功";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }


    /**
     * 获取备料单中最大的code
     *
     * @return
     */
    public String getMaxBillCode() {
        //收货单编号
        String outstorageCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.BL_CODE_FORMAT);
        //获取最大收货单编号
        String maxoutstorageCode = spareBillDAO.getMaxOutstorageCode();
        if (Strings.isNullOrEmpty(maxoutstorageCode)) {
            outstorageCode = "PBOM000001";
        } else {
            Integer outstorageCode_count = Integer.parseInt(maxoutstorageCode.replace("PBOM", ""));
            outstorageCode = df.format(outstorageCode_count + 1);
        }
        return outstorageCode;
    }

    /**
     * 生成出库单详情
     *
     * @param outStorageId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> toOutStorageDetail(Long outStorageId) {
        Map<String, Object> map = new HashMap<>();
        map.put("result",false);
        map.put("msg","无出库详情生成");
        //根据id获取出库单
        OutStorageManagerVO outStorageManagerVO = outStorageDAO.selectById(outStorageId);
        if (outStorageManagerVO == null || Strings.isNullOrEmpty(outStorageManagerVO.getBillType()) || Strings.isNullOrEmpty(outStorageManagerVO.getCustomerCode())) {
            return map;
        }

        Customer customer = customerService.selectOne(
                new EntityWrapper<Customer>()
                        .eq("customer_code", outStorageManagerVO.getCustomerCode())
        );
        if (customer == null) {
            return map;
        }

        //获取备料单
        SpareBillManagerVO spareBillManagerVO = this.selectOne(
                new EntityWrapper<SpareBillManagerVO>()
                        .eq("spare_bill_code", outStorageManagerVO.getSpareBillCode())
        );
        if (spareBillManagerVO == null) {
            return map;
        }

        //获取备料单详情
        List<SpareBillDetailManagerVO> spareBillDetailManagerVOList = spareBillDetailDAO.selectList(
                new EntityWrapper<SpareBillDetailManagerVO>()
                        .eq("spare_bill_id", spareBillManagerVO.getId())
        );
        if (spareBillDetailManagerVOList.isEmpty()) {
            return map;
        }

        for (SpareBillDetailManagerVO spareBillDetailManagerVO : spareBillDetailManagerVOList) {
            //判断备料单参数是否为空
            if (Strings.isNullOrEmpty(spareBillDetailManagerVO.getUsedBox()) && Strings.isNullOrEmpty(spareBillDetailManagerVO.getQuantityReady())
                    && Strings.isNullOrEmpty(spareBillDetailManagerVO.getMaterialCode().trim()) && Strings.isNullOrEmpty(spareBillDetailManagerVO.getPositionCode())) {
                continue;
            }
            //使用数量
            Double usedBox = Double.parseDouble(spareBillDetailManagerVO.getUsedBox());
            //准备数量
            Double quantityReady = Double.parseDouble(spareBillDetailManagerVO.getQuantityReady());
            //使用重量
            Double userWeight = Double.parseDouble(spareBillDetailManagerVO.getUserAmount());
            //准备重量
            Double quantityWeight = Double.parseDouble(spareBillDetailManagerVO.getQuantityWeight());


            //获取物料信息
            Materiel materiel = materielService.selectOne(
                    new EntityWrapper<Materiel>()
                            .eq("materiel_code", spareBillDetailManagerVO.getMaterialCode().trim())
            );
            if (materiel == null) {
                continue;
            }

            //获取符合条件的库存，并按生产日期升序排序
            List<Stock> stockList = stockDAO.selectList(
                    new EntityWrapper<Stock>()
                            .eq("material_code", spareBillDetailManagerVO.getMaterialCode().trim())
                            .eq("position_code", spareBillDetailManagerVO.getPositionCode())
                            .eq("material_type", outStorageManagerVO.getBillType())
                            .eq("customer_code", customer.getCustomerCode())
                            .orderBy(true, "product_date")//???
            );
            //判断获取的库存集合，若为空，则结束当前循环，进入下次循环
            if (stockList.isEmpty()) {
                continue;
            }
            for (Stock stock : stockList) {
                //获取剩余需要准备的数量/重量，若为0，则跳出库存循环，进入下一个备料单详情中的物料
                Double remainReserves = usedBox-quantityReady;
                Double reservesWeight = userWeight - quantityWeight;
                if (remainReserves == 0) {
                    break;
                }
                if (reservesWeight == 0.0){
                    break;
                }
                //判断库存可用数量是否为空或为0，是，则结束本次循环
                if (Strings.isNullOrEmpty(stock.getAvailableStockAmount()) || stock.getAvailableStockAmount().equals(0)) {
                    continue;
                }
                if (Strings.isNullOrEmpty(stock.getAvailableStockWeight())|| stock.getAvailableStockWeight().equals(0.0)){
                    continue;
                }

                //新建出库单详情
                OutStorageDetailManagerVO outStorageDetailManagerVO = new OutStorageDetailManagerVO();
                outStorageDetailManagerVO.setOutstorageBillId(outStorageId.toString());
                outStorageDetailManagerVO.setMaterialCode(stock.getMaterialCode());
                outStorageDetailManagerVO.setMaterialName(stock.getMaterialName());
                outStorageDetailManagerVO.setUnit(materiel.getUnit());
                outStorageDetailManagerVO.setBatchNo(stock.getBatchNo());
                outStorageDetailManagerVO.setPositionCode(spareBillDetailManagerVO.getPositionCode());
                outStorageDetailManagerVO.setProductData(stock.getProductDate());

                //库存可用数量
                Double availableStockAmount = Double.parseDouble(stock.getAvailableStockAmount())-Double.parseDouble(stock.getOccupyStockAmount());

                //库存可用重量
                Double availableStockWeight = Double.parseDouble(stock.getAvailableStockWeight())-Double.parseDouble(stock.getOccupyStockWeight());
//                //若库存中出库单编号不为空则+“，”隔开存放
//                if (StringUtils.isNotEmpty(stock.getOutstorageBillCode())){
//                    stock.setOutstorageBillCode(stock.getOutstorageBillCode()+","+outStorageManagerVO.getOutstorageBillCode());
//                }else {
//                    stock.setOutstorageBillCode(outStorageManagerVO.getOutstorageBillCode());
//                }

                //若库存可用数量大于或等于使用数量，可用重量大于等于使用重量，则更新库存中出库占用数量
                if (availableStockAmount >= remainReserves && availableStockWeight >= reservesWeight ) {

                    outStorageDetailManagerVO.setWeight(reservesWeight.toString());
                    outStorageDetailManagerVO.setAmount(remainReserves.toString());
                    quantityWeight = quantityWeight + reservesWeight;
                    quantityReady = quantityReady + remainReserves;
                } else {

                    outStorageDetailManagerVO.setAmount(availableStockAmount.toString());
                    outStorageDetailManagerVO.setWeight(availableStockWeight.toString());
                    quantityReady = quantityReady + availableStockAmount;
                    quantityWeight = quantityWeight + availableStockWeight;
                }
                spareBillDetailManagerVO.setQuantityReady(quantityReady.toString());
                spareBillDetailManagerVO.setQuantityWeight(quantityWeight.toString());

                //更新备料详情中准备数量\准备重量
                if (spareBillDetailDAO.updateById(spareBillDetailManagerVO) <= 0) {
                    map.put("result", false);
                    map.put("msg","备料单详情更新失败");
                }
                //添加关于此出库ID的出库单详情
                if (outStorageDetailDAO.insert(outStorageDetailManagerVO) <= 0) {
                    map.put("result", false);
                    map.put("msg","出库单详情更新添加失败");
                }
            }
            map.put("result",true);
            map.put("msg","出库详情生成成功");
        }

        return map;
    }


    @Override
    public List<SpareBillManagerVO> getAllList(String ids) {
        return spareBillDAO.getAllList(StringUtils.stringToList(ids));
    }


    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<SpareBillManagerVO> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "备料单表" + "(" + date + ")";
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
            mapFields.put("spareBillCode", "备料单编号");
            mapFields.put("productNo", "生产任务单号");
            mapFields.put("productName", "生产产品信息");
            mapFields.put("totalProductAmount", "生产数量");
            mapFields.put("mixUseLine", "产线信息");
            mapFields.put("productUser", "生产审核人");
            mapFields.put("productTime", "审核时间");
            mapFields.put("productState", "审核状态");
            mapFields.put("materialsUser", "资材审核人");
            mapFields.put("materialsTime", "审核时间");
            mapFields.put("materialsState", "审核状态");
            mapFields.put("qualityUser", "品质审核人");
            mapFields.put("qualityTime", "审核时间");
            mapFields.put("qualityState", "审核状态");
            mapFields.put("state", "状态");
            mapFields.put("remark", "备注");

            //对收货计划字段进行转译
            for(SpareBillManagerVO is:list){
                String productState = is.getProductState();
                String materialsState = is.getMaterialsState();
                String qualityState = is.getQualityState();
                String state = is.getState();
                //单据类型（0：自采；1：客供）
                switch (productState){
                    case "0":is.setProductState("未审核");break;
                    case "1":is.setProductState("已审核");break;
                    default:is.setProductState("");
                }
                switch (materialsState){
                    case "0":is.setMaterialsState("未审核");break;
                    case "1":is.setMaterialsState("已审核");break;
                    default:is.setMaterialsState("");
                }
                switch (qualityState){
                    case "0":is.setQualityState("未审核");break;
                    case "1":is.setQualityState("已审核");break;
                    default:is.setQualityState("");
                }
                //状态（0：未提交；1：待收货；2：收货中；3：已完成；4：异常收货完成）
                switch (state){
                    case "0":is.setState("未生成出库单");break;
                    case "1":is.setState("已生成部分出库单");break;
                    case "2":is.setState("已生成出库单");break;
                    case "3":is.setState("出库中");break;
                    case "4":is.setState("已完成");break;
                    default:is.setState("");
                }
            }
            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
