(function( $, fun ){
	fun($);
}(jQuery, function($){

	const inputText = `<div class="control-group {{class}}"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><div class="input-append span12  "> <input class="span11 {{vali}}" type="text" name="{{oldId}}" id="{{id}}" {{disabled}}  click-relate = "{{click-relate}}"  change-relate = "{{change-relate}}"    placeholder="{{name}}"></div></div></div>`; 
	const inputText_hide = `<input type="hidden" name="{{oldId}}" id="{{id}}" />`; 
	const textarea = `<div class="control-group  {{class}}"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><div class="input-append span12  "> <textarea class="span11  {{vali}}"  name="{{oldId}}" id="{{id}}"  click-relate = "{{click-relate}}" change-relate = "{{change-relate}}" placeholder="{{name}}"></textarea></div></div></div>`; 
	const select2 =`<div class="control-group {{class}}"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><input class="span11 select2_input  {{vali}}" type="text" name="{{oldId}}" id="{{id}}"  click-relate = "{{click-relate}}" change-relate = "{{change-relate}}"></input></div></div>`;
	const selectTree =`<div class="control-group"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><input class="span11  {{vali}} " type="text" name="{{oldId}}" id="{{id}}"  click-relate = "{{click-relate}}" change-relate = "{{change-relate}}"></input></div></div>`;
	const ztree = `<ul id="{{id}}" name="{{oldId}}"  click-relate = "{{click-relate}}"  change-relate = "{{change-relate}}" class="ztree {{class}}" ></ul>`;
	const table = `<div class="row-fluid" ><table class="table" id="{{id}}"  click-relate = "{{click-relate}}" change-relate = "{{change-relate}}" style="width:100%;" >  </table><div id="grid-pager"></div></div>`;
	const check = `<div class="control-group {{class}}"><label class="control-label" for="">{{name}}：</label><div class="controls">{{checkbox}}</div></div>`;
	const checkbox = `<div class=""><div class="checker"> <input role="checkbox" type="checkbox" id="{{id}}" class="cbox  {{vali}}" name="{{oldId}}" value ="{{value}}" >  <label for="{{id}}" class="check-box"></label></div><span>{{name}}</span></div>`;
	const radio = `<div class="control-group {{class}} "><label class="control-label ">{{name}}：</label><div class="controls ui-radio ui-radio-info">{{radiobox}}</div></div>`;
	const radiobox = `<label class="ui-radio-inline" style="margin-right: 5px;"><input class=" {{vali}}" type="radio" id="{{id}}" name="{{oldId}}" value="{{value}}"><span>{{name}}</span></label>`; 
	const date = `<div class="control-group {{class}}"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><div class="input-append span12 form_date "> <input class="span11  {{vali}}" type="text" name="{{oldId}}" id="{{id}}" {{disabled}}  click-relate = "{{click-relate}}"  change-relate = "{{change-relate}}"    placeholder="{{name}}" /><span class="date_icon" ><i class="icon-calendar"></i></span></div></div></div>`; 
	const datetime = `<div class="control-group {{class}}"><label class="control-label" for="{{id}}">{{name}}：</label><div class="controls"><div class="input-append span12 form_date "> <input class="span11  {{vali}}" type="text" name="{{oldId}}" id="{{id}}" {{disabled}}  click-relate = "{{click-relate}}"  change-relate = "{{change-relate}}"    placeholder="{{name}}" /><span class="date_icon" ><i class="icon-calendar"></i></span></div></div></div>`; 
    
	
	
	var funs = function(ui){
		jQuery.each( ("changeDate blur focus focusin focusout load resize scroll unload click dblclick " +
				"mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " +
				"change select submit keydown keypress keyup error contextmenu").split(" "), function( i, name ) {
			    var _name = name.toLowerCase();
			    if( typeof ui.opts[_name + "-fun"] =="string"   && $.isFunction(  window[ ui.opts[_name + "-fun"] ]  )  ){
			    	 $(ui).find("input").on( name, null, window[ui.opts[_name + "-fun"]]  );
			    }
			    else if( $.isFunction(ui.opts[_name + "-fun"]) ){
			    	 $(ui).find("input").on( name, null, ui.opts[_name + "-fun"]);
			    }
			   
			});
	};
	var replaceStr = function( attrs,dom ){
		var _dom  = dom;
		attrs["click-relate"]  = (attrs["click-relate"] ? attrs["click-relate"] : "");
		attrs["change-relate"]  = (attrs["change-relate"] ? attrs["change-relate"] : "");
		attrs["oldId"]  = (attrs["oldId"] ? attrs["oldId"] : attrs["id"]);
		attrs["vali"]  =  (attrs["vali"] && ( typeof attrs["vali"]  == "object" )) ? JSON.stringify(attrs["vali"]).replaceAll("\"","\'") :"";
		for(var k in attrs ){
			if( attrs.hasOwnProperty(k) ){
				var name = attrs[k] && attrs[k].constructor.name,attrName="" , attrValue="";
				if(name == "Attr" ){
					attrName = attrs[k].name;
					attrValue = attrs[k].value;
				}else{
					attrName = k;
					attrValue = attrs[k];
				}
			
				_dom = _dom.replaceAll("{{"+attrName+"}}", attrValue );
			}
			
		};
		return _dom;
	};
	var replaceStr2 = function( attrs,dom ){
		var _dom  = dom;
		
//	attrs["click-relate"]  = (attrs["click-relate"] ? attrs["click-relate"] : "");
		attrs["vali"]  =  (attrs["vali"] && ( typeof attrs["vali"]  == "object" )) ? JSON.stringify(attrs["vali"]).replaceAll("\"","\'") :"";

		for(var attr in attrs ){
			var value = attrs[attr];
			
			_dom = _dom.replaceAll("{{"+attr+"}}", value );
		};
		
		return _dom;
	};
	var replaceAll = function( dom ){
		var _this = this;
		var _dom = replaceStr( _this.opts, dom);
		var _$ = $(_dom);
		_$.oldDom = this;
		_$.opts = this.opts;
	    $(this).replaceWith( _$ );
	    funs( _$ );
	    return _$;
	};
	var attrsToObject = function(attrs){
		var obj = {};
		for(var i of attrs){
			obj[i.name] = i.value;
		}
		return obj;
	}
	var loadDom = function( dom, opts ){
		if( !dom.hasAttribute("Tui") &&  dom.localName != "tui" ) return;
		
		dom.opts =$.extend({}, attrsToObject(dom.attributes),  opts[ dom.getAttribute("id") ] );
		var type = dom.getAttribute("type") || dom.opts["type"];
		_tui()[type].call(dom);
		
	};
	var _tuiLoadFun = {
	    ztree : {
	    	ready : function(){
				var _this = this;
	    		$.fn.zTree.init( $("#"+_this.oldDom.getAttribute("id") ), _this.oldDom.opts.setting);

	    	},
	    	returnStr : function( attrs ){
	    		var dom  = replaceStr2(attrs, ztree);
	    		var _attrs = $.extend({}, attrs);
	    		var _input = attrs.input;
	    		_input.oldId = _input.id;
	     		_input.id = _input.id +(new Date()).getTime() + parseInt(Math.random()*10000);
	     		//console.dir(_input.id );
	    		dom +=  replaceStr2(attrs.input, inputText_hide);
	    	//	console.dir(dom);
                var js = 'var setting = { '+
		    	'		    check: {'+
		    	'		        enable: 	'+(attrs["enable"] ? !!attrs["enable"] : true)+
		    	'		    },'+
		    	'		    data: {'+
		    	'		        simpleData: {'+
		    	'		            enable: true'+
		    	'		        }'+
		    	'		    },'+
		    	'		    edit: {'+
		    	'		        enable: false,'+
		    	'		        drag:{'+
		    	'		        	isCopy:false,'+
		    	'		        	isMove:false'+
		    	'		        }'+
		    	'		    },'+
		    	'			async: {'+
		    	'				enable: true,'+
		    	'				url:"'+attrs.url+'",'+
		    	'				autoParam:["id"],'+
		    	'				otherParam: '+JSON.stringify(attrs.otherParam)+','+
		    	'				type: "POST"'+
		    	'			},'+
				'	    	callback: {'+
				'                onAsyncSuccess: function(event, treeId, treeNode, msg) {'+
				'                      var nodes = JSON.parse(msg);'+
				'	    			   var menuids = "";'+
				'                      for(var rolei=0,count = nodes.length;rolei<count; rolei++){'+
				'	    				var menuNode = nodes[rolei];'+
				'	    				if(menuNode.checked>0){'+
				'	    					if(menuids.length>0){'+
				'	    						menuids += ","+menuNode.id;'+
				'	    					}else{'+
				'	    						menuids += menuNode.id;'+
				'	    					}'+
				'	    				}'+
				'	    			}'+
				'	    			$("#'+	_input.id+'").val(menuids);'+
				'                },'+
				'	    		onCheck: function(event, treeId, treeNode) {'+
				'	    			var treeObj = $.fn.zTree.getZTreeObj(treeId);'+
				'	    			var nodes = treeObj.getCheckedNodes(true);'+
				'	    			var menuids = "";'+
				'	    			for(var rolei=0,count = nodes.length;rolei<count; rolei++){'+
				'	    				var menuNode = nodes[rolei];'+
				'	    				if(menuNode.id>0){'+
				'	    					if(menuids.length>0){'+
				'	    						menuids += ","+menuNode.id;'+
				'	    					}else{'+
				'	    						menuids += menuNode.id;'+
				'	    					}'+
				'	    				}'+
				'	    			}'+
				'	    					$("#'+	_input.id+'").val(menuids);'+
				'	    		},'+
				'		    	onClick: function(event, treeId, treeNode) {'+
				'	    			var treeObj = $.fn.zTree.getZTreeObj(treeId);'+
				'	    			var nodes = treeObj.getSelectedNodes();'+
				'	    			var menuids = "";'+
				'	    			for(var rolei=0,count = nodes.length;rolei<count; rolei++){'+
				'	    				var menuNode = nodes[rolei];'+
				'	    				if(menuNode.id>0){'+
				'	    					if(menuids.length>0){'+
				'	    						menuids += ","+menuNode.id;'+
				'	    					}else{'+
				'	    						menuids += menuNode.id;'+
				'	    					}'+
				'	    				}'+
				'	    			}'+
				'	    			$("#'+	_input.id+'").val(menuids);'+
				'	    		}'+
				'	    	}'+
		    	'		};'+
		    	'	$.fn.zTree.init( $("#'+attrs.id+'" ), setting);';
	    		dom += '<script type="text/javascript">'+
	    		           js
	    		       +'</script>';
	    		return dom;
	    	}
	    },
	    textarea : {
	    	returnStr : function(attrs){
	    		return  replaceStr2(attrs, textarea);
	    	}
	    },
	    inputText_hide : {
	    	returnStr : function(attrs){
	    		attrs.oldId = attrs.id;
	    		attrs.id = attrs.id +(new Date()).getTime();
	    		return  replaceStr2(attrs, inputText_hide);
	    	}
	    },
	    textinput : {
	    	ready : function(){
				//var _this = this;
	    		//$.fn.zTree.init( $("#"+_this.oldDom.getAttribute("id") ), _this.oldDom.opts.setting);

	    	},
	    	returnStr : function(attrs){
	    		return  replaceStr2(attrs, inputText);	
	    	}
	    },
	    date :{
	    	ready : function(){
	    		var _this = this;
			    $("#"+_this.oldDom.getAttribute("id") ).datepicker({
			    	    language: "zh-CN",
			            autoclose: true,
			            format: "yyyy-mm-dd"
			    });
	    	},
	    	returnStr :  function(attrs){
	    		return  replaceStr2(attrs, date);	
	    	}
	    },
	    datetime :{
	    	ready : function(){
	    		var _this = this;
			    $("#"+_this.oldDom.getAttribute("id") ).datetimepicker({
			    	   
			            format: "YYYY-MM-DD HH:mm"
			    });
	    	},
	    	returnStr :  function(attrs){
	    		return  replaceStr2(attrs, datetime);	
	    	}
	    },
	    checkbox : {
	    	returnStr : function(attrs){
	    		var dom = "",one = true;
	    		for(var i in attrs.values){
	    			if(!Array.prototype.hasOwnProperty(i)){
	    				var attr = $.extend({},attrs.values[i]);
	    				attr.oldId = attrs.id;
	    				attr.vali = attrs.vali && one ? attrs.vali :"";
	    				one= false;
		    			dom += replaceStr2(attr, checkbox);	
	    			}
	    		
	    		}
	    		attrs.checkbox = dom;
	    		return 	replaceStr2(attrs, check);	
	    	}
	    },
	    radio : {
	    	
	    	returnStr : function(attrs){
	    		var dom = "", one= true;
	    		//debugger;
	    		for(var i in attrs.values){
	    			if(!Array.prototype.hasOwnProperty(i)){
	    				var attr = $.extend({},attrs.values[i]);
	    				attr.oldId = attr.id;
	    				attr.vali = attrs.vali && one ? attrs.vali :"";
	    				one= false;
	    			//	attr.id = attr.id + (new Date()).getTime()
		    			dom += replaceStr2(attr, radiobox);	
	    			}
	    		
	    		}
	    		attrs.radiobox = dom;
	    		return 	replaceStr2(attrs, radio);	
	    	}
	    },
	    selectTree : {
			returnStr : function(attrs){
		    	var  dom  =  replaceStr2(attrs, selectTree);
				var js =    '$("#'+attrs.id+'" ).selectTree('+
				               JSON.stringify( attrs.opts ) +
					        ');'
			    dom += '<script type="text/javascript">(function(){'+js+'}())</script>';
				return dom;	
			}
	    },
		select2 :{
			returnStr : function(attrs){
				var  dom  =  replaceStr2(attrs, select2);
				var js =    '$("#'+attrs.id+'" ).select2({'+
			    	        'placeholder:"'+attrs.name+'",'+
							'minimumInputLength:0,   '+
						    'allowClear: true,  '+
							'delay: 250,'+
							'formatNoMatches:"没有结果",'+
							'formatSearching:"搜索中...",'+
							'formatAjaxError:"加载出错啦！",'+
							'ajax : {'+
							'	url:"'+attrs.url+'",'+
							'	type:"POST",'+
							'	dataType : "json",'+
							'	delay : 250,'+
							'	data: function (term,pageNo) {    '+
						    '        term = $.trim(term);'+
					        '        return {'+
					        '        	queryString: term,    '+
					        '       	pageSize: 15,    '+
					        '            pageNo:pageNo,    '+
					        '            time:new Date()  '+
					        '          }'+
						    '     },'+
						    '    results: function (data,pageNo) {'+
						    '    		var res = data.result;'+
					   	    '        if(res.length>0){   '+
					   	    '          var more = (pageNo*15)<data.total; '+
					   	    '           return {'+
					   	    '                results:res,more:more'+
					   	    '             };'+
					   	    '         }else{'+
					   	    '         	return {'+
					   	    '        		results:{}'+
					   	    '        	};'+
					   	    '        }'+
						    '    },'+
							'	cache : true'+
							'}'+
		
					        '});'
			    dom += '<script type="text/javascript">(function(){'+js+'}())</script>';
				return dom;	
				
			},
			ready : function(){
				var _this = this;
				if( !_this.opts.select){
					_this.opts.select = {};
				}
			    $("#"+_this.oldDom.getAttribute("id") ).select2($.extend({},{
			    	placeholder:_this.opts.label,
					minimumInputLength:0,   //至少输入n个字符，才去加载数据
				    allowClear: true,  //是否允许用户清除文本信息
					delay: 250,
					formatNoMatches:"没有结果",
					formatSearching:"搜索中...",
					formatAjaxError:"加载出错啦！",
					ajax : {
						url:_this.opts.url,
						type:"POST",
						dataType : 'json',
						delay : 250,
						params :_this.opts.params,
						data: function (term,pageNo) {     //在查询时向服务器端传输的数据
				            term = $.trim(term);
			                return {
			                	queryString: term,    //联动查询的字符
			                	pageSize: 15,    //一次性加载的数据条数
			                    pageNo:pageNo,    //页码
			                    time:new Date()   //测试
			                 }
				        },
				        results: function (data,pageNo) {
				        		var res = data.result;
			   	            if(res.length>0){   //如果没有查询到数据，将会返回空串
			   	               var more = (pageNo*15)<data.total; //用来判断是否还有更多数据可以加载
			   	               return {
			   	                    results:res,more:more
			   	                 };
			   	            }else{
			   	            	return {
			   	            		results:{}
			   	            	};
			   	            }
				        },
						cache : true
					}

			    },_this.opts.select));
			}
		},
		table : {
			ready : function(){
				var _this = this;
				var _opts_ = {
						url : "",
						datatype : "json",
						styleUI: 'Bootstrap',
						colNames : [],
						colModel :[], 
			            rowNum : 20,
			            rowList : [ 10, 20, 30 ],
			            pager : '#grid-pager',
			            sortname : 'ID',
			            sortorder : "desc",
			            rownumbers:true,
			            altRows: true,
			            viewrecords : true,
			            caption : _this.oldDom.getAttribute("label"),
			            hidegrid:false,
			            multiselect:false,
			           // autowidth : true,
			            height:jQuery(_this).parent().height() -133,
			            width: jQuery(_this).parent().width(),
			            loadComplete : function(data) 
			            {
			            	var table = this;
			            	setTimeout(function(){updatePagerIcons(table);enableTooltips(table);}, 0);
			            	oriData = data;
			            },
			            emptyrecords: "没有相关记录",
			            loadtext: "加载中...",
			            pgtext : "页码 {0} / {1}页",
			            recordtext: "显示 {0} - {1}共{2}条数据"
					};
				 var _$table =jQuery("#" + _this.oldDom.getAttribute("id"));
				 _$table.jqGrid($.extend(_opts_, _this.oldDom.opts));
				 _$table.jqGrid('filterToolbar',{
					 searchOperators : true,
					 beforeSearch : function(){
						 
					 }});
				 _$table.navGrid('#grid-pager',{edit:false,add:false,del:false,search:false,refresh:false})
				 .navButtonAdd('#grid-pager',{  
					caption:"",   
					buttonicon:"fa fa-refresh green",   
					onClickButton: function(){   
						$("#grid-table").jqGrid('setGridParam', 
								{
							 		postData: {queryJsonString:"",page:1} //发送数据 
								}
						).trigger("reloadGrid");
					}
				})
				 .navButtonAdd('#grid-pager',{
			    	  caption: "",
					  buttonicon:"fa  icon-cogs",   
			          onClickButton : function (){
			        	  _$table.jqGrid('columnChooser',{
			        		  done: function(){
			        			  _$table.setGridWidth( jQuery(_this).parent().width());
			        			  _$table.setGridHeight(jQuery(_this).parent().height() -133);
			        		  }
			        	  });
			          }
			     }); 
				 if( _this.oldDom.opts.configGroup ){
					
					 _$table.navButtonAdd('#grid-pager',{
				    	  caption: "",
						  buttonicon:"fa  icon-random",   
				          onClickButton : function (){
				        	  $(this).jqGrid('setGridParam', { multiselect: !this.p.multiselect }).trigger("reGrid");
				          }
				     });
				 }
				 if( _this.oldDom.opts.exportFile ){
					 _$table.navButtonAdd('#grid-pager',{
				    	  caption: "",
						  buttonicon:"fa   icon-download-alt",   
				          onClickButton : function (){
				        	   var _this  = this;
				        	   $(".pop").remove();
				        	   if($(".pop").length ==0){
				   					var parent =$(_this).parents("[id ^= 'gbox_']").parent();
						    		new pop({
						    			id : parent,
						    			ts :　_this,
						    			sign : "exportFile",
						    			jsFun : function(){}
						    		});
				   				}
				          }
				     }); 
				 }
				 if(_this.oldDom.opts.addData){
					_$table.navButtonAdd('#grid-pager',{
				    	  caption: "",
						  buttonicon:"fa  icon-plus",   
				          onClickButton : function (){
				        	   var _this  = this;
				        	   $(".pop").remove();
				        	   if($(".pop").length ==0){
				   					var parent =$(_this).parents("[id ^= 'gbox_']").parent();
						    		new pop({
						    			id : parent,
						    			ts :　_this,
						    			sign : "addData",
						    			jsFun : function(){}
						    		});
				   				}
				          }
				     });
				 }
				 if(_this.oldDom.opts.editData){
					 _$table.navButtonAdd('#grid-pager',{
				    	  caption: "",
						  buttonicon:"fa  icon-edit",   
				          onClickButton : function (){
				        	   var _this  = this;
				        	   $(".pop").remove();
				        	   if($(".pop").length ==0){
				   					var parent =$(_this).parents("[id ^= 'gbox_']").parent();
						    		new pop({
						    			id : parent,
						    			ts :　_this,
						    			sign : "editData",
						    			jsFun : function(){}
						    		});
				   				}
				          }
				     });
				 }
				
				 if(_this.oldDom.opts.deleteData){
					 _$table.navButtonAdd('#grid-pager',{
				    	  caption: "",
						  buttonicon:"fa  icon-trash",   
				          onClickButton : function (){
				        	   var _this  = this;
				        	  // var selectAmount = getGridCheckedNum("#grid-table");
				        	    var data =  getGridSelectData(_this, _this.p.delParam);
				        		if($.isEmptyObject(data)){
				        			layer.msg("请选择一条记录！",{icon:2});
				        			return;
				        		}
				        		layer.confirm('确定删除？', 
				        			{
				        			  shift: 6,
				        			  moveType: 1, 
				        			  title:"操作提示",  
				        			  icon: 3,     
				        			  btn: ['确定', '取消']
				        			}, function(index, layero){
				        				$.ajax({
				        					url:_this.p.delDataurl,
				        					type:"POST",
				        					data:data,
				        					dataType:"json",
				        					success:function(data){
				        						if(data._result || data == true || data.result == true){
				        							layer.msg("操作成功!",{icon:1});
				        							//刷新列表
				        	  						$("#grid-table").jqGrid('setGridParam', 
				        								{
				        									postData: {queryJsonString:""} //发送数据 
				        								}
				        							).trigger("reloadGrid");
				        							layer.close(index);
				        						}else{
				        							layer.msg("操作失败!",{icon:2});
				        						}
				        					}
				        				});
				        				
				        			}, function(index){
				        			  layer.close(index);
				        			});
				        	
				          }
				     });
				 }
			
				
				 
				 $(window).on('resize.jqGrid', function () {
					 _$table.jqGrid( 'setGridWidth', jQuery(_this).parent().width());
					 _$table.jqGrid( 'setGridHeight',jQuery(_this).parent().height() -133 );
					});

			}
		}
	};
	var _tui = function( ){
		//var _this = this;
		return {
			inputText : function(){
				//debugger;
				replaceAll.call(this, inputText);
			},
			inputText_hide : function(){
		
				replaceAll.call(this, inputText_hide);
			},
			date : function(){
				_tuiLoadFun.date.ready.apply(  replaceAll.call(this, date) );	
			},
			datetime : function(){
				_tuiLoadFun.datetime.ready.apply(  replaceAll.call(this, datetime) );	
			},
			textArea: function(){
				replaceAll.call(this, textarea);
			},
			radio : function(){
				replaceAll.call(this,  _tuiLoadFun.radio.returnStr(this.opts) );
			},
			checkbox : function(){
				replaceAll.call(this,  _tuiLoadFun.checkbox.returnStr(this.opts) );
			},
			select2 : function(){
	
				_tuiLoadFun.select2.ready.apply(  replaceAll.call(this, select2) );	
			},
			selectTree : function(){
				
				_tuiLoadFun.selectTree.ready.apply(  replaceAll.call(this, selectTree) );	
			},
			ztree : function(){
			
				_tuiLoadFun.ztree.ready.apply( replaceAll.call(this, ztree) );	
			},
			table : function(){
				
				_tuiLoadFun.table.ready.apply( replaceAll.call(this, table) );	
			}
		};
		
	};
	
	var Tui = function( $dom , opts, data ){
		if( !$dom ||  ! ($dom instanceof jQuery) ) return;
		
	    var doms =[];
		$( "*" ,$dom ).each(function(i,e){
			if( e.hasAttribute("Tui") || e.localName == "tui" ) 
			{
				doms.push(e);
			}
			loadDom(e, opts);
			
		});
		doms.forEach((n,i) =>{  var id =n.attributes.id.value; var r = new Relate( $("#"+id)[0] ); r.bind(n); });
		
	};
	Tui.setFormData = function( $dom , data, type ){
		if( !$dom ||  ! ($dom instanceof jQuery) ) return;
		
		var form = ( type === true ? $dom : $( ".editData_group form" ,$dom ) ), _data = data;
		 Array.prototype.forEach.call(form[0].elements,function(){
    		 var input = arguments[0];     
    		 var name = input.name || input.id;
    	
    		 if($(input).hasClass("select2_input")){
    			 $(input).select2("data", data[name]); 
    		 }
    		 if($(input).hasClass("selecttree")){
    			 $(input).setsTreeData( data[name]); 
    		 }
    		 else if(input.type == "radio"){
    			  input.value == data[name] && (input.checked = true); 
    		 } else if(input.type == "text"){
   			  $(input).val(data[name]);
    		 }else if(input.type == "checkbox"){
    			  input.checked = data[name] ? true : false; 
    		 }
             else if(input.type =="textarea"){
            	  $(input).val(data[name]);
              }
             else if(input.type =="hidden"){
            	  $(input).val(data[name]);
              }           		 
         });
    	 $('.ztree', form).each(function(){
    		 var treeObj = $.fn.zTree.getZTreeObj( $(this).attr("id") );
    		 $.extend( treeObj.setting.async.otherParam, _data);
    		 treeObj.reAsyncChildNodes(null, "refresh");
    	 })
	}
	var TuiStr = function(  o ){
		var opts = $.extend(true, {}, o);
		var type = opts["type"];
		opts.oldId = opts.id;
		opts.id = opts.id + (new Date()).getTime()+ parseInt(Math.random()*10000);
		return _tuiLoadFun[type].returnStr( opts );	
	};
	window.Tui = Tui;
	window.TuiStr = TuiStr;
	window._tuiLoadFun = _tuiLoadFun;
}))