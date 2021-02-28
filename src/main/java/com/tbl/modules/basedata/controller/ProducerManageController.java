package com.tbl.modules.basedata.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.PageTbl;
import com.tbl.common.utils.PageUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.entity.Producer;
import com.tbl.modules.basedata.service.ProducerService;
import com.tbl.modules.platform.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 供应商管理Controller
 *
 * @author yuany
 * @date 2018-12-27
 */
@Controller
@RequestMapping(value = "/producerManage")
public class ProducerManageController extends AbstractController {

    //生产产商管理service
    @Autowired
    private ProducerService producerService;

    /**
     * 跳转到供应商管理页面
     *
     * @return
     * @author pz
     * @date 2019-01-11
     */
    @RequestMapping(value = "/toList.do")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("techbloom/basedata/producerManage/producerManage_list");
        return mv;
    }

    /**
     * 获取供应商列表数据
     *
     * @return
     * @author pz
     * @date 2019-01-11
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Object getList(String queryJsonString) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(queryJsonString)) {
            map = JSON.parseObject(queryJsonString);
        }
        PageTbl page = this.getPage();
        map.put("page", page.getPageno());
        map.put("limit", page.getPagesize());
        String sortName = page.getSortname();
        if (StringUtils.isEmptyString(sortName)) {
            sortName = "id";
            page.setSortname(sortName);
        }
        String sortOrder = page.getSortorder();
        if (StringUtils.isEmptyString(sortOrder)) {
            sortOrder = "asc";
            page.setSortorder(sortOrder);
        }
        map.put("sidx", page.getSortname());
        map.put("order", page.getSortorder());
        PageUtils producer = producerService.queryPageS(map);
        page.setTotalRows(producer.getTotalCount() == 0 ? 1 : producer.getTotalCount());
        map.put("rows", producer.getList());
        executePageMap(map, page);
        return map;
    }

    /**
     * 跳转到弹出的添加/编辑页面
     *
     * @return
     * @author pz
     * @date 2019-01-11
     */
    @RequestMapping(value = "/toEdit")
    @ResponseBody
    public ModelAndView toEdit(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/producerManage/producerManage_edit");
        Producer producer = null;
        int status = 1;

        //修改
        if (id != -1) {
            producer = producerService.selectById(id);
            status = 2;
        }

        mv.addObject("status", status);
        mv.addObject("producer", producer);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 跳转到弹出的详情页面
     *
     * @return
     * @author anss
     * @date 2019-3-4
     */
    @RequestMapping(value = "/toDetail")
    @ResponseBody
    public ModelAndView toDetail(Long id, Long sign) {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("techbloom/basedata/producerManage/producerManage_detail");
        Producer producer = producerService.selectById(id);

        mv.addObject("status", 2);
        mv.addObject("producer", producer);
        mv.addObject("edit", 1);
        return mv;
    }

    /**
     * 判断产商编码是否存在
     *
     * @author pz
     * @date 2019-01-11
     */
    @RequestMapping(value = "/hasC")
    @ResponseBody
    public boolean hasC(String producerCode, Long id) {
        boolean flag = true;
        // 根据producerCode查询实体list
        EntityWrapper<Producer> wraProducer = new EntityWrapper<>();
        wraProducer.eq("producer_code", producerCode);
        try {
            if (id != null) {
                int count = producerService.selectCount(wraProducer);
                if (count > 1) {
                    flag = false;
                }
            } else {
                int count = producerService.selectCount(wraProducer);
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
     * 添加/修改产商
     *
     * @return
     * @author pz
     * @date 2018-12-11
     */
    @RequestMapping(value = "/addProducer")
    @ResponseBody
    public boolean addProducer(Producer producer) {
        boolean result = false;

        //当前时间
        String nowTime = DateUtils.getTime();

        if (producer == null) {
            return result;
        }
        //修改
        if (producer.getId() != null) {
            producer.setUpdateTime(nowTime);
            result = producerService.updateById(producer);
            //新增
        } else {
            producer.setCreateTime(nowTime);
            producer.setUpdateTime(nowTime);
            result = producerService.insert(producer);
        }
        return result;
    }

    /**
     * 删除产商
     * @author pz
     * @date 2018-12-10
     * @return
     */
    @RequestMapping(value = "/delProducer")
    @ResponseBody
    public boolean delProducer(String ids){
        if (StringUtils.isEmpty(ids)) {
            return false;
        }
        return producerService.delLstProducer(ids, getUserId());
    }

    /**
     * 导出Excel
     *
     * @author pz
     * @date 2019-01-11
     */
    @RequestMapping(value = "/toExcel")
    @ResponseBody
    public void producerExcel(String ids) {
        List<Producer> list = producerService.getAllListP(ids);
        producerService.toExcel(response, "", list);
    }



}