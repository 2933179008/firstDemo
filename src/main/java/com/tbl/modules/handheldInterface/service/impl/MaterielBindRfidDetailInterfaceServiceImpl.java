package com.tbl.modules.handheldInterface.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidDetailInterfaceDAO;
import com.tbl.modules.handheldInterface.dao.MaterielBindRfidInterfaceDAO;
import com.tbl.modules.handheldInterface.service.MaterielBindRfidDetailInterfaceService;
import com.tbl.modules.handheldInterface.service.MaterielBindRfidInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料绑定RFID详情接口Service实现
 */
@Service(value = "materielBindRfidDetailInterfaceService")
public class MaterielBindRfidDetailInterfaceServiceImpl
        extends ServiceImpl<MaterielBindRfidDetailInterfaceDAO, MaterielBindRfidDetail> implements MaterielBindRfidDetailInterfaceService {

    //日志接口DAO
    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private MaterielBindRfidDetailInterfaceDAO materielBindRfidDetailInterfaceDAO;

    @Autowired
    private MaterielBindRfidInterfaceDAO materielBindRfidInterfaceDAO;

    @Autowired
    private MaterielBindRfidInterfaceService materielBindRfidInterfaceService;


    @Override
    public List<MaterielBindRfidDetail> getMaterielBindRfidDetail(String rfid) {

        return materielBindRfidDetailInterfaceDAO.getMaterielBindRfidDetail(rfid);
    }

    /**
     * RFID 查询未入库的详情
     */
    @Override
    public Map<String, Object> selectRfidDetail(String rfid) {
        Map<String, Object> map = new HashMap<>();
        String parameter = "Rfid:" + rfid;
        if (Strings.isNullOrEmpty(rfid)){
            interfaceLogService.interfaceLogInsert("调用判断是否是白糖绑定接口", parameter, "失败原因：未获取查询参数", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未获取查询参数");
            return map;
        }
        //查询RFID是否入库
        MaterielBindRfid materielBindRfid = materielBindRfidInterfaceService.selectOne(
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_PROCESSED)
                        .eq("rfid", rfid)

        );

        //若为null，则RFID未入库，获取详情
        if (materielBindRfid == null) {
            interfaceLogService.interfaceLogInsert("调用判断是否是白糖绑定接口", parameter, "关于此RFID符合条件的相关信息", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "无关于此RFID符合条件的相关信息");
            return map;
        }
        //获取详情
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("status", DyylConstant.STATE_PROCESSED)
                        .eq("materiel_bind_rfid_by", materielBindRfid.getId())
        );
        //判断集合是否为空
        if (materielBindRfidDetailList.isEmpty()){
            interfaceLogService.interfaceLogInsert("调用判断是否是白糖绑定接口", parameter, "关于此RFID符合条件的相关信息", DateUtils.getTime());
            map.put("result",false);
            map.put("msg","关于此RFID符合条件的相关信息");
            return map;
        }
        interfaceLogService.interfaceLogInsert("调用判断是否是白糖绑定接口", parameter, "查询成功", DateUtils.getTime());
        map.put("result",true);
        map.put("data",materielBindRfidDetailList);
        return map;
    }

    /**
     * 删除关于此id的绑定详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> deleteRfidDetail(Long id) {
        Map<String,Object> map = new HashMap<>();
        String parameter = "id:" + id;
        if (id==null){
            interfaceLogService.interfaceLogInsert("调用删除关于此id的绑定详情接口", parameter, "失败原因：未获取查询参数", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：未获取查询参数");
            return map;
        }
        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailInterfaceDAO.selectById(id);
        if (materielBindRfidDetail == null){
            interfaceLogService.interfaceLogInsert("调用删除关于此id的绑定详情接口", parameter, "失败原因：id未找到对应的绑定详情", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：id未找到对应的绑定详情");
            return map;
        }
        materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
        Integer count = materielBindRfidDetailInterfaceDAO.updateById(materielBindRfidDetail);
        if (count<=0){
            interfaceLogService.interfaceLogInsert("调用删除关于此id的绑定详情接口", parameter, "失败原因：想请删除操作失败", DateUtils.getTime());
            map.put("result", false);
            map.put("msg", "失败原因：id未找到对应的绑定详情");
            return map;
        }

        //获取关于此物料绑定主表的未删除绑定详情
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailInterfaceDAO.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                .eq("materiel_bind_rfid_by",materielBindRfidDetail.getMaterielBindRfidBy())
                .eq("delete_flag",DyylConstant.NOTDELETED)
        );
        //若未删除绑定详情集合为空，则删除此物料绑定主表
        if (materielBindRfidDetailList.isEmpty() || materielBindRfidDetailList.size()<=0){
            MaterielBindRfid materielBindRfid = materielBindRfidInterfaceDAO.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
            materielBindRfid.setDeletedFlag(DyylConstant.DELETED);
            if (!materielBindRfidInterfaceService.updateById(materielBindRfid)){
                interfaceLogService.interfaceLogInsert("调用删除关于此id的绑定详情接口", parameter, "失败原因：物料绑定RFID主表删除失败", DateUtils.getTime());
                map.put("result", false);
                map.put("msg", "失败原因：物料绑定RFID主表删除失败");
                return map;
            }
        }



        interfaceLogService.interfaceLogInsert("调用删除关于此id的绑定详情接口", parameter, "删除成功", DateUtils.getTime());
        map.put("result", true);
        map.put("msg", "删除成功");
        return map;
    }

}
