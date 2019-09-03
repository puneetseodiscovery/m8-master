package com.mandy.m8car.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.mandy.m8car.Adapter.MyShareAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.GetSharedApi;
import com.mandy.m8car.ServiceGenerator;

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

    ArrayList<GetSharedApi.Datum> arrayList = new ArrayList<>();

    public MyShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_share, container, false);

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

        textView.setText("Properties I have shared");

        getAllItem();

        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
    }


    private void getAllItem() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetSharedApi> call = apiInterface.getSharedApi(HomeActivity.userId, HomeActivity.categoryId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");

        call.enqueue(new Callback<GetSharedApi>() {
            @Override
            public void onResponse(Call<GetSharedApi> call, Response<GetSharedApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    arrayList.clear();
                    if (response.body().getStatus().equals(200)) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            arrayList.add(response.body().getData().get(i));
                            //set data into recyclerView
                            adapter = new MyShareAdapter(context, manager, arrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(adapter);

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

}
