package com.tbl.modules.instorage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.QrCodeUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.InstorageDetail;
import com.tbl.modules.instorage.service.InstorageDetailService;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.outstorage.entity.OutStorageDetailManagerVO;
import com.tbl.modules.outstorage.service.OutStorageDetailService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库单管理controller
 *
 * @author anss
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/instorage")
public class InstorageController extends AbstractController {
    @Autowired
    private InstorageService instorageService;
    @Autowired
    private InstorageDetailService instorageDetailService;
    @Autowired
    private OutStorageDetailService outStorageDetailService;

    /**
    * @Description:  跳转到入库单列表页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/instorage/instorageBill/instorage_list");
        return mv;
    }

    /**
    * @Description:  获取入库单列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/getInstorageList")
    @ResponseBody
    public Map<String,Object> getInstorageList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        Page<Instorage> instoragePageList =  instorageService.queryPage(map);
        page.setTotalRows(instoragePageList.getTotal()==0?1:instoragePageList.getTotal());
        map.put("rows",instoragePageList.getRecords());
        executePageMap(map,page);
        return map;
    }

    /**
    * @Description:  获取出库单下拉列表数据源
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/23
    */
    @RequestMapping(value = "/getSelectOutstorageBill")
    @ResponseBody
    public Map<String, Object> getSelectOutstorageBill(String queryString, int pageNo, int pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> outstorageBillList =instorageService.getSelectOutstorageBillList(queryString, pageSize, pageNo);
        map.put("result", outstorageBillList);
        map.put("total", instorageService.getSelectOutstorageBillTotal(queryString));
        return map;
    }


    /**
     * @Description:  获取收货单下拉列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/2/23
     */
    @RequestMapping(value = "/getSelectReceiptPlan")
    @ResponseBody
    public Map<String, Object> getSelectReceiptPlan(String queryString, int pageNo, int pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> outstorageBillList =instorageService.getSelectReceiptPlanList(queryString, pageSize, pageNo);
        map.put("result", outstorageBillList);
        map.put("total", instorageService.getSelectOutstorageBillTotal(queryString));
        return map;
    }


