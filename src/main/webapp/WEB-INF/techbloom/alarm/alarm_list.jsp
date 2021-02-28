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
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="type" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:65px;">预警类型：</span>
                        <select name="type" id="type" style="width: calc(100% - 70px);">
                            <option value="">--请选择--</option>
                            <option value="0">BOM差异预警</option>
                            <option value="1">质检超时预警</option>
                            <option value="2">RFID未绑定预警</option>
                            <%--<option value="3">叉车取货预警</option>--%>
                            <option value="4">扫描门出库预警</option>
                            <option value="5">库存到期预警</option>
                            <option value="6">库存差异预警</option>
                        </select>
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="areaBy" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:65px;">预警库区：</span>
                        <select name="areaBy" id="areaBy" style="width: calc(100% - 70px);">
                            <option value="">--请选择--</option>
                            <c:forEach items="${areaList}" var="area">
                                <option value="${area.id}">${area.areaName}</option>
                            </c:forEach>
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

    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-editQx" onclick="stateDispose();">
            处理<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="lookAlarm();">
            查看<i class="icon-zoom-in" aria-hidden="true" style="margin-left:5px;"></i>
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

    $("#queryForm #type").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #type").on("change.select2",function(){
        $("#queryForm #type").trigger("keyup")}
    );

    $("#queryForm #areaBy").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #areaBy").on("change.select2",function(){
        $("#queryForm #areaBy").trigger("keyup")}
    );

    $(function () {
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            <%--{label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addScene, iconClass:'glyphicon glyphicon-plus'},--%>
            <%--{label: "编辑", disabled: (${sessionUser.editQx} == 1 ? false : true), onclick: editScene, iconClass:'glyphicon glyphicon-pencil'},--%>
            <%--{label: "删除", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: delScene, iconClass:'glyphicon glyphicon-trash'}--%>
        ]
    });
    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/alarm/getList",
            datatype: "json",
            colNames: ["主键", "预警编号", "预警开始时间", "预警处理时间", "预警时长(min)", "预警库区", "预警类型", "收件人", "发送方式", "发送内容", "状态"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "alarmCode", index: "alarmCode", width: 30},
                {
                    name: "startTime", index: "startTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "endTime", index: "endTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {name: "duration", index: "duration", width: 35},
                {name: "areaName", index: "areaName", width: 25, sortable: false},
                {
                    name: "type", index: "type", width: 30,
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
                {name: "sendContent", index: "sendContent", width: 80, sortable: false},
                {
                    name: "state", index: "state", width: 20,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:#d8120e;font-weight:bold;'>未处理</span>";
                        } else {
                            return "<span style='color:#1d9d74;font-weight:bold;'>已处理</span>";
                        }
                    }
                }
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

    // 查询
    function queryOk() {
        var queryParam = iTsai.form.serialize($("#queryForm"));
        querySceneByParam(queryParam);
    }

    function querySceneByParam(jsonParam) {
        // 将json对象反序列化到列表页面中隐藏的form中
        iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
        // 将json对象转换成json字符串
        var queryJsonString = JSON.stringify(queryParam);
        // 执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset() {
        $("#queryForm #type").select2("val", " ");
        $("#queryForm #areaBy").select2("val", " ");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 处理
    function stateDispose() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择要处理的预警！");
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个对象进行处理！");
            return false;
        } else {
            var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            layer.confirm("确定处理选中的预警？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/alarm/stateDispose?sceneId=" + sceneId,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("处理成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("处理失败", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }


    //查看
    function lookAlarm() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的预警！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个预警进行查看！");
            return false;
        } else {
            var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/alarm/lookAlarm?sceneId=" + sceneId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "预警查看",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["750px", "600px"],
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

</script>