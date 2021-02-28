<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <input type="hidden" id="id" name="id" value="${movePosition.id}">

        <%--移位类型--%>
        <div class="control-group">
            <label class="control-label" for="movePositionType">移位类型：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <select name="movePositionType" id="movePositionType" class="span11">
                        <option value="0"
                                <c:if test="${'0' eq movePosition.movePositionType}">selected</c:if> >散货移位
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq movePosition.movePositionType}">selected</c:if> >整货移位
                        </option>
                    </select>
                </div>
            </div>
        </div>

        <%--移库单号--%>
        <div class="control-group">
            <div id="rfidDiv">
                <label class="control-label" for="rfid">移位RFID：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" id="rfid" name="rfid" value="${movePosition.rfid}"
                               placeholder="移位RFID" <c:if test="${error!=null}">readonly="readonly"</c:if>>
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <div id="formerPositionDiv">
                <label class="control-label" for="formerPosition">移出库位：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <select name="formerPosition" id="formerPosition" class="span11"
                                <c:if test="${error!=null}">readonly="readonly"</c:if>>
                            <option value="">--请选择--</option>
                            <c:forEach var="position" items="${depotPositionList}">
                                <option value="${position.id}"
                                        <c:if test="${position.id==movePosition.formerPosition}">selected</c:if>>${position.positionName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <div id="materielCodeDiv">
                <label class="control-label" for="materielCode">物料编码：</label>
                <div class="controls">
                    <div class="span12 required" style=" float: none !important;">
                        <select name="materielCode" id="materielCode" class="span11 select2_input" onclick="getName()"
                                <c:if test="${error!=null}">readonly="readonly"</c:if>>
                            <option value="">--请选择--</option>
                            <c:forEach var="materiel" items="${materielList}">
                                <option value="${materiel.materielCode}"
                                        <c:if test="${materiel.materielCode==movePosition.materielCode}">selected</c:if>>${materiel.materielCode}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <div id="materielNameDiv">
                <label class="control-label" for="materielName">物料名称：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" id="materielName" name="materielName" class="span11" readonly="true"
                               value="${movePosition.materielName}"
                               placeholder="依据物料编码自动赋值">
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <div id="batchNoDiv">
                <label class="control-label" for="batchNo">批次号：</label>
                <div class="controls">
                    <div class="span12 required" style=" float: none !important;">
                        <select name="batchNo" id="batchNo" class="span11 select2_input"
                                <c:if test="${error!=null}">readonly="readonly"</c:if>>
                            <option value="">--请选择--</option>
                            <c:forEach var="batch" items="${BatchNoList}">
                                <option value="${batch.batchNo}"
                                        <c:if test="${batch.batchNo==movePosition.batchNo}">selected</c:if>>${batch.batchNo}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <div id="movePositionAmountDiv">
                <label class="control-label" for="movePositionAmount">移位数量：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" id="movePositionAmount" name="movePositionAmount" class="span11"
                               value="${movePosition.movePositionAmount}"
                               placeholder="移位数量">
                    </div>
                </div>
            </div>
        </div>


        <div class="control-group">
            <div id="movePositionWeightDiv">
                <label class="control-label" for="movePositionWeight">移位重量：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" id="movePositionWeight" name="movePositionWeight" class="span11"
                               value="${movePosition.movePositionWeight}"
                               placeholder="移位重量">
                    </div>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="positionBy">移入库位：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <select name="positionBy" id="positionBy" class="span11"
                            <c:if test="${error!=null}">readonly="readonly"</c:if>>
                        <option value="">--请选择--</option>
                        <c:forEach var="position" items="${depotPositionList}">
                            <option value="${position.id}"
                                    <c:if test="${position.id==movePosition.positionBy}">selected</c:if>>${position.positionName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="moveUserId">移库人员：</label>
            <div class="controls">
                <div class="input-append span12required">
                    <select name="moveUserId" id="moveUserId" class="span11">
                        <option value="">--请选择--</option>
                        <c:forEach var="user" items="${userList}">
                            <option value="${position.moveUserId}"
                                    <c:if test="${user.userId==uposition.moveUserId}">selected</c:if>>${user.username}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="remarks">备注：</label>
            <div class="controls">
                <div class="input-append span12">
                    <textarea type="text" class="span11" id="remarks" name="remarks" placeholder="备注"
                              style="width: calc(100% - 25px);"
                              <c:if test="${error!=null}">readonly="readonly"</c:if>>${movePosition.remarks}</textarea>
                </div>
            </div>
        </div>

        <c:if test="${error!=null}">
            <div class="control-group">
                <label class="control-label" for="rfid">注意：</label>
                <div class="controls">
                    <div class="input-append span12 required">
                        <input type="text" class="span11" value="${error}" style="color: #d8120e"
                               <c:if test="${error!=null}">readonly="readonly"</c:if>>
                    </div>
                </div>
            </div>
        </c:if>
    </form>

    <div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
        <c:if test="${error==null}">
            <span class="savebtn btn btn-success" onclick="saveTaskDetail()">提交</span>
        </c:if>
        <span class="btn btn-danger" onclick="closeWindow()">取消</span>
    </div>
</div>
<script type="text/javascript">

    var context_path = '<%=path%>';
    $("#baseInfor").validate({
        rules: {
            /*移位类型*/
            "movePositionType": {
                required: true,
                maxlength: 1
            },
            /*移位RFID*/
            "rfid": {
                required: true,
                maxlength: 20,
                remote: context_path + "/movePosition/hasC?id=" + $('#id').val()
            },
            /*移出库位*/
            "formerPosition": {
                required: true,
                maxlength: 20
            },
            /*移入库位*/
            "positionBy": {
                required: true,
                maxlength: 20
            },
            /*物料编码*/
            "materielCode": {
                required: true,
                maxlength: 20
            },
            /*批次号*/
            "batchNo": {
                required: true,
                maxlength: 20
            },
            /*移位数量*/
            "movePositionAmount": {
                required: true,
                maxlength: 20
            },
            /*移位重量*/
            "movePositionWeight": {
                required: true,
                maxlength: 20
            },
            /*备注*/
            "remarks": {
                maxlength: 150
            }
        },
        messages: {
            /*移位类型*/
            "movePositionType": {
                required: "请选择移位类型"
            },
            "rfid": {
                required: "请输入RFID",
                remote: "您输入的RFID不存在，请重新输入！"
            },
            "formerPosition": {
                required: "请选择移出库位"
            },
            "positionBy": {
                required: "请选择移入库位"
            },
            "materielCode": {
                required: "请输入物料编码"
            },
            "batchNo": {
                required: "请输入批次号"
            },
            "movePositionAmount": {
                required: "请输入移位数量"
            },
            /*移位重量*/
            "movePositionWeight": {
                required: "请输入移位重量"
            },
            "remarks": {
                maxlength: "您最多可以输入150个字符"
            }


        },
        errorClass:
            "help-inline",
        errorElement:
            "span",
        highlight:

            function (element, errorClass, validClass) {
                $(element).parents('.control-group').addClass('error');
            }

        ,
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
        }
    });

    $("#baseInfor #materielCode").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#baseInfor #materielCode").on("change.select2", function () {
            $("#baseInfor #materielCode").trigger("keyup")
        }
    );

    $("#baseInfor #batchNo").select2({
        minimumInputLength: 0,
        placeholderOption: "first",
        allowClear: true,
        delay: 250,
        //  width:200,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！"
    });
    $("#baseInfor #batchNo").on("change.select2", function () {
            $("#baseInfor #batchNo").trigger("keyup")
        }
    );

    //获取库位名称
    function getName() {
        var materielCodeVal = $("#baseInfor #materielCode").val();
        if (materielCodeVal != null) {
            $.ajax({
                url: context_path + "/movePosition/getMaterielName?materielCode=" + materielCodeVal,
                type: "post",
                dataType: "JSON",
                success: function (data) {
                    if (data != null) {
                        $("#baseInfor #materielName").val(data.materielName)
                    }
                }
            });
        }
    }

    /**
     * 保存/修改
     * @param jsonParam
     */
    function saveTaskDetail() {

        var movePositionTypeVal = $("#baseInfor #movePositionType").val();
        if (movePositionTypeVal == 0) { //散货移位
            $('#baseInfor #rfid').rules("remove");
            $('#baseInfor #movePositionAmount').rules('add', {required: true});

        } else if (movePositionTypeVal == 1) { //整货移位

            $('#baseInfor #formerPosition').rules("remove");
            $('#baseInfor #materielCode').rules("remove");
            $('#baseInfor #batchNo').rules("remove");
            $('#baseInfor #movePositionAmount').rules("remove");
            $('#baseInfor #movePositionWeight').rules("remove");
            $('#baseInfor #rfid').rules('add', {required: true});
        }

        var roleParam = $("#baseInfor").serialize();
        if ($("#baseInfor" +
            "").valid()) {
            $(".savebtn").attr("disabled", "disabled");
            $.ajax({
                url: context_path + "/movePosition/addMovePosition",
                type: "POST",
                data: roleParam,
                dataType: "JSON",
                success: function (data) {
                    ajaxStatus = 1; //将标记设置为可请求
                    if (data.result) {
                        //刷新表格
                        $("#grid-table").jqGrid('setGridParam',
                            {
                                postData: {queryJsonString: ""} //发送数据
                            }
                        ).trigger("reloadGrid");
                        layer.msg(data.msg, {icon: 1});
                        layer.close($queryWindow);
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });
        }
    }

    //移位类型change事件
    $("#baseInfor #movePositionType").on("change", function (e) {
        //盘点类型下拉框值
        var movePositionTypeVal = $("#baseInfor #movePositionType").val();
        if (movePositionTypeVal == "0") {//散货移位
            $("#baseInfor #materielCodeDiv").attr("style", "display: inline");
            $("#baseInfor #materielNameDiv").attr("style", "display: inline");
            $("#baseInfor #formerPositionDiv").attr("style", "display: inline");
            $("#baseInfor #movePositionAmountDiv").attr("style", "display: inline");
            $("#baseInfor #batchNoDiv").attr("style", "display: inline");
            $("#baseInfor #movePositionWeightDiv").attr("style", "display: inline");
            $("#baseInfor #rfidDiv").attr("style", "display: none");
        } else if (movePositionTypeVal == "1") {//整货移位
            $("#baseInfor #materielCodeDiv").attr("style", "display: none");
            $("#baseInfor #materielNameDiv").attr("style", "display: none");
            $("#baseInfor #formerPositionDiv").attr("style", "display: none");
            $("#baseInfor #movePositionAmountDiv").attr("style", "display: none");
            $("#baseInfor #movePositionWeightDiv").attr("style", "display: none");
            $("#baseInfor #rfidDiv").attr("style", "display: inline");
            $("#baseInfor #batchNoDiv").attr("style", "display: none");
        }
    });

    //编辑页面初始化时，下拉框赋值
    if ($("#baseInfor #id").val() != "" && $("#baseInfor #id").val() != undefined && $("#baseInfor #id").val() != null) {
        //入库类型下拉框值
        var inventoryTypeVal = $("#baseInfor #movePositionType").val();
        if (inventoryTypeVal == "0") {//散货盘点
            $("#baseInfor #rfidDiv").attr("style", "display: none");
        } else {//整货盘点
            $("#baseInfor #materielCodeDiv").attr("style", "display: none");
            $("#baseInfor #materielNameDiv").attr("style", "display: none");
            $("#baseInfor #formerPositionDiv").attr("style", "display: none");
            $("#baseInfor #movePositionAmountDiv").attr("style", "display: none");
            $("#baseInfor #movePositionWeightDiv").attr("style", "display: none");
            $("#baseInfor #batchNoDiv").attr("style", "display: none");
        }
    } else {//添加页面初始化时，下拉框赋值
        $("#baseInfor #rfidDiv").attr("style", "display: none");
    }

    /**
     * 关闭弹出框
     * @param jsonParam
     */
    function closeWindow() {
        layer.close($queryWindow);
    }
</script>