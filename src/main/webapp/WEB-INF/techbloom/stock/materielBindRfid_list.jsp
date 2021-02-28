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
    <form id="hiddenQueryForm" style="display:none;">
        <input name="rfid" id="rfidS" value=""/>
        <input name="positionBy" id="positionByS" value=""/>
        <input name="status" id="statusS" value="">
        <input name="startTime" id="startTimeS" value="">
        <input name="endTime" id="endTimeS" value="">
        <input name="createBy" id="createByS" value="">
    </form>
    <div class="query_box " id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="rfid" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">RFID编码：</span>
                            <input type="text" name="rfid" id="rfid" value=""
                                   style="width: calc(100% - 97px);" placeholder="RFID编码">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="positionBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">库位：</span>
                            <select id="positionBy" name="positionBy" style="width:calc(100% - 97px);">
                                <option value="">-请选择-</option>
                                <c:forEach items="${depotPositionList}" var="position">
                                    <option value="${position.id}">${position.positionName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="status" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">状态：</span>
                            <select id="status" name="status" style="width:calc(100% - 97px);">
                                <option value="">-请选择-</option>
                                <option value="0">未入库</option>
                                <option value="1">已入库</option>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="startTime" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">开始时间：</span>
                            <input class="form-control date-picker" id="startTime" name="startTime"
                                   style="width: calc(100% - 97px);" type="text" placeholder="开始时间"/>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="endTime" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">截止时间：</span>
                            <input class="form-control date-picker" id="endTime" name="endTime"
                                   style="width: calc(100% - 97px);" type="text" placeholder="截止时间"/>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="createBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">操作人：</span>
                            <select id="createBy" name="createBy" style="width:calc(100% - 97px);">
                                <option value="">-请选择-</option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.userId}">${user.username}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>
                </div>
            </ul>
            <div class="field-button">
                <div class="btn btn-info" onclick="queryOk();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>
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
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD"});
    var context_path = '<%=path%>';
    var _grid;

    $("#queryForm #positionBy").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #positionBy").on("change.select2",function(){
        $("#queryForm #positionBy").trigger("keyup")}
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

    $("#queryForm #createBy").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #createBy").on("change.select2",function(){
        $("#queryForm #createBy").trigger("keyup")}
    );

    $(function () {
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick
    :
    addBinding, iconClass
    :
    'glyphicon glyphicon-plus'
    },
    {
        label: "编辑", disabled
    :
        (${sessionUser.editQx} == 1 ? false : true
    ),
        onclick: editBinding, iconClass
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
            url: context_path + "/materielBindRfid/list.do",
            datatype: "json",
            colNames: ["主键", "绑定编号", "RFID编码", "库区", "库位", "绑定时间", "操作人", "状态","入库类型", "备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "bindCode", index: "bindCode", width: 30},
                {name: "rfid", index: "rfid", width: 40},
                {name: "areaName", index: "areaName", sortable:false, width: 20},
                {name: "positionName", index: "positionName", sortable: false, width: 20},
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
                {name: "createName", index: "createName", sortable:false, width: 20},
                {
                    name: "status", index: "status", width: 20,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu == 0) {
                            return '<span style="color:#DD0000;font-weight:bold;">未入库</span>';
                        } else if (cellValu == 1) {
                            return '<span style="color:#1d9d74;font-weight:bold;">已入库</span>';
                        }
                    }
                },
                {
                    name: "instorageProcess", index: "instorageProcess", width: 20,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu == 0) {
                            return '<span style="color:#DD0000;font-weight:bold;">一般入库</span>';
                        } else if (cellValu == 1) {
                            return '<span style="color:#1d9d74;font-weight:bold;">白糖入库</span>';
                        }
                    }
                },
                {name: "remarks", index: "remarks", sortable:false, width: 40}
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

        if ($("#queryForm #startTime").val() != "" && $("#queryForm #endTime").val()=="") {
            layer.alert("请输入截止时间");
            return false;
        }
        if ($("#queryForm #endTime").val() != "" && $("#queryForm #startTime").val()=="") {
            layer.alert("请输入开始时间");
            return false;
        }

        if ($("#queryForm #endTime").val() != "" && $("#queryForm #startTime").val()) {
            //判断结束时间是否大于开始时间
            var startTime = $("#queryForm #startTime").val();
            var endTime = $("#queryForm #endTime").val();
            var d1 = new Date(startTime.replace(/\-/g, "\/"));
            var d2 = new Date(endTime.replace(/\-/g, "\/"));
            if (d1 > d2) {
                layer.alert("截止时间不能小于开始时间");
                return false;
            }
        }

        var queryParam = iTsai.form.serialize($('#queryForm'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
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
        $("#queryForm #positionBy").val(null).trigger("change");
        $("#queryForm #status").val(null).trigger("change");
        $("#queryForm #rfid").val("");
        $("#queryForm #startTime").val("");
        $("#queryForm #endTime").val("");
        $("#queryForm #createBy").val(null).trigger("change");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 添加
    function addBinding() {
        $.post(context_path + "/materielBindRfid/toEdit.do?id=-1&sign=0", {}, function (str) {
            $queryWindow = layer.open({
                title: "绑定添加",
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

    // 修改
    function editBinding() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
        var rowData = $("#grid-table").getRowData(sceneId);
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的对象！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个对象进行编辑操作！");
            return false;
        } else if (rowData.instorageProcess == '<span style="color:#DD0000;font-weight:bold;">一般入库</span>'
            && rowData.status == '<span style="color:#1d9d74;font-weight:bold;">已入库</span>') {
            layer.alert("一般入库，已入库数据不可进行编辑");
            return false;
        } else {
            $.post(context_path + "/materielBindRfid/toEdit.do?id=" + sceneId, {}, function (str) {
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
                layer.msg("已入库不可编辑！", {icon: 2});
            });
        }
    }

    // 删除
    function delBinding() {
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        if (ids == 0) {
            layer.alert("请选择要删除的绑定单据！");
        } else {
            layer.confirm("确定删除选中的绑定单据？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/materielBindRfid/delMaterielBindRfid?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("删除失败：已入库数据不可删除", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

    //查看
    function lookPlatform() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的物料！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个物料进行查看！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/materielBindRfid/toDetail.do?id=" + vehicleId, {}, function (str) {
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
</script>