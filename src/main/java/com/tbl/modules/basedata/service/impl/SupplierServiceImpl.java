package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.SupplierDAO;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.basedata.service.SupplierService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.util.DeriveExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 供应商service实现
 *
 * @author pz
 * @date 2018-01-02
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierDAO, Supplier> implements SupplierService {

    // 供应商DAO
    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    public PageUtils queryPageS(Map<String, Object> params) {
        //供应商编码
        String supplierCode = (String) params.get("supplierCode");
        //供应商名称
        String supplierName = (String) params.get("supplierName");
        //联系人
        String linkman = (String) params.get("linkman");

        Page<Supplier> page = this.selectPage(
                new Query<Supplier>(params).getPage(),
                new EntityWrapper<Supplier>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotBlank(supplierCode),"supplier_code", supplierCode)
                        .like(StringUtils.isNotBlank(supplierName),"supplier_name", supplierName)
                        .like(StringUtils.isNotBlank(linkman),"linkman", linkman)
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
    public boolean delLstSupplier(String ids, Long userId) {
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Supplier> lstSupplier = this.selectBatchIds(lstIds);

        for (Supplier supplier: lstSupplier) {
            supplier.setDeletedFlag(DyylConstant.DELETED);
            supplier.setDeletedBy(userId);
        }
        return updateBatchById(lstSupplier);
    }

    /**
     * 获取导出列
     * @return List<Supplier>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<Supplier> getAllListS(String ids) {
        System.out.println(ids);
        List<Supplier> list = null;
        if(ids.equals("")||ids==null) {
            EntityWrapper<Supplier> wraSupplier = new EntityWrapper<>();
            wraSupplier.eq("deleted_flag", DyylConstant.NOTDELETED);
            list = this.selectList(wraSupplier);
        }else{
            list = supplierDAO.getAllLists(StringUtils.stringToList(ids));
            list.forEach(a->{

            });
        }
        return list;
    }

    /**
     * 导出excel
     * @param response
     * @param path
     * @param list
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Supplier> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "供应商管理表" + "(" + date + ")";
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
            mapFields.put("supplierCode", "供应商编码");
            mapFields.put("supplierName", "供应商名称");
            mapFields.put("supplierType", "供应商类别");
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
