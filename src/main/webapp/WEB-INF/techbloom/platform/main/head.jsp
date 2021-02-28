<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%> 
<%
	String pathh = request.getContextPath();
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/animate.css" />
	<div id="navbar" class="navbar navbar-default" style="z-index:2;">
		<div class="navbar-container" id="navbar-container">
			<!-- #section:basics/sidebar.mobile.toggle -->
			<button type="button" class="navbar-toggle menu-toggler pull-left"
				id="menu-toggler" data-target="#sidebar">
				<span class="sr-only">Toggle sidebar</span> <span class="icon-bar"></span>
				<span class="icon-bar"></span><span class="icon-bar"></span>
			</button>

			<!-- /section:basics/sidebar.mobile.toggle -->
			<div class="navbar-header pull-left">
				<a href="#" class="navbar-brand"> <small> 
				    <i class="fa fa-home fa-lg" aria-hidden="true"></i>
				    	智能仓储管理系统
				</small>
				</a>
			</div>

			<div class="navbar-buttons navbar-header pull-right" role="navigation">
				<ul class="nav ace-nav">
					<li class="light-blue">
					   <a data-toggle="dropdown" href="#" class="dropdown-toggle"> 
					   		<i class = "fa fa-user fa-2x"></i>
						   <span class="user-info">${user.username}</span> 
						   <i class="ace-icon fa fa-caret-down"></i>
					   </a>
						<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li>
							    <a href="javascript:;" onclick="userSet();return false;"><i class="ace-icon fa fa-cog"></i>个人设置 </a>
							</li>
							<li class="divider"></li>
							<li>
							    <a href="javascript:;" onclick="logout();return false;"> 
							       <i class="ace-icon fa fa-power-off"></i>退出
							    </a>
							</li>
						 </ul>
					  </li>
				</ul>
			</div>
		</div>
	</div>