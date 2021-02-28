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
                <span>填写出库单信息</span>
            </div>
            <hr class="step_line"/>
            <div class="flex-row align-items-center justify-content-start step_name">
                <i class="instorage_circle">2</i>
                <span>出库单详情提交</span>
            </div>
        </div>
        <form action="" class="form-horizontal" id="boxForm-d" name="materialForm" method="post" target="_ifr">
            <input type="hidden" id="outStorageId" name="outStorageId" value="${outStorageId}"/>
            <%--一行数据 --%>
            <div class="row" style="margin:0;padding:0;">
                <%--装箱条码--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="boxedBarCode">出库单号：</label>
                    <div class="controls">
                        <div class="input-append required span12">
                            <input type="text" id="boxedBarCode" class="span10" name="outstorageBillCode"
                                   placeholder="后台自动生成" readonly="readonly" value="${outstorageBillCode}"/>
                        </div>
                    </div>
                </div>

                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="spareBillCode">备料单：</label>
                    <div class="controls">
                        <div class="input-append required  span12" style="float: none !important;">
                            <input class="span10" type="text" id="spareBillCode" name="spareBillCode"
                                   value="${spareBillManagerVO.spareBillCode}" readonly="readonly"/>
                        </div>
                    </div>
                </div>
            </div>
            <%--货主--%>
            <div class="row" style="margin:0;padding:0;">
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="outBillType">出库类型：</label>
                    <div class="controls">
                        <div class="input-append required span12" style="float: none !important;">
                            <input class="span10" type="text" id="outBillType" name="outBillType"
                                   value="生产领料出库" readonly="readonly"/>
                        </div>
                    </div>
                </div>

                <%--出库时间--%>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="outstoragePlanTime">出库时间：</label>
                    <div class="controls">
                        <div class="input-append required span12" style="float: none !important;">
                            <input class="span10 form-control date-picker" id="outstoragePlanTime"
                                   name="outstoragePlanTime"
                                   type="text" value="${outStorageManagerVO.outstoragePlanTime}" placeholder="出库时间"
                                   class="span10"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin:0;padding:0;">
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="billType">单据类型：</label>
                    <div class="controls">
                        <div class="required span12" style="float: none !important;">
                            <select class="mySelect2 span10" id="billType" name="billType"
                                    data-placeholder="请选择单据类型" style="margin-left:0px;">
                                <option value=""></option>
                                <option value="0" <c:if test="${billType==0}">selected="selected"</c:if>>
                                    无RFID单据
                                </option>
                                <option value="1" <c:if test="${billType==1}">selected="selected"</c:if>>
                                    有RFID单据
                                </option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="materialType">物料类型：</label>
                    <div class="controls">
                        <div class="required span12" style="float: none !important;">
                            <input class="span10" type="hidden" id="materialType" name="materialType"
                                   value=""/>
                            <select class="mySelect2 span10" id="materialTypeSelect" name="materialTypeSelect"
                                    data-placeholder="请选择物料类型" style="margin-left:0px;">
                                <option value=""></option>
                                <option value="0">自采</option>
                                <option value="1">客供</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin:0;padding:0;">
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="customerCode">货主：</label>
                    <div class="controls">
                        <div class="required span12" style="float: none !important;">
                            <input class="span10" type="hidden" value=""
                                   id="customerCode" name="customerCode"/>
                            <select class="mySelect2 span10" id="customerIdSelect" name="customerIdSelect"
                                    data-placeholder="请选择货主" style="margin-left:0px;">
                                <option value=""></option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option value="${customer.customer_code}">
                                            ${customer.customer_name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="control-group span6" style="display: inline">
                    <label class="control-label" for="remark">备注：</label>
                    <div class="controls">
                        <div class="span12">
                            <input id="remark" name="remark" type="text" placeholder="备注" class="span10"
                                   value=""/>
                        </div>
                    </div>
                </div>
            </div>


            <div class="flex-row justify-content-end"
                 style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
                <span class="btn btn-info" id="formSave">
                <i class="ace-icon fa fa-check bigger-110"></i>保存
                </span>
                <c:if test="${outStorageManagerVO.outstorageBillType==null}">
                <span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				   <i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
				</span>
                </c:if>
                <c:if test="${outStorageManagerVO.outstorageBillType!=null}">
                <span class="btn btn-info" id="next_step" style="margin-left: 5px" onclick="nextStep()">
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
                <span>填写出库单信息</span>
            </div>
            <hr class="step_line badge-ok"/>
            <div class="flex-row align-items-center justify-content-start step_name active">
                <i class="instorage_circle">2</i>
                <span>出库单详情提交</span>
            </div>
        </div>
        <div id="shelvesLocationDiv" style="margin: 10px;">
            <!-- 下拉框 -->
            <label class="inline" for="materialBox">物料：</label>
            <input type="text" id="materialBox" name="materialBox" style="width:350px;margin-right:10px;"/>
            <button id="addShelvesLocationBtn" class="btn btn-xs btn-primary" onclick="addMaterial();">
                <i class="icon-plus" style="margin-right:6px;"></i>添加
            </button>
        </div>
        <!-- 表格div -->
        <div id="grid-div-c" style="width:100%;margin:0px;border:0px;">
            <!-- 	表格工具栏 -->
            <button id="btchdel" class="btn btn-info" onclick="delOutStorageDetail();"
            style="margin-bottom: 0.5rem;float: right">
            <i class="icon-minus" style="margin-right:6px;"></i>批量删除
            </button>
            <div style="clear: both;"></div>
            <!-- 物料详情信息表格 -->
            <table id="grid-table-d" style="width:100%;height:100%;"></table>
            <!-- 表格分页栏 -->
            <div id="grid-pager-c"></div>
        </div>
        <div class="flex-row justify-content-end"
        style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 0.3rem;">
        <%--<span class="btn btn-info" id="formSave">--%>
        <%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
        <%--</span>--%>
        <br>
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
    var oriDataDetail;
    var _grid_detail;        //表格对象

    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss', useMinutes: true, useSeconds: true});

    var lastsel2;
    var selectParam = "";
    var selectData = "";
    //单元格编辑成功后，回调的函数
    var editFunction = function eidtSuccess(XHR) {
        var data = eval("(" + XHR.responseText + ")");
        if (data["msg"] != "") {
            layer.alert(data["msg"]);
        }
        jQuery("#grid-table-d").jqGrid('setGridParam',
            {
                postData: {
                    id: $('#boxForm-d #outStorageId').val(),
                    queryJsonString: ""
                }
            }
        ).trigger("reloadGrid");
    };

    _grid_detail = jQuery("#grid-table-d").jqGrid({
        url: context_path + "/outStorageManager/getEditList?outStorageId=" + $("#boxForm-d #outStorageId").val(),
        datatype: "json",

        colNames: ["详情主键", "物料编号", "物料名称", "物料单位","库位","生产日期", "批次号", "数量", "重量","操作"],
        colModel: [
            {name: "id", index: "id", width: 55, hidden: true},
            {name: "materialCode", index: "materialCode", width: 40},
            {name: "materialName", index: "materialName", width: 40},
            {name: "unit", index: "unit", width: 30},
            {name: "positionCode", index: "positionCode", width: 40},
            {name: "productData", index: "productData", width: 40},
            {name: "batchNo", index: "batchNo", width: 60, /*editable: true,*/
                // editoptions: {
                //     size: 25,
                //     dataEvents: [{
                //         type: 'blur',     //blur,focus,change.............
                //         fn: function (e) {
                //             var $element = e.currentTarget;
                //             var $elementId = $element.id;
                //             var rowid = $elementId.split("_")[0];
                //             var id = $element.parentElement.parentElement.children[1].textContent;
                //             // var reg = new RegExp("^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$");
                //             // if (!reg.test($("#" + $elementId).val())) {
                //             //     layer.alert("非法的重量！(注：只可为两位小数的正实数)");
                //             //     return;
                //             // }
                //             $.ajax({
                //                 url: context_path + '/outStorageManager/updateDetailAmount',
                //                 type: "POST",
                //                 data: {
                //                     id: id,
                //                     batchNo: $("#" + rowid + "_batchNo").val()
                //                 },
                //                 dataType: "json",
                //                 success: function (data) {
                //                     if (data.result) {
                //                         layer.msg(data.msg);
                //                     } else {
                //                         layer.msg(data.msg)
                //                     }
                //                     $("#grid-table-c").jqGrid("setGridParam",
                //                         {
                //                             url: context_path + '/outStorageManager/getEditList?outStorageId=' + $("#boxForm-d #outStorageId").val(),
                //                             postData: {queryJsonString: ""} //发送数据  :选中的节点
                //                         }
                //                     ).trigger("reloadGrid");
                //                 }
                //             });
                //         }
                //     }]
                // }
                },
            {name: "amount", index: "amount", width: 20, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[1-9]\\d*$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的数量！(注：只可为正整数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/outStorageManager/updateDetailAmount',
                                type: "POST",
                                data: {
                                    id: id,
                                    amount: $("#" + rowid + "_amount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (data.result) {
                                        layer.msg(data.msg);
                                    } else {
                                        layer.msg(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/outStorageManager/getEditList?outStorageId=' + $("#boxForm-d #outStorageId").val(),
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }},
            {name: "weight", index: "weight", width: 20, editable: true,
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
                                url: context_path + '/outStorageManager/updateDetailAmount',
                                type: "POST",
                                data: {
                                    id: id,
                                    weight: $("#" + rowid + "_weight").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (data.result) {
                                        layer.msg(data.msg);
                                    } else {
                                        layer.msg(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam",
                                        {
                                            url: context_path + '/outStorageManager/getEditList?outStorageId=' + $("#boxForm-d #outStorageId").val(),
                                            postData: {queryJsonString: ""} //发送数据  :选中的节点
                                        }
                                    ).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }},
            {
                name: "del", index: "del", width: 20, formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    return "<a onclick='delOneOutStorageDetail(" + id + ")' style='text-decoration:underline;display: block;'>删除</a>"
                }
            }
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: '#grid-pager-c',
        sortname: 'id',
        sortorder: "asc",
        altRows: true,
        cellsubmit: "remote",
        viewrecords: true,
        autowidth: true,
        multiselect: true,
        multiboxonly: true,
        cellurl: context_path + '/outStorageManager/updateDetailAmount',
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
    $("#grid-table-d").navGrid("#grid-pager-c", {
        edit: false,
        add: false,
        del: false,
        search: false,
        refresh: false
    }).navButtonAdd("#grid-pager-c", {
        caption: "",
        buttonicon: "ace-icon fa fa-refresh green",
        onClickButton: function () {
            $("#grid-table-d").jqGrid("setGridParam",
                {
                    url: context_path + '/outStorageManager/getEditList',
                    postData: {outStorageId: $("#boxForm-d #outStorageId").val()} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-d").jqGrid("setGridWidth", 750 - 3);
        var height = $(".layui-layer-title", _grid_detail.parents(".layui-layer")).outerHeight(true) +
            $("#boxForm-d").outerHeight(true) + $("#shelvesLocationDiv").outerHeight(true) +
            $("#grid-pager-c").outerHeight(true) + $("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true) + $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-d").jqGrid("setGridWidth", $("#grid-div-c").width() - 3);
        $("#grid-table-d").jqGrid("setGridHeight", _grid_detail.parents(".layui-layer").height() - height - 200);
    });
    $(window).triggerHandler("resize.jqGrid");

    //清空物料多选框中的值
    function removeChoice() {
        $("#s2id_shelvesLocation .select2-choices").children(".select2-search-choice").remove();
        $("#materialBox").select2("val", "");
        selectData = 0;
    }

    //提交
    $("#formSubmit").click(function () {
        if ($("#boxForm-d #outStorageId").val() == '') {
            layer.alert("请先保存单据");
            return;
        } else {
            layer.confirm("确定提交,提交之后数据不能修改", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/outStorageManager/setOutState",
                    dataType: "json",
                    data: {id: $('#boxForm-d #outStorageId').val()},
                    success: function (data) {
                        if (data.result) {
                            layer.closeAll();
                            layer.msg("提交成功", {icon: 1, time: 1200});
                            gridReload();
                        } else {
                            layer.alert(data.msg);
                        }
                    }
                })
            })
        }
    })

    $("#formSave").click(function () {

        if ($('#boxForm-d').valid()) {
            //通过验证：获取表单数据，保存表单信息
            var formdata = $('#boxForm-d').serialize();
            saveFormInfo(formdata);
        }
    })
    $("#boxForm-d").validate({
        ignore: "",
        rules: {
            "positionCodeSelect": {
                required: true,
            },
            "customerIdSelect": {
                required: true,
            },
            "outstoragePlanTime": {
                required: true,
            },
            // "outstorageBillTypeSelect": {
            //     required: true,
            // },
            "materialTypeSelect": {
                required: true,
            },
            "billType": {
                required: true,
            }
        },
        messages: {
            "positionCodeSelect": {
                required: "请选择库位",
            },
            "customerIdSelect": {
                required: "请选择货主",
            },
            "outstoragePlanTime": {
                required: "请选择出库时间",
            },
            // "outstorageBillTypeSelect": {
            //     required: "请选择出库类型",
            // },
            "materialTypeSelect": {
                required: "请选择物料类型",
            },
            "billType": {
                required: "请选择单据类型",
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
    })

    //保存
    function saveFormInfo(formdata) {
        $("#formSave").attr("disabled", "disabled");
        $(window).triggerHandler('resize.jqGrid');
        $.ajax({
            type: "POST",
            url: context_path + "/spareBillManager/saveOutStorage",
            data: formdata,
            dataType: "json",
            success: function (data) {
                if (data.result) {
                    $("#boxForm-d #outStorageId").val(data.outstorageId);
                    $("#boxForm-d #outstorageBillCode").val(data.outstorageBillCode);
                    layer.alert(data.msg);
                    $("#formSave").removeAttr("disabled");
                    gridReload();
                    setNextBtn();
                } else {
                    layer.alert(data.msg);
                }
            }
        })
    }

    //添加物料详情
    function addMaterial() {
        if ($("#boxForm-d #outStorageId").val() == "") {
            layer.alert("请先保存表单信息！");
            return;
        }
        if (selectData != 0) {
            //将选中的物料添加到数据库中
            $.ajax({
                type: "POST",
                url: context_path + '/outStorageManager/addOutStorageDetail',
                data: {outStorageId: $('#boxForm-d #outStorageId').val(), materialContent: selectData.toString()},
                dataType: "json",
                success: function (data) {
                    removeChoice();   //清空下拉框中的值
                    if (Boolean(data.result)) {
                        layer.msg("添加成功", {icon: 1, time: 1200});
                        //重新加载详情表格
                        reloadDetailTableList();
                    } else {

                    }
                }
            });
        } else {
            layer.alert("请选择物料！");
        }
    }

    //工具栏
    $("#__toolbar__-c").iToolBar({
        id: "__tb__01",
        items: [
            {label: "删除", onclick: delOutStorageDetail},
        ]
    });

    //删除详情
    function delOutStorageDetail() {delOneOutStorageDetail
        var checkedNum = getGridCheckedNum("#grid-table-d", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的详情！");
            return false;
        } else {
            var ids = jQuery("#grid-table-d").jqGrid('getGridParam', 'selarrrow');
            layer.confirm("确定删除选中的详情?", function () {
                $.ajax({
                    type: "POST",
                    url: context_path + "/outStorageManager/delOutStorageDetail?ids=" + ids,
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            layer.msg(data.msg);
                            //刷新列表
                            reloadDetailTableList();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            })
        }
    }

    function reloadDetailTableList() {
        $("#grid-table-d").jqGrid("setGridParam", {
            postData: {outStorageId: $("#boxForm-d #outStorageId").val()} //发送数据  :选中的节点
        }).trigger("reloadGrid");
    }

    //数量输入验证
    function numberRegex(value, colname) {
        var regex = /^\d+\.?\d{0,2}$/;
        reloadDetailTableList();
        if (!regex.test(value)) {
            return [false, ""];
        }
        else return [true, ""];
    }



    $("#materialBox").select2({
        placeholder: "请选择物料",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        multiple: true,
        closeOnSelect: false,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        ajax: {
            type: "POST",
            url: context_path + "/outStorageManager/getOutStorageMaterialList",
            dataType: "json",
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    queryString: term, //联动查询的字符
                    pageSize: 15, //一次性加载的数据条数
                    pageNo: pageNo, //页码
                    time: new Date(),
                    outStorageId: $('#boxForm-d #outStorageId').val()
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
                        results: {}
                    };
                }
            },
            cache: true
        }

    });
    $("#materialBox").on("change", function (e) {
        debugger;
        var datas = $("#materialBox").select2("val");
        selectData = datas;
        var selectSize = datas.length;
        if (selectSize > 1) {
            var $tags = $("#s2id_shelvesLocation .select2-choices");
            var $choicelist = $tags.find(".select2-search-choice");
            var $clonedChoice = $choicelist[0];
            $tags.children(".select2-search-choice").remove();
            $tags.prepend($clonedChoice);
            $tags.find(".select2-search-choice").find("div").html(selectSize + "个被选中");
            $tags.find(".select2-search-choice").find("a").removeAttr("tabindex");
            $tags.find(".select2-search-choice").find("a").attr("href", "#");
            $tags.find(".select2-search-choice").find("a").attr("onclick", "removeChoice();");
        }
        //执行select的查询方法
        /*        $("#materialBox").select2("search", selectParam);*/
    });

    /**
     * 下拉列表
     */
    $('#boxForm-d .mySelect2').select2();

    //客户选择
    $("#boxForm-d #customerIdSelect").change(function () {
        $('#boxForm-d #customerCode').val($('#boxForm-d #customerIdSelect').val());
    });

    //库位选择
    $("#boxForm-d #positionCodeSelect").change(function () {
        $('#boxForm-d #positionCode').val($('#boxForm-d #positionCodeSelect').val());
    });

    //备料单选择
    $("#boxForm-d #spareBillCodeSelect").change(function () {
        $('#boxForm-d #spareBillCode').val($('#boxForm-d #spareBillCodeSelect').val());
    });

    //出库类型
    // $("#boxForm-d #outstorageBillTypeSelect").change(function () {
    //     $('#boxForm-d #outstorageBillType').val($('#boxForm-d #outstorageBillTypeSelect').val());
    // });

    // //单据类型
    // $("#boxForm-d #billTypeSelect").change(function () {
    //     $('#boxForm-d #billType').val($('#boxForm-d #billTypeSelect').val());
    // });

    //物料类型
    $("#boxForm-d #materialTypeSelect").change(function () {
        $('#boxForm-d #materialType').val($('#boxForm-d #materialTypeSelect').val());
    });

    $("#boxForm-d #receiptIdSelect").change(function () {
        $('#boxForm-d #receiptId').val($('#boxForm-d #receiptIdSelect').val());
    });

    // $('#boxForm-d #outstorageBillTypeSelect').change(function () {
    //     if ($('#boxForm-d #outstorageBillTypeSelect').val() == 0) {
    //         //表示的是获取备料单
    //         $("#beiliao").removeAttrs("style", "visibility:hidden");
    //         $("#shouhuo").attr("style", "visibility:hidden");
    //         //清空下拉框中的值
    //         $("#boxForm-d #receiptIdSelect").select2("val", "");
    //         //加验证
    //         $('#boxForm-d #spareBillCodeSelect').rules('add', {required: true});
    //     }
    //     //表示的是获取收货单
    //     else if ($('#boxForm-d #outstorageBillTypeSelect').val() == 1) {
    //         $("#shouhuo").removeAttrs("style", "visibility:hidden");
    //         $("#beiliao").attr("style", "visibility:hidden");
    //         //清空下拉框中的值
    //         $("#boxForm-d #receiptIdSelect").select2("val", "");
    //         $('#boxForm-d #receiptIdSelect').rules('add', {required: true});
    //     } else {
    //         $("#beiliao").attr("style", "visibility:hidden");
    //         $("#shouhuo").attr("style", "visibility:hidden");
    //         $("#boxForm-d #receiptIdSelect").select2("val", "");
    //         $("#boxForm-d #spareBillCodeSelect").select2("val", "");
    //
    //         $('#boxForm-d #spareBillCodeSelect').rules("remove");
    //         $('#boxForm-d #receiptIdSelect').rules("remove");
    //     }
    // })

    function setNextBtn() {
        $("#next_step").attr('class', 'btn btn-info');
        $("#next_step").on('click', function () {
            reloadDetailTableList();
            $(".step1").css('display', 'none');
            $(".step2").css('display', 'block');

        });
        $("#pre_step").on('click', function () {
            $(".step1").css('display', 'block');
            $(".step2").css('display', 'none');
        });

    }

    function nextStep() {
        reloadDetailTableList();
        $(".step1").css('display', 'none');
        $(".step2").css('display', 'block');
        $("#pre_step").on('click', function () {
            $(".step1").css('display', 'block');
            $(".step2").css('display', 'none');
        });
    }

    function delOneOutStorageDetail(id) {
        layer.confirm("确定删除选中的详情?", function () {
            $.ajax({
                type: "POST",
                url: context_path + "/outStorageManager/delOutStorageDetail?ids=" + id,
                dataType: "json",
                success: function (data) {
                    if (data.result) {
                        layer.msg(data.msg);
                        //刷新列表
                        reloadDetailTableList();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            })
        })

    }
</script>