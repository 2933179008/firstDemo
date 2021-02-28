package com.tbl.modules.noah.service.impl;

import com.alibaba.fastjson.JSON;
import com.tbl.common.utils.EncodeUtil;
import com.tbl.common.utils.HttpUtil;
import com.tbl.modules.alarm.service.AlarmService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.noah.service.ScanGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lcg
 * data 2019/3/26
 */
@Service("scanGateService")
public class ScanGateServiceImpl implements ScanGateService {

    @Value("${noah.ScanGateURL}")
    private String ScanGateURL;

    @Override
    public Object scanGate(String ip) {
        String url = ScanGateURL + ip + "/" + 0 + "/" + 1 + "/" + 1;
        Map<String, Object> responseMap = HttpUtil.postJSONWithResponseHeaders(url, null, null);
        return null;
    }
}
