<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
<form id="pwd_form" action="#" method="get" class="form-horizontal">
  <div class="control-group">
    <label class="control-label">密码:</label>
    <div class="controls">
      <input class="span11" id="pwd" type="password" placeholder="密码" name="pwd">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">新密码:</label>
    <div class="controls">
      <input class="span11" type="password" placeholder="新密码" name="new_pwd" id="new_pwd">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">确认密码:</label>
    <div class="controls">
      <input  class="span11"  type="password" placeholder="确认密码" name="new_pwd_1">
    </div>
  </div>
  <div class="form-actions" style="text-align: right;border-top: 0px;">
    <span  class="btn btn-success">保存</span>
    <span  class="btn btn-danger">取消</span>
  </div>
</form></div>
<script>
(function(){
     $("#pwd_form").validate({
		rules:{
			pwd:{
				required:true
			},
			new_pwd:{
				required:true
			},
			new_pwd_1:{
				required:true,
				equalTo:"#new_pwd"
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
	$("#pwd_form .btn-success").off("click").on("click", function(){
	  if($("#pwd_form").valid()){
		//密码初始化
	    	layer.confirm('确定修改密码？<br> 注：密码修改成功后，系统会自动退出，请使用新密码重新登录。', /*显示的内容*/
   			{
   			  shift: 6,
   			  moveType: 1, //拖拽风格，0是默认，1是传统拖动
   			  title:"操作提示",  /*弹出框标题*/
   			  icon: 3,      /*消息内容前面添加图标*/
   			  btn: ['确定']/*可以有多个按钮*/
   			}, function(index, layero){
   				 $.ajax({
   					  url:context_path+"/user/updateUserPass",
   					  type:"POST",
   					  data:{oldPass : $("#pwd").val(),newPass : $("#new_pwd").val()},
   					  dataType:"JSON",
   					  success:function(data){
   						  if(data.result){
   							  layer.closeAll();
	   						  layer.msg('密码修改成功！3秒钟后退出重新登录...', {
	   							  icon: 1
	   						  }, function(){
	   							window.location.href = context_path+"/login/logout.do";
	   						  });
   						  }else{
   							  layer.alert(data.data,{icon:2});
   						  }
   					  }
   				  });
   			});
	  }
	});
	$("#pwd_form .btn-danger").off("click").on("click", function(){
	  layer.closeAll();
	});
}());
</script>