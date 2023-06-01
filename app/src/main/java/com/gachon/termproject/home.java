package com.gachon.termproject;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class home extends Fragment {

     static final String WEBVIEW_URL = "webview_url";
    public static WebView browser;

    public home() {
        // Required empty public constructor
    }

    public static home newInstance() {
        return new home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        browser = view.findViewById(R.id.webkit);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());


        if (savedInstanceState != null) {
            String url = savedInstanceState.getString(WEBVIEW_URL);
            browser.loadUrl(url);
        } else {
            browser.loadUrl("https://www.bbc.com/korean/topics/cpzd4k7n846t");
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (browser.canGoBack()) {
                    browser.goBack();
                } else {
                    requireActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WEBVIEW_URL, browser.getUrl());
    }

    // Rest of the code...
}
