<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<link rel="stylesheet" href="<%=path%>/static/techbloom/system/scene/css/index.css"/>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
	<div class="step1">
		<div class="flex-row justify-content-around step_title">
			<div class="flex-row align-items-center justify-content-end step_name active">
				<i class="instorage_circle">1</i>
				<span>填写收货计划信息</span>
			</div>
			<hr class="step_line"/>
			<div class="flex-row align-items-center justify-content-start step_name">
				<i class="instorage_circle">2</i>
				<span>添加物料</span>
			</div>
		</div>
	<form id="baseInfor" class="form-horizontal" target="_ifr">
			<!-- 收货单主键 -->
  	        <input type="hidden" id="id" name="id" value="${receipt.id}">
  	        <%--一行数据 --%>
	        <div class="row" style="margin:0;padding:0;">
	            <%--收货编号--%>
	            <div class="control-group span6" style="display: inline">
	                <label class="control-label" for="receiptCode" >收货编号：</label>
	                <div class="controls">
	                    <div class="input-append  span12" >
	                        <input type="text" id="receiptCode" class="span10" name="receiptCode"   placeholder="后台自动生成" readonly="readonly" value="${receiptCode}"/>
	                    </div>
	                </div>
	            </div>
				<%--预计收货日期--%>
				<div class="control-group span6" style="display: inline">
					<label class="control-label" for="estimatedDeliveryTime" >预计收货时间：</label>
					<div class="controls">
						<div class="span12" >
							<input class="form-control date-picker" id="estimatedDeliveryTime" name="estimatedDeliveryTime" type="text" value="${receipt.estimatedDeliveryTime}" placeholder="预计收货时间" />
						</div>
					</div>
				</div>
	        </div>
  	        <div class="row" style="margin:0;padding:0;">
				<%--供应商--%>
				<div class="control-group span6" style="display: inline">
					<label class="control-label" for="supplierCode" >供应商：</label>
					<div class="controls">
						<div class="span12 required" style=" float: none !important;">
							<input class="span10 select2_input" type="text"  id="supplierCode" name="supplierCode" value="${receipt.supplierCode}">
							<input type="hidden" id="supplierName" name="supplierName" value="${receipt.supplierName}">
							<%--<input type="hidden" id="supplier" name="supplier" value="${receipt.supplierCode}">--%>
						</div>
					</div>
				</div>
				<%--货主--%>
				<div class="control-group span6" style="display: inline">
					<label class="control-label" for="customerCode" >货主：</label>
					<div class="controls">
						<div class="span12 required" style=" float: none !important;">
							<input class="span10 select2_input" type="text" id="customerCode" name="customerCode" value="${receipt.customerCode}">
							<input type="hidden"  id="customerName" name="customerName" value="${receipt.customerName}">
							<%--<input type="hidden"  id="customer" name="customer" value="${receipt.customerCode}">--%>
						</div>
					</div>
				</div>
			</div>
  	    	<div class="row" style="margin:0;padding:0;">
				<%--单据类型--%>
				<div class="control-group span6" style="display: inline">
					<label class="control-label" for="documentType" >单据类型：</label>
					<div class="controls">
						<div class="input-append span12 required" >
							<select name="documentType" id="documentType"  value="${receipt.documentType}" style="width: calc(100% - 40px);">
								<option value="0" <c:if test="${'0' eq receipt.documentType}">selected</c:if> >自采</option>
								<option value="1" <c:if test="${'1' eq receipt.documentType}">selected</c:if> >客供</option>
							</select>
						</div>
					</div>
				</div>
				<%--备注--%>
				<div class="control-group span6" style="display: inline">
					<label class="control-label" for="remark" >备注：</label>
					<div class="controls">
						<div class="input-append span12" >
							<input class="span10" type="text" id="remark" name="remark" placeholder="备注" value="${receipt.remark}">
						</div>
					</div>
				</div>
        	</div>
		<div class="flex-row justify-content-end" style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
            <span class="btn btn-info" id="formSave">
		       <i class="ace-icon fa fa-check bigger-110"></i>保存
            </span>
            <%--<span class="btn btn-info" id="formSubmit">
		        <i class="ace-icon fa fa-check bigger-110"></i>&nbsp;提交
            </span>--%>
			<span class="btn btn-default disabled" id="next_step" style="margin-left: 5px">
				<i class="ace-icon fa fa-arrow-right bigger-110"></i>下一步
			</span>
        </div> 
  	   </form>
