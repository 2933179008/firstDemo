/**
 * 新增字典类型
 */
function addDictType() {
	$.get( context_path + "/dictType/toAdd.do").done(function(data){
		layer.open({
		    title : "字典类型添加", 
	    	type:1,
	    	skin : "layui-layer-molv",
	    	area : ["600px"],
	    	shade : 0.6, //遮罩透明度
		    moveType : 1, //拖拽风格，0是默认，1是传统拖动
		    anim : 2,
		    content : data,
            success:function(layero, index){
                layer.closeAll('loading');
            }
		});
	});
}

/**
 * 修改字典类型
 */
function editDictType() {
    var checkedNum = getGridCheckedNum("#grid-table", "id");
    if (checkedNum == 0) {
        layer.alert("请选择一个要编辑的字典类型！");
        return false;
    } else if (checkedNum > 1) {
        layer.alert("只能选择一个字典类型进行编辑操作！");
        return false;
    } else {
        var DictTypeid = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
    	/*$.get( context_path + "/dictType/toAdd?id=" + DictTypeid).done(function(data){*/
    	$.get( context_path + "/dictType/toAdd.do?id=" + DictTypeid).done(function(data){
    		layer.open({
    		    title : "字典类型编辑", 
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
 *删除字典类型
 */
function delDictType() {
    var checkedNum = getGridCheckedNum("#grid-table", "id");  //选中的数量
    if (checkedNum == 0) {
    	layer.alert("请选择一个要删除的字典类型！");
    } else {
        //从数据库中删除选中的物料，并刷新物料表格
        var ids = jQuery("#grid-table").jqGrid('getGridParam', 'selarrrow');
        layer.confirm('确定删除选中的字典类型？',
            function () {
                $.ajax({
                    type: "POST",
                    url: context_path + '/dictType/deleteDictType.do?ids=' + ids,
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

//字典导出
function toExcel(){
    var selectid = jQuery("#grid-table").jqGrid('getGridParam', 'selarrrow');
    $("#ids").val(selectid);
    $("#hiddenForm").submit();
}
/*
 * json字符串转json对象：jQuery.parseJSON(jsonStr);
 * json对象转json字符串：JSON.stringify(jsonObj);
 */

/**
 * 入库单查询功能:获取查询页面中的值，并将值放入列表页面中隐藏的form
 * @param jsonParam     查询页面传递过来的json对象
 */
function queryDictTypeListByParam(jsonParam) {
    //序列化表单：iTsai.form.serialize($('#frm'))
    //反序列化表单：iTsai.form.deserialize($('#frm'),json)
    iTsai.form.deserialize($('#hiddenQueryForm'), jsonParam);   //将json对象反序列化到列表页面中隐藏的form中
    var queryParam = iTsai.form.serialize($('#hiddenQueryForm'));
    var queryJsonString = JSON.stringify(queryParam);         //将json对象转换成json字符串
    //执行查询操作
    $("#grid-table").jqGrid('setGridParam',
        {
            postData: {queryJsonString: queryJsonString} //发送数据
        }
    ).trigger("reloadGrid");
}

function disableAddButton() {
    if ($("#dictTypeForm").valid()) { 
        $("#dictTypeButton").attr("disabled", "disabled");
        $("#dictTypeForm").submit();
    }
}

/**
 * 查询字典详情
 */
function selectDictTypeDetail(){
    var checkedNum = getGridCheckedNum("#grid-table","id");
    if(checkedNum != 1)
    {
        layer.alert("只能且必须选择一个要需查询详情的字典详情！");
        return false;
    }
    else
    {
        var id = jQuery("#grid-table").jqGrid('getGridParam', 'selrow');
        var rowData = jQuery("#grid-table").jqGrid('getRowData',id);
        var dictTypeNum = rowData.dictTypeNum;
        $.get(context_path +  "/dictDetail/toDictDetailList?dictTypeNum="+dictTypeNum).done(function(data){
            layer.open({
                title : "查询字典详情",
                type:1,
                skin : "layui-layer-molv",
                area : ['900px', '550px'],
                shade : 0.6, //遮罩透明度
                moveType : 1, //拖拽风格，0是默认，1是传统拖动
                anim : 2,
                content : data
            });
        });
    }
}
