package com.wingsoft.wifikey.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.transform.ErrorListener;

public class NewsActivity extends ActionBarActivity {
    private ListView mListView;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        mListView = (ListView)findViewById(R.id.listview_news);
        mTextView = (TextView)findViewById(R.id.textwheater);
        final ArrayList<News>list = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest("http://m.bitauto.com/appapi/News/List.ashx/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        Log.i("volley", jsonArray.toString());
                        JSONObject jsonObject;
                        for(int i = 0 ; i < jsonArray.length();i++){
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                News news = new News();
                                news.setmFirstPicUrl(jsonObject.getString("FirstPicUrl"));
                                news.setmId(jsonObject.getString("ID"));
                                news.setmPublishTime(jsonObject.getString("PublishTime"));
                                news.setmTitle(jsonObject.getString("Title"));
                                list.add(news);
                                
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("volley","jsonerror");
                            }

                        }
                        mTextView.setText(list.get(2).getmTitle());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("volley","error");

            }
        });
        queue.add(request);


    }


}
