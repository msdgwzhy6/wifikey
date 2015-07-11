package com.wingsoft.wifikey.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by wing on 15/7/11.
 */
public class BitmapCache implements ImageLoader.ImageCache{
    private LruCache<String,Bitmap> mCache;
    public BitmapCache(){
        int maxSize = 4*1024*1024;
        mCache = new LruCache<String,Bitmap>(maxSize){


            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String s) {
        return  mCache.get(s);

    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        if(bitmap!=null){
            mCache.put(s,bitmap);
        }
    }
}
