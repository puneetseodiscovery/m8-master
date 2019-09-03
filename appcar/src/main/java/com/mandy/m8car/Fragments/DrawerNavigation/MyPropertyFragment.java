package com.mandy.m8car.Fragments.DrawerNavigation;


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
import com.mandy.m8car.Adapter.MypropertyAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.GetPropertyApi;
import com.mandy.m8car.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPropertyFragment extends Fragment {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView;
    ImageView imageView;
    View view;
    Context context;
    RecyclerView recyclerView;
    FragmentManager manager;

    ArrayList<GetPropertyApi.Datum> arrayList = new ArrayList<>();

    public MyPropertyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_property, container, false);

        init();

        manager = getActivity().getSupportFragmentManager();

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


        textView.setText("My Property");

        GetAllProperty();

        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        imageView = (ImageView) view.findViewById(R.id.toolbarLogo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void setIntoRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MypropertyAdapter adapter = new MypropertyAdapter(context, manager, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    public void GetAllProperty() {
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPropertyApi> call = apiInterface.getPropertyApi(HomeActivity.userId, HomeActivity.categoryId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetPropertyApi>() {
            @Override
            public void onResponse(Call<GetPropertyApi> call, Response<GetPropertyApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    clear();
                    if (response.body().getStatus().equals(200)) {

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            arrayList.add(response.body().getData().get(i));


                            setIntoRecycler();
                        }
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPropertyApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void clear() {
        arrayList.clear();
    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}
