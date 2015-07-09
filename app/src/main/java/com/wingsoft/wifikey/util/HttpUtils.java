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
    public static String LOGIN = "http://www.youngsoft.science:8080/Wifikey/check.do";
    public static String REG = "http://www.youngsoft.science:8080/Wifikey/reg.do";

    public static void sendPost(String address, String username, String password, Handler handler) {
        HttpURLConnection connection = null;
        HttpCallbackListener listener = new HttpCallbackListener() {
            @Override
            public void onFinish(String res, Handler handler) {
                Message msg = handler.obtainMessage();
                msg.what = LoginState.NETWORK_SUCCESS;
                User user = null;
                if (res.substring(0, 4).equals("true")) {

                    String username = res.substring(4);
                    Log.i("POST", "登录成功" + username);
                    user = new User(username, "");
                    Log.i("POST", "state is " + res.substring(0, 4));

                } else if (res.substring(0, 5).equals("false")) {



                    Log.i("POST", "state is " + res.substring(0, 5));


                } else if (res.substring(0, 6).equals("failed")) {
                    msg.arg1 = LoginState.REG_FAILED;
                    Log.i("POST", "注册失败");
                    Log.i("POST", "state is " + res.substring(0, 6));
                } else if (res.substring(0, 7).equals("success")) {
                    String username = res.substring(7);
                    user = new User(username, "");
                    msg.arg1 = LoginState.REG_SUCCESS;
                    Log.i("POST", "state is " + res.substring(0, 7));
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

            URL url = new URL(address);
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
            Log.i("POST", "server return " + sb.toString());
            if (listener != null) {
                listener.onFinish(sb.toString(), handler);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(handler);
            }
            Log.i("POST", e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

}
