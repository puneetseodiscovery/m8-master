package com.m8.m8.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.NoInternetActivity;
import com.m8.m8.Adapter.CommissionTreeAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.ComissionListApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.SharedToken;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommissionFragment extends Fragment {
    ArrayList<ComissionListApi.Datum> arraylist = new ArrayList<>();

    View view;
    Context context;
    RecyclerView recyclerView;
    CommissionTreeAdapter adapter;
    Toolbar toolbar;
    ImageView drawer, toolbarLo;
    TextView textView;
    FragmentManager manager;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedToken sharedToken;
    String catId, userid;
    RelativeLayout noRecord;
    public com.google.android.gms.ads.AdView mAdView;


    public CommissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_commission, container, false);

        sharedToken = new SharedToken(context);

        catId = sharedToken.getCatId();
        userid = sharedToken.getUserId();

        //find all id
        init();

        manager = getActivity().getSupportFragmentManager();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        textView.setText(R.string.commisiontree);
        textView.setPadding(0,0,30,0);
        toolbarLo.setVisibility(View.VISIBLE);

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        //Get data from api
        if (CheckInternet.isInternetAvailable(context)) {
            getData();
        } else {
            startActivity(new Intent(context, NoInternetActivity.class));
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 1000);
            }
        });

        return view;
    }

    private void init() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        toolbarLo = (ImageView) view.findViewById(R.id.toolbarLogo);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);
        noRecord = view.findViewById(R.id.noRecord);

        HomeActivity.cunter = 0;
        addAds();

    }


    public void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ComissionListApi> call = apiInterface.getComissionList(userid, catId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<ComissionListApi>() {
            @Override
            public void onResponse(Call<ComissionListApi> call, Response<ComissionListApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    clear();
                    if (response.body().getStatus().equals(200)) {

                        if (response.body().getData().size()>0)
                        {
                            noRecord.setVisibility(View.GONE);
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arraylist.add(response.body().getData().get(i));

                                setDataIntoRecycler();

                            }
                        }
                        else
                        {
                            noRecord.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ComissionListApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setDataIntoRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommissionTreeAdapter(context, manager, arraylist);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void clear() {
        arraylist.clear();
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
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
}
