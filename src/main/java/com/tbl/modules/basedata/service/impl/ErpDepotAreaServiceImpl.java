package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.basedata.dao.ErpDepotAreaDAO;
import com.tbl.modules.basedata.entity.ErpDepotArea;
import com.tbl.modules.basedata.service.ErpDepotAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * erp库区管理service实现
 *
 * @author pz
 * @date 2019-04-29
 */
@Service("erpDepotAreaService")
public class ErpDepotAreaServiceImpl extends ServiceImpl<ErpDepotAreaDAO, ErpDepotArea> implements ErpDepotAreaService {

    // 库区DAO
    @Autowired
    private ErpDepotAreaDAO erpDepotAreaDao;

    @Override
    public PageUtils queryPage(Map<String, Object> parms) {
        //获取库区编码
        String areaCode = (String) parms.get("areaCode");
        //获取库区名称
        String areaName = (String) parms.get("areaName");

        Page<ErpDepotArea> page = this.selectPage(
                new Query<ErpDepotArea>(parms).getPage(),
                new EntityWrapper<ErpDepotArea>().like("area_code", areaCode)
                        .like("area_name", areaName)
        );
        for (ErpDepotArea erpDepotArea : page.getRecords()) {
            String FTypeID = erpDepotArea.getFtypeId();
            if(FTypeID.equals("503")){
                erpDepotArea.setTypeName("虚仓");
            }else{
                erpDepotArea.setTypeName("实仓");
            }
        }
        return new PageUtils(page);
    }

    /**
     * 获取库区编号
     *
     * @return
     * @author pz
     * @date 2019-04-29
     */
    @Override
    public String getAreaCode() {

        //物料编号
        String areaCode = null;

        //获取物料集合
        List<ErpDepotArea> erpDepotAreaList = this.selectList(
                new EntityWrapper<>()
        );

        //如果集合为长度为0则为第一条添加的数据
        if (erpDepotAreaList.size() == 0) {
            areaCode = "KQ000001";
        } else {
            //获取集合中最后一条数据
            ErpDepotArea erpDepotArea = erpDepotAreaList.get(erpDepotAreaList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Long number = Long.parseLong(erpDepotArea.getAreaCode().substring(2)) + 1;
            //拼接字符串
            areaCode = "KQ00000" + number.toString();
        }
        return areaCode;
    }
}
