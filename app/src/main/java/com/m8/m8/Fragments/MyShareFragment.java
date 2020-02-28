package com.m8.m8.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.m8.m8.Adapter.MyShareAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetSharedApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.SharedToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyShareFragment extends Fragment {

    View view;
    Context context;
    RecyclerView recyclerView;
    MyShareAdapter adapter;
    FragmentManager manager;
    Toolbar toolbar;
    TextView textView;
    ImageView drawer;
    RelativeLayout noRecord;
    SharedToken sharedToken;
    String userId, categoryId;
    ArrayList<GetSharedApi.Datum> arrayList = new ArrayList<>();
    public com.google.android.gms.ads.AdView mAdView;

    public MyShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_share, container, false);
        sharedToken = new SharedToken(context);
        userId = sharedToken.getUserId();
        categoryId = sharedToken.getCatId();

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

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        textView.setText("Items I have shared");

        getAllItem();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        noRecord = view.findViewById(R.id.noRecord);
        addAds();
    }


    private void getAllItem() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetSharedApi> call = apiInterface.getSharedApi(userId, categoryId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");

        call.enqueue(new Callback<GetSharedApi>() {
            @Override
            public void onResponse(Call<GetSharedApi> call, Response<GetSharedApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    arrayList.clear();
                    if (response.body().getStatus().equals(200)) {

                        if (response.body().getData().size()>0)
                        {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayList.add(response.body().getData().get(i));

                                    noRecord.setVisibility(View.GONE);
                                    //set data into recyclerView
                                    adapter = new MyShareAdapter(context, manager, arrayList);
                                    LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(manager);
                                    recyclerView.setAdapter(adapter);

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
            public void onFailure(Call<GetSharedApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
