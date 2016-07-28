package com.example.androidsummary.map_sub_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.androidsummary.R;

/**
 * 选择兴趣点POI
 * 
 * @author 
 * 
 */
public class PoiChooseActivity extends Activity implements OnClickListener {

	// 地点
	private String place;
	// 目标地点经纬度
	private double latitude, longitude;
	private EditText et_poi_search;
	private Button btn_poi_search;
	private GridView gv_pois;
	// 选择的poi
	private String keyWord;
	// 兴趣点poi数组
	private String[] pois;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		initView();
		initData();
		setListener();
	}

	private void initView() {
		setContentView(R.layout.activity_poi_choose);
		et_poi_search = (EditText) findViewById(R.id.et_poi_search);
		btn_poi_search = (Button) findViewById(R.id.btn_poi_search);
		gv_pois = (GridView) findViewById(R.id.gv_pois);
	}

	private void initData() {
		// 获取传递的经纬度 地点
		latitude = getIntent().getDoubleExtra("latitude", 0.0);
		longitude = getIntent().getDoubleExtra("longitude", 0.0);
		place = getIntent().getStringExtra("place");
		if (TextUtils.isEmpty(place)) {
			et_poi_search.setHint("在当前位置附近搜索");
		} else {
			et_poi_search.setHint("在" + place + "附近搜索");
		}
		pois = getResources().getStringArray(R.array.poi_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				PoiChooseActivity.this, R.layout.poi_choose_item,
				R.id.tv_poi_item, pois);
		gv_pois.setAdapter(adapter);

	}

	private void setListener() {
		btn_poi_search.setOnClickListener(this);
		MyOnItemClickListener listener = new MyOnItemClickListener();
		gv_pois.setOnItemClickListener(listener);
	}

	/**
	 * 点击POI条目，跳转页面 ，将所需数据传递过去，查询相应的poi
	 * 
	 * @author sf
	 * 
	 */
	class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			keyWord = pois[position];
			toPoiList();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_poi_search:
			String poi = et_poi_search.getText().toString();
			if (TextUtils.isEmpty(poi)) {
				Toast.makeText(PoiChooseActivity.this, "请输入兴趣点",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				keyWord = poi;
			}
			toPoiList();
			break;

		default:
			break;
		}
	}

	/**
	 * 跳转到PoiListActivity
	 */
	public void toPoiList() {
		Intent intent = new Intent(PoiChooseActivity.this,
				PoiListActivity.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("keyWord", keyWord);
		startActivity(intent);
	}
}
