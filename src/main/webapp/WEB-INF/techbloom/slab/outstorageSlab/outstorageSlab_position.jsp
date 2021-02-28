<%--
  Created by IntelliJ IDEA.
  User: HQKS-LU
  Date: 2019/3/22
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <br/>
        <div class="row" style="margin:0;padding:0;">
            <label style="font-weight: bold;">&nbsp;&nbsp;托一 RFID绑定信息：</label>
        </div>
        <hr>
        <input type="hidden" id="id" name="id">
        <input type="hidden" id="lowerId" name="lowerId">
        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialCode">物料编号：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="materialCode" name="materialCode">

                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialName">物料名称：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="materialName" name="materialName">

                    </div>
                </div>
            </div>

        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialCode">数量：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="amount" name="amount">

                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialName">重量：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="weight" name="weight">

                    </div>
                </div>
            </div>

        </div>

        <%--<div class="row" style="margin:0;padding:0;">--%>
            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label" for="materialCode">占用数量：</label>--%>
                <%--<div class="controls">--%>
                    <%--<div class="span12" style=" float: none !important;">--%>
                        <%--<input class="span10 required" type="text" id="occupyStockAmount" name="occupyStockAmount">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label" for="materialName">占用重量：</label>--%>
                <%--<div class="controls">--%>
                    <%--<div class="span12" style=" float: none !important;">--%>
                        <%--<input class="span10 required" type="text" id="occupyStockWeight" name="occupyStockWeight">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

        <%--</div>--%>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionId">库位：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="positionId" name="positionId">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="rfid">rfid编号：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="rfid" name="rfid">

                    </div>
                </div>
            </div>
        </div>
        <hr/>
        <div class="row" style="margin:0;padding:0;">
            <label style="font-weight: bold;">&nbsp;&nbsp;托二 RFID绑定信息：</label>
        </div>
        <hr>
        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialCode1">物料编号：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="materialCode1" name="materialCode1">

                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialName1">物料名称：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="materialName1" name="materialName1">

                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialCode">数量：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="amount1" name="amount1">

                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="materialName">重量：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="weight1" name="weight1">

                    </div>
                </div>
            </div>

        </div>

        <%--<div class="row" style="margin:0;padding:0;">--%>
            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label" for="materialCode">占用数量：</label>--%>
                <%--<div class="controls">--%>
                    <%--<div class="span12" style=" float: none !important;">--%>
                        <%--<input class="span10 required" type="text" id="occupyStockAmount1" name="occupyStockAmount1">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

            <%--<div class="control-group span6" style="display: inline">--%>
                <%--<label class="control-label" for="materialName">占用重量：</label>--%>
                <%--<div class="controls">--%>
                    <%--<div class="span12" style=" float: none !important;">--%>
                        <%--<input class="span10 required" type="text" id="occupyStockWeight1" name="occupyStockWeight1">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

        <%--</div>--%>

        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionId1">库位：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="positionId1" name="positionId1">
                    </div>
                </div>
            </div>

            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="rfid1">rfid编号：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span10 required" type="text" id="rfid1" name="rfid1">

                    </div>
                </div>
            </div>
        </div>
        <hr/>
    </form>
    <div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
        <span class="savebtn btn btn-success" id="formSave">确定</span>
    </div>
</div>
<script type="text/javascript">
    $("#formSave").click(function () {
        $.ajax({
            url: context_path + "/outstorageSlab/confirmState",
            type: "post",
            data: {lowerId: $("#id").val(), rfid: $("#rfid").val(), rfid1: $("#rfid1").val()},
            dataType: "JSON",
            success: function (data) {
                if (data.result) {
                    //关闭页面
                    layer.closeAll();
                    layer.msg(data.msg, {icon: 1, time: 1200});
                } else {
                    layer.msg(data.msg, {icon: 2, time: 1200});
                }
            }
        })
    })

    // function ss() {
    //     if ("WebSocket" in window) {
    //         alert("您的浏览器支持 WebSocket!");
    //         //打开websocket
    //         var ws = new WebSocket("ws://localhost:8082/dyyl/webSocket");
    //         ws.onopen = function () {
    //             // Web Socket 已连接上，使用 send() 方法发送数据
    //             ws.send("");
    //         }
    //         //返回数据
    //         ws.onmessage = function (evt) {
    //             var received_msg = evt.data;
    //             alert(received_msg);
    //             // ws.onclose();
    //         }
    //     } else {
    //         debugger;
    //         //浏览器不支持 WebSocket
    //         alert("您的浏览器不支持 WebSocket!");
    //     }
    // }

    outStorageSlab_interval1 = window.setInterval(pollingBackground, 2000);

    function pollingBackground() {
        $.ajax({
            type: "POST",
            url: context_path + "/outstorageSlab/getSlabExecuteState",
            dataType: 'json',
            cache: false,
            success: function (data) {
                if (data.result) {
                    if (data.code == "0") {
                        layer.msg(data.msg, {icon: 2, time: 1200});
                    } else {
                        var lowerId = data.taskMap.lower_id;
                        var rfid = data.taskMap.rfid;
                        var id = data.taskMap.id;
                        $("#lowerId").val(lowerId);
                        $("#id").val(id);
                        var state = data.taskMap.state;
                        if (state == 2) {
                            //将定时器关闭,关闭当前页面
                            window.clearInterval(outStorageSlab_interval1);
                            //将页面关闭
                            layer.closeAll();
                            return;
                        }
                        $.ajax({
                            type: "POST",
                            url: context_path + "/outstorageSlab/getAllValue",
                            data: {lowerId: lowerId, rfid: rfid, id: id},
                            dataType: 'json',
                            cache: false,
                            success: function (data) {
                                $("#materialCode").val(data.materialCode0);
                                $("#materialName").val(data.materialName0);
                                $("#amount").val(data.amount0);
                                $("#weight").val(data.weight0);
                                $("#occupyStockAmount").val(data.occupyStockAmount0);
                                $("#occupyStockWeight").val(data.occupyStockWeight0);
                                $("#positionId").val(data.positionId0);
                                $("#rfid").val(data.rfid0);
                                $("#materialCode1").val(data.materialCode1);
                                $("#materialName1").val(data.materialName1);
                                $("#amount1").val(data.amount1);
                                $("#weight1").val(data.weight1);
                                $("#occupyStockAmount1").val(data.occupyStockAmount1);
                                $("#occupyStockWeight1").val(data.occupyStockWeight1);
                                $("#positionId1").val(data.positionId1);
                                $("#rfid1").val(data.rfid1);
                            }
                        })
                    }
                } else {
                    layer.msg(data.msg, {icon: 2, time: 2000});
                }
            }
        })
    }
</script>

