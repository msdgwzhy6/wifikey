package com.wingsoft.wifikey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsoft.wifikey.R;

/**
 * Created by wing on 15/7/8.
 */
public class HelpFragment extends Fragment {
    private TextView mTextView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceBundle){
        View view = inflater.inflate(R.layout.fragment_help,container,false);
        mTextView = (TextView) view.findViewById(R.id.helptext);
        mTextView.setText(Html.fromHtml("<h1>wifi密码读取器能做什么？</h1>wifi密码读取器可以帮您保存您的wifi名和密码，让您更直观的" +
                "管理您的wifi信息。并且您可以给您的wifi添加备注。<h1>wifi信息要自己一条一条添加吗，好麻烦。</h1>完全不必！您可以点击一键导入，来让管家自己导入。" +
                "您曾经连过的wifi信息。当然您也可以根据自己的需要来手动添加信息。<h1>我点击了“一键导入”按钮，没有反应，提示需要Root权限</h1>" +
                "如果您需要使用一键导入功能，请root您的手机。普通手机请下载“百度一键root”等root工具进行root，点击 http://root.baidu.com 下载,小米手机需刷成“开发版”，并在系统自带的“安全中心”->“授权管理”中打开root权限 来获得root权限(红米除外)开发版下载地址：http://www.miui.com/getrom.php" +
                "<h1>为什么要加广告，好烦人。</h1>请您理解，作者尚为在校大学生一枚，做出产品加入广告缓解家庭压力也是无奈之举，还请谅解。"));

        return view;
    }

}
