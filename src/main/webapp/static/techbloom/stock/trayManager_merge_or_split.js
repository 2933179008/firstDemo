//绑定单保存
$("#formSave").click(function () {
    if ($('#baseInfor').valid()) {
        $.ajax({
            url: context_path + "/trayManager/saveTray",
            type: "post",
            data: $("#baseInfor").serialize(),
            dataType: "JSON",
            success: function (data) {
                if (data.result) {
                    $("#baseInfor #id").val(data.trayBy);
                    //刷新托盘管理列表
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {queryJsonString: ""} //发送数据
                    }).trigger("reloadGrid");

                    //根据刷新物料绑定详情
                    $("#grid-table-c").jqGrid("setGridParam", {
                        url: context_path + '/trayManager/getDetailList.do',
                        postData: {queryJsonString: ""} //发送数据  :选中的节点
                    }).trigger("reloadGrid");
                    layer.msg("操作成功！", {icon: 1, time: 1200});
                } else {
                    layer.msg("操作失败！", {icon: 2, time: 1200});
                }
                _grid.trigger("reloadGrid");  //重新加载表格
            }
        });

    }
});

// 查询
function queryOk() {
    var queryParam = iTsai.form.serialize($("#queryForm1"));
    querySceneByParam(queryParam);
}

function querySceneByParam(jsonParam) {
    // debugger;
    // 将json对象反序列化到列表页面中隐藏的form中
    iTsai.form.deserialize($("#hiddenQueryForm1"), jsonParam);
    var queryParam = iTsai.form.serialize($("#hiddenQueryForm1"));
    // 将json对象转换成json字符串
    var queryJsonString = JSON.stringify(queryParam);
    // 执行查询操作
    $("#grid-table-c").jqGrid("setGridParam",
        {
            postData: {queryJsonString: queryJsonString} //发送数据
        }
    ).trigger("reloadGrid");
}


//重置查询条件
function reset() {
    $("#queryForm1 #rfidd").val("");
    $("#grid-table-c").jqGrid("setGridParam",
        {
            postData: {queryJsonString: ""} //发送数据
        }
    ).trigger("reloadGrid");
}


function reloadDetailTableList() {
    $("#grid-table-c").jqGrid("setGridParam", {
        postData: {receiptId: $("#baseInfor #id").val()} //发送数据  :选中的节点
    }).trigger("reloadGrid");
}
