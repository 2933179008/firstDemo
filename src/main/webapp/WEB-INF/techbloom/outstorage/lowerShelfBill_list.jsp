<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="grid-div">
    <!-- 隐藏区域：存放查询条件 -->
    <form id="hiddenQueryForm" style="display:none;">
        <!-- 下架单编号 -->
        <input id="lowerShelfBillCode" name="lowerShelfBillCode" value=""/>
        <!-- 出库单编号 -->
        <input id="outstorageBillCode" name="outstorageBillCode" value=""/>
        <%--<!-- 客户商-->--%>
        <%--<input id="customerId" name="customerId" value=""/>--%>
        <%--<!-- 库区-->--%>
        <%--<input id="warehouseId" name="warehouseId" value="">--%>
    </form>
    <div class="query_box" id="yy" title="查询选项">
        <form id="queryForm" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid3">
                    <label class="inline" for="lowerShelfBillCode" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:80px;">下架单编号：</span>
                        <input id="lowerShelfBillCode" name="lowerShelfBillCode" type="text" style="width: calc(100% - 85px);" placeholder="下架单编号"/>
                    </label>
                </li>
                <li class="field-group field-fluid3">
                    <label class="inline" for="outstorageBillCode" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:80px;">出库单编号：</span>
                        <input id="outstorageBillCode" name="outstorageBillCode" type="text" style="width: calc(100% - 85px);" placeholder="出库单编号">
                    </label>
                </li>
                <%--<li class="field-group field-fluid3">--%>
                    <%--<label class="inline" for="warehouseId" style="margin-right:20px;width:100%;">--%>
                        <%--<span class="form_label" style="width:80px;">库区：</span>--%>
                        <%--<input id="warehouseId" name="warehouseId" type="text" style="width: calc(100% - 85px);" placeholder="库区">--%>
                    <%--</label>--%>
                <%--</li>--%>
                <%--<li class="field-group-top field-group field-fluid3">--%>
                    <%--<label class="inline" for="customerId" style="margin-right:20px;width:100%;">--%>
                        <%--<span class="form_label" style="width:80px;">货主：</span>--%>
                        <%--<input id="customerId" name="customerId" type="text" style="width: calc(100% - 85px);" placeholder="客户">--%>
                    <%--</label>--%>
                <%--</li>--%>

            </ul>
            <div class="field-button" style="">
                <div class="btn btn-info" onclick="queryOk();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>
            </div>
        </form>
    </div>
    <div id="fixed_tool_div" class="fixed_tool_div">
        <div id="__toolbar__" style="float:left;overflow:hidden;"></div>
    </div>
    <!--    物料信息表格 -->
    <table id="grid-table" style="width:100%;height:100%;"></table>
    <!-- 	表格分页栏 -->
    <div id="grid-pager"></div>
