package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.ReceiptDAO;
import com.tbl.modules.instorage.entity.Instorage;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.instorage.entity.Receipt;
import com.tbl.modules.instorage.service.ReceiptService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.util.DeriveExcel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: dyyl
 * @description: 收货计划service实现类
 * @author: zj
 * @create: 2019-01-07 13:33
 **/
@Service("receiptService")
public class ReceiptServiceImpl extends ServiceImpl<ReceiptDAO, Receipt> implements ReceiptService {
    @Autowired
    private ReceiptDAO receiptDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //收货单号
        String receiptCode = (String) params.get("receiptNo");
        //状态
        String state = (String) params.get("status");

        Page<Receipt> page = this.selectPage(
                new Query<Receipt>(params).getPage(),
                new EntityWrapper<Receipt>()
                        .like(StringUtils.isNotEmpty(receiptCode), "receipt_code", receiptCode)
                        .eq(StringUtils.isNotEmpty(state), "state", state)
        );

        for (Receipt receipt : page.getRecords()) {
            if (receipt.getCreateBy() != null) {
                receipt.setCreateName(userDAO.selectById(receipt.getCreateBy()).getUsername());
            }
        }

        return new PageUtils(page);
    }


    @Override
    public String generateReceiptCode() {
        //收货单编号
        String receiptCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.RECEIPT_CODE_FORMAT);
        //获取最大收货单编号
        String maxReceiptCode = receiptDAO.getMaxReceiptCode();
        if(StringUtils.isEmptyString(maxReceiptCode)){
            receiptCode = "AS00000001";
        }else{
            Integer maxReceiptCode_count = Integer.parseInt(maxReceiptCode.replace("AS",""));
            receiptCode = df.format(maxReceiptCode_count+1);
        }
        return receiptCode;
    }

    @Override
    public List<Map<String, Object>> getSelectSupplierList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return receiptDAO.getSelectSupplierList(page,queryString);
    }

    @Override
    public Integer getSelectSupplierTotal(String queryString) {
        return receiptDAO.getSelectSupplierTotal(queryString);
    }

    @Override
    public List<Map<String, Object>> getSelectCustomerList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return receiptDAO.getSelectCustomerList(page,queryString);
    }

    @Override
    public Integer getSelectCustomerTotal(String queryString) {
        return receiptDAO.getSelectCustomerTotal(queryString);
    }

    @Override
    public Map<String,Object> saveReceipt(Receipt receipt) {
        Map<String,Object> map = new HashMap<String,Object>();
        boolean code = false;
        //当前时间
        String nowTime = DateUtils.getTime();
        if(receipt.getId() == null){//新增保存
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            receipt.setCreateBy(user.getUserId());
            receipt.setCreateTime(nowTime);
            receipt.setUpdateTime(nowTime);
            receipt.setState("0");
            //在并发的情况下，防止插入相同的收货单编号，同时插入相同的收货单编号，后插入的收货单编号会比页面上显示的大1
            receipt.setReceiptCode(this.generateReceiptCode());
            if(receiptDAO.insert(receipt) > 0){//插入成功
                code = true;
            }

        }else{//修改保存
            if(receiptDAO.updateById(receipt) > 0){//更新成功
                code = true;
            }

        }
        map.put("code",code);
        //返回插入后的收货单的id
        map.put("receiptId",receipt.getId());
        map.put("receiptCode",receipt.getReceiptCode());
        return map;
    }

    @Override
    public boolean updateReceiptState(Long receiptId) {
        boolean result = false;
        Integer updateResult = receiptDAO.updateState(receiptId);
        if(updateResult > 0){
            result = true;
        }
        return result;
    }

    @Override
    public void updateReceiptAbnormalState(Long receiptId) {
        Receipt receipt = new Receipt();
        receipt.setId(receiptId);
        //将收货单更新为异常收货完成
        receipt.setState("4");
        receiptDAO.updateById(receipt);
    }
    /**
     * 获取导出列
     *
     * @return List<Customer>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<Receipt> getAllList(String ids) {
        System.out.println(ids);
        List<Receipt> list = null;
        list = receiptDAO.getAllLists(StringUtils.stringToList(ids));
        list.forEach(a->{

        });
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
    public void toExcel(HttpServletResponse response, String path, List<Receipt> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "收货计划表" + "(" + date + ")";
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
            mapFields.put("receiptCode", "收获计划单编号");
            mapFields.put("supplierName", "供应商");
            mapFields.put("customerName", "货主");
            mapFields.put("documentTypeContent", "单据类型");
            mapFields.put("estimatedDeliveryTime", "预计收货时间");
            mapFields.put("receiptStartTime", "收货开始时间");
            mapFields.put("createName", "收货人");
            mapFields.put("remark", "备注");
            mapFields.put("stateContent", "状态");
            //对收货计划字段进行转译
            List receiptList = new ArrayList();
            for(Receipt is:list){
                String documentType = is.getDocumentType();
                String state = is.getState();
                //单据类型（0：自采；1：客供）
                switch (documentType){
                    case "0":is.setDocumentTypeContent("自采");break;
                    case "1":is.setDocumentTypeContent("客供");break;
                    default:is.setDocumentTypeContent("");
                }
                //状态（0：未提交；1：待收货；2：收货中；3：已完成；4：异常收货完成）
                switch (state){
                    case "0":is.setStateContent("未提交");break;
                    case "1":is.setStateContent("待收货");break;
                    case "2":is.setStateContent("收货中");break;
                    case "3":is.setStateContent("已完成");break;
                    case "4":is.setStateContent("异常收货完成");break;
                    default:is.setStateContent("");
                }
                receiptList.add(is);
            }
            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    