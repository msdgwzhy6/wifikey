package com.wingsoft.wifikey.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.LoginState;
import com.wingsoft.wifikey.model.User;
import com.wingsoft.wifikey.util.HttpUtils;

public class LoginActivity extends ActionBarActivity {
    private Button mLoginButton, mRegButton;
    private User mUser;
    private EditText mEdit_Username, mEdit_Password,mEdit_Password2;
    private boolean isReg = false;
    private LinearLayout mLinearLayout;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LoginState.NETWORK_SUCCESS:
                    mUser = (User) msg.obj;
                    progressCancel();
                    if (mUser != null) {
                        if (msg.arg1 == LoginState.REG_SUCCESS) {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("user", mUser);
                        setResult(LoginState.RESULT, intent);
                        finish();
                    } else {
                        if(msg.arg1 == LoginState.REG_FAILED){
                            Toast.makeText(LoginActivity.this,"用户已存在",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case LoginState.NETWORK_FAILED:
                    progressCancel();
                    Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdit_Username = (EditText) findViewById(R.id.edit_username);
        mEdit_Password = (EditText) findViewById(R.id.edit_password);
        mEdit_Password2 = (EditText)findViewById(R.id.edit_password2);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mRegButton = (Button) findViewById(R.id.button_reg);
        mLinearLayout = (LinearLayout)findViewById(R.id.linear_confirm);
        mLinearLayout.setVisibility(View.GONE);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEdit_Username.getText().toString();
                final String password = mEdit_Password.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"用户名密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("处理中");
                mProgressDialog.show();
                Log.i("POST", username + " Login "+  password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.sendPost(HttpUtils.LOGIN, username, password, mHandler);
                    }
                }).start();
            }
        });
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isReg){
                    isReg = true;
                    mLinearLayout.setVisibility(View.VISIBLE);
                    return;
                }
                final String username = mEdit_Username.getText().toString();
                final String password = mEdit_Password.getText().toString();
                final String password2 = mEdit_Password2.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(password2)){
                    Toast.makeText(LoginActivity.this,"用户名密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password2)){
                    Toast.makeText(LoginActivity.this,"确认密码不符，请检查",Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("处理中");
                mProgressDialog.show();
                Log.i("POST", "reg ：" + username +" "+ password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.sendPost(HttpUtils.REG, username, password, mHandler);
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onResume() {
        isReg = false;
        super.onResume();
    }

    private void progressCancel() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();

        }
        return true;
    }
}
