package com.tbl.modules.stock.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.OutStorageService;
import com.tbl.modules.outstorage.service.SpareBillService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.stock.entity.*;
import com.tbl.modules.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 托盘管理控制类
 *
 * @author yuany
 * @date 2019-01-15
 */

@Controller
@RequestMapping(value = "/trayManager")
public class TrayManagerController extends AbstractController {

    //托盘管理Service
    @Autowired
    private TrayService trayService;

    //托盘管理详情Service
    @Autowired
    private TrayDetailService trayDetailService;

    //库位管理service
    @Autowired
    private DepotPositionService depotPositionService;

    //库区管理service
    @Autowired
    private DepotAreaService depotAreaService;

    //物料绑定详情service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    //物料绑定service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    @Autowired
    private OutStorageService outStorageService;

    @Autowired
    private StockService stockService;

    /**
     * @Description: 跳转到托盘管理页面
     * @Param:
     * @return:
     * @Author: zj
     * @Date: 2018/12/28
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/trayManager_list");
        return mv;
    }

    /**
     * @Description: 获取托盘管理列表
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-15
     */
    @RequestMapping(value = "/getList.do")
    @ResponseBody
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
            sortName = "createTime";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = trayService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }


    /**
     * @Description: 跳转托盘合并/拆分页面
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-16
     */
    @RequestMapping(value = "/toTrayMergeOrSplit")
    public ModelAndView toTrayMergeOrSplit(Long sceneId, Integer sign) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/trayManager_merge_or_split");
        Tray tray = new Tray();

        //sceneId等于-1为合并，等于-2为拆分,否则为查看
        if (sceneId == -1) {
            tray.setType(DyylConstant.MERGE);
        } else if (sceneId == -2) {
            tray.setType(DyylConstant.SPLIT);
        } else {
            tray = trayService.selectById(sceneId);
        }

        //获取自动生成的最新的拆分/合并编码
        String mergeOrSplitCode = trayService.getMergeOrSplitCode(tray.getType());
        mv.addObject("mergeOrSplitCode", mergeOrSplitCode);

        //获取库位集合
        List<DepotPosition> depotPositionList = depotPositionService.selectList(
                new EntityWrapper<>()
        );
        for (DepotPosition depotPosition : depotPositionList) {
            depotPosition.setAreaName(depotAreaService.selectById(depotPosition.getDepotareaId()).getAreaName());
        }

        //获取物料绑定详情集合
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                new EntityWrapper<>()
        );

        //遍历物料绑定详情集合 ，清除上一次物料拆分的数量
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            materielBindRfidDetailService.updateNumWeiById(materielBindRfidDetail.getId());
            if (materielBindRfidDetail.getAmount() == null) {
                //当amount为null时，逻辑删除此条物料绑定详情
                materielBindRfidDetailService.updateDeleteFlag(getUserId(), materielBindRfidDetail.getId());
            }
        }

        mv.addObject("tray", tray);
        mv.addObject("depotPositionList", depotPositionList);
        return mv;
    }

    /**
     * @Description: 跳转托盘查看页面
     * @Author: anss
     * @Date: 2019-3-5
     */
    @RequestMapping(value = "/toTrayDtail")
    public ModelAndView toTrayDtail(Long sceneId, Integer sign) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/trayManager_detail");
        Tray tray = trayService.selectById(sceneId);

        //获取自动生成的最新的拆分/合并编码
        String mergeOrSplitCode = trayService.getMergeOrSplitCode(tray.getType());
        mv.addObject("mergeOrSplitCode", mergeOrSplitCode);

        //获取物料绑定详情集合
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                new EntityWrapper<>()
        );

        //遍历物料绑定详情集合 ，清除上一次物料拆分的数量
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            materielBindRfidDetailService.updateNumWeiById(materielBindRfidDetail.getId());
            if (materielBindRfidDetail.getAmount() == null) {
                //当amount为null时，逻辑删除此条物料绑定详情
                materielBindRfidDetailService.updateDeleteFlag(getUserId(), materielBindRfidDetail.getId());
            }
        }

        DepotPosition depotPosition = depotPositionService.selectById(tray.getPositionBy());
        if (depotPosition != null) {
            depotPosition.setAreaName(depotAreaService.selectById(depotPosition.getDepotareaId()).getAreaName());
        }

        mv.addObject("tray", tray);
        mv.addObject("depotPosition", depotPosition);
        return mv;
    }

    /**
     * 保存/修改合并/拆分
     *
     * @return
     * @author yuany
     * @date 2019-01-09
     */
    @RequestMapping(value = "/saveTray")
    @ResponseBody
    public Map<String, Object> saveTray(Tray tray) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        //获取当前时间并格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = sdf.format(date);

        if (tray == null) {
            map.put("result", result);
            return map;
        }

        //设为未删除状态
        tray.setDeleteFlag(DyylConstant.NOTDELETED);
        tray.setRfid(tray.getRfid().trim());
        //若ID为空则为合并/拆分，否则为修改
        if (tray.getId() == null) {
            List<Tray> trays = trayService.selectList(
                    new EntityWrapper<Tray>()
                    .eq("delete_flag",DyylConstant.NOTDELETED)
                    .eq("status",DyylConstant.STATE_PROCESSED)
                    .eq("rfid",tray.getRfid())
            );
            if (trays.size()>0){
                for (Tray tray1 : trays){
                    List<TrayDetail> trayDetailList = trayDetailService.selectList(
                            new EntityWrapper<TrayDetail>()
                            .eq("tray_by",tray1.getId())
                    );
                    for (TrayDetail trayDetail : trayDetailList){
                        trayDetailService.deleteById(trayDetail);
                    }
                    trayService.deleteById(tray1);
                }
            }
            //添加创建人和创建时间
            tray.setCreateBy(this.getUserId());
            tray.setCreateTime(time);

            List<Tray> trayList = trayService.selectList(
                    new EntityWrapper<Tray>()
                            .eq("merge_or_split_code", tray.getMergeOrSplitCode())
            );
            //判断自动生成的mergeOrSplitCode与物料绑定集合中是否有重复
            if (trayList.size() != 0) {
                tray.setMergeOrSplitCode(trayService.getMergeOrSplitCode(tray.getType()));
            }
            result = trayService.insert(tray);
            map.put("result", result);
        } else {
            //添加修改人和修改时间
            tray.setUpdateBy(this.getUserId());
            tray.setUpdateTime(time);
            result = trayService.updateById(tray);
            map.put("result", result);
        }
        map.put("trayBy", tray.getId());
        return map;
    }

    /**
     * 判断绑定RFID编码是否存在
     *
     * @author yuany
     * @date 2019-01-17
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String rfid,String type) {
        boolean flag = true;
        // 根据materielCode查询实体list
//        EntityWrapper<Tray> entityWrapper = new EntityWrapper<>();
//        entityWrapper.eq("rfid", rfid);
//        entityWrapper.eq("delete_flag", DyylConstant.NOTDELETED);
//        int count = trayService.selectCount(entityWrapper);

        EntityWrapper<MaterielBindRfid> materielBindRfidEntityWrapper = new EntityWrapper<>();
        materielBindRfidEntityWrapper.eq("rfid", rfid.trim());
        materielBindRfidEntityWrapper.eq("deleted_flag", DyylConstant.NOTDELETED);
        int rfidCount = materielBindRfidService.selectCount(materielBindRfidEntityWrapper);
        //当在托盘合并页面输入RFID,则需要判断rfid在物料中是否存在。存在则可进行操作
        if(type.equals(DyylConstant.MERGE)){
            if (rfidCount == 0) {
                flag = false;
            }
        }else{  //当在托盘拆分页面输入RFID,则需要判断rfid在物料中是否存在。不存在则可进行操作
            if (rfidCount > 0) {
                flag = false;
            }
        }

//        EntityWrapper<MaterielBindRfid> materielBindRfidEntityWrapper = new EntityWrapper<>();
//        materielBindRfidEntityWrapper.eq("rfid", rfid);
//        materielBindRfidEntityWrapper.eq("deleted_flag", DyylConstant.NOTDELETED);
//        int rfidCount = materielBindRfidService.selectCount(materielBindRfidEntityWrapper);
//        try {
//            if (rfidCount > 0) {
//                flag = false;
//            }
//            else {
                //id 不为空则修改，为空则新建
//                if (id != null) {
//                    if (count > 1) {
//                        flag = false;
//                    }
//                } else {
//                    if (count > 0) {
//                        flag = false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
        return flag;
    }

    /**
     * 获取绑定单详情列表
     *
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-11
     */
    @RequestMapping(value = "/getDetailList.do")
    @ResponseBody
    public Map<String, Object> getDetailList(String queryJsonString) {

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
        PageUtils utils = materielBindRfidDetailService.queryMaterielBindRfidDetailPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 添加拆分数量
     *
     * @param number
     * @param id
     * @return
     * @author yuany
     * @date 2019-01-30
     */
    @RequestMapping(value = "/updateAmount.do")
    @ResponseBody
    public Map<String, Object> updateAmount(String number, Long id) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(id);

        //判断是否存在出库占用，存在则判断已占用数量+查分数量是否在物料数量范围内，若不存在，则判断拆分数量是否在物料数量范围内
        if(Strings.isNullOrEmpty(materielBindRfidDetail.getOutstorageBillCode())){
            if (Double.parseDouble(materielBindRfidDetail.getAmount()) >= Double.parseDouble(number)) {
                materielBindRfidDetail.setNumber(number);
                result = materielBindRfidDetailService.updateById(materielBindRfidDetail);
            } else {
                msg = "输入数量超出可拆分范围！请重新输入";
            }
        }else {
            //存在出库占用，先判断是否为备料单生成的出库单。
            //获取出库单
            OutStorageManagerVO outStorageManagerVO = outStorageService.selectOne(new EntityWrapper<OutStorageManagerVO>().eq("outstorage_bill_code",materielBindRfidDetail.getOutstorageBillCode()));
            //若出库单与备料单不存在绑定关系，则判断拆分数量是否满足条件
            if (Strings.isNullOrEmpty(outStorageManagerVO.getSpareBillCode())){
                Double trayAmount = Double.parseDouble(materielBindRfidDetail.getOccupyStockAmount()) + Double.parseDouble(number);
                if (Double.parseDouble(materielBindRfidDetail.getAmount()) >= trayAmount){
                    materielBindRfidDetail.setNumber(number);
                    result = materielBindRfidDetailService.updateById(materielBindRfidDetail);
                }else {
                    Double amount = Double.parseDouble(materielBindRfidDetail.getAmount()) - Double.parseDouble(materielBindRfidDetail.getOccupyStockAmount());
                    msg = "此物料存在出库单占用，输入数量超出可拆分范围！最多可拆出【"+amount+"】，请重新输入";
                }
            }else {
                msg = "此物料已被出库单全部占用，不可进行拆分/合并操作";
            }

        }

        map.put("result", result);
        map.put("msg", msg);

        return map;
    }

    /**
     * @Description: 绑定物料详情拆分重量/数量
     * @Param: rfidSelect 为页面搜索RFID的值
     * @return:
     * @Author: yuany
     * @Date: 2019-01-13
     */
    @RequestMapping(value = "/updateAmountAndWeight.do")
    @ResponseBody
    public Map<String, Object> updateAmountAndWeight(String mlWeight, Long id, Long trayBy,String rfidSelect) {
        Map<String, Object> map = new HashMap<>();
        Long userId = getUserId();
        map = trayDetailService.updateAmountAndWeight(mlWeight, id, trayBy, userId, rfidSelect);
        return map;
    }

    /**
     * 获取托盘管理详情列表
     *
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-18
     */
    @RequestMapping(value = "/queryTrayDetailPage.do")
    @ResponseBody
    public Map<String, Object> queryTrayDetailPage(String trayBy) {

        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("trayBy", trayBy);
        PageUtils PagePlatform = trayDetailService.queryTrayDetailPage(map);
        page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
        map.put("rows", PagePlatform.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 删除托盘管理数据
     *
     * @return
     * @author yuany
     * @date 2019-01-18
     */
    @RequestMapping(value = "/delTray")
    @ResponseBody
    public boolean delTray(String ids) {

        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        return trayService.delTray(ids, getUserId());
    }

    /**
     * 单据提交
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/submitTray")
    @ResponseBody
    public Map<String, Object> submitTray(String id) {
        Map<String,Object> map = new HashMap<>();
        Tray tray = trayService.selectById(id);
        if (tray.getStatus().equals(DyylConstant.INVENTORY_1)){
            map.put("msg","已提交单据不可再次提交");
            map.put("result",false);
            return map;
        }
        return trayDetailService.submitTray(id,getUserId());
    }
}
    