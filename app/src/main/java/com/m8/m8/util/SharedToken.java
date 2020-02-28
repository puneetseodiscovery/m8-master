package com.m8.m8.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedToken {
    public Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public SharedToken(Context context) {
        this.context = context;

    }

    public void setUserId(String token) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userId", token);
        editor.apply();
    }

    public void setCatid(String catid) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("catId", catid);
        editor.apply();
    }

    public String getUserId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("userId", "");
        return data;
    }

    public String getCatId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("catId", "");
        return data;
    }

    public void clearShaerd() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public void setTokenId(String token) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("tokenId", token);
        editor.apply();
    }

    public String getTokenId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("tokenId", "");
        return data;
    }

    public void setEmailId(String token) {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("emailId", token);
        editor.apply();
    }

    public String getEmailId() {
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("emailId", "");
        return data;
    }

    public void setFirstTime(String token) {
        sharedPreferences = context.getSharedPreferences("First", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("firstTime", token);
        editor.apply();
    }

    public String getFirstTime() {
        sharedPreferences = context.getSharedPreferences("First", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("firstTime", "");
        return data;
    }

}
