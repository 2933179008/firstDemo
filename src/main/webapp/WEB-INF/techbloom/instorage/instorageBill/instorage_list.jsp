<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <style type="text/css"></style>
</head>
<body style="overflow:hidden;">
<div id="grid-div">
    <form id="hiddenForm" action="<%=path%>/instorage/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="instorageBillCode" id="instorageBillCodeHide" value="">
        <input name="instorageType" id="instorageTypeHide" value="">
        <input name="userName" id="userNameHide" value="">
        <input name="createTime" id="createTimeHide" value="">
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="instorageBillCode" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">入库单编号：</span>
                        <input type="text" id="instorageBillCode" name="instorageBillCode"
                               style="width: calc(100% - 97px);" placeholder="入库单编号">
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="instorageType" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:85px;">入库类型：</span>
                        <select id="instorageType" name="instorageType" style="width: calc(100% - 120px);">
                            <option value="">--请选择--</option>
                            <option value="0">采购入库</option>
                            <option value="1">委托加工入库</option>
                            <option value="2">生产退货入库</option>
                            <%--<option value="4">收货入库</option>--%>
                            <option value="3">其他入库</option>
                        </select>
                    </label>
                </li>

                <li class="field-group-top field-group field-fluid3">
                    <label class="inline" for="userName" 0style="margin-right:20px;width:10%;">
                        <span class="form_label" style="width:70px;">入库人：</span>
                        <input type="text" id="userName" name="userName" style="width: calc(100% - 97px);"
                               placeholder="入库人">
                    </label>
                </li>

                <div class="row" style="margin:0;padding:0;">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="createTime" style="margin-right:20px;width:100%;">
                            <span class="form_label" style="width:92px;">入库时间：</span>
                            <input type="text" class="form-control date-picker" id="createTime" name="createTime"
                                   style="width: calc(100% - 97px);" placeholder="入库时间"/>
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
    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/instorageBill/instorage_list.js"></script>
