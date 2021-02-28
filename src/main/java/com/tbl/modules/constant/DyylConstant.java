package com.tbl.modules.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 *
 * @author anss
 * @date 2019-01-02
 */
public final class DyylConstant {


    /**********************全局***************************/
    /**
     * 删除标记（已删除）
     */
    public static final String DELETED = "0";
    /**
     * 删除标记（未删除）
     */
    public static final String NOTDELETED = "1";

    /**
     * 收货单号自动补全格式
     */
    public static final String RECEIPT_CODE_FORMAT = "AS00000000";

    /**
     * 入库单号自动补全格式
     */
    public static final String INSTORAGE_CODE_FORMAT = "IN00000000";

    /**
     * 上架单号自动补全格式
     */
    public static final String PUTBILL_CODE_FORMAT = "PU00000000";

    /**
     * 质检单号自动补全格式
     */
    public static final String QUALITY_CODE_FORMAT = "ZJ00000000";

    /**
     * 盘点单号自动补全格式
     */
    public static final String INVENTORY_CODE_FORMAT = "PD00000000";

    /**
     * 变动单据编号自动补全格式
     */
    public static final String CHANGE_CODE_FORMAT = "PC00000000";

    /**
     * 盘点任务单号自动补全格式
     */
    public static final String INVENTASK_CODE_FORMAT = "PT00000000";

    /**
     * 盘点任务bom单号自动补全格式
     */
    public static final String bom_CODE_FORMAT = "BM00000000";


    /***************托盘管理 类型******************/
    /**
     * 合并
     */
    public static final String MERGE = "0";

    /**
     * 拆分
     */
    public static final String SPLIT = "1";

    /**
     * 合并编号开头字符
     */
    public static final String MEGRGE_CODE = "MG000000";

    /**
     * 拆分编号开头字符
     */
    public static final String SPLIT_CODE = "SL000000";

    /**
     * 出库单号自动不全格式
     */
    public static final String OUTORAGE_CODE_FORMAT = "OU00000000";

    public static final String XJ_CODE_FORMAT = "XJ00000000";

    public static final String BL_CODE_FORMAT = "PBOM000000";

    /**************** 移动库位 状态****************/

    /**
     * 状态(0:未移位，1：移位中，2：已完成)
     * 盘点详情状态（0：未处理；1：处理中；2：已完成）
     */
    public static final String STATUS_NO = "0";

    public static final String STATUS_IN = "1";

    public static final String STATUS_OVER = "2";

    /********************* 库存变动 业务类型****************/
    /**
     * 业务类型（0：入库；1：出库；2：移位-出库；3：移位-入库；4：盘点报溢；5：盘点报损;6：拆分入库；7：拆分出库;8:合并入库;9:合并出库;10:货权转移出库;11:货权转移入库;12:越库入库;13:越库出库）
     */
    public static final String DEPOT_IN = "0";

    public static final String DEPOT_OUT = "1";

    public static final String MOVE_OUT = "2";

    public static final String MOVE_IN = "3";

    public static final String MANG_DEPOT_IN = "4";

    public static final String LESS_DEPOT_OUT = "5";

    public static final String SPLIT_IN = "6";

    public static final String SPLIT_OUT = "7";

    public static final String MG_IN = "8";

    public static final String MG_OUT = "9";

    public static final String DROIT_SHIFT_OUT = "10";

    public static final String DROIT_SHIFT_IN = "11";

    public static final String CROSSDOCKING_IN = "12";

    public static final String CROSSDOCKING__OUT = "13";

    /*********************** 预警 /入库状态**********************/
    /**
     * 状态（0：未处理；1：已处理）
     * （0：未入库；1：已入库）
     */
    public static final String STATE_PROCESSED = "0";

    public static final String STATE_UNTREATED = "1";

    /**
     * 类型（0：BOM差异预警；1：质检超时预警；2：RFID未绑定预警；3：叉车出库预警；4：扫描门出库预警；5：库存到期预警；6：库存差异预警；）
     */
    public static final String ALARM_BOM = "0";

    public static final String ALARM_TIME = "1";

    public static final String ALARM_RFID = "2";

    public static final String ALARM_GOODS = "3";

    public static final String ALARM_OUT_STOCK = "4";

    public static final String ALARM_LIBRARY_AGE = "5";

    public static final String ALARM_INVENTORY_VARIANCE = "6";

    public static final Map<String, Object> AlARM_TYPE() {
        Map<String, Object> map = new HashMap<>();
        map.put("0", "BOM差异预警");
        map.put("1", "质检超时预警");
        map.put("2", "RFID未绑定预警");
//        map.put("3", "叉车取货预警");
        map.put("4", "扫描门出库预警");
        map.put("5", "库存到期预警");
        map.put("6", "库存差异预警");

        return map;
    }

