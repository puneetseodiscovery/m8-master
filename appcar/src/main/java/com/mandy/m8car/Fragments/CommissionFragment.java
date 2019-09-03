package com.mandy.m8car.Fragments;


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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.Activities.NoInternetActivity;
import com.mandy.m8car.Adapter.CommissionTreeAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.CheckInternet;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.ComissionListApi;
import com.mandy.m8car.ServiceGenerator;


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


    public CommissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_commission, container, false);

        //find all id
        init();

        manager = getActivity().getSupportFragmentManager();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
        textView.setText(R.string.commisiontree);
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

        HomeActivity.cunter = 0;

    }


    public void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ComissionListApi> call = apiInterface.getComissionList(HomeActivity.userId, HomeActivity.categoryId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<ComissionListApi>() {
            @Override
            public void onResponse(Call<ComissionListApi> call, Response<ComissionListApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    clear();
                    if (response.body().getStatus().equals(200)) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            arraylist.add(response.body().getData().get(i));

                            setDataIntoRecycler();

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
}
