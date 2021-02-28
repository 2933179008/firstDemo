<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="alarmDeploy" class="form-horizontal" target="_ifr">
        <input type="hidden" name="id" id="id" value="${alarmDeploy.id}">
        <div class="control-group">
            <label class="control-label" for="alarmType">预警类型：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <select id="alarmType" name="alarmType" style="width: calc(100% - 95px);">
                        <option value="">--请选择--</option>
                        <c:if test="${alarmDeploy.id!=null}">
                            <c:forEach var="type" items="${map}">
                                <c:if test="${type.key eq alarmDeploy.alarmType}">
                                    <option value="${type.key}"
                                            <c:if test="${type.key eq alarmDeploy.alarmType}">selected</c:if>>${type.value}</option>
                                </c:if>
                            </c:forEach>
                        </c:if>
                        <c:if test="${alarmDeploy.id == null}">
                            <c:forEach var="alarmType" items="${typeMap}">
                                <option value="${alarmType.key}"
                                        <c:if test="${alarmType.key eq alarmDeploy.alarmType}">selected</c:if>>${alarmType.value}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="addressesBys">发送人：</label>
            <div class="controls">
                <div class="span12">
                    <input class="span11" type="text" name="addressesBys" id="addressesBys"
                           style="width: calc(100% - 95px);">
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="sendType">发送方式：</label>
            <div class="controls">
                <div class="input-append span12 ">
                    <select id="sendType" name="sendType" style="width: calc(100% - 95px);">
                        <option value="">--请选择--</option>
                        <option value="0"
                                <c:if test="${'0' eq alarmDeploy.sendType}">selected</c:if> >短信
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq alarmDeploy.sendType}">selected</c:if> >邮件
                        </option>
                        <option value="0,1"
                                <c:if test="${'0,1' eq alarmDeploy.sendType}">selected</c:if> >短信&邮件
                        </option>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="sendContent">发送内容：</label>
            <div class="controls">
                <div class="input-append span12 ">
                    <textarea type="text" class="span11" id="sendContent" name="sendContent"
                              placeholder="发送内容" style="width: calc(100% - 95px);">${alarmDeploy.sendContent}</textarea>
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
    $("#alarmDeploy").validate({
        rules: {
            /*预警类型*/
            "alarmType": {
                required: true
            }
        },
        messages: {
            "alarmType": {
                required: "请选择预警类型"
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

    // 发送人
    $("#addressesBys").select2({
        placeholder: "--请选择--",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        multiple: true,
        ajax: {
            url: context_path + "/alarmDeploy/getSelectUser",
            type: "POST",
            dataType: "json",
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    baseStationName: term, //联动查询的字符
                    addressesBys: '${alarmDeploy.addressesBys}'
                }
            },
            results: function (data, pageNo) {
                var res = data.result;
                if (res.length > 0) { //如果没有查询到数据，将会返回空串
                    var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
                    return {
                        results: res,
                        more: more
                    };
                } else {
                    return {
                        results: {}
                    };
                }
            },
            cache: true
        }
    });

    // 修改时加载月台和基站信息
    if ('${alarmDeploy.id}') {

        // 用户赋值
        $.ajax({
            url: context_path + "/alarmDeploy/getUserInfo",
            type: "post",
            dataType: "JSON",
            data: {
                addressesBys: '${alarmDeploy.addressesBys}'
            },
            async: false,
            success: function (data) {
                if (data) {
                    var obj = [];
                    for (let i = 0; i < data.length; i++) {
                        obj.push({
                            id: data[i].userId,
                            text: data[i].name
                        });
                    }
                    $("#addressesBys").select2("data", obj);
                }
            }
        });
    }


    /**
     * 保存/修改
     * @param jsonParam
     */
    function saveTaskDetail() {
        var roleParam = $("#alarmDeploy").serialize();
        if ($("#alarmDeploy" +
            "").valid()) {
            $(".savebtn").attr("disabled", "disabled");
            $.ajax({
                url: context_path + "/alarmDeploy/addAlarmDeploy?tm=" + new Date(),
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