<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 入库单主键 -->
        <input type="hidden" id="id" name="id" value="${spareBillManagerVO.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--入库单编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">备料单编号：</label>
                <div class="controls" style="margin-top:4px;">
                    ${spareBillManagerVO.productName}
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label">&nbsp;</label>
            </div>

        </div>
        <div class="row" style="margin:0;padding:0;">

            <%--生产任务单号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">生产任务单号：</label>
                <div class="controls" style="margin-top:4px;">
                    ${spareBillManagerVO.productNo}
                </div>
            </div>

            <%--生产产品信息--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">生产产品信息：</label>
                <div class="controls" style="margin-top:4px;">
                    ${spareBillManagerVO.productName}
                </div>
            </div>

        </div>

        <div class="row" style="margin:0;padding:0;">
            <%--生产数量--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">生产数量：</label>
                <div class="controls" style="margin-top:4px;">
                    ${spareBillManagerVO.totalProductAmount}
                </div>
            </div>
            <%--货主--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">产线信息：</label>
                <div class="controls" style="margin-top:4px;">
                    ${spareBillManagerVO.mixUseLine}
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--备注--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">备注：</label>
                <div class="controls" style="margin-top:4px;">
                    ${instorage.remark}
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

    $(document).ready(function () {
        //入库类型下拉框值
        var instorageTypeVal = '${instorage.instorageType}';
        if (instorageTypeVal == "2") {//如果是“生产退货入库”类型
            $("#baseInfor #outstorageBillDiv").attr("style", "display: inline");
            $("#baseInfor #outstorageBillId").select2("data", {
                id: $("#baseInfor #outstorageBillId").val(),
                text: $("#baseInfor #outstorageBillCode").val()
            });
        } else if (instorageTypeVal == "0" || instorageTypeVal == "1") {//如果是“采购入库”或“委托加工入库”类型
            $("#baseInfor #outstorageBillDiv").attr("style", "display: none");
            $("#baseInfor #crossDockingDiv").attr("style", "display: inline");
        } else {
            $("#baseInfor #outstorageBillDiv").attr("style", "display: none");
            $("#baseInfor #crossDockingDiv").attr("style", "display: none");
        }
    });

    function reloadDetailTableList() {
        $("#grid-table-c").jqGrid("setGridParam", {
            postData: {instorageId: $("#baseInfor #id").val()} //发送数据  :选中的节点
        }).trigger("reloadGrid");
    }

</script>
<script type="text/javascript">

    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var spareBillId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/spareBillManager/getDetailList?spareBillId=" + spareBillId,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "供应商名称", "库位名称", "使用数量","使用量", "准备数量","准备重量","仓库发货数量", "生产剩余数量"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "materialCode", index: "materialCode", width: 20},
            {name: "materialName", index: "materialName", width: 20},
            {name: "supplierName", index: "supplierName", width: 30},
            {name: "positionCode", index: "positionCode", width: 15},
            {name: "usedBox", index: "usedBox", width: 20},
            {name: "userAmount", index: "userAmount", width: 20},
            {name: "quantityReady", index: "quantityReady", width: 25},
            {name: "quantityWeight", index: "quantityWeight", width: 25},
            {name: 'sendAmount', index: 'sendAmount', width: 25},
            {name: "surplusAmount", index: "surplusAmount", width: 25}
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: "#grid-pager-c",
        sortname: "material_code",
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
                    url: context_path + '/instorage/getDetailList?receiptId=' + receiptId,
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