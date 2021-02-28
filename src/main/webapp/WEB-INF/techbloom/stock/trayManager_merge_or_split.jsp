<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 托盘主键 -->
        <input type="hidden" id="id" name="id" value="${tray.id}">
        <input type="hidden" id="type" name="type" value="${tray.type}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--合并/拆分编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="mergeOrSplitCode">
                    <c:if test="${tray.type==0}">合并：</c:if>
                    <c:if test="${tray.type==1}">拆分：</c:if>
                </label>
                <div class="controls">
                    <div class="input-append required span12">
                        <input type="text" id="mergeOrSplitCode" class="span10" name="mergeOrSplitCode"
                               placeholder="后台自动生成"
                               readonly="readonly"
                               <c:if test="${tray.id==null}">value="${mergeOrSplitCode}" </c:if>
                               <c:if test="${tray.id!=null}">value="${tray.mergeOrSplitCode}"</c:if>/>
                    </div>
                </div>
            </div>
            <%--RFID--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="rfid">RFID：</label>
                <div class="controls">
                    <div class="input-append required span12" style=" float: none !important;">
                        <input type="text" class="span10" id="rfid" name="rfid" value="${tray.rfid}"
                               placeholder="RFID"/>
                    </div>
                </div>
            </div>
        </div>

        <%--二行数据--%>
        <div class="row" style="margin:0;padding:0;">
            <%--&lt;%&ndash;库位&ndash;%&gt;--%>
            <%--<div class="control-group span6" style="display: inline">--%>
            <%--<label class="control-label" for="positionBy">库位：</label>--%>
            <%--<div class="controls">--%>
            <%--<div class="input-append required span12" style=" float: none !important;">--%>
            <%--<select id="positionBy" name="positionBy" style="width:calc(100% - 42px);">--%>
            <%--<option value="">请选择</option>--%>
            <%--<c:forEach items="${depotPositionList}" var="position">--%>
            <%--<option value="${position.id}"--%>
            <%--<c:if test="${position.id eq tray.positionBy}">selected</c:if> >--%>
            <%--${position.positionName}</option>--%>
            <%--</c:forEach>--%>
            <%--</select>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--</div>--%>

            <%--备注--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="remarks">备注：</label>
                <div class="controls">
                    <div style="float: none !important;">
                        <input class="span10" type="text" id="remarks" name="remarks"
                               placeholder="备注" value="${tray.remarks}"/>
                    </div>
                </div>
            </div>


        </div>

        <%--三行数据--%>
        <div class="row" style="margin:0;padding:0;">

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="remarks"></label>
                <div class="controls">
                    <span class="btn btn-info" id="formSave">
                       <i class="ace-icon fa fa-check bigger-110"></i>保存
                    </span>
                </div>
            </div>

        </div>
    </form>
    <!-- 表格div -->
    <div id="grid-div-c" style="width:100%;margin:0px auto;">
        <form id="hiddenQueryForm1" style="display:none;">
            <input name="rfid" id="rfids" value=""/>
        </form>
        <div class="query_box" id="yy" title="查询选项">
            <form id="queryForm1" style="max-width:100%;">
                <ul class="form-elements">
                    <li class="field-group field-fluid3">
                        <label class="inline" for="rfid" style="margin-right:20px;width:200%;">
                            <span class="form_label" style="width:65px;">RFID：</span>
                            <input type="text" name="rfid" id="rfidd" value="" style="width: calc(100% - 70px);"
                                   placeholder="RFID编号">
                        </label>
                    </li>
                </ul>
                <div class="field-button">
                    <div class="btn btn-info" onclick="queryOk();">
                        <i class="ace-icon fa fa-check bigger-110"></i>查询
                    </div>
                </div>
            </form>
        </div>
        <!-- 物料详情信息表格 -->
        <table id="grid-table-c" style="width:100%;height:100%;"></table>
        <!-- 表格分页栏 -->
        <div id="grid-pager-c"></div>
        <div class="btn btn-info" id="formSubmit" style="float: right">
            <i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交
        </div>
    </div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
    $("#baseInfor").validate({
        rules: {
            /*物料编号*/
            "rfid": {
                required: true,
                <%--<c:if test="${tray.type==1}">--%>
                remote: context_path + "/trayManager/hasC?type=" +${tray.type},
                <%--</c:if>--%>
                maxlength: 20
            },
            /*库位*/
            "positionBy": {
                required: true,
                maxlength: 20
            },
            /*库位*/
            "status": {
                required: true,
                maxlength: 20
            },
            /*备注*/
            "remarks": {
                maxlength: 150
            }
        },
        messages: {
            "rfid": {
                <c:if test="${tray.type==0}">
                remote: "您输入的RFID不存在，请重新输入！",
                </c:if>
                <c:if test="${tray.type==1}">
                remote: "您输入的RFID已存在，请重新输入！",
                </c:if>
                required: "请输入RFID"
            },
            "positionBy": {
                required: "请选择库位"
            },
            "status": {
                required: "请选择状态"
            },
            "remarks": {
                maxlength: "您最多可以输入150个字符"
            }
        },
        errorClass:
            "help-inline",
        errorElement:
            "span",
        highlight:

            function (element, errorClass, validClass) {
                $(element).parents('.control-group').addClass('error');
            }

        ,
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
        }
    })
    ;
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/stock/trayManager_merge_or_split.js"></script>

