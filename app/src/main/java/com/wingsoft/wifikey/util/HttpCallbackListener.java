package com.wingsoft.wifikey.util;

import android.os.Handler;

/**
 * Created by wing on 15/7/9.
 */
public interface HttpCallbackListener {
    void onFinish(String res,Handler handler);
    void onError(Handler handler);
}
