var oriData;
var _grid;
var $queryWindow;  //查询窗口对象
var dynamicDefalutValue="60cce761390a4e51bc4636271b52ab94";
_grid = jQuery("#grid-table").jqGrid({
				url : context_path + '/user/list.do',
			    datatype : "json",
			    colNames : [ '用户名', '姓名','所属角色','状态','邮箱','手机号码', '最近登录时间','备注' ],
			    colModel : [ 
			                 {name : 'username',index : 'username',width : 60},
			                 {name : 'name',index : 'name',width : 50},
			                 {name : 'rolename',index : 'rolename',width : 60},
			                 {name : 'status',index : 'status',width : 40,
			                	 formatter:function(cellValu){
			                		 return cellValu==1?"启用":"禁用";
			                 	 } 
			                 }, 
			                 {name : 'email',index : 'email',width : 70},
			                 {name : 'phone',index : 'phone',width : 60},
			                 {name : 'lastLogin',index : 'lastLogin',width : 80,
			                 	formatter:function(cellValu){
									if (cellValu != null) {
										return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
									} else {
										return "";
									}
			                 	}
			                 },
			                 {name : 'remark',index : 'REMARK',width : 90}
			               ],
			    rowNum : 20,
			    rowList : [ 10, 20, 30 ],
			    pager : '#grid-pager',
			    sortname : 'USER_ID',
			    sortorder : "desc",
	            altRows: true,
	            viewrecords : true,
	            hidegrid:false,
	            multiselect:true,
	            multiboxonly: true,
                beforeRequest:function (){
                    dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
                    //重新加载列属性
                },
                loadComplete : function(data){
                                var table = this;
                                setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
                                oriData = data;
                            },
                            emptyrecords: "没有相关记录",
                            loadtext: "加载中...",
                            pgtext : "页码 {0} / {1}页",
                            recordtext: "显示 {0} - {1}共{2}条数据"
            });

