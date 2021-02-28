<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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


<style>
    .docs_example {
        margin-right: 0px;
        margin-left: 0;
        background-color: #fff;
        border-color: #ddd;
        border-width: 1px;
        border-radius: 4px;
        -webkit-box-shadow: none;
        box-shadow: none;
        position: relative;
        padding: 8px 25px 8px;
        border-style: solid;
        margin-bottom: 10px;
        margin-top: 10px;
    }
    .highlight{
        margin-top: -1px;
        margin-right: 0;
        margin-left: 0;
        border-width: 1px;
        border-bottom-right-radius: 4px;
        border-bottom-left-radius: 4px;
        border: 1px solid #e1e1e8;
        margin-bottom: 14px;
        padding: 8px 10px 8px;
    }
    .float_left{
        float: left;
        width: 100%;
    }
    .row_nomargin{
        margin-right: 15px;
        margin-left: 15px;
        padding: 0px 10px;
    }
    .row{
        font-weight: 550;
    }
</style>


<body>
<div  class="container-fluid">
    <!--<h3>Carset Lbale G38/ G38 PHEV - K01 Carset Front</h3>-->
    <div id="t1" class=" row docs_example " style="margin-top: 10px;">
        <span class="">
             任务单号：${task_code}
        </span>
        <%--<span style="float: right;"></span>--%>
    </div>
    <%--<div id="t2" class=" row row_nomargin">--%>
    <%--<span class="">过点时间：</span>--%>
    <%--<span style="float: right;">OrderNo:</span>--%>
    <%--</div>--%>
    <div id="t3" class=" row row_nomargin" style="margin-bottom: 10px;">
        <%--<span style="float: left;">--%>
        <%--<span class="float_left">车型:</span>--%>
        <%--<span class="float_left">VIN:</span>--%>
        <%--&lt;%&ndash;<span class="float_left">过点时间： ${task.triger_time}</span>&ndash;%&gt;--%>
        <%--</span>--%>
        <span style="float: right; display: grid;text-align: right; text-align: -webkit-right;margin-top: 3px;" >
            <img src="<%=basePath%>inventoryTask/generateBarcode?msg=${task_code}" style="width: 150px;"/>
        </span>
    </div>
    <table id="t4" class="table table-bordered">
        <thead>
        <tr>
            <th>序号</th><th>物料编码</th><th>物料名称</th><th>批次号</th><th>库位</th><th>盘点数量</th><th>库存数量</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${taskDetailList}" var="detail" varStatus="vs">
            <tr>
                <td style="height:39.2px">${vs.index + 1}</td>
                <td style="height:39.2px" align = "left">${detail.materialCode}</td>
                <td style="height:39.2px" align = "left">${detail.materialName}</td>
                <td style="height:39.2px" align = "left">${detail.batchNo}</td>
                <td style="height:39.2px" align = "left">${detail.positionCode}</td>
                <td style="height:39.2px" align = "left">${detail.inventoryAmount}</td>
                <td style="height:39.2px" align = "left">${detail.stockAmount}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
