package com.m8.m8.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.m8.m8.Models.TokenResponseModel;
import com.m8.m8.RetrofitModel.CurrecnyConvter;
import com.m8.m8.SplashScreen;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.R;
import com.m8.m8.Adapter.StartAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.Config;
import com.m8.m8.util.ProgressBarClass;

import com.m8.m8.RetrofitModel.GetStartCategoryApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.SpacesItemDecoration;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;

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

        SharedToken sharedToken = new SharedToken(StartActivity.this);
        final String token = sharedToken.getTokenId();
        if (token.length()>0)
        {
            getTokenData(token);
            AppUpdater appUpdater = new AppUpdater(this).setDisplay(Display.DIALOG);
            appUpdater.start();
        }

        init();

        double price = 2000;

        Locale swedishLocale = new Locale(getUserCountry(this), getUserCountry(this).toUpperCase());
        String currecy = Currency.getInstance(swedishLocale).getCurrencyCode();
        getCurrencyConvert(currecy);
        checkPermissions();
        //Log.d("++++++","++ this is test ++"+solution("Codility We test coders",14));
    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (haveNetworkConnection()) {
            if (CheckInternet.isInternetAvailable(StartActivity.this)) {
                getData();

            } else {
                startActivity(new Intent(StartActivity.this, NoInternetActivity.class));
            }
        }
        else
        {
            startActivity(new Intent(StartActivity.this, NoInternetActivity.class));
        }

        if (SplashScreen.code.length()>0)
        {
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
        SharedToken sharedToken = new SharedToken(StartActivity.this);
        final String userId = sharedToken.getUserId();
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetStartCategoryApi> call = apiInterface.startApi(userId);
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
                        if (response.body().getShowItservice()) {
                        arrayList.add(arrayList.get(arrayList.size()-1));
                        SetData();
                        }
                        if (!response.body().getPaypal() && !response.body().getStripe())
                        {
                            //openDialog();
                            SharedToken sharedToken = new SharedToken(StartActivity.this);
                            if (sharedToken.getFirstTime().equals("No"))
                            {
                                commission_earningsDialog();
                            }
                        }
                    } else {
//                        Toast.makeText(StartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetStartCategoryApi> call, Throwable t) {
//                Toast.makeText(StartActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }



    private void getTokenData(String token) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TokenResponseModel> call = apiInterface.getTokenDetails("Bearer "+token,"application/json");
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(402)) {

                        SharedToken sharedToken = new SharedToken(StartActivity.this);
                        sharedToken.clearShaerd();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                                edit().clear().apply();
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        //Toast.makeText(StartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
//                Toast.makeText(StartActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String solution(String message, int K) {
        // write your code in Java SE 8

        if (message.charAt(K+1)==' ')
        {
            message = message.substring(0,K);
        }
        else
        {
            for (int i=1;i<K;i--)
            {
                if (message.charAt(i)==' ')
                {
                    message = message.substring(0,i);
                }
            }
        }

        return message;

    }

//    private void openDialog() {
//        final Dialog dialog;
//        dialog = new Dialog(this, R.style.DialogThemes);
//        dialog.setContentView(R.layout.layout_commission_earning);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
//        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
//        TextView tvThis = (TextView) dialog.findViewById(R.id.tvThis);
//        TextView tvHere = (TextView) dialog.findViewById(R.id.tvHere);
//        TextView tvClickHere = (TextView) dialog.findViewById(R.id.tvClickHere);
//        TextView tvAccounthere = (TextView) dialog.findViewById(R.id.tvAccounthere);
//        CheckBox checkbox = dialog.findViewById(R.id.checkbox);
//        String text = "<font color=#000000>To withdraw sales earnings and sharers commission automatically from M8 you must have a STRIPE account, you can open an account here\n</font> <font color=#1d34fa>https://www.stripe.com</font>";
//        tvAccounthere.setText(Html.fromHtml(text));
//
//        tvAccounthere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StartActivity.this,OpenStripeAccount.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        tvThis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(StartActivity.this,StripeConnectActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        tvHere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(StartActivity.this,PaypalAddAtStart.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        tvClickHere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(StartActivity.this,WatchHowToUseAtStart.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });
//
//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                // TODO Auto-generated method stub
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    //dialog.dismiss();
//                    finishAffinity();
//                }
//                return true;
//            }
//        });
//
//
//        tv_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //dialog.dismiss();
//                Toast.makeText(StartActivity.this, "You must have a Stripe or PayPal account to continue.", Toast.LENGTH_SHORT).show();
//                //finishAffinity();
//            }
//        });
//        dialog.show();
//    }

    private void commission_earningsDialog() {
        final Dialog dialog;
        dialog = new Dialog(this, R.style.DialogThemes);
        dialog.setContentView(R.layout.layout_commission_earning);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tvStripLink = (TextView) dialog.findViewById(R.id.tvStripLink);
        TextView tvThis = (TextView) dialog.findViewById(R.id.tvThis);
        TextView tvHere = (TextView) dialog.findViewById(R.id.tvHere);
        TextView tvClickHere = (TextView) dialog.findViewById(R.id.tvClickHere);
        TextView tvAccounthere = (TextView) dialog.findViewById(R.id.tvAccounthere);
        CheckBox checkbox = dialog.findViewById(R.id.checkbox);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    SharedToken sharedToken = new SharedToken(StartActivity.this);
                    sharedToken.setFirstTime("Do");
                }
                else
                {
                    SharedToken sharedToken = new SharedToken(StartActivity.this);
                    sharedToken.setFirstTime("No");
                }
            }
        });

        tvThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartActivity.this,StripeConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvStripLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartActivity.this,OpenStripeAccount.class);
                startActivity(intent);
                finish();
            }
        });

        tvHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartActivity.this,PaypalAddAtStart.class);
                startActivity(intent);
                finish();
            }
        });
        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartActivity.this,WatchHowToUseAtStart.class);
                startActivity(intent);
                finish();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

                dialog.setOnKeyListener(new Dialog.OnKeyListener() {


            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();
    }

}
