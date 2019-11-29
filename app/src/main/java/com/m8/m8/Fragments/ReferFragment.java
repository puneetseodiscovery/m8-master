package com.m8.m8.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.NoInternetActivity;
import com.m8.m8.Activities.StartActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.ReferCodeApi;
import com.m8.m8.RetrofitModel.ReferLinkApi;
import com.m8.m8.ServiceGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferFragment extends Fragment {

    View view;
    Context context;
    Spinner spinner;
    EditText businessName, businessOwnerName, businessEmail, businessPhone, Name, Email, Phone;
    Button btnRefer;
    Toolbar toolbar;
    TextView toolbarText;
    ImageView drawer;
    String category;
    String referCode;
    String abc;
    String Bname, Bemail, BownerName = "", Bphone, name, email, phone;
    ProgressDialog progressDialog;


    public ReferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_refer, container, false);


        //Find all id
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

        toolbarText.setText("Refer & Earn");

        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertext, StartActivity.arrayCate);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getSelectedItem().toString();
                abc = StartActivity.arrayList.get(position).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(businessName.getText().toString())) {
                    businessName.setError("Inavlid Field");
                    businessName.requestFocus();
                } else if (TextUtils.isEmpty(businessEmail.getText().toString())) {
                    businessEmail.setError("Invalid Field");
                    businessEmail.requestFocus();
                } else if (TextUtils.isEmpty(businessPhone.getText().toString())) {
                    businessPhone.setError("Invalid Field");
                    businessPhone.requestFocus();
                } else if (TextUtils.isEmpty(Name.getText().toString())) {
                    Name.setError("Invalid Field");
                    Name.requestFocus();
                } else if (TextUtils.isEmpty(Phone.getText().toString())) {
                    Phone.setError("Invalid Field");
                    Phone.requestFocus();
                } else if (TextUtils.isEmpty(Email.getText().toString())) {
                    Email.setError("Invalid Field");
                    Email.requestFocus();
                } else {
                    progressDialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
                    progressDialog.show();

                    if (CheckInternet.isInternetAvailable(context)) {
                        //get user details
                        getReferCode();
                    } else {
                        startActivity(new Intent(context, NoInternetActivity.class));
                    }
                }
            }
        });

        if (CheckInternet.isInternetAvailable(context)) {
            //get user details
            GetMetaDetails();
        } else {
            startActivity(new Intent(context, NoInternetActivity.class));
        }
        return view;
    }

    //create link for refer
    private void createLink(String referCode) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.amit.com/" + referCode))
                .setDomainUriPrefix("amrm8.page.link")
                // Open links with this app on Android  amitpandey12.page.link
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("amrm8.page.link").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.d("hello123", "1" + dynamicLink.getUri());


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://" + dynamicLink.getUri().toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            sendReferCode(shortLink);

                        } else {

                        }
                    }
                });

    }

    private void init() {
        spinner = (Spinner) view.findViewById(R.id.spineer);
        businessName = (EditText) view.findViewById(R.id.B_name);
        businessEmail = (EditText) view.findViewById(R.id.B_email);
        businessPhone = (EditText) view.findViewById(R.id.B_phone);
        businessOwnerName = (EditText) view.findViewById(R.id.B_owwername);
        Name = (EditText) view.findViewById(R.id.name);
        Email = (EditText) view.findViewById(R.id.email);
        Phone = (EditText) view.findViewById(R.id.phone);
        btnRefer = (Button) view.findViewById(R.id.btn_refer);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        toolbarText = (TextView) view.findViewById(R.id.toolbarText);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);

    }


    //get refer code
    private void getReferCode() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ReferCodeApi> call = apiInterface.getReferApi(HomeActivity.userId);

        call.enqueue(new Callback<ReferCodeApi>() {
            @Override
            public void onResponse(Call<ReferCodeApi> call, Response<ReferCodeApi> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        referCode = response.body().getData().toString();
                        createLink(referCode);
                        Log.d("amama", referCode);
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ReferCodeApi> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }


    //send refer code into mail
    private void sendReferCode(Uri shortLink) {
        Bname = businessName.getText().toString();
        BownerName = businessOwnerName.getText().toString();
        Bemail = businessEmail.getText().toString();
        Bphone = businessPhone.getText().toString();
        name = Name.getText().toString();
        email = Email.getText().toString();
        phone = Phone.getText().toString();

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ReferLinkApi> call = apiInterface.sendReferlcode(HomeActivity.userId, abc, Bname, BownerName, Bemail, Bphone, name, email, phone, shortLink.toString());
        call.enqueue(new Callback<ReferLinkApi>() {
            @Override
            public void onResponse(Call<ReferLinkApi> call, Response<ReferLinkApi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals(200)) {
                        for (int i=0; i < 3; i++)
                        {
                            Toast.makeText(context, "Thank you for referring a business to M8. If they buy a M8 package we will send you half of what they pay. Good luck, the more businesses you refer to us the more you will earn.", Toast.LENGTH_LONG).show();
                        }
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.framelayout, new HomeFragment());
                        transaction.commit();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReferLinkApi> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //auto get user details
    public void GetMetaDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetMetaData> call = apiInterface.getMetaData(HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetMetaData>() {
            @Override
            public void onResponse(Call<GetMetaData> call, Response<GetMetaData> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Name.setText(response.body().getData().getName());
                        Phone.setText(response.body().getData().getContactNo());
                        Email.setText(response.body().getData().getEmail());
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
