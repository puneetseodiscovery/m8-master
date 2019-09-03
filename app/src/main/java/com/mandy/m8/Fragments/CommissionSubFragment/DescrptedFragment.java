package com.mandy.m8.Fragments.CommissionSubFragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.Activities.LoginActivity;
import com.mandy.m8.Activities.NoInternetActivity;
import com.mandy.m8.Adapter.CheckAdapter;
import com.mandy.m8.Adapter.ImageAdapter;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.Fragments.HomeFragment;
import com.mandy.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.mandy.m8.util.ProgressBarClass;
import com.mandy.m8.R;
import com.mandy.m8.RetrofitModel.AccpectedApi;
import com.mandy.m8.RetrofitModel.BuyApi;
import com.mandy.m8.RetrofitModel.ContactSellerApi;
import com.mandy.m8.RetrofitModel.DescriptedApi;
import com.mandy.m8.RetrofitModel.GetShareNumberApi;
import com.mandy.m8.RetrofitModel.ItemAllDeatilsApi;
import com.mandy.m8.RetrofitModel.ShareLinkApi;
import com.mandy.m8.ServiceGenerator;
import com.mandy.m8.util.SharedRate;
import com.mandy.m8.util.UtileDilog;

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

    TextView textView, textBuyShare, txtDescripation, txtHeading, txtPrice, txtComission, txtFeature, txtFeature2;
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

    String txtBuyShare, txtnumber = "0";
    String shareId, deeplink;
    ProgressDialog progressDiog;
    SharedRate sharedRate;


    public DescrptedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_descrpted, container, false);

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
                buyItem();
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

        progressDiog = ProgressBarClass.showProgressDialog(context, "Please wait...");

        Bundle bundle = getArguments();
        if (bundle != null) {
            deeplink = bundle.getString("deeplink");
            if (!HomeActivity.userId.equals("") && !HomeActivity.categoryId.equals("")) {
                if (CheckInternet.isInternetAvailable(context)) {
                    progressDiog.show();
                    getLink(deeplink);
                } else {
                    context.startActivity(new Intent(context, NoInternetActivity.class));
                }

            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                getActivity().finish();
            }


        }

        sharedRate = new SharedRate(context);

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

                        Id = response.body().getData().getId().toString();
                        shareId = response.body().getData().getShareId().toString();

                        getAllDeatils();


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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
                        double price1 = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPrice());
                        txtPrice.setText(sharedRate.getCurrencyCode() + " " +  String.format("%.2f", price1));
                        if (datum.getMandate() != null) {
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getMandate().getCalculatePrice());
                            txtComission.setText(sharedRate.getCurrencyCode() + " " +  String.format("%.2f", price));
                        } else {
                            txtComission.setText(sharedRate.getCurrencyCode() + " 0");
                        }
                        txtDescripation.setText(datum.getDescription());
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


                        for (int a = 0; a < datum.getImages().size(); a++) {

                            array.add("http://m8.amrdev.site/public/upload/items/" + datum.getImages().get(a).getImage());

                            setImage(array);
                        }

                        getShareNumber();

                        SendData();


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
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    //apis for the descripted link
    public void GetShareLink() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ShareLinkApi> call = apiInterface.sharelink(HomeActivity.userId, Id);
        call.enqueue(new Callback<ShareLinkApi>() {
            @Override
            public void onResponse(Call<ShareLinkApi> call, Response<ShareLinkApi> response) {
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
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}
