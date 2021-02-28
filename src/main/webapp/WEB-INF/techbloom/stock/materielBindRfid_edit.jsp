<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<link rel="stylesheet" href="<%=path%>/static/techbloom/system/scene/css/index.css"/>

<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <div class="step1">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name active">
                <i class="instorage_circle">1</i>
                <span>填写RFID信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <form id="baseInfor" class="form-horizontal" target="_ifr">
            <!-- 物料绑定RFId主键 -->
            <input type="hidden" id="id" name="id" value="${materielBindRfid.id}">
            <input type="hidden" id="status" name="status" value="${materielBindRfid.status}">
            <%--一行数据 --%>
            <div class="row" style="margin:0;padding:0;">
                <%--绑定编号--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="bindCode">绑定单号：</label>
                    <div class="controls">
                        <div class="input-append required span12">
                            <input type="text" id="bindCode" class="span10" name="bindCode" placeholder="后台自动生成"
                                   readonly="readonly"
                                   <c:if test="${materielBindRfid.id==null}">value="${bindCode}" </c:if>
                                   <c:if test="${materielBindRfid.id!=null}">value="${materielBindRfid.bindCode}"</c:if>/>
                        </div>
                    </div>
                </div>
                <%--RFID--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="rfid">RFID：</label>
                    <div class="controls">
                        <div class="input-append required span12" style=" float: none !important;">
                            <input type="text" class="span10" id="rfid" name="rfid" value="${materielBindRfid.rfid}"
                                   placeholder="RFID"
                                   <c:if test="${materielBindRfid.id!=null}">readonly="readonly"</c:if>/>
                        </div>
                    </div>
                </div>
            </div>

            <%--二行数据--%>
            <div class="row" style="margin:0;padding:0;">
                <%--&lt;%&ndash;库位&ndash;%&gt;--%>
                <%--<c:if test="${materielBindRfid.status!=1}">--%>
                <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label" for="positionBy">库位：</label>--%>
                <%--<div class="controls">--%>
                <%--<div class="input-append required span12" style=" float: none !important;">--%>
                <%--<select id="positionBy" name="positionBy" style="width:calc(100% - 42px);">--%>
                <%--<option value="">请选择</option>--%>
                <%--<c:forEach items="${depotPositionList}" var="position">--%>
                <%--<option value="${position.id}"--%>
                <%--<c:if test="${position.id eq materielBindRfid.positionBy}">selected</c:if> >--%>
                <%--${position.positionName}</option>--%>
                <%--</c:forEach>--%>
                <%--</select>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</c:if>--%>
                <%--备注--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="remarks">备注：</label>
                    <div class="controls">
                        <div class="input-append span12">
                            <input class="span10" type="text" id="remarks" name="remarks"
                                   placeholder="备注" value="${materielBindRfid.remarks}"
                                   style="width: calc(100% - 15px);"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <c:if test="${materielBindRfid.id!=null}">
                <span class="btn btn-info" id="next_step" style="margin-left: 5px" onclick="nextStep()">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
                </c:if>
                <c:if test="${materielBindRfid.id==null}">
                <span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
                </c:if>

            </div>
        </form>
    </div>
    <div class="step2" style="overflow-y: hidden">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name">
                <i class="instorage_circle badge-ok"><i class="ace-icon fa fa-check bigger-110"></i></i>
                <span>填写RFID信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <c:if test="${materielBindRfid.status!=1}">
            <div id="materialDiv" style="margin:10px;">
                <!-- 下拉框 -->
                <label class="inline" for="materialInfor">物料：</label>
                <input type="text" id="materialInfor" name="materialInfor" style="width:350px;margin-right:10px;"/>
                <button id="addMaterialBtn" class="btn btn-xs btn-primary" onclick="addDetail();">
                    <i class="icon-plus" style="margin-right:6px;"></i>添加
                </button>
            </div>
        </c:if>
        <!-- 表格div -->
        <div id="grid-div-c" style="width:100%;margin:0px auto;">
            <!-- 	表格工具栏 -->
            <c:if test="${materielBindRfid.status!=1}">
                <button id="btchdel" class="btn btn-info" onclick="delDetail();"
                        style="margin-bottom: 0.5rem; float: right">
                    <i class="icon-minus" style="margin-right:6px;"></i>批量删除
                </button>
                <div style="clear: both;"></div>
            </c:if>
            <c:if test="${materielBindRfid.status==1}">
                <br/>
            </c:if>
            <!-- 物料详情信息表格 -->
            <table id="grid-table-c" style="width:100%;height:100%;"></table>
            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
        </div>
        <div class="flex-row justify-content-end"
             style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <span class="btn btn-info" id="formSubmit">
                <i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交
            </span>
            <span class="btn btn-info ml-3" id="pre_step">
				<i class="ace-icon fa fa-arrow-left bigger-110"></i>&nbsp;上一步
			</span>
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
                maxlength: 20,
                <c:if test="${materielBindRfid.id==null}">
                remote: context_path + "/materielBindRfid/hasC?id=" + $('#id').val()
                </c:if>

            },
            /*库位*/
            "positionBy": {
                required: true,
                maxlength: 20
            },
            /*备注*/
            "remarks": {
                required: false,
                maxlength: 150
            }
        },
        messages: {
            "rfid": {
                required: "请输入RFID",
                remote: "您输入的RFID已经存在，请重新输入！"
            },
            "positionBy": {
                required: "请选择库位"
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
<script type="text/javascript" src="<%=path%>/static/techbloom/stock/materielBindRfid_edit.js"></script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var materielBindRfidBy = $("#baseInfor #id").val();
    var status = $("#baseInfor #status").val();
    var oriDataDetail;
    var _grid_detail;
    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/materielBindRfid/getDetailList?materielBindRfidBy=" + materielBindRfidBy,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "批次号", "库位", "规格包装", "数量", "重量（kg）", "操作"],
        colModel: [
            {name: "id", index: "id", width: 20, hidden: true},
            {name: "materielCode", index: "materielCode", width: 20},
            {name: "materielName", index: "materielName", width: 20},
            {name: "batchRule", index: "batchRule", width: 20},
            {name: "positionName", index: "positionName", width: 20},
            {name: "unit", index: "unit", width: 20},

            {
                name: 'amount', index: 'amount', width: 20, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的数量！(注：只可为两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/materielBindRfid/updateAmount',
                                type: "POST",
                                data: {
                                    materielBindRfidDetailBy: id,
                                    amount: $("#" + rowid + "_amount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (data.result) {
                                        layer.msg("数量操作成功！");
                                    } else {
                                        layer.msg(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/materielBindRfid/getDetailList?materielBindRfidBy=' + $("#baseInfor #id").val(),
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {
                name: 'weight', index: 'weight', width: 20, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的重量！(注：只可为两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/materielBindRfid/updateWeight',
                                type: "POST",
                                data: {
                                    materielBindRfidDetailBy: id,
                                    weight: $("#" + rowid + "_weight").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (data.result) {
                                        layer.msg("重量操作成功！");
                                    } else {
                                        layer.msg("重量操作失败！");
                                        layer.msg(data.msg)
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/materielBindRfid/getDetailList?materielBindRfidBy=' + $("#baseInfor #id").val(),
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {
                name: "del",
                index: "del",
                width: 20,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (status == "1") {
                        return "<a style='text-decoration:underline;display: block;color: #EBE0EB'>删除</a>"
                    } else {
                        return "<a onclick='delOneDetail(\"" + rowObject.id + "\")' style='text-decoration:underline;display: block;'>删除</a>"
                    }
                }
            }
        ],
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: "#grid-pager-c",
        sortname: "materielCode",
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
                    url: context_path + '/materielBindRfid/getDetailList?materielBindRfidBy=' + materielBindRfidBy,
                    postData: {queryJsonString: ""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", 750 - 3);
        var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).height() +
            $("#baseInfor").outerHeight(true) + $("#materialDiv").outerHeight(true) +
            $("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) +
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height - 200);
    });
    $(window).triggerHandler("resize.jqGrid");


    //提交
    $("#formSubmit").click(function () {
        layer.closeAll();
        layer.msg("提交成功", {icon: 1, time: 1200});
    })

</script>