<script type="text/javascript">
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    var dynamicDefalutValue = "7eaf063c25564277ab90aea11790e3ac";

    $("#queryForm #instorageType").select2({
        minimumInputLength: 0,
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#queryForm #instorageType").on("change.select2", function () {
            $("#queryForm #instorageType").trigger("keyup")
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
    addInstorage, iconClass
    :
    'glyphicon glyphicon-plus'
    },
    {
        label: "编辑", disabled
    :
        (${sessionUser.editQx} == 1 ? false : true
    ),
        onclick:editInstorage, iconClass
    :
        'glyphicon glyphicon-pencil'
    }
    ,
    {
        label: "删除", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick:deleteInstorage, iconClass
    :
        'glyphicon glyphicon-trash'
    }
    ,
    {
        label: "查看", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:detailInstorage, iconClass
    :
        'icon-ok'
    }
    ,
    {
        label: "生成上架单", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:generatePutBill, iconClass
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
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            $("#ids").val(ids);
            $("#hiddenForm").submit();
        }
    ,
        iconClass:' icon-share'
    }
    ,
    {
        label:"打印", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:function () {
            print();
            // var queryBean = iTsai.form.serialize($('#hiddenQueryForm'));   //获取form中的值：json对象
            // var queryJsonString = JSON.stringify(queryBean);
            // var url = context_path + "/instorageOrder/exportPrint?"+"id=" + 1;
            // window.open(url);
        }
    ,
        iconClass:' icon-print'
    }

    ]
    })
    ;

    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/instorage/getInstorageList",
            datatype: "json",
            colNames: ["主键", "入库单编号", "收货计划单编号", "入库类型", "入库时间", "入库人", "状态","备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "instorageBillCode", index: "instorage_Bill_Code", width: 40},
                {name: "receiptCode", index: "receipt_Code", width: 40},
                {
                    name: "instorageType", index: "instorage_Type", width: 40,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>采购入库</span>";
                        } else if (cellValue == 1) {
                            return "<span style='font-weight:bold;'>委托加工入库</span>";
                        } else if (cellValue == 2) {
                            return "<span style='font-weight:bold;'>生产退货入库</span>";
                        } else if (cellValue == 3) {
                            return "<span style='font-weight:bold;'>其他入库</span>";
                        } else if (cellValue == 4) {
                            return "<span style='font-weight:bold;'>收货入库</span>";
                        }

                    }
                },
                {name: "createTime", index: "create_Time", width: 40},
                {name: "userName", index: "userName", width: 40},
                {
                    name: "state", index: "state", width: 40,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>未提交</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:blue;font-weight:bold;'>待入库</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:green;font-weight:bold;'>入库中</span>";
                        } else if (cellValue == 3) {
                            return "<span style='font-weight:bold;'>入库完成</span>";
                        }

                    }
                },
                {name: "remark", index: "remark", width: 40, sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "instorage_bill_code",
            sortorder: "desc",
            altRows: false,
            viewrecords: true,
            //caption : "入库单列表",
            autowidth: true,
            multiselect: true,
            multiboxonly: true,
            beforeRequest: function () {
                dynamicGetColumns(dynamicDefalutValue, "grid-table", $(window).width() - $("#sidebar").width() - 7);
                //重新加载列属性
            },
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
        //在分页工具栏中添加按钮
        jQuery("#grid-table").navGrid("#grid-pager", {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd('#grid-pager', {
            caption: "",
            buttonicon: "ace-icon fa fa-refresh green",
            onClickButton: function () {
                //jQuery("#grid-table").trigger("reloadGrid");  //重新加载表格
                $("#grid-table").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString: ""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        }).navButtonAdd("#grid-pager", {
            caption: "",
            buttonicon: "faicon-cogs",
            onClickButton: function () {
                jQuery("#grid-table").jqGrid("columnChooser", {
                    done: function (perm, cols) {
                        dynamicColumns(cols, dynamicDefalutValue);
                        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() - 3);
                    }
                });
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
        $("#queryForm #instorageBillCode").val("");
        $("#queryForm #instorageType").val("").trigger("change");
        $("#queryForm #userName").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addInstorage() {
        $.post(context_path + "/instorage/toEdit", {}, function (str) {
            $queryWindow = layer.open({
                title: "入库单添加",
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
    function editInstorage() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的入库单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个入库单进行编辑操作！");
            return false;
        } else {
            var instorageId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', instorageId).state;
            if (state != '<span style="font-weight:bold;">未提交</span>') {
                layer.msg("入库单已提交，不可修改", {time: 1200, icon: 2});
                return false;
            }

            // var instorageType = jQuery("#grid-table").jqGrid('getRowData', instorageId).instorageType;
            // if(instorageType=='<span style="font-weight:bold;">采购入库</span>' || instorageType=='<span style="font-weight:bold;">委托加工入库</span>'){
            //     layer.msg("采购入库和委托加工类型的单据不可修改",{time:3000,icon:2});
            //     return false;
            // }

            $.post(context_path + "/instorage/toEdit?instorageId=" + instorageId + "&edit=edit", {}, function (str) {
                $queryWindow = layer.open({
                    title: "入库单编辑",
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

    //查看
    function detailInstorage() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的入库单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个入库单进行查看操作！");
            return false;
        } else {
            var instorageId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/instorage/toDetail?instorageId=" + instorageId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "入库单详情",
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
    function deleteInstorage() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的入库单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个入库单进行删除操作！");
            return false;
        } else {
            //从数据库中删除选中的入库单，并刷新入库单表格
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            //弹出确认窗口
            layer.confirm("确定删除选中的入库单？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/instorage/deleteInstorage?id=" + id,
                    dataType: 'json',
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data.result) {
                            layer.msg(data.msg, {icon: 1, time: 1000});
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 1000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    //生成上架单
    function generatePutBill() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个入库单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个入库单进行操作！");
            return false;
        } else {
            var instorageId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', instorageId).state;
            if (state == '<span style="font-weight:bold;">未提交</span>') {
                layer.msg("入库单未提交，不可操作", {time: 1200, icon: 2});
                return false;
            }
            if (state == '<span style="font-weight:bold;">入库完成</span>') {
                layer.msg("入库单已入库完成，不可操作", {time: 1200, icon: 2});
                return false;
            }
            //弹出确认窗口
            layer.confirm("确定生成上架单？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/instorage/generatePutBill?instorageId=" + instorageId,
                    dataType: 'json',
                    cache: false,
                    success: function (data) {
                        layer.closeAll();
                        if (data.result) {
                            layer.msg(data.msg, {icon: 1, time: 1000});
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 1000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }
</script>
</html>