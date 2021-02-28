package com.tbl.modules.outstorage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.entity.Supplier;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.basedata.service.SupplierService;
import com.tbl.modules.outstorage.dao.SpareBillDetailDAO;
import com.tbl.modules.outstorage.entity.SpareBillDetailManagerVO;
import com.tbl.modules.outstorage.service.SpareBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcg
 * data 2019/1/15
 */
@Service("spareBillDetailService")
public class SpareBillDetailServiceImpl extends ServiceImpl<SpareBillDetailDAO, SpareBillDetailManagerVO> implements SpareBillDetailService {

    @Autowired
    private SpareBillDetailDAO spareBillDetailDAO;

    @Autowired
    private MaterielService materielService;

    @Autowired
    private SupplierService supplierService;

    /**
     * 获取对应的备料单的详情
     *
     * @param id
     * @return
     */
    @Override
    public List<SpareBillDetailManagerVO> spareBillDetailList(String id) {
        List<SpareBillDetailManagerVO> list = spareBillDetailDAO.spareBillDetailList(id);
        return list;
    }

    /**
     * 通过备料单的id获取备料单的详情数据
     *
     * @param id
     * @param i
     * @return
     */
    @Override
    public List<SpareBillDetailManagerVO> getSpareBillList(String id) {
        List<SpareBillDetailManagerVO> list = spareBillDetailDAO.spareDetailList(id);
        return list;
    }

    /**
     * 更新备料单中的仓库的发货数量
     *
     * @param spreBillId
     * @param map
     * @return
     */
    @Override
    public Object updateDetail(String spreBillId, Map<String, Object> map) {
        String weight = map.get("weight").toString();
        String amount = map.get("amount").toString();
        String materialCode = map.get("material_code").toString();
        spareBillDetailDAO.updateDetail(spreBillId, weight, amount, materialCode);
        return null;
    }

//    /**
//     * 备料单详情的添加
//     * @param json
//     * @param spareBillId
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Integer saveSpareDetailBill(String json, Integer spareBillId) {
//        JSONArray jsonArray = JSONObject.parseArray(json);
//        SpareBillDetailManagerVO spareBillDetailManagerVO = new SpareBillDetailManagerVO();
//        spareBillDetailManagerVO.setSpareBillId(spareBillId.longValue());
//        Integer id = null;
//        for(Object jsond : jsonArray){
//            JSONObject jsonObject = JSONObject.parseObject(jsond.toString());
//            if(jsonObject.get("material_code")==null || jsonObject.get("material_code").equals("")){
//                continue;
//            }
//            spareBillDetailManagerVO.setMaterialCode(jsonObject.get("material_code")==null?"":jsonObject.get("material_code").toString());
//            spareBillDetailManagerVO.setPositionCode(jsonObject.get("position_code")==null?"":jsonObject.get("position_code").toString());
//            spareBillDetailManagerVO.setSupplierCode(jsonObject.get("supplier_code")==null?"":jsonObject.get("supplier_code").toString());
//            spareBillDetailManagerVO.setUserAmount(jsonObject.get("user_amount")==null?"":jsonObject.get("user_amount").toString());
//            spareBillDetailManagerVO.setUsedAmount(jsonObject.get("used_amount")==null?"":jsonObject.get("used_amount").toString());
//            spareBillDetailManagerVO.setQueryWeight(jsonObject.get("query_weight")==null?"":jsonObject.get("query_weight").toString());
//            spareBillDetailManagerVO.setQueryBox(jsonObject.get("query_box")==null?"":jsonObject.get("query_box").toString());
//            spareBillDetailManagerVO.setQueryAdd(jsonObject.get("query_add")==null?"":jsonObject.get("query_add").toString());
//            spareBillDetailManagerVO.setUsedBox(jsonObject.get("used_box")==null?"":jsonObject.get("used_box").toString());
//            spareBillDetailManagerVO.setAddAmount(jsonObject.get("add_amount")==null?"":jsonObject.get("add_amount").toString());
//            spareBillDetailManagerVO.setSendAmount(jsonObject.get("send_amount")==null?"":jsonObject.get("send_amount").toString());
//            spareBillDetailManagerVO.setTheoryAmount(jsonObject.get("theory_amount")==null?"":jsonObject.get("theory_amount").toString());
//            spareBillDetailManagerVO.setRealAmount(jsonObject.get("real_amount")==null?"":jsonObject.get("real_amount").toString());
//            spareBillDetailManagerVO.setDifference(jsonObject.get("difference")==null?"":jsonObject.get("difference").toString());
//            spareBillDetailManagerVO.setMonth(jsonObject.get("month")==null?"":jsonObject.get("month").toString());
//            spareBillDetailManagerVO.setDay(jsonObject.get("day")==null?"":jsonObject.get("day").toString());
//            spareBillDetailManagerVO.setSurplusAmount(jsonObject.get("surplus_amount")==null?"":jsonObject.get("surplus_amount").toString());
//            spareBillDetailManagerVO.setOrder(jsonObject.get("order")==null?"":jsonObject.get("order").toString());
//            id = spareBillDetailDAO.insert(spareBillDetailManagerVO);
//        }
//        if(id<=0){
//            //将事务进行回滚
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//        return id;
//    }


    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        //物料绑定RFID 主键id
        String spareBillId = (String) parms.get("spareBillId");

        Page<SpareBillDetailManagerVO> spareBillDetailManagerVOPage = new Page<SpareBillDetailManagerVO>();

        //如果materielBindRfidBy不为空分页查询获取的数据
        if (StringUtils.isNotBlank(spareBillId)) {
            spareBillDetailManagerVOPage = this.selectPage(
                    new Query<SpareBillDetailManagerVO>(parms).getPage(),
                    new EntityWrapper<SpareBillDetailManagerVO>()
                            .eq("spare_bill_id", spareBillId)

            );
            for (SpareBillDetailManagerVO spareBillDetailManagerVO : spareBillDetailManagerVOPage.getRecords()) {
                //添加物料名称
                if (StringUtils.isNotEmpty(spareBillDetailManagerVO.getMaterialCode())) {
                    Materiel materiel = materielService.selectOne(
                            new EntityWrapper<Materiel>()
                                    .eq("materiel_code", spareBillDetailManagerVO.getMaterialCode())
                    );
                    if (materiel != null && StringUtils.isNotEmpty(materiel.getMaterielName())) {
                        spareBillDetailManagerVO.setMaterialName(materiel.getMaterielName());
                    }
                }
                //添加供应商名称
                if (StringUtils.isNotEmpty(spareBillDetailManagerVO.getSupplierCode())) {
                    Supplier supplier = supplierService.selectOne(
                            new EntityWrapper<Supplier>()
                            .eq("supplier_code",spareBillDetailManagerVO.getSupplierCode())
                    );
                    if (supplier!=null && StringUtils.isNotEmpty(supplier.getSupplierName())){
                        spareBillDetailManagerVO.setSupplierName(supplier.getSupplierName());
                    }
                }
            }
        }

        return new PageUtils(spareBillDetailManagerVOPage);
    }
}
