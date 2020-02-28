package com.m8.m8.Fragments.CommissionSubFragment;


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
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.NoInternetActivity;
import com.m8.m8.Adapter.ChildAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.Fragments.DetailsFragment;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetComissionPrice;
import com.m8.m8.RetrofitModel.TreeListApi;
import com.m8.m8.ServiceGenerator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommissionDetailsFragment extends Fragment {

    String Id, name, comission, image, price,owner_name,ownerIdString,item_tree_status;
    int owner_id;
    Toolbar toolbar;
    ImageView drawer;
    TextView textView,parent_text,itemStatus;
    View view;

    Boolean puneet = false;

    RecyclerView recyclerView;
    RoundedImageView roundedImageView;
    TextView txtChf, txtPropertyName, txtComission, txtPrice;
    ImageView imageView;
    ChildAdapter childAdapter;
    String userId;
    public ArrayList<TreeListApi.Treeuser> arrayList = new ArrayList<>();


    FragmentManager manager;
    SharedToken sharedToken;
    String userid, catId;

    Context context;
    final String totalCommissionPrice="";
    ArrayList<String> backForCommission;
    ArrayList<String> backForCommissionName;

    public CommissionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_commission_details, container, false);
        sharedToken = new SharedToken(context);
        catId = sharedToken.getCatId();
        userid = sharedToken.getUserId();
        backForCommission = new ArrayList<>();
        backForCommissionName = new ArrayList<>();

        init();

        manager = getActivity().getSupportFragmentManager();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backForCommission.size()>=1)
                {
                    if (puneet)
                    {
                        getUserCommission(backForCommission.get(backForCommission.size()-2));
                        backForCommission.remove(backForCommission.size()-1);
                        if (backForCommission.size()!=1) {
                            String[] arrayList = backForCommissionName.get(backForCommissionName.size()-2).split("~!~");
                            String text = "<font color=#444862>" + arrayList[0] + "</font> <br>" + arrayList[1];
                            //txtChf.setText("<font color=\"#444862\">" + response.body().getData().getName() + "</font>" + "\n" + comission);
                            txtChf.setText(Html.fromHtml(text));
                            backForCommissionName.remove(backForCommissionName.size()-1);
                            puneet = true;
                        }
                        if (backForCommission.size()==1)
                        {
                            getUserCommission(ownerIdString);
                            getCommsionPrice();
                            puneet = false;
                        }
                    }
                    else
                    {
                        getActivity().onBackPressed();
                        backForCommissionName.clear();
                        backForCommission.clear();
                    }

                }
                else {
                    getActivity().onBackPressed();
                    backForCommissionName.clear();
                    backForCommission.clear();
                }
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
        txtComission.setText(comission);

        txtPropertyName.setText(name);
        Glide.with(context).load("https://app.m8s.world/public/upload/items/" + image).into(imageView);

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
            //getUserCommission(HomeActivity.userId);
            getUserCommission(ownerIdString);
            getCommsionPrice();
            backForCommission.add(ownerIdString);
            backForCommissionName.add(ownerIdString);
        } else {
            context.startActivity(new Intent(context, NoInternetActivity.class));
        }
        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        itemStatus = (TextView) view.findViewById(R.id.itemStatus);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.parent_image);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        txtChf = (TextView) view.findViewById(R.id.parent_chf);
        txtPropertyName = (TextView) view.findViewById(R.id.propert_name);
        txtPrice = (TextView) view.findViewById(R.id.property_price);
        txtComission = (TextView) view.findViewById(R.id.property_comission);
        recyclerView = (RecyclerView) view.findViewById(R.id.expandableList);
        parent_text = view.findViewById(R.id.parent_text);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Id = bundle.getString("PropertyId");
            name = bundle.getString("name");
            comission = bundle.getString("comission");
            price = bundle.getString("price");
            image = bundle.getString("image");
            owner_name = bundle.getString("owner_name");
            if (bundle.getString("item_tree_status")!=null)
            item_tree_status = bundle.getString("item_tree_status");
            else
                item_tree_status = "";
            owner_id = bundle.getInt("owner_id");
            ownerIdString = String.valueOf(owner_id);
        }
        if (owner_name!=null)
        {
            parent_text.setText(owner_name+" "+"is the first sharer in all of this items commission trees, the more times this item is shared the more the commission is divided in each tree.\n\nThe more times you share this item the more commission trees you create so the more chances you have earning the commission or part of it.\n\nPlease look at the amount next to your name, that is the amount all of the tree above your name will earn if you decide to buy.\n\nIf you share on, the amount next to the buyer’s name is the amount everyone on the winning commission tree will earn.\n\nShare intelligently to earn more and good luck!\n\n");
        }

        if (item_tree_status!=null) {
            if (item_tree_status.equals("under_offer")) {
                itemStatus.setText(" Under offer ");
            }

            if (item_tree_status.equals("contract_exchange")) {
                itemStatus.setText(" Contracts exchanged ");
            }

            if (item_tree_status.equals("sale_completed")) {
                itemStatus.setText(" Sold ");
            }
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
                        Glide.with(context).load("https://app.m8s.world/public/upload/users/profiles/" + response.body().getData().getProfileImage()).placeholder(R.drawable.myaccount).into(roundedImageView);
                        String nn = response.body().getData().getName();
                        if (!puneet) {
                            String[] arrayList = comission.split(" ");
                            String text = "<font color=#444862>"+response.body().getData().getName()+"</font> <br>"+"CHF " + arrayList[2];
                            //txtChf.setText("<font color=\"#444862\">" + response.body().getData().getName() + "</font>" + "\n" + comission);
                            txtChf.setText(Html.fromHtml(text));

                        }
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
            public void onItemclick(int pos,String name) {
                userId = String.valueOf(pos);
                backForCommission.add(userId);
                backForCommissionName.add(name);
                getUserCommission(userId);
                String[] arrayList = name.split("~!~");
                String text = "<font color=#444862>"+arrayList[0]+"</font> <br>" + arrayList[1];
                //txtChf.setText("<font color=\"#444862\">" + response.body().getData().getName() + "</font>" + "\n" + comission);
                txtChf.setText(Html.fromHtml(text));
                puneet = true;
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
        Call<GetComissionPrice> call = apiInterface.getComissionPrice(Id, userid);
        call.enqueue(new Callback<GetComissionPrice>() {
            @Override
            public void onResponse(Call<GetComissionPrice> call, Response<GetComissionPrice> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        SharedRate sharedRate = new SharedRate(context);
                        String abc = response.body().getData().getTotal().replace(",", "");
                        double price = Double.valueOf(response.body().getData().getTotal()) * Double.valueOf(sharedRate.getShared());
//                        txtComission.setText(" CHF " + " " + response.body().getData().getTotal());
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
