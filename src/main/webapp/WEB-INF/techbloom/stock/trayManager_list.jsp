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
    <form id="hiddenQueryForm" style="  display:none;">
        <input name="mergeOrSplitCode" id="mergeOrSplitCodes" value=""/>
        <input name="type" id="types" value=""/>
        <input name="status" id="statuss" value=""/>
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="mergeOrSplitCode" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:65px;">拆合编号：</span>
                        <input type="text" name="mergeOrSplitCode" id="mergeOrSplitCode" value=""
                               style="width: calc(100% - 70px);"
                               placeholder="合并/拆分编号">
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="type" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:50px;">类型：</span>
                        <select id="type" name="type" style="width:calc(100% - 120px);">
                            <option value="">--请选择--</option>
                            <option value="0">合并</option>
                            <option value="1">拆分</option>
                        </select>
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="status" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:50px;">状态：</span>
                        <select id="status" name="status" style="width:calc(100% - 120px);">
                            <option value="">--请选择--</option>
                            <option value="0">已提交</option>
                            <option value="1">未提交</option>
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

    $("#queryForm #status").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #status").on("change.select2",function(){
        $("#queryForm #status").trigger("keyup")}
    );


    $(function () {
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "合并", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick
    :
    trayMerge, iconClass
    :
    'glyphicon glyphicon-plus'
    },
    {
        label: "拆分", disabled
    :
        (${sessionUser.editQx} == 1 ? false : true
    ),
        onclick: traySplit, iconClass
    :
        'glyphicon glyphicon-pencil'
    }
    ,
    {
        label: "删除", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick: delBinding, iconClass
    :
        'glyphicon glyphicon-trash'
    }
    ,
    {
        label: "查看", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:lookPlatform, iconClass
    :
        'icon-zoom-in'
    }
    ]
    })
    ;
    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/trayManager/getList.do",
            datatype: "json",
            colNames: ["主键", "合并/拆分编号", "类型", "库位", "创建人", "创建时间", "状态","RFID", "备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "mergeOrSplitCode", index: "mergeOrSplitCode", width: 50},
                {
                    name: "type", index: "type", width: 20,
                    formatter: function (cellValu) {
                        if (cellValu == 0) {
                            return "<span style='color:#d8120e;font-weight:bold;'>合并</span>";
                        } else if (cellValu == 1) {
                            return "<span style='color:#1d9d74;font-weight:bold;'>拆分</span>";
                        }
                    }
                },
                {name: "positionName", index: "positionName",sortable:false, width: 30},
                {name: "createName", index: "createName",sortable:false, width: 30},
                {
                    name: "createTime", index: "createTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "status", index: "status", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu == 0) {
                            return "<span style='color:#DD0000;font-weight:bold;'>未提交</span>";
                        } else if (cellValu == 1) {
                            return "<span style='color:#1d9d74;font-weight:bold;'>已提交</span>";
                        }else {
                            return "<span font-weight:bold;'> </span>";
                        }
                    }
                },
                {name: "rfid", index: "rfid", width: 50},
                {name: "remarks", index: "remarks",sortable:false, width: 50}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "createTime",
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
                - $("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
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
        $("#queryForm #mergeOrSplitCode").val("");
        $("#queryForm #type").val(null).trigger("change");
        $("#queryForm #status").val(null).trigger("change");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 合并
    function trayMerge() {
        $.post(context_path + "/trayManager/toTrayMergeOrSplit?sceneId=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "托盘合并",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px", "680px"],
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

    // 拆分
    function traySplit() {
        $.post(context_path + "/trayManager/toTrayMergeOrSplit?sceneId=-2", {}, function (str) {
            $queryWindow = layer.open({
                title: "托盘拆分",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px", "680px"],
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

    //查看
    function lookPlatform() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的详情！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个详情进行查看！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/trayManager/toTrayDtail?sceneId=" + vehicleId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "物料查看",
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

    // 删除
    function delBinding() {
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        if (ids == 0) {
            layer.alert("请选择要删除的单据！");
        } else {
            layer.confirm("确定删除选中的单据？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/trayManager/delTray?ids=" + ids,
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

</script>