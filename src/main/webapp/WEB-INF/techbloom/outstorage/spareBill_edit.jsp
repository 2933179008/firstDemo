<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form action="" class="form-horizontal" id="boxForm" name="materialForm" method="post" target="_ifr">
        <input type="hidden" id="boxId" name="id" value="" />
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--装箱条码--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxedBarCode" >备料单：</label>
                <div class="controls">
                    <div class="input-append required span12" >
                        <input type="text" id="boxedBarCode" class="span10" name="boxedBarCode" placeholder="后台自动生成" readonly="readonly" value="" />
                    </div>
                </div>
            </div>
            <%--装箱类型--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxedType" >出库时间：</label>
                <div class="controls">
                    <div class="required span12" style=" float: none !important;">
                        <input type="text" class="span10" id="boxedType" name="boxedType" placeholder="批次号" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--装箱人员--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxUserId" >出库类型：</label>
                <div class="controls">
                    <div class="required  span12" style="float: none !important;">
                        <input class="span10" type="text" id="boxUserId" name="boxUserId"  placeholder="物料编号" />
                    </div>
                </div>
            </div>
            <%--装箱时间;--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxTime" >出库完成时间：</label>
                <div class="controls">
                    <div class="required span12" >
                        <input class="form-control date-picker" id="boxTime" name="boxTime" type="text" value="" placeholder="出库完成时间" class="span10" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--装箱人员--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxUserId" >生产任务单号：</label>
                <div class="controls">
                    <div class="required  span12" style="float: none !important;">
                        <input class="span10" type="text" id="boxUserId" name="boxUserId"  placeholder="生产任务单号" />
                    </div>
                </div>
            </div>
            <%--装箱时间;--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxTime" >生产产品信息：</label>
                <div class="controls">
                    <div class="required span12" >
                        <input class="form-control date-picker" id="boxTime" name="boxTime" type="text" value="" placeholder="生产产品信息" class="span10" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--装箱人员--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxUserId" >生产数量：</label>
                <div class="controls">
                    <div class="required  span12" style="float: none !important;">
                        <input class="span10" type="text" id="boxUserId" name="boxUserId"  placeholder="生产数量" />
                    </div>
                </div>
            </div>
            <%--装箱时间;--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="boxTime" >产线信息：</label>
                <div class="controls">
                    <div class="required span12" >
                        <input class="form-control date-picker" id="boxTime" name="boxTime" type="text" value="" placeholder="产线信息" class="span10" />
                    </div>
                </div>
            </div>
        </div>
        <div style="margin-left:10px;">
            <span class="btn btn-info" id="formSave">
		       <i class="ace-icon fa fa-check bigger-110"></i>保存
            </span>
            <%--<span class="btn btn-info" id="formSubmit">--%>
            <%--<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交--%>
            <%--</span>--%>
        </div>
    </form>
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
        <div id="fixed_tool_div" class="fixed_tool_div detailToolBar">
            <div id="__toolbar__-c" style="float:left;overflow:hidden;"></div>
        </div>
        <!-- 物料详情信息表格 -->
        <table id="grid-table-c" style="width:100%;height:100%;"></table>
        <!-- 表格分页栏 -->
        <div id="grid-pager-c"></div>
    </div>
