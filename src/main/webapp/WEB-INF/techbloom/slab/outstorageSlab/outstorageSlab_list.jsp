<%--
  Created by IntelliJ IDEA.
  User: HQKS-LU
  Date: 2019/3/11
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
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
    </form>
    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <div id="grid-pager"></div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
<script type="text/javascript">
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var interval;
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    $(function  (){
        $(".toggle_tools").click();
    });
    $("#__toolbar__").iToolBar({
        id:"__tb__01",
        items:[
            {label: "下架", onclick:slabOutBill,iconClass:'glyphicon glyphicon-plus'}
        ]
    });

    $(function(){
        _grid = jQuery("#grid-table").jqGrid({
            url : context_path + "/outstorageSlab/getList",
            datatype : "json",
            colNames : [ "主键","下架单编号","出库单编号","操作人","下架时间","状态","下架详情"],
            colModel : [
                {name : "id",index : "id",width : 20,hidden:true},
                {name : "lowerShelfBillCode",index : "lowerShelfBillCode",width : 40},
                {name : "outstorageBillCode",index : "outstorageBillCode",width : 40},
                {name : "userId",index : "userId",width : 40, sortable: false},
                {name : "lowerShelfTime",index : "lowerShelfTime",width : 40},
                {name : "state",index : "state",width : 40,
                    formatter:function(cellValue){
                        if (cellValue == 0) {
                            return "<input id = 'state' value='0' type='hidden'><span style='font-weight:bold;'>未提交</span>";
                        } else if (cellValue == 1) {
                            return "<input id = 'state' value='1' type='hidden'><span style='color:blue;font-weight:bold;'>待下架</span>";
                        } else if (cellValue == 2) {
                            return "<input id = 'state' value='2' type='hidden'><span style='color:blue;font-weight:bold;'>下架完成</span>";
                        }else if(cellValue == 3){
                            return "<input id = 'state' value='2' type='hidden'><span style='color:blue;font-weight:bold;'>已分配</span>";
                        }
                    }
                },
                {name:"opertion",index:"opertion",width:60, sortable: false,
                    formatter: function (cellValu, option, rowObject) {
                        return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' onclick='viewDetailList(" + rowObject.id + ")'>下架详情</div>"
                    }
                }
            ],
            rowNum : 20,
            rowList : [ 10, 20, 30 ],
            pager : "#grid-pager",
            sortname : "lsb.id",
            sortorder : "desc",
            altRows: false,
            viewrecords : true,
            autowidth:true,
            multiselect:true,
            multiboxonly: true,
            // beforeRequest:function (){
            //     dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
            //     //重新加载列属性
            // },
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

    //下架
    function slabOutBill(){
        var checkedNum = getGridCheckedNum("#grid-table","id");  //选中的数量
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要下架的下架单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一条下架单进行操作！");
            return false;
        } else{
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            //弹出确认窗口
            layer.confirm("确定下架？", function() {
                $.ajax({
                    type : "POST",
                    url:context_path + "/outstorageSlab/slabOutBill?lowerId="+id,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if(data.result){
                            layer.msg(data.msg, {icon: 1,time:2000});
                            //开启定时器
                            // pollingBackground();
                            //打开推荐的页面,将对应的物料信息传送过去
                            $.post(context_path+"/outstorageSlab/toSlabPosition", {}, function(str){
                                $queryWindow = layer.open({
                                    title : "确认物料信息是否正确",
                                    type: 1,
                                    skin : "layui-layer-molv",
                                    area : ['40%', '70%'],
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
                        }else{
                            layer.msg(data.msg, {icon: 2,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });
        }
    }

    function viewDetailList(id){
        $.get(context_path + "/outstorageSlab/toDetailView?id=" + id).done(
            function(data) {
                layer.open({
                    title : "下架单查看",
                    type : 1,
                    skin : "layui-layer-molv",
                    area : ['50%', '80%'],
                    shade : 0.6, // 遮罩透明度
                    moveType : 1, // 拖拽风格，0是默认，1是传统拖动
                    anim : 2,
                    content : data
                });
            });
    }

</script>
</html>
