package com.m8sworld.m8.Fragments.DrawerNavigation;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.Activities.NoInternetActivity;
import com.m8sworld.m8.Adapter.MypropertyAdapter;
import com.m8sworld.m8.ApiInterface;
import com.m8sworld.m8.util.CheckInternet;
import com.m8sworld.m8.util.ProgressBarClass;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.GetPropertyApi;
import com.m8sworld.m8.ServiceGenerator;
import com.m8sworld.m8.util.SharedToken;

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
    SharedToken sharedToken;
    String userId, categoryId;

    ArrayList<GetPropertyApi.Datum> arrayList = new ArrayList<>();
    public static String DATA_RECEIVE = "data_receive";
    String type = "",address="";

    public MyPropertyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_property, container, false);
        sharedToken = new SharedToken(context);
        Bundle args = getArguments();
        if (args != null) {
            //showReceivedData.setText(args.getString(DATA_RECEIVE));
            String currentString = args.getString(DATA_RECEIVE);
            String[] separated = currentString.split("#");
            userId = separated[0];
            if (separated.length>1)
            address = separated[1];
            else
                address="";
            //userId = sharedToken.getUserId();
            type = "map";
        }
        else
        {
            userId = sharedToken.getUserId();
            address = "";
            type="";
        }
        categoryId = sharedToken.getCatId();
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


        //textView.setText("My Property");
        textView.setText("Items");

        if (CheckInternet.isInternetAvailable(context)) {
            GetAllProperty();
        } else {
            context.startActivity(new Intent(context, NoInternetActivity.class));
        }

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
        Call<GetPropertyApi> call = apiInterface.getPropertyApi(userId,type,address, categoryId);
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
