<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	
%>

<style type="text/css">

	*{transition: 0.3s;}
	.msgDiv{height:47%;width:48%;border:solid #547f98 1px;float:left;
	        margin:10px;box-shadow:3px 3px 10px #3e3e3e;display:inline;border-radius:5px;}
	.msgTitle{color:#ffffff;background-color:#3b73af;
		height:30px;line-height:30px;vertical-align: middle;    border-top-left-radius: 3px;
        border-top-right-radius: 3px;padding-left:20px;}
	.msgTitle:hover{cursor:pointer}
	.lockPng{position: relative;z-index: 100;float:left;margin-left:38%;margin-top:9%;}
</style>

<div style="height:100%;width:100%;">
	<div class="msgDiv">
		<div class="msgTitle disable" ondblclick="enterDetail(1);">入库未审核</div>
		<img class="lockPng zoomIn animated" id="inImg" style="<c:if test="${1==1}">display:none;</c:if>"
		 alt="暂无权限" src="<%=path %>/static/images/lock_128_blue.png">
		<!-- 入库未审核 -->
		<div class="grid-div <c:if test="${1!=1}">hide</c:if>" id="in-grid-div">
	<!--    物料信息表格 -->
			<table id="in-grid-table"></table>
	<!-- 	表格分页栏 -->
			<div id="in-grid-pager" style="width:97%;"></div>
	    </div>
	</div>
	<div class="msgDiv">
		<div class="msgTitle" ondblclick="enterDetail(2);">出库未审核</div>
		<img class="lockPng  zoomIn animated" id="outImg" style="<c:if test="${1==1}">display:none;</c:if>"
		alt="暂无权限" src="<%=path %>/static/images/lock_128_blue.png">
		<!-- 出库未审核 -->
		<div class="grid-div <c:if test="${1!=1}">hide</c:if>" id="out-grid-div">
	<!--    物料信息表格 -->
			<table id="out-grid-table"></table>
	<!-- 	表格分页栏 -->
			<div id="out-grid-pager" style="width:97%;"></div>
	    </div>
	</div>
	<div class="msgDiv">
		<div class="msgTitle" ondblclick="enterDetail(3);">库存报警</div>
		<img class="lockPng zoomIn animated" id="stockImg"  style="<c:if test="${1==1}">display:none;</c:if>"
		alt="暂无权限" src="<%=path %>/static/images/lock_128_blue.png">
		<div class="grid-div <c:if test="${1!=1}">hide</c:if>" id="grid-div">
			<table id="grid-table"></table>
			<div id="grid-pager" style="width:97%;"></div>
	    </div>
	</div>
	<div class="msgDiv">
		<div class="msgTitle" ondblclick="enterDetail(4);">收发存汇总</div>
		<img class="lockPng zoomIn animated" id="outAndInImg" style="<c:if test="${1==1}">display:none;</c:if>"
		alt="暂无权限" src="<%=path %>/static/images/lock_128_blue.png">
		<div class="<c:if test="${1!=1}">hide</c:if>" id="mainchartdiv"  style="height:100%;width:100%;"></div>
	</div>
</div><!-- /.main-content -->

<%--<script src="<%=path%>/common/plugins/echarts.min.js"></script>--%>
<script src="<%=path%>/plugins/public_components/js/echarts.min.js"></script>

<script type="text/javascript">
	alert("home")
var context_path = '<%=path%>';
var myChart;
$(function(){
	// 图表实例化------------------
	// srcipt标签式引入
	
	 $("#mainchartdiv").css({
		 width:$('#in-grid-div').width()-5,
		 height:(document.documentElement.clientHeight*0.48 - $("#in-grid-pager").height() - 90)
		
		 });
	myChart = echarts.init(document.getElementById('mainchartdiv'));
   
	// ajax getting data...............
	$.ajax({
		type:"POST",
		url:context_path +"/material/getInAndOutInfor",
		dataType:"json",
		success:function(data){
		
			
			// 图表使用-------------------
			var option = {
				    tooltip: {
				        trigger: 'item'
				    },
				    grid: {
				        borderWidth: 0,
				        y: 80,
				        y2: 60
				    },
				    xAxis: [
				        {
				            type: 'category',
				            show: false,
				            data: ['入库', '出库', '结余']
				        }
				    ],
				    yAxis: [
				        {
				            type: 'value',
				            show: false
				        }
				    ],
				    series: [
				        {
				            name: '收发存汇总',
				            type: 'bar',
				            itemStyle: {
				                normal: {
				                    color: function(params) {
				                        // build a color map as your need.
				                        var colorList = [
				                          '#5cb85c','#C1232B','#45adde'
				                        ];
				                        return colorList[params.dataIndex]
				                    },
				                    label: {
				                        show: true,
				                        position: 'top',
				                        formatter: '{b}\n{c}'
				                    }
				                }
				            },
				            data: [data.inamount,data.outamount,data.canamount]
				        }
				    ]
			};
			myChart.setOption(option);
		}
	});
	
	//初始化表格：显示已提交的入库单
	jQuery("#in-grid-table").jqGrid({
       url : context_path + '/instorageAudit/unAduitInstorageList',
       datatype : "json",
       colNames : [ '主键','入库单编号','供应商','入库时间','制单人'],
       colModel : [ 
					{name : 'id',index : 'id',width : 20,hidden:true}, 
                    {name : 'documentNo',index : 'DOCUMENT_NO',width : 60}, 
                    {name : 'supplierName',width : 50}, 
                    {name : 'instorageDate',index:'INSTORAGE_DATE',width : 70,
                    	formatter:function(cellValu,option,rowObject){
                    		return cellValu.substring(0,19);
                    	}	
                    },
                    {name : 'makername',width : 50}, 
                  ],
       rowNum : 20,
       rowList : [ 10, 20, 30 ],
       pager : '#in-grid-pager',
       sortname : 'ID',
       sortorder : "desc",
       altRows: true,
       viewrecords : true,
       autowidth:true,
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
	
  //在分页工具栏中添加按钮
  jQuery("#in-grid-table").navGrid('#in-grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
    .navButtonAdd('#in-grid-pager',{  
	   caption:"",   
	   buttonicon:"ace-icon fa fa-refresh green",   
	   onClickButton: function(){   
		   $("#in-grid-table").jqGrid('setGridParam', 
					{
						postData: {queryJsonString:""} //发送数据 
					}
			).trigger("reloadGrid");
	   }
	});
	
	$(window).on('resize.jqGrid', function () {
		$("#in-grid-table").jqGrid( 'setGridWidth', $('#in-grid-div').width()-5);
		$("#in-grid-table").jqGrid( 'setGridHeight', (document.documentElement.clientHeight*0.48 - $("#in-grid-pager").height() - 130) );
	});
	
	
	
	jQuery("#out-grid-table").jqGrid({
	       url : context_path + '/audit/getUnList',
	       datatype : "json",
	       colNames : [ 'id','出库单编号','工程','出库时间','制单人','领料人'],
	       colModel : [
						{name : 'id',index : 'id',width : 20,hidden:true}, 
	                    {name : 'document_no',index : 'document_no',width : 40},
	                    {name : 'projectname',index : 'projectname',width : 50},
	                    {name : 'out_time',index : 'out_time',width : 60},
	                    {name : 'makername',index : 'makername',width : 30},
	                    {name : 'receiptorname',index : 'receiptorname',width : 30},
	                    ],
	       rowNum : 20,
	       rowList : [ 10, 20, 30 ],
	       pager : '#out-grid-pager',
	       sortname : 'out_time',
	       sortorder : "desc",
	       altRows: true,
	       viewrecords : true,
	       autowidth:true,
	       loadComplete : function(data) 
	       {
	           var table = this;
	           setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
	           oriData = data;
	       },
	       emptyrecords: "没有相关记录",
	       loadtext: "加载中...",
	       pgtext : "页码 {0} / {1}页",
	       recordtext: "显示 {0} - {1} / 共{2}条数据"
	  });
	//在分页工具栏中添加按钮
	  jQuery("#out-grid-table").navGrid('#out-grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
	    .navButtonAdd('#out-grid-pager',{  
		   caption:"",   
		   buttonicon:"ace-icon fa fa-refresh green",   
		   onClickButton: function(){   
			   $("#out-grid-table").jqGrid('setGridParam', 
						{
							postData: {queryJsonString:""} //发送数据 
						}
				).trigger("reloadGrid");
		   }
		});
	
	  $(window).on('resize.jqGrid', function () {
		  $("#out-grid-table").jqGrid( 'setGridWidth', $("#out-grid-div").width() -5);
		  $("#out-grid-table").jqGrid( 'setGridHeight', (document.documentElement.clientHeight*0.48 - $("#in-grid-pager").height() - 130) );
	  });
	
	  
	  //--------------------库存报警-------------------------
	  jQuery("#grid-table").jqGrid({
	       url : context_path + '/alarm/getList',
	       datatype : "json",
	       colNames : [ 'id','物料编号', '物料名称','单位','报警值','库存'],
	       colModel : [
						{name : 'id',index : 'id',width : 55,hidden:true}, 
	                    {name : 'material_no',index : 'material_no',width : 40},
	                    {name : 'material_name',index : 'material_name',width : 80},
	                    {name : 'material_unit',index : 'material_unit',width : 20},
	                    {name : 'alarm_value',index : 'alarm_value',width : 30,cellattr: addCellAttr},
	                    {name : 'invamount',index : 'invamount',width : 30,cellattr: addCellAttr}
	                  ],
	       rowNum : 20,
	       rowList : [ 10, 20, 30 ],
	       pager : '#grid-pager',
	       sortname : 'id',
	       sortorder : "desc",
	       altRows: true,
	       viewrecords : true,
	       autowidth:true,
	       multiselect:true,
	       loadComplete : function(data) 
	       {
	           var table = this;
	           setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
	           oriData = data;
	       },
	       emptyrecords: "没有相关记录",
	       loadtext: "加载中...",
	       pgtext : "页码 {0} / {1}页",
	       recordtext: "显示 {0} - {1} / 共{2}条数据"
	  });
	  
	  $(window).on('resize.jqGrid', function () {
		  $("#grid-table").jqGrid( 'setGridWidth', $("#grid-div").width() -5);
		  $("#grid-table").jqGrid( 'setGridHeight', (document.documentElement.clientHeight*0.48 - $("#in-grid-pager").height() - 130) );
	  });
	  
	  //在分页工具栏中添加按钮
	  jQuery("#grid-table").navGrid('#grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
	    .navButtonAdd('#grid-pager',{  
		   caption:"",   
		   buttonicon:"ace-icon fa fa-refresh green",   
		   onClickButton: function(){   
			   $("#grid-table").jqGrid('setGridParam', 
						{
							postData: {queryJsonString:""} //发送数据 
						}
				).trigger("reloadGrid");
		   }
		});
	  
	  $(window).triggerHandler('resize.jqGrid');
	  
});

/*改变文字颜色*/
function addCellAttr(rowId, val, rawObject, cm, rdata){
	return "style='color:red'";
}

function enterDetail(type){
	if(type == 1){
		//入库
		if(1==1){
			window.location.href=context_path+"/instorageAudit/toInstorageAuditListPage";
		}else{
			layer.alert("没有权限");
		}
	}else if(type == 2){
		//出库
		if(1==1){
			window.location.href=context_path+"/audit/forwardAudit";
		}else{
			layer.alert("没有权限");
		}
	}else if(type == 3){
		//库存报警
		if(1==1){
			window.location.href=context_path+"/alarm/forwordAlarm";
		}else{
			layer.alert("没有权限");
		}
	}else if(type == 4){
		//库存报警
		if(1==1){
			window.location.href=context_path+"/tranSummary/tansList";
		}else{
			layer.alert("没有权限");
		}
	}
}

window.onunload = checkLeave();
function checkLeave(){
		// 图表清空-------------------
		//myChart.clear();

		// 图表释放-------------------
		//myChart.dispose();
}
</script>
