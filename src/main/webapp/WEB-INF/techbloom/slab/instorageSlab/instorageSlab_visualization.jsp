<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    var context_path = '<%=path%>';
</script>
<style>
    .p-absolute-auto {
        position: unset !important;
        top: 0;
        bottom: 0;
        right: 0;
        left: 0;
    }
</style>
<link rel="stylesheet" href="<%=path%>/static/techbloom/system/scene/css/index.css"/>
</head>
<div class="card-body" style="height: calc(100% - 40px);">
    <div class="card-title">
        <div class="flex-row justify-content-between">
            <div class="flex-row">
                <ul class="form-elements">
                    <li class="field-group">
                        <select name="area" id="tabs" style="margin-bottom:0;width:100px;">
                            <%--<option value="0">所有库区</option>--%>
                            <option value="2">原料库A</option>
                            <option value="9">原料库B</option>
                            <option value="11">原料库C</option>
                        </select>
                    </li>
                </ul>
                <input id="forkliftTag" type="hidden" value="${forkliftTag}"/>
            </div>
            <div class="visual_title_second flex-row align-items-center">
                <table class="table">
                    <tr>
                        <td>上架单号</td>
                        <td id="putBillCodeTd"></td>
                        <td>操作人</td>
                        <td id="operatorNameTd"></td>
                    </tr>
                </table>
            </div>
            <div class="flex-row align-items-center ">
                <img src="<%=path%>/static/techbloom/system/scene/img/truck1.png" alt=""
                     style="margin-right:1rem;height: 2rem;"/>
                <img src="<%=path%>/static/techbloom/system/scene/img/truck2.png" alt="" style="height:2rem;"/>
            </div>
            <div class="flex-row justify-content-between ">
                <div class="flex-row align-items-center">
                    <span class="visual_legend legend1"></span>
                    <span class="ml-1 mr-3">库位</span>
                </div>
                <div class="flex-row align-items-center">
                    <span class="visual_legend legend2"></span>
                    <span class="ml-1 mr-3">推荐库位</span>
                </div>
                <div class="flex-row align-items-center">
                    <span class="visual_legend legend3"></span>
                    <span class="ml-1 mr-3">道路</span>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-content" style="height: calc(100% - 80px);">
        <div id="content1" class="p-relative h-100 w-100 rotate rotate270deg">
            <div class="p-absolute-auto a-right">
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-01 materielBySorts">
                    <span class="rotate180deg">A01</span>
                    <span class="drawer rotate180deg" id="A01"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-02 materielBySorts">
                    <span class="rotate180deg">A02</span>
                    <span class="drawer rotate180deg" id="A02"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-03 materielBySorts">
                    <span class="rotate180deg">A03</span>
                    <span class="drawer rotate180deg" id="A03"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-04 materielBySorts">
                    <span class="rotate180deg">A04</span>
                    <span class="drawer rotate180deg" id="A04"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-05 materielBySorts">
                    <span class="rotate180deg">A05</span>
                    <span class="drawer rotate180deg" id="A05"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-06 materielBySorts">
                    <span class="rotate180deg">A06</span>
                    <span class="drawer rotate180deg" id="A06"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-07 materielBySorts">
                    <span class="rotate180deg">A07</span>
                    <span class="drawer rotate180deg" id="A07"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-08 materielBySorts">
                    <span class="rotate180deg">A08</span>
                    <span class="drawer rotate180deg" id="A08"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-09 materielBySorts">
                    <span class="rotate180deg">A09</span>
                    <span class="drawer rotate180deg" id="A09"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-10 materielBySorts">
                    <span class="rotate180deg">A10</span>
                    <span class="drawer rotate180deg" id="A10"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-11 materielBySorts">
                    <span class="rotate180deg">A11</span>
                    <span class="drawer rotate180deg" id="A11"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-12 materielBySorts">
                    <span class="rotate180deg">A12</span>
                    <span class="drawer rotate180deg" id="A-12"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-13 materielBySorts">
                    <span class="rotate180deg">A13</span>
                    <span class="drawer rotate180deg" id="A13"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-14 materielBySorts">
                    <span class="rotate180deg">A14</span>
                    <span class="drawer rotate180deg" id="A14"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-15 materielBySorts">
                    <span class="rotate180deg">A15</span>
                    <span class="drawer rotate180deg" id="A15"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-16 materielBySorts">
                    <span class="rotate180deg">A16</span>
                    <span class="drawer rotate180deg" id="A16"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-17 materielBySorts">
                    <span class="rotate180deg">A17</span>
                    <span class="drawer rotate180deg" id="A17"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-18 materielBySorts">
                    <span class="rotate180deg">A18</span>
                    <span class="drawer rotate180deg" id="A18"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-19 materielBySorts">
                    <span class="rotate180deg">A19</span>
                    <span class="drawer rotate180deg" id="A19"></span>
                </div>
                <div class="border_visual_row p-absolute a_01-20 flex-row align-items-center justify-content-between a-20 materielBySorts">
                    <span class="rotate180deg">A20</span>
                    <span class="drawer rotate180deg" id="A20"></span>
                </div>
            </div>
            <div class="p-absolute-auto a-middle">
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-21 materielBySorts">
                    <span class="rotate180deg">A21</span>
                    <span class="drawer rotate180deg" id="A21"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-22 materielBySorts">
                    <span class="rotate180deg">A22</span>
                    <span class="drawer rotate180deg" id="A22"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-23 materielBySorts">
                    <span class="rotate180deg">A23</span>
                    <span class="drawer rotate180deg" id="A23"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-24 materielBySorts">
                    <span class="rotate180deg">A24</span>
                    <span class="drawer rotate180deg" id="A24"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-25 materielBySorts">
                    <span class="rotate180deg">A25</span>
                    <span class="drawer rotate180deg" id="A25"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-26 materielBySorts">
                    <span class="rotate180deg">A26</span>
                    <span class="drawer rotate180deg" id="A26"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-27 materielBySorts">
                    <span class="rotate180deg">A27</span>
                    <span class="drawer rotate180deg" id="A27"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-28 materielBySorts">
                    <span class="rotate180deg">A28</span>
                    <span class="drawer rotate180deg" id="A28"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-29 materielBySorts">
                    <span class="rotate180deg">A29</span>
                    <span class="drawer rotate180deg" id="A29"></span>
                </div>
                <div class="border_visual_row p-absolute a_21-30 flex-row align-items-center justify-content-between a-30 materielBySorts">
                    <span class="rotate180deg">A30</span>
                    <span class="drawer rotate180deg" id="A30"></span>
                </div>
            </div>
            <div class="p-absolute-auto a-left">
                <div class="border_visual_column p-absolute a_33-37 flex-row align-items-center justify-content-between a-33 materielBySorts">
                    <span class="">A33</span>
                    <span class="drawer" id="A33"></span>
                </div>
                <div class="border_visual_column p-absolute a_33-37 flex-row align-items-center justify-content-between a-34 materielBySorts">
                    <span class="">A34</span>
                    <span class="drawer" id="A34"></span>
                </div>
                <div class="border_visual_column p-absolute a_33-37 flex-row align-items-center justify-content-between a-35 materielBySorts">
                    <span class="">A35</span>
                    <span class="drawer" id="A35"></span>
                </div>
                <div class="border_visual_column p-absolute a_33-37 flex-row align-items-center justify-content-between a-36 materielBySorts">
                    <span class="">A36</span>
                    <span class="drawer" id="A36"></span>
                </div>
                <div class="border_visual_column p-absolute a_33-37 flex-row align-items-center justify-content-between a-37 materielBySorts">
                    <span class="">A37</span>
                    <span class="drawer" id="A37"></span>
                </div>
                <div class="border_visual_column p-absolute a_44-48 flex-row align-items-center justify-content-between a-44 materielBySorts">
                    <span class="">A44</span>
                    <span class="drawer" id="A44"></span>
                </div>
                <div class="border_visual_column p-absolute a_44-48 flex-row align-items-center justify-content-between a-45 materielBySorts">
                    <span class="">A45</span>
                    <span class="drawer" id="A45"></span>
                </div>
                <div class="border_visual_column p-absolute a_44-48 flex-row align-items-center justify-content-between a-46 materielBySorts">
                    <span class="">A46</span>
                    <span class="drawer" id="A46"></span>
                </div>
                <div class="border_visual_column p-absolute a_44-48 flex-row align-items-center justify-content-between a-47 materielBySorts">
                    <span class="">A47</span>
                    <span class="drawer" id="A47"></span>
                </div>
                <div class="border_visual_column p-absolute a_44-48 flex-row align-items-center justify-content-between a-48 materielBySorts">
                    <span class="">A48</span>
                    <span class="drawer" id="A48"></span>
                </div>
            </div>
            <div class="p-absolute-auto a-down">
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-53 materielBySorts">
                    <span class="">A53</span>
                    <span class="drawer" id="A53"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-54 materielBySorts">
                    <span class="">A54</span>
                    <span class="drawer" id="A54"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-55 materielBySorts">
                    <span class="">A55</span>
                    <span class="drawer" id="A55"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-56 materielBySorts">
                    <span class="">A56</span>
                    <span class="drawer" id="A56"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-57 materielBySorts">
                    <span class="">A57</span>
                    <span class="drawer" id="A57"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-58 materielBySorts">
                    <span class="">A58</span>
                    <span class="drawer" id="A58"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-59 materielBySorts">
                    <span class="">A59</span>
                    <span class="drawer" id="A59"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-60 materielBySorts">
                    <span class="">A60</span>
                    <span class="drawer" id="A60"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-61 materielBySorts">
                    <span class="">A61</span>
                    <span class="drawer" id="A61"></span>
                </div>
                <div class="border_visual_column p-absolute a_53-62 flex-row align-items-center justify-content-between a-62 materielBySorts">
                    <span class="">A62</span>
                    <span class="drawer" id="A62"></span>
                </div>
                <div class="border_visual_row p-absolute a_63-66 flex-row align-items-center justify-content-between a-63
                    materielBySorts">
                    <span class="rotate180deg">A63</span>
                    <span class="drawer rotate180deg" id="A63"></span>
                </div>

                <div class="border_visual_row p-absolute a_63-66 flex-row align-items-center justify-content-between a-64"
                     materielBySorts>
                    <span class="rotate180deg">A64</span>
                    <span class="drawer rotate180deg" id="A64"></span>
                </div>
                <div class="border_visual_row p-absolute a_63-66 flex-row align-items-center justify-content-between a-65 materielBySorts">
                    <span class="rotate180deg">A65</span>
                    <span class="drawer rotate180deg" id="A65"></span>
                </div>
                <div class="border_visual_row p-absolute a_63-66 flex-row align-items-center justify-content-between a-66 materielBySorts">
                    <span class="rotate180deg">A66</span>
                    <span class="drawer rotate180deg" id="A66"></span>
                </div>
            </div>
            <div class="p-absolute-auto a-door">
                <div class="door_visual p-absolute a_door1"><span class="rotate90deg">书库</span></div>
                <div class="door_visual p-absolute a_door2"><span class="rotate90deg">原料仓库</span></div>
                <div class="door_visual p-absolute a_door3"><span class="rotate90deg">前室</span></div>
            </div>
            <div class="p-absolute-auto c-road">
                <div class="border_visual_row p-absolute flex-row flex-wrap road_visual_column a_road1">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
                <div class="border_visual_row p-absolute flex-row road_visual_row a_road2">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
            </div>
        </div>
        <div id="content2" class="p-relative h-100 w-100 rotate rotate270deg">
            <div class="p-absolute-auto b-right">
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-01 materielBySorts">
                    <span class="rotate180deg">B01</span>
                    <span class="drawer rotate180deg" id="B01"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-02 materielBySorts">
                    <span class="rotate180deg">B02</span>
                    <span class="drawer rotate180deg" id="B02"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-03 materielBySorts">
                    <span class="rotate180deg">B03</span>
                    <span class="drawer rotate180deg" id="B03"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-04 materielBySorts">
                    <span class="rotate180deg">B04</span>
                    <span class="drawer rotate180deg" id="B04"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-05 materielBySorts">
                    <span class="rotate180deg">B05</span>
                    <span class="drawer rotate180deg" id="B05"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-06 materielBySorts">
                    <span class="rotate180deg">B06</span>
                    <span class="drawer rotate180deg" id="B06"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-07 materielBySorts">
                    <span class="rotate180deg">B07</span>
                    <span class="drawer rotate180deg" id="B07"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-08 materielBySorts">
                    <span class="rotate180deg">B08</span>
                    <span class="drawer rotate180deg" id="B08"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-09 materielBySorts">
                    <span class="rotate180deg">B09</span>
                    <span class="drawer rotate180deg" id="B09"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-10 materielBySorts">
                    <span class="rotate180deg">B10</span>
                    <span class="drawer rotate180deg" id="B10"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-11 materielBySorts">
                    <span class="rotate180deg">B11</span>
                    <span class="drawer rotate180deg" id="B11"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-12 materielBySorts">
                    <span class="rotate180deg">B12</span>
                    <span class="drawer rotate180deg" id="B12"></span>
                </div>
                <div class="border_visual_row p-absolute b_01-12 flex-row align-items-center justify-content-between b-13 materielBySorts">
                    <span class="rotate180deg">B13</span>
                    <span class="drawer rotate180deg" id="B13"></span>
                </div>
            </div>
            <div class="p-absolute-auto b-road">
                <div class="border_visual_row p-absolute flex-row flex-wrap road_visual_column b_road1">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
                <div class="border_visual_row p-absolute flex-row road_visual_row b_road2">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
            </div>
            <div class="p-absolute-auto b-left">
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-23 materielBySorts">
                    <span class="rotate180deg">B23</span>
                    <span class="drawer rotate180deg" id="B23"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-24 materielBySorts">
                    <span class="rotate180deg">B24</span>
                    <span class="drawer rotate180deg" id="B24"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-25 materielBySorts">
                    <span class="rotate180deg">B25</span>
                    <span class="drawer rotate180deg" id="B25"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-26 materielBySorts">
                    <span class="rotate180deg">B26</span>
                    <span class="drawer rotate180deg" id="B26"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-27 materielBySorts">
                    <span class="rotate180deg">B27</span>
                    <span class="drawer rotate180deg" id="B27"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-28 materielBySorts">
                    <span class="rotate180deg">B28</span>
                    <span class="drawer rotate180deg" id="B28"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-29 materielBySorts">
                    <span class="rotate180deg">B29</span>
                    <span class="drawer rotate180deg" id="B29"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-30 materielBySorts">
                    <span class="rotate180deg">B30</span>
                    <span class="drawer rotate180deg" id="B30"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-31 materielBySorts">
                    <span class="rotate180deg">B31</span>
                    <span class="drawer rotate180deg" id="B31"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-32 materielBySorts">
                    <span class="rotate180deg">B32</span>
                    <span class="drawer rotate180deg" id="B32"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-33 materielBySorts">
                    <span class="rotate180deg">B33</span>
                    <span class="drawer rotate180deg" id="B33"></span>
                </div>
                <div class="border_visual_row p-absolute b_23-34 flex-row align-items-center justify-content-between b-34 materielBySorts">
                    <span class="rotate180deg">B34</span>
                    <span class="drawer rotate180deg" id="B34"></span>
                </div>
                <div class="border_visual_row p-absolute b_35-38 flex-row align-items-center justify-content-between b-35 materielBySorts">
                    <span class="rotate180deg">B35</span>
                    <span class="drawer rotate180deg" id="B35"></span>
                </div>
                <div class="border_visual_row p-absolute b_35-38 flex-row align-items-center justify-content-between b-36 materielBySorts">
                    <span class="rotate180deg">B36</span>
                    <span class="drawer rotate180deg" id="B36"></span>
                </div>
                <div class="border_visual_row p-absolute b_35-38 flex-row align-items-center justify-content-between b-37 materielBySorts">
                    <span class="rotate180deg">B37</span>
                    <span class="drawer rotate180deg" id="B37"></span>
                </div>
                <div class="border_visual_row p-absolute b_35-38 flex-row align-items-center justify-content-between b-38 materielBySorts">
                    <span class="rotate180deg">B38</span>
                    <span class="drawer rotate180deg" id="B38"></span>
                </div>
            </div>
            <div class="p-absolute-auto b-down">
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-14 materielBySorts">
                    <span>B14</span>
                    <span class="drawer" id="B14"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-15 materielBySorts">
                    <span>B15</span>
                    <span class="drawer" id="B15"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-16 materielBySorts">
                    <span>B16</span>
                    <span class="drawer" id="B16"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-17 materielBySorts">
                    <span>B17</span>
                    <span class="drawer" id="B17"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-18 materielBySorts">
                    <span>B18</span>
                    <span class="drawer" id="B18"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-19 materielBySorts">
                    <span>B19</span>
                    <span class="drawer" id="B19"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-20 materielBySorts">
                    <span>B20</span>
                    <span class="drawer" id="B20"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-21 materielBySorts">
                    <span>B21</span>
                    <span class="drawer" id="B21"></span>
                </div>
                <div class="border_visual_column p-absolute b_14-22 flex-row align-items-center justify-content-between b-22 materielBySorts">
                    <span>B22</span>
                    <span class="drawer" id="B22"></span>
                </div>
            </div>
            <div class="p-absolute-auto b-door">
                <div class="door_visual p-absolute b_door1"><span class="rotate90deg">前室</span></div>
                <div class="door_visual p-absolute b_door2"><span class="rotate90deg">广场</span></div>
            </div>
        </div>
        <div id="content3" class="p-relative h-100 w-100">
            <div class="p-absolute-auto c-up">
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-01 materielBySorts">
                    <span class="">C01</span>
                    <span class="drawer" id="C01"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-02 materielBySorts">
                    <span class="">C02</span>
                    <span class="drawer" id="C02"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-03 materielBySorts">
                    <span class="">C03</span>
                    <span class="drawer" id="C03"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-04 materielBySorts">
                    <span class="">C04</span>
                    <span class="drawer" id="C04"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-05 materielBySorts">
                    <span class="">C05</span>
                    <span class="drawer" id="C05"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-06 materielBySorts">
                    <span class="">C06</span>
                    <span class="drawer" id="C06"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-07 materielBySorts">
                    <span class="">C07</span>
                    <span class="drawer" id="C07"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-08 materielBySorts">
                    <span class="">C08</span>
                    <span class="drawer" id="C08"></span>
                </div>
                <div class="border_visual_column p-absolute c_01-10 flex-row align-items-center justify-content-between c-09 materielBySorts">
                    <span class="">C09</span>
                    <span class="drawer" id="C09"></span>
                </div>
                <div class="border_visual_row p-absolute c_01-10 flex-row align-items-center justify-content-between c-10 materielBySorts">
                    <span class="">C10</span>
                    <span class="drawer" id="C10"></span>
                </div>
            </div>
            <div class="p-absolute-auto c-left">
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-11 materielBySorts">
                    <span class="">C11</span>
                    <span class="drawer" id="C11"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-12 materielBySorts">
                    <span class="">C12</span>
                    <span class="drawer" id="C12"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-13 materielBySorts">
                    <span class="">C13</span>
                    <span class="drawer" id="C13"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-14 materielBySorts">
                    <span class="">C14</span>
                    <span class="drawer" id="C14"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-15 materielBySorts">
                    <span class="">C15</span>
                    <span class="drawer" id="C15"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-16 materielBySorts">
                    <span class="">C16</span>
                    <span class="drawer" id="C16"></span>
                </div>
                <div class="border_visual_row p-absolute c_11-17 flex-row align-items-center justify-content-between c-17 materielBySorts">
                    <span class="">C17</span>
                    <span class="drawer" id="C17"></span>
                </div>
            </div>
            <div class="p-absolute-auto c-right">
                <div class="border_visual_column p-absolute c_18-21 flex-row align-items-center justify-content-between c-18 materielBySorts">
                    <span class="">C18</span>
                    <span class="drawer" id="C18"></span>
                </div>
                <div class="border_visual_column p-absolute c_18-21 flex-row align-items-center justify-content-between c-19 materielBySorts">
                    <span class="">C19</span>
                    <span class="drawer" id="C19"></span>
                </div>
                <div class="border_visual_column p-absolute c_18-21 flex-row align-items-center justify-content-between c-20 materielBySorts">
                    <span class="">C20</span>
                    <span class="drawer" id="C20"></span>
                </div>
                <div class="border_visual_column p-absolute c_18-21 flex-row align-items-center justify-content-between c-21 materielBySorts">
                    <span class="">C21</span>
                    <span class="drawer" id="C21"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-22 materielBySorts">
                    <span class="">C22</span>
                    <span class="drawer" id="C22"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-23 materielBySorts">
                    <span class="">C23</span>
                    <span class="drawer" id="C23"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-24 materielBySorts">
                    <span class="">C24</span>
                    <span class="drawer" id="C24"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-25 materielBySorts">
                    <span class="">C25</span>
                    <span class="drawer" id="C25"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-26 materielBySorts">
                    <span class="">C26</span>
                    <span class="drawer" id="C26"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-27 materielBySorts">
                    <span class="">C27</span>
                    <span class="drawer" id="C27"></span>
                </div>
                <div class="border_visual_column p-absolute c_22-28 flex-row align-items-center justify-content-between c-28 materielBySorts">
                    <span class="">C28</span>
                    <span class="drawer" id="C28"></span>
                </div>
            </div>
            <div class="p-absolute-auto c-door">
                <div class="door_visual p-absolute c_door1"><span class="">冷藏库</span></div>
                <div class="door_visual p-absolute c_door2"><span class="">前室</span></div>
                <div class="door_visual p-absolute c_door3"><span class="" style="font-size: 50px;">计量台</span></div>
            </div>
            <div class="p-absolute-auto c-road">
                <div class="border_visual_row p-absolute flex-row road_visual_row c_road1">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
                <div class="border_visual_row p-absolute flex-row flex-wrap road_visual_column c_road2">
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                    <span><i></i></span>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    // $(".rotate").css({
    //     "width": document.body.clientHeight - 188 - 40,
    //     "height": document.body.clientWidth - 188 - 40,
    //     "position": "absolute",
    //     "top": ((document.body.clientHeight - document.body.clientWidth) / 2),
    //     "left": -((document.body.clientHeight - document.body.clientWidth) / 2)
    // });

    if (window.screen.width > 1024) {
        $(".rotate").css({
            "width": document.body.clientHeight - 188 - 40,
            "height": document.body.clientWidth - 188 - 40,
            "position": "absolute",
            "top": ((document.body.clientHeight - document.body.clientWidth) / 2),
            "left": -((document.body.clientHeight - document.body.clientWidth) / 2)
        });
    } else {
        $(".rotate").css({
            "width": document.body.clientHeight + 40,
            "height": document.body.clientWidth + 40,
            "position": "absolute",
            "top": ((document.body.clientHeight - document.body.clientWidth) / 2),
            "left": -((document.body.clientHeight - document.body.clientWidth) / 2)
        });
    }
