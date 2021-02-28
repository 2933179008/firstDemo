<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
	<script type="text/javascript">
		var context_path = "<%=path%>";
	</script>
  	<div style="padding-left:30px;">
   	    <form id="queryForm-c">
			<ul class="form-elements">
				<!-- 用户名 -->
				<li class="field-group">
					<label class="inline" for="username" style="margin-right:20px;">
						用&nbsp;户&nbsp;&nbsp;名：
						<input type="text" name="username" id="username" value="${user.username }"
						style="width: 200px;" placeholder="用户名"/>
					</label>
				</li>
				<!-- 姓名 -->
				<li class="field-group">
					<label class="inline" for="name" style="margin-right:20px;">
						姓&emsp;&emsp;名：
						<input type="text" name="name" id="name" value="${user.name }"
						style="width: 200px;" placeholder="姓名"/>
					</label>
				</li>
				<!-- 邮箱 -->
				<li class="field-group">
					<label class="inline" for="email" style="margin-right:20px;">
						邮&emsp;&emsp;箱：
						<input type="text" name="email" id="email" value="${user.email }"
						style="width: 200px;" placeholder="邮箱"/>
					</label>
				</li>
				<!-- 手机号码 -->
				<li class="field-group">
					<label class="inline" for="phone" style="margin-right:20px;">
						手机号码：
						<input type="text" name="phone" id="phone" value="${user.phone }"
						style="width: 200px;" placeholder="手机号码"/>
					</label>
				</li>
				
				<!-- 底部工具按钮 -->
				<li class="field-group">
					<div class="field-button">
						<div class="btn btn-info" onclick="queryOkc();">
				            <i class="ace-icon fa fa-check bigger-110"></i>确定
			            </div> &nbsp; &nbsp;
						<div class="btn" onclick="layer.closeAll();"><i class="glyphicon glyphicon-remove"></i>&nbsp;取消</div>
					</div>
				</li>
			</ul>
		</form>
  	</div>
  <script type="text/javascript">
  
		function queryOkc(){
			//var formJsonParam = $('#queryForm').serialize();
			var queryParam = iTsai.form.serialize($('#queryForm-c'));
			//执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
			Dialog.openerWindow().queryUserListByParam(queryParam);
			//操作成功,关闭当前子窗口
	    	ownerDialog.close();
		}
		
  </script>
