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
                <span>填写入库单信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <form id="baseInfor" class="form-horizontal" target="_ifr">
            <!-- 入库单主键 -->
            <input type="hidden" id="id" name="id" value="${instorage.id}">
            <%--一行数据 --%>
            <div class="row" style="margin:0;padding:0;">
                <%--入库单编号--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="instorageBillCode">入库单编号：</label>
                    <div class="controls">
                        <div class="input-append  span12">
                            <input type="text" id="instorageBillCode" class="span10" name="instorageBillCode"
                                   placeholder="后台自动生成" readonly="readonly" value="${instorageBillCode}"/>
                        </div>
                    </div>
                </div>
                <%--出库单--%>
                <div id="outstorageBillDiv" class="control-group span6" style="display: none">
                    <label class="control-label" for="outstorageBillId">出库单：</label>
                    <div class="controls">
                        <div class="span12 required" style=" float: none !important;">
                            <input class="span10 select2_input" type="text" id="outstorageBillId"
                                   name="outstorageBillId" value="${instorage.outstorageBillId}">
                            <input type="hidden" id="outstorageBillCode" name="outstorageBillCode"
                                   value="${instorage.outstorageBillCode}">
                        </div>
                    </div>
                </div>
                <div id="crossDockingDiv" class="control-group span6" style="display: none">
                    <label class="control-label" for="crossDocking">是否越库：</label>
                    <div class="controls">
                        <div class="input-append span12 required">
                            <select name="crossDocking" id="crossDocking" value="${instorage.crossDocking}"
                                    style="width: calc(100% - 40px);">
                                <option value="">--请选择--</option>
                                <option value="0"
                                        <c:if test="${'0' eq instorage.crossDocking}">selected</c:if> >否
                                </option>
                                <option value="1"
                                        <c:if test="${'1' eq instorage.crossDocking}">selected</c:if> >是
                                </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin:0;padding:0;">
                <%--入库类型--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="instorageType">入库类型：</label>
                    <div class="controls">
                        <div class="input-append span12 required">
                            <c:choose>
                                <c:when test="${'0' eq instorage.instorageType}">
                                    <input type="hidden" id="instorageType" name="instorageType" value="${instorage.instorageType}">
                                    <select disabled="true"
                                            value="${instorage.instorageType}" style="width: calc(100% - 40px);">
                                        <option value="0"
                                                <c:if test="${'0' eq instorage.instorageType}">selected</c:if> >采购入库
                                        </option>
                                    </select>
                                </c:when>
                                <c:when test="${'1' eq instorage.instorageType}">
                                    <input type="hidden" id="instorageType" name="instorageType" value="${instorage.instorageType}">
                                    <select disabled="true"
                                            value="${instorage.instorageType}" style="width: calc(100% - 40px);">
                                        <option value="1"
                                                <c:if test="${'1' eq instorage.instorageType}">selected</c:if> >委托加工入库
                                        </option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <select name="instorageType" id="instorageType" name="instorageType"
                                            value="${instorage.instorageType}" style="width: calc(100% - 40px);">
                                        <option value="">--请选择--</option>
                                            <%--<option value="0" <c:if test="${'0' eq instorage.instorageType}">selected</c:if> >采购入库</option>--%>
                                            <%--<option value="1" <c:if test="${'1' eq instorage.instorageType}">selected</c:if> >委托加工入库</option>--%>
                                        <option value="2"
                                                <c:if test="${'2' eq instorage.instorageType}">selected</c:if> >生产退货入库
                                        </option>
                                            <%--<option value="4" <c:if test="${'4' eq instorage.instorageType}">selected</c:if> >收货入库</option>--%>
                                        <option value="3"
                                                <c:if test="${'3' eq instorage.instorageType}">selected</c:if> >其他入库
                                        </option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <%--入库流程--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="instorageProcess">入库流程：</label>
                    <div class="controls">
                        <div class="input-append span12 required">
                            <select name="instorageProcess" id="instorageProcess" value="${instorage.instorageProcess}"
                                    style="width: calc(100% - 40px);">
                                <option value="">--请选择--</option>
                                <option value="0"
                                        <c:if test="${'0' eq instorage.instorageProcess}">selected</c:if> >一般类型
                                </option>
                                <option value="1"
                                        <c:if test="${'1' eq instorage.instorageProcess}">selected</c:if> >白糖类型
                                </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin:0;padding:0;">
                <%--供应商--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="supplierCode">供应商：</label>
                    <div class="controls">
                        <div class="span12 required" style=" float: none !important;">
                            <input class="span10 select2_input" type="text" id="supplierCode" name="supplierCode"
                                   value="${instorage.supplierCode}">
                            <input type="hidden" id="supplierName" name="supplierName"
                                   value="${instorage.supplierName}">
                        </div>
                    </div>
                </div>
                <%--货主--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="customerCode">货主：</label>
                    <div class="controls">
                        <div class="span12 required" style=" float: none !important;">
                            <input class="span10 select2_input" type="text" id="customerCode" name="customerCode"
                                   value="${instorage.customerCode}">
                        </div>
                        <input type="hidden" id="customerName" name="customerName" value="${instorage.customerName}">
                    </div>
                </div>
            </div>
            <div class="row" style="margin:0;padding:0;">
                <%--&lt;%&ndash;收货单&ndash;%&gt;--%>
                <%--<div class="control-group span6" style="display: inline;visibility:hidden" id="shouhuo">--%>
                <%--<label class="control-label" for="receiptPlanId" >收货单：</label>--%>
                <%--<div class="controls">--%>
                <%--<div class="span12 required" style=" float: none !important;">--%>
                <%--<input class="span10 select2_input" type="text"  id="receiptPlanId" name="receiptPlanId" value="${instorage.receiptPlanId}">--%>
                <%--<input type="hidden" id="receiptCode" name="receiptCode" value="${instorage.receiptCode}">--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--备注--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="remark">备注：</label>
                    <div class="controls">
                        <div class="input-append span12">
                            <input class="span10" type="text" id="remark" name="remark" placeholder="备注"
                                   value="${instorage.remark}">
                        </div>
                    </div>
                </div>
            </div>

            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
            </div>
        </form>
    </div>

    <div class="step2" style="overflow-y: hidden">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name">
                <i class="instorage_circle badge-ok"><i class="ace-icon fa fa-check bigger-110"></i></i>
                <span>填写入库单信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <div id="materialDiv" style="margin:10px 5px;">
            <!-- 下拉框 -->
            <label class="inline" for="materialInfor">物料：</label>
            <input type="text" id="materialInfor" name="materialInfor" style="width:350px;margin-right:10px;"/>
            <button id="addMaterialBtn" class="btn btn-xs btn-primary" onclick="addDetail();">
                <i class="icon-plus" style="margin-right:6px;"></i>添加
            </button>
        </div>
        <!-- 表格div -->
        <div id="grid-div-c" style="width:100%;margin:0px;">
            <!-- 	表格工具栏 -->
            <button id="btchdel" class="btn btn-info" onclick="delDetail();" style="margin-bottom: 0.5rem;float: right">
                <i class="icon-minus" style="margin-right:6px;"></i>批量删除
            </button>
            <div style="clear: both;"></div>
            <!-- 物料详情信息表格 -->
            <div style="margin-top: 5px">
                <table id="grid-table-c" style="width:100%;height:100%;"></table>
            </div>

            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
        </div>

        <div class="flex-row justify-content-end"
             style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <%--<span class="btn btn-info" id="formSave">--%>
            <%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
            <%--</span>--%>
            <span class="btn btn-info ml-3" id="formSubmit">
				<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;审核并提交
			</span>
            <span class="btn btn-info ml-3" id="pre_step">
				<i class="ace-icon fa fa-arrow-left bigger-110"></i>&nbsp;上一步
			</span>
        </div>
    </div>
