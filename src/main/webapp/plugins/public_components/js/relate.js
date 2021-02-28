const text = Symbol('text');  
const select = Symbol('select');  
const ztree = Symbol('ztree');  
const table = Symbol('table');  
const _uiType = Symbol('_uiType'); 
const clickRelate = Symbol('clickRelate'); 
const changeRelate = Symbol('changeRelate'); 
const funs = {
	"text" : text,	
	"select" : select,	
	"ztree" : ztree,	
	"table" : table,
	"_uiType" : _uiType,	
	"click-relate" : clickRelate,	
	"change-relate" : changeRelate,	
};
class Relate{
	constructor(ui){
		this.ui = ui;
		this.type =[ "click-relate" , "change-relate" ];
	}
	
	bind( _n ){
		 var _this = this; 
		 this.type.forEach((n,i)=>{ _this.ui && _this.ui.hasAttribute(n) && _this[funs[n]](_n); });
	}
	
	[text](id, param){
	   	$("#"+id).val(param);
	}
	
	[select](){
	   	
	}
	
	[ztree](id){
		var treeObj =  $.fn.zTree.getZTreeObj(id);
		treeObj.reAsyncChildNodes("","refresh");
	}
	
	[table](id ,param){
	   	$("#"+id).jqGrid('setGridParam', {postData: param} ).trigger("reloadGrid");
	}
	
	[_uiType](id){
		let _ui =$("#"+id), type="";
		
		if( _ui[0].attributes["class"].value =="ztree" ){
			type = "ztree";
		}
		else if( _ui[0].attributes["class"].value.indexOf("table") != -1 ){
			type = "table";
		}
		else if( _ui[0].attributes["class"].value.indexOf("select2") != -1){
			type = "select";
		}else{
			type = "text";
		}
		
		return type;
	}
	[clickRelate]( _n ){
        let id = this.ui.attributes["click-relate"].value, uiType ="";

        if( $("#"+id).length == 0  ) return;
        uiType = this[_uiType](id);
        if( uiType == "" ) return;
        var param =  _n.attributes["click-relate-param"] &&  _n.attributes["click-relate-param"].value;
        this.ui.clickRelateFun = (function(_this, type , id, param ){
        	return  function(data){
        		//debugger;
        		var  obj  ={};
        		param && ( obj[param] = data[param], data =obj);
        		//var obj  ={};
        		_this[type]( id , data);
 	        };
        }(this, funs[uiType], id, param ));
	}
    [changeRelate]( _n ){
        let id = this.ui.attributes["change-relate"].value, uiType ="";
     
        if( $("#"+id).length == 0  ) return;
        uiType = this[_uiType](id);
        if( uiType == "" ) return;
        
        var param =  _n.attributes["click-relate-param"] &&  _n.attributes["click-relate-param"].value;
        this.ui.changeRelateFun = (function(_this, type , id, param ){
        	return  function(data){
        		//debugger;
        		var  obj  ={};
        		param && ( obj[param] = data[param], data =obj);
        		//var obj  ={};
        		_this[type]( id , data);
 	        };
        }(this, funs[uiType], id, param ));
	}
	
}