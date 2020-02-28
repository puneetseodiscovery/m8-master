package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.Adapter.CommissionAdapter;
import com.m8.m8.Adapter.ReferralsAdapter;
import com.m8.m8.Adapter.SalesAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.Models.CommissionModel;
import com.m8.m8.Models.MyM8sReferralModel;
import com.m8.m8.Models.SalesModel;
import com.m8.m8.R;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.ProgressBarClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyM8sEarningActivity extends AppCompatActivity {

    TextView referrals,sales,commissions;
    com.google.android.gms.ads.AdView mAdView;
    RecyclerView itemRecyclerView;
    ArrayList<MyM8sReferralModel.Datum> arrayList = new ArrayList<>();
    ArrayList<CommissionModel.Datum> arrayListCommission = new ArrayList<>();
    ArrayList<SalesModel.Datum> arrayListSales = new ArrayList<>();
    ReferralsAdapter referralsAdapter;
    SalesAdapter salesAdapter;
    String userId;
    RelativeLayout noRecord;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_m8s_earning);
        referrals = findViewById(R.id.referrals);
        sales = findViewById(R.id.sales);
        commissions = findViewById(R.id.commissions);
        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        noRecord = findViewById(R.id.noRecord);
        backButton = findViewById(R.id.backButton);
        userId = getIntent().getStringExtra("userId");

        referrals.setBackgroundResource(R.drawable.potentionalbutton);
        sales.setBackgroundResource(R.drawable.potentialbuttoninactive);
        commissions.setBackgroundResource(R.drawable.potentialbuttoninactive);

        if (userId.length()>0)
        {
            referralEarning(userId);
        }


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
                mAdView.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        referrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                referrals.setBackgroundResource(R.drawable.potentionalbutton);
                sales.setBackgroundResource(R.drawable.potentialbuttoninactive);
                commissions.setBackgroundResource(R.drawable.potentialbuttoninactive);
                if (userId.length()>0)
                {
                    referralEarning(userId);
                }

            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sales.setBackgroundResource(R.drawable.potentionalbutton);
                referrals.setBackgroundResource(R.drawable.potentialbuttoninactive);
                commissions.setBackgroundResource(R.drawable.potentialbuttoninactive);

                if (userId.length()>0)
                {
                    salesEarning(userId);
                }

            }
        });

        commissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commissions.setBackgroundResource(R.drawable.potentionalbutton);
                referrals.setBackgroundResource(R.drawable.potentialbuttoninactive);
                sales.setBackgroundResource(R.drawable.potentialbuttoninactive);
                if (userId.length()>0)
                {
                    commissionEarning(userId);
                }
            }
        });

    }

    public void referralEarning(String userId)
    {
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<MyM8sReferralModel> call = apiInterface.getReferrals(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(MyM8sEarningActivity.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<MyM8sReferralModel>() {
            @Override
            public void onResponse(Call<MyM8sReferralModel> call, Response<MyM8sReferralModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    clear();
                    if (response.body().getStatus().equals(200)) {
                        if (response.body().getData().size()>0)
                        {
                            noRecord.setVisibility(View.GONE);
                            itemRecyclerView.setVisibility(View.VISIBLE);
                            arrayList.addAll(response.body().getData());
                            referralsAdapter = new ReferralsAdapter(MyM8sEarningActivity.this,arrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            itemRecyclerView.setLayoutManager(mLayoutManager);
                            itemRecyclerView.addItemDecoration(new DividerItemDecoration(itemRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            itemRecyclerView.setAdapter(referralsAdapter);
                        }
                        else
                        {
                            noRecord.setVisibility(View.VISIBLE);
                            itemRecyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(MyM8sEarningActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MyM8sEarningActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<MyM8sReferralModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MyM8sEarningActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void salesEarning(String userId)
    {
        //userId = "156";
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SalesModel> call = apiInterface.getSales(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(MyM8sEarningActivity.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<SalesModel>() {
            @Override
            public void onResponse(Call<SalesModel> call, Response<SalesModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    arrayListSales.clear();
                    if (response.body().getStatus().equals(200)) {
                        if (response.body().getData().size()>0)
                        {
                            noRecord.setVisibility(View.GONE);
                            itemRecyclerView.setVisibility(View.VISIBLE);
                            arrayListSales.addAll(response.body().getData());
                            SalesAdapter salesAdapter = new SalesAdapter(MyM8sEarningActivity.this,arrayListSales);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            itemRecyclerView.setLayoutManager(mLayoutManager);
                            itemRecyclerView.addItemDecoration(new DividerItemDecoration(itemRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            itemRecyclerView.setAdapter(salesAdapter);
                        }
                        else
                        {
                            noRecord.setVisibility(View.VISIBLE);
                            itemRecyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(MyM8sEarningActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MyM8sEarningActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SalesModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MyM8sEarningActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void commissionEarning(String userId)
    {
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CommissionModel> call = apiInterface.getCommission(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(MyM8sEarningActivity.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<CommissionModel>() {
            @Override
            public void onResponse(Call<CommissionModel> call, Response<CommissionModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    arrayListCommission.clear();
                    if (response.body().getStatus().equals(200)) {
                        if (response.body().getData().size()>0)
                        {
                            noRecord.setVisibility(View.GONE);
                            itemRecyclerView.setVisibility(View.VISIBLE);
                            arrayListCommission.addAll(response.body().getData());
                            CommissionAdapter commissionAdapter = new CommissionAdapter(MyM8sEarningActivity.this,arrayListCommission);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            itemRecyclerView.setLayoutManager(mLayoutManager);
                            itemRecyclerView.addItemDecoration(new DividerItemDecoration(itemRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            itemRecyclerView.setAdapter(commissionAdapter);
                        }
                        else
                        {
                            noRecord.setVisibility(View.VISIBLE);
                            itemRecyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MyM8sEarningActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MyM8sEarningActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommissionModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MyM8sEarningActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clear() {
        arrayList.clear();
    }
}
