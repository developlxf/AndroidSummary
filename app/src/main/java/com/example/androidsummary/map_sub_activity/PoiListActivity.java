package com.example.androidsummary.map_sub_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.androidsummary.R;
import com.example.androidsummary.widget.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据传入的地点 和POI展示 poi列表，可以切换地图模式
 * 
 * @author
 * 
 */
public class PoiListActivity extends Activity implements
		OnGetPoiSearchResultListener, OnClickListener, XListView.IXListViewLoadMore {

	private TextView tv_title;
	private Button btn_poi_show;
	private XListView xlv_poi_list;
	private RelativeLayout rl_loading;
	// 目标地点经纬度
	private double latitude, longitude;
	// 查询关键词
	private String keyWord;

	private PoiSearch mPoiSearch;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private static boolean isEnd = true;// 加载是否结束
	// 限制显示地图结果 还是列表结果
	private boolean showInMap = false;
	// 记录查询记录页数
	private int pageIndex = 0;
	// 查询结果
	private PoiResult result;
	// POI查询结果列表
	private List<PoiInfo> poiList = new ArrayList<PoiInfo>();
	private int pageNum;// 记录结果条数
	private PoiAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		//实例化 关键字搜索工具  并设置监听搜索事件
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		setContentView(R.layout.activity_poi_list);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_poi_show = (Button) findViewById(R.id.btn_poi_show);
		xlv_poi_list = (XListView) findViewById(R.id.xlv_poi_list);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		mMapView = (MapView) findViewById(R.id.bd_mapview);
		xlv_poi_list.setVisibility(View.GONE);
		rl_loading.setVisibility(View.VISIBLE);
	}

	private void initData() {
		mBaiduMap = mMapView.getMap();
		Intent intent = getIntent();
		latitude = intent.getDoubleExtra("latitude", 0.0);
		longitude = intent.getDoubleExtra("longitude", 0.0);
		keyWord = intent.getStringExtra("keyWord");
		tv_title.setText(keyWord);
		System.out.println("latitude:" + latitude + "--longitude:" + longitude
				+ "--keyWord:" + keyWord);
		getPoiList(pageIndex);
		showPoiList();
	}

	private void setListener() {
		btn_poi_show.setOnClickListener(this);
		//设置上啦加载的监听事件
		xlv_poi_list.setPullLoadEnable(this);
//		View view = View.inflate(PoiListActivity.this, R.layout.xlv_footer,
//				null);
//		xlv_poi_list.addFooterView(view);
	}

	/**
	 * 查询POI  分页
	 */
	private void getPoiList(int pageIndex) {
		isEnd = false;
		LatLng ll = new LatLng(latitude, longitude);
		mPoiSearch.searchNearby(new PoiNearbySearchOption().location(ll)
				.keyword(keyWord).radius(10000).pageNum(pageIndex));
	}

	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(PoiListActivity.this, "未找到结果", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		this.result = result;
		pageNum = result.getTotalPageNum();
		poiList = result.getAllPoi();
		xlv_poi_list.stopLoadMore();// 获得数据后停止加载
		adapter.notifyDataSetChanged();
		if (poiList.size() > 0) {// 显示列表
			rl_loading.setVisibility(View.GONE);//加载进度条消失
			xlv_poi_list.setVisibility(View.VISIBLE);//显示xlistview
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result)
	{
		//获取某个结果中详细信息的方法
		//弹出信息提示 ，显示此点的相信信息
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoiListActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(PoiListActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * POI数据适配器
	 * 
	 * @author sf
	 * 
	 */
	private class PoiAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return poiList.size();
		}

		@Override
		public PoiInfo getItem(int position) {
			return poiList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.poi_list_item, null);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			PoiInfo info = getItem(position);

			holder.tv_name.setText((position + 1) + "." + info.name);
			holder.tv_address.setText(info.address);
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_address;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_poi_show:
			changeShowMode();
			break;

		default:
			break;
		}
	}

	/**
	 * 在地图上展示POI
	 */
	private void changeShowMode() {
		showInMap = !showInMap;
		if (showInMap) {
			btn_poi_show.setText("列表");
			xlv_poi_list.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			showPoiInMap();
		} else {
			btn_poi_show.setText("地图");
			mMapView.setVisibility(View.GONE);
			xlv_poi_list.setVisibility(View.VISIBLE);
			showPoiList();
		}

	}

	/**
	 * 显示POI列表
	 */
	private void showPoiList() {
		adapter = new PoiAdapter();
		xlv_poi_list.setAdapter(adapter);
	}

	/**
	 * 地图展示POI查询结果
	 */
	private void showPoiInMap() {
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			//在本市可以找到
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiVoerlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}

		if(result.error== SearchResult.ERRORNO.AMBIGUOUS_KEYWORD){
			//此关键字 在本市没有找到，但在其他城市能找到，返回包含该关键字搜索结果的城市列表
			String  strinfo = "在";
			for(CityInfo cityinfo : result.getSuggestCityList()){
				strinfo  +=  cityinfo.city+" ";
			}
			Toast.makeText(PoiListActivity.this, strinfo+" 能找到结果",Toast.LENGTH_SHORT).show();

		}
	}

	class MyPoiVoerlay extends PoiOverlay {

		public MyPoiVoerlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			//获取这个点的详细信息
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			// }
			return true;
		}
	}

	@Override
	public void onLoadMore() {
		if (pageNum - 1 == pageIndex) {
			Toast.makeText(PoiListActivity.this, "暂无更多数据!", Toast.LENGTH_SHORT)
					.show();
			xlv_poi_list.stopLoadMore();
			return;
		}

		pageIndex++;
		getPoiList(pageIndex);
		poiList.clear();
		rl_loading.setVisibility(View.VISIBLE);
		xlv_poi_list.setVisibility(View.GONE);
	}

}
