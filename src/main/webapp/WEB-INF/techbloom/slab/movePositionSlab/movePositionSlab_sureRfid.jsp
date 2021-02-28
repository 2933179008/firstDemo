<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <%--<form id="baseInfor" class="form-horizontal" target="_ifr">--%>
    <%--<div class="row" style="margin:0;padding:0;">--%>
    <%--rfid：<input type="text" id="rfid"/></br>--%>
    <%--移出库位：<input type="text" id="outPositionCode"/>--%>
    <%--</div>--%>
    <div id="mbrd"></div>
    <div style="margin-left:170px;">
            <span class="btn btn-info" onclick="saverfid()">
		        <i class="ace-icon fa fa-check bigger-110"></i>确定
            </span>
        <span class="btn btn-danger" onclick="cancel()">
               <i class="icon-remove"></i>&nbsp;取消
            </span>
    </div>
    </form>
</div>
<script type="text/javascript">
    var interval3 = window.setInterval(sureRfid, 2000);

    function sureRfid() {
        $.ajax({
            type: "POST",
            url: context_path + "/movePositionSlab/getExecuteRfid",
            dataType: 'json',
            cache: false,
            success: function (data) {
                // if(data.result){
                //     var rfid = data.rfid;
                //     var outPositionCode = data.outPositionCode;
                //     $("#rfid").val(rfid);
                //     $("#outPositionCode").val(outPositionCode);
                // }
                var con = "";
                if (data.result) {
                    // alert(rfid);
                    var rfid = data.rfid;
                    var outPositionCode = data.outPositionCode;
                    con += " <div class=\"row\" style=\"margin-left:15px;margin-top:10px\">";
                    con += " <div class=\"control-group span6\" style=\"display: inline\">";
                    con += " <span style=\"color: red;font-weight: bold;font-size: 20px;\">&nbsp;&nbsp;" + rfid + ":" + "</span>";
                    con += " </div>";
                    con += " </div>";
                    var list = data.materielBindRfidDetailList;
                    // alert(list.length)
                    for (var i = 0; i < list.length; i++) {
                        // alert(data.mapList[rfid][i]["materiel_code"]);
                        con += "  <div class=\"row\" style=\"margin-left:40px;margin-top:0px\">";
                        con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                        con += "  <label class=\"control-label\">物料编号：</label>";
                        con += "  <div class=\"controls\">";
                        con += "  <div class=\"span12\" style=\" float: none !important;\">";
                        con += "  <input class=\'span10 required\' type=\'text\' value=\' " + list[i]["materielCode"] + " \'>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                        con += "  <label class=\"control-label\">物料名称：</label>";
                        con += "  <div class=\"controls\">";
                        con += "  <div class=\"span12\" style=\" float: none !important;\">";
                        con += "  <input class=\'span10 required\' type=\'text\' value=\'" + list[i]["materielName"] + "\'>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  <div class=\"row\" style=\"margin-left:40px;margin-top:0px\">";
                        con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                        con += "  <label class=\"control-label\">数量：</label>";
                        con += "  <div class=\"controls\">";
                        con += "  <div class=\"span12\" style=\" float: none !important;\">";
                        con += "  <input class=\'span10 required\' type=\'text\' value=\' " + list[i]["amount"] + " \'>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                        con += "  <label class=\"control-label\">重量：</label>";
                        con += "  <div class=\"controls\">";
                        con += "  <div class=\"span12\" style=\" float: none !important;\">";
                        con += "  <input class=\'span10 required\' type=\'text\' value=\'" + list[i]["weight"] + "\'>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        con += "  </div>";
                        if (i+1 == list.length) {
                            con += "<hr size=\"5px\" noshade=false />";
                        }else {
                            con += "<hr size=\"1px\" noshade=false />";
                        }
                    }
                    con += " <div class=\"row\" style=\"margin-left:40px;margin-top:0px\">";
                    con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                    con += "  <label class=\"control-label\">移出库位：</label>";
                    con += "  <div class=\"controls\">";
                    con += "  <div class=\"span12\" style=\" float: none !important;\">";
                    con += "  <input class=\'span10 required\' type=\'text\' value=\' " + outPositionCode + " \'>";
                    con += "  </div>";
                    con += "  </div>";
                    con += "  </div>";
                    con += " </div>";
                } else {
                    con += " <div class=\"row\" style=\"margin-left:40px;margin-top:0px\">";
                    con += " <div class=\"control-group span6\" style=\"display: inline\">";
                    con += " <span style=\"color: red;font-weight: bold;font-size: 20px;margin-left:80px;\">&nbsp;&nbsp;" + data.msg() + ":" + "</span>";
                    con += " </div>";
                    con += " </div>";
                }
                $("#mbrd").html(con);
            }
        });
    }

    //更新平板入库操作参数绑定关系表的字段
    function updateAlert(flag) {
        $.ajax({
            type: "POST",
            url: context_path + "/movePositionSlab/updateAlert",
            data: {flag: flag},
            dataType: 'json',
            cache: false,
            success: function (data) {
            }
        });
    }

    function saverfid() {
        updateAlert("Y");
        layer.closeAll();
    }

    function cancel() {
        updateAlert("N");
        interval1 = window.setInterval(pollingBackground, 2000);
        layer.closeAll();
    }

</script>