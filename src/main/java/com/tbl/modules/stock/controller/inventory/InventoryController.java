package com.tbl.modules.stock.controller.inventory;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.stock.entity.*;
import com.tbl.modules.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pz
 * @Description: 盘点controller
 * @date 2018-12-26
 */
@Controller
@RequestMapping(value = "/inventory")
public class InventoryController extends AbstractController {

    //盘点service
    @Autowired
    private InventoryService inventoryService;

    //盘点详情service
    @Autowired
    private InventoryDetailService inventoryDetailService;

    //盘点任务service
    @Autowired
    private InventoryTaskService inventoryTaskService;

    //用户service
    @Autowired
    private UserService userService;

    //库存service
    @Autowired
    private StockService stockService;

    //物料service
    @Autowired
    private MaterielService materielService;

    //物料绑定service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    /**
     * @Description: 跳转到盘点列表页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/inventory/inventory_list");
        return mv;
    }

    /**
     * @Description: 获取盘点管理列表
     * @Param:
     * @return:
     * @Author: pz
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
        PageUtils Inventoryform = inventoryService.queryPageI(map);
        page.setTotalRows(Inventoryform.getTotalCount() == 0 ? 1 : Inventoryform.getTotalCount());
        map.put("rows", Inventoryform.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * @param
     * @return ModelAndView
     * @Description: 弹出到添加/编辑页面
     * @author pz
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        if (id == -1) {//跳转到新增页面
            //获取盘点编号
            String inventoryCode = inventoryService.generateInventoryCode();
            // 根据inventoryCode查询实体list
            mv.addObject("inventoryCode", inventoryCode);
        } else {
            Inventory inventory = inventoryService.selectById(id);
            mv.addObject("inventoryCode", inventory.getInventoryCode());
            mv.addObject("inventory", inventory);
        }

        //获取盘点任务集合
        List<InventoryTask> inventoryTaskList = inventoryTaskService.selectList(
                new EntityWrapper<>()
        );

        //获取物料集合
        List<Materiel> materielList = materielService.selectList(
                new EntityWrapper<Materiel>().eq("deleted_flag", DyylConstant.NOTDELETED)
        );

        //获取用户集合
        List<User> userList = userService.selectList(
                new EntityWrapper<>()
        );

        //获取物料绑定集合
        List<MaterielBindRfid> materielBindRfidList = materielBindRfidService.selectList(
                new EntityWrapper<MaterielBindRfid>().eq("deleted_flag",DyylConstant.NOTDELETED)
                        .eq("status",DyylConstant.STATE_UNTREATED)
        );

        mv.addObject("materielBindRfidList", materielBindRfidList);
        mv.addObject("materielList", materielList);
        mv.addObject("userList", userList);
        mv.addObject("inventoryTaskList", inventoryTaskList);
        mv.setViewName("techbloom/stock/inventory/inventory_edit");
        return mv;
    }

    /**
     * @Description: 添加/修改盘点
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-14
     */
    @RequestMapping(value = "/saveInventory")
    @ResponseBody
    public Map<String, Object> saveInventory(Inventory inventory) {
        return inventoryService.saveInventory(inventory, getUserId());
    }

    /**
     * @Description: 提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    @RequestMapping(value = "/submitInventory")
    @ResponseBody
    public Map<String, Object> submitInventory(Long id) {
        return inventoryService.submitInventory(id);
    }

    /**
     * @Description: 删除盘点
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-09
     */
    @RequestMapping(value = "/delInventory")
    @ResponseBody
    public Map<String, Object> delInventory(String ids) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        if (StringUtils.isEmpty(ids)) {
            map.put("result", false);
            map.put("msg", "请选择一个要删除的任务单！");
            return map;
        }
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //依据盘点id与状态为已提交查询盘点记录，如果存在则不能删除。反之可删除
        EntityWrapper<Inventory> wraInventory = new EntityWrapper<>();
        wraInventory.in("id", lstIds);
        wraInventory.eq("state", DyylConstant.INVENTORY_1);
        int cou = inventoryService.selectCount(wraInventory);

        if (cou == 0) {
            //删除盘点记录
            boolean inventoryFlag = inventoryService.deleteBatchIds(lstIds);
            // 根据盘点记录ids批量删除盘点记录详请实体list
            EntityWrapper<InventoryDetail> wraInventoryDetail = new EntityWrapper<>();
            wraInventoryDetail.in("inventory_id", lstIds);
            boolean inventoryDetailFlag = inventoryDetailService.delete(wraInventoryDetail);

            result = inventoryFlag && inventoryDetailFlag;
            if (result) {
                map.put("msg", "删除成功！");
            } else {
                map.put("msg", "删除失败，请稍后重试。");
            }
        } else {
            map.put("msg", "盘点已提交，不可删除！");
        }

        map.put("result", result);
        return map;
    }

    /**
     * @Description: 导出盘点Excel
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-10
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void inventoryExcel(String ids) {
        List<Inventory> list = inventoryService.getAllListI(ids);
        inventoryService.toExcel(response, "", list);
    }

    /**
     * @Description: 获取盘点详细列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/01/09
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(Long id, String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }

        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils Inventoryform = inventoryDetailService.queryPageID(map, id);
        page.setTotalRows(Inventoryform.getTotalCount() == 0 ? 1 : Inventoryform.getTotalCount());
        map.put("rows", Inventoryform.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 获取库位下拉列表数据源
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    @RequestMapping(value = "/getSelectPositionCode")
    @ResponseBody
    public Map<String, Object> getSelectPositionCode(Long inventoryTaskId, String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> PositionList = inventoryService.getSelectPositionList(inventoryTaskId, queryString, pageSize, pageNo);
        map.put("result", PositionList);
        map.put("total", inventoryService.getSelectPositionTotal(inventoryTaskId, queryString));
        return map;

    }

    /**
     * @Description: 保存详情
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    @RequestMapping(value = "/addInventoryDetail")
    @ResponseBody
    public Map<String, Object> addInventoryDetail(Long id, String positionCode,String rfid,String materielCode,String materielType) {
        return inventoryDetailService.saveInventoryDetail(id, positionCode,rfid, materielCode, materielType, getUserId());
    }

    /**
     * @Description: 删除盘点详情
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-09
     */
    @RequestMapping(value = "/delInventoryDetail")
    @ResponseBody
    public Map<String, Object> delInventoryDetail(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(ids)) {
            map.put("result", false);
            map.put("msg", "请选择一个要删除的盘点详情信息！");
            return map;
        }
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        boolean result = inventoryDetailService.deleteBatchIds(lstIds);

        if (result) {
            map.put("msg", "删除成功！");
        } else {
            map.put("msg", "删除失败，请稍后重试。");
        }
        map.put("result", result);
        return map;
    }

    /**
     * @Description: 修改库位编号
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updatePositionCode"})
    @ResponseBody
    public Map<String, Object> updatePositionCode(Long id, String positionCode) {
        Map<String, Object> map = Maps.newHashMap();
        if (id == null || StringUtils.isEmptyString(positionCode)) {
            map.put("message", "请输入库位编码!");
            map.put("result", false);
            return map;
        }
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        int cou = stockService.selectCount(wraStock.eq("position_code", positionCode));
        if (cou == 0) {
            map.put("message", "请输入库存中存在的库位!");
            map.put("result", false);
            return map;
        }
        return inventoryService.updatePositionCode(id, positionCode);
    }

    /**
     * @Description: 修改rfid
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateRfid"})
    @ResponseBody
    public Map<String, Object> updateRfid(Long id, String rfid) {
        return inventoryService.updateRfid(id, rfid);
    }


    /**
     * @Description: 修改物料编号
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateMaterialCode"})
    @ResponseBody
    public Map<String, Object> updateMaterialCode(Long id, String materialCode) {
        Map<String, Object> map = Maps.newHashMap();
        if (id == null || StringUtils.isEmptyString(materialCode)) {
            map.put("message", "请输入物料编号!");
            map.put("result", false);
            return map;
        }
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        int cou = stockService.selectCount(wraStock.eq("material_code", materialCode));
        if (cou == 0) {
            map.put("message", "请输入库存中存在的物料编号!");
            map.put("result", false);
            return map;
        }
        return inventoryService.updateMaterialCode(id, materialCode);
    }

    /**
     * @Description: 修改物料名称
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateMaterialName"})
    @ResponseBody
    public Map<String, Object> updateMaterialName(Long id, String materialName) {
        Map<String, Object> map = Maps.newHashMap();
        if (id == null || StringUtils.isEmptyString(materialName)) {
            map.put("message", "请输入物料名称!");
            map.put("result", false);
            return map;
        }
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        int cou = stockService.selectCount(wraStock.eq("material_name", materialName));
        if (cou == 0) {
            map.put("message", "请输入库存中存在的物料名称!");
            map.put("result", false);
            return map;
        }
        return inventoryService.updateMaterialName(id, materialName);
    }

    /**
     * @Description: 修改批次号
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateBatchNo"})
    @ResponseBody
    public Map<String, Object> updateBatchNo(Long id, String batchNo) {
        Map<String, Object> map = Maps.newHashMap();
        if (id == null || StringUtils.isEmptyString(batchNo)) {
            map.put("message", "请输入批次号!");
            map.put("result", false);
            return map;
        }
        EntityWrapper<Stock> wraStock = new EntityWrapper<>();
        int cou = stockService.selectCount(wraStock.eq("batch_no", batchNo));
        if (cou == 0) {
            map.put("message", "请输入库存中存在的批次号!");
            map.put("result", false);
            return map;
        }
        return inventoryService.updateBatchNo(id, batchNo);
    }

    /**
     * @Description: 修改盘点结果数量
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateInventoryAmount"})
    @ResponseBody
    public Map<String, Object> updateInventoryAmount(Long id, String inventoryAmount) {
        return id == null || inventoryAmount == null ? null : inventoryService.updateInventoryAmount(id, inventoryAmount);
    }

    /**
     * @Description: 修改盘点结果重量
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-17
     */
    @RequestMapping(value = {"/updateInventoryWeight"})
    @ResponseBody
    public Map<String, Object> updateInventoryWeight(Long id, String inventoryWeight) {
        return id == null || inventoryWeight == null ? null : inventoryService.updateInventoryWeight(id, inventoryWeight);
    }

    /**
     * @Description: 获取库位下拉列表数据源
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/getSelectPosition")
    @ResponseBody
    public Map<String, Object> getSelectPosition(Long inventoryTaskId, String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> positionList = inventoryService.getSelectPositionList(inventoryTaskId, queryString, pageSize, pageNo);
        map.put("result", positionList);
        map.put("total", inventoryService.getSelectPositionTotal(inventoryTaskId, queryString));
        return map;
    }


    /**
     * @Description: 获取物料下拉列表数据源
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/24
     */
    @RequestMapping(value = "/getSelectMateriel")
    @ResponseBody
    public Map<String, Object> getSelectMateriel(Long inventoryTaskId, String positionCode, String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> materielList = inventoryService.getSelectMaterielList(inventoryTaskId, positionCode, queryString, pageSize, pageNo);
        map.put("result", materielList);
        map.put("total", inventoryService.getSelectMaterielTotal(inventoryTaskId, positionCode, queryString));
        return map;
    }

    /**
     * 跳转到弹出的查看页面
     *
     * @return
     * @author anss
     * @date 2019-3-5
     */
    @RequestMapping(value = "/toDetail.do")
    @ResponseBody
    public ModelAndView toDetail(Long id) {
        ModelAndView mv = this.getModelAndView();

        Inventory inventory = null;

        //根据id获取一条绑定数据，根据绑定的RFID获取符合条件的物料结合
        inventory = inventoryService.selectById(id);

        //获取盘点记录详情
        List<InventoryDetail> inventoryDetailList = inventoryDetailService.selectList(
                new EntityWrapper<InventoryDetail>().
                        eq("inventory_id", id)
        );
        mv.addObject("inventory", inventory);
        mv.addObject("inventoryDetailList", inventoryDetailList);
        mv.setViewName("techbloom/stock/inventory/inventory_detail");
        return mv;
    }

}
