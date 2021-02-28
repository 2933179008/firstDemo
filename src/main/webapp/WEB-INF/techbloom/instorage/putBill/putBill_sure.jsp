<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <!-- 上架单主键 -->
        <input type="hidden" id="id" name="id" value="${putBill.id}">
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--上架单编号--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="putBillCode" >上架单编号：</label>
                <div class="controls">
                    <div class="input-append  span12" >
                        <input type="text" id="putBillCode" class="span10" name="putBillCode"   placeholder="后台自动生成" readonly="readonly" value="${putBillCode}"/>
                    </div>
                </div>
            </div>
            <%--入库单--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="instorageBillCode" >入库单：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input type="text" id="instorageBillCode" class="span10" name="instorageBillCode" readonly="readonly" value="${putBill.instorageBillCode}"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin:0;padding:0;">
            <%--操作人--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="operatorName" >操作人：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input type="text" id="operatorName" class="span10" name="operatorName" readonly="readonly" value="${putBill.operatorName}"/>
                    </div>
                </div>
            </div>
            <%--备注--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="remark" >备注：</label>
                <div class="controls">
                    <div class="input-append span12" >
                        <input readonly="readonly" class="span10" type="text" id="remark" name="remark" placeholder="备注" value="${instorage.remark}">
                    </div>
                </div>
            </div>
        </div>
        <%--<div style="margin-left:10px;">--%>
            <%--<span class="btn btn-info" id="formSave">--%>
		       <%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
            <%--</span>--%>
            <%--<span class="btn btn-info" id="formSubmit">--%>
		        <%--<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交--%>
            <%--</span>--%>
        <%--</div>--%>
    </form>
    <!-- 分割线 -->
    <HR align=center width='100%' color=#987cb9 SIZE=1>
    <%--<div id="materialDiv" style="margin:10px;">--%>
        <%--<!-- 下拉框 -->--%>
        <%--<label class="inline" for="materialInfor">物料：</label>--%>
        <%--<input type="text" id = "materialInfor" name="materialInfor" style="width:350px;margin-right:10px;" />--%>
        <%--<button id="addMaterialBtn" class="btn btn-xs btn-primary" onclick="addDetail();">--%>
            <%--<i class="icon-plus" style="margin-right:6px;"></i>添加--%>
        <%--</button>--%>
    <%--</div>--%>
    <!-- 表格div -->
    <div id="grid-div-c" style="width:100%;margin:0px auto;">
        <!-- 	表格工具栏 -->
        <div id="fixed_tool_div" class="fixed_tool_div detailToolBar">
            <div id="__toolbar__-c" style="float:left;overflow:hidden;"></div>
        </div>
        <!-- 物料详情信息表格 -->
        <table id="grid-table-c" style="width:100%;height:100%;"></table>
        <!-- 表格分页栏 -->
        <div id="grid-pager-c"></div>
    </div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss',useMinutes:true,useSeconds:true});
    var putBIllId=$("#baseInfor #id").val();
    var oriDataDetail;
    var _grid_detail;
    var select_dataLocation = "";
    _grid_detail=jQuery("#grid-table-c").jqGrid({
        url : context_path + "/putBill/getDetailList?putBIllId="+putBIllId,
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","批次号","包装单位","上架数量","上架重量（kg）","RFID","库位","操作"],
        colModel : [
            {name : "id",index : "id",width : 20,hidden:true},
            {name : "materialCode",index:"material_Code",width :20},
            {name : "materialName",index:"material_Name",width : 20},
            {name : "batchNo",index:"batch_No",width : 20},
            {name : "unit",index:"unit",width : 20},
            {name: 'putAmount', index: 'put_Amount', width: 20},
            {name : "putWeight",index:"put_Weight",width : 20},
            {name: 'rfid', index: 'rfid', width: 30},
            {name : "positionCode",index:"position_Code",width : 30,
                edittype: "select",
                editoptions:{
                    value:getPosition()
                },
                formatter: function (cellValue, option, rowObject) {
                    var vle = cellValue || "";
                    if (select_dataLocation && select_dataLocation.result) {
                        for (var i = 0; i < select_dataLocation.result.length; i++) {
                            if (select_dataLocation.result[i].positionCode == cellValue) {
                                vle = select_dataLocation.result[i].positionName;
                            }
                        }
                    }
                    return vle;
                }
            },
            {name: 'operation',sortable: false, index: 'operation', width: 30,
                formatter:function(cellValu,option,rowObject){
                    if(rowObject.state==1){
                        return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' disabled='disabled'>上架完成</div>";
                    }else{
                        return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' onclick='completePutBill("+rowObject.id+");'>确认完成</div>";
                    }
                }
            }
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : "#grid-pager-c",
        sortname : "material_code",
        sortorder : "desc",
        altRows: true,
        viewrecords : true,
        autowidth:true,
        multiselect:true,
        multiboxonly: true,
        loadComplete : function(data){
            var table = this;
            setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
            oriDataDetail = data;
            $(window).triggerHandler('resize.jqGrid');
        },
        cellEdit: true,
        cellsubmit : "clientArray",
        emptyrecords: "没有相关记录",
        loadtext: "加载中...",
        pgtext : "页码 {0} / {1}页",
        recordtext: "显示 {0} - {1}共{2}条数据",
    });
    //在分页工具栏中添加按钮
    $("#grid-table-c").navGrid("#grid-pager-c",{edit:false,add:false,del:false,search:false,refresh:false}).navButtonAdd('#grid-pager-c',{
        caption:"",
        buttonicon:"ace-icon fa fa-refresh green",
        onClickButton: function(){
            $("#grid-table-c").jqGrid("setGridParam",
                {
                    url:context_path + '/putBill/getDetailList?putBIllId='+putBIllId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3 );
        var height = $(".layui-layer-title",_grid_detail.parents(".layui-layer")).height()+
            $("#baseInfor").outerHeight(true)+$("#materialDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight',_grid_detail.parents(".layui-layer").height()-height-50);
    });
    $(window).triggerHandler("resize.jqGrid");

    //获取库位下拉框数据源
    function getPosition(){
        var str = "";
        $.ajax({
            type: "post",
            async: false,
            url: context_path + "/putBill/getPosition",
            dataType: "json",
            success: function (data) {
                select_dataLocation = data;
                for (var i = 0; i < data.result.length; i++) {
                    str += data.result[i].positionCode + ":" + data.result[i].positionName + ";";
                }
                str = str.substring(0, str.length - 1);
            }
        })
        return str;
    }


    //确认完成上架
    function completePutBill(putBillDetailId){
        layer.confirm("确认该货物完成上架吗？",function(){
            $.ajax({
                url:context_path + '/putBill/completePutBill?putBillDetailId='+putBillDetailId,
                type:"POST",
                dataType:"JSON",
                success:function(data){
                    if(data.result){
                        layer.msg(data.msg,{icon:1,time:1200});
                        //刷新上架单详情页面
                        $("#grid-table-c").jqGrid("setGridParam",
                            {
                                url:context_path + '/putBill/getDetailList?putBIllId='+putBIllId,
                                postData: {queryJsonString:""} //发送数据  :选中的节点
                            }
                        ).trigger("reloadGrid");

                        //刷新上架单页面
                        $("#grid-table").jqGrid("setGridParam",
                            {
                                url:context_path + '/putBill/getList',
                                postData: {queryJsonString:""} //发送数据  :选中的节点
                            }
                        ).trigger("reloadGrid");

                    } else{
                        layer.msg(data.msg,{icon:2,time:1200});
                    }
                }
            });
        });

    }

</script>