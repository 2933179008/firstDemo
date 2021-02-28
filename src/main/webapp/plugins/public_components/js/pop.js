(function(){
	
	var _parm = {
	   id : "",
	   content :"",
	   jsFun : function(){
		   
	   }
	};

	var clearData = function( form ){
		 Array.prototype.forEach.call(form[0].elements,function(){
    		 var input  = arguments[0];   
    		 if(input.type == "checkbox"){
    			  input.checked = false; 
    		 }else if(input.type == "radio"){
    			 $(input).attr("checked",false)
    		 }else if($(input).hasClass("select2_input")){
    			 $(input).select2("data", ""); 
    		 }
             else if(input.type =="textarea"){
            	  $(input).val("");
              }
             else if(input.type =="hidden"){
            	  $(input).val("");
              } 
             else if(input.type == "text"){
   			  $(input).val("");
    		 }
         });
    	 $('.ztree', form).each(function(){
    		 var treeObj = $.fn.zTree.getZTreeObj( $(this).attr("id") );
    		 treeObj.checkAllNodes(false);
    		 var nodes = treeObj.getSelectedNodes();
    		 if (nodes.length>0) { 
    			 for(var i = 0; i < nodes.length; i++){
         		 	treeObj.cancelSelectedNode(nodes[i]);
    			 }
    		 }
    	 })
	}
	var formDom =function( p, type ){
	    var _p = $.extend({}, p);
	  //  debugger;
		var group =[],	 content_form = [];
		if(_p.ts.p["formdata"]){
			var formdata = _p.ts.p["formdata"];
			
			var ul = "<ul class='tab_group'>", sign = true;
			for(var i = 0; i < formdata.length; i++){
				var element =$.extend({}, formdata[i]);
			    if(  element["disabled"] ){
			    	var  disabled = element["disabled"];
			    	if( disabled.constructor.name == "Object" ){
			    		element["disabled"] = disabled[type] ? "disabled" :"";
			    	}else{
			    		element["disabled"] = 	!!disabled ? "disabled" :"";
			    	}
			    }else{
			    	element["disabled"] = "";
			    }
			    
			    if(  element["hide"] ){
			    	var  hide = element["hide"];
			    	if( hide.constructor.name == "Object" ){
			    		element["class"] = hide[type] ? "hide" :"";
			    	}else{
			    		element["class"] = 	!! hide[type] ? "hide" :"";
			    	}
			    }else{
			    	//element["class"] = "";
			    }
			    
			    if( element["remove"] === true ){
			    	continue;
			    }else if( element["remove"] && element["remove"].constructor.name == "Object" ){
			    	if( element["remove"][type] == true ){
			    		continue;
			    	}
			    }
			   
				if(element["group"]){
				//	group.push(element["group"]);
					if(  !group[element["group"]] ){
					   ul += "<li class="+( sign == true ? "active_li":"")+">"+element["group"]+"</li>";
					   group[element["group"]]  =[];
					   group.length++;
					   sign = false;
					}
				 
				   group[element["group"]].push( TuiStr(element)  ); 
				
				}else{
					content_form.push( TuiStr(element) );
				}
				
			}
			ul +="</ul>";
			//debugger;
		}
		 var tab_form =""; 
		 for( var g in group ){
        	 if(!Array.prototype.hasOwnProperty(g)){
        		 tab_form +="<div  class='"+( sign == false ? "active_li_show":"active_li_hide")+"' title='"+g+"' tab_group='"+g+"' >"+ group[g].join("") +"</div>";
        		 sign = true;
        	 }
         }
	
		 return  ( group.length > 0 ? ul  : "" )+
         ( group.length > 0 ? "<div class='form-box'>" : "" )+
         ( group.length > 0 ?  tab_form : "" )+	        
         content_form.join("") +
         ( group.length > 0 ? "</div>" : "" );
	};
	var pop = function( parm ){
		var p = $.extend({}, _parm , parm);
		if(p.id == "") return;
		var content = "";	
		//formDom( p );
		if(p.ts.p["addData"] && p.sign == "addData" ){
		  content += '<div class="addData_group"> '+
			         '<div class="group_title"><span > <i class=" icon-plus"></i>新增</span><span class="action"> <i title="重置" class=" icon-refresh"></i> <i title="保存" class=" icon-save"></i></span><span class="expand"></span></div>  '+
			         '<form class="form-horizontal group_hide" ><div class="row span12" style=" padding: 13px;     border-bottom: 1px solid #ccc; ">'+
			         formDom( p , "addData" )+
			         '</div></form>'+
			         '</div> ';
		     $(document).off("click",".addData_group .icon-refresh").on("click",".addData_group .icon-refresh",function(){
            	 var form = $(this).parents(".group_title").next();
            	 clearData(form);
             });
		             
		     $(document).off("click",".addData_group .icon-save").on("click",".addData_group .icon-save" ,function(){
		    	  if( !$(this).parents(".group_title").next().valid() ) return;
		    		  
            	  var Param = $(this).parents(".group_title").next().serialize();
            	   $.ajax({
      	  			url: p.ts.p["addDataUrl"],
      	  			type:"POST",
      				data:Param,
      				dataType:"JSON",
      				success:function(data){
      				if(data){
      						$("#"+p.ts.id).jqGrid('setGridParam', 
      							{
      							postData: {queryJsonString:""} 
      								}
      						).trigger("reloadGrid");
      						layer.msg("操作成功！",{icon:1});
      					}else{
      						layer.alert("操作失败！",{icon:2});
      					}
      				}
      			});
              });
		}
		if(p.ts.p["editData"] && p.sign == "editData"){
			var inputs = "",selectRowData =  p.ts.p["selectRowData"];
			for(var i = 0; i <selectRowData.length ; i++){
				var element =$.extend({}, selectRowData[i]);
				inputs += _tuiLoadFun["inputText_hide"].returnStr( element );
			}
		
			content +=   '<div class="editData_group"> '+
				         '<div class="group_title"><span > <i class="icon-edit"></i>编辑</span><span class="action"> <i title="重置" class=" icon-refresh"></i> <i title="保存" class=" icon-save"></i></span><span class="expand"></span></div>  '+
				         '<form class="form-horizontal group_hide"><div class="row span12" style=" padding: 13px;     border-bottom: 1px solid #ccc; ">'+
				         inputs+
				         formDom( p , "editData" )+
				         '</div></form>'+
				         '</div> ';		
		     $(document).off("click",".editData_group .icon-refresh").on("click",".editData_group .icon-refresh",function(){
            	 var form = $(this).parents(".group_title").next();
            	 clearData(form);
             });
		             
		     $(document).off("click",".editData_group .icon-save").on("click",".editData_group .icon-save" ,function(){
            	// debugger;
		    	  if( !$(this).parents(".group_title").next().valid() ) return;

		    	 var Param = $(this).parents(".group_title").next().serialize();
      	    	  $.ajax({
      	  			url:p.ts.p["editDataUrl"],
      	  			type:"POST",
      				data:Param,
      				dataType:"JSON",
      				success:function(data){
      				if(data){
      						$("#"+p.ts.id).jqGrid('setGridParam', 
      							{
      							postData: {queryJsonString:""} 
      								}
      						).trigger("reloadGrid");
      						layer.msg("操作成功！",{icon:1});
      					}else{
      						layer.alert("操作失败！",{icon:2});
      					}
      				}
      			});
              });
		}
		/*if(p.ts.p["deleteData"] && p.sign == "deleteData" ){
			content +=   '<div class="deleteData_group"> '+
				         '<div class="group_title"><span > <i class="icon-trash"></i>删除</span><span class="action"><i title="删除" class=" icon-remove-circle"></i></span><span class="expand"></span></div>  '+
				         '</div> ';
		}*/
		if(p.ts.p["exportFile"]){
			var inputs ="";
			var   export_grid_data = [];
			for(var i =0 ; i< p.ts.p["colModel"].length; i++){
				if( p.ts.p["colNames"][i] == '' ) continue;
				
				var obj  = {
					 id : i+1,
		 			 fieldName :  p.ts.p["colModel"][i]["name"],
		 			 description : p.ts.p["colNames"][i],
		 			 byname : p.ts.p["colNames"][i]
				};
				export_grid_data.push(obj);
				var postData = p.ts.p["postData"]["queryJsonString"],
			        queryJson = postData && JSON.parse(p.ts.p["postData"]["queryJsonString"]),
			        name =  p.ts.p["colModel"][i]["name"].toLocaleUpperCase();
				if(queryJson){
					inputs += '<input name="'+ name +'" id="'+ name +'" value="'+ (queryJson[name]||"") +'" type="hidden"/>';
				}
			}
			
			content += '<div class="export_group"> <div class="group_title"><span > <i class="icon-download-alt"></i> 导出</span><span class="expand"></span></div>  '+
				       '<div class="group_show" style="padding: 13px;">'+
					   '<div id="export_grid-div">'+
						'<div id="fixed_tool_div" class="fixed_tool_div">'+
						'<div id="__toolbar__" style="float:left;overflow:hidden;"></div>'+
						'</div>'+
						'<table id="export_grid-table" style="width:100%;height:100%;"></table>'+
						'<div id="export_grid-pager"></div>'+
						'</div>'+
						'<form id="hiddenQueryForm" action="'+p.ts.p["exportFileUrl"]+'" method="post" >'+
						'<input name="byname" id="byname" value="" type="hidden">'+
						inputs+
						'<ul class="form-elements">'+
						'<li class="field-group">'+
						'<label class="inline" for="branches" >'+
						'导出条数：'+
						'<input type="text" name="branches" id="branches" value=""'+
						'style="width:190px;" placeholder="最大导出条数为60000条"/>'+
						'</label>'+
						  
						'<div class="field-button">'+
						'<div class="btn btn-info btn-sm" onclick="daochu();return false;" style="padding: 2px 12px;">'+
						'<i class="ace-icon fa fa-check bigger-110"></i>开始导出'+
						'</div>'+
						'</div>'+
						'</li>'+
						'</ul>'+
						'</form>'+
				        '</div>';
		
			content += '<script type="text/javascript">'+
			           'var export_grid_data ='+ JSON.stringify(export_grid_data)+";"+
			           'jQuery("#export_grid-table").jqGrid({'+
						'		subGrid : false,			'+
						'        data : export_grid_data,'+
						'		datatype : "local",'+
						'		colNames : [ "主键", "字段名", "描述", "别名" ],'+
						'		colModel : [ {'+
						'			name : "id",'+
						'			index : "id",'+
						'			hidden : true,'+
						'			width : 60'+
						'		}, {'+
						'			name : "fieldName",'+
						'			hidden : true,'+
						'			index : "fieldName",'+
						'			width : 60'+
						'		}, {'+
						'			name :  "description",'+
						'			index : "description",'+
						'			width : 100'+
						'		}, {'+
						'			name :  "byname",'+
						'			index : "byname",'+
						'			width : 190,'+
						'			formatter:function (cellValue,option,rowObject){'+
						'			  	return "<input value=\'"+cellValue+"\' id=\'"+rowObject.id+"_byname\'>";'+
						'			}'+
						'		} ],'+
						'		altRows : true,'+
						'		viewrecords : true,'+
						'		autowidth : false,'+
						'		multiselect : true,'+
						'		emptyrecords : "没有相关记录",'+
						'		loadtext : "加载中...",'+
						'		pgtext : "页码 {0} / {1}页",'+
						'		recordtext : "显示 {0} - {1}共{2}条数据",'+
						'	});'+
				        ' 	function daochu() {'+
				    	'		var ids = $("#export_grid-div #export_grid-table").jqGrid(\'getGridParam\', \'selarrrow\');'+
				    	'		if(ids.length==0){'+
				    	'			layer.alert("请选择要导出的列！",{icon:2});'+
				    	'			return;'+
				    	'		}'+
				    	'		var content="";'+
				    	'		for (var i = 0; i < ids.length; i++) {'+
				    	'			var id = ids[i];'+
				    	'			var rowData = $("#export_grid-div #export_grid-table").jqGrid(\'getRowData\', id);'+
				    	'			var idd="#"+id+"_byname";'+
				    	'			content+=rowData.fieldName+","+$(idd).val() + ",";'+
				    	'		}'+
				    	'		$("#byname").val(content.substr(0,content.length-1));'+
				    	'		$("#hiddenQueryForm").submit();'+
				    	'	}'+
				       '</script></div>';
		}
		
		var dom = "<div class='pop'>" +
		  "<div class='label'><span><i class='icon-laptop'></i>操作</span><span class='remove_pop_btn'><i class='icon-remove'></i></span></div>"+
		  "<div class='content'>" 
		   +content+
		  "</div>"
          "</div>";
        		
		$(p.id).prepend(dom);
		
	    if( $(".group_title").length ){
	    	var heigh = $(".container-fluid").height() - 41 * $(".group_title").length - 34 ;
	    	$(".form-horizontal").css({
	    		"max-height" : heigh,
	    		"overflow" :"auto"
	    	});
	    }
		
		$(document).off("click",".tab_group li").on("click",".tab_group li",function(){
			 $(this).parent(".tab_group").find(".active_li").removeClass("active_li");
			 $(this).addClass("active_li");
			 $(this).parents(".form-horizontal").find("[tab_group]").removeClass("active_li_show").addClass("active_li_hide");
			 $(this).parents(".form-horizontal").find("[tab_group = '"+ $(this).html().trim() +"']").removeClass("active_li_hide").addClass("active_li_show");
		});
		$(document).off("click",".pop .expand").on("click",".pop .expand",function(){
			//  debugger;
			  var group_show = $(this).parents(".pop").find(".group_show").not( $(this).parents(".group_title").next() );
			  group_show.removeClass(" group_show ").addClass("group_hide");	
			  group_show.prev().find(".expand").removeClass("minus");
		      $(this).parents(".group_title").next().toggleClass("group_hide").toggleClass("group_show ");	
		      $(this).toggleClass("minus");
		});
		
		$(".remove_pop_btn").on("click",function(){
			$(this).parents(".pop").remove();
		});
	}
	
	window.pop = pop;
}())