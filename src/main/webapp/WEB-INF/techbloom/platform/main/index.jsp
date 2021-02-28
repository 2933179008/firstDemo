<%@ page import="com.tbl.common.config.StaticConfig" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>	
<%
	String path = request.getContextPath();
	String sys_name = "东洋饮料智能仓储系统";
	String sys_icon = "plugins/public_components/img/dyyl.png";
	String adapteRfid = StaticConfig.getGetAdapteRfid();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title><%=sys_name%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- 框架css-->
<link rel="shortcut icon" href="<%=path%>/<%=sys_icon%>"
	type="image/x-icon">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/bootstrap-responsive.min.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/fullcalendar.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/matrix-style.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/matrix-media.css" />
<link href="<%=path%>/plugins/public_components/css/font-awesome.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/jquery.gritter.css" />

<!-- font-awesome图标库 -->
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/font-awesome.min.css" />
<!-- jqgrid表格样式 -->
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/ui.jqgrid.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/myjqgrid.css" />

<!-- 下拉框样式 -->
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/select2.css" />
<!-- 事件框架 -->
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/datepicker.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/daterangepicker.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/layer.css" />
<!-- 弹出框样式 -->
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/uniform.css" />
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/metroStyle.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/fui.css" type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/form.css" type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/query.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/ace.min.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/dh.css" type="text/css">
<link rel="stylesheet"
	href="<%=path%>/plugins/public_components/css/pop.css" type="text/css">
<!-- end -->

<style>
* {
	font-family: '微软雅黑', 'Microsoft YaHei';
}

.green {
	color: #37e073;
}

.menu_scale+footer {
	width: 41px !important;
}

.menu_scale+footer .dh {
	display: none;
}

.menu_scale+footer #shrink_menu {
	margin-right: 3px !important;
}

.menu_scale+footer .fa-angle-left:before {
	content: "\f105";
}

.submenu ul {
	list-style: none;
}

.footer_box {
	width: 185px;
	position: absolute;
	top: 50px;
	background: #4A5064 !important;
	padding: 0px !important;
	height: 35px !important;
	margin: 0px !important;
	z-index: 100;
	border-top: 1px solid #42485b !important;
}

.footer_box .dh {
	float: left;
	width: 130px;
	color: #fff;
	padding: 0px 10px;
	border-right: 1px solid #5f6061;
	line-height: 35px;
	cursor: pointer;
}

.footer_box #shrink_menu {
	width: 23px;
	height: 30px;
	line-height: 35px;
	display: block;
	float: right;
	cursor: pointer;
}

.footer_box #shrink_menu :hover, .footer_box .dh:hover {
	color: #00c1de !important;
}

.language_config {
	color: #cacaca;
	border: 1px solid #cacaca;
	padding: 2px 11px;
	border-radius: 3px;
	right: 12px;
	position: absolute;
	margin-top: 12px;
	font-size: 12px;
}

.language_config:hover {
	color: #fcfcfc;
	border: 1px solid #fcfcfc;
	cursor: pointer;
}

.toggle_navbar{
	position: absolute;
	top: 50%;
	margin-top:-50px;
	color: #fff;
	z-index: 999;
	background: #00c1de;
	display: flex;
	justify-content: center;
	align-items: center;
}
.toggle_navbar_icon1{
	right:0;
	width: 30px;
	height: 100px;
	border-top-left-radius: 8px;
	border-bottom-left-radius: 8px;
}
.toggle_navbar_icon2{
	bottom: 0;
	right: 0;
	left: 0;
	width: 100%;
	height: 60px;
	top: auto;
}
.toggle_navbar i{
	font-size: 24px;
}
</style>

</head>

