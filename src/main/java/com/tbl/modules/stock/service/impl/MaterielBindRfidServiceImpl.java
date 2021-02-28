package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.DepotArea;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.stock.dao.MaterielBindRfidDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 物料绑定RFID管理service实现
 *
 * @author yuany
 * @date 2019-01-07
 */
@Service("materielBindRfidService")
public class MaterielBindRfidServiceImpl extends ServiceImpl<MaterielBindRfidDAO, MaterielBindRfid> implements MaterielBindRfidService {

    //用户管理Dao
    @Autowired
    private UserDAO userDAO;

    //库位管理Dao
    @Autowired
    private DepotPositionDAO depotPositionDao;

    //库区管理Dao
    @Autowired
    private DepotAreaDAO depotAreaDao;

    //物料绑定RFID详情管理Dao
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDao;

    //物料绑定RFID管理Dao
    @Autowired
    private MaterielBindRfidDAO materielBindRfidDao;


    /**
     * 物料绑定RFID管理分页查询
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-07
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        String rfid = (String) parms.get("rfid");
        //去掉RFID前后空格
        if (StringUtils.isNotBlank(rfid)) {
            rfid = rfid.trim();
        }

        String positionBy = (String) parms.get("positionBy");
        String status = (String) parms.get("status");
        String startTime = (String) parms.get("startTime");
        String endTime = (String) parms.get("endTime");
        String createBy = (String) parms.get("createBy");

        if (StringUtils.isNotBlank(startTime)&& StringUtils.isNotBlank(endTime) && startTime.equals(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
        }

        Page<MaterielBindRfid> rfidPage = this.selectPage(
                new Query<MaterielBindRfid>(parms).getPage(),
                new EntityWrapper<MaterielBindRfid>()
                        .eq("deleted_flag", DyylConstant.NOTDELETED)
                        .eq(StringUtils.isNotBlank(status), "status", status)
                        .eq(StringUtils.isNotBlank(positionBy), "position_by", positionBy)
                        .ge(StringUtils.isNotEmpty(startTime), "create_time", startTime)
                        .le(StringUtils.isNotEmpty(endTime), "create_time", endTime)
                        .like(StringUtils.isNotBlank(rfid), "rfid", rfid)
                        .eq(StringUtils.isNotBlank(createBy), "create_by", createBy)
        );

        //遍历添加
        for (MaterielBindRfid materielBindRfid : rfidPage.getRecords()) {
            if (materielBindRfid.getCreateBy() != null) {
                User user = userDAO.selectById(materielBindRfid.getCreateBy());
                if (user != null) {
                    materielBindRfid.setCreateName(user.getUsername());
                }
            }

            if (materielBindRfid.getPositionBy() != null) {
                DepotPosition depotPosition = depotPositionDao.selectById(materielBindRfid.getPositionBy());
                if (depotPosition != null) {
                    materielBindRfid.setPositionName(depotPosition.getPositionName());
                    if (depotPosition.getDepotareaId() != null) {
                        DepotArea area = depotAreaDao.selectById(depotPosition.getDepotareaId());
                        if (area != null) {
                            materielBindRfid.setAreaName(area.getAreaName());
                        }
                    }
                }
                if (materielBindRfid.getPositionBy().equals(0L)) {
                    materielBindRfid.setPositionName("P-01");
                }
            }
        }

        return new PageUtils(rfidPage);
    }

    /**
     * 删除绑定RFID实体（逻辑删除）
     * 同时删除详情（逻辑删除）
     *
     * @param ids:要删除的id
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2018-01-09
     */
    @Override
    public boolean delMaterielBindRfid(String ids, Long userId) {

        List<Long> lstMaterialIds = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<MaterielBindRfid> lmbr = this.selectBatchIds(lstMaterialIds);
        //遍历物料绑定list
        for (MaterielBindRfid materielBindRfid : lmbr) {
            if (materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED)) {
                materielBindRfid.setDeletedBy(userId);
                materielBindRfid.setDeletedFlag(DyylConstant.DELETED);

                //根据物料绑定ID获取关于此ID的物料详情
                List<MaterielBindRfidDetail> mbrdList = materielBindRfidDetailDao.selectList(
                        new EntityWrapper<MaterielBindRfidDetail>()
                                .eq("materiel_bind_rfid_by", materielBindRfid.getId())
                );

                //遍历物料绑定详情list
                for (MaterielBindRfidDetail materielBindRfidDetail : mbrdList) {
                    materielBindRfidDetail.setDeleteBy(userId);
                    materielBindRfidDetail.setDeleteFlag(DyylConstant.DELETED);
                    materielBindRfidDetailDao.updateById(materielBindRfidDetail);
                }
            } else {
                return false;
            }
        }

        return updateBatchById(lmbr);
    }

    /**
     * 获取绑定编号
     *
     * @return
     * @author yuany
     * @date 2019-01-11
     */
    @Override
    public String getBindCode() {

        //绑定编号
        String bindCode = null;

        //获取绑定表数据集合
        List<MaterielBindRfid> materielBindRfidList = this.selectList(
                new EntityWrapper<>()
        );

        //如果集合为长度为0则为第一条添加的数据
        if (materielBindRfidList.size() == 0) {
            bindCode = "BD0000001";
        } else {
            //获取集合中最后一条数据
            MaterielBindRfid materielBindRfid = materielBindRfidList.get(materielBindRfidList.size() - 1);
            //获取最后一条数据中绑定编码并在数字基础上加1
            Integer number = Integer.valueOf(materielBindRfid.getBindCode().substring(2)) + 1;
            //拼接字符串
            bindCode = "BD000000" + number.toString();
        }
        return bindCode;
    }

    /**
     * 根据rfid获取实体
     *
     * @param rfid
     * @return
     */
    @Override
    public MaterielBindRfid materielBindRfid(String rfid) {
        MaterielBindRfid materielBindRfid = materielBindRfidDao.materielBindRfid(rfid);

        return materielBindRfid;
    }

}
