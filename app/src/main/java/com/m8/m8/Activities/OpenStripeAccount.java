package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.m8.m8.R;
import com.m8.m8.util.SharedToken;

public class OpenStripeAccount extends AppCompatActivity {

    WebView webview;
    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtTerms;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_stripe_account);

        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        textView = (TextView) findViewById(R.id.toolbarText);
        imageView = (ImageView) findViewById(R.id.toolbarLogo);

        AppCompatActivity activity = (AppCompatActivity) this;
        activity.setSupportActionBar(toolbar);

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


        textView.setText("Connect to stripe.");
        imageView.setVisibility(View.VISIBLE);

        SharedToken sharedToken = new SharedToken(OpenStripeAccount.this);
        String userId = sharedToken.getUserId();

        String urlWithId = "https://www.stripe.com/";

        final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait, loading...", true);

        //webview = (AdvancedWebView) findViewById(R.id.webview);
        webview = findViewById(R.id.webview);
        //webview.setListener(Main2Activity.this,this);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);

        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
        });
        Log.d("+++++++++","++ urlWithId ++"+urlWithId);
        webview.loadUrl(urlWithId);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
    }
}
