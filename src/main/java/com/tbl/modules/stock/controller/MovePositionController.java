package com.tbl.modules.stock.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MovePosition;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.stock.service.MovePositionService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.*;

/**
 * 移位管理Controller
 *
 * @author yuany
 * @date 2019-01-21
 */
@Controller
@RequestMapping(value = "/movePosition")
public class MovePositionController extends AbstractController {

    /**
     * 移位管理service
     */
    @Autowired
    private MovePositionService movePositionService;

    /**
     * 用户管理Service
     */
    @Autowired
    private UserService userService;

    /**
     * 物料管理Service
     */
    @Autowired
    private MaterielService materielService;

    /**
     * 物料绑定Service
     */
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    /**
     * 库区Service
     */
    @Autowired
    private DepotAreaService depotAreaService;

    /**
     * 库位Service
     */
    @Autowired
    private DepotPositionService depotPositionService;

    /**
     * 库存Service
     */
    @Autowired
    private StockService stockService;

    /**
     * 跳转到移位列表页面
     *
     * @return
     * @author yuany
     * @date 2019-01-21
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();
        //获取物料集合
        List<Materiel> lstMateriel = materielService.selectList(
                new EntityWrapper<Materiel>().eq("deleted_flag", DyylConstant.NOTDELETED)
        );
        mv.setViewName("techbloom/stock/movePosition_list");
        mv.addObject("lstMateriel", lstMateriel);
        return mv;
    }

    /**
     * 获取移位列表数据
     *
     * @param queryJsonString
     * @return
     * @author yuany
     * @date 2019-01-21
     */
    @ResponseBody
    @RequestMapping(value = "/getList.do")
    public Map<String, Object> getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (!com.tbl.common.utils.StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortName)) {
            sortName = "moveFoundTime";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = movePositionService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到添加/编辑页面
     *
     * @param sceneId
     * @return
     */
    @RequestMapping(value = "/toEdit")
    public ModelAndView toTrayMergeOrSplit(Long sceneId) {
        ModelAndView mv = new ModelAndView();

        List<User> userList = userService.selectList(
                new EntityWrapper<>()
        );

        mv.addObject("userList", userList);

        //获取物料列表
        List<Materiel> materielList = materielService.selectList(
                new EntityWrapper<>()
        );
        mv.addObject("materielList", materielList);

        List<DepotPosition> depotPositionList = depotPositionService.selectList(
                new EntityWrapper<>()
        );

        //显示库存中散货物料
        List<Stock> stockUniqueList = stockService.selectList(
                new EntityWrapper<Stock>().eq("material_type", DyylConstant.MATERIAL_NORFID)
        );

        //对库存中散货物料进行批次号去重显示
        List<Stock> BatchNoList = stockUniqueList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(Stock::getBatchNo))), ArrayList::new)
        );
        mv.addObject("BatchNoList", BatchNoList);


        MovePosition movePosition = movePositionService.selectById(sceneId);
        mv.addObject("depotPositionList", depotPositionList);
        mv.addObject("movePosition", movePosition);
        //若此条数据状态不等于未移位，则不可编辑
        if (sceneId == -1 || movePosition.getStatus().equals(DyylConstant.STATUS_NO)) {
            mv.setViewName("techbloom/stock/movePosition_edit");
        }

        return mv;
    }

    /**
     * @Description: 依据物料编码获取物料名称
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/12
     */
    @RequestMapping(value = "/getMaterielName")
    @ResponseBody
    public Materiel getMaterielName(String materielCode) {
        EntityWrapper<Materiel> entityMateriel = new EntityWrapper<>();
        entityMateriel.eq("materiel_code", materielCode);
        return materielService.selectOne(entityMateriel);
    }

    /**
     * 添加/修改
     *
     * @return
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/addMovePosition")
    @ResponseBody
    public Map<String, Object> addMovePosition(MovePosition movePosition) {
        Map<String, Object> map = new HashMap<>();
        map = movePositionService.addMovePosition(movePosition, getUserId());
        return map;
    }

    /**
     * 逻辑删除移位数据
     *
     * @return
     * @author yuany
     * @date 2019-01-22
     */
    @RequestMapping(value = "/delMovePosition")
    @ResponseBody
    public boolean delMovePosition(String ids) {

        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        return movePositionService.delMovePosition(ids, getUserId());
    }


    /**
     * 开始移位/移位完成
     *
     * @return
     * @author yuany
     * @date 2019-01-22
     */
    @RequestMapping(value = "/statusMovePosition")
    @ResponseBody
    public boolean statusMovePosition(String ids, Integer sign) {
        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        return movePositionService.statusMovePosition(ids, DateUtils.getTime(), sign, getUserId());
    }

    /**
     * 判断RFID是否存在
     *
     * @author yuany
     * @date 2019-01-22
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String rfid, Long id) {
        boolean result = false;
        // 根据rfid查询实体list
//        EntityWrapper<MaterielBindRfid> entityWrapper = new EntityWrapper<>();
//        entityWrapper.eq("rfid", rfid);
//        entityWrapper.eq("deleted_flag", DyylConstant.NOTDELETED);
//        entityWrapper.eq("status", DyylConstant.STATE_UNTREATED);
        List<MaterielBindRfid> materielBindRfidList = materielBindRfidService.selectList(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_UNTREATED)
                        .eq("rfid", rfid)
        );

        if (materielBindRfidList.size() == 1) {
            result = true;
        }

        return result;
    }

    /**
     * 导出Excel
     *
     * @author yuany
     * @date 2019-01-23
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void exportMovePosition(String ids) {
        List<MovePosition> list = movePositionService.getAllLists(ids);
        //遍历添加名称
        for (MovePosition movePosition : list) {
            movePosition.setMoveUserName(userService.selectById(movePosition.getMoveUserId()).getUsername());
            String areaName = depotAreaService.selectById(depotPositionService.selectById(movePosition.getPositionBy()).getDepotareaId()).getAreaName();
            String positionName = depotPositionService.selectById(movePosition.getPositionBy()).getPositionName();
            movePosition.setPositionName(areaName + "-" + positionName);
            //原库位
            String formerAreaName = depotAreaService.selectById(depotPositionService.selectById(movePosition.getFormerPosition()).getDepotareaId()).getAreaName();
            String formerPositionName = depotPositionService.selectById(movePosition.getFormerPosition()).getPositionName();
            movePosition.setFormerPositionName(formerAreaName + "-" + formerPositionName);
            if (movePosition.getStatus().equals(DyylConstant.STATUS_NO)) {
                movePosition.setStatusName("未移位");
            } else if (movePosition.getStatus().equals(DyylConstant.STATUS_IN)) {
                movePosition.setStatusName("移位中");
            } else if (movePosition.getStatus().equals(DyylConstant.STATUS_OVER)) {
                movePosition.setStatusName("已完成");
            }
        }
        movePositionService.toExcel(response, "", list);
    }

    /**
     * @Description: 跳转移位查询详情页
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/11
     */
    @RequestMapping(value = "/toDetailView")
    @ResponseBody
    public ModelAndView toDetailView(long id, String movePositionType) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/movePositionDetail_list");
        mv.addObject("movePositionId", id);
        mv.addObject("movePositionType", movePositionType);
        return mv;
    }

    /**
     * @Description: 获取移位查询详情列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/04/11
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String queryJsonString, Long movePositionId) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "createTime";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("suserid", getSessionUser().getUserId());
        PageUtils PageStockDetail = movePositionService.queryPageS(map, movePositionId);
        page.setTotalRows(PageStockDetail.getTotalCount() == 0 ? 1 : PageStockDetail.getTotalCount());
        map.put("rows", PageStockDetail.getList());
        executePageMap(map, page);
        return map;
    }

}
