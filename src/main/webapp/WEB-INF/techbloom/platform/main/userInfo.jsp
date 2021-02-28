<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
<form id="userInfo_form" action="#" method="get" class="form-horizontal">
  <div class="control-group">
    <label class="control-label">姓名:</label>
    <div class="controls">
    <div class="input-append span12 required">
      <input class="span11" type="text" placeholder="姓名" name="user_name" id="user_name">
     </div>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">手机:</label>
    <div class="controls">
      <input class="span11" type="text" placeholder="手机" name="user_phone" id="user_phone">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label">邮箱:</label>
    <div class="controls">
      <input class="span11" type="text" placeholder="邮箱" name="user_email" id="user_email">
    </div>
  </div>
  <div class="control-group">
		<label class="control-label" for="user_remark">备注：</label>
		<div class="controls">
			<textarea class="span11" name="user_remark" id="user_remark" rows="3" cols="20"></textarea>
		</div>
  </div>
  <div class="form-actions" style="text-align: right;border-top: 0px;">
    <span  class="btn btn-success">保存</span>
    <span  class="btn btn-danger">取消</span>
  </div>
</form>

</div>
<script>
(function(){
     $("#userInfo_form input[type=radio]").uniform();
     $("#userInfo_form").validate({
		rules : {
			user_name : {
				required : true
			},
			user_phone : {
				mobile : true
			},
			user_email : {
				email : true
			}
		},
		errorClass: "help-inline",
		errorElement: "span",
		errorPlacement : function(label, elm){
			if (elm[0].type === 'radio') {
			    elm.parents(".controls").append(label);
			}else{
			   label.insertAfter(elm);
			}
		},
		highlight:function(element, errorClass, validClass) {
			$(element).parents('.control-group').addClass('error');
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).parents('.control-group').removeClass('error');
		}
	});
     
	$.ajax({
		url:context_path + "/user/getSessionUser",
		type:"POST",
		dataType:"JSON",
		success:function(data){
			if(data){
				$("#user_name").val(data.name);
				$("#user_email").val(data.email);
				$("#user_phone").val(data.phone);
				$("#user_remark").val(data.remark);
			}
		}
	});
	
	$("#userInfo_form .btn-success").off("click").on("click", function(){
	  if($("#userInfo_form").valid()){
		  $.ajax({
				url:context_path + "/user/updateUserInfo",
				type:"POST",
				data:{name:$("#user_name").val(),email:$("#user_email").val(),
					phone:$("#user_phone").val(),remark:$("#user_remark").val()},
				dataType:"JSON",
				success:function(data){
					if(data){
						layer.closeAll();
						layer.msg("用户信息更新成功！");
					}else{
						layer.alert("用户信息更新失败！",{icon:2});
					}
				}
			});
	  }
	});
	$("#userInfo_form .btn-danger").off("click").on("click", function(){
	  layer.closeAll();
	});
}());
</script>