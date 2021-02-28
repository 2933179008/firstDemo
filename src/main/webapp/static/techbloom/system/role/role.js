var oriData; 
var _grid;
var $queryWindow;  //查询窗口对象

var dynamicDefalutValue="eb2f71f889ef4c8e885dda8c5b292b64";

var openwindowtype = 0; //打开窗口类型：0新增，1修改

var zTree;   //树形对象
var selectTreeId = 0;   //存放选中的树节点id
//树的相关设置
var setting = {
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
		onAsyncSuccess: zTreeOnAsyncSuccess
    },
	async: {
		enable: true,
		url:context_path+"/role/getMenulistByRoleId",
		autoParam:["id"],
		otherParam: {"rid":null},
		type: "POST"
	},//异步加载数据
};

//ztree加载成功之后的回调函数
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	zTree.expandAll(true);
};


_grid = jQuery("#grid-table").jqGrid({
				url : context_path + '/role/roleList.do',
			    datatype : "json",
			    colNames : [ '角色名称', /*'新增权限', '编辑权限','删除权限', '查询权限',*/'备注' ],
			    colModel : [ 
                 {name : 'roleName',index : 'ROLE_NAME',width : 85}, 
                 // {name : 'addQx',index : 'ADD_QX',width : 60,formatter:addQxF},
                 // {name : 'editQx',index : 'EDIT_QX',width : 60,formatter:editQxF},
                 // {name : 'delQx',index : 'DEL_QX',width : 60,align : "left",formatter:delQxF},
                 // {name : 'chaQx',index : 'CHA_QX',width : 60,formatter:chaQxF,hidden:true},
                 {name : 'remark',index : 'REMARK',width : 100}
                ],
			    rowNum : 20,
			    rowList : [ 10, 20, 30 ],
			    pager : '#grid-pager',
			    sortname : 'ROLE_ID',
			    sortorder : "desc",
	            altRows: true,
	            viewrecords : true,
	            caption : "角色列表",
	            hidegrid:false,
	            multiselect:true,
	            multiboxonly: true,
    			beforeRequest:function (){
        			dynamicGetColumns(dynamicDefalutValue,'grid-table', $(window).width()-$("#sidebar").width() -7);
					//重新加载列属性
    			},
    			loadComplete : function(data)
	            {
	            	var table = this;
	            	setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
	            	oriData = data;
	            	
	            	/*//初始化开关控件
	            	$(".bootstrap-switch").bootstrapSwitch();*/
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
			postData: {queryJsonString:""} //发送数据 
				}
		).trigger("reloadGrid");
	}
})
.navButtonAdd('#grid-pager',{
	caption: "",
	buttonicon:"fa  icon-cogs",
	onClickButton : function (){
		jQuery("#grid-table").jqGrid('columnChooser',{
			done: function(perm, cols){
				dynamicColumns(cols,dynamicDefalutValue);
                //cols页面获取隐藏的列,页面表格的值
                $("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width() -7);
            }
        });
    }
});



$(window).on('resize.jqGrid', function () {
	$("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width() -7);
	$("#grid-table").jqGrid( 'setGridHeight', ($(window).height()-$("#table_toolbar").outerHeight(true)- $("#grid-pager").outerHeight(true)-$("#user-nav").height()-$("#breadcrumb").height()-$(".ui-jqgrid-labels").height()-135 ) );
});

$(window).triggerHandler('resize.jqGrid');


//重新加载表格
function gridReload()
{
	_grid.trigger("reloadGrid");  //重新加载表格  
}


/***
 * 更改权限
 * @param obj
 * @param roleId
 * @param qxName
 */
function changeQX(obj,roleId,qxName) {
	var value = obj.checked ? "1" : "0"; 
	$.ajax({
		type : "POST",
		url : context_path + '/role/changeQX.do?tm=' + new Date().getTime(),
		data : {
			"roleId" : roleId,
			"qxName" : qxName,
			"qxValue" : value
		},
		dataType : 'json',
		cache : false,
		success : function(data) 
		{
			if (!data) {
				Dialog.error("更新权限异常，请尝试重新操作或联系系统管理员！");
			}
		}
	});
}

/**
 * 生成新增权限样式
 * @returns {String}
 */
function addQxF(cellvalue, options, rowObject) {
	var _addQx = rowObject.addQx;
	var checkInfo = (_addQx == 1) ? " checked='checked' " : "";
	
	return "<input id=\"id-button-borders\" " + checkInfo + " onclick=\"changeQX(this,"
			+ rowObject.roleId + ",'add_qx'"
			+ ")\" type=\"checkbox\" class=\"ace ace-switch ace-switch-8\" /><span class=\"lbl middle\"></span>";
}

/**
 * 生成删除权限样式
 * @returns {String}
 */
function delQxF(cellvalue, options, rowObject) {
	var _delQx = rowObject.delQx;
	var checkInfo = (_delQx == 1) ? " checked='checked' " : "";
	return "<input id=\"id-button-borders\" " + checkInfo + " onclick=\"changeQX(this,"
			+ rowObject.roleId + ",'del_qx'"
			+ ")\" type=\"checkbox\" class=\"ace ace-switch ace-switch-8\" /><span class=\"lbl middle\"></span>";
}

/**
 * 生成编辑权限样式
 * @returns {String}
 */
function editQxF(cellvalue, options, rowObject) {
	var _editQx = rowObject.editQx;
	var checkInfo = (_editQx == 1) ? " checked='checked' " : "";
	return "<input id=\"id-button-borders\" " + checkInfo + " onclick=\"changeQX(this,"
	       + rowObject.roleId + ",'edit_qx'"
	       + ")\" type=\"checkbox\" class=\"ace ace-switch ace-switch-8\" /><span class=\"lbl middle\"></span>";
}

/**
 * 生成查询权限样式
 * @returns {String}
 */
function chaQxF(cellvalue, options, rowObject) {
	var _chaQx = rowObject.chaQx;
	var checkInfo = (_chaQx == 1) ? " checked='checked' " : "";
	return "<input disabled='disabled' id=\"id-button-borders\" type=\"checkbox\" " + checkInfo + " " +
			"onclick=\"changeQX(this,"+rowObject.roleId+",'cha_qx');\"" +
			"class=\"ace ace-switch ace-switch-8\" /><span class=\"lbl middle\"></span>";
}


/**
 * 查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryLogListByParam(jsonParam){
	//console.log(jsonParam);
	//序列化表单：iTsai.form.serialize($('#frm'))
	//反序列化表单：iTsai.form.deserialize($('#frm'),json)
	var queryJsonString = JSON.stringify(jsonParam);         //将json对象转换成json字符串
	//执行查询操作
	$("#grid-table").jqGrid('setGridParam', 
			{
		postData: {queryJsonString:queryJsonString} //发送数据 
			}
	).trigger("reloadGrid");
}

/**
 * 查询按钮点击事件
 */
function openQueryPage(){

	var queryParam = iTsai.form.serialize($('#query_form'));
	queryLogListByParam(queryParam);
}

/*打开添加页面*/
function openAddPage(){
	layer.load(2);
	openwindowtype = 0;
	$.post(context_path+'/role/role_edit', {}, function(str){
		$queryWindow = layer.open({
			    title : "角色添加", 
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
	var selectid = getGridCheckedId("#grid-table","roleId");
	layer.load(2);
	$.post(context_path+'/role/role_edit', {}, function(str){
		$queryWindow = layer.open({
			    title : "角色编辑", 
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
		});
}

/*根据主键删除表格记录*/
function deleteRow(){
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
			var ids = getGridCheckedId("#grid-table","roleId");
			$.ajax({
				url:context_path+"/role/delRole",
				type:"POST",
				data:{ids : ids},
				dataType:"json",
				success:function(data){
					if(data._result){
						layer.msg("操作成功!",{icon:1});
						//刷新列表
  						$("#grid-table").jqGrid('setGridParam', 
							{
								postData: {queryJsonString:""} //发送数据 
							}
						).trigger("reloadGrid");
						layer.close(index);
					}else{
						layer.msg("操作失败!其中 {"+data.roleInfo+"} 已经关联用户",{icon:2});
					}
				}
			});
			
		}, function(index){
		  //取消按钮的回调
		  layer.close(index);
		});
}