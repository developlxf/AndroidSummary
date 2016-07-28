package com.example.androidsummary.map_sub_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.androidsummary.R;

/**
 * 根据传入的地点，显示其在地图中的位置
 *
 * @author
 */
public class PlaceSearchActivity extends Activity implements
        OnGetGeoCoderResultListener, OnClickListener {

    /**
     * Geocoder可以在街道地址和经纬度地图坐标之间进行转换。它提供了对两种地理编码功能的访问：
     * Forward Geocoding(前向地理编码)：查找某个地址的经纬度
     * Reverse Geocoding(反向地理编码)：查找一个给定的经纬度所对应的街道地址。
     */
    GeoCoder mSearch = null;
    /**
     * 百度地图对象
     */
    BaiduMap mBaiduMap = null;
    /**
     * 地图控件
     */
    MapView mMapView = null;
    // 要查询的城市
    private String city;
    // 要查询的具体地址
    private String place;
    // 记录定位的经纬度
    private double latitude, longitude;
    private Button btn_near;
    private Button btn_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();
        setListener();
        placeSearch();
    }

    private void initView() {
        setContentView(R.layout.activity_place_search);
        mMapView = (MapView) findViewById(R.id.bmapView);
        btn_near = (Button) findViewById(R.id.btn_near);
        btn_route = (Button) findViewById(R.id.btn_way);
    }

    private void initData() {
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        place = intent.getStringExtra("place");
        mBaiduMap = mMapView.getMap();
        //实例化GeoCoder
        mSearch = GeoCoder.newInstance();

    }

    private void setListener() {
        mSearch.setOnGetGeoCodeResultListener(this);
        btn_near.setOnClickListener(this);
        btn_route.setOnClickListener(this);
    }

    /**
     * 执行搜索  搜索的过程会触发事件
     */
    private void placeSearch() {
        mSearch.geocode(new GeoCodeOption().city(city).address(place));
    }

    /**
     * 获取搜索结果
     */
    //地名搜经纬度
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (null == result || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PlaceSearchActivity.this, "抱歉，未能找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mBaiduMap.clear();
        //获得搜索地方的经纬度
        LatLng ll = result.getLocation();
        this.latitude = ll.latitude;
        this.longitude = ll.longitude;

        //将标识该位置的覆盖物  显示在map上  定义好位置、图标
        mBaiduMap.addOverlay(new MarkerOptions().position(ll).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
        //将地图的中心位置定义到此地址
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));
        //提示用户
        String strInfo = String
                .format("纬度：%f 经度：%f", ll.latitude, ll.longitude);
        Toast.makeText(PlaceSearchActivity.this, strInfo, Toast.LENGTH_LONG)
                .show();
    }

    //经纬度搜地名
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_near:
                poiChooseNearBy();
                break;
            case R.id.btn_way:
                Intent intent  = new Intent(this,RouteLineActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 附近兴趣点选择
     */
    private void poiChooseNearBy() {
        Intent intent = new Intent(PlaceSearchActivity.this,
                PoiChooseActivity.class);
        intent.putExtra("place", place);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
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
        mSearch.destroy();
        super.onDestroy();
    }

}
