<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="platform_edit_page" class="row-fluid" style="height: inherit;">
	<form id="materielManagerForm" class="form-horizontal" style="overflow: auto; height: calc(100% - 70px);">
		<input type="hidden" name="id" id="id" value="${supplier.id}" >

		<div class="control-group">
			<label class="control-label" for="supplierCode">供应商编码：</label>

			<c:if test="${status == 1}">
				<div class="controls">
					<div class="input-append span12 required">
						<input type="text" class="span11" id="supplierCode" name="supplierCode"
							   value="${supplier.supplierCode}" placeholder="供应商编码">
					</div>
				</div>
			</c:if>

			<c:if test="${status == 2}">
				<div class="controls">
					<div class="input-append span12 required">
						<input type="text" class="span11"  name="supplierCode"
							   value="${supplier.supplierCode}" placeholder="供应商编码" readonly="true">
					</div>
				</div>
			</c:if>

		</div>

		<div class="control-group">
			<label class="control-label" for="supplierName">供应商名称：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<input type="text" class="span11" id="supplierName" name="supplierName" value="${supplier.supplierName}"  placeholder="供应商名称">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="supplierType">供应商类别：</label>
			<div class="controls">
				<div class="input-append span12 required">
					<select id="supplierType" name="supplierType" style="width:91%;">
						<option value="">请选择</option>
						<option value="原厂"
								<c:if test="${'原厂' eq supplier.supplierType}">selected</c:if> >原厂
						</option>
						<option value="代理"
								<c:if test="${'代理' eq supplier.supplierType}">selected</c:if> >代理
						</option>
						<option value="经营商"
								<c:if test="${'经营商' eq supplier.supplierType}">selected</c:if> >经营商
						</option>
					</select>
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="linkman">联系人：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="linkman" name="linkman" value="${supplier.linkman}"  placeholder="联系人">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="telephone">联系电话：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="telephone" name="telephone" value="${supplier.telephone}"  placeholder="联系电话">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="address">地址：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="address" name="address" value="${supplier.address}"  placeholder="地址">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="mail">邮箱：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="mail" name="mail" value="${supplier.mail}"  placeholder="邮箱">
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="remark">备注：</label>
			<div class="controls">
				<div class="input-append span12required">
					<input type="text" class="span11" id="remark" name="remark" value="${supplier.remark}"  placeholder="备注">
				</div>
			</div>
		</div>

	</form>
	<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
		<span class="savebtn btn btn-success" onclick="saveSupplier()">保存</span>
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
            /*供应商编码*/
            "supplierCode" : {
                required:true,
                maxlength:7,
                remote: context_path + "/supplierManage/hasC?id="+$('#id').val()
            },
            /*供应商名称*/
            "supplierName":{
                required:true,
                maxlength:255
            },
            /*供应商类别*/
            "supplierType":{
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
            "supplierCode" : {
                required:"请输入供应商编码",
                remote: "您输入的供应商编码已经存在，请重新输入！"
            },
            "supplierName":{
                required:"请输入供应商名称",
            }
            // "supplierType":{
            //     required:"请输入供应商类别"
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
    function saveSupplier() {
        var roleParam = $("#materielManagerForm").serialize();
        if($("#materielManagerForm" +
            "").valid()){
            $(".savebtn").attr("disabled","disabled");
            $.ajax({
                url:context_path+"/supplierManage/addSupplier?tm="+new Date(),
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