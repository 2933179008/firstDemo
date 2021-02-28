package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.basedata.entity.Customer;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.CustomerService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.StockDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Stock;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 库存service实现
 *
 * @author pz
 * @date 2019-01-08
 */
@Service("stockService")
public class StockServiceImpl extends ServiceImpl<StockDAO, Stock> implements StockService {

    //库存DAO
    @Autowired
    private StockDAO stockDao;

    //物料绑定RFID详情Service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    @Autowired
    private MaterielBindRfidDAO materielBindRfidDAO;

    //库位Service
    @Autowired
    private DepotPositionService depotPositionService;

    //物料Service
    @Autowired
    private MaterielService materielService;

    //顾客Service
    @Autowired
    private CustomerService customerService;

    /**
     * 库存分页查询
     *
     * @param parms
     * @return
     * @author pz
     * @date 2018-01-09
     */
    public PageUtils queryPage(Map<String, Object> parms) {

        //获取保质期为空的库存记录
        List<Stock> stockList = stockDao.selectByListQuality();
        //循环依据生产日期赋值保质期
        if(stockList!=null&&stockList.size()>0){
            this.setQualityDate(stockList);
        }

        Page<Stock> page = this.selectPage(
                new Query<Stock>(parms).getPage(),
                new EntityWrapper<>()
        );

        List<Stock> lstStock = stockDao.selectByList(page, parms);

        //遍历添加
        for (Stock stock : lstStock) {
            if(StringUtils.isNotBlank(stock.getCustomerCode())){
                EntityWrapper<Customer> wraCustomer = new EntityWrapper<>();
                wraCustomer.eq("customer_code",stock.getCustomerCode());
                Customer customer = customerService.selectOne(wraCustomer);
                stock.setCustomerName(customer.getCustomerName());
            }
        }

        return new PageUtils(page.setRecords(lstStock));
    }

    /**
     * 库存详细分页查询
     *
     * @param params
     * @return
     * @author pz
     * @date 2018-01-10
     */
    public PageUtils queryPageS(Map<String, Object> params, Long stockId) {

        Page<MaterielBindRfidDetail> page = null;

        Stock stock = stockDao.selectById(stockId);
        if (stock == null) {
            return new PageUtils(page);
        }

        //库存物料批次号
        String batchNo = stock.getBatchNo();
        //库存物料编码
        String stockMaterialCode = stock.getMaterialCode();
        //库存物料RFID
        String rfid = stock.getRfid();
        if (!Strings.isNullOrEmpty(batchNo) && !Strings.isNullOrEmpty(stockMaterialCode) && !Strings.isNullOrEmpty(rfid)) {
            List<String> lstRfids = Arrays.stream(rfid.split(",")).map(s -> s.trim()).collect(Collectors.toList());
            page = materielBindRfidDetailService.selectPage(
                    new Query<MaterielBindRfidDetail>(params).getPage(),
                    new EntityWrapper<MaterielBindRfidDetail>()
                            .eq("delete_flag", DyylConstant.NOTDELETED)
                            .eq("batch_rule", batchNo)
                            .eq("materiel_code", stockMaterialCode)
                            .in("rfid", lstRfids)
            );
            for (MaterielBindRfidDetail materielBindRfidDetail : page.getRecords()) {
                MaterielBindRfid materielBindRfid = materielBindRfidDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
                if (materielBindRfid.getPositionBy() != null) {
                    DepotPosition depotPosition = depotPositionService.selectById(materielBindRfid.getPositionBy());
                    if (depotPosition != null && depotPosition.getPositionName() != null) {
                        materielBindRfidDetail.setPositionName(depotPosition.getPositionName());
                    }
                }
            }
            return new PageUtils(page);
        } else {
            return null;
        }
    }

    /**
     * select查询盘点类型数据字典
     *
     * @param map
     * @return PageUtils
     * @author pz
     * @date 20181009
     */
    public Page<Stock> getSelectPositionList(Map<String, Object> map) {
        Page<Stock> page = this.selectPage(
                new Query<Stock>(map).getPage(),
                new EntityWrapper<>()
        );
        return page.setRecords(stockDao.getSelectPositionList(page, map));
    }

    /**
     * 获取保质期为空的库存记录
     *
     * @param stockList
     * @return
     * @author pz
     * @date 20190522
     */
    public void setQualityDate(List<Stock> stockList) {
        //遍历添加
        for (Stock stock : stockList) {
            String productDate = stock.getProductDate();
            String qualityDate = stock.getQualityDate();
            if (productDate != null && StringUtils.isEmpty(qualityDate)) {
                //获取物料信息
                String materialCode = stock.getMaterialCode();
                EntityWrapper<Materiel> wraMateriel = new EntityWrapper<>();
                wraMateriel.eq("materiel_code", materialCode);
                Materiel materiel = materielService.selectOne(wraMateriel);
                //根据物料编号获取保质期
                Integer qualityPeriod = Integer.parseInt(StringUtils.isEmpty(materiel.getQualityPeriod()) ? "0" : materiel.getQualityPeriod());

                Date aD = DateUtils.stringToDate(productDate, "yyyyMMdd");

                String productDateS = DateUtils.format(aD, "yyyy-MM-dd");

                Date productDate_ = DateUtils.stringToDate(productDateS, "yyyy-MM-dd");

                //有效期至
                String deadline = DateUtils.format(DateUtils.addDateDays(productDate_, qualityPeriod), "yyyyMMdd");
                stock.setQualityDate(deadline);
                stockDao.updateById(stock);
            }
        }
    }

}
