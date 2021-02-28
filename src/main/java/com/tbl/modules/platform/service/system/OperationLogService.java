package com.tbl.modules.platform.service.system;

import com.baomidou.mybatisplus.service.IService;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.platform.entity.system.OperationLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 操作日志service
 *
 * @author yuany
 * @date 2019-01-02
 */
public interface OperationLogService extends IService<OperationLog> {

     /**
      * 获取操作日志分页列表
      * @author yuany
      * @date 2019-01-02
      * @param params
      * @return
      */
     PageUtils queryPage(Map<String, Object> params);

     boolean logInsert(String operation, long action, HttpServletRequest request);

}