jQuery("#grid-table").navGrid('#grid-pager',{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager',{  
	caption:"",   
	buttonicon:"fa fa-refresh green",   
	onClickButton: function(){   
		$("#grid-table").jqGrid('setGridParam', 
				{
			postData: {queryJsonString:""} //发送数据 
				}
		).trigger("reloadGrid");
	}
}).navButtonAdd('#grid-pager',{
	  caption: "",
	  buttonicon:"fa  icon-cogs",   
      onClickButton : function (){
    	  jQuery("#grid-table").jqGrid('columnChooser',{
    		  done: function(perm, cols){
                  dynamicColumns(cols,dynamicDefalutValue);
    			  $("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width() -7);
    		  }
    	  });
      }
});
$(window).on('resize.jqGrid', function () {
	$("#grid-table").jqGrid( 'setGridWidth', $(window).width()-$("#sidebar").width() -7);
    $("#grid-table").jqGrid( 'setGridHeight', $(".container-fluid").height()-$(".query_box").outerHeight(true)-$("#table_toolbar").outerHeight(true)- $("#grid-pager").outerHeight(true)-$("#gview_grid-table .ui-jqgrid-hbox").outerHeight(true));
});

$(window).triggerHandler('resize.jqGrid');


/**
 * 重新加载表格
 */
function gridReload()
{
    //重新加载表格
	_grid.trigger("reloadGrid");
}


/**
 * 查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryLogListByParam(jsonParam){
    //将json对象转换成json字符串
	var queryJsonString = JSON.stringify(jsonParam);
	//执行查询操作
	$("#grid-table").jqGrid('setGridParam', 
			{
				//发送数据
				postData: {queryJsonString:queryJsonString}
			}
	).trigger("reloadGrid");
}

/**
 * 打开添加页面
 */
function openAddPage(){
	layer.load(2);
	$.post(context_path+'/user/user_add', {}, function(str){
		$queryWindow = layer.open({
			    title : "用户添加", 
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
			layer.closeAll();
    		layer.msg('加载失败！',{icon:2});
		});
}

/**
 * 打开编辑页面
 */
function openEditPage(){
	var selectAmount = getGridCheckedNum("#grid-table");
	if(selectAmount==0){
		layer.msg("请选择一条记录！",{icon:2});
		return;
	}else if(selectAmount>1){
		layer.msg("只能选择一条记录！",{icon:8});
		return;
	}
	layer.load(2);
	$.post(context_path+'/user/user_edit', {}, function(str){
		$queryWindow = layer.open({
			    title : "用户编辑", 
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
			layer.closeAll();
    		layer.msg('加载失败！',{icon:2});
		});
}

/**
 * 根据用户主键删除用户
 */
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
			var ids = getGridCheckedId("#grid-table","userId");
			$.ajax({
				url:context_path+"/user/delUById",
				type:"POST",
				data:{ids : ids},
				dataType:"json",
				success:function(data){
					if(data){
						layer.msg("操作成功!",{icon:1});
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

/**
 * 禁用用户
 */
function disabledUser(){
	var selectAmount = getGridCheckedNum("#grid-table");
	if(selectAmount==0){
		layer.msg("请选择一条记录！",{icon:2});
		return;
	}
	var selectid = getGridCheckedId("#grid-table","userId");
	layer.confirm('确定禁用？', {
		  moveType: 1, //拖拽风格，0是默认，1是传统拖动
		  title:"操作提示",  /*弹出框标题*/
		  btn: ['确定', '取消']
		}, function(index, layero){
		    //确定按钮回调函数
			var ids = getGridCheckedId("#grid-table","userId");
			setUserState(ids,2,index);
		}, function(index){
		   //取消按钮回调函数
			layer.close(index);
		});
}

/**
 * 初始化部门下来框和角色下拉框的值
 */
function initDept(){
    $.ajax({
        url:context_path+"/user/getSomeInfo?tm="+new Date(),
        type:"POST",
        data:{ userId: $("#userId").val() },
        dataType:"JSON",
        async:false,
        success:function(data){
            if(data){
                if(data.role){//给角色赋值
                    var role = data.role;
                    $("#roleId").select2("data", {id: role.roleId, text: role.roleName});
                }
            }
        }
    });
}

/**
 * 启用用户
 */
function enabledUser(){
	var selectAmount = getGridCheckedNum("#grid-table");
	if(selectAmount==0){
		layer.msg("请选择一条记录！",{icon:2});
		return;
	}
	var selectid = getGridCheckedId("#grid-table","userId");
	layer.confirm('确定启用？', {
		  moveType: 1, //拖拽风格，0是默认，1是传统拖动
		  title:"操作提示",  /*弹出框标题*/
		  btn: ['确定', '取消']
		}, function(index, layero){
		  //确定按钮的回调
			var ids = getGridCheckedId("#grid-table","userId");
			setUserState(ids,1,index);
			layer.msg("aaa");
		}, function(index){
		  //取消的回调
			layer.close(index);
			return false;
		});
}

/**
 * 设置用户的启用状态：
 * @param windowIndex   弹出窗口的引用
 * @param ids           用户主键，逗号连接的字符串
 * @param state         1启用，2禁用
 */
function setUserState(ids,state,windowIndex){
	$.ajax({
		url:context_path+"/user/changeStat",
		type:"POST",
		data:{ids : ids,state : state},
		dataType:"json",
		success:function(data){
			if(data){
				layer.msg("操作成功!",{icon:1});
				//刷新用户列表
				$("#grid-table").jqGrid('setGridParam', 
					{
						postData: {queryJsonString:""} //发送数据 
					}
				).trigger("reloadGrid");
				if(windowIndex){
					layer.close(windowIndex);
				}else{
					layer.closeAll();
				}
			}else{
				layer.msg("操作失败!",{icon:2});
			}
		}
	});
}

/**
 * 部门导出
 */
function exportLogFile(){
    var selectid = getGridCheckedId("#grid-table","userId");
    $("#ids").val(selectid);
    $("#hiddenForm").submit();
}

/**
 * 查询按钮点击事件
 */
function openQueryPage(){

	var queryParam = iTsai.form.serialize($('#query_form'));
	queryLogListByParam(queryParam);
}

/**
 * 重置
 */
function reset(){
    $("#username").val("");
    $("#name").val("");
    $("#email").val("");
    $("#phone").val("");

    openQueryPage();
}