<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
%>
<link rel="stylesheet" href="<%=path%>/static/techbloom/system/scene/css/index.css"/>

<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <div class="step1">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name active">
                <i class="instorage_circle">1</i>
                <span>填写上架单信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 上架单主键 -->
        <input type="hidden" id="id" name="id" value="${putBill.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--上架单编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="putBillCode" >上架单编号：</label>
                <div class="controls">
                    <div class="input-append  span12" >
                        <input type="text" id="putBillCode" class="span10" name="putBillCode"   placeholder="后台自动生成" readonly="readonly" value="${putBillCode}"/>
                    </div>
                </div>
            </div>
            <%--入库单--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="instorageBillId" >入库单：</label>
                <div class="controls">
                    <div class="span12 required" style=" float: none !important;">
                        <input class="span10 select2_input" type="text"  id="instorageBillId" name="instorageBillId" value="${putBill.instorageBillId}">
                        <input type="hidden" id="instorageBillCode" name="instorageBillCode" value="${putBill.instorageBillCode}">
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--操作人--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="operator" >操作人：</label>
                <div class="controls">
                    <div class="span12 required" style=" float: none !important;">
                        <input class="span10 select2_input" type="text"  id="operator" name="operator" value="${putBill.operator}">
                        <input type="hidden" id="operatorName" name="operatorName" value="${putBill.operatorName}">
                    </div>
                </div>
            </div>
            <%--备注--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="remark" >备注：</label>
                <div class="controls">
                    <div class="input-append span12" >
                        <input class="span10" type="text" id="remark" name="remark" placeholder="备注" value="${instorage.remark}">
                    </div>
                </div>
            </div>
        </div>
        <div class="flex-row justify-content-end" style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
            <span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
        </div>
    </form>
    </div>

    <div class="step2" style="overflow-y: hidden">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name">
                <i class="instorage_circle badge-ok"><i class="ace-icon fa fa-check bigger-110"></i></i>
                <span>填写上架单信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <div id="materialDiv" style="margin:10px;">
        <!-- 下拉框 -->
        <label class="inline" for="materialInfor">物料：</label>
        <input type="text" id = "materialInfor" name="materialInfor" style="width:350px;margin-right:10px;" />
        <button id="addMaterialBtn" class="btn btn-xs btn-primary" onclick="addDetail();">
            <i class="icon-plus" style="margin-right:6px;"></i>添加
        </button>
    </div>
        <!-- 表格div -->
        <div id="grid-div-c" style="width:100%;margin:0px;">
            <!-- 	表格工具栏 -->
            <button id="btchdel" class="btn btn-info" onclick="delDetail();" style="margin-bottom: 0.5rem;float: right">
                <i class="icon-minus" style="margin-right:6px;"></i>批量删除
            </button>
            <div style="clear: both;"></div>

            <!-- 物料详情信息表格 -->
            <div style="margin-top: 5px"><table id="grid-table-c" style="width:100%;height:100%;"></table></div>

            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
        </div>
        <div class="flex-row justify-content-end" style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <%--<span class="btn btn-info" id="formSave">--%>
            <%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
            <%--</span>--%>
            <span class="btn btn-info" id="formSubmit">
				<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;审核并提交
			</span>
            <span class="btn btn-info ml-3" id="pre_step">
				<i class="ace-icon fa fa-arrow-left bigger-110"></i>&nbsp;上一步
			</span>
        </div>
    </div>

