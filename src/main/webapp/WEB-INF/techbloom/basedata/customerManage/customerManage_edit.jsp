<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="customerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="id" id="id" value="${customer.id}" >

		<div class="control-group">
			<label class="control-label" for="customerCode">客户编码：</label>
			<c:if test="${status == 1}">
				<div class="controls">
					<div class="input-append span12 required">
						<input type="text" class="span11" id="customerCode" name="customerCode"
							   value="${customer.customerCode}" placeholder="客户编码">
					</div>
				</div>
			</c:if>

			<c:if test="${status == 2}">
				<div class="controls">
					<div class="input-append span12 required">
						<input type="text" class="span11"  name="customerCode"
							   value="${customer.customerCode}" placeholder="客户编码" readonly="true">
					</div>
				</div>
			</c:if>

		</div>

		<div class="control-group">
			<label class="control-label" for="customerName">客户名称：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input type="text" class="span11" id="customerName" name="customerName" value="${customer.customerName}"  placeholder="客户名称">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="customerType">客户类型：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="customerType" name="customerType" value="${customer.customerType}"  placeholder="客户类型">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="linkman">联系人：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="linkman" name="linkman" value="${customer.linkman}"  placeholder="联系人">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="telephone">联系电话：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="telephone" name="telephone" value="${customer.telephone}"  placeholder="联系电话">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="address">地址：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="address" name="address" value="${customer.address}"  placeholder="地址">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="mail">邮箱：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="mail" name="mail" value="${customer.mail}"  placeholder="邮箱">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="remark">备注：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="remark" name="remark" value="${customer.remark}"  placeholder="备注">
				</div>
			</div>
		</div>

	</form>
	<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
		<span class="savebtn btn btn-success" onclick="saveCustomer()">保存</span>
		<span class="btn btn-danger" onclick="closeWindow()">取消</span>
	</div>
</div>
<script type="text/javascript">
    $(".date-picker").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    /**
     * 表单校验
     * @param jsonParam
     */
    $("#customerForm").validate({
        rules:{
            /*客户编码*/
            "customerCode" : {
                required:true,
                maxlength:9,
                remote: context_path + "/customerManage/hasC?id="+$('#id').val()
            },
            /*客户名称*/
            "customerName":{
                required:true,
                maxlength:255
            },
            /*客户类型*/
            "customerType":{
                required:false,
                maxlength:50
            },
            /*联系人*/
            "linkman":{
                required:false,
                maxlength:50
            },
            /*联系电话*/
            "telephone":{
                required:false,
                mobile: true,
                maxlength:50
            },
            /*地址*/
            "address":{
                required:false,
                maxlength:255
            },
            /*邮箱*/
            "mail":{
                required:false,
                email : true,
                maxlength:50
            },
            /*备注*/
            "remark":{
                required:false,
                maxlength:255
            }
        },
        messages:{
            "customerCode" : {
                required:"请输入客户编码",
                remote: "您输入的客户编码已经存在，请重新输入！"
            },
            "customerName":{
                required:"请输入客户名称",
            }
            // "customerType":{
            //     required:"请输入客户类型"
            // }
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
	function saveCustomer() {
        var roleParam = $("#customerForm").serialize();
        if($("#customerForm" + "").valid()){
            $(".savebtn").attr("disabled","disabled");
            $.ajax({
                url:context_path+"/customerManage/addCustomer?tm="+new Date(),
                type:"POST",
                data:roleParam,
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

    /**
     * 关闭弹出框
     * @param jsonParam
     */
	function closeWindow(){
		layer.close($queryWindow);
	}
</script>