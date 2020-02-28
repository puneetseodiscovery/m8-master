package com.m8.m8.Fragments.CommissionSubFragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.NoInternetActivity;
import com.m8.m8.Activities.WatchVideoActivity;
import com.m8.m8.Adapter.CheckAdapter;
import com.m8.m8.Adapter.ImageAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.SplashScreen;
import com.m8.m8.SpripePayment;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.Fragments.HomeFragment;
import com.m8.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.AccpectedApi;
import com.m8.m8.RetrofitModel.BuyApi;
import com.m8.m8.RetrofitModel.ContactSellerApi;
import com.m8.m8.RetrofitModel.DescriptedApi;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.RetrofitModel.ItemAllDeatilsApi;
import com.m8.m8.RetrofitModel.ShareLinkApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;
import com.m8.m8.util.UtileDilog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescrptedFragment extends Fragment {

    Toolbar toolbar;
    ImageView drawer;
    ViewPager itemImage;

    TextView textView, textBuyShare, txtDescripation, txtHeading, txtPrice, txtComission, txtFeature, txtFeature2,featureText,esigalnih,buyButtonText,watchVideo;
    View view;
    FragmentTransaction transaction;
    FragmentManager manager;
    LinearLayout linearShare, linearContact, linearBuy;
    String Id = "";
    PagerAdapter adapter;
    ArrayList<String> array = new ArrayList<>();
    ImageView imageNext, imagePer;
    RecyclerView recyclerView;

    Context context;

    String txtBuyShare, txtnumber = "0", CatId;
    String shareId, deeplink;
    ProgressDialog progressDiog;
    SharedRate sharedRate;
    SharedToken sharedToken;
    String userId, categoryId;
    String UserName,commission;
    TextView itemStatus;
    public com.google.android.gms.ads.AdView mAdView;
    String catid;
    RelativeLayout buybuttonrelative;
    String videoUrl;


    public DescrptedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_descrpted, container, false);
        sharedToken = new SharedToken(context);


        //        ca-app-pub-3940256099942544~3347511713 - dummy
//        ca-app-pub-3864021669352159~4680319766 - benzy

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

        init();

        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.replace(R.id.framelayout, new HomeFragment());
                transaction1.commit();

            }
        });

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        textView.setText(R.string.sharethis);


        textBuyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.framelayout, new SharePackageFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtnumber.equals("0")) {
                    if (CheckInternet.isInternetAvailable(context)) {
                        GetShareLink();
                    } else {
                        Toast.makeText(context, "" + view.getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Dialog dialog = UtileDilog.dialog(context, context.getResources().getString(R.string.doyou), "Buy", manager);
                    dialog.show();
                }
            }
        });


        linearBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(context)) {
                    if (catid.equals("2"))
                    {
                        buyItem();
                    }
                    else if (catid.equals("6"))
                    {
                        buyItem();
                    }
                    else if (catid.equals("1"))
                    {
                        buyItem();
                    }
                    else
                    {
                        buyItemStripe();
                    }

                } else {
                    context.startActivity(new Intent(context, NoInternetActivity.class));
                }
            }
        });
        //get the all item details


        linearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact();
            }
        });
        return view;

    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        itemImage = (ViewPager) view.findViewById(R.id.imageItem);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        textBuyShare = (TextView) view.findViewById(R.id.buy_share);
        txtHeading = (TextView) view.findViewById(R.id.txtHeading);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtDescripation = (TextView) view.findViewById(R.id.txtDescripation);
        txtFeature = (TextView) view.findViewById(R.id.txtFeature);
        txtComission = (TextView) view.findViewById(R.id.txtComission);
        linearContact = (LinearLayout) view.findViewById(R.id.linearContact);
        linearShare = (LinearLayout) view.findViewById(R.id.linearShare);
        linearBuy = (LinearLayout) view.findViewById(R.id.linearBuy);
        txtFeature2 = (TextView) view.findViewById(R.id.txtFeature2);
        imageNext = (ImageView) view.findViewById(R.id.imageNext);
        imagePer = (ImageView) view.findViewById(R.id.imagePer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerCheck);
        itemStatus = (TextView) view.findViewById(R.id.itemStatus);
        featureText = (TextView) view.findViewById(R.id.featureText);
        esigalnih = (TextView) view.findViewById(R.id.esigalnih);
        buyButtonText = (TextView) view.findViewById(R.id.buyButtonText);
        watchVideo = view.findViewById(R.id.watchVideo);

        buybuttonrelative = view.findViewById(R.id.buybuttonrelative);
        buybuttonrelative.setVisibility(View.VISIBLE);

        progressDiog = ProgressBarClass.showProgressDialog(context, "Please wait...");

        Bundle bundle = getArguments();
        if (bundle != null) {
            deeplink = bundle.getString("deeplink");
            if (!sharedToken.getUserId().equals("")) {
                if (CheckInternet.isInternetAvailable(context)) {
                    progressDiog.show();
                    getLink(deeplink);
                } else {
                    context.startActivity(new Intent(context, NoInternetActivity.class));
                }

            } else {
                Intent intent = new Intent(context, SplashScreen.class);
                context.startActivity(intent);
                getActivity().finish();
            }


        }

        itemStatus = (TextView) view.findViewById(R.id.itemStatus);

        watchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoUrl",videoUrl);
                context.startActivity(intent);
            }
        });

        sharedRate = new SharedRate(context);
        userId = sharedToken.getUserId();
