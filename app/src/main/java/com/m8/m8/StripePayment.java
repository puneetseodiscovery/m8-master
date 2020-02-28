package com.m8.m8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbb20.CountryCodePicker;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.StartActivity;
import com.m8.m8.RetrofitModel.BuyApi;
import com.m8.m8.RetrofitModel.GetPlanApi;
import com.m8.m8.RetrofitModel.SendPlanData;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripePayment extends AppCompatActivity {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtTerms,itemText,priceText;
    ImageView imageView;

    CardMultilineWidget cardMultilineWidget;
    Button paybutton;
    String cardnumber,cardcvv,cardexpmonth,cardexpyear;
    String userId,shareId,directBuy,emailUser;
    CheckBox checkBox;
    CountryCodePicker ccp;
    ImageView productImage;
    String imageForStripe;

    String PUBLISH_KEY_STRIPE = "pk_live_R6yDm1aiPk60zEQ58zjjQnXE004W2MQN2a";

    // Card Views
    EditText edtEmail,tvNameOnCard,tvZipCode;
    CheckBox checkboxRemindme;
    TextView tvCountrySelect;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        AppCompatActivity activity = StripePayment.this;
        activity.setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra("userId");
        shareId = getIntent().getStringExtra("shareId");
        imageForStripe = getIntent().getStringExtra("imageForStripe");
        SharedToken sharedToken = new SharedToken(StripePayment.this);
        emailUser = sharedToken.getEmailId();
        init();
        if (getIntent().getStringExtra("directBuy")!=null&&getIntent().getStringExtra("directBuy").length()>0)
        {
            directBuy = getIntent().getStringExtra("directBuy");
            itemText.setText("Payment for "+getIntent().getStringExtra("directBuyName"));
            priceText.setText(directBuy);
            paybutton.setText("PAY "+directBuy);
        }
        else
        {
            directBuy="";
        }

/*
//        cardnumber = "4242424242424242";
//        cardcvv = "123";
//        cardexpmonth  = "10";
//        cardexpyear = "20";
 */

        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
//                    edtEmail.setError("Invalid Email");
//                }

//                final ProgressDialog myDialog = ProgressBarClass.showProgressDialog(StripePayment.this, "Please wait for a while...");
//                myDialog.show();

                paybutton.setClickable(false);
                //myDialog.dismiss();
                if (cardMultilineWidget==null||cardMultilineWidget.getCard()==null||cardMultilineWidget.getCard().getNumber()==null||cardMultilineWidget.getCard().getCVC()==null||cardMultilineWidget.getCard().getExpMonth()==null)
                {
                    Toast.makeText(StripePayment.this, "Fill your details.", Toast.LENGTH_SHORT).show();
                    paybutton.setClickable(true);
                }
                else {
                    cardnumber = cardMultilineWidget.getCard().getNumber();
                    cardexpmonth = cardMultilineWidget.getCard().getExpMonth()+"";
                    cardexpyear = cardMultilineWidget.getCard().getExpYear()+"";
                    cardcvv = cardMultilineWidget.getCard().getCVC();
                    Log.d("++++++++","++ Stripe payment details ++"+cardnumber);
                    Log.d("++++++++","++ Stripe payment details ++"+cardexpmonth);
                    Log.d("++++++++","++ Stripe payment details ++"+cardexpyear);
                    Log.d("++++++++","++ Stripe payment details ++"+cardcvv);
                    dialog = ProgressBarClass.showProgressDialog(StripePayment.this, "Please wait");
                    dialog.show();
                    onClickSomething(cardnumber, cardexpmonth, cardexpyear, cardcvv);
                    if (checkBox.isChecked()) {
                        SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("hello1", "1");
                        edit.putString("pemm", cardnumber);
                        edit.putString("lemm", cardexpmonth);
                        edit.putString("gemm", cardexpyear);
                        edit.apply();
                    } else {
                        SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("hello1", "0");
                        edit.apply();
                    }
                    paybutton.setClickable(true);
                }
            }
        });
    }

    private void init() {

        getPlanDetails();

        cardMultilineWidget = findViewById(R.id.card_widget);
        paybutton = findViewById(R.id.paybutton);
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        textView = (TextView) findViewById(R.id.toolbarText);
        itemText = (TextView) findViewById(R.id.itemText);
        priceText = (TextView) findViewById(R.id.priceText);
        imageView = (ImageView) findViewById(R.id.toolbarLogo);


        //TODO init payment UI views

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        tvNameOnCard = (EditText) findViewById(R.id.tvNameOnCard);
        tvZipCode = (EditText) findViewById(R.id.tvZipCode);
        tvCountrySelect = (TextView) findViewById(R.id.tvCountrySelect);
        ccp = (CountryCodePicker) findViewById(R.id.business_country);
        productImage = findViewById(R.id.productImage);
        if (imageForStripe!=null)
        {
            Glide.with(this).load(imageForStripe).dontAnimate().dontTransform().into(productImage);
        }
        //checkboxRemindme = (CheckBox) findViewById(R.id.checkboxRemindme);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                tvCountrySelect.setText(ccp.getSelectedCountryName());
            }
        });

        edtEmail.setText(emailUser);


        drawer.setVisibility(View.INVISIBLE);


        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        textView.setText("");
        imageView.setVisibility(View.VISIBLE);
        checkBox = (CheckBox) findViewById(R.id.checkboxSign);

        SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
        String id = shared.getString("hello1", "");
        if (id.equals("1")) {
            checkBox.setChecked(true);
            cardMultilineWidget.setCardNumber(shared.getString("pemm", ""));
//            editPassword.setText(shared.getString("pem", ""));
//            editLogin.setText(shared.getString("lem", ""));

        } else {
            checkBox.setChecked(false);
            cardMultilineWidget.setCardNumber("");
        }
    }

    public void onClickSomething(final String cardNumber, final String cardExpMonth, final String cardExpYear, final String cardCVC) {
        Card card = new Card(
                cardNumber,
                Integer.valueOf(cardExpMonth),
                Integer.valueOf(cardExpYear),
                cardCVC

        );

        Log.d("++++++++","++ Stripe payment details ++"+cardNumber);
        Log.d("++++++++","++ Stripe payment details ++"+cardExpMonth);
        Log.d("++++++++","++ Stripe payment details ++"+cardExpYear);
        Log.d("++++++++","++ Stripe payment details ++"+cardCVC);

        card.validateNumber();
        card.validateCVC();

        //Stripe stripe = new Stripe(StripePayment.this, "pk_test_7nMgS2ZoQnOOId6BtAZ0u5Jw00TXVNccdT");
        Stripe stripe = new Stripe(StripePayment.this, PUBLISH_KEY_STRIPE);
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                Log.d("card_token", "card " + error);
                Toast.makeText(StripePayment.this, ""+error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(@NonNull final Token token) {
                Log.d("card_token1", "card " + token.toString());
                Log.d("token", "fff " + token.getId());
                Log.d("cardinfo",cardNumber+"");
                if (directBuy.length()>0)
                {
                    sendintoServerDirect(token.getId());
                }
                else {
                    sendintoServer(token.getId());
                }

                //  tokenPay = token.getId();

                //  SetData(userToken, eventId, totalprice, tokenPay, total_tickets);
            }
        });
    }

    private void sendintoServer(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SendPlanData> call = apiInterface.sendBuyshareDetails(userId, shareId, id);
//        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(StripePayment.this, "Please wait");
//        dialog.show();
        call.enqueue(new Callback<SendPlanData>() {
            @Override
            public void onResponse(Call<SendPlanData> call, Response<SendPlanData> response) {
                dialog.dismiss();
                if (response.body().getStatus()==200) {
                    Toast.makeText(StripePayment.this, "Payment Sucessful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StripePayment.this,StartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(StripePayment.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<SendPlanData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(StripePayment.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendintoServerDirect(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BuyApi> call = apiInterface.sendBuyshareDetailsDirect(userId, shareId, id);
//        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(StripePayment.this, "Please wait");
//        dialog.show();
        call.enqueue(new Callback<BuyApi>() {
            @Override
            public void onResponse(Call<BuyApi> call, Response<BuyApi> response) {
                dialog.dismiss();
                if (response.body().getStatus()==200) {
                    Toast.makeText(StripePayment.this, ""+response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StripePayment.this,StartActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(StripePayment.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BuyApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(StripePayment.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    private void getPlanDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPlanApi> call = apiInterface.getPlanData();
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(StripePayment.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetPlanApi>() {
            @Override
            public void onResponse(Call<GetPlanApi> call, Response<GetPlanApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if (shareId.equals("1")) {
                            GetPlanApi.Datum datum = response.body().getData().get(0);
                        }

                        if (shareId.equals("2")) {
                            final GetPlanApi.Datum datum = response.body().getData().get(1);
                            if (getIntent().getStringExtra("directBuy")!=null&&getIntent().getStringExtra("directBuy").length()>0)
                            {

                            }
                            else
                            {
                                directBuy="";
                                itemText.setText("Payment for "+datum.getPlanName()+" Package");
                                priceText.setText(datum.getPlanPrice().toString()+" USD");
                                paybutton.setText("PAY "+ datum.getPlanPrice().toString()+" USD" );
                            }

                        }


                        if (shareId.equals("3")) {
                            final GetPlanApi.Datum datum = response.body().getData().get(2);
                            if (getIntent().getStringExtra("directBuy")!=null&&getIntent().getStringExtra("directBuy").length()>0)
                            {

                            }
                            else
                            {
                                directBuy="";
                                itemText.setText("Payment for "+datum.getPlanName()+" Package");
                                priceText.setText(datum.getPlanPrice().toString()+" USD");
                                paybutton.setText("PAY "+ datum.getPlanPrice().toString()+" USD" );
                            }

                        }

                        if (shareId.equals("4")) {
                            final GetPlanApi.Datum datum = response.body().getData().get(3);
                            if (getIntent().getStringExtra("directBuy")!=null&&getIntent().getStringExtra("directBuy").length()>0)
                            {

                            }
                            else
                            {
                                directBuy="";
                                itemText.setText("Payment for "+datum.getPlanName()+" Package");
                                priceText.setText(datum.getPlanPrice().toString()+" USD");
                                paybutton.setText("PAY "+ datum.getPlanPrice().toString()+" USD" );
                            }

                        }

                        if (shareId.equals("5")) {
                            final GetPlanApi.Datum datum = response.body().getData().get(4);
                            if (getIntent().getStringExtra("directBuy")!=null&&getIntent().getStringExtra("directBuy").length()>0)
                            {

                            }
                            else
                            {
                                directBuy="";
                                itemText.setText("Payment for "+datum.getPlanName()+" Package");
                                priceText.setText(datum.getPlanPrice().toString()+" USD");
                                paybutton.setText("PAY "+ datum.getPlanPrice().toString()+" USD" );
                            }

                        }
                    }
                } else {
                    Toast.makeText(StripePayment.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetPlanApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(StripePayment.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
