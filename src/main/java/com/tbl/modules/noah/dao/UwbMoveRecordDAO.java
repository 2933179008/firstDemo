package com.tbl.modules.noah.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tbl.modules.noah.entity.UwbMoveRecord;
import org.apache.ibatis.annotations.Param;

/**
 * UWB移动记录DAO
 *
 * @author yuany
 * @date 2019-03-04
 */
public interface UwbMoveRecordDAO extends BaseMapper<UwbMoveRecord> {

    Integer updateUwb(@Param("tag") String tag, @Param("xsize") String xsize,
                            @Param("ysize") String ysize, @Param("isMove") String isMove,
                            @Param("sceneCode") String sceneCode,@Param("creatTime")String creatTime);

    UwbMoveRecord getUwbByUserIp(@Param("userIp")String userIp);


}
