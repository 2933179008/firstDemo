package com.tbl.modules.stock.controller.inventory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.common.utils.BarcodeUtil;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.InventoryTask;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.InventoryTaskDetailService;
import com.tbl.modules.stock.service.InventoryTaskService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 盘点任务controller
 * @Param:
 * @return:
 * @Author: pz
 * @date 2018-12-26
 */
@Controller
@RequestMapping(value = "/inventoryTask")
public class InventoryTaskController extends AbstractController {

    //盘点任务service
    @Autowired
    private InventoryTaskService inventoryTaskService;

    //盘点任务详情service
    @Autowired
    private InventoryTaskDetailService inventoryTaskDetailService;

    //用户service
    @Autowired
    private UserService userService;

    //库存service
    @Autowired
    private StockService stockService;

    //库存Dao
    @Autowired
    private StockDAO stockDAO;

    /**
     * @Description: 跳转到盘点记录列表页面
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2018/12/26
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/stock/inventory/inventoryTask_list");
        return mv;
    }

    /**
     * @Description: 获取盘点记录管理列表
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
        PageUtils Inventoryform = inventoryTaskService.queryPageI(map);
        page.setTotalRows(Inventoryform.getTotalCount() == 0 ? 1 : Inventoryform.getTotalCount());
        map.put("rows", Inventoryform.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 弹出到添加/编辑页面
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-14
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(Long id) {
        ModelAndView mv = this.getModelAndView();
        if (id == -1) {//跳转到新增页面
            //获取盘点编号
            String inventoryTaskCode = inventoryTaskService.generatInventoryTaskCode();
            mv.addObject("inventoryTaskCode", inventoryTaskCode);
        } else {
            InventoryTask inventoryTask = inventoryTaskService.selectById(id);
            mv.addObject("inventoryTaskCode", inventoryTask.getInventoryTaskCode());
            mv.addObject("inventoryTask", inventoryTask);
        }
        //获取用户集合
        List<User> userList = userService.selectList(
                new EntityWrapper<>()
        );
        mv.addObject("userList", userList);
        mv.setViewName("techbloom/stock/inventory/inventoryTask_edit");
        return mv;
    }

    /**
     * 获取库位下拉数据
     *
     * @param positionCode
     * @return Map
     * @author pz
     * @date 2019-03-15
     */
    @RequestMapping(value = "/getSelectPositionCode")
    @ResponseBody
    public Map<String, Object> getSelectPositionCode(String positionCode) {
        Map<String, Object> map = new HashMap<>();

        List<Stock> lstStock=stockDAO.uniqeList(null);
        JSONArray arr = new JSONArray();
        JSONObject obj = null;
        for (Stock s : lstStock) {
            obj = new JSONObject();
            // 库位编码
            obj.put("id", s.getPositionCode());
            // 库位名称
            obj.put("text", s.getPositionCode());
            arr.add(obj);
        }

        // 根据条件获取已添加的库位信息
        if (StringUtils.isNotBlank(positionCode)) {
            Map<String, Object> sceneMap = new HashMap<>();
            sceneMap.put("position_code", positionCode);
            List<Stock> stockList = stockService.selectByMap(sceneMap);
            if (stockList != null && stockList.size() > 0) {
                for (Stock s : stockList) {
                    obj = new JSONObject();
                    obj.put("id", s.getPositionCode());
                    obj.put("text", s.getPositionCode());
                    arr.add(obj);
                }
            }
        }
        map.put("result", arr);
        return map;
    }

    /**
     * 获取库位信息（修改时，库位下拉框赋值使用）
     *
     * @return
     * @author pz
     * @date 2019-03-15
     */
    @RequestMapping(value = "/getPositionInfo")
    @ResponseBody
    public JSONArray getPlatformInfo(String positionCodes) {
        JSONArray arr = new JSONArray();
        JSONObject obj = null;
        Map<String, Object> map = new HashMap<>();
        //lambl表达式
        List<String> lstPositionCodes = Arrays.stream(positionCodes.split(",")).collect(Collectors.toList());

        List<Stock> lstStock=stockDAO.uniqeList(lstPositionCodes);
        if (lstStock != null && lstStock.size() > 0) {
            for (Stock s : lstStock) {
                obj = new JSONObject();
                obj.put("userId", s.getPositionCode());
                obj.put("name", s.getPositionCode());
                arr.add(obj);
            }
        }
        return arr;
    }

    /**
     * select查询盘点类型数查询对应的盘点类型
     *
     * @return Map<String , Object>
     * @author pz
     * @date 2019-01-28
     */
    @RequestMapping(value = "/getInventoryTaskById")
    @ResponseBody
    public InventoryTask getInventoryTaskById(Long id) {
        Map<String, Object> map = new HashMap<>();
        InventoryTask sc = inventoryTaskService.getInventoryTaskVoById(id);
        Page<Stock> pageList = stockService.getSelectPositionList(map);
        List<Stock> list = pageList.getRecords();

        if (sc != null && list.size() > 0) {
            for (Stock vo : list) {
                if (vo.getPositionCode().equals(sc.getPositionCode())) {
                    sc.setPositionCode(vo.getPositionCode());
                    break;
                }
            }
        }
        return sc;
    }

