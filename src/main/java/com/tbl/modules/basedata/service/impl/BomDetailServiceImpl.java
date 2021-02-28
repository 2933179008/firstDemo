package com.tbl.modules.basedata.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.BomDetailDAO;
import com.tbl.modules.basedata.entity.BomDetail;
import com.tbl.modules.basedata.service.BomDetailService;
import com.tbl.modules.constant.DyylConstant;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: dyyl
 * @description: bom详情service实现类
 * @author: zj
 * @create: 2019-01-04 10:43
 **/
@Service("bomDetailService")
public class BomDetailServiceImpl extends ServiceImpl<BomDetailDAO, BomDetail> implements BomDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //BOM id主键
        String bomId = (String) params.get("bomId");
        //物料编码
        String materialCode = (String) params.get("materialCode");
        //物料名称
        String materialName = (String) params.get("materialName");

        Page<BomDetail> page = this.selectPage(
                new Query<BomDetail>(params).getPage(),
                new EntityWrapper<BomDetail>()
                        .eq("bom_id",bomId)
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotEmpty(materialCode), "material_code", materialCode)
                        .like(StringUtils.isNotEmpty(materialName), "material_name", materialName)

        );
        return new PageUtils(page);
    }
}
    