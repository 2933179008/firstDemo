package com.tbl.modules.stock.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Strings;
import com.tbl.common.utils.*;
import com.tbl.modules.basedata.entity.DepotPosition;
import com.tbl.modules.basedata.service.DepotAreaService;
import com.tbl.modules.basedata.service.DepotPositionService;
import com.tbl.modules.constant.DyylConstant;
import com.tbl.modules.instorage.service.ReceiptDetailService;
import com.tbl.modules.platform.controller.AbstractController;
import com.tbl.modules.platform.entity.system.User;
import com.tbl.modules.platform.service.system.UserService;
import com.tbl.modules.stock.entity.MaterielBindRfid;
import com.tbl.modules.stock.entity.MaterielBindRfidDetail;
import com.tbl.modules.stock.entity.Tray;
import com.tbl.modules.stock.service.MaterielBindRfidDetailService;
import com.tbl.modules.stock.service.MaterielBindRfidService;
import com.tbl.modules.stock.service.TrayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dyyl
 * @description: 物料绑定rfid控制类
 * @author: yuany
 * @create: 2019-01-08
 **/
@Controller
@RequestMapping(value = "/materielBindRfid")
public class MaterielBindRfidController extends AbstractController {

    //物料绑定RFID管理service
    @Autowired
    private MaterielBindRfidService materielBindRfidService;

    //物料绑定RFID详情管理service
    @Autowired
    private MaterielBindRfidDetailService materielBindRfidDetailService;

    //库位管理service
    @Autowired
    private DepotPositionService depotPositionService;

    //库区管理service
    @Autowired
    private DepotAreaService depotAreaService;

    //收获计划详情service
    @Autowired
    private ReceiptDetailService receiptDetailService;

    //托盘管理service
    @Autowired
    private TrayService trayService;

    //用户管理service
    @Autowired
    private UserService userService;


    /**
     * @Description: 跳转到物料绑定rfid列表页面
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-03
     */
    @RequestMapping(value = "/toView")
    public ModelAndView toView() {
        ModelAndView mv = new ModelAndView();

        //获取库位集合
        List<DepotPosition> depotPositionList = depotPositionService.selectList(
                new EntityWrapper<>()
        );
        for (DepotPosition depotPosition : depotPositionList) {
            depotPosition.setAreaName(depotAreaService.selectById(depotPosition.getDepotareaId()).getAreaName());
        }

        //获取库位集合
        List<User> userList = userService.selectList(
                new EntityWrapper<>()
        );

        mv.addObject("depotPositionList", depotPositionList);
        mv.addObject("userList", userList);
        mv.setViewName("techbloom/stock/materielBindRfid_list");
        return mv;
    }


    /**
     * 获取物料绑定RFID列表数据
     *
     * @param queryJsonString
     * @return Map
     * @author yuany
     * @date 2019-01-07
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Map<String, Object> listLog(String queryJsonString) {

        Map<String, Object> map = new HashMap<>();
        if (!com.tbl.common.utils.StringUtils.isEmpty(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortName)) {
            sortName = "createTime";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (com.tbl.common.utils.StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "desc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils utils = materielBindRfidService.queryPage(map);
        page.setTotalRows(utils.getTotalCount() == 0 ? 1 : utils.getTotalCount());
        map.put("rows", utils.getList());
        executePageMap(map, page);
        return map;
    }


    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @return
     * @author yuany
     * @date 2019-01-03
     */
    @RequestMapping(value = "/toEdit.do")
    @ResponseBody
    public ModelAndView toEdit(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();

        MaterielBindRfid materielBindRfid = null;

        //判断是否为编辑页面
        if (id != -1 || sign == 1) {

            //根据id获取一条绑定数据，根据绑定的RFID获取符合条件的物料结合
            materielBindRfid = materielBindRfidService.selectById(id);

            //获取物料绑定详情
            List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                    new EntityWrapper<MaterielBindRfidDetail>().
                            eq("materiel_bind_rfid_by", materielBindRfid.getId())
            );
            mv.addObject("materielBindRfid", materielBindRfid);
            mv.addObject("materielBindRfidDetailList", materielBindRfidDetailList);
            mv.setViewName("techbloom/stock/materielBindRfid_edit");

        } else {
            //自动生成绑定编码
            String bindCode = materielBindRfidService.getBindCode();

            mv.addObject("bindCode", bindCode);
            mv.setViewName("techbloom/stock/materielBindRfid_edit");
        }

