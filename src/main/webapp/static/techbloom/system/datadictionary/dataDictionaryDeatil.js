/**
 * 新增字典值
 */
function addDictDetail() {
	 var dictTypeNum = jQuery("#dictTypeNumc").val();
	$.get( context_path + "/dictDetail/toDictDetailEdit.do?dictTypeNum="+dictTypeNum).done(function(data){
		 window.childDiv = layer.open({
		    title : "字典值添加", 
	    	type:1,
	    	skin : "layui-layer-molv",
	    	area : ["600px"],
	    	shade : 0.6, //遮罩透明度
		    moveType : 1, //拖拽风格，0是默认，1是传统拖动
		    anim : 2,
		    content : data
		});
	});        
}

/**
 * 修改字典值
 */
function editDictDetail() {
    var checkedNum = getGridCheckedNum("#grid-table-c", "id");
    var dictTypeNum = jQuery("#dictTypeNumc").val();
    if (checkedNum == 0) {
        layer.alert("请选择一个要编辑的字典值！");
        return false;
    } else if (checkedNum > 1) {
        layer.alert("只能选择一个字典值进行编辑操作！");
        return false;
    } else {
        var DictDetailid = jQuery("#grid-table-c").jqGrid('getGridParam', 'selrow');
    	$.get( context_path + "/dictDetail/toDictDetailEdit.do?dictTypeNum="+dictTypeNum+"&id=" + DictDetailid).done(function(data){
    window.childDiv = layer.open({
    		    title : "字典值编辑", 
    	    	type:1,
    	    	skin : "layui-layer-molv",
    	    	area : ["600px"],
    	    	shade : 0.6, //遮罩透明度
    		    moveType : 1, //拖拽风格，0是默认，1是传统拖动
    		    anim : 2,
    		    content : data
    		});
    	});          
    }
}
/** 表格刷新 */
function gridReload() {
    _grid.trigger("reloadGrid");  //重新加载表格
}

/**
 * 显示提示窗口
 * @param msg   显示信息
 * @param delay 持续时间,结束之后窗口消失
 */
function showTipMsg(msg, delay) {
	layer.msg(msg, {icon: 1,time:delay});
}

/**
 *删除字典值
 */
function delDictDetail() {
    var checkedNum = getGridCheckedNum("#grid-table-c", "id");  //选中的数量
    if (checkedNum == 0) {
        layer.alert("请选择一个要删除的字典值！");
    } else {
        //从数据库中删除选中的物料，并刷新物料表格
        var ids = jQuery("#grid-table-c").jqGrid('getGridParam', 'selarrrow');
        layer.confirm('确定删除选中的字典值？',
            function () {
                $.ajax({
                    type: "POST",
                    url: context_path + '/dictDetail/deleteDictDetail.do?ids=' + ids,
                    dataType: "json",
                    success: function (data) {
                        if (Boolean(data.result)) {
                            //弹出提示信息
                            showTipMsg(data.msg, 1000);
                        } else {
                            showTipMsg(data.msg, 1000);
                        }
                        _grid.trigger("reloadGrid");  //重新加载表格
                    }
                });
            }
        );

    }
}


/*
 * json字符串转json对象：jQuery.parseJSON(jsonStr);
 * json对象转json字符串：JSON.stringify(jsonObj);
 */

/**
 * 打开查询界面
 */
function openDictDetailListSearchPage() {
    var queryBean = iTsai.form.serialize($('#hiddenQueryForm-c'));   //获取form中的值：json对象
    var queryJsonString = JSON.stringify(queryBean);         //将json对象转换成json字符串
    var dictTypeNum = jQuery("#dictTypeNumc").val();
    $.get(context_path + "/dictDetail/toQueryPage?jsonString=" + queryJsonString+"&dictTypeNum="+dictTypeNum).done(function(data){
    	 window.childDiv =  layer.open({
    	    title : "字典值查询", 
	    	type:1,
	    	skin : "layui-layer-molv",
	    	area : ['400px', '260px'],
	    	shade : 0.6, //遮罩透明度
		    moveType : 1, //拖拽风格，0是默认，1是传统拖动
		    anim : 2,
		    content : data
		});
    });        
}


/**
 * 入库单查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryDictDetailListByParam(jsonParam) {
    //序列化表单：iTsai.form.serialize($('#frm'))
    //反序列化表单：iTsai.form.deserialize($('#frm'),json)
    iTsai.form.deserialize($('#hiddenQueryForm-c'), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
    var queryParam = iTsai.form.serialize($('#hiddenQueryForm-c'));
    var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
    //执行查询操作
    $("#grid-table-c").jqGrid('setGridParam',
        {
            postData: {queryJsonString: queryJsonString} //发送数据
        }
    ).trigger("reloadGrid");
}

function disableAddButton() {
    if ($("#dictDetailForm").valid()) { 
        $("#dictDetailButton").attr("disabled", "disabled");
        $("#dictDetailForm").submit();
    }
}
