

//物料下拉框列表
$("#addressesBys").select2({
    placeholder : "--请选择发件人--",//文本框的提示信息
    minimumInputLength : 0, //至少输入n个字符，才去加载数据
    allowClear : true, //是否允许用户清除文本信息
    multiple: true,
    closeOnSelect:false,
    ajax : {
        url : context_path + "/alarmDeploy/getSelectUser",
        dataType : "json",
        delay : 250,
        data : function(term, pageNo) { //在查询时向服务器端传输的数据
            term = $.trim(term);
            selectParam = term;
            return {
                /* docId : $("#baseInfor #id").val(), */
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
$("#addressesBys").on("change",function(e){
    var datas=$("#addressesBys").select2("val");
    var selectSize = datas.length;
    if(selectSize>1){
        var $tags = $("#s2id_addressesBys .select2-choices");   //
        //$("#s2id_addressesBys").html(selectSize+"个被选中");
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
    $("#addressesBys").select2("search",selectParam);
});

//清空物料多选框中的值
function removeChoice(){
    $("#s2id_addressesBys .select2-choices").children(".select2-search-choice").remove();
    $("#addressesBys").select2("val","");
}
