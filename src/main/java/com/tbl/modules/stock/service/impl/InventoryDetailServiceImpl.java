package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.basedata.service.MaterielService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.stock.dao.InventoryDetailDAO;
import com.tbl.modules.stock.dao.InventoryTaskDAO;
import com.tbl.modules.stock.entity.Inventory;
import com.tbl.modules.stock.entity.InventoryDetail;
import com.tbl.modules.stock.entity.InventoryTaskDetail;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.service.InventoryDetailService;
import com.tbl.modules.stock.service.InventoryService;
import com.tbl.modules.stock.service.InventoryTaskDetailService;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 盘点详细service实现
 *
 * @author pz
 * @date 2019-01-09
 */
@Service("inventoryDetailService")
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailDAO, InventoryDetail> implements InventoryDetailService {

    //盘点Service
    @Autowired
    private InventoryService inventoryService;

    //盘点记录详情DAO
    @Autowired
    private InventoryDetailDAO inventoryDetailDAO;

    //盘点任务DAOService
    @Autowired
    private InventoryTaskDAO inventoryTaskDao;

    //盘点详情Service
    @Autowired
    private InventoryDetailService inventoryDetailService;

    //物料service
    @Autowired
    private MaterielService materielService;

    //库位service
    @Autowired
    private DepotPositionService depotPositionService;

    //盘点任务详情service
    @Autowired
    private InventoryTaskDetailService inventoryTaskDetailService;

    //物料绑定rfid详情service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;


    /**
     * @Description: 保存详情
     * @Param:
     * @return:
     * @Author: pz
     * @Date: 2019/1/27
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> saveInventoryDetail(Long id, String positionCode, String rfid, String materielCode, String materielType, Long userId) {
        Map<String, Object> map = new HashMap<>();
        String msg = "保存成功";
        boolean flag = false;
        //获取库位id
        Long PositionBy = inventoryTaskDao.selectBYCode(positionCode);
        ////判断物料类型
        InventoryDetail inventoryDetail = null;

        //判断物料类型，如果为散货类型则从物料列表取数据保存详情;如果为整货类型则从库存中取数据保存详情
        if (materielType.equals(DyylConstant.MOVEPOSITIONTYPE_CARGO)) {
            //获取相关物料绑定rfid详情记录
            EntityWrapper<MaterielBindRfidDetail> wraMaterielBindRfidDetail = new EntityWrapper<>();
            wraMaterielBindRfidDetail.eq("rfid", rfid);
            wraMaterielBindRfidDetail.eq("delete_flag", DyylConstant.NOTDELETED);
            List<MaterielBindRfidDetail> lstMaterielBindRfidDetail = materielBindRfidDetailService.selectList(wraMaterielBindRfidDetail);
            if (lstMaterielBindRfidDetail.size() > 0 && lstMaterielBindRfidDetail != null) {
                for (MaterielBindRfidDetail materielBindRfidDetail : lstMaterielBindRfidDetail) {
                    //获取库位
                    Long PositionId = materielBindRfidDetail.getPositionId();
                    //获取库位编码
                    String mbrdPositionCode = depotPositionService.selectById(PositionId).getPositionCode();
                    //获取物料编码
                    String mbrdMaterialCode = materielBindRfidDetail.getMaterielCode();
                    //获取批次号
                    String mbrdBatchNo = materielBindRfidDetail.getBatchRule();

                    //获取盘点任务单号
                    Inventory inventory = inventoryService.selectById(id);
                    Long inventoryTaskId = inventory.getInventoryTaskId();

                    //根据物料编号与库位编码查询盘点任务详情相关信息
                    EntityWrapper<InventoryTaskDetail> wraInventoryTaskDetail = new EntityWrapper<>();
                    wraInventoryTaskDetail.eq("inventory_task_id", inventoryTaskId);
                    wraInventoryTaskDetail.eq("position_code", mbrdPositionCode);
                    wraInventoryTaskDetail.eq("material_code", mbrdMaterialCode);
                    wraInventoryTaskDetail.eq("batch_no", mbrdBatchNo);
                    wraInventoryTaskDetail.ne("rfid", "");

                    InventoryTaskDetail inventoryTaskDetail = inventoryTaskDetailService.selectOne(wraInventoryTaskDetail);
                    Long InventoryTaskDetailId = null;

                    if (inventoryTaskDetail != null) {
                        InventoryTaskDetailId = inventoryTaskDetail.getId();
                    }
                    if (materielBindRfidDetail != null) {
                        inventoryDetail = new InventoryDetail();
                        //插入盘点id
                        inventoryDetail.setInventoryId(id);
                        //插入物料编码
                        inventoryDetail.setMaterialCode(mbrdMaterialCode);
                        //插入盘点任务详情id
                        inventoryDetail.setInventoryTaskDetailId(InventoryTaskDetailId);
                        //插入批次号
                        inventoryDetail.setBatchNo(mbrdBatchNo);
                        //插入rfid
                        inventoryDetail.setRfid(rfid);
                        //插入物料名称
                        inventoryDetail.setMaterialName(materielBindRfidDetail.getMaterielName());
                        //插入库位编码
                        inventoryDetail.setPositionCode(mbrdPositionCode);
                        //当前时间
                        inventoryDetail.setCreateTime(DateUtils.getTime());
                        //当前登录人
                        inventoryDetail.setCreateBy(userId);
                        flag = inventoryDetailService.insert(inventoryDetail);
                    } else {
                        flag = false;
                        msg = "该rfid:" + rfid + "不存在" + "物料:" + materielCode + "物料的记录";
                    }
                }
            }else {
                flag = false;
                msg = "该rfid:" + rfid + "不存在" + "物料:" + materielCode + "物料的记录";
            }
        } else {
            //依据物料编码，获取物料基础信息
            EntityWrapper<Materiel> entity = new EntityWrapper<>();
            entity.eq("materiel_code", materielCode);
            Materiel materiel = materielService.selectOne(entity);

            inventoryDetail = new InventoryDetail();
            //插入盘点id
            inventoryDetail.setInventoryId(id);
            //获取盘点任务单号
            Inventory inventory = inventoryService.selectById(id);
            Long inventoryTaskId = inventory.getInventoryTaskId();
            //根据物料编号与库位编码查询盘点任务详情相关信息
            InventoryTaskDetail inventoryTaskDetail = inventoryDetailDAO.getInventoryTaskDetail(inventoryTaskId,inventory.getPositionCode(), materielCode);
            Long InventoryTaskDetailId = null;
            if (inventoryTaskDetail != null) {
                //插入盘点任务详情id
                InventoryTaskDetailId = inventoryTaskDetail.getId();
            }
            inventoryDetail.setInventoryTaskDetailId(InventoryTaskDetailId);
            //插入物料编码
            inventoryDetail.setMaterialCode(materielCode);
            //插入物料名称
            inventoryDetail.setMaterialName(materiel.getMaterielName());
            //插入库位编码
            inventoryDetail.setPositionCode(inventory.getPositionCode());
            //当前时间
            inventoryDetail.setCreateTime(DateUtils.getTime());
            //当前登录人
            inventoryDetail.setCreateBy(userId);
            flag = inventoryDetailService.insert(inventoryDetail);
        }
        map.put("result", flag);
        map.put("msg", msg);
        return map;
    }

    /**
     * @Description: 库存详细分页查询
     * @Param:params
     * @return:
     * @author pz
     * @date 2018-01-10
     */
    public PageUtils queryPageID(Map<String, Object> params, Long id) {

        Page<InventoryDetail> page = this.selectPage(
                new Query<InventoryDetail>(params).getPage(),
                new EntityWrapper<InventoryDetail>()
                        .eq("inventory_id", id)
        );

        return new PageUtils(page);
    }

}
