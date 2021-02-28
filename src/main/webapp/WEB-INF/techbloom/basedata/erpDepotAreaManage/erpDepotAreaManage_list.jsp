<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<style type="text/css"></style>
<div id="grid-div">
    <form id="hiddenForm" action="<%=path%>/erpDepotAreaManage/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="areaCode" id="areaCodeS" value=""/>
        <input name="areaName" id="areaNameS" value="">
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="areaCode" style="margin-right:25px;width:100%;">
                        <span class="form_label" style="width:80px;">库区编码：</span>
                        <input type="text" name="areaCode" id="areaCode" value=""
                               style="width: calc(100% - 120px);" placeholder="库区编码">
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="areaName" style="margin-right:25px;width:100%;">
                        <span class="form_label" style="width:80px;">库区名称：</span>
                        <input type="text" name="areaName" id="areaName" value=""
                               style="width: calc(100% - 120px);" placeholder="库区名称">
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
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var _grid;

    $(function () {
        $(".toggle_tools").click();
    });

    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick
    :
    addPlatform, iconClass
    :
    'glyphicon glyphicon-plus'
    },
    {
        label: "编辑", disabled
    :
        (${sessionUser.editQx} == 1 ? false : true
    ),
        onclick: editPlatform, iconClass
    :
        'glyphicon glyphicon-pencil'
    }
    ,
    {
        label: "删除", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick: deletePlatform, iconClass
    :
        'glyphicon glyphicon-trash'
    }
    ]
    })
    ;

    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/erpDepotAreaManage/list.do",
            datatype: "json",
            colNames: ["主键", "库区编码", "库区名称", "库区类型", "库位数（个）",  "备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "areaCode", index: "areaCode", width: 40},
                {name: "areaName", index: "areaName", width: 40},
                {name: "typeName", index: "typeName", width: 40,sortable: false},
                {name: "positionAmount", index: "positionAmount", width: 40},
                {name: "remark", index: "remark", width: 50}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "id",
            sortorder: "asc",
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

    //重置查询条件
    function reset() {
        $("#queryForm #areaCode").val("");
        $("#queryForm #areaName").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addPlatform() {
        $.post(context_path + "/erpDepotAreaManage/toEdit.do?id=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "库区添加",
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
            layer.alert("请选择一个要编辑的库区！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个库区进行编辑操作！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/erpDepotAreaManage/toEdit.do?id=" + vehicleId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "库区编辑",
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
            layer.alert("请选择一个要删除的库区！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的库区？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/erpDepotAreaManage/delDepotArea?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功！", {icon: 1, time: 1000});
                        } else {
                            layer.msg("选中的库区中包含已使用库位，不可删除！", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

</script>