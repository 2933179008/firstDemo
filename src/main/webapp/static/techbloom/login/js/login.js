(function(){
   
   var error = {
	"100":"服务器连接失败！",
	"101":"用户名或密码不正确！",
	"102":"您的账号被禁止登录，请联系管理员！",
	"103":"您的账号信息不存在，请联系管理员！",
	"104":"未知错误！",
    "105":"请输入用户名！",
    "106":"请输入密码！",
    "107":"系统参数adapteRfid未配置"
   };
   var loginurl  = context_path + "/login/aysncLogCheck";
   
   var myApp = angular.module('login', []);
   myApp.controller('loginController', function ($scope) {
      
	  $scope.user = {
		  name : $.cookie('name') || "",
		  pwd : $.cookie('pwd') || ""
	  }

      $scope.isremember =  $scope.user.name && $scope.user.pwd ? true : false;

	  var _vali = function(){
          var _vali = true;
		  if( !$scope.user.name ){
		      $scope.user_error = error["105"];
		      _vali = false;
		  }
		  if( !$scope.user.pwd ){
			  $scope.pwd_error = error["106"];
			   _vali = false;
		  }
	      return _vali;
      };

      $scope.$watch("user.name",function( newV, oldV ){
	      if( newV ){
		         $scope.user_error = ""; 
				 if( $scope.isremember ){
				    $.cookie('name',$scope.user.name, {
				        expires : 7
		            });
				 }
		  }
	  });
      $scope.$watch("user.pwd",function( newV, oldV ){
	      if( newV ){
		         $scope.pwd_error = ""; 
				 if( $scope.isremember ){
				   $.cookie('pwd', $scope.user.pwd, { 
				        expires : 7
		            });
				 }
		  }
	  });
      $(document).on("keydown",function(e){  if(e.keyCode == 13){  
    	  
    	  $scope.$apply(function(){
    		  $scope.login(); 
    	  });
      } })
      $scope.login = function(){
	       if( _vali() == false ) return; 

		   $.ajax({
		     type : "post",
		     url :　loginurl,
             data : {loginname : $scope.user.name , password : $scope.user.pwd }, 
             dataType: "json",
			 success : function( data ){
			   $scope.login_error  ="";
			   if( data && data.errcode){			   
				   $scope.login_error   = error[data.errcode];
			   }
			   if(data && data.errcode ==""){
				   document.location =context_path+data.url;
			   }
			   $scope.$apply();
			 } 
		  });
		  return _vali;
	  };  

      $scope.saveCookie =function(){
	       if( _vali() == false) {
		     
			 $scope.isremember = false;
		     return ;
		   }
		   if( $scope.isremember === false ){
		       $.removeCookie("name");
               $.removeCookie("pwd");
		   }else{
			   $.cookie('name',$scope.user.name, {
					expires : 7
			   });
			   $.cookie('pwd', $scope.user.pwd, { 
					expires : 7
			   });
		   }
		   
	  };
   });

}());