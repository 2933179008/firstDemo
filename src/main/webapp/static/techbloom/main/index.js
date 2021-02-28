(function(){
	var $layerAlertWindow = null;
	window.page_interval = [];

	$(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError ){
		//console.dir(jqXHR);
		//console.log(thrownError);
		var responseStatus = jqXHR.status;
		var responseStatusText = jqXHR.statusText;
		/*console.log("-----------------ajaxError---------------------");
		console.log(responseStatus);*/
		if(responseStatus==500){
			layer.confirm("系统内部错误，请联系管理员！");
		}else if(responseStatus==404){
			//layer.confirm("资源找不到！");
		}
		
	});
	
	var menuUrl =  context_path + "/login/getMenu";
    //加载菜单
    $(function(){
    	$(".sidebar-item").hover(function(){
    		$(".sidebar-item .sub-menu.service-list").css("max-width","1100px");
    	},function(){
    		$(".sidebar-item .sub-menu.service-list").css("max-width", "0px");
    	});
    
    })
	var init_new = function(menus){
			 var parent  =  $("#sidebar>div");
			 var trs ="", tr_index = -1;
			 for(let i = 0 ,l = menus.length; i < l; i++){
			     if( parseInt(i/6) != tr_index){
			    	 if(tr_index != -1){
			    		 trs += "</tr>";
			    	 }
			    	 tr_index =  parseInt(i/6);
			    	 trs += "<tr>";
			    	
			     }
			     trs +='<td class="sub-nav-section"><h3>'+menus[i].menuName+'</h3><ul>';
    		     if(menus[i].subMenu){
    		    	 for(let j = 0; j < menus[i].subMenu.length; j++){
    		    		 trs +="<li id="+menus[i].subMenu[j].menuId+"><a><i class='fa "+( menus[i].subMenu[j].menuIcon  ?　menus[i].subMenu[j].menuIcon:'iconfont  fa-angle-right')　+"'></i>"+menus[i].subMenu[j].menuName+"</a></li>";
    		    		 let data = menus[i].subMenu[j];
    		    		 parent.on("click", "#"+data.menuId, function(){
    		    			   
    		  		    	if(data.menuUrl && !data.menuUrl.startsWith("#")){
    		  			    	
    		  			    	for( var i in page_interval ){
    		  			    		 clearInterval(page_interval[i]);
    		  			    	}
    		  		    		layer.closeAll();
    		  			    	layer.msg('加载中', {icon: 16});
    		  			    	$("#breadcrumb .current").remove();
    		  			    	$("#breadcrumb").append(`<a href="#" class="current">${data.menuName}</a>`);
    		  			    	$("#sidebar .active").removeClass("active");
    		  			    	$(this).addClass("active");
    		  				    $.ajax({
    		  				    	url:context_path + "/" +data.menuUrl,
    		  				    	type:"POST",
    		  				    	success:function(data){
    		  				    		$(".sidebar-item .sub-menu.service-list").css("max-width", "0px");
    		  				    		layer.closeAll();
    		  				    		
    		  				    		if( data && data.url ){
    		  				    			/* if(data.url.indexOf("/system/") != -1 ){
    		  					    			  data.url ="/public-resources" +data.url;
    		  					    		  }*/
    		  					    		  
    		  	 							  $.get( context_path+data.url ).done(function( data ){
    		  								        if( data ){
    		  								        	//将网页添加进来
    		  										   $(".container-fluid").empty();
    		  										   $(".container-fluid").append(data);
    		  										   //初始化菜单按钮
    		  										   initButtonState();
    		  										}
    		  								  });
    		  							
    		  							}else if(data){
    		  									if( data.indexOf("admin_login") != -1 ){
    		  										window.location =  context_path + "/login/logout.do";
    		  						    			return false;
    		  						    		}
    		  								   $(".container-fluid").empty();
    		  								   $(".container-fluid").append(data);
    		  							}
    		  				    	},
    		  				    	error:function(e){
    		  				    		if(e.status==200){
    		  				    			window.location.href = window.location.href; 
    		  				    		}
    		  				    		layer.closeAll();
    		  				    		layer.msg('加载失败！',{icon:2});
    		  				    	}
    		  				    	
    		  				    });
    		  		    	}
    		  				//return false;
    		  			});
    		    	 }
    		     };
    		     trs +='</ul></td>';
			 }
			 var table='<div class="sub-menu service-list" id="serviceList" ><table><tbody>'+trs+'</tbody></table></div>';
			 /*parent.append("<div class='sidebar-item service-overview tip-disable loaded'><a ><span>产品服务</span> <i class='fa fa-angle-right text' style='right: 10px; position: absolute;top: 13px;'></i></a>"+table+"</div>");*/
	}

	var init = function( menus ,pid ){
		var user_menu = [];
		$.ajax({
			 url:context_path + "/user/getUserMenu",
			 async: false,
             success: function( data){
            	 if(data){
            		 for(var i = 0; i< data.length ; i++){
            			 user_menu[ data[i]["menu_id"] ] = data[i];
            		 }
            	 }else{
            		 user_menu  = []; 
            	 }
            
             }
	    });
		if( user_menu.length == 0 ) return;
		
	    for(var i = 0 ,l = menus.length; i < l; i++){
		    let data = menus[i];
		    let _class = data.subMenu && data.subMenu.length ? "submenu" : ""
		    let _si_ = false;
		    let __menuId = data.menuI;
		    if(  user_menu[data.menuId] ){
		    	_si_ = true;
		    }	
		    if( data.parentId && user_menu[data.parentId]){
		    	_si_ = true;
		    }
		    if( _class ){
	    	  for( var k =0; k < data.subMenu.length; k++ ){
		    	    if(  user_menu[  data.subMenu[k].menuId  ] ){
		    	    	_si_ = true;
				    }	
			    }
			}
		    if(_si_ == true){
		    	 let _li = `<li id="${data.menuId}" class="${_class}" pid="${data.parentId}"><a><i class="fa ${data.menuIcon || 'fa-angle-right'}"></i> <span>${data.menuName}</span>${data.menuIcon ? '':""}</a> </li>`;
				    var parent  =  $("#sidebar>ul");
				    if( pid ){
				    	!$(pid).find("ul").length &&  $(pid).append("<ul></ul>");
				    	parent = $(pid).find("ul");
				    }
				    parent.append( _li ).on("click", "#"+data.menuId, function(){
				   
				    	if(data.menuUrl && !data.menuUrl.startsWith("#")){
		  			    	for( var i in page_interval ){
		  			    		 clearInterval(page_interval[i]);
		  			    	}
				    		layer.closeAll();
					    	layer.msg('加载中', {icon: 16});
					    	$("#breadcrumb .current").remove();
					    	$("#breadcrumb").append(`<a href="#" class="current">${data.menuName}</a>`);
					    	$("#sidebar .active").removeClass("active");
					    	$(this).addClass("active");
						    $.ajax({
						    	url:context_path + "/" +data.menuUrl,
						    	type:"POST",
						    	success:function(data){
						    		layer.closeAll();

						    		/**在点击菜单时，关闭叉车平板的定时器**/
                                    window.clearInterval(interval1);
                                    window.clearInterval(interval2);
                                    window.clearInterval(interval3);
                                    window.clearInterval(outStorageSlab_interval1);
						    		if( data && data.url ){
						    			/* if(data.url.indexOf("/system/") != -1 ){
							    			  data.url ="/public-resources" +data.url;
							    		  }*/
							    		  
			 							  $.get( context_path+data.url ).done(function( data ){
										        if( data ){
										        	//将网页添加进来
												   $(".container-fluid").empty();
												   $(".container-fluid").append(data);
												   //初始化菜单按钮
												   initButtonState();
												}
										  });
									
									}else if(data){
											if( data.indexOf("admin_login") != -1 ){
												window.location =  context_path + "/login/logout.do";
								    			return false;
								    		}
										   $(".container-fluid").empty();
										   $(".container-fluid").append(data);
									}
						    	},
						    	error:function(e){
						    		if(e.status==200){
						    			window.location.href = window.location.href; 
						    		}
						    		layer.closeAll();
						    		layer.msg('加载失败！',{icon:2});
						    	}
						    	
						    });
				    	}
						//return false;
					});
				    if( _class  ){
				    	  init(data.subMenu, "#"+data.menuId );
				    }
		    }
		    
		}
	};
    
	$.ajax({
	  type : "get",
      url : menuUrl,
      success : function( data ){
	     if(data && $.type(data)== "array" && data.length ) init_new( data ); init( data );
	     //默认打开月台管理
	     setTimeout(function(){
	    	  $("#sidebar #10>a").trigger("click");
	 	     $("#sidebar #10 #11").trigger("click"); 
	     },400);
	   
	  }
	});
	
	//初始化按钮状态：根据用户的权限判断
	function initButtonState(){
		$.ajax({
			url:context_path+"/user/getSessionUser",
			type:"POST",
			dataType:"JSON",
			success:function(data){
				if(data){
					for ( var d in data) {
						if(d.endWith("Qx")){
							if(data[d]==1){
								//有权限
							}else{
								//没有权限
								$("button.btn-"+d).attr("disabled","disabled");
								$("button.btn-"+d).attr("title","没有该权限");
							}
						}
					}
				}
			}
				
		});
	}
	
	//加载菜单--end
	
	var openLayer = function( url, title, area ){
		$.post(context_path + url, function(str){
			  layer.open({
			    type : 1,
			    title : title, 
			    area : area ||"600px",
			    skin : "layui-layer-molv",
			    moveType: 1, //拖拽风格，0是默认，1是传统拖动
			    content: str //注意，如果str是object，那么需要字符拼接。
			  });
		}).error(function() {
			layer.closeAll();
    		layer.msg('加载失败！',{icon:2});
		});
	}
	$("#profile-messages").on("click","li", function(){
		if( $("i", this).hasClass("icon-check") ){
			openLayer('/index/pwd' , "设置密码");
		}
		if( $("i", this).hasClass("icon-user") ){
            openLayer('/index/userInfo' , "个人资料");
		}
		if( $("i", this).hasClass("icon-external-link") ){
            openLayer('/index/productCode' , "代码生成", ['800px', '645px']);
		}
	});

	$(function(){
		 $.get( context_path+"/index/main_page" ).done(function( data ){
		        if( data ){										   
				   $(".container-fluid").empty();
				   $(".container-fluid").append(data);
				}
		    });
	})
}());