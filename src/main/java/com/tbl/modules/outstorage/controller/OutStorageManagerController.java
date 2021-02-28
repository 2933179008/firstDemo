package com.tbl.modules.outstorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.InstorageDetailService;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.instorage.service.ReceiptService;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.LowerShelfService;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.StockChangeService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @program: dyyl
 * @description: 出库单管理控制类
 * @author: zj
 * @create: 2018-12-28 17:03
 **/
@Controller
@RequestMapping(value = "/outStorageManager")
public class OutStorageManagerController extends AbstractController {

    @Autowired
    private OutStorageService outStorageService;

    @Autowired
    private OutStorageDetailService outStorageDetailService;

    @Autowired
    private LowerShelfService lowerShelfService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private InstorageService instorageService;

    @Autowired
    private InstorageDetailService instorageDetailService;

    @Autowired
    private DepotPositionService depotPositionService;

    @Autowired
    private StockChangeService stockChangeService;

    @Autowired
    private ReceiptDetailService receiptDetailService;

    /**
     * @Description: 跳转出库单管理控制类
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/outstorage/outStorage_list");
        return mv;
    }

    /**
     * @Description: 获取出库单管理列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        PageTbl pageTbl = this.getPage();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        map.put("page", pageTbl.getPageno());
        map.put("limit", pageTbl.getPagesize());
        String sortName = pageTbl.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "ob.id";
            pageTbl.setSortname(sortName);
        }
        String sortOrder = pageTbl.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            pageTbl.setSortorder(sortOrder);
        }

        map.put("sidx", pageTbl.getSortname());
        map.put("order", pageTbl.getSortorder());
        PageUtils pageUtils = outStorageService.queryPage(map);
        pageTbl.setTotalRows(pageUtils.getTotalCount() == 0 ? 1 : pageUtils.getTotalCount());
        map.put("rows", pageUtils.getList());
        executePageMap(map, pageTbl);
        return map;
    }

    /**
     * @Description: 跳转到出库单管理编辑页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(String id) {
        ModelAndView mv = new ModelAndView();
        String outstorageBillCode = "";
        if ("-1".equals(id)) {//表示的是添加
            //获取最大出库编号
            outstorageBillCode = outStorageService.getMaxBillCode();
        } else {//表示的是编辑
            //根据ID获取对应的属性的值
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(id);
            mv.addObject("outStorageManagerVO", outStorageManagerVO);
            outstorageBillCode = outStorageManagerVO.getOutstorageBillCode();
        }
        //获取货主的信息
        List<Map<String, Object>> customerList = outStorageService.getCustomerList();
        mv.addObject("customerList", customerList);
        //获取库位的信息
        List<Map<String, Object>> areaList = outStorageService.getAreaList();
        mv.addObject("areaList", areaList);
        //获取备料单的信息
        List<Map<String, Object>> spareList = outStorageService.getSpareList();
        mv.addObject("spareList", spareList);

        //获取收货单的信息
        List<Map<String, Object>> receipList = outStorageService.getReceipList();
        mv.addObject("receipList", receipList);

        //获取入库单的信息
        List<Map<String, Object>> instorageList = outStorageService.getInstorageList();
        mv.addObject("instorageList", instorageList);

        mv.addObject("outstorageBillCode", outstorageBillCode);
        mv.setViewName("techbloom/outstorage/outStorage_edit");
        return mv;
    }

    /**
     * 出库单的添加或者编辑
     *
     * @param outStorageManagerVO
     * @return
     */
    @RequestMapping(value = "/saveOutStorage")
    @ResponseBody
    public Map<String, Object> saveOutStorage(OutStorageManagerVO outStorageManagerVO) {
        return outStorageService.saveOutstorage(outStorageManagerVO);
    }

