package com.tbl.modules.stock.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.MaterielPower;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielPowerService;
import com.tbl.modules.stock.service.StockChangeService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 库存查询控制类
 * @author: zj
 * @create: 2018-12-26 10:26
 **/
@Controller
@RequestMapping(value = "/stockQuery")
public class StockQueryController extends AbstractController {

    //库存service
    @Autowired
    private StockService stockService;

    //物料service
    @Autowired
    private MaterielService materielService;

    //物料绑定rfid详情service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    //库位service
    @Autowired
    private DepotPositionService depotPositionService;

    //客户service
    @Autowired
    private CustomerService customerService;

    //库存查询service
    @Autowired
    private StockChangeService stockChangeService;

    //库存查询service
    @Autowired
    private MaterielPowerService materielPowerService;

    /**
     * @Description: 跳转到库存查询页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();

        //获取物料集合
        List<Materiel> lstMateriel = materielService.selectList(
                new EntityWrapper<Materiel>().eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        //获取库位集合
        List<DepotPosition> lstDepotPosition = depotPositionService.selectList(
                new EntityWrapper<>()
        );

        //获取客户集合
        List<Customer> lstCustomer = customerService.selectList(
                new EntityWrapper<Customer>().eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        mv.setViewName("techbloom/stock/stockQuery_list");
        mv.addObject("lstMateriel", lstMateriel);
        mv.addObject("lstCustomer", lstCustomer);
        mv.addObject("lstDepotPosition", lstDepotPosition);
        return mv;
    }

    /**
     * @Description: 获取库存查询列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils PageStock = stockService.queryPage(map);
        page.setTotalRows(PageStock.getTotalCount() == 0 ? 1 : PageStock.getTotalCount());
        map.put("rows", PageStock.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 跳转到散货的货权转移页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toTransferView")
    @ResponseBody
    public ModelAndView toTransferView(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/stock/materielPowerTransfer_edit");
        Stock stock = null;

        //获取物料信息集合
        List<Materiel> materielList = materielService.selectList(
                new EntityWrapper<Materiel>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        //获取库位集合
        List<DepotPosition> depotPositionList = depotPositionService.selectList(
                new EntityWrapper<>()
        );

        //货权转移
        stock = stockService.selectById(id);

        mv.addObject("stock", stock);
        mv.addObject("materielList", materielList);
        mv.addObject("depotPositionList", depotPositionList);
        mv.addObject("toTransferView", 1);
        return mv;
    }

    /**
     * @Description: 跳转到整货的货权转移页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toTransferRfidView")
    @ResponseBody
    public ModelAndView toTransferRfidView(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/stock/materielPowerRfidTransfer_edit");

        //获取物料绑定rfid信息集合
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectOne(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("id", id)
        );

        //获取库位集合
        List<DepotPosition> depotPositionList = depotPositionService.selectList(
                new EntityWrapper<>()
        );

        //获取物料信息集合
        List<Materiel> materielList = materielService.selectList(
                new EntityWrapper<Materiel>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        mv.addObject("materielList", materielList);
        mv.addObject("depotPositionList", depotPositionList);
        mv.addObject("materielBindRfidDetail", materielBindRfidDetail);
        mv.addObject("toTransferView", 1);
        return mv;
    }

    /**
     * 保存散货的货权转移信息
     *
     * @return
     * @author pz
     * @date 2019-01-09
     */
    @RequestMapping(value = "/saveMaterielPower")
    @ResponseBody
    public Map<String, Object> saveMaterielPower(Stock stock) {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;

        Long stockId = stock.getId();

        //获取货权转移对应的的库存信息
        Stock s = stockService.selectById(stockId);

        //货权转移的可用数量变化
        int availableAmount = Integer.valueOf(s.getAvailableStockAmount()) - Integer.valueOf(stock.getAvailableStockAmount());
        //货权转移的可用重量变化
        int availableWeight = Integer.valueOf(s.getAvailableStockWeight()) - Integer.valueOf(stock.getAvailableStockWeight());

        //判断库存中是否有库位编号，物料编号，批次号相同,如果有的话则提示
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        wraStock.eq("position_code", s.getPositionCode());
        wraStock.eq("material_code", stock.getMaterialCode());
        wraStock.eq("batch_no", stock.getBatchNo());
        int count = stockService.selectCount(wraStock);

        if (count > 0) {
            map.put("result", false);
            map.put("msg", "请先将" + stock.getPositionCode() + "中的" + stock.getMaterialCode() + "用完，再创建同批次物料");
            return map;
        }

        //货权转移数量不能大于可用数量
        if (availableAmount < 0) {
            map.put("result", false);
            map.put("msg", "货权转移数量不能大于库存可用数量！");
            return map;
        }

        //货权转移重量不能大于可用重量
        if (availableWeight < 0) {
            map.put("result", false);
            map.put("msg", "货权转移重量不能大于库存可用重量！");
            return map;
        }

        //货权转移出库物料编码
        String outMaterielCode = s.getMaterialCode();
        //货权转移出库物料名称
        String outMaterielName = s.getMaterialName();
        //货权转移出库批次号
        String outBatchNo = s.getBatchNo();
        //货权转移入库物料编码
        String inMaterielCode = stock.getMaterialCode();
        //货权转移入库批次号
        String inBatchNo = stock.getBatchNo();
        //依据物料编码获取物料名称
        EntityWrapper<Materiel> wraMateriel = new EntityWrapper<>();
        wraMateriel.eq("materiel_code", stock.getMaterialCode());
        Materiel materiel = materielService.selectOne(wraMateriel);
        //货权转移入库物料名称
        String inMaterielName = materiel.getMaterielName();

        //判断是否全部获取转移
        if (stock.getAvailableStockAmount().equals(s.getStockAmount())) {    //全部货权转移
            //修改货权转移信息
            s.setMaterialCode(stock.getMaterialCode());
            s.setMaterialName(inMaterielName);
            s.setBatchNo(stock.getBatchNo());
            s.setMaterielPower("1");
            s.setMaterialSource(s.getId());
            flag = stockService.updateById(s);
            //插入新增的货权转移表的数据
            MaterielPower materielPower = new MaterielPower();
            EntityWrapper<MaterielPower> wrapperMaterielPower = new EntityWrapper<>();
            wrapperMaterielPower.eq("stock_id",s.getId());
            List<MaterielPower> lstMaterielPower = materielPowerService.selectList(wrapperMaterielPower);
            if(lstMaterielPower.size()==0){
                materielPower.setStockId(s.getId());
                materielPower.setDocumentType(stock.getDocumentType());
                materielPower.setMaterielCode(outMaterielCode);
                materielPowerService.insert(materielPower);
            }else{
                materielPower = lstMaterielPower.get(0);
                materielPower.setDocumentType(stock.getDocumentType());
                materielPowerService.updateById(materielPower);
            }
        } else {

            //1.修改库存货权转移过的库存数量与库存重量
            s.setStockAmount(String.valueOf(availableAmount));
            s.setStockWeight(String.valueOf(availableWeight));
            s.setAvailableStockAmount(String.valueOf(availableAmount));
            s.setAvailableStockWeight(String.valueOf(availableWeight));
            s.setMaterielPower("1");
            boolean updFlag = stockService.updateById(s);

            //2.建立货权转移后新的库存信息
            stock.setCreateTime(DateUtils.getTime());
            stock.setCreateBy(getUserId());
            stock.setBatchNo(stock.getBatchNo());
            stock.setPositionCode(s.getPositionCode());
            stock.setStockAmount(stock.getAvailableStockAmount());
            stock.setStockWeight(stock.getAvailableStockWeight());
            stock.setPositionName(s.getPositionName());
            stock.setMaterialType(DyylConstant.MATERIAL_NORFID);
            stock.setCustomerCode(s.getCustomerCode());
            stock.setMaterialName(inMaterielName);
            stock.setMaterialSource(s.getId());
            stock.setProductDate(s.getProductDate());
            stock.setQualityDate(s.getQualityDate());
            boolean insFlag = stockService.insert(stock);
            //插入新增的货权转移表的数据
            MaterielPower materielPower = new MaterielPower();
            materielPower.setStockId(stock.getId());
            materielPower.setDocumentType(stock.getDocumentType());
            materielPowerService.insert(materielPower);
            flag = updFlag && insFlag;
        }
        if (flag) {
            map.put("result", true);
            map.put("msg", "货权转移成功！");
            //插入库存变动信息(货权转移出库与货权转移入库)
            this.stockChange(s.getPositionCode(), outMaterielCode, outMaterielName, outBatchNo, DyylConstant.DROIT_SHIFT_OUT, stock.getAvailableStockAmount(), "", getUserId());
            this.stockChange(s.getPositionCode(), inMaterielCode, inMaterielName, inBatchNo, DyylConstant.DROIT_SHIFT_IN, "", stock.getAvailableStockAmount(), getUserId());
        } else {
            map.put("result", false);
            map.put("msg", "货权转移失败！");
        }
        return map;
    }

