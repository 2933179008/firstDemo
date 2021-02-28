package com.tbl.modules.erpInterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.SupplierDAO;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.erpInterface.service.SupplierErpInterfaceService;
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
@Service("supplierErpInterfaceService")
public class SupplierErpInterfaceServiceImpl implements SupplierErpInterfaceService {

    @Autowired
    private SupplierDAO supplierDAO;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 生成客户供应商信息
     *
     * @param supplierInfo
     * @return
     * @author pz
     * @date 2019-02-15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String supplierInfo(String supplierInfo) {

        String nowDate = DateUtils.getTime();

        JSONObject supplierInfoObj = JSON.parseObject(supplierInfo);

        JSONObject resultObj = new JSONObject();
        Long supId = supplierInfoObj.getLong("id");
        if (supId == null) {
            resultObj.put("msg", "供应商id不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }
        Supplier sup = supplierDAO.selectById(supId);
        //供应商编号
        String supplierCode = supplierInfoObj.getString("supplierCode");
        //供应商名称
        String supplierName = supplierInfoObj.getString("supplierName");
        //供应商类型
        String supplierType = supplierInfoObj.getString("supplierType");
        //联系人
        String linkman = supplierInfoObj.getString("linkman");
        //联系电话
        String telephone = supplierInfoObj.getString("telephone");
        //地址
        String address = supplierInfoObj.getString("address");
        //邮箱
        String mail = supplierInfoObj.getString("mail");
        //备注
        String remark = supplierInfoObj.getString("remark");

        if (StringUtils.isEmpty(supplierCode)) {
            resultObj.put("msg", "供应商编码不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        if (StringUtils.isEmpty(supplierName)) {
            resultObj.put("msg", "供应商名称不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        if (sup == null) {

            EntityWrapper<Supplier> supplierEntity = new EntityWrapper<>();
            supplierEntity.eq("supplier_code", supplierCode);
            int count = supplierDAO.selectCount(supplierEntity);

            if (count > 0) {
                resultObj.put("msg", "供应商编码不能重复！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }

            boolean supplierResult = supplierDAO.savaSupplier(supId, supplierCode, supplierName, supplierType,
                    linkman, telephone, address, mail, remark, nowDate);
            if (supplierResult) {
                resultObj.put("msg", "供应商添加成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("供应商调用接口", supplierInfoObj.getString("supplierCode"), resultObj.get("msg").toString(), nowDate);
            } else {
                resultObj.put("msg", "失败原因：“供应商添加失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("供应商调用接口", supplierInfoObj.getString("supplierCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        } else {

            // 保存供应商实体
            sup.setSupplierCode(supplierCode);
            sup.setSupplierName(supplierName);
            sup.setSupplierType(supplierType);
            sup.setLinkman(linkman);
            sup.setTelephone(telephone);
            sup.setAddress(address);
            sup.setMail(mail);
            sup.setRemark(remark);
            sup.setUpdateTime(nowDate);
            boolean updateSupplierResult = supplierDAO.updateById(sup) > 0;
            if (updateSupplierResult) {
                resultObj.put("msg", "供应商修改成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("供应商调用接口", supplierInfoObj.getString("supplierCode"), resultObj.get("msg").toString(), nowDate);
            } else {
                resultObj.put("msg", "失败原因：“供应商修改失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("供应商调用接口", supplierInfoObj.getString("supplierCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        }

        return JSON.toJSONString(resultObj);
    }

}
    