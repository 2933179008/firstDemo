<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<style type="text/css"></style>
<div id="grid-div-c">
    <div id="fixed_tool_div-c" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table-c" style="width:100%;height:100%;"></table>
    <div id="grid-pager-c"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var _grid_c;
    $(function () {
        _grid_c = jQuery("#grid-table-c").jqGrid({
            url: context_path + "/stockQuery/getDetailList?stockId="+${stockId},
            datatype: "json",
            colNames: ["主键", "物料编码","物料名称","批次号","库位","包装","数量","重量（kg）","RFID"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "materielCode", index: "materielCode", width: 30,sortable: false},
                {name: "materielName", index: "materielName", width: 30,sortable: false},
                {name: "batchRule", index: "batchRule", width: 80,sortable: false},
                {name: "positionName", index: "positionName", width: 30,sortable: false},
                {name: "unit", index: "unit", width: 20,sortable: false},
                {name: "amount", index: "amount", width: 20,sortable: false},
                {name: "weight", index: "wegiht", width: 30,sortable: false},
                {name: "rfid", index: "rfid", width: 50,sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager-c",
            sortname: "id",
            sortorder: "desc",
            altRows: true,
            viewrecords: true,
            autowidth: true,
            multiselect: true,
            multiboxonly: true,
            loadComplete: function (data) {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                oriData = data;
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext: "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        jQuery("#grid-table-c").navGrid("#grid-pager-c", {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd("#grid-pager-c", {
            caption: "",
            buttonicon: "ace-icon fa fa-refresh green",
            onClickButton: function () {
                $("#grid-table-c").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString: ""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() );
            $("#grid-table-c").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy-c").outerHeight(true)-$("#fixed_tool_div-c").outerHeight(true)-$("#grid-pager-c").outerHeight(true)
                -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true)-30);
        });

        $(window).triggerHandler("resize.jqGrid");
    });

    // 查询
    function queryOk(){
        var queryParam = iTsai.form.serialize($("#queryForm-c"));
        querySceneByParam(queryParam);
    }

    function querySceneByParam(jsonParam){
        // 将json对象反序列化到列表页面中隐藏的form中
        iTsai.form.deserialize($("#hiddenQueryForm-c"), jsonParam);
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm-c"));
        // 将json对象转换成json字符串
        var queryJsonString = JSON.stringify(queryParam);
        // 执行查询操作
        $("#grid-table-c").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset(){
        $("#queryForm-c #materialCode").val("");
        $("#queryForm-c #materialName").val("");
        $("#grid-table-c").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }



</script>