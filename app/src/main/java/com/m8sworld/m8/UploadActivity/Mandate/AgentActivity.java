package com.m8sworld.m8.UploadActivity.Mandate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.ApiInterface;
import com.m8sworld.m8.util.ProgressBarClass;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.AddMandataApi;
import com.m8sworld.m8.RetrofitModel.FreeGentApi;
import com.m8sworld.m8.RetrofitModel.UploadBulkMandateApi;
import com.m8sworld.m8.ServiceGenerator;
import com.m8sworld.m8.util.SharedRate;
import com.m8sworld.m8.util.SharedToken;

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
    TextView textView, textView2,important,clickLink;
    Spinner spinner;
    String offer, date;
    String Id, type;
    FragmentManager manager;
    Button button;
    RelativeLayout relativeLayout;
    boolean UserPackage;
    SharedRate sharedRate;
    String freeOffer = "";

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
        drawer.setVisibility(View.INVISIBLE);
//        logo.setVisibility(View.GONE);
        textView.setText("VERY IMPORTANT");

        AddSpinner();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedToken sharedToken = new SharedToken(AgentActivity.this);
                sharedToken.getCatId();
                if (!sharedToken.getCatId().equals("1"))
                {
                    UserPackage = false;
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    date = date + " 00:00:00";
                    if (sharedToken.getCatId().equals("2"))
                    {
                        freeOffer = "10";

                    }else if (sharedToken.getCatId().equals("3"))
                    {
                        freeOffer = "12";
                    }else if (sharedToken.getCatId().equals("4"))
                    {
                        freeOffer = "12";
                    }else if (sharedToken.getCatId().equals("5"))
                    {
                        freeOffer = "12";
                    }else if (sharedToken.getCatId().equals("7"))
                    {
                        freeOffer = "12";
                    }
                }
                if (type.equals("1")) {
                    if (UserPackage) {
                        getData();
                    } else {
                        getFreeData();
                    }
                }

                if (type.equals("0")) {
                    if (UserPackage) {
                        bulkMandate();
                    } else {
                        bulkFreeMandate();
                    }

                }

            }
        });


        if (UserPackage == true) {

            SharedToken sharedToken = new SharedToken(this);
            sharedToken.getCatId();

            if (sharedToken.getCatId().equals("1"))
            {
                textView2.setText(getResources().getString(R.string.agent));
                relativeLayout.setVisibility(View.VISIBLE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.VISIBLE);
            }
            else if (sharedToken.getCatId().equals("2"))
            {
                textView2.setText(getResources().getString(R.string.freagentJobs));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
                offer = "10";
            }else if (sharedToken.getCatId().equals("3"))
            {
                textView2.setText(getResources().getString(R.string.freagentCars));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
                offer = "12";
            }else if (sharedToken.getCatId().equals("4"))
            {
                textView2.setText(getResources().getString(R.string.freagentBoatss));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
                offer = "12";
            }else if (sharedToken.getCatId().equals("5"))
            {
                textView2.setText(getResources().getString(R.string.freagentJwels));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
                offer = "12";
            }else if (sharedToken.getCatId().equals("6"))
            {
                textView2.setText(getResources().getString(R.string.freagentInvest));
                relativeLayout.setVisibility(View.VISIBLE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.VISIBLE);
                important.setText(getResources().getString(R.string.freagentInvest2));
            }else if (sharedToken.getCatId().equals("7"))
            {
                offer = "12";
                textView2.setText(getResources().getString(R.string.freagentMics));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
            }
        } else {

            SharedToken sharedToken = new SharedToken(this);
            sharedToken.getCatId();
            if (sharedToken.getCatId().equals("1"))
            {
                freeOffer = "3";
                textView2.setText(getResources().getString(R.string.freagent));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.VISIBLE);
                important.setVisibility(View.GONE);
            }
            else if (sharedToken.getCatId().equals("2"))
            {
                freeOffer = "10";
                textView2.setText(getResources().getString(R.string.freagentJobs));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);

            }else if (sharedToken.getCatId().equals("3"))
            {
                freeOffer = "12";
                textView2.setText(getResources().getString(R.string.freagentCars));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
            }else if (sharedToken.getCatId().equals("4"))
            {
                freeOffer = "12";
                textView2.setText(getResources().getString(R.string.freagentBoatss));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
            }else if (sharedToken.getCatId().equals("5"))
            {
                freeOffer = "12";
                textView2.setText(getResources().getString(R.string.freagentJwels));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
            }else if (sharedToken.getCatId().equals("6"))
            {
                freeOffer = "5";
                textView2.setText(getResources().getString(R.string.freagentInvest));
                relativeLayout.setVisibility(View.VISIBLE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.VISIBLE);
                important.setText(getResources().getString(R.string.freagentInvest2));

            }else if (sharedToken.getCatId().equals("7"))
            {
                freeOffer = "12";
                textView2.setText(getResources().getString(R.string.freagentMics));
                relativeLayout.setVisibility(View.GONE);
                clickLink.setVisibility(View.GONE);
                important.setVisibility(View.GONE);
            }
        }

        clickLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(AgentActivity.this, R.style.MyDialogTheme);
