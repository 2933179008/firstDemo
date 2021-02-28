package com.tbl.modules.visualization.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.StockService;
import com.tbl.modules.visualization.entity.StockCar;
import com.tbl.modules.visualization.service.StockCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/visual")
public class VisualController extends AbstractController {

    //库区Service
    @Autowired
    private DepotAreaService depotAreaService;

    //库位Service
    @Autowired
    private DepotPositionService depotPositionService;

    //库存Service
    @Autowired
    private StockService stockService;

    //小车Service
    @Autowired
    private StockCarService stockCarService;

    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @RequestMapping(value = "/toList.do")
    @ResponseBody
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/visualization/visual_index");

        return mv;
    }


    @RequestMapping(value = "/getVisual")
    @ResponseBody
    public String getVisual(String areaType) {

        JSONArray positionJson = new JSONArray();

        //获取库区中的库位信息
        EntityWrapper<DepotPosition> wraDepotPosition = new EntityWrapper<>();
        wraDepotPosition.eq("depotarea_id", areaType);
        List<DepotPosition> lstDepotPosition = depotPositionService.selectList(wraDepotPosition);

        //获取库存信息
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        List<Stock> lstStock = stockService.selectList(wraStock);

        for (int i = 0; i < lstDepotPosition.size(); i++) {
            DepotPosition position = lstDepotPosition.get(i);
            String positionCode = position.getPositionCode();

            List<Stock> stockResult = null;
            stockResult = lstStock.stream().filter((Stock s) -> positionCode.equals(s.getPositionCode())).collect(Collectors.toList());

            int sumAge = 0;
            if (stockResult != null && stockResult.size() != 0) {
                sumAge = stockAmountSum(stockResult);
            }

            JSONObject positionObject = new JSONObject();
            positionObject.put("positionName", position.getPositionName());

            int capacityRfidAmount = 0;
            if (StringUtils.isNotEmpty(position.getCapacityRfidAmount())) {
                capacityRfidAmount = Integer.valueOf(position.getCapacityRfidAmount());
            }
            positionObject.put("posOccupy", sumAge+"/"+capacityRfidAmount);
            if(capacityRfidAmount==0||sumAge==0){
                positionObject.put("percentage", 0);
            }else{
                positionObject.put("percentage", (1.0*sumAge/capacityRfidAmount)*100);
            }
            positionJson.add(positionObject);
        }
        return JSON.toJSONString(positionJson);
    }

    //库存rfid求和
    public int stockAmountSum(List<Stock> lstStock) {
        String rfids = null;
        List<String> rfidlst = new ArrayList<>();
        //遍历库存，拼接RFID字符串
        for (Stock stock : lstStock) {
            if (rfids == null) {
                rfids = stock.getRfid();
            } else {
                rfids = rfids + "," + stock.getRfid();
            }
        }
        //获取关于rfid的字符串数组
        String[] rfid = rfids.split(",");
        //遍历数组
        for (int i = 0; i < rfid.length; i++) {
            //判断list集合中是否存在下标为 i 的RFID，若不存在则添加
            if (!rfidlst.contains(rfid[i])) {
                rfidlst.add(rfid[i]);
            }
        }
        return rfidlst.size();
//        int sumAge=0;
//        for(int i=0;i<lstStock.size();i++){
//            Stock s=lstStock.get(i);
//            //获取库存中的库存rfid数量
//            String stockRfidAmount = s.getStockRfidAmount();
//            //如果stockRfidAmount包含小数点，则需要截取小数点之前的数字。因为rfid的数量不会存在小数
//            if(stockRfidAmount.contains(".")){
//                stockRfidAmount = stockRfidAmount.substring(0,stockRfidAmount.indexOf("."));
//            }
//            sumAge=sumAge+Integer.parseInt(stockRfidAmount);
//        }
//        return sumAge;
    }


    /**
     * 获取库区
     *
     * @return
     */
    @RequestMapping(value = "/getAllDepotArea")
    @ResponseBody
    public List<DepotArea> getAllDepotArea() {
        EntityWrapper<DepotArea> wraDepotArea = new EntityWrapper<>();
        return depotAreaService.selectList(wraDepotArea);
    }

    /**
     * 获取库区对应的库位
     *
     * @return
     */
    @RequestMapping(value = "/getDepotPosition")
    @ResponseBody
    public List<DepotPosition> getDepotPosition(Long depotareaId) {
        EntityWrapper<DepotPosition> wraDepotPosition = new EntityWrapper<>();
        wraDepotPosition.eq("depotarea_id", depotareaId);
        List<DepotPosition> lstDepotPosition = depotPositionService.selectList(wraDepotPosition);
        return lstDepotPosition;
    }


    /**
     * 获取小车位置
     */
    @RequestMapping(value = "/getCarPosition")
    @ResponseBody
    public List<StockCar> getCarPosition(String areaId) {

        String areaCode = depotAreaService.selectById(areaId).getAreaCode();
        List<StockCar> lstStockCar = stockCarService.getCarPosition(areaCode);
        return lstStockCar;
    }

    /**
     * 跳转到弹窗列表
     *
     * @author yuany
     * @date 2019-06-12
     */
    @RequestMapping(value = "/toDetail")
    @ResponseBody
    public ModelAndView toDetail(String positionCode){
        ModelAndView mv = new ModelAndView();
        DepotPosition depotPosition = null;
        //若库位编码不为空，则获取库位信息，关于此库位的物料绑定数量
        if (StringUtils.isNotEmpty(positionCode)){
            depotPosition = depotPositionService.selectOne(new EntityWrapper<DepotPosition>().eq("position_code",positionCode));
            mv.addObject("depotPosition",depotPosition);
        }

        mv.setViewName("techbloom/visualization/materielBySotr_detail");
        return mv;
    }

    /**
     * 获取绑定单详情列表
     *
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-06-12
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String positionId) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("positionId", positionId);
        PageUtils PagePlatform = materielBindRfidDetailService.getMaterielBySort(map);
        if (PagePlatform != null) {
            page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
            map.put("rows", PagePlatform.getList());
        } else {
            page.setTotalRows(0);
            map.put("rows", null);
        }

        executePageMap(map, page);
        return map;
    }


}
