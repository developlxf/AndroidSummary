package com.example.androidsummary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.androidsummary.map_sub_activity.PlaceSearchActivity;
import com.example.androidsummary.map_sub_activity.PoiChooseActivity;
import com.example.androidsummary.map_sub_activity.RouteLineActivity;

public class BaiduMapActivity extends Activity implements View.OnClickListener {

    private LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;

    // 添加百度相关控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 附近
    private Button btn_near;
    // 路线
    private Button btn_way;
    // 地址查询
    private Button btn_place_search;
    // 查询地址输入框
    private EditText et_place_search;
    private EditText et_city_search;
    boolean isFirstLoc = true;// 是否首次定位

    private MyLocationListener mLocationListener = new MyLocationListener();
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
         *   注意该方法要再setContentView方法之前实现
         */
        SDKInitializer.initialize(getApplicationContext());
        initView();
        initData();
        setOnClickListener();
    }

    private void initView() {
        setContentView(R.layout.activity_baidu_map);
        et_city_search = (EditText) findViewById(R.id.et_city_search);
        et_place_search = (EditText) findViewById(R.id.et_place_search);
        btn_place_search = (Button) findViewById(R.id.btn_place_search);
        btn_near = (Button) findViewById(R.id.btn_near);
        btn_way = (Button) findViewById(R.id.btn_way);
        mMapView = (MapView) findViewById(R.id.bd_mapview);
    }

    private void initData() {
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//普通模式
//        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;//罗盘模式
//        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//跟随模式
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//                mCurrentMode, true, null));
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);// 打开gps
        option.setScanSpan(1000);
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

    private void setOnClickListener() {
        btn_near.setOnClickListener(this);
        btn_way.setOnClickListener(this);
        btn_place_search.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place_search:
                placeSearch();
                break;
            case R.id.btn_near:
                poiChooseNearBy();
                break;
            case R.id.btn_way:
                showRouteLine();
                break;

        }
    }

    /**
     * 显示路线搜索,起点与终点的选择
     */
    private void showRouteLine() {
        Intent intent = new Intent(BaiduMapActivity.this,
                RouteLineActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    /**
     * 附近兴趣点选择
     */
    private void poiChooseNearBy() {
        Intent intent = new Intent(BaiduMapActivity.this, PoiChooseActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    /**
     * 地址查询
     */
    private void placeSearch() {
        String city = et_city_search.getText().toString();
        String place = et_place_search.getText().toString();
        if (TextUtils.isEmpty(place) || TextUtils.isEmpty(city)) {
            Toast.makeText(BaiduMapActivity.this, "请输入目的地址", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            System.out.println("city:" + city + "-------place" + place);
            Intent intent = new Intent(BaiduMapActivity.this,
                    PlaceSearchActivity.class);
            intent.putExtra("city", city);
            intent.putExtra("place", place);
            startActivity(intent);
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius()).direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(msu);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
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
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


}
