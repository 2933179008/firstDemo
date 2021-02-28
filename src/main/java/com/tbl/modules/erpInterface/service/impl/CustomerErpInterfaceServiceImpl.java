package com.tbl.modules.erpInterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.CustomerDAO;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.erpInterface.service.CustomerErpInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: dyyl
 * @description: 生成顾客与供应商调用接口service实现
 * @author: pz
 * @create: 2019-02-15
 **/
@Service("customerErpInterfaceService")
public class CustomerErpInterfaceServiceImpl implements CustomerErpInterfaceService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 生成客户信息
     *
     * @param customerInfo
     * @return
     * @author pz
     * @date 2019-02-15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String customerInfo(String customerInfo) {

        String nowDate = DateUtils.getTime();

        JSONObject customerInfoObj = JSON.parseObject(customerInfo);

        JSONObject resultObj = new JSONObject();

        Long cusId = customerInfoObj.getLong("id");
        if (cusId == null) {
            resultObj.put("msg", "顾客id不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }
        Customer cust = customerDAO.selectById(cusId);
        //顾客编码
        String customerCode = customerInfoObj.getString("customerCode");
        //顾客名称
        String customerName = customerInfoObj.getString("customerName");
        //顾客类型
        String customerType = customerInfoObj.getString("customerType");
        //联系人
        String linkman = customerInfoObj.getString("linkman");
        //联系电话
        String telephone = customerInfoObj.getString("telephone");
        //地址
        String address = customerInfoObj.getString("address");
        //邮箱
        String mail = customerInfoObj.getString("mail");
        //备注
        String remark = customerInfoObj.getString("remark");

        if (StringUtils.isEmpty(customerCode)) {
            resultObj.put("msg", "顾客编码不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        if (StringUtils.isEmpty(customerName)) {
            resultObj.put("msg", "顾客名称不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        if (cust == null) {

            EntityWrapper<Customer> customerEntity = new EntityWrapper<>();
            customerEntity.eq("customer_code", customerCode);
            int count = customerDAO.selectCount(customerEntity);

            if (count > 0) {
                resultObj.put("msg", "顾客编码不能重复！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }

            boolean customerResult = customerDAO.savaCustomer(cusId, customerCode, customerName, customerType,
                    linkman, telephone, address, mail, remark, nowDate);
            if (customerResult) {
                resultObj.put("msg", "顾客添加成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("顾客调用接口", customerInfoObj.getString("customerCode"), resultObj.get("msg").toString(), nowDate);
            } else {
                resultObj.put("msg", "顾客添加失败原因：“添加失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("顾客调用接口", customerInfoObj.getString("customerCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        } else {

            // 保存客户实体
            cust.setCustomerCode(customerCode);
            cust.setCustomerName(customerName);
            cust.setCustomerType(customerType);
            cust.setLinkman(linkman);
            cust.setTelephone(telephone);
            cust.setAddress(address);
            cust.setMail(mail);
            cust.setRemark(remark);
            cust.setUpdateTime(nowDate);

            boolean updateCustomerResult = customerDAO.updateById(cust) > 0;

            if (updateCustomerResult) {
                resultObj.put("msg", "顾客修改成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("顾客调用接口", customerInfoObj.getString("customerCode"), resultObj.get("msg").toString(), nowDate);
            } else {
                resultObj.put("msg", "失败原因：“顾客修改失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("顾客调用接口", customerInfoObj.getString("customerCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        }

        return JSON.toJSONString(resultObj);
    }

}
    