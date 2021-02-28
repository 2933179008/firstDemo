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
    <form id="hiddenForm" action="<%=path%>/materielManage/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="matrixCode" action="<%=path%>/materielManage/matrixCode" method="POST" style="display: none;">
        <input id="id" name="id" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="materielCode" id="materielCodes" value=""/>
        <input name="materielName" id="materielNames" value="">
        <input name="customerBy" id="customerBys" value="">
        <input name="supplierBy" id="supplierBys" value="">
        <input name="producerBy" id="producerBys" value="">
    </form>
    <div class="query_box " id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="customerBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">货主：</span>
                            <select id="customerBy" name="customerBy" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option value="${customer.id}"
                                            <c:if test="${customer.customerCode eq customer.customerName}">selected</c:if> >${customer.customerName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="supplierBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">供应商：</span>
                            <select id="supplierBy" name="supplierBy" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${supplierList}" var="supplier">
                                    <option value="${supplier.id}"
                                            <c:if test="${supplier.supplierCode eq supplier.supplierName}">selected</c:if> >${supplier.supplierName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="producerBy" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">生产厂家：</span>
                            <select id="producerBy" name="producerBy" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${producerList}" var="producer">
                                    <option value="${producer.id}"
                                            <c:if test="${producer.producerCode eq producer.producerName}">selected</c:if> >${producer.producerName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="materielCode" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">物料编码：</span>
                            <input type="text" name="materielCode" id="materielCode" value=""
                                   style="width:70%;" placeholder="物料编码">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="materielName" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:65px;">物料名称：</span>
                            <input type="text" name="materielName" id="materielName" value=""
                                   style="width: 70%;" placeholder="物料名称">
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
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var _grid;

    $("#queryForm #customerBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #customerBy").on("change.select2", function () {
            $("#queryForm #customerBy").trigger("keyup")
        }
    );

    $("#queryForm #supplierBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #supplierBy").on("change.select2", function () {
            $("#queryForm #supplierBy").trigger("keyup")
        }
    );

    $("#queryForm #producerBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #producerBy").on("change.select2", function () {
            $("#queryForm #producerBy").trigger("keyup")
        }
    );

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
    ,
    {
        label: "导出", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:function () {
            var selectid = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            $("#ids").val(selectid);
            $("#hiddenForm").submit();
        }
    ,
        iconClass:' icon-share'
    }
    ,
    {
        label: "打印二维码", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:function () {
            var selectid = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            if (selectid == 0) {
                layer.alert("请选择一个要生成二维码的物料！");
                return false;
            } else {
                window.open(context_path + "/materielManage/matrixCode?id=" + selectid);
            }
        }
    ,
        iconClass:' icon-qrcode'
    }
    <%--{--%>
    <%--label: "生成二维码", disabled--%>
    <%--:--%>
    <%--(${sessionUser.queryQx} == 1 ? false : true--%>
    <%--),--%>
    <%--onclick:matrixCode, iconClass--%>
    <%--:--%>
    <%--' icon-qrcode'--%>
    <%--}--%>
    ]
    })
    ;


    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/materielManage/list.do",
            datatype: "json",
            colNames: ["主键", "物料编码", "物料名称", "物料条码", "物料规格", "包装单位", "保质期(天)", "助记码"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "materielCode", index: "materielCode", width: 30},
                {name: "materielName", index: "materielName", width: 30, sortable: false},
                {name: "barcode", index: "barcode", width: 30},
                {name: "spec", index: "spec", width: 20},
                {name: "unit", index: "unit", width: 20},
                {name: "qualityPeriod", index: "qualityPeriod", width: 20},
                {name: "mnemonicCode", index: "mnemonicCode", width: 20}
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
        $("#queryForm #customerBy").select2("val", " ");
        $("#queryForm #supplierBy").select2("val", " ");
        $("#queryForm #producerBy").select2("val", " ");
        $("#queryForm #materielCode").val("");
        $("#queryForm #materielName").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addPlatform() {
        $.post(context_path + "/materielManage/toEdit.do?id=-1&sign=0", {}, function (str) {
            $queryWindow = layer.open({
                title: "物料添加",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px", "550px"],
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
            layer.alert("请选择一个要编辑的物料！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个物料进行编辑操作！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/materielManage/toEdit.do?id=" + vehicleId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "物料编辑",
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
            $.post(context_path + "/materielManage/toDetail.do?id=" + vehicleId, {}, function (str) {
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

    // //生成二维码
    // function matrixCode() {
    //     var checkedNum = getGridCheckedNum("#grid-table", "id");
    //     if (checkedNum == 0) {
    //         layer.alert("请选择一个要生成二维码的物料！");
    //         return false;
    //     } else {
    //         var selectid = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
    //
    //         $.post(context_path + "/materielManage/matrixCode?id=" + selectid, {}, function (str) {
    //             $queryWindow = layer.open({
    //                 title: "物料二维码",
    //                 type: 1,
    //                 skin: "layui-layer-molv",
    //                 area: ["750px", "600px"],
    //                 shade: 0.6,
    //                 moveType: 1,
    //                 content: str,
    //                 success: function (layero, index) {
    //                     layer.closeAll("loading");
    //                 }
    //             });
    //         }).error(function () {
    //             layer.closeAll();
    //             layer.msg("加载失败！", {icon: 2});
    //         });
    //     }
    // }

    //删除
    function deletePlatform() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的物料！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的物料？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/materielManage/delMateriel?ids=" + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功!", {icon: 1, time: 1000});
                        } else {
                            layer.msg("删除失败!", {icon: 7, time: 2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

</script>