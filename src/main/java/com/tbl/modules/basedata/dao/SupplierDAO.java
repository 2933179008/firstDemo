package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pz
 * @date 2019-01-02
 */
public interface SupplierDAO extends BaseMapper<Supplier> {

    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Supplier>
     * */
    List<Supplier> getAllLists(List<Long> ids);

    /**
     * 保存客户
     * @param
     * @return
     * @author pz
     * @date 2019-02-20
     */
    Boolean savaSupplier(@Param("id") Long id, @Param("supplierCode") String supplierCode, @Param("supplierName") String supplierName,
                         @Param("supplierType") String supplierType, @Param("linkman") String linkman, @Param("telephone") String telephone,
                         @Param("address") String address, @Param("mail") String mail, @Param("remark") String remark, @Param("createTime") String createTime);

}
