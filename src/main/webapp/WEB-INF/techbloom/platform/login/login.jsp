<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>东洋饮料智能仓储系统</title>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <script type="text/javascript" src="<%=path%>/static/js/jquery-1.7.2.js"></script>
    <script src="<%=request.getContextPath()%>/static/js/jquery.cookie.js"></script>
    <script src="<%=request.getContextPath()%>/plugins/angular.js"></script>
    <style type="text/css">
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            font-size: 13px;
            overflow: hidden;
        }

        body {
            background: #647aa4 url(<%=path%>/static/images/login_bg.png) repeat-x 0 0;
            font-size: 1.1em;
        }

        h2 {
            font-size: 22px;
            color: #fff;
            margin: 0 0 15px 0;
        }

        #loginBox {
            color: #fff;
        }

        a {
            color: #aaa;
            text-decoration: none;
        }

        a:hover {
            color: #fff;
        }

        label {
            display: inline-block;
            text-shadow: 1px 0px 0px #aaa;
        }

        .login-input {
            width: 245px;
            height: 39px;
            background: url(<%=path%>/static/images/login_input_bg.png) no-repeat 0 0;
        }

        .login-input input {
            border-width: 0;
            color: #444;
            font-size: 22px;
            font-family: Arial, Helvetica, sans-serif;
            width: 226px;
            margin: 8px 0 0 10px;
            padding: 0;
            background: none transparent;
            outline: none;
        }

        .login-btn {
            border-width: 0;
            width: 87px;
            height: 39px;
            background: transparent url(<%=path%>/static/images/login_btn.png) no-repeat 0 0;
            cursor: pointer;
        }

        .dwn {
            background-position: 0 -270px;
        }

        #err-msg {
            text-align: center;
            font-weight: bold;
            font-size: 18px;
            padding: 10px;
            color: #cba244;
        }

        #err-msg a {
            color: #cba244;
        }
    </style>
</head>

<body>
<div style="float:left;width:100%;height:100%;">
    <div style="width:630px;margin:0 auto;margin-top:3%;">
        <div class="logo_shadow" style="height:315px;">
            <img src="<%=path %>/static/images/login_logo_techbloom_cc.png"/>
        </div>
        <div id="loginBox" style="position:relative;width:600px;margin:0 auto;">
            <form action="<%=path%>/login/loginCheck" name="LoginForm" id="LoginForm" method="post">
                <table border="0" cellspacing="0" cellpadding="2" align="center">
                    <tr>
                        <td width="250" height="30">&nbsp;<label for="login_name"
                                                                 style="font-size:16px;">用户名</label></td>
                        <td width="250">&nbsp;<label for="login_pwd"
                                                     style="font-size:16px;">密码</label></td>
                        <td width="90">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <div class="login-input">
                                <input type="text" id="login_name" name="loginname" value=""/>
                            </div>
                        </td>
                        <td>
                            <div class="login-input">
                                <input type="password" id="login_pwd" name="password" value=""/>
                            </div>
                        </td>
                        <td>
                            <input id="_submit" type="button" onclick="doSubmit();" value="&nbsp;" class="login-btn"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="40" rowspan="3">
                            <input type="checkbox" id="remember_me" name="rememberme" value="1"
                                   style="vertical-align:middle; "/>
                            <label for="remember_me" style="font-size:10px">记住我的登录状态</label>
                        </td>
                    </tr>
                </table>
                <div id="err-msg"></div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=path %>/static/js/login.js"></script>
<script type="text/javascript">
    $(function () {
        if (window != top)
            top.location.href = location.href;

        var err = ${err};
        if (!isEmpty(err)) {
            document.getElementById("err-msg").innerHTML = login_msg[err];
            wobbleLogin();
        }

        var loginname = $.cookie('loginname');
        var password = $.cookie('password');
        if (!isEmpty(loginname) && !isEmpty(password)) {
            $("#login_name").val(loginname);
            $("#login_pwd").val(password);
            $("#remember_me").attr("checked", true);
        }
        document.LoginForm.loginname.focus();
    });
</script>
</body>
</html>