        return mv;
    }

    /**
     * 跳转到弹出的查看页面
     *
     * @return
     * @author anss
     * @date 2019-3-5
     */
    @RequestMapping(value = "/toDetail.do")
    @ResponseBody
    public ModelAndView toDetail(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();

        MaterielBindRfid materielBindRfid = null;

        //根据id获取一条绑定数据，根据绑定的RFID获取符合条件的物料结合
        materielBindRfid = materielBindRfidService.selectById(id);

        //获取物料绑定详情
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                new EntityWrapper<MaterielBindRfidDetail>().
                        eq("materiel_bind_rfid_by", materielBindRfid.getId())
        );
        mv.addObject("materielBindRfid", materielBindRfid);
        mv.addObject("materielBindRfidDetailList", materielBindRfidDetailList);

        DepotPosition depotPosition = depotPositionService.selectById(materielBindRfid.getPositionBy());
        if (depotPosition != null) {
            depotPosition.setAreaName(depotAreaService.selectById(depotPosition.getDepotareaId()).getAreaName());
        }

        mv.addObject("depotPosition", depotPosition);
        mv.setViewName("techbloom/stock/materielBindRfid_detail");
        return mv;
    }

    /**
     * 判断绑定RFID编码是否存在
     *
     * @author yuany
     * @date 2019-01-10
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String rfid, Long id) {
        boolean flag = true;
        // 根据rfid查询实体list
        EntityWrapper<MaterielBindRfid> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("rfid", rfid);
        entityWrapper.eq("deleted_flag", DyylConstant.NOTDELETED);

//        EntityWrapper<Tray> trayEntityWrapper = new EntityWrapper<>();
//        trayEntityWrapper.eq("rfid", rfid);
//        trayEntityWrapper.eq("delete_flag", DyylConstant.NOTDELETED);

        try {

//            if (trayService.selectCount(trayEntityWrapper) > 0) {
//                flag = false;
//            }
            //？？？？
            if (id != null) {
                int count = materielBindRfidService.selectCount(entityWrapper);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = materielBindRfidService.selectCount(entityWrapper);
                if (count > 0) {
                    flag = false;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return flag;
    }

    /**
     * 保存/修改物料绑定
     *
     * @return
     * @author yuany
     * @date 2019-01-09
     */
    @RequestMapping(value = "/saveMaterielBindRfid")
    @ResponseBody
    public Map<String, Object> saveMaterielBindRfid(MaterielBindRfid materielBindRfid) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        //获取当前时间并格式化
        String time = DateUtils.getTime();

        if (materielBindRfid == null) {
            map.put("result", result);
            return map;
        }

        //设为未删除状态
        materielBindRfid.setDeletedFlag(DyylConstant.NOTDELETED);

        //若ID为空则位添加，否则为修改
        if (materielBindRfid.getId() == null) {
            //添加创建人和创建时间
            materielBindRfid.setCreateBy(this.getUserId());
            materielBindRfid.setCreateTime(time);
            materielBindRfid.setStatus(DyylConstant.STATE_PROCESSED);
            materielBindRfid.setInstorageProcess(DyylConstant.INSTORAGEPROCESS0);
            List<MaterielBindRfid> materielBindRfidList = materielBindRfidService.selectList(
                    new EntityWrapper<MaterielBindRfid>()
                            .eq("bind_code", materielBindRfid.getBindCode())
            );
            //判断自动生成的bindCode与物料绑定集合中是否有重复
            if (materielBindRfidList.size() != 0) {
                materielBindRfid.setBindCode(materielBindRfidService.getBindCode());
            }
            result = materielBindRfidService.insert(materielBindRfid);
        } else {
            //添加修改人和修改时间
            materielBindRfid.setUpdateBy(this.getUserId());
            materielBindRfid.setUpdateTime(time);
            result = materielBindRfidService.updateById(materielBindRfid);
        }
        map.put("materielBindRfidBy", materielBindRfid.getId());
        map.put("result", result);
        return map;
    }

    /**
     * @Description: 获取物料下拉列表数据源
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-13
     */
    @RequestMapping(value = "/getSelectMaterial")
    @ResponseBody
    public Map<String, Object> getSelectMaterial(String queryString, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> materialList = receiptDetailService.getSelectMaterialList(queryString, pageSize, pageNo);
        map.put("result", materialList);
        map.put("total", receiptDetailService.getSelectMaterialTotal(queryString));
        return map;

    }

    /**
     * @Description: 保存(物料详情)
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-14
     */
    @RequestMapping(value = "/saveMaterielBindRfidDetail")
    @ResponseBody
    public Map<String, Object> saveMaterielBindRfidDetail(Long materielBindRfidBy, String materielCodes) {
        Map<String, Object> map = new HashMap<>();
        boolean ret = true;
        boolean result = false;
        String msg = "";
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectList(
                new EntityWrapper<MaterielBindRfidDetail>()
                        .eq("delete_flag", DyylConstant.NOTDELETED)
                        .eq("materiel_bind_rfid_by", materielBindRfidBy)
        );
        List<String> lstmaterielCode = Arrays.asList(materielCodes.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());

        outer:
        for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
            for (String materielCode : lstmaterielCode) {
                if (materielBindRfidDetail.getMaterielCode().equals(materielCode)) {
                    ret = false;
                    result = true;
                    break outer;
                }
            }
        }
        if (ret) {
            ret = materielBindRfidDetailService.saveReceiptDetail(materielBindRfidBy, materielCodes);
        }

        if (ret) {
            msg = "添加成功！";
        } else {
            if (result) {
                msg = "添加失败！已存在添加的物料";
            } else {
                msg = "添加失败！";
            }
        }

        map.put("result", ret);
        map.put("msg", msg);
        return map;
    }

    /**
     * 获取绑定单详情列表
     *
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-11
     */
    @RequestMapping(value = "/getDetailList")
    @ResponseBody
    public Map<String, Object> getDetailList(String materielBindRfidBy, String status) {
        Map<String, Object> map = new HashMap<>();
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        map.put("materielBindRfidBy", materielBindRfidBy);
        PageUtils PagePlatform = materielBindRfidDetailService.queryPage(map);
        if (PagePlatform != null) {
            page.setTotalRows(PagePlatform.getTotalCount() == 0 ? 1 : PagePlatform.getTotalCount());
            map.put("rows", PagePlatform.getList());
        } else {
            page.setTotalRows(0);
            map.put("rows", null);
        }

        executePageMap(map, page);
        return map;
    }

    /**
     * @Description: 绑定物料详情数量
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-13
     */
    @RequestMapping(value = "/updateAmount")
    @ResponseBody
    public Map<String, Object> updateAmount(String amount, Long materielBindRfidDetailBy) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;

        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(materielBindRfidDetailBy);
        if (materielBindRfidDetail == null && materielBindRfidDetail.getMaterielBindRfidBy()==null ) {
            map.put("result", false);
            map.put("msg", "ID未获取对应物料绑定详情");
            return map;
        }
        if (StringUtils.isEmpty(amount)){
            map.put("result", false);
            map.put("msg", "输入数量不能为空！");
            return map;
        }

        MaterielBindRfid materielBindRfid = materielBindRfidService.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
        if (materielBindRfid == null){
            map.put("result", false);
            map.put("msg", "未获取对应物料绑定信息");
            return map;
        }

        if (materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED) && materielBindRfid.getInstorageProcess().equals(DyylConstant.INSTORAGEPROCESS0)) {
            int count = materielBindRfidDetailService.updateAmount(amount, materielBindRfidDetailBy);
            if (count > 0) {
                result = true;
                map.put("result", result);
            }
        } else {
            map = materielBindRfidDetailService.sugarBindAmount(materielBindRfid.getRfid(), materielBindRfidDetail.getMaterielCode(),amount);
        }
        return map;
    }

    /**
     * @Description: 绑定物料详情重量
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-28
     */
    @RequestMapping(value = "/updateWeight")
    @ResponseBody
    public Map<String, Object> updateWeight(String weight, Long materielBindRfidDetailBy) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        String msg = "";

        MaterielBindRfidDetail materielBindRfidDetail = materielBindRfidDetailService.selectById(materielBindRfidDetailBy);
        if (materielBindRfidDetail == null && materielBindRfidDetail.getMaterielBindRfidBy()==null ) {
            map.put("result", false);
            map.put("msg", "ID未获取对应物料绑定详情");
            return map;
        }
        if (Strings.isNullOrEmpty(materielBindRfidDetail.getAmount())){
            map.put("result", false);
            map.put("msg", "请先输入数量,再输入重量！");
            return map;
        }

        MaterielBindRfid materielBindRfid = materielBindRfidService.selectById(materielBindRfidDetail.getMaterielBindRfidBy());
        if (materielBindRfid == null){
            map.put("result", false);
            map.put("msg", "未获取对应物料绑定信息");
            return map;
        }

        if (materielBindRfid.getStatus().equals(DyylConstant.STATE_PROCESSED) && materielBindRfid.getInstorageProcess().equals(DyylConstant.INSTORAGEPROCESS0)) {
            int count = materielBindRfidDetailService.updateWeight(weight, materielBindRfidDetailBy);
            if (count > 0) {
                result = true;
                map.put("result", result);
            }
        } else {
            map = materielBindRfidDetailService.sugarBind(materielBindRfid.getRfid(), materielBindRfidDetail.getMaterielCode(), materielBindRfidDetail.getAmount(), weight,getUserId());
        }

        return map;
    }

    /**
     * @Description: 删除物料详情
     * @Param:
     * @return:
     * @Author: yuany
     * @Date: 2019-01-14
     */
    @RequestMapping(value = "/deleteMaterielBindRfidDetail")
    @ResponseBody
    public Map<String, Object> deleteMaterielBindRfidDetail(String ids) {
        boolean result = false;
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(ids)) {
            result = materielBindRfidDetailService.deleteMaterielBindRfidDetail(ids);
        }
        map.put("result", result);
        return map;
    }

    /**
     * 逻辑删除物料绑定RFID数据
     *
     * @return
     * @author yuany
     * @date 2019-01-10
     */
    @RequestMapping(value = "/delMaterielBindRfid")
    @ResponseBody
    public boolean delMaterielBindRfid(String ids) {

        //如果ids为空，则返回false
        if (StringUtils.isEmpty(ids)) {
            return false;
        }

        return materielBindRfidService.delMaterielBindRfid(ids, getUserId());
    }

    /**
     * 入库单详情二维码打印
     *
     * @return
     * @author yuany
     * @date 2019-04-01
     */
    @RequestMapping(value = "/matrixCode")
    @ResponseBody
    public ModelAndView tomatrixCode(String ids) {

        ModelAndView mv = this.getModelAndView();

        List<Long> lstid = Arrays.stream(ids.split(",")).map(a -> Long.parseLong(a)).collect(Collectors.toList());
        //根据绑定单详情ID获取需要生成二维码的信息
        List<MaterielBindRfidDetail> materielBindRfidDetailList = materielBindRfidDetailService.selectBatchIds(lstid);

        //存储数据的集合
        List<Map<String, Object>> matrixCodeList = new ArrayList<>();

        //判断根据id获取的入库单详情集合是否为空
        if (!materielBindRfidDetailList.isEmpty()) {
            for (MaterielBindRfidDetail materielBindRfidDetail : materielBindRfidDetailList) {
                Map<String, Object> map = new HashMap<>();
                //二维码内容/宽/高/
                String text = materielBindRfidDetail.getMaterielCode() + "," + materielBindRfidDetail.getMaterielName() + "," + materielBindRfidDetail.getBatchRule() + ","
                        + materielBindRfidDetail.getAmount() + materielBindRfidDetail.getUnit() + "/" + materielBindRfidDetail.getWeight() + "kg/"
                        +materielBindRfidDetail.getProductData();
                String binary =
                        QrCodeUtils.creatRrCode(text, 400, 400);
                map.put("binary", binary);
                map.put("materielCode", materielBindRfidDetail.getMaterielCode());
                map.put("materielName", materielBindRfidDetail.getMaterielName());
                map.put("batchNo", materielBindRfidDetail.getBatchRule());
                map.put("amount", materielBindRfidDetail.getAmount() + materielBindRfidDetail.getUnit());
                map.put("weight", materielBindRfidDetail.getWeight() + "kg");
                map.put("productData",materielBindRfidDetail.getProductData());
                matrixCodeList.add(map);
            }
        }

        //放入页面数据
        mv.addObject("matrixCodeList", matrixCodeList);
        //跳转地址
        mv.setViewName("techbloom/stock/materielBindRfid_matrixCode");

        return mv;
    }
}
    