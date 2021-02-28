<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<form id="hiddenForm" action="<%=path%>/qualityBill/toExcel" method="POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="hiddenQueryForm" style="display:none;">
		<input name="qualityCode" id="qualityCodeHide" value="">
		<input name="instorageBillCode" id="instorageBillCodeHide" value="">
	</form>
	<div class="query_box" id="yy" title="查询选项">
		<form id="queryForm" style="max-width:100%;">
			<ul class="form-elements">
				<li class="field-group field-fluid3">
					<label class="inline" for="qualityCode" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:92px;">质检单编号：</span>
						<input type="text" id="qualityCode" name="qualityCode" style="width: calc(100% - 97px);" placeholder="质检单编号">
					</label>
				</li>
				<li class="field-group field-fluid3">
					<label class="inline" for="instorageBillCode" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:92px;">入库单编号：</span>
						<input type="text" id="instorageBillCode" name="instorageBillCode" style="width: calc(100% - 97px);" placeholder="入库单编号">
					</label>
				</li>
			</ul>
			<div class="field-button" style="">
				<div class="btn btn-info" onclick="queryOk();">
					<i class="ace-icon fa fa-check bigger-110"></i>查询
				</div>
				<div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
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
<script type="text/javascript">
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    var dynamicDefalutValue="7eaf063c25564277ab90aea11790e3ac";
    $(function  (){
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id:"__tb__01",
        items:[
            {label: "添加", disabled:(${sessionUser.addQx}==1?false:true),onclick:addQualityBill,iconClass:'glyphicon glyphicon-plus'},
			{label: "编辑", disabled:(${sessionUser.editQx}==1?false:true),onclick:editQualityBill,iconClass:'glyphicon glyphicon-pencil'},
			{label: "删除", disabled:(${sessionUser.deleteQx}==1?false:true),onclick:deleteQualityBill,iconClass:'glyphicon glyphicon-trash'},
			{label: "查看", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:detailQualityBill, iconClass:'icon-ok'},
			{label: "导出", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function () {
				var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
				$("#ids").val(ids);
				$("#hiddenForm").submit();
			},iconClass:' icon-share'},
			{label:"打印", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:function(){
				print();
				// var queryBean = iTsai.form.serialize($('#hiddenQueryForm'));   //获取form中的值：json对象
				// var queryJsonString = JSON.stringify(queryBean);
				// var url = context_path + "/instorageOrder/exportPrint?"+"id=" + 1;
				// window.open(url);
			},iconClass:' icon-print'}
    ]
    });

    $(function(){
        _grid = jQuery("#grid-table").jqGrid({
            url : context_path + "/qualityBill/getList",
            datatype : "json",
            colNames : [ "主键","质检单编号","入库单编号","质检时间","状态","备注"],
            colModel : [
                {name : "id",index : "id",width : 20,hidden:true},
                {name : "qualityCode",index : "quality_Code",width : 40},
                {name : "instorageBillCode",index : "instorage_Bill_Code",width : 40},
                {name : "qualityTime",index : "quality_Time",width : 40},
                {name : "state",index : "state",width : 40,
                    formatter:function(cellValue){
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>未提交</span/>";
                        } else if (cellValue == 1) {
                            return "<span style='color:blue;font-weight:bold;'>质检通过</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:red;font-weight:bold;'>质检退回</span>";
                        }
                    }
                },
                {name : "remark",index : "remark",width : 40, sortable: false}
            ],
            rowNum : 20,
            rowList : [ 10, 20, 30 ],
            pager : "#grid-pager",
            sortname : "quality_code",
            sortorder : "desc",
            altRows: false,
            viewrecords : true,
            autowidth:true,
            multiselect:true,
            multiboxonly: true,
            beforeRequest:function (){
                dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
                //重新加载列属性
            },
            loadComplete : function(data) {
                var table = this;
                setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
                oriData = data;
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext : "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        //在分页工具栏中添加按钮
        jQuery("#grid-table").navGrid("#grid-pager",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager',{
            caption:"",
            buttonicon:"ace-icon fa fa-refresh green",
            onClickButton: function(){
                //jQuery("#grid-table").trigger("reloadGrid");  //重新加载表格
                $("#grid-table").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString:""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        }).navButtonAdd("#grid-pager",{
            caption: "",
            buttonicon:"faicon-cogs",
            onClickButton : function (){
                jQuery("#grid-table").jqGrid("columnChooser",{
                    done: function(perm, cols){
                        dynamicColumns(cols,dynamicDefalutValue);
                        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width()-3);
                    }
                });
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() );
            $("#grid-table").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy").outerHeight(true)-$("#fixed_tool_div").outerHeight(true)-$("#grid-pager").outerHeight(true)
                -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
        });
        $(window).triggerHandler("resize.jqGrid");
    });
    //查询
    function queryOk(){
        var queryParam = iTsai.form.serialize($("#queryForm"));
        queryPlatformByParam(queryParam);
    }

    function queryPlatformByParam(jsonParam){
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
    function reset(){
        $("#queryForm #licensePlateNumber").val("");
        $("#queryForm #status").val("");
        $("#queryForm #createTime").val("");
		$("#queryForm #qualityCode").val("");
		$("#queryForm #instorageBillCode").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }
    //添加
    function addQualityBill(){
        $.post(context_path+"/qualityBill/toEdit", {}, function(str){
            $queryWindow = layer.open({
                title : "质检单添加",
                type: 1,
                skin : "layui-layer-molv",
                area : ["750px","650px"],
                shade: 0.6, //遮罩透明度
                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                content: str,//注意，如果str是object，那么需要字符拼接。
                success:function(layero, index){
                    layer.closeAll("loading");
                }
            });
        }).error(function() {
            layer.closeAll();
            layer.msg("加载失败！",{icon:2});
        });
    }

    //修改
    function editQualityBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要编辑的质检单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个质检单进行编辑操作！");
            return false;
        } else {
            var qualityBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', qualityBillId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("质检单已提交，不可修改",{time:1200,icon:2});
                return false;
            }
            $.post(context_path+"/qualityBill/toEdit?qualityBillId="+qualityBillId+"&edit=edit", {}, function(str){
                $queryWindow = layer.open({
                    title : "质检单编辑",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["750px","650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });
        }
    }

    //查看
    function detailQualityBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要查看的质检单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个质检单进行查看操作！");
            return false;
        } else {
            var qualityBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path+"/qualityBill/toDetail?qualityBillId="+qualityBillId, {}, function(str){
                $queryWindow = layer.open({
                    title : "质检单详情",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["750px","650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });
        }
    }

    //删除
    function deleteQualityBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");  //选中的数量
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要删除的质检单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个质检单进行删除操作！");
            return false;
        } else{
            //从数据库中删除选中的质检单，并刷新质检单表格
            var qualityBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', qualityBillId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("质检单已提交，不可删除",{time:1200,icon:2});
                return false;
            }
            //弹出确认窗口
            layer.confirm("确定删除选中的质检单？", function() {
                $.ajax({
                    type : "POST",
                    url:context_path + "/qualityBill/deleteQualityBill?id="+qualityBillId,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if(data.result){
                            layer.msg(data.msg, {icon: 1,time:1000});
                        }else{
                            layer.msg(data.msg, {icon: 2,time:1000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

</script>
</html>