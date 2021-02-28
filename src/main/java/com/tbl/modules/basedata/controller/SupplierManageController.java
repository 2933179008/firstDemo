package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.basedata.service.SupplierService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 供应商管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/supplierManage")
public class SupplierManageController extends AbstractController {

    //供应商管理service
    @Autowired
    private SupplierService supplierService;

    /**
     * 跳转到供应商管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/supplierManage/supplierManage_list");
        return mv;
    }

    /**
     * 获取供应商列表数据
     *
     * @return
     * @author pz
     * @date 2019-01-02
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Object getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(queryJsonString)) {
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
        PageUtils utils = supplierService.queryPageS(map);
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
     * @date 2019-01-02
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/supplierManage/supplierManage_edit");
        Supplier supplier = null;
        int status = 1;

        //修改
        if (id != -1) {
            supplier = supplierService.selectById(id);
            status = 2;
        }

        mv.addObject("status", status);
        mv.addObject("supplier", supplier);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 跳转到弹出的详情页面
     * @return
     * @author anss
     * @date 2019-3-4
     */
    @RequestMapping(value = "/toDetail")
    @ResponseBody
    public ModelAndView toDetail(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/supplierManage/supplierManage_detail");
        Supplier supplier = supplierService.selectById(id);

        mv.addObject("status", 2);
        mv.addObject("supplier", supplier);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 判断供应商编码是否存在
     *
     * @author pz
     * @date 2019-01-02
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String supplierCode, Long id) {
        boolean flag = true;
        // 根据supplierCode查询实体list
        EntityWrapper<Supplier> wraSupplier = new EntityWrapper<>();
        wraSupplier.eq("supplier_code", supplierCode);
        try {
            if (id != null) {
                int count = supplierService.selectCount(wraSupplier);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = supplierService.selectCount(wraSupplier);
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
     * 添加/修改供应商
     *
     * @return
     * @author pz
     * @date 2018-12-10
     */
    @RequestMapping(value = "/addSupplier")
    @ResponseBody
    public boolean addSupplier(Supplier supplier) {
        boolean result = false;

        //当前时间
        String nowTime = DateUtils.getTime();

        if (supplier == null) {
            return result;
        }
        //修改
        if (supplier.getId() != null) {
            supplier.setUpdateTime(nowTime);
            result = supplierService.updateById(supplier);
            //新增
        } else {
            supplier.setCreateTime(nowTime);
            supplier.setUpdateTime(nowTime);
            result = supplierService.insert(supplier);
        }
        return result;
    }


    /**
     * 删除供应商
     *
     * @return
     * @author pz
     * @date 2018-12-10
     */
    @RequestMapping(value = "/delSupplier")
    @ResponseBody
    public boolean delSupplier(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return false;
        }
        return supplierService.delLstSupplier(ids, getUserId());
    }

    /**
     * 导出Excel
     *
     * @author pz
     * @date 2019-01-08
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void supplierExcel(String ids) {
        List<Supplier> list = supplierService.getAllListS(ids);
        supplierService.toExcel(response, "", list);
    }

}