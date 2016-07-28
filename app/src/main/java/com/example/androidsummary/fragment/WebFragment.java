package com.example.androidsummary.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 *
 */
public class WebFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static WebFragment newInstance(String param1) {
        WebFragment fragment = new WebFragment();
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
        WebView webView = new WebView(getActivity());
//        webView.loadUrl("file:///android_asset/Note.html");
        webView.loadUrl(mParam1);
        return webView;
    }


}
