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
        <input id="materialCodeS" name="materialCode" value=""/>
        <input id="materialNameS" name="materialName" value=""/>
        <input id="businessTypeS" name="businessType" value=""/>
        <input id="startTimeS" name="startTime" value=""/>
        <input id="endTimeS" name="endTime" value=""/>
        <input id="positionByS" name="positionBy" value=""/>
        <input id="changeCodeS" name="changeCode" value=""/>
    </form>

    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="startTime" style="margin-right:20px;width:90%;">
                            <span class="form_label" style="width:65px;">开始时间：</span>
                            <input type="text" name="startTime" id="startTime" class="form-control date-picker"
                                   value="" style="width: calc(100% - 90px);" placeholder="开始时间">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="endTime" style="margin-right:20px;width:90%;">
                            <span class="form_label" style="width:65px;">截止时间：</span>
                            <input type="text" name="endTime" id="endTime" class="form-control date-picker"
                                   value="" style="width: calc(100% - 90px);" placeholder="截止时间">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="positionBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">库位：</span>
                            <select id="positionBy" name="positionBy" style="width:63%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstDepotPosition}" var="depotPosition">
                                    <option value="${depotPosition.id}"
                                            <c:if test="${depotPosition.positionCode eq depotPosition.positionType}">selected</c:if> >${depotPosition.positionName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="materialCode" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">物料编号：</span>
                            <select id="materialCode" name="materialCode" style="width:62%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstMateriel}" var="materiel">
                                    <option value="${materiel.materielCode}"
                                            <c:if test="${materiel.materielCode eq materiel.materielName}">selected</c:if> >${materiel.materielCode}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="materialName" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">物料名称：</span>
                            <select id="materialName" name="materialName" style="width:62%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstMateriel}" var="materiel">
                                    <option value="${materiel.materielName}"
                                            <c:if test="${materiel.materielCode eq materiel.materielName}">selected</c:if> >${materiel.materielName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="businessType" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">业务类型：</span>
                            <select id="businessType" name="businessType" style="width:calc(100% - 120px);">
                                <option value="">--请选择--</option>
                                <option value="0">入库</option>
                                <option value="1">出库</option>
                                <option value="2">移位-出库</option>
                                <option value="3">移位-入库</option>
                                <option value="4">盘点报溢</option>
                                <option value="5">盘点报损</option>
                                <option value="6">拆分入库</option>
                                <option value="7">拆分出库</option>
                                <option value="8">合并入库</option>
                                <option value="9">合并出库</option>
                                <option value="10">货权转移出库</option>
                                <option value="11">货权转移入库</option>
                                <option value="12">越库入库</option>
                                <option value="13">越库出库</option>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="changeCode" style="margin-right:20px;width:83%;">
                            <span class="form_label" style="width:65px;">单据号：</span>
                            <input type="text" name="changeCode" id="changeCode" value=""
                                   style="width: calc(100% - 70px);" placeholder="单据号">
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

    $("#queryForm #materialName").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        //  width:200,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #materialName").on("change.select2",function(){
        $("#queryForm #materialName").trigger("keyup")}
    );

    $("#queryForm #materialCode").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        //  width:200,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #materialCode").on("change.select2",function(){
        $("#queryForm #materialCode").trigger("keyup")}
    );

    $("#queryForm #businessType").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        //  width:200,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #businessType").on("change.select2",function(){
        $("#queryForm #businessType").trigger("keyup")}
    );

    $("#queryForm #positionBy").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        //  width:200,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #positionBy").on("change.select2",function(){
        $("#queryForm #positionBy").trigger("keyup")}
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
            url: context_path + "/stockChange/getList",
            datatype: "json",
            colNames: ["主键", "单据编号", "物料编号", "物料名称", "批次号", "业务类型", "收入数量", "发出数量", "结余数量", "库存变动日期", "操作人"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "changeCode", index: "changeCode", width: 30},
                {name: "materialCode", index: "materialCode", width: 30},
                {name: "materialName", index: "materialName", width: 30},
                {name: "batchNo", index: "batchNo", width: 60},
                {
                    name: "businessType", index: "businessType", width: 30, formatter: function (cellvalue) {
                        if (cellvalue == "0") {
                            return "<span style='color:blue;font-weight:bold;'>入库</span>";
                        } else if (cellvalue == "1") {
                            return "<span style='color:darkcyan;font-weight:bold;'>出库</span>";
                        } else if (cellvalue == "2") {
                            return "<span style='color:#b33630;font-weight:bold;'>移位-出库</span>";
                        } else if (cellvalue == "3") {
                            return "<span style='color:mediumblue;font-weight:bold;'>移位-入库</span>";
                        } else if (cellvalue == "4") {
                            return "<span style='color:#d3a61a;font-weight:bold;'>盘点报溢</span>";
                        } else if (cellvalue == "5") {
                            return "<span style='color:#df8505;font-weight:bold;'>盘点报损</span>";
                        } else if (cellvalue == "6") {
                            return "<span style='color:#6d5ca1;font-weight:bold;'>拆分入库</span>";
                        } else if (cellvalue == "7") {
                            return "<span style='color:#4D5C73;font-weight:bold;'>拆分出库</span>";
                        } else if (cellvalue == "8") {
                            return "<span style='color:#4D5C73;font-weight:bold;'>合并入库</span>";
                        } else if (cellvalue == "9") {
                            return "<span style='color:#5D5F73;font-weight:bold;'>合并出库</span>";
                        }else if (cellvalue == "10") {
                            return "<span style='color:#5a4daa;font-weight:bold;'>货权转移出库</span>";
                        }else if (cellvalue == "11") {
                            return "<span style='color:#5a9ec2;font-weight:bold;'>货权转移入库</span>";
                        }else if (cellvalue == "12") {
                            return "<span style='color:#6AAEDF;font-weight:bold;'>越库入库</span>";
                        }else if (cellvalue == "13") {
                            return "<span style='color:#4D5F78;font-weight:bold;'>越库出库</span>";
                        }
                    }
                },
                {name: "inAmount", index: "inAmount", width: 30},
                {name: "outAmount", index: "outAmount", width: 30},
                {name: "balanceAmount", index: "balanceAmount", width: 30},
                {name: "createTime", index: "createTime", width: 40},
                {name: "createName", index: "createName", width: 30, sortable: false},
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
        if ($("#queryForm #startTime").val() == "") {
            layer.alert("请输入开始时间");
            return false;
        }
        if ($("#queryForm #endTime").val() == "") {
            layer.alert("请输入结束时间");
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
            } else {
                var queryParam = iTsai.form.serialize($('#queryForm'));
                //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
                querySceneByParam(queryParam);
            }
        }
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
        $("#queryForm #materialCode").val(null).trigger("change");
        $("#queryForm #materialName").val(null).trigger("change");
        $("#queryForm #businessType").val(null).trigger("change");
        $("#queryForm #startTime").val("");
        $("#queryForm #endTime").val("");
        $("#queryForm #positionBy").val(null).trigger("change");
        $("#queryForm #changeCode").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 添加
    function addScene() {
        $.post(context_path + "/scene/toEdit.do?sceneId=-1", {}, function (str) {
            $queryWindow = layer.open({
                title: "场景添加",
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
    function editScene() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的场景！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个场景进行编辑操作！");
            return false;
        } else {
            var sceneId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/scene/toEdit.do?sceneId=" + sceneId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "场景编辑",
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

    // 删除
    function delScene() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的场景！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的场景？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/scene/delScene?ids=" + ids,
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