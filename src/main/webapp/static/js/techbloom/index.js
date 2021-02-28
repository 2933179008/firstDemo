(function(){
    
	var $layerAlertWindow = null;
	
	
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
	var init = function( menus ,pid ){
	    for(var i = 0 ,l = menus.length; i < l; i++){
		    let data = menus[i];
		    let _class = data.subMenu && data.subMenu.length ? "submenu" : ""
		    let _li = `<li id="${data.menuId}" class="${_class}" pid="${data.parentId}"><a><i class="fa ${data.menuIcon || 'fa-angle-right'}"><b class="bg-primary"></b></i> <span>${data.menuName}</span>${data.menuIcon ? '<i class="fa fa-angle-down text" style="float: right;"></i>':""}</a> </li>`;
		    var parent  =  $("#sidebar>ul");
		    if( pid ){
		    	!$(pid).find("ul").length &&  $(pid).append("<ul></ul>");
		    	parent = $(pid).find("ul");
		    }
		    parent.append( _li ).on("click", "#"+data.menuId, function(){
		    
		    	if(data.menuUrl && !data.menuUrl.startsWith("#")){

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
				    		if( data && data.url ){
							  $.get( context_path+data.url ).done(function( data ){
							        if( data ){
							        	//将网页添加进来
									   $(".container-fluid").empty();
									   $(".container-fluid").append(data);
									   //初始化菜单按钮
									   //initButtonState();
									}
							  });
							}else if(data){
								   $(".container-fluid").empty();
								   $(".container-fluid").append(data);
								   //初始化菜单按钮
								   //initButtonState();
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
		  if( _class ){
			  init(data.subMenu, "#"+data.menuId );
		  }
		}
	};
    
	$.ajax({
	  type : "get",
      url : menuUrl,
      success : function( data ){
	     if(data && $.type(data)== "array" && data.length ) init( data );
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
			openLayer('/web/main/pwd.jsp' , "设置密码");
		}
		if( $("i", this).hasClass("icon-user") ){
			openLayer('/web/main/userInfo.jsp' , "个人资料");
		}
		if( $("i", this).hasClass("icon-external-link") ){
			openLayer('/web/main/productCode.jsp' , "代码生成", ['800px', '645px']);
			
		}
	});
	
}());


/*$(function() {
	if (typeof ($.cookie('menusf')) == "undefined") {
		$("#menusf").attr("checked", true);
		$("#sidebar").attr("class", "menu-min");
	} else {
		$("#sidebar").attr("class", "");
	}
});
function cmainFrame() 
{
	var hmain = document.getElementById("mainFrame");
	var bheight = document.documentElement.clientHeight;
	hmain.style.width = '100%';
	hmain.style.height = (bheight - 51 - $("#breadcrumbs").height() - 2) + 'px';
}
cmainFrame();
window.onresize = function() {
	cmainFrame();
};
*/