// Create TextView
                final TextView input = new TextView (AgentActivity.this);
                input.setScroller(new Scroller(AgentActivity.this));
                input.setVerticalScrollBarEnabled(true);
                input.setMovementMethod(new ScrollingMovementMethod());
                input.setPadding(10,10,10,10);
                alert.setView(input);
                input.setText(R.string.freeuseralert);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // Do something with value!
                        dialog.dismiss();

                    }
                });

                alert.show();
            }
        });


    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        logo = (ImageView) findViewById(R.id.toolbarLogo);
        textView = (TextView) findViewById(R.id.toolbarText);
        textView2 = (TextView) findViewById(R.id.textView);
        important = (TextView) findViewById(R.id.important);
        clickLink = (TextView) findViewById(R.id.clickLink);
        button = (Button) findViewById(R.id.btnNext);
        spinner = (Spinner) findViewById(R.id.spineer);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);


        Id = getIntent().getStringExtra("PropertyId");
        type = getIntent().getStringExtra("type");
        UserPackage = getIntent().getExtras().getBoolean("boolean");

        //Toast.makeText(this, "" + UserPackage, Toast.LENGTH_SHORT).show();
        manager = getSupportFragmentManager();
    }


    // set data into spinner
    private void AddSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Double> abc = new ArrayList<>();
        SharedToken sharedToken = new SharedToken(this);
        sharedToken.getCatId();
        double a = 1.5;
        double b = 20;
        if (sharedToken.getCatId().equals("6"))
        {
            a = 4.5;
            b = 25;
        }

        while (a < b) {
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
        spinner.setSelection(0);
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
                        //Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        double Minprice;
                        double Maxprice;

                        if (sharedRate.getShared()!=null && response.body().getData().getFixedStart()!=null && sharedRate.getShared()!=null && response.body().getData().getFixedEnd()!=null) {
                            Minprice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getFixedStart());
                            Maxprice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getFixedEnd());
                        }
                        else
                        {
                            Minprice=0;
                            Maxprice=0;
                        }

                        Intent intent = new Intent(AgentActivity.this, MandateActivity.class);
                        intent.putExtra("PropertyId", response.body().getData().getItemId().toString());
                        intent.putExtra("min", String.valueOf(Minprice));
                        intent.putExtra("max", String.valueOf(Maxprice));
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

    private void bulkFreeMandate() {
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

                        Intent intent = new Intent(AgentActivity.this, Mandate2Activity.class);
                        intent.putExtra("PropertyId", response.body().getData().getId().toString());
                        intent.putExtra("responce", "https://app.m8s.world/public/upload/items/mandate/" + response.body().getData().getMandatePdf());
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
        SharedToken sharedToken = new SharedToken(this);
        sharedToken.getCatId();
        if (sharedToken.getCatId().equals("6"))
        {
            freeOffer = offer;
        }
        Call<FreeGentApi> call = apiInterface.freeMandate(HomeActivity.userId,"0", Id, freeOffer, date + " 00:00:00");
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(AgentActivity.this, "please wait...");
        dialog.show();
        call.enqueue(new Callback<FreeGentApi>() {
            @Override
            public void onResponse(Call<FreeGentApi> call, Response<FreeGentApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        //Toast.makeText(AgentActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AgentActivity.this, Mandate2Activity.class);
                        intent.putExtra("PropertyId", response.body().getData().getItemId().toString());
                        intent.putExtra("responce", "https://app.m8s.world/public/upload/items/mandate/" + response.body().getData().getMandatePdf());
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
