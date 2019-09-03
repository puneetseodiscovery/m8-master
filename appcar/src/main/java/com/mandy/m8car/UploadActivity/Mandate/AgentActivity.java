package com.mandy.m8car.UploadActivity.Mandate;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.AddMandataApi;
import com.mandy.m8car.RetrofitModel.FreeGentApi;
import com.mandy.m8car.RetrofitModel.UploadBulkMandateApi;
import com.mandy.m8car.ServiceGenerator;
import com.mandy.m8car.util.SharedRate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView drawer, logo;
    TextView textView, textView2;
    Spinner spinner;
    String offer, date;
    String Id, type;
    FragmentManager manager;
    Button button;
    RelativeLayout relativeLayout;
    boolean UserPackage;
    SharedRate sharedRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandate);


        init();

        sharedRate = new SharedRate(AgentActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        drawer.setVisibility(View.GONE);
        textView.setText("VERY IMPORTANT");

        AddSpinner();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0")) {
                    bulkMandate();
                }
                if (type.equals("1")) {
                    if (UserPackage == true) {
                        getData();
                    } else {
                        getFreeData();
                    }
                }

            }
        });


        if (UserPackage == true) {
            textView2.setText(getResources().getString(R.string.agent));
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            textView2.setText(getResources().getString(R.string.freagent));
            relativeLayout.setVisibility(View.GONE);
        }


    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        logo = (ImageView) findViewById(R.id.toolbarLogo);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView2 = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.btnNext);
        spinner = (Spinner) findViewById(R.id.spineer);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);


        Id = getIntent().getStringExtra("PropertyId");
        type = getIntent().getStringExtra("type");
        UserPackage = getIntent().getExtras().getBoolean("boolean");

        Toast.makeText(this, "" + UserPackage, Toast.LENGTH_SHORT).show();
        manager = getSupportFragmentManager();
    }


    // set data into spinner
    private void AddSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Double> abc = new ArrayList<>();
        double a = 1.5;
        while (a < 20) {
            a = a + 0.5;
            abc.add(a);
        }
        for (int i = 0; i < abc.size(); i++) {
            arrayList.add(String.valueOf(abc.get(i)));
        }


        Log.d("abcd", "" + abc);


        //mandate percentage   drop down
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(AgentActivity.this, R.layout.spinnertext, arrayList);
        arrayAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(arrayAdapter1);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offer = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AddMandataApi> call = apiInterface.agentMandate(HomeActivity.userId, "0", Id, offer);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(AgentActivity.this, "please wait...");
        dialog.show();
        call.enqueue(new Callback<AddMandataApi>() {
            @Override
            public void onResponse(Call<AddMandataApi> call, Response<AddMandataApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                        double Minprice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getFixedStart());
                        double Maxprice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getFixedEnd());

                        Intent intent = new Intent(AgentActivity.this, MandateActivity.class);
                        intent.putExtra("PropertyId", response.body().getData().getItemId().toString());
                        intent.putExtra("min", Minprice);
                        intent.putExtra("max", Maxprice);
                        intent.putExtra("type", type);
                        startActivity(intent);


                    } else {
                        Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AgentActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddMandataApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AgentActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void bulkMandate() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<UploadBulkMandateApi> call = apiInterface.bulkAgentMandate(Id, HomeActivity.userId, "0", offer);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(AgentActivity.this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<UploadBulkMandateApi>() {
            @Override
            public void onResponse(Call<UploadBulkMandateApi> call, Response<UploadBulkMandateApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals(200)) {

                        Intent intent = new Intent(AgentActivity.this, MandateActivity.class);
                        intent.putExtra("PropertyId", response.body().getData().getId().toString());
                        intent.putExtra("type", type);
                        startActivity(intent);

                    } else {
                        Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(AgentActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadBulkMandateApi> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    private void getFreeData() {
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<FreeGentApi> call = apiInterface.freeMandate(HomeActivity.userId, "0", Id, "3", date + " 00:00:00");
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(AgentActivity.this, "please wait...");
        dialog.show();
        call.enqueue(new Callback<FreeGentApi>() {
            @Override
            public void onResponse(Call<FreeGentApi> call, Response<FreeGentApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AgentActivity.this, Mandate2Activity.class);
                        intent.putExtra("PropertyId", response.body().getData().getItemId().toString());
                        intent.putExtra("responce", "http://m8.amrdev.site/public/upload/items/mandate/" + response.body().getData().getMandatePdf());
                        intent.putExtra("type", type);
                        startActivity(intent);


                    } else {
                        Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AgentActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FreeGentApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AgentActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
