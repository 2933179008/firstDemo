<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
%>
<style type="text/css"></style>
<div id="grid-div">
	<form id="matrixCode" action = "<%=path%>/supplierManage/toExcel" method = "POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="hiddenQueryForm" style="display:none;">
		<input id="supplierCodeS" name="supplierCode" value=""/>
		<input id="supplierNameS" name="supplierName" value="">
		<input id="linkmanS" name="linkman"  value="">
	</form>
	<div class="query_box" id="yy" title="查询选项">
		<form id="queryForm" style="max-width:100%;">
			<ul class="form-elements">
				<li class="field-group field-fluid3">
					<label class="inline" for="supplierCode" style="margin-right:25px;width:100%;">
						<span class="form_label" style="width:80px;">供应商编码：</span>
						<input type="text" name="supplierCode" id="supplierCode" value="" style="width: calc(100% - 120px);" placeholder="供应商编码">
					</label>
				</li>

				<li class="field-group field-fluid3">
					<label class="inline" for="supplierName" style="margin-right:25px;width:100%;">
						<span class="form_label" style="width:80px;">供应商名称：</span>
						<input type="text" name="supplierName" id="supplierName" value="" style="width: calc(100% - 120px);" placeholder="供应商名称">
					</label>
				</li>

				<li class="field-group field-fluid3">
					<label class="inline" for="linkman" style="margin-right:25px;width:100%;">
						<span class="form_label" style="width:60px;">联系人：</span>
						<input type="text" name="linkman" id="linkman" value="" style="width: calc(100% - 120px);" placeholder="联系人">
					</label>
				</li>

			</ul>

			<div class="field-button">
				<div class="btn btn-info" onclick="queryOk();">
					<i class="ace-icon fa fa-check bigger-110"></i>查询
				</div>
				<div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
			</div>
		</form>
	</div>

	<div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
		<button class=" btn btn-primary btn-addQx" onclick="addSupplier();">
			添加<i class="fa fa-plus" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-editQx" onclick="editSupplier();">
			编辑<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-deleteQx" onclick="delSupplier();">
			删除<i class="fa fa-trash" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-editQx" onclick="lookSupplier();">
			查看<i class="icon-zoom-in" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class="col-md-1 btn btn-primary btn-queryQx" onclick="exportLogFile();">
			导出<i class="fa fa-share" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-editQx" onclick="print();">
			打印<i class="icon-print" aria-hidden="true" style="margin-left:5px;"></i>
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
    $(".date-picker").datepicker({format: "yyyy-mm-dd"});
    var context_path = '<%=path%>';
    var _grid;

    $(function  (){
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
            url: context_path + "/supplierManage/getList",
            datatype: "json",
            colNames: ["主键","供应商编码","供应商名称","供应商类别","联系人","联系方式","地址","邮箱","备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "supplierCode", index: "supplierCode", width: 65},
                {name: "supplierName", index: "supplierName", width: 65},
                {name: "supplierType", index: "supplierType", width: 65},
                {name: "linkman", index: "linkman", width: 65,sortable: false},
                {name: "telephone",index:"telephone",width:50,sortable: false},
                {name: "address",index:"address",width:50,sortable: false},
                {name: "mail",index:"mail",width:50,sortable: false},
                {name: "remark",index:"remark",width:50,sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            sortname: "supplierCode",
            sortorder: "asc",
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
            $("#grid-table").jqGrid("setGridWidth", $("#grid-div").width() );
            $("#grid-table").jqGrid("setGridHeight",  $(".container-fluid").height()-$("#yy").outerHeight(true)-$("#fixed_tool_div").outerHeight(true)-$("#grid-pager").outerHeight(true)
                -$("#gview_grid-table .ui-jqgrid-hdiv").outerHeight(true)-45);
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
        $("#queryForm #supplierCode").val("");
        $("#queryForm #supplierName").val("");
        $("#queryForm #linkman").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    //添加
    function addSupplier(){
        $.post(context_path+"/supplierManage/toEdit?id=-1", {}, function(str){
            $queryWindow = layer.open({
                title : "供应商添加",
                type: 1,
                skin : "layui-layer-molv",
                area : ["750px","650px"],
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

    //修改
    function editSupplier(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要编辑的供应商！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个供应商进行编辑操作！");
            return false;
        } else {
            var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path+"/supplierManage/toEdit?id="+id, {}, function(str){
                $queryWindow = layer.open({
                    title : "供应商编辑",
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

    //删除
    function delSupplier(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的供应商！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的供应商？", function() {
                $.ajax({
                    type : "POST",
                    url : context_path + "/supplierManage/delSupplier?ids="+ids ,
                    dataType : "json",
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if (data) {
                            layer.msg("删除成功", {icon: 1,time:1000});
                        }else{
                            layer.msg("删除失败", {icon: 7,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

    /**
     * 供应商导出
     */
    function exportLogFile(){
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        $("#ids").val(ids);
        $("#hiddenForm").submit();
    }

    //查看
    function lookSupplier() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的供应商！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个供应商进行查看操作！");
            return false;
        } else {
            var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/supplierManage/toDetail?id=" + id, {}, function (str) {
                $queryWindow = layer.open({
                    title: "供应商查看",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["600px", "500px"],
                    shade: 0.6,
                    moveType: 1,
                    content: str,
                    success: function (layero, index) {
                        layer.closeAll("loading");
                    }
                });
            }).error(function () {
                layer.closeAll();
                layer.msg("加载失败！", {icon: 2});
            });
        }
    }

</script>