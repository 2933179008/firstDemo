<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form action="" class="form-horizontal" id="boxForms" name="materialForms" method="post" target="_ifr">
        <input type="hidden" id="id" name="id" value="${lowerId}" />
        <%--一行数据 --%>
        <div class="row" style="margin:0;padding:0;">
            <%--库位--%>
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="userId" >人员分配：</label>
                <div class="controls">
                    <div class="required span12" style=" float: none !important;">
                        <input type="hidden" class="span10" id="userId" name="userId" />
                        <select class="mySelect2 span10" id="positionCodeSelect" name="positionCodeSelect" data-placeholder="请选择人员进行操作" style="margin-left:0px;">
                            <option value=""></option>
                            <c:forEach items="${userList}" var="area">
                                <option value="${area.id}" >
                                        ${area.username}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>


        <div style="text-align: center">
            <span class="btn btn-info" id="formSaves">
		       <i class="ace-icon fa fa-check bigger-110"></i>保存
            </span>
        </div>
    </form>
</div>
<script type="text/javascript">

    $("#boxForms #formSaves").click(function(){
        $("#formSave").attr("disabled","disabled");
        var ss = $("#userId").val();
        var dd  = $("#id").val();
        $.ajax({
            type:"POST",
            url:context_path + "/lowerShelfBillManager/updateOperation",
            data:{userId:ss,lowerId:dd},
            dataType:"json",
            success:function(data){
                layer.closeAll();
                layer.alert("更新成功");
                gridReload();
                // if(data.result){
                //     layer.alert("单据保存成功");
                //     $("#formSave").removeAttr("disabled");
                //     gridReload();
                //     layer.closeAll();
                // }else{
                //     layer.alert("单据保存失败");
                // }
            }
        })
    });

    //列表刷新
    function gridReload(){
        $("#grid-table").jqGrid('setGridParam',
            {
                url : context_path + '/lowerShelfBillManager/getList',
                postData: {queryJsonString:""} //发送数据  :选中的节点
            }
        ).trigger("reloadGrid");
    }

    /**
     * 下拉列表
     */
    $('#boxForms .mySelect2').select2();

    $("#boxForms #positionCodeSelect").change(function(){
        $('#boxForms #userId').val($('#boxForms #positionCodeSelect').val());
    });
</script>