package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.platform.util.DeriveExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库区管理service实现
 *
 * @author yuany
 * @date 2019-01-04
 */
@Service("depotAreaService")
public class DepotAreaServiceImpl extends ServiceImpl<DepotAreaDAO, DepotArea> implements DepotAreaService {

    // 库区DAO
    @Autowired
    private DepotAreaDAO depotAreaDao;

    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        Page<DepotArea> page = this.selectPage(
                new Query<DepotArea>(parms).getPage(),
                new EntityWrapper<DepotArea>()
        );
        return new PageUtils(page);
    }

    /**
     * 获取导出列
     *
     * @return List<Materiel>
     * @author DepotArea
     * @date 2019-01-04
     */
    @Override
    public List<DepotArea> getAllLists(String ids) {
        List<DepotArea> list = depotAreaDao.getAllLists(StringUtils.stringToList(ids));
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
     * @author yuany
     * @date 2019-01-04
     */
    @Override
    public void toExcel(HttpServletResponse response, String path, List<DepotArea> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "库区管理表" + "(" + date + ")";
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
            mapFields.put("areaCode", "库区编码");
            mapFields.put("areaName", "库区名称");
            mapFields.put("positionAmount", "库位数");
            mapFields.put("areaTypeName", "库区类别");
            mapFields.put("xsizeStart", "X轴起始坐标");
            mapFields.put("ysizeStart", "Y轴起始坐标");
            mapFields.put("xsizeEnd", "X轴终点坐标");
            mapFields.put("ysizeEnd", "Y轴终点坐标");
            mapFields.put("remark", "备注");
            mapFields.put("createTime", "创建时间");
            mapFields.put("updateTime", "更新时间");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
