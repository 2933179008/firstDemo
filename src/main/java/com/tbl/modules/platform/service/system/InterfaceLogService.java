package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.platform.entity.system.InterfaceLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 接口日志service
 *
 * @author anss
 * @date 2018-09-16
 */
public interface InterfaceLogService extends IService<InterfaceLog> {

    /**
     * 获取接口日志分页列表
     * @author anss
     * @date 2018-09-16
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存调用接口日志，传入的时间为开始调用接口的时间，结束时间为调用此方法的系统时间
     * @param interfaceName 接口名称
     * @param parameter 请求参数
     * @param msg 报错内容
     * @param errorInfo 报错时间
     * @return boolean
     * */
    boolean interfaceLogInsert(String interfaceName, String parameter, String msg, String errorInfo);

}
