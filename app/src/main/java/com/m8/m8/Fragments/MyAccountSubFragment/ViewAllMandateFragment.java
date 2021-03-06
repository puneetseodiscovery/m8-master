package com.m8.m8.Fragments.MyAccountSubFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Adapter.GetAllMandateAdapter;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetAllMandate;
import com.m8.m8.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllMandateFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView drawer;
    TextView textView;
    Context context;
    ArrayList<GetAllMandate.Datum> allMandates = new ArrayList<>();
    public com.google.android.gms.ads.AdView mAdView;

    public ViewAllMandateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_all_mandate, container, false);

        init();


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

        textView.setText("My Mandates");


        getData();

        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        addAds();
    }

    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetAllMandate> call = apiInterface.getallMandat(HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        call.enqueue(new Callback<GetAllMandate>() {
            @Override
            public void onResponse(Call<GetAllMandate> call, final Response<GetAllMandate> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    allMandates.clear();
                    if (response.body().getStatus() == 200) {
                        for (int i = 0; i < response.body().getData().size(); i++) {

                            allMandates.add(response.body().getData().get(i));
                            setData();
                        }


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetAllMandate> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setData() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayout);
        final GetAllMandateAdapter allMandateAdapter = new GetAllMandateAdapter(context, allMandates);
        recyclerView.setAdapter(allMandateAdapter);

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
