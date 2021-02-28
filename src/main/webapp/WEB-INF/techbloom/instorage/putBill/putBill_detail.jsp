<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<form id="baseInfor" class="form-horizontal" target="_ifr">
		<!-- 上架单主键 -->
		<input type="hidden" id="id" name="id" value="${putBill.id}">
		<%--一行数据 --%>
		<div class="row" style="margin:0;padding:0;">
			<%--上架单编号--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">上架单编号：</label>
				<div class="controls" style="margin-top:4px;">
					${putBillCode}
				</div>
			</div>
			<%--入库单--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">入库单：</label>
				<div class="controls" style="margin-top:4px;">
					${putBill.instorageBillCode}
				</div>
			</div>
		</div>
		<div class="row" style="margin:0;padding:0;">
			<%--操作人--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">操作人：</label>
				<div class="controls" style="margin-top:4px;">
					${putBill.operatorName}
				</div>
			</div>
			<%--备注--%>
			<div class="control-group span6" style="display: inline">
				<label class="control-label">备注：</label>
				<div class="controls" style="margin-top:4px;">
					${instorage.remark}
				</div>
			</div>
		</div>
	</form>
	<div id="materialDiv" style="margin:10px;">
		<br />
	</div>
	<!-- 表格div -->
	<div id="grid-div-c" style="width:100%;margin:0px auto;">
		<!-- 物料详情信息表格 -->
		<table id="grid-table-c" style="width:100%;height:100%;"></table>
		<!-- 表格分页栏 -->
		<div id="grid-pager-c"></div>
	</div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';

    function reloadDetailTableList(){
        $("#grid-table-c").jqGrid("setGridParam", {
            postData: {instorageId:$("#baseInfor #id").val()} //发送数据  :选中的节点
        }).trigger("reloadGrid");
    }

    $("#baseInfor #instorageBillId").select2("data", {
        id: $("#baseInfor #instorageBillId").val(),
        text: $("#baseInfor #instorageBillCode").val()
    });
    $("#baseInfor #operator").select2("data", {
        id: $("#baseInfor #operator").val(),
        text: $("#baseInfor #operatorName").val()
    });


    //操作人下拉列表数据源获取
    $("#baseInfor #operator").select2({
        placeholder: "--选择操作人--",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        ajax: {
            url: context_path + "/putBill/getSelectOperator",
            type: "POST",
            dataType: 'json',
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
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
                        results: {}
                    };
                }
            },
            cache: true
        }

    });
</script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss',useMinutes:true,useSeconds:true});
    var putBIllId=$("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    var select_dataLocation = "";
    _grid_detail=jQuery("#grid-table-c").jqGrid({
        url : context_path + "/putBill/getDetailList?putBIllId="+putBIllId,
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","批次号","包装单位","上架数量","上架重量（kg）","RFID","库位"],
        colModel : [
            {name : "id",index : "id",width : 20,hidden:true},
            {name : "materialCode",index:"material_Code",width :20},
            {name : "materialName",index:"material_Name",width : 20},
            {name : "batchNo",index:"batch_No",width : 20},
            {name : "unit",index:"unit",width : 20},
            {name: 'putAmount', index: 'put_Amount', width: 20},
            {name : "putWeight",index:"put_Weight",width : 30},
            {name: 'rfid', index: 'rfid', width: 30},
            {name : "positionCode",index:"position_Code",width : 30,
                editoptions:{
                    value:getPosition(),
                    dataEvents:[{				//给当前空间 追加事件处理
                        type:'focus',
                        fn:function(e){
                            var putBilDetailId  = $('#grid-table-c').jqGrid('getGridParam','selrow');
                            // $("#grid-table-c select option").remove();
                            $.ajax({
                                type: "post",
                                async: false,
                                url: context_path + "/putBill/getRecommendPosition?putBilDetailId="+putBilDetailId,
                                dataType: "json",
                                success: function (data) {
                                    var recommendPositionCode = data.recommendPositionCode;
                                    $("#grid-table-c select").val(recommendPositionCode);
                                }
                            })

                        }
                    }],
                },
                formatter: function (cellValue, option, rowObject) {
                    var vle = cellValue || "";
                    if (select_dataLocation && select_dataLocation.result) {
                        for (var i = 0; i < select_dataLocation.result.length; i++) {
                            if (select_dataLocation.result[i].positionCode == cellValue) {
                                vle = select_dataLocation.result[i].positionName;
                            }
                        }
                    }
                    return vle;
                }
            }
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : "#grid-pager-c",
        sortname : "material_code",
        sortorder : "desc",
        altRows: true,
        viewrecords : true,
        autowidth:true,
        multiselect:true,
        multiboxonly: true,
        loadComplete : function(data){
            var table = this;
            setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
            oriDataDetail = data;
            $(window).triggerHandler('resize.jqGrid');
        },
        cellEdit: true,
        cellsubmit : "clientArray",
        emptyrecords: "没有相关记录",
        loadtext: "加载中...",
        pgtext : "页码 {0} / {1}页",
        recordtext: "显示 {0} - {1}共{2}条数据",
    });
    //在分页工具栏中添加按钮
    $("#grid-table-c").navGrid("#grid-pager-c",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager-c',{
        caption:"",
        buttonicon:"ace-icon fa fa-refresh green",
        onClickButton: function(){
            $("#grid-table-c").jqGrid("setGridParam",
                {
                    url:context_path + '/putBill/getDetailList?receiptId='+receiptId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3 );
        var height = $(".layui-layer-title",_grid_detail.parents(".layui-layer")).height()+
            $("#baseInfor").outerHeight(true)+$("#materialDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight',_grid_detail.parents(".layui-layer").height()-height);
    });
    $(window).triggerHandler("resize.jqGrid");

    //获取库位下拉框数据源
    function getPosition(){
        var str = "";
        $.ajax({
            type: "post",
            async: false,
            url: context_path + "/putBill/getPosition",
            dataType: "json",
            success: function (data) {
                select_dataLocation = data;
                for (var i = 0; i < data.result.length; i++) {
                    str += data.result[i].positionCode + ":" + data.result[i].positionName + ";";
                }
                str = str.substring(0, str.length - 1);
            }
        })
        return str;
    }
</script>