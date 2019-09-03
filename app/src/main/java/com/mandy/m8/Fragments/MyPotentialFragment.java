package com.mandy.m8.Fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.R;
import com.mandy.m8.Fragments.PotentialSubFragment.AllFragment;
import com.mandy.m8.Fragments.PotentialSubFragment.OfferFragment;
import com.mandy.m8.Fragments.PotentialSubFragment.SeleFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPotentialFragment extends Fragment {

    View view;
    Toolbar toolbar;
    ImageView drawer;
    TextView txtAll, txtOffer, txtSele;
    FragmentManager manager;
    FragmentTransaction transaction;
    Context context;

    public MyPotentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_potential, container, false);

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


        //here see all three fragment
        manager = getActivity().getSupportFragmentManager();


        manager.beginTransaction().replace(R.id.potential_framelayout, new AllFragment()).commit();
        txtAll.setBackgroundResource(R.drawable.potentionalbutton);
        txtOffer.setBackgroundResource(R.drawable.potentialbuttoninactive);
        txtSele.setBackgroundResource(R.drawable.potentialbuttoninactive);

        GetFragment();


        return view;
    }


    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.imageLogo);
        txtAll = (TextView) view.findViewById(R.id.all);
        txtOffer = (TextView) view.findViewById(R.id.offer);
        txtSele = (TextView) view.findViewById(R.id.sele);

    }


    private void GetFragment() {

        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAll.setBackgroundResource(R.drawable.potentionalbutton);
                txtOffer.setBackgroundResource(R.drawable.potentialbuttoninactive);
                txtSele.setBackgroundResource(R.drawable.potentialbuttoninactive);

                manager.beginTransaction().replace(R.id.potential_framelayout, new AllFragment()).commit();

            }
        });

        txtSele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSele.setBackgroundResource(R.drawable.potentionalbutton);
                txtOffer.setBackgroundResource(R.drawable.potentialbuttoninactive);
                txtAll.setBackgroundResource(R.drawable.potentialbuttoninactive);

                manager.beginTransaction().replace(R.id.potential_framelayout, new SeleFragment()).commit();

            }
        });

        txtOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOffer.setBackgroundResource(R.drawable.potentionalbutton);
                txtAll.setBackgroundResource(R.drawable.potentialbuttoninactive);
                txtSele.setBackgroundResource(R.drawable.potentialbuttoninactive);

                manager.beginTransaction().replace(R.id.potential_framelayout, new OfferFragment()).commit();

            }
        });


    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}