</script>

<script>

    $(".materielBySorts").on("click", function () {
        var positionCode = $(this).children("span").eq(0).text();
        $.post(context_path + "/visual/toDetail?positionCode=" + positionCode, {}, function (str) {
            $queryWindow = layer.open({
                title: "物料查看",
                type: 1,
                skin: "layui-layer-molv",
                area: ["850px", "600px"],
                shade: 0.6,
                moveType: 1,
                content: str,
                success: function (layero, index) {
                    layer.closeAll("loading");
                }
            });
        }).error(function () {
            layer.closeAll();
            layer.msg("加载失败！", {icon: 2});
        });
    })


    $(function () {
        var putBillCode = "${putBillCode}";
        var operatorName = "${operatorName}";
        $("#putBillCodeTd").html(putBillCode);
        $("#operatorNameTd").html(operatorName);

        classContro();
        positionArea();
        getCarPosition();
        var intervalId = window.setInterval(function (args) {
            var areaid = $("#tabs").val();
            if (areaid != "" && areaid != null) {
                getCarPosition();
            }
        }, 800);

    });

    /**
     * 样式控制
     */
    function classContro() {
        var li = $('#tabs');
        var cc = $('#tabs option');
        var len = cc.length;
        li.change(function () {
            var t = parseInt(li.get(0).selectedIndex) + 1;
            for (var i = 1; i < len + 1; i++) {
                if (t == i) {
                    //alert(i);
                    $('#content' + t).show();
                } else {
                    $('#content' + i).hide();
                }
            }
        });

    }

    function positionArea() {
        $.ajax({
            url: context_path + "/visual/getVisual?areaType=" + $("#tabs").val(),
            type: "post",
            dataType: "JSON",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    //$("#" + data[i].positionName).html(data[i].posOccupy + '<img src="<%=path%>/static/techbloom/system/scene/img/drawer.png"/>');
                    $("#" + data[i].positionName).html(
                        '<div class="progress-list">' +
                        '<div class="progress right">' +
                        '<span>' + data[i].posOccupy +
                        '</span>' +
                        '<div class="progress-bar">' +

                        '</div>' +
                        '</div>' +
                        '</div>');
                    // debugger
                    var numPer = data[i].percentage + "%";
                    $(".border_visual_column" + " #" + data[i].positionName + " .progress-bar").css('height', numPer);
                    $(".border_visual_row" + " #" + data[i].positionName + " .progress-bar").css('width', numPer);
                }
                if (data != null) {
                    //刷新收货单列表
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {queryJsonString: ""} //发送数据
                    }).trigger("reloadGrid");
                    layer.closeAll();

                    // clearTimeout(timeId);
                    // layer.msg(data.msg, {icon: 1, time: 1200})
                } else {
                    layer.msg("获取失败", {icon: 2, time: 1200})
                }
            }
        });
    }

    $("#tabs").on("change", function (e) {
        $.ajax({
            url: context_path + "/visual/getVisual?areaType=" + $("#tabs").val(),
            type: "post",
            dataType: "JSON",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    $("#" + data[i].positionName).html(
                        '<div class="progress-list">' +
                        '<div class="progress right">' +
                        '<span>' + data[i].posOccupy +
                        '</span>' +
                        '<div class="progress-bar">' +

                        '</div>' +
                        '</div>' +
                        '</div>');
                    // debugger
                    var numPer = data[i].percentage + "%";
                    $(".border_visual_column" + " #" + data[i].positionName + " .progress-bar").css('height', numPer);
                    $(".border_visual_row" + " #" + data[i].positionName + " .progress-bar").css('width', numPer);
                }
                getCarPosition();
                if (data != null) {
                    //刷新收货单列表
                    $("#grid-table").jqGrid("setGridParam", {
                        postData: {queryJsonString: ""} //发送数据
                    }).trigger("reloadGrid");
                    // layer.msg(data.msg, {icon: 1, time: 1200})
                } else {
                    window.clearInterval(interval);
                    layer.msg("获取失败", {icon: 2, time: 1200})
                }
            }
        });
    });


    function getCarPosition() {
        $.ajax({
            url: context_path + "/instorageSlab/getCarPosition",
            data: {
                areaId: $("#tabs").val(),
                forkliftTag: $("#forkliftTag").val()
            },
            type: "post",
            dataType: "JSON",
            success: function (data) {
                $(".truck").remove();
                if (data != null) {
                    for (var i = 0; i < data.length; i++) {
                        var order = i + 1;
                        truckPosition(data[i].xsize, data[i].ysize, data[i].sceneCode, data[i].forkliftName, order);
                    }
                } else {
                    /*window.clearInterval(interval);
                    layer.msg("获取失败", {icon: 2, time: 1200})*/
                }
            }

        });
    }

    function truckPosition(x, y, area, carName, order) {
        //小车的图标
        var img2 = '<div class="truck truck_' + area + '_' + order + '" ><span>' + carName + '</span><img src="<%=path%>/static/techbloom/system/scene/img/truck2.png" alt=""/>';

        //判断库区
        if (area == "KQ0000001") {
            $("#content1").append(img2);
            totalX = 48.812;
            totalY = 48.812;
        } else if (area == "KQ0000002") {
            $("#content2").append(img2);
            totalX = 29.692;
            totalY = 44.518;
        } else if (area == "KQ0000003") {
            $("#content3").append(img2);
            totalX = 29.66;
            totalY = 25.7;
        } else {
            alert("没有该" + area + "库区");
        }
        //小车定位
        $(".truck_" + area + "_" + order).css({
            "top": Number(((totalY - y) / totalY) * 100).toFixed(2) + "%",
            "left": Number((x / totalX) * 100).toFixed(2) + "%",
        });
    }

    //开启定时器1(变量定义在菜单初始化页面)
    interval1 = window.setInterval(pollingBackground, 2000);
    interval2 = window.setInterval(pollingBackground2, 2000);

    var $queryWindow123;

    //轮询后台，从数据库表中获取叉车执行状态
    function pollingBackground() {
        $.ajax({
            type: "POST",
            url: context_path + "/instorageSlab/getSlabExecuteState",
            dataType: 'json',
            cache: false,
            success: function (data) {
                if (data.result) {
                    if (data.code == "0") {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    } else if (data.code == "1") {
                        //关闭定时器1
                        window.clearInterval(interval1);
                        $.post(context_path + "/instorageSlab/toSureRfid", {}, function (str) {
                            $queryWindow123 = layer.open({
                                title: "托盘物料确认",
                                type: 1,
                                skin: "layui-layer-molv",
                                area: ["500px", "300px"],
                                shade: 0.6, //遮罩透明度
                                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                                content: str,//注意，如果str是object，那么需要字符拼接。
                                success: function (layero, index) {
                                    layer.closeAll("loading");
                                }
                            });
                        }).error(function () {
                            layer.closeAll();
                            layer.msg("加载失败！", {icon: 2});
                        });
                    }
                } else {
                    layer.msg(data.msg, {icon: 2, time: 2000});
                    //关闭定时器1
                    window.clearInterval(interval1);
                }
            }
        });
    }

    function pollingBackground2() {
        $.ajax({
            type: "POST",
            url: context_path + "/instorageSlab/getSlabExecuteState",
            dataType: 'json',
            cache: false,
            success: function (data) {
                if (data.result) {
                    if (data.code == "0") {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    } else if (data.code == "2") {
                        window.clearInterval(interval2);
                        var areaId = $("#tabs").val();
                        $.post(context_path + "/instorageSlab/toSlabPosition", {area: areaId}, function (str) {
                            var $queryWindow2 = layer.open({
                                title: "库位选择",
                                type: 1,
                                skin: "layui-layer-molv",
                                area: ["350px", "150px"],
                                shade: 0.6, //遮罩透明度
                                moveType: 1, //拖拽风格，0是默认，1是传统拖动
                                content: str,//注意，如果str是object，那么需要字符拼接。
                                success: function (layero, index) {
                                    layer.closeAll("loading");
                                }
                            });
                        }).error(function () {
                            layer.closeAll();
                            layer.msg("加载失败！", {icon: 2});
                        });

                    }
                } else {
                    layer.msg(data.msg, {icon: 2, time: 2000});
                    //关闭定时器2
                    window.clearInterval(interval2);
                }
            }
        });
    }


</script>