package com.m8.m8.Fragments;


import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.Fragments.MyAccountSubFragment.ViewProfileFragment;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.RetrofitModel.ProfileDataApi;
import com.m8.m8.ServiceGenerator;
import com.hbb20.CountryCodePicker;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    Toolbar toolbar;
    TextView textView,countryText;
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

    CheckBox checkBox;
    LinearLayout layoutBusiness;
    EditText edtBusinessName, edtBusinessEmail, edtBusinessPhone, edtBusinessAddress;

    String Lcountry;
    String bCountry, bName, bContact, bEmail, bPhone, bAddress1, bAddress2, bCity, bPostcode, bType, Lname, Lemail,
            Lphone, Laddress1, Laddress2, Lcity, Lpostcode;
    EditText edtLname, edtLemail, edtLphone, edtLaddress1, edtLaddress2, edtLcity, edtLpostcode;

    String name, email, mobile, address;

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
                if (checkBox.isChecked()) {
                    if (TextUtils.isEmpty(edtBusinessName.getText().toString()) || TextUtils.isEmpty(edtBusinessEmail.getText().toString())
                            || TextUtils.isEmpty(edtBusinessPhone.getText().toString()) || TextUtils.isEmpty(edtBusinessAddress.getText().toString())) {

                        validation(edtBusinessName);
                        validation(edtBusinessEmail);
                        validation(edtBusinessPhone);
                        validation(edtBusinessAddress);
                    } else {
                        sendIntent();
                    }
                } else {
                    sendIntent();
                }
            }
        });


        //for business
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    layoutBusiness.setVisibility(View.VISIBLE);
                } else {
                    layoutBusiness.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }


    // for validation
    private void validation(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Invalid data");
            editText.requestFocus();
        }
    }

    //set intent to go next activity
    private void sendIntent() {
        if (TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtContactName.getText().toString())
                || TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPhone.getText().toString()) || TextUtils.isEmpty(edtAddress1.getText().toString())
                || TextUtils.isEmpty(edtCity.getText().toString()) || TextUtils.isEmpty(edtPostcode.getText().toString())) {

            validation(edtName);
            validation(edtContactName);
            validation(edtEmail);
            validation(edtPhone);
            validation(edtAddress1);
//            validation(edtAdress2);
            validation(edtCity);
            validation(edtPostcode);
        } else {
            country = ccp.getSelectedCountryName();

            setSharedData();

//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.replace(R.id.framelayout, new Business2Fragment());
//            transaction.addToBackStack(null);
//            transaction.commit();

        }
    }



    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        countryText = (TextView) view.findViewById(R.id.countryText);
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

        layoutBusiness = (LinearLayout) view.findViewById(R.id.linearBusniess);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        edtBusinessName = (EditText) view.findViewById(R.id.editName);
        edtBusinessEmail = (EditText) view.findViewById(R.id.editEmail);
        edtBusinessPhone = (EditText) view.findViewById(R.id.editPhone);
        edtBusinessAddress = (EditText) view.findViewById(R.id.editAddress);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryText.setText(ccp.getSelectedCountryName());
            }
        });

        manager = getActivity().getSupportFragmentManager();
        setData();
        getShareNumber();
    }

    // set data into shared memory
    private void setSharedData() {
        String bName = "", bMobile = "", bEmail = "", bAdress = "";

        String name, contactname, email, phone, address1, address2, city, postcode;
        name = edtName.getText().toString();
        contactname = edtContactName.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        address1 = edtAddress1.getText().toString();
        address2 = edtAdress2.getText().toString();
        city = edtCity.getText().toString();
        postcode = edtPostcode.getText().toString();

        // for businesss
        bName = edtBusinessName.getText().toString();
        bAdress = edtBusinessAddress.getText().toString();
        bEmail = edtBusinessEmail.getText().toString();
        bMobile = edtBusinessPhone.getText().toString();

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

        editor.putString("name", bName);
        editor.putString("phone", bMobile);
        editor.putString("address", bAdress);
        editor.putString("email", bEmail);

        editor.apply();
        setDataNew();
    }

    //set data into shared memory
    private void setDataNew() {
        Lname = "";
        Lemail = "";
        Lphone = "";
        Laddress1 = "";
        Laddress2 = "";
        Lcity = "";
        Lpostcode ="";


        bName = sharedPreferences.getString("bName", "");
        bContact = sharedPreferences.getString("bContact", "");
        bEmail = sharedPreferences.getString("bEmail", "");
        bPhone = sharedPreferences.getString("bPhone", "");
        bAddress1 = sharedPreferences.getString("bAddress1", "");
        bAddress2 = sharedPreferences.getString("bAddress2", "");
        bCity = sharedPreferences.getString("bCity", "");
        bPostcode = sharedPreferences.getString("bPostcode", "");
        bType = sharedPreferences.getString("bType", "");
        bCountry = sharedPreferences.getString("bCountry", "");

        name = sharedPreferences.getString("name", "");
        mobile = sharedPreferences.getString("phone", "");
        address = sharedPreferences.getString("address", "");
        email = sharedPreferences.getString("email", "");


        //hit the api to create the profile
        getShareDataNew();
    }

    private void getShareDataNew() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ProfileDataApi> call = apiInterface.getShareProfileApi(HomeActivity.userId, bType, bName +" "+bContact, bContact, bEmail, bPhone, bAddress1,
                bAddress2, bCity, bPostcode, bCountry, Lname, Lemail, Lphone, Laddress1, Laddress2, Lcity, Lpostcode, Lcountry, name, mobile, address, email);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<ProfileDataApi>() {
            @Override
            public void onResponse(Call<ProfileDataApi> call, Response<ProfileDataApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.framelayout, new HomeFragment());
                    transaction.commit();
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDataApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
        if (ViewProfileFragment.data==null)
        {
            GetMetaDetails();
        }
        else {
            String[] spt = ViewProfileFragment.data.getName().split(" ");
            edtName.setText(spt[0]);
            if (spt.length>1)
            edtContactName.setText(spt[1]);
            edtEmail.setText(ViewProfileFragment.data.getEmail());
            edtPhone.setText(ViewProfileFragment.data.getContactNo());


            if (ViewProfileFragment.data.getProfile() != null) {
                if (ViewProfileFragment.data.getProfile().getAddress() != null)
                    edtAddress1.setText(ViewProfileFragment.data.getProfile().getAddress().toString());
                if (ViewProfileFragment.data.getProfile().getTown() != null)
                    edtCity.setText(ViewProfileFragment.data.getProfile().getTown().toString());
                if (ViewProfileFragment.data.getProfile().getPostalCode() != null)
                    edtPostcode.setText(ViewProfileFragment.data.getProfile().getPostalCode().toString());
                if (ViewProfileFragment.data.getProfile().getAddress2() != null)
                    edtAdress2.setText(ViewProfileFragment.data.getProfile().getAddress2().toString());
                if (ViewProfileFragment.data.getProfile().getCountry()!=null)
                    countryText.setText(ViewProfileFragment.data.getProfile().getCountry());
            }
            if (ViewProfileFragment.data.getProfile().getBName() != null) {
                if (ViewProfileFragment.data.getProfile().getBName() != null)
                    edtBusinessName.setText(ViewProfileFragment.data.getProfile().getBName().toString());
                if (ViewProfileFragment.data.getProfile().getBEmail() != null)
                    edtBusinessEmail.setText(ViewProfileFragment.data.getProfile().getBEmail().toString());
                if (ViewProfileFragment.data.getProfile().getBPhone() != null)
                    edtBusinessPhone.setText(ViewProfileFragment.data.getProfile().getBPhone().toString());
                if (ViewProfileFragment.data.getProfile().getBAddress() != null)
                    edtBusinessAddress.setText(ViewProfileFragment.data.getProfile().getBAddress().toString());

            }
        }
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    //Get the user details
    public void GetMetaDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        SharedToken sharedToken = new SharedToken(context);
        String userId = sharedToken.getUserId();
        Call<GetMetaData> call = apiInterface.getMetaData(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetMetaData>() {
            @Override
            public void onResponse(Call<GetMetaData> call, Response<GetMetaData> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    GetMetaData.Data data1 = response.body().getData();
                    ViewProfileFragment.data = data1;
                    String[] spt = ViewProfileFragment.data.getName().split(" ");
                    edtName.setText(spt[0]);
                    edtContactName.setText(spt[1]);
                    edtEmail.setText(ViewProfileFragment.data.getEmail());
                    edtPhone.setText(ViewProfileFragment.data.getContactNo());
                    if (ViewProfileFragment.data.getProfile() != null) {
                        if (ViewProfileFragment.data.getProfile().getAddress() != null)
                            edtAddress1.setText(ViewProfileFragment.data.getProfile().getAddress().toString());
                        if (ViewProfileFragment.data.getProfile().getAddress2() != null)
                            edtAdress2.setText(ViewProfileFragment.data.getProfile().getAddress2().toString());
                        if (ViewProfileFragment.data.getProfile().getTown() != null)
                            edtCity.setText(ViewProfileFragment.data.getProfile().getTown().toString());
                        if (ViewProfileFragment.data.getProfile().getPostalCode() != null)
                            edtPostcode.setText(ViewProfileFragment.data.getProfile().getPostalCode().toString());
                    }
                    if (ViewProfileFragment.data.getProfile().getBName() != null) {
                        if (ViewProfileFragment.data.getProfile().getBName() != null)
                            edtBusinessName.setText(ViewProfileFragment.data.getProfile().getBName().toString());
                        if (ViewProfileFragment.data.getProfile().getBEmail() != null)
                            edtBusinessEmail.setText(ViewProfileFragment.data.getProfile().getBEmail().toString());
                        if (ViewProfileFragment.data.getProfile().getBPhone() != null)
                            edtBusinessPhone.setText(ViewProfileFragment.data.getProfile().getBPhone().toString());
                        if (ViewProfileFragment.data.getProfile().getBAddress() != null)
                            edtBusinessAddress.setText(ViewProfileFragment.data.getProfile().getBAddress().toString());
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetMetaData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
