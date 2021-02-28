<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<form id="baseInfor" class="form-horizontal" target="_ifr">
		<!-- 收货单主键 -->
		<input type="hidden" id="id" name="id" value="${receipt.id}">
		<%--一行数据 --%>
		<div class="row" style="margin:0;padding:0;">
			<%--收货编号--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">收货编号：</label>
				<div class="controls" style=" margin-top:4px;">
					${receiptCode}
				</div>
			</div>
			<%--预计收货日期--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">预计收货时间：</label>
				<div class="controls" style=" margin-top:4px;">
					${receipt.estimatedDeliveryTime}
				</div>
			</div>
		</div>
		<div class="row" style="margin:0;padding:0;">
			<%--供应商--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">供应商：</label>
				<div class="controls" style=" margin-top:4px;">
					${receipt.supplierName}
				</div>
			</div>
			<%--货主--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">货主：</label>
				<div class="controls" style=" margin-top:4px;">
					${receipt.customerName}
				</div>
			</div>
		</div>
		<div class="row" style="margin:0;padding:0;">
			<%--单据类型--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">单据类型：</label>
				<div class="controls" style=" margin-top:4px;">
					<c:if test="${receipt.documentType == 0}">
						自采
					</c:if>
					<c:if test="${receipt.documentType == 1}">
						客供
					</c:if>
				</div>
			</div>
			<%--备注--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">备注：</label>
				<div class="controls" style=" margin-top:4px;">
					${receipt.remark}
				</div>
			</div>
		</div>
		<div style="margin-left:10px;">
            <br />
		</div>
	</form>
	<!-- 表格div -->
	<div id="grid-div-c" style="width:100%;margin:0px auto;">
		<!-- 	表格工具栏 -->
		<div id="fixed_tool_div" class="fixed_tool_div detailToolBar">
			<div id="__toolbar__-c" style="float:left;overflow:hidden;"></div>
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
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss',useMinutes:true,useSeconds:true});
    var receiptId=$("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;

    _grid_detail=jQuery("#grid-table-c").jqGrid({
        url : context_path + "/receiptManage/getDetailList?receiptId="+receiptId,
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","包装单位","计划收货数量","计划收货重量（kg）"],
        colModel : [
            {name : "id",index : "id",width : 20,hidden:true},
            {name : "materialCode",index:"material_Code",width :20},
            {name : "materialName",index:"material_Name",width : 20},
            {name : "unit",index:"unit",width : 20},
            {name: 'planReceiptAmount', index: 'plan_Receipt_Amount', width: 20},
            {name : "planReceiptWeight",index:"plan_Receipt_Weight",width : 30}
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : "#grid-pager-c",
        sortname : "materialCode",
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
                    url:context_path + '/receiptManage/getDetailList?receiptId='+receiptId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3 );
        var height = $(".layui-layer-title",_grid_detail.parents(".layui-layer")).height()+
            $("#baseInfor").outerHeight(true)+$("#materialDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight',_grid_detail.parents(".layui-layer").height()-height);
    });
    $(window).triggerHandler("resize.jqGrid");
</script>