<body class="default">

	<div id="header">

		<h1 style="width:450px">
			<img style="float: left;height: 35px; margin-right: 6px; margin-top: 1px;"
				src="<%=path%>/<%=sys_icon%>"></img><a href="#"><%=sys_name%></a>
		</h1>
	</div>
	<div id="user-nav" class="navbar navbar-inverse">
		<ul class="nav">
			<li class="">
				<div class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
					<div class="ant-tabs-nav-wrap">
						<div class="ant-tabs-nav-scroll">
							<div class="ant-tabs-nav ant-tabs-nav-animated">
								<div class="ant-tabs-ink-bar ant-tabs-ink-bar-animated"
									style="display: block; transform: translate3d(0px, 0px, 0px); width: 81px;"></div>
								<div class="ant-tabs-tab-active ant-tabs-tab">${count }</div>
								<div class=" ant-tabs-tab">消息 (1)</div>
							</div>
						</div>
					</div>
					<div class="ant-tabs-content ant-tabs-content-animated"
						style="margin-left: 0%;">
						<div class="ant-tabs-tabpane ant-tabs-tabpane-active">
							<div>
								<div class="ant-list ant-list-split">
										
									<c:forEach items="${nlist}" var="notice">
                                      <div class="ant-list-item">
										<div class="ant-list-item-meta">
											<div class="ant-list-item-meta-avatar">
												<span class="ant-avatar ant-avatar-circle ant-avatar-image">
													<i class="icon icon-envelope-alt"></i>
												</span>
											</div>
											<div class="ant-list-item-meta-content">
												<h4 class="ant-list-item-meta-title">
													${notice.notice }
												</h4>
												<div class="ant-list-item-meta-description">
													<div>
														<div class="ant_datetime">${notice.createtime}</div>
													</div>
												</div>
											</div>
										</div>
									</div>
                                    </c:forEach>
								</div>
								<div class="ant_clear">清空通知</div>
							</div>
						</div>

						<div class="ant-tabs-tabpane  ant-tabs-tabpane-inactive">
							<div>
								<div class="ant-list  ant-list-split">
									<div class="ant-list-item ">
										<div class="ant-list-item-meta ">
											<div class="ant-list-item-meta-avatar">
												<span class="ant-avatar  ant-avatar-circle ant-avatar-image">
													<img
													src="https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg">
												</span>
											</div>
											<div class="ant-list-item-meta-content">
												<h4 class="ant-list-item-meta-title">
													<div>
														库位饱和度
														<div></div>
													</div>
												</h4>
												<div class="ant-list-item-meta-description">
													<div>
														<div title="库位饱和度"
															style="font-size: 12px; line-height: 1.5;">库位饱和度即将饱满</div>
														<div class="ant_datetime">1 个月前</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="ant_clear">清空消息</div>
							</div>
						</div>

						<div class="ant-tabs-tabpane  ant-tabs-tabpane-inactive">
							<div>
								<div class="ant-list  ant-list-split">
									<div class="ant-list-item ">
										<div class="ant-list-item-meta " style="    width: 100%;">
											<div class="ant-list-item-meta-content">
												<h4 class="ant-list-item-meta-title">
													<div>
														拉动任务DL000001
														<div class='ant_extra'>
															<div data-show="true" class="ant-tag"
																style="margin-right: 0px;">
																<span class="ant-tag-text">未开始</span>
															</div>
														</div>
													</div>
												</h4>
												<div class="ant-list-item-meta-description">
													<div>
														<div style="font-size: 12px;line-height: 1.5;"
															title="任务需要在 2017-01-12 20:00 前启动">任务需要在 2017-01-12
															20:00 前启动</div>
														<div class="ant_datetime"></div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="ant_clear">清空待办</div>
							</div>
						</div>

					</div>
				</div></li>
			<li class="dropdown" id="profile-messages"><a title="" href="#"
				data-toggle="dropdown" data-target="#profile-messages"
				class="dropdown-toggle"><i class="icon icon-user"></i> <span
					class="text">欢迎你，${user.username}</span><b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="#"><i class="icon-user"></i> 个人资料</a></li>
					<li class="divider"></li>
					<li><a href="#"><i class="icon-check"></i>修改密码</a></li>
				</ul></li>
			<li class=""><a title="" href="<%=path%>/login/logout.do"><i
					class="icon icon-share-alt"></i> <span class="text">退出系统</span></a></li>
		</ul>
	</div>
	<div id="sidebar">
		<div style="position: fixed;"></div>
		<ul style=""></ul>
		<span class="toggle_navbar toggle_navbar_icon1" id="toggle_menu"><i class="fa fa-angle-left"></i></span>
	</div>
	<%--<footer class="footer lt hidden-xs b-t b-dark footer_box" style="">
		<div class="dh">
			<i class="fa fa-cog" style="margin-right: 4px;"></i><span>自定义导航</span>
		</div>
		<span id="shrink_menu"> <i class="fa  icon-indent-left text"></i>
		</span>
	</footer>--%>
	<div id="content">
		<!--路径导航-->
		<div id="content-header">
			<div id="breadcrumb">
				<a href="<%=path%>/login/main/index.do" title="首页"
					class="tip-bottom"><i class="icon-home"></i>首页</a>
			</div>
		</div>
		<!--End-breadcrumbs-->

		<!---------------------------------------------  常用js   --------------------------------------------->
		<script>
   var context_path= "<%=path%>";
   var context_sys_path = "<%=path%>/techbloom/platform";
   var adapteRfid="<%=adapteRfid%>";
   var _user_id =${user.userId};
   var testparam = "12522";

   //叉车平板模块的定时器定义
   var interval1;
   var interval2;
   var interval3;

   var outStorageSlab_interval1;
