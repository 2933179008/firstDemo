package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.ErpDepotAreaDAO;
import com.tbl.modules.basedata.dao.ErpDepotPositionDAO;
import com.tbl.modules.basedata.entity.ErpDepotPosition;
import com.tbl.modules.basedata.service.ErpDepotPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * erp库位管理service实现
 *
 * @author pz
 * @date 2019-04-29
 */
@Service("erpDepotPositionService")
public class ErpDepotPositionServiceImpl extends ServiceImpl<ErpDepotPositionDAO, ErpDepotPosition> implements ErpDepotPositionService {

    // erp库区DAO
    @Autowired
    private ErpDepotAreaDAO erpDepotAreaDAO;

    // erp库位DAO
    @Autowired
    private ErpDepotPositionDAO erpDepotPositionDAO;


    /**
     * 库位分页
     *
     * @param parms
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {
        //获取库位编码
        String positionCode = (String) parms.get("positionCode");
        if (StringUtils.isNotBlank(positionCode)) {
            positionCode = positionCode.trim();
        }

        Page<ErpDepotPosition> dePage = this.selectPage(
                new Query<ErpDepotPosition>(parms).getPage(),
                new EntityWrapper<ErpDepotPosition>()
                        .like("position_code", positionCode)
        );

        //遍历添加库区名称
        for (ErpDepotPosition erpDepotPosition : dePage.getRecords()) {
            if (erpDepotPosition.getDepotareaId() != null && erpDepotAreaDAO.selectById(erpDepotPosition.getDepotareaId()) != null) {
                erpDepotPosition.setAreaName(erpDepotAreaDAO.selectById(erpDepotPosition.getDepotareaId()).getAreaName());
            }
        }

        return new PageUtils(dePage);
    }

    /**
     * 获取库位编号
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @Override
    public String getPositionCode() {

        //库位编号
        String positionCode = null;

        //获取库位集合
        List<ErpDepotPosition> erpDepotPositionList = this.selectList(
                new EntityWrapper<>()
        );

        //如果集合为长度为0则为第一条添加的数据
        if (erpDepotPositionList.size() == 0) {
            positionCode = "KW000001";
        } else {
            //获取集合中最后一条数据
            ErpDepotPosition depotPosition = erpDepotPositionList.get(erpDepotPositionList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Long number = Long.parseLong(depotPosition.getPositionCode().substring(2)) + 1;
            //拼接字符串
            positionCode = "KW00000" + number.toString();
        }
        return positionCode;
    }
}
