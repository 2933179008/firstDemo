<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!--startprint-->
<div id="grid-div">
    <!-- 隐藏区域：存放查询条件 -->
    <form id="hiddenQueryForm" action="<%=path%>/outStoragePlan/exportExcel" method="POST" style="display:none;">
        <input name="ids" id="ids" value=""/>
        <input name="spare_bill_code" id="spare_bill_code" value=""/>
        <input id="product_no" name="product_no" value=""/>
    </form>
    <form id="hiddenForm" action = "<%=path%>/spareBillManager/toExcel" method = "POST" style="display: none;">
        <input id="excelids" name="excelids" value=""/>
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="spare_bill_code" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">备料单编号：</span>
                        <input type="text" id="spare_bill_code" name="spare_bill_code" value=""
                               style="width: calc(100% - 97px);" placeholder="备料单编号">
                    </label>
                </li>

                <li class="field-group field-fluid3">
                    <label class="inline" for="product_no" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">生产任务单号：</span>
                        <input type="text" id="product_no" name="product_no" value="" style="width: calc(100% - 97px);"
                               placeholder="生产任务单号">
                    </label>
                </li>

            </ul>
            <div class="field-button" style="">
                <div class="btn btn-info" onclick="queryOk();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <%--<a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>--%>
            </div>
        </form>
    </div>

    <div id="grid-div" style="width:100%;margin:0px auto;">
        <div id="fixed_tool_div" class="fixed_tool_div">
            <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
        </div>
        <table id="grid-table" style="width:100%;height:100%;overflow:auto;"></table>
        <div id="grid-pager"></div>
    </div>

