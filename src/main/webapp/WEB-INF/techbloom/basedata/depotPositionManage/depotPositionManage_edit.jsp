<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
    <form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${depotPosition.id}">

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionCode">库位编码：</label>
                <div class="controls">
                    <c:if test="${depotPosition.id==null}">
                        <div class="input-append span12 required">
                            <input type="text" class="span11" id="positionCode" name="positionCode" placeholder="库位编码"
                                   style="width: calc(100% - 70px);"
                                   <c:if test="${depotPosition.id==null}">value="" </c:if>/>
                        </div>
                    </c:if>
                    <c:if test="${depotPosition.id!=null}">
                        <div class="input-append span12 required">
                            <input type="text" class="span11" name="positionCode" value="${depotPosition.positionCode}"
                                   placeholder="库位编码" readonly="true" style="width: calc(100% - 70px);">
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionName">库位名称：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="positionName" name="positionName"
                               style="width: calc(100% - 70px);"
                               value="${depotPosition.positionName}" placeholder="库位名称">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="depotareaId">所属库区：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <select id="depotareaId" name="depotareaId" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <c:forEach items="${areaList}" var="area">
                                <option value="${area.id}"
                                        <c:if test="${area.id eq depotPosition.depotareaId}">selected</c:if> >${area.areaName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="row">排：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="row" name="row" value="${depotPosition.row}"
                               style="width: calc(100% - 44px);"
                               placeholder="排">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="column">列：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="column" name="column" value="${depotPosition.column}"
                               style="width: calc(100% - 44px);"
                               placeholder="列">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="layer">层：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="layer" name="layer" value="${depotPosition.layer}"
                               style="width: calc(100% - 44px);"
                               placeholder="层">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionType">库位类型：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <select id="positionType" name="positionType" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <option value="0" <c:if test="${'0' eq depotPosition.positionType}">selected</c:if>>地堆
                            </option>
                            <option value="1" <c:if test="${'1' eq depotPosition.positionType}">selected</c:if>>货架
                            </option>
                            <option value="2" <c:if test="${'2' eq depotPosition.positionType}">selected</c:if>>不良品
                            </option>
                            <option value="3" <c:if test="${'3' eq depotPosition.positionType}">selected</c:if>>暂存
                            </option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="blendType">混放类型：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <select id="blendType" name="blendType" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <option value="0" <c:if test="${'0' eq depotPosition.blendType}">selected</c:if>>可混放
                            </option>
                            <option value="1" <c:if test="${'1' eq depotPosition.blendType}">selected</c:if>>不可混放
                            </option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="classify">ABC分类：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <select id="classify" name="classify" style="width: calc(100% - 70px);">
                            <option value="">请选择</option>
                            <option value="A" <c:if test="${'A' eq depotPosition.classify}">selected</c:if>>A</option>
                            <option value="B" <c:if test="${'B' eq depotPosition.classify}">selected</c:if>>B</option>
                            <option value="C" <c:if test="${'C' eq depotPosition.classify}">selected</c:if>>C</option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="capacityRfidAmount">托盘容量：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="capacityRfidAmount" name="capacityRfidAmount"
                               style="width: calc(100% - 70px);"
                               value="${depotPosition.capacityRfidAmount}"
                               placeholder="托盘容量">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="capacityWeight">重量容量：</label>
                <div class="controls">
                    <div class="input-append span12required">
                        <input type="text" class="span11" id="capacityWeight" name="capacityWeight"
                               style="width: calc(100% - 44px);"
                               value="${depotPosition.capacityWeight}"
                               placeholder="重量容量">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="length">长（m）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="length" name="length" style="width: calc(100% - 70px);"
                               value="${depotPosition.length}"
                               placeholder="长（m）">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="width">宽（m）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="width" name="width" style="width: calc(100% - 70px);"
                               value="${depotPosition.width}"
                               placeholder="宽（m）">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="startX">起始x轴（左下）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="startX" name="startX" style="width: calc(100% - 70px);"
                               value="${depotPosition.startX}"
                               placeholder="起始x轴（左下）">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="startY">起始y轴（左下）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="startY" name="startY" style="width: calc(100% - 70px);"
                               value="${depotPosition.startY}"
                               placeholder="起始y轴（左下）">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="endX">终点x轴（右上）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="endX" name="endX" style="width: calc(100% - 70px);"
                               value="${depotPosition.endX}"
                               placeholder="终点x轴（右上）">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="endY">终点y轴（右上）：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="endY" name="endY" style="width: calc(100% - 70px);"
                               value="${depotPosition.endY}"
                               placeholder="终点y轴（右上）">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="remark">备注：</label>
                <div class="controls">
                    <div class="input-append span12">
                    <textarea type="text" class="span11" id="remark" name="remark" value="${depotPosition.remark}"
                              style="width: calc(100% - 44px);"
                              placeholder="备注">${depotPosition.remark}</textarea>
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
            /*库位编码*/
            "positionCode": {
                required: true,
                maxlength: 16,
                <c:if test="${depotPosition.id==null}">
                remote: context_path + "/depotPositionManage/hasC?id=" + $('#id').val()
                </c:if>
            },
            /*库位名称*/
            "positionName": {
                required: true,
                maxlength: 20
            },
            /*所属库区*/
            "depotareaId": {
                required: true,
                maxlength: 20
            },
            /*托盘容量*/
            "capacityRfidAmount": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*长（m）*/
            "length": {
                required: true,
                number: true,
                maxlength: 16
            },
            /* 宽（m）*/
            "width": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*起始x轴（左下）*/
            "startX": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*起始y轴（左下）*/
            "startY": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*终点x轴（右上）*/
            "endX": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*终点y轴（右上）*/
            "endY": {
                required: true,
                number: true,
                maxlength: 16
            },
            /*库位类型*/
            "positionType": {
                required: true,
                maxlength: 20
            },
            /*混放类型*/
            "blendType": {
                required: true,
                maxlength: 20
            },
            /*ABC分类*/
            "classify": {
                required: true,
                maxlength: 20
            },
            /*备注*/
            "remark": {
                maxlength: 200
            }
        },
        messages: {
            "positionCode": {
                required: "请输入库位编号",
                remote: "您输入的库位编码已经存在，请重新输入！"
            },
            "positionName": {
                required: "请输入库位名称"
            },
            "depotareaId": {
                required: "请选择所属库区"
            },
            "positionType": {
                required: "请选择库位类型"
            },
            "blendType": {
                required: "请选择混放类型"
            },
            "classify": {
                required: "请选择分类"
            },
            "capacityRfidAmount": {
                required: "请输入托盘容量"
            },
            /*重量容量*/
            "capacityWeight": {
                required: "请输入重量容量"
            },
            "remark": {
                maxlength: "请输入少于200个字符"
            },
            /*长（m）*/
            "length": {
                required: "请输入长度"
            },
            /* 宽（m）*/
            "width": {
                required: "请输入宽度"
            },
            "startX": {
                required: "请输入起始x轴"
            },
            "startY": {
                required: "请输入起始y轴"
            },
            "endX": {
                required: "请输入终点x轴"
            },
            "endY": {
                required: "请输入终点x轴"
            },
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
                url: context_path + "/depotPositionManage/addDepotPosition?tm=" + new Date(),
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