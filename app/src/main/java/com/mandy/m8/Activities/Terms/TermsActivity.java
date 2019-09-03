package com.mandy.m8.Activities.Terms;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandy.m8.R;
import com.mandy.m8.SplashScreen;

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
        drawer.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsActivity.this.onBackPressed();
            }
        });

        terms = getIntent().getStringExtra("terms");


        toolbarTxt.setText(terms);

        if (terms.equals("Terms & Condition")) {
            txtView.setText(Html.fromHtml(SplashScreen.data.getTermAndCondition()));
        }

        if (terms.equals("Art & Statement")) {
            txtView.setText(Html.fromHtml(SplashScreen.data.getArtStatement()));
        }
        if (terms.equals("Privacy policy")) {
            txtView.setText(Html.fromHtml(SplashScreen.data.getPrivacyPolicy()));
        }

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        toolbarTxt = (TextView) findViewById(R.id.toolbarText);
        txtView = (TextView) findViewById(R.id.txtTerms);
    }
}
