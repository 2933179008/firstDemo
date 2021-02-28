package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.CustomerDAO;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.dao.ProducerDAO;
import com.tbl.modules.basedata.dao.SupplierDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.util.DeriveExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料管理service实现
 *
 * @author yuany
 * @date 2019-01-02
 */
@Service("materialService")
public class MaterielServiceImpl extends ServiceImpl<MaterielDAO, Materiel> implements MaterielService {

    //物料管理Dao
    @Autowired
    private MaterielDAO materielDao;

    //客户管理Dao
    @Autowired
    private SupplierDAO supplierDAO;

    //供应商管理Dao
    @Autowired
    private CustomerDAO customerDAO;

    //生产厂商管理Dao
    @Autowired
    private ProducerDAO producerDAO;

    /**
     * 物料管理分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-02
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        String materielCode = (String) parms.get("materielCode");
        //去掉首尾空格
        if (StringUtils.isNotBlank(materielCode)) {
            materielCode = materielCode.trim();
        }

        String materielName = (String) parms.get("materielName");
        if (StringUtils.isNotBlank(materielName)) {
            materielName = materielName.trim();
        }

        String customerBy = (String) parms.get("customerBy");
        String supplierBy = (String) parms.get("supplierBy");
        String producerBy = (String) parms.get("producerBy");

        Page<Materiel> maPage = this.selectPage(
                new Query<Materiel>(parms).getPage(),
                new EntityWrapper<Materiel>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotBlank(materielCode), "materiel_code", materielCode)
                        .like(StringUtils.isNotBlank(materielName), "materiel_name", materielName)
                        .eq(StringUtils.isNotBlank(customerBy), "customer_by", customerBy)
                        .eq(StringUtils.isNotBlank(supplierBy), "customer_by", supplierBy)
                        .eq(StringUtils.isNotBlank(producerBy), "customer_by", producerBy)
        );

        //遍历添加客户和供应商的名称
        for (Materiel materiel : maPage.getRecords()) {
            if (materiel.getCustomerBy() != null && customerDAO.selectById(materiel.getCustomerBy()) != null) {
                materiel.setCustomerName(customerDAO.selectById(materiel.getCustomerBy()).getCustomerName());
            }

            if (materiel.getSupplierBy() != null && supplierDAO.selectById(materiel.getSupplierBy()) != null) {
                materiel.setSupplierName(supplierDAO.selectById(materiel.getSupplierBy()).getSupplierName());
            }

            if (materiel.getProducerBy() != null && producerDAO.selectById(materiel.getProducerBy()) != null) {
                materiel.setProducerName(producerDAO.selectById(materiel.getProducerBy()).getProducerName());
            }
        }

        return new PageUtils(maPage);
    }

    /**
     * 删除物料实体（逻辑删除）
     *
     * @param ids:要删除的id集合
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2018-01-03
     */
    @Override
    public boolean delLstMateriel(String ids, Long userId) {
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Materiel> lstMateriel = this.selectBatchIds(lstIds);

        for (Materiel materiel : lstMateriel) {
            materiel.setDeletedFlag(DyylConstant.DELETED);
            materiel.setDeletedBy(userId);
        }
        return updateBatchById(lstMateriel);
    }

    /**
     * 获取导出列
     *
     * @return List<Materiel>
     * @author anss
     * @date 2018-09-13
     */
    @Override
    public List<Materiel> getAllLists(String ids) {
        List<Materiel> list = materielDao.getAllLists(StringUtils.stringToList(ids));
        list.forEach(a -> {

        });
        return list;
    }

    /**
     * 导出excel
     *
     * @param response
     * @param path
     * @param list
     * @author anss
     * @date 2018-09-13
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Materiel> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "物料管理表" + "(" + date + ")";
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
            mapFields.put("materielCode", "物料编码");
            mapFields.put("materielName", "物料名称");
            mapFields.put("barcode", "物料条码");
            mapFields.put("customerName", "货主");
            mapFields.put("supplierName", "供应商");
            mapFields.put("producerName", "生产厂家");
            mapFields.put("length", "长");
            mapFields.put("wide", "宽");
            mapFields.put("height", "高");
            mapFields.put("volume", "体积");
            mapFields.put("spec", "物料规格");
            mapFields.put("unit", "包装单位（一、二级单位）");
            mapFields.put("batchRule", "批次规则");
            mapFields.put("upshelfClassify", "上架分类");
            mapFields.put("pickClassify", "拣货分类");
            mapFields.put("qualityPeriod", "保质期(天)");
            mapFields.put("trayWeight", "托盘容量");
            mapFields.put("createTime", "数据创建时间");
            mapFields.put("updateTime", "数据更新时间");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id更新rfid
     *
     * @return
     * @author yuany
     * @date 2019-01-09
     */
    @Override
    public int updateMaterielRfid(String rfid, Long id) {
        int count = materielDao.updateMaterielRfid(rfid, id);
        return count;
    }

    /**
     * 获取物料编号
     *
     * @return
     * @author yuany
     * @date 2019-02-01
     */
    @Override
    public String getMaterielCode() {

        //物料编号
        String materielCode = null;

        //获取物料集合
        List<Materiel> materielList = this.selectList(
                new EntityWrapper<>()
        );

        //如果集合为长度为0则为第一条添加的数据
        if (materielList.size() == 0) {
            materielCode = "WL000001";
        } else {
            //获取集合中最后一条数据
            Materiel materiel = materielList.get(materielList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Double number = Double.valueOf(materiel.getMaterielCode().substring(2)) + 1;
            //拼接字符串
            materielCode = "WL000000" + number.toString();
        }
        return materielCode;
    }

}
