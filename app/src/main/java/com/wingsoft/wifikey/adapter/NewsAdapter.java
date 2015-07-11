package com.wingsoft.wifikey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.imageloader.BitmapCache;
import com.wingsoft.wifikey.model.News;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.util.NetworkUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 15/7/11.
 */
public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mInflater ;
    private ArrayList<News> mList;
    private ViewHolder mViewHolder;
    private Context mContext;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;
    public NewsAdapter(Context context, ArrayList<News> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        mQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mQueue,new BitmapCache());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        News news = mList.get(i);
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_news, null);
            mViewHolder.mImageView = (NetworkImageView) convertView.findViewById(R.id.image_listnews);
            mViewHolder.mTextView_Time = (TextView) convertView.findViewById(R.id.text_listnews_time);
            mViewHolder.mTextView_Title = (TextView) convertView.findViewById(R.id.text_listnews_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

        }
        mViewHolder.mTextView_Time.setText(news.getmPublishTime());
        mViewHolder.mTextView_Title.setText(news.getmTitle());

        mViewHolder.mImageView.setImageUrl(news.getmFirstPicUrl(),mImageLoader);
        return convertView;
    }

    class ViewHolder {
        TextView mTextView_Title;
        TextView mTextView_Time;
        NetworkImageView mImageView;
    }
}