    /**
     * 保存整货的货权转移信息
     *
     * @return
     * @author pz
     * @date 2019-01-09
     */
    @RequestMapping(value = "/saveRfidMaterielPower")
    @ResponseBody
    public Map<String, Object> saveRfidMaterielPower(MaterielBindRfidDetail materielBindRfidDetail) {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;
        Long mbrdId = materielBindRfidDetail.getId();
        //获取货权转移前的物料绑定数据详情信息
        MaterielBindRfidDetail mbrd = materielBindRfidDetailService.selectById(mbrdId);

        //货权转移出库物料编码
        String outMaterielCode = mbrd.getMaterielCode();
        //货权转移出库物料名称
        String outMaterielName = mbrd.getMaterielName();
        //货权转移入库物料编码
        String inMaterielCode = materielBindRfidDetail.getMaterielCode();
        //依据物料编码获取物料名称
        EntityWrapper<Materiel> wraMateriel = new EntityWrapper<>();
        wraMateriel.eq("materiel_code", materielBindRfidDetail.getMaterielCode());
        Materiel materiel = materielService.selectOne(wraMateriel);
        //货权转移入库物料名称
        String inMaterielName = materiel.getMaterielName();
        //批次号
        String batchRule = mbrd.getBatchRule();

        /**修改物料绑定详情表的数据**/
        mbrd.setMaterielCode(inMaterielCode);
        mbrd.setMaterielName(inMaterielName);
        materielBindRfidDetailService.updateById(mbrd);

        /**修改库存表的数据**/
        //依据库位id.获取库位编码
        Long positionId = mbrd.getPositionId();
        DepotPosition depotPosition = depotPositionService.selectById(positionId);
        //获取库存表中对应的数据
        EntityWrapper<Stock> wrapperStock = new EntityWrapper<>();
        wrapperStock.eq("material_code",outMaterielCode);
        wrapperStock.eq("batch_no",batchRule);
        wrapperStock.eq("rfid",mbrd.getRfid());
        wrapperStock.eq("position_code",depotPosition.getPositionCode());
        Stock stock = stockService.selectOne(wrapperStock);
        //修改货权转移信息
        stock.setMaterialCode(inMaterielCode);
        stock.setMaterialName(inMaterielName);
        stock.setMaterielPower("1");
        stock.setMaterialSource(stock.getId());
        flag = stockService.updateById(stock);
        //插入新增的货权转移表的数据
        MaterielPower materielPower = new MaterielPower();
        EntityWrapper<MaterielPower> wrapperMaterielPower = new EntityWrapper<>();
        wrapperMaterielPower.eq("stock_id",stock.getId());
        List<MaterielPower> lstMaterielPower = materielPowerService.selectList(wrapperMaterielPower);
        if(lstMaterielPower.size()==0){
            materielPower.setStockId(stock.getId());
            materielPower.setDocumentType(materielBindRfidDetail.getDocumentType());
            materielPower.setMaterielCode(outMaterielCode);
            materielPowerService.insert(materielPower);
        }else{
            materielPower = lstMaterielPower.get(0);
            materielPower.setDocumentType(materielBindRfidDetail.getDocumentType());
            materielPowerService.updateById(materielPower);
        }


        /**插入库存变动数据**/
        if (flag) {
            map.put("result", true);
            map.put("msg", "货权转移成功！");
            //插入库存变动信息(货权转移出库与货权转移入库)
            this.stockChange(stock.getPositionCode(), outMaterielCode, outMaterielName, batchRule, DyylConstant.DROIT_SHIFT_OUT, stock.getAvailableStockAmount(), "", getUserId());
            this.stockChange(stock.getPositionCode(), inMaterielCode, inMaterielName, batchRule, DyylConstant.DROIT_SHIFT_IN, "", stock.getAvailableStockAmount(), getUserId());
        } else {
            map.put("result", false);
            map.put("msg", "货权转移失败！");
        }
        return map;
    }

