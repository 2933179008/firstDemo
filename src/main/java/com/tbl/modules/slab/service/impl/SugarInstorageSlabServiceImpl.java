package com.tbl.modules.slab.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.tbl.modules.instorage.entity.PutBill;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.dao.SugarInstorageSlabDAO;
import com.tbl.modules.slab.service.SugarInstorageSlabService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: dyyl
 * @description: 平板白糖入库service实现类
 * @author: zj
 * @create: 2019-03-05 13:25
 **/
@Service("sugarInstorageSlabService")
public class SugarInstorageSlabServiceImpl implements SugarInstorageSlabService {

    @Autowired
    private SugarInstorageSlabDAO sugarInstorageSlabDAO;

    @Override
    public Page<PutBill> queryPage(Map<String, Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));

        Page<PutBill> pagePutBill = new Page<PutBill>(pg,rows,sortname,order);

        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        params.put("userId",userId);
        //获取白糖类型上架单列表
        List<PutBill> list = sugarInstorageSlabDAO.getPagePutBillList(pagePutBill, params);

        return pagePutBill.setRecords(list);
    }
}
    