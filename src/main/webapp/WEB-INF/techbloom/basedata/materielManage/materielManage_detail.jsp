<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="id" id="id" value="${materiel.id}">

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">物料编码：</label>
				<div class="controls" style=" margin-top:6px;">
						${materiel.materielCode}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">物料名称：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.materielName}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">物料条码：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.barcode}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">货主：</label>
				<div class="controls" style=" margin-top:6px;">
					${customerName}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">供应商：</label>
				<div class="controls" style=" margin-top:6px;">
					${supplierName}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">生产厂家：</label>
				<div class="controls" style=" margin-top:6px;">
					${producerName}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">物料规格：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.spec}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">包装单位：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.unit}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">批次规则：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.batchRule}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">保质期(天)：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.qualityPeriod}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">上架分类：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.upshelfClassify}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">拣选分类：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.pickClassify}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">长(m)：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.length}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">宽(m)：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.wide}
				</div>
			</div>
		</div>

		<div class="row" style="margin:0;padding:0;">
			<div class="control-group span6" style="display: inline">
				<label class="control-label">高(m)：</label>
				<div class="controls" style=" margin-top:6px;">
					${materiel.height}
				</div>
			</div>

			<div class="control-group span6" style="display: inline">
				<label class="control-label">托盘容量(kg)：</label>
				<div class="controls" style=" margin-top:4px;">
					${materiel.trayWeight}
				</div>
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
    function closeWindow() {
        layer.close($queryWindow);
    }
</script>