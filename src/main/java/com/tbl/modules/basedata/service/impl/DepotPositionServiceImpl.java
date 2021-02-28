package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotPositionService;
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
 * 库位管理service实现
 *
 * @author yuany
 * @date 2019-01-04
 */
@Service("depotPositionService")
public class DepotPositionServiceImpl extends ServiceImpl<DepotPositionDAO, DepotPosition> implements DepotPositionService {

    // 库区DAO
    @Autowired
    private DepotAreaDAO depotAreaDao;

    // 库位DAO
    @Autowired
    private DepotPositionDAO depotPositionDao;


    /**
     * 库位分页
     *
     * @param parms
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        String positionCode = (String) parms.get("positionCode");
        if (StringUtils.isNotBlank(positionCode)) {
            positionCode = positionCode.trim();
        }
        String positionType = (String) parms.get("positionType");

        Page<DepotPosition> dePage = this.selectPage(
                new Query<DepotPosition>(parms).getPage(),
                new EntityWrapper<DepotPosition>()
                        .in("position_type", positionType)
                        .like("position_code", positionCode)
        );

        //遍历添加库区名称
        for (DepotPosition depotPosition : dePage.getRecords()) {
            if (depotPosition.getDepotareaId() != null && depotAreaDao.selectById(depotPosition.getDepotareaId()) != null) {
                depotPosition.setAreaName(depotAreaDao.selectById(depotPosition.getDepotareaId()).getAreaName());
            }
        }

        return new PageUtils(dePage);
    }

    /**
     * 获取导出列
     *
     * @return List<DepotPosition>
     * @author yuany
     * @date 2019-01-04
     */
    @Override
    public List<DepotPosition> getAllLists(String ids) {
        List<DepotPosition> list = depotPositionDao.getAllLists(StringUtils.stringToList(ids));
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
    public void toExcel(HttpServletResponse response, String path, List<DepotPosition> list) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String date = sd.format(new Date());
            String sheetName = "库位管理表" + "(" + date + ")";
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
            mapFields.put("positionCode", "库位编码");
            mapFields.put("positionName", "库位名称");
            mapFields.put("areaName", "所属库区");
            mapFields.put("row", "排");
            mapFields.put("column", "列");
            mapFields.put("layer", "层");
            mapFields.put("length", "长（m）");
            mapFields.put("width", "宽（m）");
            mapFields.put("startX", "起始x轴（左下）");
            mapFields.put("startY", "起始y轴（左下）");
            mapFields.put("endX", "终点x轴（右上");
            mapFields.put("endY", "终点y轴（右上）");
            mapFields.put("positionTypeName", "库位类型");
            mapFields.put("blendTypeName", "混放类型");
            mapFields.put("classify", "ABC分类");
            mapFields.put("capacityRfidAmount", "托盘容量");
            mapFields.put("capacityWeight", "重量容量");
            mapFields.put("remark", "备注");
            mapFields.put("createTime", "创建时间");
            mapFields.put("updateTime", "更新时间");

            DeriveExcel.exportExcel(sheetName, list, mapFields, response, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
