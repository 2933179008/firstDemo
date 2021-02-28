package com.tbl.modules.erpInterface.service;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: dyyl
 * @description: 调用接口dao
 * @author: pz
 * @create: 2019-02-15
 **/
public interface MaterielErpInterfaceService {

    String materielInfo(@RequestBody String materielArr);

}
    