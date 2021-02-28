package com.tbl.modules.basedata.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.QrCodeUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.entity.Producer;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.basedata.service.ProducerService;
import com.tbl.modules.basedata.service.SupplierService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/materielManage")
public class MaterielManageController extends AbstractController {

    //物料管理service
    @Autowired
    private MaterielService materielService;

    //客户管理service
    @Autowired
    private CustomerService customerService;

    //供应商管理service
    @Autowired
    private SupplierService supplierService;

    //生产厂家管理service
    @Autowired
    private ProducerService producerService;


    /**
     * 跳转到物料管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();

        //获取客户集合
        List<Customer> customerList = customerService.selectList(
                new EntityWrapper<Customer>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        //获取供应商集合
        List<Supplier> supplierList = supplierService.selectList(
                new EntityWrapper<Supplier>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        //获取生产厂家集合
        List<Producer> producerList = producerService.selectList(
                new EntityWrapper<Producer>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        mv.setViewName("techbloom/basedata/materielManage/materielManage_list");
        mv.addObject("customerList", customerList);
        mv.addObject("supplierList", supplierList);
        mv.addObject("producerList", producerList);
        return mv;
    }

    /**
     * 获取物料列表数据
     *
     * @param queryJsonString
     * @return Map
     * @author yuany
     * @date 2019-01-02
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Map<String, Object> listLog(String queryJsonString) {

        Map<String, Object> map = new HashMap<>();
        if (!com.tbl.common.utils.StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = materielService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @return
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/materielManage/materielManage_edit");
        Materiel materiel = null;
        //判断是否为编辑页面
        if (id != -1) {
            materiel = materielService.selectById(id);
        } else {
            //自动生成物料编码
//            String materielCode = materielService.getMaterielCode();
//            mv.addObject("materielCode", materielCode);
        }

        //获取客户集合
        List<Customer> customerList = customerService.selectList(
                new EntityWrapper<>()
        );

        //获取供应商集合
        List<Supplier> supplierList = supplierService.selectList(
                new EntityWrapper<>()
        );

        //获取生产厂家集合
        List<Producer> producerList = producerService.selectList(
                new EntityWrapper<>()
        );

        mv.addObject("customerList", customerList);
        mv.addObject("supplierList", supplierList);
        mv.addObject("producerList", producerList);
        mv.addObject("materiel", materiel);
        mv.addObject("edit", 1);//？？？
        return mv;
    }

    /**
     * 跳转到弹出的详情页面
     *
     * @return
     * @author anss
     * @date 2019-3-4
     */
    @RequestMapping(value = "/toDetail.do")
    @ResponseBody
    public ModelAndView toDetail(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/materielManage/materielManage_detail");
        Materiel materiel = materielService.selectById(id);

        //客户名称
        String customerName = "";
        if (materiel.getCustomerBy() != null) {
            Customer customer = customerService.selectById(materiel.getCustomerBy());
            customerName = customer.getCustomerName();
        }

        //供应商名称
        String supplierName = "";
        if (materiel.getCustomerBy() != null) {
            Supplier supplier = supplierService.selectById(materiel.getSupplierBy());
            supplierName = supplier.getSupplierName();
        }

        //生产厂家名称
        String producerName = "";
        if (materiel.getCustomerBy() != null) {
            Producer producer = producerService.selectById(materiel.getProducerBy());
            producerName = producer.getProducerName();
        }

        mv.addObject("customerName", customerName);
        mv.addObject("supplierName", supplierName);
        mv.addObject("producerName", producerName);
        mv.addObject("materiel", materiel);
        return mv;
    }

