package com.example.androidsummary.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 欢迎界面的Fragment
 *  通过set、getArguments方法与Activity交互，通过传递参数的不同 返回不同的视图
 */
public class Fragment_Welcome extends Fragment {
    private static final String LAYOUT_ID = "layoutid";

    private int layout_id ;


    public static Fragment_Welcome newInstance(int resId) {
        Fragment_Welcome fragment = new Fragment_Welcome();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, resId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layout_id = getArguments().getInt(LAYOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout_id, container, false);
    }


}
