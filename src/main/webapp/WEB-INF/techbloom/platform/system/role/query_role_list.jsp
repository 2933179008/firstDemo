<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
    <title>角色查询</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<link rel="stylesheet" href="<%=path%>/plugins/ace/assets/css/select2.css" />
	<link rel="stylesheet" href="<%=path%>/plugins/ace//assets/css/font-awesome.css" />
	<style type="text/css">
	</style>
  </head>
  <body>
  	<div style="padding-left:30px;">
   	    <form id="queryForm-c">
			<ul class="form-elements">
				<!-- 角色名称 -->
				<li class="field-group">
					<label class="inline" for="roleName" style="margin-right:20px;">
						角色名称：
						<input type="text" name="roleName" id="roleName" value="${role.roleName }"
						style="width: 200px;" placeholder="角色名称"/>
					</label>
				</li>
				<!-- 底部工具按钮 -->
				<li class="field-group">
					<div class="field-button">
						<div class="btn btn-info" onclick="queryOkc();">
				            <i class="ace-icon fa fa-check bigger-110"></i>确定
			            </div> &nbsp; &nbsp;
						<div class="btn" onclick="ownerDialog.close();"><i class="glyphicon glyphicon-remove"></i>&nbsp;取消</div>
					</div>
				</li>
			</ul>
		</form>
  	</div>
  	<script type="text/javascript" src="<%=path%>/plugins/public_components/js/select2.js"></script>
  <script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
  <script type="text/javascript">
	$(function(){
	});
	function queryOkc(){
		//var formJsonParam = $('#queryForm').serialize();
		var queryParam = iTsai.form.serialize($('#queryForm-c'));
		//执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
		Dialog.openerWindow().queryRoleListByParam(queryParam);
		//操作成功,关闭当前子窗口
	   	ownerDialog.close();
	}
  </script>
  </body>
</html>
