<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<form id="baseInfor" class="form-horizontal" target="_ifr">
		<!-- 托盘主键 -->
		<input type="hidden" id="id" name="id" value="${tray.id}">
		<input type="hidden" id="type" name="type" value="${tray.type}">
		<%--一行数据 --%>
		<div class="row" style="margin:0;padding:0;">
			<%--合并/拆分编号--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">
					<c:if test="${tray.type==0}">合并：</c:if>
					<c:if test="${tray.type==1}">拆分：</c:if>
				</label>
				<div class="controls" style="margin-top:6px;">
					${tray.mergeOrSplitCode}
				</div>
			</div>
			<%--RFID--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">RFID：</label>
				<div class="controls" style="margin-top:6px;">
					${tray.rfid}
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

			<%--状态--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">状态：</label>
				<div class="controls" style="margin-top:6px;">
					<c:if test="${not empty tray.status}">
						<c:if test="${tray.status == 0}">
							未入库
						</c:if>
						<c:if test="${tray.status == 1}">
							已入库
						</c:if>
					</c:if>
				</div>
			</div>

		</div>

		<%--三行数据--%>
		<div class="row" style="margin:0;padding:0;">

			<%--备注--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">备注：</label>
				<div class="controls" style="margin-top:6px;">
					${tray.remarks}
				</div>
			</div>
		</div>
	</form>
	<!-- 表格div -->
	<div id="grid-div-c" style="width:100%;margin:0px auto;">
		<!-- 物料详情信息表格 -->
		<table id="grid-table-c" style="width:100%;height:100%;"></table>
		<!-- 表格分页栏 -->
		<div id="grid-pager-c"></div>
	</div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/stock/trayManager_merge_or_split.js"></script>

	<script type="text/javascript">
        $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
        var trayBy = $("#baseInfor #id").val();
        var oriDataDetail;
        var _grid_detail;

        _grid_detail = jQuery("#grid-table-c").jqGrid({
            url: context_path + "/trayManager/queryTrayDetailPage.do?trayBy=" + trayBy,
            datatype: "json",
            colNames: ["详情主键", "物料编号", "物料名称", "批次号", "规格包装", "数量", "重量（kg）"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "materielCode", index: "materielCode", width: 20},
                {name: "materielName", index: "materielName", width: 20,sortable:false},
                {name: "batchRule", index: "batchRule", width: 30,sortable:false},
                {name: "unit", index: "unit", width: 20,sortable:false},
                {name: "amount", index: "amount", width: 20},
                {name: "weight", index: "weight", width: 20}
            ],
            rowNum: 10,
            rowList: [10, 20, 30],
            pager: "#grid-pager-c",
            sortname: "id",
            sortorder: "asc",
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
                        url: context_path + "/trayManager/queryTrayDetailPage.do?trayBy=" + trayBy,
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
            $("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height);
        });
        $(window).triggerHandler("resize.jqGrid");
	</script>