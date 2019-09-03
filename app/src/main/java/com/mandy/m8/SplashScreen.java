package com.mandy.m8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.Activities.LoginActivity;
import com.mandy.m8.Activities.StartActivity;
import com.mandy.m8.Fragments.CommissionSubFragment.DescrptedFragment;
import com.mandy.m8.RetrofitModel.TermsConditionApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.mandy.m8.util.CheckInternet;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    public static String code = "";
    public static TermsConditionApi.Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        if (CheckInternet.isInternetAvailable(SplashScreen.this)) {
            getData();
        } else {
            Toast.makeText(this, "" + getResources().getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
        }, 2000);




//            FirebaseDynamicLinks.getInstance()
//                    .getDynamicLink(getIntent())
//                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                        @Override
//                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                            // Get deep link from result (may be null if no link is found)
//                            Uri deepLink = null;
//                            if (pendingDynamicLinkData != null) {
//                                deepLink = pendingDynamicLinkData.getLink();
//
//                                code = deepLink.toString().substring(deepLink.toString().lastIndexOf('/') + 1);
//
//                                Log.d("deeplink", "" + code);
//                            }
//                            // ...
//                        }
//                    })
//                    .addOnFailureListener(this, new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("abc", "getDynamicLink:onFailure", e);
//                        }
//                    });

        }



    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TermsConditionApi> call = apiInterface.getTermsCondition();
        call.enqueue(new Callback<TermsConditionApi>() {
            @Override
            public void onResponse(Call<TermsConditionApi> call, Response<TermsConditionApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        data = response.body().getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<TermsConditionApi> call, Throwable t) {
            }
        });
    }


}
