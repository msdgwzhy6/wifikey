package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.adapter.NewsAdapter;
import com.wingsoft.wifikey.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wing on 15/7/6.
 */
public class NewsFragment extends Fragment{
    private ListView mListView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_news,container,false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        mListView = (ListView)view.findViewById(R.id.listview_news);
        progressDialog.setMessage("loading");
        progressDialog.show();
        final ArrayList<News> list = new ArrayList<News>();



        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        Log.i("volley",list.size()+"");
                        NewsAdapter adapter = new NewsAdapter(getActivity(),list);
                        mListView.setAdapter(adapter);

                        progressDialog.cancel();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("volley","error");
                Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
        queue.add(request);
        return view;

    }

}
