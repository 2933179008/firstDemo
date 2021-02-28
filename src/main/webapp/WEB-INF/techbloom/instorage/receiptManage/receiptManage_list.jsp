<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
%>
<style type="text/css"></style>
<div id="grid-div">
	<form id="hiddenForm" action = "<%=path%>/receiptManage/toExcel" method = "POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="hiddenQueryForm" style="display:none;">
		<input name="licensePlateNumber" id="licensePlateNumberId" value=""/>
		<input name="status" id="statusId" value="">
		<input name="receiptNo" id="receiptNoHide" value="">
		<input name="createTime" id="createTimeId" value="">
	</form>
	<div class="query_box" id="yy" title="查询选项">
		<form id="queryForm" style="max-width:100%;">
			<ul class="form-elements">
				<li class="field-group field-fluid3">
					<label class="inline" for="receiptNo" style="margin-right:25px;width:100%;">
						<span class="form_label" style="width:80px;">收货单编号：</span>
						<input type="text" name="receiptNo" id="receiptNo" value="" style="width: calc(100% - 120px);" placeholder="收货单编号">
					</label>
				</li>

				<li class="field-group field-fluid3">
					<label class="inline" for="status" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:48px;">状态：</span>
						<select id="status" name="status"  style="width: calc(100% - 120px);">
							<option value="">--请选择--</option>
							<option value="0">未提交</option>
							<option value="1">待收货</option>
							<option value="2">收货中</option>
							<option value="3">已完成</option>
							<option value="4">强制完成</option>
							<option value="5">超收</option>
						</select>
					</label>
				</li>
			</ul>

			<div class="field-button">
				<div class="btn btn-info" onclick="queryOk();">
					<i class="ace-icon fa fa-check bigger-110"></i>查询
				</div>
				<div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
			</div>
		</form>
	</div>
	<div id="fixed_tool_div" class="fixed_tool_div">
		<div id="__toolbar__" style="float:left;overflow:hidden;"></div>
	</div>
	<table id="grid-table" style="width:100%;height:100%;"></table>
	<div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<%--<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/receiptManage/receiptManage_list.js"></script>--%>
