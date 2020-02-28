package com.m8.m8;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.m8.m8.Activities.LoginActivity;
import com.m8.m8.Activities.SignupActivity;
import com.m8.m8.Activities.StartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.util.SharedToken;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    public static String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        if (CheckInternet.isInternetAvailable(SplashScreen.this)) {

        } else {
            Toast.makeText(this, "" + getResources().getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            //code = deepLink.toString().substring(deepLink.toString().lastIndexOf('/') + 1);
                            code = deepLink.toString();

                            Log.d("deeplink", "" + deepLink);
                            return;
                        }
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("abc", "getDynamicLink:onFailure", e);
                    }
                });

        final SharedToken sharedToken = new SharedToken(SplashScreen.this);
        final String token = sharedToken.getUserId();
        if (sharedToken.getFirstTime()==null || sharedToken.getFirstTime().equals("")) {
            sharedToken.setFirstTime("Yes");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedToken.getFirstTime().equals("Yes"))
                {
                    sharedToken.setFirstTime("No");
                    Intent intent = new Intent(SplashScreen.this, SignupActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    if (token.equals("")) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 2000);
    }
}
