package com.tbl.modules.outstorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.outstorage.dao.LowerShelfDAO;
import com.tbl.modules.outstorage.dao.LowerShelfDetailDAO;
import com.tbl.modules.outstorage.dao.OutStorageDAO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.outstorage.entity.OutStorageManagerVO;
import com.tbl.modules.outstorage.service.LowerShelfService;
import com.tbl.modules.outstorage.service.OutStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcg
 * data 2019/2/14
 */
@Service("lowerShelfService")
public class LowerShelfServiceImpl extends ServiceImpl<LowerShelfDAO,LowerShelfBillVO> implements LowerShelfService {

    @Autowired
    private LowerShelfDAO lowerShelfDAO;

    @Autowired
    private LowerShelfDetailDAO lowerShelfDetailDAO;

    @Autowired
    private OutStorageService outStorageService;

    /**
     * 通过出库单的ID获取对应的下架单的数量
     * @param id
     * @return
     */
    @Override
    public Integer getDtailCount(String id) {
        return lowerShelfDAO.getDtailCount(id);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<LowerShelfBillVO> page = this.selectPage(
                new Query<LowerShelfBillVO>(params).getPage(),
                new EntityWrapper<LowerShelfBillVO>()
        );
        return new PageUtils(page.setRecords(lowerShelfDAO.selectLowerShelfList(page, params)));
    }

    /**
     * 通过下架单的ID获取对应的下架单的数值
     * @param id
     * @return
     */
    @Override
    public LowerShelfBillVO getLowerShelfVO(String id) {
        return lowerShelfDAO.getLowerShelfVO(id);
    }

    /**
     * 查询系统中的操作人员
     * @return
     */
    @Override
    public List<Map<String, Object>> getUserList() {
        List<Map<String,Object>> userList = lowerShelfDAO.getUserList();
        return userList;
    }

    /**
     * 更新操作人
     * @param lowerId
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> updateOperation(String lowerId, String userId) {
        Map<String,Object> map = Maps.newHashMap();
        LowerShelfBillVO lowerShelfBillVO = this.selectById(lowerId);
        lowerShelfBillVO.setUserId(userId);
        Boolean result = this.updateById(lowerShelfBillVO);
        map.put("result",result);
        return map;
    }

    /**
     * 更新状态
     * @param id
     * @param state
     * @return
     */
    @Override
    public Boolean updateLoserState(String id, String state) {
        LowerShelfBillVO lowerShelfBillVO = this.selectById(id);
        lowerShelfBillVO.setState(state);
        Boolean result = this.updateById(lowerShelfBillVO);
        return result;
    }

    /**
     * 通过下架单的ID删除对应的下架的信息
     * @param lowerId
     * @return
     */
    @Override
    public Map<String, Object> delLower(String lowerId) {
        Map<String,Object> map = Maps.newHashMap();
        String msg = "";
        boolean result = false;
        LowerShelfBillVO lowerShelfBillVO = lowerShelfDAO.selectById(lowerId);
        //删除详情
        lowerShelfDetailDAO.deleteLowerDetail(lowerId);
        //删除下架单
        Integer id = lowerShelfDAO.deleteById(Long.parseLong(lowerId));
        if(id>0){
            //更新对应的出库单的状态
            lowerShelfDAO.updateOutStorageState(lowerShelfBillVO.getOutstorageBillId());
            //将对应出库单状态改回去
            /*String outid = lowerShelfBillVO.getOutstorageBillId();
            OutStorageManagerVO  storageManagerVO =  outStorageService.selectById(outid);
            storageManagerVO.setState("2");
            outStorageService.updateById(storageManagerVO);*/
            msg = "删除成功";
            result = true;
        }else{
            msg = "删除失败";
        }
        map.put("msg",msg);
        map.put("result",result);
        return map;
    }


}
