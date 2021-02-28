package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.stock.dao.InventoryTaskDetailDAO;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.InventoryTaskDetailService;
import com.tbl.modules.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 盘点任务详情service实现
 *
 * @author pz
 * @date 2019-01-14
 */
@Service("inventoryTaskDetailService")
public class InventoryTaskDetailServiceImpl extends ServiceImpl<InventoryTaskDetailDAO, InventoryTaskDetail> implements InventoryTaskDetailService {

    // 盘点任务详情DAO
    @Autowired
    private InventoryTaskDetailDAO inventoryTaskDetailDao;

    @Autowired
    private StockService stockService;

    @Autowired
    private MaterielService materielService;

    /**
     * 盘点任务详情分页查询
     *
     * @param params
     * @return
     */

    public PageUtils queryPageI(Map<String, Object> params, Long id) {

        Page<InventoryTaskDetail> page = this.selectPage(
                new Query<InventoryTaskDetail>(params).getPage(),
                new EntityWrapper<InventoryTaskDetail>()
                        .eq("inventory_task_id", id)

        );
        for (InventoryTaskDetail inventoryTaskDetail : page.getRecords()) {
            //判断库存类型有无RFID
            String billType = null;
            if (Strings.isNullOrEmpty(inventoryTaskDetail.getRfid())){
                billType = DyylConstant.MATERIAL_NORFID;
            }else {
                billType = DyylConstant.MATERIAL_RFID;
            }
            //获取库存
            Stock stock = stockService.selectOne(
                    new EntityWrapper<Stock>()
                            .eq("material_type", billType)
                            .eq("material_code", inventoryTaskDetail.getMaterialCode())
                            .eq("batch_no", inventoryTaskDetail.getBatchNo())
                            .eq("position_code", inventoryTaskDetail.getPositionCode())
            );
           //获取物料基础信息
            Materiel materiel = materielService.selectOne(new EntityWrapper<Materiel>().eq("materiel_code",inventoryTaskDetail.getMaterialCode()));
            inventoryTaskDetail.setQualityDate(stock.getQualityDate());
            inventoryTaskDetail.setQualityPeriod(materiel.getQualityPeriod());

            //获取库存数量
            String stockAmount = inventoryTaskDetail.getStockAmount();
            //获取盘点数量
            String inventoryAmount = inventoryTaskDetail.getInventoryAmount();
            //判断状态 空：未盘；0：盘亏；1：盘盈；2：盘平
            if(!inventoryTaskDetail.getState().equals("0")){
                if(StringUtils.isEmpty(stockAmount)){
                    inventoryTaskDetail.setInventoryState("1");
                }else if(StringUtils.isEmpty(inventoryAmount)){
                    inventoryTaskDetail.setInventoryState("0");
                }else{
                    if(Double.valueOf(stockAmount)<Double.valueOf(inventoryAmount))
                    {
                        inventoryTaskDetail.setInventoryState("1");
                    }else if(Double.valueOf(stockAmount)>Double.valueOf(inventoryAmount))
                    {
                        inventoryTaskDetail.setInventoryState("0");
                    }else{
                        inventoryTaskDetail.setInventoryState("2");
                    }
                }
            }
            if(StringUtils.isNotEmpty(stockAmount)&&stockAmount.contains(".")){
                inventoryTaskDetail.setStockAmount(formatDouble(Double.parseDouble(stockAmount)));
                inventoryTaskDetailDao.updateById(inventoryTaskDetail);
            }
            if(StringUtils.isNotEmpty(inventoryAmount)&&inventoryAmount.contains(".")){
                inventoryTaskDetail.setInventoryAmount(formatDouble(Double.parseDouble(inventoryAmount)));
                inventoryTaskDetailDao.updateById(inventoryTaskDetail);
            }
        }
        return new PageUtils(page);
    }

    @Override
    public List<Map<String, Object>> getSelectPositionList(String queryString, int pageSize, int pageNo) {
        Page page=new Page(pageNo,pageSize);
        return inventoryTaskDetailDao.getSelectPositionList(page,queryString);
    }

    @Override
    public Integer getSelectPositionTotal(String queryString) {
        return inventoryTaskDetailDao.getSelectPositionTotal(queryString);
    }

    /**
     * 获取进行中任务单详细
     * @author pz
     * @date 2019-02-11
     * @param id
     * @return
     */
    @Override
    public List<InventoryTaskDetail> taskDeatilList(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("inventory_task_id", id);
        return inventoryTaskDetailDao.selectByMap(map);
    }

    //如果小数点后为零则显示整数否则保留两位小数
    public static String formatDouble(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

}
