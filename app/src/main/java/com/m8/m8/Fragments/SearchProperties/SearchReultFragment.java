package com.m8.m8.Fragments.SearchProperties;


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
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Adapter.SearchResultAdapter;
import com.m8.m8.Fragments.ViewAllFragment;
import com.m8.m8.R;
import com.m8.m8.util.SharedToken;


public class SearchReultFragment extends Fragment {

    View view;
    Context context;
    Toolbar toolbar;
    TextView textView;
    ImageView drawer;
    RecyclerView recyclerView;
    SearchResultAdapter searchResultAdapter;
    FragmentManager manager;
    SharedToken sharedToken;
    String userId, categoryId;
    public com.google.android.gms.ads.AdView mAdView;

    public SearchReultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_reult, container, false);
        sharedToken = new SharedToken(context);
        userId = sharedToken.getUserId();
        categoryId = sharedToken.getCatId();

        manager = getActivity().getSupportFragmentManager();

        init();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
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

        textView.setText("Search Results");

        setData();

        return view;
    }

    private void init() {
        toolbar = view.findViewById(R.id.tooolbar);
        textView = view.findViewById(R.id.toolbarText);
        drawer = view.findViewById(R.id.tooolbarImage);
        recyclerView = view.findViewById(R.id.recyclerView);
        addAds();
    }

    private void setData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        searchResultAdapter = new SearchResultAdapter(context, manager, ViewAllFragment.arrayData);
        recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();
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
