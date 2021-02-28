package com.tbl.modules.instorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.dao.QualityBillDAO;
import com.tbl.modules.instorage.dao.QualityBillDetailDAO;
import com.tbl.modules.instorage.entity.QualityBill;
import com.tbl.modules.instorage.entity.QualityBillDetail;
import com.tbl.modules.instorage.service.QualityBillDetailService;
import com.tbl.modules.instorage.service.QualityBillService;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.tbl.modules.platform.util.DeriveExcel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 质检单service实现类
 * @author: zj
 * @create: 2019-02-11 16:11
 **/
@Service("qualityBillService")
public class QualityBillServiceImpl extends ServiceImpl<QualityBillDAO, QualityBill> implements QualityBillService {
    @Autowired
    private QualityBillDAO qualityBillDAO;
    @Autowired
    private QualityBillDetailDAO qualityBillDetailDAO;

    @Override
    public Page<QualityBill> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<QualityBill> pageQualityBill = new Page<QualityBill>(pg,rows,sortname,order);
        //获取质检单列表
        List<QualityBill> list = qualityBillDAO.getPageQualityBillList(pageQualityBill, params);

        return pageQualityBill.setRecords(list);
    }

    @Override
    public String generateQualityCode() {
        //质检单编号
        String qualityCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.QUALITY_CODE_FORMAT);
        //获取最大质检单编号
        String maxQualityCode = qualityBillDAO.getMaxQualityCode();
        if(StringUtils.isEmptyString(maxQualityCode)){
            qualityCode = "ZJ00000001";
        }else{
            Integer maxQualityCode_count = Integer.parseInt(maxQualityCode.replace("ZJ",""));
            qualityCode = df.format(maxQualityCode_count+1);
        }
        return qualityCode;
    }

    @Override
    public Map<String, Object> saveQualityBill(QualityBill qualityBill) {
        Map<String,Object> map = new HashMap<String,Object>();
        boolean code = true;
        String msg = "";
        /**校验质检单对应的入库单是否已质检通过**/
        //入库单id
        Long instorageBillId = qualityBill.getInstorageBillId();
        EntityWrapper<QualityBill> qualityBillEntity = new EntityWrapper<QualityBill>();
        qualityBillEntity.eq("instorage_bill_id",instorageBillId);
        qualityBillEntity.eq("state","1");
        Integer count = qualityBillDAO.selectCount(qualityBillEntity);
        if(count > 0){//如果质检单对应的入库单是“质检通过”的单据数量大于0
            code = false;
            msg = "该入库单已质检通过，不可重复质检";
            map.put("code",code);
            map.put("msg",msg);
            return map;
        }

        //当前时间
        String nowTime = DateUtils.getTime();
        if(qualityBill.getId() == null){//新增保存
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            User user = (User) session.getAttribute(Const.SESSION_USER);
            qualityBill.setCreateBy(user.getUserId());
            qualityBill.setCreateTime(nowTime);
            qualityBill.setUpdateTime(nowTime);
            qualityBill.setState("0");
            //在并发的情况下，防止插入相同的质检单编号，同时插入相同的质检单编号，后插入的质检单编号会比页面上显示的大1
            qualityBill.setQualityCode(this.generateQualityCode());
            if(qualityBillDAO.insert(qualityBill) > 0){//插入成功
                code = true;
                msg = "保存成功";
            }else{
                code = false;
                msg = "保存失败";
            }
            map.put("code",code);
            map.put("msg",msg);
            //返回插入后的质检单的id
            map.put("qualityBillId",qualityBill.getId());
            map.put("qualityCode",qualityBill.getQualityCode());
            return map;
        }else{//修改保存
            if(qualityBillDAO.updateById(qualityBill) > 0){//更新成功
                code = true;
                msg = "修改成功";
            }else{
                code = false;
                msg = "修改失败";
            }
            map.put("code",code);
            map.put("msg",msg);
            //返回插入后的质检单的id
            map.put("qualityBillId",qualityBill.getId());
            map.put("qualityCode",qualityBill.getQualityCode());
            return map;
        }

    }


    @Override
    public List<Map<String, Object>> getSelectInstorageList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return qualityBillDAO.getSelectInstorageList(page,queryString);
    }

    @Override
    public Integer getSelectInstorageTotal(String queryString) {
        return qualityBillDAO.getSelectInstorageTotal(queryString);
    }

    @Override
    public List<Map<String, Object>> getSelectMaterialList(Long instorageBillId, String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return qualityBillDAO.getSelectMaterialList(page,instorageBillId,queryString);
    }

    @Override
    public Integer getSelectMaterialTotal(Long instorageBillId, String queryString) {
        return qualityBillDAO.getSelectMaterialTotal(instorageBillId,queryString);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitQualityBill(String qualityId) {

        /**1.更新质检单状态**/

        EntityWrapper<QualityBillDetail> detailEntity = new EntityWrapper<QualityBillDetail>();
        detailEntity.eq("quality_id",qualityId);
        detailEntity.eq("state","1");
        //获取质检状态不合格的物料种类数
        Integer unqualifiedCount = qualityBillDetailDAO.selectCount(detailEntity);
        QualityBill qualityBill = qualityBillDAO.selectById(qualityId);
        if(unqualifiedCount > 0){
            //如果有不合格的物料，则将质检单状态更新为2（质检退回）
            qualityBill.setState("2");
        }else{
            //如果有不合格的物料，则将质检单状态更新为1（质检通过）
            qualityBill.setState("1");
        }
        qualityBillDAO.updateById(qualityBill);

        /**2.更新可用库存**/
        //注：如果质检通过，此处只会更新该质检单对应的入库单中已入库（即已上架）的物料的可用库存
        //质检单状态
        String qualityBillState = qualityBill.getState();
        if("1".equals(qualityBillState)){//如果质检通过
            //入库单id
            Long instorageBillId = qualityBill.getInstorageBillId();
            //对于已上架的物料的可用库存，需要查询该入库单对应的所有上架单中的上架单详情的的物料状态为1（已上架）的物料数量和重量
            List<Map<String,Object>> lstPutBillDetail = qualityBillDAO.selectPutDetailByInstorageBillId(instorageBillId);
            if(lstPutBillDetail!=null && lstPutBillDetail.size()>0){
                //更新库存表中的可用库存数量、可用库存重量、可用托盘库存数量、可用RFID
                qualityBillDAO.updateStock(lstPutBillDetail);
            }
        }

    }

    @Override
    public Map<String,Object> getInstorageProcess(String qualityId) {
        return qualityBillDAO.getInstorageProcess(qualityId);
    }

    @Override
    public List<Map<String, Object>> getPutBillDetail(String qualityId) {
        return qualityBillDAO.getPutBillDetail(qualityId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteQualityBill(Long id) {
        /**1.删除质检单**/
        qualityBillDAO.deleteById(id);
        /**2.删除质检单详情**/
        EntityWrapper<QualityBillDetail>  qualityBillEntity = new EntityWrapper<>();
        qualityBillEntity.eq("quality_id",id);
        qualityBillDetailDAO.delete(qualityBillEntity);
    }

    public List<Map<String, Object>> getTimeOutList(){
        return qualityBillDAO.getTimeOutList();
    };

    /**
     * 获取导出列
     *
     * @return List<Customer>
     * @author pz
     * @date 2019-01-08
     */
    @Override
    public List<QualityBill> getAllList(String ids) {
        System.out.println(ids);
        List<QualityBill> list = null;
        if(ids.equals("")||ids==null) {
            EntityWrapper<QualityBill> wraQualityBill = new EntityWrapper<>();
            list = this.selectList(wraQualityBill);
        }else{
            list = qualityBillDAO.getAllLists(StringUtils.stringToList(ids));
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
    public void toExcel(HttpServletResponse response, String path, List<QualityBill> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "质检收货表" + "(" + date + ")";
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
            mapFields.put("qualityCode", "质检单编号");
            mapFields.put("instorageBillCode", "入库单编号");
            mapFields.put("qualityTime", "质检时间");
            mapFields.put("remark", "备注");
            mapFields.put("stateContent", "状态");
            //对上架单字段进行转译
            List qualityBillList = new ArrayList();
            for(QualityBill is:list){
                String state = is.getState();
                //状态（0：未提交；1：质检通过；2：质检退回；）
                switch (state){
                    case "0":is.setStateContent("未提交");break;
                    case "1":is.setStateContent("质检通过");break;
                    case "2":is.setStateContent("质检退回");break;
                    default:is.setStateContent("");
                }
                qualityBillList.add(is);
            }
            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    