</div>
<script type="text/javascript">
    $(".mySelect2").select2();
    var context_path = '<%=path%>';
    var oriData;      //表格数据
    var _grid;        //表格对象
    // var dynamicDefalutValue="bf967f547da541909a2c2b900c95d093";//列表码
    $(function  (){
        $(".toggle_tools").click();
    });

    $("#__toolbar__").iToolBar({
        id:"__tb__01",
        items:[
            {label: "分配操作人",disabled:(${sessionUser.addQx}==1?false:true),onclick:lowerAllocation,iconClass:'icon-plus'},
            {label: "删除",disabled:(${sessionUser.addQx}==1?false:true),onclick:lowerDetail,iconClass:'icon-trash'}
        ]
    });
    $(function () {
        //初始化表格：显示上架单
        _grid = jQuery("#grid-table").jqGrid({
            url: context_path + "/lowerShelfBillManager/getList",
            datatype: "json",
            colNames: ["主键", "下架单编号","出库单编号", "货主","操作人", "下架时间", "状态","操作"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "lowerShelfBillCode", index: "lowerShelfBillCode", width: 40},
                {name: "outstorageBillCode", index: "outstorageBillCode", width: 40},
                {name: "customerCode",index:"customerCode", width: 60},
                //{name: "positionCode",index:"positionCode", width: 80},
                {name: "userId",index:"userId", width: 80},
                {name: "lowerShelfTime",index:"lowerShelfTime",width : 70,formatter:function(cellValu,option,rowObject){
                        if(undefined==cellValu)return"";
                        return cellValu.substring(0,19);
                    }
                },
                {name: "state",index:"state", width: 30,
                    formatter: function (cellValue, option, rowObject) {
                        if(cellValue==0){
                            return "<input id = 'state' value='0' type='hidden'><span style='color:red;font-weight:bold;'>未下架</span>" ;
                        }if(cellValue==1){
                            return "<input id = 'state' value='1' type='hidden'><span  style='color:orange;font-weight:bold;'>下架中</span>" ;
                        }
                        if(cellValue==2){
                            return "<input id = 'state' value='2' type='hidden'><span  style='color:green;font-weight:bold;'>下架完成</span>" ;
                        }
                        if(cellValue==3){
                            return "<input id = 'state' value='3' type='hidden'><span  style='color:green;font-weight:bold;'>单据已分配</span>" ;
                        }
                    }
                },
                {name:"opertion",index:"opertion",width:60, sortable: false,
                    formatter: function (cellValu, option, rowObject) {
                        return "<div style='margin-bottom:5px' class='btn btn-xs btn-success' onclick='viewDetailList(" + rowObject.id + ")'>下架</div>"
                    }
                }
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "lowerShelfBillCode",
            sortorder: "desc",
            altRows: true,
            viewrecords: true,
            autowidth: true,
            multiselect: true,
            multiboxonly: true,
            // beforeRequest:function (){
            //     dynamicGetColumns(dynamicDefalutValue,"grid-table",$(window).width()-$("#sidebar").width() -7);
            //     //重新加载列属性
            // },
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
        }).navButtonAdd("#grid-pager",{
            caption: "",
            // buttonicon:"fa  icon-cogs",
            onClickButton : function (){
                jQuery("#grid-table").jqGrid("columnChooser",{
                    done: function(perm, cols){
                        // dynamicColumns(cols,dynamicDefalutValue);
                        $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width()-3);
                    }
                });
            }
        });

        $(window).on("resize.jqGrid", function () {
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() - 3);
            var height = $("#header").outerHeight(true)+
                $(".query_box").outerHeight(true)+
                $("#grid-pager").outerHeight(true)+$("#fixed_tool_div").outerHeight(true)+
                $("#breadcrumb").outerHeight(true)+
                $("#gview_grid-table .ui-jqgrid-hbox").outerHeight(true);
            $("#grid-table").jqGrid("setGridHeight", (document.documentElement.clientHeight) - height);
        });
    });
    //查询
    var _queryForm_data = iTsai.form.serialize($('#queryForm'));
    function queryOk(){
        var queryParam = iTsai.form.serialize($('#queryForm'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
        queryInstoreListByParam(queryParam);
    }
    /**
     * 下架单查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
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

    function reset(){
        iTsai.form.deserialize($("#queryForm"),_queryForm_data);
        queryInstoreListByParam(_queryForm_data);
    }
    // $("#queryForm #warehouseId").select2({
    //     placeholder: "请选择库区",
    //     minimumInputLength: 0, //至少输入n个字符，才去加载数据
    //     allowClear: true, //是否允许用户清除文本信息
    //     delay: 250,
    //     formatNoMatches: "没有结果",
    //     formatSearching: "搜索中...",
    //     formatAjaxError: "加载出错啦！",
    //     ajax: {
    //         url: context_path + "/area/getSelectArea",
    //         type: "POST",
    //         dataType: "json",
    //         delay: 250,
    //         data: function (term, pageNo) { //在查询时向服务器端传输的数据
    //             term = $.trim(term);
    //             return {
    //                 queryString: term, //联动查询的字符
    //                 pageSize: 15, //一次性加载的数据条数
    //                 pageNo: pageNo, //页码
    //                 time: new Date()
    //                 //测试
    //             }
    //         },
    //         results: function (data, pageNo) {
    //             var res = data.result;
    //             if (res.length > 0) { //如果没有查询到数据，将会返回空串
    //                 var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
    //                 return {
    //                     results: res,
    //                     more: more
    //                 };
    //             } else {
    //                 return {
    //                     results: {}
    //                 };
    //             }
    //         },
    //         cache: true
    //     }
    // });
    // $("#queryForm #customerId").select2({
    //     placeholder: "选择客户",
    //     minimumInputLength: 0, //至少输入n个字符，才去加载数据
    //     allowClear: true, //是否允许用户清除文本信息
    //     delay: 250,
    //     formatNoMatches: "没有结果",
    //     formatSearching: "搜索中...",
    //     formatAjaxError: "加载出错啦！",
    //     ajax: {
    //         url: context_path + "/getbills/getSelectCusom",
    //         type: "POST",
    //         dataType: 'json',
    //         delay: 250,
    //         data: function (term, pageNo) { //在查询时向服务器端传输的数据
    //             term = $.trim(term);
    //             return {
    //                 queryString: term, //联动查询的字符
    //                 pageSize: 15, //一次性加载的数据条数
    //                 pageNo: pageNo, //页码
    //                 time: new Date()
    //                 //测试
    //             }
    //         },
    //         results: function (data, pageNo) {
    //             var res = data.result;
    //             if (res.length > 0) { //如果没有查询到数据，将会返回空串
    //                 var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
    //                 return {
    //                     results: res,
    //                     more: more
    //                 };
    //             } else {
    //                 return {
    //                     results: {}
    //                 };
    //             }
    //         },
    //         cache: true
    //     }
    //
    // });


    /*查看入库单详情*/
    function viewDetailList(id) {
        $.get(context_path + "/lowerShelfBillManager/toDetailView?id=" + id).done(
            function(data) {
                layer.open({
                    title : "下架单查看",
                    type : 1,
                    skin : "layui-layer-molv",
                    area : [ '780px', '620px' ],
                    shade : 0.6, // 遮罩透明度
                    moveType : 1, // 拖拽风格，0是默认，1是传统拖动
                    anim : 2,
                    content : data
                });
            });
    }

    /**
     * 分配
     */
    function lowerAllocation(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要分配的下架单！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个下架单进行分配操作！");
            return false;
        }else {
            var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', id).state;
            var state = +$(rowData)[0].value;
            if(state==0){               //只有未下架的单据才能被分配
                $.get(context_path + "/lowerShelfBillManager/lowerAllocation?lowerId=" + id).done(
                    function(data) {
                        layer.open({
                            title : "下架单分配",
                            type : 1,
                            skin : "layui-layer-molv",
                            area : [ '700px', '300px' ],
                            shade : 0.6, // 遮罩透明度
                            moveType : 1, // 拖拽风格，0是默认，1是传统拖动
                            anim : 2,
                            content : data
                        });
                    });
            }else{
                layer.alert("当前单据不能继续分配");
            }
        }
    }

    function lowerDetail(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的下架单！");
            return false;
        }else if (checkedNum > 1) {
            layer.alert("只能选择一个下架单进行删除操作！");
            return false;
        }else {
            var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
            var rowData = jQuery("#grid-table").jqGrid('getRowData', ids).state;
            var state = +$(rowData)[0].value;
            if(state==0 || state==3){
                layer.confirm("确定删除选中的下架单?",function(){
                    $.ajax({
                        type: "POST",
                        url: context_path + "/lowerShelfBillManager/delLower?ids=" + ids,
                        dataType: "json",
                        success:function(data){
                            if(data.result){
                                //弹出提示信息
                                layer.msg(data.msg);
                                //刷新列表
                                $("#grid-table").jqGrid('setGridParam',
                                    {
                                    }
                                ).trigger("reloadGrid");
                            }else{
                                layer.msg(data.msg);
                            }
                        }
                    })
                })
            }
            if(state == 2){
                layer.alert("当前单据已经完成,不能删除");
            }
            if(state == 1){
                layer.alert("当前单据正在尽心中,不能删除");
            }
        }
    }
</script>

