package com.tbl.modules.erpInterface.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tbl.common.utils.DateUtils;
import com.tbl.common.utils.StringUtils;
import com.tbl.modules.basedata.dao.MaterielDAO;
import com.tbl.modules.basedata.entity.Materiel;
import com.tbl.modules.erpInterface.service.MaterielErpInterfaceService;
import com.tbl.modules.platform.service.system.InterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: dyyl
 * @description: 生成物料信息
 * @author: pz
 * @create: 2019-02-15
 **/
@Service("materielErpInterfaceService")
public class MaterielErpInterfaceServiceImpl implements MaterielErpInterfaceService {

    //物料DAO
    @Autowired
    private MaterielDAO materielDAO;

    //接口日志servive
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 生成物料信息
     *
     * @param
     * @return
     * @author pz
     * @date 2019-02-15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materielInfo(@RequestBody String materielInfo) {

        JSONObject materielInfoObj = JSON.parseObject(materielInfo);

        //获取当前时间
        String nowDate = DateUtils.getTime();

        JSONObject resultObj = new JSONObject();

        //获取物料id
        Long id = materielInfoObj.getLong("id");

        if (id == null) {
            resultObj.put("msg", "id不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        //物料编码
        String materielCode = materielInfoObj.getString("materielCode");
        //物料名称
        String materielName = materielInfoObj.getString("materielName");
        //物料条码
        String barcode = materielInfoObj.getString("barcode");
        //货主id
        Long customerBy = materielInfoObj.getLongValue("customerBy");
        //保质期（天）
        String qualityPeriod = materielInfoObj.getString("qualityPeriod");
        //长
        String length = materielInfoObj.getString("length");
        //宽
        String wide = materielInfoObj.getString("wide");
        //高
        String height = materielInfoObj.getString("height");
        //包装单位
        String unit = materielInfoObj.getString("unit");
        //商品规格
        String spec = materielInfoObj.getString("spec");
        //上架分类
        String upshelfClassify = materielInfoObj.getString("upshelfClassify");
        //拣货分类
        String pickClassify = materielInfoObj.getString("pickClassify");
        //批次规则
        String batchRule = materielInfoObj.getString("batchRule");
        //供应商id
        Long supplierBy = materielInfoObj.getLongValue("supplierBy");
        //生产产商id
        Long producerBy = materielInfoObj.getLongValue("producerBy");
        //体积
        String volume = "";
        if (StringUtils.isNotBlank(length) && StringUtils.isNotBlank(wide) && StringUtils.isNotBlank(height)) {
            volume = String.valueOf(Integer.parseInt(length) * Integer.parseInt(wide) * Integer.parseInt(height));
        }

        if (StringUtils.isEmpty(materielCode)) {
            resultObj.put("msg", "物料编码不能为空！");
            resultObj.put("success", false);
            return JSON.toJSONString(resultObj);
        }

        Materiel mat = materielDAO.selectById(id);
        if (mat == null) {
            EntityWrapper<Materiel> materielEntity = new EntityWrapper<>();
            materielEntity.eq("materiel_code", materielCode);
            int count = materielDAO.selectCount(materielEntity);

            if (count > 0) {
                resultObj.put("msg", "物料编码不能重复！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }

            // 保存物料实体
            boolean materielResult = materielDAO.savaMateriel(id, materielCode, materielName, barcode, customerBy, qualityPeriod,
                    length, wide, height, volume, unit, spec, upshelfClassify, pickClassify, batchRule, supplierBy, producerBy, nowDate);


            if (StringUtils.isEmpty(materielName)) {
                resultObj.put("msg", "物料名称不能为空！");
                resultObj.put("success", false);
                return JSON.toJSONString(resultObj);
            }

            // 判断物料是否添加成功
            if (materielResult) {
                resultObj.put("msg", "物料添加成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("物料调用接口", materielInfoObj.getString("materielCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            } else {
                resultObj.put("msg", "失败原因：“物料添加失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("物料调用接口", materielInfoObj.getString("materielCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        } else {

            mat.setMaterielCode(materielCode);
            mat.setMaterielName(materielName);
            mat.setBarcode(barcode);
            mat.setCustomerBy(customerBy);
            mat.setQualityPeriod(qualityPeriod);
            mat.setLength(length);
            mat.setWide(wide);
            mat.setHeight(height);
            mat.setVolume(volume);
            mat.setUnit(unit);
            mat.setSpec(spec);
            mat.setUpshelfClassify(upshelfClassify);
            mat.setPickClassify(pickClassify);
            mat.setBatchRule(batchRule);
            mat.setSupplierBy(supplierBy);
            mat.setProducerBy(producerBy);
            mat.setUpdateTime(nowDate);
            boolean updMaterlResult = materielDAO.updateById(mat) > 0;
            // 判断物料是否添加成功
            if (updMaterlResult) {
                resultObj.put("msg", "物料修改成功！");
                resultObj.put("success", true);
                interfaceLogService.interfaceLogInsert("物料调用接口", materielInfoObj.getString("materielCode"), resultObj.get("msg").toString(), nowDate);
            } else {
                resultObj.put("msg", "失败原因：“物料修改失败”！");
                resultObj.put("success", false);
                interfaceLogService.interfaceLogInsert("物料调用接口", materielInfoObj.getString("materielCode"), resultObj.get("msg").toString(), nowDate);
                return JSON.toJSONString(resultObj);
            }
        }
        return JSON.toJSONString(resultObj);
    }
}
    