</div>

<script type="text/javascript">
    var context_path = '<%=path%>';
    var instorageType = "${instorage.instorageType}";
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/instorageBill/instorage_edit.js"></script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});
    var instorageId = $("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    var outstorageBillId = "${instorage.outstorageBillId}";
    var outstorageBillCode = "${instorage.outstorageBillCode}";
    var edit = "${edit}";
    var editBoolean = true;
    if (instorageType == "0" || instorageType == "1") {
        editBoolean = false;
    }

    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + "/instorage/getDetailList?instorageId=" + instorageId,
        datatype: "json",
        colNames: ["详情主键", "物料编号", "物料名称", "批次号", "生产日期", "包装单位", "入库数量", "入库重量（kg）", "操作"],
        colModel: [
            {name: "id", index: "id", width: 1, hidden: true},
            {name: "materialCode", index: "material_Code", width: 30},
            {name: "materialName", index: "material_Name", width: 30},
            {
                name: "batchNo", index: "batch_No", width: 60, editable: editBoolean,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            if (!strDateTime($("#" + $elementId).val())) {
                                layer.alert("请输入yymmdd的日期格式数据");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/instorage/updateBatchNo',
                                type: "POST",
                                data: {
                                    instorageDetailId: id,
                                    batchNo: $("#" + rowid + "_batchNo").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {
                name: "productDate", index: "product_Date", width: 20, editable: editBoolean,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("请输入YYYYMMDD的日期格式数据");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/instorage/updateProductDate',
                                type: "POST",
                                data: {
                                    instorageDetailId: id,
                                    productDate: $("#" + rowid + "_productDate").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {name: "unit", index: "unit", width: 17},
            {
                name: 'instorageAmount', index: 'instorage_Amount', width: 17, editable: editBoolean,
                editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的数量！(注：可以有两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/instorage/updateInstorageAmount',
                                type: "POST",
                                data: {
                                    instorageDetailId: id,
                                    instorageAmount: $("#" + rowid + "_instorageAmount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {
                name: "instorageWeight", index: "instorage_Weight", width: 27, editable: editBoolean,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的重量！(注：可以有两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/instorage/updateInstorageWeight',
                                type: "POST",
                                data: {
                                    instorageDetailId: id,
                                    instorageWeight: $("#" + rowid + "_instorageWeight").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {
                name: "del", index: "del", width: 10, formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    if (instorageType == "0" || instorageType == "1") {
                        return "<a style='text-decoration:underline;display: block;color: #EBE0EB'>删除</a>"
                    } else {
                        return "<a onclick='delOneDetail(" + id + ")' style='text-decoration:underline;display: block;'>删除</a>"
                    }
                }
            }
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: "#grid-pager-c",
        sortname: "material_code",
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
                    url: context_path + '/instorage/getDetailList?receiptId=' + receiptId,
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

    $(function () {
        if (edit == "edit") {
            if ($('#baseInfor').valid()) {
                setNextBtn();
            }
        }
    });

    /**
     * 验证是否位yymmdd日期格式
     * @param str
     */
    function strDateTime(str) {
        if (str == null || str == "" || str.length != 6) {
            return false;
        } else {
            var yy = str.substring(0, 2);
            var mm = str.substring(2, 4);
            var dd = str.substring(4);
            var d = new Date(yy, mm - 1, dd);

            return d.getYear() == yy && (d.getMonth() + 1) == mm && d.getDate() == dd;
        }
    }

    // //收货
    // $("#baseInfor #receiptCodeSelect").change(function () {
    //     $('#baseInfor #receiptCode').val($('#baseInfor #receiptCodeSelect').val());
    // });


    $('#baseInfor #instorageType').change(function () {
        if ($('#baseInfor #instorageType').val() == 4) {
            $("#shouhuo").removeAttrs("style", "visibility:hidden");
            $("#outstorageBillDiv").attr("style", "visibility:hidden");
            //加验证
            $('#boxForm #spareBillCodeSelect').rules('add', {required: true});
        } else if ($('#baseInfor #instorageType').val() == 2) {
            $("#shouhuo").attr("style", "visibility:hidden");
            $("#materialDiv #materialInfor").select2("readonly","readonly");
            $("#materialDiv #addMaterialBtn").attr("disabled","disabled");
            $("#btchdel").attr("disabled","disabled");
        } else {
            $("#shouhuo").attr("style", "visibility:hidden");
            $("#outstorageBillDiv").attr("style", "visibility:hidden");
        }
    })
</script>