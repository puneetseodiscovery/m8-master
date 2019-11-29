package com.m8.m8.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m8.m8.R;


public class CommingActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comming);

        init();

        textView.setText("Coming Soon");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommingActivity.this.onBackPressed();
            }
        });

        drawer.setVisibility(View.INVISIBLE);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        textView = (TextView) findViewById(R.id.toolbarText);
    }

}
