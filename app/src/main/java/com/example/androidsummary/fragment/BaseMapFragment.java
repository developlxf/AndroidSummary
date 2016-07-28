package com.example.androidsummary.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * 熟悉android应该知道，fragment与fragment之间的通信需要间接地使用宿主activity，主要有 1.在一个fragment中通过
 * 使用getacvtivty获得宿主从而获得宿主拥有的另一个fragment，进而操作该fragment的元素，甚至信息。另一个是 通过在
 * fragment中编写接口，让activity 实现该接口，在fragment中把宿主activity当成该接口使用。请问这两种方式是什么关
 * 系，后一种方式 的价值在哪里?
 *

 如果要讨论第二种方式的意义在哪，在就要从设计模式从头开始说起。设计模式中的一个基本原则是开闭原则。开闭原则是说模块
 应该对扩展开放，而对修改关闭。模块应该尽量不修改代码的情况下进行扩展。那么说第一种方式，第一种方式虽然行得通，但是
 违反了设计模式的开闭原则。fragment天生是一种独立的开发组件，如果fragment要与自己的宿主activity交互，那么就需要知
 道activity是如何工作的，这本身就破坏了fragment的独立性。也就是说这个fragment只能在这个activity里使用，不能再
 另一个activity里使用。而第二种方式，在fragment里定义了接口，由宿主的activity来实现借口处理任务。如果想换一个宿
 主activity，只需要在新的activity中实现借口就好，fragment始终不需要进行改变。这样就满足了设计模式的开闭原则。其
 实第二种的实现方式，就是设计模式中的代理模式。多一个代理类出来，替原对象进行一些操作。这种使用方式在iOS开放中也是
 基本方式，iOS中的delegate和第二种方式思想基本一致。
 */
public class BaseMapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private MapView mapView;//地图控件
    private BaiduMap baiduMap;//地图对象

    public BaseMapFragment() {

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BaseMapFragment.
     */
    public static BaseMapFragment newInstance(String param1) {
        BaseMapFragment fragment = new BaseMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("x") && intent.hasExtra("y")){
            //坐标点
            LatLng p = new LatLng(intent.getExtras().getDouble("x"),intent.getExtras().getDouble("y"));
            BaiduMapOptions options = new BaiduMapOptions();
            options.mapStatus(new MapStatus.Builder().target(p).build())//设置目标点
            .compassEnabled(true)
            .zoomControlsEnabled(true);//默认就会true  可变焦距

            mapView = new MapView(getContext(),options);
        }else{
            mapView = new MapView(getContext());
        }

        return mapView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * 这个接口必须被使用这个fragment的Activity实现  为了允许fragment与使用它的Activity和activity中的其他fragment交互
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
