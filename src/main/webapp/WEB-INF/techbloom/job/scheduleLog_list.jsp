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
        <input id="jobIdS" name="jobId" value=""/>
    </form>
    <div class="query_box" id="yy" title="查询选项">
            <form id="queryForm" style="max-width:100%;">
			 <ul class="form-elements">
				<label class="inline" for="jobId" style="margin-right:20px;width:100%;">
					<span class="form_label" style="width:85px;">任务ID：</span>
					<input type="text" name="jobId" id="jobId" value="" style="width: 385px" placeholder="任务ID">
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

$(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/scheduleLog/list.do",
            datatype: "json",
            colNames: ["日志ID", "任务ID", "bean名称","方法名称", "参数", "状态", "耗时(单位：毫秒)", "执行时间"],
            colModel: [
                {name: "logId", index: "logId", width: 20},
                {name: "jobId", index: "jobId", width: 20},
                {name: "beanName", index: "beanName", width: 65},
                {name: "methodName", index: "methodName", width: 100},
                {name: "params", index: "params", width: 100},
                {name: "status",index:"status",width:50,formatter:function(cellvalue){
                        return cellvalue === 0 ?
                            '<span class="label label-success">成功</span>' :
                            '<span class="label label-danger pointer">失败</span>';
                    }
                },
                {name: "times", index: "times", width: 100},
                {name: "createTime", index: "createTime", width: 100}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "createTime",
            sortorder: "desc",
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
	   $("#queryForm #jobId").val("");
	   $("#grid-table").jqGrid("setGridParam",
			{
				postData: {queryJsonString:""} //发送数据
			}
		).trigger("reloadGrid");
	}

</script>