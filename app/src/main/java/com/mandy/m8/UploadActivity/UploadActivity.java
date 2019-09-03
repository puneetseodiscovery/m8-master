package com.mandy.m8.UploadActivity;

import android.app.Dialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.UploadActivity.UploadSubFragment.UploadFragment;
import com.mandy.m8.UploadActivity.UploadSubFragment.BulkUploadFragment;
import com.mandy.m8.R;
import com.mandy.m8.util.UtileDilog;

public class UploadActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView textView;
    ImageView drawer, toolbarLogo;
    RadioGroup radioGroup;
    FragmentManager manager;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        init();

        setSupportActionBar(toolbar);
        drawer.setVisibility(View.GONE);
        textView.setText("Upload a item");
        toolbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        radioGroup.check(R.id.Radio_resident);
        transaction.replace(R.id.framelay, new UploadFragment());
        transaction.commit();

        //radio button click listner
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Radio_resident:
                        FragmentTransaction transaction2 = manager.beginTransaction();
                        transaction2.replace(R.id.framelay, new UploadFragment());
                        transaction2.commit();
                        break;
                    case R.id.Radio_commercial:
                        if (HomeActivity.propertyNumber == 1) {
                            Dialog dialog = UtileDilog.dialog(UploadActivity.this, getResources().getString(R.string.doyou), "Buy", manager);
                            dialog.show();
                            radioGroup.check(R.id.Radio_resident);
                            FragmentTransaction transaction3 = manager.beginTransaction();
                            transaction3.replace(R.id.framelay, new UploadFragment());
                            transaction3.commit();
                        } else {
                            FragmentTransaction transaction1 = manager.beginTransaction();
                            transaction1.replace(R.id.framelay, new BulkUploadFragment());
                            transaction1.commit();
                        }
                        break;
                }
            }
        });


    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        toolbarLogo = (ImageView) findViewById(R.id.toolbarLogo);
        textView = (TextView) findViewById(R.id.toolbarText);
        radioGroup = (RadioGroup) findViewById(R.id.Radio_group);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
