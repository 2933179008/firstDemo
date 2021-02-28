<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%-- <jsp:forward page="/login/login.do" /> --%>
<script type="text/javascript">
    top.window.location.href = "<%=path%>/login/login";
</script>