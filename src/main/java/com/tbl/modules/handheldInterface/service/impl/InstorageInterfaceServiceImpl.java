package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.handheldInterface.dao.InstorageInterfaceDAO;
import com.tbl.modules.handheldInterface.service.InstorageInterfaceService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.entity.Instorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * 入库接口实现类
 *
 * @author yuany
 * @date 2019-02-18
 */
@Service(value = "instorageInterfaceService")
public class InstorageInterfaceServiceImpl extends ServiceImpl<InstorageInterfaceDAO, Instorage> implements InstorageInterfaceService {

    @Autowired
    private InstorageInterfaceDAO instorageInterfaceDAO;

    @Override
    public String generateInstorageCode() {
        //入库单编号
        String instorageCode = "";
        DecimalFormat df = new DecimalFormat(DyylConstant.INSTORAGE_CODE_FORMAT);
        //获取最大入库单编号
        String maxInstorageCode = instorageInterfaceDAO.getMaxInstorageCode();
        if(StringUtils.isEmptyString(maxInstorageCode)){
            instorageCode = "IN00000001";
        }else{
            Integer maxInstorageCode_count = Integer.parseInt(maxInstorageCode.replace("IN",""));
            instorageCode = df.format(maxInstorageCode_count+1);
        }
        return instorageCode;
    }
}
