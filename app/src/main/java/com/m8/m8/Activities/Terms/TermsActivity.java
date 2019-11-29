package com.m8.m8.Activities.Terms;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m8.m8.Activities.LoginActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.TermsConditionApi;
import com.m8.m8.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView drawer;
    TextView toolbarTxt, txtView;
    String terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        init();
        drawer.setVisibility(View.INVISIBLE);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsActivity.this.onBackPressed();
            }
        });

        terms = getIntent().getStringExtra("terms");


        toolbarTxt.setText(terms);

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        toolbarTxt = (TextView) findViewById(R.id.toolbarText);
        txtView = (TextView) findViewById(R.id.txtTerms);
        getData();
    }
    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TermsConditionApi> call = apiInterface.getTermsCondition();
        call.enqueue(new Callback<TermsConditionApi>() {
            @Override
            public void onResponse(Call<TermsConditionApi> call, Response<TermsConditionApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        LoginActivity.data = response.body().getData();
                        if (terms.equals("Terms & Condition")) {
                       txtView.setText(Html.fromHtml(LoginActivity.data.getTermAndCondition()));
                        }

                        if (terms.equals("Art & Statement")) {
                   txtView.setText(Html.fromHtml(LoginActivity.data.getArtStatement()));
                        }
                        if (terms.equals("Privacy policy")) {
                            txtView.setText(Html.fromHtml(LoginActivity.data.getPrivacyPolicy()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TermsConditionApi> call, Throwable t) {
                Toast.makeText(TermsActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