</div>
	<div class="step2" style="overflow-y: hidden">
		<div class="flex-row justify-content-around step_title">
			<div class="flex-row align-items-center justify-content-end step_name">
				<i class="instorage_circle badge-ok"><i class="ace-icon fa fa-check bigger-110"></i></i>
				<span>填写收货计划信息</span>
			</div>
			<hr class="step_line badge-ok"/>
			<div class="flex-row align-items-center justify-content-start step_name active">
				<i class="instorage_circle">2</i>
				<span>添加物料</span>
			</div>
		</div>
		<div id="materialDiv" style="margin:10px 5px;">
			<!-- 下拉框 -->
			<label class="inline" for="materialInfor">物料：</label>
			<input type="text" id = "materialInfor" name="materialInfor" style="width:350px;margin-right:10px;" />
			<button id="addMaterialBtn" class="btn btn-xs btn-primary" onclick="addDetail();">
				<i class="icon-plus" style="margin-right:6px;"></i>添加
			</button>
		</div>
		<!-- 表格div -->
		<div id="grid-div-c" style="width:100%;margin:0px;">
			<!-- 	表格工具栏 -->
			<button id="btchdel" class="btn btn-info" onclick="delDetail();" style="float: right;
            margin-bottom: 0.5rem;">
				<i class="icon-minus" style="margin-right:6px;"></i>批量删除
			</button>
			<div style="clear: both;"></div>
			<!-- 物料详情信息表格 -->
			<div style="margin-top: 5px"><table id="grid-table-c" style="width:100%;height:100%;"></table></div>

			<!-- 表格分页栏 -->
			<div id="grid-pager-c"></div>
		</div>
		<div class="flex-row justify-content-end" style="margin-right:1rem;margin-top:2rem;position: absolute;right: 0;bottom: 1rem;">
			<%--<span class="btn btn-info" id="formSave">--%>
			<%--<i class="ace-icon fa fa-check bigger-110"></i>保存--%>
			<%--</span>--%>
			<span class="btn btn-info" id="formSubmit">
				<i class="ace-icon fa fa-check bigger-110"></i>&nbsp;审核并提交
			</span>
			<span class="btn btn-info ml-3" id="pre_step">
				<i class="ace-icon fa fa-arrow-left bigger-110"></i>&nbsp;上一步
			</span>
		</div>
	</div>

</div>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<script type="text/javascript" src="<%=path%>/static/techbloom/instorage/receiptManage/receiptManage_edit.js"></script>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: 'YYYY-MM-DD HH:mm:ss',useMinutes:true,useSeconds:true});
    var receiptId=$("#baseInfor #id").val();
    var edit = "${edit}";
    var oriDataDetail;
    var _grid_detail;

    _grid_detail=jQuery("#grid-table-c").jqGrid({
        url : context_path + "/receiptManage/getDetailList?receiptId="+receiptId,
        datatype : "json",
        colNames : [ "详情主键","物料编号","物料名称","包装单位","计划收货数量","计划收货重量（kg）","操作"],
        colModel : [
            {name : "id",index : "id",width : 20,hidden:true},
            {name : "materialCode",index:"material_Code",width :20},
            {name : "materialName",index:"material_Name",width : 20},
            {name : "unit",index:"unit",width : 20},
            {name: 'planReceiptAmount', index: 'plan_Receipt_Amount', width: 20, editable: true,
                editrules: {custom: true, custom_func: numberRegex},
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
                            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
                            if (!reg.test($("#" + $elementId).val())) {
                                layer.alert("非法的数量！(注：可以有两位小数的正实数)");
                                return;
                            }
                            $.ajax({
                                url: context_path + '/receiptManage/updatePlanReceiptAmount',
                                type: "POST",
                                data: {
                                    receiptDetailId: id,
                                    planReceiptAmount: $("#" + rowid + "_planReceiptAmount").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId:$("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }
            },
            {name : "planReceiptWeight",index:"plan_Receipt_Weight",width : 30, editable: true,
                editoptions: {
                    size: 25,
                    dataEvents: [{
                        type: 'blur',     //blur,focus,change.............
                        fn: function (e) {
                            var $element = e.currentTarget;
                            var $elementId = $element.id;
                            var rowid = $elementId.split("_")[0];
                            var id = $element.parentElement.parentElement.children[1].textContent;
							var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
							if (!reg.test($("#" + $elementId).val())) {
								layer.alert("非法的重量！(注：可以有两位小数的正实数)");
								return;
							}
                            $.ajax({
                                url: context_path + '/receiptManage/updatePlanReceiptWeight',
                                type: "POST",
                                data: {
                                    receiptDetailId: id,
                                    planReceiptWeight: $("#" + rowid + "_planReceiptWeight").val()
                                },
                                dataType: "json",
                                success: function (data) {
                                    if (!data.result) {
                                        layer.alert(data.msg);
                                    }
                                    $("#grid-table-c").jqGrid("setGridParam", {
                                        postData: {receiptId:$("#baseInfor #id").val()} //发送数据  :选中的节点
                                    }).trigger("reloadGrid");
                                }
                            });
                        }
                    }]
                }

			}
			,{name : "del",index:"del",width : 20,formatter:function(cellvalue, options, rowObject){
                    var id = rowObject.id;
					return "<a onclick='delOneDetail("+id+")' style='text-decoration:underline;display: block;'>删除</a>"
                }
        	}
        ],
        rowNum : 20,
        rowList : [ 10, 20, 30 ],
        pager : "#grid-pager-c",
        sortname : "materialCode",
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
                    url:context_path + '/receiptManage/getDetailList?receiptId='+receiptId,
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }
    });

    $(window).on("resize.jqGrid", function () {
        $("#grid-table-c").jqGrid("setGridWidth", 750-3 );
        var height = $(".layui-layer-title",_grid_detail.parents(".layui-layer")).height()+
            $("#baseInfor").outerHeight(true)+$("#materialDiv").outerHeight(true)+
            $("#grid-pager-c").outerHeight(true)+$("#fixed_tool_div.fixed_tool_div.detailToolBar").outerHeight(true)+
            //$("#gview_grid-table-c .ui-jqgrid-titlebar").outerHeight(true)+
            $("#gview_grid-table-c .ui-jqgrid-hbox").outerHeight(true);
        $("#grid-table-c").jqGrid('setGridHeight',_grid_detail.parents(".layui-layer").height()-height-200);
    });
    $(window).triggerHandler("resize.jqGrid");
	$(function () {
		if(edit=="edit"){
			if($('#baseInfor').valid()){
				setNextBtn();
			}
		}
	});
</script>