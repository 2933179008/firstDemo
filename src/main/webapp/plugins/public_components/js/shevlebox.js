(function($){
	var background_src_h = context_path + "/plugins/public_components/img/h.png";
	var background_image_h  = '';
	var background_src_s1 = context_path + "/plugins/public_components/img/s1.png";
	var background_image_s1  = '';
	var background_src_s2 =  context_path + "/plugins/public_components/img/s2.png";
	var background_image_s2  = '';
	var background_src_s3 =  context_path + "/plugins/public_components/img/s3.png";
	var background_image_s3  = '';
	var _ceateShelves = function(x, y, goods,lightdata, graph, w){
	    this.graph  = graph;
	    this.x = x;
	    this.y = y;
	    this.w = w||2;
	    this.lightdata = lightdata;
	    this.goods = goods;
	    this.boxData = [];
	    this.ctlData = [];
	    this.macData =[];
	    this.place=[];
	    
	    
   };

	var _box = function(){
		 this.boxImg =   context_path +"/plugins/public_components/img/box_N.png";
		 var im  = document.createElement('img');
	     im.src = this.boxImg;
	     this.boxWidth  = im.width;
	    
	};
	_box.prototype = {
			//创建货架
			createBoxs : function(x, y, z){  //无值传递时，就是初始化，有值时就是自定义添加box, x列y行第z个货架加上一个货物
				var _x = x || "";
				var _y = y || "";
				var _self = this;
				var createBox = function(x, y,location){
					if( _self.boxData[location] ) return ;
					
					  var node = _self.graph.createNode( "", x, y+27 );
				      node.image = _self.boxImg;
				      node.zIndex =1;
				      node.movable = false;
				      node.selectable = false;
				      node.editable = false;
					  node.resizable = false;
				      node.set("sign","box-"+location);
				      _self.boxData[location] = node;
				};
				if( x && y && z ){
						var cx =  (_x -1)*_self._width - _self._width2  +  (2*z-1)* _self._width3; 
						var cy =  (_y-1)* _self._height -_self._height2 +20 ;
						if( _self.boxData[location] )  return;
						
						createBox( cx, cy, _x+"-"+_y+"-"+z ); //在(cx,cy)处绘制box
				}else{
					for(var i in _self.goods){
						var arr = i.split("");
						if(arr[0] - 1 < _self.x && arr[1] - 1 < _self.y){
							
							var cy =  (arr[1]-1) * _self._height -_self._height2 +20 ;
							for(var n = 0; n < _self.goods[i]; n++){
								var cx =  (arr[0] -1)*_self._width - _self._width2  + (2*n+1)* _self._width3; 
								createBox( cx, cy, arr[0]+"-"+arr[1]+"-"+(n+1)); //在(cx,cy)处绘制box
							}
							
						}
					}
				}
			},
			//删除货架
			delBox : function( location ){
				
				 if(this.boxData[location]){
					 this.graph.removeElement(this.boxData[location]); 
					 this.graph.graphModel.remove(this.boxData[location]); 
					 var slef = this;
					 delete slef.boxData[location];
					
				 }
			}
	}


	// 共x列， 共y列， json数据 {"12":2,"13":3} 对应第 一列第二行有2个箱子，第 一列第三行有3个箱子

var Shelves = function(x, y, goods,lightdata, graph,w){
	
	var _shelves = new _ceateShelves(x, y, goods,lightdata, graph,w);
	background_image_h  = new  Image();
	background_image_h.src = background_src_h;
	background_image_h.height = 55;
	background_image_h.width = 865;
	
	background_image_s1  = new  Image();
	background_image_s1.src = background_src_s1;
	background_image_s1.width =63;
	background_image_s1.height =250;
	
	background_image_s2  = new  Image();
	background_image_s2.src = background_src_s2;
	background_image_s2.width =63;
	background_image_s2.height =278;
	
	background_image_s3  = new  Image();
	background_image_s3.src = background_src_s3;
	background_image_s3.width =63;
	background_image_s3.height =249;

	 _shelves.setBackGround();
	 _shelves.box().createBoxs();
	 return _shelves;
  }

	_ceateShelves.prototype ={
			box : function(){
				_box.call(this);
				$.extend(this.__proto__, _box.prototype);
				return this;
			}, 
			
			setScale : function(){
				    var data  = this.graph.exportJSON();
					var im = background_image;
				    this.s = data.scale;
				    this.tx = data.tx;
				    this.ty = data.ty;
					this._width  = im.width * this.s ;
					this._height = im.height * this.s;
					this._width2 =  this._width * this.x / 2;
					this._height2 = im.height * this.s * this.y/ 2;
					this._width3 =  im.width * this.s  / (2 * this.w);
					this.x0 = this.tx - im.width * this.s / 2;
					this.y0 = this.ty - this._height2;
					
			},
			setBackGround : function(){
				this.graph.clear();
				var im = {
						width :  930, 
						height : 350 	
				};
				var w = im.width/2;                             //单个背景图的二分之一宽
				var h = im.height/2;                              //单个背景图的二分之一宽
				var x = this.x;                                 //x列
				var y =  this.y;                                //y行
		
				window.parent.gg = this.graph;
			    this.s = 1;                                     //缩放
			    var x0 = 1-x;                                   //第一个货架的x位置
			    var y0 = 1-y;                                   //第一个货架的y位置
			    for(var i = 0; i < x; i++){                     //布局背景图
			    	var x_l =  w * ( x0 +i*2 );                 //货架x上的位置
			    	for(var j =0; j < y; j++){
			    		var y_l =   h * ( y0 +j*2 );            //货架y上的位置，并生成货架
		
			    			var svg = this.graph.createNode("", x_l, y_l );
						    svg.movable = false;
							svg.selectable = false;
							svg.editable = false;
							svg.resizable = false;
						    svg.image = background_src_h;
						    var b_src = background_src_s2;
						    if(j == 0){
						    	b_src =background_src_s1;
						    }
						    if(j  == y-1){
						    	b_src =background_src_s3;
						    }
						    var svg = this.graph.createNode("", x_l-w, y_l );
						    svg.movable = false;
							svg.selectable = false;
							svg.editable = false;
							svg.resizable = false;
						    svg.image = b_src;
						    if(i == x-1){
						    	if(j == 0){
							    	b_src =background_src_s1;
							    }
							    if(j  == y-1){
							    	b_src =background_src_s3;
							    }
					    		var svg = this.graph.createNode("", x_l+w, y_l );
							    svg.movable = false;
								svg.selectable = false;
								svg.editable = false;
								svg.resizable = false;
							    svg.image = b_src;
					    	}
					
			    	}
			    	
			    	
			    }             
			    this.graph.translateTo( this.graph.width/2, this.graph.height/2,0.5); 
				this.tx = 0;
			    this.ty = 0;
				this._width  = im.width * this.s ;
				this._height = im.height * this.s;
				this._width2 =  this._width * this.x / 2;
				this._height2 = im.height * this.s * this.y/ 2;
				this._width3 =  im.width * this.s  / (2 * this.w);
				this.x0 = this.tx - im.width * this.s / 2;
				this.y0 = this.ty - this._height2;
			    
			}
			
	};
	
	window.creteShelves = Shelves;
}(jQuery))