<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="grid-div-c">
    <!-- 隐藏区域：存放查询条件 -->
    <form id="hiddenQueryForm-c" style="display:none;">
        <input id="bomId" name="bomId" value="${bomId}">
        <input id="materialCodeS" name="materialCode" value="">
    </form>

    <div class="query_box" id="yy-c" title="查询选项" style="max-width:90%;">
        <form id="queryForm-c" style="max-width:100%;">
            <ul class="form-elements">
                <li class="field-group field-fluid2">
                    <label class="inline" for="materialCode" style="margin-right:20px;width:100%;">
                        <span class="form_label" style="width:80px;">物料编号：</span>
                        <input type="text" name="materialCode" id="materialCode" value="" style="width: calc(60%);" placeholder="物料编号">
                    </label>
                </li>
            </ul>
            <div class="field-button">
                <div class="btn btn-info" onclick="queryOkc();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="resetc();"><i class="ace-icon icon-remove"></i>重置</div>
            </div>
        </form>
    </div>
    <div id="fixed_tool_div-c" class="fixed_tool_div">
        <div id="__toolbar__-c" style="float:left;overflow:hidden;"></div>
    </div>
    <table id="grid-table-c" style="width:100%;height:100%;"></table>
    <div id="grid-pager-c"></div>
</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
    var oriData;
    var _grid_c;
    var dynamicDefalutValue="9fb20fa0399446bbb7fa1b9462ee0cf6";

    $("#__toolbar__-c").iToolBar({
        id: "__tb__01",
        items: [
            {label: "添加", disabled: (${sessionUser.addQx} == 1 ? false : true), onclick:addBomDetail, iconClass:'icon-plus'},
            {label: "编辑", disabled:(${sessionUser.editQx} == 1 ? false : true),onclick: editBomDetail, iconClass:'icon-pencil'},
            {label: "删除", disabled:(${sessionUser.deleteQx} == 1 ? false : true),onclick: delBomDetail, iconClass:'icon-trash'}
    ]
    });

    $(function () {
        _grid_c = jQuery("#grid-table-c").jqGrid({
            url: context_path + '/bomManage/getDetailList?bomId=' + $("#hiddenQueryForm-c #bomId").val(),
            datatype: "json",
            colNames: ['主键','物料编号', '物料名称','物料类别','数量','重量（kg）','单价'],
            colModel: [
                {name: 'id', index: 'id', width: 20, hidden: true},
                {name: 'materialCode', index: 'materialCode', width: 80},
                {name: 'materialName', index: 'materialName', width: 80},
                {name: 'materialType', index: 'materialType', width: 80},
                {name: 'amount', index: 'amount', width: 50},
                {name: 'weight', index: 'weight', width: 50},
                {name: 'unitPrice', index: 'unitPrice', width: 50}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: '#grid-pager-c',
            sortname: 'id',
            sortorder: "desc",
            altRows: true,
            viewrecords: true,
            caption: "BOM详情列表",
            autowidth: true,
            multiselect: true,
            multiboxonly: true,
            beforeRequest:function (){
                dynamicGetColumns(dynamicDefalutValue,'grid-table-c', $("#grid-div-c").width());
                //重新加载列属性
            },
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
        //在分页工具栏中添加按钮
        jQuery("#grid-table-c").navGrid('#grid-pager-c', {
            edit: false,
            add: false,
            del: false,
            search: false,
            refresh: false
        }).navButtonAdd('#grid-pager-c', {
                caption: "",
                buttonicon: "ace-icon fa fa-refresh green",
                onClickButton: function () {
                    //jQuery("#grid-table-c").trigger("reloadGrid");  //重新加载表格
                    var queryParam = iTsai.form.serialize($('#hiddenQueryForm-c'));
                    var queryJsonString = JSON.stringify(queryParam);

                    $("#grid-table-c").jqGrid('setGridParam',
                        {
                            postData: {queryJsonString: queryJsonString} //发送数据
                        }
                    ).trigger("reloadGrid");
                }
        });

        $(window).on('resize.jqGrid', function () {
            $("#grid-table-c").jqGrid('setGridWidth', $("#grid-div-c").width() );
            $("#grid-table-c").jqGrid('setGridHeight', (508 - $("#grid-pager-c").height() -(!$(".query_box").is(":hidden") ? $(".query_box").outerHeight(true) : 0)-$("#fixed_tool_div-c").height() -74));
        });

        $(window).triggerHandler('resize.jqGrid');
    });
    var _queryForm_data_c = iTsai.form.serialize($('#queryForm-c'));
    function queryOkc(){
        //var formJsonParam = $('#queryForm').serialize();
        var queryParam = iTsai.form.serialize($('#queryForm-c'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
        queryListByParam(queryParam);

    }
    function resetc(){
        //var formJsonParam = $('#queryForm').serialize();
        iTsai.form.deserialize($('#queryForm-c'),_queryForm_data_c);
        //var queryParam = iTsai.form.serialize($('#queryForm'));
        //执行父窗口中的js方法：将当前窗口中的form的值传递到父窗口，并放到父窗口中隐藏的form中，接着执行刷新父窗口列表的操作
        queryListByParam(_queryForm_data_c);

    }

    /**
     * 入库单查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
     * @param jsonParam     查询页面传递过来的json对象
     */
    function queryListByParam(jsonParam) {
        //序列化表单：iTsai.form.serialize($('#frm'))
        //反序列化表单：iTsai.form.deserialize($('#frm'),json)
        iTsai.form.deserialize($('#hiddenQueryForm-c'), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
        var queryParam = iTsai.form.serialize($('#hiddenQueryForm-c'));
        var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
        //执行查询操作
        $("#grid-table-c").jqGrid('setGridParam',
            {
                postData: {queryJsonString: queryJsonString} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addBomDetail(){
        $.get( context_path + "/bomManage/toBomDetailEdit?bomId="+$("#hiddenQueryForm-c #bomId").val()).done(function(data){
            window.childDiv = layer.open({
                title : "BOM详情添加",
                type:1,
                skin : "layui-layer-molv",
                area : ["600px"],
                shade : 0.6, //遮罩透明度
                moveType : 1, //拖拽风格，0是默认，1是传统拖动
                anim : 2,
                content : data
            });
        });
    }

    //修改
    function editBomDetail(){
        var checkedNum = getGridCheckedNum("#grid-table-c", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一条要编辑的信息！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一条信息进行编辑操作！");
            return false;
        } else {
            var id = jQuery("#grid-table-c").jqGrid('getGridParam', 'selrow');
            $.get( context_path + "/bomManage/toBomDetailEdit?id=" + id).done(function(data){
                window.childDiv = layer.open({
                    title : "BOM详情编辑",
                    type:1,
                    skin : "layui-layer-molv",
                    area : ["600px"],
                    shade : 0.6, //遮罩透明度
                    moveType : 1, //拖拽风格，0是默认，1是传统拖动
                    anim : 2,
                    content : data
                });
            });
        }
    }

    function delBomDetail(){
        var checkedNum = getGridCheckedNum("#grid-table-c", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一条要删除的信息！");
        } else {
            var ids = jQuery("#grid-table-c").jqGrid('getGridParam', 'selarrrow');
            layer.confirm('确定删除选中的信息？',
                function () {
                    $.ajax({
                        type: "POST",
                        url: context_path + '/bomManage/delBomDetail?ids=' + ids,
                        dataType: "json",
                        success: function (data) {
                            if (data) {
                                showTipMsg("删除成功！", 1000);
                            } else {
                                showTipMsg("删除失败！", 1000);
                            }
                            _grid_c.trigger("reloadGrid");  //重新加载表格
                        }
                    });
                }
            );

        }
    }

    function showTipMsg(msg, delay) {
        layer.msg(msg, {icon: 1,time:delay});
    }

</script>