    /**
     * 状态（0：未提交；1：待进行；2：进行中；3：已完成；4：强制完成；5：超收）
     */
    public static final String STATE_UNCOMMIT = "0";
    public static final String STATE_WAIT = "1";
    public static final String STATE_HARVEST = "2";
    public static final String STATE_COMPLETED = "3";
    public static final String STATE_FORCE = "4";
    public static final String STATE_EXCEED = "5";

    /**
     * 状态（0：未上架；1：已上架）
     */
    public static final String STATE_OFF = "0";
    public static final String STATE_ON = "1";

    /**
     * 单据类型（0：自采；1：客供）
     * 入库类型（0：采购入库；1：委托加工入库；）
     */
    public static final String SELFMINING = "0";
    public static final String CUSTOMERSUPPLY = "1";

    /**
     * 冻结状态（0.未冻结 1.冻结）
     */
    public static final String FROZEN_0 = "0";
    public static final String FROZEN_1 = "1";

    /**
     * 拆分/合并类型（0.合并 1.拆分）
     */
    public static final String TYPE_MG = "0";
    public static final String TYPE_SL = "1";

    /**
     * 盘点任务状态（0：未提交；1：待盘点；2：盘点中；3： 待审核；4：已审核；5：复盘中）
     */
    public static final String INVENTORY_TASK0 = "0";

    public static final String INVENTORY_TASK1 = "1";

    public static final String INVENTORY_TASK2 = "2";

    public static final String INVENTORY_TASK3 = "3";

    public static final String INVENTORY_TASK4 = "4";

    public static final String INVENTORY_TASK5 = "5";

    /**
     * 盘点任务详情状态（0：未盘点；1：已盘点；2：已审核；3：复盘中）
     */
    public static final String INVENTORY_TASKDETAIL0 = "0";

    public static final String INVENTORY_TASKDETAIL1 = "1";

    public static final String INVENTORY_TASKDETAIL2 = "2";

    public static final String INVENTORY_TASKDETAIL3 = "3";

    /**
     * 盘点任务类型（0：库位盘点；1：动碰盘点）
     */
    public static final String INVENTORYTYPE_0 = "0";

    public static final String INVENTORYTYPE_1 = "1";

    /**
     * 提交状态（0：未提交；1：已提交）
     */
    public static final String INVENTORY_0 = "0";

    public static final String INVENTORY_1 = "1";


    /*************库位类型/混放类型*********************/
    //库位类型（0：地堆；1：货架；2：不良品；3：暂存；）
    //混放类型（ 0表示能混放 1表示不能混放）
    public static final String POSITION_TYPE0 = "0";

    public static final String POSITION_TYPE1 = "1";

    public static final String POSITION_TYPE2 = "2";

    public static final String POSITION_TYPE3 = "3";

    public static final String BLEND_TYPE0 = "0";

    public static final String BLEND_TYPE1 = "1";


    /******************预警设置 状态********************/
    //状态（0：禁用；1：启用）
    public static final String PROHIBIT = "0";

    public static final String ELABLE = "1";

    //发送方式（0：短信；1：邮件；0，1：短信&邮件）
    public static final String SENDTYPECODE = "0";

    public static final String SENDTYPEEMAIL = "1";

    public static final String SENDTYPECODEANDEmail = "0,1";

    /******************库位冻结 状态********************/
    //状态（0：未冻结；1：已冻结）
    public static final String POSITION_FROZEN0 = "0";

    public static final String POSITION_FROZEN1 = "1";

    /******************库位占用 状态（0：未占用；1：已占用）********************/
    //0：未占用；
    public static final String POSITION_UNOCCUPIED = "0";
    //1：已占用
    public static final String POSITION_OCCUPIED = "1";

    /******************货物类型 状态（0：无RFID；1：有RFID）********************/
    //0：无RFID；
    public static final String MATERIAL_NORFID = "0";
    //1：有RFID
    public static final String MATERIAL_RFID = "1";

    /******************移位类型（0：散货移位；1：整货移位）********************/
    public static final String MOVEPOSITIONTYPE_NOCARGO = "0";

    public static final String MOVEPOSITIONTYPE_CARGO = "1";


    /******************出库类型(0:生产领料出库,1:退货出库,2:越库出库,3:内部领料出库,4:报损出库,5:盘亏出库,6:其他出库)********************/
    public static final Long OUTSTORAGE_TYPE0 = Long.parseLong("0");
    public static final Long OUTSTORAGE_TYPE1 =  Long.parseLong("1");
    public static final Long OUTSTORAGE_TYPE2 =  Long.parseLong("2");


    /*************************入库流程（0：一般流程；1：白糖流程）********************************/
    public static final String INSTORAGEPROCESS0 = "0";
    public static final String INSTORAGEPROCESS1 = "1";

}
