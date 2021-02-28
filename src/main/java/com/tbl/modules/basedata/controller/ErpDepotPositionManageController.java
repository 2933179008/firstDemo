package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.ErpDepotArea;
import com.tbl.modules.basedata.entity.ErpDepotPosition;
import com.tbl.modules.basedata.service.ErpDepotAreaService;
import com.tbl.modules.basedata.service.ErpDepotPositionService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * erp库位管理Controller
 *
 * @author pz
 * @date 2019-04-29
 */
@Controller
@RequestMapping(value = "/erpDepotPositionManage")
public class ErpDepotPositionManageController extends AbstractController {


    //库位管理service
    @Autowired
    private ErpDepotPositionService erpDepotPositionService;

    //库区管理service
    @Autowired
    private ErpDepotAreaService erpDepotAreaService;

    //物料绑定RFID管理service
    @Autowired
    private MaterielBindRfidService materielBundRfidService;


    /**
     * 跳转到库位管理页面
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/erpDepotPositionManage/erpDepotPositionManage_list");
        return mv;
    }

    /**
     * 获取库位列表数据
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Map<String, Object> listLog(String queryJsonString) {

        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = erpDepotPositionService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/erpDepotPositionManage/erpDepotPositionManage_edit");
        ErpDepotPosition depotPosition = null;

        //判断是否为编辑页面
        if (id != -1) {
            depotPosition = erpDepotPositionService.selectById(id);
        } else {
            //自动生成库位编码
            String positionCode = erpDepotPositionService.getPositionCode();
            mv.addObject("positionCode", positionCode);
        }

        //获取库区集合
        List<ErpDepotArea> areaList = erpDepotAreaService.selectList(
                new EntityWrapper<ErpDepotArea>()
        );

        mv.addObject("areaList", areaList);
        mv.addObject("depotPosition", depotPosition);
        mv.addObject("edit", 1);//？？？

        return mv;
    }

    /**
     * 判断库位编码是否存在
     *
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String positionCode, Long id) {
        boolean flag = true;
        // 根据materielCode查询实体list
        EntityWrapper<ErpDepotPosition> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("position_code", positionCode);
        try {
            //？？？？
            if (id != null) {
                int count = erpDepotPositionService.selectCount(entityWrapper);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = erpDepotPositionService.selectCount(entityWrapper);
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
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/addDepotPosition")
    @ResponseBody
    public boolean addMateriel(ErpDepotPosition depotPosition) {
        boolean result = false;
        //获取当前时间并格式化
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        if (depotPosition == null) {
            return result;
        }
        //如果映射获取的物料实力ID不为空，则为修改物料，否则为添加物料
        if (depotPosition.getId() != null) {
            depotPosition.setUpdateTime(time);
            result = erpDepotPositionService.updateById(depotPosition);
        } else {
            List<ErpDepotPosition> depotPositionList = erpDepotPositionService.selectList(
                    new EntityWrapper<ErpDepotPosition>()
                            .eq("position_code", depotPosition.getPositionCode())
            );
            if (depotPositionList.size() != 0) {
                depotPosition.setPositionCode(erpDepotPositionService.getPositionCode());
            }
            depotPosition.setCreateTime(time);
            result = erpDepotPositionService.insert(depotPosition);
        }
        return result;
    }

    /**
     * 物理删除库位
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @RequestMapping(value = "/delDepotPosition")
    @ResponseBody
    public boolean delDepotPosition(String ids) {
        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        //获取删除的库位中是否含有物料绑定RFID的占用
        int count = materielBundRfidService.selectCount(
                new EntityWrapper<MaterielBindRfid>().in("position_by", ids)
        );
        //若有占用库位则此库位不可删除
        if (count != 0) {
            return false;
        }

        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return erpDepotPositionService.deleteBatchIds(lstIds);
    }

    @RequestMapping(value = "/getDepotPosition")
    @ResponseBody
    public List<ErpDepotPosition> getDepotPosition(Long depotareaId) {

        HashMap<String, Object> areaIdMap = new HashMap<>();
        areaIdMap.put("depotarea_id", depotareaId);
        List<ErpDepotPosition> depotPositionList = erpDepotPositionService.selectByMap(areaIdMap);
        return depotPositionList;
    }

}
