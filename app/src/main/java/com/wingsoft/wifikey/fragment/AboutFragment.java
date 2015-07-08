package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wingsoft.wifikey.R;

/**
 * Created by wing on 15/7/6.
 */
public class AboutFragment extends Fragment {
    private TextView _textView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstenceBundle){
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        _textView = (TextView)view.findViewById(R.id.version);

        try {
            _textView.setText("version "+getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }
}
