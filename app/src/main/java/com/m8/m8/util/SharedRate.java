package com.m8.m8.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRate {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedRate(Context context) {
        this.context = context;
    }

    public void setSharedata(String rate, String currencyCode) {
        sharedPreferences = context.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("rate", rate);
        editor.putString("currency", currencyCode);
        editor.apply();
    }

    public String getShared() {
        sharedPreferences = context.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("rate", "");
        return data;
    }

    public String getCurrencyCode() {
        sharedPreferences = context.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        String currency = sharedPreferences.getString("currency", "");
        return currency;
    }

    public void clearShaerd() {
        sharedPreferences = context.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
