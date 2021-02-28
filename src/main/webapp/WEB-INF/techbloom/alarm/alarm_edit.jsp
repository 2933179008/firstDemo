<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <input type="hidden" id="id" name="id" value="${sceneId}"/>
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
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var sceneId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;

    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/alarm/getAlarmById?id=" + sceneId,
        datatype: "json",
        colNames: ["主键", "预警编号", "预警开始时间", "预警处理时间", "预警时长(min)", "预警库区", "预警类型", "状态"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "alarmCode", index: "alarmCode", width: 50},
            {
                name: "startTime", index: "startTime", width: 50,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu != null) {
                        return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
                    } else {
                        return "";
                    }
                }
            },
            {
                name: "endTime", index: "endTime", width: 50,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu != null) {
                        return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
                    } else {
                        return "";
                    }
                }
            },
            {name: "duration", index: "duration", width: 30},
            {name: "areaName", index: "areaName", width: 30},
            {
                name: "type", index: "type", width: 50,
                formatter: function (cellValue) {
                    if (cellValue == 0) {
                        return "<span style='color:#B8A608;font-weight:bold;'>BOM差异预警</span>";
                    } else if (cellValue == 1) {
                        return "<span style='color:#d15b47;font-weight:bold;'>质检超时预警</span>";
                    } else if (cellValue == 2) {
                        return "<span style='color:#3d579d;font-weight:bold;'>RFID未绑定预警</span>";
                    } else if (cellValue == 3) {
                        return "<span style='color:#D67E31;font-weight:bold;'>叉车取货预警</span>";
                    } else if (cellValue == 4) {
                        return "<span style='color:#daa304;font-weight:bold;'>扫描门出库预警</span>";
                    } else if (cellValue == 5) {
                        return "<span style='color:#3498DB;font-weight:bold;'>库存库龄预警</span>";
                    } else if (cellValue == 6) {
                        return "<span style='color:#4d9dcc;font-weight:bold;'>库存差异预警</span>";
                    }
                }
            },
            {
                name: "state", index: "state", width: 50,
                formatter: function (cellValue) {
                    if (cellValue == 0) {
                        return "<span style='color:#d8120e;font-weight:bold;'>未处理</span>";
                    } else {
                        return "<span style='color:#1d9d74;font-weight:bold;'>已处理</span>";
                    }
                }
            }
        ],
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: "#grid-pager-c",
        sortname: "id",
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
                    url: context_path + '/alarm/getAlarmById?id=' + sceneId,
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
