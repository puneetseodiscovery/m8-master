package com.m8sworld.m8.Activities.itservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.Activities.LoginActivity;
import com.m8sworld.m8.Activities.StartActivity;
import com.m8sworld.m8.Activities.Terms.TermsActivity;
import com.m8sworld.m8.ApiInterface;
import com.m8sworld.m8.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment2;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.TermsConditionApi;
import com.m8sworld.m8.ServiceGenerator;
import com.m8sworld.m8.util.SharedToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ITServices extends AppCompatActivity {

    RecyclerView recyclerView;
    Button submitButton;
    RelativeLayout backButton;
    ArrayList<ITServiceResponseModel.Datum> arrayList = new ArrayList();
    ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itservices);

        recyclerView = findViewById(R.id.recyclerView);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progDailog = new ProgressDialog(this);
        progDailog.setMessage("Loading");
        progDailog.setCancelable(false);


        getData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ITServiceAdapter.lastCheckedPosition!=-1)
                {
                    SharedToken sharedToken = new SharedToken(ITServices.this);
                    String userId = sharedToken.getUserId();
                    String categoryId = arrayList.get(ITServiceAdapter.lastCheckedPosition).getId().toString();
                    sendData(userId,categoryId);
                }
                else
                {
                    Toast.makeText(ITServices.this, "Choose your service.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void setData(ArrayList arrayList)
    {
        ITServiceAdapter itServiceAdapter = new ITServiceAdapter(this,arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itServiceAdapter);
    }

    private void getData() {
        progDailog.show();
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ITServiceResponseModel> call = apiInterface.getITServices();
        call.enqueue(new Callback<ITServiceResponseModel>() {
            @Override
            public void onResponse(Call<ITServiceResponseModel> call, Response<ITServiceResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        progDailog.dismiss();
                        arrayList.addAll(response.body().getData());
                        setData(arrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ITServiceResponseModel> call, Throwable t) {
                progDailog.dismiss();
            }
        });
    }

    private void sendData(String userId,String categoryId) {
        progDailog.show();
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ITServiceRequestModel> call = apiInterface.sendITRequest(userId,categoryId);
        call.enqueue(new Callback<ITServiceRequestModel>() {
            @Override
            public void onResponse(Call<ITServiceRequestModel> call, Response<ITServiceRequestModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {

                        progDailog.dismiss();
                        final Dialog dialog = new Dialog(ITServices.this);

                        dialog.setContentView(R.layout.custom_dialog_upload);
                        dialog.setCancelable(true);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        dialog.show();

                        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                        text.setText(response.body().getMessage());

                        btnBuy.setText("Ok");

                        btnBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ITServiceAdapter.lastCheckedPosition=-1;
                                Intent intent = new Intent(ITServices.this, StartActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ITServiceRequestModel> call, Throwable t) {
                progDailog.dismiss();
                Toast.makeText(ITServices.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
