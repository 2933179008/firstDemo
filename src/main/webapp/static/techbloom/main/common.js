/**
 * 系统公用js
 * @name     common.js
 * @author   xjiang
 * @version  1.0
 * @date     2016/1/8   
 */



/**
 * jquery浏览器校验
 */
$.browser={
		mozilla : /firefox/.test(navigator.userAgent.toLowerCase()),
		webkit : /webkit/.test(navigator.userAgent.toLowerCase()),
		opera : /opera/.test(navigator.userAgent.toLowerCase()),
		msie : /msie/.test(navigator.userAgent.toLowerCase())
};

/**
 * 判断是否是mail
 * @param mail
 * @returns
 */
function ismail(mail){
	return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
}

(function(){
	$("body").off("click",".toggle_tools").on("click",".toggle_tools",function(){
		console.dir(11);
		var form = $(this).parents("form");
		form.find("li:gt(2)").toggle();
		var toggle = 	form.find("li:gt(2)").is(":hidden");
		$(this).html( ( toggle ? '展开 <i class="fa fa-angle-down"></i>' : '收起 <i class="fa fa-angle-up"></i>' )  );
	});
}());
//---------------------------------------------------------jqgrid---------------------------------
/**
 * 获取选中记录个数v
 * @param _grid
 * @returns
 */
function getGridCheckedNum(_grid)
{
	var idAddr = jQuery(_grid).getGridParam("selarrrow");
    if(idAddr && idAddr.length > 0)
    {
    	return idAddr.length;
    }
    return 0;
}

/**
 * 获取选中行的值，
 * —grid    表格的id
 * _key     要获取值的name
 */
function getGridCheckedId(_grid,_key)
{
	var idAddr = jQuery(_grid).getGridParam("selarrrow");
	var _ids = "";
	if( idAddr && idAddr.length > 0 )
	{
	   for( var i = 0; i < idAddr.length; i++ )
	   {
	      var _value = oriData['rows'][idAddr[i]-1][_key];
	   	  _ids += ((_ids == "") ? "" : ",") + _value;
	   }
	}
	return _ids;
}

/**
 * 更新jqgrid表格样式
 * @param table
 */
function updatePagerIcons(table) {
	var replacement = 
	{
		'ui-icon-seek-first' : 'icon icon-step-backward',
		'ui-icon-seek-prev' : 'icon icon-backward',
		'ui-icon-seek-next' : 'icon icon-forward',
		'ui-icon-seek-end' : 'icon icon-step-forward'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
		if($class in replacement) icon.attr('class', ' ' + replacement[$class]);
	});
}

function enableTooltips(table) {
	$('.navtable .ui-pg-button').tooltip({container:'body'});
	$(table).find('.ui-pg-div').tooltip({container:'body'});
}
// 公共export
function exportData( options ){
	 
	 var opt = $.extend( {
		   grid_data : [],
		   form_data : {},
		   url :""
	   },options );
	 layer.load(2,{shade: 0.6});
	 $.post(context_path+'/techbloom/les/web/Export.jsp', {}, function(html){
			$queryWindow = layer.open({
				title : "数据导出", 
				type: 1,
				skin : "layui-layer-molv",
				area : ['600px','500px'],
				shade: 0.6, 
				moveType: 1, 
				content: html,
				success:function(layero, index){
					layer.closeAll('loading');
					_export_data(opt);
				}
			});
		});
}

//---------------------------------------------------------layer弹出框---------------------------------
function dynamicColumns(col,dynamicDefalutValue){
	let colNames="";
    for(let i=0;i<col.length;i++){
        colNames+=col[i].name+",";
	}
    colNames=colNames.substr(0,colNames.length-1);
    if(colNames.length==2){
        colNames="";
	}else{
        colNames=colNames.substr(3,colNames.length);
	}
    $.ajax({
        url:context_path+"/dynamicColumns/saveColumn?tm="+new Date(),
        type:"POST",
        data:{ columns:colNames,uuid:dynamicDefalutValue},
        dataType:"JSON",
        async:false,
        success:function(data){

         }
    });
}

function dynamicGetColumns(dynamicDefalutValue,tableName,width){
    $.ajax({
        url:context_path+"/dynamicColumns/getColumns?tm="+new Date(),
        type:"POST",
        data:{uuid:dynamicDefalutValue},
        dataType:"JSON",
        async:false,
        success:function(data){
			 if(data.data!=''){
				let columns=data.data.split(",");
                 $("#"+tableName).jqGrid('hideCol', columns);
				/* columns.forEach(function (item,index){
					 $("#"+tableName).setGridParam().hideCol(item).trigger("reloadGrid");
				 })*/
                 $("#"+tableName).jqGrid( 'setGridWidth', width);
			 }
         }
    });
}

/**
 * 毫秒格式化成指定格式的时间
 * @param time
 * @param format
 * @returns {*|XML|string|void|{by}}
 */
var formatDate = function(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
};

