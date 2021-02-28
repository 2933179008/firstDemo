
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String sys_name = "东洋饮料智能仓储系统";
	String sys_icon = "plugins/public_components/img/logo.png";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8" />
	<title><%=sys_name%></title>
	<meta name="author" content="DeathGhost" />
	<link rel="shortcut icon" href="<%=path%>/<%=sys_icon%>" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href="<%=path%>/static/techbloom/login/css/style.css" />
	
	<script src="<%=path%>/plugins/public_components/js/jquery-2.1.4.js"></script>
	<script src="<%=path%>/plugins/public_components/js/jquery.cookie.js"></script>
	<script src="<%=path%>/plugins/public_components/js/angular.js"></script>
</head>
<style>
 html,body {
	margin:0;
	overflow:hidden;
	width:100%;
	height:100%;
	display: flex;
    flex-direction: column;
/* 	cursor:none; */
/* 	background:black;
	background:linear-gradient(to bottom,#000000 0%,#5788fe 100%); */
}
.filter {
	width:100%;
	height:100%;
	position:absolute;
	top:0;
	left:0;
	background:#fe5757;
	animation:colorChange 30s ease-in-out infinite;
	animation-fill-mode:both;
	mix-blend-mode:overlay;
}
@keyframes colorChange {
	0%,100% {
	opacity:0;
}
50% {
	opacity:.9;
}
}.landscape {
	position:absolute;
	bottom:0px;
	left:0;
	width:100%;
	height:100%;
	/*background-image:url(https://openclipart.org/image/2400px/svg_to_png/250847/Trees-Landscape-Silhouette.png);
	*/
background-image:url('img/xkbg.png');
	background-size:1000px 250px;
	background-repeat:repeat-x;
	background-position:center bottom;
}

</style>
<body ng-app="login" >
    <div style="background: #fff; padding: 12px 0px 12px 70px;">
      <img  style="width: 120px;" src="<%=path%>/plugins/public_components/img/logo.png"/>
    </div>
	<div class="login_main" style="
	    background-image: url(<%=path%>/plugins/public_components/img/bg.png);
	    padding: 0px;
	    box-sizing: border-box;
	    background-origin: content-box;
	    background-repeat: no-repeat;
	    background-size: 100% 100%;
	">
	<dl class="admin_login" ng-controller="loginController">
			<dt>
			 <div style=" margin: 0px auto;display: flex; flex-wrap: nowrap; flex-direction: row;width:250px;">
				 <font color="#FFFFFF">东洋饮料智能仓储系统</font>
			 </div>
			</dt>
			<div class="login_content">
			<dd class="user_icon" style=" margin-top: 0px;">
				<input type="text" placeholder="账号" ng-model="user.name" id="account"  autocomplete="off" class="login_txtbx" />
			</dd>
			<span class="error">{{user_error}}</span>
			<dd class="pwd_icon">
				<input type="password" placeholder="密码" ng-model="user.pwd"  autocomplete="off" class="login_txtbx" />
			</dd>
			<span class="error">{{pwd_error}}</span>
			<dd class="login-text">
				<input type="checkbox" ng-click="saveCookie()" ng-model="isremember" id="remember"> <label for="remember">记住密码</label> 
				<!-- <a class="forget_pwd">忘记密码？</a> -->
			</dd>
			<span class="error">{{login_error}}</span>
			<dd style=" margin-bottom: 0px;"> 
				<input type="button" value="登陆" ng-click="login()" class="submit_btn" />
			</dd>
			</div>
     </dl>
	</div>
	<div style="background: #fff; padding: 18px 0px 18px 70px;">
      <p style="    font-size: 13px;">© 2019-2021 华清科盛版权所有</p>
    </div>
</body>
<script>
   var context_path= "<%=path%>";
   $("#account").focus();
</script>
<script src="<%=path%>/static/techbloom/login/js/login.js"></script>
</html>
