package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.adapter.AboutAdapter;

/**
 * Created by wing on 15/7/6.
 */
public class AboutFragment extends Fragment {
    private TextView mTextView;
    private ListView mListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstenceBundle) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mTextView = (TextView) view.findViewById(R.id.version);

        try {
            mTextView.setText("version " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mListView = (ListView)view.findViewById(R.id.listview);
        String list[] = getResources().getStringArray(R.array.about_list);
        AboutAdapter adapter = new AboutAdapter(getActivity(),R.layout.fragment_about_list,list);
        mListView.setAdapter(adapter);

        return view;
    }
}
