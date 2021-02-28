//将数据格式化成两位小数：四舍五入
function formatterNumToFixed(value,options,rowObj){
    if(value!=null){
        var floatNum = parseFloat(value);
        return floatNum.toFixed(2);
    }else{
        return "0.00";
    }
}
//数量输入验证
function numberRegex(value, colname) {

    var regex = /^\d+\.?\d{0,2}$/;
    reloadDetailTableList();
    if (!regex.test(value)) {
        return [false, ""];
    }
    else  return [true, ""];
}

//入库表单校验
$("#baseInfor").validate({
    rules:{
        "supplierCode":{
            required:true
        },
        "customerCode":{
            required:true
        },
        /*"outstorageBillId":{
            required:true
        },*/
        "crossDocking":{
            required:true
        },
        "instorageProcess":{
            required:true
        },
        "instorageType":{
            required:true
        }

    },
    messages: {
        "supplierCode":{
            required:"请选择供应商！"
        },
        "customerCode":{
            required:"请选择货主！"
        },
        /*"outstorageBillId":{
            required:"请选择出库单！"
        },*/
        "crossDocking":{
            required:"请选择是否越库！"
        },
        "instorageProcess":{
            required:"请选择入库流程！"
        },
        "instorageType":{
            required:"请选择入库类型！"
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

//入库类型下拉框change事件
$("#baseInfor #instorageType").on("change",function(e){
    //入库类型下拉框值
    var instorageTypeVal = $("#baseInfor #instorageType").val();
    if(instorageTypeVal=="2"){//如果是“生产退货入库”类型
        $("#baseInfor #outstorageBillDiv").attr("style","display: inline");
        $("#baseInfor #outstorageBillId").val(outstorageBillId);
        $("#baseInfor #outstorageBillCode").val(outstorageBillCode);
    }else{
        $("#baseInfor #outstorageBillDiv").attr("style","display: none");
        $("#baseInfor #outstorageBillId").val("");
        $("#baseInfor #outstorageBillCode").val("");
    }
});

//出库单下拉列表数据源
$("#baseInfor #outstorageBillId").select2({
    placeholder: "--选择出库单--",
    minimumInputLength: 0, //至少输入n个字符，才去加载数据
    allowClear: true, //是否允许用户清除文本信息
    delay: 250,
    formatNoMatches: "没有结果",
    formatSearching: "搜索中...",
    formatAjaxError: "加载出错啦！",
    ajax: {
        url: context_path + "/instorage/getSelectOutstorageBill",
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

//出库单下拉框change事件
$("#baseInfor #outstorageBillId").on("change",function(e){
    $("#baseInfor #outstorageBillCode").val(e.added.text);
});

//供应商下拉列表数据源获取
$("#baseInfor #supplierCode").select2({
    placeholder: "--选择供应商--",
    minimumInputLength: 0, //至少输入n个字符，才去加载数据
    allowClear: true, //是否允许用户清除文本信息
    delay: 250,
    formatNoMatches: "没有结果",
    formatSearching: "搜索中...",
    formatAjaxError: "加载出错啦！",
    ajax: {
        url: context_path + "/receiptManage/getSelectSupplier",
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
    },
    change:function (e) {

    }

});

//供应商下拉框change事件
$("#baseInfor #supplierCode").on("change",function(e){
    $("#baseInfor #supplierName").val(e.added.text);
})


//货主（客户）下拉列表数据源获取
$("#baseInfor #customerCode").select2({
    placeholder: "--选择货主--",
    minimumInputLength: 0, //至少输入n个字符，才去加载数据
    allowClear: true, //是否允许用户清除文本信息
    delay: 250,
    formatNoMatches: "没有结果",
    formatSearching: "搜索中...",
    formatAjaxError: "加载出错啦！",
    ajax: {
        url: context_path + "/receiptManage/getSelectCustomer",
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

//货主下拉框change事件
$("#baseInfor #customerCode").on("change",function(e){
    $("#baseInfor #customerName").val(e.added.text);
})

// //收货单下拉列表数据源
// $("#baseInfor #receiptPlanId").select2({
//     placeholder: "--选择出库单--",
//     minimumInputLength: 0, //至少输入n个字符，才去加载数据
//     allowClear: true, //是否允许用户清除文本信息
//     delay: 250,
//     formatNoMatches: "没有结果",
//     formatSearching: "搜索中...",
//     formatAjaxError: "加载出错啦！",
//     ajax: {
//         url: context_path + "/instorage/getSelectReceiptPlan",
//         type: "POST",
//         dataType: 'json',
//         delay: 250,
//         data: function (term, pageNo) { //在查询时向服务器端传输的数据
//             term = $.trim(term);
//             return {
//                 queryString: term, //联动查询的字符
//                 pageSize: 15, //一次性加载的数据条数
//                 pageNo: pageNo, //页码
//                 time: new Date()
//                 //测试
//             }
//         },
//         results: function (data, pageNo) {
//             var res = data.result;
//             if (res.length > 0) { //如果没有查询到数据，将会返回空串
//                 var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
//                 return {
//                     results: res,
//                     more: more
//                 };
//             } else {
//                 return {
//                     results: {}
//                 };
//             }
//         },
//         cache: true
//     }
// });
//
// //收货单下拉框change事件
// $("#baseInfor #receiptPlanId").on("change",function(e){
//     $("#baseInfor #receiptCode").val(e.added.text);
// });


//编辑页面初始化时，下拉框赋值
if($("#baseInfor #id").val()!="" && $("#baseInfor #id").val()!=undefined && $("#baseInfor #id").val()!=null){

    $("#baseInfor #supplierCode").select2("data", {
        id: $("#baseInfor #supplierCode").val(),
        text: $("#baseInfor #supplierName").val()
    });
    $("#baseInfor #customerCode").select2("data", {
        id: $("#baseInfor #customerCode").val(),
        text: $("#baseInfor #customerName").val()
    });
    //入库类型下拉框值
    var instorageTypeVal = $("#baseInfor #instorageType").val();
    if(instorageTypeVal=="2"){//如果是“生产退货入库”类型
        $("#baseInfor #outstorageBillDiv").attr("style","display: inline");
        $("#baseInfor #outstorageBillId").select2("data", {
            id: $("#baseInfor #outstorageBillId").val(),
            text: $("#baseInfor #outstorageBillCode").val()
        });
    }else if(instorageTypeVal=="0" || instorageTypeVal=="1"){//如果是“采购入库”或“委托加工入库”类型
        $("#baseInfor #outstorageBillDiv").attr("style","display: none");
        $("#baseInfor #crossDockingDiv").attr("style","display: inline");
    }else{
        $("#baseInfor #outstorageBillDiv").attr("style","display: none");
        $("#baseInfor #crossDockingDiv").attr("style","display: none");
    }
}

//收货单保存
$("#formSave").click(function(){
    // add by anss 2019-3-30 start
    // 生产退货类型时，出库单加不为空验证，反之不加
    $('#baseInfor #outstorageBillId').rules('add', { required: true});
    if ($("#baseInfor #instorageType").val() != '2') {
        $('#baseInfor #outstorageBillId').rules("remove");
    }
    // add by anss 2019-3-30 end
    if($('#baseInfor').valid()){
        $.ajax({
            url:context_path+"/instorage/saveInstorage",
            type:"post",
            data:$("#baseInfor").serialize(),
            dataType:"JSON",
            success:function (data){
                if(data.code){
                    $("#baseInfor #id").val(data.instorageId);
                    //刷新入库单列表
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {queryJsonString: ""} //发送数据
                    }).trigger("reloadGrid");

                    //根据入库单主键id刷新查询入库单详情
                    $("#grid-table-c").jqGrid("setGridParam", {
                        url : context_path + '/instorage/getDetailList?instorageId='+data.instorageId,
                        postData: {queryJsonString:""} //发送数据  :选中的节点
                    }).trigger("reloadGrid");
                    setNextBtn();
                    layer.msg("操作成功！",{icon:1,time:1200});
                }else{
                    layer.msg("操作失败！",{icon:2,time:1200});
                }
            }
        });

    }
});

//收货单提交
$("#formSubmit").click(function(){
    if($("#baseInfor #id").val()!=""){
        layer.confirm("提交后的数据将不能修改，确认提交吗？",function(){
            $.ajax({
                url:context_path+"/instorage/submitInstorage?instorageId="+$("#baseInfor #id").val(),
                type:"post",
                dataType:"JSON",
                success:function (data){
                    if(data.result){
                        //刷新收货单列表
                        $("#grid-table").jqGrid("setGridParam", {
                            postData: {queryJsonString: ""} //发送数据
                        }).trigger("reloadGrid");
                        layer.closeAll();
                        layer.msg(data.msg,{icon:1,time:1200})
                    }else{
                        layer.msg(data.msg,{icon:2,time:1200})
                    }
                }
            });
        });
    }else{
        layer.msg("请先保存单据",{icon:2,time:1200})
    }
})

//物料下拉框列表
$("#materialInfor").select2({
    placeholder : "--请选择物料--",//文本框的提示信息
    minimumInputLength : 0, //至少输入n个字符，才去加载数据
    allowClear : true, //是否允许用户清除文本信息
    multiple: true,
    closeOnSelect:false,
    ajax : {
        url : context_path + "/instorage/getSelectMaterial",
        dataType : "json",
        delay : 250,
        data : function(term, pageNo) { //在查询时向服务器端传输的数据
            term = $.trim(term);
            selectParam = term;
            return {
                outstorageBillId : $("#baseInfor #outstorageBillId").val(),
                queryString : term, //联动查询的字符
                pageSize : 15, //一次性加载的数据条数
                pageNo : pageNo, //页码
                time : new Date()
                //测试
            }
        },
        results : function(data, pageNo) {
            var res = data.result;
            if (res.length > 0) { //如果没有查询到数据，将会返回空串
                var more = (pageNo * 15) < data.total; //用来判断是否还有更多数据可以加载
                return {
                    results : res,
                    more : more
                };
            } else {
                return {
                    results : {
                        "id" : "0",
                        "text" : "没有更多结果"
                    }
                };
            }

        },
        cache : true
    }
});

//物料下拉框列表change事件
$("#materialInfor").on("change",function(e){
    var datas=$("#materialInfor").select2("val");
    var selectSize = datas.length;
    if(selectSize>1){
        var $tags = $("#s2id_materialInfor .select2-choices");   //
        //$("#s2id_materialInfor").html(selectSize+"个被选中");
        var $choicelist = $tags.find(".select2-search-choice");
        var $clonedChoice = $choicelist[0];
        $tags.children(".select2-search-choice").remove();
        $tags.prepend($clonedChoice);

        $tags.find(".select2-search-choice").find("div").html(selectSize+"个被选中");
        $tags.find(".select2-search-choice").find("a").removeAttr("tabindex");
        $tags.find(".select2-search-choice").find("a").attr("href","#");
        $tags.find(".select2-search-choice").find("a").attr("onclick","removeChoice();");
    }
    //执行select的查询方法
    $("#materialInfor").select2("search",selectParam);
});


//清空物料多选框中的值
function removeChoice(){
    $("#s2id_materialInfor .select2-choices").children(".select2-search-choice").remove();
    $("#materialInfor").select2("val","");
}
//添加物料详情
function addDetail(){
    if($("#baseInfor #id").val() == ""){
        $("#materialInfor").select2("val","");
        layer.alert("请先保存表单信息！");
        return;
    }
    if($("#materialInfor").select2("val") != "" && $("#materialInfor").select2("val") != null){
        //将选中的物料添加到数据库中
        $.ajax({
            type:"POST",
            url:context_path + "/instorage/saveInstorageDetail",
            data:{
                instorageId:$("#baseInfor #id").val(),
                materialCodes:$("#materialInfor").select2("val").toString()
            },
            dataType:"json",
            success:function(data){
                removeChoice();   //清空下拉框中的值
                if(data.result){
                    layer.msg(data.msg,{icon:1,time:1200});
                    //重新加载详情表格
                    reloadDetailTableList();
                }else{
                    layer.msg(data.msg,{icon:2,time:1200});
                }
            }
        });
    }else{
        layer.alert("请选择物料！");
    }
}

//工具栏

//删除物料详情
function delDetail(){
    var ids = jQuery("#grid-table-c").jqGrid("getGridParam","selarrrow");
    if (ids == "") {
        layer.alert("至少选择一个要删除的物料信息！");
        return false;
    }
    $.ajax({
        url:context_path + '/instorage/deleteInstorageDetail?ids='+ids,
        type:"POST",
        dataType:"JSON",
        success:function(data){
            if(data.result){
                layer.msg("操作成功！");
                //重新加载详情表格
                reloadDetailTableList();
            }
        }
    });
}
function reloadDetailTableList(){
    $("#grid-table-c").jqGrid("setGridParam", {
        postData: {instorageId:$("#baseInfor #id").val()} //发送数据  :选中的节点
    }).trigger("reloadGrid");
}


//如果是自采类型的单据
if(instorageType == "0" || instorageType == "1" || instorageType == "2"){
    $("#materialDiv #materialInfor").select2("readonly","readonly");
    $("#materialDiv #addMaterialBtn").attr("disabled","disabled");
    $("#btchdel").attr("disabled","disabled");
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

function delOneDetail(id){
    $.ajax({
        url:context_path + '/instorage/deleteInstorageDetail?ids='+id,
        type:"POST",
        dataType:"JSON",
        success:function(data){
            if(data.result){
                layer.msg("操作成功！");
                //重新加载详情表格
                reloadDetailTableList();
            }
        }
    });
}

