<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<style type="text/css"></style>
<div id="grid-div">
    <form id="hiddenForm" action="<%=path%>/platform/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="type" id="types" value=""/>
        <input name="areaBy" id="areaBys" value=""/>
    </form>

    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-editQx" onclick="addBinding();">
            添加<i class="icon-plus" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="editBinding();">
            编辑<i class="icon-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="delBinding();">
            删除<i class="icon-trash" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="prohibitting();">
            禁用<i class="icon-remove" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="enableding();">
            启用<i class="icon-ok" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
    </div>

    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var _grid;

    $(function () {
        $(".toggle_tools").click();
    });
    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/alarmDeploy/getList.do",
            datatype: "json",
            colNames: ["主键", "预警类型", "收件人", "发送方式", "发送内容", "应用状态"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {
                    name: "alarmType", index: "alarmType", width: 30,
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
                            return "<span style='color:#3498DB;font-weight:bold;'>库存到期预警</span>";
                        } else if (cellValue == 6) {
                            return "<span style='color:#4d9dcc;font-weight:bold;'>库存差异预警</span>";
                        }
                    }
                },
                {name: "addressesNames", index: "addressesNames", width: 30, sortable: false},
                {
                    name: "sendType", index: "sendType", width: 20,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:#B8A608;font-weight:bold;'>短信</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:#d15b47;font-weight:bold;'>邮件</span>";
                        } else if (cellValue == 0, 1) {
                            return "<span style='color:#3d579d;font-weight:bold;'>短信&邮件</span>";
                        }
                    }
                },
                {name: "sendContent", index: "sendContent", width: 80},
                {
                    name: "apply", index: "apply", width: 20,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:red;font-weight:bold;'>禁用</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:green;font-weight:bold;'>启用</span>";
                        }
                    }
                },
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
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
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width());
            $("#grid-table").jqGrid("setGridHeight", $(".container-fluid").height() - $("#yy").outerHeight(true) - $("#fixed_tool_div").outerHeight(true) - $("#grid-pager").outerHeight(true)
                - $("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true) - 35);
        });


        $(window).triggerHandler("resize.jqGrid");
    });

    // 添加
    function addBinding() {
        $.post(context_path + "/alarmDeploy/toEdit.do?id=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "预警设置添加",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px", "700px"],
                shade: 0.6, //遮罩透明度
                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                content: str,//注意，如果str是object，那么需要字符拼接。
                success: function (layero, index) {
                    layer.closeAll("loading");
                }
            });
        }).error(function () {
            layer.closeAll();
            layer.msg("加载失败！", {icon: 2});
        });
    }

    // 编辑
    function editBinding() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的对象！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个对象进行编辑操作！");
            return false;
        } else {
            var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/alarmDeploy/toEdit.do?id=" + sceneId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "绑定编辑",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["750px", "700px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success: function (layero, index) {
                        layer.closeAll("loading");
                    }
                });
            }).error(function () {
                layer.closeAll();
                layer.msg("加载失败！", {icon: 2});
            });
        }
    }

    // 删除
    function delBinding() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");

        if (checkedNum == 0) {
            layer.alert("请选择要删除的预警设置！");
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个预警设置进行删除操作！");
            return false;
        } else {
            layer.confirm("确定删除选中的预警设置？", function () {
                var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
                $.ajax({
                    type: "POST",
                    url: context_path + "/alarmDeploy/delAlarmDeploy?id=" + id,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("删除失败", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    // 禁用
    function prohibitting() {
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        if (ids == 0) {
            layer.alert("请选择要禁用的预警设置！");
        } else {
            layer.confirm("确定禁用选中的预警设置？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/alarmDeploy/prohibitting?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("禁用成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("禁用失败", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    // 启用
    function enableding() {
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        if (ids == 0) {
            layer.alert("请选择要启用的预警设置！");
        } else {
            layer.confirm("确定启用选中的预警设置？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/alarmDeploy/enableding?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("启用成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("启用失败", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }


</script>