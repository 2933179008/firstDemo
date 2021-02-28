package com.tbl.modules.slab.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ctc.wstx.util.DataUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageUtils;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.handheldInterface.service.OutStorageInterfaceService;
import com.tbl.modules.outstorage.dao.LowerShelfDAO;
import com.tbl.modules.outstorage.dao.LowerShelfDetailDAO;
import com.tbl.modules.outstorage.entity.LowerShelfBillVO;
import com.tbl.modules.platform.constant.Const;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.slab.dao.OutStorageSlabDAO;
import com.tbl.modules.slab.service.OutStorageSlabService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lcg
 * data 2019/3/11
 */
@Service("outStorageSlabService")
public class OutStorageSlabServiceImpl implements OutStorageSlabService {

    @Autowired
    private OutStorageSlabDAO outStorageSlabDAO;

    @Autowired
    private LowerShelfDAO lowerShelfDAO;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private OutStorageInterfaceService outStorageInterfaceService;

    /**
     * 获取当前人的任务
     * @return
     */
    @Override
    public Page<LowerShelfBillVO> queryPage(Map<String,Object> params) {
        int pg = Integer.parseInt(params.get("page").toString());
        int rows = Integer.parseInt(params.get("limit").toString());
        boolean order = false;
        if("asc".equals(String.valueOf(params.get("order")))){
            order = true;
        }
        String sortname = String.valueOf(params.get("sidx"));
        Page<LowerShelfBillVO> pagePutBill = new Page<LowerShelfBillVO>(pg,rows,sortname,order);
        //获取上架单列表
        List<LowerShelfBillVO> list = outStorageSlabDAO.getPagePutBillList(pagePutBill, params);

        return pagePutBill.setRecords(list);
    }

    /**
     * 更新绑定表中的操作类型
     * @param userIp
     */
    @Override
    public void updateType(String userIp) {
        Integer count = outStorageSlabDAO.getUserIpCount(userIp);
        Map<String,Object> map = Maps.newHashMap();
        map.put("userIp",userIp);
        map.put("operate_type","2");
        String nowTime = DateUtils.getTime();
        if(count>0){
            map.put("update_time",nowTime);
            //表示的是更新
            outStorageSlabDAO.updateMap(map);
        }else{
            map.put("create_time",nowTime);
            map.put("update_time",nowTime);
            //表示的添加
            outStorageSlabDAO.insertMap(map);
        }
    }

    /**
     * 更新绑定信息
     * @param lowerId
     */
    @Override
    public void insertOrUpdateSlabOutBillBunding(String lowerId,String userIp) {
        // shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        //当前登陆人id
        Long userId = user.getUserId();
        //更具IP查询平板是否已经有绑定数据
        Integer count = outStorageSlabDAO.getIpCount(userIp);
        Map<String,Object> map = Maps.newHashMap();
        String nowTime = DateUtils.getTime();
        map.put("lowerId",lowerId);
        map.put("update_time",nowTime);
        map.put("user_id",userId);
        map.put("user_ip",userIp);
        if(count>0){
            //表示已经存在,只能进行更新
            outStorageSlabDAO.updateSlabOutBillBunding(map);
        }else{
            map.put("create_time",nowTime);
            //表示不存在,则进行插入操作
            outStorageSlabDAO.insertSlabOutBillBunding(map);
        }
    }

    /**
     * 更新下架单的状态
     * @param lowerId
     */
    @Override
    public void updateLowerBillState(String lowerId) {
        outStorageSlabDAO.updateLowerState(lowerId);
        outStorageSlabDAO.updateLowerDetailState(lowerId);
    }

