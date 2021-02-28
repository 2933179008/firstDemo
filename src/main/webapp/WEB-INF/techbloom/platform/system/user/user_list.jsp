<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<!-- 表格 -->
<div class="row-fluid" id="grid-div"
     style="position:relative;margin-top: 0px;    float: left;">
    <form id="hiddenForm" action="<%=path%>/user/toExcel" method="POST" style="display: none;">
        <!-- 选中的用户 -->
        <input id="ids" name="ids" value=""/>
    </form>
    <!-- 工具栏 -->
    <div class="query_box" id="yy" title="查询选项">
        <form id="query_form" style="max-width:  100%;">
            <ul class="form-elements">
                <!-- 货架编号 -->
                <li class="field-group field-fluid3">
                    <label class="inline" for="username" style="margin-right:20px;width:  100%;">
                        <span class='form_label'>用户名：</span>
                        <input type="text" name="username" id="username" value="" style="width: calc(100% - 75px);"
                               placeholder="用户名">
                    </label>

                </li>
                <!-- 货架名称 -->
                <li class="field-group field-fluid3">
                    <label class="inline" for="name" style="margin-right:20px;width:  100%;">
                        <span class='form_label'>姓名：</span>
                        <input type="text" name="name" id="name" value="" style="width: calc(100% - 75px);"
                               placeholder="姓名">
                    </label>

                </li>
                <li class="field-group field-fluid3">
                    <label class="inline" for="email" style="margin-right:20px;width:  100%;">
                        <span class='form_label'>邮箱：</span>
                        <input type="text" name="email" id="email" value="" style="width: calc(100% - 75px);"
                               placeholder="邮箱">
                    </label>

                </li>
                <!-- 第二行 3个li标签 ，第一个li标签的class要加 field-group-top  ，  -->
                <li class=" field-group-top field-group field-fluid3">
                    <label class="inline" for="phone" style="margin-right:20px;width:  100%;">
                        <span class='form_label'>手机号：</span>
                        <input type="text" name="phone" id="phone" value="" style="width: calc(100% - 75px);"
                               placeholder="手机号">
                    </label>

                </li>


            </ul>
            <div class="field-button" style="">
                <div class="btn btn-info" onclick="openQueryPage();">
                    <i class="ace-icon fa fa-check bigger-110"></i>查询
                </div>
                <div class="btn" onclick="reset();"><i class="ace-icon icon-remove"></i>重置</div>
                <a style="margin-left: 8px;color: #40a9ff;" class="toggle_tools">收起 <i class="fa fa-angle-up"></i></a>
            </div>
        </form>

    </div>
    <div class="row-fluid" id="table_toolbar" style="padding:5px 3px;">
        <button class=" btn btn-primary btn-addQx" onclick="openAddPage();">
            添加<i class="fa fa-plus" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="openEditPage();">
            编辑<i class="fa fa-pencil" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-deleteQx" onclick="deleteUser();">
            删除<i class="fa fa-trash" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="disabledUser();">
            禁用<i class="fa fa-close" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class=" btn btn-primary btn-editQx" onclick="enabledUser();">
            启用<i class="fa fa-check" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
        <button class="col-md-1 btn btn-primary btn-queryQx" onclick="exportLogFile();">
            导出<i class="fa fa-share" aria-hidden="true" style="margin-left:5px;"></i>
        </button>
    </div>
    <!-- 表格 -->
    <div class="row-fluid" style="padding:0 3px;">
        <!-- 表格数据 -->
        <table id="grid-table" style="width:100%;"></table>
        <!-- 表格底部 -->
        <div id="grid-pager"></div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    $(function () {
              $(".toggle_tools").click();
    });
</script>
<script src="<%=request.getContextPath()%>/static/techbloom/system/user/user.js"></script>
