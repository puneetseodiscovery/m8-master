package com.m8.m8.Activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.m8.controller.Controller;
import com.m8.m8.Adapter.StartAdapter;
import com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment;
import com.m8.m8.Fragments.WatchHowToFragment;
import com.m8.m8.Models.LegalIsPresentOrNot;
import com.m8.m8.RetrofitModel.GetPotentialEarn;
import com.m8.m8.SplashScreen;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.UploadActivity.UploadActivity;
import com.m8.m8.Fragments.DetailsFragment;
import com.m8.m8.Fragments.HomeFragment;
import com.m8.m8.Fragments.MyPotentialFragment;
import com.m8.m8.Fragments.ReferFragment;
import com.m8.m8.R;
import com.m8.m8.ApiInterface;
import com.m8.m8.Fragments.CommissionFragment;
import com.m8.m8.Fragments.CommissionSubFragment.DescrptedFragment;
import com.m8.m8.Fragments.DrawerNavigation.ArtStatementFragment;
import com.m8.m8.Fragments.DrawerNavigation.MyPropertyFragment;
import com.m8.m8.Fragments.DrawerNavigation.PrivacyPolicyFragment;
import com.m8.m8.Fragments.DrawerNavigation.Terms_ConditionFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.EditBankFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.ViewAllMandateFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.ViewProfileFragment;
import com.m8.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.m8.m8.Fragments.ViewAllFragment;
import com.m8.m8.RetrofitModel.TermsConditionApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;
import com.m8.m8.util.UtileDilog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements Controller.GetPropertyNumber {

    public static String userId = "";
    public static String categoryId = "";
    public static int propertyNumber;
    public static int cunter;
    public static TermsConditionApi.Data data;
    public static BottomNavigationView bottomNavigationView;
    public static DrawerLayout drawerLayout;
    public String earning;

    NavigationView navigationView;
    FragmentManager fragmentManager;
    ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    ImageView imageView;
    Controller controller;


    boolean doubleBackToExitPressedOnce = false;

    com.google.android.gms.ads.AdView mAdView;


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

        controller = new Controller(this);


        fragmentManager = getSupportFragmentManager();


        SharedToken sharedToken = new SharedToken(HomeActivity.this);

        userId = sharedToken.getUserId();
        categoryId = sharedToken.getCatId();
        if (!StartAdapter.fromStart)
        getShareDataNew();
        StartAdapter.fromStart = true;


        init();

        if (CheckInternet.isInternetAvailable(HomeActivity.this)) {
            controller.setGetPropertyNumber(userId);
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

                            //String url = deepLink.toString();
                            String url = method(deepLink.toString());
                            Log.d("+++++", "++ url deep ++" + url.charAt(12));
                            Log.d("+++++", "++ url deep ++" + url);
                            //Toast.makeText(HomeActivity.this, ""+url.charAt(13), Toast.LENGTH_SHORT).show();
                            if (url.charAt(12) == 'm') {
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
                            } else {

                                if (url.charAt(11) == 'm') {
                                    String[] separated = url.split("=");
                                    String[] separateditem = separated[1].split("&");
                                    DetailsFragment descrptedFragment = new DetailsFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("deeplink", url.toString());
                                    bundle.putString("ItemId", separateditem[0]);
                                    descrptedFragment.setArguments(bundle);
                                    Log.d("+++++", "++ url deep ++" + separateditem[0]);
                                    Log.d("+++++", "++ url deep ++" + separated[2]);
                                    SharedToken sharedToken = new SharedToken(HomeActivity.this);
                                    sharedToken.setCatid(separated[2]);
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.framelayout, descrptedFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                                else {

                                    SplashScreen.code = deepLink.toString().substring(deepLink.toString().lastIndexOf('/') + 1);
                                    Intent intent = new Intent(HomeActivity.this, StartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
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

        getEarning();

    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '~') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void init() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView = (NavigationView) findViewById(R.id.Drawer_navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        imageView = (ImageView) findViewById(R.id.tooolbarImage);

        String abc = getIntent().getStringExtra("ItemId");
        if (!TextUtils.isEmpty(abc) && abc.equals("ItemId")){

            Business2Fragment detailsFragment = new Business2Fragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.framelayout, detailsFragment);
            transaction.commit();

        }
        else if (!TextUtils.isEmpty(abc)) {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ItemId", abc);
            detailsFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.framelayout, detailsFragment);
            transaction.commit();
        }else {
            fragmentManager.beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();
        }
    }

    private void BottomNavigationClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigationHome:
                        Intent intent1 = new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent1);
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.replace(R.id.framelayout, new HomeFragment());
//                        transaction.commit();
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

        // set new title to the MenuIte
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
                    //bottomNavigationView.setSelectedItemId(R.id.navigationHome);
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    startActivity(intent);

                }

                if (id == R.id.Watch) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzRRx1SBFGCVFBsDszTddWw/videos"));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setPackage("com.google.android.youtube");
//                    startActivity(intent);
//                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayout, new WatchHowToFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

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
                SharedToken sharedToken = new SharedToken(HomeActivity.this);
                sharedToken.clearShaerd();
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                        edit().clear().apply();
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
           Intent intent = new Intent(this,StartActivity.class);
           startActivity(intent);
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click back twice to exit.", Toast.LENGTH_LONG).show();
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 1000);
        } else {

            super.onBackPressed();
        }


    }


    @Override
    public void onSucessnumber(Response<GetShareNumberApi> response) {
        if (response.body().getStatus() == 200) {
            propertyNumber = response.body().getData();
        } else {
            Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckInternet.isInternetAvailable(HomeActivity.this)) {
            controller.setGetPropertyNumber(userId);
        } else {
            startActivity(new Intent(HomeActivity.this, NoInternetActivity.class));
        }
    }

    //get the earing price
    private void getEarning() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPotentialEarn> call = apiInterface.getPotentialEarn(userId, categoryId);

        call.enqueue(new Callback<GetPotentialEarn>() {
            @Override
            public void onResponse(Call<GetPotentialEarn> call, Response<GetPotentialEarn> response) {
                if (response.isSuccessful()) {
                    earning = response.body().getData().getCurrency() + " " + response.body().getData().getTotal();
                    // get menu from navigationView
                    Menu menu = navigationView.getMenu();
                    // find MenuItem you want to change
                    MenuItem nav_camara = menu.findItem(R.id.erning);

                    // set new title to the MenuItem
                    nav_camara.setTitle(Html.fromHtml(""+"<font><b>" + earning + "</b></font>"+""));

                    SpannableString spanString = new SpannableString(nav_camara.getTitle().toString());
                    spanString.setSpan(new ForegroundColorSpan(Color.RED), 0,     spanString.length(), 0); //fix the color to white

                    nav_camara.setTitle(spanString);

                }
            }

            @Override
            public void onFailure(Call<GetPotentialEarn> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getShareDataNew() {

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<LegalIsPresentOrNot> call = apiInterface.checklegalrepApi(HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<LegalIsPresentOrNot>() {
            @Override
            public void onResponse(Call<LegalIsPresentOrNot> call, Response<LegalIsPresentOrNot> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getData().getLegalAddress())
                    {

                    }
                    else
                    {
                        final Dialog dialog = new Dialog(HomeActivity.this);

                        dialog.setContentView(R.layout.custom_dialog_upload);
                        dialog.setCancelable(false);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        dialog.show();

                        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                        text.setText("Buying or selling property involves a legal process, we need the details of your legal representative for our buyers legal representatives to transact the purchase. This final step is mandatory.");

                        btnBuy.setText("Ok");

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
                                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                                intent.putExtra("ItemId", "ItemId");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }

                } else {
                    Toast.makeText(HomeActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LegalIsPresentOrNot> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

}
