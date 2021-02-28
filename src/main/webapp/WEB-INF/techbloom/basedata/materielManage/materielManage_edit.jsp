<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${materiel.id}">

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materielCode">物料编码：</label>
                <c:if test="${materiel.id == null}">
                    <div class="controls">
                        <div class="input-append span12 required">
                            <input type="text" class="span11" id="materielCode" name="materielCode"
                                   placeholder="物料编码" style="width: calc(100% - 70px);"
                                   <c:if test="${materiel.id==null}">value="" </c:if>>
                                   <%--<c:if test="${materiel.id!=null}">value="${materiel.materielCode}"</c:if>/>--%>
                        </div>
                    </div>
                </c:if>

                <c:if test="${materiel.id !=null}">
                    <div class="controls">
                        <div class="input-append span12 required">
                            <input type="text" class="span11" name="materielCode"
                                   value="${materiel.materielCode}" placeholder="物料编码" readonly="true"
                                   style="width: calc(100% - 70px);">
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materielName">物料名称：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="materielName" name="materielName"
                               value="${materiel.materielName}" placeholder="物料名称" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="barcode">物料条码：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="barcode" name="barcode" value="${materiel.barcode}"
                               placeholder="物料条码" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="customerBy">货主：</label>
                <div class="controls">
                    <div class="span12required">
                        <select id="customerBy" name="customerBy" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <c:forEach items="${customerList}" var="customer">
                                <option value="${customer.id}"
                                        <c:if test="${customer.id eq materiel.customerBy}">selected</c:if> >${customer.customerName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="supplierBy">供应商：</label>
                <div class="controls">
                    <div class="span12required">
                        <select id="supplierBy" name="supplierBy" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <c:forEach items="${supplierList}" var="supplier">
                                <option value="${supplier.id}"
                                        <c:if test="${supplier.id eq materiel.supplierBy}">selected</c:if> >${supplier.supplierName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="producerBy">生产厂家：</label>
                <div class="controls">
                    <div class="span12required">
                        <select id="producerBy" name="producerBy" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <c:forEach items="${producerList}" var="producer">
                                <option value="${producer.id}"
                                        <c:if test="${producer.id eq materiel.producerBy}">selected</c:if> >${producer.producerName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="spec">物料规格：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="spec" name="spec" value="${materiel.spec}"
                               placeholder="商品规格（单位关联重量）" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="unit">包装单位：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="unit" name="unit" value="${materiel.unit}"
                               placeholder="物料单位（一、二级单位）" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="batchRule">批次规则：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="batchRule" name="batchRule" value="${materiel.batchRule}"
                               placeholder="批次规则" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="qualityPeriod">保质期(天)：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" onkeyup="clearNoPx(this)" onblur="clearNoPx(this)" id="qualityPeriod" name="qualityPeriod"
                               value="${materiel.qualityPeriod}" placeholder="保质期" style="width: calc(100% - 70px);">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="upshelfClassify">上架分类：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <select id="upshelfClassify" name="upshelfClassify" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <option value="A"
                                    <c:if test="${'A' eq materiel.upshelfClassify}">selected</c:if> >A
                            </option>
                            <option value="B"
                                    <c:if test="${'B' eq materiel.upshelfClassify}">selected</c:if> >B
                            </option>
                            <option value="C"
                                    <c:if test="${'C' eq materiel.upshelfClassify}">selected</c:if> >C
                            </option>
                            <option value="D"
                                    <c:if test="${'D' eq materiel.upshelfClassify}">selected</c:if> >D
                            </option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="pickClassify">拣选分类：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <select id="pickClassify" name="pickClassify" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <option value="A"
                                    <c:if test="${'A' eq materiel.pickClassify}">selected</c:if> >A
                            </option>
                            <option value="B"
                                    <c:if test="${'B' eq materiel.pickClassify}">selected</c:if> >B
                            </option>
                            <option value="C"
                                    <c:if test="${'C' eq materiel.pickClassify}">selected</c:if> >C
                            </option>
                            <option value="D"
                                    <c:if test="${'D' eq materiel.pickClassify}">selected</c:if> >D
                            </option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="length">长(m)：</label>
                <div class="controls">
                    <div class="input-append span12 ">
                        <input type="text" class="span11" onkeyup="clearNoPx(this)" onblur="clearNoPx(this)" id="length" name="length" value="${materiel.length}"
                               placeholder="长(m)" style="width: calc(100% - 44px);">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="wide">宽(m)：</label>
                <div class="controls">
                    <div class="input-append span12 ">
                        <input type="text" class="span11" onkeyup="clearNoPx(this)" onblur="clearNoPx(this)" id="wide" name="wide" value="${materiel.wide}"
                               placeholder="宽(m)" style="width: calc(100% - 44px);">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="height">高(m)：</label>
                <div class="controls">
                    <div class="input-append span12 ">
                        <input type="text" class="span11" onkeyup="clearNoPx(this)" onblur="clearNoPx(this)" id="height" name="height" value="${materiel.height}"
                               placeholder="高(m)" style="width: calc(100% - 44px);">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="trayWeight">托盘容量(kg)：</label>
                <div class="controls">
                    <div class="input-append span12 ">
                        <input type="text" class="span11" onkeyup="clearNoPx(this)" onblur="clearNoPx(this)" id="trayWeight" name="trayWeight" value="${materiel.trayWeight}"
                               placeholder="托盘容量(kg)" style="width: calc(100% - 44px);">
                    </div>
                </div>
            </div>
        </div>
    </form>
    <div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
        <span class="savebtn btn btn-success" onclick="saveTaskDetail()">保存</span>
        <span class="btn btn-danger" onclick="closeWindow()">取消</span>
    </div>
</div>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 表单校验
     * @param jsonParam
     */
    $("#materielManagerForm").validate({
        rules: {
            /*物料编号*/
            "materielCode": {
                required: true,
                maxlength: 16,
                <c:if test="${materiel.id==null}">
                remote: context_path + "/materielManage/hasC?id=" + $('#id').val()
                </c:if>

            },
            /*物料名称*/
            "materielName": {
                required: true,
                maxlength: 20
            },
            /*物料条码*/
            "barcode": {
                required: true,
                maxlength: 20
            },
            /*货主*/
            // "customerBy": {
            //     required: true,
            //     maxlength: 16
            // },
            /*供应商*/
            // "supplierBy": {
            //     required: true,
            //     maxlength: 16
            // },
            /*生产厂家*/
            // "producerBy": {
            //     required: true,
            //     maxlength: 16
            // },
            /* 物料规格*/
            // "spec": {
            //     required: true,
            //     maxlength: 20
            // },
            /* 包装单位*/
            "unit": {
                required: true,
                maxlength: 20
            },
            /* 批次规则*/
            // "batchRule": {
            //     required: true,
            //     maxlength: 20
            // },
            /*保质期（天）*/
            "qualityPeriod": {
                required: true,
                number: true,
                maxlength: 20
            },
            /* 上架分类*/
            // "upshelfClassify": {
            //     required: true,
            //     maxlength: 20
            // },
            /* 拣选分类*/
            // "pickClassify": {
            //     required: true,
            //     maxlength: 20
            // },
            /* 长度*/
            "length": {
                number: true,
                maxlength: 16
            },
            /* 宽度*/
            "wide": {
                number: true,
                maxlength: 16
            },
            /* 高度*/
            "height": {
                number: true,
                maxlength: 16
            },
            /* 托盘容量*/
            "trayWeight": {
                number: true,
                maxlength: 16
            }
        },
        messages: {
            "materielCode": {
                required: "请输入物料编号",
                remote: "您输入的物料编码已经存在，请重新输入！"
            },
            "materielName": {
                required: "请输入物料名称",
            },
            "barcode": {
                required: "请输入物料条码"
            },
            // "customerBy": {
            //     required: "请选择货主"
            // },
            // "supplierBy": {
            //     required: "请选择供应商"
            // },
            // "producerBy": {
            //     required: "请选择生产厂家"
            // },
            "unit": {
                required: "请输入物料包装单位"
            },
            // "spec": {
            //     required: "请输入物料规格"
            // },
            // "batchRule": {
            //     required: "请输入批次规则"
            // },
            "qualityPeriod": {
                required: "请输入保质期"
            }
            // "upshelfClassify": {
            //     required: "请选择物料上架分类"
            // },
            // "pickClassify": {
            //     required: "请选择物料拣选分类"
            // }
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

    //只能输入整数
    function clearNoPx(obj) {
        obj.value = obj.value.replace(/-/g,"");
    }

    $("#materielManagerForm #customerBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#materielManagerForm #customerBy").on("change.select2", function () {
            $("#materielManagerForm #customerBy").trigger("keyup")
        }
    );

    $("#materielManagerForm #supplierBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#materielManagerForm #supplierBy").on("change.select2", function () {
            $("#materielManagerForm #supplierBy").trigger("keyup")
        }
    );

    $("#materielManagerForm #producerBy").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#materielManagerForm #producerBy").on("change.select2", function () {
            $("#materielManagerForm #producerBy").trigger("keyup")
        }
    );

    /**
     * 保存/修改
     * @param jsonParam
     */
    function saveTaskDetail() {
        var roleParam = $("#materielManagerForm").serialize();
        if ($("#materielManagerForm" +
            "").valid()) {
            $(".savebtn").attr("disabled", "disabled");
            $.ajax({
                url: context_path + "/materielManage/addMateriel?tm=" + new Date(),
                type: "POST",
                data: roleParam,
                dataType: "JSON",
                success: function (data) {
                    ajaxStatus = 1; //将标记设置为可请求
                    if (data) {
                        //刷新表格
                        $("#grid-table").jqGrid('setGridParam',
                            {
                                postData: {queryJsonString: ""} //发送数据
                            }
                        ).trigger("reloadGrid");
                        layer.msg("操作成功！", {icon: 1});
                        layer.close($queryWindow);
                    } else {
                        layer.alert("操作失败！", {icon: 2});
                    }
                }
            });
        }
    }

    /**
     * 关闭弹出框
     * @param jsonParam
     */
    function closeWindow() {
        layer.close($queryWindow);
    }
</script>