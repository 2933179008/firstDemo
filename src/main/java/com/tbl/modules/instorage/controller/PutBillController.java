package com.tbl.modules.instorage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.PutBillDetail;
import com.tbl.modules.instorage.service.InstorageService;
import com.tbl.modules.instorage.service.PutBillDetailService;
import com.tbl.modules.instorage.service.PutBillService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.service.MaterielBindRfidService;
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
* @Description:  上架单管理controller
* @Param:
* @return:
* @Author: zj
* @Date: 2019/1/22
*/
@Controller
@RequestMapping(value = "/putBill")
public class PutBillController extends AbstractController {
    @Autowired
    private PutBillService putBillService;
    @Autowired
    private PutBillDetailService putBillDetailService;
    @Autowired
    private InstorageService instorageService;
    @Autowired
    private MaterielBindRfidService materielBindRfidService;



    /**
    * @Description:  跳转到上架单管理列表页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    @RequestMapping(value = "/toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/instorage/putBill/putBill_list");
        return mv;
    }

    /**
    * @Description:  获取上架单管理列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
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
        Page<PutBill> instoragePageList =  putBillService.queryPage(map);
        page.setTotalRows(instoragePageList.getTotal()==0?1:instoragePageList.getTotal());
        map.put("rows",instoragePageList.getRecords());
        executePageMap(map,page);
        return map;
    }

    /**
    * @Description:  跳转到上架单添加/编辑页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/22
    */
    @RequestMapping(value="/toEdit")
    public ModelAndView toEdit(String putBillId,String edit) {
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isEmptyString(putBillId)){//跳转到新增页面
            //获取上架单编号
            String putBillCode = putBillService.generatePutBillCode();
            mv.addObject("putBillCode", putBillCode);
        }else{
            PutBill putBill = putBillService.selectById(putBillId);
            mv.addObject("putBillCode", putBill.getPutBillCode());
            mv.addObject("putBill", putBill);
        }
        mv.addObject("edit",edit);
        mv.setViewName("techbloom/instorage/putBill/putBill_edit");
        return mv;
    }

    /**
     * 跳转到上架单详情页面
     * @Author: anss
     * @Date: 2019/3/4
     * @param putBillId
     * @return
     */
    @RequestMapping(value="/toDetail")
    public ModelAndView toDetail(String putBillId) {
        ModelAndView mv = new ModelAndView();
        PutBill putBill = putBillService.selectById(putBillId);
        mv.addObject("putBillCode", putBill.getPutBillCode());
        mv.addObject("putBill", putBill);
        mv.setViewName("techbloom/instorage/putBill/putBill_detail");
        return mv;
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
        List<Map<String, Object>> instorageList =putBillService.getSelectInstorageList(queryString, pageSize, pageNo);
        map.put("result", instorageList);
        map.put("total", putBillService.getSelectInstorageTotal(queryString));
        return map;
    }

    /**
    * @Description:  获取操作人（用户）下拉框数据源
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/23
    */
    @RequestMapping(value="/getSelectOperator")
    @ResponseBody
    public Map<String,Object> getSelectOperator(String queryString,int pageSize,int pageNo){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String, Object>> operatorList =putBillService.getSelectOperatorList(queryString, pageSize, pageNo);
        map.put("result", operatorList);
        map.put("total", putBillService.getSelectOperatorTotal(queryString));
        return map;
    }

    /**
    * @Description:  上架单的新增/修改 保存
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    @RequestMapping(value="/savePutBill")
    @ResponseBody
    public Map<String,Object> savePutBill(PutBill putBill){
        return putBillService.savePutBill(putBill);
    }

    /**
    * @Description:  获取上架单详情列表
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String,Object> getDetailList(String putBIllId) {
        Map<String,Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page",page.getPageno());
        map.put("limit",page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("putBIllId", putBIllId);
        Page<PutBillDetail> putBillDetailPageList = putBillDetailService.queryPage(map);
        page.setTotalRows(putBillDetailPageList.getTotal()==0?1:putBillDetailPageList.getTotal());
        map.put("rows",putBillDetailPageList.getRecords());
        executePageMap(map,page);

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
        List<Map<String, Object>> materialList =putBillDetailService.getSelectMaterialList(instorageBillId, queryString, pageSize, pageNo);
        map.put("result", materialList);
        map.put("total", putBillDetailService.getSelectMaterialTotal(instorageBillId, queryString));
        return map;
    }


    /**
    * @Description:  保存上架单详情(物料详情)
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    @RequestMapping(value = "/savePutBillDetail")
    @ResponseBody
    public Map<String,Object> savePutBillDetail(Long putBillId,String materialCodes){
        Map<String,Object> map = new HashMap<>();
        boolean ret=false;
        String msg="";
        ret=putBillDetailService.savePutBillDetail(putBillId,materialCodes);
        if(ret){
            msg = "添加成功！";
        }else{
            msg = "添加失败！";
        }
        map.put("result", ret);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description:  删除上架单详情（物料详情）
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/24
    */
    @RequestMapping(value = "/deletePutBillDetail")
    @ResponseBody
    public Map<String,Object> deletePutBillDetail(String ids){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = putBillDetailService.deletePutBillDetail(ids);
        map.put("result", result);
        return map;
    }

    /**
    * @Description:  删除上架单
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/18
    */
    @RequestMapping(value = "/deletePutBill")
    @ResponseBody
    public Map<String,Object> deletePutBill(Long id){
        Map<String,Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        try{
            putBillService.deletePutBill(id);
            result = true;
            msg = "删除上架单成功";
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            msg = "删除上架单失败";
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
    }

    /**
    * @Description:  获取库位下拉框数据源
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    @RequestMapping(value = "/getPosition")
    @ResponseBody
    public Map<String,Object> getPosition(){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Map<String,Object>> lstPosition = putBillDetailService.getPosition();
        map.put("result", lstPosition);
        return map;
    }

    /**
    * @Description:  获取推荐库位
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/29
    */
    @RequestMapping(value = "/getRecommendPosition")
    @ResponseBody
    public Map<String,Object> getRecommendPosition(String putBilDetailId){
        Map<String,Object> map = Maps.newHashMap();
        String recommendPositionCode = putBillDetailService.getRecommendPosition(putBilDetailId);
        map.put("recommendPositionCode", recommendPositionCode);
        return map;
    }

    /**
    * @Description:  更新上架单上架数量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    @RequestMapping(value = "/updatePutAmount")
    @ResponseBody
    public Map<String,Object> updatePutAmount(Long putBillDetailId,String putAmount){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="数量修改失败！";
        //根据上架单详情id获取上架单状态
        String putBillDetailState = putBillDetailService.getPutBillDetailByDetailId(putBillDetailId);
        if(StringUtils.isNotEmpty(putBillDetailState)){
            if("0".equals(putBillDetailState)){
                result = putBillDetailService.updatePutAmount(putBillDetailId,putAmount);
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
    * @Description:  更新上架单的上架重量
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    @RequestMapping(value = "/updatePutWeight")
    @ResponseBody
    public Map<String,Object> updatePutWeight(Long putBillDetailId,String putWeight){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="重量修改失败！";
        //根据上架单详情id获取上架单状态
        String putBillDetailState = putBillDetailService.getPutBillDetailByDetailId(putBillDetailId);
        if(StringUtils.isNotEmpty(putBillDetailState)){
            if("0".equals(putBillDetailState)){
                result = putBillDetailService.updatePutWeight(putBillDetailId,putWeight);
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
    * @Description:  更新上架单的rfid
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/28
    */
    @RequestMapping(value = "/updateRfid")
    @ResponseBody
    public Map<String,Object> updateRfid(Long putBillDetailId,String rfid){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="RFID修改失败！";
        //根据上架单详情id获取上架单状态
        String putBillDetailState = putBillDetailService.getPutBillDetailByDetailId(putBillDetailId);
        if(StringUtils.isNotEmpty(putBillDetailState)){
            if("0".equals(putBillDetailState)){
                result = putBillDetailService.updateRfid(putBillDetailId,rfid);
                if(result){
                    msg="RFID修改成功！";
                }
            }else{
                msg +="订单已提交，RFID不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description:  更新上架单库位编码
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    @RequestMapping(value = "/updatePositionCode")
    @ResponseBody
    public Map<String,Object> updatePositionCode(Long putBillDetailId,String positionCode){
        Map<String,Object> map=new HashMap<>();
        boolean result=false;
        String msg="库位修改失败！";
        //根据上架单详情id获取上架单状态
        String putBillDetailState = putBillDetailService.getPutBillDetailByDetailId(putBillDetailId);
        if(StringUtils.isNotEmpty(putBillDetailState)){
            if("0".equals(putBillDetailState)){
                result = putBillDetailService.updatePositionCode(putBillDetailId,positionCode);
                if(result){
                    msg="上架单库位修改成功！";
                }
            }else{
                msg +="订单已提交，库位不可修改";
            }
        }
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    /**
    * @Description: 上架单提交
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    @RequestMapping(value="/submitPutBill")
    @ResponseBody
    public Map<String,Object> submitPutBill(Long putBillId){
        Map<String,Object> map=new HashMap<String,Object>();
        boolean result = true;
        String msg = "";
        PutBill putBill = putBillService.selectById(putBillId);
        //入库单id
        Long instorageBillId = putBill.getInstorageBillId();
        Instorage instorage = instorageService.selectById(instorageBillId);
        //入库流程（0：一般流程；1：白糖流程）
        String instorageProcess = instorage.getInstorageProcess();

        //根据上架单id查询上架单详情
        EntityWrapper<PutBillDetail> entity = new EntityWrapper<PutBillDetail>();
        entity.eq("put_bill_id",putBillId);
        List<PutBillDetail> lstPutBillDetail = putBillDetailService.selectList(entity);
        if(lstPutBillDetail != null && lstPutBillDetail.size() > 0){
            for(PutBillDetail putBillDetail : lstPutBillDetail){
                //物料编号
                String materialCode = putBillDetail.getMaterialCode();
                //上架数量
                Double putAmount = putBillDetail.getPutAmount()==null||StringUtils.isBlank(putBillDetail.getPutAmount())?0d:Double.parseDouble(putBillDetail.getPutAmount());
                //上架重量
                Double putWeight = putBillDetail.getPutWeight()==null||StringUtils.isBlank(putBillDetail.getPutWeight())?0d:Double.parseDouble(putBillDetail.getPutWeight());
                //RFID
                String rfid = putBillDetail.getRfid();
                //库位编码
                String positionCode = putBillDetail.getPositionCode();
                if(StringUtils.isEmptyString(rfid)){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialCode+"】的RFID是否填写";
                    break;
                }
                if(StringUtils.isEmptyString(positionCode)){
                    result = false;
                    msg = "提交失败！请确认物料【"+materialCode+"】的库位是否填写";
                    break;
                }

                //校验一个rfid不能放在多个库位上
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("putBillId",putBillId);
                paramMap.put("rfid",rfid);
                Map<String,Object> map3 = putBillDetailService.selectRfidInPosition(paramMap);
                if(map3 == null){
                    result = false;
                    msg = "提交失败！请确认托盘【"+rfid+"】是否放在了多个的库位上";
                    break;
                }

                //一般流程的入库流程的单据需要验证数量和重量,都不可以为0
                if("0".equals(instorageProcess)){
                    if(putAmount == 0){
                        result = false;
                        msg = "提交失败！物料数量不能为0";
                        break;
                    }
                    if(putWeight == 0){
                        result = false;
                        msg = "提交失败！物料重量不能为0";
                        break;
                    }
                    //校验：1.rfid绑定的物料数量和重量是否与上架数量和重量一致；2.上架数量和重量是否小于等于入库单的可拆分数量和重量
                    Map<String,Object> map1 = putBillService.checkRfidAmountAndWeight(instorageBillId,rfid,materialCode,putAmount,putWeight);
                    result = map1.get("result")==null?false:Boolean.parseBoolean(map1.get("result").toString());
                    msg = map1.get("msg")==null?"":map1.get("msg").toString();
                    if(!result){
                        break;
                    }

                }
                //白糖流程的入库流程的单据需要验证数量和重量,必须为0
                if("1".equals(instorageProcess)){
                    if(putAmount != 0){
                        result = false;
                        msg = "提交失败！该单据是白糖流程类型，确保物料数量都为0";
                        break;
                    }
                    if(putWeight != 0){
                        result = false;
                        msg = "提交失败！该单据是白糖流程类型，确保物料重量都为0";
                        break;
                    }
                    EntityWrapper<MaterielBindRfid> entityRfid = new EntityWrapper<>();
                    entityRfid.eq("rfid",rfid);
                    entityRfid.eq("deleted_flag","1");
                    entityRfid.eq("status","0");
                    Integer count = materielBindRfidService.selectCount(entityRfid);
                    if(count > 0){
                        result = false;
                        msg = "提交失败！该单据为白糖类型，而托盘【"+rfid+"】已绑定了物料，请确认！";
                        break;
                    }
                }

                //校验库位是否可以上架(注：白糖类型的在判断库位时无法检验)
                Map<String,Object> map2 = putBillService.isAvailablePosition(rfid,positionCode);
                result = map2.get("result")==null?false:Boolean.parseBoolean(map2.get("result").toString());
                msg = map2.get("msg")==null?"":map2.get("msg").toString();
                if(!result){
                    break;
                }
            }
        }else{
            result = false;
            msg = "提交失败！请填写物料信息";
        }

        if(result){
            //获取上架单的状态
            String state = putBill.getState();
            if("0".equals(state)){
                try{
                    putBillService.submitPutBill(instorageBillId,putBillId,instorageProcess);
                    result=true;
                    msg="提交成功！";
                }catch (Exception e){
                    e.printStackTrace();
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
    * @Description:  跳转上架确认操作页面
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    @RequestMapping(value="/toSurePutBill")
    @ResponseBody
    public ModelAndView toSurePutBill(String putBillId){
        ModelAndView mv = new ModelAndView();
        PutBill putBill = putBillService.selectById(putBillId);
        mv.addObject("putBillCode", putBill.getPutBillCode());
        mv.addObject("putBill", putBill);
        mv.setViewName("techbloom/instorage/putBill/putBill_sure");
        return mv;
    }

    /**
    * @Description:  确认完成上架
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/31
    */
    @RequestMapping(value="/completePutBill")
    @ResponseBody
    public Map<String,Object> completePutBill(String putBillDetailId){
        Map<String,Object> map = new HashMap();
        boolean code = false;
        String msg = "";
        try{
            putBillDetailService.completePutBill(putBillDetailId);
            code = true;
            msg = "上架成功";
        }catch (Exception e){
            e.printStackTrace();
            code = false;
            msg = "系统错误";
        }
        map.put("result",code);
        map.put("msg",msg);
        return  map;
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
        List<PutBill> list = putBillService.getAllList(ids);
        putBillService.toExcel(response, "", list);
    }

}
