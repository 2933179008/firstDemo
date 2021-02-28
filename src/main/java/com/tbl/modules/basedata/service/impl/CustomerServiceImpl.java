package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.CustomerDAO;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.util.DeriveExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 顾客service实现
 *
 * @author pz
 * @date 2018-01-02
 */
@Service("customerService")
public class CustomerServiceImpl extends ServiceImpl<CustomerDAO, Customer> implements CustomerService {

    // 客户DAO
    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public PageUtils queryPageC(Map<String, Object> params) {

        //客户编码
        String customerCode = (String) params.get("customerCode");
        //货主
        String customerName = (String) params.get("customerName");

        Page<Customer> page = this.selectPage(
                new Query<Customer>(params).getPage(),
                new EntityWrapper<Customer>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotEmpty(customerCode), "customer_code", customerCode)
                        .like(StringUtils.isNotEmpty(customerName), "customer_name", customerName)

        );
        return new PageUtils(page);
    }

    /**
     * 删除客户实体（逻辑删除）
     * @author pz
     * @date 2018-01-02
     * @param ids:要删除的id集合
     * @param userId：当前登陆人Id
     * @return
     */
    @Override
    public boolean delLstCustomer(String ids, Long userId) {
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Customer> lstCustomer = this.selectBatchIds(lstIds);

        for (Customer customer: lstCustomer) {
            customer.setDeletedFlag(DyylConstant.DELETED);
            customer.setDeletedBy(userId);
        }
        return updateBatchById(lstCustomer);
    }

    /**
     * 获取导出列
     *
     * @return List<Customer>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<Customer> getAllListC(String ids) {
        System.out.println(ids);
        List<Customer> list = null;
        if(ids.equals("")||ids==null) {
            EntityWrapper<Customer> wraCustomer = new EntityWrapper<>();
            wraCustomer.eq("deleted_flag", DyylConstant.NOTDELETED);
            list = this.selectList(wraCustomer);
        }else{
            list = customerDAO.getAllLists(StringUtils.stringToList(ids));
            list.forEach(a->{

            });
        }

        return list;
    }

    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Customer> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "客户管理表" + "(" + date + ")";
            if (path != null && !"".equals(path)) {
                sheetName = sheetName + ".xls";
            } else {
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-disposition", "attachment;filename="
                        + new String(sheetName.getBytes("gbk"), "ISO8859-1") + ".xls");
            }

            Map<String, String> mapFields = new LinkedHashMap<String, String>();
            mapFields.put("customerCode", "客户编码");
            mapFields.put("customerName", "客户名称");
            mapFields.put("customerType", "客户类别");
            mapFields.put("linkman", "联系人");
            mapFields.put("telephone", "联系方式");
            mapFields.put("address", "地址");
            mapFields.put("mail", "邮箱");
            mapFields.put("remark", "备注");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
