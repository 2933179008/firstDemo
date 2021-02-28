package com.tbl.modules.instorage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.instorage.entity.QualityBill;
import com.tbl.modules.instorage.entity.QualityBillDetail;
import com.tbl.modules.instorage.service.QualityBillDetailService;
import com.tbl.modules.instorage.service.QualityBillService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description:  质检收货
* @Param:
* @return:
* @Author: zj
* @Date: 2019/2/11
*/
@Controller
@RequestMapping(value = "/qualityBill")
public class QualityBillController extends AbstractController {
    @Value("${dyyl.qualifiedRate}")
    private String qualifiedRate;

    @Autowired
    private QualityBillService qualityBillService;
    @Autowired
    private QualityBillDetailService qualityBillDetailService;
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    /**
     * 跳转到质检收货列表页面
     * @author anss
     * @date 2018-12-27
     * @return
     */
    @RequestMapping(value = "/toList")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/instorage/qualityBill/qualityBill_list");
        return mv;
    }

    /**
    * @Description:  获取质检收货列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Map<String,Object> getList(String queryJsonString) {
        Map<String,Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        Page<QualityBill> qualityBillPageList =  qualityBillService.queryPage(map);
        page.setTotalRows(qualityBillPageList.getTotal()==0?1:qualityBillPageList.getTotal());
        map.put("rows",qualityBillPageList.getRecords());
        executePageMap(map,page);
        return map;
    }

    /**
    * @Description:  跳转到质检收货添加/修改页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/11
    */
    @RequestMapping(value="/toEdit")
    @ResponseBody
    public ModelAndView toEdit(String qualityBillId,String edit) {
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isEmptyString(qualityBillId)){//跳转到新增页面
            //获取质检单编号
            String qualityCode = qualityBillService.generateQualityCode();
            mv.addObject("qualityCode", qualityCode);
        }else{
            QualityBill qualityBill = qualityBillService.selectById(qualityBillId);
            mv.addObject("qualityCode", qualityBill.getQualityCode());
            mv.addObject("qualityBill", qualityBill);
        }
        mv.addObject("edit",edit);
        mv.setViewName("techbloom/instorage/qualityBill/qualityBill_edit");
        return mv;
    }

    /**
     * 跳转到质检收货详情页面
     * @Author: anss
     * @Date: 2019/3/4
     * @param qualityBillId
     * @return
     */
    @RequestMapping(value="/toDetail")
    @ResponseBody
    public ModelAndView toDetail(String qualityBillId) {
        ModelAndView mv = new ModelAndView();
        QualityBill qualityBill = qualityBillService.selectById(qualityBillId);
        mv.addObject("qualityCode", qualityBill.getQualityCode());
        mv.addObject("qualityBill", qualityBill);
        mv.setViewName("techbloom/instorage/qualityBill/qualityBill_detail");
        return mv;
    }


    /**
     * @Description:  质检单的新增/修改 保存
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/21
     */
    @RequestMapping(value="/saveQualityBill")
    @ResponseBody
    public Map<String,Object> saveQualityBill(QualityBill qualityBill){
        return qualityBillService.saveQualityBill(qualityBill);
    }

    /**
     * @Description:  获取入库单下拉框数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/23
     */
    @RequestMapping(value="/getSelectInstorage")
    @ResponseBody
    public Map<String,Object> getSelectInstorage(String queryString,int pageSize,int pageNo){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> instorageList =qualityBillService.getSelectInstorageList(queryString, pageSize, pageNo);
        map.put("result", instorageList);
        map.put("total", qualityBillService.getSelectInstorageTotal(queryString));
        return map;
    }


    /**
     * @Description:  获取物料下拉列表数据源
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/getSelectMaterial")
    @ResponseBody
    public Map<String, Object> getSelectMaterial(Long instorageBillId, String queryString, int pageNo, int pageSize) {
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> materialList =qualityBillService.getSelectMaterialList(instorageBillId, queryString, pageSize, pageNo);
        map.put("result", materialList);
        map.put("total", qualityBillService.getSelectMaterialTotal(instorageBillId, queryString));
        return map;
    }


    /**
     * @Description:  保存质检单详情(物料详情)
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/saveQualityBillDetail")
    @ResponseBody
    public Map<String,Object> saveQualityBillDetail(Long qualityId,String materialCodes){
        Map<String,Object> map = new HashMap<>();
        boolean ret=false;
        String msg="";
        try{
            if(qualityBillDetailService.hasMaterial(qualityId,materialCodes)){
                ret = false;
                msg="选择的物料已添加！";
            }else{
                qualityBillDetailService.saveQualityBillDetail(qualityId,materialCodes);
                ret = true;
                msg = "添加成功！";
            }

        }catch (Exception e){
            ret = false;
            msg = "添加失败！";
        }
        map.put("result", ret);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  获取质检单详情列表
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String,Object> getDetailList(String qualityId) {
        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("qualityId", qualityId);
        Page<QualityBillDetail> qualityBillDetailPageList = qualityBillDetailService.queryPage(map);
        page.setTotalRows(qualityBillDetailPageList.getTotal()==0?1:qualityBillDetailPageList.getTotal());
        map.put("rows",qualityBillDetailPageList.getRecords());
        executePageMap(map,page);

        return map;
    }

    /**
     * @Description:  删除质检单详情（物料详情）
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/deleteQualityBillDetail")
    @ResponseBody
    public Map<String,Object> deleteQualityBillDetail(String ids){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = qualityBillDetailService.deleteQualityBillDetail(ids);
        map.put("result", result);
        return map;
    }

    /**
    * @Description:  删除质检单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/18
    */
    @RequestMapping(value = "/deleteQualityBill")
    @ResponseBody
    public Map<String,Object> deleteQualityBill(Long id){
        Map<String,Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        try{
            qualityBillService.deleteQualityBill(id);
            result = true;
            msg = "删除质检单成功";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "删除质检单失败";
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
    }


    /**
     * @Description:  更新质检单的样本重量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/28
     */
    @RequestMapping(value = "/updateSampleWeight")
    @ResponseBody
    public Map<String,Object> updateSampleWeight(Long qualityBillDetailId,String sampleWeight){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="";
        try{
            qualityBillDetailService.updateSampleWeight(qualityBillDetailId,sampleWeight);
            result = true;
            msg = "样本重量修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "样本重量修改失败！";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description:  更新质检单的合格重量
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2019/1/28
     */
    @RequestMapping(value = "/updateQualifiedWeight")
    @ResponseBody
    public Map<String,Object> updateQualifiedWeight(Long qualityBillDetailId,String qualifiedWeight){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="";
        try{
            qualityBillDetailService.updateQualifiedWeight(qualityBillDetailId,qualifiedWeight);
            result = true;
            msg = "合格重量修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "合格重量修改失败！";
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description: 计算合格率
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/13
    */
    @RequestMapping(value="/calculate")
    @ResponseBody
    public Map<String,Object> calculate(String id,String sampleWeight,String qualifiedWeight){
        Map<String,Object> map = Maps.newHashMap();
        boolean result = false;
        String msg = "";
        double rate=0.0;       //合格率
        double sampleWeight1 = Double.parseDouble(sampleWeight);
        double qualifiedWeight1 = Double.parseDouble(qualifiedWeight);

        rate=qualifiedWeight1/sampleWeight1*100;
        double quatifiedRate= Double.parseDouble(qualifiedRate);
        if(sampleWeight1<qualifiedWeight1){
            msg = "合格重量不能大于样本重量";
        }

        if(rate < quatifiedRate){
            //更新该状态为不合格的状态
            result = qualityBillDetailService.updateDetailState(id,"1",rate);
        }else{
            //更新状态为合格
            result = qualityBillDetailService.updateDetailState(id,"0",rate);
        }

        map.put("msg",msg);
        map.put("result",result);
        return map;
    }

    /**
    * @Description:  质检单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/2/14
    */
    @RequestMapping(value="/submitQualityBill")
    @ResponseBody
    public Map<String,Object> submitQualityBill(String qualityId){
        Map<String,Object> map = Maps.newHashMap();
        boolean result = true;
        String msg = "";
        try{

            /** 验证质检单提交条件 **/
            EntityWrapper<QualityBillDetail> qualityBillDetailEntity = new EntityWrapper<QualityBillDetail>();
            qualityBillDetailEntity.eq("quality_id",qualityId);
            List<QualityBillDetail> lstQualityBillDetail = qualityBillDetailService.selectList(qualityBillDetailEntity);
            if(lstQualityBillDetail != null && lstQualityBillDetail.size()>0){
                for(QualityBillDetail qualityBillDetail:lstQualityBillDetail){
                    if(StringUtils.isEmpty(qualityBillDetail.getPassRate())){
                        result = false;
                        msg = "请确认数据都已填写！";
                        break;
                    }
                }
            }else{
                result = false;
                msg = "请添加物料！";
            }

            //根据质检单id查询对应的入库单的入库流程（0：一般流程；1：白糖流程）和入库单状态
            Map<String,Object> instorageMap = qualityBillService.getInstorageProcess(qualityId);
            //入库流程（0：一般流程；1：白糖流程）
            String instorageProcess = "";
            //入库单状态
            String state = "";
            if(instorageMap != null){
                instorageProcess = instorageMap.get("instorage_process")==null?"":instorageMap.get("instorage_process").toString();
                state = instorageMap.get("state")==null?"":instorageMap.get("state").toString();
            }

            //如果是白糖流程的单据，注：白糖流程的质检单需要物料入库存之后，并且入库单状态为已完成（即对应的rfid都做了绑定）才可以做质检操作
//            if("1".equals(instorageProcess)){
//                //根据质检单id查询质检单对应上架单详情
//                List<Map<String,Object>> lstPutBillDetail = qualityBillService.getPutBillDetail(qualityId);
//                if(lstPutBillDetail!=null && lstPutBillDetail.size()>0){
//                    OUT:
//                    for(Map<String,Object> map1 : lstPutBillDetail){
//                        //上架单详情状态（0：未上架；1：已上架）
//                        String putBillDetailState = map1.get("state")==null?"0":map1.get("state").toString();
//                        //上架单详情的rfid
//                        String rfid = map1.get("rfid")==null?"":map1.get("rfid").toString();
//                        if("0".equals(putBillDetailState)){//如果有未上架的物料，则不给提交
//                            result = false;
//                            msg = "提交失败！白糖流程的单据需要绑定RFID之后才可质检";
//                            break;
//                        }
//                        /**验证RFID是否已绑定了物料**/
//                        EntityWrapper<MaterielBindRfidDetail> entity = new EntityWrapper<MaterielBindRfidDetail>();
//                        entity.eq("rfid",rfid);
//                        //未删除
//                        entity.eq("delete_flag","1");
//                        //已入库状态
//                        entity.eq("status","1");
//                        List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = materielBindRfidDetailService.selectList(entity);
//                        if(lstMaterielBindRfidDetail!=null && lstMaterielBindRfidDetail.size()>0){
//                            //注：如果有多条数据，表示一个rfid绑定了多种物料，不过因为白糖流程的物料种类只有一种，
//                            // 所以这边基本上不会存在多条数据的情况，但这边先预留着防止后面修改
//                            for(MaterielBindRfidDetail materielBindRfidDetail : lstMaterielBindRfidDetail){
//                                //绑定的数量
//                                Double amount = StringUtils.isEmpty(materielBindRfidDetail.getAmount())?0d:Double.parseDouble(materielBindRfidDetail.getAmount());
//                                //绑定的重量
//                                Double weight = StringUtils.isEmpty(materielBindRfidDetail.getWeight())?0d:Double.parseDouble(materielBindRfidDetail.getWeight());
//                                //如果绑定的数量和重量都为0，则视为未绑定
//                                if(amount==0 && weight==0){
//                                    result = false;
//                                    msg = "提交失败！白糖流程的单据需要绑定RFID之后才可质检";
//                                    //跳出外层循环
//                                    break OUT;
//                                }
//                            }
//                        }else{
//                            result = false;
//                            msg = "提交失败！白糖流程的单据需要绑定RFID之后才可质检";
//                            break;
//                        }
//
//                    }
//                }else{
//                    result = false;
//                    msg = "提交失败！白糖流程的单据需要绑定RFID之后才可质检";
//                }
//
//            }
            if("1".equals(instorageProcess) && !"3".equals(state)){
                result = false;
                msg = "提交失败！白糖流程的单据需要入库单完成后才可质检";
            }
            if(result){
                qualityBillService.submitQualityBill(qualityId);
                result = true;
                msg = "提交成功！";
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "提交失败！";
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
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
        List<QualityBill> list = qualityBillService.getAllList(ids);
        qualityBillService.toExcel(response, "", list);
    }
}