    /**
     * 叉车上的读写器读到叉到货物调用的方法
     * @param userIp
     * @param rfid
     * @return
     */
    @Override
    public Map<String,Object> slabDownRfid(String userIp, String rfid) {
        Map<String,Object> map = Maps.newHashMap();
        boolean result = false;
        String msg = "";
        String alertKey="";
        String alertValue="";
        String slabRfid= "";
        String time = DateUtils.getTime();
        try{
            //根据userIp 获取当前的任务
            if(Strings.isNullOrEmpty(userIp) || Strings.isNullOrEmpty(rfid)){
                map.put("result",false);
                map.put("msg","调用接口失败，请确认参数是否有效");
                return map;
            }else{
                //根据userIP获取对应的绑定的信息,此信息唯一的
                Map<String,Object> taskMap = this.getTaskMap(userIp);
                String lowerId = "";

                if(taskMap!=null){
                    lowerId = taskMap.get("lower_id").toString();
                    LowerShelfBillVO lowerShelfBillVO = lowerShelfDAO.selectById(lowerId);
                    //通过下架单的ID以及叉车叉到的rfid查询该rfid是否是当前的任务
                    Integer count =  outStorageSlabDAO.getLowerTask(lowerId,rfid);
                    if(count>0){
                        //表示的是rfid 存在当前的任务中
                        alertKey="1";
                        alertValue="验证通过";
                        slabRfid = rfid;
                    }else{
//                        //查询当前的标签是否是当前任务中的统一批次号的
//                        //先查询对应的rfid 的绑定关系
//                        Map<String,Object> rfidBind = outStorageSlabDAO.rfidBind(rfid,lowerShelfBillVO.getOutstorageBillId());
//                        if(rfidBind!=null && !rfidBind.isEmpty()){
//                            //通过批次号以及数量在下架单详情中进行判断,如果批次号以及数量相等的话,则是同一批次进行更新
//                            Object rfidCount = outStorageSlabDAO.isExist(lowerId,rfidBind.get("batch_rule").toString(),rfidBind.get("occupy_stock_weight").toString());
//                            if(rfidCount!=null){
//                                //表示查询到到了对应的rfid编号
//                                slabRfid = rfid;
//                                //更新下架单详情中的数值
//                                outStorageSlabDAO.updateRfid(rfid,rfidBind.get("position_id").toString(),lowerId,rfidBind.get("batch_rule").toString(),rfidBind.get("occupy_stock_weight").toString(),rfidCount.toString());
//                                //将原先的物料绑定关系解除
//                                outStorageSlabDAO.removeMaterial(rfidCount.toString(),lowerShelfBillVO.getOutstorageBillId());
//                                outStorageSlabDAO.removeStock(rfidCount.toString(),lowerShelfBillVO.getOutstorageBillId());
//                                alertKey="1";
//                                alertValue="验证通过";
//                            }else{
//                                //表示该rfid 不存在当前的任务
//                                alertKey="0";
//                                alertValue="当前rfid绑定的物料在下架中不存在";
//                            }
//                        }else{
//                            alertKey="0";
//                            alertValue="当前rfid没有正在下架的任务";
//                        }
                        map = outStorageInterfaceService.determineRfid(rfid,lowerId);
                        alertKey = map.get("alertKey").toString();
                        alertValue = map.get("alertValue").toString();

                    }
                    if(taskMap.get("rfid") == null){
                        slabRfid = rfid;
                    }else{
                        String avRfid = taskMap.get("rfid").toString();
                        String[] rfidList = avRfid.split(",");
                        if(rfidList.length==1){
                            //判断读到的标签是否相等
                            if(!rfid.equals(rfidList[0])){
                                slabRfid = rfidList[0]+","+rfid;
                            }
                        }
                        if(rfidList.length==0){
                            slabRfid = rfid;
                        }
                    }
                }else{
                    map.put("result",false);
                    map.put("msg","没有找到有效的任务");
                }
            }

            //更新绑定表中的数值
            Map<String,Object> resultMap = Maps.newHashMap();
            resultMap.put("alertKey",alertKey);
            resultMap.put("alertValue",alertValue);
            resultMap.put("userIP",userIp);
            resultMap.put("time",time);
            resultMap.put("slabRfid",slabRfid);
            outStorageSlabDAO.updateSlabOutBillBundingByKey(resultMap);
            result = true;
            msg = "接口执行成功";
        }catch (Exception e){
            result = false;
            msg = "接口执行失败";
            e.printStackTrace();
        }
        map.put("result",result);
        map.put("msg",msg);
        return map;
    }

    /**
     * 叉车放下料箱后的调用的方法
     * @param userIp
     * @return
     */
    @Override
    public Object slabDrop(String userIp) {
        //通过userIP获取当前的rfid 的值
        Map<String,Object> map = outStorageSlabDAO.getTaskMap(userIp);
        //通过下架单ID以及下架的rfid获取对应的详情的任务
        Object lowerDetailId = outStorageSlabDAO.getLowerDetailId(map);
        return lowerDetailId;
    }

    @Override
    public Map<String,Object> alivableRfid(String userIp) {
        Map<String,Object> avliableRfid = outStorageSlabDAO.avliableRfid(userIp);
        return avliableRfid;
    }

    /**
     * 清除表中的rfid的信息
     * @param ip
     */
    @Override
    public void updateRfid(String ip) {
        outStorageSlabDAO.removeRfid(ip);
    }

    /**
     * 获取下架单的id
     * @param map
     * @return
     */
    @Override
    public Object getLowerId(Map<String, Object> map) {
        Object lowerDetailId = outStorageSlabDAO.getLowerDetailId(map);
        return lowerDetailId;
    }

    /**
     * 获取任务的集合
     * @param userIp
     * @return
     */
    @Override
    public Map<String, Object> getTaskMap(String userIp) {
        return outStorageSlabDAO.getTaskMap(userIp);
    }

    /**
     * 通过下架单ID以及rfid获取对应的物料的基础信息
     * @param rfid
     * @param lowerId
     * @return
     */
    @Override
    public Map<String, Object> getMaterialMap(String rfid, String lowerId) {
        return outStorageSlabDAO.getMaterialMap(rfid,lowerId);
    }

    /**
     * 通过单据Id以及rfid 更新当前系统中的可用的rfid 编号
     * @param id
     * @param rfid
     * @return
     */
    @Override
    public Map<String, Object> confirmState(String id, String rfid,String rfid1) {
        Map<String,Object> map = Maps.newHashMap();
//        Object avilablerfid = outStorageSlabDAO.getRfid(id);
        String avilabledRfid = "";
        if (Strings.isNullOrEmpty(rfid)) {
            map.put("msg","确认失败");
            map.put("result",false);
        }else {
            if(!Strings.isNullOrEmpty(rfid1)){
                avilabledRfid = rfid+","+rfid1;
            }else{
                avilabledRfid =  rfid;
            }
//            if (avilablerfid != null) {
//                //更新数据
//                avilabledRfid = avilablerfid.toString() + "," + rfid;
//            } else {
//                avilabledRfid = rfid;
//            }
            //进行数据的更新
            Integer ids = outStorageSlabDAO.updateAvilabledRfid(avilabledRfid, id);
            map.put("msg", "确认成功");
            map.put("result", true);
        }
        return map;
    }

    /**
     * 通过id获取对应的可用的rfid的值
     * @param id
     * @return
     */
    public Object getArfid(String id){
        return outStorageSlabDAO.getArfid(id);
    }
}
