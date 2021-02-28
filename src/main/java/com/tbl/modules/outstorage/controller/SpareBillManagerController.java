package com.tbl.modules.outstorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.entity.SpareBillManagerVO;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 备料单管理控制类
 * @author: zj
 * @create: 2018-12-28 14:54
 **/
@Controller
@RequestMapping(value = "/spareBillManager")
public class SpareBillManagerController extends AbstractController {

    @Autowired
    private SpareBillService spareBillService;

    @Autowired
    private SpareBillDetailService spareBillDetailService;

    @Autowired
    private OutStorageService outStorageService;

    @Autowired
    private MaterielService materielService;

    /**
     * @Description: 跳转备料单管理页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/outstorage/spareBill_list");
        return mv;
    }

    /**
     * @Description: 获取备料单管理列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl pageTbl = this.getPage();
        map.put("page", pageTbl.getPageno());
        map.put("limit", pageTbl.getPagesize());

        map.put("sidx", pageTbl.getSortname());
        map.put("order", pageTbl.getSortorder());
        PageUtils pageUtils = spareBillService.queryPage(map);
        pageTbl.setTotalRows(pageUtils.getTotalCount() == 0 ? 1 : pageUtils.getTotalCount());
        map.put("rows", pageUtils.getList());
        executePageMap(map, pageTbl);

        return map;
    }


    /**
     * @Description: 跳转到备料单编辑页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(String id) {
        ModelAndView mv = new ModelAndView();
        if ("-1".equals(id)) {
            //表示的是添加
            SpareBillManagerVO spareBillManagerVO = new SpareBillManagerVO();
            List<SpareBillDetailManagerVO> spareBillDetailList = new ArrayList<>();
            mv.addObject("spareBillManagerVO", spareBillManagerVO);
            mv.addObject("spareBillDetailList1", spareBillDetailList);
            mv.addObject("spareBillDetailList2", spareBillDetailList);
            mv.addObject("spareBillDetailList3", spareBillDetailList);
            mv.addObject("spareBillDetailList4", spareBillDetailList);
        } else {
            //表示的是编辑
            //通过ID获取对应的主表的信息
            SpareBillManagerVO spareBillManagerVO = spareBillService.getSpareBillById(id);
            List<SpareBillDetailManagerVO> spareBillDetailList = spareBillDetailService.getSpareBillList(id);
            //每张表单6条，详情条数/6 ，取整
            Integer count = spareBillDetailList.size() / 6;
            //详情条数%6 ，取余
            Integer num = spareBillDetailList.size() % 6;
            //若取余大于零，则加一
            if (num > 0) {
                count++;
            }
            //遍历获取符合count个数的表头，并根据表头数量生成六个为一组的详情
            for (Integer i = 1; i <= count; i++) {
                String spareBill = "spareBillManagerVO" + i.toString();
                mv.addObject(spareBill, spareBillManagerVO);
                List<SpareBillDetailManagerVO> spareBillDetailManagerVOList = new ArrayList<>();
                if (spareBillDetailList.size() >= 6) {
                    for (int j = 0; j < 6; j++) {
                        //获取备料详情
                        SpareBillDetailManagerVO spareBillDetailManagerVO = spareBillDetailList.get(0);
                        //获取基础物料信息
                        Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",spareBillDetailManagerVO.getMaterialCode()));
                        //讲助记码放入备料详情的supplierCode中
                        spareBillDetailManagerVO.setSupplierCode(materiel.getMnemonicCode());

                        spareBillDetailManagerVOList.add(spareBillDetailManagerVO);
                        spareBillDetailList.remove(0);
                    }
                    String spareBillDetail = "spareBillDetailList" + i.toString();
                    mv.addObject(spareBillDetail, spareBillDetailManagerVOList);
                } else {
                    for (int j = 0; j < spareBillDetailList.size(); j++) {
                        //获取备料详情
                        SpareBillDetailManagerVO spareBillDetailManagerVO = spareBillDetailList.get(j);
                        //获取基础物料信息
                        Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",spareBillDetailManagerVO.getMaterialCode()));
                        //讲助记码放入备料详情的supplierCode中
                        spareBillDetailManagerVO.setSupplierCode(materiel.getMnemonicCode());

                        spareBillDetailManagerVOList.add(spareBillDetailManagerVO);
                    }
                    String spareBillDetail = "spareBillDetailList" + i.toString();
                    mv.addObject(spareBillDetail, spareBillDetailManagerVOList);
                }
            }
        }
        mv.setViewName("techbloom/outstorage/spareBill_detail");
        return mv;
    }

    /**
     * @Description: 跳转到生成出库单页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/outStorage")
    @ResponseBody
    public ModelAndView outStorage(String id) {
        ModelAndView mv = new ModelAndView();
        SpareBillManagerVO spareBillManagerVO = spareBillService.selectById(id);
        String billType = "0";
        //若备料单为未生成出库单状态，则默认生成无RFID单据（操作中可重新定义）
        if (spareBillManagerVO.getState().equals("1")) {
            List<OutStorageManagerVO> outStorageManagerVOList = outStorageService.selectList(
                    new EntityWrapper<OutStorageManagerVO>()
                            .eq("spare_bill_code", spareBillManagerVO.getSpareBillCode())
            );
            //出库单唯一，则已生成一条出库单
            if (outStorageManagerVOList.size() == 1) {
                OutStorageManagerVO outStorageManagerVO = outStorageManagerVOList.get(0);
                if (outStorageManagerVO != null && StringUtils.isNotBlank(outStorageManagerVO.getMaterialType()) && outStorageManagerVO.getBillType().equals("0")) {
                    billType = "1";
                }
            } else {
                mv.isEmpty();
                return mv;
            }
        }
        //获取货主的信息
        List<Map<String, Object>> customerList = outStorageService.getCustomerList();
        mv.addObject("customerList", customerList);

        mv.addObject("billType", billType);
        mv.addObject("spareBillManagerVO", spareBillManagerVO);
        mv.setViewName("techbloom/outstorage/spareBillToOutStorage_edit");
        return mv;
    }
    /**
     * @Description: 获取备料单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     *//*
    @RequestMapping(value = "/getEditList")
    @ResponseBody
    public Map<String, Object> getEditList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",1);
        map1.put("materialNo","wl001");
        map1.put("materialName","白糖");
        map1.put("batchNo","bn001");
        map1.put("stockAmount","50");
        map1.put("weight","20kg");
        map1.put("spec","库位1");
        list.add(map1);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("id",2);
        map2.put("materialNo","wl002");
        map2.put("materialName","色素");
        map2.put("batchNo","bn002");
        map2.put("stockAmount","10");
        map2.put("weight","50kg");
        map2.put("spec","库位2");
        list.add(map2);

        map.put("rows",list);

        return map;
    }*/

