package com.binaa.android.binaa.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.binaa.android.binaa.R;


/**
 * Created by Muhammad on 7/29/2017
 */

public class PrivacyFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);

        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("http://binaacompany.com/privacy");

        return view;
    }

}
