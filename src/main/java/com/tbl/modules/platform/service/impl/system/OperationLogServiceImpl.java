package com.tbl.modules.platform.service.impl.system;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.*;
import com.tbl.modules.platform.dao.system.OperationLogDAO;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.OperationLog;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.OperationLogService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志service实现
 *
 * @author yuany
 * @date 2019-01-02
 */
@Service("operationLogService")
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDAO, OperationLog> implements OperationLogService {

    // 用户DAO
    @Autowired
    private UserDAO userDAO;

    /**
     * 获取操作日志分页列表
     * @author yy
     * @date 2018-12-12
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //操作时间
        String operationTime = (String)params.get("operationTime");
        Long minOperationTime = null;
        Long maxOperationTime = null;
        if (StringUtils.isNotBlank(operationTime)) {
            minOperationTime = DateUtils.stringToLong(operationTime + "00:00:00", "yyyy-MM-dd HH:mm:ss");
            maxOperationTime = DateUtils.stringToLong(operationTime + "23:59:59", "yyyy-MM-dd HH:mm:ss");
        }

        Page<OperationLog> page=this.selectPage(
                new Query<OperationLog>(params).getPage(),
                new EntityWrapper<OperationLog>()
                        .ge(minOperationTime != null, "operation_time", minOperationTime)
                        .le(maxOperationTime != null, "operation_time", maxOperationTime)

        );

        for (OperationLog operationLog: page.getRecords()) {
            //根据Userid 获取UserName
            operationLog.setUsername(userDAO.selectById(operationLog.getUserId()).getUsername());
            //把时间戳转换为时间格式
            operationLog.setOperationT(DateUtils.stampToDate(operationLog.getOperationTime()));
        }
        return new PageUtils(page);
    }

    public boolean logInsert(String operation, long action, HttpServletRequest request){
        if(request==null){
            request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        User user = (User) request.getSession().getAttribute(Const.SESSION_USER);
        boolean isSucceed = false;
        if(user != null){
            OperationLog _log = new OperationLog();
            _log.setUserId(user.getUserId());
            _log.setUseraction(action);
            _log.setOperation(operation);
            _log.setUserip(request.getRemoteAddr());
            _log.setUsermac(LogUtil.getRealMacInfo( request.getRemoteAddr()));
            _log.setOperationTime(new Date().getTime());
            isSucceed = this.insert(_log);

        }
        return isSucceed;
    }

    public Long logInsertGrenId(String operation,long action,HttpServletRequest request,Object json){
        if(request==null){
            request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        if(json==null){
            json=new JSONObject();
        }
        User user = (User) request.getSession().getAttribute(Const.SESSION_USER);
        //boolean isSucceed = false;
        Long id=0L;
        if(user != null){
            OperationLog _log = new OperationLog();
            _log.setUserId(user.getUserId());
            _log.setUseraction(action);
            _log.setOperation(operation);
            _log.setUserip(request.getRemoteAddr());
            _log.setUsermac(LogUtil.getRealMacInfo( request.getRemoteAddr()));
            _log.setOperationTime(DateUtils.dateToSs());
            id=saveLogGrentId(_log,json.toString());
        }
        return id;
    }


    /**
     * 保存日志
     * @param log
     * @return Long
     */
    public Long saveLogGrentId(OperationLog log, String objectJsonStr) {
        log.setJsonstr(objectJsonStr);
        this.insert(log);
        return log.getId() ;
    }
}
