package com.m8.m8.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.m8.m8.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class WatchVideoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView drawer;
    WebView webview;
    VideoView videoView1;
    private ProgressDialog progDailog;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        webview = findViewById(R.id.videoView);
        videoView1 = findViewById(R.id.videoView1);

        drawer.setVisibility(View.INVISIBLE);
        AppCompatActivity activity = this;
        activity.setSupportActionBar(toolbar);
        // Set title bar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final String videoUrl = getIntent().getStringExtra("videoUrl");
        String[] arrayList1 = videoUrl.split("=");
        if (arrayList1.length>1) {
            final String url = arrayList1[1];
//                if(!url.startsWith("www.")&& !url.startsWith("http://")){
//                    url = "www."+url;
//                }
//                if(!url.startsWith("http://")){
//                    url = "http://"+url;
//                }

            YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = url;
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
        else
        {
            Toast.makeText(activity, "Invalid youtube video link.", Toast.LENGTH_SHORT).show();
        }
////        videoView1.setVideoPath("https://www.youtube.com/watch?v=mBCE7oN3yd8");
////        videoView1.start();
////        webview.getSettings().setJavaScriptEnabled(true);
////        webview.getSettings().setLoadWithOverviewMode(true);
////        webview.getSettings().setUseWideViewPort(true);
////        webview.getSettings().setBuiltInZoomControls(true);
////        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
////        // For API level below 18 (This method was deprecated in API level 18)
////        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
////        if (Build.VERSION.SDK_INT >= 19) {
////            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
////        }
////        else {
////            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
////        }
////
////        webview.setWebChromeClient(new WebChromeClient());
////        webview.loadUrl("https://www.youtube.com/watch?v=mBCE7oN3yd8");
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
//        final String mimeType = "text/html";
//        final String encoding = "UTF-8";
//        String html = getHTML();
//        webview.setWebChromeClient(new WebChromeClient() {
//        });
//        webview.loadDataWithBaseURL("", html, mimeType, encoding, "");
//
//    }
//
//    public String getHTML() {
//        String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\""+url
//                + "J2fB5XWj6IE"
//                + "?fs=0\" frameborder=\"0\">\n"
//                + "</iframe>\n";
//        return html;
   }
}
