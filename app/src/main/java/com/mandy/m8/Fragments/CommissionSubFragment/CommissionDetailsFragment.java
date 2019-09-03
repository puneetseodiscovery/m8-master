package com.mandy.m8.Fragments.CommissionSubFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.Activities.NoInternetActivity;
import com.mandy.m8.Adapter.ChildAdapter;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.Fragments.DetailsFragment;
import com.mandy.m8.util.ProgressBarClass;
import com.mandy.m8.R;
import com.mandy.m8.RetrofitModel.GetComissionPrice;
import com.mandy.m8.RetrofitModel.TreeListApi;
import com.mandy.m8.ServiceGenerator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandy.m8.util.SharedRate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommissionDetailsFragment extends Fragment {

    String Id, name, comission, image, price;
    Toolbar toolbar;
    ImageView drawer;
    TextView textView;
    View view;

    RecyclerView recyclerView;
    RoundedImageView roundedImageView;
    TextView txtChf, txtPropertyName, txtComission, txtPrice;
    ImageView imageView;
    ChildAdapter childAdapter;
    String userId;
    public ArrayList<TreeListApi.Treeuser> arrayList = new ArrayList<>();

    FragmentManager manager;

    Context context;

    public CommissionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_commission_details, container, false);

        init();


        manager = getActivity().getSupportFragmentManager();


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        textView.setText("Commission tree");

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        txtPrice.setText(price);
        txtChf.setText(comission);
        txtPropertyName.setText(name);
        Glide.with(context).load("http://m8.amrdev.site/public/upload/items/" + image).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ItemId", Id);
                detailsFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, detailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        if (CheckInternet.isInternetAvailable(context)) {
            getUserCommission(HomeActivity.userId);
            getCommsionPrice();
        } else {
            context.startActivity(new Intent(context, NoInternetActivity.class));
        }


        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.parent_image);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        txtChf = (TextView) view.findViewById(R.id.parent_chf);
        txtPropertyName = (TextView) view.findViewById(R.id.propert_name);
        txtPrice = (TextView) view.findViewById(R.id.property_price);
        txtComission = (TextView) view.findViewById(R.id.property_comission);
        recyclerView = (RecyclerView) view.findViewById(R.id.expandableList);


        Bundle bundle = getArguments();
        if (bundle != null) {
            Id = bundle.getString("PropertyId");
            name = bundle.getString("name");
            comission = bundle.getString("comission");
            price = bundle.getString("price");
            image = bundle.getString("image");


        }


    }

    private void getUserCommission(String userId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TreeListApi> call = apiInterface.getTreeList(userId, Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<TreeListApi>() {
            @Override
            public void onResponse(Call<TreeListApi> call, Response<TreeListApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    arrayList.clear();
                    if (response.body().getStatus().equals(200)) {
                        Glide.with(context).load("http://m8.amrdev.site/public/upload/users/profiles/" + response.body().getData().getProfileImage()).placeholder(R.drawable.myaccount).into(roundedImageView);

                        if (response.body().getData().getTreeusers() == null) {
                            Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < response.body().getData().getTreeusers().size(); i++) {
                                arrayList.add(response.body().getData().getTreeusers().get(i));

                                setIntoRecycler();
                            }
                        }
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TreeListApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setIntoRecycler() {
        childAdapter = new ChildAdapter(context, arrayList);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(childAdapter);

        childAdapter.setOnItem(new ChildAdapter.onItemClick() {
            @Override
            public void onItemclick(int pos) {
                userId = String.valueOf(pos);
                getUserCommission(userId);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }


    //get the comissio price
    private void getCommsionPrice() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetComissionPrice> call = apiInterface.getComissionPrice(Id, HomeActivity.userId);
        call.enqueue(new Callback<GetComissionPrice>() {
            @Override
            public void onResponse(Call<GetComissionPrice> call, Response<GetComissionPrice> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        SharedRate sharedRate = new SharedRate(context);
                        String abc = response.body().getData().getTotal().replace(",", "");
                        double price = Double.valueOf(response.body().getData().getTotal()) * Double.valueOf(sharedRate.getShared());

                        txtComission.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetComissionPrice> call, Throwable t) {

            }
        });
    }
}
