(function(){
	window.dh = function( el ){
		var _t = this;
		el.off("click").on("click",function(){
				_t.open(); 
		 });
	};
	dh.prototype = {
	    constructor : function(){},
		menus : (()=>{return  $.get(context_path + "/login/getMenu");})(),
		user_menu : (()=>{return  $.get(context_path + "/user/getUserMenu");})(),
		afterLoad: function(){
		    var user_menu = this.user_menu.responseJSON || [];
			for(let i = 0; i < user_menu.length; i++){
				var id  = user_menu[i].menu_id;
				$("[menu_id='"+id+"']")[0].checked = true;
				$("[menu_id='"+id+"']").trigger("change");
			}
		},
		bindFun : function(){
			var _this = this;

			$(".all-product input").off("change").on("change", (function(_this){
				return function(){
					var value = this.attributes.menu_id.value;
					var data = _this.menuData[value];
					var name  = data.menuName, id  = data.menuId;
                    if( this.checked  == true ){
	                   $(".seleted-product").append("<div class='selected-item' id='"+id+"' data-service='"+id+"'><i class='fa fa-close'></i><p>"+name+"</p></div>");
					}else{
						$("[data-service='"+id+"']").remove();
					}
                   
                	/*if($(".seleted-product .selected-item").length < 8){
                    	$(".all-product input").removeAttr("disabled");
					}else{
						$(".all-product input").not(":checked").attr("disabled", "disabled");
					}*/
                	$(".selected-count").html($(".seleted-product .selected-item").length );

                	var subMenu = data.subMenu || [];
                	for(let j = 0; j < subMenu.length; j++){
                		$("[data-service='"+subMenu[j].menuId+"']").remove();
                		$("[menu_id='"+subMenu[j].menuId+"']")[0].checked = this.checked ;
                	}
                	if(data.parentId && data.parentId != "0" ){
                		var list = $('[menu_id="'+ id +'"]').parents(".product-list");
                		if(list.find(":checked").length != list.find("input").length && $("[menu_id='"+data.parentId+"']")[0].checked  ){
                			$("[menu_id='"+data.parentId+"']")[0].checked = false;
                			$("[data-service='"+data.parentId+"']").remove();
                			for(let k = 0; k < list.find(":checked").length; k++){
                				var _id_ = list.find(":checked")[k].attributes.menu_id.value;
                				var data = _this.menuData[_id_];
                				var name  = data.menuName, id  = data.menuId;
         	                   $(".seleted-product").append("<div class='selected-item' id='"+id+"' data-service='"+id+"'><i class='fa fa-close'></i><p>"+name+"</p></div>");

                			}
                			//$("[menu_id='"+data.parentId+"']").trigger("change");
                		}
                	}
				}
			}(_this)) );
			
			$(".seleted-product").off("click", ".selected-item .fa-close").on("click",".selected-item .fa-close", (function(){
                return function(){
                	var id= this.parentElement.attributes["data-service"].value;
                	$("[menu_id='"+id+"']")[0].checked = false;
                	$("[menu_id='"+id+"']").trigger("change");
                }
				
			}()) );
			$( ".seleted-product" ).sortable();
			
			$(".dialog-ok-btn").off("click").on("click",function(){
				var arr = $( ".seleted-product" ).sortable( "toArray" );
				$.ajax({
					url : context_path + "/user/setUserMenu",
					type:"post",
					data : {menus: arr.join(",")},
					success: function(data){
						if(data == true){
							layer.msg("保存成功！",{icon:1});
							_this.user_menu = (()=>{return  $.get(context_path + "/user/getUserMenu");})();
                            layer.closeAll();
						}else{
							layer.msg("保存失败！",{icon:1})
						}
					}
				});
			});
			$(".dialog-close-btn ").off("click").on("click",function(){
				layer.closeAll();
			});
		},
		createDom : function(){
			var dom ="", 
			    user_menu_dom = "",
			    menus_dom = "",
			    user_menu = this.user_menu.responseJSON || [],
			    menus = this.menus.responseJSON || [],
			    _this = this;
			
			    _this.menuData = [];
			    
			
			for(let i = 0; i < menus.length; i++){
                var subMenu = menus[i].subMenu || [], type="";
                _this.menuData[menus[i].menuId] = menus[i];
				for(let j = 0; j < subMenu.length; j++){
				    _this.menuData[subMenu[j].menuId] = subMenu[j];
					type +=('<label class="product-item">'+
						   '<input type="checkbox" menu_id="'+subMenu[j].menuId+'" class="product-cb" name="service">'+subMenu[j].menuName+'</label>');
                }
				
				menus_dom +='<div class="service-type"><h3><span style=" padding-bottom: 5px;" ><input type="checkbox" menu_id="'+menus[i].menuId+'" class="product-cb" name="service" >'+menus[i].menuName+'</span></h3><div class="product-list">'+type+'</div></div>';

			}
			/*for(let i = 0; i < user_menu.length; i++){
				var id  = user_menu[i].MENU_ID;
				user_menu_dom += "<div class='selected-item' id='"+id+"' data-service='"+id+"'><i class='fa fa-close'></i><p>"+ _this.menuData[id].menuName+"</p></div>";
				
			}*/
			return ("<div class='dialog-panel'><div class='left'><h3>导航产品</h3>"+
			       "<div class='seleted-product'>"+user_menu_dom+"</div>"+
			       "<span class='float-right count-wrapper'>已选<i class='selected-count'>0</i></span></div>"+
			       "<div class='all-product'>"+menus_dom+"</div></div><div class='dialog-foot' >"+
			       "<span class='dialog-btn dialog-ok-btn'>确定</span><span class='dialog-btn dialog-close-btn  '>取消</span></div>");
		},
		open: function(){
			var _this= this;
			layer.open({
			    type : 1,
			    title : "自定义导航", 
			    area : ["940px","520px"],
			    moveType: 1, //拖拽风格，0是默认，1是传统拖动
			    content: _this.createDom()
			  });
			this.bindFun();
			this.afterLoad();
		}
	};
	
}());