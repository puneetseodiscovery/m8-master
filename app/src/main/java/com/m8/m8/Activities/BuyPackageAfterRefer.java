package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.m8.m8.ApiInterface;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetPlanApi;
import com.m8.m8.RetrofitModel.SendPlanData;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.StripePayment;
import com.m8.m8.util.Config;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyPackageAfterRefer extends AppCompatActivity {

    View view;
    Context context;
    Button btnNovice, btnPlayer, btnProfessional, btnBusiness,btnFree;
    String number;
    SharedRate sharedRate;
    RelativeLayout backButton;

    TextView txtFreePrice, txtFreeHeading, txtFreeShare, txtNovicePrice, txtNoviceHeading, txtNoviceShare, txtPlayerPrice, txtPlayerHeading, txtPlayerShare,
            txtProPrice, txtProHeading, txtProShare, txtBusinessPrice, txtBusinessHeading, txtBusinessShare,
            txtFreeProperty, txtNoviceProperty, txtPlayerProperty, txtProProperty, getTxtBusinessProperty;

    String shareId;

    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    String amount = "";

    public com.google.android.gms.ads.AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_package_after_refer);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyPackageAfterRefer.this,StartActivity.class);
                startActivity(intent);
            }
        });

        init();




        // start payment Gateway

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
        
        //get the paln details
        getPlanDetails();

    }

    //paypal payment
    private void PaymentProgress(String id, String plan) {

        /*PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Purchase the " + plan + " share package", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);*/
        SharedToken sharedToken = new SharedToken(BuyPackageAfterRefer.this);

        Intent intent = new Intent(this, StripePayment.class);
        intent.putExtra("userId",sharedToken.getUserId());
        intent.putExtra("shareId",shareId);
        startActivity(intent);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {

        btnNovice = (Button) findViewById(R.id.btnNovice);
        btnFree = (Button) findViewById(R.id.btnFree);
        btnBusiness = (Button) findViewById(R.id.btnBusiness);
        btnPlayer = (Button) findViewById(R.id.btnPlayer);
        btnProfessional = (Button) findViewById(R.id.btnProfessional);


        txtFreePrice = (TextView) findViewById(R.id.txtFreePrice);
        txtFreeHeading = (TextView) findViewById(R.id.txtFreeHeading);
        txtFreeShare = (TextView) findViewById(R.id.txtFreeShares);
        txtFreeProperty = (TextView) findViewById(R.id.txtProperty);

        txtNoviceHeading = (TextView) findViewById(R.id.txtNoviceHeading);
        txtNoviceShare = (TextView) findViewById(R.id.txtNoviceShare);
        txtNovicePrice = (TextView) findViewById(R.id.txtNovicePrice);
        txtNoviceProperty = (TextView) findViewById(R.id.txtNoviceProperty);

        txtPlayerPrice = (TextView) findViewById(R.id.txtPlayerPrice);
        txtPlayerHeading = (TextView) findViewById(R.id.txtPlayerHeading);
        txtPlayerShare = (TextView) findViewById(R.id.txtPlayerShare);
        txtPlayerProperty = (TextView) findViewById(R.id.txtPlayerProperty);

        txtProPrice = (TextView) findViewById(R.id.txtProfessionalPrice);
        txtProHeading = (TextView) findViewById(R.id.txtProfessionalHeading);
        txtProShare = (TextView) findViewById(R.id.txtProfessionalShare);
        txtProProperty = (TextView) findViewById(R.id.txtProfessionalProperty);

        txtBusinessPrice = (TextView) findViewById(R.id.txtBusinessPrice);
        txtBusinessHeading = (TextView) findViewById(R.id.txtBusinessHeading);
        txtBusinessShare = (TextView) findViewById(R.id.txtBusinessShare);
        getTxtBusinessProperty = (TextView) findViewById(R.id.txtBusinessProperty);

        number = String.valueOf(HomeActivity.propertyNumber);

        sharedRate = new SharedRate(context);
        addAds();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {
                    try {
                        String payemtDetails = confirmation.toJSONObject().toString(4);
                        String id = confirmation.getProofOfPayment().getPaymentId().toString();

                        Log.d("++++", "" + id);

                        sendintoServer(id);


                        Log.d("payment", payemtDetails + "\n" + id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Transaction Cancel", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(context, "Invalid Transaction", Toast.LENGTH_LONG).show();
        }
    }

    private void sendintoServer(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        SharedToken sharedToken = new SharedToken(BuyPackageAfterRefer.this);
        Call<SendPlanData> call = apiInterface.sendBuyshareDetails(sharedToken.getUserId(), shareId, id);
        call.enqueue(new Callback<SendPlanData>() {
            @Override
            public void onResponse(Call<SendPlanData> call, Response<SendPlanData> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(context, "Payment Sucessful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,StartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendPlanData> call, Throwable t) {

                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void getPlanDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPlanApi> call = apiInterface.getPlanData();

        call.enqueue(new Callback<GetPlanApi>() {
            @Override
            public void onResponse(Call<GetPlanApi> call, Response<GetPlanApi> response) {

                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if (i == 0) {
                            GetPlanApi.Datum datum = response.body().getData().get(0);
                            //double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtFreePrice.setText("USD" + "\n" + datum.getPlanPrice().toString());
                            txtFreeHeading.setText(datum.getPlanName());
                            //txtFreeShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtFreeProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnFree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(BuyPackageAfterRefer.this,StartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        if (i == 1) {
                            final GetPlanApi.Datum datum = response.body().getData().get(1);
//                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());
                            txtNovicePrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtNoviceHeading.setText(datum.getPlanName());
                            //txtNoviceShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtNoviceProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnNovice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }


                        if (i == 2) {
                            final GetPlanApi.Datum datum = response.body().getData().get(2);
//                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());
                            txtPlayerPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtPlayerHeading.setText(datum.getPlanName());
                            //txtPlayerShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtPlayerProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnPlayer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }

                        if (i == 3) {
                            final GetPlanApi.Datum datum = response.body().getData().get(3);
                            //double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtProPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtProHeading.setText(datum.getPlanName());
                            //txtProShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtProProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnProfessional.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }

                        if (i == 4) {
                            final GetPlanApi.Datum datum = response.body().getData().get(4);
                            //double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtBusinessPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtBusinessHeading.setText(datum.getPlanName());
                            //getTxtBusinessProperty.setText("Upload  Unlimted  Property to Sell");
                            //txtBusinessShare.setText("Unlimted Shares");
                            btnBusiness.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetPlanApi> call, Throwable t) {

                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void addAds()
    {
        MobileAds.initialize(this, "ca-app-pub-3864021669352159~4680319766");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
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
