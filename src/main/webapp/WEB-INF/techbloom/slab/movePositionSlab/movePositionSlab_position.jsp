<%@ page language="java" import="java.lang.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionId" >移入库位：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span12 required" type="text"  id="positionId" name="positionId">
                        <input type="hidden" id="positionCode" name="positionCode">
                    </div>
                </div>
            </div>
        </div>
        <div style="margin-left:100px;">
            <span class="btn btn-info" id="formSave">
		       <i class="ace-icon fa fa-check bigger-110"></i>确定
            </span>
        </div>
    </form>
</div>
<script type="text/javascript">
    //库位下拉列表数据源
    $("#baseInfor #positionId").select2({
        placeholder: "--选择库位--",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        ajax: {
            url: context_path + "/movePositionSlab/getSelectPosition",
            type: "POST",
            dataType: 'json',
            delay: 250,
            data: function (term, pageNo) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    queryString: term, //联动查询的字符
                    pageSize: 15, //一次性加载的数据条数
                    pageNo: pageNo, //页码
                    time: new Date()
                    //测试
                }
            },
            results: function (data, pageNo) {
                var res = data.result;
                if (res.length > 0) { //如果没有查询到数据，将会返回空串
                    var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
                    return {
                        results: res,
                        more: more
                    };
                } else {
                    return {
                        results: {}
                    };
                }
            },
            cache: true
        }
    });

    //库位下拉框change事件
    $("#baseInfor #positionId").on("change",function(e){
        $("#baseInfor #positionCode").val(e.added.text);
    });


    //收货单表单校验
    $("#baseInfor").validate({
        rules:{
            "positionId":{
                required:true
            }
        },
        messages: {
            "positionId":{
                required:"请选择库位！"
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

    //确定
    $("#formSave").click(function(){
        if($('#baseInfor').valid()){
            $.ajax({
                url:context_path+"/movePositionSlab/confirmPosition",
                type:"post",
                data:$("#baseInfor").serialize(),
                dataType:"JSON",
                success:function (data){
                    if(data.result){
                        layer.msg(data.msg,{icon:1,time:2000});
                        layer.closeAll();
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }else{
                        layer.msg(data.msg,{icon:2,time:2000});
                    }
                }
            });
        }
    });


</script>