<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<form id="matrixCode" action="<%=path%>/materielBindRfid/matrixCode" method="POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="baseInfor" class="form-horizontal" target="_ifr">
		<!-- 物料绑定RFId主键 -->
		<input type="hidden" id="id" name="id" value="${materielBindRfid.id}">
		<input type="hidden" id="status" name="status" value="${materielBindRfid.status}">
		<%--一行数据 --%>
		<div class="row" style="margin:0;padding:0;">
			<%--绑定编号--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">绑定单号：</label>
				<div class="controls" style="margin-top:6px;">
					${materielBindRfid.bindCode}
				</div>
			</div>
			<%--RFID--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">RFID：</label>
				<div class="controls" style="margin-top:6px;">
					${materielBindRfid.rfid}
				</div>
			</div>
		</div>

		<%--二行数据--%>
		<div class="row" style="margin:0;padding:0;">
			<%--库位--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">库位：</label>
				<div class="controls" style="margin-top:6px;">
					${depotPosition.positionName}
				</div>
			</div>
			<%--备注--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">备注：</label>
				<div class="controls" style="margin-top:6px;">
					${materielBindRfid.remarks}
				</div>
			</div>
		</div>
	</form>
	<div id="materialDiv" style="margin:10px;">
		<br />
	</div>
	<!-- 表格div -->
	<div id="grid-div-c" style="width:100%;margin:0px auto;">
		<div class="row-fluid"  style="padding:5px 3px;text-align: right;width: 700px">
			<button class=" btn btn-info" onclick="getIds();" >
				<i class="icon-qrcode" aria-hidden="true" >&nbsp;打印二维码</i>
			</button>
		</div>
		<!-- 物料详情信息表格 -->
		<table id="grid-table-c" style="width:100%;height:100%;"></table>
		<!-- 表格分页栏 -->
		<div id="grid-pager-c"></div>
	</div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/stock/materielBindRfid_edit.js"></script>
<script type="text/javascript">

    function getIds() {
        var checkedNum = getGridCheckedNum("#grid-table-c","id");
        if (checkedNum == 0) {
            layer.alert("请选择要生成二维码的详情单！");
            return false;
        }else {
            var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
            window.open(context_path + "/materielBindRfid/matrixCode?ids=" + ids);
        }
    }

	$(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
	var materielBindRfidBy = $("#baseInfor #id").val();
    var status = $("#baseInfor #status").val();
	var oriDataDetail;
	var _grid_detail;

	_grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/materielBindRfid/getDetailList?materielBindRfidBy=" + materielBindRfidBy+ "&status=" + status,
		datatype: "json",
		colNames: ["详情主键", "物料编号", "物料名称","批次号","重量（kg）","数量","单位", "RFID"],
		colModel: [
			{name: "id", index: "id", width: 30, hidden: true},
			{name: "materielCode", index: "materielCode", width: 30},
			{name: "materielName", index: "materielName", width: 30},
            {name: "batchRule", index: "batchRule", width: 70},
            {name: 'weight', index: 'weight', width: 30},
            {name: 'amount', index: 'amount', width: 30},
			{name: "unit", index: "unit", width: 30},
            {name: 'rfid', index: 'rfid', width: 30}

		],
		rowNum: 10,
		rowList: [10, 20, 30],
		pager: "#grid-pager-c",
		sortname: "materielCode",
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
			oriDataDetail = data;
			$(window).triggerHandler('resize.jqGrid');
		},
		cellEdit: true,
		cellsubmit: "clientArray",
		emptyrecords: "没有相关记录",
		loadtext: "加载中...",
		pgtext: "页码 {0} / {1}页",
		recordtext: "显示 {0} - {1}共{2}条数据",
	});
	//在分页工具栏中添加按钮
	$("#grid-table-c").navGrid("#grid-pager-c", {
		edit: false,
		add: false,
		del: false,
		search: false,
		refresh: false
	}).navButtonAdd('#grid-pager-c', {
		caption: "",
		buttonicon: "ace-icon fa fa-refresh green",
		onClickButton: function () {
			$("#grid-table-c").jqGrid("setGridParam",
				{
					url: context_path + '/materielBindRfid/getDetailList?materielBindRfidBy=' + materielBindRfidBy,
					postData: {queryJsonString: ""} //发送数据  :选中的节点
				}
			).trigger("reloadGrid");
		}
	});

	$(window).on("resize.jqGrid", function () {
		$("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3);
		var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).height() +
			$("#baseInfor").outerHeight(true) + $("#materialDiv").outerHeight(true) +
			$("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) +
			//$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
			$("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
		$("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height-45);
	});
	$(window).triggerHandler("resize.jqGrid");
</script>