<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
%>
<div class="widget-box" style="border:10px;margin:10px">
    <div id="grid-div-d" style="width:100%;margin:10px auto;">
        <div id="fixed_tool_div" class="fixed_tool_div detailToolBar">
            <div id="__toolbar__-d" style="float:left;overflow:hidden;"></div>
        </div>
        <table id="grid-table-d" style="width:100%;height:100%;"></table>
        <div id="grid-pager-d"></div>
    </div>
</div>
<style>
    .ui-jqgrid .ui-jqgrid-hbox{
        padding-right:0;
        width:100%;
    }
    .ui-jqgrid-htable,#grid-table-d{
        width: 100% !important;
    }
</style>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var receiptPlanId=${receiptPlanId};
    var oriDataDetail;
    var _grid_detail_d;        //表格对象
    var lastsel2;

    //单元格编辑成功后，回调的函数
    var editFunction = function eidtSuccess(XHR){
        var data = eval("("+XHR.responseText+")");
        if(data["msg"]!=""){
            layer.alert(data["msg"]);
        }
        jQuery("#grid-table-d").jqGrid('setGridParam',
            {
                postData: {
                    id:$('#id').val(),
                    queryJsonString:""
                }
            }
        ).trigger("reloadGrid");
    };

    var eidtMaterialMemory = {};

    _grid_detail_d=jQuery("#grid-table-d").jqGrid({
        url : context_path + '/receiptManage/getDetailList?receiptId='+receiptPlanId,
        datatype : "json",
        colNames : [ '详情主键','物料编号','物料名称','批次号（yymmdd）','生产日期（YYYYMMDD）','保质期（天）','计划收货数量','未收货数量','入库数量','计划收货重量（kg）','未收货重量（kg）','入库重量（kg）'],
        colModel : [
            {name : 'id',index : 'id',width : 20,hidden:true},
            {name : 'materialCode',index:'material_Code',width :20},
            {name : 'materialName',index:'material_Name',width : 15},
            {name : 'batchNo',sortable: false,index:'batch_No',width : 25,editable : true,formatter:formatterEditData,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            if (!strDateTime($("#" + $elementId).val())) {
                                layer.alert("请输入yymmdd的日期格式数据");
                                $("#" + $elementId).val("");
                            }else{
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["batchNo"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"batchNo":$("#" + $elementId).val()};
                                }
                            }
                        }
                    }]
                }},
            {name : 'productDate',sortable: false,index:'product_Date',width : 32,editable : true,formatter:formatterEditData,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("请输入YYYYMMDD的日期格式数据");
                                $("#" + $elementId).val("");
                            }else{
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["productDate"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"productDate":$("#" + $elementId).val()};
                                }
                            }
                        }
                    }]
                }},
            {name: 'qualityPeriod',sortable: false, index: 'quality_period', width: 13,editable : true,formatter:formatterEditData,
                // editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]*[1-9][0-9]*$");
                            var qualityPeriod = $("#" + $elementId).val();
                            var flag = true;
                            if (!reg.test(qualityPeriod)) {
                                layer.alert("请输入正确的保质期天数");
                                flag = false;
                            }
                            if(flag){
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["qualityPeriod"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"qualityPeriod":$("#" + $elementId).val()};
                                }
                            }else{
                                $("#" + $elementId).val("");
                            }
                        }
                    }]
                }},
            {name : 'planReceiptAmount',index:'plan_Receipt_Amount',width : 16},
            {name : 'separableAmount',index:'separable_Amount',width : 14},
            {name: 'inStorageAmount', index: 'actual_Receipt_Amount', width: 13,editable : true,formatter:formatterEditData,
                editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            var separableAmount = $("#" + $elementId).parent().prev().text();
                            var inStorageAmount = $("#" + $elementId).val();
                            var flag = true;
                            if (!reg.test(inStorageAmount)) {
                                layer.alert("非法的数量！(注：可以有两位小数的正实数)");
                                flag = false;
                            }
                            if(Number(inStorageAmount)>Number(separableAmount)){
                                layer.alert("物料的入库数量不可高于未收货数量！");
                                flag = false;
                            }
                            if(flag){
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["inStorageAmount"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"inStorageAmount":$("#" + $elementId).val()};
                                }
                            }else{
                                $("#" + $elementId).val("");
                            }
                        }
                    }]
                }},
            {name : 'planReceiptWeight',index:'plan_Receipt_Weight',width : 23},
            {name : 'separableWeight',index:'separable_Weight',width : 21},
            {name : 'inStorageWeight',index:'actual_Receipt_Weight',width : 18, editable: true,formatter:formatterEditData,
                editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            var separableWeight = $("#" + $elementId).parent().prev().text();
                            var inStorageWeight = $("#" + $elementId).val();
                            var flag = true;
                            if (!reg.test(inStorageWeight)) {
                                layer.alert("非法的重量！(注：可以有两位小数的正实数)");
                                flag = false;
                            }
                            if(Number(inStorageWeight)>Number(separableWeight)){
                                layer.alert("物料的入库重量不可高于未收货重量！");
                                flag = false;
                            }
                            if(flag){
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["inStorageWeight"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"inStorageWeight":$("#" + $elementId).val()};
                                }
                            }else{
                                $("#" + $elementId).val("");
                            }
                        }
                    }]
                }}
        ],
        //rowNum : 10,
        //rowList : [ 5, 10, 20 ],
        //pager : '#grid-pager-d',
        sortname : 'ID',
        sortorder : "asc",
        altRows: true,
        viewrecords : true,
        autowidth:true,
        multiselect: true,
        multiboxonly: true,
        loadComplete : function(data){
            var table = this;
            setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
            oriDataDetail = data;
        },
        cellEdit: true,
        cellsubmit : "clientArray",
        emptyrecords: "没有相关记录",
        loadtext: "加载中...",
        pgtext : "页码 {0} / {1}页",
        recordtext: "显示 {0} - {1}共{2}条数据",
    });
    //在分页工具栏中添加按钮
    $("#grid-table-d").navGrid('#grid-pager-d',{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager-d',{
        caption:"",
        buttonicon:"ace-icon fa fa-refresh green",
        onClickButton: function(){
            $("#grid-table-d").jqGrid('setGridParam',
                {
                    url:context_path + '/ASNmanage/detailList?receiptId='+receiptId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on('resize.jqGrid', function () {
        $("#grid-table-d").jqGrid("setGridWidth", $("#grid-div-d").width() - 3 );
        var height = $(".layui-layer-title",_grid_detail_d.parents(".layui-layer")).height()+
            // $("#materialDiv").outerHeight(true)+
            $("#grid-pager-d").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            $("#gbox_grid-table-d .ui-jqgrid-hdiv").outerHeight(true);
        $("#grid-table-d").jqGrid('setGridHeight',_grid_detail_d.parents(".layui-layer").height()-height-50);
    });
    $(window).triggerHandler('resize.jqGrid');
    if($("#id").val()!=""){
        //reloadDetailTableList();   //重新加载详情列表
    }


    //将数据格式化成两位小数：四舍五入
    function formatterNumToFixed(value,options,rowObj){
        if(value!=null){
            var floatNum = parseFloat(value);
            return floatNum.toFixed(2);
        }else{
            return "0.00";
        }
    }

    //给表格中编辑数据添加默认值
    function formatterEditData(cellvalue, options, rowObject){
        var id = options.rowId;
        var cellName = options.colModel.name;
        if(cellvalue){
            return cellvalue;
        }else{
            if(eidtMaterialMemory[id]!=undefined&&eidtMaterialMemory[id]!=null&&eidtMaterialMemory[id][cellName]!=undefined&&eidtMaterialMemory[id][cellName]!=null){
                return eidtMaterialMemory[id][cellName]
            }else{
                return "";
            }
        }
    }

    //数量输入验证
    function numberRegex(value, colname) {

        var regex = /^\d+\.?\d{0,2}$/;
        //reloadDetailTableList();
        if (!regex.test(value)) {
            return [false, ""];
        }
        else  return [true, ""];
    }
    if($("#receiptId").val()!=""){

        $("#receiptUser").select2("data", {
            id: $("#receiptUserId").val(),
            text: $("#userName").val()
        });
        $("#supplyId").select2("data", {
            id: $("#supply").val(),
            text: $("#supplyName").val()
        });
    }else{
        $('#customer').val("")
    }
    //清空物料多选框中的值
    function removeChoice(){
        $("#s2id_materialInfor .select2-choices").children(".select2-search-choice").remove();
        $("#materialInfor").select2("val","");
        selectData = 0;

    }

    //添加物料详情
    function addDetail(){
        if($("#receiptId").val()==""){
            layer.alert("请先保存表单信息！");
            return;
        }
        if(selectData!=0){
            //将选中的物料添加到数据库中
            $.ajax({
                type:"POST",
                url:context_path + '/ASNmanage/saveDetail',
                data:{receiptId:$('#baseInfor #receiptId').val(),materialIds:selectData.toString()},
                dataType:"json",
                success:function(data){
                    removeChoice();   //清空下拉框中的值
                    if(Boolean(data.result)){
                        layer.msg("添加成功",{icon:1,time:1200});
                        //重新加载详情表格
                        reloadDetailTableList();
                    }else{
                        layer.msg(data.msg,{icon:2,time:1200});
                    }
                }
            });
        }else{
            layer.alert("请选择物料！");
        }
    }
    //工具栏
    $("#__toolbar__-d").iToolBar({
        id:"__tb__01",
        items:[
            {label:"确认生成入库单", onclick:generateInstorage}
        ]
    });

    //确认生成入库单
    function generateInstorage(){
        //收货单详情主键拼接
        var receiptPlanDetailIdStr = "";
        //入库数量拼接字符串
        var inStorageAmountStr = "";
        //入库重量拼接字符串
        var inStorageWeightStr = "";
        //批次号拼接字符串
        var batchNoStr = "";
        //生产日期拼接字符串
        var productDateStr = "";
        //保质期拼接字符串
        var qualityPeriodStr = "";

        var ids=$('#grid-table-d').jqGrid('getGridParam','selarrrow');
        if(ids==null || ids=='' || ids==undefined){
            layer.msg("请先选择物料",{icon:2,time:2000});
            return false;
        }

        var flag = true;
        var flag2 = true;
        var flag3 = true;
        var flag4 = true;
        var flag5 = true;
        var flag6 = true;
        var flag7 = true;
        var flag8 = true;

        jQuery(ids).each(function(){
            var rowData = $("#grid-table-d").jqGrid("getRowData",this);
            var id = rowData.id;
            var batchNo = rowData.batchNo&&$(rowData.batchNo).val()!=undefined?$("#"+$(rowData.batchNo).attr("id")).val():rowData.batchNo;
            var productDate = rowData.productDate&&$(rowData.productDate).val()!=undefined?$("#"+$(rowData.productDate).attr("id")).val():rowData.productDate;
            var inStorageAmount = rowData.inStorageAmount&&$(rowData.inStorageAmount).val()!=undefined?$("#"+$(rowData.inStorageAmount).attr("id")).val():rowData.inStorageAmount;
            var inStorageWeight = rowData.inStorageWeight&&$(rowData.inStorageWeight).val()!=undefined?$("#"+$(rowData.inStorageWeight).attr("id")).val():rowData.inStorageWeight;
            var qualityPeriod = rowData.qualityPeriod&&$(rowData.qualityPeriod).val()!=undefined?$("#"+$(rowData.qualityPeriod).attr("id")).val():rowData.qualityPeriod;

            receiptPlanDetailIdStr += id +",";
            batchNoStr += batchNo + ",";
            productDateStr += productDate + ",";
            inStorageAmountStr += inStorageAmount + ",";
            inStorageWeightStr += inStorageWeight + ",";
            qualityPeriodStr += qualityPeriod + ",";
            if(batchNo ==null || batchNo == undefined || batchNo.trim()==''){
                flag = false;
                return;
            }else{
                if (!strDateTime(batchNo)) {
                    flag2 = false;
                    return;
                }
            }

            if(productDate ==null || productDate == undefined || productDate.trim()==''){
                flag = false;
                return;
            }else{
                var reg = new RegExp("^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$");
                if (!reg.test(productDate)) {
                    flag3 = false;
                    return;
                }
            }

            if(inStorageAmount ==null || inStorageAmount == undefined || inStorageAmount.trim()==''){
                flag = false;
                return;
            }else{
                // var reg = new RegExp("^[0-9]*$");
                var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                if (!reg.test(inStorageAmount)) {
                    flag4 = false;
                    return;
                }
                if(Number(inStorageAmount)>Number(rowData.separableAmount)){
                    flag6 = false;
                    return;
                }
            }
            if(inStorageWeight ==null || inStorageWeight == undefined || inStorageWeight.trim()==''){
                flag = false;
                return;
            }else{
                // var reg = new RegExp("^[0-9]*$");
                var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                if (!reg.test(inStorageWeight)) {
                    flag5 = false;
                    return;
                }
                if(Number(inStorageWeight)>Number(rowData.separableWeight)){
                    flag7 = false;
                    return;
                }
            }
            if(qualityPeriod ==null || qualityPeriod == undefined || qualityPeriod.trim()==''){
                flag = false;
                return;
            }else{
                // var reg = new RegExp("^[0-9]*$");
                // var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                var reg = new RegExp("^[0-9]*[1-9][0-9]*$");
                if (!reg.test(qualityPeriod)) {
                    flag8 = false;
                    return;
                }
            }
        });

        if(!flag){
            layer.msg("生成入库单失败！请确认选中的物料的参数是否填写",{icon:2,time:3000});
            return false;
        }
        if(!flag2){
            layer.msg("生成入库单失败！请确认物料的批次号是yymmdd的日期格式",{icon:2,time:3000});
            return false;
        }
        if(!flag3){
            layer.msg("生成入库单失败！请确认物料的生产日期是YYYYMMDD的日期格式",{icon:2,time:3000});
            return false;
        }
        if(!flag4){
            layer.msg("生成入库单失败！请确认物料的入库数量是数字",{icon:2,time:3000});
            return false;
        }
        if(!flag5){
            layer.msg("生成入库单失败！请确认物料的入库重量是数字",{icon:2,time:3000});
            return false;
        }
        if(!flag6){
            layer.msg("生成入库单失败！请确认物料的入库数量不大于未收货数量",{icon:2,time:3000});
            return false;
        }
        if(!flag7){
            layer.msg("生成入库单失败！请确认物料的入库重量不大于未收货重量",{icon:2,time:3000});
            return false;
        }
        if(!flag8){
            layer.msg("生成入库单失败！请确认物料的保质期是否为正整数",{icon:2,time:3000});
            return false;
        }

        //截取逗号
        receiptPlanDetailIdStr = receiptPlanDetailIdStr.substr(0,receiptPlanDetailIdStr.length-1);
        inStorageAmountStr = inStorageAmountStr.substr(0,inStorageAmountStr.length-1);
        inStorageWeightStr = inStorageWeightStr.substr(0,inStorageWeightStr.length-1);
        batchNoStr = batchNoStr.substr(0,batchNoStr.length-1);
        productDateStr = productDateStr.substr(0,productDateStr.length-1);
        qualityPeriodStr = qualityPeriodStr.substr(0,qualityPeriodStr.length-1);

        layer.confirm("请确认数量填写正确，确认生成入库单？",function(){
            $.ajax({
                url:context_path + '/receiptManage/generateInstorage',
                data:{
                    receiptPlanId:receiptPlanId,
                    receiptPlanDetailIdStr:receiptPlanDetailIdStr,
                    inStorageAmountStr:inStorageAmountStr,
                    inStorageWeightStr:inStorageWeightStr,
                    batchNoStr:batchNoStr,
                    productDateStr:productDateStr,
                    qualityPeriodStr:qualityPeriodStr
                },
                type:"POST",
                dataType:"JSON",
                success:function(data){
                    if(data.code){
                        layer.msg(data.msg,{icon:1,time:3000});
                        _grid_detail_d.trigger("reloadGrid");  //重新加载表格
                        _grid.trigger("reloadGrid");  //重新加载父页面表格
                    }else{
                        layer.msg(data.msg,{icon:2,time:3000});
                    }
                }
            });
        });

    }
    function reloadDetailTableList(){
        $("#grid-table-d").jqGrid('setGridParam',
            {
                postData: {receiptId:$("#baseInfor #receiptId").val()} //发送数据  :选中的节点
            }
        ).trigger("reloadGrid");
    }


    /**
     * 验证是否位yymmdd日期格式
     * @param str
     */
    function strDateTime(str) {
        if (str == null || str == "" || str.length != 6) {
            return false;
        } else {
            var yy = str.substring(0, 2);
            var mm = str.substring(2, 4);
            var dd = str.substring(4);
            var d = new Date(yy, mm - 1, dd);

            return d.getYear() == yy && (d.getMonth() + 1) == mm && d.getDate() == dd;
        }
    }
</script>
