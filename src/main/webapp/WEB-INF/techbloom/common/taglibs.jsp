<meta charset="utf-8" />
<style type="text/css">
		*{font-family:"微软雅黑";}
</style>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/animate.css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/select2.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/bootstrap-timepicker.css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/daterangepicker.css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/bootstrap-datetimepicker.css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/bootstrap.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/font-awesome.css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/jquery-ui.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/datepicker.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/ui.jqgrid.css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/ace-fonts.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/plugins/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/plugins/fui/css/fui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/plugins/fui/css/itree.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/plugins/fui/css/form.validate.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/plugins/fui/css/form.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/plugins/fui/themes/default.css">


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/main.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/query.css" />
<!-- ace settings handler -->
<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace-extra.js"></script>
<!--[if lte IE 8]>
	<script src="<%=request.getContextPath()%>/plugins/assets/js/html5shiv.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/assets/js/respond.js"></script>
<![endif]-->

    <!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery|| document.write("<script src='<%=request.getContextPath()%>/plugins/ace/assets/js/jquery.js'>"+ "<"+"/script>");
	</script>
	<!-- <![endif]-->

	<!--[if IE]>
			<script type="text/javascript">
			 window.jQuery || document.write("<script src='<%=request.getContextPath()%>/plugins/ace/assets/js/jquery1x.js'>"+"<"+"/script>");
			</script>
	<![endif]-->
<%-- 	<script type="text/javascript">
		if ('ontouchstart' in document.documentElement)
			document.write("<script src='<%=request.getContextPath()%>/plugins/ace/assets/js/jquery.mobile.custom.js'>"+ "<"+"/script>");
	</script> --%>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/bootstrap.js"></script>

	<!--[if lte IE 8]>
		  <script src="<%=request.getContextPath()%>/assets/js/excanvas.js"></script>
	<![endif]-->
	<script src="<%=request.getContextPath()%>/plugins/public_components/js/jquery-migrate-1.1.0.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/public_components/js/jquery.cookie.js"></script>
	
	
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jquery-ui.custom.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jquery.ui.touch-punch.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jquery.easypiechart.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jquery.sparkline.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/jqGrid/i18n/grid.locale-en.js"></script>
	
	<!-- ace scripts -->
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.scroller.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.colorpicker.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.typeahead.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.spinner.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.wizard.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.aside.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.ajax-content.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.touch-drag.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.sidebar.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.sidebar-scroll-1.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.submenu-hover.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.widget-box.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.widget-on-reload.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.searchbox-autocomplete.js"></script>
	<script type="text/javascript">
		ace.vars['base'] = '<%=request.getContextPath()%>';
	</script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/elements.onpage-help.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/ace/ace.onpage-help.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/docs/assets/js/rainbow.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/docs/assets/js/language/generic.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/docs/assets/js/language/html.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/docs/assets/js/language/css.js"></script>
	<script src="<%=request.getContextPath()%>/plugins/ace/docs/assets/js/language/javascript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/fui/fui.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/fui/fui.validate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/fui/fui.itree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/public_components/js/common.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/public_components/js/select2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/plugins/public_components/js/iTsai-webtools.form.js"></script>
	
	<script src="<%=request.getContextPath()%>/plugins/ace/assets/js/date-time/moment-zh.js"></script>
  <script src="<%=request.getContextPath()%>/plugins/ace/assets/js/date-time/bootstrap-datepicker-zh.js"></script><!-- 时间控件 -->
  <script src="<%=request.getContextPath()%>/plugins/ace/assets/js/date-time/bootstrap-datetimepicker.js"></script>
	

    
