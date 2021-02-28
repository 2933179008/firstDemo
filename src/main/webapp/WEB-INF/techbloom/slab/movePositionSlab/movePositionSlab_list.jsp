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
<div id="grid-div">
    <form id="hiddenForm" action="<%=path%>/movePosition/toExcel" method="POST" style="display: none;">
        <input id="ids" name="ids" value=""/>
    </form>
    <form id="hiddenQueryForm" style="display:none;">
        <input name="movePositionCode" id="movePositionCodes" value=""/>
        <input name="rfid" id="rfids" value=""/>
        <input name="status" id="statu" value=""/>
    </form>
    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-editQx" onclick="startMoveplace();">
            新建移位<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
    </div>
    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var _grid;
    $(function () {
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id: "__tb__01",
        items: [
            <%--{label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addScene, iconClass:'glyphicon glyphicon-plus'},--%>
            <%--{label: "编辑", disabled: (${sessionUser.editQx} == 1 ? false : true), onclick: editScene, iconClass:'glyphicon glyphicon-pencil'},--%>
            <%--{label: "删除", disabled: (${sessionUser.deleteQx} == 1 ? false : true), onclick: delScene, iconClass:'glyphicon glyphicon-trash'}--%>
        ]
    });
    $(function () {
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/movePositionSlab/getList",
            datatype: "json",
            colNames: ["主键", "绑定单号", "RFID", "原库位", "移动库位", "创建时间", "开始时间", "完成时间", "移位人员", "状态", "备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "movePositionCode", index: "movePositionCode", width: 50},
                {name: "rfid", index: "rfid", width: 50},
                {name: "formerPositionName", index: "formerPositionName", width: 50},
                {name: "positionName", index: "positionName", width: 50},
                {
                    name: "moveFoundTime", index: "moveFoundTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "movePositionTime", index: "movePositionTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
                        } else {
                            return "";
                        }
                    }
                },
                {
                    name: "completeTime", index: "completeTime", width: 30,
                    formatter: function (cellValu, option, rowObject) {
                        if (cellValu != null) {
                            return getFormatDateByLong(new Date(cellValu), "yyyy-MM-dd HH:mm");
                        } else {
                            return "";
                        }
                    }
                },
                {name: "moveUserName", index: "moveUserName", width: 30, sortable: false},
                {
                    name: "status", index: "status", width: 30,
                    formatter: function (cellValue) {
                        if (cellValue == 0) {
                            return "<span style='color:#B8A608;font-weight:bold;'>未移位</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:#ACD128;font-weight:bold;'>移位中</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:#76b86b;font-weight:bold;'>已完成</span>";
                        }
                    }
                },
                {name: "remarks", index: "remarks", width: 30, sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "id",
            sortorder: "desc",
            altRows: true,
            viewrecords: true,
            autowidth: true,
            // multiselect: true,
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
        jQuery("#grid-table").navGrid("#grid-pager", {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd("#grid-pager", {
            caption: "",
            buttonicon: "ace-icon fa fa-refresh green",
            onClickButton: function () {
                $("#grid-table").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString: ""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width());
            $("#grid-table").jqGrid("setGridHeight", $(".container-fluid").height() - $("#yy").outerHeight(true) - $("#fixed_tool_div").outerHeight(true) - $("#grid-pager").outerHeight(true)
                - $("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true) - 35);
        });

        $(window).triggerHandler("resize.jqGrid");
    });

    // 查询
    function queryOk() {
        var queryParam = iTsai.form.serialize($("#queryForm"));
        querySceneByParam(queryParam);
    }

    function querySceneByParam(jsonParam) {
        // 将json对象反序列化到列表页面中隐藏的form中
        iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
        // 将json对象转换成json字符串
        var queryJsonString = JSON.stringify(queryParam);
        // 执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset() {
        $("#queryForm #movePositionCode").val("");
        $("#queryForm #rfid").val("");
        $("#queryForm #status").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: ""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //定义定时器1
    var interval1;
    //定义定时器2
    var interval2;

    // 开始移位
    function startMoveplace() {
        $.ajax({
            type : "POST",
            url:context_path + "/movePositionSlab/startMoveplace",
            dataType : 'json',
            cache : false,
            success : function(data) {
                layer.closeAll();
                if(data.result){
                    layer.msg(data.msg, {icon: 1,time:2000});
                    //开启定时器
                    interval1 = window.setInterval(pollingBackground,2000);
                    interval2 = window.setInterval(pollingBackground2,2000);
                }else{
                    layer.msg(data.msg, {icon: 2,time:2000});
                }
                _grid.trigger("reloadGrid");  //重新加载表格
            }
        });
    }

    //轮询后台，从数据库表中获取叉车执行状态
    function pollingBackground(){
        $.ajax({
            type : "POST",
            url:context_path + "/movePositionSlab/getSlabExecuteState",
            dataType : 'json',
            cache : false,
            success : function(data) {
                if(data.result){
                    if(data.code=="0"){
                        layer.msg(data.msg,{icon: 2,time:2000});
                    }else if(data.code=="1"){
                        //关闭定时器1
                        window.clearInterval(interval1);
                        $.post(context_path+"/movePositionSlab/toSureRfid", {}, function(str){
                            var $queryWindow1 = layer.open({
                                title : "托盘确认",
                                type: 1,
                                skin : "layui-layer-molv",
                                area : ["500px","400px"],
                                shade: 0.6, //遮罩透明度
                                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                                content: str,//注意，如果str是object，那么需要字符拼接。
                                success:function(layero, index){
                                    layer.closeAll("loading");
                                }
                            });
                        }).error(function() {
                            layer.closeAll();
                            layer.msg("加载失败！",{icon:2});
                        });
                    }

                }else{
                    layer.msg(data.msg,{icon: 2,time:2000});
                    //关闭定时器
                    window.clearInterval(interval1);
                }
            }
        });
    }


    function pollingBackground2(){
        $.ajax({
            type : "POST",
            url:context_path + "/movePositionSlab/getSlabExecuteState",
            dataType : 'json',
            cache : false,
            success : function(data) {
                if(data.result){
                    if(data.code=="2"){
                        window.clearInterval(interval2);
                        $.post(context_path+"/movePositionSlab/toSlabPosition", {}, function(str){
                            var $queryWindow2 = layer.open({
                                title : "库位选择",
                                type: 1,
                                skin : "layui-layer-molv",
                                area : ["500px","400px"],
                                shade: 0.6, //遮罩透明度
                                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                                content: str,//注意，如果str是object，那么需要字符拼接。
                                success:function(layero, index){
                                    layer.closeAll("loading");
                                }
                            });
                        }).error(function() {
                            layer.closeAll();
                            layer.msg("加载失败！",{icon:2});
                        });

                    }
                }else{
                    layer.msg(data.msg,{icon: 2,time:2000});
                    //关闭定时器2
                    window.clearInterval(interval2);
                }
            }
        });
    }

</script>