    /**
     * @Description: 跳转整货货权转移页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toRfidDetailView")
    @ResponseBody
    public ModelAndView toRfidDetailView(long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/stockQueryRfidDetail_list");
        mv.addObject("stockId", id);
        return mv;
    }

    /**
     * @Description: 跳转库存查询详情页
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toDetailView")
    @ResponseBody
    public ModelAndView toDetailView(long id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/stockQueryDetail_list");
        mv.addObject("stockId", id);
        return mv;
    }

    /**
     * @Description: 获取库存查询详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String queryJsonString, Long stockId) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "createTime";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("suserid", getSessionUser().getUserId());
        PageUtils PageStockDetail = stockService.queryPageS(map, stockId);
        if (PageStockDetail == null) {
            page.setTotalRows(0);
            map.put("rows", null);
        } else {
            page.setTotalRows(PageStockDetail.getTotalCount() == 0 ? 1 : PageStockDetail.getTotalCount());
            map.put("rows", PageStockDetail.getList());
        }
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 插入库存变动信息
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/20
     */
    public void stockChange(String changeCode, String materialCode, String materialName, String batchNo, String businessType, String outAmount, String inAmount, Long userId) {
        StockChange stockChange = new StockChange();
        stockChange.setChangeCode(changeCode);
        stockChange.setMaterialCode(materialCode);
        stockChange.setMaterialName(materialName);
        stockChange.setBatchNo(batchNo);
        stockChange.setBusinessType(businessType);
        stockChange.setOutAmount(outAmount);
        stockChange.setInAmount(inAmount);
        stockChange.setCreateTime(DateUtils.getTime());
        stockChange.setCreateBy(userId);
        stockChangeService.insert(stockChange);
    }

}