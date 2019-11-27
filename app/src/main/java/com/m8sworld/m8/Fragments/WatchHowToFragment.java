package com.m8sworld.m8.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.R;

import im.delight.android.webview.AdvancedWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchHowToFragment extends Fragment implements AdvancedWebView.Listener {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtTerms;
    ImageView imageView;
    View view;
    Context context;
    String watch="";
    //WebView webview;
    private AdvancedWebView webview;

    public WatchHowToFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_watchhowto, container, false);

        init();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


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
        webview.setListener(getActivity(),this);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                AdvancedWebView newWebView = new AdvancedWebView(getContext());
                // myParentLayout.addView(newWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                return true;
            }
        });
        webview.loadUrl("https://app.m8s.world//html/index.html");

        return view;
    }


    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        imageView = (ImageView) view.findViewById(R.id.toolbarLogo);
        //txtTerms = (TextView) view.findViewById(R.id.textView);
        webview = (AdvancedWebView) view.findViewById(R.id.webview);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
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
}
