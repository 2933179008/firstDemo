package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.service.CustomerService;
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
 * 客户管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/customerManage")
public class CustomerManageController extends AbstractController {

    //顾客管理service
    @Autowired
    private CustomerService customerService;

    /**
     * 跳转到客户管理页面
     *
     * @return
     * @author yuany
     * @date 2018-12-27
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/customerManage/customerManage_list");
        return mv;
    }

    /**
     * 获取客户列表数据
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
        PageUtils utils = customerService.queryPageC(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     * @author pz
     * @date 2019-01-02
     * @return
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/customerManage/customerManage_edit");
        Customer customer = null;
        int status=1;

        //修改
        if (id != -1) {
            customer = customerService.selectById(id);
            status=2;
        }

        mv.addObject("status", status);
        mv.addObject("customer", customer);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 跳转到弹出的详情页面
     * @author anss
     * @date 2019-3-4
     * @return
     */
    @RequestMapping(value = "/toDetail")
    @ResponseBody
    public ModelAndView toDetail(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/customerManage/customerManage_detail");
        Customer customer = customerService.selectById(id);
        int status=2;

        mv.addObject("status", status);
        mv.addObject("customer", customer);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 判断客户编码是否存在
     *  @author pz
     *  @date 2019-01-02
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String customerCode,Long id) {
        boolean errInfo = true;
        // 根据customerCode查询实体list
        EntityWrapper<Customer> wraCustomer = new EntityWrapper<>();
        wraCustomer.eq("customer_code",customerCode);
        try {
            if(id != null)
            {
                int count = customerService.selectCount(wraCustomer);
                if(count>1){
                    errInfo = false;
                }
            }
            else{
                int count = customerService.selectCount(wraCustomer);
                if(count>0){
                    errInfo = false;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return errInfo;
    }

    /**
     * 添加/修改顾客
     * @author pz
     * @date 2018-12-10
     * @return
     */
    @RequestMapping(value = "/addCustomer")
    @ResponseBody
    public boolean addCustomer(Customer customer) {
        boolean result = false;

        if (customer == null) {
            return result;
        }

        //当前时间
        String nowTime = DateUtils.getTime();

        //修改
        if (customer.getId() != null) {
            customer.setUpdateTime(nowTime);
            result = customerService.updateById(customer);
            //新增
        } else {
            customer.setCreateTime(nowTime);
            customer.setUpdateTime(nowTime);
            result = customerService.insert(customer);
        }

        return result;
    }


    /**
     * 删除顾客
     * @author pz
     * @date 2018-12-10
     * @return
     */
    @RequestMapping(value = "/delCustomer")
    @ResponseBody
    public boolean delCustomer(String ids){
        if (StringUtils.isEmpty(ids)) {
            return false;
        }
        return customerService.delLstCustomer(ids, getUserId());
    }

    /**
     * 导出Excel
     *
     * @author pz
     * @date 2019-01-08
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void customerExcel(String ids) {
        List<Customer> list = customerService.getAllListC(ids);
        customerService.toExcel(response, "", list);
    }

}
