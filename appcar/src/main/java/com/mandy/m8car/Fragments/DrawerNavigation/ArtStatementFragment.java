package com.mandy.m8car.Fragments.DrawerNavigation;


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

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtStatementFragment extends Fragment {
    Toolbar toolbar;
    ImageView drawer;
    TextView textView, txtArt;
    ImageView imageView;
    View view;
    Context context;

    public ArtStatementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art_statement, container, false);

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


        textView.setText("Art Statement");
        imageView.setVisibility(View.VISIBLE);


        txtArt.setText(Html.fromHtml(HomeActivity.data.getArtStatement()));

        return view;
    }


    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        imageView = (ImageView) view.findViewById(R.id.toolbarLogo);
        txtArt = (TextView) view.findViewById(R.id.textView);
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