</script>

		<script src="<%=path%>/plugins/public_components/js/jquery-2.1.4.js"></script>

		<script src="<%=path%>/plugins/public_components/js/bootstrap2.3.2.min.js"></script>
		<script src="<%=path%>/static/techbloom/main/index.js"></script>
		<!-- 框架js-->
		<script src="<%=path%>/plugins/public_components/js/excanvas.min.js"></script>
		<%-- <script src="<%=path%>/plugins/base-bootstrap/js/jquery.ui.custom.js"></script>  --%>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.flot.min.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.flot.resize.min.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.peity.min.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/fullcalendar.min.js"></script>
		<script src="<%=path%>/plugins/public_components/js/matrix.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/matrix.dashboard.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.gritter.min.js"></script>
		<script src="<%=path%>/plugins/public_components/js/jquery.wizard.js"></script>

		<script src="<%=path%>/plugins/public_components/js/matrix.popover.js"></script>
		<script src="<%=path%>/plugins/public_components/js/echarts.min.js"></script>
		<!-- echarts js -->
		<!-- 框架js end-->
		<script src="<%=path%>/plugins/public_components/js/layer.js"></script>
		<!-- 弹出框js -->
		<script
			src="<%=path%>/plugins/public_components/js/jquery-migrate-3.0.0.js"></script>
		<!-- jquery 兼容個版本 -->
		<script
			src="<%=path%>/plugins/public_components/js/jquery.validate.js"></script>
		<!-- jquery 校驗 -->

		<script src="<%=path%>/plugins/public_components/js/jquery.uniform.js"></script>
		<!-- jquery form 优化 -->

		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>

		<script
			src="<%=path%>/plugins/public_components/js/jquery.jqGrid.src.js"></script>
		<!-- jqgrid表格js -->
		<script src="<%=path%>/plugins/public_components/js/grid.locale-cn.js"></script>
		<script src="<%=path%>/plugins/public_components/js/fui.js"></script>
		<!-- fui.js -->

		<script
			src="<%=path%>/plugins/public_components/js/bootstrap-datepicker-zh.js"></script>
		<!-- 时间控件 -->

		<script src="<%=path%>/plugins/public_components/js/moment-zh.js"></script>
		<!-- 时间范围控件 -->
		<script
			src="<%=path%>/plugins/public_components/js/daterangepicker.js"></script>
		<!-- 时间范围控件 -->
		<script src="<%=path%>/plugins/public_components/js/common.js"></script>
		<!-- common.js 公共方法js -->
		<script src="<%=path%>/plugins/public_components/js/select2.js"></script>
		<!-- 下拉框控件 -->
		<script src="<%=path%>/plugins/public_components/js/select2tree.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/bootstrap-datetimepicker.js"></script>
		<!-- 日期范围控件 -->

		<!-- ztree控件 -->
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/jquery.ztree.core.js"></script>
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/jquery.ztree.excheck.js"></script>
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/jquery.ztree.exedit.js"></script>
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/assembly.js"></script>
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/relate.js"></script>
		<script type="text/javascript"
			src="<%=path%>/plugins/public_components/js/pop.js"></script>

		<script src="<%=path%>/plugins/public_components/js/jquery.ui.core.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.widget.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.mouse.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.draggable.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.sortable.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.position.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.resizable.js"></script>
		<script
			src="<%=path%>/plugins/public_components/js/jquery.ui.dialog.js"></script>

		<!---------------------------------------------  常用js   --------------------------------------------->

		<!--内容块-->
		<div class="container-fluid"
			style="padding: 0px; height: calc(100% - 40px);    overflow-y: auto;  overflow-x: hidden;">
		</div>
	</div>

	<!--end-main-container-part-->
