package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.adapter.WifiAdapter;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.dialog.ItemDialog;
import com.wingsoft.wifikey.model.Wifi;

import java.util.List;

/**
 * Created by wing on 15/7/6.
 */
public class WifiFragment extends Fragment {
    private ListView listView;
    private WifiAdapter adapter;
    private static List _list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        _list = getList(getActivity());
        adapter = new WifiAdapter(getActivity(), R.layout.listwifi, _list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Wifi wifi = (Wifi) _list.get(i);
                Main main = (Main) getActivity();
                ItemDialog.getDialog(getActivity(), wifi, main.getFragmentAbout(), i);
                return false;
            }
        });

        return view;
    }

    private List getList(Context context) {
        List list = null;
        list = WifiDB.getWifiDB(context).fetchAllData();
        return list;
    }

    public void change(Context context) {
        _list = getList(context);
        adapter.notifyDataSetChanged();
    }

    public static List get_list() {
        return _list;
    }

    ;

}
