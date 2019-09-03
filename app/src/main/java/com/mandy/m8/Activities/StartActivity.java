package com.mandy.m8.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.mandy.m8.RetrofitModel.CurrecnyConvter;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.R;
import com.mandy.m8.Adapter.StartAdapter;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.util.Config;
import com.mandy.m8.util.ProgressBarClass;

import com.mandy.m8.RetrofitModel.GetStartCategoryApi;
import com.mandy.m8.ServiceGenerator;
import com.mandy.m8.SpacesItemDecoration;
import com.mandy.m8.util.SharedRate;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    public static ArrayList<GetStartCategoryApi.Datum> arrayList = new ArrayList<>();
    public static ArrayList<String> arrayCate = new ArrayList<>();

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();

        double price = 2000;

        Locale swedishLocale = new Locale(getUserCountry(this), getUserCountry(this).toUpperCase());
        String currecy = Currency.getInstance(swedishLocale).getCurrencyCode();

        getCurrencyConvert(currecy);

        checkPermissions();
        if (CheckInternet.isInternetAvailable(StartActivity.this)) {
            getData();
        } else {
            startActivity(new Intent(StartActivity.this, NoInternetActivity.class));
        }

    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissions) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }

                    }
                }
                return;
            }

        }
    }

    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetStartCategoryApi> call = apiInterface.startApi();
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<GetStartCategoryApi>() {
            @Override
            public void onResponse(Call<GetStartCategoryApi> call, Response<GetStartCategoryApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            arrayList.add(response.body().getData().get(i));
                            arrayCate.add(response.body().getData().get(i).getName());
                            SetData();
                        }
                    } else {
                        Toast.makeText(StartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetStartCategoryApi> call, Throwable t) {
                Toast.makeText(StartActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void clear() {
        arrayList.clear();
        arrayCate.clear();
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void SetData() {
        StartAdapter startAdapter = new StartAdapter(getApplicationContext(), arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(startAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setMotionEventSplittingEnabled(false);
        startAdapter.notifyDataSetChanged();

    }


    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }


    private void getCurrencyConvert(final String currency) {
        String url = Config.CURRENCY_LINK + "CHF" + "&to=" + currency + "&amount=10";
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CurrecnyConvter> call = apiInterface.GetCurrency(url);

        call.enqueue(new Callback<CurrecnyConvter>() {
            @Override
            public void onResponse(Call<CurrecnyConvter> call, Response<CurrecnyConvter> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == true) {
                        SharedRate sharedRate = new SharedRate(StartActivity.this);
                        sharedRate.setSharedata(response.body().getInfo().getRate().toString(), currency);

                    }
                }
            }

            @Override
            public void onFailure(Call<CurrecnyConvter> call, Throwable t) {

            }
        });

    }

}
