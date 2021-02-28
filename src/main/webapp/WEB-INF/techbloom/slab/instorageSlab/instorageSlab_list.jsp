<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <style type="text/css"></style>
</head>
<body style="overflow:hidden;">
<div id="grid-div">
    <form id="hiddenQueryForm" style="display:none;">
        <input name="putBillCode" id="putBillCode" value="">
        <input name="instorageBillCode" id="instorageBillCode" value="">
        <input name="userName" id="userName" value="">
    </form>
    <%--<div class="query_box" id="yy" title="">--%>
        <%--<form id="queryForm" style="max-width:100%;">--%>
            <%--<ul class="form-elements">--%>
                <%--<li class="field-group field-fluid3">--%>
                    <%--<label class="inline" for="forklift" style="margin-right:20px;width:100%;">--%>
                        <%--<span class="form_label" style="width:85px;">叉车：</span>--%>
                        <%--<select id="forklift" name="forklift" style="width: calc(100% - 120px);">--%>
                            <%--&lt;%&ndash;<option value="">--请选择--</option>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<option value="0">叉车1</option>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<option value="1">叉车2</option>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<option value="2">叉车3</option>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<option value="3">叉车4</option>&ndash;%&gt;--%>
                        <%--</select>--%>
                    <%--</label>--%>
                <%--</li>--%>
            <%--</ul>--%>
        <%--</form>--%>
    <%--</div>--%>
    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/instorageBill/instorage_list.js"></script>
