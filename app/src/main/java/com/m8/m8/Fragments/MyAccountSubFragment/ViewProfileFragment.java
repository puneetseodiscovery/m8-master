package com.m8.m8.Fragments.MyAccountSubFragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.LoginActivity;
import com.m8.m8.Activities.MyM8sEarningActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.Fragments.DrawerNavigation.MyPropertyFragment;
import com.m8.m8.Fragments.MyAccountFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment;
import com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment2;
import com.m8.m8.Fragments.MyPotentialFragment;
import com.m8.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.m8.m8.Fragments.ViewingCommissionFragment;
import com.m8.m8.Models.PayOutstandingModel;
import com.m8.m8.Models.TokenResponseModel;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.AccountDetailsApi;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.ProfileImageApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.util.MultiPart;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE = 121;
    public static int REQUEST_CAMERA = 122;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;
    Context context;
    public static int fromProfileToSale = 0;

    Toolbar toolbar;
    ImageView imageLogo, drawer;
    View view;
    TextView editBank, editProfile;
    TextView txtAgent, txtName, txtPhone, txtEmail, txtAddress;
    RoundedImageView profileImage;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    MultipartBody.Part part;
    Bitmap bitmap;
    TextView txtAccount, txtAccountStatus, txtProperty, txtComission, txtView, txtSale, txtMandate, txtEarning, txtRefer;

    LinearLayout linearMyContract, linearItemslist, linearCommission, linearSales, linearBank,linearViewing,myEarningTillDate,paidOut;
    FragmentManager manager;
    public static GetMetaData.Data data;

    SharedRate sharedRate;
    SharedToken sharedToken;
    String userId, categoryId;

    TextView txtBName, txtBEmail, txtBPhone, txtBAdress;
    LinearLayout linearbusiness;

    Button payOutstanding;
    String balanceMoney;

    public com.google.android.gms.ads.AdView mAdView;
    public ViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        sharedToken = new SharedToken(context);
        userId = sharedToken.getUserId();
        categoryId = sharedToken.getCatId();

        SharedToken sharedToken = new SharedToken(context);
        final String token = sharedToken.getTokenId();
        if (token.length()>0)
        {
            getTokenData(token);
        }

        init();

        sharedRate = new SharedRate(context);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        imageLogo.setVisibility(View.VISIBLE);

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyAccountFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialg();
            }
        });

        if (CheckInternet.isInternetAvailable(context)) {
            GetMetaDetails();
            GetAllData();
        } else {
            Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
        }
        linearClick();
        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        imageLogo = (ImageView) view.findViewById(R.id.toolbarLogo);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        editBank = (TextView) view.findViewById(R.id.editBank);
        editProfile = (TextView) view.findViewById(R.id.editProfile);
        profileImage = (RoundedImageView) view.findViewById(R.id.profile_image);
        txtAddress = (TextView) view.findViewById(R.id.user_address);
        txtAgent = (TextView) view.findViewById(R.id.user_detail);
        txtName = (TextView) view.findViewById(R.id.user_name);
        txtPhone = (TextView) view.findViewById(R.id.user_phone);
        txtEmail = (TextView) view.findViewById(R.id.user_email);

        txtAccount = (TextView) view.findViewById(R.id.txtAccount);
        txtAccountStatus = (TextView) view.findViewById(R.id.txtAccountStatus);
        txtProperty = (TextView) view.findViewById(R.id.txtPropertyList);
        txtComission = (TextView) view.findViewById(R.id.txtComissionTree);
        txtView = (TextView) view.findViewById(R.id.txtViewing);
        txtSale = (TextView) view.findViewById(R.id.txtSales);
        txtMandate = (TextView) view.findViewById(R.id.txtMyMandate);
        txtEarning = (TextView) view.findViewById(R.id.txtEarning);
        txtRefer = (TextView) view.findViewById(R.id.txtRefer);

        HomeActivity.cunter = 0;

        linearMyContract = (LinearLayout) view.findViewById(R.id.linearMyContract);
        linearItemslist = (LinearLayout) view.findViewById(R.id.linearItemslist);
        linearCommission = (LinearLayout) view.findViewById(R.id.linearCommission);
        linearSales = (LinearLayout) view.findViewById(R.id.linearSales);
        linearBank = (LinearLayout) view.findViewById(R.id.linearBank);
        linearViewing = (LinearLayout) view.findViewById(R.id.linearViewing);
        myEarningTillDate = (LinearLayout) view.findViewById(R.id.myEarningTillDate);
        paidOut = (LinearLayout) view.findViewById(R.id.paidOut);

        txtBAdress = (TextView) view.findViewById(R.id.txtBusinessAddress);
        txtBName = (TextView) view.findViewById(R.id.txtBusinessName);
        txtBEmail = (TextView) view.findViewById(R.id.txtBusinessEmail);
        txtBPhone = (TextView) view.findViewById(R.id.txtBusinessPhone);
        linearbusiness = (LinearLayout) view.findViewById(R.id.linearBusiness);

        manager = getActivity().getSupportFragmentManager();

        txtBAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new Business2Fragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtBEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new Business2Fragment2());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        payOutstanding = view.findViewById(R.id.payOutstanding);

        payOutstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (balanceMoney.equals("CHF  0.00"))
                {
                    Toast.makeText(context, "No amount to pay.", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendPayOutstandings();
                }
            }
        });

        addAds();
    }

    //    chose image from camera and gallery
    private void showDialg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Images");
        builder.setCancelable(false);
        //builder.setPositiveButton("OK", null);

        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(dialogOptions[which])) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }


                } else if ("Gallery".equals(dialogOptions[which])) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                } else if ("Cancel".equals(dialogOptions[which])) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                MultiPart multi = new MultiPart(context);
                part = multi.sendImageFileToserver(bitmap);
                if (CheckInternet.isInternetAvailable(context)) {
                    sendProfileImage();
                } else {
                    Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            try {
                MultiPart multi = new MultiPart(context);
                part = multi.sendImageFileToserver(bitmap);
                if (CheckInternet.isInternetAvailable(context)) {
                    sendProfileImage();
                } else {
                    Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload data in to the server
    public void sendProfileImage() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ProfileImageApi> call = apiInterface.profileImage(userId, part);
        final ProgressDialog progressDialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        progressDialog.show();
        call.enqueue(new Callback<ProfileImageApi>() {
            @Override
            public void onResponse(Call<ProfileImageApi> call, Response<ProfileImageApi> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetMetaDetails();
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageApi> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    //Get the user details
    public void GetMetaDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetMetaData> call = apiInterface.getMetaData(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetMetaData>() {
            @Override
            public void onResponse(Call<GetMetaData> call, Response<GetMetaData> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    txtAgent.setText("My Profile");

                    txtName.setText(response.body().getData().getName());
                    txtPhone.setText(response.body().getData().getContactNo());
                    txtEmail.setText(response.body().getData().getEmail());

                    if (response.body().getData().getProfile() != null) {
                        txtAddress.setText(response.body().getData().getProfile().getAddress());
                    }
                    if (data.getProfile().getBName() != null) {
                        //linearbusiness.setVisibility(View.VISIBLE);
                        linearbusiness.setVisibility(View.GONE);
                        txtBName.setText(data.getProfile().getBName().toString());
                        //txtBAdress.setText(data.getProfile().getBAddress().toString());
                        txtBPhone.setText(data.getProfile().getBPhone().toString());
                        //txtBEmail.setText(data.getProfile().getBEmail().toString());
                    }
                    if (response.body().getData().getProfileImage() == null) {
                        profileImage.setImageResource(R.drawable.ic_person);
                    } else {
                        Glide.with(context).load("https://app.m8s.world/public/upload/users/profiles/" + response.body().getData().getProfileImage()).dontAnimate().placeholder(R.drawable.myaccount).into(profileImage);
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetMetaData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //get all inforamtion
    private void GetAllData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AccountDetailsApi> call = apiInterface.getAccount(userId);
        call.enqueue(new Callback<AccountDetailsApi>() {
            @Override
            public void onResponse(Call<AccountDetailsApi> call, Response<AccountDetailsApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        txtAccountStatus.setText(response.body().getData().getAccountStatus() + " Agent package");
                        txtProperty.setText(response.body().getData().getPropertyListed().toString());
                        txtComission.setText(response.body().getData().getCommisionTreeInPlay().toString());
                        txtView.setText(response.body().getData().getViewingThrough().toString());
                        txtSale.setText(response.body().getData().getSalesThrough().toString());
                        txtMandate.setText(response.body().getData().getMyMandates().toString());
//                        double earningPrice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getEarningsToDate().toString());
//                        double accountPrice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getPaidOut().toString());
//                        double referPrice = Double.valueOf(sharedRate.getShared()) * Double.valueOf(response.body().getData().getReferEarn().toString());
//
//                        txtEarning.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", earningPrice));
//                        txtAccount.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", accountPrice));
//                        txtRefer.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", referPrice));
                        txtEarning.setText(response.body().getData().getCurrency() + " " + response.body().getData().getEarningsToDate().toString());
                        txtAccount.setText(response.body().getData().getCurrency() + " " + response.body().getData().getPaidOut().toString());
                        txtRefer.setText(response.body().getData().getCurrency() + " " + response.body().getData().getReferEarn().toString());
                        payOutstanding.setText("Pay outstanding balance: "+response.body().getData().getCurrency() + " " + response.body().getData().getPayOutstandingBalance());
                        balanceMoney = response.body().getData().getCurrency() + " " + response.body().getData().getPayOutstandingBalance();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsApi> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //click event on linear layout for get the more details
    private void linearClick() {
        //get all my items lists
        linearItemslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyPropertyFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyPropertyFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // go to the commission tree
        linearCommission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationCommission);
                fromProfileToSale = 2;
            }
        });

        // go to the commission tree
        txtComission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationCommission);
                fromProfileToSale = 2;
            }
        });

        // view all my mandates
        linearMyContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new ViewAllMandateFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtMandate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new ViewAllMandateFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        linearSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyPotentialFragment());
                fromProfileToSale = 1;
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyPotentialFragment());
                fromProfileToSale = 1;
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        linearBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new EditBankFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        linearViewing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new ViewingCommissionFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtAccountStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new SharePackageFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        myEarningTillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.framelayout, new MyPotentialFragment());
//                fromProfileToSale = 1;
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent intent = new Intent(context, MyM8sEarningActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        paidOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, new MyPotentialFragment());
                fromProfileToSale = 1;
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    //upload data in to the server
    public void sendPayOutstandings() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PayOutstandingModel> call = apiInterface.payoutstandings(userId);
        final ProgressDialog progressDialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        progressDialog.show();
        call.enqueue(new Callback<PayOutstandingModel>() {
            @Override
            public void onResponse(Call<PayOutstandingModel> call, Response<PayOutstandingModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getMessage().equals("Please add your PayPal address or enter your bank details."))
                    {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.framelayout, new EditBankFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (Build.VERSION.SDK_INT >= 26) {
                            ft.setReorderingAllowed(false);
                        }
                        ft.detach(ViewProfileFragment.this).attach(ViewProfileFragment.this).commit();
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PayOutstandingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void addAds()
    {
        MobileAds.initialize(getContext(), "ca-app-pub-3864021669352159~4680319766");

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
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

    private void getTokenData(String token) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TokenResponseModel> call = apiInterface.getTokenDetails("Bearer "+token,"application/json");
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(402)) {

                        SharedToken sharedToken = new SharedToken(context);
                        sharedToken.clearShaerd();
                        PreferenceManager.getDefaultSharedPreferences(context).
                                edit().clear().apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        //Toast.makeText(StartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
