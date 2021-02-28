<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="grid-div">
    <!-- 隐藏区域：存放查询条件 -->
    <form id="hiddenQueryForm" <%--action="<%=path%>/outStoragePlan/exportExcel"--%> method="POST" style="display:none;">
        <input name="outstorage_bill_code" id="outstorage_bill_code" value="" />
        <input id="outstorage_bill_type" name="outstorage_bill_type" value="" />
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="outstorage_bill_code" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">出库单号：</span>
                        <input type="text" id="outstorage_bill_code" name="outstorage_bill_code" value="" style="width: calc(100% - 97px);" placeholder="出库计划编号">
                    </label>
                </li>
                <li class="field-group field-fluid3">
                    <label class="inline" for="outstorage_bill_type" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:92px;">出库类型：</span>
                        <select class="mySelect2" style = "width:calc(100% - 97px);" id="outstorage_bill_type" name="outstorage_bill_type" data-placeholder="请选择出库类型">
                            <option value=""></option>
                            <option value="0">生产领料出库</option>
                            <option value="1">退货出库</option>
                            <option value="2">越库出库</option>
                            <option value="3">内部领料出库</option>
                            <option value="4">报损出库</option>
                            <option value="5">盘亏出库</option>
                            <option value="6">其他出库</option>
                        </select>
                    </label>
                </li>

            </ul>
            <div class="field-button" style="">
                <div class="btn btn-info" onclick="queryOk();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <%--<a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>--%>
            </div>
        </form>
    </div>
    <div id="grid-div" style="width:100%;margin:0px auto;">
        <div id="fixed_tool_div" class="fixed_tool_div">
            <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
        </div>
        <table id="grid-table" style="width:100%;height:100%;overflow:auto;"></table>
        <div id="grid-pager"></div>
    </div>
</div>

