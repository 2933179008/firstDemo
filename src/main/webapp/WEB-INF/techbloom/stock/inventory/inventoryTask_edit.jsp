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
                <span>填写盘点任务信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>任务详情</span>
            </div>
        </div>
        <form id="baseInfor" class="form-horizontal" target="_ifr">
            <input type="hidden" id="id" name="id" value="${inventoryTask.id}"/>
            <input type="hidden" id="state" name="state" value="${inventoryTask.state}"/>
            <%--一行数据 --%>
            <div class="row" style="margin:0;padding:0;">
                <%--盘点编号--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryTaskCode">任务编号：</label>
                    <div class="controls">
                        <div class="span12" style=" float: none !important;">
                            <input type="text" class="span11" id="inventoryTaskCode" name="inventoryTaskCode"
                                   value="${inventoryTaskCode}" placeholder="后台自动生成" readonly="readonly"
                                   style="width: calc(100% - 44px);">
                        </div>
                    </div>
                </div>

                <%--盘点人员--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryUserId">盘点人员：</label>
                    <div class="controls">
                        <div class="input-append span12 required" style=" float: none !important;">
                            <select id="inventoryUserId" name="inventoryUserId" style="width:74%;">
                                <option value="">--请选择--</option>
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.userId}"
                                            <c:if test="${user.userId eq inventoryTask.inventoryUserId}">selected</c:if> >${user.username}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>

            </div>

            <div class="row" style="margin:0;padding:0;">
                <%--盘点类型--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="inventoryType">盘点类型：</label>
                    <div class="controls">
                        <div class="input-append span12 required" style="float: none !important;">
                            <select id="inventoryType" name="inventoryType" style="width:82%;">
                                <option value="">--请选择--</option>
                                <option value="0"
                                        <c:if test="${'0' eq inventoryTask.inventoryType}">selected</c:if> >库位盘点
                                </option>
                                <option value="1"
                                        <c:if test="${'1' eq inventoryTask.inventoryType}">selected</c:if> >动碰盘点
                                </option>
                            </select>
                        </div>
                    </div>
                </div>

                <%--盘点库位编码;--%>
                <div class="control-group span6" style="display: inline">
                    <div id="positionCodeDiv">
                        <label class="control-label" for="positionCode">库位编码：</label>
                        <div class="controls">
                            <div class="span12 required">
                                <input type="text" id="positionCode" name="positionCode" class="span10" value=""
                                       style="width: calc(100% - 110px);">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin:0;padding:0;">
                <%--备注--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="remark">备注：</label>
                    <div class="controls">
                        <div class="span12" style=" float: none !important;">
                            <input class="span10" type="text" id="remark" name="remark" value="${inventoryTask.remark}"
                                   placeholder="备注" style="width: calc(100% - 44px);">
                        </div>
                    </div>
                </div>

                <%--盘点时间;--%>
                <div class="control-group span6" style="display: inline">
                    <div id="positionStartDiv">
                        <%--<span class="form_label" style="width:65px;">截止时间：</span>--%>
                        <label class="control-label" for="inventoryTime">开始时间：</label>
                        <div class="controls">
                            <div class="input-append span12 required">
                                <input class="form-control date-picker" id="inventoryTime" name="inventoryTime"
                                       type="text"
                                       value="${inventoryTask.inventoryTime}" placeholder="盘点时间"
                                       style="width: calc(100% - 105px);">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin:0;padding:0;">
                <%--结止时间--%>
                <div class="control-group span6" style="display: inline">
                    <div id="positionEndDiv">
                        <label class="control-label" for="completeTime">截止时间：</label>
                        <div class="controls">
                            <div class="input-append span12 required" style=" float: none !important;">
                                <input class="form-control date-picker" type="text" id="completeTime"
                                       name="completeTime"
                                       value="${inventoryTask.completeTime}"
                                       placeholder="截止时间" style="width: calc(100% - 70px);">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%--<div style="margin-left:10px;padding-bottom:10px;">
                <span class="btn btn-info" id="formSave">
                   <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <span class="btn btn-info" id="formSubmit">
                   <i class="ace-icon fa fa-check bigger-110"></i>提交
                </span>
                <span class="btn btn-info" onclick="auditTaskCheck()">
                   <i class="ace-icon fa fa-check bigger-110"></i>审核
                </span>
            </div>--%>
            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
            </div>

            <%--function setNextBtn() {--%>
            <%--$("#next_step").attr('class','btn btn-info');--%>
            <%--$("#next_step").on('click',function(){--%>
            <%--_grid_detail.trigger("reloadGrid");--%>
            <%--$(".step1").css('display','none');--%>
            <%--$(".step2").css('display','block');--%>

            <%--});--%>
            <%--$("#pre_step").on('click',function(){--%>
            <%--$(".step1").css('display','block');--%>
            <%--$(".step2").css('display','none');--%>
            <%--});--%>

            <%--}--%>
        </form>
    </div>
    <!-- 表格div -->
    <div class="step2" style="overflow-y: hidden">
        <div class="flex-row justify-content-around step_title">
            <div class="flex-row align-items-center justify-content-end step_name">
                <i class="instorage_circle badge-ok"><i class="ace-icon fa fa-check bigger-110"></i></i>
                <span>填写盘点任务信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>任务详情</span>
            </div>
        </div>
        <div id="grid-div-c" style="width:100%;margin:10px auto;">
            <!-- 	表格工具栏 -->
            <button id="btchdel" class="btn btn-info" onclick="delDetail();" style="margin-bottom: 0.5rem;float: right">
                <i class="icon-minus" style="margin-right:6px;"></i>批量删除
            </button>
            <div style="clear: both;"></div>
            <!-- 物料详情信息表格 -->
            <table id="grid-table-c" style="width:100%;height:100%;"></table>
            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <span class="btn btn-info" id="formSubmit">
				<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交
			</span>
                <span class="btn btn-info ml-3" onclick="auditTaskCheck()">
		       <i class="ace-icon fa fa-check bigger-110"></i>审核
            </span>
                <span class="btn btn-info ml-3" id="pre_step">
				<i class="ace-icon fa fa-arrow-left bigger-110"></i>&nbsp;上一步
			</span>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var id = $("#id").val();
    var stateVal = $("#baseInfor #state").val();
    var oriDataDetail;
    var _grid_detail;        //表格对象

    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});

    $("#baseInfor").validate({
        ignore: "",
        rules: {
            /*盘点类型*/
            "inventoryType": {
                required: true,
                maxlength: 1
            },
            /*盘点人员*/
            "inventoryUserId": {
                required: true,
                maxlength: 11
            }
        },
        messages: {
            "inventoryType": {
                required: "请输入盘点类型！",
            },
            "inventoryUserId": {
                required: "请输入盘点人员！",
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

    // 库位编码
    $("#positionCode").select2({
        placeholder: "--请选择库位--",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        multiple: true,
        ajax: {
            url: context_path + "/inventoryTask/getSelectPositionCode",
            type: "POST",
            dataType: "json",
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    baseStationName: term, //联动查询的字符
                    positionCode: $("#positionCode").val()
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
                        results: {}
                    };
                }
            },
            cache: true
        }
    });

    // 修改时加载库位信息
    if ('${inventoryTask.id}') {
        // 用户赋值
        $.ajax({
            url: context_path + "/inventoryTask/getPositionInfo",
            type: "post",
            dataType: "JSON",
            data: {
                positionCodes: '${inventoryTask.positionCode}'
            },
            async: false,
            success: function (data) {
                if (data) {
                    var obj = [];
                    for (let i = 0; i < data.length; i++) {
                        obj.push({
                            id: data[i].userId,
                            text: data[i].name
                        });
                    }
                    $("#positionCode").select2("data", obj);
                }
            }
        });
    }

    //盘点类型change事件
    $("#baseInfor #inventoryType").on("change", function (e) {
        //盘点类型下拉框值
        var inventoryTypeVal = $("#baseInfor #inventoryType").val();
        if (inventoryTypeVal == "0") {//库位盘点
            $("#baseInfor #positionEndDiv").attr("style", "display: none");
            $("#baseInfor #positionStartDiv").attr("style", "display: none");
            $("#baseInfor #positionCodeDiv").attr("style", "display: inline");
            $('#positionCode').prop("required", true);
            $('#inventoryTime').prop("required", false);
            $('#completeTime').prop("required", false);
        } else {//动碰盘点
            $("#baseInfor #positionEndDiv").attr("style", "display: inline");
            $("#baseInfor #positionStartDiv").attr("style", "display: inline");
            $("#baseInfor #positionCodeDiv").attr("style", "display: none");
            $('#inventoryTime').prop("required", true);
            $('#completeTime').prop("required", true);
            $('#positionCode').prop("required", false);
        }
    });

    //编辑页面初始化时，下拉框赋值
    if ($("#baseInfor #id").val() != "" && $("#baseInfor #id").val() != undefined && $("#baseInfor #id").val() != null) {
        setNextBtn();
        if ($("#baseInfor #state").val() > 0) {
            $("#btchdel").attr("disabled", "disabled");
        }
        //盘点任务类型下拉框值
        var inventoryTypeVal = $("#baseInfor #inventoryType").val();
        if (inventoryTypeVal == "0") {//库位盘点
            $("#baseInfor #positionEndDiv").attr("style", "display: none");
            $("#baseInfor #positionStartDiv").attr("style", "display: none");
        } else {//动碰盘点类型
            $("#baseInfor #positionCodeDiv").attr("style", "display: none");
        }
    } else {//添加页面初始化时，下拉框赋值
        $("#baseInfor #positionEndDiv").attr("style", "display: none");
        $("#baseInfor #positionStartDiv").attr("style", "display: none");
        $("#baseInfor #positionCodeDiv").attr("style", "display: none");
    }

    //保存
    $("#formSave").click(function () {

        if ($("#baseInfor #state").val() > 0) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }

        if ($("#baseInfor #inventoryType").val() > 0) {
            if ($("#baseInfor #completeTime").val() != "" && $("#baseInfor #inventoryTime").val()) {
                //判断结束时间是否大于开始时间
                var inventoryTime = $("#baseInfor #inventoryTime").val();
                var completeTime = $("#baseInfor #completeTime").val();
                var d1 = new Date(inventoryTime.replace(/\-/g, "\/"));
                var d2 = new Date(completeTime.replace(/\-/g, "\/"));
                if (d1 > d2) {
                    layer.alert("结束时间不能等于小于开始时间");
                    return false;
                }
            }
        }

        if ($('#baseInfor').valid()) {
            $.ajax({
                url: context_path + "/inventoryTask/saveInventoryTask",
                type: "post",
                data: $("#baseInfor").serialize(),
                dataType: "JSON",
                success: function (data) {
                    if (data.code) {
                        $("#baseInfor #id").val(data.id);
                        //刷新收货单列表
                        $("#grid-table").jqGrid("setGridParam", {
                            postData: {queryJsonString: ""} //发送数据
                        }).trigger("reloadGrid");
                        setNextBtn();
                        //根据收货单主键id刷新查询收货单详情
                        $("#grid-table-c").jqGrid("setGridParam", {
                            url: context_path + '/inventoryTask/getTaskDetailList?id=' + data.id,
                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                        }).trigger("reloadGrid");
                        layer.msg("操作成功！", {icon: 1, time: 1200});
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 1200});
                    }
                }
            });
        }
    })


    //提交
    $("#formSubmit").click(function () {
        if ($("#baseInfor #state").val() > 0) {
            layer.msg("提交已完成，不必重复提交", {time: 1200, icon: 2});
            return false;
        }
        if ($("#baseInfor #id").val() != "") {
            layer.confirm("提交后的数据将不能修改，确认提交吗？", function () {
                $.ajax({
                    url: context_path + "/inventoryTask/submitInventoryTask?id=" + $("#baseInfor #id").val(),
                    type: "post",
                    dataType: "JSON",
                    success: function (data) {
                        if (data.result) {
                            //刷新收货单列表
                            $("#grid-table").jqGrid("setGridParam", {
                                postData: {queryJsonString: ""} //发送数据
                            }).trigger("reloadGrid");
                            layer.closeAll();
                            layer.msg(data.msg, {icon: 1, time: 1200})
                        } else {
                            layer.msg(data.msg, {icon: 2, time: 1200})
                        }
                    }
                });
            });
        } else {
            layer.msg("请先保存单据", {icon: 2, time: 1200})
        }
    })

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

    var eidtMaterialMemory = {};

    _grid_detail = jQuery("#grid-table-c").jqGrid({
        url: context_path + '/inventoryTask/getTaskDetailList?id=' + id,
        datatype: "json",
        colNames: ["盘点详情主键", "物料编号", "物料名称","库位", "RFID", "批次号", "库存数量", "盘点数量", "保质期（天）","保质到期时间","盘点结果","状态", "操作", "删除"],
        colModel: [
            {name: 'id', index: 'id', width: 30, hidden: true},
            {name: 'materialCode', index: 'materialCode', width: 25},
            {name: 'materialName', index: 'materialName', width: 25},
            {name: 'positionCode', index: 'positionCode', width: 20},
            {name: 'rfid', index: 'rfid', width: 40},
            {name: 'batchNo', index: 'batchNo', width: 60},
            {name: 'stockAmount', index: 'stockAmount', width: 20},
            {name: 'inventoryAmount', index: 'inventoryAmount', width: 20},
            {name: 'qualityPeriod', index: 'qualityPeriod', width: 25,editable : true,formatter:formatterEditData,
                // editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]*[1-9][0-9]*$");
                            var qualityPeriod = $("#" + $elementId).val();
                            var flag = true;
                            if (!reg.test(qualityPeriod)) {
                                layer.alert("请输入正确的保质期天数");
                                flag = false;
                            }
                            if(flag){
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["qualityPeriod"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"qualityPeriod":$("#" + $elementId).val()};
                                }
                            }else{
                                $("#" + $elementId).val("");
                            }
                        }
                    }]
                }},
            {name: 'qualityDate', index: 'qualityDate', width: 30,editable : true,formatter:formatterEditData,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'change',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("请输入YYYYMMDD的日期格式数据");
                                $("#" + $elementId).val("");
                            }else{
                                if(eidtMaterialMemory[id]){
                                    eidtMaterialMemory[id]["qualityDate"] = $("#" + $elementId).val();
                                }else{
                                    eidtMaterialMemory[id] = {"qualityDate":$("#" + $elementId).val()};
                                }
                            }
                        }
                    }]
                }},
            {
                name: "inventoryState", index: "inventoryState", width: 20, sortable: false,
                formatter: function (cellValue) {
                    if (cellValue == 0) {
                        return "<span style='color:#B8A608;font-weight:bold;'> 盘亏</span>";
                    } else if (cellValue == 1) {
                        return "<span style='color:#d15b47;font-weight:bold;'> 盘盈</span>";
                    } else if (cellValue == 2) {
                        return "<span style='color:#d15b47;font-weight:bold;'> 盘平</span>";
                    } else {
                        return "<span style='color:#d15b47;font-weight:bold;'> 未盘</span>";
                    }
                }
            },
            {
                name: 'state', index: 'state', width: 20,
                formatter: function (cellValue) {
                    if (cellValue == 0) {
                        return "<span style='color:#B8A608;font-weight:bold;'>未盘点</span>";
                    } else if (cellValue == 1) {
                        return "<span style='color:#ACD128;font-weight:bold;'>已盘点</span>";
                    } else if (cellValue == 2) {
                        return "<span style='color:#ACD128;font-weight:bold;'>已审核</span>";
                    } else if (cellValue == 3) {
                        return "<span style='color:#d15b47;font-weight:bold;'>复盘中</span>";
                    }
                }
            },
            {
                name: 'operate', index: 'operate', width: 40, sortable: false, fixed: true,
                formatter: function (cellvalue, option, rowObject) {
                    return "<span class='btn btn-minier btn-success' style='transition:background-color 0.3;-webkit-transition: background-color 0.3s;' " +
                        "onclick='replayInventory(\"" + rowObject.id + "\",\"" + rowObject.state + "\")'>复盘</span>";
                }
            },
            {
                name: "del",
                index: "del",
                width: 20,
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    if ($("#baseInfor #state").val() > 0) {
                        return "<a style='text-decoration:underline;display: block;color: #EBE0EB'>删除</a>"
                    } else {
                        return "<a onclick='delOneDetail(" + id + ")' style='text-decoration:underline;display: block;'>删除</a>"
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
        },
        //  cellurl : context_path + "/transfer/updateAmount",
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
    }).navButtonAdd("#grid-pager-c", {
        caption: "",
        buttonicon: "ace-icon fa fa-refresh green",
        onClickButton: function () {
            $("#grid-table-c").jqGrid('setGridParam',
                {
                    url: context_path + '/transfer/detailList?transferId=' + transferId,
                    postData: {queryJsonString: ""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", 1100);
        var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).height() +
            $("#baseInfor").outerHeight(true) + $("#materialDiv").outerHeight(true) +
            $("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) +
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid("setGridHeight", _grid_detail.parents(".layui-layer").height() - height - 85);
    });
    $(window).triggerHandler('resize.jqGrid');

    //给表格中编辑数据添加默认值
    function formatterEditData(cellvalue, options, rowObject){
        var id = options.rowId;
        var cellName = options.colModel.name;
        if(cellvalue){
            return cellvalue;
        }else{
            if(eidtMaterialMemory[id]!=undefined&&eidtMaterialMemory[id]!=null&&eidtMaterialMemory[id][cellName]!=undefined&&eidtMaterialMemory[id][cellName]!=null){
                return eidtMaterialMemory[id][cellName]
            }else{
                return "";
            }
        }
    }

    //清空库存多选框中的值
    function removeChoice() {
        $("#s2id_positionCode.select2-choices").children(".select2-search-choice").remove();
        $("#positionCode").select2("val", "");
        selectData = 0;
    }

    //工具栏
    $("#__toolbar__-c").iToolBar({
        id: "__tb__01",
        items: [
            {label: "删除", onclick: delDetail},
        ]
    });

    /**
     * 审核盘点任务
     */
    function auditTaskCheck() {
        //生产日期拼接字符串
        var qualityDateStr = "";
        //保质期拼接字符串
        var qualityPeriodStr = "";
        //选中的库存ID
        var stockIdStr = "";

        // var checkedNum = getGridCheckedNum("#grid-table-c", "id");  //选中的数量
        var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
        if(ids==null || ids=='' || ids==undefined){
            layer.msg("请选择要审核的盘点任务！",{icon:2,time:2000});
            return false;
        }

        var flag = true;
        var flag2 = true;
        var flag3 = true;
        jQuery(ids).each(function(){
            var rowData = $("#grid-table-c").jqGrid("getRowData",this);
            var id = rowData.id;
            var qualityDate = rowData.qualityDate&&$(rowData.qualityDate).val()!=undefined?$("#"+$(rowData.qualityDate).attr("id")).val():rowData.qualityDate;
            var qualityPeriod = rowData.qualityPeriod&&$(rowData.qualityPeriod).val()!=undefined?$("#"+$(rowData.qualityPeriod).attr("id")).val():rowData.qualityPeriod;
            qualityDateStr += qualityDate + ",";
            qualityPeriodStr += qualityPeriod + ",";
            stockIdStr += id +",";

            if(qualityDate ==null || qualityDate == undefined || qualityDate.trim()==''){
                flag = false;
                return;
            }else{
                var reg = new RegExp("^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$");
                if (!reg.test(qualityDate)) {
                    flag2 = false;
                    return;
                }
            }

            if(qualityPeriod ==null || qualityPeriod == undefined || qualityPeriod.trim()==''){
                flag = false;
                return;
            }else{
                // var reg = new RegExp("^[0-9]*$");
                // var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                var reg = new RegExp("^[0-9]*[1-9][0-9]*$");
                if (!reg.test(qualityPeriod)) {
                    flag3 = false;
                    return;
                }
            }
        });

        if(!flag2){
            layer.msg("生成入库单失败！请确认物料的生产日期是YYYYMMDD的日期格式",{icon:2,time:3000});
            return false;
        }
        if(!flag3){
            layer.msg("生成入库单失败！请确认物料的保质期是否为正整数",{icon:2,time:3000});
            return false;
        }

        //截取逗号
        stockIdStr = stockIdStr.substr(0,stockIdStr.length-1);
        qualityDateStr = qualityDateStr.substr(0,qualityDateStr.length-1);
        qualityPeriodStr = qualityPeriodStr.substr(0,qualityPeriodStr.length-1);

        if ($("#baseInfor #state").val() == 0 || $("#baseInfor #state").val() == 1 || $("#baseInfor #state").val() == 2) {
            layer.msg("状态尚未待审核，不可以审核", {time: 1200, icon: 2});
            return false;
        }

        var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
        layer.confirm("确定审核的盘点任务？", function () {
            $.ajax({
                type: "POST",
                url: context_path + "/inventoryTask/auditTaskCheck",
                data:{
                    stockIdStr:stockIdStr,
                    qualityDateStr:qualityDateStr,
                    qualityPeriodStr:qualityPeriodStr
                },
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.result) {
                        layer.closeAll();
                        layer.msg(data.message,{icon:1,time:1200});
                    } else {
                        layer.msg(data.message,{icon:2,time:1200});
                    }
                    _grid.trigger("reloadGrid");//重新加载表格
                }
            });
        })
    }


    //删除
    function delDetail() {
        var checkedNum = getGridCheckedNum("#grid-table-c", "id");  //选中的数量
        if ($("#baseInfor #state").val() > 0) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的盘点任务详情！");
        } else {
            var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的盘点任务详情？", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + '/inventoryTask/delInventoryTaskDetail?ids=' + ids,
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        if (data.result) {
                            layer.msg("操作成功！");
                            //重新加载详情表格
                            _grid_detail.trigger("reloadGrid");  //重新加载表格
                        }
                    }
                });
            });
        }
    }

    /**
     * @Description:  复盘
     */
    function replayInventory(id, state) {
        if (stateVal != 4) {
            layer.msg("盘点尚未审核，无法复盘", {time: 1200, icon: 2});
            return false;
        }
        layer.confirm("确定复盘操作？", function () {
            $.ajax({
                type: "POST",
                url: context_path + "/inventoryTask/replayInventory?id=" + id,
                dataType: 'json',
                cache: false,
                success: function (data) {
                    layer.closeAll();
                    if (data.result) {
                        layer.msg("复盘成功！", {icon: 1, time: 1000});
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 1000});
                    }
                    _grid_detail.trigger("reloadGrid");  //重新加载表格
                    _grid.trigger("reloadGrid");//重新加载表格
                }
            });
        });
    }

    function setNextBtn() {
        $("#next_step").attr('class', 'btn btn-info');
        $("#next_step").on('click', function () {
            _grid_detail.trigger("reloadGrid");
            $(".step1").css('display', 'none');
            $(".step2").css('display', 'block');

        });
        $("#pre_step").on('click', function () {
            $(".step1").css('display', 'block');
            $(".step2").css('display', 'none');
        });

    }

    function delOneDetail(id) {
        if ($("#baseInfor #state").val() > 0) {
            layer.msg("已提交，不可修改", {time: 1200, icon: 2});
            return false;
        }
        var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
        layer.confirm("确定删除选中的盘点任务详情？", function () {
            $.ajax({
                type: "POST",
                url: context_path + '/inventoryTask/delInventoryTaskDetail?ids=' + id,
                dataType: "json",
                cache: false,
                success: function (data) {
                    if (data.result) {
                        layer.msg("操作成功！");
                        //重新加载详情表格
                        _grid_detail.trigger("reloadGrid");  //重新加载表格
                    }
                }
            });
        });
    }
</script>
