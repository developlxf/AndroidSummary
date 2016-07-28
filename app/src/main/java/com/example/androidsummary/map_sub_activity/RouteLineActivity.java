package com.example.androidsummary.map_sub_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.androidsummary.R;

public class RouteLineActivity extends AppCompatActivity {

    private Button mBtnPre = null;// 上一个节点
    private Button mBtnNext = null;// 下一个节点
    private EditText editSt;
    private EditText editEn;
    private int nodeIndex = -1;// 节点索引,供浏览节点时使用
    private RouteLine route = null; // 线路
    private OverlayManager routeOverlay = null;// 覆盖物管理
    private boolean useDefaultIcon = false; // 是否使用默认的图标
    private TextView popupText = null;// 泡泡view

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    private String city = "长沙";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_route_line);

        this.init();

        this.event();
    }

    private void init() {
        // 界面初始化
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        mBaidumap.setMapStatus(MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder()
                        .target(new LatLng(28.11, 113)).zoom(15).build()));

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        editSt = (EditText) findViewById(R.id.start);
        editEn = (EditText) findViewById(R.id.end);

        //初始化搜索路径模块
        mSearch = RoutePlanSearch.newInstance();
        this.event();
    }

    // 注册事件监听
    private void event() {
        //线路搜索获取结果的事件监听
        mSearch.setOnGetRoutePlanResultListener(rlistener);
    }

    //线路搜索获取结果的事件监听
    private OnGetRoutePlanResultListener rlistener = new OnGetRoutePlanResultListener() {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            if(result==null|| result.error!= SearchResult.ERRORNO.NO_ERROR){
                Toast.makeText(RouteLineActivity.this, "抱歉 未搜到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if(result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
                //result.getSuggestAddrInfo();
                return;
            }
            if(result.error == SearchResult.ERRORNO.NO_ERROR){
                nodeIndex=-1;
                route = result.getRouteLines().get(0);//有可能会的到多条线路
                //覆盖物
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                //重写 覆盖物中  图形处理的过程
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {
            if(result==null|| result.error!=SearchResult.ERRORNO.NO_ERROR){
                Toast.makeText(RouteLineActivity.this,"抱歉 未搜到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if(result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
                //result.getSuggestAddrInfo();
                return;
            }
            if(result.error == SearchResult.ERRORNO.NO_ERROR){
                nodeIndex=-1;
                route = result.getRouteLines().get(0);//有可能会的到多条线路
                //覆盖物
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if(result==null|| result.error!=SearchResult.ERRORNO.NO_ERROR){
                Toast.makeText(RouteLineActivity.this,"抱歉 未搜到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if(result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
                //result.getSuggestAddrInfo();
                return;
            }
            if(result.error == SearchResult.ERRORNO.NO_ERROR){
                nodeIndex=-1;
                route = result.getRouteLines().get(0);//有可能会的到多条线路
                //覆盖物
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
    };

    // 搜索按钮处理事件
    public void SearchButtonProcess(View v) {
        // 重置地图
        route = null;
        mBtnPre.setVisibility(View.VISIBLE);
        mBtnNext.setVisibility(View.VISIBLE);

        //获取起点 终点的信息
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, editSt.getText().toString());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, editEn.getText().toString());

        if(v.getId()==R.id.drive){
            mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
        }else if(v.getId()==R.id.transit){
            mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).to(enNode).city(city));
        }else if(v.getId()==R.id.walk){
            mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
        }
    }

    // 节点浏览事件 上一个 下一个
    public void nodeClick(View v) {
        if(route == null || route.getAllStep()==null){
            return;
        }

        if(nodeIndex==-1 && v.getId() ==R.id.pre){
            return;
        }

        if(v.getId()==R.id.next){
            if(nodeIndex<route.getAllStep().size()-1){
                nodeIndex++;
            }else{
                return;
            }
        }else if(v.getId()==R.id.pre){
            if(nodeIndex>0){
                nodeIndex--;
            }else{
                return;
            }
        }

        //获取节点信息 ，并以弹出显示
        LatLng  nodeLocation  = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if(step  instanceof WalkingRouteLine.WalkingStep){
            nodeLocation = ((WalkingRouteLine.WalkingStep)step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep)step).getInstructions();
        }else if(step  instanceof DrivingRouteLine.DrivingStep){
            nodeLocation = ((DrivingRouteLine.DrivingStep)step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep)step).getInstructions();
        }else if(step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep)step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep)step).getInstructions();
        }

        if(nodeLocation==null ||  nodeTitle==null){
            return;
        }

        //将地图移动至当前节点为中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        //弹出 提示信息
        popupText = new TextView(RouteLineActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        System.out.println(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText,nodeLocation,0));

    }


    //自定义覆盖物 ，重写图形处理过程
    class MyWalkingRouteOverlay extends  WalkingRouteOverlay{

        public MyWalkingRouteOverlay(BaiduMap arg0) {
            super(arg0);
        }

        //更改起点图标
        @Override
        public BitmapDescriptor getStartMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }


        //更改终点图标
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }

    }

    class  MyTransitRouteOverlay extends  TransitRouteOverlay{

        public MyTransitRouteOverlay(BaiduMap arg0) {
            super(arg0);
        }

        //更改起点图标
        @Override
        public BitmapDescriptor getStartMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }


        //更改终点图标
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }

    }

    class  MyDrivingRouteOverlay extends  DrivingRouteOverlay{

        public MyDrivingRouteOverlay(BaiduMap arg0) {
            super(arg0);
        }

        //更改起点图标
        @Override
        public BitmapDescriptor getStartMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }


        //更改终点图标
        @Override
        public BitmapDescriptor getTerminalMarker() {
            if(useDefaultIcon==false){
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }

    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
