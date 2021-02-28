<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
.material_table{
  width:100%;
}
.material_table thead tr{
    border: 2px solid #666;
}
.material_table thead tr th:not(:last-child){
    border-right: 1px solid #666;
}
.material_table thead tr th{
    padding: 0px;
}
.material_table .border_bottom_solid {
    border-bottom: 1px solid #666;
}
.material_table .border_bottom_dashed {
      border-bottom: 1px #666 dashed;
}
.material_table .left_title{
    float: left;
    border-right: 1px solid #666;
}
.material_table .cc_box{
    float: left;
    height: 42px;
    width: 30%;
    text-align: center;
    align-items: center;
    display: flex;
    justify-content: center;
    box-sizing: border-box;
    padding: 0px 8px;
}
.material_table tbody tr{
    border: 2px solid #666;
    border-width: 0px 2px 1px 2px;
}
.material_table tbody tr td{
    padding: 0px;
    display: table-cell;
    overflow: hidden;
    padding: 0px;
    height: 77px;
}
.material_table tbody tr td:not(:last-child){
    border-right: 1px solid #666;
}
.material_table .text_div{
    text-align: center;
    padding: 10px 0px;
    display: flex;
    align-items: center;
    justify-content: center;
        height: 50%;
}
.material_table .text_div:not(:last-child){
    border-bottom: 1px solid #dedede;
}
.material_table  .user_num{
    border-bottom: 1px solid #666;
    padding:10px;
    text-align: center;
    display: flex;
}
.material_table  .user_num .number{
    font-size: 22px;
    width: 70%;
    color: rgb(54, 54, 247);
    margin: 0px 5px;
}
.material_table  .num_box{
    width: 25%;
    display: flex;
    box-shadow:1px 1px 0px 0px #ccc;
  /*   border: 1px solid #ccc; */
    box-sizing: border-box;
    float: left;
    height: 100%;
    text-align: center;
    padding: 0px;
    margin: 0px;
    color: rgb(54, 54, 247);
    justify-content: center;
    align-items: center;
    min-height: 22px;
}
.material_table  .num_box1{
    width: 100%;
    display: block;
    height: 100%;
}
.material_table .float_left{
    float: left;
    height: 100%;
    width: 70%;
    display: flex;
    flex-direction: column;
}
.material_table .flex{
    display: flex;
    flex-direction: column;
}
.material_table .flex_1{
    display: flex;
    flex: 1;
    justify-content: center;
    align-items: center;
}
.material_table tbody tr:last-child {
    border-width: 0px 2px 2px 2px;
}
.material_table .add_data{
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    color: #7b7b7b;
    cursor: pointer;
}
.material_table .add_data:hover{
    color: #666;
}
.input_in input{
    width: 90%;
    height: 100%;
    padding: 0px;
    border: none;
    border-bottom: 1px solid #333;
}
.input_in.sy{
    height: 100%;
    font-size: 22px;
    width: 30%;
    color: rgb(54, 54, 247);
    margin: 0px 5px;
}
.del_btn{
    text-align: center;
    color: #148c98;
}
.del_btn:hover{
    color: #1dcbdc;
    cursor: pointer;
}
</style>
<div id="cy_demo_code" class="row-fluid" style="    height: calc(100% - 70px); overflow: auto;">
	<form class="form-horizontal">
		<fieldset>
		
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-7">使用开始预定日</label>
				<div class="controls">
				  <div class="input-append span12 required">
					<input id="textinput-7"  class="span11"  name="textinput-7" type="text"
						placeholder="请输入使用开始预定日" class="input-xlarge" >
				  </div>
				</div>
			</div>
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-1">产品名</label>
				<div class="controls">
				  <div class="input-append span12 required">
					<input id="textinput-1"  class="span11"  name="textinput-1" type="text"
						placeholder="请输入产品名" class="input-xlarge">
				  </div>
				</div>
			</div>
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-2">总生产数</label>
				<div class="controls">
				  <div class="input-append span12 required">				
					<input id="textinput-2"  class="span11"  name="textinput-2" type="text"
						placeholder="请输入总生产数" class="input-xlarge">
				  </div>
				</div>
			</div>
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-3">单缸投料数</label>
				<div class="controls">
				  <div class="input-append span12 required">				
					<input id="textinput-3"  class="span11"  name="textinput-3" type="text"
						placeholder="请输入单缸投料数" class="input-xlarge">
				  </div>
				</div>
			</div>
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-4">缸投料</label>
				<div class="controls">
				  <div class="input-append span12 required">				
					<input id="textinput-4"  class="span11"  name="textinput-4" type="text"
						placeholder="请输入缸投料" class="input-xlarge" >
				  </div>
				</div>
			</div>
			<!-- Text input-->
			<div class="control-group">
				<label class="control-label" for="textinput-5">调配室使用线</label>
				<div class="controls">
				  <div class="input-append span12 required">				
					<input id="textinput-5"  class="span11"  name="textinput-5" type="text"
						placeholder="请输入调配室使用线" class="input-xlarge">
				  </div>
				</div>
			</div>
			<!-- Textarea -->
			<div class="control-group">
			  <label class="control-label" for="textarea-1">特殊事项栏</label>
			  <div class="controls">   
				  <div class="input-append span12 ">			                    
			        <textarea id="textarea-1" class="span11" name="textarea-1"></textarea>
                  </div>
			  </div>
			</div>
			<!-- table -->
			<div class="control-group" style="padding: 10px;">
			   <table class="material_table">
			      <thead>
			        <tr>
			          <th style="width: 20%;"><div>原材料名</div><div>厂家名</div></th>
			          <th style="width: 10%;"><div>库位</div></th>
			          <th style="width: 25%;"><div class="border_bottom_solid">使用量</div><div class="border_bottom_dashed">使用数量</div><div>每缸投料数</div></th>
			          <th style="width: 25%;"><div class="border_bottom_solid">准备数量</div><div class="left_title cc_box">仓库发货数量</div><div class="cc_box" style="width:70%;">调配数量确认</div></th>
			          <th style="width: 15%;"><div>生产剩余数量</div></th>
			           <th style="width: 5%;"><div>操作</div></th>
			        </tr>
			      </thead>
			     <tbody>
			      <!--  <tr>
			          <td><div class="text_div">白糖</div><div class="text_div">AAA白糖有限公司</div></td>
			          <td><div class="text_div">A库位</div></th>
			          <td>
			          <div class="flex">
			            <div class="user_num"><span class="number">6.0</span><span class="unit">kg</span></div>
			            <div  style="    display: flex;"><span class="num_box">1.0</span><span class="num_box unit">kgx</span><span class="value num_box">6</span><span class="unit num_box">箱</span></div>
			            <div  style="    display: flex;"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box">0.00</span><span class="unit num_box">kg</span></div>
			          </div>
			          </td>
			          <td>
			          <div class="num_box1">
			            <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number">6.0</span></div>
			            <div class="float_left">
			               <div class="flex_1" ><span class="num_box">1.0</span><span class="num_box unit">kgx</span><span class="value num_box">6</span><span class="unit num_box">箱</span></div>
			               <div class="flex_1" ><span class="num_box"></span><span class="num_box">+</span><span class="value num_box">0.00</span><span class="unit num_box">kg</span></div>
			            </div>
			          </div>
			          </td>
			          <td><div class="text_div"><span>3.0</span><span class="unit">kg</span></div></td>
			        </tr> -->
			        <tr> 
			         <td colspan="6">
			           <div class="add_data"><i class="fa fa-plus"></i><div>添加数据</div></div>
			         </td>
			        </tr>
			     </tbody>
			   </table>
			  
			</div>
		</fieldset>
	</form>
</div>
<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
	<span class="savebtn btn btn-success">保存</span>
	<span class="btn btn-danger" >取消</span>
</div>
<script type="text/javascript">
$(function(){

  var dom=`<tr>
      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
      <td><div class="text_div input_in"></div></th>
      <td>
      <div class="flex">
        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
      </div>
      </td>
      <td>
      <div class="num_box1">
        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
        <div class="float_left">
           <div class="flex_1" ><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
           <div class="flex_1" ><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
        </div>
      </div>
      </td>
      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
      <td><div class="del_btn">删除</div></td>
    </tr>`;	
  $(".add_data").on("click",()=>{
	  $(".add_data").parents("tr").before(dom);	  
  }) ;  
  $("body").off("click",".input_in").on("click",".input_in",(event)=>{
	  var value = $(event.target).html();
	  $(event.target).html("<input type='text'  value='"+value+"' />");
	  $(event.target).find("input").focus();
  });
  $("body").off("blur",".input_in").on(" blur",".input_in",(event)=>{
	  var value = $(event.target).val();
	  $(event.target).parent().html(value);
  });
  $("body").off("click",".del_btn").on(" click",".del_btn",(event)=>{
	  $(event.target).parents("tr").remove();
  });
});
</script>