<script type="text/javascript">
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    var dynamicDefalutValue="7eaf063c25564277ab90aea11790e3ac";
    $(function  (){
        $(".toggle_tools").click();
    });
    // $("#__toolbar__").iToolBar({
    //     id:"__tb__01",
    //     items:[
    //         {label: "上架", onclick:slabPutBill,iconClass:'glyphicon glyphicon-plus'}
    //     ]
    // });

    $(function(){
        _grid = jQuery("#grid-table").jqGrid({
            url : context_path + "/instorageSlab/getList",
            datatype : "json",
            colNames : [ "主键","上架单编号","入库单编号","操作人","上架时间","状态","操作"],
            colModel : [
                {name : "id",index : "id",width : 20,hidden:true},
                {name : "putBillCode",index : "put_Bill_Code",width : 40},
                {name : "instorageBillCode",index : "instorage_Bill_Code",width : 40},
                {name : "operatorName",index : "operator_Name",width : 40},
                {name : "createTime",index : "create_Time",width : 40},
                {name : "state",index : "state",width : 40,
                    formatter:function(cellValue){
                        if (cellValue == 0) {
                            return "<span style='font-weight:bold;'>未提交</span>";
                        } else if (cellValue == 1) {
                            return "<span style='color:blue;font-weight:bold;'>待上架</span>";
                        } else if (cellValue == 2) {
                            return "<span style='color:blue;font-weight:bold;'>上架中</span>";
                        } else if (cellValue == 3) {
                            return "<span style='font-weight:bold;'>上架完成</span>";
                        }
                    }
                },
                {name : "operate",index : "operate",width : 40, sortable: false,
                    formatter: function (cellValu, option, rowObject) {
                        // return "<a href='"+context_path+"/instorageSlab/toVisualization' style='margin-bottom:5px' class='btn btn-xs btn-success'>上架</a>"
                        return "<a href='#' onclick='slabPutBill(\"" + rowObject.id + "\")' style='margin-bottom:5px' class='btn btn-xs btn-success'>上架</a>";
                    }
                }
            ],
            rowNum : 20,
            rowList : [ 10, 20, 30 ],
            pager : "#grid-pager",
            sortname : "put_bill_code",
            sortorder : "desc",
            altRows: false,
            viewrecords : true,
            autowidth:true,
            multiselect:true,
            multiboxonly: true,
            beforeRequest:function (){
                dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
                //重新加载列属性
            },
            loadComplete : function(data) {
                var table = this;
                setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
                oriData = data;
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext : "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        //在分页工具栏中添加按钮
        jQuery("#grid-table").navGrid("#grid-pager",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager',{
            caption:"",
            buttonicon:"ace-icon fa fa-refresh green",
            onClickButton: function(){
                //jQuery("#grid-table").trigger("reloadGrid");  //重新加载表格
                $("#grid-table").jqGrid("setGridParam",
                    {
                        postData: {queryJsonString:""} //发送数据
                    }
                ).trigger("reloadGrid");
            }
        }).navButtonAdd("#grid-pager",{
            caption: "",
            buttonicon:"faicon-cogs",
            onClickButton : function (){
                jQuery("#grid-table").jqGrid("columnChooser",{
                    done: function(perm, cols){
                        dynamicColumns(cols,dynamicDefalutValue);
                        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width()-3);
                    }
                });
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() );
            $("#grid-table").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy").outerHeight(true)-$("#fixed_tool_div").outerHeight(true)-$("#grid-pager").outerHeight(true)
                -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true));
        });
        $(window).triggerHandler("resize.jqGrid");
    });
    //查询
    function queryOk(){
        var queryParam = iTsai.form.serialize($("#queryForm"));
        queryPlatformByParam(queryParam);
    }

    function queryPlatformByParam(jsonParam){
        iTsai.form.deserialize($("#hiddenQueryForm"), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($("#hiddenQueryForm"));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //重置查询条件
    function reset(){
        $("#queryForm #licensePlateNumber").val("");
        $("#queryForm #status").val("");
        $("#queryForm #createTime").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //上架
    function slabPutBill(putBillId){
        // var forkliftTag = $("#forklift").val();
        // if(forkliftTag==null || forkliftTag=='' || forkliftTag==undefined){
        //     layer.alert("请先选择操作的叉车！");
        //     return false;
        // }else{
            $.ajax({
                type : "POST",
                url:context_path + "/instorageSlab/sureSlabPutBillOperate?putBillId="+putBillId,
                success : function(data) {
                    if(data.result){
                        $.ajax({
                            type : "POST",
                            url:context_path + "/instorageSlab/toVisualization?putBillId="+putBillId/*+"&forkliftTag="+forkliftTag*/,
                            success : function(data) {
                                $(".container-fluid").empty();
                                $(".container-fluid").append(data);
                            }
                        });

                    }else{
                        _grid.trigger("reloadGrid");  //重新加载表格
                        layer.msg(data.msg,{icon: 2,time:2000});
                    }

                }
            });
        // }
    }

    //轮询后台，从数据库表中获取叉车执行状态
    function pollingBackground(){
        $.ajax({
            type : "POST",
            url:context_path + "/instorageSlab/getSlabExecuteState",
            dataType : 'json',
            cache : false,
            success : function(data) {
                if(data.result){
                    if(data.code=="0"){
                        layer.msg(data.msg,{icon: 2,time:2000});
                    }else if(data.code=="1"){
                        $.post(context_path+"/instorageSlab/toSlabPosition", {}, function(str){
                            var $queryWindow1 = layer.open({
                                title : "库位选择",
                                type: 1,
                                skin : "layui-layer-molv",
                                area : ['20%', '20%'],
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

                        //关闭定时器
                        window.clearInterval(interval);
                    }

                }else{
                    layer.msg(data.msg,{icon: 2,time:2000});
                    //关闭定时器
                    window.clearInterval(interval);
                }
            }
        });
    }

    getForkliftSelected();

    //后台获取叉车下拉框列表数据
    function getForkliftSelected(){
        // $("#forklift").remove();//清空select列表数据
        $.ajax({
            type : "POST",
            url:context_path + "/instorageSlab/getForkliftSelected",
            dataType : 'json',
            cache : false,
            success : function(data) {
                if(data.result){
                    $("#forklift").prepend("<option value=''>请选择</option>");//添加第一个option值
                    for (var i = 0; i < data.list.length; i++) {
                        var op = "<option value='"+data.list[i].tag+"'>"+data.list[i].forklift_name+"</option>";
                        $("#forklift").append(op);
                    }
                }
            }
        });
    }
</script>
</html>