</body>
<script src="<%=path%>/static/techbloom/main/dh.js"></script>
<script src="<%=path%>/static/techbloom/main/dh_n.js"></script>
<script type="text/javascript">
$(function(){
	 new dh($(".dh"));
	 var top = 0, h= 44 ; 
	 $(".ant-tabs-tab").bind("click",function(){
		 $(".ant-tabs-tab").removeClass("ant-tabs-tab-active");
		 $(this).addClass("ant-tabs-tab-active");
		 $(".ant-tabs-tabpane").removeClass("ant-tabs-tabpane-active").addClass("ant-tabs-tabpane-inactive");
		 $(".ant-tabs-tabpane").eq($(this).index()-1).addClass("ant-tabs-tabpane-active").removeClass("ant-tabs-tabpane-inactive");
	 });
	 $("#sidebar").bind("mousewheel", function(event, delta, deltaX, deltaY) {
		    
		    if( !$(this).hasClass("menu_scale") ){
		    	return;
		    }
		    
		    if($(event.target).attr("pid") && $(event.target).attr("pid") !="0"){
		    	return;
		    }
		    if($(event.target).closest("li").attr("pid") && $(event.target).closest("li").attr("pid") !="0"){
		    	return;
		    }
		    $(this).addClass("hide_menu");
		    var content_h = $("#content").height() -32;
		    var _h = $("#sidebar").children("ul").height();
		    var c = content_h - _h;	
		    if(delta === 1 && top < -1){
		    	top = top + h;     	
		    	$("#sidebar").animate({top : top + "px"})
		    	
		    }else if(top > c && delta === -1){
		    	top = top - h;
		    	$("#sidebar").animate({top : top + "px"})
		    }
	 });
	 $("#shrink_menu").bind("click",function(){
		// debugger;
		 $("#sidebar").toggleClass("menu_scale");
		 $("#content").toggleClass("shrink_content");
		 $("body").resize();
		 if( $("#sidebar").hasClass("menu_scale")){
			 $(".submenu").removeClass("open");
			 $(".submenu ul").hide();
		 }
		 $(this).find("i").toggleClass("icon-indent-left").toggleClass("icon-indent-right");
		
	 }) ;

	 $("li.submenu").hover(function(){
		 if($(this).parents(".menu_scale").length == 0){
			 return ;
		 }
		$("#sidebar").removeClass("hide_menu");
		var _ul = $(this).find("ul");
		var h = _ul[0].scrollHeight;
		var p = $(this).position();
		var content_h = $("#content").height() -40;
		var pos = $("#sidebar").position();
		var c = p.top + pos.top -58;
		//debugger
		if( content_h < h){
			_ul.css({
				height : content_h,
				top:  -c
			});
		}else{
			_ul.css({
				height : h
			});
			if( c + 45 > h ){
					_ul.css({
						top : -(h - 44) 
					});	
		
			}else{
				_ul.css({
					top : -c
				});
			}
		
		}
		
		
	 }, function(){
		 
	 });
   

});

// toggle_menu
$("#toggle_menu").bind("click",function(){
    // debugger;
    $("#sidebar").toggleClass("menu_scale");
    $("#content").toggleClass("shrink_content");
    $("body").resize();
    if( $("#sidebar").hasClass("menu_scale")){
        $(".submenu").removeClass("open");
        $(".submenu ul").hide();
    }
    $(this).toggleClass("toggle_navbar_icon1").toggleClass("toggle_navbar_icon2");
    $(this).find("i").toggleClass("fa-angle-left").toggleClass("fa-angle-right");

}) ;

</script>
</html>
