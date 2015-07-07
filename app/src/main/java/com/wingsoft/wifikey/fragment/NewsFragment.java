package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wingsoft.wifikey.R;

/**
 * Created by wing on 15/7/6.
 */
public class NewsFragment extends Fragment{
    private WebView _WebView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        _WebView = (WebView)view.findViewById(R.id.webview);
        _WebView.getSettings().setJavaScriptEnabled(true);
        _WebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view ,String url){
                view.loadUrl(url);
                return true;
            }
        });
        _WebView.loadUrl("http://m.baidu.com/news");
        return view;

    }

}
