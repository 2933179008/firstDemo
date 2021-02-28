<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<style type="text/css"></style>
<div id="grid-div">
	<form id="hiddenForm" action = "<%=path%>/platform/toExcel" method = "POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
    <form id="hiddenQueryForm" style="display:none;">
        <input id="beanNameS" name="beanName" value=""/>
    </form>
    <div class="query_box" id="yy" title="查询选项">
            <form id="queryForm" style="max-width:100%;">
			 <ul class="form-elements">
				<label class="inline" for="beanName" style="margin-right:20px;width:100%;">
					<span class="form_label" style="width:85px;">bean名称：</span>
					<input type="text" name="beanName" id="beanName" value="" style="width: 385px" placeholder="bean名称">
				</label>
			 </ul>
			<div class="field-button">
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
var context_path = '<%=path%>';
var _grid;
$(function  (){
    $(".toggle_tools").click();
});
$("#__toolbar__").iToolBar({
    id: "__tb__01",
    items: [
        {label: "新增", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addJob, iconClass:'glyphicon glyphicon-plus'},
        {label: "修改", disabled: (${sessionUser.editQx} == 1 ? false : true), onclick: editJob, iconClass:'glyphicon glyphicon-pencil'},
        {label: "删除", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: deleteJob, iconClass:'glyphicon glyphicon-trash'},
		{label: "暂停", disabled: (${sessionUser.queryQx} == 1 ? false:true), onclick: pauseJob,iconClass:'fa fa-pause'},
		{label: "恢复", disabled: (${sessionUser.queryQx} == 1 ? false:true), onclick:resumeJob,iconClass:'fa fa-play'},
		{label: "立即执行", disabled: (${sessionUser.queryQx} == 1 ? false:true), onclick:runOnceJob,iconClass:'fa fa-arrow-circle-right'}
        ]
});
$(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/schedule/list.do",
            datatype: "json",
            colNames: ["任务ID", "bean名称", "方法名称","参数", "cron表达式", "状态","备注",],
            colModel: [
                {name: "jobId", index: "jobId", width: 20, hidden: true},
                {name: "beanName", index: "beanName", width: 65},
                {name: "methodName", index: "methodName", width: 100},
                {name: "params", index: "params", width: 100},
                {name: "cronExpression", index: "cronExpression", width: 100},
                {name: "status",index:"status",width:50,formatter:function(cellvalue){
                        if(cellvalue == 0) {
                            return "<span class=\'label label-success\'>正常</span>";
                        } else if(cellvalue == 1) {
                            return "<span class=\'label label-danger\'>暂停</span>";
                        }
                    }
                },
                {name: "remark", index: "remark", width: 100, sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "jobId",
            sortorder: "asc",
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
                oriData = data;
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext: "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        jQuery("#grid-table").navGrid("#grid-pager", {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd("#grid-pager", {
                    caption: "",
                    buttonicon: "ace-icon fa fa-refresh green",
                    onClickButton: function () {
                        $("#grid-table").jqGrid("setGridParam",
                                {
                                    postData: {queryJsonString: ""} //发送数据
                                }
                        ).trigger("reloadGrid");
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
	   $("#queryForm #beanName").val("");
	   $("#grid-table").jqGrid("setGridParam",
			{
				postData: {queryJsonString:""} //发送数据
			}
		).trigger("reloadGrid");
	}

	//添加
	function addJob(){
	   $.post(context_path+"/schedule/toEdit.do?jobId=-1", {}, function(str){
			$queryWindow = layer.open({
				title : "定时任务添加",
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
	function editJob(){
		var checkedNum = getGridCheckedNum("#grid-table","id");
		if(checkedNum == 0)
		{
			layer.alert("请选择一个要编辑的记录！");
			return false;
		} else if(checkedNum >1){
			layer.alert("只能选择一个要编辑的记录！");
			return false;
		} else {
            debugger;
            var jobId = getGridCheckedId("#grid-table","jobId");
			$.post(context_path+"/schedule/toEdit.do?jobId="+jobId, {}, function(str){
			$queryWindow = layer.open({
				title : "定时任务编辑",
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
	function deleteJob(){
		var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
		if (checkedNum == 0) {
			layer.alert("请选择一个要删除的任务！");
		} else {
            var jobIds = getGridCheckedId("#grid-table","jobId");
			$.ajax({
				type : "POST",
				url : context_path + "/schedule/delete?jobIds="+jobIds ,
				dataType : "json",
				cache : false,
				success : function(data) {
					layer.closeAll();
					if (Boolean(data.result)) {
						layer.msg(data.msg, {icon: 1,time:1000});
					}else{
						layer.msg(data.msg, {icon: 7,time:2000});
					}
					_grid.trigger("reloadGrid");  //重新加载表格
				}
			});
		}
	}

	//暂停
	function pauseJob() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要暂停的任务！");
        } else {
            debugger;
            var ids = getGridCheckedId("#grid-table","jobId");
            layer.confirm("确定要暂停选中的记录？", function() {
				$.ajax({
					type : "POST",
					url : context_path + "/schedule/pause?jobIds="+ids ,
					dataType : "json",
					cache : false,
					success : function(data) {
						layer.closeAll();
						if (Boolean(data.result)) {
							layer.msg(data.msg, {icon: 1,time:1000});
						}else{
							layer.msg(data.msg, {icon: 7,time:2000});
						}
						_grid.trigger("reloadGrid");  //重新加载表格
					}
				});
            });
        }
	}

	//恢复
	function resumeJob() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要恢复的任务！");
        } else {
            var ids = getGridCheckedId("#grid-table","jobId");
            layer.confirm("确定要恢复选中的记录？", function() {
                $.ajax({
                    type : "POST",
                    url : context_path + "/schedule/resume?jobIds="+ids ,
                    dataType : "json",
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if (Boolean(data.result)) {
                            layer.msg(data.msg, {icon: 1,time:1000});
                        }else{
                            layer.msg(data.msg, {icon: 7,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
	}

	//立即执行
	function runOnceJob() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要立即执行选中的任务！");
        } else {
            var ids = getGridCheckedId("#grid-table","jobId");
            layer.confirm("确定要立即执行选中的记录？", function() {
                $.ajax({
                    type : "POST",
                    url : context_path + "/schedule/run?jobIds="+ids ,
                    dataType : "json",
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if (Boolean(data.result)) {
                            layer.msg(data.msg, {icon: 1,time:1000});
                        }else{
                            layer.msg(data.msg, {icon: 7,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
	}
</script>