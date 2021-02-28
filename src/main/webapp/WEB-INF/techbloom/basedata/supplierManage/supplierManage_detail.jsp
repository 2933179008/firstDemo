<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="id" id="id" value="${supplier.id}" >

		<div class="control-group">
			<label class="control-label">供应商编码：</label>

			<c:if test="${status == 1}">
				<div class="controls" style=" margin-top:6px;">
						${supplier.supplierCode}
				</div>
			</c:if>

			<c:if test="${status == 2}">
				<div class="controls" style=" margin-top:6px;">
						${supplier.supplierCode}
				</div>
			</c:if>

		</div>

		<div class="control-group">
			<label class="control-label">供应商名称：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.supplierName}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">供应商类别：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.supplierType}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">联系人：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.linkman}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">联系电话：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.telephone}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">地址：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.address}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.mail}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls" style=" margin-top:6px;">
				${supplier.remark}
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