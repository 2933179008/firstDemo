<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
    <title>用户设置</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
    <link rel="stylesheet" href="<%=path%>/plugins/ace/assets/css/select2.css" />
    
    <link rel="stylesheet" href="<%=path%>/plugins/ace/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="<%=path%>/plugins/ace/assets/css/font-awesome.css" />
    <link rel="stylesheet" href="<%=path%>/plugins/ace/assets/css/jquery-ui.custom.css" />
    <style type="text/css">
	</style>
	<script type="text/javascript">
		var context_path = "<%=path%>";
		function result(res){
			if( Boolean(res) ){
				Dialog.success("编辑用户成功！",function(){
				   Dialog.openerWindow().gridReload();
		    	   ownerDialog.close();
				});
			}else{
				Dialog.error("编辑用户失败！");
			}
		}
	</script>
	<script type="text/javascript" src="<%=path%>/static/js/techbloom/system/user.js"></script>
  </head>
  <body class="no-skin" style="overflow-y: hidden;">
  	<div class="col-sm-offset-1 col-sm-10">
		<form class="form-horizontal">
			<div class="tabbable" style="margin-top:20px;">
				<ul class="nav nav-tabs padding-16">
					<li class="active">
						<a data-toggle="tab" href="#edit-basic">
							<i class="green ace-icon fa fa-pencil-square-o bigger-125"></i>
							基本信息
						</a>
					</li>
					<li>
						<a data-toggle="tab" href="#edit-password">
							<i class="blue ace-icon fa fa-key bigger-125"></i>
							密码修改
						</a>
					</li>
				</ul>

				<div class="tab-content profile-edit-tab-content" style="border:solid 0px #fff;border-top:solid 1px #bebebe;box-shadow: 0 0 0 0;">
					<div id="edit-basic" class="tab-pane in active">
						<h4 class="blue bolder smaller">基本信息</h4>
						<div id="basicinfoform">
						<ul class="form-elements">
							<li class="field-group">
								<label class="field-label" for="username">用户名：<span class="field-required">*</span></label>
								<div class="field-input">
									<input type="text" id="username" name="username" 
									value="${user.username}" style="width: 300px;" readonly>
								</div>
							</li>
							<li class="field-group">
								<div class="field-fluid2">
									<label class="field-label">
										性别：<span class="field-required">*</span>
									</label>
									<div class="field-input">
										 <label for="gender1">
											<input type="radio" id="gender1" name="gender" value="1" <c:if test="${user.gender == 1 }">checked</c:if>>男
										</label>&nbsp;&nbsp;
										<label for="gender2">
											<input type="radio" id="gender2" name="gender" value="0" <c:if test="${user.gender == 0 }">checked</c:if>>女
										</label>
									</div>
								</div>
							</li>
							<li class="field-group">
								<div class="field-fluid2">
									<label class="field-label" for="name">
										真实姓名：<span class="field-required">*</span>
									</label>
									<div class="field-input">
										<input type="text" id="name" name="name" value="${user.name}" style="width: 300px;">
									</div>
								</div>
							</li>
							<li class="field-group">
								<div class="field-fluid2">
									<label class="field-label" for="remark">
										备注：
									</label>
									<div class="field-input">
										<textarea name="remark" id="remark" rows="3" cols="20" style="width: 300px;">${user.remark}</textarea>
									</div>
								</div>
							</li>
						</ul>
						<div class="space"></div>
						<h4 class="blue bolder smaller">联系方式</h4>
						<ul class="form-elements">
							<li class="field-group">
								<div class="field-fluid2">
									<label class="field-label" for="phone">
										手机号码：
									</label>
									<div class="field-input">
										<input type="text" id="phone" name="phone" maxlength="11" 
										value="${user.phone}" style="width: 300px;">
									</div>
								</div>
							</li>
							<li class="field-group">
								<label class="field-label" for="email">
									Email：
								</label>
								<div class="field-input">
									<input type="text" id="email" name="email" value="${user.email}" style="width: 300px;">
								</div>
							</li>
						</ul>
						<div class="space-4"></div>
						<div class="field-button">
							<div class="btn btn-info" onclick="updateInfo();">
								<i class="ace-icon fa fa-check bigger-110"></i>保存
							</div> &nbsp; &nbsp;
							<div class="btn" onclick="ownerDialog.close();"><i class="glyphicon glyphicon-remove"></i>&nbsp;取消</div>
						</div>
						</div>
					</div>

					<div id="edit-password" class="tab-pane">
						<div class="space-10"></div>
						<form id="update_password_form">
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right" for="password">旧密码</label>

							<div class="col-sm-9">
								<input type="password" name="password" id="password" />
							</div>
						</div>

						<div class="space-4"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label no-padding-right" for="newpassword">新密码</label>

							<div class="col-sm-9">
								<input type="password" id="newpassword" name = "newpassword" />
							</div>
						</div>
						</form>
						<div class="space-4"></div>
						<div class="field-button">
							<div class="btn btn-info" onclick="updateUserPassWord();">
								<i class="ace-icon fa fa-check bigger-110"></i>更新
							</div> &nbsp; &nbsp;
							<div class="btn" onclick="ownerDialog.close();"><i class="glyphicon glyphicon-remove"></i>&nbsp;取消</div>
						</div>	
					</div>
				</div>
			</div>
		</form>
	</div><!-- /.span -->
  <iframe src="about:blank" name="_ifr" height="0" class="hidden"></iframe>
  <script type="text/javascript" src="<%=path%>/plugins/public_components/js/select2.js"></script>   <!-- 下拉框控件 -->
  <script src="<%=path%>/plugins/ace/assets/js/jquery.maskedinput.js"></script>
  <script src="<%=path%>/plugins/ace/assets/js/ace/ace.widget-box.js"></script>
  <script src="<%=path%>/plugins/ace/assets/js/ace/elements.wizard.js"></script>
  <script type="text/javascript">
  $(function(){
	  $('.myselect2').select2();
  });
  	$('#userForm').validate({
  		rules:{
  			"username":{
  				required:true,
  				accept1:"^[a-zA-Z0-9_]+$",
  				maxlength:20
  				//remote:"<%=path%>/user/hasU.do?USERNAME=USERNAME"
  			},
  			"password":{
  				accept1:"^[a-zA-Z0-9]+$",
  				maxlength:20
  			},
  			"name":{
  				required:true,
  				invalidChar:"'\"\\\\",
  				maxlength:32
  			},
  			"phone":{
   			   mobile: true
   			}, 
  			"email":{
  				accept1:"[a-zA-Z0-9_]@*.com$",
  				maxlength:256
  			},
  			"role_id":{
  				required:true
  			},
  			"ip":{
  			   IP:true
  			},
  			"remark":{
  				maxlength:256
  			}
  		},
  		messages:{
  			"username":{
  				required:"请输入登录用户名！",
  				accept1:"用户名格式不正确！",
  				maxlength:"长度不能超过20个字符！",
  				remote:"您输入的用户名已经存在，请重新输入！"
  			},
  			"password":{
  				accept1:"密码格式不正确！",
  				maxlength:"长度不能超过20个字符！"
  			},
  			"re_password":{
  				required:"请输入确认密码！",
  				equalTo:"2次输入的密码不一致！"
  			},
  			"name":{
  				required:"请填写真实姓名",
  				invalidChar:"姓名中不能含有以下特殊字符 '\"\\ ",
  				maxlength:"长度不能超过32个字符或汉字！"
  			},
  			"phone":{
  				accept1:"手机号格式不正确"
   			}, 
  			"email":{
  				accept1:"邮箱格式不正确",
  				maxlength:"长度不能超过256个字符！",
  				remote:"您的邮箱已经被使用，请确认！"
  			},
  			"role_id":{
  				required:"请选择您所属的角色！"
  			},
  			"remark":{
  				maxlength:"备注内容不能超过256个字符."
  			}
  		}
  	 }); 
  	 </script>
  </body>
</html>
