package com.wingsoft.wifikey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsoft.wifikey.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 15/7/8.
 */
public class AboutAdapter extends ArrayAdapter<String> {
    private int mResourceId;

    public AboutAdapter(Context context, int resource, String[] objects) {


        super(context, resource, objects);
        mResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String str = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(R.id.about_text);
            view.setTag(viewHolder);


            }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.mTextView.setText(str);

        return view;
    }

    class ViewHolder {
        TextView mTextView;

    }
}
