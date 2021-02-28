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
	<form id="hiddenForm" action="<%=path%>/putBill/toExcel" method="POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="hiddenQueryForm" style="display:none;">
		<input name="putBillCode" id="putBillCodeHide" value="">
		<input name="instorageBillCode" id="instorageBillCodeHide" value="">
		<input name="createTime" id="createTimeHide" value="">
		<input name="userName" id="userName" value="">
	</form>
	<div class="query_box" id="yy" title="查询选项">
		<form id="queryForm" style="max-width:100%;">
			<ul class="form-elements">
				<li class="field-group field-fluid3">
					<label class="inline" for="putBillCode" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:92px;">上架单编号：</span>
						<input type="text" id="putBillCode" name="putBillCode" style="width: calc(100% - 97px);" placeholder="上架单编号">
					</label>
				</li>
				<li class="field-group field-fluid3">
					<label class="inline" for="instorageBillCode" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:92px;">入库单编号：</span>
						<input type="text" id="instorageBillCode" name="instorageBillCode" style="width: calc(100% - 97px);" placeholder="入库单编号">
					</label>
				</li>
				<li class="field-group field-fluid3">
					<label class="inline" for="createTime" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:75px;">创建时间：</span>
						<input type="text" id="createTime" name="createTime" class="form-control date-picker" style="width: calc(100% - 97px);" placeholder="创建时间" />
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
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/instorageBill/instorage_list.js"></script>
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
            {label: "添加", disabled:(${sessionUser.addQx}==1?false:true),onclick:addPutBill,iconClass:'glyphicon glyphicon-plus'},
			{label: "编辑", disabled:(${sessionUser.editQx}==1?false:true),onclick:editPutBill,iconClass:'glyphicon glyphicon-pencil'},
			{label: "删除", disabled:(${sessionUser.deleteQx}==1?false:true),onclick:deletePutBill,iconClass:'glyphicon glyphicon-trash'},
			{label: "查看", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:detailPutBill, iconClass:'icon-ok'},
			{label: "上架确认", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:surePutBill, iconClass:'icon-zoom-in'},
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
            url : context_path + "/putBill/getList",
            datatype : "json",
            colNames : [ "主键","上架单编号","入库单编号","操作人","创建时间","状态","备注"],
            colModel : [
                {name : "id",index : "id",width : 20,hidden:true},
                {name : "putBillCode",index : "put_Bill_Code",width : 40},
                {name : "instorageBillCode",index : "instorage_Bill_Code",width : 40},
                {name : "operatorName",index : "operator_Name",width : 40},
                {name : "createTime",index : "create_Time",width : 40},
                {name : "state",index : "state",width : 40,
                    formatter:function(cellValue){
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>未提交</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:blue;font-weight:bold;'>待上架</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:blue;font-weight:bold;'>上架中</span>";
                        } else if (cellValue == 3) {
                            return "<span style='font-weight:bold;'>上架完成</span>";
                        }
                    }
                },
				{name : "remark",index : "remark",width : 40, sortable: false},
            ],
            rowNum : 20,
            rowList : [ 10, 20, 30 ],
            pager : "#grid-pager",
            sortname : "put_bill_code",
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
        $("#queryForm #instorageBillCode").val("");
        $("#queryForm #putBillCode").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }
    //添加
    function addPutBill(){
        $.post(context_path+"/putBill/toEdit", {}, function(str){
            $queryWindow = layer.open({
                title : "上架单添加",
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
    function editPutBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要编辑的上架单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个上架单进行编辑操作！");
            return false;
        } else {
            var putBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', putBillId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("上架单已提交，不可修改",{time:1200,icon:2});
                return false;
            }
            $.post(context_path+"/putBill/toEdit?putBillId="+putBillId+"&edit=edit", {}, function(str){
                $queryWindow = layer.open({
                    title : "上架单编辑",
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
    function detailPutBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要查看的上架单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个上架单进行查看操作！");
            return false;
        } else {
            var putBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path+"/putBill/toDetail?putBillId="+putBillId, {}, function(str){
                $queryWindow = layer.open({
                    title : "上架单详情",
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
    function deletePutBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");  //选中的数量
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要删除的上架单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个上架单进行删除操作！");
            return false;
        } else{
            //从数据库中删除选中的上架单，并刷新上架单表格
            var putBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', putBillId).state;
            if(state != '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("上架单已提交，不可删除",{time:1200,icon:2});
                return false;
            }

            //弹出确认窗口
            layer.confirm("确定删除选中的上架单？", function() {
                $.ajax({
                    type : "POST",
                    url:context_path + "/putBill/deletePutBill?id="+putBillId,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if(data.result){
                            layer.msg(data.msg, {icon: 1,time:2000});
                        }else{
                            layer.msg(data.msg, {icon: 2,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    //上架确认
    function surePutBill() {
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要上架的上架单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个上架单进行上架操作！");
            return false;
        } else {
            var putBillId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', putBillId).state;
            if(state == '<span style="font-weight:bold;">未提交</span>'){
                layer.msg("上架单还未提交，不可上架",{time:1200,icon:2});
                return false;
            }
            if(state == '<span style="font-weight:bold;">上架完成</span>'){
                layer.msg("上架单已上架完成",{time:1200,icon:2});
                return false;
            }
            $.post(context_path+"/putBill/toSurePutBill?putBillId="+putBillId, {}, function(str){
                $queryWindow = layer.open({
                    title : "上架确认操作",
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
</script>
</html>