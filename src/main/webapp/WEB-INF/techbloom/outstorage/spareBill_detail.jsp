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
    <form  class="form-horizontal" id = "spareBill" action="" method="post" style="display:none;">
        <input type="hidden" id = "user_start_time" name = "userStartTime"/>
        <input type="hidden" id = "product_name" name = "productName"/>
        <input type="hidden" id = "total_product_amount" name = "totalProductAmount"/>
        <input type="hidden" id = "simplex_feed_amount" name = "simplexFeedAmount"/>
        <input type="hidden" id = "cylinder_feed_amount" name = "cylinderFeedAmount"/>
        <input type="hidden" id = "mix_use_line" name = "mixUseLine"/>
        <input type="hidden" id = "special_matter" name = "specialAatter"/>
    </form>

    <form  class="form-horizontal" id = "spareBillDetail1"  action="" method="post" style="display:none;">

    </form>
    <form  class="form-horizontal" id = "spareBillDetail2"  action="" method="post" style="display:none;">

    </form>
    <form  class="form-horizontal" id = "spareBillDetail3" action="" method="post" style="display:none;">

    </form>
    <form  class="form-horizontal" id = "spareBillDetail4"action="" method="post" style="display:none;">

    </form>



    <%--<form class="form-horizontal">--%>

        <!-- 表单1 -->
        <fieldset>
            <form class="form-horizontal" id = "spare" name="spare" action="" method="post" target="_ifr">
                <input type="hidden" id = "id" name ="id" value = ${spareBillManagerVO1.id}>
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
                                <div> <span class="input_in data_time" id = "user_start_time">${spareBillManagerVO1.userStartTime}</span></div>
                            </div>
                            <div class="flex_uls">
                                <div>
                                    <span>产品名</span>
                                    <div class="text_div input_in" id = "product_name" >${spareBillManagerVO1.productName}</div>
                                    <%--<div class="text_div input_in" id = ""></div>--%>
                                </div>
                                <div>
                                    <span>总生产数</span>
                                    <div class="flex_1">
                                        <span class="input_in" id = "total_product_amount" >${spareBillManagerVO1.totalProductAmount}</span>
                                        <span>箱</span>
                                    </div>
                                </div>
                                <div>
                                    <span>单缸投料数</span>
                                    <div class="flex_1">
                                        <span class="input_in" id = "simplex_feed_amount" >${spareBillManagerVO1.simplexFeedAmount}</span>
                                        <span>箱</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="th_2">
                            <span>杠投料</span>
                            <div class="flex_1">
                                <span class="input_in" id = "cylinder_feed_amount" >${spareBillManagerVO1.cylinderFeedAmount}</span>
                                <span>缸</span>
                            </div>
                        </div>
                        <div class="th_3">
                            <span>调配室使用线</span>
                            <div class="flex_1">
                                <span class="input_in" id = "mix_use_line">${spareBillManagerVO1.mixUseLine}</span>
                            </div>
                        </div>
                    </div>
                    <div class="title_s" >
                        特殊事项栏
                    </div>
                    <div>
                        <textarea id = "special_matter" name="special_matter">${spareBillManagerVO1.specialMatter}</textarea>
                    </div>
                </div>
            </div>
            </form>
            <!-- table -->
            <form class="form-horizontal" id = "spareDetail1">
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
                    <tr id = "spareDetail11">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[0].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[0].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[0].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[0].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[0].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[0].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[0].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[0].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[0].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[0].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[0].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[0].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[0].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[0].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[0].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[0].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[0].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                        <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[0].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail12">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[1].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[1].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[1].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[1].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[1].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[1].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[1].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[1].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[1].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[1].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[1].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[1].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[1].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[1].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[1].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[1].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[1].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[1].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail13">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[2].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[2].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[2].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[2].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[2].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[2].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[2].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[2].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[2].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[2].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[2].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[2].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[2].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[2].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[2].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[2].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[2].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[2].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail14">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[3].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[3].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[3].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[3].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[3].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[3].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[3].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[3].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[3].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[3].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[3].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[3].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[3].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[3].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[3].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[3].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[3].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[3].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail15">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[4].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[4].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[4].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[4].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[4].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[4].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[4].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[4].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[4].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[4].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[4].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[4].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[4].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[4].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[4].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[4].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[4].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[4].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail16">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList1[5].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList1[5].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList1[5].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList1[5].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList1[5].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList1[5].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[5].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[5].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList1[5].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList1[5].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList1[5].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList1[5].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList1[5].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList1[5].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList1[5].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList1[5].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList1[5].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList1[5].surplusAmount}</span><span class="unit">kg</span></div></td>
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
            </form>
        </fieldset>
    <%--</form>--%>
    <%--<form class="form-horizontal">--%>
        <!-- 表单2 -->
        <fieldset>
        <form class="form-horizontal" id = "spare2" name="spare" action="" method="post" target="_ifr">
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
                                <div> <span class="input_in data_time" id = "user_start_time">${spareBillManagerVO2.userStartTime}</span></div>
                            </div>
                            <div class="flex_uls">
                                <div>
                                    <span>产品名</span>
                                    <div class="text_div input_in" id = "product_name" >${spareBillManagerVO2.productName}</div>
                                    <%--<div class="text_div input_in" id = ""></div>--%>
                                </div>
                                <div>
                                    <span>总生产数</span>
                                    <div class="flex_1">
                                        <span class="input_in" id = "total_product_amount" >${spareBillManagerVO2.totalProductAmount}</span>
                                        <span>箱</span>
                                    </div>
                                </div>
                                <div>
                                    <span>单缸投料数</span>
                                    <div class="flex_1">
                                        <span class="input_in" id = "simplex_feed_amount" >${spareBillManagerVO2.simplexFeedAmount}</span>
                                        <span>箱</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="th_2">
                            <span>杠投料</span>
                            <div class="flex_1">
                                <span class="input_in" id = "cylinder_feed_amount" >${spareBillManagerVO2.cylinderFeedAmount}</span>
                                <span>缸</span>
                            </div>
                        </div>
                        <div class="th_3">
                            <span>调配室使用线</span>
                            <div class="flex_1">
                                <span class="input_in" id = "mix_use_line">${spareBillManagerVO2.mixUseLine}</span>
                            </div>
                        </div>
                    </div>
                    <div class="title_s" >
                        特殊事项栏
                    </div>
                    <div>
                        <textarea id = "special_matter" name="special_matter">${spareBillManagerVO2.specialMatter}</textarea>
                    </div>
                </div>
            </div>
        </form>
        <!-- table -->
        <form class="form-horizontal" id = "spareDetail2">
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
                    <tr id = "spareDetail21">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[0].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[0].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[0].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[0].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[0].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[0].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[0].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[0].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[0].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList2[0].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[0].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[0].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[0].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[0].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[0].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[0].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[0].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[0].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail22">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[1].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[1].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[1].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[1].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[1].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[1].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[1].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[1].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[1].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList2[1].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[1].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[1].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[1].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[1].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[1].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[1].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[1].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[1].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail23">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[2].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[2].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[2].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[2].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[2].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[2].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[2].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[2].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[2].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList2[2].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[2].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[2].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[2].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[2].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[2].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[2].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[2].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[2].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail24">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[3].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[3].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[3].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[3].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[3].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[3].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[3].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[3].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[3].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList2[3].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[3].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[3].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[3].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[3].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[3].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[3].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[3].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[3].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail25">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[4].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[4].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[4].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[4].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[4].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[4].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[4].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[4].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[4].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[4].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[4].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[4].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[4].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[4].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[4].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[4].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[4].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[4].surplusAmount}</span><span class="unit">kg</span></div></td>
                    </tr>
                    <tr id = "spareDetail26">
                        <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList2[5].id}">
                        <td><div class="text_div input_in" id ="material_code">${spareBillDetailList2[5].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList2[5].supplierCode}</div></td>
                        <td><div class="text_div input_in" id = "position_code">${spareBillDetailList2[5].positionCode}</div></td>
                        <td>
                            <div class="flex">
                                <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList2[5].userAmount}</span><span class="unit">kg</span></div>
                                <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList2[5].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList2[5].usedBox}</span><span class="unit num_box">箱</span></div>
                                <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList2[5].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList2[5].sendAmount}</span></div>
                                <div class="float_left">
                                    <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList2[5].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList2[5].queryBox}</span><span class="unit num_box">箱</span></div>
                                    <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList2[5].queryAdd}</span><span class="unit num_box">kg</span></div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="flex_box" style="flex-direction: row;">
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList2[5].theoryAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList2[5].realAmount}</span><span class="unit">kg</span></div></div>
                                <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList2[5].difference}</span><span class="unit">kg</span></div></div>

                            </div>
                        </td>
                        <td>
                            <div class="flex_box">
                                <div class="date_box">
                                    <div class="date_content">
                                        <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList2[5].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList2[5].day}</span><span>日</span>
                                    </div>
                                    <div class="icons_">~</div>
                                </div>
                                <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                <%--<div class="date_content">--%>
                                <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                <%--</div>--%>
                                <%--<div class="icons_">~</div>--%>
                                <%--</div>--%>
                            </div>
                        </td>
                        <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList2[5].surplusAmount}</span><span class="unit">kg</span></div></td>
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
        </form>
    </fieldset>

        <!-- 表单3 -->
        <fieldset>
            <form class="form-horizontal" id = "spare3" name="spare" action="" method="post" target="_ifr">
                <div style="padding: 10px 10px 0px;">
                    <div class="_title_">
                        <span>备料单<span class="radius_font">3</span></span>
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
                                    <div> <span class="input_in data_time" id = "user_start_time">${spareBillManagerVO3.userStartTime}</span></div>
                                </div>
                                <div class="flex_uls">
                                    <div>
                                        <span>产品名</span>
                                        <div class="text_div input_in" id = "product_name" >${spareBillManagerVO3.productName}</div>
                                        <%--<div class="text_div input_in" id = ""></div>--%>
                                    </div>
                                    <div>
                                        <span>总生产数</span>
                                        <div class="flex_1">
                                            <span class="input_in" id = "total_product_amount" >${spareBillManagerVO3.totalProductAmount}</span>
                                            <span>箱</span>
                                        </div>
                                    </div>
                                    <div>
                                        <span>单缸投料数</span>
                                        <div class="flex_1">
                                            <span class="input_in" id = "simplex_feed_amount" >${spareBillManagerVO3.simplexFeedAmount}</span>
                                            <span>箱</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="th_2">
                                <span>杠投料</span>
                                <div class="flex_1">
                                    <span class="input_in" id = "cylinder_feed_amount" >${spareBillManagerVO3.cylinderFeedAmount}</span>
                                    <span>缸</span>
                                </div>
                            </div>
                            <div class="th_3">
                                <span>调配室使用线</span>
                                <div class="flex_1">
                                    <span class="input_in" id = "mix_use_line">${spareBillManagerVO3.mixUseLine}</span>
                                </div>
                            </div>
                        </div>
                        <div class="title_s" >
                            特殊事项栏
                        </div>
                        <div>
                            <textarea id = "special_matter" name="special_matter">${spareBillManagerVO3.specialMatter}</textarea>
                        </div>
                    </div>
                </div>
            </form>
            <!-- table -->
            <form class="form-horizontal" id = "spareDetail3">
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
                        <tr id = "spareDetail31">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[0].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[0].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[0].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[0].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[0].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList3[0].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[0].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[0].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[0].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[0].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[0].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[0].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[0].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[0].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[0].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[0].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[0].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[0].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail32">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[1].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[1].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[1].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[1].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[1].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList3[1].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[1].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[1].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[1].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[1].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[1].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[1].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[1].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[1].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[1].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[1].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[1].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[1].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail33">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[2].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[2].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[2].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[2].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[2].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList3[2].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[2].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[2].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[2].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[2].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[2].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[2].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[2].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[2].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[2].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[2].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[2].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[2].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail34">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[3].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[3].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[3].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[3].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[3].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList3[3].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[3].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[3].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[3].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[3].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[3].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[3].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[3].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[3].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[3].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[3].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[3].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[3].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail35">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[4].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[4].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[4].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[4].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[4].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList3[4].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[4].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[4].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[4].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[4].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[4].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[4].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[4].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[4].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[4].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[4].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[4].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[4].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail36">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList3[5].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList3[5].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList3[5].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList3[5].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList3[5].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList5[5].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList3[5].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList3[5].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList3[5].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList3[5].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList3[5].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList3[5].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList3[5].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList3[5].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList3[5].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList3[5].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList3[5].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList3[5].surplusAmount}</span><span class="unit">kg</span></div></td>
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
            </form>
        </fieldset>
    <%--</form>--%>


        <!-- 表单4 -->
    <%--<form class="form-horizontal">--%>
        <fieldset>
            <form class="form-horizontal" id = "spare4" name="spare" action="" method="post" target="_ifr">
                <div style="padding: 10px 10px 0px;">
                    <div class="_title_">
                        <span>备料单<span class="radius_font">4</span></span>
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
                                    <div> <span class="input_in data_time" id = "user_start_time">${spareBillManagerVO4.userStartTime}</span></div>
                                </div>
                                <div class="flex_uls">
                                    <div>
                                        <span>产品名</span>
                                        <div class="text_div input_in" id = "product_name" >${spareBillManagerVO4.productName}</div>
                                        <%--<div class="text_div input_in" id = ""></div>--%>
                                    </div>
                                    <div>
                                        <span>总生产数</span>
                                        <div class="flex_1">
                                            <span class="input_in" id = "total_product_amount" >${spareBillManagerVO4.totalProductAmount}</span>
                                            <span>箱</span>
                                        </div>
                                    </div>
                                    <div>
                                        <span>单缸投料数</span>
                                        <div class="flex_1">
                                            <span class="input_in" id = "simplex_feed_amount" >${spareBillManagerVO4.simplexFeedAmount}</span>
                                            <span>箱</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="th_2">
                                <span>杠投料</span>
                                <div class="flex_1">
                                    <span class="input_in" id = "cylinder_feed_amount" >${spareBillManagerVO4.cylinderFeedAmount}</span>
                                    <span>缸</span>
                                </div>
                            </div>
                            <div class="th_3">
                                <span>调配室使用线</span>
                                <div class="flex_1">
                                    <span class="input_in" id = "mix_use_line">${spareBillManagerVO4.mixUseLine}</span>
                                </div>
                            </div>
                        </div>
                        <div class="title_s" >
                            特殊事项栏
                        </div>
                        <div>
                            <textarea id = "special_matter" name="special_matter">${spareBillManagerVO4.specialMatter}</textarea>
                        </div>
                    </div>
                </div>
            </form>
            <!-- table -->

            <!-- table -->
            <form class="form-horizontal" id = "spareDetail4">
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
                        <tr id = "spareDetail41">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[0].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[0].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[0].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[0].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[0].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[0].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList1[0].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList4[0].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[0].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[0].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[0].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[0].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[0].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[0].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[0].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[0].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[0].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[0].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail42">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[1].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[1].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[1].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[1].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[1].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[1].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList4[1].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList4[1].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[1].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[1].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[1].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[1].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[1].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[1].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[1].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[1].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[1].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[1].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail43">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[2].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[2].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[2].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[2].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[2].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[2].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList4[2].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList4[2].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[2].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[2].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[2].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[2].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[2].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[2].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[2].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[2].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[2].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[2].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail44">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[3].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[3].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[3].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[3].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[3].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[3].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList4[3].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList4[3].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[3].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[3].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[3].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[3].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[3].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[3].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[3].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[3].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[3].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[3].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail45">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[4].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[4].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[4].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[4].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[4].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[4].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList4[4].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList1[4].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[4].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[4].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[4].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[4].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[4].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[4].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[4].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[4].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[4].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[4].surplusAmount}</span><span class="unit">kg</span></div></td>
                        </tr>
                        <tr id = "spareDetail46">
                            <input type="hidden" id = "spareDetailId" name = "spareDetailId" value ="${spareBillDetailList4[5].id}">
                            <td><div class="text_div input_in" id ="material_code">${spareBillDetailList4[5].materialCode}</div><div class="text_div input_in" id = "supplier_code">${spareBillDetailList4[5].supplierCode}</div></td>
                            <td><div class="text_div input_in" id = "position_code">${spareBillDetailList4[5].positionCode}</div></td>
                            <td>
                                <div class="flex">
                                    <div class="user_num"><span class="number input_in" id = "user_amount">${spareBillDetailList4[5].userAmount}</span><span class="unit">kg</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" id = "used_weight">${spareBillDetailList4[5].usedWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "used_box">${spareBillDetailList4[5].usedBox}</span><span class="unit num_box">箱</span></div>
                                    <div style="display: flex;"><span class="num_box input_in" ></span><span class="num_box">+</span><span class="value num_box input_in" id = "add_amount">${spareBillDetailList4[5].addAmount}</span><span class="unit num_box">kg</span></div>
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
                                    <div class="cc_box" style="border-right: 1px solid #666; height: 100%;"><span class="number input_in" id = "send_amount" style="width: 100%;height: 20px;">${spareBillDetailList4[5].sendAmount}</span></div>
                                    <div class="float_left">
                                        <div class="flex_1"><span class="num_box input_in" id = "query_weight">${spareBillDetailList4[5].queryWeight}</span><span class="num_box unit">kg x</span><span class="value num_box input_in" id = "query_box">${spareBillDetailList4[5].queryBox}</span><span class="unit num_box">箱</span></div>
                                        <div class="flex_1"><span class="num_box"></span><span class="num_box">+</span><span class="value num_box input_in" id ="query_add">${spareBillDetailList4[5].queryAdd}</span><span class="unit num_box">kg</span></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="flex_box" style="flex-direction: row;">
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "theory_amount">${spareBillDetailList4[5].theoryAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;border-right: 1px solid #666;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "real_amount">${spareBillDetailList4[5].realAmount}</span><span class="unit">kg</span></div></div>
                                    <div style="flex: 1;"><div class="text_div" style="height: 100%;padding: 0px;"><span class="input_in" style=" width: 30px; height: 20px;" id = "difference">${spareBillDetailList4[5].difference}</span><span class="unit">kg</span></div></div>

                                </div>
                            </td>
                            <td>
                                <div class="flex_box">
                                    <div class="date_box">
                                        <div class="date_content">
                                            <span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "month">${spareBillDetailList4[5].month}</span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;" id = "day">${spareBillDetailList4[5].day}</span><span>日</span>
                                        </div>
                                        <div class="icons_">~</div>
                                    </div>
                                    <%--<div class="date_box" style="border-bottom: 0px;">--%>
                                    <%--<div class="date_content">--%>
                                    <%--<span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>月</span><span class="input_in" style="width: 30px;height: 20px; text-align: right;"></span><span>日</span>--%>
                                    <%--</div>--%>
                                    <%--<div class="icons_">~</div>--%>
                                    <%--</div>--%>
                                </div>
                            </td>
                            <td><div class="text_div"><span class="sy input_in" id = "surplus_amount">${spareBillDetailList4[5].surplusAmount}</span><span class="unit">kg</span></div></td>
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
            </form>
        </fieldset>
    <%--</form>--%>
</div>
<div class="field-button" style="text-align: center;b-top: 0px;margin: 15px auto;">
    <span class="savebtn btn btn-success" id="formSave">保存</span>
    <span class="btn btn-danger" id = "cancelButton">取消</span>
</div>
<script type="text/javascript">
    $(function(){

        $("#cancelButton").click(function(){
            //关闭页面
            layer.closeAll();
        })

        $("#formSave").click(function(){
            $("#spareBill #user_start_time").val($("#spare #user_start_time").text());
            $("#spareBill #product_name").val($("#spare #product_name").text());
            $("#spareBill #total_product_amount").val($("#spare #total_product_amount").text());
            $("#spareBill #simplex_feed_amount").val($("#spare #simplex_feed_amount").text());
            $("#spareBill #cylinder_feed_amount").val($("#spare #cylinder_feed_amount").text());
            $("#spareBill #mix_use_line").val($("#spare #mix_use_line").text());
            $("#spareBill #special_matter").val($("#spare #special_matter").val());
            //存放的表头信息
            var spareBill = $("#spareBill").serialize();
            var spareBillJson = [];
            var spareBillrow = {};
            var spareBillrow = {"user_start_time":$("#spare #user_start_time").text(),"product_name":$("#spare #product_name").text(),
            "total_product_amount":$("#spare #total_product_amount").text(),"simplex_feed_amount":$("#spare #simplex_feed_amount").text(),
            "cylinder_feed_amount":$("#spare #cylinder_feed_amount").text(),"mix_use_line":$("#spare #mix_use_line").text(),
            "special_matter":$("#spare #special_matter").val(),"id":$("#spare #id").val()};
            spareBillJson.push(spareBillrow);
            var spareBillJsonString = JSON.stringify(spareBillJson);
            debugger;
            var json = [];
            for(var i=0;i<4;i++){
                for(var j=0;j<6;j++){
                    var c = i+1;
                    var d = j+1;
                    var a = "#"+"spareDetail"+c;
                    var b = a+d;

                    //存放详情信息，分为分为详情1，详情2，详情3，详情4等等
                    var aa = $(""+a+" "+b+" #material_code").text();
                    var bb = $(""+a+" "+b+" #position_code").text();
                    var cc = $(""+a+" "+b+" #supplier_code").text();
                    var dd = $(""+a+" "+b+" #user_amount").text();
                    var ee = $(""+a+" "+b+" #used_amount").text();
                    var ff = $(""+a+" "+b+" #query_weight").text();
                    var gg = $(""+a+" "+b+" #query_box").text();
                    var hh = $(""+a+" "+b+" #query_add").text();
                    var ii = $(""+a+" "+b+" #used_box").text();
                    var jj = $(""+a+" "+b+" #used_weight").text();
                    var kk = $(""+a+" "+b+" #add_amount").text();
                    var ll = $(""+a+" "+b+" #send_amount").text();
                    var mm = $(""+a+" "+b+" #theory_amount").text();
                    var nn = $(""+a+" "+b+" #real_amount").text();
                    var oo = $(""+a+" "+b+" #difference").text();
                    var pp = $(""+a+" "+b+" #month").text();
                    var qq = $(""+a+" "+b+" #day").text();
                    var rr = $(""+a+" "+b+" #surplus_amount").text();
                    var ss = $(""+a+" "+b+" #spareDetailId").val();
                    var rows = {"material_code":aa,"position_code":bb,"supplier_code":cc,"user_amount":dd,"used_amount":ee,"query_weight":ff,
                    "query_box":gg,"query_add":hh,"used_box":ii,"used_weight":jj,"add_amount":kk,"send_amount":ll,"theory_amount":mm,"real_amount":nn,"difference":oo,
                    "month":pp,"day":qq,"surplus_amount":rr,"order":c,"id":ss};
                    json.push(rows);
                }
            }
            var jsonString = JSON.stringify(json);
            //将数值插入到数据库中
            $.ajax({
                type: "POST",
                url: context_path + "/spareBillManager/saveSpareBillDetail",
                dataType: "json",
                data:{spareBillJsonString:spareBillJsonString,json:jsonString},
                success:function(data){
                    if(data.result){
                        //刷新列表
                        gridReload();
                        layer.closeAll();
                        layer.alert(data.msg);
                    }else{
                        //刷新列表
                        layer.alert(data.msg);
                    }
                }
            })
        })

        function gridReload(){
            $("#grid-table").jqGrid('setGridParam',
                {
                    url : context_path + '/spareBillManager/getList',
                    postData: {queryJsonString:""} //发送数据  :选中的节点
                }
            ).trigger("reloadGrid");
        }

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