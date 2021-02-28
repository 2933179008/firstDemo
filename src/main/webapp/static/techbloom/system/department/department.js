var oriData; 
var _grid;
var openwindowtype = 0; //打开窗口类型：0新增，1修改
var dynamicDefalutValue="d0910f49571e454aafc9fd90e4121c9b";
var zTree;   //树形对象
var selectTreeId = 0;   //存放选中的树节点id
//树的相关设置
var setting = {
    view: {
        selectedMulti: false
    },
    check: {
        enable: true	
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    edit: {
        enable: false,
        drag:{
        	isCopy:false,
        	isMove:false
        }
    },
    callback: {
		onClick: zTreeOnClick,
		onAsyncSuccess: zTreeOnAsyncSuccess
    },
	async: {
		enable: true,
		url:context_path+"/dept/deptTree",
		autoParam:["id"],
		type: "POST"
	},//异步加载数据
};

/**
 * 获取树中选中节点的id
 * @returns {Number}
 */
function getSelectNodeId(){
	var nodes = zTree.getSelectedNodes();
	var deptid = 0;
	if(nodes.length>0){
		deptid = nodes[0].id;
	}
	return deptid;
}

//ztree加载成功之后的回调函数
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	zTree.expandAll(true);
	var datalist = JSON.parse(msg);
	//默认点击第一个
    if(datalist.length>0){
    	var clickNode = null;
    	clickNode = zTree.getNodeByParam("id", selectTreeId, null);
    	clickNode.click = zTreeOnClick(null,null,clickNode);
    	zTree.selectNode(clickNode);
    }
};

//树节点click事件
function zTreeOnClick(event, treeId, treeNode) {
	//节点点击事件 alert(treeNode.tId + ", " + treeNode.name);
	//后台获取相应工程的地图数据
	var selectnodes = zTree.getSelectedNodes();  //选中的节点
	/* var checknodes = zTree.getCheckedNodes();    //复选框选中的节点
	//console.dir(checknodes);
	for ( var int = 0,size = checknodes.length; int < size; int++) {
		console.log(checknodes[int].id);
		
	} */
	selectTreeId = treeNode.id;   //记录每次点击的树节点
	$("#grid-table").jqGrid('setGridParam', 
			{
				postData: {deptId:treeNode.id,queryJsonString:""} //发送数据 
			}
	).trigger("reloadGrid");
};


//初始化树
$.fn.zTree.init($("#treeDemo"), setting);
zTree = $.fn.zTree.getZTreeObj("treeDemo");