//        catid = sharedToken.getCatId();
    }


    // get the data from the link
    private void getLink(String deeplink) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<DescriptedApi> call = apiInterface.descriptedApi(deeplink);
        call.enqueue(new Callback<DescriptedApi>() {
            @Override
            public void onResponse(Call<DescriptedApi> call, Response<DescriptedApi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        sharedToken.setCatid(response.body().getData().getCategoryId().toString());
                        CatId = response.body().getData().getCategoryId().toString();
                        catid = CatId;
                        if (!catid.equals("1"))
                        {
                            recyclerView.setVisibility(View.GONE);
                            featureText.setVisibility(View.GONE);
                        }

                        if (catid.equals("2"))
                        {
                            esigalnih.setText(R.string.btnContactJobs);
                            buyButtonText.setText(R.string.btnBuyJobs);
                        }
                        else if (catid.equals("6"))
                        {
                            esigalnih.setText(R.string.btnContactInvestment);
                            buyButtonText.setText(R.string.btnBuyInvestment);
                        }
                        else if (catid.equals("1"))
                        {
                            esigalnih.setText(R.string.btnContact);
                            buyButtonText.setText(R.string.btnBuyProperty);
                        }
                        else
                        {
                            esigalnih.setText(R.string.btnContactOther);
                            buyButtonText.setText("");
                            buyButtonText.setBackground(ContextCompat.getDrawable(context, R.drawable.paymentnew));
                            ViewGroup.LayoutParams params = buyButtonText.getLayoutParams();
                            params.height = 160;
                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;;
                            buyButtonText.setLayoutParams(params);
                        }
                        Id = response.body().getData().getId().toString();
                        shareId = response.body().getData().getShareId().toString();

                        getAllDeatils();


                    } else {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<DescriptedApi> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //get the all item details
    private void getAllDeatils() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ItemAllDeatilsApi> call = apiInterface.getAllDetails(Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<ItemAllDeatilsApi>() {
            @Override
            public void onResponse(Call<ItemAllDeatilsApi> call, Response<ItemAllDeatilsApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        array.clear();
                        ItemAllDeatilsApi.Data datum = response.body().getData();
                        txtHeading.setText(datum.getTitle());
                        if (CatId!=null && !CatId.equals("1"))
                        {
                            String s = datum.getTitle();
                            txtHeading.setText(s.replace("Bedroom",""));
                        }
                        else
                        {
                            txtHeading.setText(datum.getTitle());
                        }
                        //double price1 = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPrice());
                        //txtPrice.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price1));
                        txtPrice.setText(response.body().getData().getCurrency() + " " + response.body().getData().getItemCurPrice());
                        if (datum.getMandate() != null) {
                            //double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getMandate().getCalculatePrice());
                            //txtComission.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
                            //txtComission.setText(" CHF " + " " + String.format("%.2f", price));
                            txtComission.setText(" CHF " + " " + datum.getMandate().getCalculatePrice());
                        } else {
                            //txtComission.setText(sharedRate.getCurrencyCode() + " 0");
                            txtComission.setText(" CHF " + " 0");
                        }
                        txtDescripation.setText(datum.getDescription());

                        if (!catid.equals("6"))
                        {
                            watchVideo.setVisibility(View.GONE);

                        }
                        else
                        {
                            if (!datum.getItemMeta().get(0).getItemValue().equals("")) {
                                watchVideo.setVisibility(View.VISIBLE);
                                videoUrl = "https://app.m8s.world/upload/items/videos/" + datum.getItemMeta().get(0).getItemValue();
                            }
                            else
                            {
                                watchVideo.setVisibility(View.GONE);
                            }
                        }
                        if (response.body().getData().getItemTreeStatus()!=null) {
                            if (response.body().getData().getItemTreeStatus().equals("under_offer")) {
                                itemStatus.setText(" Under offer ");
                            }

                            if (response.body().getData().getItemTreeStatus().equals("contract_exchange")) {
                                itemStatus.setText(" Contracts exchanged ");
                            }

                            if (response.body().getData().getItemTreeStatus().equals("sale_completed")) {
                                itemStatus.setText(" Sold ");
                            }
                        }

                        for (int i = 0; i < datum.getItemMeta().size(); i++) {

                            if (datum.getItemMeta().get(i).getItemValue() != null) {
                                if (datum.getItemMeta().get(i).getItemKey().equals("property_particularities")) {
                                    String data = datum.getItemMeta().get(i).getItemValue();
                                    List<String> arrayList = Arrays.asList(data.split("\\s*,\\s*"));
                                    Log.d("hello123", data + "\n ek" + arrayList);
                                    setCheckData(arrayList);
                                }
                            }


                        }

                        if (response.body().getData().getItemTreeStatus()!=null) {
                            if (response.body().getData().getItemTreeStatus().equals("under_offer")) {
                                itemStatus.setText(" Under offer ");
                            }

                            if (response.body().getData().getItemTreeStatus().equals("contract_exchange")) {
                                itemStatus.setText(" Contracts exchanged ");
                            }

                            if (response.body().getData().getItemTreeStatus().equals("sale_completed")) {
                                itemStatus.setText(" Sold ");
                            }
                        }


                        for (int a = 0; a < datum.getImages().size(); a++) {

                            array.add("https://app.m8s.world/public/upload/items/" + datum.getImages().get(a).getImage());

                            setImage(array);
                        }

                        getShareNumber();

                        SendData();


                    } else {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ItemAllDeatilsApi> call, Throwable t) {
                dialog.dismiss();

            }
        });

    }

    // accpected by the user
    private void SendData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AccpectedApi> call = apiInterface.accptedUrl(HomeActivity.userId, shareId);

        call.enqueue(new Callback<AccpectedApi>() {
            @Override
            public void onResponse(Call<AccpectedApi> call, Response<AccpectedApi> response) {
                progressDiog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        DilogeHome(response.body().getMessage());
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AccpectedApi> call, Throwable t) {
                progressDiog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // for check box
    private void setCheckData(List<String> arrayList) {
        CheckAdapter adapter = new CheckAdapter(context, arrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }



//    //apis for the descripted link
////    public void GetShareLink() {
////        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
////        Call<ShareLinkApi> call = apiInterface.sharelink(HomeActivity.userId, Id);
////        call.enqueue(new Callback<ShareLinkApi>() {
////            @Override
////            public void onResponse(Call<ShareLinkApi> call, Response<ShareLinkApi> response) {
////                if (response.isSuccessful()) {
////                    if (response.body().getStatus().equals(200)) {
////                        String message = response.body().getData();
////                        Intent share = new Intent(Intent.ACTION_SEND);
////                        share.setType("text/plain");
////                        share.putExtra(Intent.EXTRA_TEXT, message);
////                        startActivity(Intent.createChooser(share, "Share M8 Items with others"));
////
////                        getShareNumber();
////
////                    } else {
////                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
////                    }
////                } else {
////                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<ShareLinkApi> call, Throwable t) {
////                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
////            }
////        });
////    }

    //apis for the descripted link
    public void GetShareLink() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ShareLinkApi> call = apiInterface.sharelink(HomeActivity.userId, Id);

        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<ShareLinkApi>() {
            @Override
            public void onResponse(Call<ShareLinkApi> call, Response<ShareLinkApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {

                        String message = response.body().getData();
                        UserName = response.body().getUsername();
                        commission = response.body().getCommission();
                        Log.d("hello123", "2" + message);
                        if (message != null)
                            createLink(message);


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ShareLinkApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //create link for refer
    private void createLink(String referCode) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("" + referCode))
                .setDomainUriPrefix("m8sworld.page.link")
                // Open links with this app on Android  amitpandey12.page.link
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.m8.m8").build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.Bainzy.M8").setFallbackUrl(Uri.parse("https://apps.apple.com/in/app/M8/id1479388084")).setAppStoreId("id1479388084").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.d("hello123", "1" + dynamicLink.getUri());


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://" + dynamicLink.getUri().toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.d("hello123", "3" + shortLink);

                            String firstPart = "Dear M8,\n" +
                                    "\n" + UserName +
                                    " has sent you a M8 link containing an item that you may want to buy to receive an amazing discount.";

                            String secondPart = "\n\nIf you do not want to buy the item you can share it to someone you may think will buy it, if you buy it you will receive a discount of "+commission
                                    +" if you share the link to some one who buys it they will receive a great discount and you will receive a commission  equal to the amount of the buyers discount.\n" +
                                    "\n" +
                                    "M8s making money with M8s is the future of buying and selling on the internet, download the M8 app and start saving or earning now.\n" +
                                    "\n" +
                                    "Warm regards\n" +
                                    "The M8 Team";

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, firstPart +"\n\n"+ shortLink + secondPart +"");
                            share.putExtra(Intent.EXTRA_SUBJECT, "One of your mates from M8 has sent you one of their money making offers");
                            share.putExtra("shared", "shared");
                            startActivity(Intent.createChooser(share, "Share M8 Items with others"));

                            getShareNumber();

                        } else {

                        }
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
                    //UserName = response.body().getData().getName();
                } else {
                    //Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetMetaData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // set the image inot view pager
    private void setImage(final ArrayList<String> array) {

        itemImage.setOffscreenPageLimit(1);

        adapter = new ImageAdapter(context, array);
        itemImage.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemImage.setCurrentItem(getItem(1), true);
                imagePer.setVisibility(View.VISIBLE);
                if (itemImage.getCurrentItem() == array.size() - 1) {
                    imageNext.setVisibility(View.GONE);
                }
            }
        });


        imagePer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemImage.setCurrentItem(itemImage.getCurrentItem() - 1, true);
                imageNext.setVisibility(View.VISIBLE);
                if (itemImage.getCurrentItem() == 0) {
                    imagePer.setVisibility(View.GONE);
                }
            }
        });


    }


    private void getShareNumber() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetShareNumberApi> call = apiInterface.getShareNumber(sharedToken.getUserId());

        call.enqueue(new Callback<GetShareNumberApi>() {
            @Override
            public void onResponse(Call<GetShareNumberApi> call, Response<GetShareNumberApi> response) {

                if (response.isSuccessful()) {
                    txtnumber = response.body().getData().toString();
                    if (txtnumber.equals("-1")) {
                        txtBuyShare = "<font color=#BDC0D8>(</font> <font color=#EE373E>" + "Unlimted" + "</font> <font color=#BDC0D8>) buy more shares</font>";
                        textBuyShare.setText(Html.fromHtml(txtBuyShare));
                    } else {
                        txtBuyShare = "<font color=#BDC0D8>(</font> <font color=#EE373E>" + txtnumber + "</font> <font color=#BDC0D8>) buy more shares</font>";
                        textBuyShare.setText(Html.fromHtml(txtBuyShare));
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetShareNumberApi> call, Throwable t) {

                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getItem(int i) {
        return itemImage.getCurrentItem() + i;
    }

    //here user contact the seller
    private void getContact() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ContactSellerApi> call = apiInterface.contactSeller(sharedToken.getUserId(), Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait");
        dialog.show();

        call.enqueue(new Callback<ContactSellerApi>() {
            @Override
            public void onResponse(Call<ContactSellerApi> call, Response<ContactSellerApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ContactSellerApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //here user buy the item
    private void buyItem() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BuyApi> call = apiInterface.buyItem(sharedToken.getUserId(), Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<BuyApi>() {
            @Override
            public void onResponse(Call<BuyApi> call, Response<BuyApi> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        if (!response.body().getLegalAddress())
                        {
                            final Dialog dialog = new Dialog(context);

                            dialog.setContentView(R.layout.custom_dialog_upload);
                            dialog.setCancelable(false);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            dialog.show();

                            final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                            Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                            TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                            text.setText(response.body().getMessage());

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
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.putExtra("ItemId", "ItemId");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BuyApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //if user already accept the url its show this dialog
    private void DilogeHome(String data) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_gohome);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Button btnOk = (Button) dialog.findViewById(R.id.btnOkay);
        TextView text = (TextView) dialog.findViewById(R.id.txtViewing);
        text.setText(data);
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, HomeActivity.class);
//                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    //here user buy the item
    private void buyItemStripe() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BuyApi> call = apiInterface.buyItemCheck(userId, Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<BuyApi>() {
            @Override
            public void onResponse(Call<BuyApi> call, Response<BuyApi> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        //Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                        final Dialog dialog = new Dialog(context);

                        dialog.setContentView(R.layout.custom_dialog_upload);
                        dialog.setCancelable(false);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        dialog.show();

                        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                        text.setText(response.body().getMessage());

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
                                Intent intent = new Intent(getContext(), SpripePayment.class);
                                intent.putExtra("userId",userId);
                                intent.putExtra("shareId",Id);
                                intent.putExtra("directBuy",txtPrice.getText().toString());
                                intent.putExtra("directBuyName",txtHeading.getText().toString());
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BuyApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
