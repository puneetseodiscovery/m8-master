package com.mandy.m8car.Fragments.SearchProperties;


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

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.Adapter.SearchResultAdapter;
import com.mandy.m8car.Fragments.ViewAllFragment;
import com.mandy.m8car.R;


public class SearchReultFragment extends Fragment {

    View view;
    Context context;
    Toolbar toolbar;
    TextView textView;
    ImageView drawer;
    RecyclerView recyclerView;
    SearchResultAdapter searchResultAdapter;
    FragmentManager manager;

    public SearchReultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_reult, container, false);

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

}
