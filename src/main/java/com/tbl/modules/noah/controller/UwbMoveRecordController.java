package com.tbl.modules.noah.controller;

import com.tbl.common.utils.DateUtils;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import com.tbl.modules.noah.service.UwbMoveRecordService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UWB移位记录接口
 *
 * @author yuany
 * @date 2019-03-04
 */

@Controller
@RequestMapping(value = "/uwbMoveRecord")
public class UwbMoveRecordController {

    /**
     * UWB移位记录Service
     */
    @Autowired
    private UwbMoveRecordService uwbMoveRecordService;


    /**
     * 添加UWB移位记录接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addUWBmoveRecord", method = RequestMethod.POST)
    @ResponseBody
    public String addUWBmoveRecord(@RequestBody String uwbMoveRecordInfo) {

        return uwbMoveRecordService.addUwbMoveRecord(uwbMoveRecordInfo);
    }

}