</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/putBill/putBill_edit.js"></script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss',useMinutes:true,useSeconds:true});
    var putBIllId=$("#baseInfor #id").val();
    var oriDataDetail;
    var edit="${edit}";
    var _grid_detail;
    var select_dataLocation = "";
    _grid_detail=jQuery("#grid-table-c").jqGrid({
        url : context_path + "/putBill/getDetailList?putBIllId="+putBIllId,
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","批次号","生产日期","包装单位","上架数量","上架重量（kg）","RFID","库位","操作"],
        colModel : [
            {name : "id",index : "id",width : 20,hidden:true},
            {name : "materialCode",index:"material_Code",width :20},
            {name : "materialName",index:"material_Name",width : 20},
            {name : "batchNo",index:"batch_No",width : 20},
            {name : "productDate",index:"product_Date",width : 20},
            {name : "unit",index:"unit",width : 20},
            {name: 'putAmount', index: 'put_Amount', width: 20, editable: true,
                editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的数量！(注：可以有两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/putBill/updatePutAmount',
                                type: "POST",
                                data: {
                                    putBillDetailId: id,
                                    putAmount: $("#" + rowid + "_putAmount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/putBill/getDetailList?putBIllId=' + putBIllId,
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {name : "putWeight",index:"put_Weight",width : 30, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的重量！(注：可以有两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/putBill/updatePutWeight',
                                type: "POST",
                                data: {
                                    putBillDetailId: id,
                                    putWeight: $("#" + rowid + "_putWeight").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/putBill/getDetailList?putBIllId=' + putBIllId,
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }

            },
            {name: 'rfid', index: 'rfid', width: 30, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            $.ajax({
                                url: context_path + '/putBill/updateRfid',
                                type: "POST",
                                data: {
                                    putBillDetailId: id,
                                    rfid: $("#" + rowid + "_rfid").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/putBill/getDetailList?putBIllId=' + putBIllId,
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {name : "positionCode",index:"position_Code",width : 30,editable: true,
                edittype: "select",
                editoptions:{
                    value:getPosition(),
                    dataEvents:[{
                        type:'blur',
                        fn:function(e){
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            $.ajax({
                                url: context_path + '/putBill/updatePositionCode',
                                type: "POST",
                                data: {
                                    putBillDetailId: id,
                                    positionCode: $("#" + rowid + "_positionCode").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/putBill/getDetailList?putBIllId=' + putBIllId,
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });

                        }
                    }]
                },
                formatter: function (cellValue, option, rowObject) {
                    var vle = cellValue || "";
                    if (select_dataLocation && select_dataLocation.result) {
                        for (var i = 0; i < select_dataLocation.result.length; i++) {
                            if (select_dataLocation.result[i].positionCode == cellValue) {
                                vle = select_dataLocation.result[i].positionName;
                            }
                        }
                    }
                    return vle;
                }
            },{name : "del",sortable:false,index:"del",width : 20,formatter:function(cellvalue, options, rowObject){
                    var id = rowObject.id;
                    return "<a onclick='delOneDetail("+id+")' style='text-decoration:underline;display: block;'>删除</a>"
                }}
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : "#grid-pager-c",
        sortname : "material_code",
        sortorder : "desc",
        altRows: true,
        viewrecords : true,
        autowidth:true,
        multiselect:true,
        multiboxonly: true,
        loadComplete : function(data){
            var table = this;
            setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
            oriDataDetail = data;
            $(window).triggerHandler('resize.jqGrid');
        },
        cellEdit: true,
        cellsubmit : "clientArray",
        emptyrecords: "没有相关记录",
        loadtext: "加载中...",
        pgtext : "页码 {0} / {1}页",
        recordtext: "显示 {0} - {1}共{2}条数据",
    });
    //在分页工具栏中添加按钮
    $("#grid-table-c").navGrid("#grid-pager-c",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager-c',{
        caption:"",
        buttonicon:"ace-icon fa fa-refresh green",
        onClickButton: function(){
            $("#grid-table-c").jqGrid("setGridParam",
                {
                    url:context_path + '/putBill/getDetailList?putBillId='+putBIllId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", 750-3 );
        var height = $(".layui-layer-title",_grid_detail.parents(".layui-layer")).height()+
            $("#baseInfor").outerHeight(true)+$("#materialDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight',_grid_detail.parents(".layui-layer").height()-height-200);
    });
    $(window).triggerHandler("resize.jqGrid");
    $(function () {
        if(edit=="edit"){
            if($('#baseInfor').valid()){
                setNextBtn();
            }
        }
    });
    //获取库位下拉框数据源
    function getPosition(){
        var str = "";
        $.ajax({
            type: "post",
            async: false,
            url: context_path + "/putBill/getPosition",
            dataType: "json",
            success: function (data) {
                select_dataLocation = data;
                for (var i = 0; i < data.result.length; i++) {
                    str += data.result[i].positionCode + ":" + data.result[i].positionName + ";";
                }
                str = str.substring(0, str.length - 1);
            }
        })
        return str;
    }


</script>