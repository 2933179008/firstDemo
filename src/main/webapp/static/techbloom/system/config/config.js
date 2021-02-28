var selectTreeId = -1;
var zTree;
var param = {
   "menuTree" :{
	   
	   setting :  {
	        view: {
	            dblClickExpand: true,
	            expandSpeed: ""
	        },		
		    async: {
		        enable: true,// 采用异步加载
		        url : context_path + "/menu/menuLists",
		        dataType : "json",
		        autoParam:["id"],
				otherParam:{"parentId":function (){
		            return selectTreeId;}},
		        type: "POST"
		    },
		    data : {
		    	keep: {
					parent: true
				},
		        key : {
		            title : "menuName",    
		            name : "menuName"
		        },
		        simpleData : {
		            enable : true,
		            idKey : "menuId",
		            pIdKey : "parentId",
		            rootPid : 000 
		        }
		    },
		    callback : {
		        onAsyncSuccess: zTreeOnAsyncSuccess, // 异步加载完成调用
		        onClick:zTreeOnClick
		    }
	}
   },
   
   "grid-table" :{
	    addData : true,	    
//	    addDataUrl : context_path+"/config/saveMenu",    
	    editData : true,
//	    editDataUrl : context_path+"/config/saveMenu", 
//	    delDataurl : context_path + "/config/deleteMenu",
//	    delParam:{"menuIds":"menuId"},
//	    deleteData : true,
	    multiselect:true,
	    multiboxonly: true,
	    configGroup:true,
	    formdata:[{
	    	id : "menuName",
	    	name : "菜单名称",
	    	type : "textinput",
	    	group : "菜单信息"
	    }, {
	    	id : "menuUrl",
	    	name : "菜单URL",
	    	type : "textinput",
	    	group : "菜单信息"
	    }, {
	    	id : "menuOrder",
	    	name : "菜单排序",
	    	type : "textinput",
	    	group : "菜单信息"
	    },{
	    	id : "menuIcon",
	    	name : "菜单图片",
	    	type : "textinput",
	    	group : "菜单信息"
	    }, {
	    	id : "parentId",
	    	name : "父菜单",
	    	opts:{
	    		url : context_path + "/menu/menuLists",
	    		id :"menuId",
	    		pId : "parentId",
	    		text: "menuName"
	    	},
	    	type : "selectTree",
	    	group : "菜单信息"
	    },{
	    	id : "vip",
	    	name : "VIP",
	    	type : "radio",
	    	values:[{value: 1, name : "VIP", id:"vip"},{value: 0, name : "普通", id:"vip"}],
	    	group : "菜单信息"
	    },  {
	    	id : "remark",
	    	name : "备 注",
	    	type : "textarea",
	    	group : "菜单信息"
	    }, 
	    ],
	    selectRowData:[{id:"menuId"}],
		url:context_path + "/menu/menuList",
	    datatype : "json",
	    colNames:['主键','菜单名称1', '菜单目录1','菜单图标1',"菜单排序","备注1","VIP"],
	    colModel:[
	    	{name:'menuId', index :'menuId',width:50,hidden:true},
	        {name:'menuName',index:'menuName', width:'20%',align:'left',searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	        {name:'menuUrl',index:'menuUrl', width:'35%',align:'left',searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	        {name:'menuIcon',index:'menuIcon', width:'20%', align:"left", sortable:false, searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	        {name:'menuOrder',index:'menuOrder', width:'0%', align:"left", sortable:false, searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	        {name:'remark',index:'remark', width:'20%', align:"left", sortable:false, searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	        {name:'vip',index:'vip', width:'0%', align:"left", sortable:false, searchoptions:{sopt:['eq','ne','le','lt','gt','ge']}},
	    ],  
	    onSelectRow : function(id){
	    	var ss  = oriData['rows'][id-1]["menuId"];
	    	ss+=ss+",";
	    }
//	    onSelectRow : function(id) {
//	    	var selectid  = oriData['rows'][id-1]["menuId"];
//   	  	    var  _this = this;
//   	    	$.ajax({
//   	    		url:context_path+"/config/getMenuById",
//   	   			type:"POST",
//   	   		    data:{menuId:selectid},
//   			    dataType:"JSON",
//   	   			success:function(data){
//   	   			openwindowtype=0;
//   	   			layer.load(2);
//   	   			$.post(context_sys_path+'/system/systemconfig/menu_add.jsp', {}, function(str){
//   	   			$queryWindow = layer.open({
//   	   				    title : "菜单修改", 
//   	   			    	type: 1,
//   	   			    	skin : "layui-layer-molv",
//   	   			    	area : "600px",
//   	   			    	shade: 0.6, //遮罩透明度
//   	   		    	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
//   	   			    	content: str,//注意，如果str是object，那么需要字符拼接。
//   	   			    	success:function(layero, index){
//   	   			    		layer.closeAll('loading');
//   	   			    	}
//   	   				});
//   	   			}).error(function() {
//   	   				layer.closeAll();
//   	   	    		layer.msg('加载失败！',{icon:2});
//   	   			});
//   	   			}
//   	   		});
//	    }
  }
};
var selectid="";
//异步加载完成时运行，此方法将所有的节点打开
function zTreeOnClick(event, treeId, treeNode) {
	/* $('#parentName').val(treeNode.menuName);
	 $('#parentId').val(treeNode.menuId);	 
	 
	 currentMenuId = treeNode.menuId;
	 refreshWindow();*/
}

//异步加载完成时运行，此方法将所有的节点打开
function zTreeOnAsyncSuccess(event, treeId, msg) {
  var treeObj = $.fn.zTree.getZTreeObj("menuTree");
  treeObj.expandAll(true);
}
Tui($("#grid-div"), param);


/**
 * 查询按钮点击事件
 */
function openQueryPage(){

	var queryParam = iTsai.form.serialize($('#query_form'));
	
	queryLogListByParam(queryParam);
}

/**
 * 查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryLogListByParam(jsonParam){
	var queryJsonString = JSON.stringify(jsonParam);         //将json对象转换成json字符串
	//执行查询操作
	$("#grid-table").jqGrid('setGridParam', 
			{
		postData: {queryJsonString:queryJsonString} //发送数据 
			}
	).trigger("reloadGrid");

}

/*打开添加页面*/
function openAddPage(){
	layer.load(2);
	openwindowtype = 0;
	$.post(context_sys_path+'/system/systemconfig/menu_add.jsp', {}, function(str){
		$queryWindow = layer.open({
			    title : "菜单添加", 
		    	type: 1,
		    	skin : "layui-layer-molv",
		    	area : "600px",
		    	shade: 0.6, //遮罩透明度
	    	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
		    	content: str,//注意，如果str是object，那么需要字符拼接。
		    	success:function(layero, index){
		    		layer.closeAll('loading');
//		    		refreshWindow();
		    	}
			});
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
	var selectid = getGridCheckedId("#grid-table","menuId");
	layer.load(2);
	$.post(context_sys_path+'/system/systemconfig/menu_add.jsp', {}, function(str){
		$queryWindow = layer.open({
			    title : "菜单编辑", 
		    	type: 1,
		    	skin : "layui-layer-molv",
		    	area : "600px",
		    	shade: 0.6, //遮罩透明度
	    	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
		    	content: str,//注意，如果str是object，那么需要字符拼接。
		    	success:function(layero, index){
		    		layer.closeAll('loading');
		    		refreshWindow();
		    	}
			});
		});
}
//重新加载表格
function gridReload()
{
	_grid.trigger("reloadGrid");  //重新加载表格  
}


/**
 * 根据主键删除表格记录
 */
function deleteMenu(){
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
			//var ids = $('#grid-table').jqGrid('getGridParam','selarrow');
			var ids = getGridCheckedId("#grid-table","menuId");
			//var ids = getGridCheckedId("#grid-table","noticeId");
			$.ajax({
				url:context_path+"/config/deleteMenu",
				type:"POST",
				data:{menuIds : ids},
				dataType:"json",
				success:function(data){
					if(data){
						layer.msg("操作成功!",{icon:1});
						refreshWindow();
						//刷新用户列表
  						$("#grid-table").jqGrid('setGridParam', 
							{
								postData: {queryJsonString:""} //发送数据 
							}
						).trigger("reloadGrid");
						layer.close(index);
					}else{
						layer.msg("操作失败!",{icon:2});
					}
				}
			});
			
		}, function(index){
		  //取消按钮的回调
		  layer.close(index);
		});
}
function refreshWindow(){
	//刷新左侧树
	//zTree.reAsyncChildNodes(null, "refresh");
	zTree = $.fn.zTree.getZTreeObj("menuTree");
	var node = zTree.getSelectedNodes()[0];
    selectTreeId  =  node.menuId;
    zTree.reAsyncChildNodes(node, "refresh",false);
	//刷新列表
	$("#grid-table").jqGrid('setGridParam', 
			{
				postData: {queryJsonString:""} //发送数据 
			}
	).trigger("reloadGrid");
}

