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
    <form id="hiddenForm" action="<%=path%>/movePosition/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="movePositionCode" id="movePositionCodeS" value=""/>
        <input name="rfid" id="rfidS" value=""/>
        <input name="status" id="statusS" value=""/>
        <input name="movePositionTime" id="movePositionTimeS" value=""/>
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="movePositionCode" style="margin-right:20px;width:92%;">
                            <span class="form_label" style="width:65px;">移位单号：</span>
                            <input type="text" name="movePositionCode" id="movePositionCode" value=""
                                   style="width: calc(100% - 70px);" placeholder="移位单号">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="rfid" style="margin-right:20px;width:98%;">
                            <span class="form_label" style="width:45px;">RFID：</span>
                            <input type="text" name="rfid" id="rfid" value="" style="width: calc(100% - 70px);"
                                   placeholder="RFID">
                        </label>
                    </li>
                    <li class="field-group field-fluid3">
                        <label class="inline" for="status" style="margin-right:20px;width:92%;">
                            <span class="form_label" style="width:65px;">状态：</span>
                            <select name="status" id="status" style="width: calc(100% - 70px);">
                                <option value="">--请选择--</option>
                                <option value="0">未移位</option>
                                <option value="1">移位中</option>
                                <option value="2">已完成</option>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="movePositionTime" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">开始时间：</span>
                            <input class="form-control date-picker" id="movePositionTime" name="movePositionTime"
                                   style="width: calc(100% - 97px);" type="text" placeholder="开始时间"/>
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
    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-addQx" onclick="addMoveplace();">
            添加<i class="fa fa-plus" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="editMoveplace();">
            编辑<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-deleteQx" onclick="delMoveplace();">
            删除<i class="fa fa-trash" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="startMoveplace();">
            开始移位<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="ensureMoveplace();">
            移位确认<i class="fa fa-check" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class="col-md-1 btn btn-primary btn-queryQx" onclick="exportMovePosition();">
            导出<i class="fa fa-share" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="print();">
            打印<i class="icon-print" aria-hidden="true" style="margin-left:5px;"></i>
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
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD"});
    var context_path = '<%=path%>';
    var _grid;
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

    $("#queryForm #status").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #status").on("change.select2", function () {
            $("#queryForm #status").trigger("keyup")
        }
    );

    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/movePosition/getList.do",
            datatype: "json",
            colNames: ["主键", "变动绑定单号","移位类型", "RFID", "移出库位", "移入库位", "创建时间", "开始时间", "完成时间", "移位人员", "状态", "备注", "详情"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "movePositionCode", index: "movePositionCode", width: 30},
                {
                    name: "movePositionType", index: "movePositionType", width: 20,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:#B8A608;font-weight:bold;'>散货移位</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:#ACD128;font-weight:bold;'>整货移位</span>";
                        }
                    }
                },
                {name: "rfid", index: "rfid", width: 30},
                {name: "formerPositionName", index: "formerPositionName", width: 20, sortable: false},
                {name: "positionName", index: "positionName", width: 20, sortable: false},
                {
                    name: "moveFoundTime", index: "moveFoundTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "movePositionTime", index: "movePositionTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "completeTime", index: "completeTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm:ss");
                        } else {
                            return "";
                        }
                    }
                },
                {name: "moveUserName", index: "moveUserName", width: 20, sortable: false},
                {
                    name: "status", index: "status", width: 20,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:#B8A608;font-weight:bold;'>未移位</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:#ACD128;font-weight:bold;'>移位中</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:#76b86b;font-weight:bold;'>已完成</span>";
                        }
                    }
                },
                {name: "remarks", index: "remarks", width: 30},

                {
                    name: 'operate',
                    index: 'operate',
                    width: 80,
                    sortable: false,
                    fixed: true,
                    formatter: function (cellvalue, option, rowObject) {
                        return "<span class='btn btn-minier btn-success'  style='transition:background-color 0.3;-webkit-transition: background-color 0.3s;' " +
                            "onclick='movePositionDetail(\"" + rowObject.id + "\")'>详情</span>";
                    }
                }
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            multiSort: true,
            sortname: "status,moveFoundTime",
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
        $("#queryForm #movePositionCode").val("");
        $("#queryForm #rfid").val("");
        $("#queryForm #status").val(null).trigger("change");
        $("#queryForm #movePositionTime").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 添加
    function addMoveplace() {
        $.post(context_path + "/movePosition/toEdit?sceneId=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "移位添加",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px", "650px"],
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
    function editMoveplace() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的移位！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个移位进行编辑操作！");
            return false;
        } else {
            var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/movePosition/toEdit?sceneId=" + sceneId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "移位编辑",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["750px", "650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success: function (layero, index) {
                        layer.closeAll("loading");
                    }
                });
            }).error(function () {
                layer.closeAll();
                layer.msg("此RFID移位已完成或移位中，不可进行编辑！", {icon: 2});
            });
        }
    }

    // 删除
    function delMoveplace() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择要删除的移位！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的移位？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/movePosition/delMovePosition?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("失败原因：移位中/已完成状态单据不可删除!", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

    // 开始移位
    function startMoveplace() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要移位的数据！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定移位选中的数据？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/movePosition/statusMovePosition?sign=0&ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("状态更改成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("状态更改失败！", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    // 移位完成
    function ensureMoveplace() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要完成的移位数据！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定更改移位状态为已完成？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/movePosition/statusMovePosition?sign=1&ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("状态更改成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("状态更改失败！", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    //库存详情
    function movePositionDetail(id) {
        $.post(context_path + '/movePosition/toDetailView?id=' + id, {}, function (str) {
            $queryWindow = layer.open({
                title: "移位查询详情",
                type: 1,
                skin: "layui-layer-molv",
                area: ['800px', '700px'],
                shade: 0.6, //遮罩透明度
                shadeClose: true,
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

    /**
     * 移位导出
     */
    function exportMovePosition() {
        var selectid = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        $("#ids").val(selectid);
        $("#hiddenForm").submit();
    }

</script>