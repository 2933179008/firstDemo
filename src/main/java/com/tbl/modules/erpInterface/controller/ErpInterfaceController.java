package com.tbl.modules.erpInterface.controller;

import com.tbl.modules.erpInterface.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: dyyl
 * @description: Erp提供接口Controller
 * @author: pz
 * @create: 2019-02-15
 **/
@Controller
@RequestMapping(value="/ErpInterface")
public class ErpInterfaceController {

    //顾客ErpService
    @Autowired
    private CustomerErpInterfaceService customerErpInterfaceService;

    //供应商ErpService
    @Autowired
    private SupplierErpInterfaceService supplierErpInterfaceService;

    //物料ErpService
    @Autowired
    private MaterielErpInterfaceService materielErpInterfaceService;

    //采购订单（收货计划）ErpService
    @Autowired
    private ReceiptErpInterfaceService receiptInterfaceService;

    //生产任务（备料单）ErpService
    @Autowired
    private SpareBillErpInterfaceService spareBillInterfaceService;



    /**
     * 生成物料信息
     * @author pz
     * @Date 2019-02-15
     * @param materielArr 参数集合
     * @return
     */
    @RequestMapping(value = "/materielInfo", method = RequestMethod.POST)
    @ResponseBody
    public String materielInfo(@RequestBody String materielArr){
        return materielErpInterfaceService.materielInfo(materielArr);
    }

    /**
     * 生成顾客信息
     * @author pz
     * @Date 19-02-15
     * @param customerInfo 参数集合
     * @return
     */
    @RequestMapping(value = "/customerInfo", method = RequestMethod.POST)
    @ResponseBody
    public String customerInfo(@RequestBody String customerInfo){
       return  customerErpInterfaceService.customerInfo(customerInfo);
    }

    /**
     * 生成供应商信息
     * @author pz
     * @Date 19-02-15
     * @param suppilerInfo 参数集合
     * @return
     */
    @RequestMapping(value = "/suppilerInfo", method = RequestMethod.POST)
    @ResponseBody
    public String suppilerInfo(@RequestBody String suppilerInfo){
        return supplierErpInterfaceService.supplierInfo(suppilerInfo);
    }

    /**
     * 生成收货单信息
     * @author pz
     * @Date 2019-02-15
     * @param paramInfo 参数集合
     * @return
     */
    @RequestMapping(value = "/receiptInfo", method = RequestMethod.POST)
    @ResponseBody
    public String receiptInfo(@RequestBody String paramInfo){

        return receiptInterfaceService.receiptInfo(paramInfo);
    }

    /**
     * 生成生产任务信息
     * @author pz
     * @Date 2019-02-15
     * @param spareInfo bomInfo 参数集合
     * @return
     */
    @RequestMapping(value = "/spareBillInfo", method = RequestMethod.POST)
    @ResponseBody
    public String spareBillInfo(@RequestBody String spareInfo){

        return spareBillInterfaceService.spareBillInfo(spareInfo);
    }

}
    