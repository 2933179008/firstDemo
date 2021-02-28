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
 * 库位管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/depotPositionManage")
public class DepotPositionManageController extends AbstractController {


    //库位管理service
    @Autowired
    private DepotPositionService depotPositionService;

    //库区管理service
    @Autowired
    private DepotAreaService depotAreaService;

    //物料绑定RFID管理service
    @Autowired
    private MaterielBindRfidService materielBundRfidService;


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
        mv.setViewName("techbloom/basedata/depotPositionManage/depotPositionManage_list");
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
        PageUtils utils = depotPositionService.queryPage(map);
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
     * @date 2019-01-204
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/depotPositionManage/depotPositionManage_edit");
        DepotPosition depotPosition = null;

        //判断是否为编辑页面
        if (id != -1) {
            depotPosition = depotPositionService.selectById(id);
        }

        //获取库区集合
        List<DepotArea> areaList = depotAreaService.selectList(
                new EntityWrapper<>()
        );

        mv.addObject("areaList", areaList);
        mv.addObject("depotPosition", depotPosition);
        mv.addObject("edit", 1);

        return mv;
    }

    /**
     * 判断库位编码是否存在
     *
     * @author yuany
     * @date 2019-01-04
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String positionCode, Long id) {
        boolean flag = true;
        // 根据materielCode查询实体list
        EntityWrapper<DepotPosition> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("position_code", positionCode);
        try {
            //？？？？
            if (id != null) {
                int count = depotPositionService.selectCount(entityWrapper);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = depotPositionService.selectCount(entityWrapper);
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
     * 添加/修改库位
     *
     * @return
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/addDepotPosition")
    @ResponseBody
    public boolean addMateriel(DepotPosition depotPosition) {
        boolean result = false;
        //获取当前时间并格式化
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        if (depotPosition == null) {
            return result;
        }
        //如果映射获取的库位实例ID不为空，则为修改库位，否则为添加库位
        if (depotPosition.getId() != null) {
            depotPosition.setUpdateTime(time);
            result = depotPositionService.updateById(depotPosition);
        } else {
            depotPosition.setCreateTime(time);
            result = depotPositionService.insert(depotPosition);
        }
        return result;
    }

    /**
     * 物理删除库位
     *
     * @return
     * @author yuany
     * @date 2019-01-05
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
        return depotPositionService.deleteBatchIds(lstIds);
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
        List<DepotPosition> depotPositionList = depotPositionService.getAllLists(ids);
        for (DepotPosition depotPosition : depotPositionList) {
            if (depotPosition.getDepotareaId() != null && depotAreaService.selectById(depotPosition.getDepotareaId()) != null) {
                depotPosition.setAreaName(depotAreaService.selectById(depotPosition.getDepotareaId()).getAreaName());
            }
            if (depotPosition.getPositionType().equals(DyylConstant.POSITION_TYPE0)) {
                depotPosition.setPositionTypeName("地堆");
            } else if (depotPosition.getPositionType().equals(DyylConstant.POSITION_TYPE1)) {
                depotPosition.setPositionTypeName("货架");
            } else if (depotPosition.getPositionType().equals(DyylConstant.POSITION_TYPE2)) {
                depotPosition.setPositionTypeName("不良品");
            } else if (depotPosition.getPositionType().equals(DyylConstant.POSITION_TYPE3)) {
                depotPosition.setPositionTypeName("暂存");
            } else {
                depotPosition.setPositionTypeName(null);
            }
            if (depotPosition.getBlendType().equals(DyylConstant.BLEND_TYPE0)) {
                depotPosition.setBlendTypeName("可混放");
            } else if (depotPosition.getBlendType().equals(DyylConstant.BLEND_TYPE1)) {
                depotPosition.setBlendTypeName("不可混放");
            } else {
                depotPosition.setBlendTypeName(null);
            }
        }
        depotPositionService.toExcel(response, "", depotPositionList);
    }

    @RequestMapping(value = "/getDepotPosition")
    @ResponseBody
    public List<DepotPosition> getDepotPosition(Long depotareaId) {

        HashMap<String, Object> areaIdMap = new HashMap<>();
        areaIdMap.put("depotarea_id", depotareaId);
        List<DepotPosition> depotPositionList = depotPositionService.selectByMap(areaIdMap);
        return depotPositionList;
    }

    /**
     * @Description: 库存冻结
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/08
     */
    @RequestMapping(value = "/stockFrozen")
    @ResponseBody
    public Map<String, Object> stockFrozen(String ids) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        boolean flag = false;

        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //获取选中的库位信息
        List<DepotPosition> lstDepotPosition = depotPositionService.selectBatchIds(lstIds);

        List<DepotPosition> lstPosition = new ArrayList<>();

        for (DepotPosition dp : lstDepotPosition) {
            dp.setPositionFrozen(DyylConstant.POSITION_FROZEN1);
            lstPosition.add(dp);
        }

        flag = depotPositionService.updateBatchById(lstPosition);
        if (flag) {
            msg = "冻结成功";
            map.put("result", flag);
            map.put("msg", msg);
        } else {
            msg = "冻结失败";
            map.put("result", flag);
            map.put("msg", msg);
        }

        return map;
    }

    /**
     * @Description: 库存解冻
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/08
     */
    @RequestMapping(value = "/stockThaw")
    @ResponseBody
    public Map<String, Object> stockThaw(String ids) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        boolean flag = false;

        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //获取选中的库位信息
        List<DepotPosition> lstDepotPosition = depotPositionService.selectBatchIds(lstIds);

        List<DepotPosition> lstPosition = new ArrayList<>();
        for (DepotPosition dp : lstDepotPosition) {
            dp.setPositionFrozen(DyylConstant.POSITION_FROZEN0);
            lstPosition.add(dp);
        }
        flag = depotPositionService.updateBatchById(lstPosition);
        if (flag) {
            msg = "解冻成功";
            map.put("result", flag);
            map.put("msg", msg);
        } else {
            msg = "解冻失败";
            map.put("result", flag);
            map.put("msg", msg);
        }

        return map;
    }
}
