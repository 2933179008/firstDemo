package com.tbl.modules.stock.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.service.StockChangeService;
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
 * @description: 库存变动查询
 * @author: zj
 * @create: 2018-12-26 15:15
 **/
@Controller
@RequestMapping(value = "/stockChange")
public class StockChangeController extends AbstractController {

    //库存变动service
    @Autowired
    private StockChangeService stockChangeService;

    //物料service
    @Autowired
    private MaterielService materielService;

    //库位service
    @Autowired
    private DepotPositionService depotPositionService;

    /**
     * @Description: 跳转到库存变动查询页面
     * @Param:
     * @return:
     * @Author: zj
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

        mv.setViewName("techbloom/stock/stockChange_list");
        mv.addObject("lstMateriel", lstMateriel);
        mv.addObject("lstDepotPosition", lstDepotPosition);
        return mv;
    }

    /**
     * @Description: 获取库存变动列表
     * @Param:
     * @return:
     * @Author: zj
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
        PageUtils PagePlatform = stockChangeService.queryPage(map);
        if(PagePlatform==null){
            map.put("rows", null);
        }
        else{
            page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
            map.put("rows", PagePlatform.getList());
        }
        executePageMap(map, page);
        return map;
    }

//    /**
//     * @param
//     * @return ModelAndView
//     * @Description: 生成变动单据编号
//     * @author pz
//     * @date 2018-12-27
//     */
//    @RequestMapping(value = "/generateChangeCode")
//    @ResponseBody
//    public String generateChangeCode() {
//        return stockChangeService.generateChangeCode();
//    }

}
    