</div>
<!--endprint-->
<script type="text/javascript">
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    var dynamicDefalutValue = "17e870237a2d470fb170daeafe6292e1";//列表码
    $(function () {
        $(".toggle_tools").click();
    });
    $("#queryForm .mySelect2").select2();
    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick
    :
    addOutPlan, iconClass
    :
    'icon-plus'
    },
    {
        label: "编辑", disabled
    :
        (${sessionUser.editQx} == 1 ? false : true
    ),
        onclick:editOutPlan, iconClass
    :
        'icon-pencil'
    }
    ,
    {
        label: "删除", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick:delOutPlan, iconClass
    :
        'icon-trash'
    }
    ,
    {
        label: "查看", disabled
    :
        (${sessionUser.queryQx} == 1 ? false : true
    ),
        onclick:detailSpareBill, iconClass
    :
        'icon-ok'
    }
    ,
    {
        label: "导出", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick:exportOutPlan, iconClass
    :
        'icon-share'
    }
    ,
    {
        label: "打印", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick:printOutPlan, iconClass
    :
        ' icon-print'
    }
    ,
    {
        label: "生成出库单", disabled
    :
        (${sessionUser.deleteQx} == 1 ? false : true
    ),
        onclick:createOutstorage, iconClass
    :
        ' icon-plus'
    }
    ,
    {
        label: "生产审核", hidden
    :
        (${sessionUser.productQX} == 0 ? false : true
    ),
        onclick:productAuditing, iconClass
    :
        ''
    }
    ,
    {
        label: "资材审核", hidden
    :
        (${sessionUser.materialQX} == 0 ? false : true
    ),
        onclick:materialAuditing, iconClass
    :
        ''
    }
    ,
    {
        label: "品质审核", hidden
    :
        (${sessionUser.qualityQX} == 0 ? false : true
    ),
        onclick:qualityAuditing, iconClass
    :
        ''
    }
    ]
    })
    ;

    _grid = jQuery("#grid-table").jqGrid({
        url: context_path + '/spareBillManager/getList',
        datatype: "json",
        colNames: ['主键', '备料单', '生产任务单号', '生产产品信息', '生产数量', '产线信息', '生产审核人', '审核时间', '审核状态', '资材审核人', '审核时间', '审核状态', '品质审核人', '审核时间', '审核状态', '状态', '备注'],
        colModel: [
            {name: 'id', index: 'id', width: 20, hidden: true},
            {name: 'spareBillCode', index: 'spareBillCode', width: 40},
            {name: 'productNo', index: 'productNo', width: 50},
            {name: 'productName', index: 'productName', width: 30},
            {name: 'totalProductAmount', index: 'totalProductAmount', width: 25},
            {name: 'mixUseLine', index: 'mixUseLine', width: 25},
            {name: 'productUser', index: 'productUser', width: 30},
            {name: 'productTime', index: 'productTime', width: 65},
            {
                name: 'productState', index: 'productState', width: 25,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu == 0) {
                        return "<input id = 'productState' type='hidden' value='0'><span style='color:red;font-weight:bold;'>未审核</span>";
                    }
                    if (cellValu == 1) {
                        return "<input id = 'productState' type='hidden' value='1'><span style='color:green;font-weight:bold;'>已审核</span>";
                    }
                }
            },
            {name: 'materialsUser', index: 'materialsUser', width: 30},
            {name: 'materialsTime', index: 'materialsTime', width: 65},
            {
                name: 'materialsState', index: 'materialsState', width: 25,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu == 0) {
                        return "<input id = 'materialsState' type='hidden' value='0'><span style='color:red;font-weight:bold;'>未审核</span>";
                    }
                    if (cellValu == 1) {
                        return "<input id = 'materialsState' type='hidden' value='1'><span style='color:green;font-weight:bold;'>已审核</span>";
                    }
                }
            },
            {name: 'qualityUser', index: 'qualityUser', width: 30},
            {name: 'qualityTime', index: 'qualityTime', width: 65},
            {
                name: 'qualityState', index: 'qualityState', width: 25,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu == 0) {
                        return "<input id = 'qualityState' type='hidden' value='0'><span style='color:red;font-weight:bold;'>未审核</span>";
                    }
                    if (cellValu == 1) {
                        return "<input id = 'qualityState' type='hidden' value='1'><span style='color:green;font-weight:bold;'>已审核</span>";
                    }
                }
            },
            {
                name: 'state', /*hidden:true,*/index: 'state', width: 40,
                formatter: function (cellValu, option, rowObject) {
                    if (cellValu == 0) {
                        return "<input id = 'state' type='hidden' value='0'><span style='color:red;font-weight:bold;'>未生成出库单</span>";
                    }
                    if (cellValu == 1) {
                        return "<input id = 'state' type='hidden' value='1'><span style='color:#3498DB;font-weight:bold;'>已生成部分出库单</span>";
                    }
                    if (cellValu == 2) {
                        return "<input id = 'state' type='hidden' value='2'><span style='color:blue;font-weight:bold;'>已生成出库单</span>";
                    }
                    if (cellValu == 3) {
                        return "<input id = 'state' type='hidden' value='3'><span style='color:orange;font-weight:bold;'>出库中</span>";
                    }
                    if (cellValu == 4) {
                        return "<input id = 'state' type='hidden' value='4'><span style='color:green;font-weight:bold;'>已完成</span>";
                    }
                }
            },
            {name: 'remark', index: 'remark', width: 60, sortable: false}
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: '#grid-pager',
        sortname: 'id',
        sortorder: "desc",
        altRows: false,
        viewrecords: true,
        autowidth: true,
        multiselect: true,
        multiboxonly: true,
        // beforeRequest:function (){
        //     dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
        //     //重新加载列属性
        // },
        loadComplete: function (data) {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            oriData = data;
            $(window).triggerHandler('resize.jqGrid');
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
        buttonicon: "fa icon-cogs",
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
        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() - 3);
        var height = $("#breadcrumb").outerHeight(true) + $(".query_box").outerHeight(true) +
            $("#fixed_tool_div").outerHeight(true) +
            $("#gview_grid-table .ui-jqgrid-hbox").outerHeight(true) +
            $("#grid-pager").outerHeight(true) + $("#header").outerHeight(true);
        $("#grid-table").jqGrid("setGridHeight", (document.documentElement.clientHeight) - height);
    })


    //  });
    var _queryForm_data = iTsai.form.serialize($('#queryForm'));

    function queryOk() {
        //var formJsonParam = $('#queryForm').serialize();
        var queryParam = iTsai.form.serialize($('#queryForm'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
        queryInstoreListByParam(queryParam);
    }

    function queryInstoreListByParam(jsonParam) {
        iTsai.form.deserialize($('#hiddenQueryForm'), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($('#hiddenQueryForm'));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table").jqGrid('setGridParam',
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    $('#queryForm .mySelect2').select2();
    $('#outstorageTypeSelect').change(function () {
        $('#queryForm #outType').val($('#outstorageTypeSelect').val());
    });

    $('#areaSelect').change(function () {
        $('#queryForm #areaId').val($('#areaSelect').val());
    });

    function reset() {
        iTsai.form.deserialize($("#queryForm"), _queryForm_data);
        queryInstoreListByParam(_queryForm_data);
    }

    $(".date-picker").datetimepicker({format: "YYYY-MM-DD", useMinutes: true, useSeconds: true});

    //添加
    function addOutPlan() {
        $.get(context_path + "/spareBillManager/toEdit?id=-1").done(function (data) {
            layer.open({
                title: "备料单添加",
                type: 1,
                skin: "layui-layer-molv",
                area: ['1250px', '750px'],
                shade: 0.6, //遮罩透明度
                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                anim: 2,
                content: data
            });
        });
    }

    //编辑
    function editOutPlan() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的备料单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单进行编辑操作！");
            return false;
        } else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).qualityState;
            var rowData1 = jQuery("#grid-table").jqGrid('getRowData', id).materialsState;
            var rowData2 = jQuery("#grid-table").jqGrid('getRowData', id).productState;
            var rowState = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var qualityState = $(rowData)[0].value;
            var materialsState = $(rowData1)[0].value;
            var productState = $(rowData2)[0].value;
            var state = $(rowState)[0].value;

            if (state != 0) {
                layer.alert("只能选择状态为未提交的数据进行编辑操作");
                return false;
            } else {
                if (qualityState == 1 || materialsState == 1 || productState == 1) {
                    layer.alert("单据已存在审核通过,不能进行编辑");
                } else {
                    $.get(context_path + "/spareBillManager/toEdit?id=" + id).done(function (data) {
                        layer.open({
                            title: "备料单编辑",
                            type: 1,
                            skin: "layui-layer-molv",
                            area: ['1250px', '750px'],
                            shade: 0.6, //遮罩透明度
                            moveType: 1, //拖拽风格，0是默认，1是传统拖动
                            anim: 2,
                            content: data
                        });
                    });
                }
            }
        }
    }

    //查看
    function detailSpareBill() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的备料单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单进行查看操作！");
            return false;
        } else {
            var spareBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/spareBillManager/toDetail?spareBillId=" + spareBillId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "备料单详情",
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

    /**
     * 刷新列表
     */
    function gridReload() {
        $("#grid-table").jqGrid('setGridParam',
            {
                url: context_path + '/spareBillManager/getList',
                postData: {queryJsonString: ""} //发送数据  :选中的节点
            }
        ).trigger("reloadGrid");
    }

    function delOutPlan() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的备料单！");
            return false;
        } else {
            var spareBillCode = "";
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selarrrow');
            for (let i = 0; i < ids.length; i++) {
                var rowData = $("#grid-table").jqGrid('getRowData', ids[i]);
                spareBillCode += rowData.spareBillCode + ",";
            }
            spareBillCode = spareBillCode.substring(0, spareBillCode.length - 1);
            layer.confirm("确定删除选中的备料单?", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/spareBillManager/deleteSpareList?ids=" + ids + "&spareBillCode=" + spareBillCode,
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            //弹出提示信息
                            layer.msg(data.msg);
                            //刷新列表
                            gridReload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            })
        }
    }


    /**
     * 生产审核
     */
    function productAuditing() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要审核的备料单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单进行审核！");
            return false;
        } else {
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', ids).productState;
            var productState = $(rowData)[0].value;
            if (productState == 1) {
                layer.alert("当前单据已经审核过,不能再进行审核");
            } else {
                $.ajax({
                    type: "POST",
                    url: context_path + "/spareBillManager/check",
                    data: {ids: ids, type: 1},
                    dataType: "json",
                    success: function (data) {
                        gridReload();
                        layer.msg(data.msg);
                    }
                })
            }
        }
    }

    /**
     * 资材审核
     */
    function materialAuditing() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要审核的备料单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单进行审核！");
            return false;
        } else {
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', ids).materialsState;
            var materialsState = $(rowData)[0].value;
            if (materialsState == 1) {
                layer.alert("当前单据已经审核过,不能再进行审核");
            } else {
                $.ajax({
                    type: "POST",
                    url: context_path + "/spareBillManager/check",
                    data: {ids: ids, type: 2},
                    dataType: "json",
                    success: function (data) {
                        gridReload();
                        layer.msg(data.msg);
                    }
                })
            }
        }
    }

    //生成出库单
    function createOutstorage() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个备料单生成出库单");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单生成出库单！");
            return false;
        } else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).qualityState;
            var rowData1 = jQuery("#grid-table").jqGrid('getRowData', id).materialsState;
            var rowData2 = jQuery("#grid-table").jqGrid('getRowData', id).productState;
            var rowState = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var qualityState = $(rowData)[0].value;
            var materialsState = $(rowData1)[0].value;
            var productState = $(rowData2)[0].value;
            var state = $(rowState)[0].value;
            if (state == 0 || state == 1) {
                if (qualityState == 0 || materialsState == 0 || productState == 0) {
                    layer.alert("存在未完成审批的单据,不能进行生成");
                } else {
                    $.get(context_path + "/spareBillManager/outStorage?id=" + id).done(function (data) {
                        layer.open({
                            title: "生成出库单",
                            type: 1,
                            skin: "layui-layer-molv",
                            area: ['850px', '600px'],
                            shade: 0.6, //遮罩透明度
                            moveType: 1, //拖拽风格，0是默认，1是传统拖动
                            anim: 2,
                            content: data
                        });
                    });

                    // layer.msg('请选择生成出库的单据类型', {
                    //     time: 0 //不自动关闭
                    //     , btn: ['有RFID单据', '无RFID单据']
                    //     , yes: function (index) {
                    //         var billType = 1;
                    //         $.ajax({
                    //             type: "POST",
                    //             url: context_path + "/spareBillManager/createOutStorage",
                    //             data: {
                    //                 id: id,
                    //                 billType: billType
                    //             },
                    //             dataType: "json",
                    //             success: function (data) {
                    //                 debugger;
                    //                 gridReload();
                    //                 layer.msg(data.msg);
                    //             }
                    //         })
                    //     }
                    //     ,btn2:function(){
                    //         var billType = 0;
                    //         $.ajax({
                    //             type: "POST",
                    //             url: context_path + "/spareBillManager/createOutStorage",
                    //             data: {
                    //                 id: id,
                    //                 billType: billType
                    //             },
                    //             dataType: "json",
                    //             success: function (data) {
                    //                 debugger;
                    //                 gridReload();
                    //                 layer.msg(data.msg);
                    //             }
                    //         })
                    //     }
                    // });
                }
            } else {
                layer.alert("当前单据已经生成出库单,不能继续生成");
            }
        }
    }


    /**
     * 品质审核
     */
    function qualityAuditing() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要审核的备料单！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个备料单进行审核！");
            return false;
        } else {
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', ids).qualityState;
            var qualityState = $(rowData)[0].value;
            if (qualityState == 1) {
                layer.alert("当前单据已经审核过,不能再进行审核");
            } else {
                $.ajax({
                    type: "POST",
                    url: context_path + "/spareBillManager/check",
                    data: {ids: ids, type: 3},
                    dataType: "json",
                    success: function (data) {
                        debugger;
                        gridReload();
                        layer.msg(data.msg);
                    }
                })
            }
        }
    }


    function exportOutPlan() {
        var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
        $("#excelids").val(ids);
        $("#hiddenForm").submit();
    }

    function printOutPlan() {
        /*bdhtml = window.document.body.innerHTML;
        sprnstr = "<!--startprint-->";
        eprnstr = "<!--endprint-->";
        prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + 17);
        prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
        window.document.body.innerHTML = prnhtml;
        window.print();*/
        print();
    }


</script>