</div>
<script type="text/javascript" src="<%=path%>/static/js/techbloom/wms/box/box.js"></script>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var oriDataDetail;
    var _grid_detail;        //表格对象
    var lastsel2;
    var selectParam ="";
    var selectData = "";
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
        url : context_path + "/spareBillManager/getEditList",
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","批次号","数量","重量","库位"],
        colModel : [
            {name : "id",index : "id",width : 55,hidden:true},
            {name : "materialNo",index:"materialNo",width : 50},
            {name : "materialName",index:"materialName",width : 50},
            {name : "batchNo",index:"batchNo",width : 50},
            {name : "stockAmount",index:"stockAmount",width:50},
            {name : "weight",index:"weight",width:50},
            {name : "spec",index:"spec",width : 50,editable : true}
        ],
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: '#grid-pager-c',
        sortname: 'ID',
        sortorder: "asc",
        altRows: true,
        cellsubmit : "remote",
        viewrecords: true,
        autowidth: true,
        multiselect: true,
        multiboxonly: true,
        cellurl : context_path + '/box/updateBoxDetailTime',
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
    $("#grid-table-c").navGrid("#grid-pager-c", {edit: false, add: false, del: false, search: false, refresh: false}).navButtonAdd("#grid-pager-c", {
        caption: "",
        buttonicon: "ace-icon fa fa-refresh green",
        onClickButton: function () {
            $("#grid-table-c").jqGrid("setGridParam",
                {
                    url: context_path + '/box/boxDetailList',
                    postData: {boxedId:$("#boxId").val()} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        var height=$(".layui-layer-title",_grid_detail.parents(".layui-layer") ).outerHeight(true)+
            $("#boxForm").outerHeight(true)+$("#shelvesLocationDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+$("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width()-3);
        $("#grid-table-c").jqGrid("setGridHeight",_grid_detail.parents(".layui-layer").height()-height);
    });
    $(window).triggerHandler("resize.jqGrid");
    //清空物料多选框中的值
    function removeChoice() {
        $("#s2id_shelvesLocation .select2-choices").children(".select2-search-choice").remove();
        $("#materialBox").select2("val", "");
        selectData = 0;
    }

    //添加物料详情
    function addMaterial() {
        if ($("#boxId").val() == "") {
            layer.alert("请先保存表单信息！");
            return;
        }
        if (selectData != 0) {
            //将选中的物料添加到数据库中
            $.ajax({
                type: "POST",
                url: context_path + '/box/addBoxDetail',
                data: {boxedId: $('#boxForm #boxId').val(), materialContent: selectData.toString()},
                dataType: "json",
                success: function (data) {
                    removeChoice();   //清空下拉框中的值
                    if (Boolean(data.result)) {
                        layer.msg("添加成功", {icon: 1, time: 1200});
                        //重新加载详情表格
                        $("#grid-table-c").jqGrid('setGridParam',
                            {
                                url: context_path + '/box/boxDetailList',
                                postData: {boxedId:$("#boxId").val()} //发送数据  :选中的节点
                            }
                        ).trigger("reloadGrid");
                    } else {
                        layer.alert(data.msg, {icon: 2, time: 1200});
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
            // {label: "删除", onclick: delBoxDetail},
        ]
    });


    $(document).ready(function () {
        //初始化盘点任务信息
        $.ajax({
            url: context_path + "/box/getBoxById?tm=" + new Date(),
            type: "POST",
            data: {id: $("#boxId").val()},
            dataType: "JSON",
            success: function (data) {
                if (data) {
                    //将盘点信息填充到form中
                    $("#boxedBarCode").val(data.boxedBarCode);
                    $("#boxTime").val(data.boxTime);
                    $("#remark").val(data.remark);
                    $("#instorageId").select2("data", {
                        id: data.instorageId,
                        text: data.instorageNo
                    });
                    $("#boxUserId").select2("data", {id: data.boxUserId, text: data.userName==null?"":data.userName});
                    $("#boxedType").select2("data", {id: data.boxedType, text: data.boxedTypeName==null?"":data.boxedTypeName});
                    if(data.boxedType=="WMS_BOXTYPE_OUT"){
                        $("#locationId").val("");
                        $("#instorageId").select2("data", {
                            id: data.instorageId,
                            text: data.instorageNo==null?"":data.instorageNo
                        });
                    }
                    else{
                        $("#locationId").select2("data", {id: data.locationId, text: data.locationName==null?"":data.locationName});
                        $("#instorageId").val("");
                    }

                }
            }
        });
    });

    //数量输入验证
    function numberRegex(value, colname) {
        var regex = /^\d+\.?\d{0,2}$/;
        reloadDetailTableList();
        if (!regex.test(value)) {
            return [false, ""];
        }
        else  return [true, ""];
    }

    /**
     * 盘点详情审核
     */
    function auditStockCheckDetail(stockCheckId,stockCheckState){
        $.ajax({
            url: context_path + "/stockCheck/auditStockCheckDetail?tm=" + new Date(),
            type: "POST",
            data: {stockCheckId:stockCheckId,stockCheckState:stockCheckState},
            dataType: "JSON",
            success: function (data) {
                if (data) {
                    $("#grid-table-c").jqGrid('setGridParam',
                        {
                            postData: {stockCheckId: $("#stockCheckId").val()} //发送数据  :选中的节点
                        }
                    ).trigger("reloadGrid");
                }
            }
        });
    }

    $("#materialBox").select2({
        placeholder: "选择翻包物料",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        ajax: {
            url: context_path + "/box/getBoxedMaterialList",
            type: "POST",
            dataType: 'json',
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    queryString: term, //联动查询的字符
                    pageSize: 15, //一次性加载的数据条数
                    pageNo: pageNo, //页码
                    time: new Date(),
                    content:$("#boxId").val()
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
    //jqGrid单元格内容设置其内容的字体颜色
    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if(rawObject.materialNo != null ){
            return "style='color:red'";
        }
    }
</script>