﻿
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 表格 -->
<div class="query_box" id="yy" title="查询选项">
    <form id="query_form" class="form-horizontal" style="max-width:100%;">
        <ul class="form-elements">
            <li class="field-group field-fluid2">
                <label class="inline" style="margin-right:20px;width:100%;">
                    <span class="form_label" style="width:65px;">角色名：</span>
                    <input class="span12" type="text" onkeydown="if(event.keyCode==13) return false;" name="roleName" id="roleName" placeholder="角色名" style="width: calc(60%);"/>
                </label>
            </li>
        </ul>
        <div class="field-button">
            <div class="btn btn-info" onclick="openQueryPage();">
                <i class="ace-icon fa fa-check bigger-110"></i>查询
            </div>
            <div class="btn" onclick="reset();"><i class="ace-icon icon-remove" id="btn"></i>重置</div>
        </div>
    </form>
</div>
<!-- 在按钮上都添加上btn-addQX用来标记列表中的操作按钮，方便控制按钮的权限 -->
<div class="row-fluid" id="table_toolbar" style="padding:5px 3px;" >
    <button class=" btn btn-primary btn-addQx" onclick="openAddPage();">
        添加<i class="fa fa-plus" aria-hidden="true" style="margin-left:5px;"></i>
    </button>
    <button class=" btn btn-primary btn-editQx" onclick="openEditPage();">
        编辑<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
    </button>
    <button class=" btn btn-primary btn-deleteQx" onclick="deleteRow();">
        删除<i class="fa fa-trash" aria-hidden="true" style="margin-left:5px;"></i>
    </button>
</div>    <!-- 表格 -->
    <div class="row-fluid" style="padding:0 3px;">
        <!-- 表格数据 -->
        <table id="grid-table" style="width:100%;"></table>
        <!-- 表格底部 -->
        <div id="grid-pager"></div>
    </div>
</div>
<script src="<%=request.getContextPath()%>/static/techbloom/system/role/role.js"></script>

<!--重置-->
<script type="text/javascript">
    function reset(){
        $("#query_form #roleName").val("");
        $("#grid-table").jqGrid("setGridParam",
            {
                postData: {queryJsonString:""} //发送数据
            }
        ).trigger("reloadGrid");
    }
</script>