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
        <input type="hidden" id="id" name="id" value="${outStorageManagerVO.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--入库单编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">出库单号：</label>
                <div class="controls" style="margin-top:4px;">
                    ${outStorageManagerVO.outstorageBillCode}
                </div>
            </div>

            <c:if test="${outStorageManagerVO.spareBillCode != null}">
                <%--生产产品信息--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label">备料单：</label>
                    <div class="controls" style="margin-top:4px;">
                            ${outStorageManagerVO.spareBillCode}
                    </div>
                </div>
            </c:if>

            <c:if test="${outStorageManagerVO.receiptCode != null}">
                <%--生产产品信息--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label">备料单：</label>
                    <div class="controls" style="margin-top:4px;">
                            ${outStorageManagerVO.receiptCode}
                    </div>
                </div>
            </c:if>

            <c:if test="${outStorageManagerVO.instorageCode != null}">
                <%--生产产品信息--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label">备料单：</label>
                    <div class="controls" style="margin-top:4px;">
                            ${outStorageManagerVO.instorageCode}
                    </div>
                </div>
            </c:if>
        </div>
        <div class="row" style="margin:0;padding:0;">

            <%--生产任务单号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">出库类型：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:if test="${outStorageManagerVO.outstorageBillType == 0}">生产领料出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 1}">退货出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 2}">越库出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 3}">内部领料出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 4}">报损出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 5}">盘亏出库</c:if>
                    <c:if test="${outStorageManagerVO.outstorageBillType == 6}">其他出库</c:if>
                </div>
            </div>

            <%--出库时间--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">出库时间：</label>
                <div class="controls" style="margin-top:4px;">
                    ${outStorageManagerVO.outstoragePlanTime}
                </div>
            </div>

        </div>

        <div class="row" style="margin:0;padding:0;">
            <%--生产数量--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">单据类型：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:if test="${outStorageManagerVO.billType == 0}">无RFID单据</c:if>
                    <c:if test="${outStorageManagerVO.billType == 1}">有RFID单据</c:if>
                </div>
            </div>
            <%--货主--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">物料类型：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:if test="${outStorageManagerVO.materialType == 0}">自产</c:if>
                    <c:if test="${outStorageManagerVO.materialType == 1}">客供</c:if>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <%--生产数量--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">货主：</label>
                <div class="controls" style="margin-top:4px;">
                    ${outStorageManagerVO.customerCode}
                </div>
            </div>

            <%--备注--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">备注：</label>
                <div class="controls" style="margin-top:4px;">
                    ${outStorageManagerVO.remark}
                </div>
            </div>

            <%--&lt;%&ndash;货主&ndash;%&gt;--%>
            <%--<div class="control-group span6" style="display: inline">--%>
            <%--<label class="control-label">库位：</label>--%>
            <%--<div class="controls" style="margin-top:4px;">--%>
            <%--${outStorageManagerVO.positionCode}--%>
            <%--</div>--%>
            <%--</div>--%>
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
    var outStorageId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/outStorageManager/getDetailList?outStorageId=" + outStorageId,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "物料批次号", "数量", "已下架数量", "重量", "已下架重量", "单位", "库位编码", "生产日期"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "materialCode", index: "materialCode", width: 20},
            {name: "materialName", index: "materialName", width: 20},
            {name: "batchNo", index: "batchNo", width: 60},
            {name: "amount", index: "amount", width: 10},
            {name: "separableAmount", index: "separableAmount", width: 15},
            {name: 'weight', index: 'weight', width: 10},
            {name: "separableWeight", index: "separableWeight", width: 15},
            {name: "unit", index: "unit", width: 10},
            {name: "positionCode", index: "positionCode", width: 20},
            {name: "productData", index: "productData", width: 20}

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