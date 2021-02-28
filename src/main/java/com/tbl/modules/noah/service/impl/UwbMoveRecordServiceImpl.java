package com.tbl.modules.noah.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.tbl.common.utils.DateUtils;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.noah.dao.UwbMoveRecordDAO;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import com.tbl.modules.noah.service.UwbMoveRecordService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UWB移动记录Service实体
 *
 * @author yuany
 * @date 2019-03-04
 */
@Service("uwbMoveRecordService")
public class UwbMoveRecordServiceImpl extends ServiceImpl<UwbMoveRecordDAO, UwbMoveRecord> implements UwbMoveRecordService {

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private UwbMoveRecordDAO uwbMoveRecordDAO;

    @Autowired
    private DepotAreaService depotAreaService;

    /**
     * 添加UWB移动记录
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addUwbMoveRecord(String uwbMoveRecordInfo) {

        JSONObject uwbMoveRecordObj = JSON.parseObject(uwbMoveRecordInfo);
        JSONObject resultObj = new JSONObject();
        String tag = uwbMoveRecordObj.getString("tag");
        String xsize = uwbMoveRecordObj.getString("xsize");
        String ysize = uwbMoveRecordObj.getString("ysize");
        String isMove = uwbMoveRecordObj.getString("is_move");
        String sceneId = uwbMoveRecordObj.getString("scene_code");

        String parameter = "tag:" + tag + "/xsize:" + xsize + "/ysize:" + ysize + "/isMove:" + isMove + "/sceneId:" + sceneId;
        //判断数据是否为空
        if (Strings.isNullOrEmpty(tag) || Strings.isNullOrEmpty(xsize) || Strings.isNullOrEmpty(ysize) || Strings.isNullOrEmpty(sceneId)) {
            resultObj.put("msg", "失败原因：参数未获取");
            resultObj.put("result", false);
            interfaceLogService.interfaceLogInsert("UWB调用接口", parameter, "失败原因：参数未获取", DateUtils.getTime());
        }

        //通过场景号获取库区编码
        String areaCode = null;
        if ("1".equals(sceneId)){
            areaCode = "KQ0000004,KQ0000005,KQ0000006";
        }else if ("4".equals(sceneId)){
            areaCode = "KQ0000003";
        }else if ("5".equals(sceneId)){
            areaCode = "KQ0000002";
        }else if ("6".equals(sceneId)){
            areaCode = "KQ0000001";
        }

        //获取关于tag的唯一值
        UwbMoveRecord uwbMoveRecord = selectOne(new EntityWrapper<UwbMoveRecord>().eq("tag", tag));
        //若为空则新增，若不为空则更新
        if (uwbMoveRecord == null) {
            uwbMoveRecord = new UwbMoveRecord();
            uwbMoveRecord.setTag(tag);
            uwbMoveRecord.setXsize(xsize);
            uwbMoveRecord.setYsize(ysize);
            uwbMoveRecord.setIsMove(isMove);
            uwbMoveRecord.setSceneCode(areaCode);
            uwbMoveRecord.setCreateTime(DateUtils.getTime());
            if (!this.insert(uwbMoveRecord)) {
                resultObj.put("msg", "失败原因：记录新增失败");
                resultObj.put("result", false);
                interfaceLogService.interfaceLogInsert("UWB调用接口", parameter, "失败原因：记录新增失败", DateUtils.getTime());
            }
        } else {
            int count = 0;
            count = uwbMoveRecordDAO.updateUwb(tag, xsize, ysize, isMove, areaCode, DateUtils.getTime());
            if (count <= 0) {
                resultObj.put("msg", "失败原因：记录更新失败");
                resultObj.put("result", false);
                interfaceLogService.interfaceLogInsert("UWB调用接口", parameter, "失败原因：记录更新失败", DateUtils.getTime());
            }
        }

        interfaceLogService.interfaceLogInsert("UWB调用接口", parameter, "添加成功!", DateUtils.getTime());
        resultObj.put("msg", "添加成功!");
        resultObj.put("result", true);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            interfaceLogService.interfaceLogInsert("调用UWB接口中sleep()方法", "millis:100", "失败原因:UWB移位接口调用sleep()方法异常", DateUtils.getTime());
        }

        return JSON.toJSONString(resultObj);
    }

    /**
     * 通过用户IP获取UWB定位数据
     *
     * @author yuany
     * @date 2019-06-03
     * @param userIp
     * @return
     */
    @Override
    public UwbMoveRecord getUwbByUserIp(String userIp) {
        return uwbMoveRecordDAO.getUwbByUserIp(userIp);
    }


}
