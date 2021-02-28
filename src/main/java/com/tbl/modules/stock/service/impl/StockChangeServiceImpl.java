package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.stock.dao.StockChangeDAO;
import com.tbl.modules.stock.entity.StockChange;
import com.tbl.modules.stock.service.StockChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 库存变动service实现
 *
 * @author pz
 * @date 2019-01-08
 */
@Service("stockChangeService")
public class StockChangeServiceImpl extends ServiceImpl<StockChangeDAO, StockChange> implements StockChangeService {

    //用户DAO
    @Autowired
    private UserDAO userDAO;

    //库位DAO
    @Autowired
    private DepotPositionDAO depotPositionDao;

    //库存变动Service
    @Autowired
    private StockChangeService stockChangeService;

    /**
     * 库存分页查询
     *
     * @param params
     * @return
     */
    public PageUtils queryPage(Map<String, Object> params) {

        //物料编号
        String materialCode = (String) params.get("materialCode");

        //物料名称
        String materialName = (String) params.get("materialName");

        //业务类型
        String businessType = (String) params.get("businessType");

        //开始时间
        String startTime = (String) params.get("startTime");

        //结止时间
        String endTime = (String) params.get("endTime");

        //库位
        String positionBy = (String) params.get("positionBy");

        //单据号
        String changeCode = (String) params.get("changeCode");

        //获取当前时间
        String nowDate = DateUtils.getTime();
        //获取七天前的日期
        String formDate = DateUtils.getAfterDayDate("-7");

        //开始时间（时分秒）
        String startTimeCurrate = startTime + " 00:00:00";
        //结束时间（时分秒）
        String endTimeCurrate = endTime + " 23:59:59";


        if (materialCode != null || materialName != null || businessType != null || positionBy != null || changeCode != null) {
            Page<StockChange> page = this.selectPage(
                    new Query<StockChange>(params).getPage(),
                    new EntityWrapper<StockChange>()
                            .like(StringUtils.isNotEmpty(materialCode), "material_code", materialCode)
                            .like(StringUtils.isNotEmpty(materialName), "material_name", materialName)
                            .like(StringUtils.isNotEmpty(businessType), "business_type", businessType)
                            .ge(StringUtils.isNotEmpty(startTimeCurrate), "create_time", startTimeCurrate)
                            .le(StringUtils.isNotEmpty(startTimeCurrate), "create_time", endTimeCurrate)
                            .like(StringUtils.isNotEmpty(positionBy), "position_by", positionBy)
                            .like(StringUtils.isNotEmpty(changeCode), "change_code", changeCode)
            );

            this.generateStockChange(page);

            return new PageUtils(page);
        } else {
            Page<StockChange> page = this.selectPage(
                    new Query<StockChange>(params).getPage(),
                    new EntityWrapper<StockChange>()
                            .ge(StringUtils.isNotEmpty(formDate), "create_time", formDate)
                            .le(StringUtils.isNotEmpty(nowDate), "create_time", nowDate)
            );

            this.generateStockChange(page);

            return new PageUtils(page);
        }
    }


    public void generateStockChange(Page<StockChange> page) {
        for (StockChange stockChange : page.getRecords()) {
            User user = userDAO.selectById(stockChange.getCreateBy());
            if (user != null) {
                stockChange.setCreateName(user.getUsername());
            }
            if (stockChange.getPositionBy() != null) {
                if (depotPositionDao.selectById(stockChange.getPositionBy()) != null) {
                    stockChange.setPositionName(depotPositionDao.selectById(stockChange.getPositionBy()).getPositionName());
                }
            } else {
                stockChange.setPositionName("");
            }
        }
    }

    /**
     * @Description:  插入库存变动记录
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/18
     */
    public boolean saveStockChange(String changeCode, String materialCode, String materialName, String batchNo, String changType, String outAmount, String inAmount, String balanceAmount, Long positionBy, Long userId) {
        StockChange stockChange = new StockChange();
        stockChange.setChangeCode(changeCode);
        stockChange.setMaterialCode(materialCode);
        stockChange.setMaterialName(materialName);
        stockChange.setBatchNo(batchNo);
        stockChange.setBusinessType(changType);
        stockChange.setOutAmount(outAmount);
        stockChange.setInAmount(inAmount);
        stockChange.setBalanceAmount(balanceAmount);
        stockChange.setPositionBy(positionBy);
        stockChange.setCreateTime(DateUtils.getTime());
        stockChange.setCreateBy(userId);
        return stockChangeService.insert(stockChange);
    }

}
