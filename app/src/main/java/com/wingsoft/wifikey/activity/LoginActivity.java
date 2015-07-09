package com.wingsoft.wifikey.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.LoginState;
import com.wingsoft.wifikey.model.User;
import com.wingsoft.wifikey.util.HttpUtils;

public class LoginActivity extends ActionBarActivity {
    private Button mLoginButton;
    private User mUser;
    private EditText mEdit_Username,mEdit_Password;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LoginState.NETWORK_SUCCESS:
                    mUser = (User)msg.obj;
                    if(mUser!=null){
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("user",mUser);
                        setResult(LoginState.RESULT,intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"用户名密码错误",Toast.LENGTH_SHORT).show();

                    }
                    break;
                case LoginState.NETWORK_FAILED:
                    Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdit_Username = (EditText)findViewById(R.id.edit_username);
        mEdit_Password = (EditText)findViewById(R.id.edit_password);

        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEdit_Username.getText().toString();
                final String password = mEdit_Password.getText().toString();

                Log.i("POST",username + password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.login(username, password, mHandler);
                    }
                }).start();
            }
        });

    }

}
