package com.wingsoft.wifikey.model;

import java.io.Serializable;

/**
 * Created by wing on 15/7/9.
 */
public class User implements Serializable{
    private String mUsername;
    private String mPassword;
    public User(String username,String password){
        mUsername = username;
        mPassword = password;
    }
    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