    /**
     * 根据ID删除详情数据
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteSpareList")
    @ResponseBody
    public Map<String, Object> deleteSpareList(String ids, String spareBillCode) {
        Map<String, Object> map = Maps.newHashMap();
        map = spareBillService.deleteSpareList(ids, spareBillCode);
        return map;
    }

    /**
     * 将备料单生成对应的出库单
     *
     * @param id 备料单的id
     * @return
     */
    @RequestMapping(value = "/createOutStorage")
    @ResponseBody
    public Map<String, Object> createOutStorage(String id, String billType) {
        Map<String, Object> map = Maps.newHashMap();
        //通过备料单的ID获取对应的实体的信息
        SpareBillManagerVO spareBillManagerVO = spareBillService.selectById(id);
        //通过备料单的Id获取对应的下架单的信息
        List<SpareBillDetailManagerVO> spareBillDetailList = spareBillDetailService.spareBillDetailList(id);
        map = spareBillService.createOutStorage(spareBillManagerVO, spareBillDetailList, outStorageService.getMaxBillCode());
        return map;
    }

    /**
     * 备料单的保存
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/saveSpareBillDetail")
    @ResponseBody
    public Map<String, Object> saveSpareBillDetail(String spareBillJsonString, String json) {
        Map<String, Object> map = Maps.newHashMap();
        //插入主表的数据
        map = spareBillService.saveSpareBill(spareBillJsonString, json);
        return map;
    }

    /**
     * 备料单审核
     *
     * @param ids
     * @param type
     * @return
     */
    @RequestMapping(value = "/check")
    @ResponseBody
    public Map<String, Object> check(String ids, String type) {
        Map<String, Object> map = Maps.newHashMap();
        map = spareBillService.check(ids, type);
        return map;
    }

