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
    <form id="matrixCode" action="<%=path%>/instorage/matrixCode" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 入库单主键 -->
        <input type="hidden" id="id" name="id" value="${instorage.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--入库单编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">入库单编号：</label>
                <div class="controls" style="margin-top:4px;">
                    ${instorageBillCode}
                </div>
            </div>
            <%--出库单--%>
            <div id="outstorageBillDiv" class="control-group span6" style="display: none">
                <label class="control-label">出库单：</label>
                <div class="controls" style="margin-top:4px;">
                    ${instorage.outstorageBillCode}
                </div>
            </div>
            <div id="crossDockingDiv" class="control-group span6" style="display: none">
                <label class="control-label">是否越库：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:if test="${not empty instorage.crossDocking}">
                        <c:if test="${instorage.crossDocking == 0}">
                            否
                        </c:if>
                        <c:if test="${instorage.crossDocking == 1}">
                            是
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--入库类型--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">入库类型：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:choose>
                        <c:when test="${'0' eq instorage.instorageType}">
                            采购入库
                        </c:when>
                        <c:when test="${'1' eq instorage.instorageType}">
                            委托加工入库
                        </c:when>
                        <c:otherwise>
                            <c:if test="${instorage.instorageType == 2}">
                                生产退货入库
                            </c:if>
                            <c:if test="${instorage.instorageType == 3}">
                                其他入库
                            </c:if>
                            <%--<c:if test="${instorage.instorageType == 4}">--%>
                                <%--收货入库--%>
                            <%--</c:if>--%>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <%--入库流程--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">入库流程：</label>
                <div class="controls" style="margin-top:4px;">
                    <c:if test="${not empty instorage.instorageProcess}">
                        <c:if test="${instorage.instorageProcess == 0}">
                            一般类型
                        </c:if>
                        <c:if test="${instorage.instorageProcess == 1}">
                            白糖类型
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--供应商--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">供应商：</label>
                <div class="controls" style="margin-top:4px;">
                    ${instorage.supplierName}
                </div>
            </div>
            <%--货主--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">货主：</label>
                <div class="controls" style="margin-top:4px;">
                    ${instorage.customerName}
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

    function getIds() {
        var checkedNum = getGridCheckedNum("#grid-table-c","id");
        if (checkedNum == 0) {
            layer.alert("请选择要生成二维码的详情单！");
            return false;
        }else {
            var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
            window.open(context_path + "/instorage/matrixCode?ids=" + ids);
        }
    }

    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var instorageId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/instorage/getDetailList?instorageId=" + instorageId,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "批次号","生产日期", "包装单位", "入库数量", "入库重量（kg）"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "materialCode", index: "material_Code", width: 20},
            {name: "materialName", index: "material_Name", width: 20},
            {name: "batchNo", index: "batch_No", width: 20},
            {name: "productDate", index: "product_Date", width: 20},
            {name: "unit", index: "unit", width: 20},
            {name: 'instorageAmount', index: 'instorage_Amount', width: 20},
            {name: "instorageWeight", index: "instorage_Weight", width: 30}
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