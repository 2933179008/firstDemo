<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="jobForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="jobId" id="jobId" value="${schedule.jobId}" >
		<input type="hidden" name="status" id="status" value="${schedule.status}" >
		<input type="hidden" name="createTime" id="createTime" value="${schedule.createTime}" >

		<div class="control-group">
			<label class="control-label" for="beanName">bean名称：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input type="text" class="span11" id="beanName" name="beanName" value="${schedule.beanName}" placeholder="bean名称">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="methodName">方法名称：</label>
			<div class="controls">
				<div class="input-append span12 required ">
					<input type="text" class="span11" id="methodName" name="methodName" value="${schedule.methodName}" placeholder="方法名称">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="params">参数：</label>
			<div class="controls">
				<input type="text" class="span11" id="params" name="params" value="${schedule.params}" placeholder="参数">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="cronExpression">cron表达式：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input type="text" class="span11" id="cronExpression" name="cronExpression" value="${schedule.cronExpression}" placeholder="如：0 0 12 * * ?">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="remark">备注：</label>
			<div class="controls">
				<textarea class="input-xlarge" name="remark" id="remark" placeholder="备注" style="width:435px;">${schedule.remark}</textarea>
			</div>
		</div>
	</form>
	<div class="field-button" style="text-align: center;border-top: 0px;margin: 15px auto;">
		<span class="savebtn btn btn-success" onclick="saveJob()">保存</span>
		<span class="btn btn-danger" onclick="closeWindow()">取消</span>
	</div>
</div>
<script type="text/javascript">

    //表单校验
    $("#jobForm").validate({
        rules:{
            /*bean名称*/
            "beanName" : {
                required:true
            },
			/*方法名称*/
            "methodName":{
                required:true
            },
            /*cron表达式*/
            "cronExpression":{
                required:true
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

	//保存/修改定时任务
	function saveJob() {
        var jobParam = $("#jobForm").serialize();
        if($("#jobForm").valid()){
            $(".savebtn").attr("disabled","disabled");
            $.ajax({
                url:context_path+"/schedule/save?tm="+new Date(),
                type:"POST",
                data:jobParam,
                dataType:"JSON",
                success:function(data){
                    ajaxStatus = 1; //将标记设置为可请求
                    if(data){
                        //刷新表格
                        $("#grid-table").jqGrid('setGridParam',
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

	//关闭弹出框
	function closeWindow(){
		layer.close($queryWindow);
	}
</script>