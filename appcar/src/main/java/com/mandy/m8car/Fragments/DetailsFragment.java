package com.mandy.m8car.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8car.Activities.NoInternetActivity;

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.Adapter.CheckAdapter;
import com.mandy.m8car.Adapter.ImageAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.CheckInternet;
import com.mandy.m8car.Fragments.MyShareSubFragment.SharePackageFragment;
import com.mandy.m8car.util.ProgressBarClass;
import com.mandy.m8car.RetrofitModel.BuyApi;
import com.mandy.m8car.RetrofitModel.ContactSellerApi;
import com.mandy.m8car.RetrofitModel.GetShareNumberApi;
import com.mandy.m8car.RetrofitModel.ItemAllDeatilsApi;
import com.mandy.m8car.RetrofitModel.ShareLinkApi;
import com.mandy.m8car.ServiceGenerator;
import com.mandy.m8car.util.SharedRate;
import com.mandy.m8car.util.UtileDilog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    Toolbar toolbar;
    ImageView drawer;
    Context context;
    public ViewPager itemImage;
    public static ViewPager viewPager;
    public static ScrollView scrollView;
    public static boolean abc = false;

    TextView textView, textBuyShare, txtDescripation, txtHeading, txtPrice, txtComission, txtFeature, txtFeature2;
    View view;
    FragmentTransaction transaction;
    FragmentManager manager;
    String Id = "";
    PagerAdapter adapter;
    ArrayList<String> array = new ArrayList<>();
    ImageView imageNext, imagePer;

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;

    String txtBuyShare, txtnumber = "0";
    SharedRate sharedRate;

    LinearLayout linearShare, linearBuy, linearContact;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_details, container, false);

        init();


        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("ac", "").equals("0")) {

        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("ac", "").equals("0")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    FragmentTransaction transaction1 = manager.beginTransaction();
                    transaction1.replace(R.id.framelayout, new HomeFragment());
                    transaction1.commit();
                } else {
                    getActivity().onBackPressed();
                }
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
                        context.startActivity(new Intent(context, NoInternetActivity.class));
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
                    buyItem();
                } else {
                    context.startActivity(new Intent(context, NoInternetActivity.class));
                }

            }
        });
        if (CheckInternet.isInternetAvailable(context)) {
            getAllDeatils();
        } else {
            context.startActivity(new Intent(context, NoInternetActivity.class));
        }
        //get the all item details


        linearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(context)) {
                    getContact();
                } else {
                    context.startActivity(new Intent(context, NoInternetActivity.class));
                }

            }
        });


        return view;
    }

    //here user contact the seller
    private void getContact() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ContactSellerApi> call = apiInterface.contactSeller(HomeActivity.userId, Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait");
        dialog.show();

        call.enqueue(new Callback<ContactSellerApi>() {
            @Override
            public void onResponse(Call<ContactSellerApi> call, Response<ContactSellerApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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
        Call<BuyApi> call = apiInterface.buyItem(HomeActivity.userId, Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<BuyApi>() {
            @Override
            public void onResponse(Call<BuyApi> call, Response<BuyApi> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    //here find all the id
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
        txtFeature2 = (TextView) view.findViewById(R.id.txtFeature2);
        imageNext = (ImageView) view.findViewById(R.id.imageNext);
        imagePer = (ImageView) view.findViewById(R.id.imagePer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerCheck);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        scrollView = (ScrollView) view.findViewById(R.id.nestedScrool);

        linearBuy = (LinearLayout) view.findViewById(R.id.linearBuy);
        linearContact = (LinearLayout) view.findViewById(R.id.linearContact);
        linearShare = (LinearLayout) view.findViewById(R.id.linearShare);


        Bundle bundle = getArguments();
        if (bundle != null) {
            Id = bundle.getString("ItemId");

        }

        sharedRate = new SharedRate(context);

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
                        txtDescripation.setText(datum.getDescription());

                        double price1 = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPrice());
                        txtPrice.setText(sharedRate.getCurrencyCode() + " " +  String.format("%.2f", price1));
                        if (datum.getMandate() != null) {
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getMandate().getCalculatePrice());
                            txtComission.setText(sharedRate.getCurrencyCode() + " " +  String.format("%.2f", price));
                        } else {
                            txtComission.setText(sharedRate.getCurrencyCode() + " 0");
                        }
                        for (int i = 0; i < datum.getItemMeta().size(); i++) {
//                            if (datum.getItemMeta().get(i).getItemValue() != null && !datum.getItemMeta().get(i).getItemValue().equals("") && !datum.getItemMeta().get(i).getItemKey().equals("property_particularities")) {
//                                String data2 = datum.getItemMeta().get(i).getItemKey().toString();
//                                String cap = data2.substring(0, 1).toUpperCase() + data2.substring(1);
//                                cap = cap.replace("_", " ");
//                                txtFeature.append(cap + " : \n");
//                                txtFeature2.append(datum.getItemMeta().get(i).getItemValue() + "\n");
//                            }

                            if (datum.getItemMeta().get(i).getItemValue() != null) {
                                if (datum.getItemMeta().get(i).getItemKey().equals("property_particularities")) {
                                    String data = datum.getItemMeta().get(i).getItemValue();
                                    List<String> arrayList = Arrays.asList(data.split("\\s*,\\s*"));
                                    Log.d("hello123", data + "\n ek" + arrayList);
                                    setCheckData(arrayList);
                                }
                            }
                        }


                        for (int a = 0; a < datum.getImages().size(); a++) {

                            array.add("http://m8.amrdev.site/public/upload/items/" + datum.getImages().get(a).getImage());

                            setImage(array);
                        }

                        getShareNumber();


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    //set the checkbox for the deatils
    private void setCheckData(List<String> arrayList) {
        CheckAdapter adapter = new CheckAdapter(context, arrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


    }

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
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(share, "Share M8 Items with others"));

                        getShareNumber();

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


        viewPager.setAdapter(adapter);
    }


    private void getShareNumber() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetShareNumberApi> call = apiInterface.getShareNumber(HomeActivity.userId);

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

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