<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var oriDataDetail;
    var _grid_detail;

    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/trayManager/getDetailList.do",
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "包装单位", "数量", "重量（kg）", "拆分数量", "拆分重量（kg）", "RFID"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "materielCode", index: "materielCode", width: 20},
            {name: "materielName", index: "materielName", width: 20},
            {name: "unit", index: "unit", width: 20},
            {name: "amount", index: "amount", width: 20},
            {name: "weight", index: "weight", width: 20},
            {
                name: 'number', index: 'number', width: 20, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            debugger;
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的重量！(注：只可为两位小数的正实数)");
                                return;
                            }
                            if ($("#baseInfor #id").val() == "") {
                                layer.alert("请先保存表单信息！");
                                return;
                            } else {
                                if ($("#" + rowid + "_number").val() != "") {
                                    $.ajax({
                                        url: context_path + '/trayManager/updateAmount.do',
                                        type: "POST",
                                        data: {
                                            id: id,
                                            number: $("#" + rowid + "_number").val(),
                                        },
                                        dataType: "json",
                                        success: function (data) {
                                            if (data.result) {
                                                layer.msg("数量操作成功！");

                                            } else {
                                                layer.msg(data.msg, {icon: 0, time: 1200});
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }]
                }
            },
            {
                name: 'mlWeight', index: 'mlWeight', width: 20, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            debugger;
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的重量！(注：只可为两位小数的正实数)");
                                return;
                            }
                            if ($("#baseInfor #id").val() == "") {
                                layer.alert("请先保存表单信息！");
                                return;
                            } else {
                                if ($("#" + rowid + "_mlWeight").val() != "") {
                                    $.ajax({
                                        url: context_path + '/trayManager/updateAmountAndWeight.do',
                                        type: "POST",
                                        data: {
                                            id: id,
                                            mlWeight: $("#" + rowid + "_mlWeight").val(),
                                            trayBy: $("#baseInfor #id").val(),
                                            rfidSelect: $("#queryForm1 #rfidd").val()
                                        },
                                        dataType: "json",
                                        success: function (data) {
                                            if (data.result) {
                                                layer.msg("重量操作成功！");
                                            } else {
                                                layer.msg(data.msg, {icon: 0, time: 1200});
                                            }
                                            _grid.trigger("reloadGrid");
                                        }
                                    });
                                }
                            }
                        }
                    }]
                }
            },
            {name: "rfid", index: "rfid", width: 20}
        ],
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: "#grid-pager-c",
        sortname: "rfid",
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
            oriDataDetail = data;
            $("#grid-table-c [aria-describedby='grid-table-c_number']").each(function (i, td) {
                if ($(td).html() && $(td).html() != "&nbsp;") {
                    $(td).addClass("not-editable-cell");
                }
            })
            $("#grid-table-c [aria-describedby='grid-table-c_mlWeight']").each(function (i, td) {
                if ($(td).html() && $(td).html() != "&nbsp;") {
                    $(td).addClass("not-editable-cell");
                }
            })
            $(window).triggerHandler('resize.jqGrid');
        },
        cellEdit: true,
        cellsubmit: "clientArray",
        emptyrecords: "没有相关记录",
        loadtext: "加载中...",
        pgtext: "页码 {0} / {1}页",
        recordtext: "显示 {0} - {1}共{2}条数据",
    });
    //在分页工具栏中添加按钮
    $("#grid-table-c").navGrid("#grid-pager-c", {
        edit: false,
        add: false,
        del: false,
        search: false,
        refresh: false
    }).navButtonAdd('#grid-pager-c', {
        caption: "",
        buttonicon: "ace-icon fa fa-refresh green",
        onClickButton: function () {
            $("#grid-table-c").jqGrid("setGridParam",
                {
                    url: context_path + '/trayManager/getDetailList.do',
                    postData: {queryJsonString: ""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3);
        var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).height() +
            $("#baseInfor").outerHeight(true) + $("#materialDiv").outerHeight(true) +
            $("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) +
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height - 100);
    });
    $(window).triggerHandler("resize.jqGrid");

    //提交
    $("#formSubmit").click(function () {
        if ($("#baseInfor #id").val() == '') {
            layer.alert("请先保存单据");
            return;
        } else {
            layer.confirm("确定提交,提交之后数据不能修改", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/trayManager/submitTray",
                    dataType: "json",
                    data: {id: $('#baseInfor #id').val()},
                    success: function (data) {
                        if (data.result) {
                            //刷新托盘管理列表
                            $("#grid-table").jqGrid("setGridParam", {
                                postData: {queryJsonString: ""} //发送数据
                            }).trigger("reloadGrid");
                            layer.closeAll();
                            layer.msg("提交成功", {icon: 1, time: 1200});
                        } else {
                            layer.alert(data.msg);
                        }
                    }
                })
            })
        }
    })
</script>