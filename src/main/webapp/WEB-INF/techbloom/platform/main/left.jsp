<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String pathl = request.getContextPath();
%>
<div id="sidebar" class="sidebar responsive">
	<ul class="nav nav-list">
		<li class="active" id="fhindex"><a href="#" onclick="changeMainIframe('<%=pathl%>/index/main.do');"> <i
				class="menu-icon fa fa-tachometer"></i> <span class="menu-text">工作台</span>
		</a> <b class="arrow"></b></li>
		<c:forEach items="${menuList}" var="menu">
			<li id="lm${menu.menuId }">
			<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa ${menu.menuIcon}"></i> 
					<span class="menu-text">${menu.menuName}</span> 
					<b class="arrow fa fa-angle-down"></b>
			</a> 
			<b class="arrow"></b>
				
			<ul class="submenu">
				<c:forEach items="${menu.subMenu}" var="sub">
					<c:choose>
						<c:when test="${not empty sub.menuUrl}">
							<li id="z${sub.menuId }"><a style="cursor:pointer;"
								href="javascript:;"
								onclick="chageMenu('<%=pathl%>/${sub.menuUrl}','${menu.menuName}','z${sub.menuId}','lm${menu.menuId}');return false;">
									<i class="menu-icon fa fa-caret-right"></i> ${sub.menuName}
							</a> <b class="arrow"></b></li>
						</c:when>
						<c:otherwise>
							<li><a href="javascript:void(0);"> <i
									class="icon-double-angle-right"></i>${sub.menuName }
							</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
			</li>
		</c:forEach>
	</ul>
	<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
		<i class="ace-icon fa fa-angle-double-left"
			data-icon1="ace-icon fa fa-angle-double-left"
			data-icon2="ace-icon fa fa-angle-double-right"></i>
	</div>
</div>
