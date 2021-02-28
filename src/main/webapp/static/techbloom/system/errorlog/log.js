var oriData; 
var _grid;
var $queryWindow;  //查询窗口对象
/*   $(".datepicker").datepicker(); */
$("#operationTime").daterangepicker({
	'applyClass' : 'btn-sm btn-success',
	'cancelClass' : 'btn-sm btn-default',
	'opens' : 'left',
	'format' :'YYYY-MM-DD',
	'timePicker' :false,
	'timePickerIncrement' : 5,
	'timePicker12Hour' : false,
	locale: {
		fromLabel: '从',
		toLabel: '到',
		applyLabel: '确定',
		cancelLabel: '取消'
	}
});
_grid = jQuery("#grid-table").jqGrid({
	url : context_path + '/errorlog/list.do',
	datatype : "json",
	styleUI: 'Bootstrap',
	colNames : [ '错误日志信息','发生时间', '备注'],
	colModel : [ 
	            
	            {name : 'info',index : 'info',sortable : false}, 
	            {name : 'createTime',index : 'createTime',width : 100,fixed:true,},
	            {name : 'remark',index : 'remark', width : 100,fixed:true},
	            ],
	            rowNum : 20,
	            rowList : [ 10, 20, 30 ],
	            pager : '#grid-pager',
	            sortname : 'createTime',
	            sortorder : "desc",
	            rownumbers:true,
	            altRows: true,
	            viewrecords : true,
	            caption : "日志明细",
	            hidegrid:false,
	            /* 	       autowidth:true, */
	            multiselect:false,
	            loadComplete : function(data) 
	            {
	            	var table = this;
	            	setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
	            	oriData = data;
	            },
	            emptyrecords: "没有相关记录",
	            loadtext: "加载中...",
	            pgtext : "页码 {0} / {1}页",
	            recordtext: "显示 {0} - {1}共{2}条数据"
});

jQuery("#grid-table").navGrid('#grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
.navButtonAdd('#grid-pager',{  
	caption:"",   
	buttonicon:"fa fa-refresh green",   
	onClickButton: function(){   
		$("#grid-table").jqGrid('setGridParam', 
				{
			 		postData: {queryJsonString:"",page:1} //发送数据 
				}
		).trigger("reloadGrid");
	}
});

$(window).on('resize.jqGrid', function () {
	$("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width() -7);
	$("#grid-table").jqGrid( 'setGridHeight', ($(window).height()-$("#table_toolbar").outerHeight(true)- $("#grid-pager").outerHeight(true)-$("#user-nav").height()-$("#breadcrumb").height()-$(".ui-jqgrid-labels").height()-35 ) );
});

$(window).triggerHandler('resize.jqGrid');


/** 日志删除  */
function delRole()
{
	var ids = getGridCheckedId("#grid-table","id");
	Dialog.confirm("确定删除选中的日志信息吗？",function(){
		$.ajax({
			type: "POST",
			url: context_path + '/log/deleteLog.do?tm=' + new Date().getTime(),
			data: {"ids" : ids},
			dataType:'json',
			cache: false,
			success: function(data){
				if(data._result){
					top.Dialog.tip("删除日志成功！", {delay:1200});
					gridReload();
				}else{
					Dialog.error("删除日志失败！");
				}
			}
		});
	},function(){
		return false;
	});
}

//日志导出
function exportLogFile(){
	layer.load(2,{shade: 0.6});
	$.post(context_sys_path+'/system/log/customize.jsp', {}, 
			function(str){
		$queryWindow = layer.open({
			title : "日志导出", 
			type: 1,
			skin : "layui-layer-molv",
			area : ['600px','500px'],
			shade: 0.6, //遮罩透明度
			moveType: 1, //拖拽风格，0是默认，1是传统拖动
			content: str,//注意，如果str是object，那么需要字符拼接。
			success:function(layero, index){
				layer.closeAll('loading');
				$("#hiddenQueryForm #userName").val($("#queryForm #userName").val());
				$("#hiddenQueryForm #operation").val($("#queryForm #operation").val());
				var times = $("#queryForm #operationTime").val().split(" - ");
				$("#hiddenQueryForm #operationTime").val(times[0]);
				$("#hiddenQueryForm #operationTime_end").val(times[1]);
			}
		});
	}
	);
}

//重新加载表格
function gridReload()
{
	_grid.trigger("reloadGrid");  //重新加载表格  
}


/**
 * 打开查询界面
 */
function openLogQueryPage(){

	var queryParam = iTsai.form.serialize($('#queryForm'));
	//console.log(queryParam);
	//接着执行刷新列表的操作
	var times = queryParam.operationTime.split(" - ");

	queryParam.operationTime = times[0];
	queryParam.operationTime_end = times[1];
	queryLogListByParam(queryParam);
	/* var queryBean = iTsai.form.serialize($('#hiddenQueryForm'));   //获取form中的值：json对象
    	var queryJsonString = JSON.stringify(queryBean);         //将json对象转换成json字符串


    	$.post(context_path+'/web/system/log/query_log_list.jsp', {log : queryBean}, function(str){
    		$queryWindow = layer.open({
    			title : "日志查询", 
   		    	type: 1,
   		    	skin : "layui-layer-molv",
   		    	area : "600px",
   		    	content: str, //注意，如果str是object，那么需要字符拼接。
   		    	success:function(layero,index){
   		    		//窗口加载成功之后调用的方法
   		    		debugger
   		    		iTsai.form.deserialize($('#queryForm'),queryBean);
   		        },
   		    	end:function(){
   		    		//关闭窗口的时候调用
   		    	}
   			});
   		});
	 */
}


/**
 * 查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryLogListByParam(jsonParam){
	//console.log(jsonParam);
	//序列化表单：iTsai.form.serialize($('#frm'))
	//反序列化表单：iTsai.form.deserialize($('#frm'),json)
	/* iTsai.form.deserialize($('#hiddenQueryForm'),jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
    	var queryParam = iTsai.form.serialize($('#hiddenQueryForm')); */
	var queryJsonString = JSON.stringify(jsonParam);         //将json对象转换成json字符串
	//执行查询操作
	$("#grid-table").jqGrid('setGridParam', 
			{
		postData: {queryJsonString:queryJsonString} //发送数据 
			}
	).trigger("reloadGrid");
}