    /**
     * @Description: 添加/修改盘点任务
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-15
     */
    @RequestMapping(value = "/saveInventoryTask")
    @ResponseBody
    public Map<String, Object> saveInventoryTask(InventoryTask inventoryTask) {
        return inventoryTaskService.saveInventoryTask(inventoryTask, getUserId());
    }

    /**
     * @Description: 提交
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/15
     */
    @RequestMapping(value = "/submitInventoryTask")
    @ResponseBody
    public Map<String, Object> submitInventoryTask(Long id) {
        return inventoryTaskService.submitInventoryTask(id);
    }

    /**
     * @param stockIdStr 盘点任务主键集合
     * @return Map<String , Object> ,Object>
     * @Description: update审核盘点任务
     * @author pz
     * @date 2019-01-15
     */
    @RequestMapping(value = {"/auditTaskCheck"})
    @ResponseBody
    public Map<String, Object> auditTaskCheck(String stockIdStr,String qualityDateStr,String qualityPeriodStr) {
        return inventoryTaskService.auditTaskCheck(stockIdStr,qualityDateStr,qualityPeriodStr,getUserId());
    }

    /**
     * @Description: 复盘
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-22
     */
    @RequestMapping(value = {"/replayInventory"})
    @ResponseBody
    public Map<String, Object> replayInventory(Long id) {
        return inventoryTaskService.replayInventory(id);
    }

    /**
     * @Description: 删除盘点任务
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-14
     */
    @RequestMapping(value = "/delInventoryTask")
    @ResponseBody
    public Map<String, Object> delInventory(String ids) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        if (StringUtils.isEmpty(ids)) {
            map.put("result", false);
            map.put("msg", "请选择一个要删除的盘点任务！");
            return map;
        }
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        EntityWrapper<InventoryTask> wraInventoryTask = new EntityWrapper<>();
        int cou = inventoryTaskService.selectCount(wraInventoryTask.in("id", lstIds).ne("state", "0"));
        if (cou == 0) {
            result = inventoryTaskService.deleteBatchIds(lstIds);

            // 根据任务单ids批量删除任务单详细实体list
            EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
            wraInventoryTaskDetail.in("inventory_task_id", lstIds);
            inventoryTaskDetailService.delete(wraInventoryTaskDetail);

            if (result) {
                map.put("msg", "删除成功！");
            } else {
                map.put("msg", "删除失败，请稍后重试。");
            }
        } else {
            map.put("msg", "盘点任务已提交，不可删除！");
        }

        map.put("result", result);
        return map;
    }

    /**
     * @Description: 导出盘点任务Excel
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-14
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void inventoryExcel(String ids) {
        List<InventoryTask> list = inventoryTaskService.getAllListI(ids);
        inventoryTaskService.toExcel(response, "", list);
    }

    /**
     * @Description: 获取盘点任务详细列表
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/01/14
     */
    @RequestMapping(value = "/getTaskDetailList")
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
        PageUtils InventoryTask = inventoryTaskDetailService.queryPageI(map, id);
        page.setTotalRows(InventoryTask.getTotalCount() == 0 ? 1 : InventoryTask.getTotalCount());
        map.put("rows", InventoryTask.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 删除盘点任务详情
     * @Param:
     * @return:
     * @author pz
     * @date 2019-01-09
     */
    @RequestMapping(value = "/delInventoryTaskDetail")
    @ResponseBody
    public Map<String, Object> delInventoryTaskDetail(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(ids)) {
            map.put("result", false);
            map.put("msg", "请选择一个要删除的盘点任务信息！");
            return map;
        }
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        boolean result = inventoryTaskDetailService.deleteBatchIds(lstIds);

        if (result) {
            map.put("msg", "删除成功！");
        } else {
            map.put("msg", "删除失败，请稍后重试。");
        }
        map.put("result", result);
        return map;
    }

    /**
     * 打印盘点单据
     *
     * @param
     * @return ModelAndView
     * @author pz
     * @date 2018-10-09R
     */
    @RequestMapping(value = "/printInventoryTask")
    @ResponseBody
    public ModelAndView printStockCheck(Long id) {
        ModelAndView mv = new ModelAndView();

        //获取盘点任务单号
        InventoryTask task = inventoryTaskService.selectById(id);
        String task_code = task.getInventoryTaskCode();

        //获取盘点任务单详情
        List<InventoryTaskDetail> taskDetailList = inventoryTaskDetailService.taskDeatilList(id);
        mv.addObject("task_code", task_code);
        mv.addObject("taskDetailList", taskDetailList);
        mv.setViewName("techbloom/stock/inventory/inventoryTask_print");
        return mv;
    }

    /**
     * @Description: 生成条形码
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/02/11
     */
    @RequestMapping(value = "/generateBarcode")
    @ResponseBody
    public void generateBarcode(HttpServletResponse resp, String msg){
//        DecimalFormat df=new DecimalFormat("0000000000");
//        msg=df.format(Integer.parseInt(msg));
        byte[] b = BarcodeUtil.generate(msg);
        try {
            resp.getOutputStream().write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
