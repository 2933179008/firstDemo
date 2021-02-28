//绑定单保存
$("#formSave").click(function () {
    if ($('#baseInfor').valid()) {
        $.ajax({
            url: context_path + "/materielBindRfid/saveMaterielBindRfid",
            type: "post",
            data: $("#baseInfor").serialize(),
            dataType: "JSON",
            success: function (data) {
                if (data.result) {
                    $("#baseInfor #id").val(data.materielBindRfidBy);
                    //刷新收货单列表
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {queryJsonString: ""} //发送数据
                    }).trigger("reloadGrid");
                    setNextBtn();
                    //根据收货单主键id刷新查询收货单详情
                    $("#grid-table-c").jqGrid("setGridParam", {
                        url: context_path + '/materielBindRfid/getDetailList?materielBindRfidBy=' + data.materielBindRfidBy,
                        postData: {queryJsonString: ""} //发送数据  :选中的节点
                    }).trigger("reloadGrid");
                    layer.msg("操作成功！", {icon: 1, time: 1200});
                } else {
                    layer.msg("操作失败！", {icon: 2, time: 1200});
                }
            }
        });

    }
});

//物料下拉框列表
$("#materialInfor").select2({
    placeholder: "--请选择物料--",//文本框的提示信息
    minimumInputLength: 0, //至少输入n个字符，才去加载数据
    allowClear: true, //是否允许用户清除文本信息
    multiple: true,
    closeOnSelect: false,
    ajax: {
        url: context_path + "/materielBindRfid/getSelectMaterial",
        dataType: "json",
        delay: 250,
        data: function (term, pageNo) { //在查询时向服务器端传输的数据
            term = $.trim(term);
            selectParam = term;
            return {
                /* docId : $("#baseInfor #id").val(), */
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
                    results: {
                        "id": "0",
                        "text": "没有更多结果"
                    }
                };
            }

        },
        cache: true
    }
});

//物料下拉框列表change事件
$("#materialInfor").on("change", function (e) {
    var datas = $("#materialInfor").select2("val");
    var selectSize = datas.length;
    if (selectSize > 1) {
        var $tags = $("#s2id_materialInfor .select2-choices");   //
        //$("#s2id_materialInfor").html(selectSize+"个被选中");
        var $choicelist = $tags.find(".select2-search-choice");
        var $clonedChoice = $choicelist[0];
        $tags.children(".select2-search-choice").remove();
        $tags.prepend($clonedChoice);

        $tags.find(".select2-search-choice").find("div").html(selectSize + "个被选中");
        $tags.find(".select2-search-choice").find("a").removeAttr("tabindex");
        $tags.find(".select2-search-choice").find("a").attr("href", "#");
        $tags.find(".select2-search-choice").find("a").attr("onclick", "removeChoice();");
    }
    //执行select的查询方法
    $("#materialInfor").select2("search", selectParam);
});


//清空物料多选框中的值
function removeChoice() {
    $("#s2id_materialInfor .select2-choices").children(".select2-search-choice").remove();
    $("#materialInfor").select2("val", "");
}

//添加物料详情
function addDetail() {
    if ($("#baseInfor #id").val() == "") {
        $("#materialInfor").select2("val", "");
        layer.alert("请先保存表单信息！");
        return;
    }
    if ($("#materialInfor").select2("val") != "" && $("#materialInfor").select2("val") != null) {
        //将选中的物料添加到数据库中
        $.ajax({
            type: "POST",
            url: context_path + "/materielBindRfid/saveMaterielBindRfidDetail",
            data: {
                materielBindRfidBy: $("#baseInfor #id").val(),
                materielCodes: $("#materialInfor").select2("val").toString()

            },
            dataType: "json",
            success: function (data) {
                removeChoice();   //清空下拉框中的值
                if (data.result) {
                    layer.msg(data.msg, {icon: 1, time: 1200});
                    //重新加载详情表格
                    reloadDetailTableList();
                } else {
                    layer.msg(data.msg, {icon: 2, time: 1200});
                }
            }
        });
    } else {
        layer.alert("请选择物料！");
    }
}

/*//工具栏
$("#__toolbar__-c").iToolBar({
    id: "__tb__01",
    items: [
        {label: "删除", onclick: delDetail},
    ]
});*/

//删除物料详情
function delDetail() {
    var ids = jQuery("#grid-table-c").jqGrid("getGridParam", "selarrrow");
    if (ids == "") {
        layer.alert("至少选择一个要删除的物料信息！");
        return false;
    }
    $.ajax({
        url: context_path + '/materielBindRfid/deleteMaterielBindRfidDetail?ids=' + ids,
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.result) {
                layer.msg("操作成功！");
                //重新加载详情表格
                reloadDetailTableList();
            }
        }
    });
}

function reloadDetailTableList() {
    $("#grid-table-c").jqGrid("setGridParam", {
        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
    }).trigger("reloadGrid");
}
function setNextBtn() {
    $("#next_step").attr('class','btn btn-info');
    $("#next_step").on('click',function(){
        reloadDetailTableList();
        $(".step1").css('display','none');
        $(".step2").css('display','block');

    });
    $("#pre_step").on('click',function(){
        $(".step1").css('display','block');
        $(".step2").css('display','none');
    });

}

function nextStep() {
    reloadDetailTableList();
    $(".step1").css('display','none');
    $(".step2").css('display','block');
    $("#pre_step").on('click',function(){
        $(".step1").css('display','block');
        $(".step2").css('display','none');
    });
}

function delOneDetail(id) {
    $.ajax({
        url: context_path + '/materielBindRfid/deleteMaterielBindRfidDetail?ids=' + id,
        type: "POST",
        dataType: "JSON",
        success: function (data) {
            if (data.result) {
                layer.msg("操作成功！");
                //重新加载详情表格
                reloadDetailTableList();
            }
        }
    });
}