<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<style>
.material_table{
  width:100%;
  margin-top: -1px;
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
.material_table .text_div, .flex_uls .text_div{
    text-align: center;
    padding: 10px 0px;
    display: flex;
    align-items: center;
    justify-content: center;
        height: 50%;
}
.material_table .text_div:not(:last-child), .flex_uls .text_div:not(:last-child){
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
._title_{
   text-align: center;
   font-size: 21px;
   padding: 10px 0px;
   border: 1.5px solid #666;
}
.child_title{
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    border: 1.5px solid #666;
    padding: 5px;
    border-top: 0px;
}
.flex_grid{
    display: flex;
    border: 1.5px solid #666;
    border-top: 0px;
    border-bottom: 0px;
}
.flex_grid_box{
    flex:1;
    border-right: 1px solid #666;
    display: flex;
}
.title_s{
    width: 10px;
    border-right: 2px solid #666;
    padding: 24px 5px;
    border-right-width: 1px;
    font-size: 12px;
    font-weight: 700;
    color: #666;
}
.flex_grid textarea{
    width: 220px;
    height: 100%;
    border: 0px;
}
.th_1{
    flex: 1;
    border-right: 1px solid #666;
    display: flex;
    flex-direction: column;
}
.th_2,.th_3{
     width: 80px;
    font-size: 12px;
    font-weight: 700;
    color: #666;
    border-right: 1px solid #666;
    display: flex;
    flex-direction: column;
}
.th_3{
    border-right-width: 1px;
}
.th_3>span, .th_2>span{
    border-bottom: 1px solid #666;
    align-items: center;
    display: flex;
    justify-content: center;
    height: 53px;
}
.date_ul{
    display: flex;
    border-bottom: 1px solid #666;
    font-size: 12px;
    font-weight: 700;
    color: #666;
}
.date_ul div{
    flex: 1;
    text-align: center;
    padding: 3px 0px;
}
.date_ul div:not(:last-child){
    border-right: 1px solid #666;
}
.flex_uls{
    display: flex;
    flex: 1;
    font-size: 12px;
    font-weight: 700;
    color: #666;
}
.flex_uls>div{
    flex: 1;
    display: flex;
    flex-direction: column;
}
.flex_uls>div:not(:last-child){
    border-right: 1px solid #666;
}
.flex_uls>div>span{
    width: 100%;
    text-align: center;
    border-bottom: 1px solid #666;
    padding: 3px 0px;
}
.th_3{
  border:0px;
}
.flex_1{
    flex: 1;
    display: flex;
    flex-direction: row;
    align-items: center;
}
.flex_1>span:first-child{
    flex: 2;
    border-right: 1px solid #666;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
.flex_1>span:last-child{
    flex: 1;
    display: flex;
    justify-content: center;
    border-right-width:0px;
}
.flex_around{
    display: flex;
    justify-content: space-around;
}
.flex_around>span{
    flex: 1;
    border-right: 1px solid #666;
}
.flex_around>span:last-child{
    border-right: 0px solid #666;
}
.flex_box{
    height: 100%;
    display: flex;
    flex-direction: column;
}
.flex_box_ul{
     display: flex;
    border-bottom: 1px solid #666;
}
.flex_box_ul>div{
    flex: 1;
    border-right: 1px solid #666;
    text-align: center;
}
.flex_box_ul>div:last-child{
    border-right: 0px solid #666;
}
.flex_box_content{
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
}
.date_box{
    flex: 1;
    border-bottom: 1px solid #666;
    display: flex;
    justify-content: center;
    align-items: center;
}
.date_content{
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-around;
}
.icons_{
    border-left: 1px solid #666;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 30px;
}
.cc_box_x {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    border-right: 1px solid #666;
}
.outinput_tr{
    display: flex;
    flex: 1;
    height: 100%;
}
.outinput_tr>div{
    display: flex;
    flex: 1;
}
.outinput_tr>div:not(:last-child){
 border-right: 1px solid #666;
}
.outinput_tr>div>span{
    border-bottom: 1px solid #666;
    height: 40%;
    padding: 4px 10px;
   text-align: center;
}
.logo_form{
    position: relative;
    width: 185px;
    top: -3px;
    right: 11px;
    float: right;
}
.radius_font{
    border-radius: 50%;
    height: 20px;
    width: 20px;
    display: inline-block;
    background: #fff;
    vertical-align: top;
    color: #666;
    border: 1.4px solid #666;
    font-size: 12px;
    font-weight: 600;
}
.date_ul .input_in{
    height: 100%;
    display: flex;
    padding: 0px 10px;
}
/* #outinput_tr{
  display:none;
} */
</style>
<div id="cy_demo_code" class="row-fluid" style="    height: calc(100% - 70px); overflow: auto;">
	<form class="form-horizontal">
	
	  <!-- 表单1 -->
	  <fieldset>
		    <div style="padding: 10px 10px 0px;">
		      <div class="_title_">
		        <span>备料单<span class="radius_font">1</span></span>
		        <div class="logo_form"><img src="/dyyl/plugins/public_components/img/logo_form.jpg"></div>
		      </div>
		      <div class="child_title">
		        <span>版本/修订状态：A/0</span>
		        <span>编号：TPC/C3.ZZ0205-01</span>
		      </div>
		      <div class="flex_grid">
		         <div class="flex_grid_box">
		            <div class="th_1">
		               <div class="date_ul">
		                 <div>制定日</div>
		                 <div>1900/1/0</div>
		                 <div>使用开始预定日</div>
		                 <div> <span class="input_in data_time"></span></div>
		               </div>
		               <div class="flex_uls">
		                 <div>
		                   <span>产品名</span>
		                   <div class="text_div input_in"></div>
		                   <div class="text_div input_in"></div>
		                 </div>
		                 <div>
		                   <span>总生产数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		                  <div>
		                   <span>单缸投料数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		               </div>
		            </div>
		            <div class="th_2">
		              <span>杠投料</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		               <span>缸</span>
		              </div>
		            </div>
		            <div class="th_3">
		              <span>调配室使用线</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		              </div>
		            </div>
		         </div>
		         <div class="title_s">
		                    特殊事项栏
		         </div>
		         <div>
		          <textarea></textarea>
		         </div>
		      </div>
		    </div>  
			<!-- table -->
			<div class="control-group" style="padding: 0px 10px 10px 10px;">
			   <table class="material_table">
			      <thead>
			        <tr>
			          <th style="width: 10%;"><div>原材料名</div><div>厂家名</div></th>
			          <th style="width: 5%;"><div>库位</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid">使用量</div><div class="border_bottom_dashed">使用数量</div><div>每缸投料数</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid ">确认栏</div><div class="border_bottom_solid flex_around"><span>品名</span><span>数量</span><span>外观</span></div><div>质保期</div></th>
			          <th style="width: 20%;"><div class="border_bottom_solid">准备数量</div><div class="left_title cc_box">仓库发货数量</div><div class="cc_box" style="width:70%;">调配数量确认</div></th>
			          <th style="width: 15%;">
			          <div style="
						    display: flex;
						    flex-direction: column;
						    flex: 1;
						    padding: 0px;
						"><div class="border_bottom_solid" style="
						    height: 20px;
						">使用后（剩余）</div><div style="
						    display: flex;
						    flex: 1;
						    height: 42px;
						"><div class="cc_box_x">理论余量</div><div class="cc_box_x" style="">实际余量</div><div class="cc_box_x" style="
						    border: 0px;
						">差異</div></div></div>
			          </th>
			          <th style="width: 10%;"><div class="border_bottom_solid">计量记录</div><div class="cc_box" style="width:100%;">计量日期/缸数</div></th>     
			          <th style="width: 10%;"><div>生产剩余数量</div></th>
			   
			        </tr>
			      </thead>
			     <tbody>			  
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
			        <tr id="outinput_tr" style="height:100px;"> 
			         <td colspan="8">
			           <div class="outinput_tr">
			             <div>
			               <span>调配备料单确认者签字</span>
			             </div>
			             <div>
			               <span>品质备料单确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材作业者签字</span>
			             </div>
			             <div>
			                <span>原料资材确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業前作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業前确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業後确认者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材受取者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後组长签字</span>
			             </div>
			           </div>
			         </td>
			        </tr>
			     </tbody>
			   </table>
			  
			</div>
		</fieldset>
	  <!-- 表单2 -->
	  <fieldset>
		    <div style="padding: 10px 10px 0px;">
		      <div class="_title_">
		        <span>备料单<span class="radius_font">2</span></span>
		        <div class="logo_form"><img src="/dyyl/plugins/public_components/img/logo_form.jpg"></div>
		      </div>
		      <div class="child_title">
		        <span>版本/修订状态：A/0</span>
		        <span>编号：TPC/C3.ZZ0205-01</span>
		      </div>
		      <div class="flex_grid">
		         <div class="flex_grid_box">
		            <div class="th_1">
		               <div class="date_ul">
		                 <div>制定日</div>
		                 <div>1900/1/0</div>
		                 <div>使用开始预定日</div>
		                 <div> <span class="input_in data_time"></span></div>
		               </div>
		               <div class="flex_uls">
		                 <div>
		                   <span>产品名</span>
		                   <div class="text_div input_in"></div>
		                   <div class="text_div input_in"></div>
		                 </div>
		                 <div>
		                   <span>总生产数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		                  <div>
		                   <span>单缸投料数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		               </div>
		            </div>
		            <div class="th_2">
		              <span>杠投料</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		               <span>缸</span>
		              </div>
		            </div>
		            <div class="th_3">
		              <span>调配室使用线</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		              </div>
		            </div>
		         </div>
		         <div class="title_s">
		                    特殊事项栏
		         </div>
		         <div>
		          <textarea></textarea>
		         </div>
		      </div>
		    </div>  
			<!-- table -->
			<div class="control-group" style="padding: 0px 10px 10px 10px;">
			   <table class="material_table">
			      <thead>
			        <tr>
			          <th style="width: 10%;"><div>原材料名</div><div>厂家名</div></th>
			          <th style="width: 5%;"><div>库位</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid">使用量</div><div class="border_bottom_dashed">使用数量</div><div>每缸投料数</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid ">确认栏</div><div class="border_bottom_solid flex_around"><span>品名</span><span>数量</span><span>外观</span></div><div>质保期</div></th>
			          <th style="width: 20%;"><div class="border_bottom_solid">准备数量</div><div class="left_title cc_box">仓库发货数量</div><div class="cc_box" style="width:70%;">调配数量确认</div></th>
			          <th style="width: 15%;">
			          <div style="
						    display: flex;
						    flex-direction: column;
						    flex: 1;
						    padding: 0px;
						"><div class="border_bottom_solid" style="
						    height: 20px;
						">使用后（剩余）</div><div style="
						    display: flex;
						    flex: 1;
						    height: 42px;
						"><div class="cc_box_x">理论余量</div><div class="cc_box_x" style="">实际余量</div><div class="cc_box_x" style="
						    border: 0px;
						">差異</div></div></div>
			          </th>
			          <th style="width: 10%;"><div class="border_bottom_solid">计量记录</div><div class="cc_box" style="width:100%;">计量日期/缸数</div></th>     
			          <th style="width: 10%;"><div>生产剩余数量</div></th>
			   
			        </tr>
			      </thead>
			     <tbody>			  
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
			        <tr id="outinput_tr" style="height:100px;"> 
			         <td colspan="8">
			           <div class="outinput_tr">
			             <div>
			               <span>调配备料单确认者签字</span>
			             </div>
			             <div>
			               <span>品质备料单确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材作业者签字</span>
			             </div>
			             <div>
			                <span>原料资材确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業前作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業前确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業後确认者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材受取者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後组长签字</span>
			             </div>
			           </div>
			         </td>
			        </tr>
			     </tbody>
			   </table>
			  
			</div>
		</fieldset>
		
	  <!-- 表单3 -->
	  <fieldset>
		    <div style="padding: 10px 10px 0px;">
		      <div class="_title_">
		        <span>备料单<span class="radius_font">3</span></span>        <div class="logo_form"><img src="/dyyl/plugins/public_components/img/logo_form.jpg"></div>
		      </div>
		      <div class="child_title">
		        <span>版本/修订状态：A/0</span>
		        <span>编号：TPC/C3.ZZ0205-01</span>
		      </div>
		      <div class="flex_grid">
		         <div class="flex_grid_box">
		            <div class="th_1">
		               <div class="date_ul">
		                 <div>制定日</div>
		                 <div>1900/1/0</div>
		                 <div>使用开始预定日</div>
		                 <div> <span class="input_in data_time"></span></div>
		               </div>
		               <div class="flex_uls">
		                 <div>
		                   <span>产品名</span>
		                   <div class="text_div input_in"></div>
		                   <div class="text_div input_in"></div>
		                 </div>
		                 <div>
		                   <span>总生产数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		                  <div>
		                   <span>单缸投料数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		               </div>
		            </div>
		            <div class="th_2">
		              <span>杠投料</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		               <span>缸</span>
		              </div>
		            </div>
		            <div class="th_3">
		              <span>调配室使用线</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		              </div>
		            </div>
		         </div>
		         <div class="title_s">
		                    特殊事项栏
		         </div>
		         <div>
		          <textarea></textarea>
		         </div>
		      </div>
		    </div>  
			<!-- table -->
			<div class="control-group" style="padding: 0px 10px 10px 10px;">
			   <table class="material_table">
			      <thead>
			        <tr>
			          <th style="width: 10%;"><div>原材料名</div><div>厂家名</div></th>
			          <th style="width: 5%;"><div>库位</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid">使用量</div><div class="border_bottom_dashed">使用数量</div><div>每缸投料数</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid ">确认栏</div><div class="border_bottom_solid flex_around"><span>品名</span><span>数量</span><span>外观</span></div><div>质保期</div></th>
			          <th style="width: 20%;"><div class="border_bottom_solid">准备数量</div><div class="left_title cc_box">仓库发货数量</div><div class="cc_box" style="width:70%;">调配数量确认</div></th>
			          <th style="width: 15%;">
			          <div style="
						    display: flex;
						    flex-direction: column;
						    flex: 1;
						    padding: 0px;
						"><div class="border_bottom_solid" style="
						    height: 20px;
						">使用后（剩余）</div><div style="
						    display: flex;
						    flex: 1;
						    height: 42px;
						"><div class="cc_box_x">理论余量</div><div class="cc_box_x" style="">实际余量</div><div class="cc_box_x" style="
						    border: 0px;
						">差異</div></div></div>
			          </th>
			          <th style="width: 10%;"><div class="border_bottom_solid">计量记录</div><div class="cc_box" style="width:100%;">计量日期/缸数</div></th>     
			          <th style="width: 10%;"><div>生产剩余数量</div></th>
			   
			        </tr>
			      </thead>
			     <tbody>			  
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
				       </div>
				      </td>
				      <td>
				      <div class="num_box1">
				        <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" style="width: 100%;height: 20px;"></span></div>
				        <div class="float_left">
				           <div class="flex_1"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				           <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				        </div>
				      </div>
				      </td>
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
			        <tr id="outinput_tr" style="height:100px;"> 
			         <td colspan="8">
			           <div class="outinput_tr">
			             <div>
			               <span>调配备料单确认者签字</span>
			             </div>
			             <div>
			               <span>品质备料单确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材作业者签字</span>
			             </div>
			             <div>
			                <span>原料资材确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業前作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業前确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業後确认者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>原料资材受取者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="/dyyl/plugins/public_components/img/pointer.jpg"></div>
			             <div>
			                <span>调配作業後组长签字</span>
			             </div>
			           </div>
			         </td>
			        </tr>
			     </tbody>
			   </table>
			  
			</div>
		</fieldset>
		
		
	   <!-- 表单4 -->
	  
		<fieldset>
		    <div style="padding: 10px 10px 0px;">
		      <div class="_title_">
		        <span>备料单<span class="radius_font">4</span></span>        <div class="logo_form"><img src="/dyyl/plugins/public_components/img/logo_form.jpg"></div>
		      </div>
		      <div class="child_title">
		        <span>版本/修订状态：A/0</span>
		        <span>编号：TPC/C3.ZZ0205-01</span>
		      </div>
		      <div class="flex_grid">
		         <div class="flex_grid_box">
		            <div class="th_1">
		               <div class="date_ul">
		                 <div>制定日</div>
		                 <div>1900/1/0</div>
		                 <div>使用开始预定日</div>
		                 <div>   <span class="input_in data_time"></span></div>
		               </div>
		               <div class="flex_uls">
		                 <div>
		                   <span>产品名</span>
		                   <div class="text_div input_in"></div>
		                   <div class="text_div input_in"></div>
		                 </div>
		                 <div>
		                   <span>总生产数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		                  <div>
		                   <span>单缸投料数</span>
		                    <div class="flex_1">
				               <span class="input_in"></span>
				               <span>箱</span>
				            </div>
		                 </div>
		               </div>
		            </div>
		            <div class="th_2">
		              <span>杠投料</span>
		              <div class="flex_1">
		               <span class="input_in"></span>
		               <span>缸</span>
		              </div>
		            </div>
		            <div class="th_3">
		              <span>调配室使用线</span>
		              <div  class="flex_1">
		               <span class="input_in"></span>
		              </div>
		            </div>
		         </div>
		         <div class="title_s">
		                    特殊事项栏
		         </div>
		         <div>
		          <textarea />
		         </div>
		      </div>
		    </div>  
			<!-- table -->
			<div class="control-group" style="padding: 0px 10px 10px 10px;">
			   <table class="material_table">
			      <thead>
			        <tr>
			          <th style="width: 10%;"><div>原材料名</div><div>厂家名</div></th>
			          <th style="width: 5%;"><div>库位</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid">使用量</div><div class="border_bottom_dashed">使用数量</div><div>每缸投料数</div></th>
			          <th style="width: 15%;"><div class="border_bottom_solid ">确认栏</div><div class="border_bottom_solid flex_around"><span>品名</span><span>数量</span><span>外观</span></div><div>质保期</div></th>
			          <th style="width: 20%;"><div class="border_bottom_solid">准备数量</div><div class="left_title cc_box">仓库发货数量</div><div class="cc_box" style="width:70%;">调配数量确认</div></th>
			          <th style="width: 15%;">
			          <div style="
						    display: flex;
						    flex-direction: column;
						    flex: 1;
						    padding: 0px;
						"><div class="border_bottom_solid" style="
						    height: 20px;
						">使用后（剩余）</div><div style="
						    display: flex;
						    flex: 1;
						    height: 42px;
						"><div class="cc_box_x" >理论余量</div><div class="cc_box_x" style="">实际余量</div><div class="cc_box_x" style="
						    border: 0px;
						">差異</div></div></div>
			          </th>
			          <th style="width: 10%;"><div class="border_bottom_solid">计量记录</div><div class="cc_box" style="width:100%;">计量日期/缸数</div></th>     
			          <th style="width: 10%;"><div>生产剩余数量</div></th>
			   
			        </tr>
			      </thead>
			     <tbody>			  
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
				    <tr>
				      <td><div class="text_div input_in"></div><div class="text_div input_in"></div></td>
				      <td><div class="text_div input_in"></div></td>
				      <td>
				      <div class="flex">
				        <div class="user_num"><span class="number input_in"></span><span class="unit">kg</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box unit">kgx</span><span class="value num_box input_in"></span><span class="unit num_box">箱</span></div>
				        <div  style="display: flex;"><span class="num_box input_in"></span><span class="num_box">+</span><span class="value num_box input_in"></span><span class="unit num_box">kg</span></div>
				      </div>
				      </td>
				      <td>
				       <div class="flex_box">
				        <div class="flex_box_ul">
				         <div>品名</div>
				         <div>数量</div>
				         <div>外观</div>
				        </div>
				        <div class="flex_box_content">
				         <span>质保期</span>
				        </div>
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
				      <td>
				        <div class="flex_box" style="flex-direction: row;">
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				    	  <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;"></span><span class="unit">kg</span></div></div>
				     
				    	</div>
				      </td>
				      <td>
				        <div class="flex_box">
					        <div class="date_box">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
					        <div class="date_box" style="border-bottom: 0px;">
					          <div class="date_content">
					           <span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>
					          </div>
					          <div class="icons_">~</div>
					        </div>
				        </div>
				      </td>
				      <td><div class="text_div"><span  class="sy input_in"></span><span class="unit">kg</span></div></td>
				    </tr>
			        <tr id="outinput_tr" style="height:100px;"> 
			         <td colspan="8">
			           <div class="outinput_tr">
			             <div>
			               <span>调配备料单确认者签字</span>
			             </div>
			             <div>
			               <span>品质备料单确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="<%=path%>/plugins/public_components/img/pointer.jpg"/></div>
			             <div>
			                <span>原料资材作业者签字</span>
			             </div>
			             <div>
			                <span>原料资材确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="<%=path%>/plugins/public_components/img/pointer.jpg"/></div>
			             <div>
			                <span>调配作業前作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業前确认者签字</span>
			             </div>
			             <div style="justify-content: center;align-items: center;"><img src="<%=path%>/plugins/public_components/img/pointer.jpg"/></div>
			             <div>
			                <span>调配作業後作业者签字</span>
			             </div>
			             <div>
			                <span>调配作業後确认者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="<%=path%>/plugins/public_components/img/pointer.jpg"/></div>
			             <div>
			                <span>原料资材受取者签字</span>
			             </div>
			              <div style="justify-content: center;align-items: center;"><img src="<%=path%>/plugins/public_components/img/pointer.jpg"/></div>
			             <div>
			                <span>调配作業後组长签字</span>
			             </div>
			           </div>
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


  $("body").off("click",".input_in").on("click",".input_in",(event)=>{
	  var value = $(event.target).html(), _event = event;
	  if(event.target.classList.value.indexOf("data_time") !=-1){
		  $(event.target).html("<input type='text'  class='data_time' value='"+value+"' />");
		  $(".data_time").datepicker({
			language: "zh-CN",
            autoclose: true,//选中之后自动隐藏日期选择框
            format: "yyyy-mm-dd"
		  }).on("changeDate", function(evt){
			  var value =  evt.date.format("yyyy-MM-dd");
			  $(_event.target).html(value);   
		  });
		  $(event.target).find("input").focus(); 
	  }else{
		  $(event.target).html("<input type='text'  value='"+value+"' />");
		  $(event.target).find("input").focus(); 
	  }
	
  });
  $("body").off("blur",".input_in").on(" blur",".input_in",(event)=>{
	      if( !$(event.target).hasClass("data_time") ){
	    	  var value = $(event.target).val();
			  $(event.target).parent().html(value);   
	      }

  });
/*   $("body").off("click",".del_btn").on(" click",".del_btn",(event)=>{
	  $(event.target).parents("tr").remove();
  }); */
/*   window.onbeforeprint = function(){
	 $("#add_data_tr").hide();
	 $(".field-button").hide();
	 $("#outinput_tr").show();
  };
  
  window.onafterprint = function(){
	$("#outinput_tr").hide();
	$(".field-button").show();
	$("#add_data_tr").show();
  } */
});
</script>