<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div  id ="role_add_page" class="row-fluid" style="height:inherit;">
	<form id="roleForm" class="form-horizontal" onkeydown="if(event.keyCode==13)return false;" style=" height: calc(100% - 70px);">
		<input type="hidden" name="roleId" id="roleId" value="" >
		<!-- 角色基础信息 -->
		<div  class="row" style="margin:0;padding:0;">
			<div  class="row" style="margin:0;padding:0;">
				<span class="span12" style="padding:10px;border-bottom:solid #009f95 1px;font-size:14px;">
					<i class="fa fa-users" aria-hidden="true"></i>&nbsp;角色信息
				</span>
			</div>
			<div  class="row" style="margin:0;padding:0;">
				<div class="control-group">
					<label class="control-label" for="roleName">角色名称：</label>
					<div class="controls">
						<div class="input-append span12 required" > 
					      <input class="span11" type="text"  name="roleName" id="roleName" placeholder="角色名称">
					    </div> 
					</div>
				</div>
				<div class="control-group " style="display: none">
					<label class="control-label" for="roleName">角色权限：</label>
					<div class="controls">
						<div class="span3">
							<input type="checkbox" name="addQx" value="1" />
							<span>增加权限</span>
						</div>
						<div class="span3">
							<input type="checkbox" name="editQx" value="1" />
							<span>修改权限</span>
						</div>
						<div class="span3">
							<input type="checkbox" name="delQx" value="1" />
							<span>删除权限</span>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="remark">备注：</label>
					<div class="controls">
						<div class="input-append span12" > 
					      <textarea class="span11" rows="4" cols="20"  name="remark" id="remark" placeholder="备注"></textarea>
					    </div> 
					</div>
				</div>
			</div>
		</div>
		<!-- 菜单树 -->
		<div  class="row" style="margin:0;padding:0;height:200px;">
			<div  class="row" style="margin:0;padding:0;">
				<span class="span12" style="padding:10px;border-bottom:solid #009f95 1px;font-size:14px;">
					<i class="fa fa-flag" aria-hidden="true"></i>&nbsp;权限菜单
				</span>
			</div>
			<div  class="row" style="margin:0;padding:0; overflow: auto; height:160px;">
				<ul id="menuTree" class="ztree" ></ul>
			</div>
		</div>
	</form>
	<!-- 底部按钮 -->
	<div class="form-actions" style="text-align: right;border-top: 0px;margin: 0px;">
		<span  class="btn btn-success">保存</span>
		<span  class="btn btn-danger">取消</span>
	</div>
</div>
<script type="text/javascript">
(function(){
	    $("#roleForm  input[type=checkbox]").uniform();
	    var selectid = getGridCheckedId("#grid-table","roleId");
	    if(openwindowtype==1){
	    	//更新
	    	$("#roleId").val(selectid);
	    	setting.async.otherParam ={"rid":selectid,"sysid":$("#index_sysid").val()};
	    }else{
	    	//新增
	    	setting.async.otherParam ={"rid":null,"sysid":$("#index_sysid").val()};
	    }
   		//初始化树
   		$.fn.zTree.init($("#menuTree"), setting);
   		zTree = $.fn.zTree.getZTreeObj("menuTree");
   		$.ajax({
   			url:context_path+"/role/getRoleById",
   			type:"POST",
   			data:{rid : $("#roleId").val()},
   			dataType:"JSON",
   			success:function(data){
   				if(data){
   					$("#roleForm #roleName").val(data.roleName);
   					$("#roleForm #remark").val(data.remark);
   					if(data.addQx==1){
   						//新增权限
   						$("input[type=checkbox][name=addQx]").trigger("click");
   					}
   					if(data.editQx==1){
   						//修改权限
   						$("input[type=checkbox][name=editQx]").trigger("click");
   					}
   					if(data.delQx==1){
   						//删除权限
   						$("input[type=checkbox][name=delQx]").trigger("click");
   					}
   					if(data.chaQx==1){
   						//查询权限
   						$("input[type=checkbox][name=chaQx]").trigger("click");
   					}
   					
   				}
   			}
   		});
		$('#roleForm').validate({
	  		rules:{
	  			"roleName":{
	  				required:true,
	  				maxlength:32,
	  				remote:context_path+"/role/hasR.do?_id="+$("#roleId").val()
	  			},
	  			"remark":{
	  				maxlength:256
	  			}
	  		},
	  		messages:{
	  			"roleName":{
	  				required:"请输入角色名称",
	  				remote:"您输入的角色名已经存在，请重新输入！"
	  			}
	  		},
	  		errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
			}
	  	 });

	var ajaxStatus = 1;     //ajax请求状态：0不能请求，1可以请求
	//确定按钮点击事件
    $("#role_add_page .btn-success").off("click").on("click", function(){
		//角色保存
		if($('#roleForm').valid()){
			//获取选中的菜单
	    	var nodes = zTree.getCheckedNodes(true);
			var menuids = "";
			for(var rolei=0,count = nodes.length;rolei<count; rolei++){
				var menuNode = nodes[rolei];
				if(menuNode.id>0){
					if(menuids.length>0){
						menuids += ","+menuNode.id;
					}else{
						menuids += menuNode.id;
					}
				}
			}
			var roleParam = $("#roleForm").serialize();
			
			if(ajaxStatus==0){
  				layer.msg("操作进行中，请稍后...",{icon:2});
  				return;
  			}
  			ajaxStatus = 0;    //将标记设置为不可请求
			$.ajax({
				url:context_path+"/role/addRole?tm="+new Date(),
				type:"POST",
				data:roleParam+"&menuId="+menuids,
				dataType:"JSON",
				success:function(data){
					ajaxStatus = 1; //将标记设置为可请求
					if(data){
						//刷新表格
						$("#grid-table").jqGrid('setGridParam', 
								{
							postData: {queryJsonString:""} //发送数据 
								}
						).trigger("reloadGrid");
						layer.msg("操作成功！",{icon:1});
						layer.close($queryWindow);
					}else{
						layer.alert("操作失败！",{icon:2});
					}
				}
			});
		}
		return false;
	});
	
	//取消按钮点击事件
	$("#role_add_page .btn-danger").off("click").on("click", function(){
	    layer.close($queryWindow);
	    return false;
	});
}());
</script>
</html>
