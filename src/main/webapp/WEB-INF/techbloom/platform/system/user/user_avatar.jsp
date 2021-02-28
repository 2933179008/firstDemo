<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
    
    <link rel="stylesheet" type="text/css" href="<%=path %>/scripts/Jcrop/jquery.Jcrop.css" />
    <link rel="stylesheet" type="text/css" href="<%=path %>/fui/css/fui.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/main.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/themes/default.css" />
    <style type="text/css">
    	html,body{overflow:hidden;}
    	body{background-color:#fff;}
    	.fl{color:#aaa;text-align:center;vertical-align:top;}
    	.preview-face{border:1px solid #aaa;margin:0 auto;overflow:hidden;margin-bottom:5px;}
    	.crop-info{text-align:left;color:#e28a00;margin-bottom:10px;}
    </style>
  </head>
 
  <body>
  	<div style="float:left;width:100%;">
  	<div style="padding:10px 20px;">
  	  <div style="padding:5px;">
  	  	 <div id="select_avatar" class="clearfix"></div>
  	  	 <span id="upload_items" style="display:none;"></span>
  	  	 <font color="#aaa">仅支持JPG,GIF,PNG图片文件，且文件小于3M</font>
  	  </div>
  	  
  	  <div>
   	  <table cellpadding="0" cellspacing="0" border="0">
   	  	<tr>
   	  		<td align="center" width="">
   	  		 	<div id="crop_box" style="width:300px;height:300px;border:1px solid #aaa;background-color:#f3f3f3;text-align:center;overflow:hidden;">
   	  		 		<img id="crop_target" src="<%=path %>/images/blank.gif"/>
   	  		 	</div>
   	  		</td>
   	  		<td><div style="width:15px;"></div></td>
   	  		<td align="left" valign="top" style="border-left:1px solid #aaa;padding-left:12px;">
   	  			<div class="crop-info">您上传的头像会自动生成三种尺寸，<br/>请注意中小尺寸的头像是否清晰.</div>
   	  			<div>
	   	  			<div class="inline fl" style="width:190px;">
		   	  			<div class="preview-face" style="width:180px;height:180px;">
		   	  				<img src="<%=path %>/images/blank.gif" id="preview_0" width="180" height="180"/> 
		   	  			</div>
		   	  			大尺寸头像，180 x 180像素
	   	  			</div>
	   	  			
	   	  			<div class="inline" style="padding-left:10px;">
		   	  			<div class="fl" style="width:90px;">
			   	  			<div class="preview-face" style="width:50px;height:50px;">
			   	  				<img src="<%=path %>/images/blank.gif" id="preview_1" width="50" height="50"/> 
			   	  			</div>
			   	  			中尺寸头像<br/>50 x 50像素<br/>(自动生成)
		   	  			</div>
		   	  			<br/>
		   	  			
		   	  			<div class="fl" style="width:90px;">
			   	  			<div class="preview-face" style="width:36px;height:36px;">
			   	  				<img src="<%=path %>/images/blank.gif" id="preview_2" width="36" height="36"/> 
			   	  			</div>
			   	  			小尺寸头像<br/>36 x 36像素<br/>(自动生成)
		   	  			</div>
	   	  			</div>
   	  			</div>
   	  		</td>
   	  	</tr>
   	  	<tr>
   	  		<td colspan="3" align="center">
   	  			<div style="padding-top:30px;line-height:30px;">
		   	  		<label class="form-btn" for="crop_btn"><button type="button" id="crop_btn" onclick="doCrop();">裁剪并保存</button></label>
			   	 	&emsp;or&emsp;
			   	 	<a href="javascript:void(0);" onclick="ownerDialog.close();">取消</a>
		   	 	</div>
   	  		</td>
   	  	</tr>
   	  </table>
   	 </div>
    </div>
    
    <form action="<%=path %>/user/User!updateAvatar.action" name="CropForm" method="post" target="crop_ifrm">
    	<input type="hidden" name="id" value="${user.id }"/>
   	 	<input type="hidden" name="avatar" id="avatar_file" value=""/>
  		<input type="hidden" name="x" id="x" value="0"/>
    	<input type="hidden" name="y" id="y" value="0"/>
    	<input type="hidden" name="w" id="w" value="0"/>
    	<input type="hidden" name="h" id="h" value="0"/>
	 </form>
	<iframe src="about:blank" name="crop_ifrm" id="crop_ifrm" height="0" style="display:none;"></iframe>
  </div>
  </body>
   <script type="text/javascript" src="<%=path %>/scripts/jquery.js"></script> 
   <script type="text/javascript" src="<%=path %>/fui/fui.js"></script>
   <script type="text/javascript" src="<%=path %>/scripts/Jcrop/jquery.Jcrop.js"></script>
   
   <script type="text/javascript">
   	 var jcrop_api, boundx, boundy;
   	 var previews = [180, 50, 36], cropSelect = [10, 10, 190, 190];
   	 
   	 function initCrop(){
   	 	if( jcrop_api ){
   	 		jcrop_api.destroy();
   	 	}
   	 	
   	 	var cropTarget = $('#crop_target');
   	 	
   	 	cropTarget.Jcrop({
	        onChange: showCoords,
	        onSelect: showCoords,
	        setSelect: cropSelect,
	        minSize: [36, 36],
	        allowSelect:false,
	        aspectRatio:1
      	}, function(){
      		 jcrop_api = this;
	      	 var bounds = this.getBounds();
	       	 boundx = bounds[0];
	         boundy = bounds[1];
	         updatePreview({"x":cropSelect[0], "y":cropSelect[1], "w":boundx, "h":boundy});
      	});
   	 }
      
	  function showCoords(c){
	      $('#x').val(c.x);
	      $('#y').val(c.y);
	      $('#w').val(c.w);
	      $('#h').val(c.h);
	      
	      if( boundx ){
	      	updatePreview(c);
	      }
	  }
	  
	  function updatePreview(c){
        if (parseInt(c.w) > 0)
        {
          for( var i = 0; i < previews.length; i++ ){
         	 var rx = previews[i] / c.w;
          	 var ry = previews[i] / c.h;
	         $('#preview_'+i).css({
	            width: Math.round(rx * boundx)+"px",
	            height: Math.round(ry * boundy)+"px",
	            marginLeft: '-' + Math.round(rx * c.x) + 'px',
	            marginTop: '-' + Math.round(ry * c.y) + 'px'
	        });
          }
        }
      }
      
      // 即时改变头像
      function loadCropAvatar( new_avatar ){
      	 document.CropForm.avatar.value = new_avatar;
      	 $("#crop_target").attr("src", "/"+new_avatar);
      	 $("#preview_0, #preview_1, #preview_2").attr("src", "/"+new_avatar);
      	 initCrop();
      }
      
      function doCrop(){
      	 var face = document.CropForm.avatar.value;
      	 if( face == "" ){
      	 	Dialog.alert("请从您的计算机中选择-张照片进行裁剪.<br/><font size='2'>点击上方的“选择照片”按钮.</font>");
      	 	return false;
      	 }
      	 $("#crop_btn").attr("disabled", "disabled").parent().addClass("disabled");
      	 document.CropForm.submit();
      }
      
   </script>
   
   <%-- gdk-fileupload组件 --%>
	<link href="<%=path%>/scripts/swfupload/css/file-upload.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=path%>/scripts/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="<%=path%>/scripts/swfupload/fileprogress.js"></script>
	<script type="text/javascript" src="<%=path%>/scripts/swfupload/handlers.js"></script>
	
	<script type="text/javascript">
	    /* 文件上传 */
		var fileupload = new FileUpload({
			targetId: 'select_avatar',
		    itemTarget:'upload_items',
		    label: "选择照片",
		 	autoUpload: true,
		 	savePath: 'avatars',
			rename: true,
		 	paramName: 'avatar',
		 	maxFileSize: 3 * 1024 * 1024,
		 	fileTypes: '*.jpg;*.jpeg;*.gif;*.png;'
		});
		
	  fileupload.setCallback({
          onUploadSuccess: function(file, serverData){
             var avatar = eval("("+serverData+")").filepath;
             loadCropAvatar(avatar);
          }
	  });
	  
	  function result(data){
	  	 if( data.result === true || data.result == 'true' ){
	  	 	var callback = "${param.callback}";
	  	 	Dialog.openerWindow()[callback].call(this, data.avatars);
	  	 	top.Dialog.tip("头像更换成功！", {delay:1200, modal:false});
	  	 	ownerDialog.close();
	  	 }else{
	  	 	$("#crop_btn").removeAttr("disabled").parent().removeClass("disabled");
	  	 	Dialog.error("<strong>头像裁剪失败！</strong><br/><span style='font-size:12px;'>请确认您上传的照片格式或照片大小是否如何要求.<br/>如仍无法裁剪请联系管理员.</span>");
	  	 }
	  }
	</script>
   
</html>
