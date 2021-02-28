<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<div id="grid-div">
    <form id="hiddenQueryForm" style="display:none;">
        <input id="address" name="address" value=""/>
        <input id="requesttimes" name="requesttime" value="">
    </form>
    <div class="query_box" id="yy" title="查询选项">
            <form id="queryForm" style="max-width:100%;">
			 <ul class="form-elements">
				<li class="field-group field-fluid2">
					<label class="inline" for="requesttime" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:80px;">请求时间：</span>
						<input class="form-control date-picker" id="requesttime" name="requesttime" style="width: calc(100% - 85px );" type="text" placeholder="请求时间" />
					</label>			
				</li>
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
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript">
var oriData; 
var _grid;
$(".date-picker").datetimepicker({format: "YYYY-MM-DD"});
$(function  (){
   // $(".toggle_tools").click();
});
var _queryForm_data = iTsai.form.serialize($("#queryForm"));
_grid = jQuery("#grid-table").jqGrid({
				url : context_path + "/interfaceLog/list",
			    datatype : "json",
			    colNames : ["主键","接口名称","请求时间","请求参数","请求结果","错误记录"],
			    colModel : [ 
			                 {name : "id", index: "id", hidden: true},
			                 {name : "interfacename",index : "interfacename",width:55},
			                 {name : "requesttime",index : "requesttime",width : 100},
			                 {name : "parameter",index : "parameter",width: 60},
			                 {name : "result",index : "result",width : 60},
			                 {name : "errorInfo",index : "errorInfo",width: 60} 
			               ],
			    rowNum : 20,
			    rowList : [ 10, 20, 30 ],
			    pager : "#grid-pager",
			    sortname : "requesttime",
			    sortorder : "desc",
	            altRows: true,
	            viewrecords : true,
	            hidegrid:false,
	     	    autowidth:true, 
	            multiselect:true,
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

jQuery("#grid-table").navGrid("#grid-pager",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd("#grid-pager",{  
	caption:"",   
	buttonicon:"fa fa-refresh green",   
	onClickButton: function(){   
		$("#grid-table").jqGrid("setGridParam", 
				{
			postData: {queryJsonString:""} //发送数据 
				}
		).trigger("reloadGrid");
	}
});

$(window).on("resize.jqGrid", function () {
	$("#grid-table").jqGrid("setGridWidth", $(window).width()-$("#sidebar").width() -7);
	$("#grid-table").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy").outerHeight(true)-$("#fixed_tool_div").outerHeight(true)-$("#grid-pager").outerHeight(true)
   -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
});

$(window).triggerHandler("resize.jqGrid");

/**
 * 重新加载表格
 */
function gridReload() {
	_grid.trigger("reloadGrid");  //重新加载表格  
}

/**
 * 查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryLogListByParam(jsonParam) {
	var queryJsonString = JSON.stringify(jsonParam);         //将json对象转换成json字符串
	//执行查询操作
	$("#grid-table").jqGrid("setGridParam", 
			{
		postData: {queryJsonString:queryJsonString} //发送数据 
			}
	).trigger("reloadGrid");
}

/**
 * 查询按钮点击事件
 */
function queryOk(){
	 var queryParam = iTsai.form.serialize($("#queryForm"));
	 //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
	 queryLogsByParam(queryParam);
}

/**
 * 获取列表数据
 * @param jsonParam
 */
function queryLogsByParam(jsonParam) {
	    iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);
	    var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
	    var queryJsonString = JSON.stringify(queryParam); 
	    $("#grid-table").jqGrid("setGridParam",
	        {
	            postData: {queryJsonString: queryJsonString}
	        }
	    ).trigger("reloadGrid");
}

/**
 * 重置
 */
function reset(){
	 iTsai.form.deserialize($("#queryForm"),_queryForm_data);
    queryOk();
}
</script>