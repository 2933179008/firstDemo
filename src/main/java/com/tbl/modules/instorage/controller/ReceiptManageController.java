package com.tbl.modules.instorage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.entity.ReceiptDetail;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.instorage.service.ReceiptService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

/**
 * 收货计划controller
 *
 * @author anss
 * @date 2018-12-26
 */
@Controller
@RequestMapping(value = "/receiptManage")
public class ReceiptManageController extends AbstractController {
    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptDetailService receiptDetailService;
    /**
     * 跳转到收货单列表页面
     * @author anss
     * @date 2018-12-26
     * @return
     */
    @RequestMapping(value = "/toList")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/instorage/receiptManage/receiptManage_list");
        return mv;
    }

    /**
     * 获取收货单列表数据
     * @author anss
     * @date 2018-12-26
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String,Object> getReceiptList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils PagePlatform = receiptService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount()==0?1:PagePlatform.getTotalCount());
        map.put("rows",PagePlatform.getList());
        executePageMap(map,page);
        return map;
    }

    /**
     * @Description:  跳转到收货单添加/修改页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/7
     */
    @RequestMapping(value="/toEdit")
    public ModelAndView toEdit(String receiptPlanId,String edit) {
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isEmptyString(receiptPlanId)){//跳转到新增页面
            //获取收货单编号
            String receiptCode = receiptService.generateReceiptCode();
            mv.addObject("receiptCode", receiptCode);
        }else{
            Receipt receipt = receiptService.selectById(receiptPlanId);
            mv.addObject("receiptCode", receipt.getReceiptCode());
            mv.addObject("receipt", receipt);
        }
        mv.addObject("edit",edit);
        mv.setViewName("techbloom/instorage/receiptManage/receiptManage_edit");
        return mv;
    }

    /**
     * @Description:  跳转到收货单详情页面
     * @Author: anss
     * @Date: 2019/3/2
     */
    @RequestMapping(value="/toDetail")
    public ModelAndView toDetail(String receiptPlanId) {
        ModelAndView mv = new ModelAndView();
        Receipt receipt = receiptService.selectById(receiptPlanId);
        mv.addObject("receiptCode", receipt.getReceiptCode());
        mv.addObject("receipt", receipt);
        mv.setViewName("techbloom/instorage/receiptManage/receiptManage_detail");
        return mv;
    }

    /**
     * @Description: 获取供应商下拉列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    @RequestMapping(value="/getSelectSupplier")
    @ResponseBody
    public Map<String,Object> getSelectSupplier(String queryString,int pageSize,int pageNo){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> supplierList =receiptService.getSelectSupplierList(queryString, pageSize, pageNo);
        map.put("result", supplierList);
        map.put("total", receiptService.getSelectSupplierTotal(queryString));
        return map;
    }

    /**
     * @Description:  获取货主（客户）下拉列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    @RequestMapping(value="/getSelectCustomer")
    @ResponseBody
    public Map<String,Object> getSelectCustomer(String queryString,int pageSize,int pageNo){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> customerList =receiptService.getSelectCustomerList(queryString, pageSize, pageNo);
        map.put("result", customerList);
        map.put("total", receiptService.getSelectCustomerTotal(queryString));
        return map;
    }

    /**
     * @Description:  收货单的新增/修改 保存
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    @RequestMapping(value="/saveReceipt")
    @ResponseBody
    public Map<String,Object> saveReceipt(Receipt receipt){
        return receiptService.saveReceipt(receipt);
    }

    /**
     * @Description:  收货单提交
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/11
     */
    @RequestMapping(value="/submitReceipt")
    @ResponseBody
    public Map<String,Object> submitReceipt(Long receiptId){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = true;
        String msg = "";
        //根据收货单id查询收货单详情
        EntityWrapper<ReceiptDetail> entity = new EntityWrapper<ReceiptDetail>();
        entity.eq("receipt_plan_id",receiptId);
        List<ReceiptDetail> lstReceiptDetail = receiptDetailService.selectList(entity);
        if(lstReceiptDetail != null && lstReceiptDetail.size() > 0){
            for(ReceiptDetail receiptDetail : lstReceiptDetail){
                //计划收货数量
                String planReceiptAmount = receiptDetail.getPlanReceiptAmount();
                //计划收货重量
                String planReceiptWeight = receiptDetail.getPlanReceiptWeight();

                if(StringUtils.isEmptyString(planReceiptAmount) || Double.valueOf(planReceiptAmount) <= 0){
                    result = false;
                    msg = "提交失败！，请确认物料【"+receiptDetail.getMaterialName()+"】的计划收货数量填写正确";
                }
                if(StringUtils.isEmptyString(planReceiptWeight) || Double.parseDouble(planReceiptWeight) <= 0){
                    result = false;
                    msg = "提交失败！，请确认物料【"+receiptDetail.getMaterialName()+"】的计划收货重量填写正确";
                }

            }
        }else{
            result = false;
            msg = "提交失败！请填写物料信息";
        }

        if(result){
            //获取收货计划单的状态
            String state = receiptService.selectById(receiptId).getState();
            if("0".equals(state)){
                result = receiptService.updateReceiptState(receiptId);
                if(result){
                    msg="提交成功！";
                }else{
                    result=false;
                    msg="提交失败！";
                }
            }else{
                result=false;
                msg="提交失败！该单据不可提交";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  收货单删除
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    @RequestMapping(value="/deleteRecipt")
    @ResponseBody
    public Map<String,Object> deleteRecipt(Long id){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="收货单删除失败！";
        //根据收货单id获取收货单状态
        String receiptState = receiptService.selectById(id).getState();
        if(StringUtils.isNotEmpty(receiptState)){
            if("0".equals(receiptState)){
                //删除收货单
                boolean result1 = receiptService.deleteById(id);
                //删除收货单详情
                EntityWrapper<ReceiptDetail> entity = new EntityWrapper<ReceiptDetail>();
                entity.eq("receipt_plan_id",id);
                boolean result2 = receiptDetailService.delete(entity);
                if(result1 && result2){
                    result = true;
                    msg="收货单删除成功！";
                }
            }else{
                msg +="订单已提交或者已收货，不可删除";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;

    }


    /**
     * @Description:  获取收货单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String,Object> getDetailList(String receiptId) {
        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("receiptId", receiptId);
        PageUtils PagePlatform = receiptDetailService.queryPage(map);
        page.setTotalRows(PagePlatform.getTotalCount()==0?1:PagePlatform.getTotalCount());
        map.put("rows",PagePlatform.getList());
        executePageMap(map,page);
        return map;
    }


    /**
     * @Description:  获取物料下拉列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/8
     */
    @RequestMapping(value = "/getSelectMaterial")
    @ResponseBody
    public Map<String, Object> getSelectMaterial(String queryString, int pageNo, int pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> materialList =receiptDetailService.getSelectMaterialList(queryString, pageSize, pageNo);
        map.put("result", materialList);
        map.put("total", receiptDetailService.getSelectMaterialTotal(queryString));
        return map;

    }

    /**
     * @Description:  保存收货单详情(物料详情)
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    @RequestMapping(value = "/saveReceiptDetail")
    @ResponseBody
    public Map<String,Object> saveReceiptDetail(Long receiptId,String materialCodes){
        Map<String,Object> map = new HashMap<>();
        boolean ret=false;
        String msg="";
        if(receiptDetailService.hasMaterial(receiptId,materialCodes)){
            msg="选择的物料已添加！";
        }else{
            ret=receiptDetailService.saveReceiptDetail(receiptId,materialCodes);
            if(ret){
                msg = "添加成功！";
            }else{
                msg = "添加失败！";
            }
        }
        map.put("result", ret);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新收货单详情（物料详情）的批次号
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    @RequestMapping(value = "/updateBatchNo")
    @ResponseBody
    public Map<String,Object> updateBatchNo(Long receiptDetailId,String batchNo){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="批次号修改失败！";
        //根据收货单详情id获取收货单状态
        String receiptState = receiptDetailService.getReceiptStateByDetailId(receiptDetailId);
        if(StringUtils.isNotEmpty(receiptState)){
            if("0".equals(receiptState)){
                result = receiptDetailService.updateBatchNo(receiptDetailId,batchNo);
                if(result){
                    msg="批次号修改成功！";
                }
            }else{
                msg +="订单已提交或者已收货，批次号不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新收货单详情的计划收货数量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    @RequestMapping(value = "/updatePlanReceiptAmount")
    @ResponseBody
    public Map<String,Object> updatePlanReceiptAmount(Long receiptDetailId,String planReceiptAmount){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="数量修改失败！";
        //根据收货单详情id获取收货单状态
        String receiptState = receiptDetailService.getReceiptStateByDetailId(receiptDetailId);
        if(StringUtils.isNotEmpty(receiptState)){
            if("0".equals(receiptState)){
                result = receiptDetailService.updatePlanReceiptAmount(receiptDetailId,planReceiptAmount);
                if(result){
                    msg="数量修改成功！";
                }
            }else{
                msg +="订单已提交或者已收货，数量不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新收货单详情的计划收货重量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    @RequestMapping(value = "/updatePlanReceiptWeight")
    @ResponseBody
    public Map<String,Object> updatePlanReceiptWeight(Long receiptDetailId,String planReceiptWeight){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="重量修改失败！";
        //根据收货单详情id获取收货单状态
        String receiptState = receiptDetailService.getReceiptStateByDetailId(receiptDetailId);
        if(StringUtils.isNotEmpty(receiptState)){
            if("0".equals(receiptState)){
                result = receiptDetailService.updatePlanReceiptWeight(receiptDetailId,planReceiptWeight);
                if(result){
                    msg="重量修改成功！";
                }
            }else{
                msg +="订单已提交或者已收货，重量不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }


    /**
     * @Description:  删除收货单详情（物料详情）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    @RequestMapping(value = "/deleteReceiptDetail")
    @ResponseBody
    public Map<String,Object> deleteReceiptDetail(String ids){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = receiptDetailService.deleteReceiptDetail(ids);
        map.put("result", result);
        return map;
    }

    /**
     * @Description:  跳转到收货页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/11
     */
    @RequestMapping(value="/toReceiptView")
    @ResponseBody
    public ModelAndView toReceiptView(Long receiptPlanId){
        ModelAndView mv = new ModelAndView();
        mv.addObject("receiptPlanId", receiptPlanId);
        mv.setViewName("techbloom/instorage/receiptManage/receiptManage_receipt");
        return mv;
    }

    /**
     * @Description:  确认生成入库单
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/15
     */
    @RequestMapping(value="/generateInstorage")
    @ResponseBody
    public Map<String,Object> generateInstorage(String receiptPlanId,String receiptPlanDetailIdStr,
                                                String inStorageAmountStr, String inStorageWeightStr,String batchNoStr,String productDateStr,String qualityPeriodStr){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean code = false;
        String msg = "";

        try{
            receiptDetailService.generateInstorage(receiptPlanId,receiptPlanDetailIdStr,inStorageAmountStr,inStorageWeightStr,batchNoStr,productDateStr,qualityPeriodStr);
            code = true;
            msg = "生成入库单成功！";
        }catch (Exception e){
            e.printStackTrace();
            code = false;
            msg = "系统错误！";
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        return resultMap;
    }

    /**
    * @Description:  异常收货
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/4
    */
    @RequestMapping(value="/abnormalReceipt")
    @ResponseBody
    public Map<String,Object> abnormalReceipt(Long receiptId){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        String msg = "";
        try{
            //异常收货
            receiptService.updateReceiptAbnormalState(receiptId);
            result = true;
            msg = "异常收货成功！";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "系统错误！";
        }
        resultMap.put("result",result);
        resultMap.put("msg",msg);
        return resultMap;
    }
    /**
     * 导出Excel
     *
     * @author pz
     * @date 2019-01-08
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void customerExcel(String ids) {
        List<Receipt> list = receiptService.getAllList(ids);
        receiptService.toExcel(response, "", list);
    }
}
