var FUI = {
	author : "yswang",
	version : "2.0.0"
};
(function() {
	var v = navigator.userAgent.toLowerCase(), A = function(e) {
		return e.test(v)
	}, j = 0, l = document.compatMode == "CSS1Compat", C = A(/opera/), g = A(/chrome/), w = A(/webkit/), z = !g
			&& A(/safari/), f = z && A(/applewebkit\/4/), b = z
			&& A(/version\/3/), D = z && A(/version\/4/), u = !C && A(/msie/), t = u
			&& A(/msie 6/), s = u && A(/msie 7/), q = u && A(/msie 8/), o = u
			&& A(/msie 9/), n = !w && A(/gecko/), d = n && A(/rv:1\.8/), a = n
			&& A(/rv:1\.9/), i = A(/firefox/), r = A(/firefox\/3/), j = (u ? v
			.match(/msie ([\d.]+)/)[1] : i ? v.match(/firefox\/([\d.]+)/)[1]
			: g ? v.match(/chrome\/([\d.]+)/)[1] : C ? v
					.match(/opera.([\d.]+)/)[1] : z ? v
					.match(/version\/([\d.]+).*safari/)[1] : 0), x = u && !l, B = A(/windows|win32/), k = A(/macintosh|mac os x/), h = A(/adobeair/), m = A(/linux/), c = /^https/i
			.test(window.location.protocol), p = A(/ipad/)
			|| A(/iphone os 3_1_2/) || A(/iphone os 3_2_2/);
	if (t) {
		try {
			document.execCommand("BackgroundImageCache", false, true)
		} catch (y) {
		}
	}
	FUI.namespace = function() {
		var E, e;
		$.each(arguments, function(F, G) {
			e = G.split(".");
			E = window[e[0]] = window[e[0]] || {};
			$.each(e.slice(1), function(H, I) {
				E = E[I] = E[I] || {}
			})
		})
	};
	FUI.CONSTANTS = {
		RANDOM_STRING : "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
		RANDOM_NUM : "0123456789"
	};
	FUI.browser = {
		isOpera : C,
		isChrome : g,
		isWebKit : w,
		isSafari : z,
		isSafari2 : f,
		isSafari3 : b,
		isSafari4 : D,
		isIE : u,
		isIE6 : t,
		isIE7 : s,
		isIE8 : q,
		isIE9 : o,
		isGecko : n,
		isGecko2 : d,
		isGecko3 : a,
		isFirefox : i,
		isFirefox3 : r,
		version : j,
		isBorderBox : x,
		isWindows : B,
		isMac : k,
		isAir : h,
		isLinux : m,
		isiPad : p
	};
	FUI.debug = function(F, E) {
		if (typeof console != "undefined" && typeof console.log != "undefined") {
			console.log(F, E ? E : "")
		}
	};
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, "")
	};
	String.prototype.replaceAll = function(F, E, e) {
		var G = e === false ? "gm" : "gmi";
		return this.replace(new RegExp(F, G), E)
	};
	String.prototype.startWith = function(F, e) {
		var E = (e === false) ? new RegExp("^" + String(F)) : new RegExp("^"
				+ String(F), "i");
		return E.test(this)
	};
	String.prototype.endWith = function(F, e) {
		var E = (e === false) ? new RegExp(String(F) + "$") : new RegExp(
				String(F) + "$", "i");
		return E.test(this)
	};
	String.prototype.toJSON = function() {
		return (new Function("return " + this + ";"))()
	};
	String.prototype.byteLength = function() {
		var E = this;
		if (typeof E == "undefined") {
			return 0
		}
		var e = E.match(/[^\x00-\x80]/g);
		return (E.length + (!e ? 0 : e.length * 2))
	};
	String.prototype.getLength = function() {
		return Math.ceil(this.byteLength() / 3)
	};
	String.prototype.ellipsis = function(e) {
		var K = this.trim();
		var I = 0, G = "";
		var F = /[^\x00-\x80]/g;
		var E = "";
		var J = K.replace(F, "**").length;
		for ( var H = 0; H < J; ++H) {
			E = K.substr(H, 1);
			I += E.match(F) != null ? 2 : 1;
			if (I > e) {
				break
			}
			G += E
		}
		if (J > e) {
			G += "..."
		}
		return G
	};
	Date.prototype.format = function(F) {
		F = F || "yyyy-MM-dd HH:mm:ss";
		var e = {
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"H+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"q+" : Math.floor((this.getMonth() + 3) / 3),
			S : this.getMilliseconds()
		};
		if (/(y+)/.test(F)) {
			F = F.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length))
		}
		for ( var E in e) {
			if (new RegExp("(" + E + ")").test(F)) {
				F = F.replace(RegExp.$1, RegExp.$1.length == 1 ? e[E]
						: ("00" + e[E]).substr(("" + e[E]).length))
			}
		}
		return F
	};
	Array.prototype.contains = function(H, F) {
		if (H === null || H === undefined) {
			return false
		}
		var e = (F === null || F === undefined) ? false : F;
		H = e ? String(H).toLowerCase() : H;
		var E = null;
		for ( var G = 0; G < this.length; G++) {
			E = e ? String(this[G]).toLowerCase() : this[G];
			if (E == H) {
				return true
			}
		}
		return false
	};
	Array.prototype.unique = function(E) {
		var G = new Array();
		var e = (E === null || E === undefined) ? false : E;
		for ( var F = 0; F < this.length; F++) {
			if (!G.contains(this[F], e)) {
				G.push(this[F])
			}
		}
		return G
	};
	Array.prototype.insert = function(e, E) {
		if (e > this.length) {
			this.push(E)
		} else {
			if (e >= 0 && e <= this.length) {
				this.splice(e, 0, E)
			}
		}
		return this
	};
	Array.prototype.remove = function(E) {
		for ( var e = 0; e < this.length; e++) {
			if (E == this[e]) {
				this.splice(e, 1)
			}
		}
		return this
	};
	Array.prototype.max = function() {
		return Math.max.apply( {}, this)
	};
	Array.prototype.min = function() {
		return Math.min.apply( {}, this)
	}
})();
FUI.namespace("FUI.dom");
FUI.dom.getDOM = function(a) {
	if (typeof a == "string") {
		a = document.getElementById(a)
	} else {
		if (typeof a == "object" && a instanceof jQuery) {
			a = a.get(0)
		}
	}
	return a
};
FUI.dom.getPosition = function(a) {
	return {
		x : $(a).offset().left,
		y : $(a).offset().top
	}
};
FUI.dom.getPositionEx = function(d) {
	d = this.getDOM(d);
	var f = this.getPosition(d);
	var e = window, a, c, b;
	while (e != e.parent) {
		if (e.frameElement) {
			b = this.getPosition(e.frameElement);
			f.x += b.x;
			f.y += b.y;
			b = null
		}
		a = Math.max(e.document.body.scrollLeft,
				e.document.documentElement.scrollLeft);
		c = Math.max(e.document.body.scrollTop,
				e.document.documentElement.scrollTop);
		f.x -= a;
		f.y -= c;
		e = e.parent
	}
	return f
};
FUI.dom.removeNode = FUI.browser.isIE ? function() {
	var a;
	return function(c, b) {
		if (c && c.tagName != "BODY") {
			a = a || (b || document).createElement("div");
			a.appendChild(c);
			a.innerHTML = ""
		}
		a = null;
		CollectGarbage()
	}
}() : function(b, a) {
	if (b && b.parentNode && b.tagName != "BODY") {
		b.parentNode.removeChild(b)
	}
};
FUI.dom.getTopWindow = function() {
	var a = window;
	while (a.parent && a.parent != a) {
		try {
			if (a.parent.document.domain != document.domain) {
				break
			}
		} catch (b) {
			break
		}
		a = a.parent
	}
	return a
};
FUI.dom.getDimensions = function(h) {
	h = h ? h : window;
	var g = h.document;
	var c = g.compatMode == "BackCompat" ? g.body.clientWidth
			: g.documentElement.clientWidth;
	var f = g.compatMode == "BackCompat" ? g.body.clientHeight
			: g.documentElement.clientHeight;
	var b = Math.max(g.documentElement.scrollLeft, g.body.scrollLeft);
	var d = Math.max(g.documentElement.scrollTop, g.body.scrollTop);
	var a = Math.max(g.documentElement.scrollWidth, g.body.scrollWidth);
	var e = Math.max(g.documentElement.scrollHeight, g.body.scrollHeight);
	if (e < f) {
		e = f
	}
	return {
		clientWidth : c,
		clientHeight : f,
		scrollLeft : b,
		scrollTop : d,
		scrollWidth : a,
		scrollHeight : e
	}
};
FUI.namespace("FUI.event");
FUI.event.getEvent = function(a) {
	a = a ? a : (window.event || arguments.callee.caller.arguments[0]);
	return a
};
FUI.event.stopEvent = function(a) {
	a = this.getEvent(a);
	if (!a) {
		return
	}
	if (a.stopPropagation) {
		a.preventDefault();
		a.stopPropagation()
	} else {
		a.cancelBubble = true;
		a.returnValue = false
	}
};
FUI.event.cancelEvent = function(a) {
	a = this.getEvent(a);
	if (!a) {
		return
	}
	if (a.stopPropagation) {
		a.stopPropagation()
	} else {
		a.cancelBubble = true
	}
};
FUI.event.fixEvent = function(c) {
	c = this.getEvent(c);
	var a = Math.max(document.documentElement.scrollLeft,
			document.body.scrollLeft);
	var b = Math.max(document.documentElement.scrollTop,
			document.body.scrollTop);
	if (typeof c.layerX == "undefined") {
		c.layerX = c.offsetX
	}
	if (typeof c.layerY == "undefined") {
		c.layerY = c.offsetY
	}
	if (typeof c.pageX == "undefined") {
		c.pageX = c.clientX + a - document.body.clientLeft
	}
	if (typeof c.pageY == "undefined") {
		c.pageY = c.clientY + b - document.body.clientTop
	}
	return c
};
FUI.event.getPosition = function(a) {
	a = this.getEvent(a);
	var b = {
		x : a.clientX,
		y : a.clientY
	};
	b.x = a.pageX
			|| b.x
			+ Math.max(document.body.scrollLeft,
					document.documentElement.scrollLeft);
	b.y = a.pageY
			|| b.y
			+ Math.max(document.body.scrollTop,
					document.documentElement.scrollTop);
	return b
};
FUI.event.getPositionEx = function(a) {
	a = this.getEvent(a);
	var e = {
		x : a.clientX,
		y : a.clientY
	};
	var d, b = (a.srcElement ? a.srcElement : a.target);
	if (FUI.browser.isGecko) {
		d = b.ownerDocument.defaultView
	} else {
		d = b.ownerDocument.parentWindow
	}
	var c;
	while (d != d.parent) {
		if (d.frameElement) {
			c = FUI.dom.getPosition(d.frameElement);
			e.x += c.x;
			e.y += c.y
		}
		d = d.parent
	}
	return e
};
var Drag = {
	obj : null,
	init : function(b, a, f) {
		if (f == null) {
			b.onmousedown = Drag.start
		}
		b.root = a;
		if (isNaN(parseInt(b.root.style.left))) {
			b.root.style.left = "0px"
		}
		if (isNaN(parseInt(b.root.style.top))) {
			b.root.style.top = "0px"
		}
		b.root.onDragStart = new Function();
		b.root.onDragEnd = new Function();
		b.root.onDrag = new Function();
		if (f != null) {
			var b = Drag.obj = b;
			f = Drag.fixe(f);
			var d = parseInt(b.root.style.top);
			var c = parseInt(b.root.style.left);
			b.root.onDragStart(c, d, f.pageX, f.pageY);
			b.lastMouseX = f.pageX;
			b.lastMouseY = f.pageY;
			document.onmousemove = Drag.drag;
			document.onmouseup = Drag.end
		}
	},
	start : function(d) {
		FUI.event.stopEvent(d);
		var a = Drag.obj = this;
		d = FUI.event.fixEvent(d);
		var c = parseInt(a.root.style.top);
		var b = parseInt(a.root.style.left);
		a.root.onDragStart(b, c, d.pageX, d.pageY);
		a.lastMouseX = d.pageX;
		a.lastMouseY = d.pageY;
		document.onmousemove = Drag.drag;
		document.onmouseup = Drag.end;
		return false
	},
	drag : function(i) {
		i = FUI.event.fixEvent(i);
		var f = Drag.obj;
		var b = i.pageY;
		var c = i.pageX;
		var h = parseInt(f.root.style.top);
		var g = parseInt(f.root.style.left);
		if (document.all) {
			Drag.obj.setCapture()
		} else {
			i.preventDefault()
		}
		var d, a;
		d = g + c - f.lastMouseX;
		a = h + (b - f.lastMouseY);
		f.root.style.left = d + "px";
		f.root.style.top = a + "px";
		f.lastMouseX = c;
		f.lastMouseY = b;
		f.root.onDrag(d, a, i.pageX, i.pageY);
		return false
	},
	end : function() {
		if (document.all) {
			Drag.obj.releaseCapture()
		}
		document.onmousemove = null;
		document.onmouseup = null;
		Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left),
				parseInt(Drag.obj.root.style.top));
		Drag.obj = null
	}
};
FUI.namespace("FUI.effect");
FUI.effect.iWobble = function(e, a) {
	if (e.wobbleTimer) {
		return false
	}
	a = a ? a : {};
	var i = {
		l : "left",
		r : "right",
		t : "top",
		b : "bottom"
	};
	var d = $.extend( {
		dir : "tb",
		delay : 92,
		modulus : [ -32, 32, -16, 16, -8, 8, -4, 4, -2, 2, -1, 1 ],
		callback : false
	}, a);
	var j = 0, h = d.modulus, l = h.length;
	var g = e, f = i[d.dir.substr(0, 1).toLowerCase()], k = g.style[f];
	k = k ? k : 0;
	g.style[f] = k;
	g.wobbleTimer = window.setInterval(function() {
		var b = h[j % l];
		g.style[f] = (parseInt(g.style[f], 10) + b) + "px";
		j++;
		if (j > l) {
			window.clearInterval(g.wobbleTimer);
			g.wobbleTimer = null;
			j = 0;
			g.style[f] = k;
			if (d.callback && typeof d.callback == "function") {
				d.callback()
			}
		}
	}, d.delay)
};
FUI.effect.iShake = function(g, a) {
	if (g.shakeTimer) {
		return false
	}
	a = a ? a : {};
	var f = $.extend( {
		dir : [ "top", "left" ],
		delay : 32,
		stepper : 4,
		frequency : 3,
		callback : false
	}, a);
	var e = f.dir, i = 0;
	var d = g.style[e[0]], c = g.style[e[1]];
	var h = [ (d ? parseInt(d, 10) : 0), (c ? parseInt(c, 10) : 0) ];
	var k = parseInt(f.stepper), j = parseInt(f.frequency) * 4 + 1;
	g.shakeTimer = window.setInterval(function() {
		g.style[e[i % 2]] = (h[i % 2] + ((i++) % 4 < 2 ? 0 : k)) + "px";
		if (i > j) {
			window.clearInterval(g.shakeTimer);
			g.shakeTimer = null;
			i = 0;
			if (f.callback && typeof f.callback == "function") {
				f.callback()
			}
		}
	}, parseInt(f.delay))
};
FUI.namespace("FUI.util");
FUI.util.checkAll = function(b, a) {
	$(":checkbox[name=" + b + "]").each(function() {
		this.checked = a
	})
};
FUI.util.checkedNum = function(a) {
	return $(":checkbox[name='" + a + "']:checked").length
};
FUI.util.randomString = function(b) {
	b = b || 5;
	var d = "", a;
	for ( var c = 0; c < b; c++) {
		a = Math.floor(Math.random() * 62);
		d += FUI.CONSTANTS.RANDOM_STRING.charAt(a)
	}
	return d
};
FUI.util.random = function(b) {
	b = b || 5;
	var d = "", a;
	for ( var c = 0; c < b; c++) {
		a = Math.floor(Math.random() * 10);
		d += FUI.CONSTANTS.RANDOM_NUM.charAt(a) + ""
	}
	return d
};
FUI.util.isEmpty = function(a) {
	return (a === null || String(a).trim().length === 0 || typeof a == "undefined")
};
FUI.util.toUTF8 = function(d) {
	var b, a, e = "", c = "";
	for (a = 0; a < d.length; a++) {
		b = d.charCodeAt(a);
		if (!(b & 65408)) {
			c += d.charAt(a)
		} else {
			if (!(b & 61440)) {
				e = "%" + (b >> 6 | 192).toString(16) + "%"
						+ (b & 63 | 128).toString(16);
				c += e
			} else {
				e = "%" + (b >> 12 | 224).toString(16) + "%"
						+ (((b >> 6) & 63) | 128).toString(16) + "%"
						+ (b & 63 | 128).toString(16);
				c += e
			}
		}
	}
	return (c)
};
FUI.util.URLEncode = function(a) {
	return encodeURI(a).replace(/=/g, "%3D").replace(/\+/g, "%2B")
};
FUI.util.htmlEncode = function(a) {
	return !a ? a : String(a).replace(/&/g, "&amp;").replace(/>/g, "&gt;")
			.replace(/</g, "&lt;").replace(/"/g, "&quot;")
};
FUI.util.htmlDecode = function(a) {
	return !a ? a : String(a).replace(/&gt;/g, ">").replace(/&lt;/g, "<")
			.replace(/&quot;/g, '"').replace(/&amp;/g, "&")
};
FUI.util.htmlToText = function(a) {
	return String(a).replace(/\s+|<([^>])+>|&amp;|&lt;|&gt;|&quot;|&nbsp;/gi,
			this._htmlToText)
};
FUI.util._htmlToText = function(a) {
	switch (a) {
	case "&amp;":
		return "&";
	case "&lt;":
		return "<";
	case "&gt;":
		return ">";
	case "&quot;":
		return '"';
	case "&nbsp;":
		return String.fromCharCode(160);
	default:
		if (/\s+/.test(a)) {
			return " "
		}
		if (/^<BR/gi.test(a)) {
			return "\n"
		}
		return ""
	}
};
FUI.util.highlight = function(e, c, b) {
	if (this.isEmpty(c)) {
		return e
	}
	var f = b ? b : '<b style="color:#ff0000"></b>';
	f = f.replace(/(<(\w+)(\s+[^<]*)*>)([^<]*)(<\/\2>)/gi, "$1$$1$5");
	c = c.trim();
	var d = c.split(/[^a-zA-Z\d\.\u4e00-\u9fa5]+/);
	var g = e;
	if (d && d.length > 0) {
		d = d.unique(true);
		for ( var a = 0; a < d.length; a++) {
			if (!this.isEmpty(d[a])) {
				g = g.replace(new RegExp("(?!<[^<>]*)(" + String(d[a]).trim()
						+ ")(?![^<>]*>)", "gi"), f)
			}
		}
	}
	return g
};
FUI.util.ellipsis = function(c, a, d) {
	if (c && c.length > a) {
		if (d) {
			var e = c.substr(0, a - 2), b = Math.max(e.lastIndexOf(" "), e
					.lastIndexOf("."), e.lastIndexOf("!"), e.lastIndexOf("?"));
			if (b == -1 || b < (a - 15)) {
				return c.substr(0, a) + " ..."
			} else {
				return e.substr(0, b) + " ..."
			}
		} else {
			return c.substr(0, a) + " ..."
		}
	}
	return c
};
FUI.util.toJSON = function(value) {
	if (!value || value == "undefined") {
		return null
	}
	if (typeof value == "string") {
		value = (new Function("return " + value + ";"))();
		if (!value || value == "undefined") {
			value = eval("(" + value + ")")
		}
		return value
	} else {
		return value
	}
};

var DialogSetting = {
	Window : {
		title : "新窗口",
		closeTitle : "点击关闭"
	},
	Alert : {
		title : "系统提示",
		buttons : [ "确定" ]
	},
	Confirm : {
		title : "系统确认",
		buttons : [ "&nbsp;是&nbsp;", "&nbsp;否&nbsp;", "取消" ]
	},
	Success : {
		title : "成功提示",
		buttons : [ "确定" ]
	},
	Error : {
		title : "错误提示",
		buttons : [ "确定" ]
	},
	Warning : {
		title : "警告提示",
		buttons : [ "确定" ]
	},
	Prompt : {
		title : "提示",
		buttons : [ "确定", "取消" ]
	}
};
var _ielt7 = (FUI.browser.isIE && parseInt(FUI.browser.version) < 7), $WinSize = function(
		a) {
	return FUI.dom.getDimensions(a)
};
function __$id__(a) {
	return document.getElementById(a)
}
Array.prototype.removeDiag = function(b) {
	for ( var a = 0; a < this.length; a++) {
		if (b == this[a]) {
			this.splice(a, 1)
		}
	}
	return this
};
function Dialog(a) {
	a = a ? a : {};
	this.id = null;
	this.title = "&nbsp;";
	this.url = false;
	this.drag = true;
	this.shadow = true;
	this.width = 600;
	this.height = false;
	this.autoHeight = false;
	this.left = "50%";
	this.top = "50%";
	this.resizable = false;
	this.theme = null;
	this.content = null;
	this.modal = true;
	this.delay = 0;
	this.closeable = true;
	this.context = FUI.dom.getTopWindow();
	this.domain = null;
	this.closeEvent = false;
	this.onLoad = false;
	this.onShow = null;
	this.onClosed = null;
	this.onShowAnimate = false;
	this.onCloseAnimate = false;
	this.openerWindow = null;
	this.openerDialog = null;
	this.innerWin = null;
	this.innerDoc = null;
	this.unauthorized = false;
	this.zindex = 9999;
	this.AlertModel = false;
	Dialog.setOptions(this, a);
	this.contextDoc = this.context.document;
	if (!this.id) {
		this.id = new Date().getTime()
	}
	this.WinName = window._iDiagWinName_
}
Dialog.idPrefix = "";
Dialog._diagArr = [];
Dialog._childDiagArr = [];
Dialog.maskDiv = null;
Dialog.prototype.getDom = function(a) {
	return this.context.__$id__(a + this.id)
};
Dialog.prototype.create = function() {
	var a = '     	<div class="idlg-struct '
			+ (this.theme ? this.theme : "")
			+ '">     		<div id="idlg-control-'
			+ this.id
			+ '" class="idlg-control-btns">	     		<a href="javascript:;" id="idlg-close-'
			+ this.id
			+ '" class="idlg-close"></a>	     	</div>     		<div id="idlg-stin-'
			+ this.id
			+ '" class="idlg-struct-inner">	     		<div id="idlg-hd-'
			+ this.id
			+ '" class="idlg-header">	     			<div class="idlg-corner-tl"></div>	     			<div class="idlg-corner-tr"></div>	     			<div class="idlg-border-t">	     				<div id="idlg-title-'
			+ this.id
			+ '" class="idlg-title">'
			+ this.title
			+ '</div>	     			</div>	     		</div>	     		<div class="idlg-body">	     			<div class="idlg-border-l"></div>	     			<div class="idlg-border-r"></div>	     			<div id="idlg-content-'
			+ this.id
			+ '" class="idlg-content"></div>	     			<div id="idlg-ft-'
			+ this.id
			+ '" class="idlg-footer">		     			<div class="idlg-corner-bl"></div>		     			<div class="idlg-corner-br"></div>		     			<div class="idlg-border-b">		     				<div id="idlg-ftbox-'
			+ this.id
			+ '"></div>		     			</div>	     			</div>	     		</div>	     	</div>     	</div>     ';
	var c = '		<div class="resize-N"></div>		<div class="resize-NE"></div>		<div class="resize-SE"></div>		<div class="resize-S"></div>		<div class="resize-SW"></div>		<div class="resize-NW"></div>		<div class="resize-W"></div>		<div class="resize-E"></div>	';
	if (this.domain != null) {
		this.contextDoc.domain = this.domain
	}
	var b = this.contextDoc.createElement("div");
	b.id = Dialog.idPrefix + this.id;
	b.className = "fui-idialog";
	this.contextDoc.body.appendChild(b);
	b.innerHTML = a + (this.resizable ? c : "");
	var d = this;
	if (this.url) {
	
		if(d.autoHeight){
			$(this.getDom("idlg-content-")).parents(".fui-idialog").css("visibility","hidden");
		}
		this.getDom("idlg-content-").innerHTML = '<iframe id="idlg-ifrm-'
				+ this.id
				+ '" src="about:blank" frameborder="0" width="100%" height="0" allowTransparency="true" scrolling="auto" style="position:absolute;left:0;top:0;border:0;width:100%;z-index:1;"></iframe><div id="idlg-ifrm-mask-'
				+ this.id
				+ '" style= position:absolute;left:0;top:-9999em;width:100%;height:100%;background-color:#fff;opacity:0;filter:alpha(opacity=0);z-index:2;"></div>';
		d.ifrmLoad = function(f) {
			var g = f.srcElement || f.target;
			try {
				g.contentWindow.ownerDialog = d;
				d.innerWin = g.contentWindow;
				d.innerDoc = g.contentWindow.document;
				//chenying 查询form中第一个input 并获取焦点， 建议bug修改
				var forms = g.contentDocument.forms; 
				if(forms && forms.length > 0){
					var one = forms[0];
					var input = $( one).find("input:visible:eq(0)")[0];
					if(input && input.type === 'text'){
							input.focus();
					}
				}
				//chenying 查询form中第一个input 并获取焦点， 建议bug修改 end
			} catch (h) {
				d.unauthorized = true
			}
			g = null;
			if (d.onLoad && typeof (d.onLoad) == "function") {
				d.onLoad.call(d)
			}
			if(d.autoHeight){
				var browserVersion = window.navigator.userAgent.toUpperCase();
				var isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
				var isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
				var isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
				var isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
				var isIE = (!!window.ActiveXObject || "ActiveXObject" in window);
				var isIE9More = (! -[1, ] == false);
				  try {
				        var iframe = f.target;
				        var bHeight = 0;
				        if (isChrome == false && isSafari == false)
				            bHeight = iframe.contentWindow.document.body.scrollHeight;

				        var dHeight = 0;
				        if (isFireFox == true)
				            dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2;
				        else if (isIE == false && isOpera == false)
				            dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
				        else if (isIE == true && isIE9More) {//ie9+
				            var heightDeviation = bHeight - eval("window.IE9MoreRealHeight" + iframeId);
				            if (heightDeviation == 0) {
				                bHeight += 3;
				            } else if (heightDeviation != 3) {
				                eval("window.IE9MoreRealHeight" + iframeId + "=" + bHeight);
				                bHeight += 3;
				            }
				        }
				        else//ie[6-8]、OPERA
				            bHeight += 3;

				        var height = Math.max(bHeight, dHeight);
				        /*if (height < minHeight) height = minHeight;*/
				        iframe.style.height = height + "px";
				        iframe.style.position = null; 
				        d.height = height;
				        d.setPosition(true); 
				      
				    	$(d.getDom("idlg-content-")).parents(".fui-idialog").css("visibility","");
				    	
				    	
				    } catch (ex) { }
			}
		};
		var e = this.getDom("idlg-ifrm-");
		if (e.attachEvent) {
			e.attachEvent("onload", d.ifrmLoad)
		} else {
			if (e.addEventListener) {
				e.addEventListener("load", d.ifrmLoad, false)
			}
		}
		e = null
	}
	this.getDom("idlg-close-").onclick = function(f) {
		if (this.disabled) {
			return false
		}
		if (d.closeEvent && typeof d.closeEvent == "function") {
			d.closeEvent.call(d);
			return false
		}
		if (d.onCloseAnimate && typeof d.onCloseAnimate == "function") {
			d.onCloseAnimate.call(d, d.getDom(Dialog.idPrefix))
		} else {
			d.close()
		}
		return false
	};
	if (this.drag) {
		this.setDragable()
	}
	if (this.resizable) {
		this.getDom(Dialog.idPrefix).onmousedown = function(f) {
			f = f || d.context.event;
			var g = f.srcElement || f.target;
			if (g.className.indexOf("resize-") != -1) {
				var h = g.className;
				h = h.substr(h.lastIndexOf("-") + 1, h.length - 1);
				Dialog_resize.call(d, f, h);
				h = null
			}
			g = null
		}
	}
	this.setSize(this.width, this.height ? this.height : -1);
	this.openerWindow = window;
	b.dialogInstance = this;
	if (window.ownerDialog) {
		this.openerDialog = window.ownerDialog
	}
	try {
		return b
	} finally {
		b = null
	}
};
Dialog.prototype.show = function() {
	var e = this.getDom(Dialog.idPrefix);
	if (e) {
		if (this.url) {
			this.getDom("idlg-ifrm-").src = this.displacePath()
		}
		return false
	} else {
		e = this.create();
		var b = Dialog.createMask(this.context);
		if (this.modal) {
			if (this.context.Dialog._diagArr.length > 0) {
				b.style.zIndex = this.context.Dialog._diagArr[this.context.Dialog._diagArr.length - 1].zindex + 1
			} else {
				Dialog.hideScrollBar(this.context, true)
			}
			b.style.top = "0px";
			Dialog.resizeMask(this.context)
		}
		e.style.zIndex = this.zindex = parseInt(
				this.context.Dialog.maskDiv.style.zIndex, 10) + 1;
		if (this.context.Dialog._diagArr.length > 0) {
			var d = this.context.Dialog._diagArr[this.context.Dialog._diagArr.length - 1];
			e.style.zIndex = this.zindex = parseInt(d.zindex, 10) + 2;
			d = null
		}
		this.context.Dialog._diagArr.push(this);
		Dialog._childDiagArr.push(this);
		if (Dialog._childDiagArr.length == 1) {
			if (window.ownerDialog) {
				window.ownerDialog.disableCloseBtn(true)
			}
		}
		this.setPosition();
		if (this.url) {
			this.getDom("idlg-ifrm-").src = this.displacePath()
		}
		if (this.content) {
			var c = this.content;
			if (typeof c == "function") {
				var a = this;
				c = c.call(this, function(f) {
					a.getDom("idlg-content-").innerHTML = f
				})
			}
			this.getDom("idlg-content-").innerHTML = c
		}
		if (this.onShow && typeof this.onShow == "function") {
			this.onShow.call(this)
		}
		b = null
	}
};
Dialog.prototype.close = function() {
	if (this.onClosed && typeof this.onClosed == "function") {
		this.onClosed.call(this)
	}
	if (this.unauthorized === false) {
		if (this.innerWin && this.innerWin.Dialog
				&& this.innerWin.Dialog._childDiagArr.length > 0) {
			return
		}
	}
	if (this === this.context.Dialog._diagArr[this.context.Dialog._diagArr.length - 1]) {
		var c = this.context.Dialog._diagArr.pop()
	} else {
		this.context.Dialog._diagArr.removeDiag(this)
	}
	Dialog._childDiagArr.removeDiag(this);
	if (Dialog._childDiagArr.length == 0) {
		if (window.ownerDialog && window.ownerDialog.closeable) {
			window.ownerDialog.disableCloseBtn(false)
		}
	}
	var d = true;
	if (this.context.Dialog._diagArr.length > 0 && this.modal && c) {
		var b = this.context.Dialog._diagArr.length;
		while (b > 0) {
			b = b - 1;
			if (this.context.Dialog._diagArr[b].modal) {
				this.context.Dialog.maskDiv.style.zIndex = this.context.Dialog._diagArr[b].zindex - 1;
				d = false;
				break
			}
		}
	}
	if (d && this.modal) {
		this.context.Dialog.maskDiv.style.top = "-9999em";
		Dialog.hideScrollBar(this.context, false)
	}
	this.openerWindow = null;
	this.openerDialog = null;
	this.innerWin = null;
	this.innerDoc = null;
	var g = this.getDom(Dialog.idPrefix);
	g.style.top = "-9999em";
	g.dialogInstance = null;
	g.onmousedown = null;
	g.onDragStart = null;
	g.onDragEnd = null;
	g.onDrag = null;
	this.getDom("idlg-close-").onclick = null;
	this.getDom("idlg-hd-").root = null;
	this.getDom("idlg-hd-").onmousedown = null;
	if (this.url) {
		var a = this.getDom("idlg-ifrm-");
		if (a.detachEvent) {
			a.detachEvent("onload", this.ifrmLoad)
		} else {
			a.removeEventListener("load", this.ifrmLoad, false)
		}
		this.ifrmLoad = null;
		try {
			a.contentWindow.ownerDialog = null;
			a.contentWindow.document.write("");
			a.contentWindow.document.close()
		} catch (f) {
		}
		a.parentNode.removeChild(a);
		a = null
	}
	FUI.dom.removeNode(g, this.contextDoc);
	g = null;
	if (document.all) {
		CollectGarbage()
	}
};
Dialog.prototype.setSize = function(c, d) {
	c = parseInt(c, 10), d = parseInt(d, 10);
	var e = $WinSize(this.context);
	c = (c >= e.clientWidth ? e.clientWidth - 10 : c);
	this.getDom(Dialog.idPrefix).style.width = c + "px";
	if (d <= 0) {
		return
	}
	d = (d >= e.clientHeight ? e.clientHeight - 10 : d);
	var a = this.getDom("idlg-hd-").offsetHeight, f = this.getDom("idlg-ft-").offsetHeight;
	var b = d - a - f;
	if (this.url) {
		this.getDom("idlg-ifrm-").height = b
	}
	this.getDom("idlg-content-").style.height = b + "px"
};
Dialog.prototype.displacePath = function() {
	if (this.url.substr(0, 7) == "http://" || this.url.substr(0, 1) == "/"
			|| this.url.substr(0, 11) == "javascript:") {
		return this.url
	} else {
		var a = this.url;
		var b = window.location.href;
		b = b.substring(0, b.lastIndexOf("/"));
		while (a.indexOf("../") >= 0) {
			a = a.substring(3);
			b = b.substring(0, b.lastIndexOf("/"))
		}
		return b + "/" + a
	}
};
Dialog.prototype.setPosition = function(sign) {
	var d = $WinSize(this.context);
	var f = this.top, c = this.left, g = this.getDom(Dialog.idPrefix);
	if( sign ){
		this.top = "50%";
		this.left = "50%"
	}
	if (typeof this.top == "string" && this.top.indexOf("%") != -1) {
		var e = parseFloat(this.top) * 0.01;
		var a = (e == 0.5 && g.offsetHeight <= d.clientHeight / 2) ? 0.382 : e;
		f = d.clientHeight * a - g.offsetHeight * e + d.scrollTop
	}
	if ( typeof this.left == "string" && this.left.indexOf("%") != -1 ) {
		var b = parseFloat(this.left) * 0.01;
		c = _ielt7 ? d.clientWidth * b - g.scrollWidth * b + d.scrollLeft
				: d.clientWidth * b - g.scrollWidth * b
	}
	this.top = f;
	this.left = c;
	g.style.left = Math.round(c) + "px";
	if (this.onShowAnimate && typeof this.onShowAnimate == "function") {
		this.onShowAnimate.call(this, g, f, c)
	} else {
		g.style.top = Math.round(f) + "px"
	}
	g = null;
	d = null
};
Dialog.prototype.disableCloseBtn = function(b) {
	var a = this.getDom("idlg-close-");
	if (b) {
		a.disabled = true;
		a.className = "idlg-close idlg-close-dis"
	} else {
		a.disabled = false;
		a.className = "idlg-close"
	}
	a = null
};
Dialog.prototype.setDragable = function() {
	var c = this, d = null, e = this.shadow ? 10 : 5;
	if (this.drag && this.context.Drag) {
		var a = this.getDom("idlg-hd-"), b = this.getDom(Dialog.idPrefix);
		this.context.Drag.init(a, b);
		b.onDragStart = function(i, h, g, f) {
			if (c.url) {
				c.getDom("idlg-ifrm-mask-").style.top = "0px"
			}
			d = $WinSize(c.context)
		};
		b.onDrag = function(f, i, h, g) {
			if (f < 5) {
				this.style.left = "5px"
			} else {
				if (f + this.clientWidth + e > d.clientWidth) {
					this.style.left = (d.clientWidth - this.clientWidth - e)
							+ "px"
				}
			}
			if (i < (d.scrollTop + 5)) {
				this.style.top = (d.scrollTop + 5) + "px"
			} else {
				if (i + this.clientHeight + e > d.scrollTop + d.clientHeight) {
					this.style.top = (d.scrollTop + d.clientHeight
							- this.clientHeight - e)
							+ "px"
				}
			}
		};
		b.onDragEnd = function(i, h, g, f) {
			if (c.url) {
				c.getDom("idlg-ifrm-mask-").style.top = "-9999em"
			}
			this.dialogInstance.left = parseInt(this.style.left);
			this.dialogInstance.top = parseInt(this.style.top)
		}
	}
};
Dialog.setOptions = function(b, a) {
	if (a) {
		for ( var c in a) {
			b[c] = a[c]
		}
	}
};
Dialog.createMask = function(a) {
	if (a.Dialog && a.Dialog.maskDiv) {
		return a.Dialog.maskDiv
	}
	var b = a.__$id__("idlg-mask-layer");
	if (!b) {
		b = a.document.createElement("div");
		b.id = "idlg-mask-layer";
		b.className = "idlg-mask-layer";
		b.style.cssText = "position:absolute;left:0px;top:0px;z-index:9999";
		a.document.getElementsByTagName("BODY")[0].appendChild(b);
		b.innerHTML = '<iframe src="about:blank" id="idlg-mask-ifrm" frameborder="0" style="position:absolute;left:0;top:0;opacity:0;filter:alpha(opacity=0);z-index:1;" width="100%" height="100%"></iframe><div id="idlg-mask-ifrm-layer" style="position:absolute;left:0;top:0;z-index:2;"></div>';
		b.oncontextmenu = function() {
			return false
		}
	}
	a.Dialog.maskDiv = b;
	b = null;
	return a.Dialog.maskDiv
};
Dialog.resizeMask = function(a) {
	var b = a.Dialog.maskDiv;
	if (b && parseInt(b.style.top, 10) >= 0) {
		var c = $WinSize(a);
		a.__$id__("idlg-mask-ifrm").width = c.scrollWidth;
		a.__$id__("idlg-mask-ifrm").height = c.scrollHeight;
		a.__$id__("idlg-mask-ifrm-layer").style.width = c.scrollWidth + "px";
		a.__$id__("idlg-mask-ifrm-layer").style.height = c.scrollHeight + "px"
	}
	b = null
};
var Dialog_resize = function(l, e) {
	var j, g, k, d, f, b, a;
	function i(h) {
		var m = {};
		if (a.indexOf("E") != -1) {
			m.width = Math.max(200, k + (h.pageX - f))
		}
		if (a.indexOf("S") != -1) {
			m.height = Math.max(150, d + (h.pageY - b))
		}
		if (a.indexOf("W") != -1) {
			m.width = Math.max(200, k + (f - h.pageX));
			m.left = f + (k - parseInt(m.width))
		}
		if (a.indexOf("N") != -1) {
			m.height = Math.max(150, d + (b - h.pageY));
			m.top = b + (d - parseInt(m.height))
		}
		if (m.left) {
			g.style.left = m.left + "px"
		}
		if (m.top) {
			g.style.top = m.top + "px"
		}
		j.setSize(m.width ? m.width : k, m.height ? m.height : d);
		FUI.event.stopEvent(h)
	}
	function c(h) {
		$(j.context.document).unbind("mousemove", i);
		$(j.context.document).unbind("mouseup", c);
		if (document.all) {
			g.releaseCapture()
		}
		if (j.url) {
			j.getDom("idlg-ifrm-mask-").style.top = "-9999em"
		}
		j = null;
		g = null
	}
	l = FUI.event.fixEvent(l);
	j = this, g = j.getDom(Dialog.idPrefix), k = g.offsetWidth,
			d = g.offsetHeight, f = l.pageX, b = l.pageY, a = e;
	$(j.context.document).bind("mousemove", i);
	$(j.context.document).bind("mouseup", c);
	if (j.url) {
		j.getDom("idlg-ifrm-mask-").style.top = "0px"
	}
	if (document.all) {
		g.setCapture()
	} else {
		l.preventDefault()
	}
};
Dialog.hideScrollBar = function(b, a) {
	if (a) {
		b.$("html").addClass("idlg-lock-scroll")
	} else {
		b.$("html").removeClass("idlg-lock-scroll")
	}
};
Dialog.prototype.setTitle = function(a) {
	this.getDom("idlg-title-").innerHTML = a
};
Dialog.prototype.reload = function(a) {
	this.getDom("idlg-ifrm-").src = a
};
Dialog.open = function(a) {
	var b = new Dialog(a);
	b.show();
	return b
};
Dialog.ownerDialog = function() {
	if (window.ownerDialog) {
		return window.ownerDialog
	} else {
		return null
	}
};
Dialog.openerWindow = function() {
	if (window.ownerDialog) {
		return window.ownerDialog.openerWindow
	} else {
		return null
	}
};
Dialog.openerDialog = function() {
	var a = Dialog.openerWindow();
	if (a && a.ownerDialog) {
		return a.ownerDialog
	} else {
		return null
	}
};
Dialog.close = function(a, b) {
	b = b ? b : FUI.dom.getTopWindow();
	var c = b.document.getElementById("idlg-close-" + a);
	if (c) {
		c.onclick()
	} else {
		FUI.debug("Can't find the Dialog by id [ " + a + " ]!")
	}
	c = null
};
Dialog.createAlert = function(l, b, a, f) {
	a = a ? a : {};
	if (!a.width) {
		a.width = 380
	}
	a.height = 0;
	a.resizable = false;
	a.closeable = false;
	var j = new Dialog(a);
	j.AlertModel = true;
	j.title = (a.title != undefined) ? a.title : DialogSetting[l].title;
	j.theme = "idlg-alert"
			+ ((a.title === undefined || $.trim(a.title) == "") ? " idlg-alert-notitle"
					: "") + (a.theme ? " " + a.theme : "");
	var g = "idlg-icon-" + l.toLowerCase()
			+ (a.iconClass ? " " + a.iconClass : "");
	var c = "";
	if ("Prompt" == l) {
		b = b.replace(/\{ID\}/gm, j.id);
		c = ' style="display:none;"'
	}
	var d = [];
	for ( var h = 0; h < f.length; ++h) {
		d.push('<a href="javascript:void(0);" tabindex="-1" class="idlg-btn'
				+ (f[h].focus ? " idlg-btn-focus" : "")
				+ '"><span class="idlg-btn-txt">' + f[h].label + "</span></a>")
	}
	j.content = '    	<div class="idlg-alert-panel">    		<table border="0" width="100%" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">    			<tr>    				<td align="center" class="idlg-alert-icon"'
			+ c
			+ '><div class="'
			+ g
			+ '"></div></td>    				<td class="idlg-alert-msg" width="100%">'
			+ b
			+ '</td>    			</tr>    			<tr style="display:'
			+ (d.length > 0 ? "" : "none")
			+ ';">    				<td colspan="2"><div id="idlg-btnp-'
			+ j.id
			+ '" class="idlg-btn-panel">'
			+ d.join("")
			+ "</div></td>    			</tr>    		</table>    	</div>    ";
	try {
		return j
	} catch (k) {
	} finally {
		j = null
	}
};
Dialog.alert = function(e, c, b) {
	b = b ? b : {};
	if (!b._innerType) {
		b._innerType = "Alert"
	}
	var d = Dialog.createAlert(b._innerType, e, b, [ {
		label : b.yesLabel ? b.yesLabel
				: DialogSetting[b._innerType].buttons[0],
		focus : true
	} ]);
	d.show();
	var a = $("a.idlg-btn", d.getDom("idlg-btnp-"));
	if (a != null && a.length > 0) {
		a[0].onclick = function() {
			d.getDom("idlg-close-").onclick();
			c && c.call(d)
		};
		a[0].focus()
	}
	return d
};
Dialog.confirm = function(f, d, a, c) {
	c = c ? c : {};
	var e = Dialog.createAlert("Confirm", f, c, [ {
		label : c.yesLabel ? c.yesLabel : DialogSetting.Confirm.buttons[0],
		focus : true
	}, {
		label : c.noLabel ? c.noLabel : DialogSetting.Confirm.buttons[1]
	} ]);
	e.show();
	var b = $("a.idlg-btn", e.getDom("idlg-btnp-"));
	if (b != null && b.length > 0) {
		b[0].onclick = function() {
			e.getDom("idlg-close-").onclick();
			d && d.call(e)
		};
		b[0].focus();
		b[1].onclick = function() {
			e.getDom("idlg-close-").onclick();
			a && a.call(e)
		}
	}
	b = null;
	return e
};
Dialog.warning = function(c, b, a) {
	a = a ? a : {};
	a._innerType = "Warning";
	return Dialog.alert(c, b, a)
};
Dialog.success = function(c, b, a) {
	a = a ? a : {};
	a._innerType = "Success";
	return Dialog.alert(c, b, a)
};
Dialog.error = function(c, b, a) {
	a = a ? a : {};
	a._innerType = "Error";
	return Dialog.alert(c, b, a)
};
Dialog.prompt = function(f, d, a, c) {
	c = c ? c : {};
	var g = '<div class="idlg-prompt-panel"><div class="idlg-prompt-title">'
			+ f + "</div>";
	if (c.multiline) {
		g += '<textarea id="idlg-prompt-{ID}" class="idlg-prompt-textarea"></textarea>'
	} else {
		g += '<input type="text" id="idlg-prompt-{ID}" class="idlg-prompt-input"/>'
	}
	g += "</div>";
	var e = Dialog.createAlert("Prompt", g, c, [ {
		label : c.yesLabel ? c.yesLabel : DialogSetting.Prompt.buttons[0],
		focus : true
	}, {
		label : c.noLabel ? c.noLabel : DialogSetting.Prompt.buttons[1]
	} ]);
	e.show();
	var b = $("a.idlg-btn", e.getDom("idlg-btnp-"));
	if (b != null && b.length > 0) {
		b[0].onclick = function() {
			d && d.call(e, e.context.__$id__("idlg-prompt-" + e.id).value);
			e.getDom("idlg-close-").onclick()
		};
		b[1].onclick = function() {
			a && a.call(e, e.context.__$id__("idlg-prompt-" + e.id).value);
			e.getDom("idlg-close-").onclick()
		}
	}
	if (!c.multiline) {
		e.context.__$id__("idlg-prompt-" + e.id).onkeydown = (function(h) {
			return function(i) {
				i = i ? i : window.event;
				if (i.keyCode == 13) {
					h[0].onclick()
				}
			}
		})(b)
	}
	b = null;
	return e
};
Dialog.tip = function(c, b) {
	b = b ? b : {};
	b.theme = "idlg-tip";
	b.width = 300;
	b.iconClass = "idlg-icon-tip";
	var a = Dialog.createAlert("Alert", c, b, []);
	a.show();
	if (b.delay && parseInt(b.delay, 10) > 0) {
		window.setTimeout(function() {
			a.close()
		}, parseInt(b.delay, 10))
	}
	return a
};
var _winUnloadCloseiDlg = function() {
	var b = window._iDiagWinName_, c = null;
	if (Dialog) {
		if (Dialog._childDiagArr.length > 0) {
			for ( var a = Dialog._childDiagArr.length - 1; a >= 0; a--) {
				c = Dialog._childDiagArr[a];
				if (c.WinName == b) {
					c.close()
				}
			}
		}
		c = null;
		if (Dialog._diagArr.length > 0) {
			for ( var a = Dialog._diagArr.length - 1; a >= 0; a--) {
				c = Dialog._diagArr[a];
				if (c.WinName == b) {
					c.close()
				}
			}
		}
	}
};
var _idlgRSTimer_ = null;
window.onresize = function() {
	if (_idlgRSTimer_) {
		window.clearTimeout(_idlgRSTimer_);
		_idlgRSTimer_ = null
	}
	_idlgRSTimer_ = window.setTimeout(function() {
		Dialog.resizeMask(window)
	}, 200)
};
window.onload = function() {
	window._iDiagWinName_ = new Date().getTime()
};
window.onunload = function() {
	_winUnloadCloseiDlg()
};
var iPopupMenu = function(g, m, l, n, f) {
	if (!g) {
		return false
	}
	if (!l) {
		l = this
	}
	var d = this, c = function() {
	};
	var k = this.MUI("div", null, false, d.classes.bdy + (f ? " " + f : ""));
	var b = this.MUI("div", k, false, d.classes.shadow);
	if (FUI.browser.isIE6) {
		b.innerHTML = '<iframe src="about:blank" frameborder="0" style="filter:alpha(opacity=0);border:0 none;width:100%;height:100%;" width="100%" height="100%"></iframe>'
	}
	b = null;
	var j = this.MUI("UL", k, false, d.classes.ul);
	if (n) {
		k.style.width = parseInt(n, 10) + "px"
	}
	j.onclick = j.onmousedown = function(i) {
		FUI.event.cancelEvent(i)
	};
	j.oncontextmenu = function(i) {
		FUI.event.cancelEvent(i);
		return false
	};
	d.UI = k;
	for ( var e = 0, h = g.length; e < h; ++e) {
		var p = g[e], o = this.MUI("LI", j, p.id ? p.id : false,
				p.disabled ? d.classes.liDis : d.classes.li);
		if (p.tagLabel) {
			o.className = d.classes.liTagLabel;
			o.innerHTML = p.tagLabel;
			if (e === 0) {
				j.style.paddingTop = "0px"
			}
			continue
		}
		if (p.html) {
			o.innerHTML = '<div class="' + d.classes.liHtml + '">' + p.html
					+ "</div>";
			continue
		}
		if (p.splitter === true) {
			o.className = d.classes.liLine;
			continue
		}
		if (p.disabled) {
			o.disabled = true
		}
		o.onmouseover = function() {
			if (this.className.indexOf(d.classes.li) == -1) {
				return false
			}
			var r = this.parentNode.getElementsByTagName("LI");
			for ( var s = 0; s < r.length; s++) {
				var q = r[s];
				if (q != this) {
					q.className == d.classes.liOpen
							&& (q.className = d.classes.li);
					q.subMenu && (q.subMenu.hide())
				}
			}
		};
		if (p.radio != undefined) {
			o.setAttribute("radio", p.radio);
			p.iconClass = this.classes.liIco;
			if (p.selected == true && !this.hasOnlyRadio) {
				p.iconClass = this.classes.radioIco;
				this.hasOnlyRadio = true
			}
		}
		if (p.checkbox != undefined) {
			o.setAttribute("multi", p.checkbox);
			p.iconClass = this.classes.liIco;
			if (p.selected == true) {
				p.iconClass = this.classes.checkboxIco;
				o.selected = true
			} else {
				o.selected = false
			}
		}
		var a = '<em class="imenu-ico'
				+ (p.iconClass ? " " + p.iconClass : "")
				+ '"'
				+ (p.icon ? ' style="background-image:url(' + p.icon + ');"'
						: "") + "></em>";
		if (p.subMenu) {
			a += '<b class="sub-arrow"></b>'
		}
		this.MUI("SPAN", this.MUI("A", o, false, p.subMenu ? d.classes.liASub
				: d.classes.liA, a), false, d.classes.liLabel, p.label);
		if (!p.subMenu) {
			if (typeof p.onclick != "function") {
				p.onclick = this.click
			}
			o.onclick = (function(i) {
				return function(u) {
					u = FUI.event.getEvent(u);
					if (this.disabled) {
						FUI.event.cancelEvent(u);
						return false
					}
					l.hide();
					if (i.radio) {
						var s = this.parentNode.getElementsByTagName("LI"), t = this
								.getAttribute("radio");
						for ( var r = 0; r < s.length; r++) {
							var q = s[r];
							if (t != q.getAttribute("radio")) {
								continue
							}
							var v = q.getElementsByTagName("EM");
							if (v != null) {
								v[0].className = (q == this) ? d.classes.radioIco
										: d.classes.liIco
							}
						}
						i.selected = this.selected = true
					} else {
						if (i.checkbox) {
							var q = (this.selected == true);
							var v = this.getElementsByTagName("EM");
							if (v != null) {
								v[0].className = !q ? d.classes.checkboxIco
										: d.classes.liIco
							}
							i.selected = this.selected = !q
						}
					}
					i.onclick.call(d.targetEle, i)
				}
			})(p)
		} else {
			new iPopupMenu(p.subMenu, o, l, p.width ? p.width : false, f)
		}
	}
	if (m) {
		if (typeof m.onmouseover == "function") {
			c = m.onmouseover
		}
		m.onmouseover = function(i) {
			if (m.disabled == true) {
				return false
			}
			var q = this;
			if (q.showTimer) {
				window.clearTimeout(q.showTimer)
			}
			q.showTimer = window
					.setTimeout(
							function() {
								c.call(q);
								q.className = d.classes.liOpen;
								var t = d.absPos(q), r = (t.x + q.offsetWidth - 2), s = t.y + 5;
								d.show(r, s, q)
							}, 200)
		};
		m.onmouseout = function() {
			window.clearTimeout(this.showTimer);
			this.hideTimer = setTimeout(function() {
				d.hide()
			}, 50)
		};
		m.getElementsByTagName("A")[0].onmouseover = this.UI.onmouseover = function() {
			clearTimeout(m.hideTimer)
		};
		m.subMenu = this
	}
};
iPopupMenu.prototype = {
	absPos : function(a) {
		return {
			x : $(a).offset().left,
			y : $(a).offset().top
		}
	},
	popUp : function(a, b) {
		this.show(a, b)
	},
	MUI : function(c, b, f, a, e) {
		var d = (b || document.body).appendChild(document.createElement(c));
		if (f) {
			d.id = f
		}
		if (a) {
			d.className = a
		}
		if (e) {
			d.innerHTML = e
		}
		if (c.toUpperCase() == "A") {
			d.setAttribute("href", "javascript:void(0)");
			d.onfocus = function() {
				this.blur()
			}
		}
		return d
	},
	show : function(g, e, i) {
		this.hide();
		var j = this.UI.offsetWidth, c = this.UI.offsetHeight, b = FUI.dom
				.getDimensions(window), a = b.clientWidth + b.scrollLeft, d = b.clientHeight
				+ b.scrollTop;
		if (c + e + 5 > d) {
			e = d - c - 1
		} else {
			if (i) {
				e -= 5
			}
		}
		if (j + g + 5 > a) {
			g -= j;
			if (i) {
				g -= i.offsetWidth - 5
			}
		} else {
			if (i) {
				g += 0
			}
		}
		var f = this.UI.style;
		f.top = e + "px";
		f.left = g + "px"
	},
	hide : function() {
		var b = this.UI.style, a = this.UI.getElementsByTagName("LI");
		b.left = "-9999em";
		b.top = "-9999em";
		for ( var c = 0; c < a.length; c++) {
			var b = a[c];
			b.className == this.classes.liOpen
					&& (b.className = this.classes.li);
			b.subMenu && b.subMenu.hide()
		}
	},
	click : function(a, b) {
	},
	setTargetEle : function(d) {
		this.targetEle = d;
		var a = this.UI.getElementsByTagName("LI");
		if (!a || a.length <= 0) {
			return false
		}
		var c;
		for ( var b = 0; b < a.length; b++) {
			c = a[b];
			if (c && c.subMenu) {
				c.subMenu.setTargetEle(d)
			}
		}
		c = null;
		a = null
	},
	classes : {
		bdy : "fui-imenu",
		ul : "imenu-ul",
		li : "imenu-li",
		liDis : "imenu-li imenu-dis",
		liOpen : "imenu-li imenu-open",
		liLine : "imenu-splitter",
		liTagLabel : "imenu-taglabel",
		liA : "imenu-a",
		liASub : "imenu-a imenu-sub",
		liLabel : "imenu-text",
		liHtml : "imenu-html",
		liIco : "imenu-ico",
		radioIco : "imenu-ico imenu-radio",
		checkboxIco : "imenu-ico imenu-checkbox",
		shadow : "imenu-shadow"
	}
};
var iMenu = {
	menuList : Array(),
	menuInit : false,
	viewPos : function(d, c) {
		var m = c.UI.offsetWidth, g = c.UI.offsetHeight, f = d.offsetWidth, b = d.offsetHeight, e = FUI.dom
				.getDimensions(window), a = e.clientWidth + e.scrollLeft, i = e.clientHeight
				+ e.scrollTop, k = FUI.dom.getPosition(d);
		var l = k.x, j = (k.y + b);
		if (l + m + 5 >= a) {
			l = l - m + f
		}
		if (j + g + 5 >= i) {
			var h = j - g - b - 1;
			if (h >= e.scrollTop) {
				j = h
			}
		}
		k.x = l;
		k.y = j;
		return k
	},
	init : function() {
		if (this.menuInit) {
			return false
		}
		var a = this;
		$(document).bind("mousedown", function() {
			a.hideMenu()
		});
		this.menuInit = true
	},
	enableMenu : function(b) {
		var a = document.getElementById(b);
		if (a) {
			a.disabled = false;
			a.className = iPopupMenu.prototype.classes.li
		}
		a = null
	},
	disableMenu : function(b) {
		var a = document.getElementById(b);
		if (a) {
			a.disabled = true;
			a.className = iPopupMenu.prototype.classes.liDis
		}
		a = null
	},
	hideMenu : function() {
		var b;
		for ( var a = 0; a < this.menuList.length; a++) {
			b = this.menuList[a];
			b && (b.hide())
		}
		b = null
	},
	makeMenu : function(a) {
		iMenu.init();
		var b = new iPopupMenu(a.items, null, null, a.width ? a.width : 150,
				a.theme);
		iMenu.menuList.push(b);
		return b
	},
	ContextMenu : function(a) {
		this.cm_prop = a;
		this.cm_menu = iMenu.makeMenu(a)
	},
	PopupMenu : function(a) {
		this.pp_prop = a;
		this.pp_menu = iMenu.makeMenu(a)
	}
};
iMenu.ContextMenu.prototype = {
	show : function(a, b) {
		if (!a) {
			alert("未发现有效的事件触发对象！");
			return false
		}
		b = b || window.event;
		FUI.event.cancelEvent(b);
		this.cm_menu.setTargetEle(a);
		var c = FUI.event.getPosition(b);
		this.cm_menu.popUp(c.x, c.y);
		c = null;
		if (this.cm_prop.rule && typeof this.cm_prop.rule == "function") {
			this.cm_prop.rule.call(a)
		}
		return false
	},
	hide : function() {
		if (this.cm_menu) {
			this.cm_menu.hide()
		}
	}
};
iMenu.PopupMenu.prototype = {
	show : function(a, b) {
		if (!a) {
			alert("未发现有效的事件触发对象！");
			return false
		}
		b = b || window.event;
		FUI.event.cancelEvent(b);
		this.pp_menu.setTargetEle(a);
		var c = iMenu.viewPos(a, this.pp_menu);
		this.pp_menu.popUp(c.x, c.y);
		if (this.pp_prop.rule && typeof this.pp_prop.rule == "function") {
			this.pp_prop.rule.call(a)
		}
		return false
	},
	hide : function() {
		if (this.pp_menu) {
			this.pp_menu.hide()
		}
	}
};
function _iToolBarUtil() {
	this.itoolbar_arr = []
}
_iToolBarUtil.prototype._reg = function(a) {
	this.itoolbar_arr.push(a)
};
_iToolBarUtil.prototype.click = function(a) {
	if (a && this.itoolbar_arr.length > 0) {
		this.itoolbar_arr[0].click(a)
	}
};
_iToolBarUtil.prototype.enable = function() {
	if (arguments.lenth <= 0) {
		return false
	}
	var c = Array.prototype.slice.apply(arguments);
	for ( var b = 0, a = this.itoolbar_arr.length; b < a; b++) {
		this.itoolbar_arr[b].enable(c)
	}
};
_iToolBarUtil.prototype.enableAll = function() {
	for ( var b = 0, a = this.itoolbar_arr.length; b < a; b++) {
		this.itoolbar_arr[b].enableAll()
	}
};
_iToolBarUtil.prototype.disable = function() {
	if (arguments.lenth <= 0) {
		return false
	}
	var c = Array.prototype.slice.apply(arguments);
	for ( var b = 0, a = this.itoolbar_arr.length; b < a; b++) {
		this.itoolbar_arr[b].disable(c)
	}
};
_iToolBarUtil.prototype.disableAll = function() {
	for ( var b = 0, a = this.itoolbar_arr.length; b < a; b++) {
		this.itoolbar_arr[b].disableAll()
	}
};
(function(a) {
	a.fn.iToolBar = function(b) {
		var c = new _iToolBarUtil();
		this.each(function(d) {
			if (!this.iToolBar) {
				this.iToolBar = new FUI.iToolBar(this, b, d + 1);
				this.appendChild(this.iToolBar.toHtml())
			}
			c._reg(this.iToolBar)
		});
		return c
	}
})(jQuery);
FUI.iToolBar = function(a, b, c) {
	b = b ? b : {};
	this.options = $.extend( {
		id : null,
		theme : "",
		items : []
	}, b);
	this.REG_OBJ = a;
	this.items = this.options.items;
	this.ITEMS_ARR = [];
	this._index = c ? c : 1
};
FUI.iToolBar.prototype.mergeItems = function() {
	var c = this.items, g = this, isHideParent = false;
	for ( var d = 0; d < c.length; d++) {
		var e = c[d];
		if(e.hidden === false){
			continue;
		}
		isHideParent = true;
		if (e.splitter === true) {
			g.ITEMS_ARR.push( [ "__GGP__", e ])
		} else {
			if (!e.group || "" == e.group) {
				g.ITEMS_ARR.push( [ "__DEF__", e ])
			} else {
				var f = e.group;
				var b = a(f);
				if (!b) {
					b = [];
					b[0] = f;
					g.ITEMS_ARR.push(b)
				}
				b.push(e);
				f = null
			}
		}
		e = null
	}
	if(isHideParent == false){
		$(this.REG_OBJ).parent().hide();
	}
	function a(m) {
		var j = false;
		for ( var l = 0, h = g.ITEMS_ARR.length; l < h; l++) {
			var k = g.ITEMS_ARR[l];
			if (k[0] == m) {
				j = k;
				break
			}
			k = null
		}
		return j
	}
};
FUI.iToolBar.prototype.createGrp = function() {
	var d = this.ITEMS_ARR;
	var h = document.createDocumentFragment(), f = null;
	try {
		if (d != null && d.length > 0) {
			for ( var b = 0, c = d.length; b < c; b++) {
				var g = d[b];
				if (g && g.length > 0) {
					f = document.createElement("div");
					if (g[0] == "__GGP__") {
						f.className = ("itbtn-grp-gap" + (g[1].cssClass ? " "
								+ g[1].cssClass : ""))
					} else {
						var k = "";
						f.className = "itbtn-grp";
						for ( var a = 1, e = g.length; a < e; a++) {
							if (a == 1) {
								k = "-gl"
							} else {
								if (a == e - 1) {
									k = "-gr"
								} else {
									k = "-gc"
								}
							}
							if (e == 2) {
								k = ""
							}
							f.appendChild(this.createItem(g[a], k))
						}
					}
					h.appendChild(f);
					f = null
				}
			}
		}
		return h
	} finally {
		f = null;
		h = null
	}
};
FUI.iToolBar.prototype.createItem = function(d, b) {
	var e = document.createElement("div");
	if (d.id) {
		e.id = d.id + (this._index <= 1 ? "" : "_" + this._index)
	}
	e.setAttribute("pos", b);
	var a = "itool-btn";
	if (d.theme) {
		a += " " + d.theme
	}
	a += " itbtn-dft" + b;
	if (d.icon || d.iconClass) {
		a += " itbtn-dft-icon"
	}
	if (d.disabled === true) {
		e.disabled = true;
		a += " itbtn-dft" + b + "-dis"
	}
	if (d.selected === true) {
		a += " itbtn-dft" + b + "-active"
	}
	if (d.hasSub === true) {
		a += " itbtn-dft" + b + "-pd"
	}
	if (d.radio) {
		e.setAttribute("radio", d.radio)
	}
	if (d.toggle) {
		e.setAttribute("toggle", d.toggle)
	}
	var c = [];
	try {
		e.className = a;
		c.push('<span class="itbtn-txt' + (!d.label ? " no-txt" : "") + '">');
		if (d.icon || d.iconClass) {
			c
					.push('<em class="itbtn-icon'
							+ (d.iconClass ? (" " + d.iconClass) : "")
							+ '"'
							+ (d.icon ? ' style="background:url(' + d.icon + ') no-repeat 0 50%;"'
									: "") + "></em>")
		}
		c.push((d.label ? d.label : "") + "</span>");
		if (d.hasSub) {
			c.push('<b class="itbtn-arr"></b>')
		}
		e.innerHTML = c.join("");
		e.onclick = (function(f) {
			return function(j) {
				if (this.disabled) {
					return false
				}
				var k = $(this);
				if (f.radio) {
					var g = k.attr("radio");
					var i = k.parent().find("div[radio='" + g + "']");
					i.each(function() {
						$(this).removeClass(
								"itbtn-dft" + $(this).attr("pos") + "-active");
						this.selected = false
					});
					k.addClass("itbtn-dft" + k.attr("pos") + "-active");
					k.removeClass("itbtn-dft" + k.attr("pos") + "-hover");
					f.selected = this.selected = true
				} else {
					if (f.toggle) {
						var h = (this.selected == true);
						if (h) {
							k.removeClass("itbtn-dft" + k.attr("pos")
									+ "-active")
						} else {
							k.addClass("itbtn-dft" + k.attr("pos") + "-active")
						}
						k.removeClass("itbtn-dft" + k.attr("pos") + "-hover");
						f.selected = this.selected = !h
					}
				}
				if (f.onclick && typeof f.onclick == "function") {
					f.onclick.call(this, f, (j ? j : window.event))
				}
			}
		})(d);
		e.onmouseover = function() {
			if (this.disabled == true || this.selected == true) {
				return false
			}
			var f = $(this);
			f.addClass("itbtn-dft" + (f.attr("pos")) + "-hover");
			f = null
		};
		e.onmouseout = function() {
			if (this.disabled == true) {
				return false
			}
			var f = $(this);
			f.removeClass("itbtn-dft" + (f.attr("pos")) + "-hover");
			f = null
		};
		e.onmousedown = function() {
			if (this.disabled == true) {
				return false
			}
			$(this).addClass("itbtn-dft" + $(this).attr("pos") + "-active")
		};
		e.onmouseup = function() {
			if (this.disabled == true) {
				return false
			}
			var f = $(this);
			f.removeClass("itbtn-dft" + f.attr("pos") + "-active");
			f = null
		};
		return e
	} finally {
		a = null;
		c = null;
		e = null
	}
};
FUI.iToolBar.prototype.toHtml = function() {
	this.mergeItems();
	var b = document.createElement("div"), a = null;
	b.oncontextmenu = function() {
		return true
	};
	if (document.all) {
		b.onselectstart = function() {
			return false
		}
	}
	try {
		if (this.options.id) {
			b.id = this.options.id
					+ (this._index <= 1 ? "" : "_" + this._index)
		}
		b.className = "fui-itoolbar"
				+ (this.options.theme ? (" " + this.options.theme) : "");
		b.appendChild(this.createGrp());
		a = document.createElement("div");
		a.style.cssText = "clear:both;font-size:0px;width:0;height:0;border:0 none;";
		b.appendChild(a);
		return b
	} finally {
		a = null;
		b = null
	}
};
FUI.iToolBar.prototype.click = function(c) {
	if (c) {
		var a = c + (this._index <= 1 ? "" : "_" + this._index);
		var b = document.getElementById(a);
		if (b && b.disabled != true) {
			b.onclick.apply(b, [ window.event ])
		}
		a = null;
		b = null
	}
};
FUI.iToolBar.prototype.enable = function(d) {
	if (!d || d.length <= 0) {
		return false
	}
	for ( var b = 0, a = d.length; b < a; b++) {
		var c = d[b] + (this._index <= 1 ? "" : "_" + this._index), e = $("#"
				+ c);
		if (e && e.length > 0) {
			e.removeClass("itbtn-dft" + e.attr("pos") + "-dis");
			e[0].disabled = false
		}
		c = null;
		e = null
	}
};
FUI.iToolBar.prototype.enableAll = function() {
	$("div.itool-btn", this.REG_OBJ).each(function() {
		this.disabled = false;
		var a = $(this);
		a.removeClass("itbtn-dft" + a.attr("pos") + "-dis");
		a = null
	})
};
FUI.iToolBar.prototype.disable = function(d) {
	if (!d || d.length <= 0) {
		return false
	}
	for ( var b = 0, a = d.length; b < a; b++) {
		var c = d[b] + (this._index <= 1 ? "" : "_" + this._index), e = $("#"
				+ c);
		if (e && e.length > 0) {
			e.addClass("itbtn-dft" + e.attr("pos") + "-dis");
			e[0].disabled = true
		}
		c = null;
		e = null
	}
};
FUI.iToolBar.prototype.disableAll = function() {
	$("div.itool-btn", this.REG_OBJ).each(function() {
		this.disabled = true;
		var a = $(this);
		a.addClass("itbtn-dft" + a.attr("pos") + "-dis");
		a = null
	})
};
(function(a) {
	a.fn.iTab = function(c) {
		var b = null;
		b = new FUI.iTab(this[0], c);
		b.initTab();
		return b
	}
})(jQuery);
FUI.iTab = function(c, b) {
	this.id = null;
	this.width = null;
	this.height = null;
	this.theme = "";
	this.items = [];
	this.scrollable = false;
	this.dblclickToClose = true;
	this.mouseWheel = false;
	this.triggerType = "mousedown";
	this.showLabelOnActive = false;
	this.showScrollHandler = true;
	this.tabType = "H";
	this.tabMaxSize = 50;
	this.tabMinSize = 1;
	this.maxChars = -1;
	this.hoverDelay = 200;
	this.scrollDelay = 120;
	this.wheelIncrement = 50;
	this.tabClick = function() {
	};
	this.tabClose = function() {
	};
	this.tabContextMenu = function() {
	};
	this.tabScrolling = null;
	this.target = c || null;
	this.tabItems = [];
	this.tabContainer = null;
	this.tabEdge = null;
	this.currActiveTab = null;
	this.scrolling = false;
	this.tabzIndex = 1000;
	this.activezIndex = 1001;
	this.hoverTimer = null;
	this.tabCounter = 0;
	if (b) {
		for ( var a in b) {
			this[a] = b[a]
		}
	}
	this.tabType = this.tabType.toUpperCase()
};
FUI.iTab.prototype = {
	initTab : function() {
		if (!this.target) {
			return false
		}
		var b = "V" == this.tabType ? "itab-vpanel" : "itab-hpanel";
		var j = '			<div id="itab-panel-{id}" class="'
				+ b
				+ '" unselectable="on">				'
				+ (function(i) {
					if (i.scrollable && i.showScrollHandler) {
						return '<a id="itab-scrollhlt-{id}" href="javascript:void(0);" class="itab-scroll-hdlt" style="display:none;"></a>'
					} else {
						return ""
					}
				})(this)
				+ '				<div id="itab-container-{id}" class="itab-container">					<div class="itab-list'
				+ (this.showLabelOnActive ? " itab-hidelabel" : "")
				+ '">						<div id="itab-edge-{id}" class="itab-edge"></div>					</div>				</div>				'
				+ (function(i) {
					if (i.scrollable && i.showScrollHandler) {
						return '<a id="itab-scrollhrb-{id}" href="javascript:void(0);" class="itab-scroll-hdrb" style="display:none;"></a>'
					} else {
						return ""
					}
				})(this) + "			</div>		";
		j = j.replace(/\{id\}/gm, this.id);
		this.target.innerHTML = j;
		this.tabHeader = $("#itab-panel-" + this.id);
		this.tabContainer = $("#itab-container-" + this.id);
		this.tabContainer.bind("contextmenu", {
			TabObj : this
		}, function(k) {
			var i = k.target || k.srcElement;
			while (i && !/itab-i/.test(i.className)) {
				i = i.parentNode
			}
			if (i) {
				k.data.TabObj.tabContextMenu.call(i, k)
			}
			return false
		});
		this.tabEdge = $("#itab-edge-" + this.id);
		if (this.theme) {
			this.tabHeader.addClass(this.theme)
		}
		if (this.tabType && this.tabType == "V") {
			var a = this.target.offsetHeight;
			if (typeof this.height == "function") {
				a = this.height()
			} else {
				if (this.height) {
					a = this.height
				}
			}
			a = parseInt(a, 10);
			this.tabHeader.css("height", a + "px");
			this.tabContainer.css("height", a + "px")
		} else {
			var d = null;
			if (typeof this.width == "function") {
				d = parseInt(this.width(), 10)
			} else {
				if (this.width) {
					d = parseInt(this.width, 10)
				}
			}
			if (null != d && d > 0) {
				this.tabHeader.css("width", d + "px");
				this.tabContainer.css("width", d + "px")
			}
		}
		if (this.scrollable && this.showScrollHandler) {
			this.scrollHandlt = $("#itab-scrollhlt-" + this.id);
			this.scrollHandrb = $("#itab-scrollhrb-" + this.id);
			this.scrollHandlt.bind("mousedown", {
				tabNav : this
			}, function(i) {
				i.data.tabNav.onScrollLeft()
			});
			this.scrollHandrb.bind("mousedown", {
				tabNav : this
			}, function(i) {
				i.data.tabNav.onScrollRight()
			})
		}
		var g = this;
		if (this.mouseWheel) {
			this.tabHeader.mousewheel(function(i, k) {
				g.onMouseWheel(i, k);
				FUI.event.stopEvent(i)
			})
		}
		this.scrollTo(0, false);
		if (this.items && this.items.length > 0) {
			var h = this.items;
			var e = null;
			for ( var c = 0, f = h.length; c < f; c++) {
				this.addTab(h[c], false);
				if (h[c].actived) {
					e = c
				}
			}
			if (e != null) {
				e = this.tabItems[e]
			} else {
				e = this.tabItems[this.tabItems.length - 1]
			}
			this.activeTab(e);
			e = null
		}
	},
	doTabClick : function(a, c) {
		if (a.disabled) {
			return false
		}
		this.setActiveTab(a);
		var b = c.data.item;
		if (b.onclick && typeof b.onclick == "function") {
			b.onclick.call(a, b, c)
		} else {
			this.tabClick.call(a, b, c)
		}
		b = null
	},
	onTabClick : function(b) {
		var a = b.data.tabNav;
		if ("mouseover" == b.type) {
			if (a.hoverTimer != null) {
				window.clearTimeout(a.hoverTimer);
				a.hoverTimer = null
			}
			var c = this;
			a.hoverTimer = window.setTimeout(function() {
				if (a.currActiveTab == c) {
					return false
				}
				a.doTabClick(c, b);
				c = null
			}, a.hoverDelay)
		} else {
			a.doTabClick(this, b)
		}
	},
	onCloseTab : function(c) {
		var b = c.data.item, a = c.data.tabNav;
		if (b.onclose && typeof b.onclose == "function") {
			b.onclose.call(document.getElementById(b.id), b, c)
		} else {
			a.tabClose.call(document.getElementById(b.id), b, c)
		}
		a.doCloseTab(document.getElementById(b.id))
	},
	doCloseTab : function(b) {
		if (!b
				|| b.disabled
				|| (this.tabItems.length <= this.tabMinSize && this.tabMinSize > 0)) {
			return false
		}
		this.tabItems.remove(b);
		var a = null, c = $(b);
		if (b == this.currActiveTab) {
			a = c.next("div.itab-i:first");
			if (!a || a.length == 0) {
				a = c.prev("div.itab-i:first")
			}
		}
		b.style.display = "none";
		c.unbind(this.triggerType, this.onTabClick);
		if (b.closable && this.dblclickToClose) {
			c.unbind("dblclick", this.onCloseTab)
		}
		if ("mouseover" == this.triggerType) {
			c.unbind("mouseout")
		}
		$("#itabClose-" + b.id).unbind("click", this.onCloseTab);
		b.innerHTML = "";
		FUI.dom.removeNode(b, document);
		b = null;
		c = null;
		FUI.browser.isIE && CollectGarbage();
		if (a && a.length > 0) {
			this.activeTab(a[0])
		}
		a = null;
		this.resetScrollArea()
	},
	setActiveTab : function(a) {
		if (this.currActiveTab) {
			$(this.currActiveTab).removeClass("itab-active");
			this.currActiveTab.style.zIndex = $(this.currActiveTab).attr(
					"zindex")
		}
		$(a).addClass("itab-active");
		a.style.zIndex = this.activezIndex;
		this.currActiveTab = a;
		this.scrollToTab(a)
	},
	lastTabSize : function() {
		var b = 80, a = null;
		a = this.tabContainer.find("div.itab-i:last");
		if (this.tabType == "H") {
			b = Math.max(a.width(), a.outerWidth(true))
		} else {
			b = Math.max(a.height(), a.outerHeight(true))
		}
		a = null;
		return b
	},
	resetScrollArea : function() {
		if (this.tabType == "H") {
			this._resetScrollHArea()
		} else {
			this._resetScrollVArea()
		}
	},
	_resetScrollHArea : function() {
		if (!this.scrollable) {
			return false
		}
		var d = this.tabItems.length, b = parseInt(this.tabHeader.css("width"),
				10), c = this.tabContainer, g = this.getScrollPos(), a = (this.tabEdge
				.offset().left - this.tabContainer.offset().left)
				+ g;
		if (d == 0 || a <= b) {
			c.scrollLeft(0).css("width", b);
			if (this.scrolling) {
				this.scrolling = false;
				this.tabHeader.removeClass("itab-hpanel-scrolling");
				if (this.scrollable && this.showScrollHandler) {
					this.scrollHandlt.css("display", "none");
					this.scrollHandrb.css("display", "none")
				}
				c.css("marginLeft", "0px")
			}
		} else {
			var e = 0, f = 0;
			if (this.scrollable && this.showScrollHandler) {
				e = parseInt(this.scrollHandlt.width());
				f = parseInt(this.scrollHandrb.width())
			}
			if (!this.scrolling) {
				this.tabHeader.addClass("itab-hpanel-scrolling")
			}
			b = b - (e + f);
			c.css("width", (b > 80 ? b : 80));
			if (!this.scrolling && this.scrollable && this.showScrollHandler) {
				this.scrollHandlt.css("display", "block");
				this.scrollHandrb.css("display", "block")
			}
			this.scrolling = true;
			if (g > (a - b)) {
				c.scrollLeft(a - b)
			} else {
				this.scrollToTab(this.currActiveTab, false)
			}
		}
		this.updateScrollHandlers()
	},
	_resetScrollVArea : function() {
		if (!this.scrollable) {
			return false
		}
		var f = this.tabItems.length, e = parseInt(
				this.tabHeader.css("height"), 10), d = this.tabContainer, g = this
				.getScrollPos(), c = (this.tabEdge.offset().top - this.tabContainer
				.offset().top)
				+ g;
		if (f == 0 || c <= e) {
			d.scrollTop(0).css("height", e);
			if (this.scrolling) {
				this.scrolling = false;
				this.tabHeader.removeClass("itab-vpanel-scrolling");
				if (this.scrollable && this.showScrollHandler) {
					this.scrollHandlt.css("display", "none");
					this.scrollHandrb.css("display", "none")
				}
				d.css("marginTop", "0px")
			}
		} else {
			var a = 0, b = 0;
			if (this.scrollable && this.showScrollHandler) {
				a = parseInt(this.scrollHandlt.height()) + 1;
				b = parseInt(this.scrollHandrb.height()) + 1
			}
			if (!this.scrolling) {
				this.tabHeader.addClass("itab-vpanel-scrolling")
			}
			e = e - (a + b);
			d.css("height", (e > 80 ? e : 80));
			if (!this.scrolling && this.scrollable && this.showScrollHandler) {
				this.scrollHandlt.css("display", "block");
				this.scrollHandrb.css("display", "block")
			}
			this.scrolling = true;
			if (g > (c - e)) {
				d.scrollTop(c - e)
			} else {
				this.scrollToTab(this.currActiveTab, false)
			}
		}
		this.updateScrollHandlers()
	},
	getScrollSize : function() {
		return this.tabType == "H" ? (this.tabEdge.offset().left
				- this.tabContainer.offset().left + this.getScrollPos())
				: (this.tabEdge.offset().top - this.tabContainer.offset().top + this
						.getScrollPos())
	},
	getScrollPos : function() {
		return (this.tabType == "H" ? parseInt(this.tabContainer.scrollLeft(),
				10) : parseInt(this.tabContainer.scrollTop(), 10))
	},
	getScrollArea : function() {
		return (this.tabType == "H" ? parseInt(this.tabContainer.css("width"),
				10) : parseInt(this.tabContainer.css("height"), 10))
	},
	scrollToTab : function(d) {
		if (!d) {
			return false
		}
		var c = d, h = this.getScrollPos(), e = this.getScrollArea(), g = (this.tabContainer
				.offset().left - $(c).offset().left)
				* -1 + h, b = g + c.offsetWidth, f = (this.tabContainer
				.offset().top - $(c).offset().top)
				* -1 + h, a = f + c.offsetHeight;
		if (this.tabType == "H") {
			if (g < h) {
				this.scrollTo(g, true)
			} else {
				if (b > (h + e)) {
					this.scrollTo((b - e), true)
				}
			}
		} else {
			if (f < h) {
				this.scrollTo(f, true)
			} else {
				if (a > (h + e)) {
					this.scrollTo((a - e), true)
				}
			}
		}
	},
	scrollTo : function(d, a) {
		var c = this;
		var b = this.tabType == "H" ? {
			left : d
		} : {
			top : d
		};
		this.tabContainer.stop().scrollTo(b, a ? this.scrollDelay : 0,
				function() {
					c.updateScrollHandlers()
				})
	},
	onMouseWheel : function(b, e) {
		var g = e * this.wheelIncrement * -1;
		FUI.event.stopEvent(b);
		var h = this.getScrollPos(), f = h + g, a = this.getScrollSize()
				- this.getScrollArea();
		var c = Math.max(0, Math.min(a, f));
		if (c != h) {
			this.scrollTo(c, false)
		}
	},
	updateScrollHandlers : function() {
		if (!this.scrollable) {
			return false
		}
		var c = this.getScrollPos();
		var b = (c > 0), a = c < (this.getScrollSize() - this.getScrollArea());
		if (this.showScrollHandler) {
			if (!b) {
				this.scrollHandlt.addClass("itab-scroll-hdlt-dis")
			} else {
				this.scrollHandlt.removeClass("itab-scroll-hdlt-dis")
			}
			if (!a) {
				this.scrollHandrb.addClass("itab-scroll-hdrb-dis")
			} else {
				this.scrollHandrb.removeClass("itab-scroll-hdrb-dis")
			}
		}
		if (this.tabScrolling && typeof this.tabScrolling == "function") {
			this.tabScrolling.call(this, b, a)
		}
	},
	addTab : function(e, c) {
		if (this.tabMaxSize > 0 && this.tabItems.length >= this.tabMaxSize) {
			alert("您打开的Tab项数量已经超过设置的 " + this.tabMaxSize
					+ " 个，\n为了您更方便的浏览已打开的Tab页，建议您关闭一些不用的Tab项，然后再打开！");
			return false
		}
		if (e.id && document.getElementById(e.id)) {
			this.activeTab(document.getElementById(e.id));
			return false
		}
		this.tabCounter = this.tabCounter + 1;
		if (!e.id) {
			e.id = "itab-" + this.id + "-" + this.tabCounter
		}
		var b = '<div class="itab-lt"><div class="itab-rb"><div class="itab-cm"><div class="itab-label">{#text#}</div>{#toolbar#}</div></div></div>';
		var f = $('<div id="' + e.id + '" title="' + (e.title ? e.title : "")
				+ '" class="itab-i" style="z-index:' + this.tabzIndex
				+ ';" zindex="' + this.tabzIndex + '"></div>');
		f.insertBefore(this.tabEdge);
		var a = "", d = "";
		if (e.icon || e.iconClass) {
			a = '<span class="itab-icon'
					+ (e.iconClass ? " " + e.iconClass : "")
					+ '"'
					+ (e.icon ? ' style="background:url(' + e.icon + ') no-repeat 50% 50%;"'
							: "") + "></span>"
		}
		if (e.label) {
			a += '<span class="itab-txt">'
					+ (this.maxChars > 0 ? (e.label).ellipsis(this.maxChars)
							: e.label) + "</span>"
		}
		if (e.closable || e.toolbar) {
			d = '<div class="itab-toolbar">'
					+ (e.toolbar ? e.toolbar : "")
					+ '<a id="itabClose-'
					+ e.id
					+ '" href="javascript:void(0);" class="itab-close"></a></div>'
		}
		b = b.replace(/\{#text#\}/gm, a).replace(/\{#toolbar#\}/gm, d);
		f.html(b);
		a = null;
		d = null;
		if (e.disabled) {
			f.addClass("itab-i-dis");
			f[0].disabled = true
		}
		f.bind(this.triggerType, {
			item : e,
			tabNav : this
		}, this.onTabClick);
		if (this.triggerType == "mouseover") {
			f.bind("mouseout", {
				tabNav : this
			}, function(h) {
				var g = h.data.tabNav;
				if (g.hoverTimer != null) {
					window.clearTimeout(g.hoverTimer);
					g.hoverTimer = null
				}
				g = null
			})
		}
		if (e.closable) {
			f[0].closable = true;
			f.addClass("itab-i-closable");
			$("#itabClose-" + e.id).bind("click", {
				item : e,
				tabNav : this
			}, this.onCloseTab).mousedown(function(g) {
				FUI.event.stopEvent(g);
				return false
			});
			if (this.dblclickToClose) {
				f.bind("dblclick", {
					item : e,
					tabNav : this
				}, this.onCloseTab)
			}
		}
		this.tabItems.push(f[0]);
		if (c !== false) {
			f.trigger(this.triggerType)
		}
		this.resetScrollArea();
		this.tabzIndex -= 1;
		f = null
	},
	setLabel : function(a, b) {
		$("#" + a).find("span.itab-txt:first").html(b)
	},
	closeTab : function(a) {
		if (a && typeof a == "string") {
			a = document.getElementById(a)
		}
		if (a && true === a.closable) {
			$("#itabClose-" + a.id).click()
		}
	},
	closeOther : function(a) {
		var d = this.tabContainer.find("div.itab-i-closable");
		if (d && d.length > 0) {
			var b = null;
			for ( var c = d.length - 1; c >= 0; --c) {
				b = d[c];
				if (b && b.id != a) {
					this.closeTab(b)
				}
				b = null
			}
			this.scrollTo(0, false);
			$("#" + a).trigger(this.triggerType)
		}
		d = null
	},
	closeAll : function() {
		var c = this.tabContainer.find("div.itab-i-closable");
		if (c && c.length > 0) {
			for ( var b = 0, a = c.length; b < a; b++) {
				this.closeTab(c[b])
			}
			this.scrollTo(0, false);
			this.tabContainer.find("div.itab-i:first")
					.trigger(this.triggerType)
		}
		c = null
	},
	activeTab : function(a) {
		if (a && typeof a == "string") {
			a = document.getElementById(a)
		}
		if (!a || a.disabled) {
			return false
		}
		$(a).trigger(this.triggerType)
	},
	activeFirstTab : function() {
		var a = this.tabContainer.find("div.itab-i:first");
		if (a && a.length > 0) {
			this.activeTab(a[0])
		}
		a = null
	},
	activeLastTab : function() {
		var a = this.tabContainer.find("div.itab-i:last");
		if (a && a.length > 0) {
			this.activeTab(a[0])
		}
		a = null
	},
	activePrevTab : function() {
		var a = $(this.currActiveTab).prev("div.itab-i:first");
		if (a && a.length > 0) {
			this.activeTab(a[0])
		}
		a = null
	},
	activeNextTab : function() {
		var a = $(this.currActiveTab).next("div.itab-i:first");
		if (a && a.length > 0) {
			this.activeTab(a[0])
		}
		a = null
	},
	onScrollLeft : function() {
		var b = this.getScrollPos(), a = Math.max(0, b - this.lastTabSize());
		if (a != b) {
			this.scrollTo(a, true)
		}
	},
	onScrollRight : function() {
		var a = this.getScrollSize() - this.getScrollArea(), c = this
				.getScrollPos(), b = Math.min(a, c + this.lastTabSize());
		if (b != c) {
			this.scrollTo(b, true)
		}
	},
	onScrollTop : function() {
		this.onScrollLeft()
	},
	onScrollBottom : function() {
		this.onScrollRight()
	},
	resizePanel : function(a) {
		if ("H" == this.tabType) {
			a = a > 80 ? a : 80;
			this.tabHeader.css("width", a + "px");
			this.tabContainer.css("width", a + "px")
		} else {
			this.tabHeader.css("height", a + "px");
			this.tabContainer.css("height", a + "px")
		}
		this.resetScrollArea()
	}
};
jQuery.fn.iPagination = function(a, c, b) {
	b = jQuery.extend( {
		pagesize : 30,
		num_display_entries : 5,
		current_page : 0,
		num_edge_entries : 2,
		link_to : "javascript:void(0);",
		link_class : "page-link",
		prev_text : "上一页",
		next_text : "下一页",
		prev_class : "page-prev",
		next_class : "page-next",
		ellipse_text : "...",
		ellipse_class : "page-ellipse",
		prev_show_always : true,
		next_show_always : true,
		pagination_class : "pagination",
		callback : function() {
			return false
		}
	}, b || {});
	if (!c || c <= 0) {
		c = 1
	}
	b.current_page = c - 1;
	return this.each(function() {
		function g() {
			return Math.ceil(a / b.pagesize)
		}
		function i() {
			var l = Math.ceil(b.num_display_entries / 2);
			var m = g();
			var k = m - b.num_display_entries;
			var n = h > l ? Math.max(Math.min(h - l, k), 0) : 0;
			var j = h > l ? Math.min(h + l, m) : Math.min(
					b.num_display_entries, m);
			return [ n, j ]
		}
		function f(k, j) {
			h = k;
			d();
			var l = b.callback(k + 1, b.pagesize);
			if (!l) {
				if (j.stopPropagation) {
					j.stopPropagation()
				} else {
					j.cancelBubble = true
				}
			}
			return l
		}
		function d() {
			e.empty();
			var k = i();
			var o = g();
			var p = function(q) {
				return function(r) {
					return f(q, r)
				}
			};
			var n = function(q, r) {
				q = q < 0 ? 0 : (q < o ? q : o - 1);
				r = jQuery.extend( {
					text : q + 1,
					classes : ""
				}, r || {});
				if (q == h) {
					var s = jQuery("<span class='current'>" + (r.text)
							+ "</span>")
				} else {
					var s = jQuery("<a>" + (r.text) + "</a>").bind("click",
							p(q)).attr(
							"href",
							b.link_to.replace("$page_no", q + 1).replace(
									"$page_size", b.pagesize))
				}
				if (r.classes) {
					s.addClass(r.classes)
				}
				e.append(s)
			};
			if (b.prev_text && (h > 0 || b.prev_show_always)) {
				n(h - 1, {
					text : b.prev_text,
					classes : b.prev_class
				})
			}
			if (k[0] > 0 && b.num_edge_entries > 0) {
				var j = Math.min(b.num_edge_entries, k[0]);
				for ( var l = 0; l < j; l++) {
					n(l, {
						classes : b.link_class
					})
				}
				if (b.num_edge_entries < k[0] && b.ellipse_text) {
					jQuery(
							'<span class="' + b.ellipse_class + '">'
									+ b.ellipse_text + "</span>").appendTo(e)
				}
			}
			for ( var l = k[0]; l < k[1]; l++) {
				n(l, {
					classes : b.link_class
				})
			}
			if (k[1] < o && b.num_edge_entries > 0) {
				if (o - b.num_edge_entries > k[1] && b.ellipse_text) {
					jQuery(
							'<span class="' + b.ellipse_class + '">'
									+ b.ellipse_text + "</span>").appendTo(e)
				}
				var m = Math.max(o - b.num_edge_entries, k[1]);
				for ( var l = m; l < o; l++) {
					n(l, {
						classes : b.link_class
					})
				}
			}
			if (b.next_text && (h < o - 1 || b.next_show_always)) {
				n(h + 1, {
					text : b.next_text,
					classes : b.next_class
				})
			}
		}
		var h = b.current_page;
		a = (!a || a < 0) ? 1 : a;
		b.pagesize = (!b.pagesize || b.pagesize < 0) ? 1 : b.pagesize;
		var e = jQuery(this);
		e.addClass(b.pagination_class);
		this.selectPage = function(j) {
			f(j)
		};
		this.prevPage = function() {
			if (h > 0) {
				f(h - 1);
				return true
			} else {
				return false
			}
		};
		this.nextPage = function() {
			if (h < g() - 1) {
				f(h + 1);
				return true
			} else {
				return false
			}
		};
		d()
	})
};
// (function(a){a.fn.iAutoSuggest=function(f,c){var
// i={id:false,searchTip:"请输入关键词查询",noResultsTip:"未匹配到任何数据",selectionLimitTip:"最多只能选择$1个数据",url:false,initData:{},selectedItemProp:"name",selectedValuesProp:"value",searchObjProps:"name",queryParam:"q",paramName:"",retrieveLimit:false,extraParams:"",matchCase:false,minChars:1,keyDelay:300,resultsHighlight:true,neverSubmit:false,selectionLimit:false,showResultList:true,start:function(){},selectionClick:function(j){},selectionAdded:function(j){},selectionRemoved:function(j){},formatList:false,beforeRetrieve:function(j){return
// j},retrieveComplete:function(j){return
// j},resultClick:function(j){},resultsComplete:function(){}};var
// e=a.extend(i,c);var d="object";var h=0;if(e.url){d="99";var b=e.url}else{var
// g=f;for(k in f){if(f.hasOwnProperty(k)){h++}}}return
// this.each(function(y){if(!e.id){y=y+""+Math.floor(Math.random()*1000);e.id=y;var
// l="as-input-"+y}else{y=e.id;var l=y}e.start.call(this);var A=a(this);var
// B=Math.max(A.outerWidth(),parseInt(A.css("width")));A.attr("autocomplete","off").addClass("as-input
// as-input-nul").attr("name","as_name_"+l).attr("id",l).attr("style","background:none;border:0
// none;").val("");var G=false;A.wrap('<ul class="as-selections"
// id="as-selections-'+y+'" style="width:'+B+'px;"></ul>').wrap('<li
// class="as-original" id="as-original-'+y+'"></li>');var
// H=a("#as-selections-"+y);var u=a("#as-original-"+y);var n=a('<div
// class="as-results" id="as-results-'+y+'"></div>').hide();var o=a('<ul
// class="as-list"></ul>');var L=a('<input type="hidden" class="as-values"
// id="as-values-'+y+'" name="as_val_'+y+'" />');var t="";H.append('<li
// id="as-input-tip-'+y+'" class="as-input-tip">'+e.searchTip+"</li>");if(typeof
// e.initData=="string"){var C=e.initData.split(",");for(var
// I=0;I<C.length;I++){var
// m={};m[e.selectedValuesProp]=C[I];if(C[I]!=""){J(m,"000"+I)}}t=e.initData}else{t="";var
// M=0;for(k in
// e.initData){if(e.initData.hasOwnProperty(k)){M++}}if(M>0){a("#as-input-tip-"+y).hide();for(var
// I=0;I<M;I++){var
// D=e.initData[I][e.selectedValuesProp];if(D==undefined){D=""}t=t+D+",";if(D!=""){J(e.initData[I],"000"+I)}}}}if(t!=""){A.val("");var
// z=t.substring(t.length-1);if(z!=","){t=t+","}a("li.as-selection-item",H).addClass("blur").removeClass("selected")}A.after(L);H.click(function(){G=true;A.focus()}).mousedown(function(){G=false}).after(n);var
// w=null;var E="";var p=0;var
// q=false;A.focus(function(){if(a(this).val()==""&&L.val()==""){a("#as-input-tip-"+y).hide()}else{if(G){a("li.as-selection-item",H).removeClass("blur");if(a(this).val()!=""){o.css("width",H.outerWidth()-2);n.show()}}}G=true;return
// true}).blur(function(){if(a(this).val()==""&&L.val()==""&&t==""){a("#as-input-tip-"+y).show()}else{if(G){a("li.as-selection-item",H).addClass("blur").removeClass("selected");n.hide()}}}).keydown(function(R){lastKeyPressCode=R.keyCode;first_focus=false;switch(R.keyCode){case
// 38:R.preventDefault();v("up");break;case
// 40:R.preventDefault();v("down");break;case 8:if(A.val()==""){var
// N=L.val().split(",");N=N[N.length-1];H.children().not(u.prev()).removeClass("selected");if(u.prev().hasClass("selected")){K.call(this,u.prev(),e)}else{e.selectionClick.call(this,u.prev());u.prev().addClass("selected")}}if(A.val().length==1){n.hide();E=""}if(a(":visible",n).length>0){if(w){clearTimeout(w)}w=setTimeout(function(){j()},e.keyDelay)}break;case
// 9:case 188:if(d!="99"){q=true;var
// P=A.val().replace(/(,)/g,"");if(P!=""&&P.length>=e.minChars){R.preventDefault();var
// O={};O[e.selectedItemProp]=P;O[e.selectedValuesProp]=P;var
// x=a("li",H).length;J(O,"00"+(x+1));A.val("")}}case 13:q=false;var
// Q=a("li.active:first",n);if(Q.length>0){Q.click();n.hide()}if(e.neverSubmit||Q.length>0){R.preventDefault()}break;default:if(e.showResultList){if(e.selectionLimit&&a("li.as-selection-item",H).length>=e.selectionLimit){o.html('<li
// class="as-message">'+e.selectionLimitTip.replace("$1",e.selectionLimit)+"</li>");n.show()}}break}});A.bind((a.browser.msie?"propertychange":"input"),function(x){lastKeyPressCode=x.keyCode;first_focus=false;if(w){window.clearTimeout(w);w=null}w=window.setTimeout(function(){j()},e.keyDelay)});function
// j(){if(e.selectionLimit&&a("li.as-selection-item",H).length>=e.selectionLimit){return}if(lastKeyPressCode==46||(lastKeyPressCode>8&&lastKeyPressCode<32)){return
// n.hide()}var
// N=A.val().replace(/[\\]+|[\/]+/g,"");if(N==E){return}E=N;if(N.length>=e.minChars){if(d=="99"){H.addClass("loading");var
// x="";if(e.retrieveLimit){x="&limit="+encodeURIComponent(e.retrieveLimit)}if(e.beforeRetrieve){N=e.beforeRetrieve.call(this,N)}var
// O=typeof
// e.extraParams=="function"?e.extraParams.call(this,N):e.extraParams;if(O&&O!=""){O="&"+O}else{O=""}a.getJSON(b+(b.indexOf("?")!=-1?"&":"?")+e.queryParam+"="+encodeURIComponent(N)+x+O,function(Q){h=0;var
// P=e.retrieveComplete.call(this,Q);for(k in
// P){if(P.hasOwnProperty(k)){h++}}r(P,N)})}else{if(e.beforeRetrieve){N=e.beforeRetrieve.call(this,N)}r(g,N)}}else{H.removeClass("loading");n.hide()}}var
// s=0;function r(P,V){if(!e.matchCase){V=V.toLowerCase()}var
// X=0;n.html(o.html("")).hide();for(var Q=0;Q<h;Q++){var R=Q;s++;var
// S=false;if(e.searchObjProps=="value"){var T=P[R].value}else{var T="";var
// U=e.searchObjProps.split(",");for(var W=0;W<U.length;W++){var
// x=a.trim(U[W]);T=T+P[R][x]+"
// "}}if(T){if(!e.matchCase){T=T.toLowerCase()}S=true}if(S){var O=a('<li
// class="as-result-item" id="as-result-item-'+R+'"></li>').click(function(){var
// ab=a(this).data("data");var
// Z=ab.num;if(a("#as-selection-"+Z,H).length<=0&&!q){var
// aa=ab.attributes;A.val("").focus();E="";J(aa,Z);e.resultClick.call(this,ab);n.hide()}q=false}).mousedown(function(){G=false}).mouseover(function(){a("li",o).removeClass("active");a(this).addClass("active")}).data("data",{attributes:P[R],num:s});var
// Y=a.extend({},P[R]);if(!e.matchCase){var N=new
// RegExp("(?![^&;]+;)(?!<[^<>]*)("+V+")(?![^<>]*>)(?![^&;]+;)","gi")}else{var
// N=new
// RegExp("(?![^&;]+;)(?!<[^<>]*)("+V+")(?![^<>]*>)(?![^&;]+;)","g")}if(e.resultsHighlight){Y[e.selectedItemProp]=Y[e.selectedItemProp].replace(N,"<em>$1</em>")}if(!e.formatList){O=O.html(Y[e.selectedItemProp])}else{O=e.formatList.call(this,Y,O)}o.append(O);delete
// Y;X++;if(e.retrieveLimit&&e.retrieveLimit==X){break}}}H.removeClass("loading");if(X<=0&&d=="99"){o.html('<li
// class="as-message">'+e.noResultsTip+"</li>")}o.css("width",H.outerWidth()-2);n.show();e.resultsComplete.call(this)}function
// F(x){var
// N=[];a(":hidden[name="+x.paramName+"]").each(function(){N.push(this.value)});a("#as-values-"+x.id).val(N.join(","))}function
// J(P,x){var
// O=a(":hidden[name="+e.paramName+"][value='"+P[e.selectedValuesProp]+"']");if(O.size()>0){O.parent().addClass("selected");O=null;return
// false}var N=a('<li class="as-selection-item"
// id="as-selection-'+x+'"></li>').click(function(){e.selectionClick.call(this,P);H.children().removeClass("selected");a(this).addClass("selected")}).mousedown(function(){G=false});var
// Q=a('<a
// class="as-close">&times;</a>').click(function(){K.call(this,N,e);G=true;A.focus();return
// false});u.before(N.html('<input type="hidden" name="'+e.paramName+'"
// value="'+P[e.selectedValuesProp]+'"/>'+P[e.selectedItemProp]).append(Q));e.selectionAdded.call(this,P);F(e)}function
// K(N,x){N.remove();var
// O=[];a(":hidden[name="+x.paramName+"]").each(function(){O.push(this.value)});a("#as-values-"+x.id).val(O.join(","));x.selectionRemoved.call(this,{})}function
// v(O){if(a(":visible",n).length>0){var x=a("li",n);if(O=="down"){var
// P=x.eq(0)}else{var P=x.filter(":last")}var
// N=a("li.active:first",n);if(N.length>0){if(O=="down"){P=N.next()}else{P=N.prev()}}x.removeClass("active");P.addClass("active")}}})}})(jQuery);
(function(b) {
	b.fn.iAutoSuggest = function(e, c) {
		var f = {
			id : false,
			searchTip : "请输入关键词查询",
			noResultsTip : "未匹配到任何数据",
			selectionLimitTip : "最多只能选择$1个数据",
			url : false,
			initData : {},
			selectedItemProp : "name",
			selectedValueProp : "value",
			searchObjProps : "name",
			queryParam : "q",
			paramName : "",
			retrieveLimit : false,
			extraParams : "",
			matchCase : false,
			minChars : 1,
			keyDelay : 300,
			resultsHighlight : true,
			neverSubmit : false,
			selectionLimit : false,
			showResultList : true,
			start : function() {
			},
			selectionClick : function(g) {
			},
			selectionAdded : function(g) {
			},
			selectionRemoved : function(g) {
			},
			formatList : false,
			beforeRetrieve : function(g) {
				return g
			},
			retrieveComplete : function(g) {
				return g
			},
			resultClick : function(g) {
			},
			resultsComplete : function() {
			}
		};
		if (this.size() == 0) {
			return null
		}
		var d = b.extend(f, c);
		return new a(this.get(0), d, e)
	};
	var a = function(j, u, L) {
		var z = "object", f = 0;
		if (u.url) {
			z = "99";
			var y = u.url
		} else {
			var e = L;
			for (k in L) {
				if (L.hasOwnProperty(k)) {
					f++
				}
			}
		}
		var B = Math.floor(Math.random() * 100);
		if (!u.id) {
			B = B + "" + Math.floor(Math.random() * 1000);
			u.id = B;
			var M = "as-input-" + B
		} else {
			B = u.id;
			var M = B
		}
		u.start.call(j);
		var d = b(j);
		var l = Math.max(d.outerWidth(), parseInt(d.css("width")));
		j.className = "";
		j.value = "";
		d.addClass("as-input").attr("autocomplete", "off").attr("name",
				"as_name_" + M).attr("id", M).attr("style", "");
		var A = false;
		d.wrap(
				'<ul class="as-selections" id="as-selections-' + B
						+ '" style="width:' + l + 'px;"></ul>').wrap(
				'<li class="as-original" id="as-original-' + B + '"></li>');
		var m = b("#as-selections-" + B);
		var c = b("#as-original-" + B);
		var N = b('<div class="as-results" id="as-results-' + B + '" style="display:none;"></div>');
		var I = b('<ul class="as-list"></ul>');
		var s = b('<input type="hidden" class="as-values" id="as-values-' + B
				+ '" name="as_val_' + B + '" />');
		var r = "";
		var D = b('<li class="as-input-tip">' + u.searchTip + "</li>");
		m.append(D);
		if (typeof u.initData == "string") {
			var C = u.initData.split(",");
			for ( var J = 0; J < C.length; J++) {
				var E = {};
				E[u.selectedValueProp] = C[J];
				if (C[J] != "") {
					F(E, "000" + J)
				}
			}
			r = u.initData
		} else {
			r = "";
			var t = 0;
			for (k in u.initData) {
				if (u.initData.hasOwnProperty(k)) {
					t++
				}
			}
			if (t > 0) {
				D.hide();
				for ( var J = 0; J < t; J++) {
					var p = u.initData[J][u.selectedValueProp];
					if (p == undefined) {
						p = ""
					}
					r = r + p + ",";
					if (p != "") {
						F(u.initData[J], "000" + J)
					}
				}
			}
		}
		if (r != "") {
			d.val("");
			var K = r.substring(r.length - 1);
			if (K != ",") {
				r = r + ","
			}
			b("li.as-selection-item", m).addClass("blur").removeClass(
					"selected")
		}
		d.after(s);
		m.click(function() {
			A = true;
			d.focus()
		}).mousedown(function() {
			A = false
		}).after(N);
		var G = null;
		var v = "";
		var h = 0;
		var O = false;
		d
				.focus(function() {
					if (b(this).val() == "" && s.val() == "") {
						D.hide()
					} else {
						if (A) {
							b("li.as-selection-item", m).removeClass("blur");
							if (b(this).val() != "") {
								I.css("width", m.outerWidth() - 2);
								N.show()
							}
						}
					}
					A = true;
					return true
				})
				.blur(
						function() {
							if (b(this).val() == "" && s.val() == "" && r == "") {
								D.show()
							} else {
								if (A) {
									b("li.as-selection-item", m).addClass(
											"blur").removeClass("selected");
									N.hide()
								}
							}
						})
				.keydown(
						function(S) {
							lastKeyPressCode = S.keyCode;
							first_focus = false;
							switch (S.keyCode) {
							case 38:
								S.preventDefault();
								q("up");
								break;
							case 40:
								S.preventDefault();
								q("down");
								break;
							case 8:
								if (d.val() == "") {
									var x = s.val().split(",");
									x = x[x.length - 1];
									m.children().not(c.prev()).removeClass(
											"selected");
									if (c.prev().hasClass("selected")) {
										H.call(this, c.prev(), u)
									} else {
										u.selectionClick.call(this, c.prev());
										c.prev().addClass("selected")
									}
								}
								if (d.val().length == 1) {
									N.hide();
									v = ""
								}
								if (b(":visible", N).length > 0) {
									if (G) {
										clearTimeout(G)
									}
									G = setTimeout(function() {
										n()
									}, u.keyDelay)
								}
								break;
							case 9:
							case 188:
								if (z != "99") {
									O = true;
									var Q = d.val().replace(/(,)/g, "");
									if (Q != "" && Q.length >= u.minChars) {
										S.preventDefault();
										var P = {};
										P[u.selectedItemProp] = Q;
										P[u.selectedValueProp] = Q;
										var i = b("li", m).length;
										F(P, "00" + (i + 1));
										d.val("")
									}
								}
							case 13:
								O = false;
								var R = b("li.active:first", N);
								if (R.length > 0) {
									R.click();
									N.hide()
								}
								if (u.neverSubmit || R.length > 0) {
									S.preventDefault()
								}
								break;
							default:
								if (u.showResultList) {
									if (u.selectionLimit
											&& b("li.as-selection-item", m).length >= u.selectionLimit) {
										I.html('<li class="as-message">'
												+ u.selectionLimitTip.replace(
														"$1", u.selectionLimit)
												+ "</li>");
										N.show()
									}
								}
								break
							}
						});
		d.bind((b.browser.msie ? "propertychange" : "input"), function(i) {
			lastKeyPressCode = i.keyCode;
			first_focus = false;
			if (G) {
				window.clearTimeout(G);
				G = null
			}
			G = window.setTimeout(function() {
				n()
			}, u.keyDelay)
		});
		function n() {
			if (u.selectionLimit
					&& b("li.as-selection-item", m).length >= u.selectionLimit) {
				return
			}
			if (lastKeyPressCode == 46
					|| (lastKeyPressCode > 8 && lastKeyPressCode < 32)) {
				return N.hide()
			}
			var x = d.val().replace(/[\\]+|[\/]+/g, "");
			if (x == v) {
				return
			}
			v = x;
			if (x.length >= u.minChars) {
				if (z == "99") {
					m.addClass("loading");
					var i = "";
					if (u.retrieveLimit) {
						i = "&limit=" + encodeURIComponent(u.retrieveLimit)
					}
					if (u.beforeRetrieve) {
						x = u.beforeRetrieve.call(this, x)
					}
					var P = typeof u.extraParams == "function" ? u.extraParams
							.call(this, x) : u.extraParams;
					if (P && P != "") {
						P = "&" + P
					} else {
						P = ""
					}
					b.getJSON(y + (y.indexOf("?") != -1 ? "&" : "?")
							+ u.queryParam + "=" + encodeURIComponent(x) + i
							+ P, function(Q) {
						w(Q, x)
					})
				} else {
					if (u.beforeRetrieve) {
						x = u.beforeRetrieve.call(this, x)
					}
					w(e, x)
				}
			} else {
				m.removeClass("loading");
				N.hide()
			}
		}
		var g = 0;
		function w(R, aa) {
			if (!u.matchCase) {
				aa = aa.toLowerCase()
			}
			var Z = 0;
			N.html(I.html("")).hide();
			R = R instanceof Array ? R : [ R ];
			for ( var S = 0, V = R.length; S < V; ++S) {
				var T = S;
				g++;
				var U = false;
				if (u.searchObjProps == "value") {
					var X = R[T].value
				} else {
					var X = "";
					var W = u.searchObjProps.split(",");
					for ( var Y = 0; Y < W.length; Y++) {
						var x = b.trim(W[Y]);
						X = X + R[T][x] + " "
					}
				}
				if (X) {
					if (!u.matchCase) {
						X = X.toLowerCase()
					}
					U = true
				}
				if (U) {
					var Q = b(
							'<li class="as-result-item" id="as-result-item-' + T + '"></li>')
							.click(function() {
								var ad = b(this).data("data");
								var i = ad.num;
								if (!O) {
									var ac = ad.attributes;
									d.val("").focus();
									v = "";
									F(ac, i);
									u.resultClick.call(this, ad);
									N.hide()
								}
								O = false
							}).mousedown(function() {
								A = false
							}).mouseover(function() {
								b("li", I).removeClass("active");
								b(this).addClass("active")
							}).data("data", {
								attributes : R[T],
								num : g
							});
					var ab = b.extend( {}, R[T]);
					var P = new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + aa
							+ ")(?![^<>]*>)(?![^&;]+;)", u.matchCase ? "g"
							: "gi");
					if (u.resultsHighlight) {
						ab[u.selectedItemProp] = ab[u.selectedItemProp]
								.replace(P, "<em>$1</em>")
					}
					if (!u.formatList) {
						Q = Q.html(ab[u.selectedItemProp])
					} else {
						Q = u.formatList.call(this, ab, Q)
					}
					I.append(Q);
					delete ab;
					Z++;
					if (u.retrieveLimit && u.retrieveLimit == Z) {
						break
					}
				}
			}
			m.removeClass("loading");
			if (Z <= 0 && z == "99") {
				I.html('<li class="as-message">' + u.noResultsTip + "</li>")
			}
			I.css("width", m.outerWidth() - 2);
			N.show();
			u.resultsComplete.call(this)
		}
		function o(i) {
			var x = [];
			b(":hidden[name=" + i.paramName + "]").each(function() {
				x.push(this.value)
			});
			b("#as-values-" + i.id).val(x.join(","))
		}
		function F(Q, i) {
			var P = b(":hidden[name=" + u.paramName + "][value='"
					+ Q[u.selectedValueProp] + "']");
			if (P.size() > 0) {
				P.parent().addClass("selected");
				P = null;
				return false
			}
			var x = b(
					'<li class="as-selection-item" id="as-selitm-' + Q[u.selectedValueProp] + '"></li>')
					.click(function() {
						u.selectionClick.call(this, Q);
						m.children().removeClass("selected");
						b(this).addClass("selected")
					}).mousedown(function() {
						A = false
					});
			var R = b('<a class="as-close">&times;</a>').click(function() {
				H.call(this, x, u);
				A = true;
				d.focus();
				return false
			});
			c.before(x.html(
					'<input type="hidden" name="' + u.paramName + '" value="'
							+ Q[u.selectedValueProp] + '"/>'
							+ Q[u.selectedItemProp]).append(R));
			u.selectionAdded.call(this, Q);
			o(u)
		}
		function H(x, i) {
			x.remove();
			var P = [];
			b(":hidden[name=" + i.paramName + "]").each(function() {
				P.push(this.value)
			});
			b("#as-values-" + i.id).val(P.join(","));
			i.selectionRemoved.call(this, {})
		}
		function q(P) {
			if (b(":visible", N).length > 0) {
				var i = b("li", N);
				if (P == "down") {
					var Q = i.eq(0)
				} else {
					var Q = i.filter(":last")
				}
				var x = b("li.active:first", N);
				if (x.length > 0) {
					if (P == "down") {
						Q = x.next()
					} else {
						Q = x.prev()
					}
				}
				i.removeClass("active");
				Q.addClass("active")
			}
		}
		this.addItem = function(Q) {
			D.hide();
			var R = m.find("li.as-selection-item").length;
			if (u.selectionLimit > 0 && R >= u.selectionLimit) {
				return
			}
			if (Q instanceof Array && Q.length > 0) {
				if (u.selectionLimit > 0) {
					for ( var P = 0, x = Math.min(Q.length, u.selectionLimit
							- R); P < x; ++P) {
						F(Q[P], Math.floor(Math.random() * 1000))
					}
				} else {
					for ( var P = 0, x = Q.length; P < x; ++P) {
						F(Q[P], Math.floor(Math.random() * 1000))
					}
				}
			} else {
				F(Q, Math.floor(Math.random() * 1000))
			}
		};
		this.removeItem = function(i) {
			H(b("#as-selitm-" + i), u)
		};
		this.removeAll = function() {
			m.find("li.as-selection-item").remove();
			s.val("");
			d.val("");
			D.show()
		}
	}
})(jQuery);
(function(a) {
	a.fn.iSelect = function() {
		return this.each(function() {
			var b = new a.iSelect(this);
			b = null
		})
	};
	a.iSelect = function(h) {
		if (!h || h.multiple) {
			return false
		}
		var j = h.id;
		if (!j || j === "" || typeof j == "undefined") {
			j = "iSel-" + Math.round(Math.random() * 10000);
			h.id = j
		}
		if (document.getElementById("_iSelWrap_" + j)) {
			return false
		}
		var d = a(h);
		var i = Math.max(d.outerWidth(), parseInt(d.css("width")));
		d.addClass("iselect").css("width", "100%");
		var e = h.onchange;
		if (i >= 500) {
			i = 500
		}
		var g = '<div id="_iSelWrap_' + j
				+ '" class="iselect-wrapper" style="width:' + i + 'px;"></div>';
		var b = '<span id="_iSelVal_' + j
				+ '" class="iselwrap-val" style="width:' + (i - 20)
				+ 'px;"></span>';
		d.wrap(g);
		a("#_iSelWrap_" + j).append(b);
		var c = a("#_iSelVal_" + j);
		h.onchange = null;
		d.change(function() {
			var k = this.options[this.selectedIndex].text;
			c.text(k);
			if (e && typeof e != "undefined") {
				e.apply(this)
			}
		});
		var f = d.find("option:selected").first();
		if (f.length <= 0) {
			f = d.find("option:first")
		}
		c.text(f.text())
	}
})(jQuery);
window.focus();
$(document)
		.bind(
				"keydown",
				function(evt) {
					evt = evt ? evt : window.event;
					if (evt.keyCode == 8) {
						var el = evt.srcElement || evt.target;
						var rdonly = $(el).attr("readonly"), disd = $(el).attr(
								"disabled");
						rdonly = (rdonly === true || rdonly == 'readonly') ? true
								: false;
						disd = (disd === true || disd == 'disabled') ? true
								: false;
						if ((el.type != "text" && el.type != "textarea" && el.type != "password" && el.type != "number" )
								|| rdonly || disd) {
							FUI.event.stopEvent(evt);
						}
					}
				});