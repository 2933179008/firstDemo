<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!doctype html>


<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="<%=path%>/plugins/public_components/js/jquery-2.1.4.js"></script>
    <script type="text/javascript" src="<%=path%>/plugins/public_components/js/jquery.mousewheel.js"></script>
    <script type="text/javascript" src="<%=path%>/plugins/public_components/js/jquery.easing.js"></script>
    <script type="text/javascript" src="<%=path%>/plugins/public_components/js/history.js"></script>


</head>

<body style="overflow-x: hidden;">


<div id="arrow">

    <ul>

        <li class="arrowup"></li>

        <li class="arrowdown"></li>

    </ul>

</div>


<div id="history">


    <div class="title">

        <h2 id="product">产品历史</h2>

        <div id="circle">

            <div class="cmsk"></div>

            <div class="circlecontent">

                <span class="fa fa-clock-o" style="font-size: 50px;line-height: 80px;"></span>
            </div>

            <a href="#" class="clock"></a>

        </div>

    </div>


    <div id="content">
        <ul class="list">
        </ul>
    </div>

</div>

</body>
<script>
    (function () {
        var name = "modelId", modelId = '';
        var path = '<%=path%>';
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null) {
            modelId = null;
        }
        else {
            modelId = results[1];
        }
        console.log(modelId);

        $.ajax({
            url: path + "/producttrack/getProductTrackListInfo",
            type: "POST",
            data: {id: modelId},
            dataType: "json",
            async: false,
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    var dom = `<li >
			<div class="liwrap">
				<div class="liright">
					<div class="date">
						<span class="year">` + data[i].complete_time+`</span>
					</div>
				</div>
				<div class="point"><b></b></div>
				<div class=" lileft">
					<div class="histt"><a href="#" style="text-decoration: none">` + data[i].node_id+ `</a></div>
					<div class="hisct">` + data[i].md + `</div>
				</div>
			</div>
		</li>`;
               $("#content .list").append(dom);
          
                }

            }
        });
    }());
</script>
<link rel="stylesheet" type="text/css" href="<%=path%>/plugins/public_components/css/history.css">
<link rel="stylesheet" href="<%=path%>/plugins/public_components/css/font-awesome.min.css">
</html>