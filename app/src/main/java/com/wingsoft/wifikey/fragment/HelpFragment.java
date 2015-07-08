package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.wingsoft.wifikey.R;

/**
 * Created by wing on 15/7/8.
 */
public class HelpFragment extends Fragment {
    private WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_help,container,false);
        mWebView = (WebView) view.findViewById(R.id.help_webview);
        mWebView.loadUrl("file:///android_asset/help.html");

        return view;
    }

}
