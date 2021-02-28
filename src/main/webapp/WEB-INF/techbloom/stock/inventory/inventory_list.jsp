<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
%>
<style type="text/css"></style>
<div id="grid-div">
	<form id="hiddenForm" action = "<%=path%>/inventory/toExcel" method = "POST" style="display: none;">
		<input id="ids" name="ids" value=""/>
	</form>
	<form id="hiddenQueryForm" style="display:none;">
		<input name="inventoryCode" id="inventoryCodeS" value=""/>
		<input name="state" id="stateS" value=""/>
	</form>
	<div class="query_box" id="yy" title="查询选项">
		<form id="queryForm" style="max-width:100%;">
			<ul class="form-elements">
				<li class="field-group field-fluid3">
					<label class="inline" for="inventoryCode" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:65px;">盘点编码：</span>
						<input type="text" name="inventoryCode" id="inventoryCode" value="" style="width: calc(100% - 70px);" placeholder="盘点编码">
					</label>
				</li>

				<li class="field-group field-fluid3">
					<label class="inline" for="state" style="margin-right:20px;width:100%;">
						<span class="form_label" style="width:45px;">状态：</span>
						<select id="state" name="state" style="width:calc(100% - 120px);">
							<option value="">--请选择--</option>
							<option value="0" >未提交</option>
							<option value="1" >已提交</option>
						</select>
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

	<div class="row-fluid" id="table_toolbar" style="padding:5px 3px;" >
		<button class=" btn btn-primary btn-addQx" onclick="addInventory();">
			添加<i class="fa fa-plus" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-editQx" onclick="editInventory();">
			编辑<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-deleteQx" onclick="delInventory();">
			删除<i class="fa fa-trash" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class="col-md-1 btn btn-primary btn-queryQx" onclick="exportInventory();">
			导出<i class="fa fa-share" aria-hidden="true" style="margin-left:5px;"></i>
		</button>
		<button class=" btn btn-primary btn-editQx" onclick="lookInventory();">
			查看<i class="icon-zoom-in" aria-hidden="true" style="margin-left:5px;"></i>
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

    $("#queryForm #state").select2({
        minimumInputLength:0,
        placeholderOption: "first",
        allowClear:true,
        delay:250,
        //  width:200,
        formatNoMatches:"没有结果",
        formatSearching:"搜索中...",
        formatAjaxError:"加载出错啦！"
    });
    $("#queryForm #state").on("change.select2",function(){
        $("#queryForm #state").trigger("keyup")}
    );

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
            url: context_path + "/inventory/getList",
            datatype: "json",
            colNames: ["主键", "盘点编码","盘点任务编码","库位名称","盘点人","盘点时间","状态","创建人","备注"],
            colModel: [
                {name: "id", index: "id", width: 20, hidden: true},
                {name: "inventoryCode", index: "inventoryCode", width: 30},
                {name: "inventoryTaskCode", index: "inventoryTaskCode", width: 30,sortable: false},
                {name: "positionCode", index: "positionCode", width: 30,sortable: false},
                {name: "inventoryName", index: "inventoryName", width: 30,sortable: false},
                {name: "inventoryTime", index: "inventoryTime", width: 30},
                {name: "state",index:"state",width:20,
                    formatter:function(cellValue){
                        if (cellValue==0) {
                            return "<span style='color:#B8A608;font-weight:bold;'>未提交</span>";
                        } else if(cellValue==1) {
                            return "<span style='color:#ACD128;font-weight:bold;'>已提交</span>";
                        }
                    }
                },
                {name: "createName", index: "createName", width: 30,sortable: false},
                {name: "remark", index: "remark", width: 50, sortable: false}
            ],
            rowNum: 20,
            rowList: [10, 20, 30],
            pager: "#grid-pager",
            multiSort: true,
            sortname: "state,inventoryTime",
            sortorder: "desc",
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

    // 查询
    function queryOk(){
        var queryParam = iTsai.form.serialize($("#queryForm"));
        querySceneByParam(queryParam);
    }

    function querySceneByParam(jsonParam){
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
    function reset(){
        $("#queryForm #inventoryCode").val("");
        $("#queryForm #state").val(null).trigger("change");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }

    // 添加
    function addInventory(){
        $.post(context_path+"/inventory/toEdit?id=-1", {}, function(str){
            $queryWindow = layer.open({
                title : "盘点添加",
                type: 1,
                skin: "layui-layer-molv",
                area: ["750px","650px"],
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

    // 修改
    function editInventory(){
        var checkedNum = getGridCheckedNum("#grid-table","id");
        if(checkedNum == 0)
        {
            layer.alert("请选择一个要编辑的盘点信息！");
            return false;
        } else if(checkedNum >1){
            layer.alert("只能选择一个盘点信息进行编辑操作！");
            return false;
        } else {
            var id = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            var state = jQuery("#grid-table").jqGrid('getRowData', id).state;
            if(state == '<span style="color:#ACD128;font-weight:bold;">已提交</span>'){
                layer.msg("盘点记录已提交，不可修改",{time:1200,icon:2});
                return false;
            }
            $.post(context_path+"/inventory/toEdit?id="+id, {}, function(str){
                $queryWindow = layer.open({
                    title : "盘点记录编辑",
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

    // 删除
    function delInventory(){
        var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
        if (checkedNum == 0) {
            layer.alert("请选择一个要删除的盘点信息！");
        } else {
            var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
            layer.confirm("确定删除选中的盘点信息？", function() {
                $.ajax({
                    type : "POST",
                    url : context_path + "/inventory/delInventory?ids="+ids ,
                    dataType : "json",
                    cache : false,
                    success : function(data) {
                        layer.closeAll();
                        if (Boolean(data.result)) {
                            layer.msg(data.msg, {icon: 1,time:1000});
                        }else{
                            layer.msg(data.msg, {icon: 7,time:2000});
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            });

        }
    }

    /**
     * 盘点导出
     */
    function exportInventory(){
        var ids = jQuery("#grid-table").jqGrid("getGridParam", "selarrrow");
        $("#ids").val(ids);
        $("#hiddenForm").submit();
    }

    //查看
    function lookInventory() {
        var checkedNum = getGridCheckedNum("#grid-table", "id");
        if (checkedNum == 0) {
            layer.alert("请选择一个要查看的盘点记录！");
            return false;
        } else if (checkedNum > 1) {
            layer.alert("只能选择一个盘点记录进行查看！");
            return false;
        } else {
            var vehicleId = jQuery("#grid-table").jqGrid("getGridParam", "selrow");
            $.post(context_path + "/inventory/toDetail.do?id=" + vehicleId, {}, function (str) {
                $queryWindow = layer.open({
                    title: "盘点记录查看",
                    type: 1,
                    skin: "layui-layer-molv",
                    area: ["750px", "600px"],
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