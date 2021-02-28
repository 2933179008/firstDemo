<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="bom_edit_page" class="row-fluid" style="height: inherit;">
    <form id="bomManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
        <input type="hidden" name="id" id="id" value="${bom.id}" >
        <div class="control-group">
            <label class="control-label" for="bomCode">bom编号：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <%--<input type="text" class="span11" id="bomCode" name="bomCode" value="${bom.bomCode}"  placeholder="bom编号">--%>
                    <%--<c:choose>--%>
                        <%--<c:when test="${bom.id != null}">--%>
                            <%--<input type="text" readonly class="span11" id="bomCode" name="bomCode" value="${bomCode}"  placeholder="bom编号">--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>
                            <%--<input type="text" class="span11" id="bomCode" name="bomCode" placeholder="bom编号">--%>
                        <%--</c:otherwise>--%>
                    <%--</c:choose>--%>
                        <input type="text" class="span11" id="bomCode" name="bomCode"
                               value="${bomCode}" placeholder="后台自动生成" readonly="readonly">

                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="bomName">bom名称：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="bomName" name="bomName" value="${bom.bomName}"  placeholder="bom名称">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="productName">产品名称：</label>
            <div class="controls">
                <div class="input-append span12 required">
                    <input type="text" class="span11" id="productName" name="productName" value="${bom.productName}"  placeholder="产品名称">
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="remark">备注：</label>
            <div class="controls">
                <div class="input-append span12">
                    <input type="text" class="span11" id="remark" name="remark" value="${bom.remark}"  placeholder="备注">
                </div>
            </div>
        </div>

    </form>
    <div class="field-button" style="text-align: center;border-top: 0px;margin: 15px auto;">
        <span class="savebtn btn btn-success" onclick="savePlatform()">保存</span>
        <span class="btn btn-danger" onclick="closeWindow()">取消</span>
    </div>
</div>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 表单校验
     * @param jsonParam
     */
    $("#bomManagerForm").validate({
        rules:{
            /*Bom编号*/
            "bomCode" : {
                required:true,
                maxlength:20,
                remote: context_path + "/bomManage/hasC?id="+$('#id').val()
            },
            /*Bom名称*/
            "bomName" : {
                required:true,
                maxlength:20
            },
            /*产品名称*/
            "productName":{
                required:true,
                maxlength:20
            }
        },
        messages:{
            "bomCode" : {
                required:"请输入Bom编号",
                remote: "您输入的Bom编号已经存在，请重新输入！"
            },
            "bomName" : {
                required:"请输入Bom名称"
            },
            "productName":{
                required:"请输入产品名称",
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
    function savePlatform() {
        if ($("#bomManagerForm").valid()) {
            $.ajax({
                url: context_path + "/bomManage/saveBom",
                type: "POST",
                data: $("#bomManagerForm").serialize(),
                dataType: "JSON",
                success: function (data) {
                    if(data){
                        layer.msg("操作成功！", {icon: 1});
                    }else{
                        layer.msg("操作失败！");
                    }
                    layer.close($queryWindow);
                    _grid.trigger("reloadGrid");  //重新加载表格
                },
                error:function(XMLHttpRequest){
                    alert(XMLHttpRequest.readyState);
                    alert("出错啦！！！");
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