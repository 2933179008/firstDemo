<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <div id="mbrd"></div>
        <hr size="5px" noshade=false />
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
    //隐藏弹出框的关闭按钮
    $('.layui-layer-close1').css('display', 'none');

    //轮询
    interval3 = window.setInterval(sureRfid, 2000);

    function sureRfid() {
        $.ajax({
            type: "POST",
            url: context_path + "/instorageSlab/getExecuteRfid",
            dataType: 'json',
            cache: false,
            success: function (data) {
                var con = "";
                if (data.result) {
                    for (var rfid in data.mapList) {
                        // alert(rfid);
                        con += " <div class=\"row\" style=\"margin:0;padding:0;\">";
                        con += " <div class=\"control-group span6\" style=\"display: inline\">";
                        con += " <span style=\"color: red;font-weight: bold;font-size: 20px;\">&nbsp;&nbsp;" + rfid + ":" + "</span>";
                        con += " </div>";
                        con += " </div>";
                        var list = data.mapList[rfid];
                        for (var i = 0; i < list.length; i++) {
                            // alert(data.mapList[rfid][i]["materiel_code"]);
                            con += "  <div class=\"row\" style=\"margin:0;padding:0;\">";
                            con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                            con += "  <label class=\"control-label\">物料编号：</label>";
                            con += "  <div class=\"controls\">";
                            con += "  <div class=\"span12\" style=\" float: none !important;\">";
                            con += "  <input class=\'span10 required\' type=\'text\' value=\' " + data.mapList[rfid][i]["materiel_code"] + " \'>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                            con += "  <label class=\"control-label\">物料名称：</label>";
                            con += "  <div class=\"controls\">";
                            con += "  <div class=\"span12\" style=\" float: none !important;\">";
                            con += "  <input class=\'span10 required\' type=\'text\' value=\'" + data.mapList[rfid][i]["materiel_name"] + "\'>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  <div class=\"row\" style=\"margin:0;padding:0;\">";
                            con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                            con += "  <label class=\"control-label\">数量：</label>";
                            con += "  <div class=\"controls\">";
                            con += "  <div class=\"span12\" style=\" float: none !important;\">";
                            con += "  <input class=\'span10 required\' type=\'text\' value=\' " + data.mapList[rfid][i]["amount"] + " \'>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  <div class=\"control-group span6\" style=\"display: inline\">";
                            con += "  <label class=\"control-label\">重量：</label>";
                            con += "  <div class=\"controls\">";
                            con += "  <div class=\"span12\" style=\" float: none !important;\">";
                            con += "  <input class=\'span10 required\' type=\'text\' value=\'" + data.mapList[rfid][i]["weight"] + "\'>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                            con += "  </div>";
                        }
                    }
                } else {
                    con += " <div class=\"row\" style=\"margin:0;padding:0;\">";
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
            url: context_path + "/instorageSlab/updateAlert",
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