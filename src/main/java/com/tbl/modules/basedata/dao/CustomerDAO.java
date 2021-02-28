package com.tbl.modules.basedata.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.basedata.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pz
 * @date 2019-01-02
 */
public interface CustomerDAO extends BaseMapper<Customer> {

    /**
     * 获取导出列
     * @author pz
     * @date 2019-01-08
     * @return List<Customer>
     * */
    List<Customer> getAllLists(List<Long> ids);

    /**
     * 保存客户
     * @param
     * @return
     * @author pz
     * @date 2019-02-20
     */
    Boolean savaCustomer(@Param("id") Long id, @Param("customerCode") String customerCode, @Param("customerName") String customerName,
                         @Param("customerType") String customerType, @Param("linkman") String linkman, @Param("telephone") String telephone,
                         @Param("address") String address, @Param("mail") String mail, @Param("remark") String remark, @Param("createTime") String createTime);


}
