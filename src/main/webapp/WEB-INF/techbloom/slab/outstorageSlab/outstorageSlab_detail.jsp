<%--
  Created by IntelliJ IDEA.
  User: HQKS-LU
  Date: 2019/3/15
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
%>
<div class="main-content" style="height:100%">
    <input type="hidden" id="id" name="id" value="${lowerId}" />
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
            colNames: ["物料主键", "物料编号", "物料名称","批次号","库位","数量","重量",'rfid编号',"状态"],
            colModel: [
                {name: "id", index: "id", width: 55, hidden: true},
                {name: "materialCode", width: 50},
                {name: "materialName", width: 40},
                {name: "batchNo", width: 60},
                {name: "positionCode", width: 30},
                {name: "amount", width: 30},
                {name: "weight", width: 30},
                {name: "rfid", width: 30},
                {name: "state", index: "state", width: 30,
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
                }
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
                        postData: {PUTBILLSID: $('#id').val()} //发送数据  :选中的节点
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



</script>
