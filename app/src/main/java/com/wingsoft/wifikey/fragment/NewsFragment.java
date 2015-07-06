package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsoft.wifikey.R;

/**
 * Created by wing on 15/7/6.
 */
public class NewsFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        return view;

    }
}