    /**
     * 出库单删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delOutStorage")
    @ResponseBody
    public Map<String, Object> delOutStorage(String ids) {
        Map<String, Object> map = Maps.newHashMap();
        map = outStorageService.delOutStorage(ids);
        return map;
    }

    /**
     * 出库单审核
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/auditing")
    @ResponseBody
    public Map<String, Object> auditing(String id) {
        Map<String, Object> map = Maps.newHashMap();
        //变更状态
        String state = "2";
        map = outStorageService.auditing(id, state);
        return map;
    }

    /**
     * @Description: 获取出库单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/getEditList")
    @ResponseBody
    public Map<String, Object> getEditList(String outStorageId) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();

        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("outStorageId", outStorageId);
        PageUtils PagePlatform = outStorageDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
        map.put("rows", PagePlatform.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 根据ID删除对应的详情的数据
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delOutStorageDetail")
    @ResponseBody
    public Map<String, Object> delOutStorageDetail(String ids) {
        Map<String, Object> map = Maps.newHashMap();
        String msg = "";
        String[] id = ids.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(id));
        boolean result = outStorageDetailService.deleteBatchIds(list);
        if (result) {
            msg = "删除成功";
        } else {
            msg = "删除失败";
        }
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 获取要添加的物料的下拉框
     *
     * @param queryString
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getOutStorageMaterialList")
    @ResponseBody
    public Map<String, Object> getOutStorageMaterialList(String outStorageId, String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = Maps.newHashMap();

        String billType = null;
        String materials = null;
        List<String> materialCodes = new ArrayList<>();
        String positionCode = null;
        String customerCode = null;
        if (StringUtils.isNotBlank(outStorageId)) {
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(outStorageId);
            if (outStorageManagerVO != null) {
                billType = outStorageManagerVO.getBillType();
                positionCode = outStorageManagerVO.getPositionCode();
                customerCode = outStorageManagerVO.getCustomerCode();
            }
            //退货出库 物料列表筛选
            if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1) && StringUtils.isNotEmpty(outStorageManagerVO.getReceiptCode())) {
                Receipt receipt = receiptService.selectOne(new EntityWrapper<Receipt>().eq("receipt_code", outStorageManagerVO.getReceiptCode()));
                if (receipt != null) {
                    List<ReceiptDetail> receiptDetailList = receiptDetailService.selectList(new EntityWrapper<ReceiptDetail>().eq("receipt_plan_id", receipt.getId()));
                    if (!receiptDetailList.isEmpty()) {
                        for (ReceiptDetail receiptDetail : receiptDetailList) {
                            materialCodes.add(receiptDetail.getMaterialCode());
                        }
                    }
                }

            }else if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE2) && StringUtils.isNotEmpty(outStorageManagerVO.getInstorageCode())){
                Instorage instorage = instorageService.selectOne(new EntityWrapper<Instorage>().eq("instorage_bill_code",outStorageManagerVO.getInstorageCode()));
                if (instorage != null) {
                    List<InstorageDetail> instorageDetailList = instorageDetailService.selectList(new EntityWrapper<InstorageDetail>().eq("instorage_bill_id",instorage.getId()));
                    if (!instorageDetailList.isEmpty()){
                        for (InstorageDetail instorageDetail : instorageDetailList){
                            materialCodes.add(instorageDetail.getMaterialCode());
                        }
                    }
                }
            }
        }
        List<Map<String, Object>> materialList = outStorageDetailService.getSelectMaterialList(positionCode, customerCode, materialCodes, billType, queryString, pageSize, pageNo);

        map.put("result", materialList);
        map.put("total", outStorageDetailService.getSelectMaterialTotal(queryString));
        return map;
    }

    /**
     * 出库单详情物料添加
     *
     * @param outStorageId
     * @param materialContent
     * @return
     */
    @RequestMapping(value = "/addOutStorageDetail")
    @ResponseBody
    public Map<String, Object> addOutStorageDetail(String outStorageId, String materialContent) {
        Map<String, Object> map = Maps.newHashMap();
        map = outStorageDetailService.addOutStorageDetail(outStorageId, materialContent);
        return map;
    }

    /**
     * 更新數量
     *
     * @param id
     * @param amount
     */
    @RequestMapping(value = "/updateDetailAmount")
    @ResponseBody
    public Map<String, Object> updateDetailAmount(String id, String amount, String weight) {
        Map<String, Object> map = new HashMap<>();
        map = outStorageDetailService.updateDetailAmount(id, amount, weight);
        return map;
    }

