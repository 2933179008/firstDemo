<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;">
    <form id="bomDetailForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" id="id" name="id" value="${bomDetail.id}">
        <input type="hidden" id="bomId" name="bomId" value="${bomId}">
        <div class="control-group">
            <label class="control-label" for="materialCode">物料编号：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="materialCode" name="materialCode" value="${bomDetail.materialCode}"  placeholder="物料编号">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="materialName">物料名称：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="materialName" name="materialName" value="${bomDetail.materialName}"  placeholder="物料名称">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="materialType">物料类型：</label>
            <div class="controls">
                <div class="input-append span12">
                    <input type="text" class="span11" id="materialType" name="materialType" value="${bomDetail.materialType}"  placeholder="物料类型">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="amount">数量：</label>
            <div class="controls">
                <div class="input-append span12">
                    <input type="text" class="span11" id="amount" name="amount" value="${bomDetail.amount}"  placeholder="数量">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="weight">重量：</label>
            <div class="controls">
                <div class="input-append span12">
                    <input type="text" class="span11" id="weight" name="weight" value="${bomDetail.weight}"  placeholder="重量">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="unitPrice">单价：</label>
            <div class="controls">
                <div class="input-append span12">
                    <input type="text" class="span11" id="unitPrice" name="unitPrice" value="${bomDetail.unitPrice}"  placeholder="单价">
                </div>
            </div>
        </div>

    </form>
    <div class="field-button" style="text-align: center;border-top: 0px;margin: 15px auto;">
		<span class="btn btn-info" onclick="saveDetailForm()">
		   <i class="ace-icon fa fa-check bigger-110"></i>保存
		</span>
        <span class="btn btn-danger" id="cancelId">
		   <i class="icon-remove"></i>&nbsp;取消
		</span>
    </div>
</div>
<script type="text/javascript">
    /**
     * 表单校验
     * @param jsonParam
     */
    $("#bomDetailForm").validate({
        rules:{
            "materialCode" : {
                required:true
            },
            "materialName":{
                required:true
            }
        },
        messages:{
            "materialCode" : {
                required:"请输入物料编号"
            },
            "materialName":{
                required:"请输入物料名称",
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

    $("#cancelId").on("click",function(){
        layer.close(childDiv);
    });

    //保存
    function saveDetailForm() {
        if ($("#bomDetailForm").valid()) {
            $.ajax({
                url: context_path + "/bomManage/saveDetail",
                type: "POST",
                data: $("#bomDetailForm").serialize(),
                dataType: "JSON",
                success: function (data) {
                    if (data) {
                        layer.msg("操作成功！",{icon:1});

                    } else {
                        layer.alert("操作失败！",{icon: 2});
                    }
                    _grid_c.trigger("reloadGrid");  //重新加载表格
                    layer.close(childDiv);
                },
                error:function(XMLHttpRequest){
                    alert(XMLHttpRequest.readyState);
                    alert("出错啦！！！");
                }
            });
        }
    }

</script>