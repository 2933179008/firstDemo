package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.ProducerDAO;
import com.tbl.modules.basedata.entity.Producer;
import com.tbl.modules.basedata.service.ProducerService;
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
@Service("producerService")
public class ProducerServiceImpl extends ServiceImpl<ProducerDAO, Producer> implements ProducerService {

    // 供应商DAO
    @Autowired
    private ProducerDAO producerDAO;

    @Override
    public PageUtils queryPageS(Map<String, Object> params) {

        //产商编码
        String producerCode = (String) params.get("producerCode");

        //产商名称
        String producerName = (String) params.get("producerName");

        Page<Producer> page = this.selectPage(
                new Query<Producer>(params).getPage(),
                new EntityWrapper<Producer>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotBlank(producerCode),"producer_code", producerCode)
                        .like(StringUtils.isNotBlank(producerName),"producer_name", producerName)
        );
        return new PageUtils(page);
    }

    /**
     * 删除产商实体（逻辑删除）
     * @author pz
     * @date 2018-01-11
     * @param ids:要删除的id集合
     * @param userId：当前登陆人Id
     * @return
     */
    @Override
    public boolean delLstProducer(String ids, Long userId) {
        //用lambda表达式把ids转为list<Long>
        List<Long> lstIds = Arrays.stream(ids.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Producer> lstProducer = this.selectBatchIds(lstIds);

        for (Producer producer: lstProducer) {
            producer.setDeletedFlag(DyylConstant.DELETED);
            producer.setDeletedBy(userId);
        }
        return updateBatchById(lstProducer);
    }

    /**
     * 获取导出列
     *
     * @return List<Producer>
     * @author pz
     * @date 2019-01-11
     */
    @Override
    public List<Producer> getAllListP(String ids) {
        System.out.println(ids);
        List<Producer> list = null;
        if(ids.equals("")||ids==null) {
            EntityWrapper<Producer> wraProducer = new EntityWrapper<>();
            wraProducer.eq("deleted_flag", DyylConstant.NOTDELETED);
            list = this.selectList(wraProducer);
        }else{
            list = producerDAO.getAllListP(StringUtils.stringToList(ids));
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
     * @date 2019-01-11
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<Producer> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "产商管理表" + "(" + date + ")";
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
            mapFields.put("producerCode", "产商编码");
            mapFields.put("producerName", "产商名称");
            mapFields.put("producerType", "产商类别");
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
