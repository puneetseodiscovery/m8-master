package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.R;

import im.delight.android.webview.AdvancedWebView;

public class WatchHowToUseAtStart extends AppCompatActivity implements AdvancedWebView.Listener {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtTerms;
    ImageView imageView;
    View view;
    Context context;
    String watch="";
    //WebView webview;
    private AdvancedWebView webview;
    public com.google.android.gms.ads.AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_how_to_use_at_start);

        init();

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


        textView.setText("M8 How Does It Work?");
        imageView.setVisibility(View.VISIBLE);
//        webview.setWebViewClient(new WebViewClient());
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        webview.setWebChromeClient(new WebChromeClient());
//        if (Build.VERSION.SDK_INT < 8) {
//            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        } else {
//            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        }
//        //webview.loadUrl("https://app.m8s.world//html/index.html");
//        webview.loadUrl("https://www.youtube.com/");
        webview.setListener(this,this);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                AdvancedWebView newWebView = new AdvancedWebView(WatchHowToUseAtStart.this);
                // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                return true;
            }
        });
        webview.loadUrl("https://app.m8s.world//html/index.html");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);

    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

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

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        textView = (TextView) findViewById(R.id.toolbarText);
        imageView = (ImageView) findViewById(R.id.toolbarLogo);
        //txtTerms = (TextView) view.findViewById(R.id.textView);
        webview = (AdvancedWebView) findViewById(R.id.webview);
        addAds();
    }
}