//表格加载
_grid = jQuery("#grid-table").jqGrid({
	url : context_path + '/dept/list.do',
	datatype : "json",
	styleUI: 'Bootstrap',
	colNames : [ '部门编号', '部门名称','部门描述'],
	colModel : [ 
	            {name : 'departmentNo',index : 'DEPARTMENT_NO',width : 60}, 
	            {name : 'departmentName',index : 'DEPARTMENT_NAME',width : 70}, 
	            {name : 'description',index : 'DESCRIPTION',width : 90} 
	            ],
	            rowNum : 20,
	            rowList : [ 10, 20, 30 ],
	            pager : '#grid-pager',
	            sortname : 'DEPARTMENT_NO',
	            sortorder : "desc",
	            altRows: true,
	            viewrecords : true,
	            hidegrid:false,
	            multiselect:true,
	            multiboxonly: true,
	            autowidth:true,
				beforeRequest:function (){
					dynamicGetColumns(dynamicDefalutValue,'grid-table', $(window).width()-$("#sidebar").width() -7);
					//重新加载列属性
				},
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
jQuery("#grid-table").navGrid('#grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
.navButtonAdd('#grid-pager',{  
	caption:"",   
	buttonicon:"fa fa-refresh green",   
	onClickButton: function(){
		$("#grid-table").jqGrid('setGridParam', 
				{
					postData: {deptId:getSelectNodeId(),queryJsonString:""} //发送数据 
				}
		).trigger("reloadGrid");
	}
})
.navButtonAdd('#grid-pager',{
	caption: "",
	buttonicon:"fa icon-cogs",
	onClickButton : function (){
		jQuery("#grid-table").jqGrid('columnChooser',{
			done: function(perm, cols){
				dynamicColumns(cols,dynamicDefalutValue);
				//cols页面获取隐藏的列,页面表格的值
				$("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width()-$("#leftdiv").width()-7);
			}
		});
	}
});
$(window).on('resize.jqGrid', function () {
	$("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width()-$("#leftdiv").width()-7 );
	$("#grid-table").jqGrid( 'setGridHeight', ($(window).height()-$("#table_toolbar").outerHeight(true)- $("#grid-pager").outerHeight(true)-$("#user-nav").height()-$("#breadcrumb").height()-$(".ui-jqgrid-labels").height()-35 ) );
});

$(window).triggerHandler('resize.jqGrid');

/*打开添加页面*/
function openAddPage(){
	openwindowtype = 0;
	layer.load(2);
	$.post(context_path+'/dept/dept_edit', {}, function(str){
		$queryWindow = layer.open({
			    title : "部门添加", 
		    	type: 1,
		    	skin : "layui-layer-molv",
		    	area : "600px",
		    	shade: 0.6, //遮罩透明度
	    	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
		    	content: str,//注意，如果str是object，那么需要字符拼接。
		    	success:function(layero, index){
		    		layer.closeAll('loading');
		    	}
			});
		}).error(function() {
			layer.msg('加载失败,请检查网络！',{icon:2});
			layer.closeAll('loading');
		});
}

/*打开编辑页面*/
function openEditPage(){
	var selectAmount = getGridCheckedNum("#grid-table");
	if(selectAmount==0){
		layer.msg("请选择一条记录！",{icon:2});
		return;
	}else if(selectAmount>1){
		layer.msg("只能选择一条记录！",{icon:8});
		return;
	}
	openwindowtype = 1;
	layer.load(2);
	$.post(context_path+'/dept/dept_edit', {}, function(str){
		$queryWindow = layer.open({
			    title : "部门编辑", 
		    	type: 1,
		    	skin : "layui-layer-molv",
		    	area : '600px',
		    	shade: 0.6, //遮罩透明度
	    	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
		    	content: str,//注意，如果str是object，那么需要字符拼接。
		    	success:function(layero, index){
		    		layer.closeAll('loading');
		    	}
			});
		}).error(function() {
			layer.msg('加载失败,请检查网络！',{icon:2});
			layer.closeAll('loading');
		});
}

//部门导出
function exportLogFile(){
    var selectid = getGridCheckedId("#grid-table","deptId");
    $("#ids").val(selectid);
    $("#hiddenForm").submit();
}

/*根据主键删除记录*/
function deleteUser(){
	var selectAmount = getGridCheckedNum("#grid-table");
	if(selectAmount==0){
		layer.msg("请选择一条记录！",{icon:2});
		return;
	}
	layer.confirm('确定删除？', /*显示的内容*/
		{
		  shift: 6,
		  moveType: 1, //拖拽风格，0是默认，1是传统拖动
		  title:"操作提示",  /*弹出框标题*/
		  icon: 3,      /*消息内容前面添加图标*/
		  btn: ['确定', '取消']/*可以有多个按钮*/
		}, function(index, layero){
		   //确定按钮的回调
			//获取表格中选中的用户记录
			var ids = getGridCheckedId("#grid-table","deptId");
			$.ajax({
				url:context_path+"/dept/delDept",
				type:"POST",
				data:{deptIds : ids},
				dataType:"json",
				success:function(data){
					if(data._result){
						layer.msg("操作成功!",{icon:1});
						//刷新左侧树
						zTree.reAsyncChildNodes(null, "refresh");
						//刷新列表
						$("#grid-table").jqGrid('setGridParam', 
								{
									postData: {deptId:getSelectNodeId(),queryJsonString:""} //发送数据 
								}
						).trigger("reloadGrid");
						layer.close(index);
					}else{
						layer.alert(data.deptInfo,{icon:2});
						layer.close(index);
					}
				}
			});
			
		}, function(index){
		  //取消按钮的回调
		  layer.close(index);
		});
}

/**
 * 查询按钮点击事件
 */
function queryRow(){
	var queryParam = iTsai.form.serialize($('#query_form'));
	var queryJsonString = JSON.stringify(queryParam);
	$("#grid-table").jqGrid('setGridParam', 
			{
				postData: {deptId:"",queryJsonString: queryJsonString} //发送数据 
			}
	).trigger("reloadGrid");
}
