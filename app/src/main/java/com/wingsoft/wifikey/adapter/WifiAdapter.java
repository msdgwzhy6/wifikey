package com.wingsoft.wifikey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.model.Wifi;

import java.util.List;

/**
 * Created by wing on 15/7/6.
 */
public class WifiAdapter extends ArrayAdapter<Wifi> {
    private int resourceId;
    public WifiAdapter(Context context, int textViewResourceId, List<Wifi> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int pos,View container,ViewGroup parent){
        Wifi wifi = getItem(pos);
        View view;
        ViewHolder viewHolder;
        if(container==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.textSsid = (TextView)view.findViewById(R.id.text_key);
            viewHolder.textKey = (TextView)view.findViewById(R.id.text_key);
            viewHolder.textComment = (TextView)view.findViewById(R.id.text_comment);
            viewHolder.image = (ImageView)view.findViewById(R.id.image);
            view.setTag(viewHolder);
        }else{
            view = container;
            viewHolder = (ViewHolder)view.getTag();

        }
        viewHolder.textComment.setText(wifi.getComment());
        viewHolder.textKey.setText(wifi.getKey());
        viewHolder.textSsid.setText(wifi.getSsid());
        return view;
    }

    class ViewHolder{
        TextView textKey;
        TextView textSsid;
        TextView textComment;
        ImageView image;
    }
}
