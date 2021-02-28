<%@ page language="java" import="java.lang.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<div class="row-fluid" style="height: inherit;margin:0px;border: 0px">
    <form id="baseInfor" class="form-horizontal" target="_ifr">
        <input type="hidden" id="area" name="area" value="${area}">
        <div class="row" style="margin:0;padding:0;">
            <div class="control-group span6" style="display: inline">
                <label class="control-label" for="positionCode">库位：</label>
                <div class="controls">
                    <div class="span12" style=" float: none !important;">
                        <input class="span12" type="text" id="positionCode" name="positionCode" style="width: 150px">
                        <input type="hidden" id="positionName" name="positionName">
                    </div>
                </div>
            </div>
        </div>
        <div style="margin-left:140px;">
            <span class="btn btn-info" id="formSave">
		       <i class="ace-icon fa fa-check bigger-110"></i>确定
            </span>
        </div>
    </form>
</div>
<script type="text/javascript">
    //隐藏弹出框的关闭按钮
    $('.layui-layer-close1').css('display', 'none');

    //库位下拉列表数据源
    $("#baseInfor #positionCode").select2({
        placeholder: "--选择库位--",
        minimumInputLength: 0, //至少输入n个字符，才去加载数据
        allowClear: true, //是否允许用户清除文本信息
        delay: 250,
        formatNoMatches: "没有结果",
        formatSearching: "搜索中...",
        formatAjaxError: "加载出错啦！",
        ajax: {
            url: context_path + "/instorageSlab/getSelectPosition",
            type: "POST",
            dataType: 'json',
            delay: 250,
            data: function (term/*, pageNo*/) { //在查询时向服务器端传输的数据
                term = $.trim(term);
                return {
                    queryString: term, //联动查询的字符
                    // pageSize: 15, //一次性加载的数据条数
                    // pageNo: pageNo, //页码
                    time: new Date()
                    //测试
                }
            },
            results: function (data, pageNo) {
                var res = data.result;
                if (res.length > 0) { //如果没有查询到数据，将会返回空串
                    // var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
                    return {
                        results: res
                        // more: more
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
    $("#baseInfor #positionCode").on("change", function (e) {
        $("#baseInfor #positionName").val(e.added.text);
    });


    //确定
    $("#formSave").click(function () {
        var positionCode = $("#positionCode").val();
        if (positionCode == null || positionCode == undefined || positionCode == '') {
            layer.msg("请先选择库位", {icon: 2, time: 1200});
        } else {
            $.ajax({
                url: context_path + "/instorageSlab/confirmPosition",
                type: "post",
                data: $("#baseInfor").serialize(),
                dataType: "JSON",
                success: function (data) {
                    if (data.result) {
                        // if(data.code=="0"){//如果定位与库位一致，则直接上架，更新平板参数绑定表的库位编号
                        //     submitPosition(positionCode);
                        // }else if(data.code=="1"){//如果不一致，给提示框
                        //     layer.confirm("定位与所选库位不一致，确定上架？", function() {
                        //         submitPosition(positionCode);
                        //     });
                        // }
                        submitPosition(positionCode);
                    } else {
                        layer.msg("系统错误", {icon: 2, time: 1200});
                    }
                }
            });
            //更新slab_bill_bunding表的alert_key字段为3（已确定库位）
            updateAlertToUp();
            //重新开启定时器
            interval1 = window.setInterval(pollingBackground, 2000);
            interval2 = window.setInterval(pollingBackground2, 2000);
        }
    });

    //更新平板入库操作参数绑定关系表的字段
    function updateAlertToUp() {
        $.ajax({
            type: "POST",
            url: context_path + "/instorageSlab/updateAlertToUp",
            dataType: 'json',
            cache: false,
            success: function (data) {
            }
        });
    }

    function submitPosition(positionCode) {
        $.ajax({
            url: context_path + "/instorageSlab/submitPosition",
            type: "post",
            data: {positionCode: positionCode},
            dataType: "JSON",
            success: function (data) {
                if (data.result) {
                    layer.closeAll();
                } else {
                    layer.msg("系统错误", {icon: 2, time: 1200});
                }
            }
        });
    }

    var areaId = $("#area").val();
    $(function () {
        //获取推荐货位
        $.ajax({
            url: context_path + "/instorageSlab/getRecommendPosition",
            type: "post",
            dataType: "JSON",
            data: {area: areaId},
            success: function (data) {
                if (data.result) {
                    $("#baseInfor #positionCode").select2("data", {
                        id: data.recommendPositionCode,
                        text: data.recommendPositionName
                    });

                    if (data.depotareaId != null && data.depotareaId != "" && data.depotareaId != undefined) {
                        var recommendPositionName = data.recommendPositionName.toLowerCase();
                        //将推荐库位高亮显示
                        $("." + recommendPositionName).attr("class", $("." + recommendPositionName).attr("class") + " legend2");
                        //库区id
                        var depotareaId = data.depotareaId;
                        //触发库区下拉列表change事件
                        $("#tabs").val(depotareaId).trigger("change");
                    }

                }
            }
        });


    });

</script>