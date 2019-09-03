package com.mandy.m8.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.mandy.m8.RetrofitModel.GetPotentialEarn;
import com.mandy.m8.SplashScreen;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.RetrofitModel.GetShareNumberApi;
import com.mandy.m8.UploadActivity.UploadActivity;
import com.mandy.m8.Fragments.DetailsFragment;
import com.mandy.m8.Fragments.HomeFragment;
import com.mandy.m8.Fragments.MyPotentialFragment;
import com.mandy.m8.Fragments.ReferFragment;
import com.mandy.m8.R;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.Fragments.CommissionFragment;
import com.mandy.m8.Fragments.CommissionSubFragment.DescrptedFragment;
import com.mandy.m8.Fragments.DrawerNavigation.ArtStatementFragment;
import com.mandy.m8.Fragments.DrawerNavigation.MyPropertyFragment;
import com.mandy.m8.Fragments.DrawerNavigation.PrivacyPolicyFragment;
import com.mandy.m8.Fragments.DrawerNavigation.Terms_ConditionFragment;
import com.mandy.m8.Fragments.MyAccountSubFragment.EditBankFragment;
import com.mandy.m8.Fragments.MyAccountSubFragment.ViewAllMandateFragment;
import com.mandy.m8.Fragments.MyAccountSubFragment.ViewProfileFragment;
import com.mandy.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.mandy.m8.Fragments.ViewAllFragment;
import com.mandy.m8.RetrofitModel.TermsConditionApi;
import com.mandy.m8.ServiceGenerator;
import com.mandy.m8.util.SharedRate;
import com.mandy.m8.util.UtileDilog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {

    public static String userId = "";
    public static String categoryId = "";
    public static int propertyNumber;
    public static int cunter;
    public static TermsConditionApi.Data data;
    public static BottomNavigationView bottomNavigationView;
    public static DrawerLayout drawerLayout;
    public static String earning;

    NavigationView navigationView;
    FragmentManager fragmentManager;
    ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    com.google.android.gms.ads.AdView mAdView;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // TODO : Google ad code for future use -->

//        ca-app-pub-3940256099942544~3347511713 - dummy
//        ca-app-pub-3864021669352159~4680319766 - benzy

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


        fragmentManager = getSupportFragmentManager();


        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

        userId = sharedPreferences.getString("Token", "");
        categoryId = sharedPreferences.getString("Category", "");

        editor = sharedPreferences.edit();

        init();

        if (CheckInternet.isInternetAvailable(HomeActivity.this)) {
            getPropertyNumber();
        } else {
            startActivity(new Intent(HomeActivity.this, NoInternetActivity.class));
        }


        setSupportActionBar(toolbar);


        //Bottomvnavigation item click listner
        BottomNavigationClick();


        onCLick();


        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(HomeActivity.this, DividerItemDecoration.VERTICAL));
        //deep link

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String url=deepLink.toString();
                            Log.d("+++++","++ url deep ++"+url);

                            if (url.charAt(7)=='m') {
                                if (url != null) {
                                    DescrptedFragment descrptedFragment = new DescrptedFragment();
                                    Bundle bundle = new Bundle();
                                    descrptedFragment.setArguments(bundle);
                                    bundle.putString("deeplink", url.toString());
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.framelayout, descrptedFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }
                            else
                            {


                                SplashScreen.code = deepLink.toString().substring(deepLink.toString().lastIndexOf('/') + 1);
                                Intent intent = new Intent(HomeActivity.this,StartActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            //Log.d("deeplink", "" + code);
                        }
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("abc", "getDynamicLink:onFailure", e);
                    }
                });




        getData();

    }

    private void init() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView = (NavigationView) findViewById(R.id.Drawer_navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        imageView = (ImageView) findViewById(R.id.tooolbarImage);

        String abc = getIntent().getStringExtra("ItemId");
        if (!TextUtils.isEmpty(abc)) {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ItemId", abc);
            detailsFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.framelayout, detailsFragment);
            transaction.commit();
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.framelayout, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void BottomNavigationClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigationHome:
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.framelayout, new HomeFragment());
                        transaction.commit();
                        return true;

                    case R.id.navigationCommission:
                        FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                        transaction1.replace(R.id.framelayout, new CommissionFragment());
                        transaction1.commit();
                        return true;


                    case R.id.navigationMyAccount:
                        FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.framelayout, new ViewProfileFragment());
                        transaction2.commit();
                        return true;

                    case R.id.navigationUpload:
                        if (propertyNumber == 0) {
                            Dialog dialog = UtileDilog.dialog(HomeActivity.this, getResources().getString(R.string.doyou), "Buy", fragmentManager);
                            dialog.show();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                            startActivity(intent);
                        }
                        return true;

                    case R.id.navigationView:
                        FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                        transaction3.replace(R.id.framelayout, new ViewAllFragment());
                        transaction3.commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onCLick() {
        Menu menu = navigationView.getMenu();

        MenuItem potentialEarning = menu.findItem(R.id.erning);
        // set new title to the MenuItem
        potentialEarning.setTitle(HomeActivity.earning);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.startTrade) {
                    startActivity(new Intent(getApplicationContext(), StartActivity.class));
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    finish();
                }

                if (id == R.id.potential) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new MyPotentialFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                if (id == R.id.Home) {
                    bottomNavigationView.setSelectedItemId(R.id.navigationHome);
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }

                if (id == R.id.Watch) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzRRx1SBFGCVFBsDszTddWw/videos"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.youtube");
                    startActivity(intent);
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }


                if (id == R.id.Refer) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new ReferFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }


                if (id == R.id.myBank) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new EditBankFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                if (id == R.id.Buy) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new SharePackageFragment());
                    transaction.commit();
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }
                if (id == R.id.Myproperty) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new MyPropertyFragment());
                    transaction.commit();

                }
                if (id == R.id.Mycontact) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new ViewAllMandateFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                if (id == R.id.Terms) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new Terms_ConditionFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                if (id == R.id.Privacy) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new PrivacyPolicyFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                if (id == R.id.Art) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new ArtStatementFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                if (id == R.id.Goto) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.m8s.world"));
                    startActivity(browserIntent);
                }
                if (id == R.id.Logout) {

                    getDilog();
                }

                return false;
            }
        });
    }

    //dilog of logout
    private void getDilog() {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.custom_dialog_upload);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
        text.setText(getResources().getString(R.string.logoutmessage));

        btnBuy.setText("Logout");


        dialog.show();

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
                editor.clear();
                editor.commit();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }


    // get the terms and conditions
    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TermsConditionApi> call = apiInterface.getTermsCondition();
        call.enqueue(new Callback<TermsConditionApi>() {
            @Override
            public void onResponse(Call<TermsConditionApi> call, Response<TermsConditionApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        data = response.body().getData();
                    } else {
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<TermsConditionApi> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        } else {

            super.onBackPressed();
        }


    }


    //get property sharer number
    private void getPropertyNumber() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetShareNumberApi> call = apiInterface.getPropertyNumber(HomeActivity.userId);
        call.enqueue(new Callback<GetShareNumberApi>() {
            @Override
            public void onResponse(Call<GetShareNumberApi> call, Response<GetShareNumberApi> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals(200)) {
                        propertyNumber = response.body().getData();
                    } else {
                        Toast.makeText(HomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetShareNumberApi> call, Throwable t) {

            }
        });
    }


    //get the earing price
    private void getEarning() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPotentialEarn> call = apiInterface.getPotentialEarn(HomeActivity.userId, HomeActivity.categoryId);

        call.enqueue(new Callback<GetPotentialEarn>() {
            @Override
            public void onResponse(Call<GetPotentialEarn> call, Response<GetPotentialEarn> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals(200)) {
                        String ad = response.body().getData().getTotal().replaceAll(",", "");
                        double price = Double.parseDouble(ad);
                        SharedRate sharedRate = new SharedRate(HomeActivity.this);
                        price = price * Double.parseDouble(sharedRate.getShared());

                        earning = sharedRate.getCurrencyCode() + " " + String.format("%.2f", price);


                    } else {
                        Toast.makeText(HomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPotentialEarn> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}