    /**
     * 判断物料编码是否存在
     *
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String materielCode, Long id) {
        boolean flag = true;
        // 根据materielCode查询实体list
        EntityWrapper<Materiel> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("materiel_code", materielCode);
        try {
            //？？？？
            if (id != null) {
                int count = materielService.selectCount(entityWrapper);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = materielService.selectCount(entityWrapper);
                if (count > 0) {
                    flag = false;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return flag;
    }

    /**
     * 添加/修改物料
     *
     * @return
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/addMateriel")
    @ResponseBody
    public boolean addMateriel(Materiel materiel) {
        boolean result = false;

        if (materiel == null) {
            return result;
        }

        //获取当前时间并格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);
        //获取体积
        String volume = null;
        if (!Strings.isNullOrEmpty(materiel.getHeight()) && !Strings.isNullOrEmpty(materiel.getLength()) && !Strings.isNullOrEmpty(materiel.getWide())) {
            volume = String.valueOf(Double.parseDouble(materiel.getHeight())
                    * Double.parseDouble(materiel.getLength()) * Double.parseDouble(materiel.getWide()));
        }
        materiel.setVolume(volume);

        //如果映射获取的物料ID不为空，则为修改物料，否则为添加物料
        if (materiel.getId() != null) {
            materiel.setUpdateTime(time);
            result = materielService.updateById(materiel);
        } else {
            List<Materiel> materielList = materielService.selectList(
                    new EntityWrapper<Materiel>()
                            .eq("materiel_code", materiel.getMaterielCode())
            );
            if (materielList.size() != 0) {
                materiel.setMaterielCode(materielService.getMaterielCode());
            }
            materiel.setCreateTime(time);
            result = materielService.insert(materiel);
        }
        return result;
    }

    /**
     * 逻辑删除物料
     *
     * @return
     * @author yuany
     * @date 2019-01-04
     */
    @RequestMapping(value = "/delMateriel")
    @ResponseBody
    public boolean delMateriel(String ids) {
        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }
        return materielService.delLstMateriel(ids, getUserId());
    }

    /**
     * 导出Excel
     *
     * @author yuany
     * @date 2019-01-04
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void artBomExcel(String ids) {
        List<Materiel> list = materielService.getAllLists(ids);
        //遍历添加名称
        for (Materiel materiel : list) {
            if (materiel.getCustomerBy() != null && customerService.selectById(materiel.getCustomerBy()) != null) {
                materiel.setCustomerName(customerService.selectById(materiel.getCustomerBy()).getCustomerName());
            }
            if (materiel.getCustomerBy() != null && producerService.selectById(materiel.getProducerBy()) != null) {
                materiel.setProducerName(producerService.selectById(materiel.getProducerBy()).getProducerName());
            }
            if (materiel.getSupplierBy() != null && supplierService.selectById(materiel.getSupplierBy()) != null) {
                materiel.setSupplierName(supplierService.selectById(materiel.getSupplierBy()).getSupplierName());
            }
        }
        materielService.toExcel(response, "", list);
    }

    /**
     * 物料二维码打印
     *
     * @return
     * @author yuany
     * @date 2019-03-29
     */
    @RequestMapping(value = "/matrixCode")
    @ResponseBody
    public ModelAndView tomatrixCode(String id) {

        ModelAndView mv = this.getModelAndView();

        List<Long> lstid = Arrays.stream(id.split(",")).map(a -> Long.parseLong(a)).collect(Collectors.toList());
        //根据物料ID获取需要生成二维码的信息
        List<Materiel> materielList = materielService.selectBatchIds(lstid);

        //存储数据的集合
        List<Map<String, Object>> matrixCodeList = new ArrayList<>();

        //判断根据id获取的物料集合是否为空
        if (!materielList.isEmpty()) {
            for (Materiel materiel : materielList) {
                Map<String, Object> map = new HashMap<>();
                //二维码内容/宽/高
                String binary = QrCodeUtils.creatRrCode(materiel.getMaterielCode(), 400, 400);
                map.put("binary", binary);
                //物料编码  物料名称 物料规格 助记码 保质期
                map.put("materielCode", materiel.getMaterielCode());
                map.put("materielName", materiel.getMaterielName());
                map.put("spec",materiel.getSpec());
                map.put("mnemonicCode",materiel.getMnemonicCode());
                map.put("qualityPeriod",materiel.getQualityPeriod());

                matrixCodeList.add(map);
            }
        }

        //放入页面数据
        mv.addObject("matrixCodeList", matrixCodeList);
        //跳转地址
        mv.setViewName("techbloom/basedata/materielManage/materielManage_matrixCode");

        return mv;
    }


}
