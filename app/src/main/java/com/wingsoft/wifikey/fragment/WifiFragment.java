package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.adapter.WifiAdapter;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.util.ImportUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 15/7/6.
 */
public class WifiFragment extends Fragment {
    private ListView listView;
    WifiAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_wifi,container,false);
        listView = (ListView)view.findViewById(R.id.listview);
        List list;
        list = getList();
        adapter = new WifiAdapter(getActivity(),R.layout.listwifi,list);
        listView.setAdapter(adapter);
        return view;
    }
    private List  getList(){
        List list = null;
        list = WifiDB.getWifiDB(getActivity()).fetchAllData();
        return list;
    }
    public void listChange(){
        adapter.notifyDataSetChanged();
    }

}
