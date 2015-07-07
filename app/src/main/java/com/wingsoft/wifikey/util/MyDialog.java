package com.wingsoft.wifikey.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.thread.ImportThread;

import java.util.Dictionary;

/**
 * Created by wing on 15/7/6.
 */
public class MyDialog {
    public static void getDialog(final Context context){
        String[] list = { "一键导入", "手动添加", "查看当前wifi密码", "取消" };
        new AlertDialog.Builder(context)
                .setTitle("请选择")
                .setItems(list,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                                switch (arg1) {
                                    case 0:
                                        Main main = (Main)context;
                                        main.changeFragment(main.getFragmentAbout());
                                        ImportThread thread = new ImportThread(context);
                                        thread.start();

                                        break;
                                    case 1:
                                        Toast.makeText(context,"待开发",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(context,"待开发",Toast.LENGTH_SHORT).show();

                                    case 3:
                                        return;
                                }

                            }
                        }).show();
    }
}
