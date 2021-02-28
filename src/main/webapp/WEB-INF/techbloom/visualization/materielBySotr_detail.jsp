<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 库位主键ID -->
        <input type="hidden" id="id" name="id" value="${depotPosition.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--库位编码--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label">库位名称：</label>
                <div class="controls" style="margin-top:6px;color: red;font-size: 35px">
                    ${depotPosition.positionCode}
                </div>
            </div>
        </div>

        <%--二行数据--%>
        <%--<div class="row" style="margin:0;padding:0;">--%>
            <%--&lt;%&ndash;冻结状态&ndash;%&gt;--%>
            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label">冻结状态：</label>--%>
                <%--<div class="controls" style="margin-top:6px;">--%>
                    <%--<c:if test="${depotPosition.positionFrozen == 0}">未冻结</c:if>--%>
                    <%--<c:if test="${depotPosition.positionFrozen == 1}">已冻结</c:if>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--&lt;%&ndash;库位方向&ndash;%&gt;--%>
            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label">库位方向：</label>--%>
                <%--<div class="controls" style="margin-top:6px;">--%>
                    <%--${depotPosition.positionDirection}--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    </form>
    <%--<div id="materialDiv" style="margin:10px;">--%>
        <%--<br/>--%>
    <%--</div>--%>
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
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var positionId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;

    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/visual/getDetailList?positionId=" + positionId,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "批次号", "重量（kg）", "数量", "单位", "RFID", "排序"],
        colModel: [
            {name: "id", index: "id", width: 30, hidden: true},
            {name: "materielCode", index: "materielCode", width: 40,sortable:false},
            {name: "materielName", index: "materielName", width: 40,sortable:false},
            {name: "batchRule", index: "batchRule", width: 70,sortable:false},
            {name: "weight", index: "weight", width: 30,sortable:false},
            {name: "amount", index: "amount", width: 30,sortable:false},
            {name: "unit", index: "unit", width: 30,sortable:false},
            {name: "rfid", index: "rfid", width: 30,sortable:false},
            {name: "sort", index: "sort", width: 15,sortable:false,cellattr: addCellAttr}

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
                    url: context_path + '/visual/getDetailList?positionId=' + positionId,
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
        $("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height - 45);
    });
    $(window).triggerHandler("resize.jqGrid");

    //给有值的排序 添加 背景颜色
    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if (rawObject.sort !=null) {
            return "style='background-color:yellow;font-weight:bold'";
        }
    }

</script>