<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id ="user_add_page" class="row-fluid" style="height: inherit;">
	<form id="queryForm" class="form-horizontal"  style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="userId" id="userId" value="">
		<div class="control-group">
			<label class="control-label" for="username">用户名：</label>
			<div class="controls">
			<div class="input-append span12 required" > 
			      <input class="span11" type="text"  name="username" id="username" placeholder="用户名">
			    </div> 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="password">密码：</label>
			<div class="controls">
				<div class="input-append span12 required">
			      <input class="span11" type="password" name="password" id="password"placeholder="密码">

			    </div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="pwd_confirm">确认密码：</label>
			<div class="controls">
				<div class="input-append span12 required">
			      <input class="span11" type="password" name="pwd_confirm" id="pwd_confirm"placeholder="确认密码">

			    </div>
			</div>
		</div>
		
		<div class="control-group">
		    <label class="control-label">性别：</label>
		    <div class="controls">
		     <label><input type="radio" id="gender1" name="gender" value="1">男</label>
		     <label><input type="radio" id="gender2" name="gender" value="0">女</label>
		    </div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="name">真实姓名：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input class="span11" type="text" name="name" id="name"
						value="" placeholder="真实姓名" />
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="roleId">所属角色：</label>
			<div class="controls">
					<input class="span11 select2_input" type="text" name="roleId" id="roleId" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="phone">手机号码：</label>
			<div class="controls">
				<input class="span11" type="text" name="phone" id="phone"
					value="" placeholder="手机号码" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="email">Email：</label>
			<div class="controls">
				<input class="span11" type="text" name="email" id="email"
					value="" placeholder="Email" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="remark">备注：</label>
			<div class="controls">
				<textarea class="span11" name="remark" id="remark" rows="3" cols="20"></textarea>
			</div>
		</div> 
		
	</form>
	<div class="form-actions" style="text-align: right;border-top: 0px;margin: 0px;">
	 <span  class="savebtn btn btn-success">保存</span>
     <span  class="btn btn-danger">取消</span>
	</div>
</div>

<script type="text/javascript">
(function(){
   	$("#queryForm input[type=radio]").uniform();
   	$("input[type=radio][name=gender][value=1]").attr("checked",true).trigger("click");
	
    //所属角色
    $("#roleId").select2({
    	placeholder: "选择角色",
		minimumInputLength:0,   //至少输入n个字符，才去加载数据
	    allowClear: true,  //是否允许用户清除文本信息
		delay: 250,
		formatNoMatches:"没有结果",
		formatSearching:"搜索中...",
		formatAjaxError:"加载出错啦！",
		ajax : {
			url: context_path+"/role/getRoleList",
			type:"POST",
			dataType : 'json',
			delay : 250,
			data: function (term,pageNo) {     //在查询时向服务器端传输的数据
	            term = $.trim(term);
                return {
                	queryString: term,    //联动查询的字符
                	pageSize: 15,    //一次性加载的数据条数
                    pageNo:pageNo,    //页码
                    time:new Date()   //测试
                 }
	        },
	        results: function (data,pageNo) {
	        		var res = data.result;
   	            if(res.length>0){   //如果没有查询到数据，将会返回空串
   	               var more = (pageNo*15)<data.total; //用来判断是否还有更多数据可以加载
   	               return {
   	                    results:res,more:more
   	                 };
   	            }else{
   	            	return {
   	            		results:{}
   	            	};
   	            }
	        },
			cache : true
		}

    });
    
    $("#roleId").on("change.select2",function(){
        $("#roleId").trigger("keyup")}
    );
    $("#deptId").on("change.select2",function(){
        $("#deptId").trigger("keyup")}
    );
	
	//表单校验
	$("#queryForm").validate({
  		rules:{
  			"username": {
  				required:true,
  				maxlength:32,
  				remote:context_path+"/user/hasU.do?userId="+$('#userId').val()
  			},
  			"password":{
  				required:true,
  				maxlength:32
  			},
  			"pwd_confirm":{
  				required:true,
  				equalTo:"#password"
  			},
  			"name":{
  				required:true,
  				invalidChar:"'\"\\\\",
  				maxlength:32
  			},
  			"phone":{
                digits:true,
				maxlength:11
    		}, 
  			"email":{
  				email : true,
  				maxlength:256
  			},
  			"roleId":{
  				required:true
  			},
  			"remark":{
  				maxlength:256
  			},
  			"deptId":{
  				required:true
  			} 
  		},
  		messages:{
  			"username" : {
  				required:"请输入用户名",
  				remote:"您输入的用户名已经存在，请重新输入！"
  			},
  			"password":{
  				required:"请输入密码"
  			},
  			"pwd_confirm":{
  				required:"请输入确认密码",
  				equalTo:"与第一次密码不一致"
  			},
  			"name":{
  				required:"请输入姓名"
  			},
  			"roleId":{
  				required:"请选择角色"
  			},
  			"deptId":{
  				required:"请选择部门"
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
	
	//确定按钮点击事件
    $("#user_add_page .btn-success").off("click").on("click", function(){
		//用户保存
	  if($("#queryForm").valid()){
			saveUserInfo($("#queryForm").serialize());
		}
	});
	
	//取消按钮点击事件
	$("#user_add_page .btn-danger").off("click").on("click", function(){
	    layer.close($queryWindow);
	});
	
	var ajaxStatus = 1;     //ajax请求状态：0不能请求，1可以请求
	//保存/修改用户信息
  	function saveUserInfo(bean){
  		if(bean){
  			if(ajaxStatus==0){
  				layer.msg("操作进行中，请稍后...",{icon:2});
  				return;
  			}
  			ajaxStatus = 0;    //将标记设置为不可请求
  			$(".savebtn").attr("disabled","disabled");
  			$.ajax({
  				url:context_path+"/user/save?tm="+new Date(),
  				type:"POST",
  				data:bean,
  				dataType:"JSON",
  				success:function(data){
  					ajaxStatus = 1; //将标记设置为可请求
  					$(".savebtn").removeAttr("disabled");
  					if(data.result){
  						layer.msg("保存成功！",{icon:1});
  						//刷新用户列表
  						$("#grid-table").jqGrid('setGridParam', 
							{
								postData: {queryJsonString:""} //发送数据 
							}
						).trigger("reloadGrid");
  						//关闭当前窗口
  						//关闭指定的窗口对象
  						layer.close($queryWindow);
  					}else{
  						layer.msg("保存失败，请稍后重试！",{icon:2});
  					}
  				}
  			});
  		}else{
  			layer.msg("出错啦！",{icon:2});
  		}
  	}
}())

</script>