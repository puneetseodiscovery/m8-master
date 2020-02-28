package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;
import com.m8.m8.ApiInterface;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.BankDeatilsApi;
import com.m8.m8.RetrofitModel.BankDeatilsUploadApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaypalAddAtStart extends AppCompatActivity {

    Toolbar toolbar;
    ImageView toolbarLogo, drawer;
    View view;
    Context context;
    Button btnOk;
    EditText edtBankName, edtAddress1, edtCity, edtPostcode, edtAccountName, edtAccountNumber, edtIBAN, edtSwift, edtPaypal;
    String bankName, address1, address2, city, country, postcode, accountName, accountNumber, iban, swift, paypal;

    CountryCodePicker ccp;

    public com.google.android.gms.ads.AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_add_at_start);

        context = PaypalAddAtStart.this;

        init();

        AppCompatActivity activity = (AppCompatActivity) this;
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drawer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                        } else
                            HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                });

        drawer.setVisibility(View.INVISIBLE);

        toolbarLogo.setVisibility(View.VISIBLE);

        if (CheckInternet.isInternetAvailable(context)) {
            //GetBankDetail();
        } else {
            Toast.makeText(context, "" + this.getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(edtBankName.getText().toString()) && edtBankName.getText().length() > 20) {
//                    edtBankName.setError("Invalid Bank name");
//                } else if (TextUtils.isEmpty(edtAddress1.getText().toString())) {
//                    edtAddress1.setError("Invalid Address");
//                } else if (TextUtils.isEmpty(edtCity.getText().toString())) {
//                    edtCity.setError("Invalid City");
//                } else if (TextUtils.isEmpty(edtPostcode.getText().toString())) {
//                    edtPostcode.setError("Invalid Postcode");
//                } else if (TextUtils.isEmpty(edtAccountName.getText().toString())) {
//                    edtAccountName.setError("Invalid Account Name");
//                } else if (TextUtils.isEmpty(edtAccountNumber.getText().toString())) {
//                    edtAccountNumber.setError("Invalid Accout Number");
//                } else if (TextUtils.isEmpty(edtIBAN.getText().toString())) {
//                    edtIBAN.setError("Invalid IBAN");
//                } else if (TextUtils.isEmpty(edtSwift.getText().toString())) {
//                    edtSwift.setError("Invalid Swift code");
//                } else if (TextUtils.isEmpty(edtPaypal.getText().toString())) {
//                    edtPaypal.setError("Invalid PayPal email");
//                } else {
//                    if (CheckInternet.isInternetAvailable(context)) {
//
//                        BankApi();
//                    } else {
//                        Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
//                    }
//
//                }
//                if (TextUtils.isEmpty(edtPaypal.getText().toString())) {
//                    edtPaypal.setError("Invalid PayPal email");
//                } else {
                if (CheckInternet.isInternetAvailable(context)) {

                    BankApi();
                } else {
                    Toast.makeText(context, "" + getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                }

//                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        toolbarLogo = (ImageView) findViewById(R.id.toolbarLogo);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        btnOk = (Button) findViewById(R.id.button_ok);
        edtAccountName = (EditText) findViewById(R.id.bank_accountName);
        edtAccountNumber = (EditText) findViewById(R.id.bank_accountNumber);
        edtAddress1 = (EditText) findViewById(R.id.bank_address1);
        edtBankName = (EditText) findViewById(R.id.bank_name);
        edtCity = (EditText) findViewById(R.id.bank_city);
        ccp = (CountryCodePicker) findViewById(R.id.bank_country);
        edtIBAN = (EditText) findViewById(R.id.bank_iban);
        edtPostcode = (EditText) findViewById(R.id.bank_postcode);
        edtSwift = (EditText) findViewById(R.id.bank_swift);
        edtPaypal = (EditText) findViewById(R.id.paypal);
        addAds();
    }


    //send the bank information to api
    private void BankApi() {
        bankName = edtBankName.getText().toString();
        address1 = edtAddress1.getText().toString();
        city = edtCity.getText().toString();
        postcode = edtPostcode.getText().toString();
        country = ccp.getSelectedCountryName();
        accountName = edtAccountName.getText().toString();
        accountNumber = edtAccountNumber.getText().toString();
        iban = edtIBAN.getText().toString();
        swift = edtSwift.getText().toString();
        paypal = edtPaypal.getText().toString();

        Log.d("getStringData", bankName + address2 + postcode + paypal);

        SharedToken sharedToken = new SharedToken(PaypalAddAtStart.this);
        String userId = sharedToken.getUserId();

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BankDeatilsUploadApi> call = apiInterface.bankDetailsApi(userId, bankName, address1, city, postcode, country, accountName, accountNumber, iban, swift, paypal);

        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait");
        dialog.show();

        call.enqueue(new Callback<BankDeatilsUploadApi>() {
            @Override
            public void onResponse(Call<BankDeatilsUploadApi> call, Response<BankDeatilsUploadApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BankDeatilsUploadApi> call, Throwable t) {
                dialog.dismiss();

            }
        });

    }


    //getBank details from apis
    private void GetBankDetail() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BankDeatilsApi> call = apiInterface.bankDetailsGet(HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<BankDeatilsApi>() {
            @Override
            public void onResponse(Call<BankDeatilsApi> call, Response<BankDeatilsApi> response) {

                dialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        edtBankName.setText(response.body().getData().getBankName());
                        edtAddress1.setText(response.body().getData().getAddress1());
                        edtCity.setText(response.body().getData().getTown());
                        edtPostcode.setText(response.body().getData().getPostCode());
                        edtAccountName.setText(response.body().getData().getAccountName());
                        edtAccountNumber.setText(response.body().getData().getAccountNumber());
                        edtIBAN.setText(response.body().getData().getIban());
                        edtSwift.setText(response.body().getData().getSwiftCode());
                        edtPaypal.setText(response.body().getData().getPaypalEmail());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                    //Toast.makeText(context, "Fill the bank Details", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BankDeatilsApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addAds()
    {
        MobileAds.initialize(context, "ca-app-pub-3864021669352159~4680319766");

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE231").build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("+++++++","+++++ loaded ++++++");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("+++++++","+++++ not loaded ++++++"+i);
            }
        });
    }
}
