package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.BomDAO;
import com.tbl.modules.basedata.entity.Bom;
import com.tbl.modules.basedata.service.BomService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.stock.dao.StockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @program: dyyl
 * @description: BOM service实现类
 * @author: zj
 * @create: 2019-01-04 10:34
 **/
@Service("bomService")
public class BomServiceImpl extends ServiceImpl<BomDAO, Bom> implements BomService {

    //库存DAO
    @Autowired
    private BomDAO bomDAO;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //bom编码
        String bomCode = (String) params.get("bomCode");
        //bom名称
        String bomName = (String) params.get("bomName");

        Page<Bom> page = this.selectPage(
                new Query<Bom>(params).getPage(),
                new EntityWrapper<Bom>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotEmpty(bomCode), "bom_code", bomCode)
                        .like(StringUtils.isNotEmpty(bomName), "bom_name", bomName)

        );
        return new PageUtils(page);
    }

    /**
     * @Description: 生成盘点任务编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    @Override
    public String generatBomCode() {
        //bom编号
        String bomCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.bom_CODE_FORMAT);
        //获取最大bom编号
        String maxBomCode = bomDAO.getMaxBomCode();
        if (StringUtils.isEmptyString(maxBomCode)) {
            bomCode = "BM00000001";
        } else {
            Integer maxBomCode_count = Integer.parseInt(maxBomCode.replace("BM", ""));
            bomCode = df.format(maxBomCode_count + 1);
        }
        return bomCode;
    }
}
    