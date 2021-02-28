function getI18NString(t) {
    if (!i18n[lang]) return t;
    var e = i18n[lang][t];
    return void 0 === e ? t: e
}
var i18n = {
    "zh-cn": {
        Name: "名称",
        "Render Color": "渲染色",
        Border: "边框",
        "Border Color": "边框颜色",
        Location: "坐标",
        Size: "尺寸",
        Rotate: "旋转",
        "Label Color": "文本颜色",
        "Background Color": "背景色",
        "Font Size": "字体大小",
        "json file is empty": "JSON文件为空",
        "Save Error": "保存错误",
        "Save Success": "保存成功",
        Update: "更新",
        Submit: "提交",
        "Export JSON": "导出JSON",
        "Load File ...": "背景上传",
        "Download File": "下载文件",
        Save: "保存",
        Rename: "重命名",
        "Input Element Name": "输入图元名称",
        "Solid Line": "实线样式",
        "Dashed Line": "虚线样式",
        "Line Width": "连线宽度",
        "Input Line Width": "输入连线宽度",
        "Line Color": "连线颜色",
        "Input Line Color": "输入连线颜色",
        "Out of Group": "脱离分组",
        "Send to Top": "置顶显示",
        "Send to Bottom": "置底显示",
        "Reset Layer": "恢复默认层",
        "Clear Graph": "清空画布",
        "Zoom In": "放大",
        "Zoom Out": "缩小",
        "1:1": "1:1",
        "Pan Mode": "平移模式",
        "Rectangle Select": "框选模式",
        Text: "文字",
        "Basic Nodes": "基本节点",
        "Register Images": "注册图片",
        "Default Shapes": "默认形状"
    }
},
lang = navigator.language.toLowerCase(); !
function(t, e) {
    var n = function(t) {
        t = t || {};
        var n = document.createElement(t.tagName || "div");
        return t["class"] && e(n).addClass(t["class"]),
        t.parent && t.parent.appendChild(n),
        t.style && n.setAttribute("style", t.style),
        t.name && n.setAttribute("name",t.name),
        t.css && e(n).css(t.css),
        t.html && e(n).html(t.html),
    
        
        n
    };
    t.createElement = n
} (Q, jQuery),
!
function(t, e) {
    function n(t) {
        t = t || window.event;
        var e = t.dataTransfer,
        n = t.target;
        e.setData("text", n.getAttribute(r))
    }
    function i(t, e, n, i) {
        var r = document.createElement("img");
        return r.src = e,
        r.setAttribute("title", n),
        i = i || {},
        i.label = i.label || n,
        i.title = n,
        i.image || i.type && "Node" != i.type || (i.image = e),
        o(r, i),
        t.appendChild(r),
        r
    }
    function o(e, i) {
        return e.setAttribute("draggable", "true"),
        e.setAttribute(r, t.exportJSON ? t.exportJSON(i, !0) : JSON.stringify(i)),
        e.ondragstart = n,
        e
    }
    var r = "draginfo",
    a = /MSIE 9/i.test(navigator.userAgent) || /MSIE 10/i.test(navigator.userAgent),
    s = !a;
    if (!s) {
        var l = {},
        h = function(t) {
            return {
                x: t.pageX,
                y: t.pageY
            }
        },
        u = document.documentElement,
        d = function() {
            u.addEventListener("mousemove",
            function(e) {
                if (l.target) {
                    t.stopEvent(e);
                    var n = h(e);
                    if (!l.dragElement) {
                        var i = l.target;
                        if (Math.abs(n.x - l.dragPoint.x) <= 5 || Math.abs(n.y - l.dragPoint.y) <= 5) return;
                        var o = document.createElement("div");
                        o.style.position = "absolute",
                        o.style.zIndex = 1e4;
                        var r = i.cloneNode(!0);
                        /canvas/i.test(r.tagName) ? r.getContext("2d").drawImage(i, 0, 0) : (o.style.maxWidth = "30px", o.style.maxWidth = "30px", o.style.cursor = "move"),
                        r.id = null,
                        o.appendChild(r),
                        u.appendChild(o),
                        l.dragElement = o;
                        var a = {
                            target: i
                        };
                        i.ondragstart instanceof Function && (l.dataTransfer = a.dataTransfer = {
                            datas: {},
                            setData: function(t, e) {
                                this.datas[t] = e
                            },
                            getData: function(t) {
                                return this.datas[t]
                            }
                        },
                        i.ondragstart(a))
                    }
                    console.log(n.x - l.dragElement.clientWidth / 2  );
                    l.dragElement.style.left = n.x - l.dragElement.clientWidth / 2 + "px",
                    l.dragElement.style.top = n.y - l.dragElement.clientHeight / 2 + "px"
                }
            },
            !1),
            u.addEventListener("mouseup",
            function(t) {
                if (l.target) {
                    delete l.dragPoint,
                    delete l.target,
                    l.dragElement && (u.removeChild(l.dragElement), delete l.dragElement);
                    for (var n = h(t), i = e(".Q-Graph"), o = 0; o < i.length;) {
                        var r = i[o]; ++o;
                        var a = f(r);
                        if (c(a, n)) {
                            r.ondrop instanceof Function && (t.dataTransfer = l.dataTransfer, r.ondrop(t));
                            break
                        }
                    }
                    delete l.dataTransfer
                }
            },
            !1)
        },
        c = function(t, e) {
            return e.x >= t.x && e.x <= t.x + t.width && e.y >= t.y && e.y <= t.y + t.height
        },
        p = function(t) {
            for (var e = 0,
            n = 0; t.offsetParent;) e += t.clientLeft + t.offsetLeft - t.scrollLeft,
            n += t.clientTop + t.offsetTop - t.scrollTop,
            t = t.offsetParent;
            return {
                x: e,
                y: n
            }
        },
        f = function(t) {
            var e = p(t),
            n = e.x + t.scrollLeft,
            i = e.y + t.scrollTop,
            o = t.clientWidth,
            r = t.clientHeight;
            console.log(n);
            return {
                x: n,
                y: i,
                left: n,
                top: i,
                right: n + o,
                bottom: i + r,
                width: o,
                height: r
            }
        },
        g = function(e) {
            return e.onmousedown = function(n) {
                l.dragPoint = h(n),
                l.target = e,
                t.stopEvent(n)
            },
            e
        };
        o = function(t, e) {
            return t.setAttribute("draggable", "true"),
            t.setAttribute(r, JSON.stringify(e)),
            t.ondragstart = n,
            g(t),
            t
        },
        d()
    }
    t.createDNDImage = i,
    t.appendDNDInfo = o
} (Q, jQuery),
!
function(t) {
    var e = e || "undefined" != typeof navigator && navigator.msSaveOrOpenBlob && navigator.msSaveOrOpenBlob.bind(navigator) ||
    function(t) {
        "use strict";
        if ("undefined" == typeof navigator || !/MSIE [1-9]\./.test(navigator.userAgent)) {
            var e = t.document,
            n = function() {
                return t.URL || t.webkitURL || t
            },
            i = e.createElementNS("http://www.w3.org/1999/xhtml", "a"),
            o = "download" in i,
            r = function(n) {
                var i = e.createEvent("MouseEvents");
                i.initMouseEvent("click", !0, !1, t, 0, 0, 0, 0, 0, !1, !1, !1, !1, 0, null),
                n.dispatchEvent(i)
            },
            a = t.webkitRequestFileSystem,
            s = t.requestFileSystem || a || t.mozRequestFileSystem,
            l = function(e) { (t.setImmediate || t.setTimeout)(function() {
                    throw e
                },
                0)
            },
            h = "application/octet-stream",
            u = 0,
            d = 500,
            c = function(e) {
                var i = function() {
                    "string" == typeof e ? n().revokeObjectURL(e) : e.remove()
                };
                t.chrome ? i() : setTimeout(i, d)
            },
            p = function(t, e, n) {
                e = [].concat(e);
                for (var i = e.length; i--;) {
                    var o = t["on" + e[i]];
                    if ("function" == typeof o) try {
                        o.call(t, n || t)
                    } catch(r) {
                        l(r)
                    }
                }
            },
            f = function(e, l) {
                var d, f, g, m = this,
                v = e.type,
                _ = !1,
                y = function() {
                    p(m, "writestart progress write writeend".split(" "))
                },
                b = function() {
                    if ((_ || !d) && (d = n().createObjectURL(e)), f) f.location.href = d;
                    else {
                        var i = t.open(d, "_blank");
                        void 0 == i && "undefined" != typeof safari && (t.location.href = d)
                    }
                    m.readyState = m.DONE,
                    y(),
                    c(d)
                },
                S = function(t) {
                    return function() {
                        return m.readyState !== m.DONE ? t.apply(this, arguments) : void 0
                    }
                },
                E = {
                    create: !0,
                    exclusive: !1
                };
                return m.readyState = m.INIT,
                l || (l = "download"),
                o ? (d = n().createObjectURL(e), i.href = d, i.download = l, r(i), m.readyState = m.DONE, y(), void c(d)) : (t.chrome && v && v !== h && (g = e.slice || e.webkitSlice, e = g.call(e, 0, e.size, h), _ = !0), a && "download" !== l && (l += ".download"), (v === h || a) && (f = t), s ? (u += e.size, void s(t.TEMPORARY, u, S(function(t) {
                    t.root.getDirectory("saved", E, S(function(t) {
                        var n = function() {
                            t.getFile(l, E, S(function(t) {
                                t.createWriter(S(function(n) {
                                    n.onwriteend = function(e) {
                                        f.location.href = t.toURL(),
                                        m.readyState = m.DONE,
                                        p(m, "writeend", e),
                                        c(t)
                                    },
                                    n.onerror = function() {
                                        var t = n.error;
                                        t.code !== t.ABORT_ERR && b()
                                    },
                                    "writestart progress write abort".split(" ").forEach(function(t) {
                                        n["on" + t] = m["on" + t]
                                    }),
                                    n.write(e),
                                    m.abort = function() {
                                        n.abort(),
                                        m.readyState = m.DONE
                                    },
                                    m.readyState = m.WRITING
                                }), b)
                            }), b)
                        };
                        t.getFile(l, {
                            create: !1
                        },
                        S(function(t) {
                            t.remove(),
                            n()
                        }), S(function(t) {
                            t.code === t.NOT_FOUND_ERR ? n() : b()
                        }))
                    }), b)
                }), b)) : void b())
            },
            g = f.prototype,
            m = function(t, e) {
                return new f(t, e)
            };
            return g.abort = function() {
                var t = this;
                t.readyState = t.DONE,
                p(t, "abort")
            },
            g.readyState = g.INIT = 0,
            g.WRITING = 1,
            g.DONE = 2,
            g.error = g.onwritestart = g.onprogress = g.onwrite = g.onabort = g.onerror = g.onwriteend = null,
            m
        }
    } ("undefined" != typeof self && self || "undefined" != typeof t && t || this.content);
    "undefined" != typeof module && null !== module ? module.exports = e: "undefined" != typeof define && null !== define && null != define.amd && define([],
    function() {
        return e
    }),
    t.saveAs = e
} (window, document),
!
function(t) {
    function e(e, n, i) {
        var o = e.name;
        if (t.isString(n)) {
            var r = new RegExp("." + n + "$", "gi");
            if (!r.test(o)) return void Dialog.alert("Please selects ." + n + " file")
        } else n instanceof Function && (i = n);
        var a = new FileReader;
        a.onload = function() {
            i(a.result)
        },
        a.readAsText(e, "utf-8")
    }
    window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem,
    t.isFileSupported = null != window.requestFileSystem,
    t.isFileSupported && (t.readerSingleFile = e)
} (Q),
!
function(t, e) {
    function n(t) {
        this._refs = {},
        t && (this.root = t.root)
    }
    function i(t) {
        if (! (t instanceof Object)) return ! t;
        if (Array.isArray(t)) return 0 == t.length;
        for (var e in t) return ! 1;
        return ! 0
    }
    function o(t, n) {
        var i = t.split(".");
        n = n || e;
        for (var o = -1; n && ++o < i.length;) {
            var r = i[o];
            n = n[r]
        }
        return n
    }
    function r(t, e, n) {
        if (t._classPath = e, t instanceof Function && (t.prototype._className = t._classPath, t.prototype._class = t), n !== !1) for (var i in t) if (! ("_" == i[0] || "$" == i[0] || "superclass" == i || "constructor" == i || "prototype" == i || i.indexOf(".") >= 0)) {
            var o = t[i];
            o && o instanceof Object && !o._classPath && r(o, e + "." + i)
        }
    }
    function a(t) {
        var e = t._className;
        if (!e) return null;
        var n = p[e];
        if (!n) {
            var i = t._class;
            n = p[e] = new i
        }
        return n
    }
    function s(t, e) {
        return t == e || t && e && t.equals && t.equals(e)
    }
    function l(t, e, n, i) {
        var o = a(i);
        e.forEach(function(e) {
            var r = i[e];
            if (!s(r, o[e])) {
                var a = t.toJSON(r); (a || !r) && (n[e] = a)
            }
        },
        i)
    }
    function h(t, e) {
        var n;
        for (var i in e) n || (n = {}),
        n[i] = t.toJSON(e[i]);
        return n
    }
    function u(t) {
        t && (this.withGlobalRefs = t.withGlobalRefs !== !1),
        this.reset()
    }
    function d(t, e) {
        var n = new u,
        o = {
            version: "2.0",
            refs: {}
        },
        r = [],
        a = {};
        if (t.currentSubNetwork) {
            var s = n.elementToJSON(t.currentSubNetwork);
            s && (o.currentSubNetwork = {
                _ref: s._refId = t.currentSubNetwork.id
            })
        }
        if (t.forEach(function(t) {
            if (!e || e(t) !== !1) {
                var i = n.elementToJSON(t);
                i && (r.push(i), a[t.id] = i)
            }
        }), n._elementRefs) for (var l in n._elementRefs) a[l]._refId = l;
        n._globalRefs && (o.refs = n._globalRefs),
        n.clearRef(),
        o.datas = r;
        for (var h in o) i(o[h]) && delete o[h];
        return o
    }
    function c(t) {
        var e = t.indexOf(".");
        return 0 > e ? parseFloat(t) : (t = t.substring(0, e) + "." + t.substring(e).replace(/\./g, ""), parseFloat(t))
    }
    if (!t.Graph.prototype.parseJSON) {
        n.prototype = {
            _refs: null,
            _index: 1,
            root: null,
            reset: function() {
                this._refs = {},
                this._index = 1
            },
            getREF: function(t) {
                return this._refs[t]
            },
            clearRef: function() {
                for (var t in this._refs) {
                    var e = this._refs[t],
                    n = e._value;
                    n && (n._refed || delete e._refId, delete n._refed, delete n._refId, delete e._value)
                }
                this.reset()
            },
            toJSON: function(t) {
                if (! (t instanceof Object)) return t;
                if (t instanceof Function && !t._classPath) return null;
                if (void 0 !== t._refId) return t._refed = !0,
                {
                    _ref: t._refId
                };
                var e = this._index++;
                t._refId = e;
                var n = this._toJSON(t);
                return n._refId = e,
                n._value = t,
                this._refs[e] = n,
                n
            },
            _toJSON: function(t) {
                if (t._classPath) return {
                    _classPath: t._classPath
                };
                if (!t._className) return t;
                var e = {
                    _className: t._className
                };
                return e.json = t.toJSON ? t.toJSON(this) : t,
                e
            },
            parseJSON: function(t) {
                return t instanceof Object ? void 0 !== t._ref ? this._refs[t._ref] : void 0 !== t._refId ? this._refs[t._refId] = this._parseJSON(t) : this._parseJSON(t) : t
            },
            _parseJSON: function(t) {
                if (t._classPath) return o(t._classPath);
                if (t._className) {
                    var e = o(t._className),
                    n = new e;
                    if (void 0 !== t._refId && (this._refs[t._refId] = n), n && t.json) if (t = t.json, n.parseJSON) n.parseJSON(t, this);
                    else for (var i in t) n[i] = t[i];
                    return n
                }
                return t
            }
        };
        var p = {};
        t.HashList.prototype.toJSON = function(t) {
            var e = [];
            return this.forEach(function(n) {
                e.push(t.toJSON(n))
            }),
            e
        },
        t.HashList.prototype.parseJSON = function(t, e) {
            t.forEach(function(t) {
                this.add(e.parseJSON(t))
            },
            this)
        };
        var f = {
            "class": !1,
            id: !1,
            fillGradient: !1,
            syncSelectionStyles: !1,
            originalBounds: !1,
            parent: !1,
            font: !1,
            $data: !1,
            $x: !1,
            $y: !1
        };
        t.BaseUI.prototype.toJSON = function(t) {
            var e = {};
            for (var n in this) if ("_" != n[0] && ("$" != n[0] || "_" != n[1]) && 0 != n.indexOf("$invalidate") && f[n] !== !1) {
                var i = this[n];
                if (! (i instanceof Function || i == this["class"].prototype[n])) try {
                    e[n] = t.toJSON(i)
                } catch(o) {}
            }
            return e
        },
        t.BaseUI.prototype.parseJSON = function(t, e) {
            for (var n in t) {
                var i = e.parseJSON(t[n]);
                this[n] = i
            }
        },
        t.Element.prototype.toJSON = function(t) {
            var e = {},
            n = ["enableSubNetwork", "zIndex", "tooltipType", "tooltip", "movable", "selectable", "resizable", "uiClass", "name", "parent", "host"];
            if (this.outputProperties && (n = n.concat(this.outputProperties)), l(t, n, e, this), this.styles) {
                var i = h(t, this.styles);
                i && (e.styles = i)
            }
            if (this.properties) {
                var o = h(t, this.properties);
                o && (e.properties = o)
            }
            var r = this.bindingUIs;
            if (r) {
                var a = [];
                r.forEach(function(e) {
                    var n = t.toJSON(e.ui);
                    a.push({
                        ui: n,
                        bindingProperties: e.bindingProperties
                    })
                }),
                e.bindingUIs = a
            }
            return e
        },
        t.Element.prototype.parseJSON = function(t, e) {
            if (t.styles) {
                var n = {};
                for (var i in t.styles) n[i] = e.parseJSON(t.styles[i]);
                this.putStyles(n, !0)
            }
            if (t.properties) {
                var o = {};
                for (var i in t.properties) o[i] = e.parseJSON(t.properties[i]);
                this.properties = o
            }
            t.bindingUIs && t.bindingUIs.forEach(function(t) {
                var n = e.parseJSON(t.ui);
                n && this.addUI(n, t.bindingProperties)
            },
            this);
            for (var i in t) if ("styles" != i && "properties" != i && "bindingUIs" != i) {
                var r = e.parseJSON(t[i]);
                this[i] = r
            }
        },
        t.Node.prototype.toJSON = function(e) {
            var n = t.doSuper(this, t.Node, "toJSON", arguments);
            return l(e, ["type","id","ip","mac","part","location", "size", "image", "rotate", "anchorPosition"], n, this),
            n
        },
        t.Group.prototype.toJSON = function(e) {
            var n = t.doSuper(this, t.Group, "toJSON", arguments);
            return l(e, ["minSize", "groupType", "padding", "groupImage", "expanded"], n, this),
            n
        },
        t.ShapeNode.prototype.toJSON = function(e) {
            var n = t.doSuper(this, t.Node, "toJSON", arguments);
            return l(e, ["id","location", "rotate", "anchorPosition", "path"], n, this),
            n
        },
        t.Edge.prototype.toJSON = function(e) {
            var n = t.doSuper(this, t.Edge, "toJSON", arguments);
            return l(e, ["id","from", "to", "edgeType", "angle", "bundleEnabled", "pathSegments"], n, this),
            n
        },
        u.prototype = {
            _refs: null,
            _refValues: null,
            _index: 1,
            root: null,
            reset: function() {
                this._globalRefs = {},
                this._elementRefs = {},
                this._refs = {},
                this._refValues = {},
                this._index = 1
            },
            getREF: function(t) {
                return this._refs[t]
            },
            clearRef: function() {
                for (var t in this._globalRefs) delete this._globalRefs[t]._value;
                for (var t in this._refValues) delete this._refValues[t]._refId;
                this.reset()
            },
            elementToJSON: function(t) {
                return this._toJSON(t)
            },
            _elementRefs: null,
            _globalRefs: null,
            withGlobalRefs: !0,
            toJSON: function(e) {
                if (! (e instanceof Object)) return e;
                if (e instanceof Function && !e._classPath) return null;
                if (!this.withGlobalRefs) return this._toJSON(e);
                if (e instanceof t.Element) return this._elementRefs[e.id] = !0,
                {
                    _ref: e.id
                };
                if (void 0 === e._refId) {
                    var n = this._toJSON(e);
                    if (!n) return n;
                    var i = e._refId = this._index++;
                    return this._refValues[i] = e,
                    this._refs[i] = n,
                    n
                }
                var i = e._refId;
                if (!this._globalRefs[i]) {
                    var n = this._refs[i];
                    if (!n) return n;
                    var o = {};
                    for (var r in n) o[r] = n[r],
                    delete n[r];
                    n.$ref = i,
                    this._globalRefs[i] = o
                }
                return {
                    $ref: i
                }
            },
            _toJSON: function(e) {
                if (e._classPath) return {
                    _classPath: e._classPath
                };
                if (!e._className) {
                    if (t.isArray(e)) {
                        var n = [];
                        return e.forEach(function(t) {
                            n.push(this.toJSON(t))
                        },
                        this),
                        n
                    }
                    n = {};
                    var i;
                    e["class"] && (i = e["class"].prototype);
                    for (var o in e) {
                        var r = e[o];
                        r instanceof Function || i && r == i[o] || (n[o] = this.toJSON(e[o]))
                    }
                    return n
                }
                var a = {
                    _className: e._className
                };
                return a.json = e.toJSON ? e.toJSON(this) : e,
                a
            },
            jsonToElement: function(t) {
                return void 0 !== t._refId && t._refId in this._refs ? this._refs[t._refId] : this._parseJSON(t)
            },
            parseJSON: function(t) {
                if (! (t instanceof Object)) return t;
                if (!this.withGlobalRefs) return this._parseJSON(t);
                if (void 0 !== t.$ref) {
                    var e = this._globalRefs[t.$ref];
                    if (!e) return;
                    return void 0 === e._value && (e._value = this.parseJSON(e)),
                    e._value
                }
                if (void 0 !== t._ref) {
                    var n = this._elementRefs[t._ref];
                    if (!n) return;
                    return this.jsonToElement(n)
                }
                return this._parseJSON(t)
            },
            _parseJSON: function(e) {
                if (! (e instanceof Object)) return e;
                if (e._classPath) return o(e._classPath);
                if (e._className) {
                    var n = o(e._className),
                    i = new n;
                    if (this.withGlobalRefs && void 0 !== e._refId && (this._refs[e._refId] = i), i && e.json) if (e = e.json, i.parseJSON) i.parseJSON(e, this);
                    else for (var r in e) i[r] = e[r];
                    return i
                }
                if (t.isArray(e)) {
                    var a = [];
                    return e.forEach(function(t) {
                        a.push(this.parseJSON(t))
                    },
                    this),
                    a
                }
                var a = {};
                for (var s in e) a[s] = this.parseJSON(e[s]);
                return a
            }
        },
        t.GraphModel.prototype.toJSON = function(t) {
            return d(this, t)
        },
        t.GraphModel.prototype.parseJSON = function(e, i) {
            var o = e.datas;
            if (o && o.length > 0) {
                if (c(e.version) <= 1.7) {
                    var r = new n(i),
                    o = e.datas;
                    return o.forEach(function(e) {
                        var n = r.parseJSON(e);
                        n instanceof t.Element && this.add(n)
                    },
                    this),
                    void r.reset()
                }
                var r = new u(i, e.g),
                a = {};
                if (o.forEach(function(t) {
                    t._refId && (a[t._refId] = t)
                }), r._globalRefs = e.refs || {},
                r._elementRefs = a, o.forEach(function(e) {
                    var n = r.jsonToElement(e);
                    n instanceof t.Element && this.add(n)
                },
                this), e.currentSubNetwork) {
                    var s = r.getREF(e.currentSubNetwork._ref);
                    s && (this.currentSubNetwork = s)
                }
                r.clearRef()
            }
        },
        t.Graph.prototype.toJSON = t.Graph.prototype.exportJSON = function(t, e) {
            e = e || {};
            var n = this.graphModel.toJSON(e.filter);
            return n.scale = this.scale,
            n.tx = this.tx,
            n.ty = this.ty,
            t && (n = JSON.stringify(n, e.replacer, e.space || "	")),
            n
        },
        t.Graph.prototype.parseJSON = function(e, n) {
            t.isString(e) && (e = JSON.parse(e)),
            this.graphModel.parseJSON(e, n);
            var i = e.scale;
            i && (this.originAtCenter = !1, this.translateTo(e.tx || 0, e.ty || 0, i))
        },
        r(t, "Q"),
        t.loadClassPath = r,
        t.exportJSON = function(t, e) {
            try {
                var n = new u({
                    withGlobalRefs: !1
                }).toJSON(t);
                return e ? JSON.stringify(n) : n
            } catch(i) {}
        },
        t.parseJSON = function(e) {
            try {
                return t.isString(e) && (e = JSON.parse(e)),
                new u({
                    withGlobalRefs: !1
                }).parseJSON(e)
            } catch(n) {}
        }
    }
} (Q, window, document),
function(t, e) {
    function n(e, n) {
        this.onBoundsChange = n,
        this.parent = e,
        this.handleSize = t.isTouchSupport ? 20 : 8,
        this.boundsDiv = this._createDiv(this.parent),
        this.boundsDiv.type = "border",
        this.boundsDiv.style.position = "absolute",
        this.boundsDiv.style.border = "dashed 1px #888";
        var i = "lt,t,rt,l,r,lb,b,rb";
        i = i.split(",");
        for (var o = 0,
        r = i.length; r > o; o++) {
            var a = i[o],
            s = this._createDiv(this.parent);
            s.type = "handle",
            s.name = a,
            s.style.position = "absolute",
            s.style.backgroundColor = "#FFF",
            s.style.border = "solid 1px #555",
            s.style.width = s.style.height = this.handleSize + "px";
            var l;
            l = "lt" == a || "rb" == a ? "nwse-resize": "rt" == a || "lb" == a ? "nesw-resize": "t" == a || "b" == a ? "ns-resize": "ew-resize",
            s.style.cursor = l,
            this[i[o]] = s
        }
        this.interaction = new t.DragSupport(this.parent, this)
    }
    function i() {
        var t = e("<div/>").html(r).contents();
        this.html = t = t[0],
        document.body.appendChild(this.html),
        t.addEventListener("mousedown",
        function(e) {
            e.target == t && this.destroy()
        }.bind(this), !1);
        var n = this._getChild(".graph-export-panel__export_scale"),
        i = this._getChild(".graph-export-panel__export_scale_label");
        n.onchange = function() {
            i.textContent = this.scale = n.value,
            this.updateOutputSize()
        }.bind(this),
        this.export_scale = n;
        var o = function(t) {
            var e = this.exportImageInfo();
            if (e) {
                var n = e.canvas,
                i = this.graph.name || "graph";
                n.toBlob(function(e) {
                    t(e, i + ".png")
                },
                "image/png")
            }
        },
        a = function(t) {
            var e = this.exportImageInfo();
            if (e) {
                var n = window.open(),
                i = n.document;
                i.title = this.graph.name || "";
                var o = i.createElement("img");
                if (o.src = e.data, i.body.style.textAlign = "center", i.body.style.margin = "0px", i.body.appendChild(o), t === !0) {
                    var r = i.createElement("style");
                    r.setAttribute("type", "text/css"),
                    r.setAttribute("media", "print");
                    var a = "img {max-width: 100%; max-height: 100%;}";
                    this.clipBounds.width / this.clipBounds.height > 1.2 && (a += "\n @page { size: landscape; }"),
                    r.appendChild(document.createTextNode(a)),
                    i.head.appendChild(r),
                    o.style.maxWidth = "100%",
                    o.style.maxHeight = "100%",
                    setTimeout(function() {
                        n.print(),
                        n.onfocus = function() {
                            n.close()
                        }
                    },
                    100)
                }
            }
        },
        s = this._getChild(".graph-export-panel__export_submit");
        s.onclick = window.saveAs && HTMLCanvasElement.prototype.toBlob ? o.bind(this, window.saveAs) : a.bind(this);
        var l = this._getChild(".graph-export-panel__print_submit");
        l.onclick = a.bind(this, !0)
    }
    function o(t) {
        a || (a = new i),
        a.show(t)
    }
    var r = '<div class="graph-export-panel modal fade">  <div class="modal-dialog">  <div class="modal-content">  <div class="modal-body">  <h3 style="text-align: center;">图片导出预览</h3>  <div>  <label>画布大小</label>  <span class ="graph-export-panel__canvas_size"></span>  </div>  <div style="text-align: center;" title="双击选择全画布范围">  <div class ="graph-export-panel__export_canvas" style="position: relative; display: inline-block;">  </div>  </div>  <div>  <label>导出范围</label>  <span class ="graph-export-panel__export_bounds"></span>  </div>  <div>  <label>缩放比例: <input class ="graph-export-panel__export_scale" type="range" value="1" step="0.2" min="0.2" max="3"><span class ="graph-export-panel__export_scale_label">1</span></label>  </div>  <div>  <label>输出大小: </label><span class ="graph-export-panel__export_size"></span>  </div>  <div style="text-align: right">  <button type="submit" class="btn btn-primary graph-export-panel__export_submit">导出</button>  <button type="submit" class="btn btn-primary graph-export-panel__print_submit">打印</button>  </div>  </div>  </div>  </div>  </div>';
    n.prototype = {
        destroy: function() {
            this.interaction.destroy()
        },
        update: function(e, n) {
            this.wholeBounds = new t.Rect(0, 0, e, n),
            this._setBounds(this.wholeBounds.clone())
        },
        ondblclick: function() {
            return this._bounds.equals(this.wholeBounds) ? (this.oldBounds || (this.oldBounds = this.wholeBounds.clone().grow( - this.wholeBounds.height / 5, -this.wholeBounds.width / 5)), void this._setBounds(this.oldBounds, !0)) : void this._setBounds(this.wholeBounds.clone(), !0)
        },
        startdrag: function(t) {
            t.target.type && (this.dragItem = t.target)
        },
        ondrag: function(e) {
            if (this.dragItem) {
                t.stopEvent(e);
                var n = e.dx,
                i = e.dy;
                if ("border" == this.dragItem.type) this._bounds.offset(n, i),
                this._setBounds(this._bounds, !0);
                else if ("handle" == this.dragItem.type) {
                    var o = this.dragItem.name;
                    "l" == o[0] ? (this._bounds.x += n, this._bounds.width -= n) : "r" == o[0] && (this._bounds.width += n),
                    "t" == o[o.length - 1] ? (this._bounds.y += i, this._bounds.height -= i) : "b" == o[o.length - 1] && (this._bounds.height += i),
                    this._setBounds(this._bounds, !0)
                }
            }
        },
        enddrag: function() {
            this.dragItem && (this.dragItem = !1, this._bounds.width < 0 ? (this._bounds.x += this._bounds.width, this._bounds.width = -this._bounds.width) : 0 == this._bounds.width && (this._bounds.width = 1), this._bounds.height < 0 ? (this._bounds.y += this._bounds.height, this._bounds.height = -this._bounds.height) : 0 == this._bounds.height && (this._bounds.height = 1), this._bounds.width > this.wholeBounds.width && (this._bounds.width = this.wholeBounds.width), this._bounds.height > this.wholeBounds.height && (this._bounds.height = this.wholeBounds.height), this._bounds.x < 0 && (this._bounds.x = 0), this._bounds.y < 0 && (this._bounds.y = 0), this._bounds.right > this.wholeBounds.width && (this._bounds.x -= this._bounds.right - this.wholeBounds.width), this._bounds.bottom > this.wholeBounds.height && (this._bounds.y -= this._bounds.bottom - this.wholeBounds.height), this._setBounds(this._bounds, !0))
        },
        _createDiv: function(t) {
            var e = document.createElement("div");
            return t.appendChild(e),
            e
        },
        _setHandleLocation: function(t, e, n) {
        	console.log( e - this.handleSize / 2);
            t.style.left = e - this.handleSize / 2 + "px",
            t.style.top = n - this.handleSize / 2 + "px"
        },
        _setBounds: function(t) {
        	console.log(t.x );
            t.equals(this.wholeBounds) || (this.oldBounds = t),
            this._bounds = t,
            t = t.clone(),
            t.width += 1,
            t.height += 1,
            this.boundsDiv.style.left = t.x + "px",
            this.boundsDiv.style.top = t.y + "px",
            this.boundsDiv.style.width = t.width + "px",
            this.boundsDiv.style.height = t.height + "px",
            this._setHandleLocation(this.lt, t.x, t.y),
            this._setHandleLocation(this.t, t.cx, t.y),
            this._setHandleLocation(this.rt, t.right, t.y),
            this._setHandleLocation(this.l, t.x, t.cy),
            this._setHandleLocation(this.r, t.right, t.cy),
            this._setHandleLocation(this.lb, t.x, t.bottom),
            this._setHandleLocation(this.b, t.cx, t.bottom),
            this._setHandleLocation(this.rb, t.right, t.bottom),
            this.onBoundsChange && this.onBoundsChange(this._bounds)
        }
    },
    Object.defineProperties(n.prototype, {
        bounds: {
            get: function() {
                return this._bounds
            },
            set: function(t) {
                this._setBounds(t)
            }
        }
    }),
    i.prototype = {
        canvas: null,
        html: null,
        exportImageInfo: function(e) {
            var e = this.graph;
            if (e) {
                var n = this.export_scale.value,
                i = this.imageInfo.scale,
                o = new t.Rect(this.clipBounds.x / i, this.clipBounds.y / i, this.clipBounds.width / i, this.clipBounds.height / i);
                o.offset(this.bounds.x, this.bounds.y);
                var r = e.exportImage(n, o);
                if (r && r.data) return r
            }
        },
        _getChild: function(t) {
            return e(this.html).find(t)[0]
        },
        initCanvas: function() {
            var e = this._getChild(".graph-export-panel__export_canvas");
            e.innerHTML = "";
            var i = t.createCanvas(!0);
            e.appendChild(i),
            this.canvas = i;
            var o, r = this._getChild(".graph-export-panel__export_bounds"),
            a = this._getChild(".graph-export-panel__export_size"),
            s = function() {
                var t = this.canvas,
                e = t.g,
                n = t.ratio || 1;
                e.save(),
                e.clearRect(0, 0, t.width, t.height),
                e.drawImage(this.imageInfo.canvas, 0, 0),
                e.beginPath(),
                e.moveTo(0, 0),
                e.lineTo(t.width, 0),
                e.lineTo(t.width, t.height),
                e.lineTo(0, t.height),
                e.lineTo(0, 0);
                var i = o.x * n,
                r = o.y * n,
                a = o.width * n,
                s = o.height * n;
                e.moveTo(i, r),
                e.lineTo(i, r + s),
                e.lineTo(i + a, r + s),
                e.lineTo(i + a, r),
                e.closePath(),
                e.fillStyle = "rgba(0, 0, 0, 0.3)",
                e.fill(),
                e.restore()
            },
            l = function(t) {
                o = t,
                this.clipBounds = o,
                s.call(this);
                var e = o.width / this.imageInfo.scale | 0,
                n = o.height / this.imageInfo.scale | 0;
                r.textContent = (o.x / this.imageInfo.scale | 0) + ", " + (o.y / this.imageInfo.scale | 0) + ", " + e + ", " + n,
                this.updateOutputSize()
            };
            this.updateOutputSize = function() {
                var t = this._getChild(".graph-export-panel__export_scale"),
                e = t.value,
                n = o.width / this.imageInfo.scale * e | 0,
                i = o.height / this.imageInfo.scale * e | 0,
                r = n + " X " + i;
                n * i > 12e6 && (r += "<span style='color: #F66;'>图幅太大，导出时可能出现内存不足</span>"),
                a.innerHTML = r
            };
            var h = new n(i.parentNode, l.bind(this));
            this.update = function() {
                var t = this.canvas.ratio || 1,
                e = this.imageInfo.width / t,
                n = this.imageInfo.height / t;
                this.canvas.setSize(e, n),
                h.update(e, n)
            }
        },
        destroy: function() {
            this.graph = null,
            this.imageInfo = null,
            this.clipBounds = null,
            this.bounds = null
        },
        show: function(t) {
            e(this.html).modal("show"),
            this.graph = t;
            var n = t.bounds;
            this.bounds = n;
            var i = this._getChild(".graph-export-panel__canvas_size");
            i.textContent = (0 | n.width) + " X " + (0 | n.height);
            var o, r = Math.min(500, screen.width / 1.3);
            o = n.width > n.height ? Math.min(1, r / n.width) : Math.min(1, r / n.height),
            this.canvas || this.initCanvas(),
            this.imageInfo = t.exportImage(o * this.canvas.ratio),
            this.imageInfo.scale = o,
            this.update()
        }
    };
    var a;
    t.showExportPanel = o
} (Q, jQuery),
function(t, e) {
    function n(t, e, n, o, r) {
        var a = document.createElement("div");
        a.className = o ? "btn-group-vertical": "btn-group",
        r && a.setAttribute("data-toggle", "buttons");
        for (var s = 0,
        l = t.length; l > s; s++) ! t[s].type && r && (t[s].type = "radio"),
        a.appendChild(i(t[s], n)).info = t[s];
        e.appendChild(a)
    }
    function i(n, i) {
        if ("search" == n.type) {
            var o = document.createElement("div");
            o.style.display = "inline-block",
            o.style.verticalAlign = "middle",
            o.style.width = "170px",
            o.style.float ="left";
            o.innerHTML = '<div class="input-group input-group-sm" >            <input type="text" class="form-control" placeholder="' + (n.placeholder || "") + '">                <span class="input-group-btn">                    <div class="btn btn-default" type="button"></div>                </span>            </div>';
            var r = o.getElementsByTagName("input")[0];
            n.id && (r.id = n.id);
            var a = e(o).find(".btn")[0];
            if (n.iconClass) {
                var s = document.createElement("div");
                e(s).addClass(n.iconClass),
                a.appendChild(s)
            } else n.name && a.appendChild(document.createTextNode(" " + n.name));
            if (n.input = r, n.search) {
                var l = function() {
                    n.searchInfo = null
                },
                h = function(t) {
                    var e = r.value;
                    if (!e) return void l();
                    if (!n.searchInfo || n.searchInfo.value != e) {
                        var i = n.search(e, n);
                        if (!i || !i.length) return void l();
                        n.searchInfo = {
                            value: e,
                            result: i
                        }
                    }
                    u(t)
                },
                u = function(t) {
                    if (n.select instanceof Function && n.searchInfo && n.searchInfo.result && n.searchInfo.result.length) {
                        var e = n.searchInfo,
                        i = n.searchInfo.result;
                        if (1 == i.length) return void n.select(i[0], 0);
                        void 0 === e.index ? e.index = 0 : (e.index += t ? -1 : 1, e.index < 0 && (e.index += i.length), e.index %= i.length),
                        n.select(i[e.index], e.index) === !1 && (n.searchInfo = null, h())
                    }
                };
                r.onkeydown = function(e) {
                    return 27 == e.keyCode ? (l(), r.value = "", void t.stopEvent(e)) : void(13 == e.keyCode && h(e.shiftKey))
                },
                a.onclick = function() {
                    h()
                }
            }
            return o
        }
        if ("file" == n.type) {
            var d = document.createElement("span"),
            r = document.createElement("input");
            if (d.className = "file-input btn btn-default btn-sm btn-file", r.setAttribute("type", "file"), r.className = "btn-file", n.action &&  ($(r).on("change",function(){
            	var o = e(this),
                r = o.get(0).files;
                d = o.val().replace(/\\/g, "/").replace(/.*\//, ""),
                r.length && n.action.call(i, r, d, t)
            })), d.appendChild(r), n.icon) {
                var s = document.createElement("img");
                s.src = n.icon,
                d.appendChild(s)
            } else if (n.iconClass) {
                var s = document.createElement("div");
                e(s).addClass(n.iconClass),
                d.appendChild(s)
            } else n.name && d.appendChild(document.createTextNode(" " + n.name));
            return n.name && d.setAttribute("title", n.name),
            d
        }
        if ("input" == n.type) {
            var o = document.createElement("div");
            o.style.display = "inline-block",
            o.style.verticalAlign = "middle",
            o.innerHTML = '<div class="input-group input-group-sm" style="width: 150px;">            <input type="text" class="form-control">                <span class="input-group-btn">                    <button class="btn btn-default" type="button"></button>                </span>            </div>';
            var r = o.getElementsByTagName("input")[0],
            a = o.getElementsByTagName("button")[0];
            return a.innerHTML = n.name,
            n.input = r,
            n.action && (a.onclick = function(t) {
                n.action.call(i || window.graph, t, n)
            }),
            o
        }
        if ("select" == n.type) {
            var o = document.createElement("select");
            o.className = "form-control";
            var c = n.options;
            return c.forEach(function(t) {
                var e = document.createElement("option");
                e.innerHTML = t,
                e.value = t,
                o.appendChild(e)
            }),
            o.value = n.value,
            n.action && (o.onValueChange = function(t) {
                n.action.call(i || window.graph, t, n)
            }),
            o
        }
        if (n.type) {
            var d = document.createElement("label"),
            a = document.createElement("input");
            n.input = a,
            a.setAttribute("type", n.type),
            d.appendChild(a),
            n.selected && (a.setAttribute("checked", "checked"), "radio" == n.type && (d.className += "active"))
        } else var d = document.createElement("div");
        if (d.className += "btn btn-default btn-sm", n.icon) {
            var s = document.createElement("img");
            s.src = n.icon,
            d.appendChild(s)
        } else if (n.iconClass) {
            var s = document.createElement("div");
            e(s).addClass(n.iconClass),
            d.appendChild(s)
        } else n.name && d.appendChild(document.createTextNode(" " + n.name));
        return n.name && d.setAttribute("title", n.name),
        n.action && ((a || d).onclick = function(t) {
            n.action.call(i || window.graph, t, n)
        }),
        d
    }
    function o(o, r, a) {
        function s() {
            return r.graph
        }
        function l() {
            var n = s(),
            i = n ? n.interactionMode: null;
            e(r).find(".btn").each(function(e, n) {
                i && n.info && n.info.interactionMode == i ? t.appendClass(n, "active") : t.removeClass(n, "active")
            })
        }
        function h(t) {
            "interactionMode" == t.kind && l()
        }
        function u(t, e, n) {
            var i = s();
            i && (i.interactionMode = e.value, i.interactionProperties = n || e)
        }
        function d(e, o, r, a, s) {
            for (var l in e) {
                var h = e[l];
                t.isArray(h) ? (h.forEach(function(t) {
                    t.interactionMode && (t.value = t.interactionMode, t.action = u)
                }), n(h, o, r, a, s)) : (h.interactionMode && (h.value = h.interactionMode, h.action = u), o.appendChild(i(h, r)).info = h)
            }
        }
        r.setGraph = function(t) {
            var e = this.graph;
            e && e.propertyChangeDispatcher.removeListener(h, this),
            this.graph = t,
            l(),
            t && t.propertyChangeDispatcher.addListener(h, this)
        };
        var before_select =[];
        var c = {
            interactionModes: [{
                name: "默认模式",
                interactionMode: t.Consts.INTERACTION_MODE_DEFAULT,
                selected: !0,
                iconClass: "q-icon toolbar-default"
            }/*,
            {
                name: "框选模式",
                interactionMode: t.Consts.INTERACTION_MODE_SELECTION,
                iconClass: "q-icon toolbar-rectangle_selection"
            },
            {
                name: "浏览模式",
                interactionMode: t.Consts.INTERACTION_MODE_VIEW,
                iconClass: "q-icon toolbar-pan"
            }*/],
            zoom: [{
                name: "放大",
                iconClass: "q-icon toolbar-zoomin",
                action: function() {
                    s().zoomIn()
                }
            },
            {
                name: "缩小",
                iconClass: "q-icon toolbar-zoomout",
                action: function() {
                    s().zoomOut()
                }
            }/*,
            {
                name: "1:1",
                iconClass: "q-icon toolbar-zoomreset",
                action: function() {
                    s().scale = 1
                }
            },
            {
                name: "纵览",
                iconClass: "q-icon toolbar-overview",
                action: function() {
                    s().zoomToOverview()
                }
            }*/],
            editor: [{
                name: "创建连线",
                interactionMode: t.Consts.INTERACTION_MODE_CREATE_EDGE,
                iconClass: "q-icon toolbar-edge"
            },
           /*{
                name: "创建L型连线",
                interactionMode: t.Consts.INTERACTION_MODE_CREATE_SIMPLE_EDGE,
                iconClass: "q-icon toolbar-edge_VH",
                edgeType: t.Consts.EDGE_TYPE_VERTICAL_HORIZONTAL
            },*/
            /* {
                name: "创建多边形",
                interactionMode: t.Consts.INTERACTION_MODE_CREATE_SHAPE,
                iconClass: "q-icon toolbar-polygon"
            },*/
            /*{
                name: "创建线条",
                interactionMode: t.Consts.INTERACTION_MODE_CREATE_LINE,
                iconClass: "q-icon toolbar-line"
            }*/
            ],
            search: {
                name: "Find",
                placeholder: "请输入设备名称",
                iconClass: "q-icon toolbar-search",
                type: "search",
                id: "search_input",
                search: function(t) {
                    var e = [],
                    n = new RegExp(t, "i");
                    return s().forEach(function(t) {
                        t.name && n.test(t.name) && e.push(t.id)
                    }),
                    e
                },
                select: function(t) {
                    if (t = s().graphModel.getById(t), !t) return ! 1;
                    for (var i = 0; i < before_select.length; i++){
                    	var _id = before_select[i];
                    	var lu = s().getUI( _id ).$data;
                    	lu.setStyle(Q.Styles.BORDER, null);
                    	lu.setStyle(Q.Styles.BORDER_COLOR,null);
                    	lu.setStyle(Q.Styles.PADDING, null);
                    	delete lu.kind_type; // 查询
                    }
                    before_select =[];
                    before_select.push(t.id);
                    t.kind_type = "query"; // 查询
                    $(".graph-editor__property").slideUp();
                //    s().setSelection(t),
                    s().sendToTop(t);
                    t.setStyle(Q.Styles.BORDER, 2);
                    t.setStyle(Q.Styles.BORDER_COLOR, "#2898E0");
                  //  t.setStyle(Q.Styles.PADDING, new Q.Insets(10, 20));
                    var e = s().getUIBounds(t);
                    e && s().centerTo(e.cx, e.cy, Math.max(2, s().scale), !0)
                }
            }/*,
            exportImage: {
                name: "导出图片",
                iconClass: "q-icon toolbar-print",
                action: function() {
                    t.showExportPanel(s())
                }
            }*/
        };
        if (a) for (var p in a) c[p] = a[p];
        return d(c, r, this, !1, !1),
        r.setGraph(o),
        r
    }
    t.createToolbar = o,
    t.createButtonGroup = n,
    t.createButton = i
} (Q, jQuery),
!
function(t, e) {
    function n(t) {
        if (!t) return a;
        var e = {};
        for (var n in a) e[n] = a[n];
        for (var n in t) {
            var i = s[n];
            i && (e[i] = t[n])
        }
        return e
    }
    function i(e) {
        return t.isString(e) || e.draw instanceof Function
    }
    var o = function(t, n, i, o) {
        var r = document.createElement(i || "div");
        return r.className = t,
        e(r).html(o),
        n && n.appendChild(r),
        r
    },
    r = function(t, e, n) {
        if (Array.isArray(t)) return t.forEach(function(t) {
            e.call(this, t)
        },
        n);
        for (var i in t) e.call(n, t[i], i)
    },
    a = {
        fillColor: "#EEE",
        lineWidth: 1,
        strokeStyle: "#2898E0",
        padding: {
            left: 1,
            top: 1,
            right: 5,
            bottom: 5
        },
        shadowColor: "#888",
        shadowOffsetX: 2,
        shadowOffsetY: 2,
        shadowBlur: 3
    },
    s = {};
    s[t.Styles.RENDER_COLOR] = "renderColor",
    s[t.Styles.RENDER_COLOR_BLEND_MODE] = "renderColorBlendMode",
    s[t.Styles.SHAPE_FILL_COLOR] = "fillColor",
    s[t.Styles.SHAPE_STROKE_STYLE] = "strokeStyle",
    s[t.Styles.SHAPE_LINE_DASH] = "borderLineDash",
    s[t.Styles.SHAPE_LINE_DASH_OFFSET] = "borderLineDashOffset",
    s[t.Styles.SHAPE_OUTLINE] = "outline",
    s[t.Styles.SHAPE_OUTLINE_STYLE] = "outlineStyle",
    s[t.Styles.LINE_CAP] = "lineGap",
    s[t.Styles.LINE_JOIN] = "lineJoin",
    s[t.Styles.BACKGROUND_COLOR] = "backgroundColor",
    s[t.Styles.BACKGROUND_GRADIENT] = "backgroundGradient",
    s[t.Styles.BORDER] = "border",
    s[t.Styles.BORDER_COLOR] = "borderColor",
    s[t.Styles.BORDER_LINE_DASH] = "borderLineDash",
    s[t.Styles.BORDER_LINE_DASH_OFFSET] = "borderLineDashOffset";
    var l = function(t) {
            var n =  t.target.parentNode;
            if(n && !e(n).hasClass("group") ) {
                n = n.parentNode;
            }
           // debugger;
            if( n && e(n).hasClass("group--closed") ){
            	 e(n).removeClass("group--closed");
            	 e(".graph-editor__canvas").addClass("left150");
            }else{
            	 e(n).addClass("group--closed");
            	 e(".graph-editor__canvas").removeClass("left150");
            }
            
        },
    h = function(t, e, n) {
        this.graph = t,
        this.html = e,
        this.init(n)
    };
    h.prototype = {
        loadButton: null,
        imageWidth: 40,
        imageHeight: 40,
        loadImageBoxes: function(e) {
            return t.isArray(e) ? void r(e,
            function(t) {
                this.loadImageBox(t)
            },
            this) : void this.loadImageBox(e)
        },
        loadImageBox: function(e, n) {
            if (t.isString(e) && (e = JSON.parse(e)), n) {
                var i = this.html.getElementsByClassName("group").item(0);
                if (i) return void this.html.insertBefore(this._createGroup(e, e.prefix), i)
            }
            this.html.appendChild(this._createGroup(e, e.prefix))
        },
        loadImageBoxFile: function(e) {
            e[0] && t.readerSingleFile(e[0], "json",
            function(t) {
                t && this.loadImageBox(t, !0)
            }.bind(this))
        },
        init: function(e) {
            {
                var n = this.html;
                this.graph
            }
            t.appendClass(n, "graph-editor__toolbox");
            var i = o("graph-editor__toolbox-buttonBar", n);
           /* r = this.loadButton = t.createButton({
                type: "file",
                name: getI18NString("Load Images..."),
                iconClass: "q-icon toolbar-add",
                action: this.loadImageBoxFile.bind(this)
            },
            this);
            i.appendChild(r);*/
            var a = [{
                label: "Node",
                image: "Q-node"
            },
            {
                type: "Text",
                label: "Text",
                html: '<span style="background-color: #2898E0; color:#FFF; padding: 3px 5px;">' + getI18NString("Text") + "</span>",
                styles: {
                    "label.background.color": "#2898E0",
                    "label.color": "#FFF",
                    "label.padding": new t.Insets(3, 5)
                }
            },
            {
                type: "Group",
                label: "Group",
                image: "Q-group"
            },
            {
                label: "SubNetwork",
                image: "Q-subnetwork",
                properties: {
                    enableSubNetwork: !0
                }
            }],
                s = [/*{
                prefix: "Q-",
                name: "basic.nodes",
                displayName: getI18NString("Basic Nodes"),
                images: a
            },
            {
                prefix: "Q-",
                name: "register.images",
                displayName: getI18NString("Register Images"),
                images: t.getAllImages()
            },
            {
                name: "default.shapes",
                displayName: getI18NString("Default Shapes"),
                prefix: "Q-",
                images: t.Shapes.getAllShapes(this.imageWidth, this.imageHeight)
            }*/];
            this.loadImageBoxes(s),
            e && this.loadImageBoxes(e)
        },
        _index: 0,
        _getGroup: function(t) {
            return this._groups[t]
        },
        hideDefaultGroups: function() {
            this.hideGroup("basic.nodes"),
            this.hideGroup("register.images"),
            this.hideGroup("default.shapes")
        },
        hideGroup: function(t) {
            var e = this._getGroup(t);
            e && (e.style.display = "none")
        },
        showGroup: function(t) {
            var e = this._getGroup(t);
            e && (e.style.display = "")
        },
        _groups: {},
        _createGroup: function(e) {
            var a = e.name,
            s = e.root,
            h = e.images,
            u = e.displayName || a,
            //d = o("group");
            d = o("group  group--closed");
            d.id = a,
            this._groups[a] = d;
            var c = o("group__title", d);
            c.onclick = l,
            o(null, c, "span", u);
           // o("q-icon group-expand toolbar-expand", c, "span");
            
            var p = o("group__items", d),
            f = document.createElement("div");
            var ul = o("accordion", p, "ul");
            var add_icon = o("add_icon", p, "div");
            var arr=[];
            if (f.style.clear = "both", d.appendChild(f), !h) return d;
            var g = e.imageWidth || this.imageWidth,
            m = e.imageHeight || this.imageHeight;
            var submit = (function(i, s, g, m, t, n, e, c, arr, _$){
        		return function(e){
        		        var mydialog = e.data;
		    			var r = {
		    			    image : "Fourchannelreader.png",//$("#show_icon",mydialog.contextDoc).text(),
		    			    groupName : $("#input_group",mydialog.contextDoc).val(),
		    			    tooltip :$("#iconname_group",mydialog.contextDoc).val(),
		    			    id:$("#hideIconId",mydialog.contextDoc).val()
		    			    
		    			 };
		    			//文件上传
		    			(function($){
		    				var jQuery = $;
		    				$.ajaxFileUpload({   
			            		url:context_path + "/visual/uploadDeviceIconFile",//用于文件上传的服务器端请求地址   
			            		secureuri:false,//一般设置为false
			            		fileElementId:"uploadImageIconFile",//文件上传空间的id属性  <input type="file" id="file" name="file" />   
			            		dataType: 'json',//返回值类型 一般设置为json   
			            		async:false,
			            		data:{groupName:r.groupName,iconName : r.tooltip,iconid : r.id==""?"-1":r.id},
			            		success: function (data, status) {     //服务器成功响应处理函数  
			            			if(data.result){
			            				r.image = data.msg;
			            				fun();   //修改图标背景图片
			            				Dialog.alert("修改成功！");
			            			}else{
			            				Dialog.alert("修改失败！");
			            			}
			            		},   
			            		error: function (data, status, e){//服务器响应失败处理函数  
			            			Dialog.alert("上传失败："+e);
			            		}   
			            	});
		    			}(window.top.$))
		    			var fun = function(){
		    				if( arr.indexOf(r.groupName) == -1 ){
			    				 var li =$('<li class="" group = "'+r.groupName+'" title="'+r.groupName+'" ><div class="link">'+r.groupName+'<i class="fa fa-chevron-down"></i></div><ul class="submenu" style="display: none;"></ul></li>');
			                	    ul.appendChild(li[0]);
			                	    arr.push(r.groupName);
			                	    li.on("click",function(){
			                	    	$(".graph-editor__toolbox .accordion >li").not($(this)).removeClass("open").find(".submenu").slideUp();
			                	    	$(this).toggleClass("open");
			                	    	$(this).find(".submenu").slideToggle();
			                	    
			                	    });
			                	    $("#select_group").append($("<option>",{
			                	    	text : r.groupName
			                	    }));
			    			 }
			    			// var r ={image:"Fourchannelreader.png",tooltip : "四通道读写器x",group :"读写器"};
			         	 	
			                 var l;
			                 l = i(r) ? r: r.image;
			                 var h, u;
			                 if (l) {
			                     var d;
			                     t.isString(l) ? (d = l, !t.hasImage(l) && s && (d = l = s + l)) : d = r.imageName || r.name || a || "drawable-" + this._index++,
			                     t.hasImage(d) || t.registerImage(d, l),
			                     h = t.createCanvas(g, m, !0),
			                     t.drawImage(d, h, n(r.styles)),
			                     i(r) ? r = {
			                         image: d
			                     }: r.image = d,
			                     e.size && (r.properties || (r.properties = {}), r.properties.size || (r.properties.size = e.size)),
			                     u = d
			                 } else {
			                     if (!r.html) return;
			                     var h = document.createElement("div");
			                     h.style.width = g + "px",
			                     h.style.height = m + "px",
			                     h.style.lineHeight = m + "px",
			                     h.style.overflow = "hidden",
			                     h.innerHTML = r.html
			                 }
			                 u = r.tooltip || r.label || u || a,
			                 h.setAttribute("title", u);
			                 var c = o("group__item");
			                 c.setAttribute("id","icon_"+r.id);
			                 t.appendDNDInfo(h, r),
			                 c.appendChild(h);
			                 var label = document.createElement("div");
			                 label.style.fontSize = "10px",
			                 label.innerHTML = u;
			                 c.appendChild(label);
			                 if( r.groupName ){
			                	 _$(ul).find('[title="'+r.groupName+'"] .submenu').append(_$("<li>",{
			                			group : r.groupName,
				                		name : u,
				                 		html:c
			                 	}));
			                 }
			                 mydialog.close();
		    			}
		    			 
    		     };
        }(i, s, g, m, t, n, e, c,arr, $));
            $(document).on("click",function(){
            	 $("#handle_icon").hide();
            });
            $(".del_icon").on("click",function(){
            	Dialog.alert("是否删除该图标！",function(){
            		$(".active_icon_li").remove();
                });
            });
            $(".edit_icon").on("click",function(){
            	var mydialog = Dialog.open({
        			id : "pop",
        		    title : "修改图标", 
        		    width : 650,  height: $(".active_icon_li").attr("title") ? 140:215,
        		    content : document.getElementById(($(".active_icon_li").attr("title") ? "pop_editgroup": "pop_addicon")).innerHTML,
        		    onShow : function(){
        		    	var _self = this;
        		    	
        		    	if($(".active_icon_li").attr("title")){
        		    		var _g = $(".active_icon_li").attr("group");
            		    	$("#group_name", this.contextDoc).val(_g);
            		    	
        		    	}else{
        		    		var _g = $(".active_icon_li").attr("group");
        		    		var _n = $(".active_icon_li").attr("name");
            		    	$("#iconname_group", this.contextDoc).val(_n);
            		    	$("#input_group", this.contextDoc).val(_g);
            		    	$("#select_group", this.contextDoc).val(_g);
            		    	
            		    	var _iconid = $(".active_icon_li .group__item").attr("id");
            		    	$("#hideIconId", this.contextDoc).val(_iconid==undefined?"-1":_iconid.split("_")[1]);
        		    	}
        		    	 $(this.contextDoc).off("click",".submit_group");
                		 $(this.contextDoc).on("click", ".submit_group", _self, function(){
                			 if($(".active_icon_li").attr("title")){
                				 var val = $("#group_name", _self.contextDoc).val();
                				 $("[group='"+$(".active_icon_li").attr("title") +"']").attr("group", val);
                				 $(".active_icon_li").attr({title: val, group: val});
                				 $(".active_icon_li").find(".link").html(val + '<i class="fa fa-chevron-down"></i>');
                				 _self.close();
             		    	}else{
             		    		$(".active_icon_li").remove();
                				submit(arguments[0]);
             		    	}
                				
                		 });
                		 $(this.contextDoc).off("click",".cancel_group");
                		 $(this.contextDoc).on("click",".cancel_group",function(){
                			 _self.close();
                		 });
        		    } 
        		});
            });
            $(document).on("contextmenu",".group__items li",function(e){
            	 var e = e || window.event  ;
                	 $("#handle_icon").css({
                			left:e.clientX,
                			top :e.clientY
                		}).show();
                 $(".active_icon_li").removeClass("active_icon_li");
                 $(this).addClass("active_icon_li");
                 return false;
            });
            
            $(add_icon).on("click",function(){
            	var mydialog = Dialog.open({
        			id:"pop",
        		    title: "添加图标", 
        		    width: 650,  height: 215,
        		    content: document.getElementById("pop_addicon").innerHTML
        		});
           	 $(mydialog.contextDoc).off("click", ".submit_group");
    		 $(mydialog.contextDoc).on("click", ".submit_group", mydialog, submit);
    		 $(mydialog.contextDoc).off("click", ".cancel_group");
    		 $(mydialog.contextDoc).on("click", ".cancel_group", function(){
    			 mydialog.close();
    		 });
            });
            
            return r(h,
            function(r, a) {
                if ("_classPath" != a && "_className" != a) {
                	if( r.groupName && arr.indexOf(r.groupName) == -1 ){
                		/*  var li = o("", ul, "li"); <i class="fa fa-paint-brush"></i>*/
                		var li ="";
                        if(r.groupName=="无线设备"){
                            li=$('<li class="" group = "'+r.groupName+'"  title="'+r.groupName+'" ><div class="link"><img src="'+basePath+'/static/images/wifi.png" style="left:20px;position: absolute;"/>'+r.groupName+'<i class="fa fa-chevron-down"></i></div><ul class="submenu" style="display: none;"></ul></li>');
                        }
                        if(r.groupName=="RFID"){
                            li=$('<li class="" group = "'+r.groupName+'"  title="'+r.groupName+'" ><div class="link"><img src="'+basePath+'/static/images/RFID.png" style="left:20px;position: absolute;"/>'+r.groupName+'<i class="fa fa-chevron-down"></i></div><ul class="submenu" style="display: none;"></ul></li>');
                        }
                        if(r.groupName=="阵列定位设备"){
                            li=$('<li class="" group = "'+r.groupName+'"  title="'+r.groupName+'" ><div class="link"><img src="'+basePath+'/static/images/bus.png" style="left:22px;position: absolute;margin-top:-3px;margin-left:-1px"/>'+r.groupName+'<i class="fa fa-chevron-down"></i></div><ul class="submenu" style="display: none;"></ul></li>');
                        }
                	    ul.appendChild(li[0]);
                	    arr.push(r.groupName);
                	    li.on("click",function(){
                	    	
                	    	$(".graph-editor__toolbox .accordion >li").not($(this)).removeClass("open").find(".submenu").slideUp();
                	    	$(this).toggleClass("open");
                	    	$(this).find(".submenu").slideToggle();
                	    
                	    });
                	    $("#select_group").append($("<option>",{
                	    	text : r.groupName
                	    }));
                	}
                	
                    var l;
                    l = i(r) ? r: r.image;
                    var h, u;
                    if (l) {
                        var d;
                        t.isString(l) ? (d = l, !t.hasImage(l) && s && (d = l = s + l)) : d = r.imageName || r.name || a || "drawable-" + this._index++,
                        t.hasImage(d) || t.registerImage(d, l),
                        h = t.createCanvas(g, m, !0),
                        t.drawImage(d, h, n(r.styles)),
                        i(r) ? r = {
                            image: d
                        }: r.image = d,
                        e.size && (r.properties || (r.properties = {}), r.properties.size || (r.properties.size = e.size)),
                        u = d
                    } else {
                        if (!r.html) return;
                        var h = document.createElement("div");
                        h.style.width = g + "px",
                        h.style.height = m + "px",
                        h.style.lineHeight = m + "px",
                        h.style.overflow = "hidden",
                        h.innerHTML = r.html
                    }
                    u = r.tooltip || r.label || u || a,
                    h.setAttribute("title", u);
                    var c = o("group__item");
                    c.setAttribute("id","icon_"+r.id);
                    t.appendDNDInfo(h, r),
                    c.appendChild(h);
                    var label = document.createElement("div");
                    label.style.fontSize = "10px",
                    label.innerHTML = u;
                    c.appendChild(label);
                    if( r.groupName ){
	                	 $(ul).find('[title="'+r.groupName+'"] .submenu').append($("<li >",{
	                		group : r.groupName,
	                		name : u,
	                 		html:c
	                 	}));
	                 }
                }
            },
            this),
            d
        }
    },
    t.ToolBox = h
} (Q, jQuery),
function(t) {
    function e(e, n, i) {
    	console.log(n );
        var o = document.documentElement,
        r = new t.Rect(window.pageXOffset, window.pageYOffset, o.clientWidth - 2, o.clientHeight - 2),
        a = e.offsetWidth,
        s = e.offsetHeight;
        n + a > r.x + r.width && (n = r.x + r.width - a),
        i + s > r.y + r.height && (i = r.y + r.height - s),
        n < r.x && (n = r.x),
        i < r.y && (i = r.y),
        e.style.left = n + "px",
        e.style.top = i + "px"
    }
    function n(t, e) {
        for (var n = e.parentNode; null != n;) {
            if (n == t) return ! 0;
            n = n.parentNode
        }
        return ! 1
    }
    function i(t) {
        return t.touches && t.touches.length && (t = t.touches[0]),
        {
            x: t.pageX,
            y: t.pageY
        }
    }
    function o(e, n) {
        var o = n.popupmenu,
        r = i(e),
        a = r.x,
        s = r.y,
        l = o.getMenuItems(n, n.getElement(e), e);
        l && (o.items = l, o.showAt(a, s), t.stopEvent(e))
    }
    var r = function(t) {
        this.items = t || []
    },
    a = "dropdown-menu";
    r.Separator = "divider",
    r.prototype = {
        dom: null,
        _invalidateFlag: !0,
        add: function(t) {
            this.items.push(t),
            this._invalidateFlag = !0
        },
        addSeparator: function() {
            this.add(r.Separator)
        },
        showAt: function(t, n) {
            return this.items && this.items.length ? (this._invalidateFlag && this.render(), this.dom.style.display = "block", document.body.appendChild(this.dom), void e(this.dom, t, n)) : !1
        },
        hide: function() {
            this.dom && this.dom.parentNode && this.dom.parentNode.removeChild(this.dom)
        },
        render: function() {
            if (this._invalidateFlag = !1, this.dom) this.dom.innerHTML = "";
            else {
                this.dom = document.createElement("ul"),
                this.dom.setAttribute("role", "menu"),
                this.dom.className = a;
                var e = t.isTouchSupport ? "touchstart": "mousedown";
                if (!this.stopEditWhenClickOnWindow) {
                    var i = this;
                    this.stopEditWhenClickOnWindow = function(t) {
                        n(i.html, t.target) && i.hide()
                    }
                }
                window.addEventListener("mousedown", this.stopEditWhenClickOnWindow, !0),
                this.dom.addEventListener(e,
                function(e) {
                    t.stopEvent(e)
                },
                !1)
            }
            for (var o = 0,
            r = this.items.length; r > o; o++) {
                var s = this.renderItem(this.items[o]);
                this.dom.appendChild(s)
            }
        },
        html2Escape: function(t) {
            return t.replace(/[<>&"]/g,
            function(t) {
                return {
                    "<": "&lt;",
                    ">": "&gt;",
                    "&": "&amp;",
                    '"': "&quot;"
                } [t]
            })
        },
        renderItem: function(e) {
            var n = document.createElement("li");
            if (n.setAttribute("role", "presentation"), e == r.Separator) return n.className = r.Separator,
            n.innerHTML = " ",
            n;
            if (t.isString(e)) return n.innerHTML = '<a role="menuitem" tabindex="-1" href="#">' + this.html2Escape(e) + "</a>",
            n;
            e.selected && (n.style.backgroundPosition = "3px 5px", n.style.backgroundRepeat = "no-repeat", n.style.backgroundImage = "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAPklEQVQ4y2P4//8/AyWYYdQA7AYAAZuamlo7ED+H4naQGNEGQDX/R8PtpBjwHIsBz+lqAGVeoDgQR1MiaRgAnxW7Q0QEK0cAAAAASUVORK5CYII=')");
            var i = document.createElement("a");
            if (i.setAttribute("role", "menuitem"), i.setAttribute("tabindex", "-1"), i.setAttribute("href", "javascript:void(0)"), n.appendChild(i), e.html) i.innerHTML = e.html;
            else {
                var o = e.text || e.name;
                o && (i.innerHTML = this.html2Escape(o))
            }
            var a = e.className;
            a && (n.className = a);
            var s = e.action,
            l = this,
            h = function(n) {
                s && s.call(e.scope, n, e),
                t.isIOS || n.target.focus(),
                setTimeout(function() {
                    l.hide()
                },
                100)
            };
            return t.isTouchSupport ? i.ontouchstart = h: n.onclick = h,
            n
        },
        getMenuItems: function(e, n) {
        	return;//屏蔽右击
            var i = [];
            if (n) {
                var o = n instanceof t.ShapeNode,
                r = (n instanceof t.Group, !o && n instanceof t.Node, n instanceof t.Edge);
                if (i.push({
                    text: getI18NString("Rename"),
                    action: function() {
                        t.prompt(getI18NString("Input Element Name"), n.name || "",
                        function(t) {
                            null !== t && (n.name = t)
                        })
                    }
                }), r) {
                    var a = n.getStyle(t.Styles.EDGE_LINE_DASH) || t.DefaultStyles[t.Styles.EDGE_LINE_DASH];
                    i.push({
                        text: getI18NString(a ? "Solid Line": "Dashed Line"),
                        action: function() {
                            n.setStyle(t.Styles.EDGE_LINE_DASH, a ? null: [5, 3])
                        }
                    }),
                    i.push({
                        text: getI18NString("Line Width"),
                        action: function() {
                            t.prompt(getI18NString("Input Line Width"), n.getStyle(t.Styles.EDGE_WIDTH) || t.DefaultStyles[t.Styles.EDGE_WIDTH],
                            function(e) {
                                null !== e && (e = parseFloat(e), n.setStyle(t.Styles.EDGE_WIDTH, e))
                            })
                        }
                    }),
                    i.push({
                        text: getI18NString("Line Color"),
                        action: function() {
                            t.prompt(getI18NString("Input Line Color"), n.getStyle(t.Styles.EDGE_COLOR) || t.DefaultStyles[t.Styles.EDGE_COLOR],
                            function(e) {
                                null !== e && n.setStyle(t.Styles.EDGE_COLOR, e)
                            })
                        }
                    })
                } else if(o){
				   var a = n.getStyle(t.Styles.SHAPE_LINE_DASH) || t.DefaultStyles[t.Styles.SHAPE_LINE_DASH];
                    i.push({
                        text: getI18NString(a ? "Solid Line": "Dashed Line"),
                        action: function() {
                            n.setStyle(t.Styles.SHAPE_LINE_DASH, [5,3])
                        }
                    }),
                    i.push({
                        text: getI18NString("Line Width"),
                        action: function() {
                            t.prompt(getI18NString("Input Line Width"), n.getStyle(t.Styles.SHAPE_STROKE) || t.DefaultStyles[t.Styles.SHAPE_STROKE],
                            function(e) {
                                null !== e && (e = parseFloat(e), n.setStyle(t.Styles.SHAPE_STROKE, e))
                            })
                        }
                    }),
                    i.push({
                        text: getI18NString("Line Color"),
                        action: function() {
                            t.prompt(getI18NString("Input Line Color"), n.getStyle(t.Styles.SHAPE_STROKE_STYLE) || t.DefaultStyles[t.Styles.SHAPE_STROKE_STYLE],
                            function(e) {
                                null !== e && n.setStyle(t.Styles.SHAPE_STROKE_STYLE, e)
                            })
                        }
                    })
				}else n.parent instanceof t.Group && i.push({
                    text: getI18NString("Out of Group"),
                    action: function() {
                        n.parent = null
                    }
                });
                i.push(t.PopupMenu.Separator),
                i.push({
                    text: getI18NString("Send to Top"),
                    action: function() {
                        n.zIndex = 1,
                        e.sendToTop(n),
                        e.invalidate()
                    }
                }),
                i.push({
                    text: getI18NString("Send to Bottom"),
                    action: function() {
                        n.zIndex = -1,
                        e.sendToBottom(n),
                        e.invalidate()
                    }
                }),
                i.push({
                    text: getI18NString("Reset Layer"),
                    action: function() {
                        n.zIndex = 0,
                        e.invalidate()
                    }
                }),
                i.push(t.PopupMenu.Separator)
            }
            i.push({
                text: getI18NString("Clear Graph"),
                action: function() {
                    e.clear()
                }
            }),
            i.push(t.PopupMenu.Separator),
            i.push({
                text: getI18NString("Zoom In"),
                action: function(t) {
                    var n = e.globalToLocal(t);
                    e.zoomIn(n.x, n.y, !0)
                }
            }),
            i.push({
                text: getI18NString("Zoom Out"),
                action: function(t) {
                    var n = e.globalToLocal(t);
                    e.zoomOut(n.x, n.y, !0)
                }
            }),
            i.push({
                text: getI18NString("1:1"),
                action: function(t) {
                    e.globalToLocal(t);
                    e.scale = 1
                }
            }),
            i.push(t.PopupMenu.Separator);
            for (var s = e.interactionMode,
            l = [/*{
                text: getI18NString("Pan Mode"),
                value: t.Consts.INTERACTION_MODE_DEFAULT
            },
            {
                text: getI18NString("Rectangle Select"),
                value: t.Consts.INTERACTION_MODE_SELECTION
            }*/], h = 0, u = l.length; u > h; h++) {
                var d = l[h];
                d.value == s && (d.selected = !0),
                d.action = function(t, n) {
                    e.interactionMode = n.value
                },
                i.push(d)
            }
            return i.push(t.PopupMenu.Separator),
            i.push(),
            i
        }
    },
    Object.defineProperties(r.prototype, {
        items: {
            get: function() {
                return this._items
            },
            set: function(t) {
                this._items = t,
                this._invalidateFlag = !0
            }
        }
    });
    var s = {
        onstart: function(t, e) {
            e._popupmenu.hide()
        }
    };
    t.isTouchSupport && (s.onlongpress = function(t, e) {
        o(t, e)
    }),
    Object.defineProperties(t.Graph.prototype, {
        popupmenu: {
            get: function() {
                return this._popupmenu
            },
            set: function(t) {
                this._popupmenu != t && (this._popupmenu = t, this._contextmenuListener || (this._contextmenuListener = s, this.addCustomInteraction(this._contextmenuListener)/*, this.html.oncontextmenu = function(t) {
                //    o(t, this)
                }.bind(this)*/))
            }
        }
    }),
    t.PopupMenu = r
} (Q, jQuery),
!
function(t) {
    function e(t, e, n, i, o) {
        this.getter = n,
        this.setter = i,
        this.scope = o,
        this.property = t,
        this.createHtml(e)
    }
    function n() {
        t.doSuperConstructor(this, n, arguments)
    }
    function i(t, e) {
        var n = t._classPath || t._tempName;
        return n || (n = t._tempName = "class-" + y++),
        e ? _[n] = {
            "class": t,
            properties: {}
        }: _[n]
    }
    function o(e, n) {
        return n == t.Consts.PROPERTY_TYPE_STYLE ? "S:" + e: n == t.Consts.PROPERTY_TYPE_CLIENT ? "C:" + e: e
    }
    function r(e, n, r) {
        var a = i(e, !0);
        n.forEach(function(e) {
            var n;
            if (e.style) e.propertyType = t.Consts.PROPERTY_TYPE_STYLE,
            e.name = e.style;
            else if (e.client) e.propertyType = t.Consts.PROPERTY_TYPE_CLIENT,
            e.name = e.client;
            else {
                if (!e.name) return;
                e.propertyType = t.Consts.PROPERTY_TYPE_ACCESSOR
            }
            var n = e.key = o(e.name, e.propertyType);
            e.groupName || (e.groupName = r || "Element"),
            a.properties[n] = e
        })
    }
    function a(t) {
        var e = {};
        for (var n in _) if (t instanceof _[n]["class"]) {
            var i = _[n].properties;
            for (var o in i) {
                var r = i[o];
                "none" == r.display ? delete e[o] : e[o] = r
            }
        }
        return new s(e)
    }
    function s(t) {
        this.properties = t;
        var e = {};
        for (var n in t) {
            var i = t[n].groupName,
            o = e[i];
            o || (o = e[i] = {}),
            o[n] = t[n]
        }
        this.group = e
    }
    function l(e, n, i, o) {
        return o && o != t.Consts.PROPERTY_TYPE_ACCESSOR ? o == t.Consts.PROPERTY_TYPE_STYLE ? e.getStyle(n, i) : o == t.Consts.PROPERTY_TYPE_CLIENT ? n.get(i) : void 0 : n[i]
    }
    function h(e, n, i, o) {
        return o && o != t.Consts.PROPERTY_TYPE_ACCESSOR ? o == t.Consts.PROPERTY_TYPE_STYLE ? n.setStyle(i, e) : o == t.Consts.PROPERTY_TYPE_CLIENT ? n.set(i, e) : void 0 : n[i] = e
    }
    function u(e, n) {
        this._formItems = [],
        this.form = t.createElement({
            "class": "form-horizontal",
            parent: n,
            tagName: "form"
        }),
        _div = t.createElement({
            "style": "position: absolute;bottom: 5px;text-align: center;margin: auto;width: 100%;",
            parent: n,
            tagName: "div"
        })
        ,
         /*t.createElement({
            "class" : "sumbitdata",
            parent: _div,
            tagName: "button",
            html : "提交"
        })*/
         t.createElement({
            "style": "margin-left: 6px;",
            "class" : "deletedata",
            parent: _div,
            tagName: "button",
            html : "删除"
        })
        this.graph = e,
        e.dataPropertyChangeDispatcher.addListener(function(t) {
            this.onDataPropertyChange(t)
        }.bind(this)),
        e.selectionChangeDispatcher.addListener(function() {
        	console.log('click');
        	//图标选中监听
        	//如果是只读模式，则直接return ，不显示详情
        	if($("#readOnly").val()==0){
        		//只读
        		return;
        	}
        	var data = arguments[0].data;
        	data = $.type(data) == "array" ? data[0] : data;
        	var _id = arguments[0].data.id; 
        	var icondata = data;
        	//删除查询选中状态
        	icondata.setStyle(Q.Styles.BORDER, null);
        	icondata.setStyle(Q.Styles.BORDER_COLOR,null);
        	icondata.setStyle(Q.Styles.PADDING, null);
        	delete icondata.kind_type;
        	//从后台获取图标对应的物理设备信息
        	$.ajax({
        		url:context_path+"/visual/getDeviceByIconId",
        		type:"POST",
        		dataType:"json",
        		data:{iconId:_id},
        		async:false,
        		success:function(data){
        			if(data!=null){
        				var device = data.DEVICE;
        				if(!device) return;
        				icondata.ip =device.ip || device.rip || device.tgip || device.gip || "";
        				icondata.mac =device.addr||"";
        				icondata.name =device.name||"";
        				icondata.part =device.port || device.tgport || "";
        				icondata.type =device.type;
        			}
        		}
        	});
        	_evt = arguments[0]._event;
        	var _this =this;
        	$.clickfun = setTimeout(function(){
        		window.event = _evt;
        		_this.datas = _this.graph.selectionModel.toDatas();
        	},500);
        }.bind(this));
      
    }
    function d(t) {
        return 0 | t
    }
    function c(t, e) {
        return t ? "point" == e ? d(t.x) + "," + d(t.y) : "size" == e ? d(t.width) + "," + d(t.height) : t.toString() : t
    }
    function p(t, e) {
        if ("number" == e) return parseFloat(t) || 0;
        if ("boolean" == e) return t ? !0 : !1;
        if ("point" != e) {
            if ("size" != e) return t;
            var n = t.split(",");
            if (2 == n.length) {
                var i = parseFloat(n[0]) || 0,
                o = parseFloat(n[1]) || 0;
                if (i && o) return {
                    width: i,
                    height: o
                }
            }
        } else {
            var n = t.split(",");
            if (2 == n.length) return {
                x: parseFloat(n[0] || 0),
                y: parseFloat(n[1]) || 0
            }
        }
    }
    e.prototype = {
        _getValue: function() {
            return this.getter.call(this.scope)
        },
        update: function() {
            this.value = this._getValue()
        },
        setValue: function(t) {
            this.input.value = c(t, this.property.type)
        },
        createHtml: function(e) {
            var n = this.property,
            i = t.createElement({
                tagName: "input",
                "class": "form-control",
                type: n.type,
                min: n.min,
                max: n.max,
                parent: e,
                name : n.name
            });
            this.input = i,
            this.update(),
            $(i).on("input",
            function() {
                this.ajdusting || this.setter.call(this.scope, this)
            }.bind(this));
            $(i).on("blur",function(){
            	var _type = this.property.name;
            	var f = vali[_type];
            	if($.type(f) === "function"){
            		if( f(this.value) == false ){
            			 if( !$(this.input).hasClass("error") ){
            				 var _operationDes = _type=="name"?"":"正确的";
            				 var _operationElment = _type=="part"?"port":(_type=="name"?"名称":_type);
                		   	 $(this.input).addClass("error").after('<div class="help-block" style="display: block;">请填写'+_operationDes+_operationElment+'</div>');
            			 }
            		}else{
            			 $(this.input).removeClass("error");
            			 $(this.input).parent().find(".help-block").remove();
            		} 
            	}
            }.bind(this));
        }
    },
    Object.defineProperties(e.prototype, {
        value: {
            get: function() {
                return p(this.input.value, this.property.type)
            },
            set: function(t) {
                this.ajdusting = !0,
                this.setValue(t),
                this.ajdusting = !1
            }
        }
    }),
    n.prototype = {
        createHtml: function(e) {
            var n = t.createElement({
                tagName: "input",
                "class": "form-control",
                parent: e
            });
            t.createElement({
                tagName: "span",
                parent: e,
                "class": "input-group-addon",
                html: "<i></i>"
            }),
            this.input = n,
            this.update(),
            $(e).colorpicker().on("changeColor.colorpicker",
            function() {
                this.ajdusting || this.setter.call(this.scope, this)
            }.bind(this));
  
        }
    },
    t.extend(n, e);
    var f = [
    /*,
    {
        style: t.Styles.LABEL_FONT_SIZE,
        type: "number",
        displayName: "Font Size"
    },
    {
        style: t.Styles.LABEL_COLOR,
        type: "color",
        displayName: "Label Color"
    },
    {
        style: t.Styles.RENDER_COLOR,
        type: "color",
        displayName: "Render Color"
    }*/],
    g = [{
        name: "name",
        displayName: "Name"
    },{
        name: "ip",
        displayName: "IP地址"
    },{
        name: "mac",
        displayName: "设备地址"
    },{
        name: "part",
        displayName: "端口"
    }
         /*{
        name: "size",
        type: "size",
        displayName: "Size"
    },
    {
        name: "location",
        type: "point",
        displayName: "Location"
    },
    {
        name: "rotate",
        type: "number",
        displayName: "Rotate"
    },
    {
        style: t.Styles.BORDER,
        type: "number",
        displayName: "Border"
    },
    {
        style: t.Styles.BORDER_COLOR,
        type: "color",
        displayName: "Border Color"
    }*/],
    m = [{
        style: t.Styles.BORDER,
        display: "none"
    },
    {
        name: "name",
        displayName: "Name"
    },
    {
        style: t.Styles.EDGE_WIDTH,
        type: "number",
        displayName: "连线宽度"
    },
    {
        style: t.Styles.EDGE_COLOR,
        type: "color",
        displayName: "连线颜色"
    }],
    v = [{
        name: "size",
        display: "none"
    },
    {
        style: t.Styles.LABEL_SIZE,
        type: "size",
        displayName: "Size"
    },
    {
        style: t.Styles.RENDER_COLOR,
        display: "none"
    },
    {
        style: t.Styles.BACKGROUND_COLOR,
        type: "color",
        displayName: "Background Color"
    }],
    x = [{
        name: "name",
        displayName: "Name"
    },{
        style: t.Styles.SHAPE_STROKE,
        type: "number",
        displayName: "宽度"
    },{
        style: t.Styles.SHAPE_STROKE_STYLE,
        type: "color",
        displayName: "颜色"
    }],
    _ = {},
    y = 0;
    r(t.Element, f, "Element"),
    //物理设备
    r(t.Node, g, " "),
    r(t.Edge, m, "连线"),
    r(t.Text, v, "Text"),
    r(t.ShapeNode, x, "分割线"),
    s.prototype = {
        contains: function(t, e) {
            var n = o(t, e);
            return this.properties[n]
        }
    };
    var b = function(t, i, o, r, a) {
        var s = t.type;
        return "color" == s ? new n(t, i, o, r, a) : new e(t, i, o, r, a)
    };
    u.prototype = {
        _formItems: null,
        onValueChange: function(t, e) {
            this.setValue(t, e)
        },
        adjusting: !1,
        _containsElement: function(t) {
            for (var e in this.datas) if (e == t) return ! 0
        },
        _containsProperty: function(t, e) {
            return this.propertyGroup && this.propertyGroup.contains(t, e)
        },
        _cellEditors: null,
        _getCellEditors: function(t, e) {
            if (this._cellEditors) {
                var n = o(t, e);
                return this._cellEditors[n]
            }
        },
        onDataPropertyChange: function(e) {
            if (!this.adjusting) {
                if (!this.datas || !this.datas.length) return null;
                var n = e.source;
                if (!this._containsElement(n)) {
                    var i = this._getCellEditors(e.kind, e.propertyType);
                    if (!i) return;
                    t.isArray(i) || (i = [i]),
                    i.forEach(function(t) {
                        t.update()
                    })
                }
            }
        },
        clear: function() {
            $(".colorpicker-element").colorpicker("hide"),
            this.form.innerHTML = "",
            this._formItems = [],
            this._cellEditors = null;
            var ele = window.event && this.graph.getElementByMouseEvent(window.event);
            var selectable = ele ? ele['selectable'] : false;
            if(!$(this.form.parentElement).is(":hidden") && selectable === false){
            	 $(this.form.parentElement).slideUp();
            }else{
            	 $(".sumbitdata").off("click");
                 var _node = this.graph.getElementByMouseEvent(window.event) /*|| this._datas*/;
                 
                 $(".deletedata").off("click");
                 $(".deletedata").on("click",(function(graph,_node){
              	   return function(){
              		   //删除按钮点击事件
              		 Dialog.confirm('确定删除？',
            				   function () {
              			 $.ajax({
                			   url:context_path+"/visual/delIconInfo",
                			   type:"POST",
                			   data:{iconId:_node.id,pid:selectProjectId},
                			   dataType:"json",
                			   success:function(data){
                				   if(!data.result){
                					   Dialog.alert("操作失败！");
                				   }
                			   }
                		   });
                		   graph.graphModel.remove(_node);    		
                		   var t = graph.exportJSON(true);
                		   graph.clear();
                		   graph.parseJSON(t);
                		   $(this).parents(".graph-editor__property").slideUp();
            		   },
            		   {
            			   title: '操作提示',
            			   yesLabel: '确定',
            			   noLabel: '取消'
            		   }
            		   );
              	   }
                 }(this.graph,_node)));
            }
        },
        createItem: function(e, n) {
            if($(this.form.parentElement).is(":hidden")){
                $(this.form.parentElement).slideDown();
            }
            var i = t.createElement({
                "class": "form-group",
                parent: e
            }),
            
            r = (t.createElement({
                parent: i,
                tagName: "label",
                "class": "col-sm-5 control-label font-small",
                html: getI18NString(n.displayName || n.name)
            }), t.createElement({
                parent: i,
                "class": "input-group input-group-sm col-sm-7"
            })),
            a = b(n, r,
            function() {
                return this.getValue(n)
            }.bind(this),
            function(t) {
                this.onValueChange(t.value, n)
            }.bind(this)),
            s = o(n.name, n.propertyType);
            this._cellEditors || (this._cellEditors = {});
            var l = this._cellEditors[s];
            return l ? l.push(a) : this._cellEditors[s] = [a],
            i
        },
        setValue: function(e, n) {
            return this.datas && this.datas.length ? (this.adjusting = !0, n.type && "string" != n.type && t.isString(e) && (e = p(e, n.type)), this.datas.forEach(function(t) {
                var i = l(this.graph, t, n.name, n.propertyType);
                i !== e && h(e, t, n.name, n.propertyType)
            },
            this), void(this.adjusting = !1)) : null
        },
        getValue: function(t) {
            return this.datas && this.datas.length ? 1 == this.datas.length ? l(this.graph, this.datas[0], t.name, t.propertyType) || "": void 0 : null
        },
        createItemGroup: function(e, n) {

            var i = t.createElement({
                "class": "class-group",
                parent: this.form
            });
            t.createElement({
                tagName: "h4",
                parent: i,
                html: e
            });
	        if(Q.exportJSON(this._datas)[0]._className == "Q.Node"){
	        	var iconSetting = this.datas[0];
	            var deviceType = this.datas[0].type;   //设备类型
	            var iconId = this.datas[0].id;         //图标id
		        var $itemId = this._datas[0].itemid;   //设备图标类型id
	        	var spliteDiv='<div style="height:5px;" class="form-group"></div>';
	        	var icontypeName = "图标类型";
	        	//添加一个标题元素元素，用来标明是什么类型的图标
	            t.createElement({
	            	parent : i,
	            	tagName : "div",
	            	html : '<div class="form-group"><label style="text-align:left;padding-left:40px;padding-bottom:6px;color:#3e3e3e;border-bottom:solid #d51c2e 2px;" id="iconTypeLable" class="col-sm-12 control-label" title="图标类型">'+icontypeName+'</label></div>'
	            });
	          //添加一个元素
	            t.createElement({
	            	parent : i,
	            	tagName : "div",
	            	html : spliteDiv
	            });
	        	
	            t.createElement({
	            	parent : i,
	            	tagName : "div",
	            	html : '<div class="form-group"><label style="" class="col-sm-5 control-label font-small" title="选择设备">选择设备</label><div class="select-device input-group input-group-sm col-sm-7" style="display: inline-block;" ></div></div>'
	            });
	            
	            $(".select-device").select2({
	            	placeholder: "选择设备",
	        		minimumInputLength:0,   //至少输入n个字符，才去加载数据
	        	    allowClear: true,  //是否允许用户清除文本信息
	        		delay: 250,
	        		formatNoMatches:"没有结果",
	        		formatSearching:"搜索中...",
	        		formatAjaxError:"加载出错啦！",
	        		ajax : {
	        			url: context_path+"/visual/getDListPdByType",
	        			type:"POST",
	        			dataType : 'json',
	        			delay : 250,
	        			data: function (term,pageNo) {     //在查询时向服务器端传输的数据
	        	            term = $.trim(term);
	                        return {
	                        	icon_id : iconId,
	                        	queryString: term,    //联动查询的字符
	                        	pageSize: 15,    //一次性加载的数据条数
	                            pageNo:pageNo,    //页码
	                            time:new Date()   //测试
	                         }
	        	        },
	        	        results: function (data,pageNo) {
	        	        	var res = data.result;
	        	            if(res.length>0){   //如果没有查询到数据，将会返回空串
	        	               var more = (pageNo*15)<data.total; //用来判断是否还有更多数据可以加载
	        	               return {
	        	                    results:res,more:more
	        	                 };
	        	            }else{
	        	        		return {
	        	                    results:{}
	        	                 };
	        	        	}
	        	        },
	        			cache : true
	        		}
	
	            }).on("change",function(){
	            	var $selectdata = $(".select-device").select2("data");
	            	//将选择的硬件id与图标关联
	            	$.ajax({
	            		url:context_path+"/visual/bindIconDevice",
	            		type:"POST",
	            		dataType:"json",
	            		data:{dId:$selectdata.id,iconId:iconId},
	            		async:false,
	            		success:function(data){
	            			if(data.result){
	            				iconSetting.setStyle(Q.Styles.RENDER_COLOR, "#ffffff");
	            				if(data.msg!=null){
	            					var device = data.msg;
	            					for(var j in device){
	            						if(j=="name"){
	                            			$("[name='name']").val(device[j]);
	                                		$("[name='name']").trigger("input");
	                            		}else if(j=="port"|| j=="tgport"){
	                            			$("[name='part']").val(device[j]);
	                                		$("[name='part']").trigger("input");
	                            		}else if(j=="ip"||j=="tgip" || j=="gip"){
	                            			$("[name='ip']").val(device[j]);
	                                		$("[name='ip']").trigger("input");
	                            		}
	                            	}
	            					$("[name='mac']").val(device["mac"]);
	            					
	            					if(device.type!=null){
	            						if(!device.product_type) return;
	            						$(".select-datatype").select2("data",{id : device.type,
	            	    					text : device.product_type+"["+device.product_name+"]"});
	            					}
	            				}
	            			}
	            		}
	            	});
	            });
	            
	            var selectDiv = '<div class="form-group"><label style="" class="col-sm-5 control-label font-small" title="设备型号">设备型号</label><div class="select-datatype input-group input-group-sm col-sm-7" style="display: inline-block;"></div></div>';
		        t.createElement({
	            	parent : i,
	            	tagName : "div",
	            	html : selectDiv
	            });
		        
		        if($(".select-datatype")){
		        	$(".select-datatype").select2({
		            	placeholder: "请选择型号",
		        		minimumInputLength:0,   //至少输入n个字符，才去加载数据
		        	    allowClear: true,  //是否允许用户清除文本信息
		        	    formatNoMatches:"没有结果",
		        		formatSearching:"搜索中...",
		        		formatAjaxError:"加载出错啦！",
		        		ajax : {
		        			url: context_path+"/visual/getDType",
		        			type:"POST",
		        			dataType : 'json',
		        			delay : 250,
		        			data: function (term,pageNo) {     //在查询时向服务器端传输的数据
			                    return {
			                    	icon_id : iconId,
			                        time:new Date()   //测试
			                     }
			    	        },
		        	        results: function (data,pageNo) {
	        	        		return {results:data.result};
		        	        },
		        			cache : true
		        		}
			        	
		            });
		        }
		        
	         }
	        $.ajax({
        		url:context_path+"/visual/getDeviceByIconId",
        		type:"POST",
        		dataType:"json",
        		data:{iconId:iconId},
        		async:false,
        		success:function(data){
        			if(!data) return;
        			if(data.ICON){
        				var _$icon = data.ICON;
        				var $iconTypeName = "";
        				if(_$icon.device_type_id.startsWith("0101")){
        					$iconTypeName = "网关类型";
    		        	}else if(_$icon.device_type_id.startsWith("0102")){
    		        		$iconTypeName = "路由类型";
    		        	}else if(_$icon.device_type_id.startsWith("0103")){
    		        		$iconTypeName = "终端类型";
    		        	}else if(_$icon.device_type_id.startsWith("0201")){
    		        		$iconTypeName = "RFID回收箱类型";
    		        	}else if(_$icon.device_type_id.startsWith("0202")){
    		        		$iconTypeName = "一体式读写器类型";
    		        	}else if(_$icon.device_type_id.startsWith("0203")){
    		        		$iconTypeName = "四通道读写器类型";
    		        	}else if(_$icon.device_type_id.startsWith("0301")){
    		        		$iconTypeName = "主控制器类型";
    		        	}else if(_$icon.device_type_id.startsWith("0302")){
    		        		$iconTypeName = "层控制器类型";
    		        	}else if(_$icon.device_type_id.startsWith("0303")){
    		        		$iconTypeName = "位控制器类型";
    		        	}
        				$("#iconTypeLable").html($iconTypeName);
        			}
        			var device = data.DEVICE;
        			if(!device) return;
        			if(device.type){
        				if(device.product_name){
        					$(".select-datatype").select2("data",{id : device.type,
    	    					text : device.product_type+"["+device.product_name+"]"});
        				}
					}
        			
        		}
        	});
            for (var e in n) {
            	//路由和终端设备不需要显示IP和端口信息
                if(deviceType.startsWith("0102")||deviceType.startsWith("0103")){
                   if(n[e].name=="ip"||n[e].name=="park") return;
                }
            	this.createItem(i, n[e]);
            }
            if(Q.exportJSON(this._datas)[0]._className == "Q.Node"){
            	$("[name='name']").attr("disabled", true);
                $("[name='mac']").attr("disabled", true);
                $("[name='ip']").attr("disabled", true);
                $("[name='part']").attr("disabled", true);
                $(".select-datatype").select2("readonly", true);
            }
        }
    },
    Object.defineProperties(u.prototype, {
        datas: {
            get: function() {
                return this._datas
            },
            set: function(e) {
                if (this._datas != e && (e && !t.isArray(e) && (e = [e]), this._datas = e, this.clear(), e.length && 1 == e.length)) {
                    this.propertyGroup = a(e[0]);
                    var n = this.propertyGroup.group;
                    if(Q.exportJSON(this._datas)[0]._className == "Q.ShapeNode"){
                      delete n["属性"];
                    }
                    for (var i in n) this.createItemGroup(i, n[i]);
                    
                }
            }
        }
    }),
    t.PropertyPane = u;
    var validata = function(){
    	$("input.form-control").trigger("blur");
    	return !$("input.form-control.error").length
    };
    vali ={
    		"name" : function(val){
    			return !!val;
    		},
        	"ip" : function(val){
    		    var re =  /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;  
    			if(!re.test(val)){  
    			    return false;  
    			}  
    			return true; 
        	},
        	/*"mac" : function(val){
        		//mac地址正则表达式 
        		var reg_name= /([0-9a-fA-F]{2})(([/\s:][0-9a-fA-F]{2}){5})/; 
        		if(reg_name.test(val) && val.length ==17 ){
        			return true;
        		}else{
        			return false;
        		}
        	},*/
        	"part" : function(val){
    		    var re =  /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;     
    		    if (!re.test(val))  
    		    {  
    		      return false; 
    		    }  
    		    return true; 
    		}
      };
} (Q),
function(t, e) {
    "use strict";
    function n(t, e) {
        this._initEditor(t, e),
        this.loadDatas(this.options.data, this.options.callback ||
        function() {
            this.graph.moveToCenter()
        })
    }
    var i = function(e, n, i, o) {
        return t.createElement({
            "class": e,
            parent: n,
            tagName: i,
            html: o
        })
    },
    o = function(t, e) {
        var n = t.find(e);
        return n.length ? n[0] : void 0
    };
    e.fn.graphEditor = function(t) {
        return this.each(function() {
            var e = this.graphEditor;
			e || (this.graphEditor = e = new n(this, t)),e
			this.n =e;
            return e;
            
        })
    };
    var r = {};
    r[t.Styles.SHAPE_FILL_COLOR] = t.toColor(3435973836),
    r[t.Styles.SELECTION_COLOR] = "#888",
    r[t.Styles.SELECTION_SHADOW_BLUR] = 5,
    r[t.Styles.SELECTION_SHADOW_OFFSET_X] = 2,
    r[t.Styles.SELECTION_SHADOW_OFFSET_Y] = 2,
    n.prototype = {

        _initEditor: function(t, n) {
            this.options = n = n || {},
            this.dom = t,
            e(t).addClass("layout graph-editor"),
            e(t).hide(),
            this.createGraph(this.options.styles || r),
            this.createToolbox(this.options.images),
            this.createToolbar(n),
            this.createPropertyPane(n),
            this.createJSONPane(),
            e(t).borderLayout(),
            this.toolbar && this.initToolbar(this.toolbar, this.graph),
            this.initContextMenu(this.graph),
           // $(this.dom.firstChild).css("width","90%");
            $(".graph-editor__toolbox").css("height","auto");
            $(".graph-editor__toolbox .group .group__title").append("<i class='fa fa-chevron-down'></i>");
            $(".graph-editor__canvas").css("left","5px");
            $(".graph-editor__toolbar").css("margin-left","20px");
            $(".graph-editor__toolbox").css("width","60px");
            window.addEventListener("beforeunload", this.onbeforeunload.bind(this))
           
        },
        onbeforeunload: function() {},
        _getFirst: function(t) {
            return o(e(this.dom), "." + t)
        },
        toolbar: null,
        toolbox: null,
        propertyPane: null,
        graph: null,
        createGraph: function(n) {
            var o = this._getFirst("graph-editor__canvas");
            o || (o = i("graph-editor__canvas", this.dom), o.setAttribute("data-options", 'region:"center"'));
            var r = this.graph = new t.Graph(o);
            return r.allowEmptyLabel = !0,
            r.originAtCenter = !1,
            r.editable = !0,
            r.styles = n,
            r.getDropInfo = function(e, n) {
                return n ? t.parseJSON(n) : void 0
            },
            r.dropAction = function() {
                return this.dropAction.apply(this, arguments)
            }.bind(this),
            e(o).bind("size.change",
            function() {
                r.updateViewport()
            }), 
            r
        },
        dropAction: function(e, n, i) {
            if (i.ondrop) {
                var o = window[i.ondrop];
                if (o instanceof Function) return o.call(this, e, this.graph, n, i),
                t.stopEvent(e),
                !1
            }
        },
        createToolbar: function() {
            var t = this._getFirst("graph-editor__toolbar");
			var _toolbar = t ? this.toolbar = t: (this.toolbar = t = i("graph-editor__toolbar", this.dom), t.setAttribute("data-options", 'region:"north", height: 40'), t);
			/*var _logo = "<div class='logo'><div class='title'></div><h2>可视化视图编辑器</h2></div>"
			$(_toolbar).append('<div class="btn-group"><div class="btn btn-default btn-sm ">导入背景图</div></div>');*/
            return _toolbar;
        },
        createToolbox: function(e) {
            var n = document.createElement("div");
            this.dom.appendChild(n),
            n.setAttribute("data-options", "region:'west', width:'60'"),
            this.toolbox = new t.ToolBox(this.graph, n, e)
        },
        createPropertyPane: function(e) {
            var n = this._getFirst("graph-editor__property");
            return n || (n = i("graph-editor__property", this.dom), n.setAttribute("data-options", "region:'east', width: '260',right: 0"),$(n).hide()),
            this.propertyPane = new t.PropertyPane(this.graph, n, e)
        },
        getJSONTextArea: function() {
            return o(e(this.jsonPane), "textarea")
        },
        loadJSONFile: function(e) {
            e[0] && t.readerSingleFile(e[0], "json",
            function(t) {
                return t ? (this.graph.clear(), void this.graph.parseJSON(t)) : void Dialog.alert(getI18NString("json file is empty"))
            }.bind(this))
        },
        loadFile: function(e) {
        	if(selectProjectId<0){
        		Dialog.alert("请选择一个工程！");
        		return;
        	}
        	var fileName = e[0].name;
        	var i = fileName.lastIndexOf(".");
        	if(i > -1){
        		var ext = fileName.substring(i+1);
        		if (ext && /^(jpg|png|bmp|svg)$/.test(ext.toLowerCase())){
        			
    			} else {
    				Dialog.alert("请上传格式为 jpg|png|bmp|svg 的文件！");
    				return false;	
    			}
        	}
        	var mapObject = this.graph;
        	var mapJson = this.graph.exportJSON()
           //文件上传
        	$.ajaxFileUpload({   
        		url:context_path + "/visual/bgUploadFile",//用于文件上传的服务器端请求地址   
        		secureuri:false,//一般设置为false
        		fileElementId:"upload-file-btn",//文件上传空间的id属性  <input type="file" id="file" name="file" />   
        		dataType: 'json',//返回值类型 一般设置为json   
        		data:{pid:selectProjectId},
        		uploadtype:$("#upload-file-btn").attr("uploadtype"),
        		success: function (data, status) {     //服务器成功响应处理函数  
        			if(data.msg!=null && data.msg!=""){
        				mapJson.datas[0].json.image = data.msg+"?"+Math.random(); 
        			}
        			//重新加载可视化地图
        			mapObject.clear();
    				mapObject.parseJSON(mapJson);
        		},   
        		error: function (data, status, e){//服务器响应失败处理函数  
        			Dialog.alert("上传失败："+e);
        		}   
        	});
//           this.graph.clear();
//           var graph = this.graph;
//           setTimeout(function(){
//        	   var svg = graph.createNode("");
//               svg.movable = false;
//               svg.selectable = false;
//           	   svg.editable = false;
//           	   svg.resizable = false;
//           	   svg.zIndex = -1;
//               svg.image = context_path+"/static/yzgr.svg";
//           	   svg.name="map";
//               svg.size = {width: 900};
//           },2000);
        	
         
        },
        exportJSONFile: function(t) {
            if (t) {
                var e = this.graph.name || "graph",
                n = this.graph.exportJSON(!0),
                i = new Blob([n], {
                    type: "text/plain;charset=utf-8"
                });
                t(i, e + ".json")
            }
        },
        exportJSON: function(t) {
            if (t && this.jsonPane) {
                var e = this.graph.exportJSON(!0, {
                    space: "  "
                });
                return this.getJSONTextArea().value = e
            }
            return this.graph.exportJSON.apply(this.graph, arguments)
        },
        submitJSON: function() {
            var t = this.getJSONTextArea().value;
            this.graph.clear();
            this.graph.parseJSON(t)
        },
        loadDatas: function(e, n) {
            if (e) {
                if (t.isString(e)) return void t.loadJSON(e,
                function(t) {
                    this.graph.parseJSON(t.json || t),
                    n instanceof Function && n.call(this, this)
                }.bind(this),
                function() {
                    n instanceof Function && n.call(this, this)
                }.bind(this));
                this.graph.parseJSON(e)
            }
            n instanceof Function && n.call(this, this)
        },
        onsave: function(t) {
            return t ? Dialog.alert(getI18NString("Save Error")) : void Dialog.alert(getI18NString("Save Success"))
        },
        save: function() {
            if (this.options.saveService) {
                var t = this.options.saveService,
                e = this.graph.exportJSON(!0),
                n = new XMLHttpRequest;
                n.open("post", t, !0),
                n.onerror = function(t) {
                    this.onsave(t)
                }.bind(this),
                n.onload = function(t) {
                    200 == t.target.status ? this.onsave(null, t) : this.onsave(t)
                }.bind(this),
                n.setRequestHeader("Content-Type", "application/json"),
                n.send(JSON.stringify({
                    name: this.name,
                    json: e
                }))
            }
        },
        createJSONPane: function() {
            var e = this._getFirst("graph-editor__json");
            if (e) return this.jsonPane = e;
            this.jsonPane = e = i("graph-editor__json", this.dom);
            var n = document.createElement("textarea");
            e.appendChild(n),
            n.spellcheck = !1;
            var o = i("graph-editor__json__buttons", e),
            r = [{
                name: getI18NString("Update"),
                action: this.exportJSON.bind(this, !0)
            },
            {
                name: getI18NString("Submit"),
                action: this.submitJSON.bind(this)
            }];
            return t.createButtonGroup(r, o),
            e.style.display = "none",
            e
        },
        initToolbar: function(e, n) {
            var i = [
            /*{
                name: getI18NString("Export JSON"),
                iconClass: "q-icon toolbar-json",
                action: this.showJSONPanel.bind(this)
            },*/
            {
                iconClass: "q-icon toolbar-upload",
                name: getI18NString("Load File ..."),
          //      action: this.loadJSONFile.bind(this),
                action: this.loadFile.bind(this),
                type: "file"
            }];
			window.saveAs = false;
            window.saveAs && i.push({
                iconClass: "q-icon toolbar-download",
                name: getI18NString("Download File"),
                action: this.exportJSONFile.bind(this, window.saveAs)
            }),
            this.options.saveService && i.push({
                iconClass: "q-icon toolbar-save",
                name: getI18NString("Save"),
                action: this.save.bind(this)
            }),
            t.createToolbar(n, e, {
                "export": i
            })
        },
        showExportPanel: function() {
            t.showExportPanel(this.graph)
        },
        showJSONPanel: function(t) {
            var n = t.target;
            e(n).hasClass("btn") || (n = n.parentNode);
            var i = e(n).hasClass("active");
            i ? e(n).removeClass("active") : e(n).addClass("active"),
            i = !i;
            var o = this.jsonPane;
            o.style.display = i ? "": "none",
            i && this.exportJSON(!0)
        },
        initContextMenu: function(e) {
            e.popupmenu = new t.PopupMenu
        }
    },
    window.localStorage && (n.prototype.loadLocal = function() {
        return localStorage.graph ? (this.graph.clear(), this.graph.parseJSON(localStorage.graph), !0) : void 0
    },
    n.prototype.saveLocal = function() {
        localStorage.graph = this.graph.exportJSON(!0)
    })
} (Q, jQuery);