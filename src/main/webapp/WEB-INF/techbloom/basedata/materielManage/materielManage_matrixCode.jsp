<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="<%=path%>/static/css/bootstrap.min.css">
    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="<%=path%>/static/css/bootstrap-theme.min.css">
</head>

<body>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;margin:0px;border: 0px">

    <!--startprint-->
    <div class="row" style="margin:0;padding:0;">
        <div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;" onclick="doPrint()">
            <c:forEach items="${matrixCodeList}" var="matrixCode">
                <div style="margin:0 auto;width: 500px;height: 600px">
                    <img src="data:image/png;base64,${matrixCode.binary}">
                    <table style="margin:0 auto;">
                        <tr>
                            <td>编码：</td>
                            <td>${matrixCode.materielCode}</td>
                        </tr>
                        <tr>
                            <td>名称：</td>
                            <td>${matrixCode.materielName}</td>
                        </tr>
                        <tr>
                            <td>助记码：</td>
                            <td>${matrixCode.mnemonicCode}</td>
                        </tr>
                        <tr>
                            <td>规格：</td>
                            <td>${matrixCode.spec}</td>
                        </tr>
                        <tr>
                            <td>保质期：</td>
                            <td>${matrixCode.qualityPeriod}天</td>
                        </tr>
                    </table>
                </div>
            </c:forEach>
        </div>
    </div>
    <!--endprint-->

</div>
<script type="text/javascript">

    function doPrint() {
        bdhtml = window.document.body.innerHTML;
        sprnstr = "<!--startprint-->";
        eprnstr = "<!--endprint-->";
        prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + 17);
        prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
        window.document.body.innerHTML = prnhtml;
        window.print();
    }

    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 关闭弹出框
     * @param jsonParam
     */
    function closeWindow() {
        layer.close($queryWindow);
    }
</script>
</body>
</html>