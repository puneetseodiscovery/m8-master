package com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.Fragments.DetailsFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.ViewProfileFragment;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.ProfileDataApi;
import com.m8.m8.ServiceGenerator;
import com.hbb20.CountryCodePicker;
import com.m8.m8.util.SharedToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Business2Fragment extends Fragment {

    Toolbar toolbar;
    TextView textView;
    TextView countryText;
    ImageView drawer;
    Button btnNext;
    View view;
    Context context;
    CountryCodePicker ccp;
    String Lcountry;
    String bCountry, bName, bContact, bEmail, bPhone, bAddress1, bAddress2, bCity, bPostcode, bType, Lname, Lemail,
            Lphone, Laddress1, Laddress2, Lcity, Lpostcode;
    EditText edtLname, edtLemail, edtLphone, edtLaddress1, edtLaddress2, edtLcity, edtLpostcode;

    String name, email, mobile, address;
    SharedPreferences sharedPreferences;


    public Business2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business2, container, false);

        init();

        //toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                //getActivity().finishAffinity();
                Intent intent = new Intent(context,HomeActivity.class);
                startActivity(intent);
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

        textView.setText("My Legal Representative");

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryText.setText(ccp.getSelectedCountryName());
            }
        });


        //get the Countrydata
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtLname.getText().toString())) {
                    edtLname.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLemail.getText().toString())) {
                    edtLemail.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLphone.getText().toString())) {
                    edtLphone.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLaddress1.getText().toString())) {
                    edtLaddress1.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLcity.getText().toString())) {
                    edtLcity.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLpostcode.getText().toString())) {
                    edtLpostcode.setError("Invalid data");
                }else if (!isValidEmail(edtLemail.getText().toString())){
                    edtLemail.setError("Invalid data");
                }else {
                    //Lcountry = ccp.getSelectedCountryName();
                    Lcountry = countryText.getText().toString();
                    setData();

                }

//                else if (TextUtils.isEmpty(edtLaddress2.getText().toString())) {
//                    edtLaddress2.setError("Invalid data");
//                }
            }
        });

        //set the text into edit text
        seEdittData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//
//                    //your code
//                    //getActivity().finishAffinity();
//                    Intent intent = new Intent(context,HomeActivity.class);
//                    startActivity(intent);
//
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        countryText = (TextView) view.findViewById(R.id.countryText);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        btnNext = (Button) view.findViewById(R.id.button_next);
        edtLname = (EditText) view.findViewById(R.id.legal_name);
        edtLemail = (EditText) view.findViewById(R.id.legal_email);
        edtLphone = (EditText) view.findViewById(R.id.legal_phone);
        edtLaddress1 = (EditText) view.findViewById(R.id.legal_address1);
        edtLaddress2 = (EditText) view.findViewById(R.id.legal_address2);
        edtLcity = (EditText) view.findViewById(R.id.legal_city);
        edtLpostcode = (EditText) view.findViewById(R.id.legal_postcode);
        ccp = (CountryCodePicker) view.findViewById(R.id.legal_country);


        sharedPreferences = getActivity().getSharedPreferences("Business", Context.MODE_PRIVATE);

    }

    //set data into shared memory
    private void setData() {
        Lname = edtLname.getText().toString();
        Lemail = edtLemail.getText().toString();
        Lphone = edtLphone.getText().toString();
        Laddress1 = edtLaddress1.getText().toString();
        Laddress2 = edtLaddress2.getText().toString();
        Lcity = edtLcity.getText().toString();
        Lpostcode = edtLpostcode.getText().toString();


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
        getShareData();
    }


    //Add data into apis sharer user
    private void getShareData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ProfileDataApi> call = apiInterface.getShareProfileApi(HomeActivity.userId, bType, bName, bContact, bEmail, bPhone, bAddress1,
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

                    if (HomeActivity.abcd.length()>0)
                    {
                        DetailsFragment detailsFragment = new DetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("ItemId", HomeActivity.abcd);
                        detailsFragment.setArguments(bundle);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.framelayout, detailsFragment);
                        transaction.commit();
                        HomeActivity.abcd = "";
                    }
                    else {

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.framelayout, new ViewProfileFragment());
                        //transaction.replace(R.id.framelayout, new HomeFragment());
                        transaction.commit();
                    }
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


    private void seEdittData() {
        if (ViewProfileFragment.data!=null) {
            if (ViewProfileFragment.data.getProfile() != null) {
                if (ViewProfileFragment.data.getProfile().getLeagalName() != null)
                    edtLname.setText(ViewProfileFragment.data.getProfile().getLeagalName());
                if (ViewProfileFragment.data.getProfile().getLeagalEmail() != null)
                    edtLemail.setText(ViewProfileFragment.data.getProfile().getLeagalEmail());
                if (ViewProfileFragment.data.getProfile().getLeagalPhone() != null)
                    edtLphone.setText(ViewProfileFragment.data.getProfile().getLeagalPhone());
                if (ViewProfileFragment.data.getProfile().getLeagalAddress() != null) {
                    edtLaddress1.setText(ViewProfileFragment.data.getProfile().getLeagalAddress().toString());
                }

                if (ViewProfileFragment.data.getProfile().getLeagalAddress2() != null) {
                    edtLaddress2.setText(ViewProfileFragment.data.getProfile().getLeagalAddress2().toString());
                }
                if (ViewProfileFragment.data.getProfile().getLeagalTown() != null)
                    edtLcity.setText(ViewProfileFragment.data.getProfile().getLeagalTown());
                if (ViewProfileFragment.data.getProfile().getLeagalPostalcode() != null)
                    edtLpostcode.setText(ViewProfileFragment.data.getProfile().getLeagalPostalcode());
                if (ViewProfileFragment.data.getProfile().getLeagalCountry() != null)
                    countryText.setText(ViewProfileFragment.data.getProfile().getLeagalCountry().toString());
            }
        }
        else {
            GetMetaDetails();
        }
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
                    if (ViewProfileFragment.data.getProfile().getLeagalName()!=null)
                        edtLname.setText(ViewProfileFragment.data.getProfile().getLeagalName());
                    if (ViewProfileFragment.data.getProfile().getLeagalEmail()!=null)
                        edtLemail.setText(ViewProfileFragment.data.getProfile().getLeagalEmail());
                    if (ViewProfileFragment.data.getProfile().getLeagalPhone()!=null)
                        edtLphone.setText(ViewProfileFragment.data.getProfile().getLeagalPhone());
                    if (ViewProfileFragment.data.getProfile().getLeagalAddress()!=null) {
                        edtLaddress1.setText(ViewProfileFragment.data.getProfile().getLeagalAddress().toString());
                    }

                    if (ViewProfileFragment.data.getProfile().getLeagalAddress2()!=null) {
                        edtLaddress2.setText(ViewProfileFragment.data.getProfile().getLeagalAddress2().toString());
                    }
                    if (ViewProfileFragment.data.getProfile().getLeagalTown()!=null)
                        edtLcity.setText(ViewProfileFragment.data.getProfile().getLeagalTown());
                    if (ViewProfileFragment.data.getProfile().getLeagalPostalcode()!=null)
                        edtLpostcode.setText(ViewProfileFragment.data.getProfile().getLeagalPostalcode());
                    if (ViewProfileFragment.data.getProfile().getLeagalCountry()!=null)
                        countryText.setText(ViewProfileFragment.data.getProfile().getLeagalCountry().toString());

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


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
