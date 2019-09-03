package com.mandy.m8car.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment;
import com.mandy.m8car.Fragments.MyAccountSubFragment.ViewProfileFragment;

import com.mandy.m8car.R;
import com.mandy.m8car.RetrofitModel.GetShareNumberApi;
import com.mandy.m8car.ServiceGenerator;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    Toolbar toolbar;
    TextView textView;
    ImageView drawer;
    View view;
    Context context;
    FragmentManager manager;
    RadioGroup radioGroup;
    EditText edtName, edtContactName, edtEmail, edtPhone, edtAddress1, edtAdress2, edtCity, edtPostcode;
    Button btnNext;
    String type = "Sharer";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String number, country;
    CountryCodePicker ccp;


    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_account, container, false);

        init();


        final AppCompatActivity activity = (AppCompatActivity) getActivity();
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

        textView.setText("My Account");


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(getActivity(), "Select the User type", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edtName.getText().toString())) {
                    edtName.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtContactName.getText().toString())) {
                    edtContactName.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    edtEmail.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    edtPhone.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtAddress1.getText().toString())) {
                    edtAddress1.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtAdress2.getText().toString())) {
                    edtAdress2.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtCity.getText().toString())) {
                    edtCity.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtPostcode.getText().toString())) {
                    edtPostcode.setError("Invalid data");
                } else {
                    country = ccp.getSelectedCountryName();

                    setSharedData();

                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.framelayout, new Business2Fragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });


        return view;
    }


    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        edtName = (EditText) view.findViewById(R.id.business_name);
        edtContactName = (EditText) view.findViewById(R.id.business_contactName);
        edtEmail = (EditText) view.findViewById(R.id.business_email);
        edtPhone = (EditText) view.findViewById(R.id.business_phone);
        edtAddress1 = (EditText) view.findViewById(R.id.business_address1);
        edtAdress2 = (EditText) view.findViewById(R.id.business_address2);
        edtCity = (EditText) view.findViewById(R.id.business_city);
        edtPostcode = (EditText) view.findViewById(R.id.business_postcode);
        btnNext = (Button) view.findViewById(R.id.button_next);
        ccp = (CountryCodePicker) view.findViewById(R.id.business_country);


        manager = getActivity().getSupportFragmentManager();

        setData();

        getShareNumber();
    }

    // set data into shared memory
    private void setSharedData() {
        String name, contactname, email, phone, address1, address2, city, postcode;
        name = edtName.getText().toString();
        contactname = edtContactName.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        address1 = edtAddress1.getText().toString();
        address2 = edtAdress2.getText().toString();
        city = edtCity.getText().toString();
        postcode = edtPostcode.getText().toString();

        sharedPreferences = getActivity().getSharedPreferences("Business", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("bName", name);
        editor.putString("bContact", contactname);
        editor.putString("bEmail", email);
        editor.putString("bPhone", phone);
        editor.putString("bAddress1", address1);
        editor.putString("bAddress2", address2);
        editor.putString("bCity", city);
        editor.putString("bPostcode", postcode);
        editor.putString("bType", type);
        editor.putString("bCountry", country);
        editor.apply();
    }


    //get the share number
    private void getShareNumber() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetShareNumberApi> call = apiInterface.getShareNumber(HomeActivity.userId);

        call.enqueue(new Callback<GetShareNumberApi>() {
            @Override
            public void onResponse(Call<GetShareNumberApi> call, Response<GetShareNumberApi> response) {

                if (response.isSuccessful()) {
                    number = response.body().getData().toString();
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetShareNumberApi> call, Throwable t) {

                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setData() {
        String[] spt = ViewProfileFragment.data.getName().split(" ");
        edtName.setText(spt[0]);
        edtContactName.setText(spt[1]);
        edtEmail.setText(ViewProfileFragment.data.getEmail());
        edtPhone.setText(ViewProfileFragment.data.getContactNo());
        if (ViewProfileFragment.data.getProfile() != null) {
            edtAddress1.setText(ViewProfileFragment.data.getProfile().getAddress());
            edtAdress2.setText(ViewProfileFragment.data.getProfile().getAddress2());
            edtCity.setText(ViewProfileFragment.data.getProfile().getTown());
            edtPostcode.setText(ViewProfileFragment.data.getProfile().getPostalCode());
        }

    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
