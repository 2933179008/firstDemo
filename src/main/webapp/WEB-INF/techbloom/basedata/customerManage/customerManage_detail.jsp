<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="customerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">

		<div class="control-group">
			<label class="control-label">客户编码：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.customerCode}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">客户名称：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.customerName}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">客户类型：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.customerType}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">联系人：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.linkman}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">联系电话：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.telephone}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">地址：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.address}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.mail}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls" style=" margin-top:4px;">
				${customer.remark}
			</div>
		</div>

	</form>
	<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
		<span class="btn btn-danger" onclick="closeWindow()">返回</span>
	</div>
</div>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 关闭弹出框
     * @param jsonParam
     */
    function closeWindow(){
        layer.close($queryWindow);
    }
</script>