    /**
     * 返回到入库单添加/修改页面
     * @author anss
     * @date 2018-12-28
     * @return
     */
    @RequestMapping(value="/toEdit")
    public ModelAndView toEdit(String instorageId,String edit) {
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isEmptyString(instorageId)){//跳转到新增页面
            //获取入库单编号
            String instorageBillCode = instorageService.generateInstorageCode();
            mv.addObject("instorageBillCode", instorageBillCode);
        }else{
            Instorage instorage = instorageService.selectById(instorageId);
            mv.addObject("instorageBillCode", instorage.getInstorageBillCode());
            mv.addObject("instorage", instorage);
        }
        mv.addObject("edit",edit);
        mv.setViewName("techbloom/instorage/instorageBill/instorage_edit");
        return mv;
    }

    /**
     * 返回到入库单详情页面
     * @author anss
     * @date 2019-3-4
     * @param instorageId
     * @return
     */
    @RequestMapping(value="/toDetail")
    public ModelAndView toDetail(String instorageId) {
        ModelAndView mv = new ModelAndView();
        Instorage instorage = instorageService.selectById(instorageId);
        mv.addObject("instorageBillCode", instorage.getInstorageBillCode());
        mv.addObject("instorage", instorage);
        mv.setViewName("techbloom/instorage/instorageBill/instorage_detail");
        return mv;
    }

    /**
    * @Description:  入库单的新增/修改 保存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value="/saveInstorage")
    @ResponseBody
    public Map<String,Object> saveInstorage(Instorage instorage){
        return instorageService.saveInstorage(instorage);
    }


    /**
    * @Description:  获取入库单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String,Object> getDetailList(String instorageId) {
        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("instorageId", instorageId);
        PageUtils PagePlatform = instorageDetailService.queryPage(map);
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
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/getSelectMaterial")
    @ResponseBody
    public Map<String, Object> getSelectMaterial(Long outstorageBillId,String queryString, int pageNo, int pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> materialList =instorageDetailService.getSelectMaterialList(outstorageBillId,queryString, pageSize, pageNo);
        map.put("result", materialList);
        map.put("total", instorageDetailService.getSelectMaterialTotal(outstorageBillId,queryString));
        return map;
    }

    /**
    * @Description:  保存入库单详情(物料详情)
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/saveInstorageDetail")
    @ResponseBody
    public Map<String,Object> saveInstorageDetail(Long instorageId,String materialCodes){
        Map<String,Object> map = new HashMap<>();
        boolean ret=false;
        String msg="";
        if(instorageDetailService.hasMaterial(instorageId,materialCodes)){
            msg="选择的物料已添加或者添加的物料有重复！";
        }else{
            ret=instorageDetailService.saveInstorageDetail(instorageId,materialCodes);
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
    * @Description:  删除入库单详情（物料详情）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    */
    @RequestMapping(value = "/deleteInstorageDetail")
    @ResponseBody
    public Map<String,Object> deleteInstorageDetail(String ids){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = instorageDetailService.deleteInstorageDetail(ids);
        map.put("result", result);
        return map;
    }

    /**
    * @Description:  更新入库单详情（物料详情）的批次号
    * @return:
    * @Author: zj
    * @Date: 2019/1/21
    * update by anss 2019-04-22
    * @Param: 批次号生成规则：供应商代码（7位，不够用*补位）+ 客户代码（9位，不够用*补位）+ “-” + 日期（yymmdd,6位）+ “-” + 流水号（1，最长3位）
    */
    @RequestMapping(value = "/updateBatchNo")
    @ResponseBody
    public Map<String,Object> updateBatchNo(Long instorageDetailId,String batchNo){
        Map<String,Object> map = new HashMap<>();
        boolean result=false;
        String msg="批次号修改失败！";
        //根据入库单详情id获取收货单状态
        String instorageState = instorageDetailService.getInstorageStateByDetailId(instorageDetailId);
        if(StringUtils.isNotEmpty(instorageState)){
            if("0".equals(instorageState)){
                result = instorageDetailService.updateBatchNo(instorageDetailId,batchNo);
                if(result){
                    msg="批次号修改成功！";
                }
            }else{
                msg +="订单已提交，批次号不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description:  更新入库单详情（物料详情）的生产日期
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/4/3
    */
    @RequestMapping(value = "/updateProductDate")
    @ResponseBody
    public Map<String,Object> updateProductDate(Long instorageDetailId,String productDate){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="生产日期修改失败！";
        //根据入库单详情id获取收货单状态
        String instorageState = instorageDetailService.getInstorageStateByDetailId(instorageDetailId);
        if(StringUtils.isNotEmpty(instorageState)){
            if("0".equals(instorageState)){
                result = instorageDetailService.updateProductDate(instorageDetailId,productDate);
                if(result){
                    msg="生产日期修改成功！";
                }
            }else{
                msg +="订单已提交，生产日期不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新入库单详情的入库数量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/9
     */
    @RequestMapping(value = "/updateInstorageAmount")
    @ResponseBody
    public Map<String,Object> updateInstorageAmount(Long instorageDetailId,String instorageAmount){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="数量修改失败！";
        //根据入库单详情id获取收货单状态
        String instorageState = instorageDetailService.getInstorageStateByDetailId(instorageDetailId);
        if(StringUtils.isNotEmpty(instorageState)){
            if("0".equals(instorageState)){
                result = instorageDetailService.updateInstorageAmount(instorageDetailId,instorageAmount);
                if(result){
                    msg="数量修改成功！";
                }
            }else{
                msg +="订单已提交，数量不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新入库单详情的入库重量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/10
     */
    @RequestMapping(value = "/updateInstorageWeight")
    @ResponseBody
    public Map<String,Object> updateInstorageWeight(Long instorageDetailId,String instorageWeight){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="重量修改失败！";
        //根据入库单详情id获取收货单状态
        String instorageState = instorageDetailService.getInstorageStateByDetailId(instorageDetailId);
        if(StringUtils.isNotEmpty(instorageState)){
            if("0".equals(instorageState)){
                result = instorageDetailService.updateInstorageWeight(instorageDetailId,instorageWeight);
                if(result){
                    msg="重量修改成功！";
                }
            }else{
                msg +="订单已提交，重量不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description:  入库单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    @RequestMapping(value="/submitInstorage")
    @ResponseBody
    public Map<String,Object> submitInstorage(Long instorageId){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = true;
        String msg = "";
        Instorage instorage = instorageService.selectById(instorageId);
        //入库流程（0：一般流程；1：白糖流程）
        String instorageProcess = instorage.getInstorageProcess();
        //入库单类型
        String instorageType = instorage.getInstorageType();
        //出库单id
        Long outstorageBillId = instorage.getOutstorageBillId();
        //根据入库单id查询入库单详情
        EntityWrapper<InstorageDetail> entity = new EntityWrapper<InstorageDetail>();
        entity.eq("instorage_bill_id",instorageId);
        List<InstorageDetail> lstInstorageDetail = instorageDetailService.selectList(entity);
        if(lstInstorageDetail != null && lstInstorageDetail.size() > 0){
            for(InstorageDetail instorageDetail : lstInstorageDetail){
                //入库数量
                Double instorageAmount = StringUtils.isEmptyString(instorageDetail.getInstorageAmount())?0d:Double.parseDouble(instorageDetail.getInstorageAmount());
                //入库重量
                Double instorageWeight = StringUtils.isEmptyString(instorageDetail.getInstorageWeight())?0d:Double.parseDouble(instorageDetail.getInstorageWeight());
                //物料编号
                String materialCode = instorageDetail.getMaterialCode();
                //物料名称
                String materialName = instorageDetail.getMaterialName();
                //批次号
                String batchNo = instorageDetail.getBatchNo();
                //生产日期
                String productDate = instorageDetail.getProductDate();
                if(StringUtils.isEmptyString(batchNo)){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialName+"】的批次号是否填写";
                    break;
                }
                if(StringUtils.isEmptyString(productDate)){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialName+"】的生产日期是否填写";
                    break;
                }
                if(instorageAmount <= 0){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialName+"】的入库数量填写正确";
                    break;
                }
                if(instorageWeight <= 0){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialName+"】的入库重量填写正确";
                    break;
                }
                /**如果是“生产退货入库”类型的入库单**/
                if("2".equals(instorageType)){
                    //(1)验证入库的物料数量和重量是否会超过出库单的可拆分数量和重量
                    EntityWrapper<OutStorageDetailManagerVO> outStorageDetailEntity = new EntityWrapper<OutStorageDetailManagerVO>();
                    outStorageDetailEntity.eq("material_code",materialCode);
                    outStorageDetailEntity.eq("batch_no",batchNo);
                    outStorageDetailEntity.eq("outstorage_bill_id",outstorageBillId);
                    OutStorageDetailManagerVO outStorageDetail = outStorageDetailService.selectOne(outStorageDetailEntity);
                    //出库单的可拆分数量
                    Double separableAmount = outStorageDetail.getSeparableAmount()==null?0d:Double.parseDouble(outStorageDetail.getSeparableAmount());
                    //出库单的可拆分重量
                    Double separableWeight = outStorageDetail.getSeparableWeight()==null?0d:Double.parseDouble(outStorageDetail.getSeparableWeight());

                    if(instorageAmount > separableAmount){
                        result = false;
                        msg = "提交失败！请确认物料【"+materialName+"】的入库数量不能大于对应出库单的可拆分数量";
                        break;
                    }
                    if(instorageWeight > separableWeight){
                        result = false;
                        msg = "提交失败！请确认物料【"+materialName+"】的入库重量不能大于对应出库单的可拆分重量";
                        break;
                    }
                }
            }
        }else{
            result = false;
            msg = "提交失败！请填写物料信息";
        }

        //如果是白糖类型的单据
        if("1".equals(instorageProcess)){
            //查询入库单详情物料种类是否只有一种
            boolean resultType = instorageDetailService.selectMaterialType(instorageId);
            if(!resultType){
                result = false;
                msg = "提交失败！请确认白糖类型单据只有一种物料";
            }
        }

        if(result){
            //获取入库单的状态
            String state = instorage.getState();
            //是否越库（0：否；1：是）
            String crossDocking = instorage.getCrossDocking();
            if("0".equals(state)){
                try{
                    instorageService.updateInstorageState(instorageId,instorageType,crossDocking,outstorageBillId,lstInstorageDetail);
                    result=true;
                    msg="提交成功！";
                }catch (Exception e){
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
    * @Description:  删除入库单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    @RequestMapping(value="/deleteInstorage")
    @ResponseBody
    public Map<String,Object> deleteInstorage(Long id){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="";

        Instorage instorage = instorageService.selectById(id);
        //获取入库单状态
        String instorageState = instorage.getState();
        //获取入库单类型
        String instorageType = instorage.getInstorageType();

        if(StringUtils.isNotEmpty(instorageState)){
            if("0".equals(instorageState)){
                try{
                    instorageService.deleteInstorage(instorageType,id);
                    result = true;
                    msg="入库单删除成功！";
                }catch (Exception e){
                    e.printStackTrace();
                    result = false;
                    msg="入库单删除失败！";
                }
            }else{
                msg +="入库单已提交，不可删除";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;

    }

    /**
    * @Description:  生成上架单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    @RequestMapping(value="/generatePutBill")
    @ResponseBody
    public Map<String,Object> generatePutBill(Long instorageId){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="";
        try{
            Instorage instorage = instorageService.selectById(instorageId);
            //是否越库（0：否；1：是）
            String crossDocking = instorage.getCrossDocking();
            if("1".equals(crossDocking)){
                result = false;
                msg="越库的入库单不可生成上架单！";
            }else{
                instorageService.generatePutBill(instorageId);
                result = true;
                msg="生成上架单成功！";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg="生成上架单失败！";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;

    }

    /**
     * 入库单详情二维码打印
     *
     * @return
     * @author yuany
     * @date 2019-04-01
     */
    @RequestMapping(value = "/matrixCode")
    @ResponseBody
    public ModelAndView tomatrixCode(String ids) {

        ModelAndView mv = this.getModelAndView();

        List<Long> lstid = Arrays.stream(ids.split(",")).map(a -> Long.parseLong(a)).collect(Collectors.toList());
        //根据入库单详情ID获取需要生成二维码的信息
        List<InstorageDetail> instorageDetailList = instorageDetailService.selectBatchIds(lstid);

        //存储数据的集合
        List<Map<String, Object>> matrixCodeList = new ArrayList<>();

        //判断根据id获取的入库单详情集合是否为空
        if (!instorageDetailList.isEmpty()) {
            for (InstorageDetail instorageDetail : instorageDetailList) {
                Map<String, Object> map = new HashMap<>();
                //二维码内容/宽/高/
                String text = instorageDetail.getMaterialCode() +","+instorageDetail.getMaterialName()+","+instorageDetail.getBatchNo()+","
                        +instorageDetail.getInstorageAmount()+instorageDetail.getUnit()+"/"+instorageDetail.getInstorageWeight()+"kg,"+instorageDetail.getProductDate();
                String binary =
                        QrCodeUtils.creatRrCode(text, 400, 400);
                map.put("binary", binary);
                map.put("materielCode", instorageDetail.getMaterialCode());
                map.put("materielName", instorageDetail.getMaterialName());
                map.put("batchNo", instorageDetail.getBatchNo());
                map.put("instorageAmountAndWeight", instorageDetail.getInstorageAmount()+instorageDetail.getUnit()+"/"+instorageDetail.getInstorageWeight()+"kg");
                map.put("productDate", instorageDetail.getProductDate());
                matrixCodeList.add(map);
            }
        }

        //放入页面数据
        mv.addObject("matrixCodeList", matrixCodeList);
        //跳转地址
        mv.setViewName("techbloom/instorage/instorageBill/instorage_matrixCode");
        return mv;
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
        List<Instorage> list = instorageService.getAllList(ids);
        instorageService.toExcel(response, "", list);
    }
}
