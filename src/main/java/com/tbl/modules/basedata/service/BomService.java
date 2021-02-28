package com.tbl.modules.basedata.service;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.basedata.entity.Bom;

import java.util.Map;

public interface BomService extends IService<Bom> {

    /**
    * @Description:  获取bom列表数据
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/4
    */
    PageUtils queryPage(Map<String, Object> map);

    /**
     * @Description:  生成bom编号
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/2/25
     */
    String generatBomCode();
}
