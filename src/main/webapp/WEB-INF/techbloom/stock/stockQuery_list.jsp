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

    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="materialCode" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:80px;">物料编号：</span>
                            <select id="materialCode" name="materialCode" style="width:70%;">
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
                            <span class="form_label" style="width:80px;">物料名称：</span>
                            <select id="materialName" name="materialName" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstMateriel}" var="materiel">
                                    <option value="${materiel.materielName}"
                                            <c:if test="${materiel.materielCode eq materiel.materielName}">selected</c:if> >${materiel.materielCode}-${materiel.materielName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="batchNo" style="margin-right:20px;width:90%;">
                            <span class="form_label" style="width:80px;">批 次 号：</span>
                            <input type="text" name="batchNo" id="batchNo"
                                   value="" style="width: calc(100% - 90px);" placeholder="批 次 号">
                        </label>
                    </li>

                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="positionCode" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:80px;">库位编码：</span>
                            <select id="positionCode" name="positionCode" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstDepotPosition}" var="depotPosition">
                                    <option value="${depotPosition.positionCode}"
                                            <c:if test="${depotPosition.positionCode eq depotPosition.positionType}">selected</c:if> >${depotPosition.positionCode}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="productDate" style="margin-right:20px;width:97%;">
                            <span class="form_label" style="width:80px;">生产日期：</span>
                            <input type="text" name="productDate" id="productDate"
                                   value="" style="width: calc(100% - 90px);" placeholder="生产日期">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="materialType" style="margin-right:20px;width:90%;">
                            <span class="form_label" style="width:80px;">货物类型：</span>
                            <select id="materialType" name="materialType" style="width:70%;">
                                <option value="">--请选择--</option>
                                <option value="0">无rfid</option>
                                <option value="1">有rfid</option>
                            </select>
                        </label>
                    </li>
                </div>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="qualityDate" style="margin-right:20px;width:97%;">
                            <span class="form_label" style="width:80px;">保质期至：</span>
                            <input type="text" name="qualityDate" id="qualityDate"
                                   value="" style="width: calc(100% - 90px);" placeholder="保质期至">
                        </label>
                    </li>

                    <li class="field-group field-fluid3">
                        <label class="inline" for="customerCode" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:80px;">货主：</span>
                            <select id="customerCode" name="customerCode" style="width:70%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${lstCustomer}" var="customer">
                                    <option value="${customer.customerCode}"
                                            <c:if test="${customer.customerCode eq customer.customerName}">selected</c:if> >${customer.customerName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </li>
                </div>

            </ul>
            <div class="field-button" style="">
                <div class="btn btn-info" onclick="queryOk();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>
            </div>
        </form>
    </div>

    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-editQx" onclick=" materielPowerTransfer();">
            货权转移 <i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
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

    $("#queryForm #materialCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #materialCode").on("change.select2", function () {
            $("#queryForm #materialCode").trigger("keyup")
        }
    );

    $("#queryForm #materialName").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #materialName").on("change.select2", function () {
            $("#queryForm #materialName").trigger("keyup")
        }
    );

    $("#queryForm #positionCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #positionCode").on("change.select2", function () {
            $("#queryForm #positionCode").trigger("keyup")
        }
    )

    $("#queryForm #customerCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #customerCode").on("change.select2", function () {
            $("#queryForm #customerCode").trigger("keyup")
        }
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
            <%--{label: "货权转移", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: materielPowerTransfer, iconClass:''}--%>
        ]
    });
    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/stockQuery/getList",
            datatype: "json",
            colNames: ["主键", "库位编码", "库位名称", /*"库位类型",*/ "物料编号", "物料名称", "货物类型", "批次号", "库存数量", "可用数量", "库存重量", "可用重量", "生产日期", "保质期至", "货主", "操作"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "positionCode", index: "positionCode", width: 30},
                {name: "positionName", index: "positionName", width: 30},
               /* {
                    name: "positionType", index: "positionType",  sortable: false,width: 30,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>地堆</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:blue;font-weight:bold;'>货架</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:green;font-weight:bold;'>不良品</span>";
                        } else if (cellValue == 3) {
                            return "<span style='color:red;font-weight:bold;'>暂存</span>";
                        } else {
                            return "<span style='color:#d15b47;font-weight:bold;'></span>";
                        }
                    }
                },*/
                {name: "materialCode", index: "materialCode", width: 30},
                {name: "materialName", index: "materialName", width: 30},
                {
                    name: "materialType", index: "materialType", width: 30,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:blue;font-weight:bold;'>无RFID</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:green;font-weight:bold;'>有RFID</span>";
                        } else {
                            return "<span style='color:#d15b47;font-weight:bold;'></span>";
                        }
                    }
                },
                {name: "batchNo", index: "batchNo", width: 80},
                {name: "stockAmount", index: "stockAmount", width: 30},
                {name: "availableStockAmount", index: "availableStockAmount", width: 30},
                {name: "stockWeight", index: "stockWeight", width: 30},
                {name: "availableStockWeight", index: "availableStockWeight", width: 30},
                {name: "productDate", index: "productDate", width: 30},
                {name: "qualityDate", index: "qualityDate", width: 30},
                {name: "customerName", index: "customerName", width: 50, sortable: false},
                {
                    name: 'operate',
                    index: 'operate',
                    width: 80,
                    sortable: false,
                    fixed: true,
                    formatter: function (cellvalue, option, rowObject) {
                        if (rowObject.materialType == 0) {
                            return "<span class='btn btn-minier ' style='background: #C0C0C0;color:white; ' " +
                                ">详情</span>";
                        } else {
                            return "<span class='btn btn-minier btn-success'  style='transition:background-color 0.3;-webkit-transition: background-color 0.3s;' " +
                                "onclick='stockQueryDetail(\"" + rowObject.id + "\")'>详情</span>";
                        }
                    }
                }
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "positionCode",
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
                - $("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true) - 45);
        });
        $(window).triggerHandler("resize.jqGrid");


    });

    // 查询
    function queryOk() {
        var queryParam = iTsai.form.serialize($("#queryForm"));
        querySceneByParam(queryParam);
    }

    function querySceneByParam(jsonParam) {

        var queryJsonString = JSON.stringify(jsonParam);
     // 执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset() {
        $("#queryForm #materialCode").select2("val", " ");
        $("#queryForm #materialName").select2("val", " ");
        $("#queryForm #positionCode").select2("val", " ");
        $("#queryForm #batchNo").val("");
        $("#queryForm #productDate").val("");
        $("#queryForm #materialType").val("");
        $("#queryForm #qualityDate").val("");
        $("#queryForm #customerCode").select2("val", " ");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //库存详情
    function stockQueryDetail(id) {
        $.post(context_path + "/stockQuery/toDetailView?id=" + id, {}, function (str) {
            $queryWindow = layer.open({
                title: "库存查询详情",
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

    //货权转移
    function materielPowerTransfer() {
        var id = jQuery("#grid-table").jqGrid('getGridParam', 'selarrrow');
        //获取行数据
        var rowData = $('#grid-table').jqGrid('getRowData', id);

        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一条货权转移的信息！");
        } else if (checkedNum > 1) {
            layer.alert("只能选择一条信息进行货权转移！");
            return false;
        } else if (rowData.availableStockAmount == 0) {
            layer.alert("无可用数量，不能进行货权转移!")
            return false;
        } else if (rowData.materialType=='<span style="color:green;font-weight:bold;">有RFID</span>') {
            var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/stockQuery/toRfidDetailView?id=" + id, {}, function (str) {
                $queryWindow = layer.open({
                    title: "货权转移页",
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
        } else {
            var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/stockQuery/toTransferView?id=" + id, {}, function (str) {
                $queryWindow = layer.open({
                    title: "货权转移页",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["750px", "600px"],
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

    }

</script>