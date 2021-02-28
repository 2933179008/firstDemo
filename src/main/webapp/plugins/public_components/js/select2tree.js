(function($) {
	
    var replace = function( _$dom ){
      
    	var dom = '<div class="select2-container span11 select2_input select2-allowclear">'+
    		      '<a href="javascript:void(0)" class="select2-choice" tabindex="-1"> '+
    		      '<span class="select2-chosen" ></span>'+
    		      '<abbr class="select2-search-choice-close" style="display:none;"><i class="fa fa-remove"></i></abbr>'+
    		      '<span class="select2-arrow" role="presentation"><b role="presentation">'+
    		      '<i class="icon-angle-down"></i></b></span></a>'+
    		      '<input class="select2-focusser select2-offscreen selecttree" type="text"  aria-haspopup="true"'+
    		      'role="button" aria-labelledby="select2-chosen-5"></div>'; 
    	var $dom  =  $(dom);
    	var newclassname =_$dom.attr("class") + " " + $dom.attr("class")
    	$dom.find(".select2-focusser").attr("id", _$dom.attr("id")).attr("name", _$dom.attr("name")).addClass(newclassname);
    	_$dom.replaceWith($dom);
    	return $dom;
    };
	$.fn.setsTreeData = function(value){
		if(!value) return;
		var _this = this[0];
		var id = $(this).attr("id");
		$.ajax({
		    url : _this.options.url,
	        dataType : "json",
	        type : "POST",
	        success : function(data){
	        	if(data){
	        		data.forEach(function(n){
	        			if( n[_this.options.id] == value || n[_this.options.id] == +value ){
	        				$("#"+id).val( n[_this.options.id || "id"] );
				        	$("#"+ id).prevAll(".select2-choice").find(".select2-chosen").html( n[_this.options.text||"text"]);
				        	$("#"+ id).prevAll(".select2-choice").find(".select2-search-choice-close").show();	
	        			}
	        		});
	        	}
	        }
		});
	};
	$.fn.selectTree = function(options) {
		var defaults = {

		};
		var bool = false, id =  $(this).attr("id");
		var _ts = replace($(this));
		$(_ts).find("input")[0].options = options;
		_ts.find(".select2-search-choice-close").on("click",function(){
			_ts.find(".select2-chosen").html("");
			_ts.find("input").val("");
			_ts.find(".select2-search-choice-close").hide();
			return false;
		});
		$("body").on("click",function(event){
			if( $(event.target).hasClass("select2-container") ||$(event.target).hasClass("treecontent") || $(event.target).parents(".select2-container").length >0 || $(event.target).parents(".treecontent").length >0) return;
			bool = false;
			$(".treecontent").remove();
		});
		
		_ts.on("mousewheel",()=>{
			bool = false;
			$(".treecontent").remove();
		});
		_ts.parents().on("mousewheel",()=>{
			bool = false;
			$(".treecontent").remove();
		});
		_ts.off("click").on("click",function(){
			var _id = id + (new Date()).getTime();
			
			var ul = "<div class='treecontent' style="+(bool ? "display:none;":"" )+"><ul class='ztree' id="+_id+"></ul><div>";
			bool = !bool;
			$(".treecontent").remove();
			$("body").append(ul);
			/////////syzhong
			var offset;			
			var selectChoices = $(".select2-choice");
			for (var i=0;i<selectChoices.length;i++){
				var parE = selectChoices[i].parentElement;
				var elementiD = parE.lastChild.id;
				if (elementiD == id){
					offset =  $(selectChoices[i]).offset();
					offset.top +=30; 
					break;
				}
			}
			///////
			/////////////chenyin			
			/*var offset =  $(".select2-choice").offset();
			offset.top +=30; */
			////////
			
			$(".treecontent").css(offset);
			var setting = {
					 check:{
				            enable:false
				        },
				        view: {
				            dblClickExpand: true,
				            expandSpeed: ""
				        },		
				    async: {
				        enable: true,//采用异步加载
				        url : options.url,
				        dataType : "json",
				        type: "POST"
				    },
				    data : {
				    	key: {
							name: options.text || "text",
						},
				        simpleData : {
				            enable : true,
				            idKey : options.id || "id",
				            pIdKey :options.pId || "pId",
				        }
				    },
				    callback : {
				        onClick:function(){
				        	var data = arguments[2];
				        	$("#"+id).val( data[options.id || "id"] );
				        	$("#"+ id).prevAll(".select2-choice").find(".select2-chosen").html( data[options.text||"text"]);
				        	$("#"+ id).prevAll(".select2-choice").find(".select2-search-choice-close").show();
				        	bool = false;
				        	$(".treecontent").remove();
				        }
				    }
				};
			$.fn.zTree.init($("#"+_id), setting);	
			
			
		});
		
	

	}
})(jQuery);
