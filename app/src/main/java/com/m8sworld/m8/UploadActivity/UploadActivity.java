package com.m8sworld.m8.UploadActivity;

import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.m8sworld.m8.UploadActivity.UploadSubFragment.UploadFragment;
import com.m8sworld.m8.UploadActivity.UploadSubFragment.BulkUploadFragment;
import com.m8sworld.m8.R;
import com.m8sworld.m8.util.SharedToken;

public class UploadActivity extends AppCompatActivity {


    public static Toolbar toolbar;
    TextView textView;
    ImageView drawer, toolbarLogo;
    RadioGroup radioGroup;
    FragmentManager manager;
    FragmentTransaction transaction;
    RadioButton Radio_resident;
    public static LinearLayout linaer;
    FrameLayout framelay;
    public static int upToShare=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        init();

        toolbar.setVisibility(View.VISIBLE);
        linaer.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        drawer.setVisibility(View.GONE);
        textView.setText("Upload an item");


        toolbarLogo.setVisibility(View.INVISIBLE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        radioGroup.check(R.id.Radio_resident);
        SharedToken sharedToken = new SharedToken(this);
        sharedToken.getCatId();
        Radio_resident.setText("Single upload");
        if (sharedToken.getCatId().equals("2"))
        {
            Radio_resident.setText("Upload single vacancy");
        }
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
//                            Dialog dialog = UtileDilog.dialog(UploadActivity.this, getResources().getString(R.string.doyou), "Buy", manager);
//                            dialog.show();
                            final Dialog dialog = new Dialog(UploadActivity.this);

                            dialog.setContentView(R.layout.custom_dialog_upload);
                            dialog.setCancelable(false);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            dialog.show();
                            radioGroup.check(R.id.Radio_resident);
                            FragmentTransaction transaction3 = manager.beginTransaction();
                            transaction3.replace(R.id.framelay, new UploadFragment());
                            transaction3.commit();


                            final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                            Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                            TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                            text.setText(getResources().getString(R.string.doyou));

                            btnBuy.setText("Buy");

                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            btnBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    SharePackageFragment sharePackageFragment = new SharePackageFragment();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.framelay, sharePackageFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                    upToShare = 1;
                                }
                            });

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

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setVisibility(View.VISIBLE);
        linaer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        toolbarLogo = (ImageView) findViewById(R.id.toolbarLogo);
        textView = (TextView) findViewById(R.id.toolbarText);
        radioGroup = (RadioGroup) findViewById(R.id.Radio_group);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Radio_resident = findViewById(R.id.Radio_resident);
        linaer = findViewById(R.id.linaer);
        framelay = findViewById(R.id.framelay);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // handle ⬅️ button here
                super.onBackPressed();
                break;
        }
        return true;
    }
}
