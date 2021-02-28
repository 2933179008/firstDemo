<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="id" id="id" value="" >
		<input type="hidden" id="taskId" name="taskId" value="" >

		<div class="control-group">
			<label class="control-label" for="">出/入库单编号：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input type="text" class="span11" id="" name="" value=""  placeholder="出/入库单编号">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="">业务类型：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<select id="" name=""  style="width:91%;">
						<option value="0">请选择</option>
						<option value="1">采购入库</option>
						<option value="2">委托加工入库</option>
						<option value="3">其他出库</option>
					</select>
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="">出/入库类型：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<select id="" name=""  style="width:91%;">
						<option value="0">请选择</option>
						<option value="1">出库</option>
						<option value="2">入库</option>
					</select>
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="">描述：</label>
			<div class="controls">
				<div class="input-append span12required">
					<textarea type="text" id="" name="" value="" placeholder="描述" class="span11"></textarea>
				</div>
			</div>
		</div>

	</form>
	<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
		<span class="savebtn btn btn-success" onclick="saveTaskDetail()">保存</span>
		<span class="btn btn-danger" onclick="closeWindow()">取消</span>
	</div>
</div>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 表单校验
     * @param jsonParam
     */
    $("#materielManagerForm").validate({
        rules:{
            /*零件编号*/
            "materielCode" : {
                required:true,
                maxlength:16
            },
            /*零件名称*/
            "materielName":{
                required:true,
                maxlength:20
            },
            /*数量*/
            "amount":{
                required:true,
                maxlength:20
            },
            /*拣选状态*/
            "state":{
                number:true,
                maxlength:16
            },
            /*拣选开始时间*/
            "pickStartTime":{
                required:true,
                maxlength:20
            },
            /*拣选结束时间*/
            "pickEndTime":{
                required:true,
                maxlength:20
            }
        },
        messages:{
            "materielCode" : {
                required:"请输入零件编号"
            },
            "materielName":{
                required:"请输入零件名称",
            },
            "amount":{
                required:"请输入数量"
            },
            "state":{
                required:"请输入拣选状态"
            },
            "pickStartTime":{
                required:"请输入拣选开始时间"
            },
            "pickEndTime":{
                required:"请输入拣选结束时间"
            }
        },
        errorClass: "help-inline",
        errorElement: "span",
        highlight:function(element, errorClass, validClass) {
            $(element).parents('.control-group').addClass('error');
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parents('.control-group').removeClass('error');
        }
    });

    /**
     * 保存/修改
     * @param jsonParam
     */
	function saveTaskDetail() {
        var roleParam = $("#materielManagerForm").serialize();
        if($("#materielManagerForm" +
			"").valid()){
            $(".savebtn").attr("disabled","disabled");
            $.ajax({
                url:context_path+"/taskManager/addDetailTask?tm="+new Date(),
                type:"POST",
                data:roleParam,
                dataType:"JSON",
                success:function(data){
                    ajaxStatus = 1; //将标记设置为可请求
                    if(data){
                        //刷新表格
                        $("#grid-table-c").jqGrid('setGridParam',
                            {
                                postData: {queryJsonString:""} //发送数据
                            }
                        ).trigger("reloadGrid");
                        layer.msg("操作成功！",{icon:1});
                        layer.close($queryWindow);
                    }else{
                        layer.alert("操作失败！",{icon:2});
                    }
                }
            });
        }
    }

    /**
     * 关闭弹出框
     * @param jsonParam
     */
	function closeWindow(){
		layer.close($queryWindow);
	}
</script>