<script type="text/javascript" src="<%=path%>/static/js/techbloom/wms/outstorage/outstorageplan/outstorageplan.js"></script>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var oriData;
    var _grid;
    // var dynamicDefalutValue="17e870237a2d470fb170daeafe6292e1";//列表码
    $(function  (){
        $(".toggle_tools").click();
    });
    $("#queryForm .mySelect2").select2();

    $("#__toolbar__").iToolBar({
        id:"__tb__01",
        items:[
            {label: "添加",disabled:(${sessionUser.addQx}==1?false:true),onclick:addOutPlan,iconClass:'icon-plus'},
            {label: "编辑",disabled:(${sessionUser.editQx}==1?false:true),onclick:editOutPlan,iconClass:'icon-pencil'},
            {label: "删除",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:delOutPlan,iconClass:'icon-trash'},
            {label: "查看", disabled:(${sessionUser.queryQx} == 1 ? false : true),onclick:detailOutStorage, iconClass:'icon-ok'},
            {label: "导出",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:exportOutPlan,iconClass:'icon-share'},
            {label: "打印",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:printOutPlan,iconClass:' icon-print'},
            {label: "生成下架单",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:addshelve,iconClass:' icon-plus'},
            {label: "出库审核",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:spiltOutPlan,iconClass:''}
            <%--{label: "分配任务",disabled:(${sessionUser.deleteQx}==1?false:true),onclick:taskallot,iconClass:''}--%>
        ]
    });

    _grid = jQuery("#grid-table").jqGrid({
        url : context_path + '/outStorageManager/getList',
        datatype : "json",
        colNames : [ '主键','出库单号','出库类型','备料单','收货单','入库单','货主',/*'库位',*/'出库时间','状态','单据类型','备注'],
        colModel : [
            {name : 'id',index : 'id',width : 20,hidden:true},
            {name : 'outstorageBillCode',index : 'outstorageBillCode',width : 40},
            {name : 'outstorageBillType',index : 'outstorageBillType',width : 40,
                formatter:function(cellValu,option,rowObject){
                    if(cellValu==0){
                        return "<span>生产领料出库</span>" ;
                    }
                    if(cellValu==1){
                        return "<span>退货出库</span>" ;
                    }
                    if(cellValu==2){
                        return "<span>越库出库</span>" ;
                    }
                    if(cellValu==3){
                        return "<span>内部领料出库</span>" ;
                    }
                    if(cellValu==4){
                        return "<span>报损出库</span>" ;
                    }
                    if(cellValu==5){
                        return "<span>盘亏出库</span>" ;
                    }
                    if(cellValu==6){
                        return "<span>其他出库</span>" ;
                    }
                }},
            {name : 'spareBillCode',index : 'spareBillCode',width : 40},
            {name : 'receiptCode',index : 'receiptCode',width : 40},
            {name : 'instorageCode',index : 'instorageCode',width : 40},
            {name : 'customerCode',index : 'customerCode',width :40},
            /*{name : 'positionCode',index : 'positionCode',width : 40},*/
            {name : 'outstoragePlanTime',index : 'outstoragePlanTime',width : 40,formatter:function(cellValu,option,rowObject){
                    if(cellValu){
                        return cellValu.substring(0,19);
                    }else{
                        return "";
                    }
                }},
            {name :'state',index:'state',width:50,
                formatter:function(cellValu,option,rowObject){
                    if(cellValu==0){
                        return "<input id = 'state' type='hidden' value='0'><span style='color:red;font-weight:bold;'>未提交</span>" ;
                    }
                    if(cellValu==1){
                        return "<input id = 'state' type='hidden' value='1'><span style='color:green;font-weight:bold;'>已提交</span>" ;
                    }
                    if(cellValu==2){
                        return "<input id = 'state' type='hidden' value='2'><span style='color:orange;font-weight:bold;'>已审核</span>" ;
                    }
                    if(cellValu==3){
                        return "<input id = 'state' type='hidden' value='3'><span style='color:green;font-weight:bold;'>已生成下架单</span>" ;
                    }
                    if(cellValu==4){
                        return "<input id = 'state' type='hidden' value='4'><span style='color:green;font-weight:bold;'>正在出库</span>" ;
                    }
                    if(cellValu==5){
                        return "<input id = 'state' type='hidden' value='5'><span style='color:green;font-weight:bold;'>出库完成</span>" ;
                    }
                }
            },
            {name : 'billType',index : 'billType',width : 50,
            formatter:function (cellValu,option,rowObject) {
                if (cellValu == 0){
                    return "<input id = 'billType' type='hidden' value='0'><span style='color:red;font-weight: bold;'>无RFID单据<span>";
                }else if (cellValu == 1){
                    return "<input id = 'billType' type='hidden' value='1'><span style='color:green;font-weight: bold;'>有RFID单据<span>";
                }else {
                    return "<input id = 'billType' type='hidden' value=''><span style='color:black;font-weight: bold;'> <span>";
                }
            }},
            {name : 'remark',index : 'remark',width : 50, sortable: false}
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : '#grid-pager',
        sortname : 'outstorageBillCode',
        sortorder : "desc",
        altRows: true,
        viewrecords: true,
        autowidth: true,
        multiselect: true,
        multiboxonly: true,
        loadComplete : function(data){
            var table = this;
            setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
            oriData = data;
            $(window).triggerHandler('resize.jqGrid');
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
            $("#grid-table").jqGrid("setGridParam",
                {
                    postData: {queryJsonString:""} //发送数据
                }
            ).trigger("reloadGrid");
        }
    }).navButtonAdd("#grid-pager",{
        caption: "",
        onClickButton : function (){
            jQuery("#grid-table").jqGrid("columnChooser",{
                done: function(perm, cols){
                    $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width()-3);
                }
            });
        }
    });
    $(window).on("resize.jqGrid", function () {
        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() - 3 );
        var height = $("#breadcrumb").outerHeight(true)+$(".query_box").outerHeight(true)+
            $("#fixed_tool_div").outerHeight(true)+
            $("#gview_grid-table .ui-jqgrid-hbox").outerHeight(true)+
            $("#grid-pager").outerHeight(true)+$("#header").outerHeight(true);
        $("#grid-table").jqGrid("setGridHeight", (document.documentElement.clientHeight)-height );
    })


    //  });
    var _queryForm_data = iTsai.form.serialize($('#queryForm'));
    function queryOk(){
        debugger;
        //var formJsonParam = $('#queryForm').serialize();
        var queryParam = iTsai.form.serialize($('#queryForm'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
        queryInstoreListByParam(queryParam);

    }
    /**
     * 入库单查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
     * @param jsonParam     查询页面传递过来的json对象
     */
    function queryInstoreListByParam(jsonParam){
        //序列化表单：iTsai.form.serialize($('#frm'))
        //反序列化表单：iTsai.form.deserialize($('#frm'),json)
        iTsai.form.deserialize($('#hiddenQueryForm'),jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($('#hiddenQueryForm'));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table").jqGrid('setGridParam',
            {
                postData: {queryJsonString:queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }


    $('#queryForm .mySelect2').select2();

    function reset(){
        iTsai.form.deserialize($("#queryForm"),_queryForm_data);
        $("#queryForm #outstorage_bill_type").select2("val","");
        queryInstoreListByParam(_queryForm_data);
    }

    function addOutPlan(){
        $.get( context_path + "/outStorageManager/toEdit?id=-1").done(function(data){
            layer.open({
                title : "出库单添加",
                type:1,
                skin : "layui-layer-molv",
                area : ['780px', '620px'],
                shade : 0.6, //遮罩透明度
                moveType : 1, //拖拽风格，0是默认，1是传统拖动
                anim : 2,
                content : data
            });
        });
    }

    function editOutPlan(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要编辑的出库库计划单！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个出库库计划进行编辑操作！");
            return false;
        }else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var state = +$(rowData)[0].value;
            if (state != 0) {
                layer.alert("只能选择状态为未提交的数据进行编辑操作");
                return false;
            } else {
                $.get(context_path + "/outStorageManager/toEdit?id=" + id).done(function (data) {
                    layer.open({
                        title: "出库单编辑",
                        type: 1,
                        skin: "layui-layer-molv",
                        area: ['780px', '620px'],
                        shade: 0.6, //遮罩透明度
                        moveType: 1, //拖拽风格，0是默认，1是传统拖动
                        anim: 2,
                        content: data
                    });
                });
            }
        }
    }

    //查看
    function detailOutStorage(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要查看的出库单！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个出库单进行查看操作！");
            return false;
        } else {
            var outStorageId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path+"/outStorageManager/toDetail?outStorageId="+outStorageId, {}, function(str){
                $queryWindow = layer.open({
                    title : "出库单详情",
                    type: 1,
                    skin : "layui-layer-molv",
                    area : ["750px","650px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success:function(layero, index){
                        layer.closeAll("loading");
                    }
                });
            }).error(function() {
                layer.closeAll();
                layer.msg("加载失败！",{icon:2});
            });
        }
    }

    //列表刷新
    function gridReload(){
        $("#grid-table").jqGrid('setGridParam',
            {
                url : context_path + '/outStorageManager/getList',
                postData: {queryJsonString:""} //发送数据  :选中的节点
            }
        ).trigger("reloadGrid");
    }

    function delOutPlan(){
        var checkedNum = getGridCheckedNum("#grid-table","id");  //选中的数量
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要删除的出库单！");
            return false;
        }  else{
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selarrrow');
            layer.confirm("确定删除选中的出库单?",function(){
                $.ajax({
                    type: "POST",
                    url: context_path + "/outStorageManager/delOutStorage?ids=" + ids,
                    dataType: "json",
                    success:function(data){
                        if(data.result){
                            //弹出提示信息
                            layer.msg(data.msg);
                            //刷新列表
                            gridReload();
                        }else{
                            layer.msg(data.msg);
                        }
                    }
                })
            })
        }
    }

    function exportOutPlan(){

    }

    function printOutPlan(){

    }


    function spiltOutPlan(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要审核的出库库计划单！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个出库库计划进行审核操作！");
            return false;
        }else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var state = +$(rowData)[0].value;
            if(state==1){
                $.ajax({
                    type: "POST",
                    url: context_path + "/outStorageManager/auditing?id=" + id,
                    dataType: "json",
                    success:function(data){
                        if(data.result){
                            layer.msg("审核成功");
                            //刷新列表
                            gridReload();
                        }else{
                            layer.msg("审核失败");
                        }
                    }
                })
            }else{
                layer.alert("只能审核已经提交的单据");
            }
        }
    }

    /**
     * 生成下架单
     */
    function addshelve(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个出库单生成下架单！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个出库单生成下架单！");
            return false;
        }else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var state = +$(rowData)[0].value;
            var rowDatas = jQuery("#grid-table").jqGrid('getRowData', id).outstorageBillType;
            var outstorageBillType = +$(rowDatas)[0].value;
            if(state==2){
                $.ajax({
                    type: "POST",
                    url: context_path + "/outStorageManager/addshelve?id=" + id,
                    dataType: "json",
                    success:function(data){
                        if(data.result){
                            layer.alert(data.msg);
                            //刷新列表
                            gridReload();
                        }else{
                            layer.alert(data.msg);
                        }
                    }
                })
            }else{
                layer.alert("只能将已经审核的单据生成下架单");
            }
        }
    }

    //分配任务
    function taskallot(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个任务进行分配！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个任务进行分配！");
            return false;
        }else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var state = +$(rowData)[0].value;
            var rowDatas = jQuery("#grid-table").jqGrid('getRowData', id).outstorageBillType;
            var outstorageBillType = +$(rowDatas)[0].value;
            if(state==2){
                //跳转到任务分配界面
                $.get(context_path + "/outStorageManager/taskallot?id=" + id).done(function (data) {
                    layer.open({
                        title: "任务分配",
                        type: 1,
                        skin: "layui-layer-molv",
                        area: ['780px', '620px'],
                        shade: 0.6, //遮罩透明度
                        moveType: 1, //拖拽风格，0是默认，1是传统拖动
                        anim: 2,
                        content: data
                    });
                });
            }else{
                layer.alert("只能分配已经审核的单据");
            }
        }
    }
</script>