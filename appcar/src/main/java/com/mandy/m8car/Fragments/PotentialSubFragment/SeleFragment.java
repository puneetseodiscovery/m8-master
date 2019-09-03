package com.mandy.m8car.Fragments.PotentialSubFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.Adapter.PotentialAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.GetPotentailApi;
import com.mandy.m8car.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeleFragment extends Fragment {

    View view;
    Context context;
    RecyclerView recyclerView;
    ArrayList<GetPotentailApi.Datum> arrayList = new ArrayList<>();
    FragmentManager manager;

    public SeleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sele, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        manager = getActivity().getSupportFragmentManager();


        //get all sale property
        GetAllProperty();


        return view;
    }


    private void setIntoRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PotentialAdapter adapter = new PotentialAdapter(context, manager, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    public void GetAllProperty() {
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPotentailApi> call = apiInterface.getProtentail(HomeActivity.userId, HomeActivity.categoryId, "sale");
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetPotentailApi>() {
            @Override
            public void onResponse(Call<GetPotentailApi> call, Response<GetPotentailApi> response) {
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
            public void onFailure(Call<GetPotentailApi> call, Throwable t) {
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
