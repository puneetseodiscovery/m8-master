package com.m8sworld.m8.Fragments.DrawerNavigation;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Terms_ConditionFragment extends Fragment {

    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtTerms;
    ImageView imageView;
    View view;
    Context context;
    String watch="";

    public Terms_ConditionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_terms__condition, container, false);

        init();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        textView.setText("Terms & Conditions");
        imageView.setVisibility(View.VISIBLE);

        txtTerms.setText(Html.fromHtml(HomeActivity.data.getTermAndCondition()));


        return view;
    }


    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        imageView = (ImageView) view.findViewById(R.id.toolbarLogo);
        txtTerms = (TextView) view.findViewById(R.id.textView);
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}