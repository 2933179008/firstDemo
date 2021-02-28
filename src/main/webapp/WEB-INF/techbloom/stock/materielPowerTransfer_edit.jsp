<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="materielPowerTransfer_edit_page" class="row-fluid" style="height: inherit;">
    <form id="materielPowerTransferForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${stock.id}">

        <div class="control-group">
            <label class="control-label" for="positionCode">所在库位：</label>
            <div class="controls">
                <div class="input-append span12required">
                    <input type="text" class="span11" id="positionCode" name="positionCode"
                           value="${stock.positionCode}" readonly="readonly">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="availableAmount">可用数量：</label>
            <div class="controls">
                <div class="input-append span12required">
                    <input type="text" class="span11" id="availableAmount" name="availableAmount"
                           value="${stock.availableStockAmount}" readonly="readonly">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="availableWeight">库存可用重量：</label>
            <div class="controls">
                <div class="input-append span12required">
                    <input type="text" class="span11" id="availableWeight" name="availableWeight"
                           value="${stock.availableStockWeight}" readonly="readonly">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="availableStockAmount">转移数量：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="availableStockAmount" name="availableStockAmount" value=""
                           placeholder="请依据库存可用数量输入转移数量">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="availableStockWeight">转移重量（kg）: </label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="availableStockWeight" name="availableStockWeight" value=""
                           placeholder="请依据库存可用重量输入转移重量">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="materialCode">物料：</label>
            <div class="controls">
                <div class="span12 required">
                    <select id="materialCode" name="materialCode" style="width:91%;">
                        <option value="">--请选择转移成的物料--</option>
                        <c:forEach items="${materielList}" var="materiel">
                            <option value="${materiel.materielCode}"
                                    <c:if test="${materiel.materielCode eq stock.materialCode}">selected</c:if> >${materiel.materielCode}-${materiel.materielName}</option>
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
                        <option value="">--请选择--</option>
                        <option value="0">自采</option>
                        <option value="1">客供</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="batchNo">批次号：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="batchNo" name="batchNo" value="" placeholder="请输入批次号">
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

    $("#materielPowerTransferForm #materialCode").select2({
        minimumInputLength: 0,
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#materielPowerTransferForm #materialCode").on("change.select2", function () {
            $("#materielPowerTransferForm #materialCode").trigger("keyup")
        }
    );

    //表单校验
    $("#materielPowerTransferForm").validate({
        rules: {
            /*转移物料*/
            "materialCode": {
                required: true,
                maxlength: 255
            },
            /*转移重量*/
            "availableStockWeight": {
                required: true,
                maxlength: 255
            },
            /*转移数量*/
            "availableStockAmount": {
                required: true,
                maxlength: 255
            },
            /*批次号*/
            "batchNo": {
                required: true,
                maxlength: 255
            }
        },
        messages: {
            "materialCode": {
                required: "请输入转移物料"
            },
            "availableStockWeight": {
                required: "请输入转移重量"
            },
            "availableStockAmount": {
                required: "请输入转移数量"
            },
            "batchNo": {
                required: "请输入批次号"
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
        var materielBundingParam = $("#materielPowerTransferForm").serialize();
        if ($("#materielPowerTransferForm").valid()) {
            $(".savebtn").attr("disabled", "disabled");
            $.ajax({
                url: context_path + "/stockQuery/saveMaterielPower?tm=" + new Date(),
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