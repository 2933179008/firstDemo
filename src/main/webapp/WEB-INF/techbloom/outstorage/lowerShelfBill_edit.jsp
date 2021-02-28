<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
%>
<div class="main-content" style="height:100%">
    <div class="widget-header widget-header-large" id="div1">
        <input type="hidden" id="id" name="id" value="${lowerShelfBillVO.id }" />
        <%--<input type="hidden" id="rfid" name="rfid" value="${rfid }" />--%>
        <h3 class="widget-title grey lighter" style="background: none; border-bottom: none;">
            <i class="ace-icon fa fa-leaf green"></i> 下架单
        </h3>
        <div class="widget-toolbar no-border invoice-info">
            <span class="invoice-info-label">出库单编号：</span>
            <span class="red">${lowerShelfBillVO.outstorageBillCode }</span>
            <br />
            <span class="invoice-info-label">下架时间：</span>
            <span class="blue">${fn:substring(lowerShelfBillVO.lowerShelfTime, 0, 19)}</span>
        </div>
        <div class="widget-toolbar hidden-480">
            <a href="#" onclick="printDoc();" title="详情打印">
                <i class="ace-icon fa fa-print"></i>
            </a>
        </div>
    </div>
    <div class="widget-body" id="div2">
        <table style="width: 100%;font-size:16px;border-collapse:separate;border-spacing:2px 3px;">
            <tr>
                <td>
                    <i class="ace-icon fa fa-caret-right blue"></i> 货主：
                    <b class="black">${lowerShelfBillVO.customerCode }</b>
                </td>

            </tr>
            <tr>
                <td>
                    <i class="ace-icon fa fa-caret-right blue"></i> 下架人：
                    <b class="black"><c:if test="${not empty username}">${username}</c:if></b>
                </td>
                <td>
                    <i class="ace-icon fa fa-caret-right blue"></i> 状态:
                    <c:if test="${lowerShelfBillVO.state==0 }">
                        <span style="color:#d15b47;font-weight:bold;">未下架</span>
                    </c:if>
                    <c:if test="${lowerShelfBillVO.state==1 }">
                        <span style="color:#87b87f;font-weight:bold;">下架中</span>
                    </c:if>
                    <c:if test="${lowerShelfBillVO.state==2 }">
                        <span style="color:#76b86b;font-weight:bold;">下架完成</span>
                    </c:if>
                </td>
            </tr>
        </table>
    </div>
    <div id="grid-div-c">
        <table id="grid-table-c" style="width:100%;height:100%;"></table>
        <div id="grid-pager-c"></div>
    </div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var oriData;      //表格数据
    var _grid;        //表格对象
    var getbillsStatus = '${lowerShelfBillVO.state}';   //下架单状态
    $(function() {
        //初始化表格
        _grid = jQuery("#grid-table-c").jqGrid({
            url: context_path + "/lowerShelfBillManager/getDetailList?id=" + $("#id").val(),
            datatype: "json",
            colNames: ["物料主键", "物料编号", "物料名称","批次号","库位" ,"数量","重量","rfid编号","状态","操作"],
            colModel: [
                {name: "id", index: "id", width: 55, hidden: true},
                {name: "materialCode", width: 30},
                {name: "materialName", width: 30},
                {name: "batchNo", width: 80},
                {name: "positionCode", width: 30},
                {name: "amount", width: 20},
                {name: "weight", width: 20},
                {name: "rfid", width: 30},
                {name: "state", index: "state", width: 20,
                    formatter: function (value, options, rowObject) {
                    if(value==0){
                        return "<span style='color:red;font-weight:bold;'>未下架</span>";
                    }
                    if(value==1){
                        return "<span style='color:orange;font-weight:bold;'>正在下架</span>" ;
                    }
                    if(value==2){
                        return "<span style='color:green;font-weight:bold;'>下架完成</span>" ;
                    }
                }
                },
                {name: "operation", index:'operation',width: 40,
                    formatter: function (value, options, rowObject) {
                    if(rowObject.state ==2){
                        return "<div style='margin-bottom:5px' disabled='false' class='btn btn-xs btn-success' >下架完成</div>"
                        //return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' onclick='cccc()'>下架完成</div>"
                    }else{
                        return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' onclick='lowerShelf("+rowObject.id+")'>确认下架</div>"
                    }
                }}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: '#grid-pager-c',
            sortname: 'ID',
            sortorder: "asc",
            cellEdit: true, //单元格bian'ji
            altRows: true,
            viewrecords: true,
            autowidth: true,
            multiselect: false,
            multiboxonly: true,
            loadComplete: function (data) {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                oriData = data;
                $(window).triggerHandler('resize.jqGrid');
            },
            emptyrecords: "没有相关记录",
            loadtext: "加载中...",
            pgtext: "页码 {0} / {1}页",
            recordtext: "显示 {0} - {1}共{2}条数据"
        });
        //在分页工具栏中添加按钮
        jQuery("#grid-table-c").navGrid('#grid-pager-c', {
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
                        postData: {id: $('#id').val()} //发送数据  :选中的节点
                    }
                ).trigger("reloadGrid");
            }
        });
        $(window).on("resize.jqGrid", function () {
            $("#grid-table-c").jqGrid("setGridWidth", $("#grid-div-c").width() - 3);
            var height = $(".layui-layer-title",_grid.parents(".layui-layer")).height()+
                $("#div1").outerHeight(true)+$("#div2").outerHeight(true)+
                $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true)+
                $("#grid-pager-c").outerHeight(true);
            $("#grid-table-c").jqGrid("setGridHeight", _grid.parents(".layui-layer").height()-height);
        });
    });
    //将数据格式化成两位小数：四舍五入
    function formatterNumToFixed(value,options,rowObj){
        if(value!=null){
            if(rowObj.id==-1){
                return "";
            }else{
                var floatNum = parseFloat(value);
                return floatNum.toFixed(2);
            }
        }else{
            return "0.00";
        }
    }

    /**
     * 确认下架完成
     * @param id
     */
    function lowerShelf(id){
        $.ajax({
            type: "POST",
            url: context_path + '/lowerShelfBillManager/confirmLowerShelf?lowerDetailId=' + id ,
            dataType: 'json',
            cache: false,
            success: function (data) {
                if(data.result){
                    //刷新详情列表
                    $("#grid-table-c").jqGrid("setGridParam",
                        {
                        }
                    ).trigger("reloadGrid");
                    //刷新下架单列表
                    $("#grid-table").jqGrid('setGridParam',
                        {
                        }
                    ).trigger("reloadGrid");
                    layer.alert(data.msg);
                }else{
                    layer.alert(data.msg);
                }
            }
        })
    }

    function printDoc(){
        var url = context_path + "/getbills/printGetbillsDetail?putId="+$("#id").val();
        window.open(url);
    }

    function offTheShelf(id,locationId,realAmount,amount,state){
        if(realAmount == 0){
            layer.msg("请输入实际下架数量！");
            return;
        }
        var a = amount - realAmount;
        if(a>0){
            layer.confirm('当前库位物料数缺少<font color="red">  '+ a +' </font>，是否跳过后下架？', {
                btn: ['跳过'] //按钮
            }, function(){

                $.ajax({
                    type : "POST",
                    url : context_path + '/getbills/setState.do?id=' + id+'&locationId='+locationId+'&state='+state+'&amount='+realAmount,
                    dataType : 'json',
                    cache : false,
                    success : function(data) {
                        if (Boolean(data.result)) {
                            layer.msg(data.msg, {icon: 1, time: 1000});
                        } else {
                            layer.msg(data.msg, {icon: 7, time: 1000});
                        }
                        $("#grid-table-c").jqGrid('setGridParam',
                            {
                                postData: {queryJsonString: ""} //发送数据
                            }
                        ).trigger("reloadGrid");

                        $("#grid-table").jqGrid('setGridParam',
                            {
                                postData: {queryJsonString: ""} //发送数据
                            }
                        ).trigger("reloadGrid");
                    }
                });

            });
        }else if(realAmount > amount){
            layer.msg("实际下架数量超出！");
            return;
        }else{
            $.ajax({
                type : "POST",
                url : context_path + '/getbills/setState.do?id=' + id+'&locationId='+locationId+'&state='+state+'&amount='+amount,
                dataType : 'json',
                cache : false,
                success : function(data) {
                    if (Boolean(data.result)) {
                        layer.msg(data.msg, {icon: 1, time: 1000});
                    } else {
                        layer.msg(data.msg, {icon: 7, time: 1000});
                    }
                    $("#grid-table-c").jqGrid('setGridParam',
                        {
                            postData: {queryJsonString: ""} //发送数据
                        }
                    ).trigger("reloadGrid");

                    $("#grid-table").jqGrid('setGridParam',
                        {
                            postData: {queryJsonString: ""} //发送数据
                        }
                    ).trigger("reloadGrid");
                }
            });
        }
    }
    //数量输入验证
    function numberRegex(value, colname) {
        var regex = /^\d+\.?\d{0,2}$/;
        if (!regex.test(value)) {
            layer.alert("非法的数据,请输入整数");
            return [false, ""];
        }
        else  return [true, ""];
    }
    

</script>