<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="materielPowerTransfer_edit_page" class="row-fluid" style="height: inherit;">
    <form id="materielPowerRfidTransferForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${materielBindRfidDetail.id}">

        <div class="control-group">
            <label class="control-label" for="oldMaterialCode">原物料编码：</label>
            <div class="controls">
                <div class="span12 required">
                    <input type="text" class="span11" id="oldMaterialCode" name="oldMaterialCode"
                           value="${materielBindRfidDetail.materielCode}" readonly="readonly">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="oldMaterialName">原物料名称：</label>
            <div class="controls">
                <div class="span12 required">
                    <input type="text" class="span11" id="oldMaterialName" name="oldMaterialName"
                           value="${materielBindRfidDetail.materielName}" readonly="readonly">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="materielCode">转换物料：</label>
            <div class="controls">
                <div class="span12 required">
                    <select id="materielCode" name="materielCode" style="width:91%;">
                        <option value="">--请选择--</option>
                        <c:forEach items="${materielList}" var="materiel">
                            <option value="${materiel.materielCode}"
                                    <c:if test="${materielBindRfidDetail.materielCode eq materiel.materielCode}">selected</c:if> >${materiel.materielCode}-${materiel.materielName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="documentType">物料类型：</label>
            <div class="controls">
                <div class="span12 required">
                    <select id="documentType" name="documentType" style="width:91%;">
                        <option value="0">自采</option>
                        <option value="1">客供</option>
                    </select>
                </div>
            </div>
        </div>

    </form>
    <div class="field-button" style="text-align: center;border-top: 0px;margin: 15px auto;">
        <span class="savebtn btn btn-success" onclick="saveMaterielPower()">保存</span>
        <span class="btn btn-danger" onclick="closeWindow()">取消</span>
    </div>
</div>
<script type="text/javascript">
    $(function () {

    });

    $("#materielPowerRfidTransferForm #materielCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#materielPowerRfidTransferForm #materielCode").on("change.select2", function () {
            $("#materielPowerRfidTransferForm #materielCode").trigger("keyup")
        }
    );

    //表单校验
    $("#materielPowerRfidTransferForm").validate({
        rules: {
            /*转移物料*/
            "materialCode": {
                required: true,
                maxlength: 255
            }
        },
        messages: {
            "materialCode": {
                required: "请输入转移物料"
            }
        },
        errorClass: "help-inline",
        errorElement: "span",
        highlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').addClass('error');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
        }
    });

    //保存
    function saveMaterielPower() {
        var materielBundingParam = $("#materielPowerRfidTransferForm").serialize();
        if ($("#materielPowerRfidTransferForm").valid()) {
            $(".savebtn").attr("disabled", "disabled");
            $.ajax({
                url: context_path + "/stockQuery/saveRfidMaterielPower?tm=" + new Date(),
                type: "POST",
                data: materielBundingParam,
                dataType: "JSON",
                success: function (data) {
                    ajaxStatus = 1; //将标记设置为可请求

                    if (data.result) {
                        //刷新收货单列表
                        $("#grid-table").jqGrid("setGridParam", {
                            postData: {queryJsonString: ""} //发送数据
                        }).trigger("reloadGrid");
                        layer.msg(data.msg, {icon: 1, time: 2400})
                        layer.closeAll();
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2400})
                    }
                }
            });
        }
    }

    //关闭弹出框
    function closeWindow() {
        layer.close($queryWindow);
    }
</script>