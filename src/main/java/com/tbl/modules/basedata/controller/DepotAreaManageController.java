package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
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
 * 库区管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/depotAreaManage")
public class DepotAreaManageController extends AbstractController {

    //库区管理service
    @Autowired
    private DepotAreaService depotAreaService;

    //库位管理service
    @Autowired
    private DepotPositionService depotPositionService;


    /**
     * 跳转到库位管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/depotAreaManage/depotAreaManage_list");
        return mv;
    }

    /**
     * 获取库位列表数据
     *
     * @return
     * @author yuany
     * @date 2019-01-04
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
        PageUtils utils = depotAreaService.queryPage(map);
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
     * @date 2018-12-28
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/depotAreaManage/depotAreaManage_edit");
        DepotArea depotArea = null;

        //判断是否为编辑页面
        if (id != -1) {
            depotArea = depotAreaService.selectById(id);
        }
        mv.addObject("depotArea", depotArea);
        mv.addObject("edit", 1);//？？？
        return mv;
    }

    /**
     * 判断库区编码是否存在
     *
     * @author yuany
     * @date 2019-01-04
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String areaCode, Long id) {
        boolean flag = true;
        // 根据areaCode查询实体list
        EntityWrapper<DepotArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("area_code", areaCode);
        try {
            //？？？？
            if (id != null) {
                int count = depotAreaService.selectCount(entityWrapper);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = depotAreaService.selectCount(entityWrapper);
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
     * 添加/修改库区
     *
     * @return
     * @author yuany
     * @date 2019-01-04
     */
    @RequestMapping(value = "/addDepotArea")
    @ResponseBody
    public boolean addMateriel(DepotArea depotArea) {
        boolean result = false;
        //获取当前时间并格式化
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        if (depotArea == null) {
            return result;
        }
        //如果映射获取的物料实力ID不为空，则为修改物料，否则为添加物料
        if (depotArea.getId() != null) {
            depotArea.setUpdateTime(time);
            result = depotAreaService.updateById(depotArea);
        } else {
            depotArea.setCreateTime(time);
            result = depotAreaService.insert(depotArea);
        }
        return result;
    }

    /**
     * 物理删除库区
     *
     * @return
     * @author yuany
     * @date 2019-01-05
     */
    @RequestMapping(value = "/delDepotArea")
    @ResponseBody
    public boolean delDepotArea(String ids) {
        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        //获取库区中已占用的库位数
        int count = depotPositionService.selectCount(
                new EntityWrapper<DepotPosition>().in("depotarea_id", ids)
        );
        //如果库位数不等于0，则此库区不可删除
        if (count != 0) {
            return false;
        }

        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return depotAreaService.deleteBatchIds(lstIds);
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
        List<DepotArea> list = depotAreaService.getAllLists(ids);
        for (DepotArea depotArea : list){
            //库区类别（0收货区，1发货区，2良品存储区，3虚拟存储区，4退货存储区，5不良品存储区）
            if (depotArea.getAreaType().equals("0")){
                depotArea.setAreaTypeName("收货区");
            }else if (depotArea.getAreaType().equals("1")){
                depotArea.setAreaTypeName("发货区");
            }else if (depotArea.getAreaType().equals("2")){
                depotArea.setAreaTypeName("良品存储区");
            }else if (depotArea.getAreaType().equals("3")){
                depotArea.setAreaTypeName("虚拟存储区");
            }else if (depotArea.getAreaType().equals("4")){
                depotArea.setAreaTypeName("退货存储区");
            }else if (depotArea.getAreaType().equals("5")){
                depotArea.setAreaTypeName("不良品存储区");
            }else {
                depotArea.setAreaTypeName(null);
            }
        }
        depotAreaService.toExcel(response, "", list);
    }
}
