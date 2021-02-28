package com.tbl.modules.PADInterface.dao;

import org.apache.ibatis.annotations.Param;

public interface PADInterfaceDAO {
    /**
    * @Description:  获取叉车操作类型
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/3/12
    */
    String getOperateType(@Param("ip") String ip);
}
