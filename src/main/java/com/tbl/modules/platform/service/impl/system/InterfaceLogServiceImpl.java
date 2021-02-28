package com.tbl.modules.platform.service.impl.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.LogUtil;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.platform.dao.system.InterfaceLogDAO;
import com.tbl.modules.platform.entity.system.InterfaceLog;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 接口日志service实现
 *
 * @author anss
 * @date 2018-09-16
 */
@Service("interfaceLogService")
public class InterfaceLogServiceImpl extends ServiceImpl<InterfaceLogDAO, InterfaceLog> implements InterfaceLogService {

    /**
     * 获取接口日志分页列表
     * @author anss
     * @date 2018-09-16
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String operationTime = (String)params.get("requesttime");

        Page<InterfaceLog> page = this.selectPage(
                new Query<InterfaceLog>(params).getPage(),
                new EntityWrapper<InterfaceLog>()
                        .like(StringUtils.isNotBlank(operationTime),"requesttime", operationTime)
        );

        return new PageUtils(page);
    }

    /**
     * 保存调用接口日志，传入的时间为开始调用接口的时间，结束时间为调用此方法的系统时间
     * @param interfaceName 接口名称
     * @param parameter 请求参数
     * @param result 请求结果
     * @param errorInfo 报错内容
     * @return boolean
     * */
    @Override
    public boolean interfaceLogInsert(String interfaceName, String parameter, String result, String errorInfo) {
        String requesttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setInterfacename(interfaceName);
        interfaceLog.setRequesttime(requesttime);
        interfaceLog.setParameter(parameter);
        interfaceLog.setResult(result);
        interfaceLog.setErrorinfo(errorInfo);
        return this.insert(interfaceLog);
    }

}
