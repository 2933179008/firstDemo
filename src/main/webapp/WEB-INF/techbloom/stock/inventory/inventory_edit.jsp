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
                <span>填写盘点信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <form id="baseInfor" class="form-horizontal" target="_ifr">
            <input type="hidden" id="id" name="id" value="${inventory.id}"/>
            <input type="hidden" id="state" name="state" value="${inventory.state}"/>
            <input type="hidden" id="inPositionCode" name="inPositionCode" value="${inventory.positionCode}"/>
            <%--一行数据 --%>
            <div class="row" style="margin:0;padding:0;">
                <%--盘点编号--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryCode">盘点编号：</label>
                    <div class="controls">
                        <div class="input-append span12required">
                            <input type="text" class="span11" id="inventoryCode" name="inventoryCode"
                                   value="${inventoryCode}" placeholder="后台自动生成" readonly="readonly">
                        </div>
                    </div>
                </div>

                <%--盘点任务--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryTaskId">盘点任务：</label>
                    <div class="controls">
                        <div class="required span12" style="float: none !important;">
                            <select id="inventoryTaskId" name="inventoryTaskId" style="width:80%;">
                                <option value="">请选择</option>
                                <c:forEach items="${inventoryTaskList}" var="inventoryTask">
                                    <option value="${inventoryTask.id}"
                                            <c:if test="${inventoryTask.id eq inventory.inventoryTaskId}">selected</c:if> >${inventoryTask.inventoryTaskCode}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin:0;padding:0;">
                <%--处理时间--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryTime">处理时间：</label>
                    <div class="controls">
                        <div class="required  span12">
                            <input class="form-control date-picker span10" type="text" id="inventoryTime"
                                   name="inventoryTime" placeholder="处理时间" value="${inventory.inventoryTime}">
                        </div>
                    </div>
                </div>
                <%--盘点人员--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryUserId">处理人员：</label>
                    <div class="controls">
                        <div class="span12 required" style=" float: none !important;">
                            <select id="inventoryUserId" name="inventoryUserId" style="width:80%;">
                                <option value="">请选择</option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.userId}"
                                            <c:if test="${user.userId eq inventory.inventoryUserId}">selected</c:if> >${user.username}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin:0;padding:0;">
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="positionCode">库位：</label>
                    <div class="controls">
                        <div class="span12 required" style=" float: none !important;">
                            <input class="span10 select2_input" type="text" id="positionCode" name="positionCode"
                                   value="${inventory.positionCode}">
                        </div>
                    </div>
                </div>

                <%--备注--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="remark">备注：</label>
                    <div class="controls">
                        <div class="span12" style=" float: none !important;">
                            <input class="span10" type="text" id="remark" name="remark" placeholder="备注"
                                   value="${inventory.remark}">
                        </div>
                    </div>
                </div>

            </div>
            <div class="row" style="margin:0;padding:0;">


            </div>

            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <c:if test="${inventory.id!=null}">
        <span class="btn btn-info" id="next_step" style="margin-left: 5px" onclick="nextStep()">
            <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
        </span>
                </c:if>
                <c:if test="${inventory.id==null}">
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
                <span>填写盘点信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>添加物料</span>
            </div>
        </div>
        <div id="shelvesLocationDiv" style="margin: 10px;">
            <div class="row" style="margin:0;padding:0;">
                <div class="control-group span4" style="display: inline">
                    <label class="inline" for="materielType">物料类型：</label>
                    <select id="materielType" name="materielType" style="width:50%;">
                        <option value="1">有RFID</option>
                        <option value="0">无RFID</option>
                    </select>
                </div>
                <div id="rfidDiv">
                    <div class="control-group span4" style="display: inline">
                        <label class="inline" for="rfid">rfid：</label>
                        <select id="rfid" name="rfid" style="width:80%;">
                            <option value="">请选择</option>
                            <c:forEach items="${materielBindRfidList}" var="materielBindRfid">
                                <option value="${materielBindRfid.rfid}"
                                        <c:if test="${materielBindRfid.rfid eq materielBindRfid.bindCode}">selected</c:if> >${materielBindRfid.rfid}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div id="materielCodeDiv">
                    <div class="control-group span4" style="display: inline">
                        <label class="inline" for="materielCode">物料：</label>
                        <select id="materielCode" name="materielCode" style="width:50%;">
                            <option value="">请选择</option>
                            <c:forEach items="${materielList}" var="materiel">
                                <option value="${materiel.materielCode}"
                                        <c:if test="${materiel.materielCode eq materiel.materielName}">selected</c:if> >${materiel.materielName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <button id="addShelvesLocationBtn" class="btn btn-xs btn-primary" onclick="addShelvesLocation();">
                    <i class="icon-plus" style="margin-right:6px;"></i>添加
                </button>
            </div>
        </div>

        <!-- 表格div -->
        <div id="grid-div-c" style="width:100%;margin:10px auto;">
            <!-- 	表格工具栏 -->
            <button id="btchdel" class="btn btn-info" onclick="delInventoryDetail();"
                    style="margin-bottom: 0.5rem; float: right">
                <i class="icon-minus" style="margin-right:6px;"></i>批量删除
            </button>
            <div style="clear: both;"></div>
            <!-- 物料详情信息表格 -->
            <table id="grid-table-c" style="width:100%;height:100%;"></table>
            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
        </div>
        <div class="flex-row justify-content-end"
             style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <%--<span class="btn btn-info" id="formSave">--%>
            <%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
            <%--</span>--%>
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
    var id = $("#id").val();
    var inPositionCode = $("#inPositionCode").val();
    var oriDataDetail;
    var _grid_detail;        //表格对象

    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});

    /**
     * 表单校验
     * @param jsonParam
     */
    $("#baseInfor").validate({
        rules: {
            /*盘点任务*/
            "inventoryTaskId": {
                required: true,
                maxlength: 11
            },
            /*处理时间*/
            "inventoryTime": {
                required: true,
                maxlength: 20
            },
            /*处理人员*/
            "inventoryUserId": {
                required: true,
                maxlength: 11
            },
            /*库位*/
            "positionCode": {
                required: true,
                maxlength: 11
            }
        },
        messages: {
            "inventoryTaskId": {
                required: "请输入盘点任务！",
            },
            "inventoryTime": {
                required: "请输入处理时间！",
            },
            "inventoryUserId": {
                required: "请输入处理人员！",
            },
            "positionCode": {
                required: "请选择库位！",
            }

        },
        errorClass: "help-inline",
        errorElement: "span",
        highlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').addClass('error');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
        }
    });

    $("#shelvesLocationDiv #rfid").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#shelvesLocationDiv #rfid").on("change.select2", function () {
            $("#shelvesLocationDiv #rfid").trigger("keyup")
        }
    );

    $("#shelvesLocationDiv #materielCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#shelvesLocationDiv #materielCode").on("change.select2", function () {
            $("#shelvesLocationDiv #materielCode").trigger("keyup")
        }
    );

    $("#shelvesLocationDiv #positionNo").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#shelvesLocationDiv #positionNo").on("change.select2", function () {
            $("#shelvesLocationDiv #positionNo").trigger("keyup")
        }
    );

    $("#shelvesLocationDiv #batchNo").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#shelvesLocationDiv #batchNo").on("change.select2", function () {
            $("#shelvesLocationDiv #batchNo").trigger("keyup")
        }
    );

    //物料类型change事件
    $("#shelvesLocationDiv #materielType").on("change", function (e) {
        //物料类型下拉框值
        var materielTypeVal = $("#shelvesLocationDiv #materielType").val();
        if (materielTypeVal == "0") { //无RFID
            $("#shelvesLocationDiv #materielCode").val(null).trigger("change");

            $("#shelvesLocationDiv #materielCodeDiv").attr("style", "display: inline");
            $("#shelvesLocationDiv #rfidDiv").attr("style", "display: none");
        } else if (materielTypeVal == "1") { //有RFID
            $("#shelvesLocationDiv #rfid").val(null).trigger("change");

            $("#shelvesLocationDiv #rfidDiv").attr("style", "display: inline");
            $("#shelvesLocationDiv #materielCodeDiv").attr("style", "display: none");
        }
    });

    //库位下拉框列表
    $("#positionCode").select2({
        placeholder: "--请选择库位--",//文本框的提示信息
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        multiple: false,//false为单选框，true为复选框
        formatNoMatches: "没有结果",
        closeOnSelect: false,
        ajax: {
            url: context_path + "/inventory/getSelectPosition",
            dataType: "json",
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                selectParam = term;
                return {
                    inventoryTaskId: $("#baseInfor #inventoryTaskId").val(),
                    queryString: term, //联动查询的字符
                    pageSize: 15, //一次性加载的数据条数
                    pageNo: pageNo, //页码
                    time: new Date()
                    //测试
                }
            },
            results: function (data, pageNo) {
                var res = data.result;
                if (res.length > 0) { //如果没有查询到数据，将会返回空串
                    var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
                    return {
                        results: res,
                        more: more
                    };
                } else {
                    return {
                        results: {
                            "id": "0",
                            "text": "没有更多结果"
                        }
                    };
                }

            },
            cache: true
        }
    });
    //库位下拉框change事件
    $("#baseInfor #positionCode").on("change", function (e) {
        $("#baseInfor #positionCode").val(e.added.text);
    })

    //添加物料详情
    function addShelvesLocation() {
        if ($("#baseInfor #id").val() == "") {
            $("#materielCode").select2("val", "");
            layer.alert("请先保存表单信息！");
            return;
        }
        if ($("#baseInfor #state").val() > 0) {
            layer.msg("已提交，不可添加物料", {time: 1200, icon: 2});
            return false;
        }
        if ($("#materielType").val() == 1 && ($("#rfid").val() == "")) {
            layer.alert("请选择rfid！");
            return false;
        }
        if ($("#materielType").val() == 0 && ($("#materielCode").val() == "")) {
            layer.alert("请选择物料！");
            return false;
        }
        $.ajax({
            type: "POST",
            url: context_path + "/inventory/addInventoryDetail",
            data: {
                id: $("#baseInfor #id").val(),
                positionCode: inPositionCode,
                rfid: $("#shelvesLocationDiv #rfid").val(),
                materielCode: $("#shelvesLocationDiv #materielCode").val(),
                materielType: $("#shelvesLocationDiv #materielType").val(),
            },
            dataType: "json",
            success: function (data) {
                removeChoice();   //清空下拉框中的值
                if (data.result) {
                    layer.msg(data.msg, {icon: 1, time: 1200});
                    //重新加载详情表格
                    $("#grid-table-c").jqGrid("setGridParam", {
                        url: context_path + '/inventory/getDetailList?id=' + $("#baseInfor #id").val(),
                        postData: {queryJsonString: ""} //发送数据  :选中的节点
                    }).trigger("reloadGrid");
                    // _grid_detail.trigger("reloadGrid")
                } else {
                    layer.msg(data.msg, {icon: 2, time: 1200});
                    removeChoice();   //清空下拉框中的值
                }
            }
        });
    }

    //保存
    $("#formSave").click(function () {
        if ($("#baseInfor #state").val() == 1) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }
        if ($('#baseInfor').valid()) {
            $.ajax({
                url: context_path + "/inventory/saveInventory",
                type: "post",
                data: $("#baseInfor").serialize(),
                dataType: "JSON",
                success: function (data) {
                    if (data.result) {
                        $("#baseInfor #id").val(data.id);
                        //刷新收货单列表
                        $("#grid-table").jqGrid("setGridParam", {
                            postData: {queryJsonString: ""} //发送数据
                        }).trigger("reloadGrid");
                        setNextBtn();
                        //根据收货单主键id刷新查询收货单详情
                        $("#grid-table-c").jqGrid("setGridParam", {
                            url: context_path + '/inventory/getDetailList?id=' + id,
                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                        }).trigger("reloadGrid");
                        layer.msg("操作成功！", {icon: 1, time: 1200});
                    } else {
                        layer.msg(data.msg, {time: 1200, icon: 2});
                    }
                    _grid.trigger("reloadGrid");
                }
            });
        }
    });

    //提交
    $("#formSubmit").click(function () {
        if ($("#baseInfor #state").val() > 0) {
            layer.msg("提交已完成，不必重复提交", {time: 1200, icon: 2});
            return false;
        }
        if ($("#baseInfor #id").val() != "") {
            layer.confirm("提交后的数据将不能修改，确认提交吗？", function () {
                $.ajax({
                    url: context_path + "/inventory/submitInventory?id=" + $("#baseInfor #id").val(),
                    type: "post",
                    dataType: "JSON",
                    success: function (data) {
                        if (data.result) {
                            //刷新收货单列表
                            $("#grid-table-c").jqGrid("setGridParam", {
                                postData: {queryJsonString: ""} //发送数据
                            }).trigger("reloadGrid");
                            layer.closeAll();
                            layer.msg("提交成功", {icon: 1, time: 1500})
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 2500})
                        }
                        _grid.trigger("reloadGrid");
                    }
                });
            });
        } else {
            layer.msg("请先保存单据", {icon: 2, time: 1200})
        }
    })

    //清空物料多选框中的值
    function removeChoice() {
        $("#s2id_materielType .select2-choices").children(".select2-search-choice").remove();
        $("#materielType").select2("val", "");
        $("#s2id_rfid .select2-choices").children(".select2-search-choice").remove();
        $("#rfid").select2("val", "");
        $("#s2id_materielCode .select2-choices").children(".select2-search-choice").remove();
        $("#materielCode").select2("val", "");
    }

    //编辑页面初始化时，下拉框赋值
    if ($("#baseInfor #id").val() != "" && $("#baseInfor #id").val() != undefined && $("#baseInfor #id").val() != null) {
        $("#baseInfor #positionCode").select2("data", {
            id: $("#baseInfor #positionCode").val(),
            text: $("#baseInfor #positionCode").val()
        });
        $("#shelvesLocationDiv #materielCodeDiv").attr("style", "display: none");
    }else{
        $("#shelvesLocationDiv #materielCodeDiv").attr("style", "display: none");
    }

    //单元格编辑成功后，回调的函数
    var editFunction = function eidtSuccess(XHR) {
        var data = eval("(" + XHR.responseText + ")");
        if (data["msg"] != "") {
            layer.alert(data["msg"]);
        }
        jQuery("#grid-table-c").jqGrid('setGridParam',
            {
                postData: {
                    id: $('#id').val(),
                    queryJsonString: ""
                }
            }
        ).trigger("reloadGrid");
    };


    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + '/inventory/getDetailList?id=' + id,
        datatype: "json",
        colNames: ["盘点详情主键", "库位编号", "Rfid", "物料编号", "物料名称", "批次号", "盘点数量", "盘点重量（kg）", "操作"],
        colModel: [
            {name: 'id', index: 'id', width: 20, hidden: true},
            {
                name: 'positionCode', index: 'positionCode', width: 25, sortable: false, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',     //blur,focus,change.............
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                $.ajax({
                                    url: context_path + '/inventory/updatePositionCode',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        positionCode: $("#" + rowid + "_positionCode").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");

                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: 'rfid', index: 'rfid', width: 30, sortable: false, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',     //blur,focus,change.............
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                $.ajax({
                                    url: context_path + '/inventory/updateRfid',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        rfid: $("#" + rowid + "_rfid").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");

                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: 'materialCode', index: 'materialCode', width: 30, sortable: false, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',     //blur,focus,change.............
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                $.ajax({
                                    url: context_path + '/inventory/updateMaterialCode',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        materialCode: $("#" + rowid + "_materialCode").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");
                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: 'materialName', index: 'materialName', width: 30, sortable: false, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',     //blur,focus,change.............
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                $.ajax({
                                    url: context_path + '/inventory/updateMaterialName',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        materialName: $("#" + rowid + "_materialName").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");

                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: 'batchNo', index: 'batchNo', width: 70, sortable: false, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',     //blur,focus,change.............
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                $.ajax({
                                    url: context_path + '/inventory/updateBatchNo',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        batchNo: $("#" + rowid + "_batchNo").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");

                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: 'inventoryAmount', index: 'inventoryAmount', width: 30, editable: true,
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
                                url: context_path + '/inventory/updateInventoryAmount',
                                type: "POST",
                                data: {
                                    id: id,
                                    inventoryAmount: $("#" + rowid + "_inventoryAmount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/inventory/getDetailList?id=' + $("#baseInfor #id").val(),
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
                name: 'inventoryWeight', index: 'inventoryWeight', width: 35, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [
                        {
                            type: 'blur',
                            cellsubmit: "clientArray",
                            fn: function (e) {
                                var $element = e.currentTarget;
                                var $elementId = $element.id;
                                var rowid = $elementId.split("_")[0];
                                var id = $element.parentElement.parentElement.children[1].textContent;
                                var indocType = 1;
                                var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                                if (!reg.test($("#" + $elementId).val())) {
                                    layer.alert("非法的数量！(注：可以有两位小数的正实数)");
                                    return;
                                }
                                $.ajax({
                                    url: context_path + '/inventory/updateInventoryWeight',
                                    type: "POST",
                                    data: {
                                        id: id,
                                        inventoryWeight: $("#" + rowid + "_inventoryWeight").val()
                                    },
                                    dataType: "json",
                                    success: function (data) {
                                        if (!data.result) {
                                            layer.alert(data.message);
                                        }
                                        $("#grid-table-c").jqGrid('setGridParam',
                                            {
                                                url: context_path + '/inventory/getDetailList?id=' + $("#baseInfor #id").val(),
                                                postData: {queryJsonString: ""} //发送数据  :选中的节点
                                            }
                                        ).trigger("reloadGrid");

                                    }
                                });
                            }
                        }
                    ]
                }
            },
            {
                name: "del",
                index: "del",
                width: 10,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    if ($("#baseInfor #state").val() == 1) {
                        return "<a style='text-decoration:underline;display: block;color: #EBE0EB'>删除</a>"
                    } else {
                        return "<a onclick='delOneInventoryDetail(" + id + ")' style='text-decoration:underline;display: block;'>删除</a>"
                    }
                }
            }
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: '#grid-pager-c',
        sortname: 'id',
        sortorder: "asc",
        altRows: true,
        viewrecords: true,
        caption: "详情列表",
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
    }).navButtonAdd("#grid-pager-c", {
        caption: "",
        buttonicon: "ace-icon fa fa-refresh green",
        onClickButton: function () {
            $("#grid-table-c").jqGrid('setGridParam',
                {
                    url: context_path + '/transfer/detailList?id=' + id,
                    postData: {boxedId: $("#boxId").val()} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    if ($("#baseInfor #state").val() == 1) {
        $("#btchdel").attr("disabled", "disabled");
    }

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", 750 - 3);
        var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).height() +
            $("#baseInfor").outerHeight(true) + $("#materialDiv").outerHeight(true) + $("#shelvesLocationDiv").outerHeight(true) +
            $("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) +
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight', _grid_detail.parents(".layui-layer").height() - height - 250);
    });
    $(window).triggerHandler("resize.jqGrid");
    if ($("#id").val() != "") {
        //reloadDetailTableList();   //重新加载详情列表
    }


    //删除盘点详情
    function delInventoryDetail() {
        if ($("#baseInfor #state").val() == 1) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }
        var checkedNum = getGridCheckedNum("#grid-table-c", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的盘点详情！");
        } else {
            var ids = jQuery("#grid-table-c").jqGrid('getGridParam', 'selarrrow');
            $.ajax({
                url: context_path + '/inventory/delInventoryDetail?ids=' + ids,
                type: "POST",
                dataType: "JSON",
                success: function (data) {
                    if (data.result) {
                        layer.msg("操作成功！");
                        //重新加载详情表格
                        _grid_detail.trigger("reloadGrid");  //重新加载表格
                    }
                }
            });
        }
    }

    function setNextBtn() {
        _grid_detail.trigger("reloadGrid");
        $("#next_step").attr('class', 'btn btn-info');
        $("#next_step").on('click', function () {
            $(".step1").css('display', 'none');
            $(".step2").css('display', 'block');

        });
        $("#pre_step").on('click', function () {
            $(".step1").css('display', 'block');
            $(".step2").css('display', 'none');
        });

    }

    function nextStep() {
        _grid_detail.trigger("reloadGrid");
        $(".step1").css('display', 'none');
        $(".step2").css('display', 'block');
        $("#pre_step").on('click', function () {
            $(".step1").css('display', 'block');
            $(".step2").css('display', 'none');
        });
    }

    function delOneInventoryDetail(id) {
        if ($("#baseInfor #state").val() == 1) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }
        var ids = jQuery("#grid-table-c").jqGrid('getGridParam', 'selarrrow');
        $.ajax({
            url: context_path + '/inventory/delInventoryDetail?ids=' + id,
            type: "POST",
            dataType: "JSON",
            success: function (data) {
                if (data.result) {
                    layer.msg("操作成功！");
                    //重新加载详情表格
                    _grid_detail.trigger("reloadGrid");  //重新加载表格
                }
            }
        });

    }
</script>