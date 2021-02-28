package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.Query;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.DepotAreaDAO;
import com.tbl.modules.basedata.dao.DepotPositionDAO;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.platform.dao.system.UserDAO;
import com.tbl.modules.stock.dao.MaterielBindRfidDetailDAO;
import com.tbl.modules.stock.dao.TrayDAO;
import com.tbl.modules.stock.dao.TrayDetailDAO;
import com.tbl.modules.stock.entity.Tray;
import com.tbl.modules.stock.entity.TrayDetail;
import com.tbl.modules.stock.service.TrayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 托盘管理Service实现
 *
 * @author yuany
 * @date 2019-01-15
 */

@Service("trayService")
public class TrayServiceImpl extends ServiceImpl<TrayDAO, Tray> implements TrayService {

    //库位DAO
    @Autowired
    private DepotPositionDAO depotPositionDao;

    //库区DAO
    @Autowired
    private DepotAreaDAO depotAreaDao;

    //托盘管理详情Dao
    @Autowired
    private TrayDetailDAO trayDetailDao;

    //物料绑定详情Dao
    @Autowired
    private MaterielBindRfidDetailDAO materielBindRfidDetailDao;

    //用户DAO
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TrayDAO trayDAO;


    /**
     * 获取托盘管理分页列表
     *
     * @param parms
     * @return
     * @author yuany
     * @date 2019-01-15
     */
    @Override
    public PageUtils queryPage(Map<String, Object> parms) {

        //获取查询条件，不为空时 去掉前后空格
        String mergeOrSplitCode = (String) parms.get("mergeOrSplitCode");
        if (StringUtils.isNotBlank(mergeOrSplitCode)) {
            mergeOrSplitCode = mergeOrSplitCode.trim();
        }
        String type = (String) parms.get("type");

        String status = (String) parms.get("status");

        Page<Tray> trayPage = this.selectPage(
                new Query<Tray>(parms).getPage(),
                new EntityWrapper<Tray>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .like(StringUtils.isNotBlank(mergeOrSplitCode), "merge_or_split_code", mergeOrSplitCode)
                        .eq(StringUtils.isNotBlank(type), "type", type)
                        .eq(StringUtils.isNotBlank(status), "status", status)
        );

        for (Tray tray : trayPage.getRecords()) {
            if (tray.getPositionBy() != null) {
                DepotPosition depotPosition = depotPositionDao.selectById(tray.getPositionBy());
                if (depotPosition != null) {
                    tray.setPositionName(depotPosition.getPositionName());
                }
            }

            tray.setCreateName(userDAO.selectById(tray.getCreateBy()).getUsername());
        }

        return new PageUtils(trayPage);
    }

    /**
     * 自动生成拆分/合并编码
     *
     * @return
     */
    @Override
    public String getMergeOrSplitCode(String type) {
        //合并/拆分编号
        String mergeOrSplitCode = null;

        //合并编号开头字符
        if (Integer.parseInt(type) == 0) {
            mergeOrSplitCode = DyylConstant.MEGRGE_CODE;
        } else if (Integer.parseInt(type) == 1) {
            //拆分编号开头字符
            mergeOrSplitCode = DyylConstant.SPLIT_CODE;
        }

        List<Tray> trayList = new ArrayList<>();
        //若编号不为空则根据编号开头字符获取集合
        if (StringUtils.isNotBlank(mergeOrSplitCode)) {
            //获取合并单数据集合
            trayList = this.selectList(
                    new EntityWrapper<Tray>()
                            .like("merge_or_split_code", mergeOrSplitCode)
                            .orderBy(true, "id")
            );
        }

        //如果集合为长度为0则为第一条添加的数据
        if (trayList.size() == 0) {
            mergeOrSplitCode = mergeOrSplitCode + 1;
        } else {
            //获取集合中最后一条数据
            Tray tray = trayList.get(trayList.size() - 1);
            //获取最后一条数据中拆分/合并编码并在数字基础上加1
            Integer number = Integer.valueOf(tray.getMergeOrSplitCode().substring(2)) + 1;
            //拼接字符串
            mergeOrSplitCode = mergeOrSplitCode + number.toString();
        }
        return mergeOrSplitCode;
    }

    /**
     * 删除绑定RFID实体（逻辑删除）
     * 同时删除详情（逻辑删除）
     *
     * @param ids:要删除的id
     * @param userId：当前登陆人Id
     * @return
     * @author yuany
     * @date 2018-01-18
     */
    @Override
    public boolean delTray(String ids, Long userId) {

        List<Long> idList = Arrays.asList(ids.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Tray> trayList = this.selectBatchIds(idList);

        //遍历物料绑定list
        for (Tray tray : trayList) {
            //根据托盘管理ID获取关于此ID的托盘管理详情
            List<TrayDetail> trayDetailList = trayDetailDao.selectList(
                    new EntityWrapper<TrayDetail>()
                            .eq("tray_by", tray.getId())
            );
            //遍历托盘管理详情list
            for (TrayDetail trayDetail : trayDetailList) {
                trayDetailDao.deleteById(trayDetail);
            }
            Integer count = trayDAO.deleteById(tray);
            if (count<=0){
                return false;
            }
        }

        return true;
    }
}
