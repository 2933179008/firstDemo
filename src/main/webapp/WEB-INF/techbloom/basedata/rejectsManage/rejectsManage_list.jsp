<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<style type="text/css"></style>
<div id="grid-div">
    <form id="hiddenForm" action="<%=path%>/platform/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="licensePlateNumber" id="licensePlateNumberId" value=""/>
        <input name="status" id="statusId" value="">
        <input name="createTime" id="createTimeId" value="">
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="type" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">不良品类型：</span>
                        <div class="select2-container" id="s2id_type" style="width: calc(100% - 97px);"><a
                                href="javascript:void(0)" class="select2-choice select2-default" tabindex="-1"> <span
                                class="select2-chosen" id="select2-chosen-20">请选择不良品类型</span><abbr
                                class="select2-search-choice-close"></abbr> <span class="select2-arrow"
                                                                                  role="presentation"><b
                                role="presentation"><i class="icon-angle-down"></i></b></span></a><label
                                for="s2id_autogen20" class="select2-offscreen"></label><input
                                class="select2-focusser select2-offscreen" type="text" aria-haspopup="true"
                                role="button" aria-labelledby="select2-chosen-20" id="s2id_autogen20"></div>
                        <input type="text" id="type" name="type" value=""
                               style="width: calc(100% - 97px); display: none;" placeholder="请选择不良品类型" tabindex="-1">
                    </label>
                </li>
                <li class="field-group field-fluid3">
                    <label class="inline" for="code" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">物料编号：</span>
                        <input type="text" id="code" name="code" style="width: calc(100% - 97px);" placeholder="物料编号">
                    </label>
                </li>
            </ul>
            <div class="field-button" style="">
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
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var _grid;

    $(function () {
        $(".toggle_tools").click();
    });

    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addPlatform, iconClass:'glyphicon glyphicon-plus'},
    {label: "编辑", disabled: (${sessionUser.editQx} == 1 ? false : true), onclick: editPlatform, iconClass:'glyphicon glyphicon-pencil'},
    {label: "删除", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: deletePlatform, iconClass:'glyphicon glyphicon-trash'},
    {label: "查看", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:editPlatform, iconClass:'icon-zoom-in'},
    {label: "导出", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function () {
        var selectid = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        $("#ids").val(selectid);
        $("#hiddenQueryForm").submit();
    },iconClass:' icon-share'
    },
    {label:"打印", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function(){
        var queryBean = iTsai.form.serialize($('#hiddenQueryForm'));   //获取form中的值：json对象
        var queryJsonString = JSON.stringify(queryBean);
        var url = context_path + "/instorageOrder/exportPrint?"+"id=" + 1;
        window.open(url);
    },
        iconClass:' icon-print'}
    ]
    });


    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/rejectsManage/list.do",
            datatype: "json",
            colNames: ["主键", "出/入库单编号", "物料条码", "库区", "不良品类型", "出/入库时间", "备注", "状态", "责任人"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "code", index: "code", width: 65},
                {name: "materielCode", index: "materielCode", width: 65},
                {name: "libraryArea", index: "libraryArea", width: 65},
                {name: "rejectsType", index: "rejectsType", width: 50,
                    formatter:function(cellValue){
                    if (cellValue==1) {
                        return "<span style='color:#B8A608;font-weight:bold;'>返修品</span>";
                    }else if (cellValue==3) {
                        return "<span style='color:#1E0FBE;font-weight:bold;'>报废品</span>";
                    }else {
                        return "<span style='color:#3a6d1b;font-weight:bold;'>退换物料</span>";
                    }
                    }},
                {name: "date", index: "date", width: 50},
                {name: "remarks", index: "remarks", width: 50},
                {name: "states", index: "states", width: 50},
                {name: "dutyPerson", index: "dutyPerson", width: 50}
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
                - $("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
        });

        $(window).triggerHandler("resize.jqGrid");
    });

    //查询
    function queryOk() {
        var queryParam = iTsai.form.serialize($("#queryForm"));
        queryPlatformByParam(queryParam);
    }

    function queryPlatformByParam(jsonParam) {
        iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset() {
        $("#queryForm #licensePlateNumber").val("");
        $("#queryForm #status").val("");
        $("#queryForm #createTime").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addPlatform() {
        $.post(context_path + "/rejectsManage/toEdit.do?vehicleId=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "不良品添加",
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

    //修改
    function editPlatform() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的车辆！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个车辆进行编辑操作！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/rejectsManage/toEdit.do?vehicleId=" + vehicleId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "不良品编辑",
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
                layer.msg("加载失败！", {icon: 2});
            });
        }
    }

    //删除
    function deletePlatform() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的车辆！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的车辆？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/vehicle/delVehicle?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (Boolean(data.result)) {
                            layer.msg(data.msg, {icon: 1, time: 1000});
                        } else {
                            layer.msg(data.msg, {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

</script>