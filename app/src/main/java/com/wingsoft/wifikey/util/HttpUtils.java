package com.wingsoft.wifikey.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wingsoft.wifikey.enmu.LoginState;
import com.wingsoft.wifikey.model.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wing on 15/7/9.
 */
public class HttpUtils {
    public static String ADDRESS = "http://192.168.43.177:8080/Wifikey/check.do";

    public static void login(String username, String password, Handler handler) {
        HttpURLConnection connection = null;
        HttpCallbackListener listener = new HttpCallbackListener() {
            @Override
            public void onFinish(String res, Handler handler) {
                Message msg = handler.obtainMessage();
                msg.what = LoginState.NETWORK_SUCCESS;
                User user = null;
                if(res.equals("true")){
                    user = new User("admin","123");

                }
                msg.obj = user;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Handler handler) {
                Message msg = handler.obtainMessage();
                msg.what = LoginState.NETWORK_FAILED;
                handler.sendMessage(msg);
            }
        };
        try {

            URL url = new URL(ADDRESS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("username=" + username + "&&password=" + password);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.i("POST", sb.toString());
            if (listener != null) {
                listener.onFinish(sb.toString(), handler);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(handler);
            }
            Log.i("POST",e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

}