    /**
     * 单据提交
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setOutState")
    @ResponseBody
    public Map<String, Object> setOutState(String id) {
        Map<String, Object> map = Maps.newHashMap();
        boolean result = true;
        String msg = "";

        //id获取出库单
        OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(id);
        if (outStorageManagerVO == null) {
            map.put("msg", "无对应出库单");
            map.put("result", false);
            return map;
        }

        //判断是否存在详情,如果不存在详情的话,则不能进行提交
        List<OutStorageDetailManagerVO> list = outStorageDetailService.getDetailList(id);
        if (list.size() == 0) {
            //表示当前的出库单没有详情,不能进行提交
            msg = "单据没有详情,不能进行提交";
            result = false;
        } else {
            //跃库出库
            if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE2)) {
                //添加库存变动信息
                for (OutStorageDetailManagerVO outStorageDetailManagerVO : list) {
                    Long positionBy = null;
                    if (StringUtils.isNotEmpty(outStorageDetailManagerVO.getPositionCode())) {
                        DepotPosition depotPosition = depotPositionService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code", outStorageDetailManagerVO.getPositionCode()));
                        if (depotPosition != null) {
                            positionBy = depotPosition.getId();
                        }
                    }
                    StockChange stockChange = new StockChange();
                    stockChange.setChangeCode(outStorageManagerVO.getOutstorageBillCode());
                    stockChange.setMaterialCode(outStorageDetailManagerVO.getMaterialCode());
                    stockChange.setMaterialName(outStorageDetailManagerVO.getMaterialName());
                    stockChange.setBatchNo(outStorageDetailManagerVO.getBatchNo());
                    stockChange.setBusinessType(DyylConstant.CROSSDOCKING__OUT);
                    stockChange.setPositionBy(positionBy);
                    stockChange.setOutAmount(outStorageDetailManagerVO.getAmount());
                    stockChange.setOutWeight(outStorageDetailManagerVO.getWeight());
                    stockChange.setCreateTime(DateUtils.getTime());
                    stockChange.setCreateBy(getUserId());
                    stockChangeService.insert(stockChange);
                }
                //出库完成
                outStorageManagerVO.setState("5");
                outStorageService.updateById(outStorageManagerVO);
                map.put("result", result);
                map.put("msg", "提交成功，单据已完成");
                return map;
            }
            //判断单据能不能进行提交
            for (OutStorageDetailManagerVO outStorageDetailManagerVO : list) {
                result = true;
                if (Strings.isNullOrEmpty(outStorageDetailManagerVO.getAmount()) || outStorageDetailManagerVO.getAmount().equals("0")
                        || Strings.isNullOrEmpty(outStorageDetailManagerVO.getWeight()) || outStorageDetailManagerVO.getWeight().equals("0.0")) {
                    result = false;
                    msg = "存在数量/重量没有填写或者数量/重量填写为0的数据,不能进行提交";
                    break;
                }
                //判断对应的单据是否能够正常的出库
                Stock stock = stockService.selectOne(
                        new EntityWrapper<Stock>()
                                .eq("material_type", outStorageManagerVO.getBillType())
                                .eq("material_code", outStorageDetailManagerVO.getMaterialCode())
                                .eq("position_code", outStorageDetailManagerVO.getPositionCode())
                                .eq("batch_no", outStorageDetailManagerVO.getBatchNo())
                );
                if (stock == null) {
                    msg = "出库单中，物料编号[" + outStorageDetailManagerVO.getMaterialCode() + "],物料名称[" + outStorageDetailManagerVO.getMaterialName() + "]未找到对应库存,提交失败";
                    result = false;
                    break;
                }

                //可用出库数量
                Double aviOutStorageAmount = 0.0;
                //可用出库重量
                Double aviOutStorageWeight = 0.0;
                //退货出库
                if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
                    //获取库存可用出库数量
                    aviOutStorageAmount = Double.parseDouble(stock.getStockAmount()) - Double.parseDouble(stock.getOccupyStockAmount());
                    //获取库存可用出库重量
                    aviOutStorageWeight = Double.parseDouble(stock.getStockWeight()) - Double.parseDouble(stock.getOccupyStockWeight());

                } else {
                    //其它出库
                    //获取库存可用出库数量
                    aviOutStorageAmount = Double.parseDouble(stock.getAvailableStockAmount()) - Double.parseDouble(stock.getOccupyStockAmount());
                    //获取库存可用出库重量
                    aviOutStorageWeight = Double.parseDouble(stock.getAvailableStockWeight()) - Double.parseDouble(stock.getOccupyStockWeight());
                }

                Double amount = Double.parseDouble(outStorageDetailManagerVO.getAmount());
                Double weight = Double.parseDouble(outStorageDetailManagerVO.getWeight());

                if (aviOutStorageAmount < amount || aviOutStorageWeight < weight) {
                    //查询对应的是否存在货权转移并且货权转移的书鲁昂足够多的的
                    result = false;
                    msg = "库存中的物料编号[" + outStorageDetailManagerVO.getMaterialCode() + "],物料名称[" + outStorageDetailManagerVO.getMaterialName() + "]库存已经不够,不能进行出库";
                    break;
                }
                if (outStorageManagerVO.getBillType().equals(DyylConstant.MATERIAL_RFID)) {
                    if (!outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1) && Strings.isNullOrEmpty(stock.getAvailableRfid())) {
                        result = false;
                        msg = "提交失败,当前库存无可用的rfid";
                        break;
                    }else if (Strings.isNullOrEmpty(stock.getRfid())){
                        result = false;
                        msg = "提交失败,当前库存无rfid";
                        break;
                    }
                }
            }
            if (result) {
                //更新库存中出库单占用数据
                for (OutStorageDetailManagerVO outStorageDetailManagerVO : list) {
                    Stock stock = stockService.selectOne(
                            new EntityWrapper<Stock>()
                                    .eq("material_type", outStorageManagerVO.getBillType())
                                    .eq("material_code", outStorageDetailManagerVO.getMaterialCode())
                                    .eq("batch_no", outStorageDetailManagerVO.getBatchNo())
                                    .eq("position_code", outStorageDetailManagerVO.getPositionCode())
                    );

                    //退货出库
                    if (outStorageManagerVO.getOutstorageBillType().equals(DyylConstant.OUTSTORAGE_TYPE1)) {
                        //通过物料编号与批次号查询物料是否能够出库
                        map = outStorageDetailService.getMaterialList(outStorageDetailManagerVO.getMaterialCode(),
                                outStorageDetailManagerVO.getBatchNo(), outStorageDetailManagerVO.getAmount(), outStorageDetailManagerVO.getWeight(), id, stock.getRfid(), outStorageDetailManagerVO.getId());
                    } else {
                        //根据物料编号以及批次号进行库存物料的锁定
                        map = outStorageDetailService.getMaterialList(outStorageDetailManagerVO.getMaterialCode(),
                                outStorageDetailManagerVO.getBatchNo(), outStorageDetailManagerVO.getAmount(), outStorageDetailManagerVO.getWeight(), id, stock.getAvailableRfid(), outStorageDetailManagerVO.getId());
                    }

                    result = (boolean) map.get("result");
                    msg = (String) map.get("msg");
                    if (!result) {
                        break;
                    }
                }

                if (result) {
                    //更新出库单状态
                    Object mm = outStorageDetailService.updateState(id);
                }
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * 生成下架单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/addshelve")
    @ResponseBody
    public Map<String, Object> addshelve(String id) {
        Map<String, Object> map = Maps.newHashMap();
        //判断是否已经生成了下架单,如果已经生成了下架单,则不能继续生成下架单
        Integer count = lowerShelfService.getDtailCount(id);
        if (count > 0) {
            map.put("msg", "当前单据已经生成下架单,不能进行重复生成");
            map.put("result", false);
            return map;
        } else {
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(id);
            //生成下架单
            map = outStorageDetailService.addshelve(getUserId(), outStorageManagerVO, id);
        }
        return map;
    }


    /**
     * 返回到出库单详情页面
     *
     * @param outStorageId
     * @return
     * @author yuany
     * @date 2019-4-8
     */
    @RequestMapping(value = "/toDetail")
    public ModelAndView toDetail(String outStorageId) {
        ModelAndView mv = new ModelAndView();
        OutStorageManagerVO outStorageManagerVO = outStorageService.selectById(outStorageId);

        if (StringUtils.isNotEmpty(outStorageManagerVO.getReceiptCode())) {
            Receipt receipt = receiptService.selectOne(new EntityWrapper<Receipt>().eq("receipt_code", outStorageManagerVO.getReceiptCode()));
            if (receipt != null) {
                outStorageManagerVO.setReceiptCode(receipt.getReceiptCode());
            }
        }

        if (StringUtils.isNotEmpty(outStorageManagerVO.getInstorageCode())) {
            Instorage instorage = instorageService.selectOne(new EntityWrapper<Instorage>().eq("Instorage_bill_code", outStorageManagerVO.getInstorageCode()));
            if (instorage != null) {
                outStorageManagerVO.setInstorageCode(instorage.getInstorageBillCode());
            }
        }

        mv.addObject("outStorageManagerVO", outStorageManagerVO);
        mv.setViewName("techbloom/outstorage/outStorage_detail");
        return mv;
    }

    /**
     * @Description: 获取出库单详情列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019/4/8
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String outStorageId) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("outStorageId", outStorageId);
        PageUtils PagePlatform = outStorageDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
        map.put("rows", PagePlatform.getList());
        executePageMap(map, page);

        return map;
    }
}
    