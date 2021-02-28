/**
 * Created by CY on 2018/11/20.
 */

var imgUrl ="./img/ww.jpg",
    pulsingIcon = L.icon.pulse({iconSize: [10, 10], color: "red"}),
    map_h= 10000,
    map_w=	20000,
    bounds =[[0,0], [map_h, map_w]],
    GridJson = [],
    GridDataJson =[],
    GridIconJson = [],
    GridIcon1Json =[],
    markerJson = [],
    select_color = "green",
    _area = "", titleIcon ="", px = 16, polygon="";

// leaflet.js, 初始化地图
var map = L.map("map",{
    crs: L.CRS.Simple, // 简单模式
    zoom: -5,           // 初始缩放比例
    minZoom: -10,
    zoomSnap: 0.15,     // 缩放系数
    maxZoom:-2,
    dragging:true,
    doubleClickZoom:false,
    zoomControl:false,
    touchZoom:false,
    trackResize:true,
});

map.fitBounds(bounds);                      //设置包含可能具有最大缩放级别的给定地理边界的地图视图
/*   map.addControl(new L.Control.Fullscreen());*/
// 地图缩放事件结束后执行
map.on("zoomend", function(){
    for(var i in GridIconJson){
        if(GridIconJson.hasOwnProperty(i)){
            px = 14 + 24 / ( (-2+4)/0.15 ) * (arguments[0].target._zoom+4)/0.15;
            // GridIconJson[i].options.icon.options.iconSize =[px,px];
            // GridIcon1Json[i].options.icon.options.iconSize =[px,px];
            // GridIcon1Json[i].setIcon(GridIcon1Json[i].options.icon);
            // GridIconJson[i].setIcon(GridIconJson[i].options.icon);
        }

    }

});
/**
 *   _______ (x1,y1)
 * |       |
 * |_______|
 * (x0,y0)
 *
 *
 * x0为区域 左下角点的x坐标轴
 * y0为区域 左下角点的y坐标轴
 * x1为区域 右上角点的x坐标轴
 * y1为区域 右上角点的y坐标轴
 *   grids=[ {
     *      id:1,
     *      area:[[10,10],[20,20]],
     *      color: "blue"       // 如果不设置颜色，默认灰色
     *   } ];
 *
 * */
function loadGrid( grids , type){
    removeAllGrid();                  // 清空地图数据
    _area && _area.remove();          // 清空地图
    titleIcon && titleIcon.remove();  // 清空图标
    polygon && polygon.remove();      // 清空图标
    if(type == 2){
        creatBox_1();                  //根据类型 加载不同的场景
    }else if(type == 9){
        creatBox_2();
    }else if(type == 11){
        creatBox_3();
    }
    // 根据grids来画车位
    if( grids && grids.length >0 ){
        for(var i = 0; i < grids.length; i++){
            var grid = grids[i];
            // 车位初始化
            var rect = L.rectangle(grid.area, {
                color: "#fff",
                fillColor: grid.color,
                weight: 1,
                fillOpacity: 1
            }).addTo(map);
            // 车位上的图标初始化
            var _icon = L.marker([ (grid.area[0][0] + grid.area[1][0])/2 , (grid.area[1][1] - 60)  ], {icon:  L.icon({
                    iconUrl: context_path+"/static/techbloom/system/scene/img/drawer.png",
                    iconSize: [20, 20]
                })}).addTo(map);
            // var _icon_ = L.marker([(grid.area[0][0] + grid.area[1][0])/2 , (grid.area[0][1] + 200) ], {icon: L.icon({
            //         iconUrl: context_path+"/static/techbloom/system/scene/img/"+grid.icons[2],
            //         iconSize: [px, px]
            //     })}).addTo(map);
            // 记录数据，用于清空
            GridJson[grid.id] = rect;
            GridDataJson[grid.id] = grid;
            GridIconJson[grid.id] = _icon;
            //GridIcon1Json[grid.id]  =_icon_;
        }

    }
};
//清空地图数据的方法
function removeAllGrid(){
    for(var i in GridJson){
        GridJson.hasOwnProperty(i) &&   GridJson[i].remove();
        GridIconJson.hasOwnProperty(i) &&   GridIconJson[i].remove();
        GridIcon1Json.hasOwnProperty(i) &&   GridIcon1Json[i].remove();
    }
    GridJson = [];
    GridIconJson = [];
    GridIcon1Json = [];
    GridDataJson = [];
}

// 用于创建原料A的环境
var creatBox_1 = function(){
    var grid_w =30; // 网格宽 30米 x轴
    var grid_h =50; // 网格高 50米 y轴

    var x =  grid_w*100;
    var y= grid_h*100;

    var latlngs = [[0, 0],[x, 0],[x, y],[0, y]];  //场景范围
    //根据上面的范围画边框
    polygon = L.polygon(latlngs, {color: 'transparent',weight:1, fillOpacity:0}).addTo(map);
    //添加原料A的字样
    _icon = L.divIcon({
        html: "原料A",
        className: 'my-div-icon',
        iconSize:20
    });
    //设置字样的位置
    titleIcon = L.marker([2500,500], { icon: _icon }).addTo(map);
    _area  =  L.rectangle([[0,0],[3000,5000]], {
        color: "#ccc",
        fillColor: "#fff",
        weight: 1,
        fillOpacity: 0.5,
    }).addTo(map);

}
// 用于创建原料B的环境
var creatBox_2 = function(){

    var grid_w = 30; // 网格宽 100米 x轴
    var grid_h = 45; // 网格高 200米 y轴

    var x =  grid_w*100;
    var y= grid_h*100;

    var latlngs = [[0, 0],[x, 0],[x, y],[0, y]];

    polygon = L.polygon(latlngs, {color: '#ccc',weight:1, fillOpacity:0}).addTo(map);
    /*        map.fitBounds(polygon.getBounds());*/
    _icon = L.divIcon({
        html: "原料B",
        className: 'my-div-icon',
        iconSize:20
    });
    titleIcon = L.marker([2500,3500], { icon: _icon }).addTo(map);
    _area  =  L.rectangle([[0, 0],[3000,4500]], {
        color: "#ccc",
        fillColor: "#fff",
        weight: 1,
        fillOpacity: 0.5,
    }).addTo(map);

}

// 用于创建原料C的环境
var creatBox_3 = function(){

    var grid_w = 30; // 网格宽 100米 x轴
    var grid_h = 26; // 网格高 200米 y轴

    var x =  grid_w*100;
    var y= grid_h*100;

    var latlngs = [[0, 0],[x, 0],[x, y],[0, y]];

    polygon = L.polygon(latlngs, {color: '#ccc',weight:1, fillOpacity:0}).addTo(map);
    /*        map.fitBounds(polygon.getBounds());*/
    _icon = L.divIcon({
        html: "原料C",
        className: 'my-div-icon',
        iconSize:20
    });
    titleIcon = L.marker([2800,1800], { icon: _icon }).addTo(map);
    _area  =  L.rectangle([[0, 0],[3000,2600]], {
        color: "#ccc",
        fillColor: "#fff",
        weight: 1,
        fillOpacity: 0.5,
    }).addTo(map);

}