    /**
     * 返回到备料单详情页面
     *
     * @param spareBillId
     * @return
     * @author yuany
     * @date 2019-4-8
     */
    @RequestMapping(value = "/toDetail")
    public ModelAndView toDetail(String spareBillId) {
        ModelAndView mv = new ModelAndView();
        SpareBillManagerVO spareBillManagerVO = spareBillService.selectById(spareBillId);
        mv.addObject("spareBillManagerVO", spareBillManagerVO);
        mv.setViewName("techbloom/outstorage/spareBill_trueDetail");
        return mv;
    }

    /**
     * @Description: 获取备料单详情列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019/4/8
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String spareBillId) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("spareBillId", spareBillId);
        PageUtils PagePlatform = spareBillDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
        map.put("rows", PagePlatform.getList());
        executePageMap(map, page);

        return map;
    }

    /**
     * 生成出库单保存
     *
     * @param outStorageManagerVO
     * @return
     */
    @RequestMapping(value = "/saveOutStorage")
    @ResponseBody
    public Map<String, Object> saveOutStorage(OutStorageManagerVO outStorageManagerVO) {
        Map<String, Object> map = new HashMap<>();

        if (Strings.isNullOrEmpty(outStorageManagerVO.getSpareBillCode())) {
            map.put("msg", "未获取备料单编号");
            return map;
        }
        List<OutStorageManagerVO> outStorageManagerVOS = outStorageService.selectList(
                new EntityWrapper<OutStorageManagerVO>()
                        .eq("spare_bill_code", outStorageManagerVO.getSpareBillCode())
        );
        if (!outStorageManagerVOS.isEmpty() && outStorageManagerVOS.size() == 1) {
            OutStorageManagerVO storageManagerVO = outStorageManagerVOS.get(0);
            if (storageManagerVO.getBillType().equals(outStorageManagerVO.getBillType())) {
                map.put("msg", "此单据类型出库单已存在，不可再生成");
                return map;
            }
        } else if (!outStorageManagerVOS.isEmpty() && outStorageManagerVOS.size() == 2) {
            map.put("msg", "备料单已生成所有单据类型出库单，不可再生成出库单");
            return map;
        }


        //生产领料出库
        outStorageManagerVO.setOutstorageBillType(0L);
        map = outStorageService.saveOutstorage(outStorageManagerVO);

        //若出库单保存成功，则添加保存出库单详情
        if ((boolean) map.get("result")) {
            Long outStorageId = (Long) map.get("outstorageId");

            List<OutStorageManagerVO> outStorageManagerVOList = outStorageService.selectList(
                    new EntityWrapper<OutStorageManagerVO>()
                            .eq("spare_bill_code", map.get("spareBillCode"))
            );
            SpareBillManagerVO spareBillManagerVO = spareBillService.selectOne(
                    new EntityWrapper<SpareBillManagerVO>()
                            .eq("spare_bill_code", map.get("spareBillCode"))
            );
            //若同一个备料单出两个出库单，则备料单生成出库单完成,更备料单的改状态
            if (!outStorageManagerVOList.isEmpty() && spareBillManagerVO != null) {
                if (outStorageManagerVOList.size() == 1) {
                    spareBillManagerVO.setState("1");
                }
                if (outStorageManagerVOList.size() == 2) {
                    spareBillManagerVO.setState("2");
                }
                spareBillService.updateById(spareBillManagerVO);
            }
            //生成出库详情
            Map<String, Object> resultMap = new HashMap<>();
            resultMap = spareBillService.toOutStorageDetail(outStorageId);
            if (!(boolean) resultMap.get("result")) {
                map.put("msg", "无出库详情生成");
                return map;
            }
        }
        if ((boolean) map.get("result")) {
            map.put("msg", "单据保存成功");
        } else {
            map.put("msg", "单据保存失败");
        }

        return map;
    }

    /**
     * 导出Excel
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void customerExcel(String excelids) {
        List<SpareBillManagerVO> list = spareBillService.getAllList(excelids);
        spareBillService.toExcel(response, "", list);
    }
}
    