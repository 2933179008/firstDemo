<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
    <form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${depotArea.id}">

        <div class="control-group">
            <label class="control-label" for="areaCode">库区编码：</label>
            <div class="controls">
                <c:if test="${depotArea.id==null}">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="areaCode" name="areaCode" placeholder="库区编码"

                              value="${areaCode}"/>
                    </div>
                </c:if>
                <c:if test="${depotArea.id!=null}">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" name="areaCode" value="${depotArea.areaCode}"
                               placeholder="库区编码" readonly="true">
                    </div>
                </c:if>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="areaName">库区名称：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="areaName" name="areaName" value="${depotArea.areaName}"
                           placeholder="库区名称">
                </div>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label" for="positionAmount">库位数（个）：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="positionAmount" name="positionAmount"
                           value="${depotArea.positionAmount}" placeholder="库位数">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="areaType">库区类别：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <select id="areaType" name="areaType" style="width:91%;">
                        <option value="">请选择</option>
                        <option value="0"
                                <c:if test="${'0' eq depotArea.areaType}">selected</c:if> >收货区
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq depotArea.areaType}">selected</c:if> >发货区
                        </option>
                        <option value="2"
                                <c:if test="${'2' eq depotArea.areaType}">selected</c:if> >良品存储区
                        </option>
                        <option value="3"
                                <c:if test="${'3' eq depotArea.areaType}">selected</c:if> >虚拟存储区
                        </option>
                        <option value="4"
                                <c:if test="${'4' eq depotArea.areaType}">selected</c:if> >退货存储区
                        </option>
                        <option value="5"
                                <c:if test="${'5' eq depotArea.areaType}">selected</c:if> >不良品存储区
                        </option>

                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="xsizeStart">X轴起始坐标：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="xsizeStart" name="xsizeStart"
                           value="${depotArea.xsizeStart}" placeholder="X轴起始坐标">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="ysizeStart">Y起始坐标：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="ysizeStart" name="ysizeStart"
                           value="${depotArea.ysizeStart}" placeholder="Y起始坐标">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="xsizeEnd">X轴终点坐标：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="xsizeEnd" name="xsizeEnd"
                           value="${depotArea.xsizeEnd}" placeholder="X轴终点坐标">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="ysizeEnd">Y轴终点坐标：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="ysizeEnd" name="ysizeEnd"
                           value="${depotArea.ysizeEnd}" placeholder="Y轴终点坐标">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="remark">备注：</label>
            <div class="controls">
                <div class="input-append span12required">
                    <textarea type="text" class="span11" id="remark" name="remark" value="${depotArea.remark}"
                              placeholder="备注">${depotArea.remark}</textarea>
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
            /*库区编码*/
            "areaCode": {
                required: true,
                maxlength: 16,
                <c:if test="${depotArea.id==null}">
                remote: context_path + "/erpDepotAreaManage/hasC?id=" + $('#id').val()
                </c:if>
            },
            /*库区名称*/
            "areaName": {
                required: true,
                maxlength: 20
            },
            /*库位数*/
            "positionAmount": {
                required: true,
                number: true,
                maxlength: 20
            },
            /*库区类别*/
            "areaType": {
                required: true,
                maxlength: 16
            },
            /*X轴起始坐标*/
            "xsizeStart": {
                required: true,
                number: true,
                maxlength: 20
            },
            /*Y起始坐标*/
            "ysizeStart": {
                required: true,
                number: true,
                maxlength: 20
            },
            /*X轴终点坐标*/
            "xsizeEnd": {
                required: true,
                number: true,
                maxlength: 20
            },
            /*Y轴终点坐标*/
            "ysizeEnd": {
                required: true,
                number: true,
                maxlength: 20
            },
            /*备注*/
            "remark": {
                maxlength: 200
            }
        },
        messages: {
            "areaCode": {
                required: "请输入库区编号",
                remote: "您输入的库区编码已经存在，请重新输入！"
            },
            "areaName": {
                required: "请输入库区名称"
            },
            "xsizeStart": {
                required: "请输入X轴起始坐标"
            },
            "ysizeStart": {
                required: "请输入Y轴起始坐标"
            },
            "xsizeEnd": {
                required: "请输入X轴终点坐标"
            },
            "ysizeEnd": {
                required: "请输入Y轴终点坐标"
            },
            "positionAmount": {
                required: "请输入库位数"
            },
            "areaType": {
                required: "请输入库区类别"
            },
            "remark": {
                maxlength: "请输入少于200个字符"
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
                url: context_path + "/erpDepotAreaManage/addDepotArea?tm=" + new Date(),
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
                        layer.msg("操作失败！", {icon: 2});
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