package com.mandy.m8car.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckInternet {

    private static final String TAG = CheckInternet.class.getSimpleName();

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            Log.d(TAG, "internet connection available...");
            return false;
        } else {
            if (info.isConnected()) {
                Log.d(TAG, "internet connection available.....");
                return true;
            } else {
                Log.d(TAG, "internet connection");
                return true;
            }
        }
    }
}