<script type="text/javascript">
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var _grid;

    $("#queryForm #status").select2({
        minimumInputLength:0,
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #status").on("change.select2",function(){
        $("#queryForm #status").trigger("keyup")}
    );

    $(function  (){
        $(".toggle_tools").click();
    });

    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addRecipt, iconClass:'glyphicon glyphicon-plus'},
			{label: "编辑", disabled: (${sessionUser.editQx} == 1 ? false : true), onclick: editRecipt, iconClass:'glyphicon glyphicon-pencil'},
			{label: "删除", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: deleteRecipt, iconClass:'glyphicon glyphicon-trash'},
			{label: "查看", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:detailRecipt, iconClass:'icon-zoom-in'},
			{label: "收货", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:receiptSure, iconClass:'glyphicon glyphicon-pencil'},
			{label: "异常完成收货", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: abnormalReceipt, iconClass:'glyphicon icon-file'},
			{label: "导出", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function () {
				var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
				$("#ids").val(ids);
				$("#hiddenForm").submit();
			},iconClass:' icon-share'
			},
			{label:"打印", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function(){
				print();
				// var queryBean = iTsai.form.serialize($('#hiddenQueryForm'));   //获取form中的值：json对象
				// var queryJsonString = JSON.stringify(queryBean);
				// var url = context_path + "/instorageOrder/exportPrint?"+"id=" + 1;
				// window.open(url);
			},iconClass:' icon-print'}
			]
    });

    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/receiptManage/getList",
            datatype: "json",
            colNames: ["主键", "收货单编号", "供应商", "货主", "单据类型", "预计收货时间", "收货人",  "状态","备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "receiptCode", index: "receipt_Code", width: 65},
                {name: "supplierName", index: "supplier_Name", width: 65},
                {name: "customerName", index: "customer_Name", width: 65},
                {name: "documentType", index: "document_Type", width: 65,
                    formatter:function(cellValue){
                        if (cellValue==0) {
                            return "<span style='font-weight:bold;'>自采</span>";
                        } else if(cellValue==1) {
                            return "<span style='font-weight:bold;'>客供</span>";
                        }
                    }
                },
                {name: "estimatedDeliveryTime", index: "estimated_Delivery_Time", width: 65},
                {name: "createName", index: "create_by", width: 65},
                {name: "state",index:"state",width:50,
                    formatter:function(cellValue){
                        if (cellValue==0) {
                            return "<span style='font-weight:bold;'>未提交</span>";
                        } else if(cellValue==1) {
                            return "<span style='color:blue;font-weight:bold;'>待收货</span>";
                        } else if(cellValue==2) {
                            return "<span style='color:green;font-weight:bold;'>收货中</span>";
                        } else if(cellValue==3) {
                            return "<span style='font-weight:bold;'>已完成</span>";
                        } else if(cellValue==4) {
                            return "<span style='color:red;font-weight:bold;'>异常收货完成</span>";
                        }
                    }
                },
                {name: "remark",index:"remark",width:50, sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "receiptCode",
            sortorder: "desc",
            altRows: true,
            viewrecords: true,
            autowidth: true,
            multiselect: true,
            multiboxonly: true,
            loadComplete: function (data) {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                oriData = data;
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext: "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        jQuery("#grid-table").navGrid("#grid-pager", {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd("#grid-pager", {
            caption: "",
            buttonicon: "ace-icon fa fa-refresh green",
            onClickButton: function () {
                $("#grid-table").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString: ""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() );
            $("#grid-table").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy").outerHeight(true)-$("#fixed_tool_div").outerHeight(true)-$("#grid-pager").outerHeight(true)
                -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
        });

        $(window).triggerHandler("resize.jqGrid");
    });

    //查询
    function queryOk(){
        var queryParam = iTsai.form.serialize($("#queryForm"));
        queryPlatformByParam(queryParam);
    }

    function queryPlatformByParam(jsonParam){
        iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset(){
        $("#queryForm #licensePlateNumber").val("");
		$("#queryForm #status").val("").trigger("change");
        $("#queryForm #receiptNo").val("");
        $("#queryForm #createTime").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addRecipt(){
        $.post(context_path+"/receiptManage/toEdit", {}, function(str){
            $queryWindow = layer.open({
                title : "收货单添加",
                type: 1,
                skin : "layui-layer-molv",
                area : ["750px","650px"],
                shade: 0.6, //遮罩透明度
                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                content: str,//注意，如果str是object，那么需要字符拼接。
                success:function(layero, index){
                    layer.closeAll("loading");
                }
            });
        }).error(function() {
            layer.closeAll();
            layer.msg("加载失败！",{icon:2});
        });
    }

    //修改
    function editRecipt(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要编辑的收货单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个收货单进行编辑操作！");
            return false;
        } else {
            var receiptPlanId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', receiptPlanId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("收货单已提交，不可修改",{time:1200,icon:2});
                return false;
            }
            var documentType = jQuery("#grid-table").jqGrid('getRowData', receiptPlanId).documentType;
            if(documentType=='<span style="font-weight:bold;">自采</span>'&&state!='<span style="font-weight:bold;">未提交</span>'){
                layer.msg("自采类型的单据不可修改",{time:3000,icon:2});
                return false;
            }

            $.post(context_path+"/receiptManage/toEdit?receiptPlanId="+receiptPlanId+"&edit=edit", {}, function(str){
                $queryWindow = layer.open({
                    title : "收货单编辑",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["750px","650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });
        }
    }

    //查看
    function detailRecipt(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0) {
            layer.alert("请选择一个要查看的收货单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个收货单进行查看操作！");
            return false;
        } else {
            var receiptPlanId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path+"/receiptManage/toDetail?receiptPlanId="+receiptPlanId, {}, function(str){
                $queryWindow = layer.open({
                    title : "收货单详情",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["750px","650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });
        }
    }

    //删除
    function deleteRecipt(){
        var checkedNum = getGridCheckedNum("#grid-table","id");  //选中的数量
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要删除的收货单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个收货单进行删除操作！");
            return false;
        } else{
            //从数据库中删除选中的收货单，并刷新收货单表格

            var receiptPlanId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', receiptPlanId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("收货单已提交，不可删除",{time:1200,icon:2});
                return false;
            }
            var documentType = jQuery("#grid-table").jqGrid('getRowData', receiptPlanId).documentType;
            if(documentType=='<span style="font-weight:bold;">自采</span>'){
                layer.msg("自采类型的单据不可删除",{time:3000,icon:2});
                return false;
            }
            //弹出确认窗口
            layer.confirm("确定删除选中的收货订单？", function() {
                $.ajax({
                    type : "POST",
                    url:context_path + "/receiptManage/deleteRecipt?id="+receiptPlanId,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if(data.result){
                            layer.msg("收货订单删除成功！", {icon: 1,time:1000});
                        }else{
                            layer.msg(data.msg, {icon: 2,time:1000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    /**
    * @Description:  收货
    * @Param:
    * @return:
    * @Author: zj
    * @Date: 2019/1/10
    */
    function receiptSure() {
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0){
            layer.alert("请选择一条要收货的收货订单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一条收货订单进行收货操作！");
            return false;
        } else {
            var receiptId = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var state = jQuery("#grid-table").jqGrid('getRowData', receiptId).state;
            if(state != '<span style="color:blue;font-weight:bold;">待收货</span>' && state != '<span style="color:green;font-weight:bold;">收货中</span>'){
                layer.msg("该单据状态不可收货",{time:1200,icon:2});
                return;
            }
            var receiptPlanId = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            $.post(context_path+"/receiptManage/toReceiptView?receiptPlanId="+receiptPlanId, {}, function(str){
                layer.open({
                    title : "收货计划单收货",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["1100px","550px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });

        }
    }

    //异常收货
    function abnormalReceipt(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0){
            layer.alert("请选择一条要异常收货的收货订单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一条收货订单进行异常收货操作！");
            return false;
        } else {
            var receiptId = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var state = jQuery("#grid-table").jqGrid('getRowData', receiptId).state;
            if(state != '<span style="color:blue;font-weight:bold;">待收货</span>' && state != '<span style="color:green;font-weight:bold;">收货中</span>'){
                layer.msg("该单据状态不可异常收货",{time:1200,icon:2});
                return;
            }
			layer.confirm("确定异常收货？", function() {
                $.ajax({
                    type : "POST",
                    url:context_path + "/receiptManage/abnormalReceipt?receiptId="+receiptId,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if(data.result){
                            layer.msg(data.msg, {icon: 1,time:2000});
                        }else{
                            layer.msg(data.msg, {icon: 2,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
		}
	}



</script>